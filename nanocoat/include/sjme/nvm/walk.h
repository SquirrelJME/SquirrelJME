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
 * The current type being walked.
 * 
 * @since 2025/06/21
 */
typedef enum sjme_nvm_walk_type
{
	SJME_TODO_WALK_TYPE,
} sjme_nvm_walk_type;

/**
 * The current state for walking.
 *
 * @since 2025/06/21
 */
typedef struct sjme_nvm_walk_state sjme_nvm_walk_state;

/**
 * Functions to handle walking.
 *
 * @since 2025/06/21
 */
typedef struct sjme_nvm_walk_functions
{
	int todo;
} sjme_nvm_walk_functions;

struct sjme_nvm_walk_state
{
	/** The functions to use for walking. */
	sjme_nvm_walk_functions functions;
};

/**
 * Performs serialized walking.
 * 
 * @param at The current at item.
 * @param parent The parent item this case from.
 * @param walkState The global walking state.
 * @return Any resultant error, if any.
 * @since 2025/06/21
 */
sjme_errorCode sjme_nvm_walk(
	sjme_attrInNotNull sjme_nvm_common at,
	sjme_attrInNotNull sjme_nvm_common parent,
	sjme_attrInNotNull sjme_nvm_walk_state* walkState);

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
