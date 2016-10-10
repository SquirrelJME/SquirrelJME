// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.egl;

/**
 * This is the SquirrelJME implementation of OpenGL ES which provides access
 * to implemented rasterizers via the service interface.
 *
 * @since 2016/10/10
 */
class __EGL__
	implements EGL11
{
	public boolean eglBindTexImage(EGLDisplay __a, EGLSurface __b, 
		int __c)
	{
		throw new Error("TODO");
	}
		
	public boolean eglChooseConfig(EGLDisplay __a, int[] __b, 
		EGLConfig[] __c, int __d, int[] __e)
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
	public boolean eglDestroyContext(EGLDisplay __a, EGLContext __b
		)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglDestroySurface(EGLDisplay __a, EGLSurface __b
		)
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
	public EGLDisplay eglGetDisplay(Object __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int eglGetError()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public boolean eglInitialize(EGLDisplay __a, int[] __b)
	{
		throw new Error("TODO");
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

