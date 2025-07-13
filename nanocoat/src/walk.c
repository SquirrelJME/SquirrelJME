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
		sjme_sm(.typeId, inNvmId), \
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
			sjme_sm(.typeId, 0), \
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
		SJME_WS_NORM_P(mainClassPathByName, SJME_NVM_WALK_PSEUDO_LPSTR)),
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
	const sjme_nvm_walk_stepSelect* select;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Walk on this item. */
	if (at->breadth == SJME_NVM_WALK_BREADTH_LEVEL)
	{
		/* Execute item handler. */
		if (sjme_error_is(error = function(root, parent, at)))
			return sjme_error_default(error);
	}

	/* Dive into this specific item. */
	else if (at->breadth == SJME_NVM_WALK_BREADTH_DIVE)
	{
		/* Diving into a normal structure? */
		if (at->typeId != SJME_NVM_WALK_PSEUDO_LIST &&
			at->typeId != SJME_NVM_WALK_PSEUDO_FIXED_ARRAY)
		{
			/* We can only dive into known types. */
			select = sjme_nvm_select(at->typeId);
			if (select == NULL)
				return SJME_ERROR_NONE;
		}
		
		/* If this is a pointer, we need to dereference it. */
		if (at->inStep->isPointer)
		{
			/* Dereference. */
			at->valueP.value = *at->valueP.pointer;

			/* Set new structure base. */
			at->base.walkLayer = at->valueP.walkLayer; 
			at->baseStruct.walkLayer = at->base.walkLayer; 
		}
		
		/* Go back to the root item walking for this. */
		if (sjme_error_is(error = sjme_nvm_walk(root, parent,
			at, function)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walkArray(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	const sjme_nvm_walk_step* variantStep;
	const sjme_nvm_walk_step* currentStep;
	sjme_jint i, n;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* We do not know enough to walk through this array. */
	currentStep = at->inStep;
	if (currentStep == NULL)
		return SJME_ERROR_NONE;
	
	/* Determine the fixed array length. */
	n = currentStep->size / currentStep->memberSize;
	
	/* Go through the entire list. */
	variantStep = at->variantStep;
	for (i = 0; i < n; i++)
	{
		/* Setup base step information. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.depth = at->depth + 1;
		subStep.parent = at;
		
		/* The value is directly where the array element is */
		subStep.valueP.walkLayer = at->baseStruct.walkLayer +
			(i * currentStep->memberSize);
		
		/* Set item specific data. */
		subStep.index = i;
		subStep.inStep = variantStep;
		subStep.typeId = variantStep->typeId;

		/* Walk on this item. */
		if (sjme_error_is(error = sjme_nvm_walkItem(root, at,
			&subStep, function)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_walkList(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_nvm_walk_state subStep;
	sjme_list_void* voidList;
	const sjme_nvm_walk_step* variantStep;
	sjme_jint i, n;
	
	if (root == NULL || at == NULL || function == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Mismatched parent or in some other bad state? */
	if (at->parent != parent)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Recover the associated void list. */
	voidList = at->valueP.value;

	/* Skip null pointers. */
	if (voidList == NULL)
		return SJME_ERROR_NONE;

	/* Go through the entire list. */
	variantStep = at->variantStep;
	for (i = 0, n = voidList->length; i < n; i++)
	{
		/* Setup base step information. */
		memmove(&subStep, at, sizeof(subStep));
		subStep.uniqueId = ++at->nextUniqueId;
		subStep.depth = at->depth + 1;
		subStep.parent = at;

		/* The structure base is the pointer of the list. */
		subStep.baseStruct.walkLayer = (sjme_intPointer)voidList;

		/* If the list itself is a pointer, then the base structure is also */
		/* the list because it is another allocated structure. */
		if (at->inStep->isPointer)
			subStep.base.walkLayer = subStep.baseStruct.walkLayer;
		
		/* Where is the actual pointer directly to this item? */
		subStep.valueP.walkLayer = (sjme_intPointer)voidList +
			voidList->elementOffset + (voidList->elementSize * i);
		
		/* Set item specific data. */
		subStep.index = i;
		subStep.inStep = variantStep;
		subStep.typeId = variantStep->typeId;

		/* Walk on this item. */
		if (sjme_error_is(error = sjme_nvm_walkItem(root, at,
			&subStep, function)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_walkStruct(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function)
{
	sjme_errorCode error;
	sjme_jint atIndex, stepAdd;
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
	for (atIndex = 0, currentStep = inSelect->steps, stepAdd = 1;
		currentStep->memberName != NULL; currentStep += stepAdd, atIndex++)
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
		subStep.inSelect = sjme_nvm_select(currentStep->typeId);
		subStep.index = atIndex;
		subStep.typeId = currentStep->typeId;
		subStep.inStep = currentStep;
		
		/* Is this a variant? That is an array or list. */
		if (currentStep->typeId == SJME_NVM_WALK_PSEUDO_FIXED_ARRAY ||
			currentStep->typeId == SJME_NVM_WALK_PSEUDO_LIST)
		{
			stepAdd = 2;
			subStep.variantStep = (currentStep + 1);
		}

		/* Not a variant. */
		else
		{
			stepAdd = 1;
			subStep.variantStep = NULL;
		}
		
		/* Walk on this item. */
		if (sjme_error_is(error = sjme_nvm_walkItem(root, at,
			&subStep, function)))
			return sjme_error_default(error);
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

	/* Skip walking null pointers. */
	if (at->base.walkLayer == 0 || at->baseStruct.walkLayer == 0)
		return SJME_ERROR_NONE;

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
		subStep.root = at->root;
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
		rootState.root = &rootState;
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
