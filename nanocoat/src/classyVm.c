/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <string.h>

#include "sjme/nvm/classyVm.h"
#include "sjme/listUtil.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/task.h"
#include "sjme/util.h"

/** The amount the class list grows by. */
#define SJME_VM_CLASS_GROW_LEN 32

static sjme_errorCode sjme_nvm_vmClass_checkInitMethodBind(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_jclass thisClass,
	sjme_attrInNotNull sjme_jclass superClass,
	sjme_attrInValue sjme_nvm_class_instanceType instanceType,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNotNull sjme_nvm_class_methodInfo thisInfo,
	sjme_attrOutNotNull sjme_jmethodID* outBind)
{
	sjme_errorCode error;
	sjme_jmethodID result;
	sjme_nvm_class_methodInfo found;
	sjme_nvm_class_methodInfo lastScan, thisScan;
	sjme_jint i, n;
	sjme_jboolean wantStatic;

	if (inState == NULL || thisClass == NULL || thisInfo == NULL ||
		outBind == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (superClass != sjme_atomic_sjme_jclass_get(&thisClass->superClass))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (index < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_METHOD_ID,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;

	/* Always in the current class. */
	result->member.inClass = thisClass;

	/* The identifier hash is used for lookup. */
	result->member.idHash = thisInfo->idHash;
	
	/* The names always get set. */
	result->member.name = thisInfo->name;
	result->member.type = thisInfo->type;

	/* Also copy flags. */
	result->flags = thisInfo->flags;
	
	/* Constructors always bind to self. */
	/* Along with any private methods. */
	/* Static as well. */
	if (sjme_charSeq_equalsUtfR(thisInfo->name->seq, "<init>") ||
		sjme_charSeq_equalsUtfR(thisInfo->name->seq, "<clinit>") ||
		thisInfo->flags.member.access.private ||
		thisInfo->flags.member.isStatic)
	{
		/* Just to self always. */
		result->info[SJME_NVM_CALL_NON_VIRTUAL] = thisInfo;
		result->info[SJME_NVM_CALL_VIRTUAL] = thisInfo;
		result->info[SJME_NVM_CALL_SUPER] = NULL;
		
		/* This is now successful. */
		goto skip_success;
	}
	
	/* Is static desired? */
	wantStatic = (instanceType == SJME_NVM_CLASS_MEMBER_STATIC);

	/* Go through the entire class chain to find a non-private signature. */
	/* This also determines the super-method call for the given spot. */
	/* Start off with nothing set at all. */
	lastScan = NULL;
	thisScan = 0;
	for (i = 0, n = thisClass->methodCount[instanceType]; i < n; i++)
	{
		/* Lookup this index. */
		found = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_methodSourceByIndex(
			thisClass, instanceType, SJME_JNI_TRUE, i,
			&found)) || found == NULL)
			goto fail_badFind;
		
		/* If not the same method or type, skip. */
		if (!sjme_charSeq_equalsR(
				found->name->seq, thisInfo->name->seq) ||
			!sjme_charSeq_equalsR(
				found->type->seq, thisInfo->type->seq))
			continue;
		
		/* Private methods just go poof. */
		if (found->flags.member.access.private)
			continue;
			
		/* Package private methods in different packages go poof. */
		if (!found->flags.member.access.private &&
			!found->flags.member.access.protected &&
			!found->flags.member.access.public)
		{
			/* Not in same package, skip. */
			if (!sjme_charSeq_equalsR(
					found->inClass->inPackage->seq,
					thisInfo->inClass->inPackage->seq))
				continue;
		}
		
		/* Ignore static difference. */
		if (thisInfo->flags.member.isStatic != wantStatic)
			continue;
		
		/* If the current scan is final, then oops! */
		if (thisScan != NULL && thisScan->flags.member.final)
			goto fail_changed;
		
		/* Shift up and set. */
		lastScan = thisScan;
		thisScan = found;
	}
	
	/* Non-virtual is always to self. */
	/* Otherwise virtual and super-virtual is the last scan. */
	result->info[SJME_NVM_CALL_NON_VIRTUAL] = thisInfo;
	result->info[SJME_NVM_CALL_VIRTUAL] = thisScan;
	result->info[SJME_NVM_CALL_SUPER] = lastScan;
	
	/* Success! */
skip_success:
	*outBind = result;
	return SJME_ERROR_NONE;
	
	/* Class changed in an incompatible way? */
fail_changed:
	error = SJME_ERROR_CLASS_CHANGED;
	
fail_noMethod:
fail_badFind:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));

	return sjme_error_default(error);
}

static sjme_errorCode sjme_nvm_vmClass_checkInitMethodBinds(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInValue sjme_nvm_class_instanceType instanceType,
	sjme_attrOutNotNull sjme_list_sjme_jmethodID** outList)
{
	sjme_errorCode error;
	sjme_jclass superClass;
	sjme_nvm_class_methodInfo methodInfo;
	sjme_jint i, n;
	sjme_list_sjme_jmethodID* result;
	sjme_jmethodID bind;
	
	if (inLoader == NULL || inClass == NULL || outList == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* The super class can be used as a basis for linkage. */
	superClass = sjme_atomic_sjme_jclass_get(&inClass->superClass);
	
	/* Allocate result. */
	result = NULL;
	n = inClass->methodCount[instanceType];
	if (sjme_error_is(error = sjme_list_alloc(inLoader->inState->allocPool,
		n, &result, sjme_jmethodID, 0)) || result == NULL)
		goto fail_allocResult;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("Binding %d/%d methods...",
		inClass->methodCount[0], inClass->methodCount[1]);
#endif
	
	/* Bind individual methods. */
	for (i = 0; i < n; i++)
	{
		/* Which method is being bound? */
		methodInfo = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_methodSourceByIndex(
			inClass, instanceType,
			SJME_JNI_TRUE, i, &methodInfo)) ||
			methodInfo == NULL)
			goto fail_noIndex;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("Binding %s %s%s...",
			inClass->binaryName,
			sjme_charSeq_tempUtf(methodInfo->name->seq),
			sjme_charSeq_tempUtf(methodInfo->type->seq));
#endif
		
		/* Perform the binding. */
		bind = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInitMethodBind(
			inLoader->inState, inClass, superClass,
			instanceType, i, methodInfo,
			&bind)) || bind == NULL)
			goto fail_initBind;

		/* Stopped being consistent? */
		if (result->length != n)
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* Store bind. */
		result->elements[i] = bind;
			
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("Bound `%s` %s %s%s -> %s %s%s",
			inClass->binaryName,
			&methodInfo->inClass->name->chars[0],
			&methodInfo->name->chars[0],
			&methodInfo->type->chars[0],
			&bind->info[1]->inClass->name->chars[0],
			&bind->info[1]->name->chars[0],
			&bind->info[1]->type->chars[0]);
#endif
	}
	
	/* Success! */
	*outList = result;
	return SJME_ERROR_NONE;
	
fail_initBind:
fail_noIndex:
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
	return sjme_error_vmError(NULL, error);
}

static sjme_errorCode sjme_nvm_vmClass_checkInitStaticFields(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInValue sjme_javaTypeId typeId)
{
	sjme_errorCode error;
	sjme_nvm_fieldValues* fieldValues;
	sjme_nvm_class_info info;
	sjme_jint i, n;
	sjme_nvm_class_fieldInfo fieldInfo;
	
	if (allocPool == NULL || inClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* No static fields for this kind, ignore. */
	info = inClass->info;
	n = info->fieldCount[SJME_NVM_CLASS_MEMBER_STATIC][typeId];
	if (n == 0)
		return SJME_ERROR_NONE;
	
	/* Allocate field values. */
	fieldValues = NULL;
	if (sjme_error_is(error = sjme_alloc(allocPool,
		sjme_nvm_fieldValueSize(typeId, n),
		(sjme_pointer)&fieldValues)) || fieldValues == NULL)
		return sjme_error_default(error);
	
	/* Store type and count for this tread. */
	fieldValues->type = typeId;
	fieldValues->count = n;
	
	/* Setup individual static fields with non-object constants. */
	if (typeId != SJME_JAVA_TYPE_ID_OBJECT)
		for (i = 0; i < n; i++)
		{
			/* Locate field information. */
			fieldInfo = NULL;
			if (sjme_error_is(error = sjme_nvm_vmClass_fieldSourceByIndex(
				inClass, SJME_NVM_CLASS_MEMBER_STATIC,
				typeId, i,
				&fieldInfo)) || fieldInfo == NULL)
				return sjme_error_vmError(NULL,
					sjme_error_defaultOr(error, SJME_ERROR_CLASS_CHANGED));
			
			/* Is there a static value to copy? */
			if (fieldInfo->constVal.type == typeId)
			{
				if (typeId == SJME_JAVA_TYPE_ID_LONG)
					fieldValues->values.j[i] =
						fieldInfo->constVal.value.java.j;
				else if (typeId == SJME_JAVA_TYPE_ID_FLOAT)
					fieldValues->values.f[i] =
						fieldInfo->constVal.value.java.f;
				else if (typeId == SJME_JAVA_TYPE_ID_DOUBLE)
					fieldValues->values.d[i] =
						fieldInfo->constVal.value.java.d;
				else
					fieldValues->values.i[i] =
						fieldInfo->constVal.value.java.i;
			}
		}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_vmClass_checkInitSuper(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_jclass inSuperClass)
{
	sjme_nvm_class_info superInfo;
	sjme_nvm_class_instanceType index;
	sjme_jint i;
	
	if (inClass == NULL || inSuperClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Superclass info is required. */
	superInfo = inSuperClass->info;
	if (superInfo == NULL)
		return sjme_error_vmError(NULL, SJME_ERROR_SUPER_CLASS_INVALID);
	
	/* For both static and instance fields. */
	for (index = 0; index < SJME_NVM_CLASS_NUM_INSTANCE_TYPE; index++)
	{
		/* The field bases vary per type, however this is derived from */
		/* the parent class for storage sizes. */
		for (i = 0; i < SJME_NUM_JAVA_TYPE_IDS; i++)
		{
			/* This is just a copy of the field count. */
			inClass->fieldBase[index][i] = inSuperClass->fieldCount[index][i];
			
			/* However, we add the info to get our current field count. */
			inClass->fieldCount[index][i] = inClass->fieldBase[index][i] +
				inClass->info->fieldCount[index][i];
			
			/* Overflowed? */
			if (inClass->fieldBase[index][i] < 0 ||
				inClass->fieldCount[index][i] < 0)
				return sjme_error_vmError(NULL,
					SJME_ERROR_CLASS_TOO_MANY_MEMBERS);
		}
		
		/* Methods are simpler as they are based on static/instance and not */
		/* from any of their type information. */
		inClass->methodBase[index] = inSuperClass->methodCount[index];
		inClass->methodCount[index] = inClass->methodBase[index] +
			inClass->info->methodCount[index];
		
		/* Overflowed? */
		if (inClass->methodBase[index] < 0 ||
			inClass->methodCount[index] < 0)
			return sjme_error_vmError(NULL,
				SJME_ERROR_CLASS_TOO_MANY_MEMBERS);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_vmClass_checkInitArray(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_nvm_vmClass_loader classLoader)
{
	sjme_cchar componentTypeName[SJME_NVM_CLASS_NAME_LIMIT];
	sjme_errorCode error;
	sjme_nvm_class_info info;
	sjme_alloc_pool allocPool;
	sjme_nvm_stringPool strings;
	sjme_nvm_stringPool_string thisName, superName;
	sjme_jclass componentType;
	sjme_nvm inState;
	
	if (inClass == NULL || contextThread == NULL || classLoader == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	inState = contextThread->inTask->inState;
	allocPool = contextThread->inTask->inState->allocPool;
	strings = classLoader->nullStrings;

	/* Lookup self name. */
	thisName = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_locateSeq(
		strings, &thisName, inClass->binaryName, 0)) || thisName == NULL)
		return sjme_error_vmError(contextThread, error);

	/* The super class is always Object. */
	superName = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_locateUtf(
		strings, &superName, "java/lang/Object", 0, -1)) || superName == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Allocate synthetic result. */
	info = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*info), SJME_NVM_STRUCT_CLASS_INFO,
		SJME_AS_NVM_COMMONP(&info))) || info == NULL)
		return sjme_error_outOfMemory(allocPool, sizeof(*info));
	
	/* Synthesize info for arrays. */
	info->version = SJME_NVM_CLASS_CLDC_1_8;
	info->name = thisName;
	info->superName = superName;
	info->flags.access.public = SJME_JNI_TRUE;
	info->flags.final = SJME_JNI_TRUE;
	info->flags.synthetic = SJME_JNI_TRUE;
	info->isArray = SJME_JNI_TRUE;

	/* Set synthetic class info. */
	inClass->info = info;

	/* Determine component type class name. */
	memset(componentTypeName, 0, sizeof(componentTypeName));
	snprintf(componentTypeName, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"%s", (sjme_lpcstr)SJME_POINTER_OFFSET(
			sjme_charSeq_tempUtf(inClass->binaryName), sizeof(sjme_cchar)));

	/* Locate component type of the array. */
	componentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFU(
		contextThread->inTask->classLoader,
		&componentType, contextThread, componentTypeName,
		SJME_JNI_FALSE)) || componentType == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Set component type, and tha phantom back link for quicker lookup. */
	sjme_atomic_sjme_jclass_compareSet(&inClass->componentType,
		NULL, componentType);
	sjme_atomic_sjme_jclass_compareSet(&componentType->phantomArrayType,
		NULL, inClass);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_vmClass_checkInitStandard(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_nvm_vmClass_loader classLoader)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_rom_library* classPath;
	sjme_nvm_class_info info;
	sjme_nvm_rom_library tryLib;
	sjme_jint i, n;
	sjme_cchar fileName[SJME_NVM_CLASS_NAME_LIMIT];
	
	if (inClass == NULL || contextThread == NULL || classLoader == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* And the class path. */	
	classPath = classLoader->classPath;
	if (classPath == NULL)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_INVALID_CLASS_LOADER);
	
	/* Determine the file name of the class. */
	memset(fileName, 0, sizeof(fileName));
	snprintf(fileName, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"%s", sjme_charSeq_tempUtf(inClass->binaryName));
	memmove(&fileName[0], &fileName[1],
		sizeof(*fileName) * (SJME_NVM_CLASS_NAME_LIMIT - 2));
	if (strlen(fileName) > 0)
		fileName[strlen(fileName) - 1] = '\0';
	strncat(fileName, ".class", SJME_NVM_CLASS_NAME_LIMIT - 1);
	
	/* Find the class within the classpath. */
	info = NULL;
	for (i = 0, n = classPath->length; i < n; i++)
	{
		/* Try this library. */
		tryLib = classPath->elements[i];
		
		/* Cache via the library handler itself. */
		if (sjme_error_is(error = sjme_nvm_rom_libraryCacheClass(
			tryLib, &info, fileName)) || info == NULL)
		{
			/* Not in this library, stop. */
			if (error == SJME_ERROR_RESOURCE_NOT_FOUND)
				continue;
			
			/* Fail. */
			return sjme_error_vmError(contextThread, SJME_ERROR_NO_CLASS);
		}
		
		/* Stop. */
		break;
	}
	
	/* No class exists. */
	if (info == NULL)
		return sjme_error_vmError(contextThread, SJME_ERROR_NO_CLASS);
	
	/* Set class info. */
	inClass->info = info;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_vmClass_loaderLoadCheck(
	sjme_attrInNotNull sjme_list_sjme_pointer* inList,
	sjme_attrInPositive sjme_jint checkIndex,
	sjme_attrInNotNull sjme_pointer checkP,
	sjme_attrInValue sjme_jint againstI,
	sjme_attrInValue sjme_pointer againstP)
{
	sjme_jclass maybe;
	
	maybe = checkP;
	if (inList == NULL || maybe == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Could it be this one? */
	if (againstI == maybe->binaryHash &&
		sjme_charSeq_equalsR(maybe->binaryName, againstP))
		return SJME_ERROR_NONE;
	
	/* Not matched. */
	return sjme_error_vmError(NULL, SJME_ERROR_NOT_MATCHED);
}

static sjme_errorCode sjme_nvm_vmClass_loaderLoadFSubAlloc(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrOutNotNull sjme_jclass* outSlot,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_charSeq binaryName)
{
	sjme_errorCode error;
	sjme_jclass result;
	sjme_charSeq dupName;
	sjme_jint autoLoad;
	sjme_alloc_pool allocPool;
	sjme_nvm_isClasses isClasses;
	sjme_jchar firstChar;
	
	if (inLoader == NULL || outClass == NULL || outSlot == NULL ||
		contextThread == NULL || binaryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot be blank. */
	if (binaryName->length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* We allocate within this pool. */
	allocPool = inLoader->inState->allocPool;

	/* Duplicate binary name. */
	dupName = NULL;
	if (sjme_error_is(error = sjme_charSeq_dup(allocPool, &dupName,
		binaryName)) || dupName == NULL)
		goto fail_dupName;
	
	/* Allocate resultant class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inLoader->inState,
		sizeof(*result), SJME_NVM_STRUCT_CLASS_INSTANCE,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Allocate class instance of check storage. */
	isClasses = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inLoader->inState,
		sizeof(*isClasses), SJME_NVM_STRUCT_IS_CLASSES,
		SJME_AS_NVM_COMMONP(&isClasses))) || isClasses == NULL)
		goto fail_allocIsClasses;
	
	/* Initialize structure. */
	isClasses->rwLock.read = &isClasses->common.lock;
	
	/* Is now being used, so count up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(result, NULL)))
		goto fail_countUp;
	
	/* Set class type ID. */
	switch (sjme_charSeq_charAtR(binaryName, 0))
	{
		case 'Z':
			result->typeId = SJME_JAVA_TYPE_ID_INTEGER;
			result->arrayTypeId = SJME_BASIC_TYPE_ID_BOOLEAN;
			break;
		
		case 'B':
			result->typeId = SJME_JAVA_TYPE_ID_INTEGER;
			result->arrayTypeId = SJME_BASIC_TYPE_ID_BYTE;
			break;
		
		case 'S':
			result->typeId = SJME_JAVA_TYPE_ID_INTEGER;
			result->arrayTypeId = SJME_BASIC_TYPE_ID_SHORT;
			break;
		
		case 'C':
			result->typeId = SJME_JAVA_TYPE_ID_INTEGER;
			result->arrayTypeId = SJME_BASIC_TYPE_ID_CHARACTER;
			break;
		
		case 'I':
			result->typeId = SJME_JAVA_TYPE_ID_INTEGER;
			result->arrayTypeId = SJME_JAVA_TYPE_ID_INTEGER;
			break;

		case 'J':
			result->typeId = SJME_JAVA_TYPE_ID_LONG;
			result->arrayTypeId = SJME_JAVA_TYPE_ID_LONG;
			break;

		case 'F':
			result->typeId = SJME_JAVA_TYPE_ID_FLOAT;
			result->arrayTypeId = SJME_JAVA_TYPE_ID_FLOAT;
			break;

		case 'D':
			result->typeId = SJME_JAVA_TYPE_ID_DOUBLE;
			result->arrayTypeId = SJME_JAVA_TYPE_ID_DOUBLE;
			break;
		
		case 'L':
		case '[':
			result->typeId = SJME_JAVA_TYPE_ID_OBJECT;
			result->arrayTypeId = SJME_JAVA_TYPE_ID_OBJECT;
			break;

		default:
			goto fail_badType;
	}

	/* Classes start as never loaded. */
	autoLoad = SJME_VM_CLASS_INIT_LOAD_NEVER;

	/* Pre-calculate hash. */
	if (sjme_error_is(error = sjme_charSeq_hash(dupName, &result->binaryHash)))
		goto fail_hash;
	
	/* Initialize base fields. */
	result->binaryName = dupName;
	sjme_atomic_sjme_jint_set(&result->error, SJME_ERROR_NONE);
	sjme_atomic_sjme_jint_set(&result->isLoaded, 0);
	sjme_atomic_sjme_jint_set(&result->isInitialized, autoLoad);
	
	/* Store into the output slot immediately for recursive loading. */
	*outSlot = result;
	
	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;

fail_hash:
fail_badType:
fail_countUp:
fail_allocIsClasses:
	if (result != NULL)
		sjme_alloc_free(isClasses);
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
		
fail_dupName:
	if (dupName)
		sjme_alloc_free(dupName);
	
	/* Make sure the slot is not valid. */
	*outSlot = NULL;
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_vmClass_checkInit(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread)
{
	sjme_errorCode error;
	sjme_nvm_class_info info;
	sjme_nvm_vmClass_loader loader;
	sjme_jint i, n;
	sjme_jclass superClass, interface, classType;
	sjme_list_sjme_jclass* interfaces;
	sjme_alloc_pool allocPool;
	sjme_list_sjme_jmethodID* binds;
	
	if (inClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Error state occurred? */
	error = sjme_atomic_sjme_jint_get(&inClass->error);
	if (sjme_error_is(error))
		return sjme_error_default(error);
	
	/* Need these in order to work at all. */
	allocPool = contextThread->state->allocPool;
	
	/* Needs loading first? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isLoaded) == SJME_VM_CLASS_INIT_LOAD_NEVER)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkLoad(inClass,
			contextThread)))
			goto fail_checkLoad;
	
	/* Set to be currently initializing. */
	if (!sjme_atomic_sjme_jint_compareSet(&inClass->isInitialized,
		SJME_VM_CLASS_INIT_LOAD_NEVER,
		SJME_VM_CLASS_INIT_LOAD_CURRENT))
	{
		/* Does not need to be initialized? */
		if (sjme_atomic_sjme_jint_get(
			&inClass->isInitialized) != SJME_VM_CLASS_INIT_LOAD_NEVER)
			return SJME_ERROR_NONE;
		
		/* Called twice? Should not normally happen. */
		goto skip_doubleCalled;
	}
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("Initializing class: %s",
		sjme_charSeq_tempUtf(inClass->binaryName));
#endif
	
	/* The class info should now be valid. */
	info = inClass->info;
	loader = contextThread->inTask->classLoader;
	if (info == NULL || loader == NULL)
	{
		error = sjme_error_vmError(contextThread,
			SJME_ERROR_INVALID_CLASS_LOADER);
		goto fail_badState;
	}
	
	/* This is always set to the @c Class type. */
	classType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFU(
		loader, &classType, contextThread,
		"Ljava/lang/Class;", SJME_JNI_FALSE)) ||
		classType == NULL)
		goto fail_findClassType;
	
	/* Set the instance type, as all classes are this type. */
	inClass->object.isClass = classType;
	
	/* The super class needs to be found first. */
	superClass = NULL;
	if (info->superName != NULL)
	{
		/* Find super class. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
			loader, &superClass, contextThread, info->superName->seq,
			SJME_JNI_FALSE)) ||
			superClass == NULL)
			goto fail_findSuper;
		
		/* Set superclass. */
		sjme_atomic_sjme_jclass_set(&inClass->superClass,
			superClass);
	}
	
	/* If there are interfaces, they need to be found as well. */
	interfaces = NULL;
	if (info->interfaceNames != NULL && info->interfaceNames->length > 0)
	{
		/* List needs to be setup first. */
		n = info->interfaceNames->length;
		if (sjme_error_is(error = sjme_list_alloc(allocPool, n,
			&interfaces, sjme_jclass, 0)) || interfaces == NULL)
			goto fail_allocInterfaces;
		
		/* Store it. */
		inClass->interfaceClasses = interfaces;
		
		/* Find all associated interfaces. */
		for (i = 0; i < n; i++)
		{
			/* Find interface class. */
			interface = NULL;
			if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
				loader, &interface, contextThread,
				info->interfaceNames->elements[i]->seq,
				SJME_JNI_FALSE)) ||
				interface == NULL)
				goto fail_findInterface;
			
			/* Set superclass. */
			interfaces->elements[i] = interface;
		}
	}
	
	/* Initialize super class now, recursive call. */
	if (superClass != NULL)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
			superClass, contextThread)))
			goto fail_initSuper;
	
	/* Then any interfaces, recursive call. */
	if (interfaces != NULL)
		for (i = 0, n = interfaces->length; i < n; i++)
			if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
				interfaces->elements[i], contextThread)))
				goto fail_initInterface;
	
	/* Lock on this. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inClass->object.common.lock)))
		return sjme_error_default(error);
	
	/* Determine instance field and method index offsets. */
	if (superClass != NULL)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInitSuper(
			inClass, superClass)))
			goto fail_super;
	
	/* The number of methods in this class for each type. */
	for (i = 0; i < SJME_NVM_CLASS_NUM_INSTANCE_TYPE; i++)
		inClass->methodCount[i] = info->methodCount[i] +
			(superClass != NULL && i != SJME_NVM_CLASS_MEMBER_STATIC ?
				superClass->methodCount[i] : 0);
	
	/* Setup static field storage. */
	for (i = 0; i < SJME_NUM_JAVA_TYPE_IDS; i++)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInitStaticFields(
			allocPool, inClass,
			(sjme_javaTypeId)i)))
			goto fail_initFieldValues;
	
	/* Bind instance and static methods. */
	for (i = 0; i < SJME_NVM_CLASS_NUM_INSTANCE_TYPE; i++)
	{
		binds = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInitMethodBinds(
			loader, inClass,
			(sjme_nvm_class_instanceType)i,
			&binds)) || binds == NULL)
			goto fail_bindMethods;
		inClass->methodBinds[i] = binds;
	}

#if 0
	/* Initialize statics fields with constant values. */
	for (;;)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Call static constructor, if one exists. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
	
	/* Set as initialized now. */
	if (!sjme_atomic_sjme_jint_compareSet(&inClass->isInitialized,
		SJME_VM_CLASS_INIT_LOAD_CURRENT,
		SJME_VM_CLASS_INIT_LOAD_DONE))
		goto fail_markDone;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL)))
		goto fail_releaseLock;
	
	/* If this is Object, then implicitly initialize Class as well. */
	if (sjme_charSeq_equalsUtfR(inClass->binaryName,
		"Ljava/lang/Object;"))
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
			classType, contextThread)))
			goto fail_initClassType;

	/* Success! */
skip_doubleCalled:
	return SJME_ERROR_NONE;
	
fail_markDone:
fail_bindMethods:
fail_initFieldValues:
fail_super:
	sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL);
	
fail_initClassType:
fail_badState:
fail_initInterface:
fail_initSuper:
fail_findInterface:
fail_allocInterfaces:
fail_findSuper:
fail_findClassType:
fail_releaseLock:
fail_checkLoad:
	/* Cache load error. */
	sjme_atomic_sjme_jint_compareSet(&inClass->error,
		SJME_ERROR_NONE, sjme_error_default(error));
	
	return sjme_error_vmError(contextThread, error);
}

sjme_errorCode sjme_nvm_vmClass_checkLoad(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread)
{
	sjme_errorCode error;
	sjme_nvm_vmClass_loader classLoader;
	
	if (inClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Error state occurred? */
	error = sjme_atomic_sjme_jint_get(&inClass->error);
	if (sjme_error_is(error))
		return sjme_error_vmError(contextThread, error);
	
	/* Does not need to be loaded? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isLoaded) != SJME_VM_CLASS_INIT_LOAD_NEVER)
		return SJME_ERROR_NONE;
		
	/* Recover the class loader. */
	classLoader = contextThread->inTask->classLoader;
	if (classLoader == NULL)
	{
		error = sjme_error_vmError(contextThread,
			SJME_ERROR_INVALID_CLASS_LOADER);
		goto fail_badState;
	}
	
	/* Lock on this. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inClass->object.common.lock)))
		return sjme_error_default(error);
	
	/* Set to be currently loading. */
	if (!sjme_atomic_sjme_jint_compareSet(&inClass->isLoaded,
		SJME_VM_CLASS_INIT_LOAD_NEVER,
		SJME_VM_CLASS_INIT_LOAD_CURRENT))
		goto skip_doubleCalled;

	/* Array type? */
	if (SJME_ERROR_NONE ==
		sjme_charSeq_charAtIs(inClass->binaryName, 0, '['))
	{
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInitArray(inClass,
			contextThread, classLoader)))
			goto fail_initSpecific;
	}

	/* Object type? */
	else if (SJME_ERROR_NONE ==
		sjme_charSeq_charAtIs(inClass->binaryName, 0, 'L'))
	{
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInitStandard(inClass,
			contextThread, classLoader)))
			goto fail_initSpecific;
	}
	
	/* Invalid */
	else
	{
		error = sjme_error_vmError(contextThread,
			SJME_ERROR_INVALID_CLASS_NAME);
		goto fail_badName;
	}
	
	/* Set as done! */
	sjme_atomic_sjme_jint_compareSet(&inClass->isLoaded,
		SJME_VM_CLASS_INIT_LOAD_CURRENT,
		SJME_VM_CLASS_INIT_LOAD_DONE);
	
	/* Unlock. */
skip_doubleCalled:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL)))
		goto fail_releaseLock;
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_noClassFound:
fail_badTryLib:
fail_initSpecific:
	sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL);
	
fail_releaseLock:
fail_badState:
fail_badName:
	/* Cache load error. */
	sjme_atomic_sjme_jint_compareSet(&inClass->error,
		SJME_ERROR_NONE, sjme_error_default(error));
	
	return sjme_error_vmError(contextThread, error);
}

sjme_errorCode sjme_nvm_vmClass_fieldSourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS)
		sjme_javaTypeId javaType,
	sjme_attrInPositive sjme_jint fieldId,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outInfo)
{
	sjme_list_sjme_nvm_class_fieldInfo* fields;
	sjme_jint i, n, base;
	sjme_jclass atClass;
	sjme_jboolean wantStatic;
	sjme_nvm_class_fieldInfo field;
	
	if (inClass == NULL || outInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE ||
		javaType < 0 || javaType >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (fieldId < 0 || fieldId >= inClass->fieldCount[instanceType][javaType])
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Do we want static? */
	wantStatic = (instanceType == SJME_NVM_CLASS_MEMBER_STATIC);
	
	/* Start at the current class for the search. */
	atClass = inClass;
	
	/* If we are below the class index, drop to the super class. */
	while (fieldId < atClass->fieldBase[instanceType][javaType])
	{
		atClass = sjme_atomic_sjme_jclass_get(&atClass->superClass);
		
		/* This should not occur. */
		if (atClass == NULL)
			return sjme_error_vmError(NULL,
				SJME_ERROR_SUPER_CLASS_INVALID);
	}

	/* Find the associated method. */
	base = atClass->fieldBase[instanceType][javaType];
	fields = atClass->info->fields;
	for (i = 0, n = fields->length; i < n; i++)
	{
		/* Get the method here. */
		field = fields->elements[i];
		if (field == NULL)
			return sjme_error_vmError(NULL, SJME_ERROR_NO_FIELD);
		
		/* If the static flag, index, and type matches, this is the one! */
		if (field->flags.member.isStatic == wantStatic &&
			field->typedIndex == (fieldId - base) &&
			field->javaType == javaType)
		{
			*outInfo = field;
			return SJME_ERROR_NONE;
		}
	}
	
	/* If this point is reached, the index is not valid. */
	return sjme_error_vmError(NULL, SJME_ERROR_NO_FIELD);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoad(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_charSeq className,
	sjme_attrInValue sjme_jboolean doInit)
{
	sjme_errorCode error;
	sjme_lpstr wrapName;
	sjme_charSeqStatic wrapSeq;
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		className == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If any array, no wrapping needs to be done. */
	error = sjme_charSeq_charAtIs(className, 0, '[');
	if (!sjme_error_is(error))
		return sjme_nvm_vmClass_loaderLoadF(inLoader, outClass,
			contextThread, className, doInit);
	else if (error != SJME_ERROR_NOT_MATCHED)
		return sjme_error_vmError(contextThread, error);

	/* Check that the name does not contain any invalid characters. */
	if (sjme_error_is(error = sjme_nvm_class_validBinaryName(className)))
		return sjme_error_vmError(contextThread, error);

	/* Allocate wrapped name. */
	wrapName = sjme_alloca(sizeof(*wrapName) * SJME_NVM_CLASS_NAME_LIMIT);
	if (wrapName == NULL)
		return sjme_error_outOfMemory(NULL, SJME_NVM_CLASS_NAME_LIMIT);
	memset(wrapName, 0, sizeof(*wrapName) * SJME_NVM_CLASS_NAME_LIMIT);

	/* Wrap it in an object specifier. */
	snprintf(wrapName, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"L%s;", sjme_charSeq_tempUtf(className));
	wrapName[SJME_NVM_CLASS_NAME_LIMIT - 1] = '\0';

	/* Setup sequence. */
	memset(&wrapSeq, 0, sizeof(wrapSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&wrapSeq,
		wrapName, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_nvm_vmClass_loaderLoadF(inLoader, outClass,
		contextThread, &wrapSeq, doInit);
}

sjme_jboolean sjme_nvm_vmClass_isAssignableFrom(
	sjme_attrInNotNull sjme_jclass canAssignTo,
	sjme_attrInNotNull sjme_jclass fromClass)
{
	if (canAssignTo == NULL || fromClass == NULL)
		return SJME_JNI_FALSE;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadArray(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositiveNonZero sjme_jint dims)
{
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (dims <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadArrayA(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr componentType,
	sjme_attrInPositiveNonZero sjme_jint dims)
{
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (dims <= 0)
		return sjme_error_vmError(contextThread,
			SJME_ERROR_NEGATIVE_ARRAY_SIZE);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadF(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_charSeq fieldName,
	sjme_attrInValue sjme_jboolean doInit)
{
	sjme_errorCode error;
	sjme_jint hash, freeSlot;
	sjme_list_sjme_jclass* classes;
	sjme_jclass maybe;
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		fieldName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine hash of the binary name for quicker checking. */
	hash = 0;
	if (sjme_error_is(error = sjme_charSeq_hash(fieldName, &hash)))
		return sjme_error_default(error);
	
	/* Grab the read lock to determine if we can skip loading. */
	if (sjme_error_is(error = sjme_thread_rwLockGrabRead(
		&inLoader->rwLock)))
		return sjme_error_default(error);
	
	/* Check to see if the class has already been loaded. */
	maybe = NULL;
	freeSlot = -1;
	classes = inLoader->classes;
	if (sjme_error_is(error = sjme_listUtil_findItemWeak(
		SJME_AS_LIST_POINTER(classes),
		&freeSlot, (sjme_pointer*)&maybe,
		sjme_nvm_vmClass_loaderLoadCheck,
		hash, (sjme_pointer)fieldName)))
		goto fail_findFail;
	
	/* Found something? */
	if (maybe != NULL)
		goto skip_foundClass;
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("Need to find class: %s", binaryName);
#endif
	
	/* Grab the write lock on top of this. */
	if (sjme_error_is(error = sjme_thread_rwLockGrabWrite(
		&inLoader->rwLock)))
		goto fail_lockWrite;
	
	/* The free slot might have been taken by something else if we got */
	/* unlucky in the lock cycle. */
	if (sjme_error_is(error = sjme_listUtil_findFree(
		SJME_AS_LIST_POINTER(classes), &freeSlot)))
		goto fail_findFree;
	
	/* Need to grow the class list? */
	if (freeSlot < 0)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Forward load of class. */
	maybe = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFSubAlloc(
		inLoader, &maybe,
		&classes->elements[freeSlot],
		contextThread, fieldName)) || maybe == NULL)
		goto fail_loadClass;
	
	/* Release the write lock. */
	if (sjme_error_is(error = sjme_thread_rwLockReleaseWrite(
		&inLoader->rwLock, NULL)))
		return sjme_error_default(error);
	
skip_foundClass:
	/* Release the read lock. */
	if (sjme_error_is(error = sjme_thread_rwLockReleaseRead(
		&inLoader->rwLock, NULL)))
		goto fail_releaseRead;
		
	/* From this point implicitly initialize as it is being requested. */
	if (doInit && sjme_atomic_sjme_jint_get(
		&maybe->isLoaded) == SJME_VM_CLASS_INIT_LOAD_NEVER)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
			maybe, contextThread)))
			return sjme_error_vmError(contextThread, error);
	
	/* Success! */
	*outClass = maybe;
	return SJME_ERROR_NONE;
	
fail_loadClass:
fail_releaseRead:
fail_findFree:
	/* Release the write lock before failing. */
	sjme_thread_rwLockReleaseWrite(&inLoader->rwLock, NULL);
	
fail_lockWrite:
fail_findFail:
	/* Release the read lock before failing. */
	sjme_thread_rwLockReleaseRead(&inLoader->rwLock, NULL);

	if (error == SJME_ERROR_NOT_MATCHED)
		error = SJME_ERROR_NO_CLASS;
	return sjme_error_vmError(contextThread, error);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadFU(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr fieldName,
	sjme_attrInValue sjme_jboolean doInit)
{
	sjme_errorCode error;
	sjme_charSeqStatic seq;
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		fieldName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup sequence. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&seq,
		fieldName, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_nvm_vmClass_loaderLoadF(inLoader, outClass,
		contextThread, &seq, doInit);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadPrimitive(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId basicType)
{
#define BUFSIZE 3
	sjme_cchar buf[BUFSIZE];
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (basicType < 0 || basicType >= SJME_NUM_BASIC_TYPE_IDS ||
		basicType == SJME_JAVA_TYPE_ID_SHORT_OR_CHAR ||
		basicType == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		basicType == SJME_JAVA_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Which type? */
	memset(buf, 0, sizeof(buf));
	switch (basicType)
	{
		case SJME_BASIC_TYPE_ID_INTEGER:
			buf[0] = 'I';
			break;
		
		case SJME_BASIC_TYPE_ID_LONG:
			buf[0] = 'J';
			break;
		
		case SJME_BASIC_TYPE_ID_FLOAT:
			buf[0] = 'F';
			break;
		
		case SJME_BASIC_TYPE_ID_DOUBLE:
			buf[0] = 'D';
			break;
		
		case SJME_BASIC_TYPE_ID_VOID:
			buf[0] = 'V';
			break;
		
		case SJME_BASIC_TYPE_ID_SHORT:
			buf[0] = 'S';
			break;
		
		case SJME_BASIC_TYPE_ID_CHARACTER:
			buf[0] = 'C';
			break;
		
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			buf[0] = 'Z';
			break;
		
		case SJME_BASIC_TYPE_ID_BYTE:
			buf[0] = 'B';
			break;
		
		default:
			return sjme_error_vmError(contextThread,
				SJME_ERROR_INVALID_FIELD_TYPE);
	}
	
	/* Forward call. */
	return sjme_nvm_vmClass_loaderLoadFU(inLoader, outClass,
		contextThread, buf, SJME_JNI_TRUE);
#undef BUFSIZE
}

sjme_errorCode sjme_nvm_vmClass_loaderNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_vmClass_loader* outLoader,
	sjme_attrInNotNull sjme_list_sjme_nvm_rom_library* classPath)
{
	sjme_errorCode error;
	sjme_nvm_vmClass_loader result;
	sjme_list_sjme_nvm_rom_library* dup;
	sjme_list_sjme_jclass* classes;
	sjme_nvm_rom_library lib;
	sjme_jint i, n, cldcCompact;
	sjme_nvm_stringPool nullStrings;
	
	if (inState == NULL || outLoader == NULL || classPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate string pool for anything not coming from libraries. */
	nullStrings = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_new(inState->allocPool,
		&nullStrings)) || nullStrings == NULL)
		goto fail_allocStrings;
	
	/* Duplicate list. */
	dup = NULL;
	if (sjme_error_is(error = sjme_list_copy(inState->allocPool,
		classPath->length, classPath, &dup, sjme_nvm_rom_library, 0)))
		goto fail_dupList;
	
	/* There cannot be any NULLs, but also find cldc-compact.jar. */
	cldcCompact = -1;
	for (i = 0, n = dup->length; i < n; i++)
	{
		lib = dup->elements[i];
		if (lib == NULL)
			goto fail_nullJar;
		
		/* Is this cldc-compact? */
		if (cldcCompact < 0 && strcmp("cldc-compact.jar", lib->name) == 0)
			cldcCompact = i;
	}
	
	/* There needs to be this. */
	if (cldcCompact < 0)
		goto fail_noCldcCompact;
	
	/* cldc-compact.jar must always be first, so swap it! */
	if (cldcCompact != 0)
	{
		lib = dup->elements[0];
		dup->elements[0] = dup->elements[cldcCompact];
		dup->elements[cldcCompact] = lib;
	}
	
	/* Classes cache. */
	classes = NULL;
	if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
		SJME_VM_CLASS_GROW_LEN, &classes, sjme_jclass, 0)) || classes == NULL)
		goto fail_classesList;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_VM_CLASS_LOADER,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_alloc; 
	
	/* Setup fields. */
	result->rwLock.read = &result->common.lock;
	result->inState = inState;
	result->classPath = dup;
	result->classes = classes;
	result->nullStrings = nullStrings;
	
	/* Success! */
	*outLoader = result;
	return SJME_ERROR_NONE;
	
fail_alloc:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
fail_classesList:
	if (classes != NULL)
		sjme_alloc_free(classes);
fail_noCldcCompact:
fail_nullJar:
fail_dupList:
	if (dup != NULL)
		sjme_alloc_free(dup);
fail_allocStrings:
	if (nullStrings != NULL)
		sjme_alloc_free(nullStrings);
	
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_vmClass_methodIDByNameType(
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
	sjme_list_sjme_jmethodID* methods;
	sjme_jmethodID method;
	
	if (inClass == NULL || contextThread == NULL || inName == NULL ||
		inType == NULL || outID == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Needs to be initialized first. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
		inClass, contextThread)))
		return sjme_error_default(error);
		
	/* Look through all methods. */
	methods = inClass->methodBinds[instanceType];
	for (i = 0, n = methods->length; i < n; i++)
	{
		/* There must be a valid method here. */
		method = methods->elements[i];
		if (method == NULL)
			return sjme_error_vmError(contextThread,
				SJME_ERROR_NO_METHOD);
		
		/* Is this the method. */
		if (sjme_charSeq_equalsR(method->member.name->seq, inName) &&
			sjme_charSeq_equalsR(method->member.type->seq, inType))
		{
			*outID = method;
			return SJME_ERROR_NONE;
		}
	}

	/* Not found. */
	if (!required)
		return SJME_ERROR_NO_METHOD;
	return sjme_error_vmError(contextThread, SJME_ERROR_NO_METHOD);
}

sjme_errorCode sjme_nvm_vmClass_methodIDByNameTypeU(
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
	return sjme_nvm_vmClass_methodIDByNameType(inClass, contextThread,
		instanceType, required, &wrapName, &wrapType, outID);
}

sjme_errorCode sjme_nvm_vmClass_methodSourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_jint methodId,
	sjme_attrOutNotNull sjme_nvm_class_methodInfo* outInfo)
{
	sjme_list_sjme_nvm_class_methodInfo* methods;
	sjme_jint i, n, base;
	sjme_jclass atClass;
	sjme_jboolean wantStatic;
	sjme_nvm_class_methodInfo method;
	
	if (inClass == NULL || outInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (methodId < 0 || methodId >= inClass->methodCount[instanceType])
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Do we want static? */
	wantStatic = (instanceType == SJME_NVM_CLASS_MEMBER_STATIC);
	
	/* Start at the current class for the search. */
	atClass = inClass;
	
	/* If we are below the class index, drop to the super class. */
	while (methodId < atClass->methodBase[instanceType])
	{
		atClass = sjme_atomic_sjme_jclass_get(&atClass->superClass);
		
		/* This should not occur, but it might. */
		if (atClass == NULL)
			return sjme_error_vmError(NULL,
				SJME_ERROR_SUPER_CLASS_INVALID);
	}

	/* Find the associated method. */
	base = atClass->methodBase[instanceType];
	methods = atClass->info->methods;
	for (i = 0, n = methods->length; i < n; i++)
	{
		/* Get the method here. */
		method = methods->elements[i];
		if (method == NULL)
			return sjme_error_vmError(NULL, SJME_ERROR_NO_METHOD);
		
		/* If the static flag and the index matches, this is the one! */
		if (method->flags.member.isStatic == wantStatic &&
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
