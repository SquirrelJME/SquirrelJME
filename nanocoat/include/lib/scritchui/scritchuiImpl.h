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

/**
 * Implementation specific initialization.
 * 
 * @param inState The state being initialized.
 * @return Any resultant error, if any.
 * @since 2024/04/15
 */
typedef sjme_errorCode (*sjme_scritchui_impl_apiInitFunc)(
	sjme_attrInNotNull sjme_scritchui inState);

/**
 * Adds the given component to the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to place the component within.
 * @param addComponent The component to add to the container.
 * @return Any error code if applicable.
 * @since 2024/04/20
 */
typedef sjme_errorCode (*sjme_scritchui_impl_containerAddFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent);

/**
 * Removes the given component from the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to remove the component within.
 * @param removeComponent The component to remove from the container.
 * @return Any error code if applicable.
 * @since 2024/07/15
 */
typedef sjme_errorCode (*sjme_scritchui_impl_containerRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent);

/**
 * Creates a new native list.
 * 
 * @param inState The input ScritchUI state.
 * @param inList The list that was created.
 * @return Any error code as per implementation.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_impl_listNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiList inList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType);

/**
 * Creates a new native menu bar.
 * 
 * @param inState The input ScritchUI state.
 * @param inMenuBar The menu bar that was created.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_impl_menuBarNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar);

/**
 * Creates a new native menu item.
 * 
 * @param inState The input ScritchUI state.
 * @param inMenuItem The menu item that was created.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_impl_menuItemNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem);

/**
 * Creates a new native menu.
 * 
 * @param inState The input ScritchUI state.
 * @param inMenu The menu that was created.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_impl_menuNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu);

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

#define SJME_SCRITCHUI_QUICK_IMPL(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_impl_, x, Func) x

/** Uses the same main implementation. */
#define SJME_SCRITCHUI_QUICK_SAME(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_, x, Func) x

struct sjme_scritchui_implFunctions
{
	/** Initialize implementation API instance. */
	SJME_SCRITCHUI_QUICK_IMPL(apiInit);
	
	/** Repaint component. */
	SJME_SCRITCHUI_QUICK_SAME(componentRepaint);
	
	/** Revalidate component. */
	SJME_SCRITCHUI_QUICK_SAME(componentRevalidate);
	
	/** Sets the input listener for a component. */
	SJME_SCRITCHUI_QUICK_SAME(componentSetInputListener);
	
	/** Set paint listener for component. */
	SJME_SCRITCHUI_QUICK_SAME(componentSetPaintListener);

	/** Set size listener for component. */
	SJME_SCRITCHUI_QUICK_SAME(componentSetSizeListener);
	
	/** Sets the listener for component visible events. */
	SJME_SCRITCHUI_QUICK_SAME(componentSetVisibleListener);
	
	/** Get size of component. */
	SJME_SCRITCHUI_QUICK_SAME(componentSize);
	
	/** Add component to container. */
	SJME_SCRITCHUI_QUICK_IMPL(containerAdd);
	
	/** Remove component from container. */
	SJME_SCRITCHUI_QUICK_IMPL(containerRemove);
	
	/** Set bounds of component in container. */
	SJME_SCRITCHUI_QUICK_SAME(containerSetBounds);
	
	/** Hardware graphics support on arbitrary buffers. */
	SJME_SCRITCHUI_QUICK_SAME(hardwareGraphics);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_SAME(labelSetString);
	
	/** Creates a new native list. */
	SJME_SCRITCHUI_QUICK_IMPL(listNew);
	
	/** Execute callback within the event loop or schedule later. */
	SJME_SCRITCHUI_QUICK_SAME(loopExecute);
	
	/** Execute call later in the loop. */
	sjme_scritchui_loopExecuteFunc loopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Creates a new menu bar. */
	SJME_SCRITCHUI_QUICK_IMPL(menuBarNew);
	
	/** Creates a new menu item. */
	SJME_SCRITCHUI_QUICK_IMPL(menuItemNew);
	
	/** Creates a new menu. */
	SJME_SCRITCHUI_QUICK_IMPL(menuNew);
	
	/** Enable/disable focus on a panel. */
	SJME_SCRITCHUI_QUICK_SAME(panelEnableFocus);
	
	/** Creates a new native panel. */
	SJME_SCRITCHUI_QUICK_IMPL(panelNew);
	
	/** The available screens. */
	SJME_SCRITCHUI_QUICK_SAME(screens);
	
	/** Set minimum size of content window. */
	SJME_SCRITCHUI_QUICK_SAME(windowContentMinimumSize);
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_IMPL(windowNew);
	
	/** Set close listener for a window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetCloseListener);
	
	/** Sets visibility of the window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetVisible);
};

#undef SJME_SCRITCHUI_QUICK_IMPL
#undef SJME_SCRITCHUI_QUICK_SAME

/**
 * Returns the choice for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outChoice The resultant choice.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/07/16 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getChoiceFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiChoice* outChoice);

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
 * Returns the labeled item for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outLabeled The resultant labeled item.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/07/22 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getLabeledFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiLabeled* outLabeled);

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
 * Common base common initialization for before and after create.
 * 
 * @param inState The input state.
 * @param inCommon The input common.
 * @param postCreate Is this after the create call?
 * @param uiType The type of common this is.
 * @return Any resultant error, if any.
 * @since 2024/07/19
 */
typedef sjme_errorCode (*sjme_scritchui_intern_initCommonFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInValue sjme_jboolean postCreate,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType);

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

/**
 * Updates the visibility state of a container.
 * 
 * @param inState The input state.
 * @param inContainer The input container.
 * @param isVisible Is this now visible?
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_intern_updateVisibleContainerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInValue sjme_jboolean isVisible);

/**
 * Updates the visibility state of a container.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param isVisible Is this now visible?
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_intern_updateVisibleComponentFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean isVisible);

/**
 * Updates the visibility state of a window.
 * 
 * @param inState The input state.
 * @param inWindow The input window.
 * @param isVisible Is this now visible?
 * @since 2024/06/28
 */
typedef sjme_errorCode (*sjme_scritchui_intern_updateVisibleWindowFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

struct sjme_scritchui_internFunctions
{
	/** Returns the built-in font, this can handle layers. */
	sjme_scritchui_fontBuiltinFunc fontBuiltin;
		
	/** Returns the choice for the given component. */
	sjme_scritchui_intern_getChoiceFunc getChoice;
		
	/** Returns the container for the given component. */
	sjme_scritchui_intern_getContainerFunc getContainer;
		
	/** Returns the labeled item for the given component. */
	sjme_scritchui_intern_getLabeledFunc getLabeled;
	
	/** Returns the paintable for the given component. */
	sjme_scritchui_intern_getPaintableFunc getPaintable;
	
	/** Common "common" initialization. */
	sjme_scritchui_intern_initCommonFunc initCommon;
	
	/** Common component initialization. */
	sjme_scritchui_intern_initComponentFunc initComponent;
	
	/** Maps the given screen. */
	sjme_scritchui_intern_mapScreenFunc mapScreen;
	
	/** Update visibility recursively on container. */
	sjme_scritchui_intern_updateVisibleContainerFunc updateVisibleContainer;
	
	/** Update visibility on component. */
	sjme_scritchui_intern_updateVisibleComponentFunc updateVisibleComponent;
	
	/** Update visibility recursively on window. */
	sjme_scritchui_intern_updateVisibleWindowFunc updateVisibleWindow;
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
