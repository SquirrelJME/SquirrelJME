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
			
				// {@squirreljme.error EB2d Invalid graphics function.
				// (The function ID)}
			default:
				throw new IllegalArgumentException("EB2d " + __id);
		}
	}
}

