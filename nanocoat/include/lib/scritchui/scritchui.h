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
#include "sjme/native.h"
#include "lib/scritchinput/scritchinput.h"

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
 * Font style for pencil fonts.
 * 
 * @since 2024/06/13
 */
typedef enum sjme_scritchui_pencilFontStyle
{
	/** Bold text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_BOLD = 1,
	
	/** Italic (slanted) text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_ITALIC = 2,
	
	/** Underlined text. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_UNDERLINED = 4,
	
	/** All styles. */
	SJME_SCRITCHUI_PENCIL_FONT_STYLE_ALL =
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_BOLD |
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_ITALIC |
		SJME_SCRITCHUI_PENCIL_FONT_STYLE_UNDERLINED,
} sjme_scritchui_pencilFontStyle;

/**
 * Represents a rectangle.
 * 
 * @since 2024/04/26
 */
typedef struct sjme_scritchui_rect
{
	/** The X position. */
	sjme_jint x;
	
	/** The Y position. */
	sjme_jint y;
	
	/** The width. */
	sjme_jint width;
	
	/** The height. */
	sjme_jint height;
} sjme_scritchui_rect;

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
 * Represents a container which can contain other components.
 * 
 * @since 2024/04/20
 */
typedef struct sjme_scritchui_uiContainerBase* sjme_scritchui_uiContainer;

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
 * ScritchUI Pencil state.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilBase* sjme_scritchui_pencil;

/**
 * Font structure for ScritchUI Pencil.
 * 
 * @since 2024/05/12
 */
typedef struct sjme_scritchui_pencilFontBase* sjme_scritchui_pencilFont;

/**
 * A single link within a loaded/known font chain.
 * 
 * @since 2024/06/10
 */
typedef struct sjme_scritchui_pencilFontLink sjme_scritchui_pencilFontLink;

/**
 * Functions which are used to lock and unlock access to the backing pencil
 * buffer, if applicable.
 * 
 * @since 2024/07/08
 */
typedef struct sjme_scritchui_pencilLockFunctions
	sjme_scritchui_pencilLockFunctions;

/** Arguments to pass for setting of listeners. */
#define SJME_SCRITCHUI_SET_LISTENER_ARGS(what) \
	sjme_attrInNullable SJME_TOKEN_PASTE3(sjme_scritchui_, what, \
		ListenerFunc) inListener, \
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd

/**
 * Listener that is called when a window closes.
 * 
 * @param inState The input state.
 * @param inWindow The window being closed.
 * @return Any resultant error, @c SJME_ERROR_CANCEL_WINDOW_CLOSE is handled
 * specifically in that it will not be treated as an error however normal
 * application exit will not happen.
 * @since 2024/05/13
 */
typedef sjme_errorCode (*sjme_scritchui_closeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow);

/**
 * Listener for input events.
 * 
 * @param inState The input state.
 * @param inComponent The component this event is for.
 * @param inEvent The event which occurred.
 * @return Any resultant error, if any.
 * @since 2024/06/29
 */
typedef sjme_errorCode (*sjme_scritchui_inputListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent);

/**
 * Callback that is used to draw the given component.
 *
 * @param inState The ScritchUI state.
 * @param inComponent The component to draw on.
 * @param g The graphics used for drawing.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param special Special value for painting, may be @c 0 or any
 * other value if it is meaningful to what is being painted.
 * @return Any error as required.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_paintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
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
 * Listener that is called when the size of a component changes.
 * 
 * @param inState The input state.
 * @param inComponent The component that was resized.
 * @param newWidth The new component width.
 * @param newHeight The new component height.
 * @return On any error if applicable.
 * @since 2024/04/26
 */
typedef sjme_errorCode (*sjme_scritchui_sizeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositiveNonZero sjme_jint newWidth,
	sjme_attrInPositiveNonZero sjme_jint newHeight);

/**
 * Listener for changes in if a component becomes visible or not.
 * 
 * @param inState The input state.
 * @param inComponent The component which has its visibility changed.
 * @param fromVisible The previous visible state.
 * @param toVisible The current visible state.
 * @return Any resultant error, if any.
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_visibleListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean fromVisible,
	sjme_attrInValue sjme_jboolean toVisible);

/** Void listener function. */
typedef sjme_errorCode (*sjme_scritchui_voidListenerFunc)(void);

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
 * Repaints the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param x The X position.
 * @param y The Y position.
 * @param width The width.
 * @param height The height.
 * @return Any error code if applicable.
 * @since 2024/04/24
 */
typedef sjme_errorCode (*sjme_scritchui_componentRepaintFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

/**
 * Revalidates the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to be revalidated.
 * @return On any error if applicable.
 * @since 2024/04/21
 */
typedef sjme_errorCode (*sjme_scritchui_componentRevalidateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

/**
 * Sets the input listener for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener for events, may be @c NULL to clear
 * the existing listener.
 * @param copyFrontEnd The front end data to copy, may be @c NULL .
 * @return Any resultant error, if any.
 * @since 2024/06/29
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetInputListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input));

/**
 * Sets the paint listener for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener for paint events, may be @c NULL to clear
 * the existing listener.
 * @param copyFrontEnd The front end data to copy, may be @c NULL .
 * @return Any error code if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetPaintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint));

/**
 * Sets the listener for size events.
 * 
 * @param inState The input state.
 * @param inComponent The component to set the listener for.
 * @param inListener The listener to set to or to clear.
 * @param copyFrontEnd Any front end data to be copied.
 * @return Any resultant error, if any.
 * @since 2024/04/26
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetSizeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size));

/**
 * Sets the listener to call when the visibility of a component changes.
 * 
 * @param inState The input state.
 * @param inComponent The component to set for.
 * @param inListener The listener to use.
 * @param copyFrontEnd The front end data to use.
 * @return Any resultant error, if any.
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetVisibleListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible));

/**
 * Returns the size of the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to get the size of.
 * @param outWidth The output width.
 * @param outHeight The output height.
 * @return Any resultant error, if any.
 * @since 2024/05/12
 */
typedef sjme_errorCode (*sjme_scritchui_componentSizeFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight);

/**
 * Adds the given component to the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to place the component within.
 * @param addComponent The component to add to the container.
 * @return Any error code if applicable.
 * @since 2024/04/20
 */
typedef sjme_errorCode (*sjme_scritchui_containerAddFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent);

/**
 * Sets the bounds of a component within the container.
 * 
 * @param inState The input state.
 * @param inContainer The container to set the component within.
 * @param inComponent The component to be placed and resized.
 * @param x The X position.
 * @param y The Y position.
 * @param width The width.
 * @param height The height.
 * @return Any error code if applicable.
 * @since 2024/04/28
 */
typedef sjme_errorCode (*sjme_scritchui_containerSetBoundsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

/**
 * Returns the default built-in font.
 * 
 * @param inState The input state.
 * @param outFont The resultant font.
 * @return Any resultant error, if any.
 * @since 2024/06/12
 */
typedef sjme_errorCode (*sjme_scritchui_fontBuiltinFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont);

/**
 * Derives a new font from an existing font.
 * 
 * @param inState The input state.
 * @param inFont The input font to derive.
 * @param inStyle The style to switch to.
 * @param inPixelSize The pixel size to use.
 * @param outDerived The resultant derived font.
 * @return Any resultant error, if any.
 * @since 2024/06/14
 */
typedef sjme_errorCode (*sjme_scritchui_fontDeriveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inState The UI state.
 * @param OutPencil The resultant pencil.
 * @param pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_hardwareGraphicsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh);

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
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus);

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
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen));

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
 * Sets the minimum content size for windows.
 * 
 * @param inState The input state.
 * @param inWindow The window to set the minimum content size for.
 * @param width The width to set.
 * @param height The height to set.
 * @return Any error code if applicable, such as if the width and/or height
 * are zero or negative.
 * @since 2024/04/21
 */
typedef sjme_errorCode (*sjme_scritchui_windowContentMinimumSizeFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
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

/**
 * Sets the close listener for a window.
 * 
 * @param inState The input state.
 * @param inWindow The window to set for.
 * @param inListener The listener to use.
 * @param copyFrontEnd The front end data to use.
 * @return Any resultant error, if any.
 * @since 2024/05/13
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetCloseListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close));

/**
 * Sets the visibility of a window.
 * 
 * @param inState The input state.
 * @param inWindow The input window.
 * @param isVisible Should the window be visible?
 * @return Any error code if applicable.
 * @since 2024/04/21
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetVisibleFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

/** Quicker API declaration in struct. */
#define SJME_SCRITCHUI_QUICK_API(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_, x, Func) x

struct sjme_scritchui_apiFunctions
{
	/** API flags. */
	SJME_SCRITCHUI_QUICK_API(apiFlags);
	
	/** Repaints the given component. */
	SJME_SCRITCHUI_QUICK_API(componentRepaint);
	
	/** Revalidates the given component. */
	SJME_SCRITCHUI_QUICK_API(componentRevalidate);
	
	/** Sets the input listener for a component. */
	SJME_SCRITCHUI_QUICK_API(componentSetInputListener);
	
	/** Sets the paint listener for a component. */
	SJME_SCRITCHUI_QUICK_API(componentSetPaintListener);
	
	/** Sets the listener for component size events. */
	SJME_SCRITCHUI_QUICK_API(componentSetSizeListener);
	
	/** Sets the listener for component visible events. */
	SJME_SCRITCHUI_QUICK_API(componentSetVisibleListener);

	/** Get size of component. */
	SJME_SCRITCHUI_QUICK_API(componentSize);
	
	/** Adds component to container. */
	SJME_SCRITCHUI_QUICK_API(containerAdd);
	
	/** Set bounds of component in a container. */
	SJME_SCRITCHUI_QUICK_API(containerSetBounds);

	/** Returns the default built-in font. */
	SJME_SCRITCHUI_QUICK_API(fontBuiltin);
	
	/** Derive a similar font. */
	SJME_SCRITCHUI_QUICK_API(fontDerive);
	
	/** Hardware graphics support on arbitrary buffers. */
	SJME_SCRITCHUI_QUICK_API(hardwareGraphics);
	
	/** Execute callback within the event loop. */
	SJME_SCRITCHUI_QUICK_API(loopExecute);
	
	/** Execute callback later in the event loop. */
	sjme_scritchui_loopExecuteFunc loopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Is the current thread in the loop? */
	SJME_SCRITCHUI_QUICK_API(loopIsInThread);
	
	/** Iterates a single run of the event loop. */
	SJME_SCRITCHUI_QUICK_API(loopIterate);
	
	/** Enable focus on a panel. */
	SJME_SCRITCHUI_QUICK_API(panelEnableFocus);
	
	/** Creates a new panel. */
	SJME_SCRITCHUI_QUICK_API(panelNew);
	
	/** Register listener. */
	SJME_SCRITCHUI_QUICK_API(screenSetListener);
	
	/** Screens available. */
	SJME_SCRITCHUI_QUICK_API(screens);
	
	/** Sets minimum size of the window contents. */
	SJME_SCRITCHUI_QUICK_API(windowContentMinimumSize);
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_API(windowNew);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetCloseListener);
	
	/** Sets visibility of window. */
	SJME_SCRITCHUI_QUICK_API(windowSetVisible);
};

#undef SJME_SCRITCHUI_QUICK_API

/**
 * Opaque internal implementation functions.
 * 
 * @since 2024/05/14
 */
typedef struct sjme_scritchui_implInternFunctions
	sjme_scritchui_implInternFunctions;

/**
 * Common data structure shared by everything.
 * 
 * @since 2024/04/02
 */
typedef struct sjme_scritchui_commonBase
{
	/** The type of what this is. */
	sjme_scritchui_uiType type;
	
	/** The state which owns this. */
	sjme_scritchui state;
	
	/**
	 * Front-end data for this, note that ScritchUI implementations must not
	 * use this for information as this is only to be used by front-ends.
	 */
	sjme_frontEnd frontEnd;
	
	/** Opaque native handle for this. */
	sjme_scritchui_handle handle;
	
	/** Secondary opaque native handle for this, as needed. */
	sjme_scritchui_handle handleB;
} sjme_scritchui_commonBase;

/**
 * Window manager details to use.
 * 
 * @since 2024/04/24
 */
typedef struct sjme_scritchui_wmInfo
{
	/** Default title. */
	sjme_lpcstr defaultTitle;
	
	/** X Window System Class. */
	sjme_lpcstr xwsClass;
} sjme_scritchui_wmInfo;

struct sjme_scritchui_stateBase
{
	/** Common data. */
	sjme_scritchui_commonBase common;
	
	/** Window manager information. */
	const sjme_scritchui_wmInfo* wmInfo;
	
	/** API functions to use. */
	const sjme_scritchui_apiFunctions* api;
	
	/** In thread API functions. */
	const sjme_scritchui_apiFunctions* apiInThread;
	
	/** Internal implementation functions to use. */
	const sjme_scritchui_internFunctions* intern;
	
	/** Implementation functions to use. */
	const sjme_scritchui_implFunctions* impl;
	
	/** Internal implementation functions, which are opaque. */
	const sjme_scritchui_implInternFunctions* implIntern;
	
	/** The allocation pool to use for allocations. */
	sjme_alloc_pool* pool;
	
	/** The event loop thread, if applicable. */
	sjme_thread loopThread;
	
	/** Loop thread initializer if one was passed. */
	sjme_thread_mainFunc loopThreadInit;
	
	/** The available screens. */
	sjme_list_sjme_scritchui_uiScreen* screens;
	
	/** The window manager type used. */
	sjme_scritchui_windowManagerType wmType;
	
	/** The internal built-in font. */
	sjme_scritchui_pencilFont builtinFont; 
	
	/** The fonts which are loaded and known to the state. */
	sjme_scritchui_pencilFontLink* fontChain;
	
	/** Function to obtain the current nanotime, for input events. */
	sjme_nal_nanoTimeFunc nanoTime;
};

/* If dynamic libraries are not supported, we cannot do this. */
#if !defined(SJME_CONFIG_SCRITCHUI_NO_DYLIB)

/**
 * Initializes the API through the dynamic library.
 * 
 * @param inPool The pool to allocate within.
 * @param loopExecute Optional callback for loop execution, may be @c NULL ,
 * the passed argument is always the state.
 * @param initFrontEnd Optional initial front end data.
 * @param outState The resultant newly created ScritchUI state.
 * @return Any error code that may occur.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_dylibApiFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
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
