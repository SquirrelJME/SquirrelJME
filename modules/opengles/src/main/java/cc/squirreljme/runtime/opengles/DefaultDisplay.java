// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.opengles;

import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CanvasItem;

/**
 * This represents a display used by the OpenGL rasterizer.
 *
 * @since 2016/10/10
 */
public class DefaultDisplay
	extends EGLDisplay
{
	/** The native display object. */
	protected final Object nativedisplay;
	
	/** Was this display initialized? */
	volatile boolean _initialized;
	
	/**
	 * Initializes the OpenGL ES display.
	 *
	 * @param __nd The native display to use.
	 * @throws IllegalArgumentException If the native display is not of a
	 * compatible type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/10
	 */
	public DefaultDisplay(Object __nd)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__nd == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EJ01 The specified class type cannot be used
		// as a display for OpenGL ES graphics. (The class type)}
		if (!(__nd instanceof Canvas || __nd instanceof CanvasItem))
			throw new IllegalArgumentException(String.format("EJ01 %s",
				__nd.getClass()));
		
		// Set
		this.nativedisplay = __nd;
	}
}

