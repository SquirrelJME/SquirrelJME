// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.IllegalSymbolException;

/**
 * This class acts as the interpreter engine.
 *
 * This class is also abstract because it is also intended that the classes
 * which extend off this provide a means to locate classes and perform some
 * file system details. So in short, this is essentially a virtual machine.
 *
 * This engine only supports JavaME 8 and may not be fully capable of running
 * Java SE code (it does not support invokedynamic or reflection).
 *
 * @since 2016/03/01
 */
public abstract class JVMEngine
{
	/** The maximum number of dimensions an array may have. */
	public static final int MAX_ARRAY_DIMENSIONS =
		255;
	
	/** The threads the interpreter owns (lock on this). */
	protected final Set<JVMThread> threads =
		new LinkedHashSet<>();
	
	/** Active loaded classes (lock on this). */
	protected final Map<String, Reference<JVMClass>> classes =
		new LinkedHashMap<>();
	
	/** Classpaths for the interpreter (how it finds classes). */
	protected final Set<JVMClassPath> classpaths =
		new LinkedHashSet<>();
	
	/** Partially loaded classes. */
	private final Set<JVMClass> _partialclasses =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the base engine.
	 *
	 * @since 2016/02/01
	 */
	public JVMEngine()
	{
	}
	
	/**
	 * This creates a program output which is used to store generated machine
	 * code for interpretation, execution, or compilation.
	 *
	 * @return The program output.
	 * @since 2016/03/24
	 */
	protected abstract JVMProgramOutput createProgramOutput();
	
	/**
	 * Adds a class path to the engine.
	 *
	 * @param __icp The class path to add.
	 * @return {@code this}.
	 * @throws IllegalArgumentExcepton If another engine owns this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	protected final JVMEngine addClassPath(JVMClassPath __icp)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__icp == null)
			throw new NullPointerException("NARG");
		
		// Differing owner?
		if (__icp.engine() != this)
			throw new IllegalArgumentException("IN0k");
		
		// Lock on the class paths
		synchronized (classpaths)
		{
			// Add it
			classpaths.add(__icp);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Creates a new a thread.
	 *
	 * @param __meth The method to start execution at.
	 * @param __args Thread arguments, these take either boxed types or
	 * {@code JVMObject}, all other classes are illegal.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final JVMThread createThread(JVMMethod __meth,
		Object... __args)
		throws NullPointerException
	{
		// Check
		if (__meth == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if the intepreter has no threads remaining which
	 * are alive and executing (they have all exited).
	 *
	 * @return {@code true} if no living threads remain, otherwise
	 * {@code false}.
	 * @since 2016/03/01
	 */
	public final boolean isTerminated()
	{
		// Lock on threads
		synchronized (threads)
		{
			// Only if no threads remain
			return threads.isEmpty();
		}
	}
	
	/**
	 * Attempts to load a class with the given binary name, the input class
	 * may also be an array for which one will be generated.
	 *
	 * @param __bn The name of the class to load.
	 * @return The loaded class or {@code null} if no such class exists.
	 * @throws IllegalArgumentException If the given name is not a valid class
	 * name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final JVMClass loadClass(String __bn)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Requesting an array class?
		int dims;
		if (__bn.startsWith("["))
			throw new Error("TODO");
		
		// A normal class
		else
		{
			// Not an array
			dims = 0;
			
			// The name cannot contain /, ;, or [
			if (__bn.indexOf('/') >= 0 || __bn.indexOf(';') >= 0 ||
				__bn.indexOf('[') >= 0)
				throw new IllegalArgumentException(String.format("IN0l %s",
					__bn));
		}
		
		// Lock on the class map
		synchronized (classes)
		{
			// See if an existing reference exists
			Reference<JVMClass> ref = classes.get(__bn);
			JVMClass rv;
			
			// Reference is still valid?
			if (ref == null || null == (rv = ref.get()))
				classes.put(__bn, new WeakReference<>((rv =
					__internalLoadClass(__bn, dims))));
			
			// Return the read class
			return rv;
		}
	}
	
	/**
	 * Returns a class which may be partially loaded by the interpreter engine.
	 *
	 * @param __bn The name of the partially loaded class.
	 * @return The partially loaded class or a fully loaded class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/26
	 */
	public final JVMClass partialClass(String __bn)
		throws NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Lock on classes
		synchronized (classes)
		{
			// Check partials for the class
			for (JVMClass pc : _partialclasses)
				try
				{
					// Get the class loader name
					String clname = pc.getClassLoaderName();
					
					// Is this name?
					if (Objects.equals(clname, __bn))
						return pc;
				}
				
				// No name was set yet
				catch (IllegalStateException ise)
				{
				}
			
			// Not in the partial list, just load it
			return loadClass(__bn);
		}
	}
	
	/**
	 * Creates a string array with the given strings.
	 *
	 * @param __strings Strings to place in the array.
	 * @return The string array as seen by the program running in the
	 * interpreter.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final JVMObject spawnStringArray(String... __strings)
		throws NullPointerException
	{
		// Check
		if (__strings == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Provides access to create outputs for {@link JVMCodeParser}.
	 *
	 * @return The created program output.
	 * @since 2016/03/24
	 */
	final JVMProgramOutput __createProgramOutput()
	{
		return createProgramOutput();
	}
	
	/**
	 * This creates an array of the given type of the specified dimensional
	 * count.
	 *
	 * @param __bn The binary name of the array type.
	 * @param __dims The number of dimensions in the array.
	 * @throws JVMEngineException If the number of dimensions is not legal.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	private final JVMClass __internalLoadArray(String __bn, int __dims)
		throws JVMEngineException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		if (__dims <= 0 || __dims >= MAX_ARRAY_DIMENSIONS)
			throw new JVMEngineException(String.format("IN0m %d", __dims));
		
		throw new Error("TODO");
	}
	
	/**
	 * This is the internal class loading logic.
	 *
	 * @param __bn The binary name of the class.
	 * @param __dims The number of dimensions in the array.
	 * @return The loaded class or {@code null} if it does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	private final JVMClass __internalLoadClass(String __bn, int __dims)
		throws NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Array?
		if (__dims != 0)
			return __internalLoadArray(__bn, __dims);
		
		// Lock on the classpath
		synchronized (classpaths)
		{
			// Go through all classpaths
			JVMClass part = null;
			for (JVMClassPath icp : classpaths)
				try (InputStream is = icp.getResourceAsStream(
						__bn.replace('.', '/') + ".class"))
				{
					// No class found
					if (is == null)
						continue;
					
					// Create class data (from files)
					JVMClass rv = part = new JVMClass(this);
					
					// Add to partial class list
					_partialclasses.add(rv);
					
					// Load in class data
					new JVMClassFile(this, rv).parse(is);
					
					// Wrong class? Ignore it
					String xn;
					if (!__bn.equals((xn = rv.getClassLoaderName())))
						throw new JVMClassFormatError(String.format(
							"IN0n %s %s", __bn, xn));
					
					// Return it
					return rv;
				}
				
				// Failed to load class, ignore
				catch (IOException|IllegalSymbolException|
					JVMClassFormatError e)
				{
					throw new JVMClassFormatError(String.format("IN0o %s",
						__bn), e);
				}
				
				// Loading either worked, or failed so remove it from the
				// partial class list
				finally
				{
					if (part != null)
						_partialclasses.remove(part);
				}
		}
		
		// A class was not loaded
		return null;
	}
}

