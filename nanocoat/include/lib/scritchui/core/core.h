/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Core.
 * 
 * @since 2024/04/06
 */

#ifndef SQUIRRELJME_CORE_H
#define SQUIRRELJME_CORE_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CORE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_scritchui_core_apiInit(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd);

sjme_errorCode sjme_scritchui_core_choiceGetSelectedIndex(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outIndex);

sjme_errorCode sjme_scritchui_core_choiceItemGet(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate);

sjme_errorCode sjme_scritchui_core_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex);

sjme_errorCode sjme_scritchui_core_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex);

sjme_errorCode sjme_scritchui_core_choiceItemRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_core_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled);

sjme_errorCode sjme_scritchui_core_choiceItemSetImage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_core_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected);

sjme_errorCode sjme_scritchui_core_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString);

sjme_errorCode sjme_scritchui_core_choiceLength(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength);

sjme_errorCode sjme_scritchui_core_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_core_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus);

sjme_errorCode sjme_scritchui_core_componentGetParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_uiComponent* outParent);

sjme_errorCode sjme_scritchui_core_componentPosition(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY);

sjme_errorCode sjme_scritchui_core_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
sjme_errorCode sjme_scritchui_core_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_core_componentSetActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(activate));

sjme_errorCode sjme_scritchui_core_componentSetInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input));

sjme_errorCode sjme_scritchui_core_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint));

sjme_errorCode sjme_scritchui_core_componentSetValueUpdateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(valueUpdate));

sjme_errorCode sjme_scritchui_core_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight);

sjme_errorCode sjme_scritchui_core_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent);

sjme_errorCode sjme_scritchui_core_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent);

sjme_errorCode sjme_scritchui_core_containerRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer);

sjme_errorCode sjme_scritchui_core_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_core_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size));
	
sjme_errorCode sjme_scritchui_core_componentSetVisibleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible));

sjme_errorCode sjme_scritchui_core_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont);
	
sjme_errorCode sjme_scritchui_core_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived);

sjme_errorCode sjme_scritchpen_core_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEnd* pencilFrontEndCopy);

sjme_errorCode sjme_scritchui_core_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString);
	
sjme_errorCode sjme_scritchui_core_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor);

sjme_errorCode sjme_scritchui_core_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType);

sjme_errorCode sjme_scritchui_core_loopExecute(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_core_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_core_loopExecuteWait(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_core_loopIsInThread(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_jboolean* outInThread);

sjme_errorCode sjme_scritchui_core_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);
	
sjme_errorCode sjme_scritchui_core_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar);
	
sjme_errorCode sjme_scritchui_core_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem);

sjme_errorCode sjme_scritchui_core_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem);

sjme_errorCode sjme_scritchui_core_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu);

sjme_errorCode sjme_scritchui_core_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex);

sjme_errorCode sjme_scritchui_core_menuRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu);

sjme_errorCode sjme_scritchui_core_objectDelete(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject);

sjme_errorCode sjme_scritchui_core_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus);

sjme_errorCode sjme_scritchui_core_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel);

sjme_errorCode sjme_scritchui_core_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen));

sjme_errorCode sjme_scritchui_core_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens);

sjme_errorCode sjme_scritchui_core_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScrollPanel* outScrollPanel);

sjme_errorCode sjme_scritchui_core_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect);

sjme_errorCode sjme_scritchui_core_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea);

sjme_errorCode sjme_scritchui_core_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos);

sjme_errorCode sjme_scritchui_core_viewSetSizeSuggestListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(sizeSuggest));
	
sjme_errorCode sjme_scritchui_core_viewSetViewListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view));

sjme_errorCode sjme_scritchui_core_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
sjme_errorCode sjme_scritchui_core_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow);

sjme_errorCode sjme_scritchui_core_windowSetCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close));
	
sjme_errorCode sjme_scritchui_core_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar);

sjme_errorCode sjme_scritchui_core_windowSetMenuItemActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(menuItemActivate));

sjme_errorCode sjme_scritchui_core_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

sjme_errorCode sjme_scritchui_core_intern_bindFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent atRover,
	sjme_attrInNotNull sjme_scritchui_uiComponent bindComponent,
	sjme_attrInValue sjme_jboolean isGrabbing);

sjme_errorCode sjme_scritchui_core_intern_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont);

sjme_errorCode sjme_scritchui_core_intern_getChoice(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiChoice* outChoice);

sjme_errorCode sjme_scritchui_core_intern_getContainer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiContainer* outContainer);

sjme_errorCode sjme_scritchui_core_intern_getLabeled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInOutNotNull sjme_scritchui_uiLabeled* outLabeled);

sjme_errorCode sjme_scritchui_core_intern_getMenuHasChildren(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasChildren* outHasChildren);

sjme_errorCode sjme_scritchui_core_intern_getMenuHasParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind inMenuKind,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuHasParent* outHasParent);

sjme_errorCode sjme_scritchui_core_intern_getPaintable(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiPaintable* outPaintable);

sjme_errorCode sjme_scritchui_core_intern_getView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiView* outView);

sjme_errorCode sjme_scritchui_core_intern_initCommon(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInValue sjme_jboolean postCreate,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType);

sjme_errorCode sjme_scritchui_core_intern_initComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean postCreate,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType);

sjme_errorCode sjme_scritchui_core_intern_mapScreen(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jint screenId,
	sjme_attrInOutNotNull sjme_scritchui_uiScreen* outScreen,
	sjme_attrInNullable sjme_scritchui_handle updateHandle);

sjme_errorCode sjme_scritchui_intern_menuItemActivate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind atRover,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind itemActivated);

sjme_errorCode sjme_scritchui_intern_menuItemActivateById(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind atRover,
	sjme_attrInNotNull sjme_jint itemActivated,
	sjme_attrInValue sjme_jint itemMask);

sjme_errorCode sjme_scritchui_core_intern_setSimpleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoAny,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void));

sjme_errorCode sjme_scritchui_core_intern_updateVisibleComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean isVisible);

sjme_errorCode sjme_scritchui_core_intern_updateVisibleContainer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInValue sjme_jboolean isVisible);

sjme_errorCode sjme_scritchui_core_intern_updateVisibleWindow(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);
	
sjme_errorCode sjme_scritchui_core_intern_viewSuggest(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_dim* suggestDim);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CORE_H
}
		#undef SJME_CXX_SQUIRRELJME_CORE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CORE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CORE_H */
