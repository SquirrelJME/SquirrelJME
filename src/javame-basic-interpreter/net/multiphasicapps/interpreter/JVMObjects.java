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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This manages and allows for the creation of objects within the interpreter.
 * When an object is initialized, this keeps a reference of it.
 *
 * @since 2016/04/06
 */
public final class JVMObjects
{
	/** The string array class. */
	public static final ClassNameSymbol STRING_ARRAY_CLASSNAME =
		new ClassNameSymbol("[Ljava/lang/String;");
	
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** Registered objects (weakly associated). */
	protected final Set<Reference<JVMObject>> objects =
		new HashSet<>();
	
	/** Reference queue to know when objects go away. */
	protected final ReferenceQueue<JVMObject> objectqueue =
		new ReferenceQueue<>();
	
	/**
	 * Initializes the object manager.
	 *
	 * @param __e The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/06
	 */
	JVMObjects(JVMEngine __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __e;
	}
	
	/**
	 * Spawns a string which wraps a string used by this host virtual machine
	 * so it may be accessed by the guest virtual machine.
	 *
	 * Strings may be recycled as if they were interned.
	 *
	 * @param __s The string to wrap.
	 * @return The wrapped string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/05
	 */
	public final JVMObject spawnString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
	public final JVMArray spawnStringArray(String... __strings)
		throws NullPointerException
	{
		// Check
		if (__strings == null)
			throw new NullPointerException("NARG");
		
		// Get the classpath
		JVMClassPath cpath = engine.classes();
		
		// Get the string array class
		JVMClass strarray = cpath.loadClass(STRING_ARRAY_CLASSNAME);
		
		// Create array which carries the same length
		int n = __strings.length;
		JVMArray rv = new JVMArray(this, strarray, n);
		
		// Create string elements
		for (int i = 0; i < n; i++)
		{
			// Get string here
			String s = __strings[i];
			
			// If null, ignore
			if (s == null)
				continue;
			
			// Set otherwise
			rv.setArrayElement(i, spawnString(s));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Registers a given object with this virtual machine.
	 *
	 * Internally the objects are stored in reference and assigned to a given
	 * reference queue. This permits the ability to have a garbage collector
	 * in the target virtual machine with regards to objects without actually
	 * implementing one at all. Thus, the target virtual machine will have the
	 * garbage collection characteristics of the host.
	 *
	 * @param __jo The object to register.
	 * @return {@code this}.
	 * @throws IllegalStateException If the object belongs to another engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/05
	 */
	final JVMObjects __registerObject(JVMObject __jo)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__jo == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error LI0g The object to register is not owned by
		// this object manager.}
		if (this != __jo.objects())
			throw new IllegalStateException("LI0g");
		
		// Lock
		Set<Reference<JVMObject>> objs = objects;
		synchronized (objs)
		{
			objs.add(new WeakReference<>(__jo, objectqueue));
		}
		
		// Self
		return this;
	}
}

