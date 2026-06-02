/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Function Pointer Types.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUIFUNCS_H
#define SJME_C_SQUIRRELJME_SCRITCHUIFUNCS_H

#include "sjme/gfxConst.h"
#include "sjme/stream.h"
#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchinput/scritchinput.h"
#include "lib/scritchui/scritchuiTypeDefs.h"
#include "lib/scritchui/scritchuiTypesSub.h"
#include "lib/scritchui/scritchuiConst.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Arguments to pass for setting of listeners. */
#define SJME_SCRITCHUI_SET_LISTENER_ARGS(what) \
	sjme_attrInNullable SJME_TOKEN_PASTE3(sjme_scritchui_, what, \
		ListenerFunc) inListener, \
	sjme_attrInNullable sjme_frontEndBindable* copyFrontEnd

#pragma region(scritchui)
	
/**
 * Listener that is called when an item is activated.
 * 
 * @param inState The input state.
 * @param inComponent The item which was activated.
 * @return Any resultant error, if any.
 * @since 2024/07/16
 */
typedef sjme_errorCode (*sjme_scritchui_activateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

/**
 * Listener that is called when a window closes.
 * 
 * @param inState The input state.
 * @param inWindow The window being closed.
 * @return Any resultant error, @link SJME_ERROR_CANCEL_WINDOW_CLOSE @endlink
 * is handled
 * specifically in that it will not be treated as an error however normal
 * application exit will not happen.
 * @since 2024/05/13
 */
typedef sjme_errorCode (*sjme_scritchui_closeListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow);

/**
 * Obtains an asset that is externally provided.
 * 
 * Note that directories cannot be opened as a stream, however they may be
 * checked for existence.
 *
 * @param inState The input state.
 * @param assetType The asset type requested, this is used as a hint.
 * @param inAsset The name of the asset to load.
 * @param outStream The resultant stream of the asset data, if this
 * is @code NULL @endcode then this only checks for existence.
 * @return Any resultant error, if any. This will
 * return @link SJME_ERROR_RESOURCE_NOT_FOUND @endlink if there is no
 * resource for this.
 * @since 2024/11/29 
 */
typedef sjme_errorCode (*sjme_scritchui_externalAssetFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_scritchui_externalAssetType assetType,
	sjme_attrInNotNull sjme_lpcstr inAsset,
	sjme_attrOutNullable sjme_stream_input* outStream);
	
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
 * This is called when a menu item has been activated.
 * 
 * @param inState The input state.
 * @param inWindow The window this is activating under.
 * @param activatedItem The menu item that was activated.
 * @return Any resultant error, if any.
 * @since 2024/07/30
 */
typedef sjme_errorCode (*sjme_scritchui_menuItemActivateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind activatedItem);

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
 * Listener callback for when a screen has been queried, or it has been
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
 * Listener for views so that a sub-component can suggest a size that it
 * could be.
 * 
 * @param inState The ScritchUI state.
 * @param inView The view this is in.
 * @param subComponent The component that is suggesting a size.
 * @param subDim The size of the sub-component.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_sizeSuggestListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inView,
	sjme_attrInNotNull sjme_scritchui_uiComponent subComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* subDim);

/**
 * Listener that is called before and after the state within a component
 * has changed, when @c isAfterUpdate is @link SJME_JNI_FALSE @endlink then
 * the component is about to be updated.
 * 
 * @param inState The input state.
 * @param inComponent The component where this event occurred.
 * @param isAfterUpdate Is this after the update has occurred?
 * @return Any resultant error, if any.
 * @since 2024/07/16
 */
typedef sjme_errorCode (*sjme_scritchui_valueUpdateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean isAfterUpdate);

/**
 * Listener for view rectangle changes.
 * 
 * @param inState The input state.
 * @param inComponent The component that triggered this.
 * @param inViewRect The new view rectangle.
 * @return Any resultant error, if any.
 * @since 2024/07/28
 */
typedef sjme_errorCode (*sjme_scritchui_viewListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_rect* inViewRect);

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
 * Initializes the native UI interface needed by ScritchUI.
 * 
 * @param inPool The allocation pool to use.
 * @param outState The resultant state.
 * @param inImplFunc The implementation functions to use.
 * @param loopExecute Optional callback for loop execution, may be @c NULL ,
 * the passed argument is always the state.
 * @param initFrontEnd Optional initial front end data.
 * @return Any error code if applicable.
 * @since 2024/03/27
 */
typedef sjme_errorCode (*sjme_scritchui_apiInitFunc)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd);

/**
 * Gets the first selected index of a choice or otherwise @c -1 .
 * 
 * @param inState The input state.
 * @param inComponent The choice to read from.
 * @param outIndex The resultant index.
 * @return Any resultant error, if any.
 * @since 2024/07/28
 */
typedef sjme_errorCode (*sjme_scritchui_choiceGetSelectedIndexFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outIndex);

/**
 * Gets the specified item template.
 * 
 * @param inState The input state.
 * @param inComponent The choice to read from.
 * @param atIndex The index to obtain the template of.
 * @param outItemTemplate A copy of the item template.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemGetFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate);

/**
 * Inserts a blank item at the given index.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param inOutIndex The input index to insert at, then resultant index
 * where it was added.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemInsertFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex);

/**
 * Removes the specified item at the given index.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to remove.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex);
	
/**
 * Removes all items from the given choice.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemRemoveAllFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	

/**
 * Sets whether the specified choice item is enabled.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param isEnabled If the item should be enabled.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetEnabledFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled);

/**
 * Sets the image of the specified choice item.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param inRgb The RGB data, may be @c NULL to clear the image.
 * @param inRgbOff The offset in the RGB data.
 * @param inRgbDataLen The data length of the RGB data.
 * @param inRgbScanLen The scanline length of the RGB data.
 * @param width The width of the image.
 * @param height The height of the image.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetImageFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

/**
 * Sets whether the specified choice item is selected.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param isSelected If the item should be selected.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetSelectedFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected);

/**
 * Sets the string of the specified choice item.
 * 
 * @param inState The input state.
 * @param inComponent The choice to modify.
 * @param atIndex The index to modify.
 * @param inString The string to set, @c NULL will clear it.
 * @return Any resultant error, if any.
 * @since 2024/07/25
 */
typedef sjme_errorCode (*sjme_scritchui_choiceItemSetStringFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString);

/**
 * Returns the length of the choice list.
 * 
 * @param inState The input state.
 * @param inComponent The choice to get the length of.
 * @param outLength The resultant length.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_choiceLengthFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength);

/**
 * Grabs the focus onto this item.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_componentFocusGrabFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	
/**
 * Checks whether the given component has focus.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param outHasFocus The result of whether the component has focus.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_componentFocusHasFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus);

/**
 * Gets the parent of this component.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The component to get the parent of.
 * @param outParent The resultant parent that contains this, or @c NULL if
 * there is no parent.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_componentGetParentFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_uiComponent* outParent);

/**
 * Returns the position of the given component.
 * 
 * @param inState The input state.
 * @param inComponent The component to get the position of.
 * @param outX The output X coordinate.
 * @param outY The output Y coordinate.
 * @return Any resultant error, if any.
 * @since 2024/08/06
 */
typedef sjme_errorCode (*sjme_scritchui_componentPositionFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY);

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
 * Sets the activation listener for the given choice.
 * 
 * @param inState The input state.
 * @param inComponent The choice to update.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetActivateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(activate));

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
 * Sets the update listener for the given choice.
 * 
 * @param inState The input state.
 * @param inComponent The choice to update.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/17
 */
typedef sjme_errorCode (*sjme_scritchui_componentSetValueUpdateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(valueUpdate));

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
 * Returns the size details of a container, such as the outer frame, the
 * content frame, and the sizes.
 * 
 * @param inState The input ScritchUI state.
 * @param inContainer The container to get the content size of.
 * @param contentSize The size of the content area.
 * @param frameBound The bounds of the frame including the extra area such
 * as decorations or otherwise.
 * @param contentBound The bounds of the content area within
 * the @a frameBound , this is the actual drawable area for widgets. 
 * @return Any resultant error, if any.
 * @since 2025/12/23
 */
typedef sjme_errorCode (*sjme_scritchui_containerGetFrameFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNullable sjme_scritchui_dim* contentSize,
	sjme_attrOutNullable sjme_scritchui_rect* frameBound,
	sjme_attrOutNullable sjme_scritchui_rect* contentBound);
	
/**
 * Removes the given component from the specified container.
 * 
 * @param inState The input state.
 * @param inContainer The container to remove the component from.
 * @param removeComponent The component to remove from the container.
 * @return Any error code if applicable.
 * @since 2024/07/15
 */
typedef sjme_errorCode (*sjme_scritchui_containerRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent);

/**
 * Removes all components from the container.
 * 
 * @param inState The input state.
 * @param inContainer The container to remove everything from.
 * @return Any error code if applicable.
 * @since 2024/07/15
 */
typedef sjme_errorCode (*sjme_scritchui_containerRemoveAllFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer);

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
 * Returns the total number of fonts available.
 * 
 * @param inState The input state.
 * @param outCount The number of known fonts.
 * @since 2026/01/18
 */
typedef sjme_errorCode (*sjme_scritchui_fontCountFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_jint* outCount);

/**
 * Obtains a font by the given face.
 *
 * @param inState The input state.
 * @param outFont The resultant font.
 * @param outParams The resultant parameters.
 * @param inFace The font face to lookup.
 * @param inParams The parameters to lookup.
 * @return Any resultant error, if any.
 * @since 2026/04/11
 */
typedef sjme_errorCode (*sjme_scritchui_fontByFaceFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* outParams,
	sjme_attrInValue sjme_scritchui_pencilFontFace inFace,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams);

/**
 * Derives a new font from an existing font.
 * 
 * @param inState The input state.
 * @param newFont The new font.
 * @param newParams The new font parameters.
 * @param oldFont The old font.
 * @param deriveParams The font paramters to derive to..
 * @return Any resultant error, if any.
 * @since 2024/06/14
 */
typedef sjme_errorCode (*sjme_scritchui_fontDeriveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* newFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* newParams,
	sjme_attrInNotNull sjme_scritchui_pencilFont oldFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* deriveParams);

/**
 * Obtains the fonts which are available in the system, if any.
 *
 * @param inState The input state.
 * @param outFonts The list which gets filled with all the fonts.
 * @param outValid The number of valid fonts.
 * @param outCount The maximum number of fonts available, this is optional.
 * @return Any resultant error, if any.
 * @since 2024/12/01
 */
typedef sjme_errorCode (*sjme_scritchui_fontListFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_list(sjme_scritchui_pencilFont)* outFonts,
	sjme_attrOutNotNull sjme_jint* outValid,
	sjme_attrOutNullable sjme_jint* outCount);

/**
 * Registers the specified font.
 * 
 * If the font is already registered
 * then @link SJME_ERROR_FONT_ALREADY_REGISTERED @endlink is returned.
 * 
 * @param inState The input state.
 * @param inFont The font to register.
 * @param isPseudo Register this font into the pseudo line of fonts?
 * @return Any resultant error, if any.
 * @since 2026/01/18
 */
typedef sjme_errorCode (*sjme_scritchui_fontRegisterFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inState The UI state.
 * @param outPencil The resultant pencil.
 * @param outWeakPencil The weak reference to the pencil.
 * @param pf The @link sjme_gfx_pixelFormat @endlink used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @param pencilFrontEndCopy Front end data that goes into the pencil.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_hardwareGraphicsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy);

/**
 * Creates a pseudo pencil which layers on top of multiple pencils for the
 * purpose of supporting planar graphics. The underlying pencils may be
 * implemented in a mix of hardware and/or software, however the pseudo
 * pencil naturally cannot support hardware acceleration.
 * 
 * The color format of the returned pencil will always be
 * either @link SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 @endlink if there is
 * an alpha channel, or @link SJME_GFX_PIXEL_FORMAT_INT_RGB888 @endlink if
 * there is no alpha channel.
 * 
 * Whether an alpha channel exists is determined by whether any of the pixel
 * formats used by the underlying pencils contain an alpha channel.
 * 
 * The channel priority is first-come-first-serve, that is if two pencils
 * have a pixel format that have a color channel only the first one will be
 * selected.
 * 
 * It is not valid for any target graphics to be indexed, as determined
 * by @link sjme_scritchpen_isIndexed() @endlink .
 * 
 * @param inState The UI state.
 * @param outPencil The resultant pencil.
 * @param outWeakPencil The weak reference to the pencil.
 * @param pencils The pencils to wrap.
 * @param numPencils The number of pencils to wrap.
 * @param pencilFrontEndCopy Front end data that goes into the pencil.
 * @return An error if the requested graphics are not valid.
 * @since 2025/12/22
 */
typedef sjme_errorCode (*sjme_scritchui_pseudoGraphicsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInNotNullBuf(numPencils) sjme_scritchui_pencil* pencils,
	sjme_attrInPositiveNonZero sjme_jint numPencils,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy);
	
/**
 * Sets the label of the specified component.
 * 
 * @param inState The input state.
 * @param inCommon The item to set the label for.
 * @param inString The label to set.
 * @return Any resultant error, if any.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_labelSetStringFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString);

/**
 * Projects or reverses a projection of a coordinate within the base
 * unscaled coordinate system to the host DPI.
 * 
 * @param inState The input state.
 * @param inContext The context of the component the projection is for, may
 * be @c NULL .
 * @param toBase Reverse projection from scaled DPI space to base coordinates.
 * @param inOutX Input/output X coordinates.
 * @param inOutY Input/output Y coordinates.
 * @param inOutW Input/output width.
 * @param inOutH Input/output height.
 * @return Any resultant error.
 * @since 2024/12/25
 */
typedef sjme_errorCode (*sjme_scritchui_lafDpiProjectFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH);

/**
 * Returns the color for the given element based on the current look and feel.
 * 
 * @param inState The current state.
 * @param inContext Optional context that can be targetted at a widget to get
 * its color themeing.
 * @param outRGB The resultant RGB color.
 * @param elementColor The color to request.
 * @return On any resultant error, if any.
 * @since 2024/07/27
 */
typedef sjme_errorCode (*sjme_scritchui_lafElementColorFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor);

/**
 * Obtains a look-and-feel metric.
 *
 * @param inState The current state.
 * @param inContext The component used as context, this is optional and may or
 * may not have an effect.
 * @param outValue The resultant value.
 * @param metricType The metric to obtain.
 * @return Any resultant error, if any.
 * @since 2026/04/18
 */
typedef sjme_errorCode (*sjme_scritchui_lafMetricFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInValue sjme_scritchui_lafMetricType metricType);

/**
 * Creates a new list.
 * 
 * @param inState The input state.
 * @param outList The resultant list.
 * @param inChoiceType The type of choice this is.
 * @return Any error code if applicable.
 * @since 2024/07/16
 */
typedef sjme_errorCode (*sjme_scritchui_listNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType);

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
 * @param blocking If the iteration should block for something to happen.
 * @param outHasTerminated Has the GUI interface terminated?
 * @return Any error code if applicable.
 * @since 2024/04/02
 */
typedef sjme_errorCode (*sjme_scritchui_loopIterateFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);

/**
 * Creates a new menu bar.
 * 
 * @param inState The input state.
 * @param outMenuBar The resultant menu bar.
 * @return Any error code if applicable.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_menuBarNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar);

/**
 * Inserts the given menu item into the menu at the specified index.
 * 
 * @param inState The ScritchUI state.
 * @param intoMenu The menu to insert into.
 * @param atIndex The index to insert at.
 * @param childItem The child menu item to add.
 * @return Any resultant error, if any.
 * @since 2024/07/23 
 */
typedef sjme_errorCode (*sjme_scritchui_menuInsertFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem);

/**
 * Creates a new menu item.
 * 
 * @param inState The input state.
 * @param outMenuItem The resultant menu item.
 * @return Any error code if applicable.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_menuItemNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem);

/**
 * Creates a new menu.
 * 
 * @param inState The input state.
 * @param outMenu The resultant menu.
 * @return Any error code if applicable.
 * @since 2024/07/21
 */
typedef sjme_errorCode (*sjme_scritchui_menuNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu);

/**
 * Removes the item at the specified index from this menu.
 * 
 * @param inState The ScritchUI state.
 * @param fromMenu The menu to remove from.
 * @param atIndex The index to remove.
 * @return Any resultant error, if any.
 * @since 2024/07/23 
 */
typedef sjme_errorCode (*sjme_scritchui_menuRemoveFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex);

/**
 * Removes all items from the given menu.
 * 
 * @param inState The ScritchUI state.
 * @param fromMenu The menu to remove from.
 * @return Any resultant error, if any.
 * @since 2024/07/23 
 */
typedef sjme_errorCode (*sjme_scritchui_menuRemoveAllFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu);

/**
 * Deletes the given object.
 * 
 * @param inState The input state.
 * @param inOutObject The object to delete.
 * @return Any resultant error, if any.
 * @since 2024/07/20
 */
typedef sjme_errorCode (*sjme_scritchui_objectDeleteFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject);

/**
 * Enables or disables focus on a panel.
 * 
 * @param inState The input state.
 * @param inPanel The input panel.
 * @param enableFocus Should focus be enabled?
 * @param defaultFocus Should this panel be set to default focus?
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
 * Returns the bounds of the screen, this includes its relative position
 * to the origin point of all screen on multiscreen devices.
 * 
 * @param inState The input state.
 * @param inScreen The screen to get the bounds of.
 * @param forComponent The component this is for.
 * @param pixelBound The resultant screen bound.
 * @param mmBound The resultant screen bound in millimeters.
 * @return Any resultant error, if any.
 * @since 2025/12/23
 */
typedef sjme_errorCode (*sjme_scritchui_screenGetBoundsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen,
	sjme_attrInNullable sjme_scritchui_uiComponent forComponent,
	sjme_attrOutNullable sjme_scritchui_rect* pixelBound,
	sjme_attrOutNullable sjme_scritchui_rect* mmBound);
	
/**
 * Sets the screen listener callback for screen changes.
 * 
 * @param inState The input state.
 * @param inListener The callback for screen information and changes.
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
 * Creates a new scroll panel which contains other components within a viewport
 * with scrollbars.
 * 
 * @param inState The ScritchUI state.
 * @param outScrollPanel The newly created scroll panel.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_scrollPanelNewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScrollPanel* outScrollPanel);

/**
 * Gets the current view rectangle of a viewport.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param outViewRect The current view rectangle.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewGetViewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect);

/**
 * Sets the area that the scroll panel provides a viewport area, this area
 * may be larger than the viewport and widgets may be placed inside.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inViewArea The view area to set.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetAreaFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea);

/**
 * Sets the view rectangle of a viewport.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inViewPos The new view position to set.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetViewFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos);

/**
 * Sets the listener which is called for a viewport when a contained component
 * has a suggestion as to how large it should be to contain it.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front-end data needed for the listener.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetSizeSuggestListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(sizeSuggest));

/**
 * Sets the listener which is called whenever the viewport changes such as it
 * being scrolled.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The viewport.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front-end data needed for the listener.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_viewSetViewListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view));

/**
 * Void listener set.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The component.
 * @param inListener The listener to set.
 * @param copyFrontEnd Any front-end data needed for the listener.
 * @return Any resultant error, if any.
 * @since 2024/07/29
 */
typedef sjme_errorCode (*sjme_scritchui_voidSetVoidListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void));

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
 * Sets the menu bar for a window.
 * 
 * @param inState The input state.
 * @param inWindow The window to set the menu bar of.
 * @param inMenuBar The menu bar to set, if @c NULL then it is removed.
 * @return Any resultant error, if any.
 * @since 2024/07/23
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetMenuBarFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar);

/**
 * Sets the menu item activation listener for a window.
 * 
 * @param inState The input state.
 * @param inWindow The window to set for.
 * @param inListener The listener to use.
 * @param copyFrontEnd The front end data to use.
 * @return Any resultant error, if any.
 * @since 2024/07/30
 */
typedef sjme_errorCode (*sjme_scritchui_windowSetMenuItemActivateListenerFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(menuItemActivate));

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

#pragma endregion(scritchui)
#pragma region(scritchui_font)
	
/**
 * Checks if two brackets refer to the same font.
 *
 * @param a The first font.
 * @param aParams The first font parameters.
 * @param b The second font.
 * @param bParams The second font parameters.
 * @return If the two fonts are the same,
 * or @link SJME_ERROR_NOT_MATCHED @endlink if they are not the same.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontEqualsFunc)(
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* aParams,
	sjme_attrInNullable sjme_scritchui_pencilFont b,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* bParams);

/**
 * Returns the direction of the given character in the font.
 *
 * @param inFont The font to check.
 * @param inCodepoint The character.
 * @param outDirection The direction of the character, will be @c -1  or @c 1 .
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricCharDirectionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrInRange(-1, 1) sjme_jint* outDirection);

/**
 * Checks whether the character in the given font is valid, as in it has
 * a render-able glyph.
 *
 * @param inFont The font to check within.
 * @param inCodepoint The character to check.
 * @param outValid If the character in the font has a glyph and is valid.
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricCharValidFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid);

/**
 * Returns the @link sjme_scritchui_pencilFontFace @endlink of a font. 
 *
 * @param inFont The font to request from.
 * @param outFace The font face, any flag
 * from @link sjme_scritchui_pencilFontFace @endlink .
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontFaceFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace);

/**
 * Returns the name of the font.
 * 
 * @param inFont The font to get the name of.
 * @param outName The font name.
 * @return Any resultant error, if any.
 * @since 2024/06/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontNameFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_lpcstr* outName);

/**
 * Returns the base style of the font.
 *
 * @param inFont The style of the font to request.
 * @param outStyle The font style, will be flags
 * from @link sjme_scritchui_pencilFontStyle @endlink .
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontStyleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontStyle* outStyle);

/**
 * Returns the ascent of the font.
 *
 * @param inFont The font to check.
 * @param inParams The font parameters for property adjustment.
 * @param isMax Should the max be obtained.
 * @param outAscent The ascent of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelAscentFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent);

/**
 * Returns the baseline of the font.
 *
 * @param inFont The font to check.
 * @param inParams The font parameters for property adjustment.
 * @param outBaseline The baseline of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelBaselineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outBaseline);

/**
 * Returns the height of the font.
 *
 * @param inFont The font to check.
 * @param inParams The font parameters for property adjustment.
 * @param outHeight The height of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelHeightFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outHeight);

/**
 * Returns the descent of the font.
 *
 * @param inFont The font to check.
 * @param inParams The font parameters for property adjustment.
 * @param isMax Should the max be obtained.
 * @param outDescent The descent of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelDescentFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent);
	
/**
 * Returns the leading of the font.
 *
 * @param inFont The font to obtain from.
 * @param inParams The font parameters for property adjustment.
 * @param outLeading The leading amount in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelLeadingFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading);
	
/**
 * Returns the pixel size of the font.
 *
 * @param inFont The font to get the size of.
 * @param inParams The font parameters for property adjustment.
 * @param inCodepoint The code point to get the height of, this will
 * be @code -1 @endcode if this is a general request for the font.
 * @param outSize The pixel size of the font.
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelSizeFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNegativeOnePositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize);

/**
 * Maps structured font parameters from flat font parameters.
 *
 * @param inState The input state.
 * @param outParams The output parameters.
 * @param inFlat The @link sjme_scritchui_pencilFontParamIndex @endlink
 * for property adjustment.
 * @param inFlatOff The offset into the flat parameters.
 * @param inFlatLen The length of the flat parameters.
 * @return Any resultant error, if any.
 * @since 2026/03/21
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontParamFromFlatFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFontParam* outParams,
	sjme_attrInNotNullBuf(inFlatLen) const sjme_jint* inFlat,
	sjme_attrInPositive sjme_jint inFlatOff,
	sjme_attrInPositiveNonZero sjme_jint inFlatLen);

/**
 * Maps structured font parameters to flat font parameters.
 *
 * @param inState The input state.
 * @param inParams The input structured parameters.
 * @param outFlat The @link sjme_scritchui_pencilFontParamIndex @endlink
 * for property adjustment.
 * @param outFlatOff The offset into the flat parameters.
 * @param outFlatLen The length of the flat parameters.
 * @return Any resultant error, if any.
 * @since 2026/03/21
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontParamToFlatFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNullBuf(outFlatLen) sjme_jint* outFlat,
	sjme_attrInPositive sjme_jint outFlatOff,
	sjme_attrInPositiveNonZero sjme_jint outFlatLen);

/**
 * Returns the width of the given character.
 *
 * @param inFont The font to obtain from.
 * @param inParams The font parameters for property adjustment.
 * @param inCodepoint The character.
 * @param outWidth The width of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontPixelCharWidthFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth);

/**
 * Renders the font glyph to a bitmap represented in a byte array. Each
 * byte within the array represents 8 pixels.
 *
 * @param inFont The font to render to the bitmap.
 * @param inParams The font parameters for property adjustment.
 * @param inCodepoint The character to render.
 * @param buf The resultant buffer.
 * @param bufOff The offset into the buffer.
 * @param bufScanLen The scanline length of the buffer.
 * @param bufHeight The buffer height.
 * @param outOffX X offset of glyph, for proper rendering.
 * @param outOffY Y offset of glyph, for proper rendering.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontRenderBitmapFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jubyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint bufHeight,
	sjme_attrOutNullable sjme_jint* outOffX,
	sjme_attrOutNullable sjme_jint* outOffY);

/**
 * Renders the given character to the resultant pencil.
 *
 * @param inFont The font to render from.
 * @param inParams The font parameters for property adjustment.
 * @param inCodepoint The character to render.
 * @param inPencil The pencil to draw into.
 * @param xPos The target X position.
 * @param yPos The target Y position.
 * @param nextXPos Optional output which contains the next X
 * coordinate accordingly for continual drawing.
 * @param nextYPos Optional output which contains the next Y
 * coordinate accordingly for continual drawing.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontRenderCharFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInValue sjme_jint xPos,
	sjme_attrInNotNull sjme_jint yPos,
	sjme_attrOutNullable sjme_jint* nextXPos,
	sjme_attrOutNullable sjme_jint* nextYPos);

/**
 * Calculates the width of the given string.
 * 
 * @param inFont The font to calculate for.
 * @param inParams The font parameters for property adjustment.
 * @param s The input character sequence.
 * @param o The offset into the sequence.
 * @param l The length of the sequence.
 * @param outWidth The resultant pixel width.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontStringWidthFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrOutNotNull sjme_jint* outWidth);
	
#pragma endregion(scritchui_font)
#pragma region(scritchui_pencil)
	
	
/**
 * Applies an anchor point.
 * 
 * @param anchor The anchor point.
 * @param x The input X coordinate.
 * @param y The output Y coordinate.
 * @param w The width of the drawable.
 * @param h The height of the drawable.
 * @param baseline The baseline position of the font, if this is one. If
 * set to a negative value this will be considered invalid to use.
 * @param outX The resultant X coordinate.
 * @param outY The resultant Y coordinate.
 * @return Any error, if applicable.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyAnchorFunc)(
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint baseline,
	sjme_attrOutNotNull sjme_jint* outX,
	sjme_attrOutNotNull sjme_jint* outY);

/**
 * Applies coordinate adjustments based on the provided transformation, so
 * that subsequent region manipulations are using the correct coordinates.
 * 
 * @param inTrans The transformation type.
 * @param x The source region x position.
 * @param y The source region y position.
 * @param w The source region width.
 * @param h The source region height.
 * @param dataWidth The width of the entire data/image.
 * @param dataHeight The height of the entire data/image.
 * @since 2025/12/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyCoordinateAdjFunc)(
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y,
	sjme_attrInOutNotNull sjme_jint* w,
	sjme_attrInOutNotNull sjme_jint* h,
	sjme_attrInPositive sjme_jint dataWidth,
	sjme_attrInPositive sjme_jint dataHeight);

/**
 * Applies rotation and scaling.
 * 
 * @param adjMatrix The resultant matrix.
 * @param inTrans The translation to use.
 * @param wSrc The source width.
 * @param hSrc The source height.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @return Any resultant error, if any.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyRotateScaleFunc)(
	sjme_attrInOutNotNull sjme_scritchui_matrix* adjMatrix,
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest);

/**
 * Applies translation to the given coordinates.
 * 
 * @param g The graphics to apply from.
 * @param x The X coordinate to translate.
 * @param y The Y coordinate to translate.
 * @return Any resultant error, if any.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyTranslateFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y);

/**
 * Function used for bit-line drawing operations.
 * 
 * @param g The pencil to draw onto.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilBitLineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);
	
/**
 * This blends RGB data from a source buffer into the target buffer.
 * 
 * @param g The graphics to operate under.
 * @param destAlpha Does the destination utilize an alpha channel?
 * @param srcAlpha Does the source utilize an alpha channel?
 * @param mulAlpha Should alpha values be multiplied?
 * @param mulAlphaValue The alpha value to multiply with.
 * @param dest The destination buffer which is written over.
 * @param src The source buffer.
 * @param numPixels The number of pixels to blend.
 * @return Any resultant error, if any.
 * @since 2024/07/11
 */
typedef sjme_errorCode (*sjme_scritchui_pencilBlendRGBIntoFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean destAlpha,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) const sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels);

/**
 * Closes the given pencil.
 * 
 * @param g The pencil to close.
 * @return Any resultant error, if any.
 * @since 2025/02/05
 */
typedef sjme_errorCode (*sjme_scritchui_pencilCloseFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * This copies one region of the image to another region.
 *
 * Copying to a display device is not permitted because it may impact how
 * double buffering is implemented, as such it is not supported.
 *
 * Pixels are copied directly and no alpha compositing is performed.
 *
 * If the source and destination overlap then it must be as if they did not
 * overlap at all, this means that the destination will be an exact copy of
 * the source.
 *
 * @param g The hardware graphics to draw with.
 * @param sx The source X position, will be translated.
 * @param sy The source Y position, will be translated.
 * @param w The width to copy.
 * @param h The height to copy.
 * @param dx The destination X position, will be translated.
 * @param dy The destination Y position, will be translated.
 * @param anchor The anchor point of the destination.
 * @return An error if the call is not valid or the native graphics
 * does not support this operation.
 * @deprecated Use @link sjme_scritchui_pencilTransferRegionFunc @endlink 
 * with direct copy mapping.
 * @since 2024/05/01
 */
typedef sjme_errorCode sjme_attrDeprecated
	(*sjme_scritchui_pencilCopyAreaFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws an arc with adjustable angles in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @param startAngle The starting angle of the arc
 * @param arcAngle The arc angle to be drawn
 * @return An error on @c NULL arguments.
 * @since 2025/11/25
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawArcFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

/**
 * Draws the given character.
 *
 * @param g The hardware graphics to draw with.
 * @param c The codepoint to draw.
 * @param x The X position.
 * @param y The Y position.
 * @param anchor The anchor point.
 * @param outCw The output codepoint width, this is optional.
 * @return An error if the graphics is not valid, does not support
 * the given operation, or if the anchor point is not valid.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawCharFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw);

/**
 * Draws the given characters.
 *
 * @param g The hardware graphics to draw with.
 * @param s The characters to draw.
 * @param o The offset into the buffer.
 * @param l The number of characters to draw.
 * @param x The X position.
 * @param y The Y position.
 * @param anchor The anchor point.
 * @return An error if the graphics is not valid, does not support
 * the given operation, if the anchor point is not valid, or if the
 * offset and/or length are out of bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawCharsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws a horizontal line in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The starting X coordinate.
 * @param y The starting Y coordinate.
 * @param w The width of the line.
 * @return An error on null arguments.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawHorizFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w);

/**
 * Draws a line in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x1 The starting X coordinate.
 * @param y1 The starting Y coordinate.
 * @param x2 The ending X coordinate.
 * @param y2 The ending Y coordinate.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawLineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2);

/**
 * Draws a single pixel in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The starting X coordinate.
 * @param y The starting Y coordinate.
 * @return An error on null arguments.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawPixelFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

/**
 * Draws a polyline in hardware with the specified set of x,y coordinates.
 * Reading begins from the x/yOffset for each array, and moves up to nPoints
 * positions.
 * 
 * @param g The hardware graphics to draw with.
 * @param inXPoints An array containing all available X vertex coordinates
 * @param xOffset The offset from which xPoints will begin being read from
 * @param inYPoints An array containing all available Y vertex coordinates
 * @param yOffset The offset from which yPoints will begin being read from
 * @param nPoints How many points should be used to construct the polygon.
 * @return An error on @c NULL arguments.
 * @since 2025/11/30
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawPolylineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints);
	
/**
 * Draws the outline of the given rectangle using the current color and
 * stroke style. The rectangle will cover an area that
 * is @code \[width + 1, height + 1\] @endcode .
 *
 * Nothing is drawn if the width and/or height are zero.
 *
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error if the graphics is not valid or does not support
 * the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Draws a region of data in a given pixel format into the target.
 *
 * @param g The hardware graphics to draw with.
 * @param pf The image format that the data is in.
 * @param data The source buffer.
 * @param off The offset into the buffer.
 * @param dataLen The total length of the data buffer.
 * @param scanLen The scanline length.
 * @param alpha Drawing with the alpha channel?
 * @param xSrc The source X position.
 * @param ySrc The source Y position.
 * @param wSrc The width of the source region.
 * @param hSrc The height of the source region.
 * @param trans Sprite translation and/or rotation,
 * see @code javax.microedition.lcdui.game.Sprite @endcode.
 * @param xDest The destination X position, is translated.
 * @param yDest The destination Y position, is translated.
 * @param anchor The anchor point.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @param origImgWidth Original image width.
 * @param origImgHeight Original image height.
 * @return Any resultant error, if any
 * @since 2025/12/07
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawRegionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint pf,
	sjme_attrInNotNull sjme_cpointer data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight);

/**
 * Draws a rectangle with rounded borders in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @param arcWidth The width of the border rounding
 * @param arcHeight The height of the border rounding
 * @return An error on @c NULL arguments.
 * @since 2025/11/25
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawRoundRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight);

/**
 * Draws the given substring.
 *
 * @param g The hardware graphics to draw with.
 * @param s The string to draw.
 * @param o The offset into the string.
 * @param l The offset into the length.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param anchor The anchor point.
 * @return An error if the graphics is not valid, this operation is
 * not supported, or on null arguments, or if the offset and/or length are
 * negative or exceed the string bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawSubstringFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws a triangle using the current color.
 *
 * @param g The graphics to use for drawing.
 * @param x1 First X coordinate.
 * @param y1 First Y coordinate.
 * @param x2 Second X coordinate.
 * @param y2 Second Y coordinate.
 * @param x3 Third X coordinate.
 * @param y3 Third Y coordinate.
 * @return An error if no graphics were specified or the graphics does
 * not actually support the given operation.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawTriangleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

/**
 * Draws a region of 32-bit RGB data into the target.
 *
 * @param g The hardware graphics to draw with.
 * @param data The source buffer.
 * @param off The offset into the buffer.
 * @param dataLen The total length of the data buffer.
 * @param scanLen The scanline length.
 * @param alpha Drawing with the alpha channel?
 * @param xSrc The source X position.
 * @param ySrc The source Y position.
 * @param wSrc The width of the source region.
 * @param hSrc The height of the source region.
 * @param trans Sprite translation and/or rotation,
 * see @c javax.microedition.lcdui.game.Sprite.
 * @param xDest The destination X position, is translated.
 * @param yDest The destination Y position, is translated.
 * @param anchor The anchor point.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @param origImgWidth Original image width.
 * @param origImgHeight Original image height.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawXRGB32RegionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight);

/**
 * Fills an arc with adjustable angles in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @param startAngle The starting angle of the arc
 * @param arcAngle The arc angle to be drawn
 * @return An error on @c NULL arguments.
 * @since 2025/11/25
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillArcFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

/**
 * Draws a filled polygon in hardware with the specified set
 * of @code [x, y] @endcode coordinates. Reading begins from
 * the @a x / @a yOffset for each array, and moves up to @a nPoints positions.
 * 
 * @param g The hardware graphics to draw with.
 * @param inXPoints An array containing all available X vertex coordinates
 * @param xOffset The offset from which xPoints will begin being read from
 * @param inYPoints An array containing all available Y vertex coordinates
 * @param yOffset The offset from which yPoints will begin being read from
 * @param nPoints How many points should be used to construct the polygon.
 * @return If any argument is @c NULL or if the offset and/or number of
 * points is not valid.
 * @since 2025/11/25
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillPolygonFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints);

/**
 * Draws a filled polygon in hardware with the specified set
 * of @code [x, y] @endcode coordinates. Reading begins from
 * the @a x / @a yOffset for each array, and moves up to @a nPoints positions.
 * 
 * @param g The hardware graphics to draw with.
 * @param inXPoints An array containing all available X vertex coordinates
 * @param xOffset The offset from which xPoints will begin being read from
 * @param inYPoints An array containing all available Y vertex coordinates
 * @param yOffset The offset from which yPoints will begin being read from
 * @param nPoints How many points should be used to construct the polygon.
 * @param safePoints If @link SJME_JNI_TRUE @endlink then the passed
 * points are considered to be safely used in calculations and will be
 * modified, this must be set to @link SJME_JNI_FALSE @endlink when the
 * input coordinates cannot be modified.
 * @return If any argument is @c NULL or if the offset and/or number of
 * points is not valid.
 * @since 2025/12/04
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillPolygonPrimFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints,
	sjme_attrInValue sjme_jboolean safePoints);

/**
 * Performs rectangular fill in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Performs rectangular fill with rounded borders in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @param arcWidth The width of the border rounding
 * @param arcHeight The height of the border rounding
 * @return An error on @c NULL arguments.
 * @since 2025/11/25
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillRoundRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight);

/**
 * Draws a filled triangle using the current color, the lines which make
 * up the triangle are included in the filled area.
 *
 * @param g The graphics to use for drawing.
 * @param x1 First X coordinate.
 * @param y1 First Y coordinate.
 * @param x2 Second X coordinate.
 * @param y2 Second Y coordinate.
 * @param x3 Third X coordinate.
 * @param y3 Third Y coordinate.
 * @return An error if no graphics were specified or the graphics does
 * not actually support the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillTriangleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

/**
 * Reads a region of pixel data from a hardware graphics context.
 * 
 * Note that if the hardware graphics does not support reading of
 * pixel data then the destination buffer may be left unmodified,
 * filled with a specific value, or filled with off-screen buffer
 * pixels that may not reflect what is visible on the screen.
 *
 * @param g The hardware graphics to draw with.
 * @param pf Integer representing the format that the target's data must be
 * converted to before being placed into the data buffer.
 * @param data The destination buffer.
 * @param off The offset into the buffer.
 * @param dataLen The total length of the data buffer.
 * @param scanLen The scanline length.
 * @param alpha If this argument is @code true @endcode, it means we must
 * blend the content retrieved from the graphics context with the destination
 * buffer's as opposed to overwriting its contents entirely.
 * @param xSrc The source X position.
 * @param ySrc The source Y position.
 * @param wSrc The width of the source region.
 * @param hSrc The height of the source region.
 * @param anchor The anchor point.
 * @return Any resultant error, if any.
 * @since 2025/12/04
 */
typedef sjme_errorCode (*sjme_scritchui_pencilGetRegionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint pf,
	sjme_attrInNotNull sjme_cpointer data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint anchor);

/**
 * Locks the pencil for drawing.
 * 
 * @param g The pencil to lock.
 * @return Any resultant error, if any. Return the specific
 * error code @link SJME_ERROR_RESOURCE_NOT_FOUND @endlink if the resource
 * backing the pencil is no longer available.
 * @since 2024/07/08
 */
typedef sjme_errorCode (*sjme_scritchui_pencilLockFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Releases the pencil drawing lock.
 * 
 * @param g The pencil to unlock.
 * @return Any resultant error, if any.
 * @since 2024/07/08
 */
typedef sjme_errorCode (*sjme_scritchui_pencilLockReleaseFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Maps a color to or from a raw color.
 * 
 * @param g The pencil to operate within.
 * @param fromRaw If @link SJME_JNI_TRUE @endlink the input color is
 * considered to be a raw pixel.
 * @param inRgbOrRaw The input value to map.
 * @param outColor The resultant full color set.
 * @return Any resultant error.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilMapColorFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);

/**
 * Reads the given pixel format from a single scanline at the given position. 
 * 
 * @param g The graphics to read from.
 * @param pf The pixel format to read as.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param dest The resultant pixel data.
 * @param inDataLen Length of the data buffer.
 * @param inNumPixels The number of pixels to read.
 * @return Any resultant error code.
 * @since 2025/11/28
 */
typedef sjme_errorCode (*sjme_scritchui_pencilPfScanGetFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inDataLen) sjme_pointer dest,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);
	
/**
 * Writes specific pixel format data to a single scanline at the given
 * position. 
 * 
 * @param g The graphics to write to.
 * @param pf The pixel format to write.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param src Source data to write.
 * @param inNumPixels The number of pixels to read.
 * @param mulAlpha Should alpha values in the input buffer be multiplied by
 * the current alpha value? That is the buffer has significant alpha values.
 * @param mulAlphaValue The value to multiply with.
 * @return Any resultant error code.
 * @since 2025/11/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilPfScanPutFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer src,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue);

/**
 * Maps the number of bits needed to represent the specified number of
 * pixels in a buffer for the given pixel format.
 * 
 * @param g The pencil to operate with.
 * @param pf The pixel format to map.
 * @param inPixels The number of pixels to map.
 * @param inBits The number of bits to use for limit calculation.
 * @param outBits The number of bits used to represent the raw pixel data.
 * @param outLimit Optional output value for the smaller of @c outBytes
 * and @c inBytes .
 * @return Any resultant error.
 * @since 2025/11/28
 */
typedef sjme_errorCode (*sjme_scritchui_pencilPfScanBitsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBits,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBits,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit);

/**
 * Maps the number of bytes needed to represent the specified number of
 * pixels in a buffer for the given pixel format.
 * 
 * @param g The pencil to operate with.
 * @param pf The pixel format to map.
 * @param inPixels The number of pixels to map.
 * @param inBytes The number of bytes to use for limit calculation.
 * @param outBytes The number of bytes used to represent the raw pixel data.
 * @param outLimit Optional output value for the smaller of @c outBytes
 * and @c inBytes .
 * @return Any resultant error.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilPfScanBytesFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBytes,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit);

/**
 * Maps a pixel format from one scan format to another.
 *
 * @param g The graphics context this is operating under.
 * @param destPf The destination pixel format.
 * @param dest The destination buffer.
 * @param destRawOff The destination offset into the buffer in raw bytes.
 * @param destRawLen The destination length of the buffer in raw bytes, if
 * this is @code -1 @endcode then this is determined by the pixel format
 * and @a inNumPixels .
 * @param srcPf The source pixel format.
 * @param srcRawOff The source offset into the buffer in raw bytes.
 * @param srcRawLen The source length of the buffer in raw bytes, if
 * this is @code -1 @endcode then this is determined by the pixel format
 * and @a inNumPixels .
 * @param src The source buffer.
 * @param inNumPixels The number of pixels to copy.
 * @return Any resultant error, if any.
 * @since 2025/11/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilPfScanToPfFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat destPf,
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInPositive sjme_jint destRawOff,
	sjme_attrInNegativeOnePositive sjme_jint destRawLen,
	sjme_attrInValue sjme_gfx_pixelFormat srcPf,
	sjme_attrInNotNull sjme_cpointer src,
	sjme_attrInPositive sjme_jint srcRawOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRawLen,
	sjme_attrInPositive sjme_jint inNumPixels);

/**
 * Maps a pixel format from one scan format to RGB.
 *
 * @param g The graphics context this is operating under.
 * @param destRgb The destination RGB buffer.
 * @param destRgbOff The destination offset into the buffer in RGB bytes.
 * @param destRgbLen The destination length of the buffer in RGB bytes, if
 * this is @code -1 @endcode then this is determined by @a inNumPixels .
 * @param srcPf The source pixel format.
 * @param srcRawOff The source offset into the buffer in raw bytes.
 * @param srcRawLen The source length of the buffer in raw bytes, if
 * this is @code -1 @endcode then this is determined by the pixel format
 * and @a inNumPixels .
 * @param src The source buffer.
 * @param inNumPixels The number of pixels to copy.
 * @return Any resultant error, if any.
 * @since 2025/11/28
 */
typedef sjme_errorCode (*sjme_scritchui_pencilPfScanToRgbFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* destRgb,
	sjme_attrInPositive sjme_jint destRgbOff,
	sjme_attrInNegativeOnePositive sjme_jint destRgbLen,
	sjme_attrInValue sjme_gfx_pixelFormat srcPf,
	sjme_attrInNotNull sjme_pointer src,
	sjme_attrInPositive sjme_jint srcRawOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRawLen,
	sjme_attrInPositive sjme_jint inNumPixels);
	
/**
 * Fills a buffer with the given value. 
 * 
 * @param g The graphics owning this.
 * @param outRaw The output raw scan buffer.
 * @param outRawOff Offset into the raw scan buffer.
 * @param outRawLen Length of the raw scan buffer.
 * @param rawPixel The raw pixel to fill with.
 * @param inNumPixels The number of pixels to fill.
 * @return Any resultant error code.
 * @since 2024/07/10
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanFillFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) sjme_pointer outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

/**
 * Reads raw data from a single scanline at the given position. 
 * 
 * @param g The graphics to read from.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param outData The resultant pixel data.
 * @param inDataLen Length of the data buffer.
 * @param inNumPixels The number of pixels to read.
 * @return Any resultant error code.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanGetFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

/**
 * Writes raw data to a single scanline at the given position, no alpha
 * blending is performed. 
 * 
 * @param g The graphics to write to.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param srcRaw The raw pixel data to write.
 * @param srcRawLen Length of the data buffer.
 * @param srcNumPixels The number of source pixels.
 * @return Any resultant error code.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanPutPureFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen,
	sjme_attrInPositiveNonZero sjme_jint srcNumPixels);

/**
 * Maps a raw scanline from raw RGB data.
 * 
 * @param g The graphics to operate within.
 * @param outRgb The output RGB data.
 * @param outRgbOff The offset into the RGB buffer.
 * @param outRgbLen The length of the RGB buffer.
 * @param inRaw The input raw scan buffer.
 * @param inRawOff Offset into the raw scan buffer.
 * @param inRawLen Length of the raw scan buffer.
 * @return Any resultant error, if any.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanToRgbFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(outRgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(inRawLen) sjme_cpointer inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen);

/**
 * Fills a scan with the given RGB value. 
 * 
 * @param g The graphics owning this.
 * @param outRgb The output RGB scan buffer.
 * @param outRgbOff Offset into the RGB scan buffer.
 * @param inNumPixels The number of pixels to fill.
 * @param inValue The raw pixel to fill with.
 * @return Any resultant error code.
 * @since 2024/07/10
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanFillFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(inNumPixels) sjme_jint* outRgb,
	sjme_attrInPositiveNonZero sjme_jint outRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jint inValue);

/**
 * Reads raw data from a single scanline at the given position. 
 * 
 * @param g The graphics to read from.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param destRgb The RGB data that has come from the image.
 * @param inNumPixels The number of pixels to read.
 * @return Any resultant error code.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanGetFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_jint* destRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

/**
 * Writes RGB data to a single scanline at the given position. 
 * 
 * @param g The graphics to write to.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param srcRgb Source RGB data to write.
 * @param inNumPixels The number of pixels to read.
 * @param srcAlpha Does the source data have an alpha channel?
 * @param mulAlpha Should alpha values in the input buffer be multiplied by
 * the current alpha value? That is the buffer has significant alpha values.
 * @param mulAlphaValue The value to multiply with.
 * @return Any resultant error code.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanPutFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const sjme_jint* srcRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue);

/**
 * Maps a pixel format from one scan format to RGB.
 *
 * @param g The graphics context this is operating under.
 * @param destPf The destination pixel format.
 * @param dest The destination buffer.
 * @param destRawOff The destination offset into the buffer in raw bytes.
 * @param destRawLen The destination length of the buffer in raw bytes, if
 * this is @code -1 @endcode then this is determined by the pixel format
 * and @a inNumPixels .
 * @param srcRgb The source RGB buffer
 * @param srcRgbOff The source offset into the buffer in RGB bytes.
 * @param srcRgbLen The source length of the buffer in RGB bytes, if
 * this is @code -1 @endcode then this is determined by @a inNumPixels .
 * @param inNumPixels The number of pixels to copy.
 * @return Any resultant error, if any.
 * @since 2025/11/28
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanToPfFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat destPf,
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInPositive sjme_jint destRawOff,
	sjme_attrInNegativeOnePositive sjme_jint destRawLen,
	sjme_attrInNotNull const sjme_jint* srcRgb,
	sjme_attrInPositive sjme_jint srcRgbOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRgbLen,
	sjme_attrInPositive sjme_jint inNumPixels);
	
/**
 * Maps a raw scanline from raw RGB data.
 * 
 * @param g The graphics to operate within.
 * @param outRaw The output raw scan buffer.
 * @param outRawOff Offset into the raw scan buffer.
 * @param outRawLen Length of the raw scan buffer.
 * @param inRgb The input RGB data.
 * @param inRgbOff The offset into the RGB buffer.
 * @param inRgbLen The length of the RGB buffer.
 * @return Any resultant error, if any.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanToRawFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) sjme_pointer outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInNotNullBuf(rgbLen) const sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositive sjme_jint inRgbLen);

/**
 * Sets the alpha color for graphics.
 * 
 * @param g The hardware graphics to draw with.
 * @param argb The color to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetAlphaColorFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb);

/**
 * Sets the blending mode to use.
 * 
 * @param g The hardware graphics to draw with.
 * @param mode The blending mode to use.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetBlendingModeFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode);

/**
 * Sets the clipping rectangle position.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetClipFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Sets that the graphics should now use the default font.
 * 
 * @param g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetDefaultFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Sets the default for the pencil.
 * 
 * @param g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/08/13
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetDefaultsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Sets to use the specified font.
 *
 * @param g The graphics used.
 * @param font The base font to use.
 * @param params The font parameters to set, if @code NULL @endcode then this
 * is derived from the font.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* params);

/**
 * Copies the parameters from the other pencil to this one.
 *
 * @param g The graphics used.
 * @param from The pencil to copy the parameters from.
 * @return An error if the graphics is not valid.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetParametersFromFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from);

/**
 * Sets the stroke style for the hardware graphics.
 * 
 * @param g The hardware graphics to draw with.
 * @param style The stroke type to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetStrokeStyleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style);

/**
 * Transfers pixel data from one pencil to another and draws it onto the
 * current pencil, this is a helper function to reduce the need to load
 * pixel data from another pencil and then draw it onto another.
 * 
 * Note that both pencils must be under the same ScritchUI state as the source
 * pencil does need to be locked accordingly.
 * 
 * Reading from the source pencil follows the same semantics as the
 * functions @link sjme_scritchui_pencilCopyAreaFunc @endlink
 * and @link sjme_scritchui_pencilGetRegionFunc @endlink , if the source
 * pencil does not support reading pixel data then what is drawn onto the
 * destination is undefined.
 *
 * @param g The hardware graphics to draw with.
 * @param srcPencil The pencil to copy from.
 * @param alpha Drawing with the alpha channel?
 * @param xSrc The source X position.
 * @param ySrc The source Y position.
 * @param wSrc The width of the source region.
 * @param hSrc The height of the source region.
 * @param trans Sprite translation and/or rotation,
 * see @code javax.microedition.lcdui.game.Sprite @endcode.
 * @param xDest The destination X position, is translated.
 * @param yDest The destination Y position, is translated.
 * @param anchor The anchor point.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @param mode The region transfer mode.
 * @return Any resultant error, if any
 * @since 2025/12/22
 */
typedef sjme_errorCode (*sjme_scritchui_pencilTransferRegionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil srcPencil,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInValue sjme_scritchui_transferRegionMode mode);

/**
 * Translates drawing operations.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X translation.
 * @param y The Y translation.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilTranslateFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);
	
#pragma endregion(scritchui_pencil)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCS_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIFUNCS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUIFUNCS_H */
