// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath;

import java.io.InputStream;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CIClass;

/**
 * This is a class unit which is a collection of classes and resources such as
 * those contained within a single JAR.
 *
 * @since 2016/05/25
 */
public abstract class ClassUnit
	implements Comparable<ClassUnit>
{
	/**
	 * Compares the key of the class unit to the given key.
	 *
	 * @param __k The key to compare against.
	 * @return The result of comparison.
	 * @throws NullPointerException If the key is null.
	 * @since 2016/05/29
	 */
	public abstract int compareTo(String __k)
		throws NullPointerException;
	
	/**
	 * Locates a class using the given class name in this unit and possibly
	 * returns it if this unit has such a class available.
	 *
	 * @param __cns The class name symbol to locate a class for, or
	 * {@code null} if it is not available in this unit.
	 * @return The class associated with the given class name.
	 * @throws CIException If the class could not be loaded.
	 * @since 2016/05/27
	 */
	public abstract CIClass locateClass(ClassNameSymbol __cns)
		throws CIException;
	
	/**
	 * Locates a resource using the given absolute name.
	 *
	 * @param __res The absolute name of the resource to find, this must not
	 * start with a forward slash.
	 * @return The input stream which is associated with the given resource or
	 * {@code null} if it was not found.
	 * @since 2016/05/28
	 */
	public abstract InputStream locateResource(String __res);
	
	/**
	 * Returns the name of the JAR file which is used for the given class
	 * unit.
	 *
	 * @return The class unit key.
	 * @since 2016/05/26
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Compares the key of this class unit to the key of the other.
	 *
	 * @param __cu The class unit to obtain a key from and then compare
	 * against.
	 * @return The result of comparison.
	 * @throws NullPointerException On null arugments.
	 * @since 2016/05/29 
	 */
	@Override
	public final int compareTo(ClassUnit __cu)
		throws NullPointerException
	{
		// Check
		if (__cu == null)
			throw new NullPointerException("NARG");
		
		return compareTo(__cu.toString());
	}
	
	/**
	 * This represents the type of class unit that this provides.
	 *
	 * @since 2016/05/26
	 */
	public static enum Type
	{
		/** Unknown. */
		UNKNOWN,
		
		/** A standard Java console application. */
		CONSOLE,
		
		/** A LIBlet. */
		LIBRARY,
		
		/** A MIDlet. */
		APPLICATION,
		
		/** Multiple types. */
		MULTIPLE,
		
		/** End. */
		;
	}
}

