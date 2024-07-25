/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal framebuffer functions.
 * 
 * @since 2024/07/24
 */

#ifndef SQUIRRELJME_FBINTERN_H
#define SQUIRRELJME_FBINTERN_H

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_FBINTERN_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

struct sjme_scritchui_implInternFunctions
{
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_FBINTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_FBINTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_FBINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FBINTERN_H */
