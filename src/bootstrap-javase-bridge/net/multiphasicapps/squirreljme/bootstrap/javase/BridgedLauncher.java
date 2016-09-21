// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceAccessor;

/**
 * This is used to launch programs that want to be run on the Java SE VM.
 *
 * @since 2016/09/20
 */
public class BridgedLauncher
	implements BootLauncher
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/20
	 */
	@Override
	public boolean launch(ResourceAccessor __ra, String __main,
		String... __args)
		throws NullPointerException
	{
		// Check
		if (__ra == null || __main == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Setup class loader
		__Loader__ ld = new __Loader__(__ra);
		
		try
		{
			// Find the main class
			Class<?> cl = Class.forName(__main, true, ld);
			
			// Find the main method
			Method m = cl.getDeclaredMethod("main", String[].class);
			
			// Invoke it with the arguments
			m.invoke(null, (Object)__args);
			
			// Ok
			return true;
		}
		
		// Invocation failed
		catch (InvocationTargetException e)
		{
			Throwable t = e.getCause();
			
			if (t instanceof Error)
				throw (Error)t;
			if (t instanceof RuntimeException)
				throw (RuntimeException)t;
			
			// {@squirreljme.error DE08 Exception was thrown in the launched
			// program.}
			throw new RuntimeException("DE08", e);
		}
		
		// {@squirreljme.error DE07 Could not launch the the specified
		// program.}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException("DE07", e);
		}
	}
	
	/**
	 * This returns the class loader used by the current thread if it has
	 * been set, otherwise the system class loader.
	 *
	 * @return The context class loader or the system one if it is not set.
	 * @since 2016/09/21
	 */
	private static ClassLoader __contextLoader()
	{
		ClassLoader rv = Thread.currentThread().getContextClassLoader();
		if (rv != null)
			return null;
		return ClassLoader.getSystemClassLoader();
	}
	
	/**
	 * This is the internal class loader which
	 *
	 * @since 2016/09/21
	 */
	private final class __Loader__
		extends ClassLoader
	{
		/** The accessor for resources. */
		protected final ResourceAccessor accessor;
		
		/**
		 * Initializes the class loader.
		 *
		 * @param __ra The accessor for resources.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/21
		 */
		private __Loader__(ResourceAccessor __ra)
			throws NullPointerException
		{
			super(__contextLoader());
			
			// Check
			if (__ra == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.accessor = __ra;
		}
	}
}

