// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.library;

import cc.squirreljme.jit.classfile.ClassFile;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a library within the JIT which is used to provide
 * access to class files and resources.
 *
 * @since 2018/02/21
 */
public abstract class Library
{
	/** The name of this library. */
	protected final String name;
	
	/**
	 * Initializes the base library.
	 *
	 * @param __name The name of this library.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public Library(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * Iterates over the entries available within this library.
	 *
	 * @return The entries which are available for usage.
	 * @since 2018/02/23
	 */
	public abstract Iterable<String> entries();
	
	/**
	 * Opens the entry with the specified name.
	 *
	 * @param __name The entry to open.
	 * @return The input stream to the entry data or {@code null} if no
	 * such entry exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public abstract InputStream open(String __name)
		throws NullPointerException;
	
	/**
	 * Iterates through the classes which are available in this library.
	 *
	 * @return The input classes available in this library.
	 * @since 2018/02/24
	 */
	public final Iterable<ClassFile> classes()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Loads the class by the specified name.
	 *
	 * @param __name The name of the class to load.
	 * @return The class file for the given class.
	 * @throws NoSuchClassException If the specified class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public final ClassFile loadClass(String __name)
		throws NoSuchClassException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

