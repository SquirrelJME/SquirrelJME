// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.lcdui;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;

/**
 * This is the canvas which is used to draw the actual game.
 *
 * @since 2016/10/08
 */
public class LCDCanvas
	extends GameCanvas
{
	/** Graphics used to draw on the canvas, required by OpenGL. */
	protected final Graphics graphics;
	
	/** The rasterizer being used. */
	protected final EGL10 egl;
	
	/**
	 * Initializes the canvas.
	 *
	 * @param __d The display to bind an OpenGL context to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public LCDCanvas(Display __d)
		throws NullPointerException
	{
		super(false);
		
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Get OpenGL ES rendering engine
		EGL10 egl = (EGL10)EGLContext.getEGL();
		this.egl = egl;
		
		// {@squirreljme.error BA06 Could not get the OpenGL ES display.}
		EGLDisplay egldisp = egl.eglGetDisplay(__d);
		if (egldisp == null || egldisp == EGL10.EGL_NO_DISPLAY)
			throw new RuntimeException("BA06");
		
		// Buffer to draw on
		Graphics graphics = this.getGraphics();
		this.graphics = graphics;
		
		throw new Error("TODO");
	}
}

