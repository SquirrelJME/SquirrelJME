/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI listener types and definitions.
 * 
 * @file
 * @since 2024/07/29
 */

#ifndef SJME_C_SCRITCHUILISTENER_H
#define SJME_C_SCRITCHUILISTENER_H

#include "sjme/tokenUtils.h"
#include "sjme/frontEnd.h"
#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiFuncs.h"
#include "lib/scritchui/scritchuiTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUITYPESLISTENER_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

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

/** Declares a ScritchUI listener set. */
#define SJME_SCRITCHUI_LISTENER_DECLARE(what) \
	struct SJME_TOKEN_PASTE(sjme_scritchui_listener_, what) \
	{ \
		/** Front end data. */ \
		sjme_frontEndBindable frontEnd; \
		 \
		/** Extra data as required. */ \
		sjme_intPointer extra; \
		 \
		/** Listener callback. */ \
		SJME_TOKEN_PASTE3(sjme_scritchui_, what, ListenerFunc) callback; \
	}

/** Void listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(void);

/** Activate choice item. */
SJME_SCRITCHUI_LISTENER_DECLARE(activate);

/** Choice items updated, before or after. */
SJME_SCRITCHUI_LISTENER_DECLARE(valueUpdate);

/** Close listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(close);

/** Input listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(input);

/** Menu item is activated. */
SJME_SCRITCHUI_LISTENER_DECLARE(menuItemActivate);

/** Paint listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(paint);

/** Size listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(size);

/** Suggest size listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(sizeSuggest);

/** View listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(view);

/** Visible listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(visible);

/* No longer needed .*/
#undef SJME_SCRITCHUI_LISTENER_DECLARE

/**
 * Listeners for components.
 * 
 * @since 2024/04/28
 */
typedef struct sjme_scritchui_uiComponentListeners
{
	/** Component activated. */
	sjme_scritchui_listener_activate activate;
	
	/** Input events. */
	sjme_scritchui_listener_input input;
	
	/** Listener for when size changes. */
	sjme_scritchui_listener_size size;
	
	/** The value of the component has changed. */
	sjme_scritchui_listener_valueUpdate valueUpdate;
	
	/** Visibility changes. */
	sjme_scritchui_listener_visible visible; 
} sjme_scritchui_uiComponentListeners;

typedef struct sjme_scritchui_uiPaintableListeners
{
	/** Paint listener. */
	sjme_scritchui_listener_paint paint;
} sjme_scritchui_uiPaintableListeners;

typedef struct sjme_scritchui_uiViewListeners
{
	/** Component size suggestion listener. */
	sjme_scritchui_listener_sizeSuggest sizeSuggest;
	
	/** Viewport listener. */
	sjme_scritchui_listener_view view;
} sjme_scritchui_uiViewListeners;

typedef struct sjme_scritchui_uiWindowListeners
{
	/** Listener for when a window is closed. */
	sjme_scritchui_listener_close close;
	
	/** Listener for when a menu item is activated. */
	sjme_scritchui_listener_menuItemActivate menuItemActivate;
} sjme_scritchui_uiWindowListeners;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPESLISTENER_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUITYPESLISTENER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUITYPESLISTENER_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHUILISTENER_H */
