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
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/util.h"

/** The amount the class list grows by. */
#define SJME_VM_CLASS_GROW_LEN 32

/** Initialize/load not happening. */
#define SJME_VM_CLASS_INIT_LOAD_NEVER 0

/** Initialize/load is currently happening. */
#define SJME_VM_CLASS_INIT_LOAD_CURRENT 1

/** Initialize/load is now done. */
#define SJME_VM_CLASS_INIT_LOAD_DONE 2

static sjme_errorCode sjme_nvm_vmClass_loaderLoadBSubAlloc(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrOutNotNull sjme_jclass* outSlot,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr binaryName)
{
	sjme_errorCode error;
	sjme_jclass result;
	sjme_lpstr dupName;
	
	if (inLoader == NULL || outClass == NULL || outSlot == NULL ||
		contextThread == NULL || binaryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Duplicate binary name. */
	dupName = NULL;
	if (sjme_error_is(error = sjme_alloc_strdup(
		inLoader->inState->reservedPool, &dupName,
		binaryName)) || dupName == NULL)
		goto fail_dupName;
	
	/* Allocate resultant class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		inLoader->inState->reservedPool,
		sizeof(*result), SJME_NVM_STRUCT_CLASS_INSTANCE,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Is now being used, so count up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(result, NULL)))
		goto fail_countUp;
	
	/* Store into the output slot immediately for recursive loading. */
	result->binaryName = dupName;
	result->binaryHash = sjme_string_hash(dupName);
	sjme_atomic_sjme_jint_set(&result->isLoaded, 0);
	sjme_atomic_sjme_jint_set(&result->isInitialized, 0);
	*outSlot = result;
	
	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;
	
fail_countUp:
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
	
	if (inClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Needs loading first? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isLoaded) == SJME_VM_CLASS_INIT_LOAD_NEVER)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkLoad(inClass,
			contextThread)))
			return sjme_error_default(error);
			
	/* Does not need to be initialized? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isInitialized) != SJME_VM_CLASS_INIT_LOAD_NEVER)
		return SJME_ERROR_NONE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_checkLoad(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread)
{
	sjme_errorCode error;
	
	if (inClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Does not need to be loaded? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isLoaded) != SJME_VM_CLASS_INIT_LOAD_NEVER)
		return SJME_ERROR_NONE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoad(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr className)
{
#define BUFSIZE 128
	sjme_cchar buf[BUFSIZE];
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		className == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine actual name to use. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, BUFSIZE - 1, "L%s;", className);
	
	/* Forward call. */
	return sjme_nvm_vmClass_loaderLoadB(inLoader, outClass,
		contextThread, buf);
#undef BUFSIZE
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
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadB(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr binaryName)
{
	sjme_errorCode error;
	sjme_jint i, n, hash, freeSlot;
	sjme_list_sjme_jclass* classes;
	sjme_jclass maybe;
	sjme_alloc_weak weak;
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		binaryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine has of the binary name for quicker checking. */
	hash = sjme_string_hash(binaryName);
	
	/* Grab the read lock to determine if we can skip loading. */
	if (sjme_error_is(error = sjme_thread_rwLockGrabRead(
		&inLoader->rwLock)))
		return sjme_error_default(error);
	
	/* Check to see if the class has already been loaded. */
	/* Also record the first free slot. */
	freeSlot = -1;
	classes = inLoader->classes;
	for (i = 0, n = classes->length; i < n; i++)
	{
		/* Skip nulls. */
		maybe = classes->elements[i];
		if (maybe == NULL)
		{
			/* Capable free slot. */
			if (freeSlot < 0)
				freeSlot = i;
			continue;
		}
		
		/* Check to see if this class is still valid. */
		weak = NULL;
		if (sjme_error_is(error = sjme_alloc_weakRefGet(maybe,
			&weak)) || weak == NULL)
		{
			/* If not a weak reference, it is considered as no longer valid */
			/* but this is not an actual error case. */
			if (error == SJME_ERROR_NOT_WEAK_REFERENCE)
			{
				error = SJME_ERROR_NONE;
				classes->elements[i] = NULL;
				
				/* Capable free slot. */
				if (freeSlot < 0)
					freeSlot = i;
				continue;
			}
			
			/* Not good! Corruption or otherwise? */
			goto fail_badWeakClass;
		}
		
		/* Could it be this one? */
		if (hash == maybe->binaryHash &&
			strcmp(maybe->binaryName, binaryName) == 0)
			goto skip_foundClass;
		
		/* Make sure this is cleared. */
		maybe = NULL;
	}
	
	/* Grab the write lock on top of this. */
	if (sjme_error_is(error = sjme_thread_rwLockGrabWrite(
		&inLoader->rwLock)))
		goto fail_lockWrite;
	
	/* Need to grow the class list? */
	if (freeSlot < 0)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Forward load of class. */
	maybe = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadBSubAlloc(
		inLoader, &maybe, &classes->elements[freeSlot],
		contextThread, binaryName)))
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
	if (sjme_atomic_sjme_jint_get(
		&maybe->isLoaded) == SJME_VM_CLASS_INIT_LOAD_NEVER)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
			maybe, contextThread)))
			return sjme_error_default(error);
	
	/* Success! */
	*outClass = maybe;
	return SJME_ERROR_NONE;
	
fail_loadClass:
fail_releaseRead:
	/* Release the write lock before failing. */
	sjme_thread_rwLockReleaseWrite(&inLoader->rwLock, NULL);
	
fail_lockWrite:
fail_badWeakClass:
	/* Release the read lock before failing. */
	sjme_thread_rwLockReleaseRead(&inLoader->rwLock, NULL);
	
	return sjme_error_default(error);
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
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Forward call. */
	return sjme_nvm_vmClass_loaderLoadB(inLoader, outClass,
		contextThread, buf);
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
	
	if (inState == NULL || outLoader == NULL || classPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Duplicate list. */
	dup = NULL;
	if (sjme_error_is(error = sjme_list_copy(inState->reservedPool,
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
	if (sjme_error_is(error = sjme_list_alloc(inState->reservedPool,
		SJME_VM_CLASS_GROW_LEN, &classes, sjme_jclass, 0)) || classes == NULL)
		goto fail_classesList;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState->reservedPool,
		sizeof(*result), SJME_NVM_STRUCT_VM_CLASS_LOADER,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_alloc; 
	
	/* Setup fields. */
	result->rwLock.read = &result->common.lock;
	result->inState = inState;
	result->classPath = dup;
	result->classes = classes;
	
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
	
	return sjme_error_default(error);
}
