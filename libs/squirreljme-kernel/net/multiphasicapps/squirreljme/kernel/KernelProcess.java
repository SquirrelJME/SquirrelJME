// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.executable.ExecutableLoadException;

/**
 * This represents a single process within the kernel and is used to manage
 * groups of threads within the kernel.
 *
 * @since 2016/11/08
 */
public abstract class KernelProcess
	implements AutoCloseable
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** The class path for this process. */
	private final SuiteDataAccessor[] _classpath;
	
	/** Threads owned by this process. */
	private final List<KernelThread> _threads =
		new ArrayList<>();
	
	/** System properties which have been set for the process. */
	private final Map<String, String> _properties =
		new HashMap<>();
	
	/** Loaded context classes. */
	private final Map<String, ContextClass> _classes =
		new HashMap<>();
	
	/**
	 * Initializes the process.
	 *
	 * @param __k The kernel owning the process.
	 * @param __cp The class path used for user space processes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	protected KernelProcess(Kernel __k, SuiteDataAccessor[] __cp)
		throws NullPointerException
	{
		// Check
		if (__k == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		
		// Copy the class path
		__cp = __cp.clone();
		for (SuiteDataAccessor sda : __cp)
			if (sda == null)
				throw new NullPointerException("NARG");
		this._classpath = __cp;
	}
	
	/**
	 * Loads the given class into an executable context which is dependent on
	 * the system.
	 *
	 * @param __e The class to load.
	 * @return The context for the given class.
	 * @throws ContextLoadException If the class could not be loaded.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/16
	 */
	protected abstract ContextClass initializeClassContext(ExecutableClass __e)
		throws ContextLoadException, NullPointerException;
	
	/**
	 * Creates a new thread which is owned by this process.
	 *
	 * @param __mc The main class
	 * @param __mm The main method.
	 * @return The new thread owned by this process.
	 * @throws NullPointerException On null arguments.
	 * @throws ThreadCreationException If the thread could not be created.
	 * @since 2017/01/16
	 */
	public final KernelThread createThread(String __mc, String __mm)
		throws NullPointerException, ThreadCreationException
	{
		// Check
		if (__mc == null || __mm == null)
			throw new NullPointerException("NARG");
		
		// Create it
		KernelThread rv = null;
		try
		{
			// Create thread
			Kernel kernel = this.kernel;
			rv = kernel.__createThread(this, __mc, __mm);
			
			// {@squirreljme.error BH05 A null thread was returned, treating
			// as failure.}
			if (rv == null)
				throw new ThreadCreationException("BH05");
			
			// {@squirreljme.error BH06 The created thread was reassigned to
			// another kernel or process during construction.}
			if (rv.kernel() != kernel || rv.process() != this)
				throw new ThreadCreationException("BH06");
			
			// Add to threads managed by this process
			List<KernelThread> threads = this._threads;
			synchronized (threads)
			{
				threads.add(rv);
			}
			
			// Return it
			return rv;
		}
		
		// Failed to create the process
		catch (RuntimeException|Error e)
		{
			// Destroy the thread if it was created
			if (rv != null)
				try
				{
					rv.close();
				}
				catch (Throwable t)
				{
					e.addSuppressed(e);
				}
			
			// Fail
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/08
	 */
	@Override
	public final void close()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the kernel which owns this process.
	 *
	 * @return The owning kernel.
	 * @since 2017/01/16
	 */
	public final Kernel kernel()
	{
		return this.kernel;
	}
	
	/** 
	 * Loads a class in the context of this process.
	 *
	 * @param __name The name of the class to load.
	 * @return The loaded class
	 * @throws ContextLoadException If the class could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/16
	 */
	public final ContextClass loadClass(String __name)
		throws ContextLoadException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Lock on classes
		Map<String, ContextClass> classes = this._classes;
		synchronized (classes)
		{
			// If the class has already been loaded use it
			ContextClass rv = classes.get(__name);
			if (rv != null)
				return rv;
			
			// It can fail
			try
			{
				// Locate class
				ExecutableClass ec = loadExecutableClass(__name);
				
				// Load into context, which is platform dependent
				rv = initializeClassContext(ec);
			}
			
			// {@squirreljme.error BH09 Could not load a context of the
			// specified class. (The class to load)}
			catch (ExecutableLoadException e)
			{
				throw new ContextLoadException(String.format("BH09 %s",
					__name), e);
			}
			
			// Store it and return it
			classes.put(__name, rv);
			return rv;
		}
	}
	
	/**
	 * Locates and loads the specified executable class with the given name.
	 *
	 * {@squirreljme.error BH0b Could not locate or load the specified class.
	 * (The name of the class)}
	 *
	 * @param __name The name of the class to find.
	 * @return The class to be read.
	 * @throws ExecutableLoadException If the class does not exist or is
	 * not correctly formatted.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/16
	 */
	public final ExecutableClass loadExecutableClass(String __name)
		throws ExecutableLoadException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// May occur
		ExecutableLoadException fail = null;
		
		// Check the system class path, it has higher priority and its classes
		// cannot be replaced
		for (SuiteDataAccessor sda : this.kernel.suiteManager().systemSuites())
			try
			{
				return sda.loadClass(__name);
			}
		
			// Failed
			catch (ExecutableLoadException e)
			{
				// Generate
				if (fail == null)
					fail = new ExecutableLoadException(String.format("BH0b %s",
						__name));
				fail.addSuppressed(e);
			}
		
		// Go through the class path to find the class
		for (SuiteDataAccessor sda : this._classpath)
			try
			{
				return sda.loadClass(__name);
			}
			
			// Could not load, set last load failure
			catch (ExecutableLoadException e)
			{
				// Generate
				if (fail == null)
					fail = new ExecutableLoadException(String.format("BH0b %s",
						__name));
				fail.addSuppressed(e);
			}
		
		// An exception would have been generated.
		throw fail;
	}
	
	/**
	 * Sets the value of the given system property.
	 *
	 * @param __k The key to set.
	 * @param __v The value to use for it, {@code null} clears the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/16
	 */
	public final void setProperty(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Lock on properties
		Map<String, String> properties = this._properties;
		synchronized (properties)
		{
			properties.put(__k, __v);
		}
	}
}

