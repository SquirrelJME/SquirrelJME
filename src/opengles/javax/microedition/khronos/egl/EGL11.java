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


public interface EGL11
	extends EGL10
{
	public static final int EGL_BACK_BUFFER =
		12420;
	
	public static final int EGL_BIND_TO_TEXTURE_RGB =
		12345;
	
	public static final int EGL_BIND_TO_TEXTURE_RGBA =
		12346;
	
	public static final int EGL_CONTEXT_LOST =
		12302;
	
	public static final int EGL_MAX_SWAP_INTERVAL =
		12348;
	
	public static final int EGL_MIN_SWAP_INTERVAL =
		12347;
	
	public static final int EGL_MIPMAP_LEVEL =
		12419;
	
	public static final int EGL_MIPMAP_TEXTURE =
		12418;
	
	public static final int EGL_NO_TEXTURE =
		12380;
	
	public static final int EGL_TEXTURE_2D =
		12383;
	
	public static final int EGL_TEXTURE_FORMAT =
		12416;
	
	public static final int EGL_TEXTURE_RGB =
		12381;
	
	public static final int EGL_TEXTURE_RGBA =
		12382;
	
	public static final int EGL_TEXTURE_TARGET =
		12417;
	
	public abstract boolean eglBindTexImage(EGLDisplay __a, EGLSurface __b, 
		int __c);
	
	public abstract boolean eglReleaseTexImage(EGLDisplay __a, EGLSurface __b
		, int __c);
	
	public abstract boolean eglSurfaceAttrib(EGLDisplay __a, EGLSurface __b, 
		int __c, int __d);
	
	public abstract boolean eglSwapInterval(EGLDisplay __a, int __b);
}


