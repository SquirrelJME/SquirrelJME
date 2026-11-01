/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/task.h"
#include "lib/scritchui/scritchuiConst.h"

sjme_jint sjme_nvm_instance_calcIdentityHash(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrInValue void* pointer)
{
	sjme_jint base;
	
	/* Use random base PRNG from task. */
	base = 0;
	if (inTask != NULL)
		base = sjme_random_nextIntR(&inTask->globals.idHash);
	
	/* Then based on the pointer. */
#if defined(SJME_CONFIG_HAS_POINTER64)
	base += (((sjme_intPointer)pointer) ^
		((((sjme_intPointer)pointer)) >> 31));
#else
	base += (sjme_jint)((sjme_intPointer)pointer);
#endif

	/* Never allow zero to be a valid identity hash. */
	return (base == 0 ? INT32_C(0x01) : base);
}

sjme_errorCode sjme_nvm_instance_countDownR(
	sjme_attrInNotNull sjme_jobject object
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_weak weak;
	sjme_errorCode error;
#if defined(SJME_CONFIG_DEBUG_GC)
	sjme_jint oldCount;
	sjme_nvm_structType typeOf;
#endif
	
	if (object == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Must be a valid object type. */
	if (!sjme_nvm_isAR(object, SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
		return SJME_ERROR_INVALID_OBJECT;
	
	/* This must be a valid weak as well! */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(object, &weak)))
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG_GC)
	/* Get old count for debugging. */
	oldCount = sjme_atomic_g(sjme_jint, &weak->count);
	typeOf = object->common.type;
#endif
	
#if defined(SJME_CONFIG_DEBUG_NO_REAL_GC)
	if (SJME_JNI_TRUE)
		return SJME_ERROR_NONE;
#endif

	/* Reduce the count on this. */
	if (sjme_error_is(error = sjme_alloc_weakUnRef(object)) ||
		weak == NULL)
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG_GC)
	/* Debug. */
	sjme_messageR(SJME_DEBUG_FILE_LINE_COPY, SJME_JNI_FALSE,
		"GC DN-1: %d:%p (%s) %d -> %d",
		typeOf,
		object, 
		(sjme_atomic_g(sjme_jclass, &object->isClass) != NULL ?
			sjme_charSeq_tempUtf(
				sjme_atomic_g(sjme_jclass,
					&object->isClass)->fieldName) : "?"),
		oldCount,
		sjme_atomic_g(sjme_jint, &weak->count));
#endif

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_countUpR(
	sjme_attrInNotNull sjme_jobject object
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_weak weak;
	sjme_errorCode error;
#if defined(SJME_CONFIG_DEBUG_GC)
	sjme_jint oldCount;
#endif
	
	if (object == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Must be a valid object type. */
	if (!sjme_nvm_isAR(object, SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
		return SJME_ERROR_INVALID_OBJECT;
	
	/* This must be a valid weak as well! */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(object, &weak)) ||
		weak == NULL)
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG_GC)
	/* Recover old count for debugging. */
	oldCount = sjme_atomic_g(sjme_jint, &weak->count);
#endif

	/* Count up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(object, &weak)) ||
		weak == NULL)
		return sjme_error_default(error);

#if defined(SJME_CONFIG_DEBUG_GC)
	/* Debug. */
	sjme_messageR(SJME_DEBUG_FILE_LINE_COPY, SJME_JNI_FALSE,
		"GC UP+1: %d:%p (%s) %d -> %d",
		object->common.type,
		object,
		(sjme_atomic_g(sjme_jclass, &object->isClass) != NULL ?
			sjme_charSeq_tempUtf(sjme_atomic_g(sjme_jclass,
				&object->isClass)->fieldName) : "?"),
		oldCount,
		sjme_atomic_g(sjme_jint, &weak->count));
#endif

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_monitorEnter(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance)
{
	sjme_errorCode error;
	
	if (contextThread == NULL || instance == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Memory barrier! */
	sjme_atomic_barrier();

	/* Grab the lock on the object. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&instance->common.lock)))
		return sjme_error_vmError(contextThread, error);

	/* Count up the monitor since we do have the lock. */
	sjme_atomic_ga(sjme_jint, &instance->monitorCount, 1);
	
	/* Memory barrier! */
	sjme_atomic_barrier();

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_monitorExit(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance)
{
	sjme_errorCode error;
	
	if (contextThread == NULL || instance == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Memory barrier! */
	sjme_atomic_barrier();

	/* There are no monitor locks on this instance? */
	if (sjme_atomic_g(sjme_jint, &instance->monitorCount) < 0)
		return SJME_ERROR_NOT_LOCK_OWNER;
	
	/* Release the lock on the object. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&instance->common.lock, NULL)))
		return sjme_error_vmError(contextThread, error);

	/* We did a successful release, so count the locks down. */
	sjme_atomic_ga(sjme_jint, &instance->monitorCount, -1);
	
	/* Memory barrier! */
	sjme_atomic_barrier();

	/* Success! */
	return SJME_ERROR_NONE;
}
