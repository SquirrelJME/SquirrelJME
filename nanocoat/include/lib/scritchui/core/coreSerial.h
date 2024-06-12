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
	
	/** @c componentRepaint . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REPAINT,
	
	/** @c componentRevalidate . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
	
	/** @c componentSetSizeListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER,
	
	/** @c componentSize . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SIZE,
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
	
	/** @c containerSetBounds . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
	
	/** @c fontBuiltin . */
	SJME_SCRITCHUI_SERIAL_TYPE_FONT_BUILTIN,
		
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
	sjme_frontEnd* volatile copyFrontEnd; \

typedef struct sjme_scritchui_serialData_componentRepaint
{
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
	
	/** The X position. */
	volatile sjme_jint x;
	
	/** The Y position. */
	volatile sjme_jint y;
	
	/** The width. */
	volatile sjme_jint width;
	
	/** The height. */
	volatile sjme_jint height;
} sjme_scritchui_serialData_componentRepaint;

typedef struct sjme_scritchui_serialData_componentRevalidate
{
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
} sjme_scritchui_serialData_componentRevalidate;

typedef struct sjme_scritchui_serialData_componentSetPaintListener
{
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
	
	/** The listener to set. */
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(paint);
} sjme_scritchui_serialData_componentSetPaintListener;

typedef struct sjme_scritchui_serialData_componentSetSizeListener
{
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
	
	/** The listener to set. */
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(size);
} sjme_scritchui_serialData_componentSetSizeListener;

typedef struct sjme_scritchui_serialData_componentSize
{
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
	
	/** The output width. */
	sjme_jint* volatile outWidth;
	
	/** The output height. */
	sjme_jint* volatile outHeight;
} sjme_scritchui_serialData_componentSize;

typedef struct sjme_scritchui_serialData_containerAdd
{
	/** The input container. */
	volatile sjme_scritchui_uiComponent inContainer;
	
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
} sjme_scritchui_serialData_containerAdd;

typedef struct sjme_scritchui_serialData_containerSetBounds
{
	/** The input container. */
	volatile sjme_scritchui_uiComponent inContainer;
	
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
	
	/** The X position. */
	volatile sjme_jint x;
	
	/** The Y position. */
	volatile sjme_jint y;
	
	/** The width. */
	volatile sjme_jint width;
	
	/** The height. */
	volatile sjme_jint height;
} sjme_scritchui_serialData_containerSetBounds;

typedef struct sjme_scritchui_serialData_fontBuiltin
{
	/** The resultant font. */
	volatile sjme_scritchui_pencilFont* outFont;
} sjme_scritchui_serialData_fontBuiltin;
	
typedef struct sjme_scritchui_serialData_panelEnableFocus
{
	/** The input panel. */
	volatile sjme_scritchui_uiPanel inPanel;
	
	/** Should focus be enabled? */
	volatile sjme_jboolean enableFocus;
} sjme_scritchui_serialData_panelEnableFocus;

typedef struct sjme_scritchui_serialData_panelNew
{
	/** The resultant panel. */
	sjme_scritchui_uiPanel* volatile outPanel;
} sjme_scritchui_serialData_panelNew;

typedef struct sjme_scritchui_serialData_screenSetListener
{
	/** The callback for screen listener. */
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(screen);
} sjme_scritchui_serialData_screenSetListener;

typedef struct sjme_scritchui_serialData_screens
{
	/** The resultant screen array. */
	sjme_scritchui_uiScreen* volatile outScreens;
	
	/** The size of the input array then the number of total screens. */
	sjme_jint* volatile inOutNumScreens;
} sjme_scritchui_serialData_screens;

typedef struct sjme_scritchui_serialData_windowContentMinimumSize
{
	/** The input window. */
	volatile sjme_scritchui_uiWindow inWindow;
	
	/** The width. */
	volatile sjme_jint width;
	
	/** The height. */
	volatile sjme_jint height;
} sjme_scritchui_serialData_windowContentMinimumSize;

typedef struct sjme_scritchui_serialData_windowNew
{
	/** The resultant window. */
	sjme_scritchui_uiWindow* volatile outWindow;
} sjme_scritchui_serialData_windowNew;

typedef struct sjme_scritchui_serialData_windowSetCloseListener
{
	/** The input window. */
	volatile sjme_scritchui_uiWindow inWindow;
	
	/** The callback for the close listener. */
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(close);
} sjme_scritchui_serialData_windowSetCloseListener;

typedef struct sjme_scritchui_serialData_windowSetVisible
{
	/** The input window. */
	volatile sjme_scritchui_uiWindow inWindow;
	
	/** Should it be visible? */
	volatile sjme_jboolean isVisible;
} sjme_scritchui_serialData_windowSetVisible;

/** Define serial data union quicker. */
#define SJME_SCRITCHUI_SDU_DEF(what) \
	volatile SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what) what

/**
 * Union for serial data.
 * 
 * @since 2024/04/17
 */
typedef union sjme_scritchui_serialDataUnion
{
	/** @c componentRepaint . */
	SJME_SCRITCHUI_SDU_DEF(componentRepaint);
	
	/** @c componentRevalidate . */
	SJME_SCRITCHUI_SDU_DEF(componentRevalidate);
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SDU_DEF(componentSetPaintListener);
	
	/** @c componentSetSizeListener . */
	SJME_SCRITCHUI_SDU_DEF(componentSetSizeListener);
	
	/** @c componentSize . */
	SJME_SCRITCHUI_SDU_DEF(componentSize);
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SDU_DEF(containerAdd);
	
	/** @c containerSetBounds . */
	SJME_SCRITCHUI_SDU_DEF(containerSetBounds);
	
	/** @c fontBuiltin . */
	SJME_SCRITCHUI_SDU_DEF(fontBuiltin);
		
	/** @c panelEnableFocus . */
	SJME_SCRITCHUI_SDU_DEF(panelEnableFocus);
		
	/** @c panelNew . */
	SJME_SCRITCHUI_SDU_DEF(panelNew);
	
	/** @c screenSetListener . */
	SJME_SCRITCHUI_SDU_DEF(screenSetListener);
		
	/** @c screens . */
	SJME_SCRITCHUI_SDU_DEF(screens);
	
	/** @c windowContentMinimumSize. */
	SJME_SCRITCHUI_SDU_DEF(windowContentMinimumSize);
	
	/** @c windowNew . */
	SJME_SCRITCHUI_SDU_DEF(windowNew);
	
	/** @c windowSetCloseListener . */
	SJME_SCRITCHUI_SDU_DEF(windowSetCloseListener);
	
	/** @c windowSetVisible . */
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

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint));
	
sjme_errorCode sjme_scritchui_coreSerial_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size));
	
sjme_errorCode sjme_scritchui_coreSerial_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight);

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);
	
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

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus);

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
