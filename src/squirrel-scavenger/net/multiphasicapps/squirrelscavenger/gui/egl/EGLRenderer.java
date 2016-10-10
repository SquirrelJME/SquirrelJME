// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.egl;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import net.multiphasicapps.squirrelscavenger.game.Game;

/**
 * This provides a bridge used by both the LCDUI and the LUI GUI front ends
 * that utilizes an Open GL ES renderer which can draw into a graphics
 * object.
 *
 * @since 2016/10/10
 */
public class EGLRenderer
{
	/** The graphics to use. */
	protected final Graphics graphics;
	
	/** OpenGL ES instance. */
	protected final EGL10 egl;
	
	/** The rasterizer. */
	protected final GL10 gl;
	
	/**
	 * Initializes the renderer for the game.
	 *
	 * @param __d The display to use when initializing OpenGL ES, may be
	 * {@code null} if it is not known or used.
	 * @param __g The graphics object to draw into.
	 * @throws NullPointerException If {@code __g} is {@code null}.
	 */
	public EGLRenderer(Display __d, Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.graphics = __g;
		
		// Get OpenGL ES rendering engine
		EGL10 egl = (EGL10)EGLContext.getEGL();
		this.egl = egl;
		
		// {@squirreljme.error BA06 Could not get the OpenGL ES display.}
		EGLDisplay egldisp = egl.eglGetDisplay((__d != null ? __d :
			EGL10.EGL_DEFAULT_DISPLAY));
		if (egldisp == null || egldisp == EGL10.EGL_NO_DISPLAY)
			throw new RuntimeException("BA06");
		
		// {@squirreljme.error BA09 Could not initialize the OpenGL ES
		// display.}
		int[] version = new int[2];
		if (!egl.eglInitialize(egldisp, version))
			throw new RuntimeException("BA09");
		
		// Try to get a R8G8B8 visual
		int[] wantconf = new int[]
			{
				EGL10.EGL_RED_SIZE, 8,
				EGL10.EGL_GREEN_SIZE, 8,
				EGL10.EGL_BLUE_SIZE, 8,
				EGL10.EGL_ALPHA_SIZE, EGL10.EGL_DONT_CARE,
				EGL10.EGL_DEPTH_SIZE, EGL10.EGL_DONT_CARE,
				EGL10.EGL_STENCIL_SIZE, EGL10.EGL_DONT_CARE,
				EGL10.EGL_NONE
			};
		
		// {@squirreljme.error BA08 Could not get a configuration that uses
		// 8-bit red, green, and blue pixels.}
		int[] xnumconf = new int[1];
		EGLConfig[] matchconfs = new EGLConfig[1];
		if (!egl.eglChooseConfig(egldisp, wantconf, matchconfs, 1, xnumconf))
			throw new RuntimeException("BA08");
		EGLConfig config = matchconfs[0];
		
		// {@squirreljme.error BA07 Failed to create an OpenGL ES context.}
		EGLContext context = egl.eglCreateContext(egldisp, config,
			EGL10.EGL_NO_CONTEXT, null);
		if (context == EGL10.EGL_NO_CONTEXT)
			throw new RuntimeException("BA07");
		
		// Get GL rasterizer
		GL10 gl = (GL10)context.getGL();
		this.gl = gl;
		
		// Create surface
		EGLSurface surface = egl.eglCreateWindowSurface(egldisp, config,
			__g, null);
		
		// Make it current
		egl.eglMakeCurrent(egldisp, surface, surface, context);
	}
	
	/**
	 * Renders the game.
	 *
	 * @param __g The game to render.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/10
	 */
	public void renderGame(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Wait until native drawing operations are completed
		Graphics g = this.graphics;
		EGL10 egl = this.egl;
		egl.eglWaitNative(EGL10.EGL_CORE_NATIVE_ENGINE, g);
		
		// Draw OpenGL stuff
		
		// Wait for OpenGL operations to complete
		egl.eglWaitGL();
	}
}

