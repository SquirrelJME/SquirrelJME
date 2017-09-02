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

/**
 * This is a variable which apart from position information also has type
 * information specified for it.
 *
 * @since 2017/08/13
 */
public final class TypedVariable
{
	/** The type used for the variable, null means not used. */
	protected final JavaType type;
	
	/** The variable. */
	protected final Variable var;
	
	/** The initialization key. */
	protected final InitializationKey initkey;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the typed variable.
	 *
	 * @param __t The type that is used, may be {@code null} to indicate that
	 * no type is used.
	 * @param __v The location where the variable is located.
	 * @throws NullPointerException If {@code __v} is {@code null}.
	 * @since 2017/08/13
	 */
	public TypedVariable(JavaType __t, Variable __v)
		throws NullPointerException
	{
		this(__t, __v, null);
	}
	
	/**
	 * Initializes the typed variable with the given initialization key.
	 *
	 * @param __t The type that is used, may be {@code null} to indicate that
	 * no type is used.
	 * @param __v The location where the variable is located.
	 * @param __i The initialization key.
	 * @throws NullPointerException If {@code __v} is {@code null}.
	 * @since 2017/08/13
	 */
	public TypedVariable(JavaType __t, Variable __v, InitializationKey __i)
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
			
		// Set
		this.type = __t;
		this.var = __v;
		this.initkey = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if the type is used for the variable.
	 *
	 * @return {@code true} if the type is used.
	 * @since 2017/08/13
	 */
	public boolean hasType()
	{
		return null != this.type;
	}
	
	/**
	 * Has this type been initialized?
	 *
	 * @return {@code true} if this type was initialized.
	 * @since 2017/08/13
	 */
	public boolean isInitialized()
	{
		return this.initkey == null;
	}
	
	/**
	 * Is this an object type?
	 *
	 * @return {@code true} if this is an object type.
	 * @since 2017/08/13
	 */
	public boolean isObject()
	{
		JavaType type = this.type;
		return type != null && !type.isPrimitive();
	}
	
	/**
	 * Is this a primitive type?
	 *
	 * @return {@code true} if this is a primitive type.
	 * @since 2017/08/13
	 */
	public boolean isPrimitive()
	{
		JavaType type = this.type;
		return type != null && type.isPrimitive();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s=%s",
				this.var, this.type)));
		
		return rv;
	}
	
	/**
	 * Returns the variable type.
	 *
	 * @return The variable type.
	 * @since 2017/09/01
	 */
	public JavaType type()
	{
		return this.type;
	}
	
	/**
	 * Returns the variable.
	 *
	 * @return The variable.
	 * @since 2017/09/01
	 */
	public Variable variable()
	{
		return this.var;
	}
}

