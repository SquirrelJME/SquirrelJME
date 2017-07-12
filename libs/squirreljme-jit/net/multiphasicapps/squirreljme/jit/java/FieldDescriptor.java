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

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents the type descriptor of a field.
 *
 * @since 2017/06/12
 */
public final class FieldDescriptor
{
	/** String representation. */
	protected final String string;
	
	/** Is this a primitive type? */
	protected final boolean primitive;
	
	/** Array dimensions. */
	protected final int dimensions;
	
	/** The component type. */
	protected final FieldDescriptor component;
	
	/** The class this refers to. */
	protected final ClassName classname;
	
	/**
	 * Initializes the field descriptor.
	 *
	 * @param __n The field descriptor to decode.
	 * @throws JITException If it is not a valid field descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public FieldDescriptor(String __n)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __n;
		
		// {@squirreljme.error JI0l The field descriptor cannot be blank. (The
		// field descriptor)}
		int n = __n.length();
		if (n <= 0)
			throw new JITException(String.format("JI0l %s", __n));
		
		// Depends on the first character
		char c = __n.charAt(0);
		switch (c)
		{
				// Primitive
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
				this.primitive = true;
				this.dimensions = 0;
				this.component = null;
				this.classname = null;
				break;
			
				// Array
			case '[':
				this.primitive = false;
				this.classname = null;
				
				throw new todo.TODO();
				
				// Class
			case 'L':
				this.primitive = false;
				this.dimensions = 0;
				this.component = null;
				
				// {@squirreljme.error JI19 The field descriptor for a class
				// must end with a semicolon. (The field descriptor)}
				if (';' != __n.charAt(n - 1))
					throw new JITException(String.format("JI19 %s", __n));
				
				// Decode
				this.classname = new ClassName(__n.substring(1, n - 1));
				break;
				
				// {@squirreljme.error JI0m The field descriptor is not valid.
				// (The field descriptor)}
			default:
				throw new JITException(String.format("JI0m %s", __n));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof FieldDescriptor))
			return false;
		
		return this.string.equals(((FieldDescriptor)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

