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
import net.multiphasicapps.squirreljme.compat.AccessChange;
import net.multiphasicapps.squirreljme.compat.AccessType;
import net.multiphasicapps.squirreljme.opengles.DefaultEGL;

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
 * For compatibility with the standard API this class should not be extended,
 * unless the class extending is internal to SquirrelJME.
 *
 * @since 2016/10/10
 */
@AccessChange(from=AccessType.PRIVATE, to=AccessType.PUBLIC,
	reason="Package encapsulation.")
public abstract class EGLContext
{
	/** The single instance of EGL, shared by everything. */
	private static volatile Reference<EGL> _EGL;
	
	/**
	 * Returns an instance of an object which is used to provide access to
	 * the underlying OpenGL ES rasterization API.
	 *
	 * @return The OpenGL ES rasterization API accessor.
	 * @since 2016/10/10
	 */
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
			EGLContext._EGL = new WeakReference<>((rv = new DefaultEGL()));
		
		// Return it
		return rv;
	}
}


