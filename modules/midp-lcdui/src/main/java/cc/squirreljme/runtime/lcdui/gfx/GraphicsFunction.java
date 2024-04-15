// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

/**
 * This represents a graphical function.
 *
 * @since 2018/11/19
 */
@Deprecated
public enum GraphicsFunction
{
	/** Set color. */
	SET_COLOR,
	
	/** Draw line. */
	DRAW_LINE,
	
	/** Get the X clip. */
	GET_CLIP_X,
	
	/** Get the Y clip. */
	GET_CLIP_Y,
	
	/** Get the width clip. */
	GET_CLIP_WIDTH,
	
	/** Get the height clip. */
	GET_CLIP_HEIGHT,
	
	/** Set the clip. */
	SET_CLIP,
	
	/** Draw rectangle. */
	DRAW_RECT,
	
	/** Get the alpha color. */
	GET_ALPHA_COLOR,
	
	/** Set the alpha color. */
	SET_ALPHA_COLOR,
	
	/** Fill rectangle. */
	FILL_RECT,
	
	/** Sets the fonts for the graphics. */
	SET_FONT,
	
	/** Gets the font to use for drawing. */
	GET_FONT,
	
	/** Draw sub-characters. */
	DRAW_SUB_CHARS,
	
	/** Draw text. */
	DRAW_TEXT,
	
	/** Get stroke style. */
	GET_STROKE_STYLE,
	
	/** Set stroke style. */
	SET_STROKE_STYLE,
	
	/** Copy area. */
	COPY_AREA,
	
	/** Draw arc. */
	DRAW_ARC,
	
	/** Draw ARGB16. */
	DRAW_ARGB16,
	
	/** Draw character. */
	DRAW_CHAR,
	
	/** Draw characters. */
	DRAW_CHARS,
	
	/** Draw RGB. */
	DRAW_RGB,
	
	/** Draw RGB16. */
	DRAW_RGB16,
	
	/** Draw round rectangle. */
	DRAW_ROUND_RECT,
	
	/** Fill arc. */
	FILL_ARC,
	
	/** Fill round rectangle. */
	FILL_ROUND_RECT,
	
	/** Fill triangle. */
	FILL_TRIANGLE,
	
	/** Get blending mode. */
	GET_BLENDING_MODE,
	
	/** Get display color. */
	GET_DISPLAY_COLOR,
	
	/** Set blending mode. */
	SET_BLENDING_MODE,
	
	/** Draw region. */
	DRAW_REGION,
	
	/** End. */
	;
	
	/**
	 * Returns the graphics function for the given ID.
	 *
	 * @param __id The ID to translate.
	 * @return The function for the ID.
	 * @throws IllegalArgumentException If the ID is not valid.
	 * @since 2018/11/19
	 */
	public static GraphicsFunction of(int __id)
		throws IllegalArgumentException
	{
		// Depends
		switch (__id)
		{
			case 0:		return GraphicsFunction.SET_COLOR;
			case 1:		return GraphicsFunction.DRAW_LINE;
			case 2:		return GraphicsFunction.GET_CLIP_X;
			case 3:		return GraphicsFunction.GET_CLIP_Y;
			case 4:		return GraphicsFunction.GET_CLIP_WIDTH;
			case 5:		return GraphicsFunction.GET_CLIP_HEIGHT;
			case 6:		return GraphicsFunction.SET_CLIP;
			case 7:		return GraphicsFunction.DRAW_RECT;
			case 8:		return GraphicsFunction.GET_ALPHA_COLOR;
			case 9:		return GraphicsFunction.SET_ALPHA_COLOR;
			case 10:	return GraphicsFunction.FILL_RECT;
			case 11:	return GraphicsFunction.SET_FONT;
			case 12:	return GraphicsFunction.GET_FONT;
			case 13:	return GraphicsFunction.DRAW_SUB_CHARS;
			case 14:	return GraphicsFunction.DRAW_TEXT;
			case 15:	return GraphicsFunction.GET_STROKE_STYLE;
			case 16:	return GraphicsFunction.SET_STROKE_STYLE;
			case 17:	return GraphicsFunction.COPY_AREA;
			case 18:	return GraphicsFunction.DRAW_ARC;
			case 19:	return GraphicsFunction.DRAW_ARGB16;
			case 20:	return GraphicsFunction.DRAW_CHAR;
			case 21:	return GraphicsFunction.DRAW_CHARS;
			case 22:	return GraphicsFunction.DRAW_RGB;
			case 23:	return GraphicsFunction.DRAW_RGB16;
			case 24:	return GraphicsFunction.DRAW_ROUND_RECT;
			case 25:	return GraphicsFunction.FILL_ARC;
			case 26:	return GraphicsFunction.FILL_ROUND_RECT;
			case 27:	return GraphicsFunction.FILL_TRIANGLE;
			case 28:	return GraphicsFunction.GET_BLENDING_MODE;
			case 29:	return GraphicsFunction.GET_DISPLAY_COLOR;
			case 30:	return GraphicsFunction.SET_BLENDING_MODE;
			case 31:	return GraphicsFunction.DRAW_REGION;
				
				/* {@squirreljme.error EB0j Invalid graphics function.
				(The function ID)} */
			default:
				throw new IllegalArgumentException("EB0j " + __id);
		}
	}
}

