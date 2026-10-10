/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Descriptors and their interpreters.
 * 
 * @file
 * @since 2024/02/04
 */

#ifndef SJME_C_DESCRIPTOR_H
#define SJME_C_DESCRIPTOR_H

#include "sjme/nvm/nvm.h"
#include "sjme/list.h"
#include "sjme/nvm/stringPool.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_DESCRIPTOR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_DESCRIPTOR_H
}
		#undef SJME_CXX_SQUIRRELJME_DESCRIPTOR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_DESCRIPTOR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_DESCRIPTOR_H */
