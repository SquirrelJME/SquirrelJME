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

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Common data structure shared by everything.
 * 
 * @since 2024/04/02
 */
typedef struct sjme_scritchui_commonBase
{
	/** The type of what this is. */
	sjme_scritchui_uiType type;
	
	/**
	 * Front-end data for this, note that ScritchUI implementations must not
	 * use this for information as this is only to be used by front-ends.
	 */
	sjme_frontEnd frontEnd;
	
	/** Opaque native handle for this. */
	sjme_scritchui_handle handle;
} sjme_scritchui_commonBase;

struct sjme_scritchui_stateBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
	
	/** The allocation pool to use for allocations. */
	sjme_alloc_pool* pool;
};

typedef struct sjme_scritchui_uiComponentBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
} sjme_scritchui_uiComponentBase;

typedef struct sjme_scritchui_uiPanelBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
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
