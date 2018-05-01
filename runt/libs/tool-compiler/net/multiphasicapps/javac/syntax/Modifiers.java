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
	 * @throws StructureDefinitionException If a modifier is duplicated or
	 * public, protected, or private are multiply specified.
	 * @since 2018/04/22
	 */
	public Modifiers(Modifier... __ms)
		throws NullPointerException, StructureDefinitionException
	{
		this(Arrays.<Modifier>asList((__ms == null ? new Modifier[0] : __ms)));
	}
	
	/**
	 * Initializes the modifier set.
	 *
	 * @param __ms The modifiers to use.
	 * @throws NullPointerException If any modifier is null.
	 * @throws StructureDefinitionException If a modifier is duplicated or
	 * public, protected, or private are multiply specified.
	 * @since 2018/04/22
	 */
	public Modifiers(Iterable<Modifier> __ms)
		throws NullPointerException, StructureDefinitionException
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
				throw new StructureDefinitionException(String.format("AQ3l %s",
					m));
			
			modifiers.add(m);
		}
		
		// {@squirreljme.error AQ4i There may be only one or none specified for
		// public, protected, or private. (The modifiers)}
		boolean a = modifiers.contains(BasicModifier.PUBLIC),
			b = modifiers.contains(BasicModifier.PROTECTED),
			c = modifiers.contains(BasicModifier.PRIVATE);
		if ((a && b) || (a && c) || (b && c))
			throw new StructureDefinitionException(String.format("AQ4i %s",
				modifiers));
		
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
	 * Is this abstract?
	 *
	 * @return If this is abstract.
	 * @since 2018/04/30
	 */
	public final boolean isAbstract()
	{
		return this._modifiers.contains(BasicModifier.ABSTRACT);
	}
	
	/**
	 * Is this final?
	 *
	 * @return If this is final.
	 * @since 2018/04/30
	 */
	public final boolean isFinal()
	{
		return this._modifiers.contains(BasicModifier.FINAL);
	}
	
	/**
	 * Is this native?
	 *
	 * @return If this is native.
	 * @since 2018/04/30
	 */
	public final boolean isNative()
	{
		return this._modifiers.contains(BasicModifier.NATIVE);
	}
	
	/**
	 * Is this private?
	 *
	 * @return If this is private.
	 * @since 2018/04/30
	 */
	public final boolean isPrivate()
	{
		return this._modifiers.contains(BasicModifier.PRIVATE);
	}
	
	/**
	 * Is this protected?
	 *
	 * @return If this is protected.
	 * @since 2018/04/30
	 */
	public final boolean isProtected()
	{
		return this._modifiers.contains(BasicModifier.PROTECTED);
	}
	
	/**
	 * Is this public?
	 *
	 * @return If this is public.
	 * @since 2018/04/30
	 */
	public final boolean isPublic()
	{
		return this._modifiers.contains(BasicModifier.PUBLIC);
	}
	
	/**
	 * Is this static?
	 *
	 * @return If this is static.
	 * @since 2018/04/30
	 */
	public final boolean isStatic()
	{
		return this._modifiers.contains(BasicModifier.STATIC);
	}
	
	/**
	 * Is this using strict floating point?
	 *
	 * @return If this is using struct floating point.
	 * @since 2018/04/30
	 */
	public final boolean isStrictFloatingPoint()
	{
		return this._modifiers.contains(BasicModifier.STRICTFP);
	}
	
	/**
	 * Is this synchronized?
	 *
	 * @return If this is synchronized.
	 * @since 2018/04/30
	 */
	public final boolean isSynchronized()
	{
		return this._modifiers.contains(BasicModifier.SYNCHRONIZED);
	}
	
	/**
	 * Is this transient?
	 *
	 * @return If this is transient.
	 * @since 2018/04/30
	 */
	public final boolean isTransient()
	{
		return this._modifiers.contains(BasicModifier.TRANSIENT);
	}
	
	/**
	 * Is this volatile?
	 *
	 * @return If this is volatile.
	 * @since 2018/04/30
	 */
	public final boolean isVolatile()
	{
		return this._modifiers.contains(BasicModifier.VOLATILE);
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
	 * @throws StructureParseException If the modifiers are not valid.
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
	
	/**
	 * This parses modifiers which are associated with formal parameters.
	 *
	 * @param __in The input token source.
	 * @return The parsed modifiers for formal parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the modifiers are not valid.
	 * @since 2018/04/29
	 */
	public static Modifiers parseForFormalParameter(BufferedTokenSource __in)
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
			
			// Annotated formal parameter
			if (token.type() == TokenType.SYMBOL_AT)
				got = Annotation.parse(__in);
				
			// Basic modifier?
			else
			{
				// Depends
				switch (token.type())
				{
					case KEYWORD_FINAL:
						got = BasicModifier.FINAL;
						break;
						
						// No more modifiers to parse
					default:
						return new Modifiers(rv);
				}
				
				// Consume token to prevent infinite loop
				__in.next();
			}
			
			// {@squirreljme.error AQ44 Duplicate modifier while parsing
			// formal parameter modifiers. (The modifier)}
			if (rv.contains(got))
				throw new StructureParseException(token,
					String.format("AQ44 %s", got));
			rv.add(got);
		}
	}
}

