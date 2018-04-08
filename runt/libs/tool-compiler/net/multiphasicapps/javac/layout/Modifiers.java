// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents modifiers that may be associated with a class, interface,
 * field or method. Modifiers include modifier keywords along with any
 * annotations which may be split between other modifiers.
 *
 * @since 2018/04/08
 */
public final class Modifiers
{
	/** Modifiers which are available. */
	private final Modifier[] _modifiers;
	
	/**
	 * Initializes the set of modifiers.
	 *
	 * @param __mods The input modifiers.
	 * @throws LayoutParserException If modifiers are duplicated.
	 * @since 2018/04/08
	 */
	public Modifiers(Modifier... __mods)
		throws LayoutParserException
	{
		this(Arrays.<Modifier>asList(
			(__mods == null ? new Modifier[0] : __mods)));
	}
	
	/**
	 * Initializes the set of modifiers.
	 *
	 * @param __mods The input modifiers.
	 * @throws LayoutParserException If modifiers are duplicated.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public Modifiers(Iterable<Modifier> __mods)
		throws LayoutParserException, NullPointerException
	{
		if (__mods == null)
			throw new NullPointerException("NARG");
		
		// Copy modifiers
		Set<Modifier> mods = new LinkedHashSet<>();
		for (Modifier m : __mods)
		{
			if (m == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ2c Duplicate modifier. (The modifier)}
			if (mods.contains(m))
				throw new LayoutParserException(String.format("AQ2c %s", m));
			
			mods.add(m);
		}
		
		// Initialize
		this._modifiers = mods.<Modifier>toArray(new Modifier[mods.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the input tokens for any modifiers.
	 *
	 * @param __t Where to read modifiers from.
	 * @throws LayoutParserException If the modifiers could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final Modifiers parse(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Set<Modifier> mods = new LinkedHashSet<>();
		
		// Modifier reading loop, since there could be no modifiers to be
		// read at all
__outer:
		for (;;)
		{
			ExpandedToken token = __t.peek();
			TokenType type = token.type();
			
			// Depends on the type
			Modifier mod;
			switch (type)
			{
					// Annotated something
				case SYMBOL_AT:
					throw new todo.TODO();
					
					// Abstract
				case KEYWORD_ABSTRACT:
					mod = StandardModifier.ABSTRACT;
					__t.next();
					break;
					
					// Final
				case KEYWORD_FINAL:
					mod = StandardModifier.FINAL;
					__t.next();
					break;
					
					// Native
				case KEYWORD_NATIVE:
					mod = StandardModifier.NATIVE;
					__t.next();
					break;
					
					// Private
				case KEYWORD_PRIVATE:
					mod = StandardModifier.PRIVATE;
					__t.next();
					break;
					
					// Protected
				case KEYWORD_PROTECTED:
					mod = StandardModifier.PROTECTED;
					__t.next();
					break;
					
					// Public
				case KEYWORD_PUBLIC:
					mod = StandardModifier.PUBLIC;
					__t.next();
					break;
					
					// Static
				case KEYWORD_STATIC:
					mod = StandardModifier.STATIC;
					__t.next();
					break;
					
					// Strict floating point
				case KEYWORD_STRICTFP:
					mod = StandardModifier.STRICTFP;
					__t.next();
					break;
					
					// Synchronized
				case KEYWORD_SYNCHRONIZED:
					mod = StandardModifier.SYNCHRONIZED;
					__t.next();
					break;
					
					// Transient
				case KEYWORD_TRANSIENT:
					mod = StandardModifier.TRANSIENT;
					__t.next();
					break;
					
					// Volatile
				case KEYWORD_VOLATILE:
					mod = StandardModifier.VOLATILE;
					__t.next();
					break;
				
					// Do not know what this is, so stop
				default:
					break __outer;
			}
			
			// {@squirreljme.error AQ2b Duplicate modifier. (The modifier)}
			if (mods.contains(mod))
				throw new LayoutParserException(token,
					String.format("AQ2b %s", mod));
			mods.add(mod);
		}
		
		return new Modifiers(mods);
	}
}

