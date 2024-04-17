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
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
		
	/** @c panelEnableFocus . */
	SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		
	/** @c panelNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
	
	/** @c screenSetListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		
	/** @c screens . */
	SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
	
	/** @c windowNew . */
	SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
	
	/** The number of serialized types. */
	SJME_SCRITCHUI_NUM_SERIAL_TYPES
} sjme_scritchui_serialType;

typedef struct sjme_scritchui_serialDataComponentSetPaintListener
{
} sjme_scritchui_serialDataComponentSetPaintListener;

typedef struct sjme_scritchui_serialDataPanelEnableFocus
{
} sjme_scritchui_serialDataPanelEnableFocus;

typedef struct sjme_scritchui_serialDataPanelNew
{
} sjme_scritchui_serialDataPanelNew;

typedef struct sjme_scritchui_serialDataScreenSetListener
{
} sjme_scritchui_serialDataScreenSetListener;

typedef struct sjme_scritchui_serialDataScreens
{
} sjme_scritchui_serialDataScreens;

typedef struct sjme_scritchui_serialDataWindowNew
{
} sjme_scritchui_serialDataWindowNew;

/**
 * Union for serial data.
 * 
 * @since 2024/04/17
 */
typedef union sjme_scritchui_serialDataUnion
{
	/** @c componentSetPaintListener . */
	sjme_scritchui_serialDataComponentSetPaintListener
		componentSetPaintListener;
		
	/** @c panelEnableFocus . */
	sjme_scritchui_serialDataPanelEnableFocus panelEnableFocus;
		
	/** @c panelNew . */
	sjme_scritchui_serialDataPanelNew panelNew;
	
	/** @c screenSetListener . */
	sjme_scritchui_serialDataScreenSetListener screenSetListener;
		
	/** @c screens . */
	sjme_scritchui_serialDataScreens screens;
	
	/** @c windowNew . */
	sjme_scritchui_serialDataWindowNew windowNew;
} sjme_scritchui_serialDataUnion;

/**
 * Data for the serial call.
 * 
 * @since 2024/04/17
 */
typedef struct sjme_scritchui_serialData
{
	/** The type of call this is. */
	sjme_scritchui_serialType type;
	
	/** The serial data. */
	sjme_scritchui_serialDataUnion data;
} sjme_scritchui_serialData;

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd);

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
