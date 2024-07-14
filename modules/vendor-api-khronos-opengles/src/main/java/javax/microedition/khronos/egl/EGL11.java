// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.egl;


import cc.squirreljme.runtime.cldc.annotation.Api;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface EGL11
	extends EGL10
{
	@Api
	int EGL_BACK_BUFFER =
		12420;
	
	/**
	 * Specifies whether or not binding color buffers to RGB textures is
	 * supported. The value may be {@link EGL10#EGL_DONT_CARE},
	 * {@link EGL10#EGL_FALSE}, or {@link EGL10#EGL_TRUE}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	@Api
	int EGL_BIND_TO_TEXTURE_RGB =
		12345;
	
	/**
	 * Specifies whether or not binding color buffers to RGBA textures is
	 * supported. The value may be {@link EGL10#EGL_DONT_CARE},
	 * {@link EGL10#EGL_FALSE}, or {@link EGL10#EGL_TRUE}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	@Api
	int EGL_BIND_TO_TEXTURE_RGBA =
		12346;
	
	@Api
	int EGL_CONTEXT_LOST =
		12302;
	
	/**
	 * The maximum value that may be passed to
	 * {@link #eglSwapInterval(EGLDisplay, int)}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	@Api
	int EGL_MAX_SWAP_INTERVAL =
		12348;
	
	/**
	 * The minimum value that may be passed to
	 * {@link #eglSwapInterval(EGLDisplay, int)}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	@Api
	int EGL_MIN_SWAP_INTERVAL =
		12347;
	
	@Api
	int EGL_MIPMAP_LEVEL =
		12419;
	
	@Api
	int EGL_MIPMAP_TEXTURE =
		12418;
	
	@Api
	int EGL_NO_TEXTURE =
		12380;
	
	@Api
	int EGL_TEXTURE_2D =
		12383;
	
	@Api
	int EGL_TEXTURE_FORMAT =
		12416;
	
	@Api
	int EGL_TEXTURE_RGB =
		12381;
	
	@Api
	int EGL_TEXTURE_RGBA =
		12382;
	
	@Api
	int EGL_TEXTURE_TARGET =
		12417;
	
	@Api
	boolean eglBindTexImage(EGLDisplay __a, EGLSurface __b, int __c);
	
	@Api
	boolean eglReleaseTexImage(EGLDisplay __a, EGLSurface __b, int __c);
	
	@Api
	boolean eglSurfaceAttrib(EGLDisplay __a, EGLSurface __b, int __c, int __d);
	
	@Api
	boolean eglSwapInterval(EGLDisplay __a, int __b);
}


