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

import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * This is the SquirrelJME implementation of OpenGL ES which provides access
 * to implemented rasterizers via the service interface.
 *
 * @since 2016/10/10
 */
public class DefaultEGL
	implements EGL11
{
	/** The OpenGL ES error code. */
	private volatile int _error =
		EGL11.EGL_SUCCESS;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglBindTexImage(EGLDisplay __a, EGLSurface __b, 
		int __c)
	{
		throw new Error("TODO");
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglChooseConfig(EGLDisplay __disp, int[] __attrl,
		EGLConfig[] __confs, int __confssize, int[] __numconf)
		throws IllegalArgumentException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglCopyBuffers(EGLDisplay __a, EGLSurface __b, 
		Object __c)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLContext eglCreateContext(EGLDisplay __a, EGLConfig __b
		, EGLContext __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglCreatePbufferSurface(EGLDisplay __a, 
		EGLConfig __b, int[] __c)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglCreatePixmapSurface(EGLDisplay __a, 
		EGLConfig __b, Object __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglCreateWindowSurface(EGLDisplay __a, 
		EGLConfig __b, Object __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglDestroyContext(EGLDisplay __a, EGLContext __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglDestroySurface(EGLDisplay __a, EGLSurface __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglGetConfigAttrib(EGLDisplay __a, EGLConfig __b,
		int __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglGetConfigs(EGLDisplay __a, EGLConfig[] __b, 
		int __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLContext eglGetCurrentContext()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLDisplay eglGetCurrentDisplay()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglGetCurrentSurface(int __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLDisplay eglGetDisplay(Object __nd)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EJ02 No native display was specified when
		// obtaining an OpenGL ES display.}
		if (__nd == null)
			throw new IllegalArgumentException("EJ02");
		
		// Just create a new display regardless of the native display object
		// used.
		return new DefaultDisplay(__nd);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int eglGetError()
	{
		int rv = this._error;
		this._error = EGL11.EGL_SUCCESS;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglInitialize(EGLDisplay __disp, int[] __ver)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EJ03 No display was specified.}
		if (__disp == null)
		{
			this._error = EGL11.EGL_BAD_DISPLAY;
			throw new IllegalArgumentException("EJ03");
		}
		
		// {@squirreljme.error EJ04 An output version was specified, however
		// it has a length lower than two.}
		if (__ver != null && __ver.length < 2)
			throw new IllegalArgumentException("EJ04");
		
		// Not our kind of display?
		if (!(__disp instanceof DefaultDisplay))
		{
			this._error = EGL11.EGL_BAD_DISPLAY;
			return false;
		}
		
		// Mark it as initialized
		DefaultDisplay dd = (DefaultDisplay)__disp;
		dd._initialized = true;
		
		// Return version number?
		if (__ver != null)
		{
			__ver[0] = 1;
			__ver[1] = 1;
		}
		
		// Ok
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglMakeCurrent(EGLDisplay __a, EGLSurface __b, 
		EGLSurface __c, EGLContext __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglQueryContext(EGLDisplay __a, EGLContext __b, 
		int __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public String eglQueryString(EGLDisplay __a, int __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglQuerySurface(EGLDisplay __a, EGLSurface __b, 
		int __c, int[] __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglReleaseTexImage(EGLDisplay __a, EGLSurface __b
		, int __c)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglSurfaceAttrib(EGLDisplay __a, EGLSurface __b, 
		int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglSwapBuffers(EGLDisplay __a, EGLSurface __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglSwapInterval(EGLDisplay __a, int __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglTerminate(EGLDisplay __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglWaitGL()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglWaitNative(int __a, Object __b)
	{
		throw new Error("TODO");
	}
}

