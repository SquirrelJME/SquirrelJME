/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Test elevator.
 * 
 * @since 2023/11/03
 */

#ifndef SQUIRRELJME_ELEVATOR_H
#define SQUIRRELJME_ELEVATOR_H

#include "sjme/nvm.h"
#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ELEVATOR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The elevator instruction type.
 * 
 * @since 2023/11/11
 */
typedef enum sjme_elevatorDoType
{
	/** Unknown. */
	SJME_ELEVATOR_DO_TYPE_UNKNOWN,
	
	/** Initialize. */
	SJME_ELEVATOR_DO_TYPE_INIT,

	/** The number of do types. */
	SJME_NUM_ELEVATOR_DO_TYPES
} sjme_elevatorDoType;

/**
 * Represents the state of the elevator.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorState
{
	/** The virtual machine state. */
	sjme_nvm_state* nvmState;
} sjme_elevatorState;

/**
 * The current run item in the elevator.
 * 
 * @since 2023/11/11
 */
typedef struct sjme_elevatorRunCurrent
{
	/** The current index of all. */
	jint indexAll;
	
	/** The current index of this type. */
	jint indexType;
	
	/** The current type. */
	sjme_elevatorDoType type;
	
	/** Data for the initialization step. */
	union
	{
	} data;
} sjme_elevatorRunCurrent;

/**
 * Data for the entire elevator un.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorRunData sjme_elevatorRunData;

/**
 * Configuration function for the elevator.
 * 
 * @param inState The input state.
 * @param inCurrent The current run item for the elevator.
 * @return Returns @c JNI_TRUE when successful.
 * @since 2023/11/11
 */
typedef jboolean (*sjme_elevatorConfigFunc)(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunCurrent* inCurrent);

/**
 * Represents an elevator initialization function.
 * 
 * @return Will return @c JNI_TRUE when successful.
 * @since 2023/11/03
 */
typedef jboolean (*sjme_elevatorDoFunc)(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);

/**
 * Structure which contains the elevator instructions to use for initializing
 * a test virtual machine.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorSet
{
	/** The configuration function. */
	sjme_elevatorConfigFunc config;

	/** Flags for elevator. */
	jint flags;
	
	/** Elevator function order. */
	sjme_elevatorDoFunc order[sjme_flexibleArrayCount];
} sjme_elevatorSet;

/**
 * Performs the elevator action.
 * 
 * @param inState The input state.
 * @param inSet The set to act on.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/1/111
 */
jboolean sjme_elevatorAct(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull const sjme_elevatorSet* inSet);

/**
 * Allocates memory
 * 
 * @param inState The input state. 
 * @param inLen The number of bytes to allocate.
 * @return The allocated buffer, returns @c NULL if allocation failed.
 * @since 2023/11/11
 */
void* sjme_elevatorAlloc(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInPositiveNonZero size_t inLen);

/**
 * Initial virtual machine initialization state.
 * 
 * @param inState The elevator state.
 * @param inData The data currently being processed.
 * @return If this was successful.
 * @since 2023/11/03
 */
jboolean sjme_elevatorDoInit(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ELEVATOR_H
}
		#undef SJME_CXX_SQUIRRELJME_ELEVATOR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ELEVATOR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ELEVATOR_H */
