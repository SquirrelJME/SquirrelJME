// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the type that may be used in the language.
 *
 * @since 2018/04/24
 */
public final class Type
{
	/** The simple type used. */
	protected final SimpleType simpletype;
	
	/** The dimensions. */
	protected final int dimensions;
	
	/**
	 * Initializes the type information.
	 *
	 * @param __st The simple type.
	 * @param __dims The dimensions used for the type.
	 * @throws IllegalArgumentException If the number of dimensions is
	 * negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/30
	 */
	public Type(SimpleType __st, int __dims)
		throws IllegalArgumentException, NullPointerException
	{
		if (__st == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ4c Type cannot have negative dimensions.}
		if (__dims < 0)
			throw new IllegalArgumentException("AQ4c");
		
		this.simpletype = __st;
		this.dimensions = __dims;
	}
	
	/**
	 * Returns the number of dimensions associated with the type.
	 *
	 * @return The number of dimensions associated with the type.
	 * @since 2018/04/30
	 */
	public final int dimensions()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type with the given number of array dimensions.
	 *
	 * @param __d The number of dimensions to use for the type.
	 * @return The type with the given dimension count.
	 * @throws IllegalArgumentException If the dimension count is negative.
	 * @since 2018/04/29
	 */
	public final Type withDimensions(int __d)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AQ4e Cannot initialize type with a negative
		// number of dimensions.}
		if (__d < 0)
			throw new IllegalArgumentException("AQ4e");
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses a single type.
	 *
	 * @param __in The input token source.
	 * @return The parsed type.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the type is not valid.
	 * @since 2018/04/24
	 */
	public static Type parseType(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Determine the simple type
		Token token = __in.peek();
		SimpleType simple;
		switch (token.type())
		{
			case KEYWORD_BYTE:
				simple = BasicType.BYTE;
				__in.next();
				break;
				
			case KEYWORD_SHORT:
				simple = BasicType.SHORT;
				__in.next();
				break;
				
			case KEYWORD_CHAR:
				simple = BasicType.CHARACTER;
				__in.next();
				break;
				
			case KEYWORD_INT:
				simple = BasicType.INTEGER;
				__in.next();
				break;
				
			case KEYWORD_LONG:
				simple = BasicType.LONG;
				__in.next();
				break;
				
			case KEYWORD_FLOAT:
				simple = BasicType.FLOAT;
				__in.next();
				break;
				
			case KEYWORD_DOUBLE:
				simple = BasicType.DOUBLE;
				__in.next();
				break;
			
				// Probably class or method handler
			case IDENTIFIER:
				simple = GenericType.parse(__in);
				break;
			
				// {@squirreljme.error AQ4b Invalid type.}
			default:
				throw new StructureParseException(token, "AQ4b");
		}
		
		// Handle dimensions
		int dims = 0;
		while ((token = __in.peek()).type() == TokenType.SYMBOL_OPEN_BRACKET)
		{
			__in.next();
			
			// {@squirreljme.error AQ4d Expected closing bracket to follow
			// opening bracket when declaring type.}
			token = __in.next();
			if (token.type() != TokenType.SYMBOL_CLOSED_BRACKET)
				throw new StructureParseException(token, "AQ4d");
			dims++;
		}
		
		// Build type
		return new Type(simple, dims);
	}
	
	/**
	 * Parses multiple types separated by a comma.
	 *
	 * @param __in The input token source.
	 * @return The parsed types.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the types are not valid.
	 * @since 2018/04/24
	 */
	public static Type[] parseTypes(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

