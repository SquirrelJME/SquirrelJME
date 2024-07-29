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
 * @since 2024/07/29
 */

#ifndef SQUIRRELJME_SCRITCHUITYPESLISTENER_H
#define SQUIRRELJME_SCRITCHUITYPESLISTENER_H

#include "lib/scritchui/scritchui.h"

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
 * Represents a class for a listener for common operation.
 * 
 * @since 2024/04/28
 */
typedef enum sjme_scritchui_listenerClass
{
	/** User based listener. */
	SJME_SCRITCHUI_LISTENER_USER = 0,
	
	/** Core based listener. */
	SJME_SCRITCHUI_LISTENER_CORE = 1,
	
	/** The number of listener classes. */
	SJME_NUM_SCRITCHUI_LISTENER = 2,
} sjme_scritchui_listenerClass;

/** Declares a ScritchUI listener set. */
#define SJME_SCRITCHUI_LISTENER_DECLARE(what) \
	typedef struct SJME_TOKEN_PASTE(sjme_scritchui_listener_, what) \
	{ \
		/** Front end data. */ \
		sjme_frontEnd frontEnd; \
		 \
		/** Extra data as required. */ \
		sjme_intPointer extra; \
		 \
		/** Listener callback. */ \
		SJME_TOKEN_PASTE3(sjme_scritchui_, what, ListenerFunc) callback; \
	} SJME_TOKEN_PASTE(sjme_scritchui_listener_, what)

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

#endif /* SQUIRRELJME_SCRITCHUITYPESLISTENER_H */
