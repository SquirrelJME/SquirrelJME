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
	/** The threads the interpreter owns (lock on this). */
	protected final Set<InterpreterThread> threads =
		new HashSet<>();
	
	/** Active loaded classes (lock on this). */
	protected final Map<String, Reference<InterpreterClass>> classes =
		new HashMap<>();
	
	/**
	 * Initializes the base engine.
	 *
	 * @since 2016/02/01
	 */
	public InterpreterEngine()
	{
	}
	
	/**
	 * Finds a resource using the given name as if it were requested from a
	 * {@link ClassLoader}.
	 *
	 * @param __res The resource to find.
	 * @return The input stream of the given resource or {@code null} if not
	 * found.
	 * @since 2016/03/02
	 */
	public abstract InputStream getResourceAsStream(String __res);
	
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
			{
				// Requesting an array?
				if (dims != 0)
					throw new Error("TODO");
				
				// Load of a normal class
				else
				{
					// Load resource
					try (InputStream is = getResourceAsStream(
						__bn.replace('.', '/') + ".class"))
					{
						// No class found
						if (is == null)
							return null;
						
						// Create class data
						rv = new InterpreterClass(this, is);
						
						// Wrong class? Ignore it
						if (!rv.getName().equals(__bn))
							throw new InterpreterClassFormatError(
								"Expected class '" + __bn +
								"' however '" + rv.getName() + "' was read.");
					}
					
					// Failed to load class, ignore
					catch (IOException e)
					{
						throw new InterpreterClassFormatError("Read error", e);
					}
				}
				
				// Cache the class
				classes.put(__bn, new WeakReference<>(rv));
			}
			
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
}

