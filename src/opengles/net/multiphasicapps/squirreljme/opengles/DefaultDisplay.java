// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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

