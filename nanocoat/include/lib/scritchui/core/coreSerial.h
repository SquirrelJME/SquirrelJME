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
	
	/** @c componentRevalidate . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
		
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
	
	/** The number of serialized types. */
	SJME_SCRITCHUI_NUM_SERIAL_TYPES
} sjme_scritchui_serialType;

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
	volatile sjme_scritchui_paintListenerFunc inListener;
	
	/** Any front-end data to set as needed. */
	sjme_frontEnd* volatile copyFrontEnd;
} sjme_scritchui_serialData_componentSetPaintListener;

typedef struct sjme_scritchui_serialData_containerAdd
{
	/** The input container. */
	volatile sjme_scritchui_uiComponent inContainer;
	
	/** The input component. */
	volatile sjme_scritchui_uiComponent inComponent;
} sjme_scritchui_serialData_containerAdd;

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
	sjme_scritchui_uiPanel volatile outPanel;
} sjme_scritchui_serialData_panelNew;

typedef struct sjme_scritchui_serialData_screenSetListener
{
	/** The callback for screen listener. */
	volatile sjme_scritchui_screenListenerFunc callback;
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

/**
 * Union for serial data.
 * 
 * @since 2024/04/17
 */
typedef union sjme_scritchui_serialDataUnion
{
	/** @c componentRevalidate . */
	volatile sjme_scritchui_serialData_componentRevalidate
		componentRevalidate;
	
	/** @c componentSetPaintListener . */
	volatile sjme_scritchui_serialData_componentSetPaintListener
		componentSetPaintListener;
	
	/** @c containerAdd . */
	volatile sjme_scritchui_serialData_containerAdd containerAdd;
		
	/** @c panelEnableFocus . */
	volatile sjme_scritchui_serialData_panelEnableFocus panelEnableFocus;
		
	/** @c panelNew . */
	volatile sjme_scritchui_serialData_panelNew panelNew;
	
	/** @c screenSetListener . */
	volatile sjme_scritchui_serialData_screenSetListener screenSetListener;
		
	/** @c screens . */
	volatile sjme_scritchui_serialData_screens screens;
	
	/** @c windowContentMinimumSize. */
	volatile sjme_scritchui_serialData_windowContentMinimumSize
		windowContentMinimumSize;
	
	/** @c windowNew . */
	volatile sjme_scritchui_serialData_windowNew windowNew;
} sjme_scritchui_serialDataUnion;

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

sjme_errorCode sjme_scritchui_coreSerial_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd);

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus);

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel);

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_screenListenerFunc callback);

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
