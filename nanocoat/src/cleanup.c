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

#define SJME_CHARSEQ_DELETE_ATOMIC(ptr) \
	SJME_CLEANUP_OP(temp = sjme_atomic_gP(sjme_charSeq, 0, (ptr)), \
		sjme_atomic_sP(sjme_charSeq, 0, (ptr), NULL), \
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

#define SJME_SIMPLE_FREE_ATOMIC(type, numPointerStars, ptr) \
	SJME_CLEANUP_OP(temp = sjme_atomic_gP(type, numPointerStars, (ptr)), \
		sjme_atomic_sP(type, numPointerStars, (ptr), NULL), \
		sjme_alloc_free(temp))

#define SJME_FLAGGED_CLOSE(free, ptr) \
	do { if ((free) && (ptr) != NULL) \
	{ \
		temp = (sjme_pointer)(ptr); \
		(ptr) = NULL; \
		if (sjme_error_is(error = sjme_closeable_close(\
			SJME_AS_CLOSEABLE(temp)))) \
			return sjme_error_default(error); \
	} } while (0)

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
	sjme_nvm_common common;
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
#if defined(SJME_CONFIG_CODE_SHOULD_WORK)
				/* The constant pool and pool entries are fine to do a */
				/* normal walk since they mostly are constant or point */
				/* to string pool strings. */
			case SJME_NVM_STRUCT_POOL:
			case SJME_NVM_WALK_PSEUDO_POOL_ENTRY:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_CLASS:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_DOUBLE:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_FLOAT:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_INTEGER:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_LONG:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_MEMBER:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_NAME_AND_TYPE:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_STRING:
			case SJME_NVM_WALK_PSEUDO_POOL_TYPE_UTF:
				/* However, if these are phantom reference, do not clean. */
				if (at->isPhantom)
					at->noDive = SJME_JNI_TRUE;
				break;
#endif
			
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
		/* Potentially eligible NVM structure that can be cleaned? */
		/* Never clean phantom pointers, as those often point back to a */
		/* parent structure. */
		/* We also cannot handle non-pointer types, or wrapped types. */
		if (!at->isPhantom && at->isPointer && at->variantStep == NULL &&
			at->typeId.i > SJME_NVM_STRUCT_UNKNOWN)
		{
			/* Skip base struct operations. */
			if (at->valueP.value == at->baseStruct.value)
				return SJME_ERROR_NONE;
			
			/* Recover the common item. */
			common = sjme_atomic_g(sjme_nvm_common,
				at->valueP.atomicNvmCommon);
			
			/* Count down any object types. */
			if (common != NULL && sjme_nvm_isAR(common,
				SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
			{
				/* Count down. */
				SJME_COUNT_DOWN_ATOMIC_NAT(sjme_jobject, 0,
					at->valueP.atomicObject);
			}

			/* Close anything else. */
			else if (common != NULL)
			{
				/* Normal close. */
				SJME_SIMPLE_CLOSE_ATOMIC_NAT(sjme_nvm_common, 0,
					at->valueP.atomicNvmCommon);
			}
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

static sjme_errorCode sjme_nvm_cleanup_postAnnotation(
	sjme_attrInNotNull sjme_nvm_class_annotation annotation)
{
	SJME_CLEANUP_DECL;
	
	if (annotation == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Close various fields. */
	SJME_SIMPLE_CLOSE(annotation->className);
	SJME_SIMPLE_CLOSE(annotation->fieldName);
	SJME_SIMPLE_CLOSE(annotation->refClass);
	SJME_SIMPLE_CLOSE(annotation->valueString);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postCodeInfo(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_class_codeInfo info;
	sjme_jint i;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	info = (sjme_nvm_class_codeInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Free the per type info local map. */
	SJME_SIMPLE_FREE_ATOMIC(sjme_pointer, 0, &info->localMapBase);
	for (i = 0; i < SJME_NVM_CODE_INFO_NUM_TYPE_IDS; i++)
		info->perType[i].localMap = NULL;

	/* Free exception list. */
	SJME_SIMPLE_FREE(info->exceptions);

	/* Free the byte code. */
	SJME_SIMPLE_FREE(info->rawCode);
	
	/* Success! */
	return SJME_ERROR_NONE;
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

static sjme_errorCode sjme_nvm_cleanup_postMemberId(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jmemberID id;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	id = (sjme_jmemberID)closeable;
	if (id == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cleanup name and type. */
	SJME_SIMPLE_CLOSE(id->name);
	SJME_SIMPLE_CLOSE(id->type);

	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postFieldId(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jfieldID id;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	id = (sjme_jfieldID)closeable;
	if (id == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Free base member details. */
	return sjme_nvm_cleanup_postMemberId(closeable);
}

static sjme_errorCode sjme_nvm_cleanup_postFrame(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_frame frame;
	sjme_jbracketTrace trace;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	frame = (sjme_nvm_frame)closeable;
	if (frame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Clear any left-over object/class/method references. */
	SJME_SIMPLE_CLOSE(frame->inClass);
	SJME_SIMPLE_CLOSE(frame->inMethod);
	SJME_SIMPLE_CLOSE(frame->pool);
	SJME_SIMPLE_CLOSE(frame->inCode);
	SJME_SIMPLE_CLOSE(frame->instance);

	/* Even though the trace point is a phantom, we want to try clearing it */
	/* as it can be made by the debugger or stack traces. */
	trace = sjme_atomic_g(sjme_jbracketTrace, &frame->phantomTracePoint);
	if (sjme_nvm_isAR(trace, SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE))
		SJME_SIMPLE_CLOSE_ATOMIC(sjme_jbracketTrace, 0,
			frame->phantomTracePoint);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postMethodId(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jmethodID id;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	id = (sjme_jmethodID)closeable;
	if (id == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Close referred to method information. */
	for (i = 0; i < SJME_NVM_NUM_METHOD_CALL_TYPE; i++)
		SJME_SIMPLE_CLOSE(id->info[i]);

	/* Free base member details. */
	return sjme_nvm_cleanup_postMemberId(closeable);
}

static sjme_errorCode sjme_nvm_cleanup_postMethodInfo(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_class_methodInfo methodInfo;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	methodInfo = (sjme_nvm_class_methodInfo)closeable;
	if (methodInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Method arguments. */
	SJME_SIMPLE_FREE(methodInfo->argT);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postArray(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jarray array;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	array = (sjme_jarray)closeable;
	if (array == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Only objects need to be GCed, we can set field items to NULL. */
	if (array->e.type == SJME_JAVA_TYPE_ID_OBJECT)
		for (n = array->e.length, i = 0; i < n; i++)
			if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
				&array->e, i, NULL, SJME_VLS_JOBJECT(NULL))))
				return sjme_error_default(error);

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
	SJME_CHARSEQ_DELETE(classy->fieldName);

	/* Free the static chunk, which contains field storage. */
	SJME_SIMPLE_FREE(classy->staticChunk);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postString(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jstring string;
	sjme_nvm_stringPool_string poolString;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	string = (sjme_jstring)closeable;
	if (string == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Strings have one of two possible states, either they use a character */
	/* sequence from a string pool, or they are dynamically allocated. */
	poolString = sjme_atomic_g(sjme_nvm_stringPool_string,
		&string->poolString);
	if (poolString != NULL)
	{
		/* Destroy reference to normal sequence, so if this ever runs again */
		/* it will never get freed. */
		sjme_atomic_s(sjme_charSeq, &string->seq, NULL);

		/* Close the pool string reference. */
		SJME_SIMPLE_CLOSE_ATOMIC(sjme_nvm_stringPool_string, 0,
			string->poolString);
	}

	/* This is a normal sequence allocation. */
	else
	{
		/* Delete the character sequence. */
		SJME_CHARSEQ_DELETE_ATOMIC(&string->seq);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postThread(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_thread thread;
	sjme_jint i, n;
	sjme_list_sjme_nvm_frame* frames;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	thread = (sjme_nvm_thread)closeable;
	if (thread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Clear all frames. */
	frames = thread->frames;
	if (frames != NULL)
	{
		/* Close any allocated frames. */
		for (n = frames->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(frames->elements[i]);
		
		/* Free frame list. */
		SJME_SIMPLE_FREE(thread->frames);
	}

	/* Free stack storage. */
	SJME_SIMPLE_FREE(thread->stack.storage);

	/* Free tossed object, if any. */
	SJME_SIMPLE_CLOSE_ATOMIC(sjme_jobject, 0, thread->tossed);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postClassInfo(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_class_info info;
	sjme_list(sjme_nvm_stringPool_string)* interfaceNames;
	sjme_list(sjme_nvm_class_fieldInfo)* fields;
	sjme_list(sjme_nvm_class_methodInfo)* methods;
	sjme_list(sjme_nvm_class_annotation)* annotations;
	sjme_nvm_class_annotation annotation;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	info = (sjme_nvm_class_info)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Close interface names. */
	interfaceNames = info->interfaceNames;
	if (interfaceNames != NULL)
	{
		/* Close items. */
		for (n = interfaceNames->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(interfaceNames->elements[i]);

		/* Free list. */
		SJME_SIMPLE_FREE(info->interfaceNames);
	}

	/* Close fields. */
	fields = info->fields;
	if (fields != NULL)
	{
		/* Close items. */
		for (n = fields->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(fields->elements[i]);

		/* Free list. */
		SJME_SIMPLE_FREE(info->fields);
	}

	/* Close methods. */
	methods = info->methods;
	if (methods != NULL)
	{
		/* Close items. */
		for (n = methods->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(methods->elements[i]);

		/* Free list. */
		SJME_SIMPLE_FREE(info->methods);
	}

	/* Class file name. */
	SJME_SIMPLE_FREE(info->fileName);
	
	/* Annotations. */
	annotations = info->annotations;
	if (annotations != NULL)
	{
		/* Free individual items. */
		for (n = annotations->length, i = 0; i < n; i++)
		{
			/* Clear list item. */
			annotation = annotations->elements[i];
			annotations->elements[i] = NULL;
			
			/* Cleanup. */
			if (annotation != NULL)
			{
				/* Internal free. */
				if (sjme_error_is(error = sjme_nvm_cleanup_postAnnotation(
					annotation)))
					return sjme_error_default(error);
				
				/* Self free. */
				SJME_SIMPLE_FREE(annotation);
			}
		}

		/* Free list. */
		SJME_SIMPLE_FREE(info->annotations);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postObject(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_jobject object, ref;
	sjme_jclass isClass, atClass;
	sjme_jint i, n;
	sjme_nvm_jclass_fields* fields;
	sjme_nvm_valueSet* valueSet;
	sjme_nvm_value* value;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	object = (sjme_jobject)closeable;
	if (object == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cleanup when there is a valid class. */
	isClass = sjme_atomic_g(sjme_jclass, &object->isClass);
	for (atClass = isClass; atClass != NULL;
		atClass = sjme_atomic_g(sjme_jclass, &atClass->superClass))
	{
		/* Class was GCed before the object? This is not something that */
		/* should occur! Can be ignored, if a class. */
		/* Strings are special objects, so they can be skipped. */
		/* Threads are special objects as well. */
		if (object->common.type != SJME_NVM_STRUCT_CLASS_INSTANCE &&
			object->common.type != SJME_NVM_STRUCT_STRING_INSTANCE &&
			object->common.type != SJME_NVM_STRUCT_BRACKET_VM_THREAD_INSTANCE)
			if (atClass->fieldName == NULL || atClass->info == NULL)
				return sjme_error_vmError(NULL, SJME_ERROR_OBJECT_GONE);
		
		/* We need to clean up all the instance fields for this object, */
		/* however at this point it is possible for the field binds to be */
		/* invalidated, so directly access the fields. */
		fields = &isClass->fields[SJME_NVM_CLASS_MEMBER_INSTANCE];
		
		/* Are there any actual objects to free? */
		n = fields->count[SJME_JAVA_TYPE_ID_OBJECT];
		if (n <= 0)
			continue;
		
		/* Recover the raw field structure. */
		valueSet = SJME_POINTER_OFFSET(object,
			fields->offset[SJME_JAVA_TYPE_ID_OBJECT]);
		
		/* Sanity check. */
		if (n != valueSet->length)
			return sjme_error_vmError(NULL, SJME_ERROR_ILLEGAL_STATE);
		
		/* Cleanup all object fields. */
		for (i = 0; i < n; i++)
			if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
				valueSet, i, NULL, SJME_VLS_JOBJECT(NULL))))
				return sjme_error_default(error);
	}

	/* Class specific cleanup? */
	if (object->common.type == SJME_NVM_STRUCT_ARRAY_INSTANCE)
	{
		if (sjme_error_is(error = sjme_nvm_cleanup_postArray(closeable)))
			return sjme_error_default(error);
	}
	else if (object->common.type == SJME_NVM_STRUCT_CLASS_INSTANCE)
	{
		if (sjme_error_is(error = sjme_nvm_cleanup_postClass(closeable)))
			return sjme_error_default(error);
	}
	else if (object->common.type == SJME_NVM_STRUCT_STRING_INSTANCE)
	{
		if (sjme_error_is(error = sjme_nvm_cleanup_postString(closeable)))
			return sjme_error_default(error);
	}
	else if (object->common.type == SJME_NVM_STRUCT_BRACKET_VM_THREAD_INSTANCE)
	{
		if (sjme_error_is(error = sjme_nvm_cleanup_postThread(closeable)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postPool(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_class_poolInfo pool;
	sjme_list(sjme_nvm_class_poolEntry)* entries;
	sjme_nvm_class_poolEntry* entry;
	sjme_jint i, n;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	pool = (sjme_nvm_class_poolInfo)closeable;
	if (pool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cleanup any pool entries. */
	entries = pool->pool;
	if (entries != NULL)
	{
		/* Cleanup any sub-entry. */
		for (n = entries->length, i = 0; i < n; i++)
		{
			entry = &entries->elements[i];
			switch (entry->type)
			{
					/* UTF entries. */
				case SJME_NVM_CLASS_POOL_TYPE_UTF:
					SJME_SIMPLE_CLOSE(entry->utf.utf);
					break;

					/* Class reference. */
				case SJME_NVM_CLASS_POOL_TYPE_CLASS:
					SJME_SIMPLE_CLOSE(entry->classRef.descriptor);
					break;

					/* Name and type. */
				case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
					SJME_SIMPLE_CLOSE(entry->nameAndType.name);
					SJME_SIMPLE_CLOSE(entry->nameAndType.descriptor);
					break;

					/* String. */
				case SJME_NVM_CLASS_POOL_TYPE_STRING:
					SJME_SIMPLE_CLOSE(entry->constString.value);
					break;

					/* No special cleanup needed. */
				default:
					break;
			}
		}
		
		/* Free the final list. */
		SJME_SIMPLE_FREE(pool->pool);
	}

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
	SJME_SIMPLE_FREE(library->name);

	/* Stop referring to the string pool. */
	SJME_SIMPLE_CLOSE(library->stringPool);

	/* Call main close on the library. */
	if (library->functions != NULL && library->functions->close != NULL)
		if (sjme_error_is(error = library->functions->close(library)))
			return sjme_error_default(error);

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
		/* Close each library. */
		for (n = libraries->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(libraries->elements[i]);
		
		/* Free list. */
		SJME_SIMPLE_FREE(suite->libraries);
	}

	/* Call main close on the suite. */
	if (suite->functions != NULL && suite->functions->close != NULL)
		if (sjme_error_is(error = suite->functions->close(suite)))
			return sjme_error_default(error);

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
	sjme_list_sjme_nvm_rom_library* classPath;
	sjme_nvm_threadSchedule* schedule;
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

	/* Cleanup schedules. */
	schedule = inState->schedule;
	if (schedule != NULL)
	{
		/* Go through each model. */
		for (i = 0; i < SJME_NVM_THREAD_NUM_SCHEDULE_MODE; i++)
			SJME_SIMPLE_FREE(schedule->mode[i].order);
		
		/* Free the schedule data. */
		SJME_SIMPLE_FREE(inState->schedule);
	}

	/* Boot parameters? */
	bootParam = (sjme_nvm_bootParam*)inState->bootParamCopy;
	if (bootParam != NULL)
	{
		/* Boot suite and library. */
		SJME_FLAGGED_CLOSE(bootParam->freeBootSuite,
			bootParam->bootSuite);
		SJME_FLAGGED_CLOSE(bootParam->freeLibrarySuite,
			bootParam->librarySuite);
		
		/* Main application. */
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

		/* Close an extra handle? */
		SJME_SIMPLE_CLOSE(bootParam->extraCloseHandle);
		
		/* Free the outer structure. */
		SJME_SIMPLE_FREE(inState->bootParamCopy);
	}

	/* Initial task configuration? */
	initTask = (sjme_nvm_task_taskNewConfig*)inState->initTaskConfig;
	if (initTask != NULL)
	{
		/* Clear the initial task classpath. */
		classPath = initTask->classPath;
		if (classPath != NULL)
		{
			/* Close each classpath entry. */
			for (n = classPath->length, i = 0; i < n; i++)
				SJME_SIMPLE_CLOSE(classPath->elements[i]);
			
			/* Clear the final list. */
			SJME_SIMPLE_FREE(initTask->classPath);
		}

		/* The other fields are direct references to bootParamCopy. */
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
	sjme_list(sjme_phantom(sjme_nvm_stringPool_string))* strings;
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
#if defined(SJME_CONFIG_HAS_BROKEN_CODE)
		/* Close each string. */
		for (n = strings->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE_ATOMIC(sjme_nvm_stringPool_string, 0,
				strings->elements[i]);
#endif

		/* Free list. */
		SJME_SIMPLE_FREE(pool->strings);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postStringPoolString(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_stringPool_string string;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	string = (sjme_nvm_stringPool_string)closeable;
	if (string == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Delete the character sequence. */
	SJME_CHARSEQ_DELETE(string->seq);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_cleanup_postTask(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_task task;
	sjme_list(sjme_nvm_thread)* threads;
	sjme_list(sjme_jstring)* mainArgs;
	sjme_list(sjme_jbracketJarPackage)* jarBrackets;
	sjme_nvm_task_globals* globals;
	sjme_jint i, n;
	sjme_jclass nukeClass;
	SJME_CLEANUP_DECL;
	
	/* Recover. */
	task = (sjme_nvm_task)closeable;
	if (task == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cleanup threads. */
	threads = task->threads;
	if (threads != NULL)
	{
		/* Free each thread. */
		for (n = threads->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(threads->elements[i]);
		
		/* Free list. */
		SJME_SIMPLE_FREE(task->threads);
	}

	/* Free the init config copy. */
	SJME_SIMPLE_FREE(task->initConfig);

	/* Lock globals. */
	globals = &task->globals;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&globals->lock)))
		goto fail_lockGlobals;

	/* Close each standard pipe. */
	for (i = 0; i < SJME_NVM_MLE_NUM_STD_PIPES; i++)
		SJME_SIMPLE_CLOSE(globals->stdPipes[i]);

	/* Close each cached Jar bracket. */
	jarBrackets = globals->jarBrackets;
	if (jarBrackets != NULL)
	{
		/* Close each item. */
		for (n = jarBrackets->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(jarBrackets->elements[i]);
		
		/* Free the list. */
		SJME_SIMPLE_FREE(jarBrackets);
	}

	/* Close main arguments. */
	mainArgs = globals->mainArgs;
	if (mainArgs != NULL)
	{
		/* Free each argument. */
		for (n = mainArgs->length, i = 0; i < n; i++)
			SJME_SIMPLE_CLOSE(mainArgs->elements[i]);
		
		/* Free list. */
		SJME_SIMPLE_FREE(globals->mainArgs);
	}
	
	/* Uncount common classes, at least once. */
	for (i = SJME_NVM_COMMON_VERY_IMPORTANT;
		i < SJME_NVM_TASK_NUM_COMMON_CLASS; i++)
	{
		/* Try basic close on the class. */
		nukeClass = sjme_atomic_g(sjme_jclass, &globals->commonClasses[i]);
		SJME_SIMPLE_CLOSE_ATOMIC_NAT(sjme_jclass, 0,
			&globals->commonClasses[i]);
	}

	/* Forcefully GC each common class. */
	for (i = 0; i < SJME_NVM_TASK_NUM_COMMON_CLASS; i++)
	{
		/* Grab the class here again. */
		nukeClass = sjme_atomic_g(sjme_jclass, &globals->commonClasses[i]);

		/* Class was already fully GCed? */
		if (nukeClass == NULL || !sjme_nvm_isAR(nukeClass,
			SJME_NVM_STRUCT_CLASS_INSTANCE))
			continue;
		
		/* Count down until it is destroyed (and still a class). */
		while (sjme_alloc_weakRefLeftR(nukeClass) >= 0 &&
			sjme_nvm_isAR(nukeClass, SJME_NVM_STRUCT_CLASS_INSTANCE))
		{
#if defined(SJME_CONFIG_DEBUG_GC)
			sjme_message("Force GC: %d:%p (%s)",
				nukeClass->object.common.type, nukeClass,
				sjme_charSeq_tempUtf(nukeClass->fieldName));
#endif
			
			/* Count it down. */
			if (sjme_error_is(error = sjme_nvm_instance_countDown(
				SJME_AS_JOBJECT(nukeClass))))
				goto fail_nukeClass;
		}
	}

	/* Unlock globals. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&globals->lock,
		NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_nukeClass:
fail_lockGlobals:
	/* Clear lock before failing. */
	sjme_thread_spinLockRelease(&task->globals.lock, NULL);

	/* Fail. */
	return sjme_error_default(error);
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
	sjme_jclass single;
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
		/* Forcefully close each class. */
		for (n = classes->length, i = 0; i < n; i++)
		{
			/* Keep a reference before closing, so we can check counts. */
			single = classes->elements[i];
			SJME_SIMPLE_CLOSE(classes->elements[i]);
			
			/* Forcefully count down the class to clean it up. */
			while (sjme_alloc_weakRefLeftR(single) >= 0 &&
				sjme_nvm_isAR(single, SJME_NVM_STRUCT_CLASS_INSTANCE))
				if (sjme_error_is(error = sjme_nvm_instance_countDown(
					SJME_AS_JOBJECT(single))))
					return sjme_error_default(error);
		}
		
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
		case SJME_NVM_STRUCT_BRACKET_VM_THREAD_INSTANCE:
		case SJME_NVM_STRUCT_CLASS_INSTANCE:
		case SJME_NVM_STRUCT_OBJECT_INSTANCE:
		case SJME_NVM_STRUCT_STRING_INSTANCE:
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
		case SJME_NVM_STRUCT_CLASS_INFO:
			postClose = sjme_nvm_cleanup_postClassInfo;
			break;

		case SJME_NVM_STRUCT_CODE_INFO:
			postClose = sjme_nvm_cleanup_postCodeInfo;
			break;
			
		case SJME_NVM_STRUCT_IS_CLASSES:
			postClose = sjme_nvm_cleanup_postIsClasses;
			break;

		case SJME_NVM_STRUCT_FIELD_ID:
			postClose = sjme_nvm_cleanup_postFieldId;
			break;

		case SJME_NVM_STRUCT_FRAME:
			postClose = sjme_nvm_cleanup_postFrame;
			break;

		case SJME_NVM_STRUCT_METHOD_ID:
			postClose = sjme_nvm_cleanup_postMethodId;
			break;

		case SJME_NVM_STRUCT_METHOD_INFO:
			postClose = sjme_nvm_cleanup_postMethodInfo;
			break;

		case SJME_NVM_STRUCT_POOL:
			postClose = sjme_nvm_cleanup_postPool;
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

		case SJME_NVM_STRUCT_TASK:
			postClose = sjme_nvm_cleanup_postTask;
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
	if (sjme_error_is(error = sjme_closeable_allocR(allocPool,
		allocSize, sjme_nvm_cleanup_close,
		SJME_AS_CLOSEABLEP(&result)
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
		result == NULL)
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

