/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * GTK2 ScritchUI Header.
 * 
 * @since 2024/04/02
 */

#ifndef SQUIRRELJME_GTK2_H
#define SQUIRRELJME_GTK2_H

#include <gtk-2.0/gtk/gtk.h>

#include "sjme/config.h"
#include "sjme/debug.h"
#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_GTK2_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * GTK2 State.
 * 
 * @since 2024/04/02
 */
struct sjme_scritchui
{
	sjme_jint todo;
};

/**
 * GTK 2 Initialize.
 * 
 * @param inPool The pool for allocations.
 * @param outState The resultant state.
 * @return Any error code if applicable.
 * @since 2024/04/02
 */
sjme_errorCode sjme_scritchui_gtk2_apiInit(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_GTK2_H
}
		#undef SJME_CXX_SQUIRRELJME_GTK2_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_GTK2_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_GTK2_H */
