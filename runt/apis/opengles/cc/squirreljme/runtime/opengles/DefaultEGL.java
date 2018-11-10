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

import javax.microedition.khronos.egl.EGL10;
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
	/** The start of temporary enumerants. */
	public static final int TEMPORARY_ENUMERANT_START =
		24576;
	
	/** The end of temporary enumerants. */
	public static final int TEMPORARY_ENUMERANT_END =
		32767;
	
	/** The start of the SquirrelJME enumerant. */
	public static final int SQUIRRELJME_ENUMERANT_START =
		TEMPORARY_ENUMERANT_START;
	
	/** The end of the SquirrelJME enumerant. */
	public static final int SQUIRRELJME_ENUMERANT_END =
		SQUIRRELJME_ENUMERANT_START + 16;
	
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
		throw new todo.TODO();
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
		// If null, then it defaults to not caring about anything
		if (__attrl == null)
			__attrl = new int[]{EGL10.EGL_NONE};
		
		// {@squirreljme.error EJ02 No display was specified for choosing a
		// configuration.}
		if (__disp == null)
		{
			this._error = EGL10.EGL_BAD_DISPLAY;
			throw new IllegalArgumentException("EJ02");
		}
		
		// Configuration must be set
		if (__numconf == null)
		{
			this._error = EGL10.EGL_BAD_PARAMETER;
			return false;
		}
		
		// {@squirreljme.error EJ03 The array containing the number of
		// configurations written has a length of zero.}
		if (__numconf.length < 1)
		{
			this._error = EGL10.EGL_BAD_PARAMETER;
			throw new IllegalArgumentException("EJ03");
		}
		
		// {@squirreljme.error EJ04 Requested more configurations than what
		// may be output.}
		if (__confs != null && __confs.length < __confssize)
		{
			this._error = EGL10.EGL_BAD_PARAMETER;
			throw new IllegalArgumentException("EJ04");
		}
		
		// Must be a SquirrelJME display
		if (!(__disp instanceof DefaultDisplay))
		{
			this._error = EGL10.EGL_BAD_DISPLAY;
			return false;
		}
		
		// Must be initialized
		DefaultDisplay dd = (DefaultDisplay)__disp;
		if (!dd._initialized)
		{
			this._error = EGL10.EGL_NOT_INITIALIZED;
			return false;
		}
		
		// Find the end of the list
		int end = 0, atn = __attrl.length;
		for (int i = 0; i < atn; i++, end++)
			if (__attrl[i] == EGL10.EGL_NONE)
				break;
		
		// {@squirreljme.error EJ05 The attribute list does not end with
		// EGL_NONE.}
		if (end >= atn)
		{
			this._error = EGL10.EGL_BAD_PARAMETER;
			throw new IllegalArgumentException("EJ05");
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglCopyBuffers(EGLDisplay __a, EGLSurface __b, 
		Object __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLContext eglCreateContext(EGLDisplay __a, EGLConfig __b
		, EGLContext __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglCreatePbufferSurface(EGLDisplay __a, 
		EGLConfig __b, int[] __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglCreatePixmapSurface(EGLDisplay __a, 
		EGLConfig __b, Object __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglCreateWindowSurface(EGLDisplay __a, 
		EGLConfig __b, Object __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglDestroyContext(EGLDisplay __a, EGLContext __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglDestroySurface(EGLDisplay __a, EGLSurface __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglGetConfigAttrib(EGLDisplay __a, EGLConfig __b,
		int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglGetConfigs(EGLDisplay __a, EGLConfig[] __b, 
		int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLContext eglGetCurrentContext()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLDisplay eglGetCurrentDisplay()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLSurface eglGetCurrentSurface(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public EGLDisplay eglGetDisplay(Object __nd)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EJ06 No native display was specified when
		// obtaining an OpenGL ES display.}
		if (__nd == null)
			throw new IllegalArgumentException("EJ06");
		
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
		// {@squirreljme.error EJ07 No display was specified.}
		if (__disp == null)
		{
			this._error = EGL11.EGL_BAD_DISPLAY;
			throw new IllegalArgumentException("EJ07");
		}
		
		// {@squirreljme.error EJ08 An output version was specified, however
		// it has a length lower than two.}
		if (__ver != null && __ver.length < 2)
			throw new IllegalArgumentException("EJ08");
		
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglQueryContext(EGLDisplay __a, EGLContext __b, 
		int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	public String eglQueryString(EGLDisplay __disp, int __key)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EJ09 Cannot query string a null display.}
		if (__disp == null)
		{
			this._error = EGL10.EGL_BAD_DISPLAY;
			throw new IllegalArgumentException("EJ09");
		}
		
		// Must be our own display
		if (!(__disp instanceof DefaultDisplay))
		{
			this._error = EGL10.EGL_BAD_DISPLAY;
			return null;
		}
		
		// Must be initialized
		DefaultDisplay dd = (DefaultDisplay)__disp;
		if (!dd._initialized)
		{
			this._error = EGL10.EGL_NOT_INITIALIZED;
			return null;
		}
		
		// Depends on the key
		switch (__key)
		{
				// Vendor
			case EGL10.EGL_VENDOR:
				return "Stephanie Gawroriski (Multi-Phasic Applications)";
			
				// Version
			case EGL10.EGL_VERSION:
				return "1.1 SquirrelJME";
			
				// No extensions are defined
			case EGL10.EGL_EXTENSIONS:
				return "";
				
				// Unknown
			default:
				this._error = EGL10.EGL_BAD_PARAMETER;
				return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglQuerySurface(EGLDisplay __a, EGLSurface __b, 
		int __c, int[] __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglReleaseTexImage(EGLDisplay __a, EGLSurface __b
		, int __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglSurfaceAttrib(EGLDisplay __a, EGLSurface __b, 
		int __c, int __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglSwapBuffers(EGLDisplay __a, EGLSurface __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglSwapInterval(EGLDisplay __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglTerminate(EGLDisplay __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglWaitGL()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglWaitNative(int __a, Object __b)
	{
		throw new todo.TODO();
	}
}

