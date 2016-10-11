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

import javax.microedition.khronos.opengles.GL;

/**
 * This interface defines the standard OpenGL ES definitions and methods.
 *
 * To initialize a display, {@link #eglGetDisplay(Object)} must be called
 * where the {@link Object} is an instance of
 * {@link javax.microedition.lcdui.Display}.
 *
 * OpenGL ES is utilized by calling
 * {@link #eglCreateWindowSurface(EGLDisplay, EGLConfig, Object, int[]). The
 * {@link Object} parameter in this call represents a native surface to draw
 * onto. In this case, it is an instance of
 * {@link javax.microedition.lcdui.Graphics}.
 */
public interface EGL10
	extends EGL
{
	public static final int EGL_ALPHA_SIZE =
		12321;
	
	public static final int EGL_BAD_ACCESS =
		12290;
	
	public static final int EGL_BAD_ALLOC =
		12291;
	
	public static final int EGL_BAD_ATTRIBUTE =
		12292;
	
	public static final int EGL_BAD_CONFIG =
		12293;
	
	public static final int EGL_BAD_CONTEXT =
		12294;
	
	public static final int EGL_BAD_CURRENT_SURFACE =
		12295;
	
	public static final int EGL_BAD_DISPLAY =
		12296;
	
	public static final int EGL_BAD_MATCH =
		12297;
	
	public static final int EGL_BAD_NATIVE_PIXMAP =
		12298;
	
	public static final int EGL_BAD_NATIVE_WINDOW =
		12299;
	
	public static final int EGL_BAD_PARAMETER =
		12300;
	
	public static final int EGL_BAD_SURFACE =
		12301;
	
	public static final int EGL_BLUE_SIZE =
		12322;
	
	public static final int EGL_BUFFER_SIZE =
		12320;
	
	public static final int EGL_CONFIG_CAVEAT =
		12327;
	
	public static final int EGL_CONFIG_ID =
		12328;
	
	public static final int EGL_CORE_NATIVE_ENGINE =
		12379;
	
	/** This is used to signify that the default display should be used. */
	public static final Object EGL_DEFAULT_DISPLAY =
		new Object();
	
	public static final int EGL_DEPTH_SIZE =
		12325;
	
	public static final int EGL_DONT_CARE =
		-1;
	
	public static final int EGL_DRAW =
		12377;
	
	public static final int EGL_EXTENSIONS =
		12373;
	
	public static final int EGL_FALSE =
		0;
	
	public static final int EGL_GREEN_SIZE =
		12323;
	
	public static final int EGL_HEIGHT =
		12374;
	
	public static final int EGL_LARGEST_PBUFFER =
		12376;
	
	public static final int EGL_LEVEL =
		12329;
	
	public static final int EGL_MAX_PBUFFER_HEIGHT =
		12330;
	
	public static final int EGL_MAX_PBUFFER_PIXELS =
		12331;
	
	public static final int EGL_MAX_PBUFFER_WIDTH =
		12332;
	
	public static final int EGL_NATIVE_RENDERABLE =
		12333;
	
	public static final int EGL_NATIVE_VISUAL_ID =
		12334;
	
	public static final int EGL_NATIVE_VISUAL_TYPE =
		12335;
	
	public static final int EGL_NONE =
		12344;
	
	public static final int EGL_NON_CONFORMANT_CONFIG =
		12369;
	
	public static final int EGL_NOT_INITIALIZED =
		12289;
	
	/** This represents a null context. */
	public static final EGLContext EGL_NO_CONTEXT =
		new EGLContext()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/10/10
			 */
			@Override
			public GL getGL()
			{
				// {@squirreljme.error EJ01 Cannot get the GL instance of
				// a null context.}
				throw new RuntimeException("EJ01");
			}
		};
	
	/** This represents a null display. */
	public static final EGLDisplay EGL_NO_DISPLAY =
		new EGLDisplay()
		{
		};
	
	/** This represents a null surface. */
	public static final EGLSurface EGL_NO_SURFACE =
		new EGLSurface()
		{
		};
	
	public static final int EGL_PBUFFER_BIT =
		1;
	
	public static final int EGL_PIXMAP_BIT =
		2;
	
	public static final int EGL_PRESERVED_RESOURCES =
		12336;
	
	public static final int EGL_READ =
		12378;
	
	public static final int EGL_RED_SIZE =
		12324;
	
	public static final int EGL_SAMPLES =
		12337;
	
	public static final int EGL_SAMPLE_BUFFERS =
		12338;
	
	public static final int EGL_SLOW_CONFIG =
		12368;
	
	public static final int EGL_STENCIL_SIZE =
		12326;
	
	public static final int EGL_SUCCESS =
		12288;
	
	public static final int EGL_SURFACE_TYPE =
		12339;
	
	public static final int EGL_TRANSPARENT_BLUE_VALUE =
		12341;
	
	public static final int EGL_TRANSPARENT_GREEN_VALUE =
		12342;
	
	public static final int EGL_TRANSPARENT_RED_VALUE =
		12343;
	
	public static final int EGL_TRANSPARENT_RGB =
		12370;
	
	public static final int EGL_TRANSPARENT_TYPE =
		12340;
	
	public static final int EGL_TRUE =
		1;
	
	public static final int EGL_VENDOR =
		12371;
	
	public static final int EGL_VERSION =
		12372;
	
	public static final int EGL_WIDTH =
		12375;
	
	public static final int EGL_WINDOW_BIT =
		4;
	
	public abstract boolean eglChooseConfig(EGLDisplay __disp, int[] __attrl,
		EGLConfig[] __confs, int __confssize, int[] __numconf)
		throws IllegalArgumentException;
	
	public abstract boolean eglCopyBuffers(EGLDisplay __a, EGLSurface __b, 
		Object __c);
	
	public abstract EGLContext eglCreateContext(EGLDisplay __a, EGLConfig __b
		, EGLContext __c, int[] __d);
	
	public abstract EGLSurface eglCreatePbufferSurface(EGLDisplay __a, 
		EGLConfig __b, int[] __c);
	
	public abstract EGLSurface eglCreatePixmapSurface(EGLDisplay __a, 
		EGLConfig __b, Object __c, int[] __d);
	
	public abstract EGLSurface eglCreateWindowSurface(EGLDisplay __a, 
		EGLConfig __b, Object __c, int[] __d);
	
	public abstract boolean eglDestroyContext(EGLDisplay __a, EGLContext __b
		);
	
	public abstract boolean eglDestroySurface(EGLDisplay __a, EGLSurface __b
		);
	
	public abstract boolean eglGetConfigAttrib(EGLDisplay __a, EGLConfig __b,
		int __c, int[] __d);
	
	public abstract boolean eglGetConfigs(EGLDisplay __a, EGLConfig[] __b, 
		int __c, int[] __d);
	
	public abstract EGLContext eglGetCurrentContext();
	
	public abstract EGLDisplay eglGetCurrentDisplay();
	
	public abstract EGLSurface eglGetCurrentSurface(int __a);
	
	/**
	 * This creates a connection to the given native display.
	 *
	 * If the return of this method succeeds, then the display must be
	 * initialized by calling {@link #eglInitialize(EGLDisplay, int[])}.
	 *
	 * No error code is set.
	 *
	 * @param __nd The native display object to use, in SquirrelJME this will
	 * be an instance of {@link javax.microedition.lcdui.Display}. This may
	 * be {@link #EGL_DEFAULT_DISPLAY} to use the default display.
	 * @return The OpenGL ES Display for the given native display, or
	 * {@code EGL_NO_DISPLAY} is returned on error.
	 * @throws IllegalArgumentException If {@code __nd} is {@code null} or is
	 * not compatible with the OpenGL ES backend.
	 * @since 2016/10/10
	 */
	public abstract EGLDisplay eglGetDisplay(Object __nd)
		throws IllegalArgumentException;
	
	/**
	 * Returns the last error code that was emitted from the last operation
	 * that was called.
	 *
	 * After the call, it is reset to {@link #EGL_SUCCESS}.
	 *
	 * @return The error code.
	 * @since 2016/10/11
	 */
	public abstract int eglGetError();
	
	/**
	 * Initializes the OpenGL ES display and optionally returns the version
	 * number of OpenGL ES.
	 *
	 * If a display is already initialized then the version numbers are
	 * returned.
	 *
	 * If initialization fails, {@code false} is returned and the following
	 * errors may be set.
	 *
	 * {@link #EGL_BAD_DISPLAY} if the display is not an EGL display
	 * connection.
	 * {@link #EGL_NOT_INITIALIZED} if initialization could not occur.
	 *
	 * {@link #eglTerminate(EGLDisplay)} is used to deinitialize a display
	 * and terminate the connection.
	 *
	 * @param __disp The display to initialize.
	 * @param __ver An optional array of at least length 2 where the first
	 * element is set to the major version number and the second element is set
	 * to the minor version number.
	 * @return {@code true} on success.
	 * @throws IllegalArgumentException If {@code __disp} is {@code null} or
	 * {@code __ver} is non-null and has a length lower than two.
	 * @since 2016/10/11
	 */
	public abstract boolean eglInitialize(EGLDisplay __disp, int[] __ver)
		throws IllegalArgumentException;
	
	public abstract boolean eglMakeCurrent(EGLDisplay __a, EGLSurface __b, 
		EGLSurface __c, EGLContext __d);
	
	public abstract boolean eglQueryContext(EGLDisplay __a, EGLContext __b, 
		int __c, int[] __d);
	
	public abstract String eglQueryString(EGLDisplay __a, int __b);
	
	public abstract boolean eglQuerySurface(EGLDisplay __a, EGLSurface __b, 
		int __c, int[] __d);
	
	public abstract boolean eglSwapBuffers(EGLDisplay __a, EGLSurface __b);
	
	public abstract boolean eglTerminate(EGLDisplay __a);
	
	public abstract boolean eglWaitGL();
	
	public abstract boolean eglWaitNative(int __a, Object __b);
}


