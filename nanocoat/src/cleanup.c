/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <unistd.h>

#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/boot.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/rom.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/walk.h"

#define SJME_CLEANUP_DECL \
	sjme_errorCode error; \
	sjme_pointer temp

#define SJME_CLEANUP_OP(get, set, op) \
	do { get; if ((temp) != NULL) \
	{ \
		set; \
		if (sjme_error_is(error = op)) \
			return sjme_error_default(error); \
	} } while (0)

#define SJME_CHARSEQ_DELETE(ptr) \
	SJME_CLEANUP_OP(temp = (ptr), \
		(ptr) = NULL, \
		sjme_charSeq_delete(temp))

#define SJME_SIMPLE_CLOSE(ptr) \
	SJME_CLEANUP_OP(temp = (ptr), \
		(ptr) = NULL, \
		sjme_closeable_close(SJME_AS_CLOSEABLE(temp)))

#define SJME_COUNT_DOWN_ATOMIC_NAT(type, numPointerStars, ptr) \
	SJME_CLEANUP_OP(temp = sjme_atomic_gP(type, numPointerStars, (ptr)), \
		sjme_atomic_sP(type, numPointerStars, (ptr), NULL), \
		sjme_nvm_instance_countDown(temp))

#define SJME_SIMPLE_CLOSE_ATOMIC_NAT(type, numPointerStars, ptr) \
	SJME_CLEANUP_OP(temp = sjme_atomic_gP(type, numPointerStars, (ptr)), \
		sjme_atomic_sP(type, numPointerStars, (ptr), NULL), \
		sjme_closeable_close(SJME_AS_CLOSEABLE(temp)))

#define SJME_SIMPLE_CLOSE_ATOMIC(type, numPointerStars, ptr) \
	SJME_SIMPLE_CLOSE_ATOMIC_NAT(type, numPointerStars, &(ptr))

#define SJME_SIMPLE_FREE(ptr) \
	SJME_CLEANUP_OP(temp = (sjme_pointer)(ptr), \
		(ptr) = NULL, \
		sjme_alloc_free(temp))

#define SJME_FLAGGED_FREE(free, ptr) \
	do { if ((free) && (ptr) != NULL) \
	{ \
		temp = (sjme_pointer)(ptr); \
		(ptr) = NULL; \
		if (sjme_error_is(error = sjme_alloc_free(temp))) \
			return sjme_error_default(error); \
	} } while (0)

/** The magic number for NVM objects. */
#define SJME_NVM_OBJECT_MAGIC UINT32_C(0x4E764D3F)

static sjme_errorCode sjme_nvm_cleanup_walkStep(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at)
{
	SJME_CLEANUP_DECL;
	
	if (root == NULL || at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG_GC) && 0
	/* Debug. */
	sjme_message("GC Walk: %s %s %p (%d)->#%d (%d)",
		(at->stage == SJME_NVM_WALK_STAGE_PRE ? "PRE" : "STEPS"),
		(at->breadth == SJME_NVM_WALK_BREADTH_LEVEL ? "LEVEL" : "DIVE"),
		at->base, at->root->typeId, at->index, at->typeId);
#endif

	/* If this is the root of the item, we may want to indicate that */
	/* depending on the item type we may or may not want to dive. */
	if (at->index < 0 && at->index != INT32_MIN)
	{
		/* Either we can perform. */
		switch ((at->variantStep != NULL ? at->variantStep->typeId.i :
			at->typeId.i))
		{
				/* The constant pool and pool entries are fine to do a */
				/* normal walk since they mostly are constant or point */
				/* to string pool strings. */
			case SJME_NVM_STRUCT_POOL:
			case SJME_NVM_WALK_PSEUDO_POOL_ENTRY:
				break;
			
				/* Do not dive by default. */
			default:
				at->noDive = SJME_JNI_TRUE;
				break;
		}

		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* Pre-stage sub-structure recursive cleanup? */
	else if (at->stage == SJME_NVM_WALK_STAGE_PRE && at->index != INT32_MIN &&
		at->index != INT32_MAX && at->index >= 0)
	{
		/* Only clean up objects if they are not ourselves, otherwise we */
		/* will end up garbage collecting too early. Objects get counted */
		/* down accordingly. Naturally NVM structures are always positive */
		/* type identifiers. */
		if (at->typeId.i > SJME_NVM_STRUCT_UNKNOWN &&
			at->valueP.value != at->baseStruct.value &&
			!at->isPhantom &&
			*at->valueP.pointer != NULL &&
			sjme_nvm_isAR(*at->valueP.pointer,
				SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
		{
			/* Count down. */
			SJME_COUNT_DOWN_ATOMIC_NAT(sjme_pointer, 0,
				at->valueP.atomicPointer);
		}

		/* Any structure type can be closed, if not an object. */
		/* And it is not phantom (points back to a parent). */
		else if (at->typeId.i > SJME_NVM_STRUCT_UNKNOWN &&
			!at->isPhantom &&
			*at->valueP.pointer != NULL &&
			at->valueP.value != at->baseStruct.value &&
			!sjme_nvm_isAR(*at->valueP.pointer,
				SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
		{
			SJME_SIMPLE_CLOSE_ATOMIC_NAT(sjme_pointer, 0,
				at->valueP.atomicPointer);
		}
	}

	/* Normal stage self clean before de-allocation. */
	else if (at->stage == SJME_NVM_WALK_STAGE_STEPS &&
		(at->index == INT32_MIN || at->index == INT32_MAX))
	{
		/* Perform any generic structureless cleanup that can be performed. */
	}

	/* Ignored otherwise. */
	return SJME_ERROR_NONE;
}

static const sjme_nvm_walk_functions sjme_nvm_cleanup_walkFunctions =
{
	sjme_sm(.pre, sjme_nvm_cleanup_walkStep),
	sjme_sm(.step, sjme_nvm_cleanup_walkStep),
};

static sjme_errorCode sjme_nvm_cleanup_close(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_common common;

	/* Recover common object. */
	common = SJME_AS_NVM_COMMON(closeable);
	if (common == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG_GC)
	/* Debug. */
	sjme_message("GC FREE: %d:%p",
		common->type, common);
#endif

	/* Lock self. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&common->lock)))
		return sjme_error_default(error);

	/* Perform a generic walk close. */
	if (sjme_error_is(error = sjme_nvm_walk_start(common, common->type,
		&sjme_nvm_cleanup_walkFunctions, NULL)))
		goto fail_walk;

	/* Is there a post-close handler for this? */
	/* Cleanup any sub-structures that would otherwise normally */
	/* not be cleaned up automatically with a generic walk. */
	if (common->postClose != NULL)
	{
		/* Call handler. */
		if (sjme_error_is(error = common->postClose(closeable)))
			goto fail_handler;

		/* Clear it since it was performed. */
		common->postClose = NULL;
	}
	
	/* Unlock self, in the event native locks claim resources. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&common->lock,
		NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_handler:
fail_walk:
	/* Release before failing. */
	sjme_thread_spinLockRelease(&common->lock, NULL);

	/* Fail. */
	return sjme_error_default(error);
}

static sjme_errorCode sjme_nvm_cleanup_postIsClasses(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_isClasses isClasses;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	isClasses = (sjme_nvm_isClasses)closeable;
	if (isClasses == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Free the class list, every class here is phantom regardless. */
	SJME_SIMPLE_FREE(isClasses->classes);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postClass(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jclass classy;
	sjme_list(sjme_jclass)* interfaces;
	sjme_list(sjme_jinterfaceID)* iBinds;
	sjme_list(sjme_jfieldID)* fBinds;
	sjme_list(sjme_jmethodID)* mBinds;
	sjme_nvm_class_instanceType instanceType;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	classy = (sjme_jclass)closeable;
	if (classy == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cleanup basic class information. */
	SJME_SIMPLE_CLOSE_ATOMIC(sjme_jclass, 0, classy->superClass);
	SJME_SIMPLE_CLOSE_ATOMIC(sjme_jclass, 0, classy->componentType);
	
	/* Cleanup interfaces, if any. */
	interfaces = classy->interfaceClasses;
	if (interfaces != NULL)
	{
		/* Close each interface class. */
		for (n = interfaces->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(interfaces->elements[i]);
		
		/* Free the list itself. */
		SJME_SIMPLE_FREE(classy->interfaceClasses);
	}

	/* Cleanup interface binds. */
	iBinds = classy->interfaceBinds;
	if (iBinds != NULL)
	{
		/* Close each interface bind. */
		for (n = iBinds->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(iBinds->elements[i]);
		
		/* Free the list. */
		SJME_SIMPLE_FREE(classy->interfaceBinds);
	}

	/* Free field and method binds. */
	for (instanceType = 0; instanceType < SJME_NVM_CLASS_NUM_INSTANCE_TYPE;
		instanceType++)
	{
		/* Cleanup field binds. */
		fBinds = classy->fields[instanceType].binds;
		if (fBinds != NULL)
		{
			/* Close each one. */
			for (n = fBinds->length, i = 0; i < n; i++)
				SJME_SIMPLE_CLOSE(fBinds->elements[i]);

			/* Free list. */
			SJME_SIMPLE_FREE(classy->fields[instanceType].binds);
		}

		/* Cleanup method binds. */
		mBinds = classy->methods[instanceType].binds;
		if (mBinds != NULL)
		{
			/* Close each one. */
			for (n = mBinds->length, i = 0; i < n; i++)
				SJME_SIMPLE_CLOSE(mBinds->elements[i]);

			/* Free list. */
			SJME_SIMPLE_FREE(classy->methods[instanceType].binds);
		}
	}

	/* Cleanup any remaining manual allocations. */
	SJME_CHARSEQ_DELETE(classy->binaryName);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postObject(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jobject object;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	object = (sjme_jobject)closeable;
	if (object == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Clear reference to class. */
	SJME_SIMPLE_CLOSE(object->isClass);

	/* Class specific cleanup? */
	if (object->common.type == SJME_NVM_STRUCT_CLASS_INSTANCE)
		if (sjme_error_is(error = sjme_nvm_cleanup_postClass(closeable)))
			return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postRomLibrary(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_rom_library library;
	sjme_list(sjme_nvm_class_info)* classInfos;
	sjme_nvm_class_info classInfo;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;

	/* Recover. */
	library = (sjme_nvm_rom_library)closeable;
	if (library == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Call main close on the library. */
	if (library->functions != NULL && library->functions->close != NULL)
		if (sjme_error_is(error = library->functions->close(library)))
			return sjme_error_default(error);

	/* Close any class information. */
	classInfos = library->classInfos;
	if (classInfos != NULL)
	{
		/* Close each library item. */
		for (i = 0, n = classInfos->length; i < n; i++)
		{
			/* Ignore blank libraries. */
			classInfo = classInfos->elements[i];
			if (classInfo == NULL)
				continue;

			/* Close the library. */
			SJME_SIMPLE_CLOSE(classInfos->elements[i]);
		}

		/* Free the list. */
		SJME_SIMPLE_FREE(library->classInfos);
	}

	/* Free other allocated fields. */
	SJME_SIMPLE_FREE(library->prefix);
	SJME_SIMPLE_FREE(library->name);

	/* Stop referring to the string pool. */
	SJME_SIMPLE_CLOSE(library->stringPool);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postRomSuite(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_rom_suite suite;
	sjme_list(sjme_nvm_rom_library)* libraries;
	sjme_jint i, n;
	sjme_nvm_rom_library library;
	SJME_CLEANUP_DECL;

	/* Recover. */
	suite = (sjme_nvm_rom_suite)closeable;
	if (suite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cleanup libraries. */
	libraries = suite->libraries;
	if (libraries != NULL)
	{
		/* Close each library item. */
		for (i = 0, n = libraries->length; i < n; i++)
		{
			/* Ignore blank libraries. */
			library = libraries->elements[i];
			if (library == NULL)
				continue;

			/* Close the library. */
			SJME_SIMPLE_CLOSE(libraries->elements[i]);
		}
		
		/* Free list. */
		SJME_SIMPLE_FREE(suite->libraries);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postState(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm inState;
	sjme_nvm_bootParam* bootParam;
	sjme_nvm_task_taskNewConfig* initTask;
	sjme_list(sjme_nvm_task)* tasks;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;

	/* Recover. */
	inState = (sjme_nvm)closeable;
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cleanup all tasks. */
	tasks = inState->tasks;
	if (tasks != NULL)
	{
		/* Call close on every task. */
		for (n = tasks->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(tasks->elements[i]);
		
		/* Free tasks. */
		SJME_SIMPLE_FREE(inState->tasks);
	}

	/* Boot parameters? */
	bootParam = (sjme_nvm_bootParam*)inState->bootParamCopy;
	if (bootParam != NULL)
	{
		SJME_FLAGGED_FREE(bootParam->freeMainClassPathById,
			bootParam->mainClassPathById);
		SJME_FLAGGED_FREE(bootParam->freeMainClassPathByName,
			bootParam->mainClassPathByName);
		SJME_FLAGGED_FREE(bootParam->freeMainClassPathByName,
			bootParam->mainClassPathByName);
		SJME_FLAGGED_FREE(bootParam->freeMainArgs,
			bootParam->mainArgs);
		SJME_FLAGGED_FREE(bootParam->freeSysProps,
			bootParam->sysProps);
		
		/* Free the outer structure. */
		SJME_SIMPLE_FREE(inState->bootParamCopy);
	}

	/* Initial task configuration? */
	initTask = (sjme_nvm_task_taskNewConfig*)inState->initTaskConfig;
	if (initTask != NULL)
	{
		/* The other fields are direct references to bootParamCopy. */
		/* initTask->classPath */
		/* initTask->sysProps */
		/* initTask->mainArgs */
		
		/* Count down class loader. */
		SJME_SIMPLE_CLOSE(initTask->classLoader);

		/* Free self. */
		SJME_SIMPLE_FREE(inState->initTaskConfig);
	}

	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postStringPool(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_stringPool pool;
	sjme_list(sjme_nvm_stringPool_string)* strings;
	sjme_nvm_stringPool_string string;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;

	/* Recover. */
	pool = (sjme_nvm_stringPool)closeable;
	if (pool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Clear any strings. */
	strings = pool->strings;
	if (strings != NULL)
	{
		for (n = strings->length, i = 0; i < n; i++)
		{
			/* Skip any blank slots. */
			string = strings->elements[i];
			if (string == NULL)
				continue;

			/* Close each string. */
			SJME_SIMPLE_CLOSE(strings->elements[i]);
		}

		/* Free list. */
		SJME_SIMPLE_FREE(pool->strings);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postStringPoolString(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_charSeq seq;
	sjme_nvm_stringPool_string string;
	
	/* Recover. */
	string = (sjme_nvm_stringPool_string)closeable;
	if (string == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Delete the character sequence. */
	seq = string->seq;
	if (seq != NULL)
	{
		string->seq = NULL;
		if (sjme_error_is(error = sjme_charSeq_delete(seq)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postTaskStrings(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_taskStrings taskStrings;
	sjme_list(sjme_jstring)* interns;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	taskStrings = (sjme_nvm_taskStrings)closeable;
	if (taskStrings == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Free interned strings. */
	interns = taskStrings->interns;
	if (interns != NULL)
	{
		/* Free each one. */
		for (n = interns->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(interns->elements[i]);
		
		/* Free the list. */
		SJME_SIMPLE_FREE(taskStrings->interns);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postVmClassLoader(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_vmClass_loader classLoader;
	sjme_list(sjme_nvm_rom_library)* classPath;
	sjme_list(sjme_jclass)* classes;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	classLoader = (sjme_nvm_vmClass_loader)closeable;
	if (classLoader == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Close loaded classes. */
	classes = classLoader->classes;
	if (classes != NULL)
	{
		/* Close each class. */
		for (n = classes->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(classes->elements[i]);
		
		/* Free class list. */
		SJME_SIMPLE_FREE(classLoader->classes);
	}

	/* Close all used libraries in the classpath. */
	classPath = classLoader->classPath;
	if (classPath != NULL)
	{
		/* Close each library. */
		for (n = classPath->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(classPath->elements[i]);
		
		/* Free classpath list. */
		SJME_SIMPLE_FREE(classLoader->classPath);
	}
	
	/* Cleanup null strings. */
	SJME_SIMPLE_CLOSE(classLoader->nullStrings);

	/* Stop referring to the state. */
	SJME_SIMPLE_CLOSE(classLoader->inState);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_jboolean sjme_nvm_cleanup_typeIsObject(
	sjme_attrInValue sjme_nvm_structType inType)
{
	switch (inType)
	{
			/* Yes. */
		case SJME_NVM_STRUCT_ARRAY_INSTANCE:
		case SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE:
		case SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE:
		case SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE:
		case SJME_NVM_STRUCT_CLASS_INSTANCE:
		case SJME_NVM_STRUCT_OBJECT_INSTANCE:
		case SJME_NVM_STRUCT_STRING_INSTANCE:
		case SJME_NVM_STRUCT_THREAD_INSTANCE:
		case SJME_NVM_STRUCT_WEAK_INSTANCE:
			return SJME_JNI_TRUE;

			/* No. */
		default:
			return SJME_JNI_FALSE;
	}
}

sjme_errorCode sjme_nvm_allocR(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_closeable_closeHandlerFunc postClose;
	sjme_nvm_common result;
	sjme_alloc_pool allocPool;
	
	if (inState == NULL || outCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inType <= SJME_NVM_STRUCT_UNKNOWN ||
		inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* This is an error, likely sizeof(result) instead of sizeof(*result). */ 
	if (allocSize < sizeof(sjme_nvm_commonBase))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover pool, some types can use an aliased pool. */
	if (((sjme_alloc_pool)inState)->magic == SJME_ALLOC_POOL_MAGIC)
		allocPool = (sjme_alloc_pool)inState;
	else if (sjme_nvm_isAR(inState, SJME_NVM_STRUCT_STATE))
		allocPool = inState->allocPool;
	else
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Is a specific pre-close handler being used? */
	postClose = NULL;
	switch (inType)
	{
		case SJME_NVM_STRUCT_IS_CLASSES:
			postClose = sjme_nvm_cleanup_postIsClasses;
			break;
		
		case SJME_NVM_STRUCT_ROM_LIBRARY:
			postClose = sjme_nvm_cleanup_postRomLibrary;
			break;
		
		case SJME_NVM_STRUCT_ROM_SUITE:
			postClose = sjme_nvm_cleanup_postRomSuite;
			break;
		
		case SJME_NVM_STRUCT_STATE:
			postClose = sjme_nvm_cleanup_postState;
			break;

		case SJME_NVM_STRUCT_STRING_POOL:
			postClose = sjme_nvm_cleanup_postStringPool;
			break;

		case SJME_NVM_STRUCT_STRING_POOL_STRING:
			postClose = sjme_nvm_cleanup_postStringPoolString;
			break;

		case SJME_NVM_STRUCT_TASK_STRINGS:
			postClose = sjme_nvm_cleanup_postTaskStrings;
			break;
			
		case SJME_NVM_STRUCT_VM_CLASS_LOADER:
			postClose = sjme_nvm_cleanup_postVmClassLoader;
			break;
		
			/* Use object cleanup or nothing at all? */
		default:
			if (sjme_nvm_cleanup_typeIsObject(inType))
				postClose = sjme_nvm_cleanup_postObject;
			break;
	}
	
	/* Allocate result. */
	result = NULL;
#if defined(SJME_CONFIG_DEBUG)
	if (sjme_error_is(error = sjme_closeable_allocR(allocPool,
		allocSize, sjme_nvm_cleanup_close, SJME_JNI_TRUE,
		SJME_AS_CLOSEABLEP(&result), file, line, func)) ||
		result == NULL)
#else
	if (sjme_error_is(error = sjme_closeable_alloc(allocPool,
		allocSize, sjme_nvm_cleanup_close, SJME_JNI_TRUE,
		SJME_AS_CLOSEABLEP(&result))) || result == NULL)
#endif
		return sjme_error_default(error);
	
	/* Set fields. */
	result->type = inType;
	result->magic = SJME_NVM_OBJECT_MAGIC;
	result->postClose = postClose;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		"GC ALLC: %d:%p (%d)", inType, result, allocSize);
#endif
	
	/* Success! */
	*outCommon = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_isA(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jboolean* outResult)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_nvm_common common;
	
	if (outResult == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType != SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE &&
		(inType <= SJME_NVM_STRUCT_UNKNOWN || inType >= SJME_NVM_NUM_STRUCT))
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Null input is always nothing. */
	if (inWhat == NULL)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* All NVM objects are weakly referenced. */
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakRefGet(inWhat, &weak)) || weak == NULL)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* Must be the type and the magic must be valid! */
	/* Aliases of object types match objects as well. */
	common = inWhat;
	if (common->magic != SJME_NVM_OBJECT_MAGIC)
		*outResult = SJME_JNI_FALSE;
	else if (common->type == inType ||
		(inType == SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE &&
			sjme_nvm_cleanup_typeIsObject(common->type)))
		*outResult = SJME_JNI_TRUE;
	else
		*outResult = SJME_JNI_FALSE;
		
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_nvm_isAR(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType)
{
	sjme_jboolean result;
	
	/* Forward call. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_nvm_isA(inWhat, inType, &result)))
		return SJME_JNI_FALSE;

	/* Was this the type? */
	return result;
}

sjme_nvm_structType sjme_nvm_typeOf(
	sjme_attrInNullable sjme_pointer inWhat)
{
	sjme_alloc_weak weak;
	sjme_nvm_common common;

	/* Null is invalid. */
	if (inWhat == NULL)
		return SJME_NVM_STRUCT_UNKNOWN;
	
	/* All NVM objects are weakly referenced. */
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakRefGet(inWhat, &weak)) || weak == NULL)
		return SJME_NVM_STRUCT_UNKNOWN;

	/* Return the type of instance this is. */
	common = inWhat;
	return common->type;
}

