// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.opengles;

import javax.microedition.khronos.egl.EGLDisplay;

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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/10
	 */
	public DefaultDisplay(Object __nd)
		throws NullPointerException
	{
		// Check
		if (__nd == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.nativedisplay = __nd;
	}
}

