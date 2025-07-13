/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/boot.h"
#include "sjme/nvm/walk.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/task.h"

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
	inJavaType, inNvmId, inMemberSize) \
	{ \
		sjme_sm(.offset, offsetof(SJME_WALK_CURRENT, inMemberName)), \
		sjme_sm(.memberName, #inMemberName), \
		sjme_sm(.isPointer, inIsPointer), \
		sjme_sm(.size, sizeof(((SJME_WALK_CURRENT*)(0))->inMemberName)), \
		sjme_sm(.memberSize, inMemberSize), \
		sjme_sm(.javaType, inJavaType), \
		sjme_sm(.structType, inNvmId), \
	}

/** Walk step a primitive value (pointer). */
#define SJME_WS_JAVA_P(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, javaType, SJME_NVM_WALK_PSEUDO_PRIMITIVE, -1)

/** Walk step a primitive value (value). */
#define SJME_WS_JAVA_V(memberName, javaType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, javaType, SJME_NVM_WALK_PSEUDO_PRIMITIVE, -1)

/** Walk step another structure type (pointer). */
#define SJME_WS_NORM_P(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, structType, -1)

/** Walk step another structure type (value). */
#define SJME_WS_NORM_V(memberName, structType) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, structType, -1)

/** Walk step an array value (value). */
#define SJME_WS_ARRY_V(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_FIXED_ARRAY, \
		sizeof(*((SJME_WALK_CURRENT*)(0))->memberName)), \
	subDef

/** Walk step a list value (pointer). */
#define SJME_WS_LIST_P(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_TRUE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_LIST, -1), \
	subDef

/** Walk step a list value (value). */
#define SJME_WS_LIST_V(memberName, subDef) \
	SJME_WS_FULL(memberName, \
		SJME_JNI_FALSE, SJME_NUM_JAVA_TYPE_IDS, \
		SJME_NVM_WALK_PSEUDO_LIST, -1), \
	subDef

/** End walk structure. */
#define SJME_WALK_END() \
		{ \
			sjme_sm(.offset, -1), \
			sjme_sm(.memberName, NULL), \
			sjme_sm(.isPointer, SJME_JNI_FALSE), \
			sjme_sm(.size, -1), \
			sjme_sm(.memberSize, -1), \
			sjme_sm(.javaType, 0), \
			sjme_sm(.structType, 0), \
		} \
	}

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

#define SJME_WALK_CURRENT sjme_nvm_bootParam
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_BOOT_PARAM)
	SJME_WS_NORM_P(bootSuite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_NORM_P(librarySuite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_LIST_P(mainClassPathById,
		SJME_WS_JAVA_V(mainClassPathById, SJME_JAVA_TYPE_ID_INTEGER)),
	SJME_WS_LIST_P(mainClassPathByName,
		SJME_WS_NORM_V(mainClassPathByName, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_NORM_P(mainClass, SJME_NVM_WALK_PSEUDO_LPSTR),
	SJME_WS_LIST_P(mainArgs,
		SJME_WS_NORM_V(mainArgs, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_LIST_P(sysProps,
		SJME_WS_NORM_V(sysProps, SJME_NVM_WALK_PSEUDO_LPSTR)),
	SJME_WS_NORM_P(nal, SJME_NVM_WALK_PSEUDO_NAL),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_closeableBase
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_CLOSEABLE)
	SJME_WS_NORM_V(isClosed, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
	SJME_WS_JAVA_V(refCounting, SJME_BASIC_TYPE_ID_BOOLEAN),
	SJME_WS_NORM_P(closeHandler,
		SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_commonBase
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_COMMON)
	SJME_WS_NORM_V(closeable, SJME_NVM_WALK_PSEUDO_CLOSEABLE),
	SJME_WS_NORM_V(type, SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE),
	SJME_WS_JAVA_V(magic, SJME_BASIC_TYPE_ID_INTEGER),
	SJME_WS_NORM_V(frontEnd, SJME_NVM_WALK_PSEUDO_FRONT_END),
	SJME_WS_NORM_V(lock, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_NORM_P(specificClose,
		SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_frontEnd
SJME_WALK_BEGIN(SJME_NVM_WALK_PSEUDO_FRONT_END)
	SJME_WS_NORM_P(wrapper, SJME_NVM_WALK_PSEUDO_FRONT_END_WRAPPER),
	SJME_WS_NORM_P(data, SJME_NVM_WALK_PSEUDO_FRONT_END_DATA),
	SJME_WS_NORM_V(bindLock, SJME_NVM_WALK_PSEUDO_SPIN_LOCK),
	SJME_WS_NORM_V(bindType, SJME_NVM_WALK_PSEUDO_BIND_TYPE),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_stateBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_STATE)
	SJME_WS_NORM_V(common, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WS_NORM_P(allocPool, SJME_NVM_WALK_PSEUDO_ALLOC_POOL),
	SJME_WS_NORM_P(bootParamCopy, SJME_NVM_WALK_PSEUDO_BOOT_PARAM),
	SJME_WS_NORM_P(hooks, SJME_NVM_WALK_PSEUDO_STATE_HOOKS),
	SJME_WS_NORM_P(nal, SJME_NVM_WALK_PSEUDO_NAL),
	SJME_WS_NORM_P(suite, SJME_NVM_STRUCT_ROM_SUITE),
	SJME_WS_LIST_P(tasks,
		SJME_WS_NORM_P(tasks, SJME_NVM_STRUCT_TASK)),
	SJME_WS_NORM_V(numRunningTasks, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
	SJME_WS_NORM_V(nextTaskId, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
	SJME_WS_NORM_V(nextThreadId, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
	SJME_WS_NORM_V(threadModel, SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL),
	SJME_WS_NORM_P(schedule, SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE),
	SJME_WS_NORM_V(terminating, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

#define SJME_WALK_CURRENT sjme_nvm_taskBase
SJME_WALK_BEGIN(SJME_NVM_STRUCT_TASK)
	SJME_WS_NORM_V(object, SJME_NVM_STRUCT_OBJECT_INSTANCE),
	SJME_WS_JAVA_V(id, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_P(inState, SJME_NVM_STRUCT_STATE),
	SJME_WS_JAVA_V(exitCode, SJME_JAVA_TYPE_ID_INTEGER),
	SJME_WS_NORM_V(status, SJME_NVM_WALK_PSEUDO_TASK_STATUS_TYPE),
	SJME_WS_NORM_V(terminate, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
	SJME_WS_ARRY_V(numThreads,
		SJME_WS_NORM_V(numThreads, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT)),
	SJME_WS_LIST_P(threads,
		SJME_WS_NORM_P(threads, SJME_NVM_STRUCT_THREAD_INSTANCE)),
	SJME_WS_NORM_P(classLoader, SJME_NVM_WALK_PSEUDO_CLASS_LOADER),
	SJME_WS_NORM_P(strings, SJME_NVM_WALK_PSEUDO_TASK_STRINGS),
	SJME_WS_NORM_V(globals, SJME_NVM_WALK_PSEUDO_TASK_GLOBALS),
	SJME_WS_NORM_V(nextFrameId, SJME_NVM_WALK_PSEUDO_ATOMIC_JINT),
SJME_WALK_END();
#undef SJME_WALK_CURRENT

const sjme_nvm_walk_stepSelect sjme_nvm_walk_select[] =
{
	/* Pseudo Structures. */
	SJME_WALK_SELECT(sjme_nvm_bootParam, SJME_NVM_WALK_PSEUDO_BOOT_PARAM),
	SJME_WALK_SELECT(sjme_closeableBase, SJME_NVM_WALK_PSEUDO_CLOSEABLE),
	SJME_WALK_SELECT(sjme_nvm_commonBase, SJME_NVM_WALK_PSEUDO_COMMON),
	SJME_WALK_SELECT(sjme_frontEnd, SJME_NVM_WALK_PSEUDO_FRONT_END),

	/* NVM Structures. */
	SJME_WALK_SELECT(sjme_nvm_stateBase, SJME_NVM_STRUCT_STATE),
	SJME_WALK_SELECT(sjme_nvm_taskBase, SJME_NVM_STRUCT_TASK),
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

static sjme_errorCode sjme_nvm_walkItem(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Execute item handler. */
	if (sjme_error_is(error = function(root, parent, at)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walkArray(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_walkList(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_walkStruct(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_jint atIndex;
	const sjme_nvm_walk_stepSelect* inSelect;
	const sjme_nvm_walk_step* currentStep;
	sjme_nvm_walk_state subStep;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* If there is no selection data, then we cannot walk this structure. */
	inSelect = at->inSelect;
	if (at->inSelect == NULL)
		return SJME_ERROR_NONE;

	/* Go through all steps. */
	for (atIndex = 0, currentStep = inSelect->steps;
		currentStep->memberName != NULL; currentStep++, atIndex++)
	{
		/* Setup base step information. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.depth = at->depth + 1;
		subStep.parent = at;
		
		/* Where is the actual pointer directly to this item? */
		subStep.valueP.walkLayer = at->baseStruct.walkLayer +
			currentStep->offset;
		
		/* Set item specific data. */
		subStep.inSelect = sjme_nvm_select(currentStep->structType);
		subStep.index = atIndex;
		subStep.typeId = currentStep->structType;
		subStep.inStep = currentStep;
		subStep.variantStep = NULL;
		
		/* When going through when level, handle each specific item. */
		if (at->breadth == SJME_NVM_WALK_BREADTH_LEVEL)
		{
			/* Walk on this item. */
			if (sjme_error_is(error = sjme_nvm_walkItem(root, at,
				&subStep, function)))
				return sjme_error_default(error);
		}

		/* Dive into this specific item. */
		else if (at->breadth == SJME_NVM_WALK_BREADTH_DIVE)
		{
			/* Go back to the root item walking for this. */
			if (sjme_error_is(error = sjme_nvm_walk(root, at,
				&subStep, function)))
				return sjme_error_default(error);
		}
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_walk(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	sjme_nvm_walk_breadthType breadth;
	sjme_jboolean skipElements;
	sjme_nvm_walk_stepOuterFunc outer;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Try to get a selection for the current item where applicable. */
	at->inSelect = sjme_nvm_select(at->typeId);
	
	/* Start a walk into the structure. */
	skipElements = SJME_JNI_FALSE;
	at->index = -1;
	if (sjme_error_is(error = function(root, parent, at)))
	{
		/* Skip element walk. */
		if (error == SJME_ERROR_WALK_SKIP_ELEMENTS)
			skipElements = SJME_JNI_TRUE;
		else
			return sjme_error_default(error);
	}
	
	/* Stepping over individual members? Walk in two breadths... */
	for (breadth = 0; breadth < SJME_NVM_WALK_NUM_BREADTH &&
		!skipElements; breadth++)
	{
		/* Determine the function used for outer stepping. */
		if (at->typeId == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY)
			outer = sjme_nvm_walkArray;
		else if (at->typeId == SJME_NVM_WALK_PSEUDO_LIST)
			outer = sjme_nvm_walkList;
		else
			outer = sjme_nvm_walkStruct;

		/* Setup basic step. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.breadth = breadth;
		subStep.depth = at->depth + 1;
		subStep.parent = at;

		/* The base structure is the value pointer if it is set. */
		/* Otherwise it just becomes the base of the object. */
		if (subStep.valueP.walkLayer != 0)
			subStep.baseStruct.walkLayer = subStep.valueP.walkLayer;
		else
			subStep.baseStruct.walkLayer = subStep.base.walkLayer;

		/* Clear anything related to the current step. */
		subStep.index = INT32_MIN;
		subStep.valueP.walkLayer = 0;
		subStep.inStep = NULL;
		subStep.variantStep = NULL;

		/* Perform stepping. */
		if (sjme_error_is(error = outer(root, at, &subStep,
			function)))
			return sjme_error_default(error);
	}
	
	/* End walk of structure. */
	/* If elements were skipped, then set a low index to indicate that. */
	at->index = (skipElements ? INT32_MIN : INT32_MAX);
	if (sjme_error_is(error = function(root, parent, at)))
	{
		/* Ignore this. */
		if (error != SJME_ERROR_WALK_SKIP_ELEMENTS)
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
	
#if 0
	/* Start of structure step? */
	if (at->index <= -1)
	{
	}

	/* Is this a variant? We can derive the info from the source step */
	if (at->variantStep != NULL)
	{
		/* Step with the variant's information. */
		startStep = at->variantStep;

		/* Fixed array. */
		if (at->inStep->structType == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY)
		{
			limitIndex = at->inStep->size / at->inStep->memberSize;
			
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* List. */
		else if (at->inStep->structType == SJME_NVM_WALK_PSEUDO_LIST)
		{
			voidList = (at->inStep->isPointer ?
				*((((sjme_list_void**)at->base.walkLayer))) :
				((sjme_list_void*)at->base.walkLayer));
			limitIndex = voidList->length;
			
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* Unknown variant, so it gets an unknown size. */
		else
			limitIndex = 0;
	}

	/* Otherwise select normally. */
	else
	{
		startStep = at->inSelect->steps;
		limitIndex = INT32_MAX;
	}
	
	/* Stepping over individual members? Walk in two breadths... */
	for (breadth = 0; breadth < SJME_NVM_WALK_NUM_BREADTH; breadth++)
		for (currentStep = startStep, atIndex = 0;
			!skipElements && atIndex < limitIndex;
			currentStep += stepAdd, atIndex++)
		{
			/* Use the default add of one, since we scan single entries */
			/* at a time. */
			/* Note that while in a variant, there is never a step */
			/* addition. */
			stepAdd = (at->variantStep == NULL ? 1 : 0);
			
			/* Stop when the step is invalid. */
			sjme_message("step(%p->%s)",
				currentStep, currentStep->memberName);
			if (currentStep->memberName == NULL)
				break;

			/* Locate the selection for this structure type, for recursion. */
			if (currentStep->structType != 0)
				subSelect = sjme_nvm_select(currentStep->structType);
			else
				subSelect = NULL;
			
			/* Fill in sub-step. */
			memset(&subStep, 0, sizeof(subStep));
			subStep.root = root;
			subStep.base.walkLayer = at->base.walkLayer;
			subStep.baseStruct.walkLayer = at->baseStruct.walkLayer;
			subStep.valueP.walkLayer = 0;
			subStep.parent = at;
			subStep.typeId = currentStep->structType;
			subStep.inSelect = subSelect;
			subStep.inStep = currentStep;
			subStep.variantStep = NULL;
			subStep.functions = at->functions;
			subStep.depth = at->depth + 1;
			subStep.stage = at->stage;
			subStep.data = at->data;
			subStep.uniqueId = ++at->nextUniqueId;
			subStep.breadth = breadth;

			/* Does this item have a variant? */
			if (currentStep->structType == SJME_NVM_WALK_PSEUDO_LIST ||
				currentStep->structType == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY)
			{
				/* The following step is the variant information. */
				subStep.variantStep = (currentStep + 1);

				/* We never walk into a variant. */
				if (at->variantStep == NULL)
					stepAdd = 2;
			}
			
			/* Base pointer to the value is here. */
			subStep.valueP.walkLayer = at->base.walkLayer +
				currentStep->offset;
			
			/* Fixed array element, and we are walking a variant. */
			if (at->variantStep != NULL &&
				currentStep->structType == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY)
			{
				subStep.valueP.walkLayer = subStep.valueP.walkLayer +
					(currentStep->memberSize * atIndex);
			}

			/* List element, and we are walking a variant. */
			else if (at->variantStep != NULL &&
				currentStep->structType == SJME_NVM_WALK_PSEUDO_LIST)
			{
				voidList = ((sjme_list_void*)subStep.valueP.walkLayer);
				subStep.valueP.walkLayer = (sjme_intPointer)voidList +
					(voidList->elementOffset + (voidList->elementSize *
						atIndex));
			}

			/* If there is a sub-select, recursive walk into it if we */
			/* are doing a diving walk. */
			/* For anything that is a variant, we always dive into it */
			/* since we do want to encode list/array elements. */
			if (breadth == SJME_NVM_WALK_BREADTH_DIVE &&
				(subSelect != NULL || subStep.variantStep != NULL))
			{
				/* Since we are diving into a structure, we always want */
				/* to be at the structure's actual pointer position. */
				if (currentStep->isPointer)
				{
					/* Never dive into null pointers. */
					if (subStep.valueP.intPointer == 0 ||
						subStep.valueP.intPointer[0] == 0)
						continue;

					/* Remap pointer base. */
					subStep.base.walkLayer = subStep.valueP.walkLayer;
					subStep.baseStruct.walkLayer = subStep.valueP.walkLayer;
				}

				/* Always start at the open of a structure. */
				subStep.index = -1;
				
				/* Do a structured walk. */
				if (sjme_error_is(error = sjme_nvm_walk(root, at,
					&subStep, function)))
				{
					/* This indicates we do not want to walk inside this. */
					if (error == SJME_ERROR_WALK_SKIP_ELEMENTS)
						continue;
					
					return sjme_error_default(error);
				}
			}

			/* Looking at this with a level walk. */
			if (breadth == SJME_NVM_WALK_BREADTH_LEVEL)
			{
				/* This is just the current index in the struct. */
				subStep.index = atIndex;

				/* Perform the stepped walk. */
				if (sjme_error_is(error = function(root, at,
					&subStep)))
				{
					/* Ignore this. */
					if (error == SJME_ERROR_WALK_SKIP_ELEMENTS)
						continue;
					
					return sjme_error_default(error);
				}
			}
		}
	
	/* We stepped over everything, so set a very high index. */
skip_endStruct:
	at->index = (skipElements ? INT32_MIN : INT32_MAX);
	if (sjme_error_is(error = function(root, parent, at)))
	{
		/* Ignore this. */
		if (error != SJME_ERROR_WALK_SKIP_ELEMENTS)
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
#endif
}

sjme_errorCode sjme_nvm_walk_start(
	sjme_attrInNotNull sjme_pointer startAt,
	sjme_attrInValue sjme_jint typeId,
	sjme_attrInNotNull const sjme_nvm_walk_functions* functions,
	sjme_attrInNullable sjme_pointer anyData)
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
		rootState.base.walkLayer = (sjme_intPointer)startAt;
		rootState.baseStruct.walkLayer = (sjme_intPointer)startAt;
		rootState.valueP.walkLayer = (sjme_intPointer)startAt;
		rootState.typeId = typeId;
		rootState.inSelect = select;
		rootState.functions = functions;
		rootState.index = -1;
		rootState.depth = 0;
		rootState.stage = stage;
		rootState.data = anyData;
		rootState.uniqueId = ++rootState.nextUniqueId;
		
		/* Perform the recursive walk. */
		if (sjme_error_is(error = sjme_nvm_walk(&rootState, NULL,
			&rootState, function)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
