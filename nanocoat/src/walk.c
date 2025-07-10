/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/walk.h"
#include "sjme/nvm/nvm.h"

#define SJME_WALK_SELECT(cType, inStructType) \
	{ \
		sjme_sm(.typeName, SJME_TOKEN_STRING_PP(cType)), \
		sjme_sm(.typeId, inStructType), \
		sjme_sm(.size, sizeof(cType)), \
		sjme_sm(.steps, SJME_TOKEN_PASTE_PP(sjme_nvm_walk_decl_, \
			cType)), \
	}

#define SJME_WALK_SELECT_END() \
	{ \
		sjme_sm(.typeName, NULL), \
		sjme_sm(.typeId, 0), \
		sjme_sm(.size, -1), \
		sjme_sm(.steps, NULL), \
	}

/** Begin walk structure. */
#define SJME_WALK_BEGIN(structType) \
	const sjme_nvm_walk_step SJME_TOKEN_PASTE_PP(sjme_nvm_walk_decl_, \
		SJME_WALK_CURRENT)[] = \
	{ \

/** Walk step, full definition. */
#define SJME_WS_FULL(inMemberName, inIsPointer, \
	inJavaType, inNvmId, inIsList) \
	{ \
		sjme_sm(.offset, offsetof(SJME_WALK_CURRENT, inMemberName)), \
		sjme_sm(.memberName, #inMemberName), \
		sjme_sm(.isPointer, inIsPointer), \
		sjme_sm(.isList, inIsList), \
		sjme_sm(.size, sizeof(((SJME_WALK_CURRENT*)(0))->inMemberName)), \
		sjme_sm(.javaType, inJavaType), \
		sjme_sm(.structType, inNvmId), \
	}

/** Walk step a primitive value (pointer). */
#define SJME_WS_PRIM_P(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, javaType, SJME_NVM_NUM_STRUCT, SJME_JNI_FALSE)

/** Walk step a primitive list of pointer values (pointer). */
#define SJME_WS_PRIM_PL(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, javaType, SJME_NVM_NUM_STRUCT, SJME_JNI_TRUE)

/** Walk step a primitive value (value). */
#define SJME_WS_PRIM_V(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, javaType, SJME_NVM_NUM_STRUCT, SJME_JNI_FALSE)

/** Walk step a primitive list values (value). */
#define SJME_WS_PRIM_VL(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, javaType, SJME_NVM_NUM_STRUCT, SJME_JNI_TRUE)

/** Walk step another structure type (pointer). */
#define SJME_WS_STRUCT_P(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, structType, SJME_JNI_FALSE)

/** Walk step another structure type as list (pointer). */
#define SJME_WS_STRUCT_PL(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, structType, SJME_JNI_TRUE)

/** Walk step another structure type (value). */
#define SJME_WS_STRUCT_V(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, structType, SJME_JNI_FALSE)

/** Walk step another structure type as list (value). */
#define SJME_WS_STRUCT_VL(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, structType, SJME_JNI_TRUE)

/** End walk structure. */
#define SJME_WALK_END() \
		{ \
			sjme_sm(.offset, -1), \
			sjme_sm(.memberName, NULL), \
			sjme_sm(.isPointer, SJME_JNI_FALSE), \
			sjme_sm(.isList, SJME_JNI_FALSE), \
			sjme_sm(.size, -1), \
			sjme_sm(.javaType, 0), \
			sjme_sm(.structType, 0), \
		} \
	}

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

#define SJME_WALK_CURRENT sjme_nvm_stateBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_STATE)
	SJME_WS_STRUCT_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_STRUCT_V(allocPool, SJME_NVM_WALK_ALLOC_POOL),
	SJME_WS_STRUCT_P(bootParamCopy, SJME_NVM_WALK_BOOT_PARAM),
	SJME_WS_STRUCT_P(hooks, SJME_NVM_WALK_STATE_HOOKS),
	SJME_WS_STRUCT_P(nal, SJME_NVM_WALK_NAL),
	SJME_WS_STRUCT_V(suite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_STRUCT_VL(tasks, SJME_NVM_STRUCT_TASK),
	SJME_WS_STRUCT_V(numRunningTasks, SJME_NVM_WALK_ATOMIC_JINT),
	SJME_WS_STRUCT_V(nextTaskId, SJME_NVM_WALK_ATOMIC_JINT),
	SJME_WS_STRUCT_V(nextThreadId, SJME_NVM_WALK_ATOMIC_JINT),
	SJME_WS_STRUCT_V(threadModel, SJME_NVM_WALK_MLE_THREAD_MODEL),
	SJME_WS_STRUCT_V(schedule, SJME_NVM_WALK_THREAD_SCHEDULE),
	SJME_WS_STRUCT_V(terminating, SJME_NVM_WALK_ATOMIC_JINT),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

const sjme_nvm_walk_stepSelect sjme_nvm_walk_select[] =
{
	SJME_WALK_SELECT(sjme_nvm_stateBase, SJME_NVM_STRUCT_STATE),
	SJME_WALK_SELECT_END()
};

/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */

static const sjme_nvm_walk_stepSelect* sjme_nvm_select(
	sjme_attrInValue sjme_jint typeId)
{
	const sjme_nvm_walk_stepSelect* select;

	/* Locate the walk stepper. */
	for (select = &sjme_nvm_walk_select[0]; select->typeName != NULL; select++)
		if (typeId == select->typeId)
			return select;

	/* Not found. */
	return NULL;
}

static sjme_errorCode sjme_nvm_walk(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	const sjme_nvm_walk_stepSelect* subSelect;
	const sjme_nvm_walk_step* currentStep;
	sjme_jint atIndex;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Set base step. */
	currentStep = at->inSelect->steps;

	/* Start of structure step? */
	if (at->index <= -1)
	{
		/* This is just the current information. */
		if (sjme_error_is(error = function(root, parent, at)))
			return sjme_error_default(error);

		/* Move onto the current index. */
		at->index = 0;
	}

	/* Stepping over individual members? */
	for (atIndex = 0;; currentStep++, atIndex++)
	{
		/* Stop at the end. */
		if (currentStep->memberName == NULL ||
			currentStep->size <= 0)
			break;

		/* Locate the selection for this structure type, for recursion. */
		if (currentStep->structType != 0)
			subSelect = sjme_nvm_select(currentStep->structType);
		else
			subSelect = NULL;
		
		/* Fill in sub-step. */
		memset(&subStep, 0, sizeof(subStep));
		subStep.root = root;
		subStep.base.raw = at->base.raw;
		subStep.parent = at;
		subStep.typeId = currentStep->structType;
		subStep.inSelect = subSelect;
		subStep.functions = at->functions;
		subStep.depth = at->depth + 1;
		subStep.stage = at->stage;

		/* Determine the pointer for this value. */
		subStep.at.raw = SJME_POINTER_OFFSET(at->base.raw,
			currentStep->offset);

		/* If there is a sub-select, recursive walk into it. */
		if (subSelect != NULL)
		{
			/* Start at the base of the structure. */
			subStep.index = -1;
			
			/* Do a structured walk. */
			if (sjme_error_is(error = sjme_nvm_walk(root, at,
				&subStep, function)))
				return sjme_error_default(error);
		}

		/* Otherwise, just step on it. */
		else
		{
			/* This is just the current index. */
			subStep.index = atIndex;

			/* Perform the stepped walk. */
			if (sjme_error_is(error = function(root, at, &subStep)))
				return sjme_error_default(error);
		}
	}
	
	/* We stepped over everything, so set a very high index. */
	at->index = INT32_MAX;
	if (sjme_error_is(error = function(root, parent, at)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_walkStart(
	sjme_attrInNotNull sjme_pointer startAt,
	sjme_attrInValue sjme_jint typeId,
	sjme_attrInNotNull const sjme_nvm_walk_functions* functions)
{
	sjme_errorCode error;
	const sjme_nvm_walk_stepSelect* select;
	sjme_nvm_walk_state rootState;
	sjme_nvm_walk_stageType stage;
	sjme_nvm_walk_stepHandlerFunc function;
	
	if (startAt == NULL || functions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Not implemented? */
	if (functions->step == NULL)
		return sjme_error_notImplemented(0);

	/* Find the selector for this. */
	select = sjme_nvm_select(typeId);
	if (select == NULL)
		return SJME_ERROR_WALK_UNKNOWN_TYPE;

	/* Perform two-stage walk. */
	for (stage = 0; stage < SJME_NVM_WALK_NUM_STAGES; stage++)
	{
		/* Which function is being called? */
		function = (stage == SJME_NVM_WALK_STAGE_PRE ?
			functions->pre : functions->step);
		if (function == NULL)
			continue;
		
		/* Initialize root state. */
		memset(&rootState, 0, sizeof(rootState));
		rootState.base.raw = startAt;
		rootState.at.raw = startAt;
		rootState.typeId = typeId;
		rootState.inSelect = select;
		rootState.functions = functions;
		rootState.index = -1;
		rootState.depth = 0;
		rootState.stage = stage;
		
		/* Perform the recursive walk. */
		if (sjme_error_is(error = sjme_nvm_walk(&rootState, NULL,
			&rootState, function)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
