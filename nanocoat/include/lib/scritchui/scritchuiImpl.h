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
 * Implementation specific initialization.
 * 
 * @param inState The state being initialized.
 * @return Any resultant error, if any.
 * @since 2024/04/15
 */
typedef sjme_errorCode (*sjme_scritchui_impl_apiInit)(
	sjme_attrInNotNull sjme_scritchui inState);

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
typedef sjme_errorCode (*sjme_scritchui_impl_componentSetPaintListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_attrInNotNull sjme_scritchui_uiPaintable inPaint,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd);

/**
 * Adds the given component to the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to place the component within.
 * @param inComponent The component to add to the container.
 * @return Any error code if applicable.
 * @since 2024/04/20
 */
typedef sjme_errorCode (*sjme_scritchui_impl_containerAddFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

/** Enables or disables focus on a panel. */
typedef sjme_scritchui_panelEnableFocusFunc
	sjme_scritchui_impl_panelEnableFocusFunc;

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

/** Obtain screens. */
typedef sjme_scritchui_screensFunc
	sjme_scritchui_impl_screensFunc;

/** Minimum size for contents in a window. */
typedef sjme_scritchui_windowContentMinimumSizeFunc
	sjme_scritchui_impl_windowContentMinimumSizeFunc;

/** Creates a new window. */
typedef sjme_scritchui_windowNewFunc
	sjme_scritchui_impl_windowNewFunc;

struct sjme_scritchui_implFunctions
{
	/** Initialize implementation API instance. */
	sjme_scritchui_impl_apiInit apiInit;
	
	/** Set paint listener for component. */
	sjme_scritchui_impl_componentSetPaintListenerFunc
		componentSetPaintListener;
	
	/** Add component to container. */
	sjme_scritchui_impl_containerAddFunc containerAdd;
	
	/** Execute callback within the event loop. */
	sjme_scritchui_loopExecuteFunc loopExecute;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Enable/disable focus on a panel. */
	sjme_scritchui_impl_panelEnableFocusFunc panelEnableFocus;
	
	/** Creates a new native panel. */
	sjme_scritchui_impl_panelNewFunc panelNew;
	
	/** The available screens. */
	sjme_scritchui_impl_screensFunc screens;
	
	/** Set minimum size of content window. */
	sjme_scritchui_impl_windowContentMinimumSizeFunc windowContentMinimumSize;
	
	/** Creates a new window. */
	sjme_scritchui_impl_windowNewFunc windowNew;
};

/**
 * Returns the container for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outContainer The resultant container.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/04/20 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getContainerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiContainer* outContainer);

/**
 * Returns the container for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outContainer The resultant container.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/04/20 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getPaintableFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiPaintable* outPaintable);

/**
 * Maps the given screen internally.
 * 
 * @param inState The input state.
 * @param screenId The screen ID to map.
 * @param outScreen The resultant screen, may be newly created or one that
 * already exists.
 * @param updateHandle If not @c NULL then the handle is updated to this.
 * @return Any resultant error, if any.
 * @since 2024/04/15
 */
typedef sjme_errorCode (*sjme_scritchui_intern_mapScreenFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jint screenId,
	sjme_attrInOutNotNull sjme_scritchui_uiScreen* outScreen,
	sjme_attrInNullable sjme_scritchui_handle updateHandle);

struct sjme_scritchui_internFunctions
{
	/** Returns the container for the given component. */
	sjme_scritchui_intern_getContainerFunc getContainer;
	
	/** Returns the paintable for the given component. */
	sjme_scritchui_intern_getPaintableFunc getPaintable;
		
	/** Maps the given screen. */
	sjme_scritchui_intern_mapScreenFunc mapScreen;
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
