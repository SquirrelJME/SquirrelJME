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
import java.util.Objects;

/**
 * This represents a type within the stack map table which maps to a type used
 * within the running virtual machine. Types in the stack map will usually be
 * initialized but they can be uninitialized in the event of {@code new}
 * operations being performed.
 *
 * @since 2017/07/26
 */
public final class JavaType
{
	/** The top of a long. */
	public static final JavaType TOP_LONG =
		new JavaType(1);
	
	/** The top of a double. */
	public static final JavaType TOP_DOUBLE =
		new JavaType(2);
	
	/** The type this refers to. */
	protected final FieldDescriptor type;
	
	/** Internal property. */
	private final int _internal;
	
	/** String representation of this table. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the stack map type.
	 *
	 * @param __cn The name of the field.
	 * @param __init Is this object initialized?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public JavaType(ClassName __cn)
		throws NullPointerException
	{
		this(new FieldDescriptor((__cn.isArray() ? __cn.toString() :
			"L" + __cn + ";")));
	}
	
	/**
	 * Initializes the stack map type.
	 *
	 * @param __f The field type.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public JavaType(FieldDescriptor __f)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Promote to integer since the VM does not have a representation for
		// types smaller than int
		if (__f.equals(FieldDescriptor.BOOLEAN) ||
			__f.equals(FieldDescriptor.BYTE) ||
			__f.equals(FieldDescriptor.SHORT) ||
			__f.equals(FieldDescriptor.CHARACTER))
			__f = FieldDescriptor.INTEGER;
		
		// Set
		this._internal = 0;
		this.type = __f;
	}
	
	/**
	 * Used internally to create special non-comparable types.
	 *
	 * @param __i Internal identifier.
	 * @throws RuntimeException If the internal value is zero.
	 * @since 2017/07/28
	 */
	private JavaType(int __i)
		throws RuntimeException
	{
		// Check
		if (__i == 0)
			throw new RuntimeException("OOPS");
		
		this._internal = __i;
		this.type = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof JavaType))
			return false;
		
		JavaType o = (JavaType)__o;
		return this._internal == o._internal &&
			Objects.equals(this.type, o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public int hashCode()
	{
		return this._internal ^ Objects.hashCode(this.type);
	}
	
	/**
	 * Is this an object type?
	 *
	 * @return {@code true} if this is an object type.
	 * @since 2017/08/13
	 */
	public boolean isObject()
	{
		return !this.type.isPrimitive();
	}
	
	/**
	 * Is this a primitive type?
	 *
	 * @return {@code true} if this is a primitive type.
	 * @since 2017/08/13
	 */
	public boolean isPrimitive()
	{
		return this.type.isPrimitive();
	}
	
	/**
	 * Is this a wide type?
	 *
	 * @return {@code true} if the type is a wide type.
	 * @since 2017/07/28
	 */
	public boolean isWide()
	{
		FieldDescriptor type = this.type;
		return FieldDescriptor.LONG.equals(type) ||
			FieldDescriptor.DOUBLE.equals(type);
	}
	
	/**
	 * Returns the type that is used for the top type.
	 *
	 * @return The associated top type used for this type or {@code null} if
	 * there is none.
	 * @since 2017/07/28
	 */
	public JavaType topType()
	{
		FieldDescriptor type = this.type;
		if (type.equals(FieldDescriptor.LONG))
			return TOP_LONG;
		else if (type.equals(FieldDescriptor.DOUBLE))
			return TOP_DOUBLE;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public String toString()
	{
		// Unknown
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Fixed type
			if (this.equals(TOP_LONG))
				rv = "top-long";
			else if (this.equals(TOP_DOUBLE))
				rv = "top-double";
			
			// Other
			else
				rv = this.type.toString();
			
			// Cache
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
	}
}

