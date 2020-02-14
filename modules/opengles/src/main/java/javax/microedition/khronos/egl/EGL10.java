// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
	/**
	 * The minimum size in bits that the alpha buffer/channel must be.
	 *
	 * A value of zero means to prefer the smallest buffer, while a positive
	 * value selects the highest available buffer size.
	 *
	 * Default 0.
	 */
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
	
	/** This is an error specifying that the given display is not valid. */
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
	
	/**
	 * The minimum size in bits that the blue buffer/channel must be.
	 *
	 * A value of zero means to prefer the smallest buffer, while a positive
	 * value selects the highest available buffer size.
	 *
	 * Default 0.
	 */
	public static final int EGL_BLUE_SIZE =
		12322;
	
	/**
	 * Attribute for the minimum desired size of the color buffer.
	 *
	 * Default 0.
	 */
	public static final int EGL_BUFFER_SIZE =
		12320;
	
	/**
	 * Configurations may have caveats, as such this allows conifgurations
	 * with caveats to be selected.
	 *
	 * May be either {@link #EGL_SLOW_CONFIG}, {@link #EGL_NONE} to use one
	 * with no caveats, {@link #EGL_NON_CONFORMANT_CONFIG}, or
	 * {@link #EGL_DONT_CARE} if it does not matter.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_CONFIG_CAVEAT =
		12327;
	
	/**
	 * A precomposed OpenGL ES configuration to choose, if this is specified
	 * then all other attributes are ignored. The value is implementation
	 * dependent.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_CONFIG_ID =
		12328;
	
	public static final int EGL_CORE_NATIVE_ENGINE =
		12379;
	
	/** This is used to signify that the default display should be used. */
	public static final Object EGL_DEFAULT_DISPLAY =
		new Object();
	
	/**
	 * The minimum size in bits that the depth buffer must be.
	 *
	 * A value of zero means to not use this buffer.
	 *
	 * Default 0.
	 */
	public static final int EGL_DEPTH_SIZE =
		12325;
	
	public static final int EGL_DONT_CARE =
		-1;
	
	public static final int EGL_DRAW =
		12377;
	
	/**
	 * A value for {@link #eglQueryString(EGLDisplay, int)} that requests the
	 * extensions that are available to this OpenGL ES implementation.
	 */
	public static final int EGL_EXTENSIONS =
		12373;
	
	public static final int EGL_FALSE =
		0;
	
	/**
	 * The minimum size in bits that the green buffer/channel must be.
	 *
	 * A value of zero means to prefer the smallest buffer, while a positive
	 * value selects the highest available buffer size.
	 *
	 * Default 0.
	 */
	public static final int EGL_GREEN_SIZE =
		12323;
	
	public static final int EGL_HEIGHT =
		12374;
	
	public static final int EGL_LARGEST_PBUFFER =
		12376;
	
	/**
	 * The exact number of buffer-levels to use, positive values mean the given
	 * number of overlay buffers and negative values are mapped to underlay
	 * buffers. Level zero is the default framebuffer of the display.
	 *
	 * Default 0.
	 */
	public static final int EGL_LEVEL =
		12329;
	
	public static final int EGL_MAX_PBUFFER_HEIGHT =
		12330;
	
	public static final int EGL_MAX_PBUFFER_PIXELS =
		12331;
	
	public static final int EGL_MAX_PBUFFER_WIDTH =
		12332;
	
	/**
	 * Specifies configurations that allow native rendering onto the surface
	 * or not. {@link #EGL_DONT_CARE} can choose any, {@link #EGL_TRUE}
	 * chooses configurations that allow native rendering, and
	 * {@link #EGL_FALSE} choose configurations that disallow native
	 * rendering.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_NATIVE_RENDERABLE =
		12333;
	
	public static final int EGL_NATIVE_VISUAL_ID =
		12334;
	
	/**
	 * A platform specified value or {@link #EGL_DONT_CARE}.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_NATIVE_VISUAL_TYPE =
		12335;
	
	public static final int EGL_NONE =
		12344;
	
	public static final int EGL_NON_CONFORMANT_CONFIG =
		12369;
	
	/** An error specifying that the display has not been initialized. */
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
				// {@squirreljme.error EJ0a Cannot get the GL instance of
				// a null context.}
				throw new RuntimeException("EJ0a");
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
	
	/**
	 * Specifies that the surface is capable of creating pixel buffer
	 * surfaces.
	 */
	public static final int EGL_PBUFFER_BIT =
		1;
	
	/** Specifies that the surface is capable of creating pixmap surfaces. */
	public static final int EGL_PIXMAP_BIT =
		2;
	
	public static final int EGL_PRESERVED_RESOURCES =
		12336;
	
	public static final int EGL_READ =
		12378;
	
	/**
	 * The minimum size in bits that the red buffer/channel must be.
	 *
	 * A value of zero means to prefer the smallest buffer, while a positive
	 * value selects the highest available buffer size.
	 *
	 * Default 0.
	 */
	public static final int EGL_RED_SIZE =
		12324;
	
	/**
	 * The minimum number of samples needed in the sample buffers.
	 *
	 * Default unspecified.
	 */
	public static final int EGL_SAMPLES =
		12337;
	
	/**
	 * The minimum number of acceptable multi-sample buffers. Configurations
	 * that are closer to this value are preferred.
	 *
	 * Default 0.
	 */
	public static final int EGL_SAMPLE_BUFFERS =
		12338;
	
	public static final int EGL_SLOW_CONFIG =
		12368;
	
	/**
	 * The minimum size in bits that the stencil buffer must be.
	 *
	 * A value of zero means to not use this buffer.
	 *
	 * Default 0.
	 */
	public static final int EGL_STENCIL_SIZE =
		12326;
	
	public static final int EGL_SUCCESS =
		12288;
	
	/**
	 * Represents the type of surface to use, this is a bitmask. The valid
	 * bits are {@link #EGL_WINDOW_BIT}, {@link #EGL_PBUFFER_BIT}, and
	 * {@link #EGL_PIXMAP_BIT}.
	 *
	 * Default {@link #EGL_WINDOW_BIT}.
	 */
	public static final int EGL_SURFACE_TYPE =
		12339;
	
	/**
	 * The transparent blue channel value to use. The value must be between
	 * {@code 0} and the maximum range for the given channel. Only
	 * configurations that use this specific color for transparent values will
	 * be considered.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_TRANSPARENT_BLUE_VALUE =
		12341;
	
	/**
	 * The transparent green channel value to use. The value must be between
	 * {@code 0} and the maximum range for the given channel. Only
	 * configurations that use this specific color for transparent values will
	 * be considered.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_TRANSPARENT_GREEN_VALUE =
		12342;
	
	/**
	 * The transparent red channel value to use. The value must be between
	 * {@code 0} and the maximum range for the given channel. Only
	 * configurations that use this specific color for transparent values will
	 * be considered.
	 *
	 * Default {@link #EGL_DONT_CARE}.
	 */
	public static final int EGL_TRANSPARENT_RED_VALUE =
		12343;
	
	public static final int EGL_TRANSPARENT_RGB =
		12370;
	
	/**
	 * The type of transparent framebuffer to use.
	 *
	 * May be {@link #EGL_NONE} then opaque framebuffers are used, otherwise
	 * {@link #EGL_TRANSPARENT_RGB} specifies that transparent framebuffers
	 * are used.
	 *
	 * Transparent framebuffers might not be supported.
	 *
	 * Default {@link #EGL_NONE}.
	 */
	public static final int EGL_TRANSPARENT_TYPE =
		12340;
	
	public static final int EGL_TRUE =
		1;
	
	/**
	 * A value for {@link #eglQueryString(EGLDisplay, int)} that requests the
	 * OpenGL ES vendor.
	 */
	public static final int EGL_VENDOR =
		12371;
	
	/**
	 * A value for {@link #eglQueryString(EGLDisplay, int)} that requests the
	 * OpenGL ES version in the form of {@code major.minor vendor-specific}.
	 */
	public static final int EGL_VERSION =
		12372;
	
	public static final int EGL_WIDTH =
		12375;
	
	/** Supports creation of window surfaces. */
	public static final int EGL_WINDOW_BIT =
		4;
	
	/**
	 * This returns an array of framebuffer configurations that match the
	 * given input attributes.
	 *
	 * This method attempts to match all of the attributes that were specified.
	 * All attributes start with a key and is followed by a value.
	 *
	 * If the attribute list is {@code null} or has {@link #EGL_NONE} as the
	 * first entry then all defaults are chosen and default selection is
	 * performed.
	 *
	 * Some values must match exactly while others may be bound to be at least
	 * or at most the given value.
	 *
	 * The possible values are:
	 * {@link #EGL_BUFFER_SIZE},
	 * {@link #EGL_RED_SIZE},
	 * {@link #EGL_GREEN_SIZE},
	 * {@link #EGL_BLUE_SIZE},
	 * {@link #EGL_CONFIG_CAVEAT},
	 * {@link #EGL_CONFIG_ID},
	 * {@link #EGL_DEPTH_SIZE},
	 * {@link #EGL_LEVEL},
	 * {@link #EGL_NATIVE_RENDERABLE},
	 * {@link #EGL_NATIVE_VISUAL_TYPE},
	 * {@link #EGL_SAMPLE_BUFFERS},
	 * {@link #EGL_STENCIL_SIZE},
	 * {@link #EGL_SURFACE_TYPE},
	 * {@link #EGL_TRANSPARENT_TYPE},
	 * {@link #EGL_TRANSPARENT_RED_VALUE},
	 * {@link #EGL_TRANSPARENT_GREEN_VALUE},
	 * {@link #EGL_TRANSPARENT_BLUE_VALUE},
	 * {@link EGL11#EGL_BIND_TO_TEXTURE_RGB},
	 * {@link EGL11#EGL_BING_TO_TEXTURE_RGBA},
	 * {@link EGL11#EGL_MAX_SWAP_INTERVAL},
	 * {@link EGL11#EGL_MIN_SWAP_INTERVAL}
	 *
	 * Conifgurations are sorted in the following order:
	 *
	 * 1. {@link #EGL_CONFIG_CAVEAT} with the order: {@link #EGL_NONE},
	 *    {@link #EGL_SLOW_CONFIG}, then {@link #EGL_NON_CONFORMANT_CONFIG}.
	 * 2. The number of bits for the red, green, blue, and alpha channels,
	 *    higher is better. If the value is zero or {@link #EGL_DONT_CARE} then
	 *    it is not considered.
	 * 3. Smaller {@link #EGL_BUFFER_SIZE}.
	 * 4. Smaller {@link #EGL_SAMPLE_BUFFERS}.
	 * 5. Smaller {@link #EGL_DEPTH_SIZE}.
	 * 6. Smaller {@link #EGL_STENCIL_SIZE}.
	 * 7. Platform dependent {@link #EGL_NATIVE_VISUAL_TYPE}.
	 * 8. Smaller {@link EGL_CONFIG_ID}.
	 * 9. Other attributes are not considered.
	 *
	 * The following errors are set:
	 *
	 * {@link #EGL_BAD_DISPLAY} if the display is not valid.
	 * {@link #EGL_BAD_ATTRIBUTE} if an attribute is not valid.
	 * {@link #EGL_NOT_INITIALIZED} if the display is not initialized.
	 * {@link #EGL_BAD_PARAMETER} if {@code __numconf} is {@code null}.
	 *
	 * @param __disp The display to get a configuration for.
	 * @param __attrl The attributes to return a matching configuration for,
	 * must end with {@link #EGL_NONE}.
	 * @param __confs The output configuration array, if {@code null} then
	 * this returns the number of matching configurations.
	 * @param __confssize The number of configurations to write at the most.
	 * @param __numconf The number of configurations that were placed in the
	 * output configuration array.
	 * @return {@code true} if a configuration was found and no errors were
	 * generated. If {@code false} then the input arguments are not modified.
	 * @throws IllegalArgumentException If no display was specified; If the
	 * attribute list is not terminated with {@link #EGL_NONE}; If the
	 * configurations were not null and the array length is shorter than the
	 * specified size; The number of configurations is not null and the length
	 * is zero.
	 * @since 2016/10/11
	 */
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
	
	/**
	 * Queries an implementation specific string from the specified display.
	 *
	 * Valid keys are: {@link #EGL_VENDOR}, {@link #EGL_VERSION}, and
	 * {@link #EGL_EXTENSIONS}.
	 *
	 * The following errors may be set:
	 *
	 * {@link #EGL_BAD_DISPLAY} if the display is not valid.
	 * {@link #EGL_NOT_INITIALIZED} if the display is not initialized.
	 * {@link #EGL_BAD_PARAMETER} if the name is not a valid value.
	 *
	 * @param __disp The display to query.
	 * @param __key The key value to obtain.
	 * @return The string value for the given display and variable or 
	 * {@code null} on failure.
	 * @throws IllegalArgumentException If {@code __disp} is {@code null}.
	 * @since 2016/10/11
	 */
	public abstract String eglQueryString(EGLDisplay __disp, int __key)
		throws IllegalArgumentException;
	
	public abstract boolean eglQuerySurface(EGLDisplay __a, EGLSurface __b, 
		int __c, int[] __d);
	
	public abstract boolean eglSwapBuffers(EGLDisplay __a, EGLSurface __b);
	
	public abstract boolean eglTerminate(EGLDisplay __a);
	
	public abstract boolean eglWaitGL();
	
	public abstract boolean eglWaitNative(int __a, Object __b);
}


