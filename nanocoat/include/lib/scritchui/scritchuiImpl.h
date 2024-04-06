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

#ifndef SQUIRRELJME_SCRITCHUIIMPL_H
#define SQUIRRELJME_SCRITCHUIIMPL_H

#include "lib/scritchui/scritchui.h"

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
 * Creates a new native panel.
 * 
 * @param inState The input ScritchUI state.
 * @param inOutPanel The input/output panel.
 * @return Any error code as per implementation.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_impl_panelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel inOutPanel);

struct sjme_scritchui_implFunctions
{
	/** Creates a new native panel. */
	sjme_scritchui_impl_panelNewFunc panelNew;
};

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
