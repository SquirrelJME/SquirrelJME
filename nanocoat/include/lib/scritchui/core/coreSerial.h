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
	
	/** @c componentSetInputListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_INPUT_LISTENER,
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
	
	/** @c componentSetSizeListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER,
	
	/** @c componentSetVisibleListener . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VISIBLE_LISTENER,
	
	/** @c componentSize . */
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SIZE,
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
	
	/** @c containerSetBounds . */
	SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
	
	/** @c fontBuiltin . */
	SJME_SCRITCHUI_SERIAL_TYPE_FONT_BUILTIN,
	
	/** @c fontDerive . */
	SJME_SCRITCHUI_SERIAL_TYPE_FONT_DERIVE,
	
	/** @c hardwareGraphics . */
	SJME_SCRITCHUI_SERIAL_TYPE_HARDWARE_GRAPHICS,
		
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

SUD_STRUCT_DEF(componentSetPaintListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(paint););

SUD_STRUCT_DEF(componentSetSizeListener,
	SDU_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(size););

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
	/** @c componentRepaint . */
	SJME_SCRITCHUI_SDU_DEF(componentRepaint);
	
	/** @c componentRevalidate . */
	SJME_SCRITCHUI_SDU_DEF(componentRevalidate);
	
	/** @c componentSetInputListener . */
	SJME_SCRITCHUI_SDU_DEF(componentSetInputListener);
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SDU_DEF(componentSetPaintListener);
	
	/** @c componentSetSizeListener . */
	SJME_SCRITCHUI_SDU_DEF(componentSetSizeListener);
	
	/** @c componentSetVisibleListener . */
	SJME_SCRITCHUI_SDU_DEF(componentSetVisibleListener);
	
	/** @c componentSize . */
	SJME_SCRITCHUI_SDU_DEF(componentSize);
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SDU_DEF(containerAdd);
	
	/** @c containerSetBounds . */
	SJME_SCRITCHUI_SDU_DEF(containerSetBounds);
	
	/** @c fontBuiltin . */
	SJME_SCRITCHUI_SDU_DEF(fontBuiltin);
	
	/** @c fontDerive . */
	SJME_SCRITCHUI_SDU_DEF(fontDerive);
	
	/** @c hardwareGraphics . */
	SJME_SCRITCHUI_SDU_DEF(hardwareGraphics);
		
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
