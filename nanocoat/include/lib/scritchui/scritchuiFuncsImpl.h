/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal ScritchUI function pointer types, for implementations.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H
#define SJME_C_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H

#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiFuncs.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui_impl)
	
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
 * @param inContainerData The container data.
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
 * @param inContainerData The container data.
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
 * @param init Initializer.
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
 * Sets the area that the scroll panel provides a viewport area, this area
 * may be larger than the viewport and widgets may be placed inside.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inViewArea The view area to set.
 * @param inViewPage The viewing page to set.
 * @return Any resultant error, if any.
 * @since 2024/07/07
 */
typedef sjme_errorCode (*sjme_scritchui_impl_viewSetAreaFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewPage);

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
	
#pragma endregion(scritchui_impl)
#pragma region(scritchui_intern)


/**
 * This is called to bind the focus to a parent window.
 * 
 * @param inState The input state.
 * @param atRover The current roving component.
 * @param bindComponent The component to bind.
 * @param isGrabbing Is focus being grabbed?
 * @return Any resultant error, if any.
 * @since 2024/08/07
 */
typedef sjme_errorCode (*sjme_scritchui_intern_bindFocusFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent atRover,
	sjme_attrInNotNull sjme_scritchui_uiComponent bindComponent,
	sjme_attrInValue sjme_jboolean isGrabbing);

/**
 * Returns the maximum size of the container's components.
 *
 * @param inState The input state.
 * @param inContainer The container to get the component sizes of.
 * @param outSize The resultant size of the components.
 * @return Any resultant error.
 * @since 2024/12/23
 */
typedef sjme_errorCode (*sjme_scritchui_intern_containerMaxSizeFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNotNull sjme_scritchui_dim* outSize);

/**
 * Iterates over fonts that are available to the system along with any
 * pseudo-fonts.
 *
 * @param inState The ScritchUI state.
 * @param inOutStep The current iteration step state.
 * @return Any resultant error, if any. @link SJME_ERROR_STOP @endlink wil
 * discontinue iteration.
 * @since 2026/04/11
 */
typedef sjme_errorCode (*sjme_scritchui_intern_fontIterateStepFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_fontIterateStep* inOutStep);

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
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES)
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
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES)
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

/**
 * Base function for common initialization logic.
 * 
 * @param inState The input state. 
 * @param inCommon The common item to be initialized.
 * @param inData Any data to use for initialization.
 * @return Any resultant error, if any.
 * @since 2024/07/22
 */
typedef sjme_errorCode (*sjme_scritchui_core_intern_objectNewImplFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_pointer inData);
	
/**
 * Basic core common initialization logic.
 * 
 * @param inState The input state. 
 * @param outCommon The resultant common.
 * @param outCommonSize The size of the resultant common.
 * @param uiType The UI type to initialize.
 * @param implNew The implementation new for this type.
 * @param inData Any data to pass to @c implNew .
 * @return Any resultant error, if any.
 * @since 2024/07/22
 */
typedef sjme_errorCode (*sjme_scritchui_core_intern_objectNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* outCommon,
	sjme_attrInPositiveNonZero sjme_jint outCommonSize,
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES)
		sjme_scritchui_uiType uiType,
	sjme_attrInNotNull sjme_scritchui_core_intern_objectNewImplFunc implNew,
	sjme_attrInNullable sjme_pointer inData);

#pragma endregion(scritchui_intern)
#pragma region(scritchui_internTypes)

struct sjme_scritchui_fontIterateStep
{
	/** The valid registers to look within. */
	sjme_jint registerMask;

	/** Limit depth traversal for font iteration. */
	sjme_jint limitDepth;

	/** Function to call for each font iteration. */
	sjme_scritchui_intern_fontIterateStepFunc iterator;

	/** The current font being looked at. */
	sjme_scritchui_pencilFont current;

	/** Generic pointer data for iteration. */
	sjme_pointer data;
};

#pragma endregion(scritchui_internTypes)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUIFUNCSIMPL_H */
