/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/classyVmMethods.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/task.h"

sjme_errorCode sjme_nvm_vmMethod_idByInterface(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrOutNotNull sjme_jmethodID* outID,
	sjme_attrInNotNull sjme_jobject forObject,
	sjme_attrInNotNull sjme_nvm_class_poolEntryMember* forMember)
{
	sjme_errorCode error;
	sjme_jclass objectClass, interfaceClass, check;
	sjme_list(sjme_phantom(sjme_jclass))* interfaceIsClasses;
	sjme_jint wantHash, i, n;
	sjme_jmethodID interfaceMethod, selfFound;
	
	if (contextThread == NULL || outID == NULL || forObject == NULL ||
		forMember == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be a reference to an interface. */
	if (forMember->type != SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD)
		return sjme_error_vmError(contextThread, SJME_ERROR_CLASS_CHANGED);

	/* Everything acts in relation to the object's class. */
	objectClass = SJME_O_C(forObject);

	/* Lookup the target interface. */
	interfaceClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_T_CL(contextThread), &interfaceClass, contextThread,
		sjme_atomic_gP(sjme_nvm_class_poolEntryClass, 1,
			&forMember->inClass)->descriptor->seq, SJME_JNI_TRUE)))
		return sjme_error_vmError(contextThread,
			sjme_error_defaultOr(error, SJME_ERROR_CLASS_CHANGED));

	/* The object's class must be assignable to the interface class. */
	if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
		contextThread, interfaceClass, objectClass)))
		return sjme_error_vmError(contextThread,
			sjme_error_defaultOr(error, SJME_ERROR_CLASS_CAST));
	
	/* We need to find the target method in all the classes that the */
	/* target interface is first, this is to be precise so that we actually */
	/* call a properly defined interface reference and not whatever. */
	interfaceIsClasses = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_isClasses(
		contextThread, interfaceClass, &interfaceIsClasses)) ||
		interfaceIsClasses == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Look through interfaces for the target method. */
	interfaceMethod = NULL;
	for (i = 0, n = interfaceIsClasses->length; i < n; i++)
	{
		/* Skip any blank slots. */
		check = sjme_atomic_g(sjme_jclass, &interfaceIsClasses->elements[i]);
		if (check == NULL)
			continue;

		/* Lookup method. */
		if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
			check, contextThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_FALSE,
			sjme_atomic_gP(sjme_nvm_class_poolEntryNameAndType, 1,
				&forMember->nameAndType)->name->seq,
			sjme_atomic_gP(sjme_nvm_class_poolEntryNameAndType, 1,
				&forMember->nameAndType)->descriptor->seq,
			&interfaceMethod)))
		{
			/* This is considered valid. */
			if (error == SJME_ERROR_NO_METHOD)
				continue;
			
			return sjme_error_vmError(contextThread, error);
		}

		/* Target method was found somewhere. */
		if (interfaceMethod != NULL)
			break;
	}

	/* The interface does not have this method? The class changed or */
	/* otherwise. */
	if (interfaceMethod == NULL)
		return sjme_error_vmError(contextThread, SJME_ERROR_CLASS_CHANGED);

	/* Since we know the method exists, now we can look for this method */
	/* in the current class using more normal means. */
	selfFound = NULL;
	if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
		objectClass, contextThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_FALSE,
		sjme_atomic_gP(sjme_nvm_class_poolEntryNameAndType, 1,
			&forMember->nameAndType)->name->seq,
		sjme_atomic_gP(sjme_nvm_class_poolEntryNameAndType, 1,
			&forMember->nameAndType)->descriptor->seq,
		&selfFound)))
	{
		/* No method is considered valid enough. */
		if (error == SJME_ERROR_NO_METHOD)
			goto skip_noMethod;
			
		return sjme_error_vmError(contextThread, error);
	}

	/* Properly found method? */
	if (selfFound != NULL && SJME_NVM_ACC_IS(selfFound->flags, PUBLIC) &&
		!SJME_NVM_ACC_IS(selfFound->flags, ABSTRACT))
	{
		*outID = selfFound;
		return SJME_ERROR_NONE;
	}
	
	/* Not found. */
skip_noMethod:
	if (!required)
		return SJME_ERROR_NO_METHOD;
	return sjme_error_vmError(contextThread, SJME_ERROR_NO_METHOD);
}

sjme_errorCode sjme_nvm_vmMethod_idByNameType(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_charSeq inName,
	sjme_attrInPositive sjme_charSeq inType,
	sjme_attrOutNotNull sjme_jmethodID* outID)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_list(sjme_jmethodID)* methods;
	sjme_jmethodID method;
	sjme_jint wantHash;
	
	if (inClass == NULL || contextThread == NULL || inName == NULL ||
		inType == NULL || outID == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Needs to be initialized first. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
		inClass, contextThread)))
		return sjme_error_default(error);

	/* Calculate the hash to lookup. */
	wantHash = sjme_nvm_class_idHashMember(inName, inType);
		
	/* Look through all methods. */
	methods = inClass->methods[instanceType].binds;
	for (i = methods->length - 1; i >= 0; i--)
	{
		/* There must be a valid method here. */
		method = methods->elements[i];
		if (method == NULL)
			continue;
			
		/* Check against the hash, which is faster. */
		if (method->member.idHash != wantHash)
			continue;
		
		/* Is this the method. */
		if (sjme_charSeq_equalsR(SJME_M_N(method)->seq, inName) &&
			sjme_charSeq_equalsR(SJME_M_T(method)->seq, inType))
		{
			/* Do not grab a static initializer for another class. */
			if (SJME_NVM_CLASS_INIT_IS(method->bits, STATIC) &&
				sjme_atomic_g(sjme_jclass, &method->member.inClass) != inClass)
				continue;
			
			*outID = method;
			return SJME_ERROR_NONE;
		}
	}

	/* Not found. */
	if (!required)
		return SJME_ERROR_NO_METHOD;
	return sjme_error_vmError(contextThread, SJME_ERROR_NO_METHOD);
}

sjme_errorCode sjme_nvm_vmMethod_idByNameTypeU(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_lpcstr inName,
	sjme_attrInPositive sjme_lpcstr inType,
	sjme_attrOutNotNull sjme_jmethodID* outID)
{
	sjme_errorCode error;
	sjme_charSeqStatic wrapName, wrapType;
	
	if (inName == NULL || inType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Wrap sequences. */
	memset(&wrapName, 0, sizeof(wrapName));
	memset(&wrapType, 0, sizeof(wrapType));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&wrapName,
		inName, 0, -1)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&wrapType,
		inType, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_nvm_vmMethod_idByNameType(inClass, contextThread,
		instanceType, required, &wrapName, &wrapType, outID);
}

sjme_errorCode sjme_nvm_vmMethod_sourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_jint methodId,
	sjme_attrOutNotNull sjme_nvm_class_methodInfo* outInfo)
{
	sjme_list(sjme_nvm_class_methodInfo)* methods;
	sjme_jint i, n, base;
	sjme_jclass atClass;
	sjme_jboolean wantStatic;
	sjme_nvm_class_methodInfo method;
	
	if (inClass == NULL || outInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (methodId < 0 || methodId >= inClass->methods[instanceType].count)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Do we want static? */
	wantStatic = (instanceType == SJME_NVM_CLASS_MEMBER_STATIC);
	
	/* Start at the current class for the search. */
	atClass = inClass;
	
	/* If we are below the class index, drop to the super class. */
	while (methodId < atClass->methods[instanceType].base)
	{
		atClass = SJME_C_SU(atClass);
		
		/* This should not occur, but it might. */
		if (atClass == NULL)
			return sjme_error_vmError(NULL,
				SJME_ERROR_SUPER_CLASS_INVALID);
	}

	/* Find the associated method. */
	base = atClass->methods[instanceType].base;
	methods = atClass->info->methods;
	for (i = methods->length - 1; i >= 0; i--)
	{
		/* Get the method here. */
		method = methods->elements[i];
		if (method == NULL)
			return sjme_error_vmError(NULL, SJME_ERROR_NO_METHOD);
		
		/* If the static flag and the index matches, this is the one! */
		if (SJME_NVM_ACC_IS(method->flags, STATIC) == wantStatic &&
			method->typedIndex == (methodId - base))
		{
			*outInfo = method;
			return SJME_ERROR_NONE;
		}
	}
	
	/* If this point is reached, the index is not valid. */
	if (!required)
		return SJME_ERROR_NO_METHOD;
	return sjme_error_vmError(NULL, SJME_ERROR_NO_METHOD);
}

