/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Custom walk handlers.
 * 
 * @since 2025/10/03
 */

#ifndef SJME_C_SQUIRRELJME_WALKCUSTOM_H
#define SJME_C_SQUIRRELJME_WALKCUSTOM_H

#include "sjme/nvm/walk.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_WALKCUSTOM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_nvm_walk_customRawArrayValues(
	sjme_attrInNotNull sjme_nvm_walk_state* root,
	sjme_attrInNotNull sjme_nvm_walk_state* parent,
	sjme_attrInNotNull sjme_nvm_walk_state* at,
	sjme_attrInNotNull sjme_nvm_walk_stepHandlerFunc function);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_WALKCUSTOM_H
}
#undef SJME_CXX_SQUIRRELJME_WALKCUSTOM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_WALKCUSTOM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_WALKCUSTOM_H */
