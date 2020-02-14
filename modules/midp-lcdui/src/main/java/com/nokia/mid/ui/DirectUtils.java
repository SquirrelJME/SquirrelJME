// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This class acts as a placeholder for utility methods.
 *
 * @since 2019/10/07
 */
public class DirectUtils
{
	/**
	 * Creates a mutable image with the given ARGB color for all pixels.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @param __argb The ARGB color to do.
	 * @return The created mutable image.
	 * @throws IllegalArgumentException If the width and/or height or negative
	 * or exceed the array bounds.
	 * @since 2019/10/07
	 */
	public static Image createImage(int __w, int __h, int __argb)
		throws IllegalArgumentException
	{
		return Image.createImage(__w, __h, true, __argb);
	}
	
	/**
	 * Loads a mutable image from the specified byte array.
	 *
	 * @param __b The byte data.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The mutable image.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IllegalArgumentException If the image could not be decoded.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/07
	 */
	public static Image createImage(byte[] __b, int __o, int __l)
	{
		// Load the base image
		Image base = Image.createImage(__b, __o, __l);
		
		// Create blank mutable
		int w, h;
		Image mutable = Image.createImage(
			(w = base.getWidth()), (h = base.getHeight()),
			base.hasAlpha(), 0);
			
		// Setup graphics state, use SRC blending mode since it is just a
		// copy of the alpha channel data!
		Graphics g = mutable.getGraphics();
		g.setBlendingMode(Graphics.SRC);
		
		// Draw image on top
		g.drawRegion(base, 0, 0, w, h, 0, 0, 0, 0);
		
		// Use resulting image
		return mutable;
	}
	
	/**
	 * Returns an interface which wraps the given graphics and provides raw
	 * pixel data access to it.
	 *
	 * @param __g The graphics object to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/07
	 */
	public static DirectGraphics getDirectGraphics(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

