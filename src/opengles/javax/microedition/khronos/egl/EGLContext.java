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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.khronos.opengles.GL;

/**
 * This class encapsulates the OpenGL context which is used to obtain instances
 * of OpenGL ES and OpenGL rasterizers.
 *
 * To create a context use {@link EGL10#eglCreateContext(EGLDisplay, EGLConfig,
 * EGLContext, int[])} method.
 *
 * To destroy a context use {@link EGL10#eglDestroyContext(EGLDisplay,
 * EGLContext}.
 *
 * @since 2016/10/10
 */
public abstract class EGLContext
{
	/** The single instance of EGL, shared by everything. */
	private static volatile Reference<EGL> _EGL;
	
	EGLContext()
	{
		super();
		throw new Error("TODO");
	}
	
	public abstract GL getGL();
	
	/**
	 * This returns an instance of an object that implements the {@link EGL}
	 * interface which is used to initialize the rasterizer for drawing.
	 *
	 * @return An instance of the OpenGL ES rasterizer.
	 * @since 2016/10/10
	 */
	public static EGL getEGL()
	{
		// Get
		Reference<EGL> ref = EGLContext._EGL;
		EGL rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			EGLContext._EGL = new WeakReference<>((rv = new __EGL__()));
		
		// Return it
		return rv;
	}
}


