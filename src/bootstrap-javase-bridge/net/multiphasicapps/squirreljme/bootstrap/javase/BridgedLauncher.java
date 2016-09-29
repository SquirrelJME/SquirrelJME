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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.CaughtException;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceAccessor;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceReference;

/**
 * This is used to launch programs that want to be run on the Java SE VM.
 *
 * @since 2016/09/20
 */
public class BridgedLauncher
	implements BootLauncher
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Completion status. */
	private volatile boolean _ok;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/20
	 */
	@Override
	public Thread launch(ResourceAccessor __ra, final CaughtException __ce,
		final String __main, final String... __args)
		throws NullPointerException
	{
		// Check
		if (__ra == null || __ce == null || __main == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Setup class loader
		final __Loader__ ld = new __Loader__(__ra);
		
		// Change context
		Thread.currentThread().setContextClassLoader(ld);
		
		// Group any threads together and any threads launched by running
		// programs (Java ME lacks ThreadGroup so they will never set it)
		ThreadGroup tg = new ThreadGroup("squirreljme-launch");
		
		// Setup thread
		Thread t = new Thread(tg, new Runnable()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/09/21
				 */
				public void run()
				{
					try
					{
						// Find the main class
						Class<?> cl = Class.forName(__main, true, ld);
			
						// Find the main method
						Method m = cl.getDeclaredMethod("main", String[].class);
			
						// Invoke it with the arguments
						m.invoke(null, (Object)__args);
						
						// Set OK
						synchronized (BridgedLauncher.this.lock)
						{
							BridgedLauncher.this._ok = true;
						}
					}
		
					// Invocation failed
					catch (InvocationTargetException e)
					{
						Throwable t = e.getCause();
						
						// Mark it
						__ce.throwable = t;
						
						// Rethrow potentially
						if (t instanceof Error)
							throw (Error)t;
						if (t instanceof RuntimeException)
							throw (RuntimeException)t;
			
						// {@squirreljme.error DE08 Exception was thrown in the
						// launched program.}
						throw new RuntimeException("DE08", e);
					}
		
					// {@squirreljme.error DE07 Could not launch the the
					// specified program.}
					catch (ReflectiveOperationException e)
					{
						// Mark it
						__ce.throwable = e;
						
						// Wrap
						throw new RuntimeException("DE07", e);
					}
				}
			}, "squirreljme-bootstrap");
		
		// Return the created thread
		return t;
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
	 * Wraps the resource reference as an URL.
	 *
	 * @param __ref The resource reference.
	 * @param __n The name of the resource.
	 * @return The URL to the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/21
	 */
	private static URL __url(final ResourceReference __ref, String __n)
		throws NullPointerException
	{
		// Check
		if (__ref == null || __n == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new URL("squirreljme", null, 0, __n, new URLStreamHandler()
				{
					/**
					 * {@inheritDoc}
					 * @since 2016/09/21
					 */
					@Override
					protected URLConnection openConnection(URL __u)
						throws IOException
					{
						return new URLConnection(__u)
							{
								/**
								 * {@inheritDoc}
								 * @since 2016/09/21
								 */
								@Override
								public void connect()
								{
								}
							
								/**
								 * {@inheritDoc}
								 * @since 2016/09/21
								 */
								@Override
								public InputStream getInputStream()
									throws IOException
								{
									return __ref.open();
								}
							};
					}
				});
		}
		
		// {@squirreljme.error DE0b Could not wrap the resource.}
		catch (IOException e)
		{
			throw new RuntimeException("DE0b", e);
		}
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
			
			// if these occur then print the exception
			catch (RuntimeException|Error e)
			{
				// Print it
				e.printStackTrace();
				
				// Rethrow
				throw e;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/21
		 */
		@Override
		protected URL findResource(String __n)
		{
			// Return the first resource that is found
			for (ResourceReference rv : this.accessor.reference(__n))
				return __url(rv, __n);
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/21
		 */
		@Override
		protected Enumeration<URL> findResources(String __n)
		{
			// Setup
			List<URL> rv = new ArrayList<>();
			
			// Go through all resources
			for (ResourceReference ref : this.accessor.reference(__n))
				rv.add(__url(ref, __n));
			
			// Enumerate
			return Collections.<URL>enumeration(rv);
		}
	}
}

