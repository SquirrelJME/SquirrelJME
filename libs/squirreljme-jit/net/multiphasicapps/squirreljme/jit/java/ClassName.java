// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents the name of a class which exists within the virtual machine.
 * It is always normalized to the form which is used in fields, that is the
 * string used is always a field descriptor so that native types an arrays can
 * be represented in a uniform fashion.
 *
 * Primitive types and arrays are in the special package.
 *
 * This class is immutable.
 *
 * @since 2017/06/15
 */
public final class ClassName
	implements Comparable<ClassName>
{
	/** String representation of the class name. */
	protected final String string;
	
	/** The number of array dimensions. */
	protected final int dimensions;
	
	/** The package this class is in. */
	private volatile Reference<PackageName> _package;
	
	/**
	 * Initializes the class name.
	 *
	 * @param __n The string that makes up the class name.
	 * @throws JITException If the class name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/05
	 */
	public ClassName(String __n)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __n;
		
		// Is this an array?
		if (__n.startsWith("["))
			throw new todo.TODO();
		
		// Not an array
		else
		{
			this.dimensions = 0;
			
			// Check characters
			boolean ls = true;
			for (int i = 0, n = __n.length(); i < n; i++)
			{
				char c = __n.charAt(i);
				
				// Slashes cannot be first and 
				if (c == '/')
				{
					// {@squirreljme.error JI0h Class names cannot have an
					// empty package or class name. (The class name)}
					if (ls)
						throw new JITException(String.format("JI0h %s", __n));
					else
						ls = true; 
				}
				else
					ls = false;
				
				// {@squirreljme.error JI0f The specified class name contains
				// an illegal character. (The class name)}
				if (c == '.' || c == ';' || c == '[')
					throw new JITException(String.format("JI0f %s", __n));
			}
			
			// {@squirreljme.error JI0g Class names cannot end with a slash.
			// (The class name)}
			if (ls)
				throw new JITException(String.format("JI0g %s", __n));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/15
	 */
	@Override
	public int compareTo(ClassName __o)
	{
		return this.string.compareTo(__o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ClassName))
			return false;
		
		return this.string.equals(((ClassName)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * Returns the package that this class resides within.
	 *
	 * If this class name represents a primitive type or an array then the
	 * special package is returned.
	 *
	 * @return The package that this class is inside.
	 * @since 2017/06/15
	 */
	public PackageName packageName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

