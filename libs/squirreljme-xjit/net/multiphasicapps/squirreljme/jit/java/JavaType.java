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

import net.multiphasicapps.squirreljme.jit.sym.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.sym.FieldSymbol;

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
	
	/** The associated field symbol. */
	protected final FieldSymbol field;
	
	/** Is this top? */
	protected final boolean istop;
	
	/** Is this wide? */
	protected final boolean iswide;
	
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
		
		// Set
		this.field = __f;
		this.istop = false;
		
		// Is this wide?
		String x = __f.toString();
		this.iswide = (x.equals("J") || x.equals("D"));
	}
	
	/**
	 * Initializes a custom type used for nothing and top.
	 *
	 * @param __top Is this top?
	 * @since 2017/05/17
	 */
	private JavaType(boolean __top)
	{
		this.istop = __top;
		this.iswide = false;
		this.field = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof JavaType))
			return false;
		
		JavaType o = (JavaType)__o;
		
		// Has a field mismatch?
		FieldSymbol tf = this.field,
			of = o.field;
		if ((tf == null) != (of == null))
			return false;
		
		// Only the top is significant
		if (tf == null)
			return this.istop == o.istop;
		return tf.equals(of);
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
		return this.field;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/17
	 */
	@Override
	public int hashCode()
	{
		// Use field representation
		FieldSymbol field = this.field;
		if (field != null)
			return field.hashCode();
		
		// Top or nothing?
		if (this.istop)
			return 0xFFFFFFFF;
		return 0;
	}
	
	/**
	 * Is this a valid type for storing of a value?
	 *
	 * @return {@code true} if this is not nothing or the top type.
	 * @since 2017/03/31
	 */
	public final boolean isValid()
	{
		return this.field != null;
	}
	
	/**
	 * Returns {@code true} if this is a wide type.
	 *
	 * @return {@code true} if a wide type.
	 * @since 2016/05/12
	 */
	public final boolean isWide()
	{
		return this.iswide;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/17
	 */
	@Override
	public String toString()
	{
		// Use field representation
		FieldSymbol field = this.field;
		if (field != null)
			return field.toString();
		
		// Top or nothing?
		if (this.istop)
			return "TOP";
		return "NOTHING";
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
		
		// Primitive types are used much so do not create them every time
		switch (__sym.toString())
		{
			case "D": return DOUBLE;
			case "F": return FLOAT;
			case "J": return LONG;
				
				// All of these map to integer (promotion)
			case "B":
			case "C":
			case "I":
			case "S":
			case "Z":
				return INTEGER;
			
				// Create
			default:
				return new JavaType(__sym);
		}
	}
	
	/**
	 * Returns the type associated with the given class name.
	 *
	 * @param __cn The name of the class to get the type for.
	 * @return The type for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/20
	 */
	public static JavaType of(ClassNameSymbol __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		return of(__cn.asField());
	}
}

