/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instance.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/task.h"

sjme_errorCode sjme_nvm_fieldValueSet(
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInNotNull sjme_nvm_fieldValues* into,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jvalue* value)
{
	if (into == NULL || value == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (into->type != javaType)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (atIndex < 0 || atIndex >= into->count)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (javaType == SJME_JAVA_TYPE_ID_INTEGER)
		into->values.i[atIndex] = value->i;
	else if (javaType == SJME_JAVA_TYPE_ID_LONG)
		into->values.j[atIndex] = value->j;
	else if (javaType == SJME_JAVA_TYPE_ID_FLOAT)
		into->values.f[atIndex] = value->f;
	else if (javaType == SJME_JAVA_TYPE_ID_DOUBLE)
		into->values.d[atIndex] = value->d;
	else
		into->values.l[atIndex] = value->l;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_jint sjme_nvm_fieldValueSize(
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInPositiveNonZero sjme_jint n)
{
	sjme_jint baseSize;

	if (javaType < 0 || javaType >= SJME_NUM_JAVA_TYPE_IDS ||
		n <= 0)
		return -1;

	if (javaType == SJME_JAVA_TYPE_ID_OBJECT)
		baseSize = (SJME_CONFIG_HAS_POINTER >> 3);
	else if (javaType == SJME_JAVA_TYPE_ID_INTEGER ||
		javaType == SJME_JAVA_TYPE_ID_FLOAT)
		baseSize = 4;
	else
		baseSize = 8;
	
	/* Base size is the offset of where values start */
	return (baseSize * n) +
		offsetof(sjme_nvm_fieldValues, values) +
		offsetof(sjme_nvm_rawFieldValues, l);
}

sjme_errorCode sjme_nvm_instance_checkPermission(
	sjme_attrInNotNull sjme_jclass fromClass,
	sjme_attrInNotNull sjme_jmemberID toMember,
	sjme_attrOutNotNull sjme_jboolean* accessOkay)
{
	if (fromClass == NULL || toMember == NULL || accessOkay == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Members in the same class are always acceptable. */
	if (toMember->inClass == fromClass)
		goto skip_okay;

skip_okay:
	*accessOkay = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_countDown(
	sjme_attrInNotNull sjme_jobject* oldP,
	sjme_attrInNotNull sjme_jobject newV)
{
	sjme_errorCode error;
	sjme_jobject oldObject;
	sjme_jboolean validObject, noSelfGc;
	sjme_alloc_weak weak;

	if (oldP == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If the old is the same as new, then do not count if the count would */
	/* result in the object being GCed before it was set. */
	oldObject = *oldP;
	noSelfGc = SJME_JNI_FALSE;
	if (oldObject != NULL && newV != NULL && oldObject == newV)
	{
		/* Only consider valid weak reference. */
		weak = NULL;
		if (sjme_error_is(error = sjme_alloc_weakRefGet(oldObject, &weak)))
		{
			if (error != SJME_ERROR_NOT_WEAK_REFERENCE)
				return sjme_error_default(error);
		}

		/* Do not self GC if it would end up freeing the object before it */
		/* could be set. */
		noSelfGc = (sjme_atomic_sjme_jint_get(&weak->count) <= 1);
	}

	/* Count down if the old object exists, or in the case as above. */
	if (oldObject != NULL && !noSelfGc)
	{
		/* Is this object actually valid? */
		validObject = SJME_JNI_FALSE;
		if (sjme_error_is(error = sjme_nvm_isA(oldObject,
			SJME_NVM_STRUCT_OBJECT_INSTANCE,
			&validObject)))
			return sjme_error_default(error);

		/* Count it down. */
		if (sjme_error_is(error = sjme_alloc_weakUnRef(oldObject)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectArrayNew(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositive sjme_jint arrayLength)
{
	if (contextThread == NULL || outObject == NULL || componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (arrayLength < 0)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_NEGATIVE_ARRAY_SIZE);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_objectArrayNewT(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId componentType,
	sjme_attrInPositive sjme_jint arrayLength)
{
	if (contextThread == NULL || outObject == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (componentType < 0 || componentType >= SJME_NUM_JAVA_TYPE_IDS ||
		componentType == SJME_JAVA_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (arrayLength < 0)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_NEGATIVE_ARRAY_SIZE);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_objectNew(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass inClass)
{
	sjme_errorCode error;
	sjme_jobject result;
	
	if (contextThread == NULL || outObject == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Make sure the class is initialized. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(inClass,
		contextThread)))
		return sjme_error_vmError(contextThread, error);
	
	/* Setup string object. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(contextThread->state,
		allocSize, inType,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		return sjme_error_vmError(contextThread, error);
	
	/* Set some basic details. */
	result->isClass = inClass;
	result->identityHash = (sjme_jint)result;
	
	/* Success! */
	*outObject = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_objectNewN(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_charSeq inClass)
{
	sjme_errorCode error;
	sjme_jclass classy;
	
	if (contextThread == NULL || outObject == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Lookup the class first. */
	classy = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		contextThread->inTask->classLoader, &classy, contextThread,
		inClass, SJME_JNI_FALSE)) || classy == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Forward call. */
	return sjme_nvm_instance_objectNew(contextThread, allocSize, inType,
		outObject, classy);
}

sjme_errorCode sjme_nvm_instance_objectNewNU(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_lpcstr inClass)
{
	sjme_errorCode error;
	sjme_charSeqStatic tempSeq;

	if (contextThread == NULL || outObject == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup wrapped sequence. */
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&tempSeq,
		inClass, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_nvm_instance_objectNewN(contextThread, allocSize, inType,
		outObject, &tempSeq);
}
