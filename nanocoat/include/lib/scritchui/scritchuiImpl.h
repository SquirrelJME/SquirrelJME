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
#include "lib/scritchui/scritchuiTypesListener.h"

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
 * List initialization parameters.
 * 
 * @since 2024/07/24
 */
typedef struct sjme_scritchui_impl_initParamList
{
	/** The type of choice used. */
	sjme_scritchui_choiceType type;
} sjme_scritchui_impl_initParamList;

/**
 * Initialization parameters for menu items.
 * 
 * @since 2024/08/01
 */
typedef struct sjme_scritchui_impl_initParamMenuItem
{
	/** The opaque ID to use for the item. */
	sjme_jint opaqueId;
} sjme_scritchui_impl_initParamMenuItem;

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
 * @param init Initializer data for the list.
 * @return Any error code as per implementation.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_impl_listNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiList inList,
	sjme_attrInNotNull const sjme_scritchui_impl_initParamList* init);

/**
 * Creates a new native menu bar.
 * 
 * @param inState The input ScritchUI state.
 * @param inMenuBar The menu bar that was created.
 * @param ignored Ignored, not used.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_impl_menuBarNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar,
	sjme_attrInNullable sjme_pointer ignored);

/**
 * Creates a new native menu item.
 * 
 * @param inState The input ScritchUI state.
 * @param inMenuItem The menu item that was created.
 * @param ignored Ignored, not used.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_impl_menuItemNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem,
	sjme_attrInValue const sjme_scritchui_impl_initParamMenuItem* init);

/**
 * Creates a new native menu.
 * 
 * @param inState The input ScritchUI state.
 * @param inMenu The menu that was created.
 * @param ignored Ignored, not used.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_impl_menuNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu,
	sjme_attrInNullable sjme_pointer ignored);

/**
 * Creates a new native panel.
 * 
 * @param inState The input ScritchUI state.
 * @param inPanel The panel that was created.
 * @param ignored Ignored, not used.
 * @return Any error code as per implementation.
 * @since 2024/04/06
 */
typedef sjme_errorCode (*sjme_scritchui_impl_panelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInNullable sjme_pointer ignored);

/**
 * Creates a new native scroll panel.
 * 
 * @param inState The input ScritchUI state.
 * @param inScrollPanel The scroll panel that was created.
 * @param ignored Ignored, not used.
 * @return Any error code as per implementation.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_impl_scrollPanelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored);

/**
 * Creates a new window.
 * 
 * @param inState The input state.
 * @param inWindow The window that was created.
 * @param ignored Ignored, not used at all.
 * @return Any resultant error, if any.
 * @since 2024/04/24
 */
typedef sjme_errorCode (*sjme_scritchui_impl_windowNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_pointer ignored);

#define SJME_SCRITCHUI_QUICK_IMPL(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_impl_, x, Func) x

/** Uses the same main implementation. */
#define SJME_SCRITCHUI_QUICK_SAME(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_, x, Func) x

struct sjme_scritchui_implFunctions
{
	/** Initialize implementation API instance. */
	SJME_SCRITCHUI_QUICK_IMPL(apiInit);
	
	/** Inserts an item into the given choice. */
	SJME_SCRITCHUI_QUICK_SAME(choiceItemInsert);
	
	/** Removes an item from the given choice. */
	SJME_SCRITCHUI_QUICK_SAME(choiceItemRemove);
	
	/** Sets whether the given choice item is enabled. */
	SJME_SCRITCHUI_QUICK_SAME(choiceItemSetEnabled);
	
	/** Sets the image of the given choice item. */
	SJME_SCRITCHUI_QUICK_SAME(choiceItemSetImage);
	
	/** Sets whether the given choice item is selected. */
	SJME_SCRITCHUI_QUICK_SAME(choiceItemSetSelected);
	
	/** Sets the string of the given choice item. */
	SJME_SCRITCHUI_QUICK_SAME(choiceItemSetString);
	
	/** Grabs the focus for this component. */
	SJME_SCRITCHUI_QUICK_SAME(componentFocusGrab);
	
	/** Checks if this component has focus. */
	SJME_SCRITCHUI_QUICK_SAME(componentFocusHas);
	
	/** Get the position of a component. */
	SJME_SCRITCHUI_QUICK_SAME(componentPosition);
	
	/** Repaint component. */
	SJME_SCRITCHUI_QUICK_SAME(componentRepaint);
	
	/** Revalidate component. */
	SJME_SCRITCHUI_QUICK_SAME(componentRevalidate);
	
	/** Sets the activate listener for a component. */
	SJME_SCRITCHUI_QUICK_SAME(componentSetActivateListener);
	
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
	
	/** Returns the element color for the look and feel. */
	SJME_SCRITCHUI_QUICK_SAME(lafElementColor);
	
	/** Creates a new native list. */
	SJME_SCRITCHUI_QUICK_IMPL(listNew);
	
	/** Execute callback within the event loop or schedule later. */
	SJME_SCRITCHUI_QUICK_SAME(loopExecute);
	
	/** Execute call later in the loop. */
	sjme_scritchui_loopExecuteFunc loopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Iterates a single run of the event loop. */
	SJME_SCRITCHUI_QUICK_SAME(loopIterate);
	
	/** Creates a new menu bar. */
	SJME_SCRITCHUI_QUICK_IMPL(menuBarNew);
	
	/** Insert menu into menu. */
	SJME_SCRITCHUI_QUICK_SAME(menuInsert);
	
	/** Creates a new menu item. */
	SJME_SCRITCHUI_QUICK_IMPL(menuItemNew);
	
	/** Removes an item from the menu. */
	SJME_SCRITCHUI_QUICK_SAME(menuRemove);
	
	/** Creates a new menu. */
	SJME_SCRITCHUI_QUICK_IMPL(menuNew);
	
	/** Enable/disable focus on a panel. */
	SJME_SCRITCHUI_QUICK_SAME(panelEnableFocus);
	
	/** Creates a new native panel. */
	SJME_SCRITCHUI_QUICK_IMPL(panelNew);
	
	/** The available screens. */
	SJME_SCRITCHUI_QUICK_SAME(screens);
	
	/** Create a new scroll panel. */
	SJME_SCRITCHUI_QUICK_IMPL(scrollPanelNew);
	
	/** Get the current view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_SAME(viewGetView);
	
	/** Set the area of the viewport's bounds, the entire scrollable area. */
	SJME_SCRITCHUI_QUICK_SAME(viewSetArea);
	
	/** Sets the view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_SAME(viewSetView);
	
	/** Sets the listener for tracking scrolling and viewport changes. */
	SJME_SCRITCHUI_QUICK_SAME(viewSetViewListener);
	
	/** Set minimum size of content window. */
	SJME_SCRITCHUI_QUICK_SAME(windowContentMinimumSize);
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_IMPL(windowNew);
	
	/** Set close listener for a window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetCloseListener);
	
	/** Sets the menu bar for a window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetMenuBar);
	
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
 * @param inCommon The input common item.
 * @param outLabeled The resultant labeled item.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/07/22 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getLabeledFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInOutNotNull sjme_scritchui_uiLabeled* outLabeled);

/**
 * Return children information for a given menu kind.
 * 
 * @param inState The input state.
 * @param inMenuKind The input menu kind.
 * @param outHasChildren The resultant children information.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/07/22 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getMenuHasChildrenFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasChildren* outHasChildren);
	
/**
 * Return parent information for a given menu kind.
 * 
 * @param inState The input state.
 * @param inMenuKind The input menu kind.
 * @param outHasParent The resultant parent information.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/07/22 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getMenuHasParentFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasParent* outHasParent);

/**
 * Returns the container for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outPaintable The resultant paintable.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/04/20 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getPaintableFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiPaintable* outPaintable);

/**
 * Returns the viewport manager for the given component.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outView The resultant viewport manager.
 * @return Any error code if applicable, such as the component is not valid.
 * @since 2024/07/29 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_getViewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiView* outView);

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
 * Activates a menu item by propagating upwards to the bar and owning window,
 * this is mostly intended for widget systems where menus are individually
 * activated rather than at the top level.
 * 
 * @param inState The input state.
 * @param atRover The current menu item, menu, or menu bar.
 * @param itemActivated The item that was activated, this does not change.
 * @return Any resultant error, if any.
 * @since 2024/07/30 
 */
typedef sjme_errorCode (*sjme_scritchui_intern_menuItemActivateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind atRover,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind itemActivated);

/**
 * Activates a menu item by propagating downwards from the bar.
 * 
 * @param inState The input state.
 * @param inWindow The window this is starting from.
 * @param atRover The current menu item, menu, or menu bar.
 * @param itemActivated The item ID that was activated, this does not change.
 * @param itemMask The mask that is valid for item IDs.
 * @return Any resultant error, if any.
 * @since 2024/08/06
 */
typedef sjme_errorCode (*sjme_scritchui_intern_menuItemActivateByIdFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind atRover,
	sjme_attrInNotNull sjme_jint itemActivated,
	sjme_attrInValue sjme_jint itemMask);

/**
 * Sets a simpler listener.
 * 
 * @param inState The ScritchUI state.
 * @param infoAny The callback information to set.
 * @param inListener The listener to set or remove.
 * @param copyFrontEnd The front end to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/19
 */
typedef sjme_errorCode (*sjme_scritchui_intern_setSimpleListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoAny,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void));

/**
 * Suggests that the parent of a component, if there is one and that it is
 * a view, that it should have this suggested size.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The component making the suggestion.
 * @param suggestDim The suggested dimension.
 * @return Any resultant error, if any.
 * @since 2024/07/19
 */
typedef sjme_errorCode (*sjme_scritchui_intern_viewSuggestFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_dim* suggestDim);

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
		
	/** Return children information for a given menu kind. */
	sjme_scritchui_intern_getMenuHasChildrenFunc getMenuHasChildren;
		
	/** Return parent information for a given menu kind. */
	sjme_scritchui_intern_getMenuHasParentFunc getMenuHasParent;
	
	/** Returns the paintable for the given component. */
	sjme_scritchui_intern_getPaintableFunc getPaintable;
	
	/** Returns the viewport manager for the given component. */
	sjme_scritchui_intern_getViewFunc getView;
	
	/** Common "common" initialization. */
	sjme_scritchui_intern_initCommonFunc initCommon;
	
	/** Common component initialization. */
	sjme_scritchui_intern_initComponentFunc initComponent;
	
	/** Set of simple user listener. */
	sjme_scritchui_intern_setSimpleListenerFunc setSimpleListener;
	
	/** Maps the given screen. */
	sjme_scritchui_intern_mapScreenFunc mapScreen;
	
	/** Menu item activation propagation, from bottom up. */
	sjme_scritchui_intern_menuItemActivateFunc menuItemActivate;
	
	/** Menu item activation propagation, from top down. */
	sjme_scritchui_intern_menuItemActivateByIdFunc menuItemActivateById;
	
	/** Update visibility recursively on container. */
	sjme_scritchui_intern_updateVisibleContainerFunc updateVisibleContainer;
	
	/** Update visibility on component. */
	sjme_scritchui_intern_updateVisibleComponentFunc updateVisibleComponent;
	
	/** Update visibility recursively on window. */
	sjme_scritchui_intern_updateVisibleWindowFunc updateVisibleWindow;
	
	/** Suggest the size and position of a coordinate to a view. */
	sjme_scritchui_intern_viewSuggestFunc viewSuggest;
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
