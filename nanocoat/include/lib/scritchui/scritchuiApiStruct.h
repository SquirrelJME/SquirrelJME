/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI API Structures.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUIAPISTRUCT_H
#define SJME_C_SQUIRRELJME_SCRITCHUIAPISTRUCT_H

#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiFuncs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCT_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Quicker API declaration in struct. */
#define SJME_SCRITCHUI_QUICK_API(x) \
	SJME_TOKEN_PASTE3(sjme_scritchui_, x, Func) x
	
/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_FONT(what, lWhat) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencilFont, what, Func) lWhat
	
/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_PENCIL(what, lWhat) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencil, what, Func) lWhat
	
#pragma region(scritchui)
	
struct sjme_scritchui_apiFunctions
{
	/** Get the first selected index of a choice. */
	SJME_SCRITCHUI_QUICK_API(choiceGetSelectedIndex);
	
	/** Gets the item information. */
	SJME_SCRITCHUI_QUICK_API(choiceItemGet);
	
	/** Inserts an item into the given choice. */
	SJME_SCRITCHUI_QUICK_API(choiceItemInsert);
	
	/** Removes an item from the given choice. */
	SJME_SCRITCHUI_QUICK_API(choiceItemRemove);
	
	/** Removes all items from the given choice. */
	SJME_SCRITCHUI_QUICK_API(choiceItemRemoveAll);
	
	/** Sets whether the given choice item is enabled. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetEnabled);
	
	/** Sets the image of the given choice item. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetImage);
	
	/** Sets whether the given choice item is selected. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetSelected);
	
	/** Sets the string of the given choice item. */
	SJME_SCRITCHUI_QUICK_API(choiceItemSetString);
	
	/** Gets the choice length. */
	SJME_SCRITCHUI_QUICK_API(choiceLength);
	
	/** Grabs the focus for this component. */
	SJME_SCRITCHUI_QUICK_API(componentFocusGrab);
	
	/** Checks if this component has focus. */
	SJME_SCRITCHUI_QUICK_API(componentFocusHas);
	
	/** Gets the parent component of this one. */
	SJME_SCRITCHUI_QUICK_API(componentGetParent);
	
	/** Get size of component. */
	SJME_SCRITCHUI_QUICK_API(componentPosition);
	
	/** Repaints the given component. */
	SJME_SCRITCHUI_QUICK_API(componentRepaint);
	
	/** Revalidates the given component. */
	SJME_SCRITCHUI_QUICK_API(componentRevalidate);
	
	/** Set listener for when a component is activated. */
	SJME_SCRITCHUI_QUICK_API(componentSetActivateListener);
	
	/** Sets the input listener for a component. */
	SJME_SCRITCHUI_QUICK_API(componentSetInputListener);
	
	/** Sets the paint listener for a component. */
	SJME_SCRITCHUI_QUICK_API(componentSetPaintListener);
	
	/** Sets the listener for component size events. */
	SJME_SCRITCHUI_QUICK_API(componentSetSizeListener);
	
	/** Set listener for when a component value has updated. */
	SJME_SCRITCHUI_QUICK_API(componentSetValueUpdateListener);
	
	/** Sets the listener for component visible events. */
	SJME_SCRITCHUI_QUICK_API(componentSetVisibleListener);

	/** Get size of component. */
	SJME_SCRITCHUI_QUICK_API(componentSize);
	
	/** Adds component to container. */
	SJME_SCRITCHUI_QUICK_API(containerAdd);
	
	/** Content size of a container. */
	SJME_SCRITCHUI_QUICK_API(containerGetFrame);
	
	/** Remove component from container. */
	SJME_SCRITCHUI_QUICK_API(containerRemove);
	
	/** Remove all components from a container. */
	SJME_SCRITCHUI_QUICK_API(containerRemoveAll);
	
	/** Set bounds of component in a container. */
	SJME_SCRITCHUI_QUICK_API(containerSetBounds);

	/** Returns the default built-in font. */
	SJME_SCRITCHUI_QUICK_API(fontBuiltin);

	/** @link sjme_scritchui_fontByFaceFunc @endlink . */
	SJME_SCRITCHUI_QUICK_API(fontByFace);
	
	/** The total number of fonts. */
	SJME_SCRITCHUI_QUICK_API(fontCount);
	
	/** Derive a similar font. */
	SJME_SCRITCHUI_QUICK_API(fontDerive);
	
	/** Return the set of available fonts. */
	SJME_SCRITCHUI_QUICK_API(fontList);
	
	/** Hardware graphics support on arbitrary buffers. */
	SJME_SCRITCHUI_QUICK_API(hardwareGraphics);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_API(labelSetString);

	/** Projects or reverses a projection of a scaled coordinate. */
	SJME_SCRITCHUI_QUICK_API(lafDpiProject);
	
	/** Returns the element color for the look and feel. */
	SJME_SCRITCHUI_QUICK_API(lafElementColor);

	/** Returns a default system metric value. */
	SJME_SCRITCHUI_QUICK_API(lafMetric);
	
	/** Creates a new list. */
	SJME_SCRITCHUI_QUICK_API(listNew);
	
	/** Execute callback within the event loop. */
	SJME_SCRITCHUI_QUICK_API(loopExecute);
	
	/** Execute callback later in the event loop. */
	sjme_scritchui_loopExecuteFunc loopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc loopExecuteWait;
	
	/** Is the current thread in the loop? */
	SJME_SCRITCHUI_QUICK_API(loopIsInThread);
	
	/** Iterates a single run of the event loop. */
	SJME_SCRITCHUI_QUICK_API(loopIterate);
	
	/** Creates a new menu bar. */
	SJME_SCRITCHUI_QUICK_API(menuBarNew);
	
	/** Insert the given menu item into a menu. */
	SJME_SCRITCHUI_QUICK_API(menuInsert);
	
	/** Creates a new menu item. */
	SJME_SCRITCHUI_QUICK_API(menuItemNew);
	
	/** Creates a new menu. */
	SJME_SCRITCHUI_QUICK_API(menuNew);
	
	/** Removes an item from the menu. */
	SJME_SCRITCHUI_QUICK_API(menuRemove);
	
	/** Removes all items from the menu. */
	SJME_SCRITCHUI_QUICK_API(menuRemoveAll);
	
	/** Deletes an object. */
	SJME_SCRITCHUI_QUICK_API(objectDelete);
	
	/** Enable focus on a panel. */
	SJME_SCRITCHUI_QUICK_API(panelEnableFocus);
	
	/** Creates a new panel. */
	SJME_SCRITCHUI_QUICK_API(panelNew);
	
	/** Pseudo pencil graphics. */
	SJME_SCRITCHUI_QUICK_API(pseudoGraphics);
	
	/** Get bounds of a screen. */
	SJME_SCRITCHUI_QUICK_API(screenGetBounds);
	
	/** Register listener. */
	SJME_SCRITCHUI_QUICK_API(screenSetListener);
	
	/** Screens available. */
	SJME_SCRITCHUI_QUICK_API(screens);
	
	/** Create a new scroll panel. */
	SJME_SCRITCHUI_QUICK_API(scrollPanelNew);
	
	/** Get the current view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_API(viewGetView);
	
	/** Set the area of the viewport's bounds, the entire scrollable area. */
	SJME_SCRITCHUI_QUICK_API(viewSetArea);
	
	/** Sets the view rect of a viewport. */
	SJME_SCRITCHUI_QUICK_API(viewSetView);
	
	/** Sets the size suggestion for this view. */
	SJME_SCRITCHUI_QUICK_API(viewSetSizeSuggestListener);
	
	/** Sets the listener for tracking scrolling and viewport changes. */
	SJME_SCRITCHUI_QUICK_API(viewSetViewListener);
	
	/** Sets minimum size of the window contents. */
	SJME_SCRITCHUI_QUICK_API(windowContentMinimumSize);
	
	/** Creates a new window. */
	SJME_SCRITCHUI_QUICK_API(windowNew);
	
	/** Sets the close listener for a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetCloseListener);

	/** Sets the menu bar for a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetMenuBar);
	
	/** Sets the activation listener for menu items in a window. */
	SJME_SCRITCHUI_QUICK_API(windowSetMenuItemActivateListener);
	
	/** Sets visibility of window. */
	SJME_SCRITCHUI_QUICK_API(windowSetVisible);
};
	
/**
 * Optional external functions for ScritchUI to use dependent on the front
 * end that is using it, this is usually to provide cross-feedback.
 *
 * @since 2024/11/29
 */
typedef struct sjme_scritchui_externalFunctions
{
	/** Loads an external asset. */
	sjme_scritchui_externalAssetFunc externalAsset;
	
	/** Execute callback within the event loop or schedule later. */
	sjme_scritchui_loopExecuteFunc externalLoopExecute;
	
	/** Execute call later in the loop. */
	sjme_scritchui_loopExecuteFunc externalLoopExecuteLater;
	
	/** Execute callback within the event loop and wait until termination. */
	sjme_scritchui_loopExecuteFunc externalLoopExecuteWait;
} sjme_scritchui_externalFunctions;
	
#pragma endregion(scritchui)
#pragma region(scritchui_font)
	
/**
 * Functions to access pencil fonts.
 * 
 * @since 2024/05/17
 */
typedef struct sjme_scritchui_pencilFontFunctions
{
	/** Checks font equality. */
	SJME_SCRITCHUI_QUICK_FONT(Equals, equals);
	
	/** Returns the direction of the character. */
	SJME_SCRITCHUI_QUICK_FONT(MetricCharDirection, metricCharDirection);
	
	/** Checks the validity of a glyph. */
	SJME_SCRITCHUI_QUICK_FONT(MetricCharValid, metricCharValid);
	
	/** Returns the face of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricFontFace, metricFontFace);
	
	/** Returns the name of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricFontName, metricFontName);
	
	/** Returns the style of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricFontStyle, metricFontStyle);
	
	/** Returns the ascent of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelAscent, metricPixelAscent);
	
	/** Returns the baseline of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelBaseline, metricPixelBaseline);
	
	/** Returns the descent of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelDescent, metricPixelDescent);
	
	/** Returns the height of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelHeight, metricPixelHeight);
	
	/** Returns the leading of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelLeading, metricPixelLeading);
	
	/** Returns the pixel size of the font. */
	SJME_SCRITCHUI_QUICK_FONT(MetricPixelSize, metricPixelSize);
	
	/** Returns the width of the font character. */
	SJME_SCRITCHUI_QUICK_FONT(PixelCharWidth, pixelCharWidth);
	
	/** Renders the font character to a bitmap. */
	SJME_SCRITCHUI_QUICK_FONT(RenderBitmap, renderBitmap);
	
	/** Renders the font character to the given pencil. */
	SJME_SCRITCHUI_QUICK_FONT(RenderChar, renderChar);
	
	/** Calculates the length of the given string. */
	SJME_SCRITCHUI_QUICK_FONT(StringWidth, stringWidth);
} sjme_scritchui_pencilFontFunctions;
	
#pragma endregion(scritchui_font)
#pragma region(scritchui_pencil)
	

/**
 * ScritchUI Pencil API functions.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilFunctions
{
	/** @c Close . */
	SJME_SCRITCHUI_QUICK_PENCIL(Close, close);
	
	/** @c CopyArea . */
	sjme_attrDeprecated SJME_SCRITCHUI_QUICK_PENCIL(CopyArea, copyArea);

	/** @c DrawArc . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawArc, drawArc);

	/** @c DrawChar . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawChar, drawChar);
	
	/** @c DrawChars . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawChars, drawChars);
	
	/** @c DrawHoriz . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHoriz);
	
	/** @c DrawLine . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLine);

	/** @c DrawPixel . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixel);

	/** @c DrawPolyline . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPolyline, drawPolyline);
	
	/** @c DrawRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawRect, drawRect);

	/** @c DrawRegion . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawRegion, drawRegion);

	/** @c DrawRoundRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawRoundRect, drawRoundRect);
	
	/** @c DrawSubstring . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawSubstring, drawSubstring);

	/** @c DrawTriangle . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawTriangle, drawTriangle);
	
	/** @c DrawXRGB32Region . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawXRGB32Region, drawXRGB32Region);

	/** @c FillArc . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillArc, fillArc);

	/** @c FillPolygon . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillPolygon, fillPolygon);

	/** @c FillRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillRect, fillRect);

	/** @c FillRoundRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillRoundRect, fillRoundRect);
	
	/** @c FillTriangle . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillTriangle, fillTriangle);

	/** @c GetRegion . */
	SJME_SCRITCHUI_QUICK_PENCIL(GetRegion, getRegion);

	/** @c MapColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(MapColor, mapColor);
	
	/** @c SetAlphaColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetAlphaColor, setAlphaColor);
	
	/** @c SetBlendingMode . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetBlendingMode, setBlendingMode);
	
	/** @c SetClip . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetClip, setClip);
	
	/** @c SetDefaultFont . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetDefaultFont, setDefaultFont);
	
	/** @c SetDefaults . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetDefaults, setDefaults);
	
	/** @c SetFont . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetFont, setFont);
	
	/** @c SetParametersFrom . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetParametersFrom, setParametersFrom);
	
	/** @c SetStrokeStyle . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetStrokeStyle, setStrokeStyle);
	
	/** @c TransferRegion . */
	SJME_SCRITCHUI_QUICK_PENCIL(TransferRegion, transferRegion);
	
	/** @c Translate . */
	SJME_SCRITCHUI_QUICK_PENCIL(Translate, translate);
} sjme_scritchui_pencilFunctions;
	
#pragma endregion(scritchui_pencil)

#undef SJME_SCRITCHUI_QUICK_API
#undef SJME_SCRITCHUI_QUICK_FONT
#undef SJME_SCRITCHUI_QUICK_PENCIL
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCT_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCT_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIAPISTRUCT_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUIAPISTRUCT_H */
