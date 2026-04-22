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

#ifndef SJME_C_CORESERIAL_H
#define SJME_C_CORESERIAL_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/scritchuiPencil.h"

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
	SJME_SCRITCHUI_SERIAL_UI_UNKNOWN,
	
	/** @c choiceGetSelectedIndex . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_GET_SELECTED_INDEX,
	
	/** @c choiceItemGet . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_GET,
	
	/** @c choiceItemInsert . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_INSERT,
	
	/** @c choiceItemRemove . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_REMOVE,
	
	/** @c choiceItemRemoveAll . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_REMOVE_ALL,
	
	/** @c choiceItemSetEnabled . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_ENABLED,
	
	/** @c choiceItemSetImage . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_IMAGE,
	
	/** @c choiceItemSetSelected . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_SELECTED,
	
	/** @c choiceItemSetString . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_STRING,
	
	/** @c choiceLength . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_LENGTH,
	
	/** @c componentFocusGrab . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_FOCUS_GRAB,
	
	/** @c componentFocusHas . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_FOCUS_HAS,
	
	/** @c componentGetParent . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_GET_PARENT,
	
	/** @c componentPosition . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_POSITION,
	
	/** @c componentRepaint . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_REPAINT,
	
	/** @c componentRevalidate . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_REVALIDATE,
	
	/** @c componentSetActivateListener . */
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_SET_ACTIVATE_LISTENER,
	
	/** @c componentSetInputListener . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_INPUT_LISTENER,
	
	/** @c componentSetPaintListener . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_PAINT_LISTENER,
	
	/** @c componentSetSizeListener . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_SIZE_LISTENER,
	
	/** @c componentSetValueUpdateListener . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_VALUE_UPDATE_LISTENER,
	
	/** @c componentSetVisibleListener . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_VISIBLE_LISTENER,
	
	/** @c componentSize . */
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SIZE,
	
	/** @c containerAdd . */
	SJME_SCRITCHUI_SERIAL_UI_CONTAINER_ADD,
	
	/** @c containerGetFrame . */
	SJME_SCRITCHUI_SERIAL_UI_CONTAINER_GET_FRAME,
	
	/** @c containerRemove . */
	SJME_SCRITCHUI_SERIAL_UI_CONTAINER_REMOVE,
	
	/** @c containerRemoveAll . */
	SJME_SCRITCHUI_SERIAL_UI_CONTAINER_REMOVE_ALL,
	
	/** @c containerSetBounds . */
	SJME_SCRITCHUI_SERIAL_UI_CONTAINER_SET_BOUNDS,
	
	/** @c fontBuiltin . */
	SJME_SCRITCHUI_SERIAL_UI_FONT_BUILTIN,
	
	/** @c fontByFace . */
	SJME_SCRITCHUI_SERIAL_UI_FONT_BY_FACE,

	/** @c fontCount . */
	SJME_SCRITCHUI_SERIAL_UI_FONT_COUNT,
	
	/** @c fontDerive . */
	SJME_SCRITCHUI_SERIAL_UI_FONT_DERIVE,
	
	/** @c fontList . */
	SJME_SCRITCHUI_SERIAL_UI_FONT_LIST,
	
	/** @c hardwareGraphics . */
	SJME_SCRITCHUI_SERIAL_UI_HARDWARE_GRAPHICS,
	
	/** @c labelSetString . */
	SJME_SCRITCHUI_SERIAL_UI_LABEL_SET_STRING,
	
	/** @c lafDpiProject. */
	SJME_SCRITCHUI_SERIAL_UI_LAF_DPI_PROJECT,
	
	/** @c lafElementColor. */
	SJME_SCRITCHUI_SERIAL_UI_LAF_ELEMENT_COLOR,

	/** @c lafMetric. */
	SJME_SCRITCHUI_SERIAL_UI_LAF_METRIC,
		
	/** @c listNew . */
	SJME_SCRITCHUI_SERIAL_UI_LIST_NEW,
		
	/** @c menuBarNew . */
	SJME_SCRITCHUI_SERIAL_UI_MENU_BAR_NEW,
	
	/** @c menuInsert . */
	SJME_SCRITCHUI_SERIAL_UI_MENU_INSERT,
		
	/** @c menuItemNew . */
	SJME_SCRITCHUI_SERIAL_UI_MENU_ITEM_NEW,
		
	/** @c menuNew . */
	SJME_SCRITCHUI_SERIAL_UI_MENU_NEW,
	
	/** @c menuRemove . */
	SJME_SCRITCHUI_SERIAL_UI_MENU_REMOVE,
	
	/** @c menuRemoveAll . */
	SJME_SCRITCHUI_SERIAL_UI_MENU_REMOVE_ALL,
	
	/** @c objectDelete . */
	SJME_SCRITCHUI_SERIAL_UI_OBJECT_DELETE,
		
	/** @c panelEnableFocus . */
	SJME_SCRITCHUI_SERIAL_UI_PANEL_ENABLE_FOCUS,
		
	/** @c panelNew . */
	SJME_SCRITCHUI_SERIAL_UI_PANEL_NEW,
	
	/** @c pseudoGraphics . */
	SJME_SCRITCHUI_SERIAL_UI_PSEUDO_GRAPHICS,
	
	/** @c screenGetBounds . */
	SJME_SCRITCHUI_SERIAL_UI_SCREEN_GET_BOUNDS,
	
	/** @c screenSetListener . */
	SJME_SCRITCHUI_SERIAL_UI_SCREEN_SET_LISTENER,
		
	/** @c screens . */
	SJME_SCRITCHUI_SERIAL_UI_SCREENS,
	
	/** @c scrollPanelNew . */
	SJME_SCRITCHUI_SERIAL_UI_SCROLL_PANEL_NEW,
	
	/** @c viewGetView . */
	SJME_SCRITCHUI_SERIAL_UI_VIEW_GET_VIEW,
	
	/** @c viewSetArea . */
	SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_AREA,
	
	/** @c viewSetView . */
	SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_VIEW,
	
	/** @c viewSetSizeSuggestListener . */
	SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_SIZE_SUGGEST_LISTENER,
	
	/** @c viewSetViewListener . */
	SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_VIEW_LISTENER,
	
	/** @c windowContentMinimumSize . */
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_CONTENT_MINIMUM_SIZE,
	
	/** @c windowNew . */
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_NEW,
	
	/** @c windowSetCloseListener . */
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_CLOSE_LISTENER,
	
	/** @c windowSetMenuBar . */
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_MENU_BAR,
	
	/** @c windowSetMenuItemActivateListener . */
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_MENU_ITEM_ACTIVATE_LISTENER,
	
	/** @c windowSetVisible . */
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_VISIBLE,
	
	/** @c lock . */
	SJME_SCRITCHUI_SERIAL_PEN_LOCK,

	/** @c lockRelease . */
	SJME_SCRITCHUI_SERIAL_PEN_LOCK_RELEASE,

	/** @c close . */
	SJME_SCRITCHUI_SERIAL_PEN_CLOSE,

	/** @c copyArea . */
	SJME_SCRITCHUI_SERIAL_PEN_COPY_AREA,

	/** @c drawArc . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_ARC,

	/** @c drawChar . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_CHAR,

	/** @c drawChars . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_CHARS,

	/** @c drawHoriz . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_HORIZ,

	/** @c drawLine . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_LINE,

	/** @c drawPixel . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_PIXEL,

	/** @c drawPolyline . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_POLYLINE,

	/** @c drawRect . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_RECT,

	/** @c drawRegion . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_REGION,

	/** @c drawRoundRect . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_ROUND_RECT,

	/** @c drawSubstring . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_SUBSTRING,

	/** @c drawTriangle . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_TRIANGLE,
	
	/** @c drawXRGB32Region . */
	SJME_SCRITCHUI_SERIAL_PEN_DRAW_XRGB32REGION,

	/** @c fillArc . */
	SJME_SCRITCHUI_SERIAL_PEN_FILL_ARC,

	/** @c fillPolygon . */
	SJME_SCRITCHUI_SERIAL_PEN_FILL_POLYGON,

	/** @c fillRect . */
	SJME_SCRITCHUI_SERIAL_PEN_FILL_RECT,

	/** @c fillRoundRect . */
	SJME_SCRITCHUI_SERIAL_PEN_FILL_ROUND_RECT,

	/** @c fillTriangle . */
	SJME_SCRITCHUI_SERIAL_PEN_FILL_TRIANGLE,

	/** @c getRegion . */
	SJME_SCRITCHUI_SERIAL_PEN_GET_REGION,

	/** @c mapColor . */
	SJME_SCRITCHUI_SERIAL_PEN_MAP_COLOR,

	/** @c setAlphaColor . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_ALPHA_COLOR,

	/** @c setBlendingMode . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_BLENDING_MODE,

	/** @c setClip . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_CLIP,

	/** @c setDefaultFont . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_DEFAULT_FONT,

	/** @c setDefaults . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_DEFAULTS,

	/** @c setStrokeStyle . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_STROKE_STYLE,

	/** @c setFont . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_FONT,

	/** @c setParametersFrom . */
	SJME_SCRITCHUI_SERIAL_PEN_SET_PARAMETERS_FROM,
	
	/** @c transferRegion . */
	SJME_SCRITCHUI_SERIAL_PEN_TRANSFER_REGION,

	/** @c translate . */
	SJME_SCRITCHUI_SERIAL_PEN_TRANSLATE,

	/** @link sjme_scritchui_pencilFontEqualsFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_EQUALS,

	/** @link sjme_scritchui_pencilFontMetricCharDirectionFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_CHAR_DIRECTION,

	/** @link sjme_scritchui_pencilFontMetricCharValidFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_CHAR_VALID,

	/** @link sjme_scritchui_pencilFontMetricFontFaceFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_FACE,

	/** @link sjme_scritchui_pencilFontMetricFontNameFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_NAME,

	/** @link sjme_scritchui_pencilFontMetricFontStyleFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_STYLE,

	/** @link sjme_scritchui_pencilFontMetricPixelAscentFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_ASCENT,

	/** @link sjme_scritchui_pencilFontMetricPixelBaselineFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_BASELINE,

	/** @link sjme_scritchui_pencilFontMetricPixelDescentFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_DESCENT,

	/** @link sjme_scritchui_pencilFontMetricPixelHeightFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_HEIGHT,

	/** @link sjme_scritchui_pencilFontMetricPixelLeadingFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_LEADING,

	/** @link sjme_scritchui_pencilFontMetricPixelSizeFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_SIZE,

	/** @link sjme_scritchui_pencilFontPixelCharWidthFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_PIXEL_CHAR_WIDTH,

	/** @link sjme_scritchui_pencilFontRenderBitmapFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_RENDER_BITMAP,

	/** @link sjme_scritchui_pencilFontRenderCharFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_RENDER_CHAR,

	/** @link sjme_scritchui_pencilFontStringWidthFunc @endlink. */
	SJME_SCRITCHUI_SERIAL_FONT_STRING_WIDTH,
	
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
	sjme_frontEndBindable* volatile copyFrontEnd

/** The name for serial data. */
#define SDX_STRUCT_NAME(where, what) \
	SJME_TOKEN_PASTE4(sjme_scritch, where, _serialData_, what)

#define SDX_STRUCT_DEF(where, what, items) \
	typedef struct SDX_STRUCT_NAME(where, what) \
	{ \
		items \
	} SDX_STRUCT_NAME(where, what)

/** Pen struct definition. */
#define SDP_STRUCT_DEF(what, items) SDX_STRUCT_DEF(pen, what, items)

/** Font struct definition. */
#define SDF_STRUCT_DEF(what, items) SDX_STRUCT_DEF(font, what, items)

/** UI struct definition. */
#define SDU_STRUCT_DEF(what, items) SDX_STRUCT_DEF(ui, what, items)

#define SDX_VAR(type, name) \
	volatile type name

#define SDX_VARP(type, name) \
	type* volatile name

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

SDU_STRUCT_DEF(choiceGetSelectedIndex,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_jint, outIndex););
	
SDU_STRUCT_DEF(choiceItemGet,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, atIndex);
	SDX_VAR(sjme_scritchui_uiChoiceItem, outItemTemplate););

SDU_STRUCT_DEF(choiceItemInsert,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_jint, inOutIndex););
	
SDU_STRUCT_DEF(choiceItemRemove,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, atIndex););
	
SDU_STRUCT_DEF(choiceItemRemoveAll,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent););
	
SDU_STRUCT_DEF(choiceItemSetEnabled,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, atIndex);
	SDX_VAR(sjme_jboolean, isEnabled););
	
SDU_STRUCT_DEF(choiceItemSetImage,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, atIndex);
	SDX_VARP(sjme_jint, inRgb);
	SDX_VAR(sjme_jint, inRgbOff);
	SDX_VAR(sjme_jint, inRgbDataLen);
	SDX_VAR(sjme_jint, inRgbScanLen);
	SDX_VAR(sjme_jint, width);
	SDX_VAR(sjme_jint, height););
	
SDU_STRUCT_DEF(choiceItemSetSelected,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, atIndex);
	SDX_VAR(sjme_jboolean, isSelected););
	
SDU_STRUCT_DEF(choiceItemSetString,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, atIndex);
	SDX_VAR(sjme_lpcstr, inString););
	
SDU_STRUCT_DEF(choiceLength,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_jint, outLength););

SDU_STRUCT_DEF(componentFocusGrab,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent););

SDU_STRUCT_DEF(componentFocusHas,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_jboolean, outHasFocus););

SDU_STRUCT_DEF(componentGetParent,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_scritchui_uiComponent, outParent););

SDU_STRUCT_DEF(componentPosition,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_jint, outX);
	SDX_VARP(sjme_jint, outY););

SDU_STRUCT_DEF(componentRepaint,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, width);
	SDX_VAR(sjme_jint, height););

SDU_STRUCT_DEF(componentRevalidate,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent););

SDU_STRUCT_DEF(componentSetInputListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(input););

SDU_STRUCT_DEF(componentSetActivateListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(activate););
	
SDU_STRUCT_DEF(componentSetPaintListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(paint););

SDU_STRUCT_DEF(componentSetSizeListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(size););

SDU_STRUCT_DEF(componentSetValueUpdateListener,
	SDX_VAR(sjme_scritchui, inState);
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(valueUpdate););
	
SDU_STRUCT_DEF(componentSetVisibleListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(visible););

SDU_STRUCT_DEF(componentSize,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_jint, outWidth);
	SDX_VARP(sjme_jint, outHeight););

SDU_STRUCT_DEF(containerAdd,
	SDX_VAR(sjme_scritchui_uiComponent, inContainer);
	SDX_VAR(sjme_scritchui_uiComponent, addComponent););

SDU_STRUCT_DEF(containerGetFrame,
	SDX_VAR(sjme_scritchui_uiComponent, inContainer);
	SDX_VARP(sjme_scritchui_dim, contentSize);
	SDX_VARP(sjme_scritchui_rect, frameBound);
	SDX_VARP(sjme_scritchui_rect, contentBound););

SDU_STRUCT_DEF(containerRemove,
	SDX_VAR(sjme_scritchui_uiComponent, inContainer);
	SDX_VAR(sjme_scritchui_uiComponent, removeComponent););

SDU_STRUCT_DEF(containerRemoveAll,
	SDX_VAR(sjme_scritchui_uiComponent, inContainer););

SDU_STRUCT_DEF(containerSetBounds,
	SDX_VAR(sjme_scritchui_uiComponent, inContainer);
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, width);
	SDX_VAR(sjme_jint, height););

SDU_STRUCT_DEF(fontBuiltin,
	SDX_VARP(sjme_scritchui_pencilFont, outFont););

SDU_STRUCT_DEF(fontByFace,
	SDX_VAR(sjme_scritchui, inState);
	SDX_VARP(sjme_scritchui_pencilFont, outFont);
	SDX_VARP(sjme_scritchui_pencilFontParam, outParams);
	SDX_VAR(sjme_scritchui_pencilFontFace, inFace);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams););

SDU_STRUCT_DEF(fontCount,
	SDX_VARP(sjme_jint, outCount););

SDU_STRUCT_DEF(fontDerive,
	SDX_VARP(sjme_scritchui_pencilFont, newFont);
	SDX_VARP(sjme_scritchui_pencilFontParam, newParams);
	SDX_VAR(sjme_scritchui_pencilFont, oldFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, deriveParams););

SDU_STRUCT_DEF(fontList,
	SDX_VARP(sjme_list(sjme_scritchui_pencilFont), outFonts);
	SDX_VARP(sjme_jint, outValid);
	SDX_VARP(sjme_jint, outCount););

SDU_STRUCT_DEF(hardwareGraphics,
	SDX_VARP(sjme_scritchui_pencil, outPencil);
	SDX_VARP(sjme_alloc_weak, outWeakPencil);
	SDX_VAR(sjme_gfx_pixelFormat, pf);
	SDX_VAR(sjme_jint, bw);
	SDX_VAR(sjme_jint, bh);
	SDX_VARP(const sjme_scritchui_pencilLockFunctions, inLockFuncs);
	SDX_VARP(const sjme_frontEndBindable, inLockFrontEndCopy);
	SDX_VAR(sjme_jint, sx);
	SDX_VAR(sjme_jint, sy);
	SDX_VAR(sjme_jint, sw);
	SDX_VAR(sjme_jint, sh);
	SDX_VARP(const sjme_frontEndBindable, pencilFrontEndCopy););

SDU_STRUCT_DEF(labelSetString,
	SDX_VAR(sjme_scritchui_uiCommon, inCommon);
	SDX_VAR(sjme_lpcstr, inString););

SDU_STRUCT_DEF(lafDpiProject,
	SDX_VAR(sjme_scritchui_uiComponent, inContext);
	SDX_VAR(sjme_jboolean, toBase);
	SDX_VARP(sjme_jint, inOutX);
	SDX_VARP(sjme_jint, inOutY);
	SDX_VARP(sjme_jint, inOutW);
	SDX_VARP(sjme_jint, inOutH););

SDU_STRUCT_DEF(lafElementColor,
	SDX_VAR(sjme_scritchui_uiComponent, inContext);
	SDX_VARP(sjme_jint, outRGB);
	SDX_VAR(sjme_scritchui_lafElementColorType, elementColor););

SDU_STRUCT_DEF(lafMetric,
	SDX_VAR(sjme_scritchui_uiComponent, inContext);
	SDX_VARP(sjme_jint, outValue);
	SDX_VAR(sjme_scritchui_lafMetricType, metricType););

SDU_STRUCT_DEF(listNew,
	SDX_VARP(sjme_scritchui_uiList, outList);
	SDX_VAR(sjme_scritchui_choiceType, inChoiceType););

SDU_STRUCT_DEF(menuBarNew,
	SDX_VARP(sjme_scritchui_uiMenuBar, outMenuBar););

SDU_STRUCT_DEF(menuInsert,
	SDX_VAR(sjme_scritchui_uiMenuKind, intoMenu);
	SDX_VAR(sjme_jint, atIndex);
	SDX_VAR(sjme_scritchui_uiMenuKind, childItem););
	
SDU_STRUCT_DEF(menuItemNew,
	SDX_VARP(sjme_scritchui_uiMenuItem, outMenuItem););

SDU_STRUCT_DEF(menuNew,
	SDX_VARP(sjme_scritchui_uiMenu, outMenu););

SDU_STRUCT_DEF(menuRemove,
	SDX_VAR(sjme_scritchui_uiMenuKind, fromMenu);
	SDX_VAR(sjme_jint, atIndex););

SDU_STRUCT_DEF(menuRemoveAll,
	SDX_VAR(sjme_scritchui_uiMenuKind, fromMenu););

SDU_STRUCT_DEF(objectDelete,
	SDX_VARP(sjme_scritchui_uiCommon, inOutObject););
	
SDU_STRUCT_DEF(panelEnableFocus,
	SDX_VAR(sjme_scritchui_uiPanel, inPanel);
	SDX_VAR(sjme_jboolean, enableFocus);
	SDX_VAR(sjme_jboolean, defaultFocus););

SDU_STRUCT_DEF(panelNew,
	SDX_VARP(sjme_scritchui_uiPanel, outPanel););

SDU_STRUCT_DEF(pseudoGraphics,
	SDX_VARP(sjme_scritchui_pencil, outPencil);
	SDX_VARP(sjme_alloc_weak, outWeakPencil);
	SDX_VARP(sjme_scritchui_pencil, pencils);
	SDX_VAR(sjme_jint, numPencils);
	SDX_VARP(const sjme_frontEndBindable, pencilFrontEndCopy););

SDU_STRUCT_DEF(screenGetBounds,
	SDX_VAR(sjme_scritchui_uiScreen, inScreen);
	SDX_VAR(sjme_scritchui_uiComponent, forComponent);
	SDX_VARP(sjme_scritchui_rect, pixelBound);
	SDX_VARP(sjme_scritchui_rect, mmBound););
	
SDU_STRUCT_DEF(screenSetListener,
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(screen););

SDU_STRUCT_DEF(screens,
	SDX_VARP(sjme_scritchui_uiScreen, outScreens);
	SDX_VARP(sjme_jint, inOutNumScreens););

SDU_STRUCT_DEF(scrollPanelNew,
	SDX_VARP(sjme_scritchui_uiScrollPanel, outScrollPanel););

SDU_STRUCT_DEF(viewGetView,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(sjme_scritchui_rect, outViewRect););

SDU_STRUCT_DEF(viewSetArea,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(const sjme_scritchui_dim, inViewArea););

SDU_STRUCT_DEF(viewSetView,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SDX_VARP(const sjme_scritchui_point, inViewPos););

SDU_STRUCT_DEF(viewSetSizeSuggestListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(sizeSuggest););

SDU_STRUCT_DEF(viewSetViewListener,
	SDX_VAR(sjme_scritchui_uiComponent, inComponent);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(view););

SDU_STRUCT_DEF(windowContentMinimumSize,
	SDX_VAR(sjme_scritchui_uiWindow, inWindow);
	SDX_VAR(sjme_jint, width);
	SDX_VAR(sjme_jint, height););

SDU_STRUCT_DEF(windowNew,
	SDX_VARP(sjme_scritchui_uiWindow, outWindow););

SDU_STRUCT_DEF(windowSetCloseListener,
	SDX_VAR(sjme_scritchui_uiWindow, inWindow);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(close););

SDU_STRUCT_DEF(windowSetMenuBar,
	SDX_VAR(sjme_scritchui_uiWindow, inWindow);
	SDX_VAR(sjme_scritchui_uiMenuBar, inMenuBar););

SDU_STRUCT_DEF(windowSetMenuItemActivateListener,
	SDX_VAR(sjme_scritchui_uiWindow, inWindow);
	SJME_SCRITCHUI_SERIAL_SET_LISTENER(menuItemActivate););
	
SDU_STRUCT_DEF(windowSetVisible,
	SDX_VAR(sjme_scritchui_uiWindow, inWindow);
	SDX_VAR(sjme_jboolean, isVisible););

/* ------------------------------------------------------------------------ */
	
SDP_STRUCT_DEF(graphicsCall,
	SDX_VAR(sjme_scritchui_pencil, g););

SDP_STRUCT_DEF(close,
	SDX_VAR(sjme_scritchui_pencil, g););

SDP_STRUCT_DEF(copyArea,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, sx);
	SDX_VAR(sjme_jint, sy);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h);
	SDX_VAR(sjme_jint, dx);
	SDX_VAR(sjme_jint, dy);
	SDX_VAR(sjme_jint, anchor););

SDP_STRUCT_DEF(drawArc,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h);
	SDX_VAR(sjme_jint, startAngle);
	SDX_VAR(sjme_jint, arcAngle););

SDP_STRUCT_DEF(drawChar,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, c);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, anchor);
	SDX_VARP(sjme_jint, outCw););

SDP_STRUCT_DEF(drawChars,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VARP(const sjme_jchar, s);
	SDX_VAR(sjme_jint, o);
	SDX_VAR(sjme_jint, l);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, anchor););

SDP_STRUCT_DEF(drawHoriz,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w););

SDP_STRUCT_DEF(drawLine,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x1);
	SDX_VAR(sjme_jint, y1);
	SDX_VAR(sjme_jint, x2);
	SDX_VAR(sjme_jint, y2););

SDP_STRUCT_DEF(drawPixel,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y););

SDP_STRUCT_DEF(drawPolyline,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VARP(const sjme_jint, inXPoints);
	SDX_VAR(sjme_jint, xOffset);
	SDX_VARP(const sjme_jint, inYPoints);
	SDX_VAR(sjme_jint, yOffset);
	SDX_VAR(sjme_jint, nPoints););

SDP_STRUCT_DEF(drawRect,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h););

SDP_STRUCT_DEF(drawRegion,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, pf);
	SDX_VAR(sjme_cpointer, data);
	SDX_VAR(sjme_jint, off);
	SDX_VAR(sjme_jint, dataLen);
	SDX_VAR(sjme_jint, scanLen);
	SDX_VAR(sjme_jboolean, alpha);
	SDX_VAR(sjme_jint, xSrc);
	SDX_VAR(sjme_jint, ySrc);
	SDX_VAR(sjme_jint, wSrc);
	SDX_VAR(sjme_jint, hSrc);
	SDX_VAR(sjme_jint, trans);
	SDX_VAR(sjme_jint, xDest);
	SDX_VAR(sjme_jint, yDest);
	SDX_VAR(sjme_jint, anchor);
	SDX_VAR(sjme_jint, wDest);
	SDX_VAR(sjme_jint, hDest);
	SDX_VAR(sjme_jint, origImgWidth);
	SDX_VAR(sjme_jint, origImgHeight););

SDP_STRUCT_DEF(drawRoundRect,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h);
	SDX_VAR(sjme_jint, arcWidth);
	SDX_VAR(sjme_jint, arcHeight););

SDP_STRUCT_DEF(drawSubstring,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_charSeq, s);
	SDX_VAR(sjme_jint, o);
	SDX_VAR(sjme_jint, l);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, anchor););

SDP_STRUCT_DEF(drawTriangle,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x1);
	SDX_VAR(sjme_jint, y1);
	SDX_VAR(sjme_jint, x2);
	SDX_VAR(sjme_jint, y2);
	SDX_VAR(sjme_jint, x3);
	SDX_VAR(sjme_jint, y3););

SDP_STRUCT_DEF(drawXRGB32Region,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VARP(sjme_jint, data);
	SDX_VAR(sjme_jint, off);
	SDX_VAR(sjme_jint, dataLen);
	SDX_VAR(sjme_jint, scanLen);
	SDX_VAR(sjme_jboolean, alpha);
	SDX_VAR(sjme_jint, xSrc);
	SDX_VAR(sjme_jint, ySrc);
	SDX_VAR(sjme_jint, wSrc);
	SDX_VAR(sjme_jint, hSrc);
	SDX_VAR(sjme_jint, trans);
	SDX_VAR(sjme_jint, xDest);
	SDX_VAR(sjme_jint, yDest);
	SDX_VAR(sjme_jint, anchor);
	SDX_VAR(sjme_jint, wDest);
	SDX_VAR(sjme_jint, hDest);
	SDX_VAR(sjme_jint, origImgWidth);
	SDX_VAR(sjme_jint, origImgHeight););

SDP_STRUCT_DEF(fillArc,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h);
	SDX_VAR(sjme_jint, startAngle);
	SDX_VAR(sjme_jint, arcAngle););

SDP_STRUCT_DEF(fillPolygon,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VARP(const sjme_jint, inXPoints);
	SDX_VAR(sjme_jint, xOffset);
	SDX_VARP(const sjme_jint, inYPoints);
	SDX_VAR(sjme_jint, yOffset);
	SDX_VAR(sjme_jint, nPoints););

SDP_STRUCT_DEF(fillRect,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h););

SDP_STRUCT_DEF(fillRoundRect,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h);
	SDX_VAR(sjme_jint, arcWidth);
	SDX_VAR(sjme_jint, arcHeight););

SDP_STRUCT_DEF(fillTriangle,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x1);
	SDX_VAR(sjme_jint, y1);
	SDX_VAR(sjme_jint, x2);
	SDX_VAR(sjme_jint, y2);
	SDX_VAR(sjme_jint, x3);
	SDX_VAR(sjme_jint, y3););

SDP_STRUCT_DEF(getRegion,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, pf);
	SDX_VAR(sjme_cpointer, data);
	SDX_VAR(sjme_jint, off);
	SDX_VAR(sjme_jint, dataLen);
	SDX_VAR(sjme_jint, scanLen);
	SDX_VAR(sjme_jboolean, alpha);
	SDX_VAR(sjme_jint, xSrc);
	SDX_VAR(sjme_jint, ySrc);
	SDX_VAR(sjme_jint, wSrc);
	SDX_VAR(sjme_jint, hSrc);
	SDX_VAR(sjme_jint, anchor););

SDP_STRUCT_DEF(mapColor,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jboolean, fromRaw);
	SDX_VAR(sjme_jint, inRgbOrRaw);
	SDX_VARP(sjme_scritchui_color, outColor););

SDP_STRUCT_DEF(setAlphaColor,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, argb););

SDP_STRUCT_DEF(setBlendingMode,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_scritchui_pencilBlendingMode, mode););

SDP_STRUCT_DEF(setClip,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y);
	SDX_VAR(sjme_jint, w);
	SDX_VAR(sjme_jint, h););

SDP_STRUCT_DEF(setDefaultFont,
	SDX_VAR(sjme_scritchui_pencil, g););

SDP_STRUCT_DEF(setDefaults,
	SDX_VAR(sjme_scritchui_pencil, g););

SDP_STRUCT_DEF(setStrokeStyle,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_scritchui_pencilStrokeMode, style););

SDP_STRUCT_DEF(setFont,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_scritchui_pencilFont, font);
	SDX_VARP(const sjme_scritchui_pencilFontParam, params););

SDP_STRUCT_DEF(setParametersFrom,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_scritchui_pencil, from););
	
SDP_STRUCT_DEF(transferRegion,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_scritchui_pencil, srcPencil);
	SDX_VAR(sjme_jboolean, alpha);
	SDX_VAR(sjme_jint, xSrc);
	SDX_VAR(sjme_jint, ySrc);
	SDX_VAR(sjme_jint, wSrc);
	SDX_VAR(sjme_jint, hSrc);
	SDX_VAR(sjme_jint, trans);
	SDX_VAR(sjme_jint, xDest);
	SDX_VAR(sjme_jint, yDest);
	SDX_VAR(sjme_jint, anchor);
	SDX_VAR(sjme_jint, wDest);
	SDX_VAR(sjme_jint, hDest);
	SDX_VAR(sjme_scritchui_transferRegionMode, mode););
	
SDP_STRUCT_DEF(translate,
	SDX_VAR(sjme_scritchui_pencil, g);
	SDX_VAR(sjme_jint, x);
	SDX_VAR(sjme_jint, y););

#pragma region(font)

SDF_STRUCT_DEF(equals,
	SDX_VAR(sjme_scritchui_pencilFont , a);
	SDX_VARP(const sjme_scritchui_pencilFontParam, aParams);
	SDX_VAR(sjme_scritchui_pencilFont , b);
	SDX_VARP(const sjme_scritchui_pencilFontParam, bParams););
	
SDF_STRUCT_DEF(metricCharDirection,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VAR(sjme_jint, inCodepoint);
	SDX_VARP(sjme_jint, outDirection););

SDF_STRUCT_DEF(metricCharValid,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VAR(sjme_jint, inCodepoint);
	SDX_VARP(sjme_jboolean, outValid););

SDF_STRUCT_DEF(metricFontFace,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(sjme_scritchui_pencilFontFace, outFace););

SDF_STRUCT_DEF(metricFontName,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(sjme_lpcstr, outName););

SDF_STRUCT_DEF(metricFontStyle,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(sjme_scritchui_pencilFontStyle, outStyle););

SDF_STRUCT_DEF(metricPixelAscent,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_jboolean, isMax);
	SDX_VARP(sjme_jint, outAscent););

SDF_STRUCT_DEF(metricPixelBaseline,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VARP(sjme_jint, outBaseline););

SDF_STRUCT_DEF(metricPixelDescent,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_jboolean, isMax);
	SDX_VARP(sjme_jint, outDescent););

SDF_STRUCT_DEF(metricPixelHeight,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VARP(sjme_jint, outHeight););

SDF_STRUCT_DEF(metricPixelLeading,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VARP(sjme_jint, outLeading););

SDF_STRUCT_DEF(metricPixelSize,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_jint, inCodepoint);
	SDX_VARP(sjme_jint, outSize););

SDF_STRUCT_DEF(pixelCharWidth,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_jint, inCodepoint);
	SDX_VARP(sjme_jint, outWidth););

SDF_STRUCT_DEF(renderBitmap,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_jint, inCodepoint);
	SDX_VARP(sjme_jubyte, buf);
	SDX_VAR(sjme_jint, bufOff);
	SDX_VAR(sjme_jint, bufScanLen);
	SDX_VAR(sjme_jint, bufHeight);
	SDX_VARP(sjme_jint, outOffX);
	SDX_VARP(sjme_jint, outOffY););

SDF_STRUCT_DEF(renderChar,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_jint, inCodepoint);
	SDX_VAR(sjme_scritchui_pencil, inPencil);
	SDX_VAR(sjme_jint, xPos);
	SDX_VAR(sjme_jint, yPos);
	SDX_VARP(sjme_jint, nextXPos);
	SDX_VARP(sjme_jint, nextYPos););
	
SDF_STRUCT_DEF(stringWidth,
	SDX_VAR(sjme_scritchui_pencilFont, inFont);
	SDX_VARP(const sjme_scritchui_pencilFontParam, inParams);
	SDX_VAR(sjme_charSeq , s);
	SDX_VAR(sjme_jint , o);
	SDX_VAR(sjme_jint , l);
	SDX_VARP(sjme_jint, outWidth););

#pragma endregion(font)
	
/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */

#undef SDX_STRUCT_DEF
#undef SDU_STRUCT_DEF
#undef SDP_STRUCT_DEF
#undef SDX_VAR
#undef SDX_VARP

/** Define serial data union quicker. */
#define SJME_SCRITCHUI_SDU_DEF(what) \
	volatile SDX_STRUCT_NAME(ui, what) what

/** Define pen serial data union quicker. */
#define SJME_SCRITCHUI_SDP_DEF(what) \
	volatile SDX_STRUCT_NAME(pen, what) what

/** Define font serial data union quicker. */
#define SJME_SCRITCHUI_SDF_DEF(what) \
	volatile SDX_STRUCT_NAME(font, what) what

/**
 * Union for serial data.
 * 
 * @since 2024/04/17
 */
typedef union sjme_scritchui_serialDataUnion
{
/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */
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
	SJME_SCRITCHUI_SDU_DEF(componentGetParent);
	SJME_SCRITCHUI_SDU_DEF(componentPosition);
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
	SJME_SCRITCHUI_SDU_DEF(containerGetFrame);
	SJME_SCRITCHUI_SDU_DEF(containerRemove);
	SJME_SCRITCHUI_SDU_DEF(containerRemoveAll);
	SJME_SCRITCHUI_SDU_DEF(containerSetBounds);
	SJME_SCRITCHUI_SDU_DEF(fontBuiltin);
	SJME_SCRITCHUI_SDU_DEF(fontByFace);
	SJME_SCRITCHUI_SDU_DEF(fontCount);
	SJME_SCRITCHUI_SDU_DEF(fontDerive);
	SJME_SCRITCHUI_SDU_DEF(fontList);
	SJME_SCRITCHUI_SDU_DEF(hardwareGraphics);
	SJME_SCRITCHUI_SDU_DEF(labelSetString);
	SJME_SCRITCHUI_SDU_DEF(lafDpiProject);
	SJME_SCRITCHUI_SDU_DEF(lafElementColor);
	SJME_SCRITCHUI_SDU_DEF(lafMetric);
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
	SJME_SCRITCHUI_SDU_DEF(pseudoGraphics);
	SJME_SCRITCHUI_SDU_DEF(screenGetBounds);
	SJME_SCRITCHUI_SDU_DEF(screenSetListener);
	SJME_SCRITCHUI_SDU_DEF(screens);
	SJME_SCRITCHUI_SDU_DEF(scrollPanelNew);
	SJME_SCRITCHUI_SDU_DEF(viewGetView);
	SJME_SCRITCHUI_SDU_DEF(viewSetArea);
	SJME_SCRITCHUI_SDU_DEF(viewSetView);
	SJME_SCRITCHUI_SDU_DEF(viewSetSizeSuggestListener);
	SJME_SCRITCHUI_SDU_DEF(viewSetViewListener);
	SJME_SCRITCHUI_SDU_DEF(windowContentMinimumSize);
	SJME_SCRITCHUI_SDU_DEF(windowNew);
	SJME_SCRITCHUI_SDU_DEF(windowSetCloseListener);
	SJME_SCRITCHUI_SDU_DEF(windowSetMenuBar);
	SJME_SCRITCHUI_SDU_DEF(windowSetMenuItemActivateListener);
	SJME_SCRITCHUI_SDU_DEF(windowSetVisible);
/* ------------------------------------------------------------------------ */
#pragma region(pencil)
	SJME_SCRITCHUI_SDP_DEF(close);
	SJME_SCRITCHUI_SDP_DEF(copyArea);
	SJME_SCRITCHUI_SDP_DEF(drawArc);
	SJME_SCRITCHUI_SDP_DEF(drawHoriz);
	SJME_SCRITCHUI_SDP_DEF(drawRect);
	SJME_SCRITCHUI_SDP_DEF(drawRegion);
	SJME_SCRITCHUI_SDP_DEF(drawRoundRect);
	SJME_SCRITCHUI_SDP_DEF(drawChar);
	SJME_SCRITCHUI_SDP_DEF(drawChars);
	SJME_SCRITCHUI_SDP_DEF(drawLine);
	SJME_SCRITCHUI_SDP_DEF(drawPixel);
	SJME_SCRITCHUI_SDP_DEF(drawPolyline);
	SJME_SCRITCHUI_SDP_DEF(drawSubstring);
	SJME_SCRITCHUI_SDP_DEF(drawTriangle);
	SJME_SCRITCHUI_SDP_DEF(drawXRGB32Region);
	SJME_SCRITCHUI_SDP_DEF(fillArc);
	SJME_SCRITCHUI_SDP_DEF(fillPolygon);
	SJME_SCRITCHUI_SDP_DEF(fillRect);
	SJME_SCRITCHUI_SDP_DEF(fillRoundRect);
	SJME_SCRITCHUI_SDP_DEF(fillTriangle);
	SJME_SCRITCHUI_SDP_DEF(getRegion);
	SJME_SCRITCHUI_SDP_DEF(mapColor);
	SJME_SCRITCHUI_SDP_DEF(setAlphaColor);
	SJME_SCRITCHUI_SDP_DEF(setBlendingMode);
	SJME_SCRITCHUI_SDP_DEF(setClip);
	SJME_SCRITCHUI_SDP_DEF(setDefaultFont);
	SJME_SCRITCHUI_SDP_DEF(setDefaults);
	SJME_SCRITCHUI_SDP_DEF(setStrokeStyle);
	SJME_SCRITCHUI_SDP_DEF(setFont);
	SJME_SCRITCHUI_SDP_DEF(setParametersFrom);
	SJME_SCRITCHUI_SDP_DEF(transferRegion);
	SJME_SCRITCHUI_SDP_DEF(translate);
#pragma endregion(pencil)
/* ------------------------------------------------------------------------ */
#pragma region(font)
	SJME_SCRITCHUI_SDF_DEF(equals);
	SJME_SCRITCHUI_SDF_DEF(metricCharDirection);
	SJME_SCRITCHUI_SDF_DEF(metricCharValid);
	SJME_SCRITCHUI_SDF_DEF(metricFontFace);
	SJME_SCRITCHUI_SDF_DEF(metricFontName);
	SJME_SCRITCHUI_SDF_DEF(metricFontStyle);
	SJME_SCRITCHUI_SDF_DEF(metricPixelAscent);
	SJME_SCRITCHUI_SDF_DEF(metricPixelBaseline);
	SJME_SCRITCHUI_SDF_DEF(metricPixelDescent);
	SJME_SCRITCHUI_SDF_DEF(metricPixelHeight);
	SJME_SCRITCHUI_SDF_DEF(metricPixelLeading);
	SJME_SCRITCHUI_SDF_DEF(metricPixelSize);
	SJME_SCRITCHUI_SDF_DEF(pixelCharWidth);
	SJME_SCRITCHUI_SDF_DEF(renderBitmap);
	SJME_SCRITCHUI_SDF_DEF(renderChar);
	SJME_SCRITCHUI_SDF_DEF(stringWidth);
#pragma endregion(font)
/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */
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

	/** The graphics context (if any). */
	volatile sjme_scritchui_pencil g;

	/** The font context (if any). */
	volatile sjme_scritchui_pencilFont inFont;
	
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

sjme_errorCode sjme_scritchui_coreSerial_componentGetParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_uiComponent* outParent);
	
sjme_errorCode sjme_scritchui_coreSerial_componentPosition(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY);
	
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
	
sjme_errorCode sjme_scritchui_coreSerial_containerGetFrame(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNullable sjme_scritchui_dim* contentSize,
	sjme_attrOutNullable sjme_scritchui_rect* frameBound,
	sjme_attrOutNullable sjme_scritchui_rect* contentBound);
	
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

sjme_errorCode sjme_scritchui_coreSerial_fontByFace(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* outParams,
	sjme_attrInValue sjme_scritchui_pencilFontFace inFace,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams);
	
sjme_errorCode sjme_scritchui_coreSerial_fontCount(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_jint* outCount);
	
sjme_errorCode sjme_scritchui_coreSerial_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* newFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* newParams,
	sjme_attrInNotNull sjme_scritchui_pencilFont oldFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* deriveParams);
	
sjme_errorCode sjme_scritchui_coreSerial_fontList(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_list(sjme_scritchui_pencilFont)* outFonts,
	sjme_attrOutNotNull sjme_jint* outValid,
	sjme_attrOutNullable sjme_jint* outCount);
	
sjme_errorCode sjme_scritchui_coreSerial_hardwareGraphics(
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
	
sjme_errorCode sjme_scritchui_coreSerial_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString);

sjme_errorCode sjme_scritchui_coreSerial_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH);

sjme_errorCode sjme_scritchui_coreSerial_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor);

sjme_errorCode sjme_scritchui_coreSerial_lafMetric(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInValue sjme_scritchui_lafMetricType metricType);

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
	
sjme_errorCode sjme_scritchui_coreSerial_pseudoGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInNotNullBuf(numPencils) sjme_scritchui_pencil* pencils,
	sjme_attrInPositiveNonZero sjme_jint numPencils,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy);

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen));
	
sjme_errorCode sjme_scritchui_coreSerial_screenGetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen,
	sjme_attrInNullable sjme_scritchui_uiComponent forComponent,
	sjme_attrOutNullable sjme_scritchui_rect* pixelBound,
	sjme_attrOutNullable sjme_scritchui_rect* mmBound);

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
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos);
	
sjme_errorCode sjme_scritchui_coreSerial_viewSetSizeSuggestListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(sizeSuggest));
	
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
	
sjme_errorCode sjme_scritchui_coreSerial_windowSetMenuItemActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(menuItemActivate));

sjme_errorCode sjme_scritchui_coreSerial_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

/*--------------------------------------------------------------------------*/
	
sjme_errorCode sjme_scritchpen_coreSerial_close(
	sjme_attrInNotNull sjme_scritchui_pencil g);

sjme_errorCode sjme_scritchpen_coreSerial_copyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_coreSerial_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

sjme_errorCode sjme_scritchpen_coreSerial_drawChar(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw);

sjme_errorCode sjme_scritchpen_coreSerial_drawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_coreSerial_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w);

sjme_errorCode sjme_scritchpen_coreSerial_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2);

sjme_errorCode sjme_scritchpen_coreSerial_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

sjme_errorCode sjme_scritchpen_coreSerial_drawPolyline(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints);

sjme_errorCode sjme_scritchpen_coreSerial_drawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

sjme_errorCode sjme_scritchpen_coreSerial_drawRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint pf,
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

sjme_errorCode sjme_scritchpen_coreSerial_drawRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight);
	
sjme_errorCode sjme_scritchpen_coreSerial_drawSubstring(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_coreSerial_drawTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

sjme_errorCode sjme_scritchpen_coreSerial_drawXRGB32Region(
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

sjme_errorCode sjme_scritchpen_coreSerial_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

sjme_errorCode sjme_scritchpen_coreSerial_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints);

sjme_errorCode sjme_scritchpen_coreSerial_fillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

sjme_errorCode sjme_scritchpen_coreSerial_fillRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight);

sjme_errorCode sjme_scritchpen_coreSerial_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

sjme_errorCode sjme_scritchpen_coreSerial_getRegion(
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

sjme_errorCode sjme_scritchpen_coreSerial_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);

sjme_errorCode sjme_scritchpen_coreSerial_setAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb);

sjme_errorCode sjme_scritchpen_coreSerial_setBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode);

sjme_errorCode sjme_scritchpen_coreSerial_setClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

sjme_errorCode sjme_scritchpen_coreSerial_setDefaultFont(
	sjme_attrInNotNull sjme_scritchui_pencil g);
	
sjme_errorCode sjme_scritchpen_coreSerial_setDefaults(
	sjme_attrInNotNull sjme_scritchui_pencil g);
	
sjme_errorCode sjme_scritchpen_coreSerial_setStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style);
	
sjme_errorCode sjme_scritchpen_coreSerial_setFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* params);

sjme_errorCode sjme_scritchpen_coreSerial_setParametersFrom(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from);
	
sjme_errorCode sjme_scritchpen_coreSerial_transferRegion(
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

sjme_errorCode sjme_scritchpen_coreSerial_translate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

#pragma region(font)

sjme_errorCode sjme_scritchui_coreSerial_fontEquals(
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* aParams,
	sjme_attrInNullable sjme_scritchui_pencilFont b,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* bParams);
	
sjme_errorCode sjme_scritchui_coreSerial_fontMetricCharDirection(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrInRange(-1, 1) sjme_jint* outDirection);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricFontFace(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricFontName(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricFontStyle(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontStyle* outStyle);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outBaseline);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelHeight(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outHeight);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading);

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNegativeOnePositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize);

sjme_errorCode sjme_scritchui_coreSerial_fontPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth);

sjme_errorCode sjme_scritchui_coreSerial_fontRenderBitmap(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jubyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint bufHeight,
	sjme_attrOutNullable sjme_jint* outOffX,
	sjme_attrOutNullable sjme_jint* outOffY);

sjme_errorCode sjme_scritchui_coreSerial_fontRenderChar(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInValue sjme_jint xPos,
	sjme_attrInNotNull sjme_jint yPos,
	sjme_attrOutNullable sjme_jint* nextXPos,
	sjme_attrOutNullable sjme_jint* nextYPos);
	
sjme_errorCode sjme_scritchui_coreSerial_fontStringWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrOutNotNull sjme_jint* outWidth);

#pragma endregion(font)

/*--------------------------------------------------------------------------*/

#undef SJME_SCRITCHUI_SERIAL_SET_LISTENER
#undef SDX_STRUCT_NAME
#undef SJME_SCRITCHUI_SDU_DEF
#undef SJME_SCRITCHUI_SDP_DEF
	
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
