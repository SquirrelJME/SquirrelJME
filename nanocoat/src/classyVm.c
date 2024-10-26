/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/listUtil.h"
#include <stdio.h>
#include <string.h>

#include "sjme/nvm/classyVm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/task.h"
#include "sjme/util.h"

/** The class name length limit. */
#define SJME_VM_CLASS_NAME_LIMIT 256

/** The amount the class list grows by. */
#define SJME_VM_CLASS_GROW_LEN 32

/** Initialize/load not happening. */
#define SJME_VM_CLASS_INIT_LOAD_NEVER 0

/** Initialize/load is currently happening. */
#define SJME_VM_CLASS_INIT_LOAD_CURRENT 1

/** Initialize/load is now done. */
#define SJME_VM_CLASS_INIT_LOAD_DONE 2

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
		strcmp(maybe->binaryName, againstP) == 0)
		return SJME_ERROR_NONE;
	
	/* Not matched. */
	return SJME_ERROR_NOT_MATCHED;
}

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
	sjme_jint autoLoad;
	
	if (inLoader == NULL || outClass == NULL || outSlot == NULL ||
		contextThread == NULL || binaryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot be blank. */
	if (strlen(binaryName) <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
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
	
	/* Unless this is a specific binary name, it is never loaded. */
	/* Arrays and primitive types are always considered to be loaded. */
	autoLoad = SJME_VM_CLASS_INIT_LOAD_NEVER;
	if (binaryName[0] == '[' || (strlen(binaryName) == 1 &&
		(binaryName[0] == 'Z' || binaryName[0] == 'B' ||
		binaryName[0] == 'S' || binaryName[0] == 'C' ||
		binaryName[0] == 'I' || binaryName[0] == 'J' ||
		binaryName[0] == 'F' || binaryName[0] == 'D' ||
		binaryName[0] == 'V')))
		autoLoad = SJME_VM_CLASS_INIT_LOAD_DONE;
	
	/* Store into the output slot immediately for recursive loading. */
	result->binaryName = dupName;
	result->binaryHash = sjme_string_hash(dupName);
	sjme_atomic_sjme_jint_set(&result->isLoaded, 0);
	sjme_atomic_sjme_jint_set(&result->isInitialized, autoLoad);
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
	sjme_nvm_class_info info;
	sjme_nvm_vmClass_loader loader;
	sjme_jint i, n;
	sjme_jclass superClass, interface;
	sjme_list_sjme_jclass* interfaces;
	sjme_alloc_pool* inPool;
	
	if (inClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need these in order to work at all. */
	inPool = contextThread->inState->reservedPool;
	
	/* Needs loading first? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isLoaded) == SJME_VM_CLASS_INIT_LOAD_NEVER)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkLoad(inClass,
			contextThread)))
			return sjme_error_default(error);
	
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
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Initializing class: %s", inClass->binaryName);
#endif
	
	/* The class info should now be valid. */
	info = inClass->info;
	loader = contextThread->inTask->classLoader;
	if (info == NULL || loader == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* The super class needs to be found first. */
	superClass = NULL;
	if (info->superName != NULL)
	{
		/* Find super class. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
			loader, &superClass, contextThread,
			(sjme_lpcstr)&info->superName->chars[0],
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
		if (sjme_error_is(error = sjme_list_alloc(inPool, n,
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
				(sjme_lpcstr)
					&info->interfaceNames->elements[i]->chars[0],
				SJME_JNI_FALSE)) ||
				interface == NULL)
				goto fail_findInterface;
			
			/* Set superclass. */
			interfaces->elements[i] = interface;
		}
	}
	
	/* Initialize super class now. */
	if (superClass != NULL)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
			superClass, contextThread)))
			goto fail_initSuper;
	
	/* Then any interfaces. */
	if (interfaces != NULL)
		for (i = 0, n = interfaces->length; i < n; i++)
			if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
				interfaces->elements[i], contextThread)))
				goto fail_initInterface;
	
	/* Lock on this. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inClass->object.common.lock)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Set as initialized now. */
	if (!sjme_atomic_sjme_jint_compareSet(&inClass->isInitialized,
		SJME_VM_CLASS_INIT_LOAD_CURRENT,
		SJME_VM_CLASS_INIT_LOAD_DONE))
		goto fail_markDone;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
skip_doubleCalled:
	return SJME_ERROR_NONE;
	
fail_markDone:
	sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL);
	
fail_initInterface:
fail_initSuper:
fail_findInterface:
fail_allocInterfaces:
fail_findSuper:
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_vmClass_checkLoad(
	sjme_attrOutNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread)
{
	sjme_errorCode error;
	sjme_nvm_vmClass_loader classLoader;
	sjme_list_sjme_nvm_rom_library* classPath;
	sjme_nvm_class_info info;
	sjme_nvm_rom_library tryLib;
	sjme_jint i, n;
	sjme_cchar fileName[SJME_VM_CLASS_NAME_LIMIT];
	
	if (inClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Does not need to be loaded? */
	if (sjme_atomic_sjme_jint_get(
		&inClass->isLoaded) != SJME_VM_CLASS_INIT_LOAD_NEVER)
		return SJME_ERROR_NONE;
	
	/* Cannot be loaded using this? */
	if (inClass->binaryName[0] != 'L')
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Recover the class loader. */
	classLoader = contextThread->inTask->classLoader;
	if (classLoader == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* And the class path. */	
	classPath = classLoader->classPath;
	if (classPath == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Determine the file name of the class. */
	memset(fileName, 0, sizeof(fileName));
	snprintf(fileName, SJME_VM_CLASS_NAME_LIMIT - 1,
		"%.*s.class", (int)(strlen(&inClass->binaryName[1]) - 1),
		&inClass->binaryName[1]);
	
	/* Lock on this. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inClass->object.common.lock)))
		return sjme_error_default(error);
	
	/* Set to be currently loading. */
	if (!sjme_atomic_sjme_jint_compareSet(&inClass->isLoaded,
		SJME_VM_CLASS_INIT_LOAD_NEVER,
		SJME_VM_CLASS_INIT_LOAD_CURRENT))
		goto skip_doubleCalled;
	
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
			goto fail_badTryLib;
		}
		
		/* Stop. */
		break;
	}
	
	/* Set class info. */
	inClass->info = info;
	
	/* Set as done! */
	sjme_atomic_sjme_jint_compareSet(&inClass->isLoaded,
		SJME_VM_CLASS_INIT_LOAD_CURRENT,
		SJME_VM_CLASS_INIT_LOAD_DONE);
	
	/* Unlock. */
skip_doubleCalled:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_badTryLib:
	sjme_thread_spinLockRelease(
		&inClass->object.common.lock, NULL);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoad(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr className,
	sjme_attrInValue sjme_jboolean doInit)
{
	sjme_cchar buf[SJME_VM_CLASS_NAME_LIMIT];
	
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		className == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine actual name to use. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, SJME_VM_CLASS_NAME_LIMIT - 1,
		"L%s;", className);
	
	/* Forward call. */
	return sjme_nvm_vmClass_loaderLoadB(inLoader, outClass,
		contextThread, buf, doInit);
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
	sjme_attrInNotNull sjme_lpcstr binaryName,
	sjme_attrInValue sjme_jboolean doInit)
{
	sjme_errorCode error;
	sjme_jint hash, freeSlot;
	sjme_list_sjme_jclass* classes;
	sjme_jclass maybe;
	
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
	maybe = NULL;
	freeSlot = -1;
	classes = inLoader->classes;
	if (sjme_error_is(error = sjme_listUtil_findItemWeak(
		SJME_AS_LIST_POINTER(classes),
		&freeSlot, (sjme_pointer*)&maybe,
		sjme_nvm_vmClass_loaderLoadCheck,
		hash, (sjme_pointer)binaryName)))
		goto fail_findFail;
	
	/* Found something? */
	if (maybe != NULL)
		goto skip_foundClass;
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
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
	if (doInit && sjme_atomic_sjme_jint_get(
		&maybe->isLoaded) == SJME_VM_CLASS_INIT_LOAD_NEVER)
		if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(
			maybe, contextThread)))
			return sjme_error_default(error);
	
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
