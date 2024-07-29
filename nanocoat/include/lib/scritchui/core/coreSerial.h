/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * GUI serialization enforcement wrappers.
 * 
 * @since 2024/04/16
 */

#ifndef SQUIRRELJME_CORESERIAL_H
#define SQUIRRELJME_CORESERIAL_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CORESERIAL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The type of serial call to perform.
 * 
 * @since 2024/04/17
 */
typedef enum sjme_scritchui_serialType
{
	/** Unknown. */
	SJME_SCRITCHUI_SERIAL_TYPE_UNKNOWN,
	
	/** @c choiceGetSelectedIndex . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_GET_SELECTED_INDEX,
	
	/** @c choiceItemGet . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_GET,
	
	/** @c choiceItemInsert . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_INSERT,
	
	/** @c choiceItemRemove . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_REMOVE,
	
	/** @c choiceItemRemoveAll . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_REMOVE_ALL,
	
	/** @c choiceItemSetEnabled . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_ENABLED,
	
	/** @c choiceItemSetImage . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_IMAGE,
	
	/** @c choiceItemSetSelected . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_SELECTED,
	
	/** @c choiceItemSetString . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_STRING,
	
	/** @c choiceLength . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_LENGTH,
	
	/** @c componentFocusGrab . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_FOCUS_GRAB,
	
	/** @c componentFocusHas . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_FOCUS_HAS,
	
	/** @c componentRepaint . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REPAINT,
	
	/** @c componentRevalidate . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
	
	/** @c componentSetActivateListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_SET_ACTIVATE_LISTENER,
	
	/** @c componentSetInputListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_INPUT_LISTENER,
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
	
	/** @c componentSetSizeListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER,
	
	/** @c componentSetValueUpdateListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VALUE_UPDATE_LISTENER,
	
	/** @c componentSetVisibleListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VISIBLE_LISTENER,
	
	/** @c componentSize . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SIZE,
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
	
	/** @c containerRemove . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE,
	
	/** @c containerRemoveAll . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE_ALL,
	
	/** @c containerSetBounds . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
	
	/** @c fontBuiltin . */
	SJME_SCRITCHUI_SERIAL_TYPE_FONT_BUILTIN,
	
	/** @c fontDerive . */
	SJME_SCRITCHUI_SERIAL_TYPE_FONT_DERIVE,
	
	/** @c hardwareGraphics . */
	SJME_SCRITCHUI_SERIAL_TYPE_HARDWARE_GRAPHICS,
	
	/** @c labelSetString . */
	SJME_SCRITCHUI_SERIAL_TYPE_LABEL_SET_STRING,
	
	/** @c lafElementColor. */
	SJME_SCRITCHUI_SERIAL_TYPE_LAF_ELEMENT_COLOR,
		
	/** @c listNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_LIST_NEW,
		
	/** @c menuBarNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_MENU_BAR_NEW,
	
	/** @c menuInsert . */
	SJME_SCRITCHUI_SERIAL_TYPE_MENU_INSERT,
		
	/** @c menuItemNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_MENU_ITEM_NEW,
		
	/** @c menuNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_MENU_NEW,
	
	/** @c menuRemove . */
	SJME_SCRITCHUI_SERIAL_TYPE_MENU_REMOVE,
	
	/** @c menuRemoveAll . */
	SJME_SCRITCHUI_SERIAL_TYPE_MENU_REMOVE_ALL,
	
	/** @c objectDelete . */
	SJME_SCRITCHUI_SERIAL_TYPE_OBJECT_DELETE,
		
	/** @c panelEnableFocus . */
	SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		
	/** @c panelNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
	
	/** @c screenSetListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		
	/** @c screens . */
	SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
	
	/** @c windowContentMinimumSize . */
	SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
	
	/** @c windowNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
	
	/** @c windowSetCloseListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_CLOSE_LISTENER,
	
	/** @c windowSetMenuBar . */
	SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_MENU_BAR,
	
	/** @c windowSetVisible . */
	SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_VISIBLE,
	
	/** The number of serialized types. */
	SJME_SCRITCHUI_NUM_SERIAL_TYPES
} sjme_scritchui_serialType;

/** Serial set listener. */
#define SJME_SCRITCHUI_SERIAL_SET_LISTENER(what) \
	/** The listener to set. */ \
	volatile SJME_TOKEN_PASTE3(sjme_scritchui_, what, ListenerFunc) \
	inListener; \
	 \
	/** Any front-end data to set as needed. */ \
	sjme_frontEnd* volatile copyFrontEnd;

/** The name for serial data. */
#define SJME_SCRITCHUI_SERIAL_DATA_NAME(what) \
	SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what)

#define SUD_STRUCT_DEF(what, items) \
	typedef struct SJME_SCRITCHUI_SERIAL_DATA_NAME(what) \
	{ \
		items \
	} SJME_SCRITCHUI_SERIAL_DATA_NAME(what)

#define SDU_VAR(type, name) \
	volatile type name

#define SDU_VARP(type, name) \
	type* volatile name

/* clang-format off */
/* ------------------------------------------------------------------------ */

SUD_STRUCT_DEF(choiceGetSelectedIndex,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VARP(sjme_jint*, outIndex););
	
SUD_STRUCT_DEF(choiceItemGet,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, atIndex);
	SDU_VAR(sjme_scritchui_uiChoiceItem, outItemTemplate););

SUD_STRUCT_DEF(choiceItemInsert,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VARP(sjme_jint, inOutIndex););
	
SUD_STRUCT_DEF(choiceItemRemove,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, atIndex););
	
SUD_STRUCT_DEF(choiceItemRemoveAll,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent););
	
SUD_STRUCT_DEF(choiceItemSetEnabled,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, atIndex);
	SDU_VAR(sjme_jboolean, isEnabled););
	
SUD_STRUCT_DEF(choiceItemSetImage,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, atIndex);
	SDU_VARP(sjme_jint*, inRgb);
	SDU_VAR(sjme_jint, inRgbOff);
	SDU_VAR(sjme_jint, inRgbDataLen);
	SDU_VAR(sjme_jint, inRgbScanLen);
	SDU_VAR(sjme_jint, width);
	SDU_VAR(sjme_jint, height););
	
SUD_STRUCT_DEF(choiceItemSetSelected,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, atIndex);
	SDU_VAR(sjme_jboolean, isSelected););
	
SUD_STRUCT_DEF(choiceItemSetString,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, atIndex);
	SDU_VAR(sjme_lpcstr, inString););
	
SUD_STRUCT_DEF(choiceLength,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VARP(sjme_jint*, outLength););

SUD_STRUCT_DEF(componentFocusGrab,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent););

SUD_STRUCT_DEF(componentFocusHas,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VARP(sjme_jboolean*, outHasFocus););

SUD_STRUCT_DEF(componentRepaint,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, x);
	SDU_VAR(sjme_jint, y);
	SDU_VAR(sjme_jint, width);
	SDU_VAR(sjme_jint, height););

SUD_STRUCT_DEF(componentRevalidate,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent););

SUD_STRUCT_DEF(componentSetInputListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(input););

SUD_STRUCT_DEF(componentSetActivateListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(activate););
	
SUD_STRUCT_DEF(componentSetPaintListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(paint););

SUD_STRUCT_DEF(componentSetSizeListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(size););

SUD_STRUCT_DEF(componentSetValueUpdateListener,
	SDU_VAR(sjme_scritchui, inState);
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(valueUpdate););
	
SUD_STRUCT_DEF(componentSetVisibleListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(visible););

SUD_STRUCT_DEF(componentSize,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VARP(sjme_jint*, outWidth);
	SDU_VARP(sjme_jint*, outHeight););

SUD_STRUCT_DEF(containerAdd,
	SDU_VAR(sjme_scritchui_uiComponent, inContainer);
	SDU_VAR(sjme_scritchui_uiComponent, addComponent););

SUD_STRUCT_DEF(containerRemove,
	SDU_VAR(sjme_scritchui_uiComponent, inContainer);
	SDU_VAR(sjme_scritchui_uiComponent, removeComponent););

SUD_STRUCT_DEF(containerRemoveAll,
	SDU_VAR(sjme_scritchui_uiComponent, inContainer););

SUD_STRUCT_DEF(containerSetBounds,
	SDU_VAR(sjme_scritchui_uiComponent, inContainer);
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_jint, x);
	SDU_VAR(sjme_jint, y);
	SDU_VAR(sjme_jint, width);
	SDU_VAR(sjme_jint, height););

SUD_STRUCT_DEF(fontBuiltin,
	SDU_VARP(sjme_scritchui_pencilFont*, outFont););

SUD_STRUCT_DEF(fontDerive,
	SDU_VAR(sjme_scritchui_pencilFont, inFont);
	SDU_VAR(sjme_scritchui_pencilFontStyle, inStyle);
	SDU_VAR(sjme_jint, inPixelSize);
	SDU_VARP(sjme_scritchui_pencilFont*, outDerived););

SUD_STRUCT_DEF(hardwareGraphics,
	SDU_VARP(sjme_scritchui_pencil*, outPencil);
	SDU_VARP(sjme_alloc_weak*, outWeakPencil);
	SDU_VAR(sjme_gfx_pixelFormat, pf);
	SDU_VAR(sjme_jint, bw);
	SDU_VAR(sjme_jint, bh);
	SDU_VARP(const sjme_scritchui_pencilLockFunctions*, inLockFuncs);
	SDU_VARP(const sjme_frontEnd*, inLockFrontEndCopy);
	SDU_VAR(sjme_jint, sx);
	SDU_VAR(sjme_jint, sy);
	SDU_VAR(sjme_jint, sw);
	SDU_VAR(sjme_jint, sh);
	SDU_VARP(const sjme_frontEnd*, pencilFrontEndCopy););

SUD_STRUCT_DEF(labelSetString,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SDU_VAR(sjme_lpcstr, inString););

SUD_STRUCT_DEF(lafElementColor,
	SDU_VAR(sjme_scritchui_uiComponent, inContext);
	SDU_VARP(sjme_jint*, outRGB);
	SDU_VAR(sjme_scritchui_lafElementColorType, elementColor););

SUD_STRUCT_DEF(listNew,
	SDU_VARP(sjme_scritchui_uiList*, outList);
	SDU_VAR(sjme_scritchui_choiceType, inChoiceType););

SUD_STRUCT_DEF(menuBarNew,
	SDU_VARP(sjme_scritchui_uiMenuBar*, outMenuBar););

SUD_STRUCT_DEF(menuInsert,
	SDU_VARP(sjme_scritchui_uiMenuKind, intoMenu);
	SDU_VAR(sjme_jint, atIndex);
	SDU_VARP(sjme_scritchui_uiMenuKind, childItem););
	
SUD_STRUCT_DEF(menuItemNew,
	SDU_VARP(sjme_scritchui_uiMenuItem*, outMenuItem););

SUD_STRUCT_DEF(menuNew,
	SDU_VARP(sjme_scritchui_uiMenu*, outMenu););

SUD_STRUCT_DEF(menuRemove,
	SDU_VARP(sjme_scritchui_uiMenuKind, fromMenu);
	SDU_VAR(sjme_jint, atIndex););

SUD_STRUCT_DEF(menuRemoveAll,
	SDU_VARP(sjme_scritchui_uiMenuKind, fromMenu););

SUD_STRUCT_DEF(objectDelete,
	SDU_VARP(sjme_scritchui_uiCommon*, inOutObject););
	
SUD_STRUCT_DEF(panelEnableFocus,
	SDU_VAR(sjme_scritchui_uiPanel, inPanel);
	SDU_VAR(sjme_jboolean, enableFocus);
	SDU_VAR(sjme_jboolean, defaultFocus););

SUD_STRUCT_DEF(panelNew,
	SDU_VARP(sjme_scritchui_uiPanel*, outPanel););

SUD_STRUCT_DEF(screenSetListener,
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(screen););

SUD_STRUCT_DEF(screens,
	SDU_VARP(sjme_scritchui_uiScreen*, outScreens);
	SDU_VARP(sjme_jint*, inOutNumScreens););

SUD_STRUCT_DEF(windowContentMinimumSize,
	SDU_VAR(sjme_scritchui_uiWindow, inWindow);
	SDU_VAR(sjme_jint, width);
	SDU_VAR(sjme_jint, height););

SUD_STRUCT_DEF(windowNew,
	SDU_VARP(sjme_scritchui_uiWindow*, outWindow););

SUD_STRUCT_DEF(windowSetCloseListener,
	SDU_VAR(sjme_scritchui_uiWindow, inWindow);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(close););

SUD_STRUCT_DEF(windowSetMenuBar,
	SDU_VAR(sjme_scritchui_uiWindow, inWindow);
	SDU_VAR(sjme_scritchui_uiMenuBar, inMenuBar););
	
SUD_STRUCT_DEF(windowSetVisible,
	SDU_VAR(sjme_scritchui_uiWindow, inWindow);
	SDU_VAR(sjme_jboolean, isVisible););

/* ------------------------------------------------------------------------ */
/* clang-format on */

#undef SUD_STRUCT_DEF
#undef SDU_VAR
#undef SDU_VARP

/** Define serial data union quicker. */
#define SJME_SCRITCHUI_SDU_DEF(what) \
	volatile SJME_SCRITCHUI_SERIAL_DATA_NAME(what) what

/**
 * Union for serial data.
 * 
 * @since 2024/04/17
 */
typedef union sjme_scritchui_serialDataUnion
{
	SJME_SCRITCHUI_SDU_DEF(choiceGetSelectedIndex);
	SJME_SCRITCHUI_SDU_DEF(choiceItemGet);
	SJME_SCRITCHUI_SDU_DEF(choiceItemInsert);
	SJME_SCRITCHUI_SDU_DEF(choiceItemRemove);
	SJME_SCRITCHUI_SDU_DEF(choiceItemRemoveAll);
	SJME_SCRITCHUI_SDU_DEF(choiceItemSetEnabled);
	SJME_SCRITCHUI_SDU_DEF(choiceItemSetImage);
	SJME_SCRITCHUI_SDU_DEF(choiceItemSetSelected);
	SJME_SCRITCHUI_SDU_DEF(choiceItemSetString);
	SJME_SCRITCHUI_SDU_DEF(choiceLength);
	SJME_SCRITCHUI_SDU_DEF(componentFocusGrab);
	SJME_SCRITCHUI_SDU_DEF(componentFocusHas);
	SJME_SCRITCHUI_SDU_DEF(componentRepaint);
	SJME_SCRITCHUI_SDU_DEF(componentRevalidate);
	SJME_SCRITCHUI_SDU_DEF(componentSetActivateListener);
	SJME_SCRITCHUI_SDU_DEF(componentSetInputListener);
	SJME_SCRITCHUI_SDU_DEF(componentSetPaintListener);
	SJME_SCRITCHUI_SDU_DEF(componentSetSizeListener);
	SJME_SCRITCHUI_SDU_DEF(componentSetValueUpdateListener);
	SJME_SCRITCHUI_SDU_DEF(componentSetVisibleListener);
	SJME_SCRITCHUI_SDU_DEF(componentSize);
	SJME_SCRITCHUI_SDU_DEF(containerAdd);
	SJME_SCRITCHUI_SDU_DEF(containerRemove);
	SJME_SCRITCHUI_SDU_DEF(containerRemoveAll);
	SJME_SCRITCHUI_SDU_DEF(containerSetBounds);
	SJME_SCRITCHUI_SDU_DEF(fontBuiltin);
	SJME_SCRITCHUI_SDU_DEF(fontDerive);
	SJME_SCRITCHUI_SDU_DEF(hardwareGraphics);
	SJME_SCRITCHUI_SDU_DEF(labelSetString);
	SJME_SCRITCHUI_SDU_DEF(lafElementColor);
	SJME_SCRITCHUI_SDU_DEF(listNew);
	SJME_SCRITCHUI_SDU_DEF(menuBarNew);
	SJME_SCRITCHUI_SDU_DEF(menuInsert);
	SJME_SCRITCHUI_SDU_DEF(menuItemNew);
	SJME_SCRITCHUI_SDU_DEF(menuNew);
	SJME_SCRITCHUI_SDU_DEF(menuRemove);
	SJME_SCRITCHUI_SDU_DEF(menuRemoveAll);
	SJME_SCRITCHUI_SDU_DEF(objectDelete);
	SJME_SCRITCHUI_SDU_DEF(panelEnableFocus);
	SJME_SCRITCHUI_SDU_DEF(panelNew);
	SJME_SCRITCHUI_SDU_DEF(screenSetListener);
	SJME_SCRITCHUI_SDU_DEF(screens);
	SJME_SCRITCHUI_SDU_DEF(windowContentMinimumSize);
	SJME_SCRITCHUI_SDU_DEF(windowNew);
	SJME_SCRITCHUI_SDU_DEF(windowSetCloseListener);
	SJME_SCRITCHUI_SDU_DEF(windowSetMenuBar);
	SJME_SCRITCHUI_SDU_DEF(windowSetVisible);
} sjme_scritchui_serialDataUnion;

/* No longer needed. */
#undef SJME_SCRITCHUI_SDU_DEF

/**
 * Data for the serial call.
 * 
 * @since 2024/04/17
 */
typedef struct sjme_scritchui_serialData
{
	/** The type of call this is. */
	volatile sjme_scritchui_serialType type;
	
	/** The error value returned. */
	volatile sjme_errorCode error;
	
	/** The current state being invoked for. */
	volatile sjme_scritchui state;
	
	/** The serial data. */
	volatile sjme_scritchui_serialDataUnion data;
} sjme_scritchui_serialData;

sjme_errorCode sjme_scritchui_coreSerial_choiceGetSelectedIndex(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outIndex);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemGet(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetImage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected);

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString);

sjme_errorCode sjme_scritchui_coreSerial_choiceLength(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength);

sjme_errorCode sjme_scritchui_coreSerial_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_coreSerial_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus);

sjme_errorCode sjme_scritchui_coreSerial_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_coreSerial_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	
sjme_errorCode sjme_scritchui_coreSerial_componentSetActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(activate));

sjme_errorCode sjme_scritchui_coreSerial_componentSetInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input));

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint));
	
sjme_errorCode sjme_scritchui_coreSerial_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size));

sjme_errorCode sjme_scritchui_coreSerial_componentSetValueUpdateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(valueUpdate));

sjme_errorCode sjme_scritchui_coreSerial_componentSetVisibleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible));
	
sjme_errorCode sjme_scritchui_coreSerial_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight);

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent);
	
sjme_errorCode sjme_scritchui_coreSerial_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent);
	
sjme_errorCode sjme_scritchui_coreSerial_containerRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer);
	
sjme_errorCode sjme_scritchui_coreSerial_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_coreSerial_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont);
	
sjme_errorCode sjme_scritchui_coreSerial_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived);
	
sjme_errorCode sjme_scritchui_coreSerial_hardwareGraphics(
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
	
sjme_errorCode sjme_scritchui_coreSerial_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_lpcstr inString);
	
sjme_errorCode sjme_scritchui_coreSerial_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor);

sjme_errorCode sjme_scritchui_coreSerial_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType);
	
sjme_errorCode sjme_scritchui_coreSerial_objectDelete(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject);

sjme_errorCode sjme_scritchui_coreSerial_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar);
	
sjme_errorCode sjme_scritchui_coreSerial_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem);

sjme_errorCode sjme_scritchui_coreSerial_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem);

sjme_errorCode sjme_scritchui_coreSerial_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu);
	
sjme_errorCode sjme_scritchui_coreSerial_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex);

sjme_errorCode sjme_scritchui_coreSerial_menuRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu);

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus);

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel);

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen));

sjme_errorCode sjme_scritchui_coreSerial_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens);

sjme_errorCode sjme_scritchui_coreSerial_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScrollPanel* outScrollPanel);

sjme_errorCode sjme_scritchui_coreSerial_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect);

sjme_errorCode sjme_scritchui_coreSerial_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea);

sjme_errorCode sjme_scritchui_coreSerial_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_rect* inViewRect);

sjme_errorCode sjme_scritchui_coreSerial_viewSetViewListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view));

sjme_errorCode sjme_scritchui_coreSerial_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);

sjme_errorCode sjme_scritchui_coreSerial_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow);

sjme_errorCode sjme_scritchui_coreSerial_windowSetCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close));
	
sjme_errorCode sjme_scritchui_coreSerial_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar);

sjme_errorCode sjme_scritchui_coreSerial_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CORESERIAL_H
}
		#undef SJME_CXX_SQUIRRELJME_CORESERIAL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CORESERIAL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CORESERIAL_H */
