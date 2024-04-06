/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal ScritchUI types.
 * 
 * @since 2024/04/02
 */

#ifndef SQUIRRELJME_SCRITCHUITYPES_H
#define SQUIRRELJME_SCRITCHUITYPES_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef struct sjme_scritchui_uiComponentBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
} sjme_scritchui_uiComponentBase;

/**
 * Base data for paintable components.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiPaintableBase
{
	/** Paint listener. */
	sjme_scritchui_paintListenerFunc listener;
	
	/** Front end for paint listener. */
	sjme_frontEnd frontEnd;
	
	/** Extra data if needed. */
	sjme_intPointer extra;
} sjme_scritchui_uiPaintableBase;

typedef struct sjme_scritchui_uiPanelBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Paint related. */
	sjme_scritchui_uiPaintableBase paint;
	
	/** Is focus enabled? */
	sjme_jboolean enableFocus;
} sjme_scritchui_uiPanelBase;

typedef struct sjme_scritchui_uiWindowBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
} sjme_scritchui_uiWindowBase;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUITYPES_H */
