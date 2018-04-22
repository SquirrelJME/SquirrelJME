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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents modifiers that are associated with something which may be
 * a basic keyword or an annotation.
 *
 * @since 2018/04/21
 */
public final class Modifiers
{
	/** The modifiers to use. */
	private final Set<Modifier> _modifiers;	
	
	/**
	 * Initializes the modifier set.
	 *
	 * @param __ms The modifiers to use.
	 * @throws NullPointerException If any modifier is null.
	 * @throws StructureParseException If a modifier is duplicated.
	 * @since 2018/04/22
	 */
	public Modifiers(Modifier... __ms)
		throws NullPointerException, StructureParseException
	{
		this(Arrays.<Modifier>asList((__ms == null ? new Modifier[0] : __ms)));
	}
	
	/**
	 * Initializes the modifier set.
	 *
	 * @param __ms The modifiers to use.
	 * @throws NullPointerException If any modifier is null.
	 * @throws StructureParseException If a modifier is duplicated.
	 * @since 2018/04/22
	 */
	public Modifiers(Iterable<Modifier> __ms)
		throws NullPointerException, StructureParseException
	{
		if (__ms == null)
			throw new NullPointerException("NARG");
		
		// Check modifiers before using them
		Set<Modifier> modifiers = new LinkedHashSet<>();
		for (Modifier m : __ms)
		{
			if (m == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ3l Duplicate modifier. (The modifier)}
			if (modifiers.contains(m))
				throw new StructureParseException(String.format("AQ3l %s",
					m));
			
			modifiers.add(m);
		}
		
		// Store
		this._modifiers = modifiers;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This parses modifiers which are associated with something.
	 *
	 * @param __in The input token source.
	 * @return The parsed modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the structure is not valid.
	 * @since 2018/04/21
	 */
	public static Modifiers parse(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse modifiers
		Set<Modifier> rv = new LinkedHashSet<>();
		for (;;)
		{
			Token token = __in.peek();
			Modifier got;
			
			// Is this an annotation? Do not parse @interface as an annotation
			// since that is not valid
			if (token.type() == TokenType.SYMBOL_AT &&
				__in.peek(1).type() != TokenType.KEYWORD_INTERFACE)
				got = Annotation.parse(__in);
				
			// Basic modifier?
			else
			{
				// Depends
				switch (token.type())
				{
					case KEYWORD_PUBLIC:
						got = BasicModifier.PUBLIC;
						break;
						
					case KEYWORD_PROTECTED:
						got = BasicModifier.PROTECTED;
						break;
						
					case KEYWORD_PRIVATE:
						got = BasicModifier.PRIVATE;
						break;
						
					case KEYWORD_STATIC:
						got = BasicModifier.STATIC;
						break;
						
					case KEYWORD_ABSTRACT:
						got = BasicModifier.ABSTRACT;
						break;
						
					case KEYWORD_FINAL:
						got = BasicModifier.FINAL;
						break;
						
					case KEYWORD_NATIVE:
						got = BasicModifier.NATIVE;
						break;
						
					case KEYWORD_SYNCHRONIZED:
						got = BasicModifier.SYNCHRONIZED;
						break;
						
					case KEYWORD_TRANSIENT:
						got = BasicModifier.TRANSIENT;
						break;
						
					case KEYWORD_VOLATILE:
						got = BasicModifier.VOLATILE;
						break;
						
					case KEYWORD_STRICTFP:
						got = BasicModifier.STRICTFP;
						break;
					
						// No more modifiers to parse
					default:
						return new Modifiers(rv);
				}
				
				// Consume token to prevent infinite loop
				__in.next();
			}
			
			// {@squirreljme.error AQ3k Duplicate modifier. (The modifier)}
			if (rv.contains(got))
				throw new StructureParseException(token,
					String.format("AQ3k %s", got));
			rv.add(got);
		}
	}
}

