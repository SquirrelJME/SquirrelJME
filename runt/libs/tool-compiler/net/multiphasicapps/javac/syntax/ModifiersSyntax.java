// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import java.util.Arrays;
import java.util.Iterator;
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
public final class ModifiersSyntax
	implements Iterable<ModifierSyntax>
{
	/** The modifiers to use. */
	private final Set<ModifierSyntax> _modifiers;	
	
	/**
	 * Initializes the modifier set.
	 *
	 * @param __ms The modifiers to use.
	 * @throws NullPointerException If any modifier is null.
	 * @throws SyntaxDefinitionException If a modifier is duplicated or
	 * public, protected, or private are multiply specified.
	 * @since 2018/04/22
	 */
	public ModifiersSyntax(ModifierSyntax... __ms)
		throws NullPointerException, SyntaxDefinitionException
	{
		this(Arrays.<ModifierSyntax>asList(
			(__ms == null ? new ModifierSyntax[0] : __ms)));
	}
	
	/**
	 * Initializes the modifier set.
	 *
	 * @param __ms The modifiers to use.
	 * @throws NullPointerException If any modifier is null.
	 * @throws SyntaxDefinitionException If a modifier is duplicated or
	 * public, protected, or private are multiply specified.
	 * @since 2018/04/22
	 */
	public ModifiersSyntax(Iterable<ModifierSyntax> __ms)
		throws NullPointerException, SyntaxDefinitionException
	{
		if (__ms == null)
			throw new NullPointerException("NARG");
		
		// Check modifiers before using them
		Set<ModifierSyntax> modifiers = new LinkedHashSet<>();
		for (ModifierSyntax m : __ms)
		{
			if (m == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ25 Duplicate modifier. (The modifier)}
			if (modifiers.contains(m))
				throw new SyntaxDefinitionException(String.format("AQ25 %s",
					m));
			
			modifiers.add(m);
		}
		
		// {@squirreljme.error AQ26 There may be only one or none specified for
		// public, protected, or private. (The modifiers)}
		boolean a = modifiers.contains(BasicModifierSyntax.PUBLIC),
			b = modifiers.contains(BasicModifierSyntax.PROTECTED),
			c = modifiers.contains(BasicModifierSyntax.PRIVATE);
		if ((a && b) || (a && c) || (b && c))
			throw new SyntaxDefinitionException(String.format("AQ26 %s",
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
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final Iterator<ModifierSyntax> iterator()
	{
		Set<ModifierSyntax> modifiers = this._modifiers;
		return Arrays.<ModifierSyntax>asList(modifiers.<ModifierSyntax>toArray(
			new ModifierSyntax[modifiers.size()])).iterator();
	}
	
	/**
	 * Is this abstract?
	 *
	 * @return If this is abstract.
	 * @since 2018/04/30
	 */
	public final boolean isAbstract()
	{
		return this._modifiers.contains(BasicModifierSyntax.ABSTRACT);
	}
	
	/**
	 * Is this final?
	 *
	 * @return If this is final.
	 * @since 2018/04/30
	 */
	public final boolean isFinal()
	{
		return this._modifiers.contains(BasicModifierSyntax.FINAL);
	}
	
	/**
	 * Is this native?
	 *
	 * @return If this is native.
	 * @since 2018/04/30
	 */
	public final boolean isNative()
	{
		return this._modifiers.contains(BasicModifierSyntax.NATIVE);
	}
	
	/**
	 * Is this private?
	 *
	 * @return If this is private.
	 * @since 2018/04/30
	 */
	public final boolean isPrivate()
	{
		return this._modifiers.contains(BasicModifierSyntax.PRIVATE);
	}
	
	/**
	 * Is this protected?
	 *
	 * @return If this is protected.
	 * @since 2018/04/30
	 */
	public final boolean isProtected()
	{
		return this._modifiers.contains(BasicModifierSyntax.PROTECTED);
	}
	
	/**
	 * Is this public?
	 *
	 * @return If this is public.
	 * @since 2018/04/30
	 */
	public final boolean isPublic()
	{
		return this._modifiers.contains(BasicModifierSyntax.PUBLIC);
	}
	
	/**
	 * Is this static?
	 *
	 * @return If this is static.
	 * @since 2018/04/30
	 */
	public final boolean isStatic()
	{
		return this._modifiers.contains(BasicModifierSyntax.STATIC);
	}
	
	/**
	 * Is this using strict floating point?
	 *
	 * @return If this is using struct floating point.
	 * @since 2018/04/30
	 */
	public final boolean isStrictFloatingPoint()
	{
		return this._modifiers.contains(BasicModifierSyntax.STRICTFP);
	}
	
	/**
	 * Is this synchronized?
	 *
	 * @return If this is synchronized.
	 * @since 2018/04/30
	 */
	public final boolean isSynchronized()
	{
		return this._modifiers.contains(BasicModifierSyntax.SYNCHRONIZED);
	}
	
	/**
	 * Is this transient?
	 *
	 * @return If this is transient.
	 * @since 2018/04/30
	 */
	public final boolean isTransient()
	{
		return this._modifiers.contains(BasicModifierSyntax.TRANSIENT);
	}
	
	/**
	 * Is this volatile?
	 *
	 * @return If this is volatile.
	 * @since 2018/04/30
	 */
	public final boolean isVolatile()
	{
		return this._modifiers.contains(BasicModifierSyntax.VOLATILE);
	}
	
	/**
	 * Returns the modifiers that have been declared.
	 *
	 * @return The declared modifiers.
	 * @since 2018/05/07
	 */
	public final ModifierSyntax[] modifiers()
	{
		Set<ModifierSyntax> modifiers = this._modifiers;
		return modifiers.<ModifierSyntax>toArray(
			new ModifierSyntax[modifiers.size()]);
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
	 * @throws SyntaxParseException If the modifiers are not valid.
	 * @since 2018/04/21
	 */
	public static ModifiersSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse modifiers
		Set<ModifierSyntax> rv = new LinkedHashSet<>();
		for (;;)
		{
			Token token = __in.peek();
			ModifierSyntax got;
			
			// Is this an annotation? Do not parse @interface as an annotation
			// since that is not valid
			if (token.type() == TokenType.SYMBOL_AT &&
				__in.peek(1).type() != TokenType.KEYWORD_INTERFACE)
				got = AnnotationSyntax.parse(__in);
				
			// Basic modifier?
			else
			{
				// Depends
				switch (token.type())
				{
					case KEYWORD_PUBLIC:
						got = BasicModifierSyntax.PUBLIC;
						break;
						
					case KEYWORD_PROTECTED:
						got = BasicModifierSyntax.PROTECTED;
						break;
						
					case KEYWORD_PRIVATE:
						got = BasicModifierSyntax.PRIVATE;
						break;
						
					case KEYWORD_STATIC:
						got = BasicModifierSyntax.STATIC;
						break;
						
					case KEYWORD_ABSTRACT:
						got = BasicModifierSyntax.ABSTRACT;
						break;
						
					case KEYWORD_FINAL:
						got = BasicModifierSyntax.FINAL;
						break;
						
					case KEYWORD_NATIVE:
						got = BasicModifierSyntax.NATIVE;
						break;
						
					case KEYWORD_SYNCHRONIZED:
						got = BasicModifierSyntax.SYNCHRONIZED;
						break;
						
					case KEYWORD_TRANSIENT:
						got = BasicModifierSyntax.TRANSIENT;
						break;
						
					case KEYWORD_VOLATILE:
						got = BasicModifierSyntax.VOLATILE;
						break;
						
					case KEYWORD_STRICTFP:
						got = BasicModifierSyntax.STRICTFP;
						break;
					
						// No more modifiers to parse
					default:
						return new ModifiersSyntax(rv);
				}
				
				// Consume token to prevent infinite loop
				__in.next();
			}
			
			// {@squirreljme.error AQ27 Duplicate modifier. (The modifier)}
			if (rv.contains(got))
				throw new SyntaxParseException(token,
					String.format("AQ27 %s", got));
			rv.add(got);
		}
	}
	
	/**
	 * This parses modifiers which are associated with formal parameters.
	 *
	 * @param __in The input token source.
	 * @return The parsed modifiers for formal parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the modifiers are not valid.
	 * @since 2018/04/29
	 */
	public static ModifiersSyntax parseForFormalParameter(
		BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse modifiers
		Set<ModifierSyntax> rv = new LinkedHashSet<>();
		for (;;)
		{
			Token token = __in.peek();
			ModifierSyntax got;
			
			// Annotated formal parameter
			if (token.type() == TokenType.SYMBOL_AT)
				got = AnnotationSyntax.parse(__in);
				
			// Basic modifier?
			else
			{
				// Depends
				switch (token.type())
				{
					case KEYWORD_FINAL:
						got = BasicModifierSyntax.FINAL;
						break;
						
						// No more modifiers to parse
					default:
						return new ModifiersSyntax(rv);
				}
				
				// Consume token to prevent infinite loop
				__in.next();
			}
			
			// {@squirreljme.error AQ28 Duplicate modifier while parsing
			// formal parameter modifiers. (The modifier)}
			if (rv.contains(got))
				throw new SyntaxParseException(token,
					String.format("AQ28 %s", got));
			rv.add(got);
		}
	}
}

