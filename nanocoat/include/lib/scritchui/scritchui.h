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
#include "sjme/gfxConst.h"
#include "sjme/nvm.h"

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
 * Component within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiComponentBase* sjme_scritchui_uiComponent;

/**
 * A panel within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiPanelBase* sjme_scritchui_uiPanel;

/**
 * A window within ScritchUI.
 * 
 * @since 2024/03/27
 */
typedef struct sjme_scritchui_uiWindowBase* sjme_scritchui_uiWindow;

/**
 * Callback that is used to draw the given component.
 *
 * @param __component The component to draw on.
 * @param __pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param __bw The buffer width, this is the scanline width of the buffer.
 * @param __bh The buffer height.
 * @param __buf The target buffer to draw to, this is cast to the correct
 * buffer format.
 * @param __bufOff The offset to the start of the buffer.
 * @param __bufLen The length of @c __buf .
 * @param __pal The color palette, may be @c NULL .
 * @param __numPal The number of colors in the palette, may be @c 0 if
 * the argument @c __pal is @c NULL .
 * @param __sx Starting surface X coordinate.
 * @param __sy Starting surface Y coordinate.
 * @param __sw Surface width.
 * @param __sh Surface height.
 * @param __special Special value for painting, may be @c 0 or any
 * other value if it is meaningful to what is being painted.
 * @return Any error as required.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_paintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui_uiComponent __component,
	sjme_attrInNotNull sjme_gfx_pixelFormat __pf,
	sjme_attrInPositive sjme_jint __bw,
	sjme_attrInPositive sjme_jint __bh,
	sjme_attrInNotNull const void* __buf,
	sjme_attrInPositive sjme_jint __bufOff,
	sjme_attrInPositive sjme_jint __bufLen,
	sjme_attrInNullable const sjme_jint* __pal,
	sjme_attrInPositive sjme_jint __numPal,
	sjme_attrInPositive sjme_jint __sx,
	sjme_attrInPositive sjme_jint __sy,
	sjme_attrInPositive sjme_jint __sw,
	sjme_attrInPositive sjme_jint __sh,
	sjme_attrInValue sjme_jint __special);

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
 * @return Any error if applicable.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetPaintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener);

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

struct sjme_scritchui_apiFunctions
{
	/** API flags. */
	sjme_scritchui_apiFlagsFunc apiFlags;
	
	/** Initialize the framework library. */
	sjme_scritchui_apiInitFunc apiInit;
	
	/** Sets the paint listener for a component. */
	sjme_scritchui_componentSetPaintListenerFunc componentSetPaintListener;
	
	/** Iterates a single run of the event loop. */
	sjme_scritchui_loopIterateFunc loopIterate;
	
	/** Creates a new panel. */
	sjme_scritchui_panelNewFunc panelNew;
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
	
	/** Implementation functions to use. */
	const sjme_scritchui_implFunctions* impl;
	
	/** The allocation pool to use for allocations. */
	sjme_alloc_pool* pool;
};

/* If dynamic libraries are not supported, we cannot do this. */
#if !defined(SJME_CONFIG_SCRITCHUI_NO_DYLIB)

/**
 * Function pointer type for obtaining the ScritchUI API functions from
 * a dynamic library.
 * 
 * @param outApi Output Core API functions.
 * @param outImpl Output Implementation functions.
 * @return Any error code that may occur.
 * @since 2024/03/29
 */
typedef sjme_errorCode (*sjme_scritchui_dylibApiFunc)(
	sjme_attrInOutNotNull const sjme_scritchui_apiFunctions** outApi,
	sjme_attrInOutNotNull const sjme_scritchui_implFunctions** outImpl);

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
