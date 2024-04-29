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

/** Core listeners. */
#define SJME_SCRITCHUI_LISTENER_CORE(x) \
	((x)->listeners[SJME_SCRITCHUI_LISTENER_CORE])

/** User listeners. */
#define SJME_SCRITCHUI_LISTENER_USER(x) \
	((x)->listeners[SJME_SCRITCHUI_LISTENER_USER])

/**
 * Implementation specific initialization.
 * 
 * @param inState The state being initialized.
 * @return Any resultant error, if any.
 * @since 2024/04/15
 */
typedef sjme_errorCode (*sjme_scritchui_impl_apiInitFunc)(
	sjme_attrInNotNull sjme_scritchui inState);

/** Repaint component. */
typedef sjme_scritchui_componentRepaintFunc
	sjme_scritchui_impl_componentRepaintFunc;

/** Revalidate component. */
typedef sjme_scritchui_componentRevalidateFunc
	sjme_scritchui_impl_componentRevalidateFunc;

/** Set the paint listener for the given component. */
typedef sjme_scritchui_componentSetPaintListenerFunc
	sjme_scritchui_impl_componentSetPaintListenerFunc;

/** Set size listener for components. */
typedef sjme_scritchui_componentSetSizeListenerFunc
	sjme_scritchui_impl_componentSetSizeListenerFunc;

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

/** Set bounds of component in container. */
typedef sjme_scritchui_containerSetBoundsFunc
	sjme_scritchui_impl_containerSetBoundsFunc;

/** Loop execution function. */
typedef sjme_scritchui_loopExecuteFunc 
	sjme_scritchui_impl_loopExecuteFunc;

/** Enables or disables focus on a panel. */
typedef sjme_scritchui_panelEnableFocusFunc
	sjme_scritchui_impl_panelEnableFocusFunc;

/**
 * Creates a new native panel.
 * 
 * @param inState The input ScritchUI state.
 * @param inPanel The panel that was created.
 * @return Any error code as per implementation.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_impl_panelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel);

/** Obtain screens. */
typedef sjme_scritchui_screensFunc
	sjme_scritchui_impl_screensFunc;

/** Minimum size for contents in a window. */
typedef sjme_scritchui_windowContentMinimumSizeFunc
	sjme_scritchui_impl_windowContentMinimumSizeFunc;

/**
 * Creates a new window.
 * 
 * @param inState The input state.
 * @param inWindow The window that was created.
 * @return Any resultant error, if any.
 * @since 2024/04/24
 */
typedef sjme_errorCode (*sjme_scritchui_impl_windowNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow);

/** Sets visibility of window. */
typedef sjme_scritchui_windowSetVisibleFunc
	sjme_scritchui_impl_windowSetVisibleFunc;

#define SJME_SCRITCHUI_QUICK_IMPL(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_impl_, x, Func) x

struct sjme_scritchui_implFunctions
{
	/** Initialize implementation API instance. */
	SJME_SCRITCHUI_QUICK_IMPL(apiInit);
	
	/** Repaint component. */
	SJME_SCRITCHUI_QUICK_IMPL(componentRepaint);
	
	/** Revalidate component. */
	SJME_SCRITCHUI_QUICK_IMPL(componentRevalidate);
	
	/** Set paint listener for component. */
	SJME_SCRITCHUI_QUICK_IMPL(componentSetPaintListener);

	/** Set size listener for component. */
	SJME_SCRITCHUI_QUICK_IMPL(componentSetSizeListener);
	
	/** Add component to container. */
	SJME_SCRITCHUI_QUICK_IMPL(containerAdd);
	
	/** Set bounds of component in container. */
	SJME_SCRITCHUI_QUICK_IMPL(containerSetBounds);
	
	/** Execute callback within the event loop or schedule later. */
	SJME_SCRITCHUI_QUICK_IMPL(loopExecute);
	
	/** Execute call later in the loop. */
	sjme_scritchui_loopExecuteFunc loopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Enable/disable focus on a panel. */
	SJME_SCRITCHUI_QUICK_IMPL(panelEnableFocus);
	
	/** Creates a new native panel. */
	SJME_SCRITCHUI_QUICK_IMPL(panelNew);
	
	/** The available screens. */
	SJME_SCRITCHUI_QUICK_IMPL(screens);
	
	/** Set minimum size of content window. */
	SJME_SCRITCHUI_QUICK_IMPL(windowContentMinimumSize);
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_IMPL(windowNew);
	
	/** Sets visibility of the window. */
	SJME_SCRITCHUI_QUICK_IMPL(windowSetVisible);
};

#undef SJME_SCRITCHUI_QUICK_IMPL

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
 * Common component initialization function for before and after create.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param postCreate Is this after the create call?
 * @param uiType The type of component this is.
 * @return Any resultant error, if any.
 * @since 2024/04/26
 */
typedef sjme_errorCode (*sjme_scritchui_intern_initComponentFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean postCreate,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType);

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
	
	/** Common post-component initialization. */
	sjme_scritchui_intern_initComponentFunc initComponent;
	
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
