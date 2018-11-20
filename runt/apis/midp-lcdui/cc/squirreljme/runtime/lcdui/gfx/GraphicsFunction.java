// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

/**
 * This represents a graphical function.
 *
 * @since 2018/11/19
 */
public enum GraphicsFunction
{
	/** Clip rectangle. */
	CLIP_RECT,
	
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
	
	/** Get X translation. */
	GET_TRANSLATE_X,
	
	/** Get Y translation. */
	GET_TRANSLATE_Y,
	
	/** Translate. */
	TRANSLATE,
	
	/** Set the clip. */
	SET_CLIP,
	
	/** Draw rectangle. */
	DRAW_RECT,
	
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
			case 0:		return CLIP_RECT;
			case 1:		return SET_COLOR;
			case 2:		return DRAW_LINE;
			case 3:		return GET_CLIP_X;
			case 4:		return GET_CLIP_Y;
			case 5:		return GET_CLIP_WIDTH;
			case 6:		return GET_CLIP_HEIGHT;
			case 7:		return GET_TRANSLATE_X;
			case 8:		return GET_TRANSLATE_Y;
			case 9:		return TRANSLATE;
			case 10:	return SET_CLIP;
			case 11:	return DRAW_RECT;
			
				// {@squirreljme.error EB2d Invalid graphics function.
				// (The function ID)}
			default:
				throw new IllegalArgumentException("EB2d " + __id);
		}
	}
}

