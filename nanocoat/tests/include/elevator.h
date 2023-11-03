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
 * Represents the state of the elevator.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorState
{
} sjme_elevatorState;

/**
 * A single set entry and parameters for configuration.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorSetEntry sjme_elevatorSetEntry;

/**
 * Represents an elevator initialization function.
 * 
 * @return Will return @c JNI_TRUE when successful.
 * @since 2023/11/03
 */
typedef jboolean (*sjme_elevatorFunc)(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorSetEntry* inEntry);

struct sjme_elevatorSetEntry
{
	/** The function to call for initialization. */
	sjme_elevatorFunc function;
};

/**
 * Structure which contains the elevator instructions to use for initializing
 * a test virtual machine.
 * 
 * @since 2023/11/03
 */
typedef struct sjme_elevatorSet
{
	/** Flags for elevator. */
	jint flags;
	
	/** Elevator function order. */
	sjme_elevatorSetEntry order[sjme_flexibleArrayCount];
} sjme_elevatorSet;

/**
 * Initial virtual machine initialization state.
 * 
 * @param inState The elevator state.
 * @param inEntry The entry currently being processed.
 * @return If this was successful.
 * @since 2023/11/03
 */
jboolean sjme_elevatorDoInit(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorSetEntry* inEntry);

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
