/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI implementation interface.
 * 
 * @since 2024/04/06
 */

#ifndef SJME_C_SCRITCHUIIMPL_H
#define SJME_C_SCRITCHUIIMPL_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypesListener.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIIMPL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Obtains the core listener for the given type.
 * 
 * @param item The structure to access.
 * @param specific The specific listener that is wanted.
 * @return A pointer to the listener info.
 * @since 2024/05/01
 */
#define SJME_SCRITCHUI_LISTENER_CORE(item, specific) \
	((item)->listeners[SJME_SCRITCHUI_LISTENER_CORE].specific)

/**
 * Obtains the user listener for the given type.
 * 
 * @param item The structure to access.
 * @param specific The specific listener that is wanted.
 * @return A pointer to the listener info.
 * @since 2024/05/01
 */
#define SJME_SCRITCHUI_LISTENER_USER(item, specific) \
	((item)->listeners[SJME_SCRITCHUI_LISTENER_USER].specific)

/**
 * List initialization parameters.
 * 
 * @since 2024/07/24
 */
typedef struct sjme_scritchui_impl_initParamList
{
	/** The type of choice used. */
	sjme_scritchui_choiceType type;
} sjme_scritchui_impl_initParamList;

/**
 * Initialization parameters for menu items.
 * 
 * @since 2024/08/01
 */
typedef struct sjme_scritchui_impl_initParamMenuItem
{
	/** The opaque ID to use for the item. */
	sjme_jint opaqueId;
} sjme_scritchui_impl_initParamMenuItem;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIIMPL_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIIMPL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIIMPL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIIMPL_H */
