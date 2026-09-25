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
 * @file
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

	/** @link sjme_nvm_bootParam @endlink . */
	SJME_NVM_WALK_PSEUDO_BOOT_PARAM = -3,

	/** @link sjme_nvm_stateHooks @endlink . */
	SJME_NVM_WALK_PSEUDO_STATE_HOOKS = -4,

	/** @link sjme_nal @endlink . */
	SJME_NVM_WALK_PSEUDO_NAL = -5,

	/** @link sjme_atomic_ @endlink types. */
	SJME_NVM_WALK_PSEUDO_ATOMIC = -6,

	/** @link sjme_nvm_mle_threadModel @endlink . */
	SJME_NVM_WALK_PSEUDO_MLE_THREAD_MODEL = -7,

	/** @link sjme_nvm_threadSchedule @endlink . */
	SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE = -8,

	/** @link sjme_closeableBase @endlink . */
	SJME_NVM_WALK_PSEUDO_CLOSEABLE = -9,

	/** @link sjme_nvm_structType @endlink . */
	SJME_NVM_WALK_PSEUDO_NVM_STRUCT_TYPE = -10,

	/** A Primitive type. */
	SJME_NVM_WALK_PSEUDO_PRIMITIVE = -11,

	/** @link sjme_frontEnd @endlink . */
	SJME_NVM_WALK_PSEUDO_FRONT_END = -12,

	/** @link sjme_thread_spinLock @endlink . */
	SJME_NVM_WALK_PSEUDO_SPIN_LOCK = -13,

	/** @link sjme_closeable_closeHandlerFunc @endlink . */
	SJME_NVM_WALK_PSEUDO_CLOSE_HANDLER = -14,

	/** @link sjme_list_... . */
	SJME_NVM_WALK_PSEUDO_LIST = -15,

	/** @link sjme_frontEndWrapper @endlink . */
	SJME_NVM_WALK_PSEUDO_FRONT_END_WRAPPER = -16,

	/** @link sjme_frontEndData @endlink . */
	SJME_NVM_WALK_PSEUDO_FRONT_END_DATA = -17,

	/** @link sjme_frontEnd_bindType @endlink . */
	SJME_NVM_WALK_PSEUDO_FRONT_END_BIND_TYPE = -18,

	/** @link sjme_lpstr @endlink . */
	SJME_NVM_WALK_PSEUDO_LPSTR = -19,

	/** @link sjme_nvm_task_statusType @endlink . */
	SJME_NVM_WALK_PSEUDO_TASK_STATUS_TYPE = -20,

	/** Fixed size array . */
	SJME_NVM_WALK_PSEUDO_FIXED_ARRAY = -21,

	/** @link sjme_javaTypeId @endlink . */
	SJME_NVM_WALK_PSEUDO_JAVA_TYPE_ID = -22,

	/** A @c union . */
	SJME_NVM_WALK_PSEUDO_UNION = -23,

	/** @link sjme_nvm_task_globals @endlink . */ 
	SJME_NVM_WALK_PSEUDO_TASK_GLOBALS = -24,

	/** Plain pointer. */
	SJME_NVM_WALK_PSEUDO_POINTER = -25,

	/** @link sjme_nvm_rom_suiteFunctions @endlink . */
	SJME_NVM_WALK_PSEUDO_SUITE_FUNCTIONS = -26,

	/** @link sjme_nvm_rom_libraryFunctions @endlink . */
	SJME_NVM_WALK_PSEUDO_LIBRARY_FUNCTIONS = -27,

	/** @link sjme_thread_rwLock @endlink . */
	SJME_NVM_WALK_PSEUDO_RW_LOCK = -28,

	/** @link sjme_nvm_task_taskNewConfig @endlink . */
	SJME_NVM_WALK_PSEUDO_INIT_TASK_CONFIG = -29,

	/** @link sjme_nvm_bootBelayType @endlink . */
	SJME_NVM_WALK_PSEUDO_BOOT_BELAY_TYPE = -30,

	/** @link sjme_nvm_bootClutterLevel @endlink . */
	SJME_NVM_WALK_PSEUDO_CLUTTER_LEVEL = -31,

	/** @link sjme_random @endlink . */
	SJME_NVM_WALK_PSEUDO_RANDOM = -32,

	/** @link sjme_basicTypeId @endlink . */
	SJME_NVM_WALK_PSEUDO_BASIC_TYPE_ID = -33,

	/** @link sjme_nvm_valueSet @endlink . */
	SJME_NVM_WALK_PSEUDO_VALUE_SET = -34,

	/** Unspecified stored binary data . */
	SJME_NVM_WALK_PSEUDO_UNSPECIFIED_BINARY = -35,

	/** @link sjme_nvm_threadSubSchedule @endlink . */
	SJME_NVM_WALK_PSEUDO_THREAD_SUB_SCHEDULE = -36,

	/** @link sjme_nvm_task_pipeRedirectType @endlink . */
	SJME_NVM_WALK_PSEUDO_TASK_PIPE_REDIRECT_TYPE = -37,

	/** @link sjme_nvm_class_version @endlink . */
	SJME_NVM_WALK_PSEUDO_CLASS_VERSION = -38,

	/** @link sjme_nvm_class_classFlags @endlink . */
	SJME_NVM_WALK_PSEUDO_CLASS_FLAGS = -39,

	/** @link sjme_nvm_class_poolEntry @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_ENTRY = -40,

	/** @link sjme_nvm_class_accessFlags @endlink . */
	SJME_NVM_WALK_PSEUDO_CLASS_FLAGS_ACCESS = -41,

	/** @link sjme_charSeq @endlink . */
	SJME_NVM_WALK_PSEUDO_CHAR_SEQ = -42,

	/** Phantom back reference . */
	SJME_NVM_WALK_PSEUDO_PHANTOM = -43,

	/** @link sjme_nvm_jclass_fields @endlink . */
	SJME_NVM_WALK_PSEUDO_CLASS_FIELDS = -44,

	/** @link sjme_nvm_jclass_methods @endlink . */
	SJME_NVM_WALK_PSEUDO_CLASS_METHODS = -45,

	/** @link sjme_nvm_jfieldAccessFunc @endlink . */
	SJME_NVM_WALK_PSEUDO_FIELD_ACCESSOR_FUNC = -46,

	/** Technically nothing. */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_NULL = -47,

	/** @link sjme_nvm_class_poolEntryUtf @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_UTF = -48,

	/** @link sjme_nvm_class_poolEntryInteger @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_INTEGER = -49,

	/** @link sjme_nvm_class_poolEntryFloat @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_FLOAT = -50,

	/** @link sjme_nvm_class_poolEntryLong @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_LONG = -51,

	/** @link sjme_nvm_class_poolEntryDouble @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_DOUBLE = -52,

	/** @link sjme_nvm_class_poolEntryClass @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_CLASS = -53,

	/** @link sjme_nvm_class_poolEntryString @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_STRING = -54,

	/** @link sjme_nvm_class_poolEntryMember @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_MEMBER = -55,

	/** @link sjme_nvm_class_poolEntryNameAndType @endlink . */
	SJME_NVM_WALK_PSEUDO_POOL_TYPE_NAME_AND_TYPE = -56,

	/** @link sjme_jmemberIDBase @endlink . */
	SJME_NVM_WALK_PSEUDO_MEMBER_ID = -57,

	/** @link sjme_nvm_class_methodFlags @endlink . */
	SJME_NVM_WALK_PSEUDO_METHOD_FLAGS = -58,

	/** @link sjme_nvm_class_methodInfoBits @endlink . */
	SJME_NVM_WALK_PSEUDO_METHOD_INFO_BITS = -59,

	/** @link sjme_nvm_class_fieldFlags @endlink */
	SJME_NVM_WALK_PSEUDO_FIELD_FLAGS = -60,

	/** @link sjme_extendedTypeId @endlink */
	SJME_NVM_WALK_PSEUDO_EXTENDED_TYPE_ID = -61,

	/** @link sjme_nvm_class_fieldConstVal @endlink . */
	SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL = -62,

	/** @link sjme_nvm_class_codePerType @endlink . */
	SJME_NVM_WALK_PSEUDO_CODE_PER_TYPE = -63,

	/** @link sjme_nvm_class_exceptionHandler @endlink . */
	SJME_NVM_WALK_PSEUDO_EXCEPTION_HANDLER = -64,

	/** @link sjme_byteCode @endlink . */
	SJME_NVM_WALK_PSEUDO_BYTE_CODE = -65,

	/** @link sjme_nvm_class_fieldConstVal @endlink with object . */
	SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL_STRING = -66,

	/** @link sjme_nvm_class_fieldConstVal @endlink with non-object . */
	SJME_NVM_WALK_PSEUDO_FIELD_CONST_VAL_NUMBER = -67,

	/** @link sjme_nvm_thread_startType @endlink . */
	SJME_NVM_WALK_PSEUDO_THREAD_START_TYPE = -68,

	/** @link sjme_nvm_thread_statusType @endlink . */
	SJME_NVM_WALK_PSEUDO_THREAD_STATUS_TYPE = -69,

	/** @link sjme_frame_threadStacks @endlink . */
	SJME_NVM_WALK_PSEUDO_THREAD_STACKS = -70,

	/** @link sjme_nvm_threadScheduleMode @endlink . */
	SJME_NVM_WALK_PSEUDO_THREAD_SCHEDULE_MODE = -71,

	/** Non-cyclic atomic reference. */
	SJME_NVM_WALK_PSEUDO_NON_CYCLIC = -72,

	/** @link sjme_pcAddr @endlink . */
	SJME_NVM_WALK_PSEUDO_PC_ADDR = -73,

	/** @link sjme_frame_frameStacks @endlink . */
	SJME_NVM_WALK_PSEUDO_FRAME_STACKS = -74,

	/** @link sjme_nvm_frame_conditionFunc @endlink . */
	SJME_NVM_WALK_PSEUDO_CONDITION_FUNCTION = -75,

	/** @link sjme_nvm_frame_stateFlags @endlink . */
	SJME_NVM_WALK_PSEUDO_FRAME_FLAGS = -76,

	/** @link sjme_frame_frameStack @endlink . */
	SJME_NVM_WALK_PSEUDO_FRAME_STACK = -77,
	
	/** @link sjme_nvm_class_annotation @endlink. */
	SJME_NVM_WALK_PSEUDO_ANNOTATION = -78,
	
	/** @link sjme_nvm_class_annotationTag @endlink. */
	SJME_NVM_WALK_PSEUDO_ANNOTATION_TAG = -79,
	
	/** @link sjme_jvalueTyped @endlink. */
	SJME_NVM_WALK_PSEUDO_JAVA_VALUE = -80,
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
	sjme_atomic(sjme_jint)* atomicInt;

	/** Atomic pointer. */
	sjme_atomic(sjme_pointer)* atomicPointer;

	/** Atomic object. */
	sjme_atomic(sjme_jobject)* atomicObject;

	/** Closeable. */
	sjme_closeableBase* closeable;

	/** Common NVM structure. */
	sjme_nvm_common nvmCommon;

	/** Common NVM structure. */
	sjme_atomic(sjme_nvm_common)* atomicNvmCommon;

	/** ROM library. */
	sjme_nvm_rom_library romLibrary;

	/** ROM suite. */
	sjme_nvm_rom_suite romSuite;

	/** Java object. */
	sjme_jobject jobject;

	/** Java class. */
	sjme_jclass jclass;
} sjme_nvm_walk_pointer;

/**
 * The ID being walked.
 *
 * @since 2025/10/13
 */
typedef union sjme_nvm_walk_id
{
	/** As integer. */
	sjme_jint i;

	/** As structure type. */
	sjme_nvm_structType n;

	/** As pseudo type. */
	sjme_nvm_walk_pseudoType p;
} sjme_nvm_walk_id;

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
	sjme_nvm_walk_id typeId;

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

	/** The number of variant steps. */
	sjme_jint numVariantSteps;

	/** Data pointer. */
	sjme_pointer data;

	/** The current breadth of this step. */
	sjme_nvm_walk_breadthType breadth;

	/** Do not perform any diving. */
	sjme_jboolean noDive : sjme_booleanBit;

	/** Current parsed string value. */
	sjme_lpcstr lpstr;

	/** Does this point to a phantom atomic reference? */
	sjme_jboolean isPhantom;

	/** Does this point to an atomic reference? */
	sjme_jboolean isAtomic;

	/** Step for an individual item type. */
	sjme_nvm_walk_stepOuterFunc stepItem;

	/** Perform normal handling for custom items. */
	sjme_nvm_walk_stepOuterFunc normalCustom;
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
	sjme_nvm_walk_id typeId;

	/**
	 * Custom step logic setup for walking. If the step function
	 * returns @link SJME_ERROR_WALK_SKIP_CUSTOM_DEFAULT @endlink then the default
	 * walk will not be performed, this assumes that the step handler
	 * called the appropriate handler function.
	 */
	sjme_nvm_walk_stepOuterFunc customStep;
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
 * Returns the walk handler for the given type.
 * 
 * @param typeId The type ID to select for.
 * @param outSelect The handler for the selection.
 * @return On any resultant error, if any.
 * @since 2025/10/17
 */
sjme_errorCode sjme_nvm_select(
	sjme_attrInValue sjme_jint typeId,
	sjme_attrOutNotNull const sjme_nvm_walk_stepSelect** outSelect);

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
