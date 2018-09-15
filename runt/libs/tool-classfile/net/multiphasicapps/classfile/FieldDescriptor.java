// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the type descriptor of a field.
 *
 * @since 2017/06/12
 */
public final class FieldDescriptor
	implements Comparable<FieldDescriptor>, MemberDescriptor
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
	 * @throws InvalidClassFormatException If it is not a valid field descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public FieldDescriptor(String __n)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __n;
		
		// {@squirreljme.error JC0s The field descriptor cannot be blank. (The
		// field descriptor)}
		int n = __n.length();
		if (n <= 0)
			throw new InvalidClassFormatException(
				String.format("JC0s %s", __n));
		
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
				
				// Count dimensions
				int dims = 0;
				for (int i = 0; i < n; i++)
					if (__n.charAt(i) != '[')
						break;
					else
						dims++;
				this.dimensions = dims;
				
				// Parse component
				this.component = new FieldDescriptor(__n.substring(dims));
				break;
				
				// Class
			case 'L':
				this.primitive = false;
				this.dimensions = 0;
				this.component = null;
				
				// {@squirreljme.error JC0t The field descriptor for a class
				// must end with a semicolon. (The field descriptor)}
				if (';' != __n.charAt(n - 1))
					throw new InvalidClassFormatException(
						String.format("JC0t %s", __n));
				
				// Decode
				this.classname = new ClassName(__n.substring(1, n - 1));
				break;
				
				// {@squirreljme.error JC0u The field descriptor is not valid.
				// (The field descriptor)}
			default:
				throw new InvalidClassFormatException(
					String.format("JC0u %s", __n));
		}
	}
	
	/**
	 * Returns the name of the used class.
	 *
	 * @return The used class or {@code null} if a class is not referred to.
	 * @since 2018/09/01
	 */
	public final ClassName className()
	{
		return this.classname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/02
	 */
	@Override
	public int compareTo(FieldDescriptor __o)
	{
		return this.string.compareTo(__o.string);
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
	 * Is this an array type?
	 *
	 * @return {@code true} if an array type.
	 * @since 2017/10/08
	 */
	public boolean isArray()
	{
		return this.component != null;
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
	 * Returns the primitive type for this field.
	 *
	 * @return The primitive type to use or {@code null} if there is none.
	 * @since 2017/10/16
	 */
	public PrimitiveType primitiveType()
	{
		// Quick detect
		if (!this.primitive)
			return null;
		
		// Depends on the string
		switch (toString())
		{
			case "B": return PrimitiveType.BYTE;
			case "C": return PrimitiveType.CHARACTER;
			case "D": return PrimitiveType.DOUBLE;
			case "F": return PrimitiveType.FLOAT;
			case "I": return PrimitiveType.INTEGER;
			case "J": return PrimitiveType.LONG;
			case "S": return PrimitiveType.SHORT;
			case "Z": return PrimitiveType.BOOLEAN;
			default:
				return null;
		}
	}
	
	/**
	 * Returns the simple storage type of the field.
	 *
	 * @return The simple storage type for this field.
	 * @since 2018/09/15
	 */
	public final SimpleStorageType simpleStorageType()
	{
		// Objects
		if (this.isObject())
			return SimpleStorageType.OBJECT;
		
		// Primitive types, these are promoted
		switch (this.primitiveType())
		{
			case BOOLEAN:
			case BYTE:
			case SHORT:
			case CHARACTER:
			case INTEGER:
				return SimpleStorageType.INTEGER;
			
			case LONG:
				return SimpleStorageType.LONG;
			
			case FLOAT:
				return SimpleStorageType.FLOAT;
			
			case DOUBLE:
				return SimpleStorageType.DOUBLE;
			
				// Should not occur
			default:
				throw new RuntimeException("OOPS");
		}
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

