// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;

/**
 * This represents the type of value which is stored in a variable either on
 * the stack or in a local. It is either a primitive type or an object.
 *
 * @since 2016/05/12
 */
public final class JavaType
{
	/** Nothing is stored here. */
	public static final JavaType NOTHING =
		new JavaType(false);
	
	/** 32-bit Integer. */
	public static final JavaType INTEGER =
		new JavaType(ClassNameSymbol.INTEGER.asField());
	
	/** 64-bit Integer. */
	public static final JavaType LONG =
		new JavaType(ClassNameSymbol.LONG.asField());
	
	/** 32-bit Float. */
	public static final JavaType FLOAT =
		new JavaType(ClassNameSymbol.FLOAT.asField());
	
	/** 64-bit Double. */
	public static final JavaType DOUBLE =
		new JavaType(ClassNameSymbol.DOUBLE.asField());
	
	/** The top of a long or double. */
	public static final JavaType TOP =
		new JavaType(true);
	
	/**
	 * Initializes a type based on the given field type.
	 *
	 * @param __f The field type to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/17
	 */
	private JavaType(FieldSymbol __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes a custom type used for nothing and top.
	 *
	 * @param __top Is this top?
	 * @since 2017/05/17
	 */
	private JavaType(boolean __top)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the closest matching field symbol.
	 *
	 * @return The closest matching field symbol or {@code null} if there
	 * is none.
	 * @since 2017/05/17
	 */
	public FieldSymbol fieldSymbol()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/17
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns {@code true} if the entry has a value. The tops of long
	 * or double values do not have values.
	 *
	 * @return {@code true} if not {@link #TOP} and not {@link #NOTHING}.
	 * @since 2017/02/08
	 */
	public final boolean hasValue()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Is this a valid type for storing of a value?
	 *
	 * @return {@code true} if this is not nothing or the top type.
	 * @since 2017/03/31
	 */
	public final boolean isValid()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if this is a wide type.
	 *
	 * @return {@code true} if a wide type.
	 * @since 2016/05/12
	 */
	public final boolean isWide()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/17
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the type of variable to use by its symbol.
	 *
	 * @param __sym The symbol to use for the variable.
	 * @return The variable which is associated with the given symbol.
	 * @throws JITException If the type is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/23
	 */
	public static JavaType of(FieldSymbol __sym)
		throws JITException, NullPointerException
	{
		// Check
		if (__sym == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// If an array then it is always an object
		if (__sym.isArray())
			return OBJECT;
		
		// Depends on the first character otherwise
		switch (__sym.charAt(0))
		{
			case 'L': return OBJECT;
			case 'D': return DOUBLE;
			case 'F': return FLOAT;
			case 'J': return LONG;
				
				// All of these map to integer (promotion)
			case 'B':
			case 'C':
			case 'I':
			case 'S':
			case 'Z':
				return INTEGER;
				
				// Unknown
			default:
				// {@squirreljme.error AQ0a The specified field symbol
				// cannot be mapped to a variable type. (The field symbol)}
				throw new JITException(String.format("AQ0a %s", __sym));
		}
		*/
	}
}

