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

import java.util.Objects;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single annotation.
 *
 * @since 2018/04/21
 */
public final class AnnotationSyntax
	implements ModifierSyntax
{
	/** The identifier which identifies the annotation. */
	protected final QualifiedIdentifierSyntax identifier;
	
	/** The expressions which make up the annotation. */
	protected final UnparsedExpressions arguments;
	
	/**
	 * Initializes the annotation which is just a marker.
	 *
	 * @param __qi The identifier used to identify the annotation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/01
	 */
	public AnnotationSyntax(QualifiedIdentifierSyntax __qi)
		throws NullPointerException
	{
		if (__qi == null)
			throw new NullPointerException("NARG");
		
		this.identifier = __qi;
		this.arguments = null;
	}
	
	/**
	 * Initializes the annotation which contains the given unparsed arguments.
	 *
	 * @param __qi The identifier used to identify the annotation.
	 * @param __ue The unparsed expression.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/01
	 */
	public AnnotationSyntax(QualifiedIdentifierSyntax __qi,
		UnparsedExpressions __ue)
		throws NullPointerException
	{
		if (__qi == null || __ue == null)
			throw new NullPointerException("NARG");
		
		this.identifier = __qi;
		this.arguments = __ue;
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
		return this.identifier.hashCode() ^
			Objects.hashCode(this.arguments);
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
	 * This parses a single annotation.
	 *
	 * @param __in The input token source.
	 * @return The parsed annotation.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the annotation is not valid.
	 * @since 2018/04/21
	 */
	public static AnnotationSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ4v Expected at symbol at start of annotation.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_AT)
			throw new SyntaxParseException(token, "AQ4v");
		
		// Read qualified identifier which identifies the type used for the
		// annotation
		QualifiedIdentifierSyntax qi = QualifiedIdentifierSyntax.parse(__in);
		
		// If there is no open parenthesis then it is just a marker
		token = __in.peek();
		if (token.type() != TokenType.SYMBOL_OPEN_PARENTHESIS)
			return new AnnotationSyntax(qi);
		
		// Just read the parenthesis block
		return new AnnotationSyntax(qi,
			UnparsedExpressions.parseArguments(__in));
	}
}

