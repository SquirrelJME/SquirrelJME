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
#include "lib/scritchui/scritchuiPencil.h"
#include "sjme/atomic.h"

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
 * Represents a class for a listener for common operation.
 * 
 * @since 2024/04/28
 */
typedef enum sjme_scritchui_listenerClass
{
	/** User based listener. */
	SJME_SCRITCHUI_LISTENER_USER,
	
	/** Core based listener. */
	SJME_SCRITCHUI_LISTENER_CORE,
	
	/** The number of listener classes. */
	SJME_NUM_SCRITCHUI_LISTENER,
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

/** Size listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(size);

/** Paint listener. */
SJME_SCRITCHUI_LISTENER_DECLARE(paint);

/**
 * Base pencil drawing structure.
 * 
 * @since 2024/05/04
 */
typedef struct sjme_scritchui_pencilBase
{
	/** The current state of the pencil. */
	sjme_scritchui_pencilState state;
	
	/** External API. */
	const sjme_scritchui_pencilFunctions* api;
	
	/** Implementation API. */
	const sjme_scritchui_pencilImplFunctions* impl;
	
	/** Front end information for paint. */
	sjme_frontEnd frontEnd;
} sjme_scritchui_pencilBase;

/**
 * Listeners for components.
 * 
 * @since 2024/04/28
 */
typedef struct sjme_scritchui_uiComponentListeners
{
	/** Listener for when size changes. */
	sjme_scritchui_listener_size size;
} sjme_scritchui_uiComponentListeners;

typedef struct sjme_scritchui_uiComponentBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
	
	/** The parent of this component. */
	sjme_scritchui_uiComponent parent;
	
	/** User and core listeners for the component. */
	sjme_scritchui_uiComponentListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
} sjme_scritchui_uiComponentBase;

/**
 * Base data for containers which may contain components.
 * 
 * @since 2024/04/20
 */
typedef struct sjme_scritchui_uiContainerBase
{
	/** Todo. */
	sjme_jint todo;
} sjme_scritchui_uiContainerBase; 

typedef struct sjme_scritchui_uiPaintableListeners
{
	/** Paint listener. */
	sjme_scritchui_listener_paint paint;
} sjme_scritchui_uiPaintableListeners;

/**
 * Base data for paintable components.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiPaintableBase
{
	/** Listeners. */
	sjme_scritchui_uiPaintableListeners listeners[SJME_NUM_SCRITCHUI_LISTENER];
	
	/** Extra data if needed. */
	sjme_intPointer extra;
	
	/** Is this currently in paint? */
	sjme_atomic_sjme_jint inPaint;
	
	/** Belayed painting. */
	sjme_scritchui_rect belayRect;
	
	/** Last error while in paint. */
	sjme_errorCode lastError;
	
	/** Pencil drawing information. */
	sjme_scritchui_pencilBase pencil;
} sjme_scritchui_uiPaintableBase;

typedef struct sjme_scritchui_uiPanelBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Container related. */
	sjme_scritchui_uiContainerBase container;
	
	/** Paint related. */
	sjme_scritchui_uiPaintableBase paint;
	
	/** Is focus enabled? */
	sjme_jboolean enableFocus;
} sjme_scritchui_uiPanelBase;

typedef struct sjme_scritchui_uiScreenBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
	
	/** The screen Id. */
	sjme_jint id;
	
	/** Generic display handle such as for X11. */
	sjme_scritchui_handle displayHandle;
} sjme_scritchui_uiScreenBase;

typedef struct sjme_scritchui_uiWindowBase
{
	/** Common data. */
	sjme_scritchui_uiComponentBase component;
	
	/** Container related. */
	sjme_scritchui_uiContainerBase container;
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
