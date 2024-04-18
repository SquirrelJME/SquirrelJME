/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Library Header.
 * 
 * @since 2024/03/27
 */

#ifndef SQUIRRELJME_SCRITCHUI_H
#define SQUIRRELJME_SCRITCHUI_H

#include "sjme/config.h"
#include "sjme/multithread.h"
#include "sjme/gfxConst.h"
#include "sjme/nvm.h"
#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents the type that this is.
 * 
 * @since 2024/04/02
 */
typedef enum sjme_scritchui_uiType
{
	/** Reserved. */
	SJME_SCRITCHUI_TYPE_RESERVED,
	
	/** Panel. */
	SJME_SCRITCHUI_TYPE_PANEL,
	
	/** Screen. */
	SJME_SCRITCHUI_TYPE_SCREEN,
	
	/** Window. */
	SJME_SCRITCHUI_TYPE_WINDOW,
	
	/** The number of possible types. */
	SJME_NUM_SCRITCHUI_UI_TYPES
} sjme_scritchui_uiType;

/**
 * An opaque native handle.
 * 
 * @since 2024/04/02
 */
typedef void* sjme_scritchui_handle;

/**
 * API Flags for ScritchUI.
 * 
 * @since 2024/03/29
 */
typedef enum sjme_scritchui_apiFlag
{
	/** Only panels are supported for this interface. */
	SJME_SCRITCHUI_API_FLAG_PANEL_ONLY = 1,
} sjme_scritchui_apiFlag;

/**
 * Which type of screen update has occurred?
 * 
 * @since 2024/04/09
 */
typedef enum sjme_scritchui_screenUpdateType
{
	/** Unknown. */
	SJME_SCRITCHUI_SCREEN_UPDATE_UNKNOWN,
	
	/** New screen. */
	SJME_SCRITCHUI_SCREEN_UPDATE_NEW,
	
	/** Deleted screen. */
	SJME_SCRITCHUI_SCREEN_UPDATE_DELETED,
	
	/** Updated screen (resolution, color, etc.) */
	SJME_SCRITCHUI_SCREEN_UPDATE_CHANGED,
	
	/** The number of update types. */
	SJME_SCRITCHUI_NUM_SCREEN_UPDATE
} sjme_scritchui_screenUpdateType;

/**
 * The type of window manager that is used.
 * 
 * @since 2024/04/15
 */
typedef enum sjme_scritchui_windowManagerType
{
	/** One frame per screen. */
	SJME_SCRITCHUI_WM_TYPE_ONE_FRAME_PER_SCREEN = 0,
	
	/** Standard desktop interface. */
	SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP = 1,
	
	/** The number of window manager types. */
	SJME_SCRITCHUI_NUM_WM_TYPES
} sjme_scritchui_windowManagerType;

/**
 * ScritchUI state.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_stateBase* sjme_scritchui;

/**
 * ScritchUI API functions, implemented by a native library accordingly.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_apiFunctions sjme_scritchui_apiFunctions;

/**
 * ScritchUI implementation functions.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_implFunctions sjme_scritchui_implFunctions;

/**
 * Internal ScritchUI API functions.
 * 
 * @since 2024/04/15
 */
typedef struct sjme_scritchui_internFunctions sjme_scritchui_internFunctions;

/**
 * Component within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiComponentBase* sjme_scritchui_uiComponent;

/**
 * Base paintable for ScritchUI.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiPaintableBase* sjme_scritchui_uiPaintable;

/**
 * A panel within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiPanelBase* sjme_scritchui_uiPanel;

/**
 * A single monitor screen on the display for ScritchUI.
 * 
 * @since 2024/04/06
 */
typedef struct sjme_scritchui_uiScreenBase* sjme_scritchui_uiScreen;

/** A list of screens. */
SJME_LIST_DECLARE(sjme_scritchui_uiScreen, 0);

/**
 * A window within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiWindowBase* sjme_scritchui_uiWindow;

/**
 * Callback that is used to draw the given component.
 *
 * @param inState The ScritchUI state.
 * @param component The component to draw on.
 * @param pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param buf The target buffer to draw to, this is cast to the correct
 * buffer format.
 * @param bufOff The offset to the start of the buffer.
 * @param bufLen The length of @c buf .
 * @param pal The color palette, may be @c NULL .
 * @param numPal The number of colors in the palette, may be @c 0 if
 * the argument @c pal is @c NULL .
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param special Special value for painting, may be @c 0 or any
 * other value if it is meaningful to what is being painted.
 * @return Any error as required.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_paintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent component,
	sjme_attrInNotNull sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint bw,
	sjme_attrInPositive sjme_jint bh,
	sjme_attrInNotNull const void* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufLen,
	sjme_attrInNullable const sjme_jint* pal,
	sjme_attrInPositive sjme_jint numPal,
	sjme_attrInPositive sjme_jint sx,
	sjme_attrInPositive sjme_jint sy,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special);

/**
 * Listener callback for when a screen has been queried or it has been
 * updated.
 * 
 * @param inState The input state.
 * @param updateType The type of update this is for.
 * @param inScreen The screen that has been updated.
 * @return Any error code if applicable.
 * @since 2024/04/09
 */
typedef sjme_errorCode (*sjme_scritchui_screenListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_scritchui_screenUpdateType updateType,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen);

/**
 * Obtains the flags which describe the interface.
 * 
 * @param inState The input ScritchUI state.
 * @param outFlags The output flags for this interface.
 * @return Any error code if applicable.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_apiFlagsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_jint outFlags);

/**
 * Initializes the native UI interface needed by ScritchUI.
 * 
 * @param inPool The allocation pool to use.
 * @param outState The resultant state.
 * @return Any error code if applicable.
 * @since 2024/03/27
 */
typedef sjme_errorCode (*sjme_scritchui_apiInitFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull const sjme_scritchui_apiFunctions* inApiFunc,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInOutNotNull sjme_scritchui* outState);

/**
 * Sets the paint listener for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener for paint events, may be @c NULL to clear
 * the existing listener.
 * @param copyFrontEnd The front end data to copy, may be @c NULL .
 * @return Any error if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetPaintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd);

/**
 * Execute the given callback within the event loop of the GUI.
 * 
 * @param inState The input state.
 * @param callback The callback to execute.
 * @param anything A value that can be passed to the listener.
 * @return Any error code if applicable.
 * @since 2024/04/09
 */
typedef sjme_errorCode (*sjme_scritchui_loopExecuteFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

/**
 * Determines whether the current thread is in the event loop or not.
 * 
 * @param inState The input state.
 * @param outInThread The result of whether this is in the event loop.
 * @return Any error code if applicable.
 * @since 2024/04/09
 */
typedef sjme_errorCode (*sjme_scritchui_loopIsInThreadFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_jboolean* outInThread);

/**
 * Iterates a single run of the event loop.
 * 
 * @param inState The input ScritchUI state.
 * @param outHasTerminated Has the GUI interface terminated?
 * @return Any error code if applicable.
 * @since 2024/04/02
 */
typedef sjme_errorCode (*sjme_scritchui_loopIterateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);

/**
 * Enables or disables focus on a panel.
 * 
 * @param inState The input state.
 * @param inPanel The input panel.
 * @param enableFocus Should focus be enabled?
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_panelEnableFocusFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus);

/**
 * Creates a new panel.
 * 
 * @param inState The input state.
 * @param outPanel The resultant panel.
 * @return Any error code if applicable.
 * @since 2024/04/02
 */
typedef sjme_errorCode (*sjme_scritchui_panelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel);

/**
 * Sets the screen listener callback for screen changes.
 * 
 * @param inState The input state.
 * @param callback The callback for screen information and changes.
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_screenSetListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_screenListenerFunc callback);

/**
 * Obtains and queries the screens which are attached to the system displays.
 * 
 * @param inState The input state.
 * @param outScreens The resultant screens.
 * @param inOutNumScreens The number of screens for input and output.
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_screensFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens);
	
/**
 * Creates a new window.
 * 
 * @param inState The input state.
 * @param outWindow The resultant newly created window.
 * @return Any error code if applicable.
 * @since 2024/04/16
 */
typedef sjme_errorCode (*sjme_scritchui_windowNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow);

struct sjme_scritchui_apiFunctions
{
	/** API flags. */
	sjme_scritchui_apiFlagsFunc apiFlags;
	
	/** Sets the paint listener for a component. */
	sjme_scritchui_componentSetPaintListenerFunc componentSetPaintListener;
	
	/** Execute callback within the event loop. */
	sjme_scritchui_loopExecuteFunc loopExecute;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Is the current thread in the loop? */
	sjme_scritchui_loopIsInThreadFunc loopIsInThread;
	
	/** Iterates a single run of the event loop. */
	sjme_scritchui_loopIterateFunc loopIterate;
	
	/** Enable focus on a panel. */
	sjme_scritchui_panelEnableFocusFunc panelEnableFocus;
	
	/** Creates a new panel. */
	sjme_scritchui_panelNewFunc panelNew;
	
	/** Register listener. */
	sjme_scritchui_screenSetListenerFunc screenSetListener;
	
	/** Screens available. */
	sjme_scritchui_screensFunc screens;
	
	/** Creates a new window. */
	sjme_scritchui_windowNewFunc windowNew;
};

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
	
	/** API functions to use. */
	const sjme_scritchui_apiFunctions* api;
	
	/** In thread API functions. */
	const sjme_scritchui_apiFunctions* apiInThread;
	
	/** Implementation functions to use. */
	const sjme_scritchui_implFunctions* impl;
	
	/** Internal implementation functions to use. */
	const sjme_scritchui_internFunctions* intern;
	
	/** The allocation pool to use for allocations. */
	sjme_alloc_pool* pool;
	
	/** The event loop thread, if applicable. */
	sjme_thread loopThread;
	
	/** The available screens. */
	sjme_list_sjme_scritchui_uiScreen* screens;
	
	/** The window manager type used. */
	sjme_scritchui_windowManagerType wmType;
};

/* If dynamic libraries are not supported, we cannot do this. */
#if !defined(SJME_CONFIG_SCRITCHUI_NO_DYLIB)

/**
 * Initializes the API through the dynamic library.
 * 
 * @param inPool The pool to allocate within.
 * @param outState The resultant newly created ScritchUI state.
 * @return Any error code that may occur.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_dylibApiFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState);

/** The name of the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_NAME(x) \
	"squirreljme-scritchui-" SJME_TOKEN_STRING_PP(x)

/** The path name for the dynamic library for ScritchUI. */
#define SJME_SCRITCHUI_DYLIB_PATHNAME(x) \
	SJME_CONFIG_DYLIB_PATHNAME(SJME_SCRITCHUI_DYLIB_NAME(x))

/** The symbol to use with @c sjme_scritchui_dylibApiFunc . */
#define SJME_SCRITCHUI_DYLIB_SYMBOL(x) \
	SJME_TOKEN_PASTE(sjme_scritchui_dylibApi, x)
		
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUI_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUI_H */
