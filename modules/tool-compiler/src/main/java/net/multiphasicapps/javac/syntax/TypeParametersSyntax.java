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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableIterator;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents multiple type parameters.
 *
 * @since 2018/04/30
 */
public final class TypeParametersSyntax
	implements Iterable<TypeParameterSyntax>
{
	/** Type parameters used. */
	private final TypeParameterSyntax[] _params;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the type parameters.
	 *
	 * @param __t The types to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/30
	 */
	public TypeParametersSyntax(TypeParameterSyntax... __t)
		throws NullPointerException
	{
		this(Arrays.<TypeParameterSyntax>asList(
			(__t == null ? new TypeParameterSyntax[0] : __t)));
	}
	
	/**
	 * Initializes the type parameters.
	 *
	 * @param __t The types to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/30
	 */
	public TypeParametersSyntax(Iterable<TypeParameterSyntax> __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		List<TypeParameterSyntax> params = new ArrayList<>();
		for (TypeParameterSyntax t : __t)
		{
			if (__t == null)
				throw new NullPointerException("NARG");
			
			params.add(t);
		}
		
		this._params = params.<TypeParameterSyntax>toArray(
			new TypeParameterSyntax[params.size()]);
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
	 * @since 2018/05/07
	 */
	@Override
	public final Iterator<TypeParameterSyntax> iterator()
	{
		return UnmodifiableIterator.<TypeParameterSyntax>of(this._params);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				Arrays.asList(this._params).toString()));
		
		return rv;
	}
	
	/**
	 * Parses a list of multiple type parameters.
	 *
	 * @param __in The input tokens.
	 * @return The parsed type parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the type parameters could not be
	 * parsed.
	 * @since 2018/04/24
	 */
	public static TypeParametersSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

