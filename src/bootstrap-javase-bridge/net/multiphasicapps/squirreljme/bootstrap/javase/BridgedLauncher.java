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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
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
		
		// Change context
		Thread.currentThread().setContextClassLoader(ld);
		
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
	 * Converts a class loader name to a file name.
	 *
	 * @param __n The class loader name to convert.
	 * @return The file name for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/21
	 */
	private static String __toFileName(String __n)
		throws NullPointerException
	{
		// Replace dots with slashes
		StringBuilder sb = new StringBuilder(__n);
		int n = sb.length();
		for (int i = 0; i < n; i++)
			if (sb.charAt(i) == '.')
				sb.setCharAt(i, '/');
		
		// Add extension
		sb.append(".class");
		
		// Build it
		return sb.toString();
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
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/21
		 */
		@Override
		protected Class<?> findClass(String __n)
			throws ClassNotFoundException, NullPointerException
		{
			// Check
			if (__n == null)
				throw new NullPointerException("NARG");
			
			// Try to open the resource
			try (InputStream is = this.accessor.open(__toFileName(__n)))
			{
				// {@squirreljme.error DE09 The specified class could not
				// be found in the resource data. (The missing class)}
				if (is == null)
					throw new ClassNotFoundException(String.format("DE09 %s",
						__n));
				
				// Load all of the class data
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
				{
					// Copy the data
					byte[] buf = new byte[512];
					for (;;)
					{
						int rc = is.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						// Copy
						baos.write(buf, 0, rc);
					}
					
					// Flush 
					baos.flush();
					
					// Define the class data
					byte[] data = baos.toByteArray();
					return super.defineClass(__n, data, 0, data.length);
				}
			}
			
			// {@squirreljme.error DE0a Failed to read the specified
			// class file. (The class name)}
			catch (IOException e)
			{
				throw new LinkageError(String.format("DE0a %s", __n), e);
			}
		}
	}
}

