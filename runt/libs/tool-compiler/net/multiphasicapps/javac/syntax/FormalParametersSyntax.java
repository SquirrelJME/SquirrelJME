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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the formal parameters of a method.
 *
 * @since 2018/04/29
 */
public final class FormalParametersSyntax
	implements Iterable<FormalParameterSyntax>
{
	/** Parameters used. */
	private final FormalParameterSyntax[] _parameters;
	
	/**
	 * Initializes the formal parameters.
	 *
	 * @param __p The parameters used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/29
	 */
	public FormalParametersSyntax(FormalParameterSyntax... __p)
		throws NullPointerException
	{
		this(Arrays.<FormalParameterSyntax>asList(
			(__p == null ? new FormalParameterSyntax[0] : __p)));
	}
	
	/**
	 * Initializes the formal parameters.
	 *
	 * @param __p The parameters used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/29
	 */
	public FormalParametersSyntax(Iterable<FormalParameterSyntax> __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		List<FormalParameterSyntax> parms = new ArrayList<>();
		for (FormalParameterSyntax p : __p)
		{
			if (p == null)
				throw new NullPointerException("NARG");
			
			parms.add(p);
		}
		
		this._parameters = parms.<FormalParameterSyntax>toArray(
			new FormalParameterSyntax[parms.size()]);
	}
	
	/**
	 * Returns the descriptor for these parameters.
	 *
	 * @return The method descriptor used.
	 * @since 2018/04/29
	 */
	public final MethodDescriptor descriptor()
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
	 * @since 2018/05/10
	 */
	@Override
	public final Iterator<FormalParameterSyntax> iterator()
	{
		return Arrays.<FormalParameterSyntax>asList(this._parameters.clone()).
			iterator();
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
	 * Parses formal parameters which are part of a description of a method.
	 *
	 * @param __in The input tokens.
	 * @return The parsed formal parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If a formal parameter is not correct.
	 * @since 2018/04/28
	 */
	public static FormalParametersSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ43 Expected opening parenthesis when parsing
		// formal parameters.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_OPEN_PARENTHESIS)
			throw new SyntaxParseException("AQ43");
		
		// Parse each one
		List<FormalParameterSyntax> rv = new ArrayList<>();
		for (;;)
		{
			token = __in.peek();
			
			// End of parameters
			if (token.type() == TokenType.SYMBOL_CLOSED_PARENTHESIS)
			{
				__in.next();
				break;
			}
			
			// Parse modifiers
			ModifiersSyntax mods = ModifiersSyntax.
				parseForFormalParameter(__in);
			
			// Parse type
			TypeSyntax type = TypeSyntax.parseType(__in);
			
			// Is this a variadic argument?
			boolean isvariadic;
			token = __in.peek();
			if ((isvariadic = (token.type() == TokenType.SYMBOL_ELLIPSES)))
				__in.next();
			
			// {@squirreljme.error AQ45 Expected identifier for the parameter
			// name.}
			token = __in.next();
			if (token.type() != TokenType.IDENTIFIER)
				throw new SyntaxParseException(token, "AQ45");
			FieldName name = new FieldName(token.characters());
			
			// Add arrays accordingly
			int extradims = 0;
			while ((token = __in.peek()).type() ==
				TokenType.SYMBOL_OPEN_BRACKET)
			{
				// {@squirreljme.error AQ47 Expected closing bracket to follow
				// opening bracket for array declaration.}
				__in.next();
				if (__in.next().type() != TokenType.SYMBOL_CLOSED_BRACKET)
					throw new SyntaxParseException(token, "AQ47");
				
				extradims++;
			}
			
			// Were there any extra dimensions?
			if (extradims > 0)
				type = type.withDimensions(type.dimensions() + extradims);
			
			// Setup parameter
			rv.add(new FormalParameterSyntax(mods, type, name));
			
			// {@squirreljme.error AQ46 Expected closing parenthesis to follow
			// variadic argument in formal parameter.}
			token = __in.peek();
			if (isvariadic && token.type() !=
				TokenType.SYMBOL_CLOSED_PARENTHESIS)
				throw new SyntaxParseException(token, "AQ46");
			
			// Consume comma
			if (token.type() == TokenType.SYMBOL_COMMA)
				__in.next();
		}
		
		return new FormalParametersSyntax(rv);
	}
}

