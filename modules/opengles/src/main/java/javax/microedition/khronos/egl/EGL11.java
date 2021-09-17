// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.egl;


public interface EGL11
	extends EGL10
{
	int EGL_BACK_BUFFER =
		12420;
	
	/**
	 * Specifies whether or not binding color buffers to RGB textures is
	 * supported. The value may be {@link EGL10#EGL_DONT_CARE},
	 * {@link EGL10#EGL_FALSE}, or {@link EGL10#EGL_TRUE}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	int EGL_BIND_TO_TEXTURE_RGB =
		12345;
	
	/**
	 * Specifies whether or not binding color buffers to RGBA textures is
	 * supported. The value may be {@link EGL10#EGL_DONT_CARE},
	 * {@link EGL10#EGL_FALSE}, or {@link EGL10#EGL_TRUE}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	int EGL_BIND_TO_TEXTURE_RGBA =
		12346;
	
	int EGL_CONTEXT_LOST =
		12302;
	
	/**
	 * The maximum value that may be passed to
	 * {@link #eglSwapInterval(EGLDisplay, int)}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	int EGL_MAX_SWAP_INTERVAL =
		12348;
	
	/**
	 * The minimum value that may be passed to
	 * {@link #eglSwapInterval(EGLDisplay, int)}.
	 *
	 * Default {@link EGL10#EGL_DONT_CARE}.
	 */
	int EGL_MIN_SWAP_INTERVAL =
		12347;
	
	int EGL_MIPMAP_LEVEL =
		12419;
	
	int EGL_MIPMAP_TEXTURE =
		12418;
	
	int EGL_NO_TEXTURE =
		12380;
	
	int EGL_TEXTURE_2D =
		12383;
	
	int EGL_TEXTURE_FORMAT =
		12416;
	
	int EGL_TEXTURE_RGB =
		12381;
	
	int EGL_TEXTURE_RGBA =
		12382;
	
	int EGL_TEXTURE_TARGET =
		12417;
	
	boolean eglBindTexImage(EGLDisplay __a, EGLSurface __b, int __c);
	
	boolean eglReleaseTexImage(EGLDisplay __a, EGLSurface __b, int __c);
	
	boolean eglSurfaceAttrib(EGLDisplay __a, EGLSurface __b, int __c,
	 int __d);
	
	boolean eglSwapInterval(EGLDisplay __a, int __b);
}


