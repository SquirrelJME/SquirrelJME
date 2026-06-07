/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Serialized walkers.
 * 
 * @since 2025/06/21
 */

#ifndef SJME_C_WALK_H
#define SJME_C_WALK_H

#include "sjme/config.h"
#include "sjme/stream.h"
#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_WALK_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Pseudo type structure.
 *
 * @since 2025/07/10
 */
typedef enum sjme_nvm_walk_pseudoType
{
	/** NVM Common type. */
	SJME_NVM_WALK_PSEUDO_COMMON = -1,

	/** Allocation pool. */
	SJME_NVM_WALK_PSEUDO_ALLOC_POOL = -2,

	/** @c sjme_nvm_bootParam . */
	SJME_NVM_WALK_PSEUDO_BOOT_PARAM = -3,

	/** @c sjme_nvm_stateHooks . */
	SJME_NVM_WALK_PSEUDO_STATE_HOOKS = -4,

	/** @c sjme_nal . */
	SJME_NVM_WALK_PSEUDO_NAL = -5,

	/** @c sjme_atomic_sjme_jint . */
	SJME_NVM_WALK_PSEUDO_ATOMIC_JINT = -6,

	/** @c sjme_nvm_mle_threadModel . */
	SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL = -7,

	/** @c sjme_nvm_threadSchedule . */
	SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE = -8,

	/** @c sjme_closeableBase . */
	SJME_NVM_WALK_PSEUDO_CLOSEABLE = -9,

	/** @c sjme_nvm_structType . */
	SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE = -10,

	/** A Primitive type. */
	SJME_NVM_WALK_PSEUDO_PRIMITIVE = -11,

	/** @c sjme_frontEnd . */
	SJME_NVM_WALK_PSEUDO_FRONT_END = -12,

	/** @c sjme_thread_spinLock . */
	SJME_NVM_WALK_PSEUDO_SPIN_LOCK = -13,

	/** @c sjme_closeable_closeHandlerFunc . */
	SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER = -14,

	/** @c sjme_list_... . */
	SJME_NVM_WALK_PSEUDO_LIST = -15,

	/** @c sjme_frontEndWrapper . */
	SJME_NVM_WALK_PSEUDO_FRONT_END_WRAPPER = -16,

	/** @c sjme_frontEndData . */
	SJME_NVM_WALK_PSEUDO_FRONT_END_DATA = -17,

	/** @c sjme_frontEnd_bindType . */
	SJME_NVM_WALK_PSEUDO_BIND_TYPE = -18,

	/** @c sjme_lpstr . */
	SJME_NVM_WALK_PSEUDO_LPSTR = -19,

	/** @c sjme_nvm_task_statusType . */
	SJME_NVM_WALK_PSEUDO_TASK_STATUS_TYPE = -20,

	/** Fixed size array . */
	SJME_NVM_WALK_PSEUDO_FIXED_ARRAY = -21,

	/** @c sjme_nvm_vmClass_loader . */
	SJME_NVM_WALK_PSEUDO_CLASS_LOADER = -22,

	/** @c sjme_nvm_taskStrings . */
	SJME_NVM_WALK_PSEUDO_TASK_STRINGS = -23,

	/** @c sjme_nvm_task_globals . */ 
	SJME_NVM_WALK_PSEUDO_TASK_GLOBALS = -24,

	/** Plain pointer. */
	SJME_NVM_WALK_PSEUDO_POINTER = -25,

	/** @c sjme_nvm_rom_suiteFunctions . */
	SJME_NVM_WALK_PSEUDO_SUITE_FUNCTIONS = -26,

	/** @c sjme_nvm_rom_libraryFunctions . */
	SJME_NVM_WALK_PSEUDO_LIBRARY_FUNCTIONS = -27,

	/** @c sjme_thread_rwLock . */
	SJME_NVM_WALK_PSEUDO_RW_LOCK = -28,

	/** @c sjme_nvm_task_taskNewConfig . */
	SJME_NVM_WALK_PSEUDO_INIT_TASK_CONFIG = -29,

	/** @c sjme_nvm_bootBelayType . */
	SJME_NVM_WALK_PSEUDO_BOOT_BELAY_TYPE = -30,

	/** @c sjme_nvm_bootClutterLevel . */
	SJME_NVM_WALK_PSEUDO_CLUTTER_LEVEL = -31,

	/** @c sjme_random . */
	SJME_NVM_WALK_PSEUDO_RANDOM = -32,
} sjme_nvm_walk_pseudoType;

/**
 * The current walking stage.
 *
 * @since 2025/07/10
 */
typedef enum sjme_nvm_walk_stageType
{
	/** Pre-walk phase. */
	SJME_NVM_WALK_STAGE_PRE,

	/** Steps phase. */
	SJME_NVM_WALK_STAGE_STEPS,

	/** The number of walk stages. */
	SJME_NVM_WALK_NUM_STAGES,
} sjme_nvm_walk_stageType;

/**
 * The current breadth type when walking through steps.
 *
 * @since 2025/07/11
 */
typedef enum sjme_nvm_walk_breadthType
{
	/** Staying at the current level. */
	SJME_NVM_WALK_BREADTH_LEVEL,

	/** Diving into sub-structures. */
	SJME_NVM_WALK_BREADTH_DIVE,
	
	/** The number of breadth steps. */
	SJME_NVM_WALK_NUM_BREADTH,
} sjme_nvm_walk_breadthType;

/**
 * Walk step definition.
 *
 * @since 2025/07/10
 */
typedef struct sjme_nvm_walk_step sjme_nvm_walk_step;

/**
 * Walk step selection definition.
 *
 * @since 2025/07/10
 */
typedef struct sjme_nvm_walk_stepSelect sjme_nvm_walk_stepSelect;

/**
 * The current state for walking.
 *
 * @since 2025/06/21
 */
typedef struct sjme_nvm_walk_state sjme_nvm_walk_state;
	
/**
 * Handles walk stepping.
 * 
 * @param root The root walking state.
 * @param parent The parent of this state.
 * @param at The current item this is walking over.
 * @return Any resultant error, if any.
 * @since 2025/07/10
 */
typedef sjme_errorCode (*sjme_nvm_walk_stepHandlerFunc)(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at);
	
/**
 * Handles walk stepping at the outer level.
 * 
 * @param root The root walking state.
 * @param parent The parent of this state.
 * @param at The current item this is walking over.
 * @param function The function for handling of steps.
 * @return Any resultant error, if any.
 * @since 2025/07/10
 */
typedef sjme_errorCode (*sjme_nvm_walk_stepOuterFunc)(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function);

/**
 * Functions to handle walking.
 *
 * @since 2025/06/21
 */
typedef struct sjme_nvm_walk_functions
{
	/** Handles pre-step through everything, if applicable. */
	sjme_nvm_walk_stepHandlerFunc pre;
	
	/** Handles actual stepping through. */
	sjme_nvm_walk_stepHandlerFunc step;
} sjme_nvm_walk_functions;

typedef union sjme_nvm_walk_pointer
{
	/** Pointer used by the walking layer. */
	sjme_intPointer walkLayer;
	
	/** A pointer type. */
	sjme_pointer* pointer;
	
	/** A value type. */
	sjme_pointer value;

	/** Pointer sized integer. */
	sjme_intPointer* intPointer;

	/** Spin lock. */
	sjme_thread_spinLock* spinLock;

	/** The structure type. */
	sjme_nvm_structType* nvmStructType;

	/** Atomic integer. */
	sjme_atomic_sjme_jint* atomicInt;

	/** Closeable. */
	sjme_closeableBase* closeable;
} sjme_nvm_walk_pointer;

struct sjme_nvm_walk_state
{
	/** The root state. */
	sjme_nvm_walk_state* root;
	
	/** The parent state. */
	sjme_nvm_walk_state* parent;
	
	/** The functions to use for walking. */
	const sjme_nvm_walk_functions* functions;

	/** The base object pointer. */
	sjme_nvm_walk_pointer base;

	/** The base structure pointer within the base. */
	sjme_nvm_walk_pointer baseStruct;

	/** The pointer to the value. */
	sjme_nvm_walk_pointer valueP;

	/** The current type being walked. */
	sjme_jint typeId;

	/** The Java type being walked. */
	sjme_javaTypeId javaType;

	/** Is this a pointer? */
	sjme_jboolean isPointer;

	/** What is the current walk stage? */
	sjme_nvm_walk_stageType stage;

	/** The current walking index. */
	sjme_jint index;

	/** The current depth. */
	sjme_jint depth;

	/** The unique step ID. */
	sjme_jint uniqueId;

	/** The next unique step ID. */
	sjme_jint nextUniqueId;

	/** The select this is in. */
	const sjme_nvm_walk_stepSelect* inSelect;

	/** The step this is in. */
	const sjme_nvm_walk_step* inStep;

	/** Step that refers to an array, list, or multi-variant type. */
	const sjme_nvm_walk_step* variantStep;

	/** Data pointer. */
	sjme_pointer data;

	/** The current breadth of this step. */
	sjme_nvm_walk_breadthType breadth;
};

struct sjme_nvm_walk_step
{
	/** The offset to the member. */
	sjme_intPointer offset;

	/** The name of the member. */
	sjme_lpcstr memberName;

	/** Is this a pointer? */
	sjme_jboolean isPointer;

	/** The size of the type. */
	sjme_jint size;

	/** The member size for this item. */
	sjme_jint memberSize;

	/** The Java type. */
	sjme_javaTypeId javaType;

	/** The structure type. */
	sjme_jint typeId;
};

struct sjme_nvm_walk_stepSelect
{
	/** The name of this type. */
	sjme_lpcstr typeName;

	/** The ID of this type. */
	sjme_jint typeId;

	/** The size of the type. */
	sjme_jint size;

	/** The steps for walking. */
	const sjme_nvm_walk_step* steps;
};

/**
 * Performs a walking step.
 * 
 * @param root The root state.
 * @param parent The parent state.
 * @param at The current step state.
 * @param function The function used for state.
 * @return Any resultant error, if any.
 * @since 2025/07/12
 */
sjme_errorCode sjme_nvm_walk(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function);
	
/**
 * Starts a walk.
 * 
 * @param startAt The pointer to start at.
 * @param typeId The type ID of this pointer.
 * @param functions The functions for the walk.
 * @param anyData Any data state, optional.
 * @return On any resultant error, if any.
 * @since 2025/07/10
 */
sjme_errorCode sjme_nvm_walk_start(
	sjme_attrInNotNull sjme_pointer startAt,
	sjme_attrInValue sjme_jint typeId,
	sjme_attrInNotNull const sjme_nvm_walk_functions* functions,
	sjme_attrInNullable sjme_pointer anyData);

/** Core dump of state. */
extern const sjme_nvm_walk_functions sjme_nvm_walk_coreDumpFunctions;
	
/** Print structures to message output. */
extern const sjme_nvm_walk_functions sjme_nvm_walk_printDumpFunctions;

/**
 * Core dumps to a NAL file.
 * 
 * @param inState The state to core dump.
 * @param nal The NAL to access for file output, if @c NULL then the NAL
 * used in @c inState is selected.
 * @param filePath The path to the file to open.
 * @return Any resultant error, if any.
 * @since 2025/07/10
 */
sjme_errorCode sjme_nvm_walk_coreDumpFile(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull sjme_lpcstr filePath);

/**
 * Core dumps to a stream.
 * 
 * @param allocPool The allocation pool to use.
 * @param inState The state to core dump.
 * @param outStream The resultant stream.
 * @return Any resultant error, if any.
 * @since 2025/07/10
 */
sjme_errorCode sjme_nvm_walk_coreDumpStream(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_stream_output outStream);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_WALK_H
}
#undef SJME_CXX_WALK_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_WALK_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_WALK_H */
