// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
 * This represents the type descriptor of a field.
 *
 * @since 2017/06/12
 */
public final class FieldDescriptor
{
	/** The boolean type. */
	public static final FieldDescriptor BOOLEAN =
		new FieldDescriptor("Z");
	
	/** The byte type. */
	public static final FieldDescriptor BYTE =
		new FieldDescriptor("B");
	
	/** The short type. */
	public static final FieldDescriptor SHORT =
		new FieldDescriptor("S");
	
	/** The char type. */
	public static final FieldDescriptor CHARACTER =
		new FieldDescriptor("C");
	
	/** The int type. */
	public static final FieldDescriptor INTEGER =
		new FieldDescriptor("I");
	
	/** The long type. */
	public static final FieldDescriptor LONG =
		new FieldDescriptor("J");
	
	/** The float type. */
	public static final FieldDescriptor FLOAT =
		new FieldDescriptor("F");
	
	/** The double type. */
	public static final FieldDescriptor DOUBLE =
		new FieldDescriptor("D");
	
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
	 * Is this a primitive type?
	 *
	 * @return {@code true} if this is a primitive type.
	 * @since 2017/07/28
	 */
	public boolean isPrimitive()
	{
		return this.primitive;
	}
	
	/**
	 * Is this an object type?
	 *
	 * @return If this is an object type.
	 * @since 2017/09/16
	 */
	public boolean isObject()
	{
		return !isPrimitive();
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

