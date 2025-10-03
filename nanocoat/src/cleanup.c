/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sjme/nvm/instance.h>
#include <sjme/nvm/walk.h>

#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/rom.h"

/** The magic number for NVM objects. */
#define SJME_NVM_OBJECT_MAGIC UINT32_C(0x4E764D3F)

static sjme_errorCode sjme_nvm_cleanup_walkStep(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at)
{
	sjme_errorCode error;
	
	if (root == NULL || at == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("GC Walk: %s %s %p (%d)->#%d (%d)",
		(at->stage == SJME_NVM_WALK_STAGE_PRE ? "PRE" : "STEPS"),
		(at->breadth == SJME_NVM_WALK_BREADTH_LEVEL ? "LEVEL" : "DIVE"),
		at->base, at->root->typeId, at->index, at->typeId);
#endif

	/* If this is the root of the item, indicate we do not want to dive as */
	/* we just want to run over items. */
	if (at->index < 0 && at->index != INT32_MIN)
	{
		at->noDive = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}

	/* Pre-stage sub-structure recursive cleanup? */
	else if (at->stage == SJME_NVM_WALK_STAGE_PRE)
	{
		/* Only clean up objects if they are not ourselves, otherwise we */
		/* will end up garbage collecting too early. Objects get counted */
		/* down accordingly. Naturally NVM structures are always positive */
		/* type identifiers. */
		if (at->typeId >= 0 && at->valueP.value != at->baseStruct.value &&
			sjme_nvm_isAR(*at->valueP.pointer,
				SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE))
			if (sjme_error_is(error = sjme_nvm_instance_countDown(
				*at->valueP.pointer)))
				return sjme_error_default(error);
	}

	/* Normal stage self clean before de-allocation. */
	else if (at->stage == SJME_NVM_WALK_STAGE_STEPS &&
		(at->index == INT32_MIN || at->index == INT32_MAX))
	{
		/* Cleanup any sub-structures that would otherwise normally */
		/* not be cleaned up automatically. */
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

	/* Is there a pre-close handler for this? */
	if (common->preClose != NULL)
	{
		/* Call handler. */
		if (sjme_error_is(error = common->preClose(closeable)))
			return sjme_error_default(error);

		/* Clear it since it was performed. */
		common->preClose = NULL;
	}

	/* Perform a generic walk close. */
	return sjme_nvm_walk_start(common, common->type,
		&sjme_nvm_cleanup_walkFunctions, NULL);
}

sjme_errorCode sjme_nvm_allocR(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_closeable_closeHandlerFunc preClose;
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
	preClose = NULL;
	switch (inType)
	{
			/* No specific close being used. */
		default:
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
	result->preClose = preClose;

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
			(common->type == SJME_NVM_STRUCT_ARRAY_INSTANCE ||
			common->type == SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE ||
			common->type == SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE ||
			common->type == SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE ||
			common->type == SJME_NVM_STRUCT_CLASS_INSTANCE ||
			common->type == SJME_NVM_STRUCT_OBJECT_INSTANCE ||
			common->type == SJME_NVM_STRUCT_STRING_INSTANCE ||
			common->type == SJME_NVM_STRUCT_THREAD_INSTANCE ||
			common->type == SJME_NVM_STRUCT_WEAK_INSTANCE)))
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

