// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;

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
public abstract class InterpreterEngine
{
	/** The maximum number of dimensions an array may have. */
	public static final int MAX_ARRAY_DIMENSIONS =
		255;
	
	/** The threads the interpreter owns (lock on this). */
	protected final Set<InterpreterThread> threads =
		new LinkedHashSet<>();
	
	/** Active loaded classes (lock on this). */
	protected final Map<String, Reference<InterpreterClass>> classes =
		new LinkedHashMap<>();
	
	/** Classpaths for the interpreter (how it finds classes). */
	protected final Set<InterpreterClassPath> classpaths =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the base engine.
	 *
	 * @since 2016/02/01
	 */
	public InterpreterEngine()
	{
	}
	
	/**
	 *
	 *
	 * @param __icp The class path to add.
	 * @return {@code this}.
	 * @throws MismatchedEngineException If another engine owns this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	protected final InterpreterEngine addClassPath(InterpreterClassPath __icp)
		throws MismatchedEngineException, NullPointerException
	{
		// Check
		if (__icp == null)
			throw new NullPointerException();
		
		// Differing owner?
		if (__icp.engine() != this)
			throw new MismatchedEngineException();
		
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
	 * {@code InterpreterObject}, all other classes are illegal.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final InterpreterThread createThread(InterpreterMethod __meth,
		Object... __args)
		throws NullPointerException
	{
		// Check
		if (__meth == null)
			throw new NullPointerException();
		
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
	public final InterpreterClass loadClass(String __bn)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException();
		
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
				throw new IllegalArgumentException("The class '" + __bn +
					"' is not a valid class name.");
		}
		
		// Lock on the class map
		synchronized (classes)
		{
			// See if an existing reference exists
			Reference<InterpreterClass> ref = classes.get(__bn);
			InterpreterClass rv = null;
			
			// Reference is still valid?
			if (ref != null)
				rv = ref.get();
			
			// Class needs to be read in
			if (rv == null)
				classes.put(__bn, new WeakReference<>((rv =
					__internalLoadClass(__bn, dims))));
			
			// Return the read class
			return rv;
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
	public final InterpreterObject spawnStringArray(String... __strings)
		throws NullPointerException
	{
		// Check
		if (__strings == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * This creates an array of the given type of the specified dimensional
	 * count.
	 *
	 * @param __bn The binary name of the array type.
	 * @param __dims The number of dimensions in the array.
	 * @throws IllegalArrayDimensionsException
	 */
	private final InterpreterClass __internalLoadArray(String __bn, int __dims)
		throws IllegalArrayDimensionsException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException();
		if (__dims <= 0 || __dims >= MAX_ARRAY_DIMENSIONS)
			throw new IllegalArrayDimensionsException(__dims);
		
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
	private final InterpreterClass __internalLoadClass(String __bn, int __dims)
		throws NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException();
		
		// Array?
		if (__dims != 0)
			return __internalLoadArray(__bn, __dims);
		
		// Lock on the classpath
		synchronized (classpaths)
		{
			// Go through all classpaths
			for (InterpreterClassPath icp : classpaths)
				try (InputStream is = icp.getResourceAsStream(
						__bn.replace('.', '/') + ".class"))
				{
					// No class found
					if (is == null)
						continue;
					
					// Create class data
					InterpreterClass rv = new InterpreterClass(this, is);
					
					// Wrong class? Ignore it
					if (!rv.getName().equals(__bn))
						throw new InterpreterClassFormatError(
							"Expected class '" + __bn +
							"' however '" + rv.getName() + "' was read.");
					
					// Return it
					return rv;
				}
				
				// Failed to load class, ignore
				catch (IOException e)
				{
					throw new InterpreterClassFormatError("Read error.",
						e);
				}
		}
		
		// A class was not loaded
		return null;
	}
}

