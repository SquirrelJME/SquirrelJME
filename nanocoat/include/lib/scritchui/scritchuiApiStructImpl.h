/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI API structures, implementation and internal specific.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H
#define SJME_C_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H

#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiFuncsImpl.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_IMPL(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_impl_, x, Func) x

/** Uses the same main implementation. */
#define SJME_SCRITCHUI_QUICK_SAME(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_, x, Func) x
	
/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_FONT(what, lWhat) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencilFont, what, Func) lWhat
	
/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_PENCIL(what, lWhat) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencil, what, Func) lWhat
	
#pragma region(scritchui)
	
struct sjme_scritchui_implFunctions
{
	/** The driver name. */
	sjme_lpcstr driverName;
	
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
	
	/** Scan fonts which are available to the system and register to them. */
	sjme_scritchui_fontCountFunc fontScanSystem;
	
	/** Hardware graphics support on arbitrary buffers. */
	SJME_SCRITCHUI_QUICK_SAME(hardwareGraphics);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_SAME(labelSetString);

	/** Projects or reverses a projection of a scaled coordinate. */
	SJME_SCRITCHUI_QUICK_SAME(lafDpiProject);
	
	/** Returns the element color for the look and feel. */
	SJME_SCRITCHUI_QUICK_SAME(lafElementColor);

	/** Returns a default system metric value. */
	SJME_SCRITCHUI_QUICK_SAME(lafMetric);
	
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
	
	/** Creates a new menu. */
	SJME_SCRITCHUI_QUICK_IMPL(menuNew);

	/** Removes an item from the menu. */
	SJME_SCRITCHUI_QUICK_SAME(menuRemove);
	
	/** Enable/disable focus on a panel. */
	SJME_SCRITCHUI_QUICK_SAME(panelEnableFocus);
	
	/** Creates a new native panel. */
	SJME_SCRITCHUI_QUICK_IMPL(panelNew);
	
	/** Get bounds of a screen. */
	SJME_SCRITCHUI_QUICK_SAME(screenGetBounds);
	
	/** The available screens. */
	SJME_SCRITCHUI_QUICK_SAME(screens);
	
	/** Create a new scroll panel. */
	SJME_SCRITCHUI_QUICK_IMPL(scrollPanelNew);
	
	/** Get the current view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_SAME(viewGetView);
	
	/** Set the area of the viewport's bounds, the entire scrollable area. */
	SJME_SCRITCHUI_QUICK_IMPL(viewSetArea);
	
	/** Sets the view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_SAME(viewSetView);
	
	/** Sets the listener for tracking scrolling and viewport changes. */
	SJME_SCRITCHUI_QUICK_SAME(viewSetViewListener);
	
	/** Set minimum size of content window. */
	SJME_SCRITCHUI_QUICK_SAME(windowContentMinimumSize);
	
	/** Content size of a container. */
	sjme_scritchui_containerGetFrameFunc windowGetFrame;
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_IMPL(windowNew);
	
	/** Set close listener for a window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetCloseListener);
	
	/** Sets the menu bar for a window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetMenuBar);
	
	/** Sets visibility of the window. */
	SJME_SCRITCHUI_QUICK_SAME(windowSetVisible);
};

struct sjme_scritchui_internFunctions
{
	/** Binds focus to a window. */
	sjme_scritchui_intern_bindFocusFunc bindFocus;

	/** Gets the max size of a container. */
	sjme_scritchui_intern_containerMaxSizeFunc containerMaxSize;
	
	/** Returns the built-in font, this can handle layers. */
	sjme_scritchui_fontBuiltinFunc fontBuiltin;

	/** Iterate through fonts. */
	sjme_scritchui_intern_fontIterateStepFunc fontIterate;

	/** Flat font parameters to structured font parameters. */
	sjme_scritchui_pencilFontParamFromFlatFunc fontParamFromFlat;

	/** Structured font parameters to flat font parameters. */
	sjme_scritchui_pencilFontParamToFlatFunc fontParamToFlat;
	
	/** Register a font for use. */
	sjme_scritchui_fontRegisterFunc fontRegister;
	
	/** Perform a full font scan and registration. */
	sjme_scritchui_fontCountFunc fontScanAll;
	
	/** Scan for SquirrelJME SQF resource fonts. */
	sjme_scritchui_fontCountFunc fontScanResource;
		
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
	
	/** Maps the given screen. */
	sjme_scritchui_intern_mapScreenFunc mapScreen;
	
	/** Menu item activation propagation, from bottom up. */
	sjme_scritchui_intern_menuItemActivateFunc menuItemActivate;
	
	/** Menu item activation propagation, from top down. */
	sjme_scritchui_intern_menuItemActivateByIdFunc menuItemActivateById;
	
	/** Create a new object instance. */
	sjme_scritchui_core_intern_objectNewFunc objectNew;
	
	/** Set of simple user listener. */
	sjme_scritchui_intern_setSimpleListenerFunc setSimpleListener;
	
	/** Update visibility recursively on container. */
	sjme_scritchui_intern_updateVisibleContainerFunc updateVisibleContainer;
	
	/** Update visibility on component. */
	sjme_scritchui_intern_updateVisibleComponentFunc updateVisibleComponent;
	
	/** Update visibility recursively on window. */
	sjme_scritchui_intern_updateVisibleWindowFunc updateVisibleWindow;
	
	/** Suggest the size and position of a coordinate to a view. */
	sjme_scritchui_intern_viewSuggestFunc viewSuggest;
};
	
#pragma endregion(scritchui)
#pragma region(scritchui_font)
	
/**
 * Functions to native implementation access pencil fonts.
 * 
 * @since 2024/06/27
 */
typedef struct sjme_scritchui_pencilFontImplFunctions
{
	/** The driver name. */
	sjme_lpcstr driverName;
	
	/** Checks font equality. */
	SJME_SCRITCHUI_QUICK_FONT(Equals, equals);
	
	/** Checks the validity of a glyph. */
	SJME_SCRITCHUI_QUICK_FONT(MetricCharValid, metricCharValid);
	
	/** Returns the ascent of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelAscent, metricPixelAscent);
	
	/** Returns the baseline of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelBaseline, metricPixelBaseline);
	
	/** Returns the descent of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelDescent, metricPixelDescent);
	
	/** Returns the leading of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelLeading, metricPixelLeading);
	
	/** Returns the pixel size of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelSize, metricPixelSize);
	
	/** Returns the width of the font character. */
	SJME_SCRITCHUI_QUICK_FONT(PixelCharWidth, pixelCharWidth);
	
	/** Renders the font character to a bitmap. */
	SJME_SCRITCHUI_QUICK_FONT(RenderBitmap, renderBitmap);
} sjme_scritchui_pencilFontImplFunctions;
	
#pragma endregion(scritchui_font)
#pragma region(scritchui_pencil)
	
/**
 * ScritchUI Pencil implementation functions, note that none of these
 * accept transformations however they may accept clipping.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilImplFunctions
{
	/** The driver name. */
	sjme_lpcstr driverName;
	
	/** Asynchronous safe, can be called outside the event thread. */
	sjme_jboolean asyncSafe;
	
	/** @c Close . */
	SJME_SCRITCHUI_QUICK_PENCIL(Close, close);
	
	/** @c CopyArea . */
	sjme_attrDeprecated SJME_SCRITCHUI_QUICK_PENCIL(CopyArea, copyArea);
	
	/** @c DrawHoriz , direct source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHorizSrc);
	
	/** @c DrawHoriz , Over source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHorizSrcOver);
	
	/** @c DrawLine , direct source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLineSrc);
	
	/** @c DrawLine , Over source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLineSrcOver);
	
	/** @c DrawPixel , direct source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixelSrc);
	
	/** @c DrawPixel , Over source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixelSrcOver);
	
	/** @c MapColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(MapColor, mapColor);
	
	/** @c RawScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanGet, rawScanGet);
	
	/** @c RawScanPutPure , to place without blending . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanPutPure, rawScanPutPure);
	
	/** @c SetAlphaColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetAlphaColor, setAlphaColor);
	
	/** @c SetBlendingMode . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetBlendingMode, setBlendingMode);
	
	/** @c SetClip . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetClip, setClip);
	
	/** @c SetStrokeStyle . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetStrokeStyle, setStrokeStyle);
} sjme_scritchui_pencilImplFunctions;

struct sjme_scritchui_pencilLockFunctions
{
	/** @c Lock . */
	SJME_SCRITCHUI_QUICK_PENCIL(Lock, lock);
	
	/** @c LockRelease . */
	SJME_SCRITCHUI_QUICK_PENCIL(LockRelease, lockRelease);
};

/**
 * Lowest level drawing primitives, note that none of these
 * accept transformations however they may accept clipping.
 * 
 * @since 2024/05/17
 */
typedef struct sjme_scritchui_pencilPrimFunctions
{
	/** @c DrawArc . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawArc, drawArc);

	/** @c DrawHoriz . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHoriz);
	
	/** @c DrawLine . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLine);
	
	/** @c DrawPixel . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixel);

	/** @c DrawRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawRect, drawRect);

	/** @c FillArc . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillArc, fillArc);
	
	/** @c FillPolygon . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillPolygonPrim, fillPolygon);

	/** @c FillRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillRect, fillRect);

	/** @c FillTriangle . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillTriangle, fillTriangle);

	/** @c MapColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(MapColor, mapColor);
	
	/** @c RawScanFill. */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanFill, rawScanFill);
	
	/** @c RawScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanGet, rawScanGet);
	
	/** @c RawScanPut without any alpha blending . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanPutPure, rawScanPutPure);
} sjme_scritchui_pencilPrimFunctions;

struct sjme_scritchui_pencilUtilFunctions
{
	/** @c ApplyAnchor . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyAnchor, applyAnchor);
	
	/** @c ApplyCoordinateAdj . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyCoordinateAdj, applyCoordinateAdj);

	/** @c ApplyRotateScale . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyRotateScale, applyRotateScale);
	
	/** @c ApplyTranslate . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyTranslate, applyTranslate);
	
	/** @c BlendRGBInto . */
	SJME_SCRITCHUI_QUICK_PENCIL(BlendRGBInto, blendRGBInto);
	
	/** @c PfScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(PfScanGet, pfScanGet);
	
	/** @c PfScanPut . */
	SJME_SCRITCHUI_QUICK_PENCIL(PfScanPut, pfScanPut);
	
	/** @c PfScanBits . */
	SJME_SCRITCHUI_QUICK_PENCIL(PfScanBits, pfScanBits);
	
	/** @c PfScanBytes . */
	SJME_SCRITCHUI_QUICK_PENCIL(PfScanBytes, pfScanBytes);
	
	/** @c PfScanToPf . */
	SJME_SCRITCHUI_QUICK_PENCIL(PfScanToPf, pfScanToPf);
	
	/** @c PfScanToRgb . */
	SJME_SCRITCHUI_QUICK_PENCIL(PfScanToRgb, pfScanToRgb);
	
	/** @c RawScanToRgb . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanToRgb, rawScanToRgb);
	
	/** @c RgbScanFill . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanFill, rgbScanFill);
	
	/** @c RgbScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanGet, rgbScanGet);
	
	/** @c RgbScanPut . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanPut, rgbScanPut);
	
	/** @c RgbScanToPf . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanToPf, rgbScanToPf);
	
	/** @c RgbScanToRaw . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanToRaw, rgbScanToRaw);
};
	
#pragma endregion(scritchui_pencil)
	
#undef SJME_SCRITCHUI_QUICK_API
#undef SJME_SCRITCHUI_QUICK_FONT
#undef SJME_SCRITCHUI_QUICK_PENCIL
#undef SJME_SCRITCHUI_QUICK_IMPL
#undef SJME_SCRITCHUI_QUICK_SAME
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUIAPISTRUCTIMPL_H */
