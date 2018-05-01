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

import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single import statement.
 *
 * @since 2018/04/21
 */
public final class ImportStatementSyntax
{
	/** Is this static? */
	protected final boolean isstatic;
	
	/** The imported class. */
	protected final BinaryName what;
	
	/** Is it a wildcard? */
	protected final boolean wildcard;
	
	/**
	 * Initializes the import declaration.
	 *
	 * @param __static Is this static?
	 * @param __what What is being imported.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/14
	 */
	public ImportStatementSyntax(boolean __static, BinaryName __what)
		throws NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		this.isstatic = __static;
		this.what = __what;
		this.wildcard = __what.toString().endsWith("/*");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ImportStatementSyntax))
			return false;
		
		ImportStatementSyntax o = (ImportStatementSyntax)__o;
		return this.isstatic == o.isstatic &&
			this.what.equals(o.what);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final int hashCode()
	{
		return this.what.hashCode();
	}
	
	/**
	 * Is this static?
	 *
	 * @return {@code true} if this is static.
	 * @since 2018/04/14
	 */
	public final boolean isStatic()
	{
		return this.isstatic;
	}
	
	/**
	 * Is this a wildcard?
	 *
	 * @return {@code true} if this is a wildcard statement.
	 * @since 2018/04/14
	 */
	public final boolean isWildcard()
	{
		return this.wildcard;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the identifier being imported.
	 *
	 * @return The imported identifier.
	 * @since 2018/04/14
	 */
	public final BinaryName what()
	{
		return this.what;
	}
	
	/**
	 * This parses a single import statement.
	 *
	 * @param __in The input token source.
	 * @return The parsed modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If this is not a valid import statement.
	 * @since 2018/04/21
	 */
	public static ImportStatementSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ3p Expected "import" when parsing an import
		// statement.}
		Token token = __in.next();
		if (token.type() != TokenType.KEYWORD_IMPORT)
			throw new SyntaxParseException(token, "AQ3p");
		
		// Is this static?
		boolean isstatic;
		token = __in.peek();
		if ((isstatic = (token.type() == TokenType.KEYWORD_STATIC)))
			__in.next();
		
		// {@squirreljme.error AQ3q Expected identifier at start of import
		// statement.}
		token = __in.peek();
		if (token.type() != TokenType.IDENTIFIER)
			throw new SyntaxParseException(token, "AQ3q");
		
		// Read identifier, due to the wildcard we cannot use
		// QualifiedIdentifierSyntax because an identifier must follow the dot
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// {@squirreljme.error AQ3r Expected identifier while parsing the
			// import statement.}
			token = __in.next();
			if (token.type() != TokenType.IDENTIFIER)
				throw new SyntaxParseException(token, "AQ3r");
			
			// Build
			sb.append(token.characters());
			
			// Done parsing?
			token = __in.next();
			if (token.type() == TokenType.SYMBOL_SEMICOLON)
				break;
			
			// Parse another statement
			else if (token.type() == TokenType.SYMBOL_DOT)
			{
				// This may be the wildcard statement
				token = __in.peek();
				if (token.type() == TokenType.OPERATOR_MULTIPLY)
				{
					// Consume it
					__in.next();
					
					// {@squirreljme.error AQ3s Expected semicolon to follow
					// the wildcard symbol in the import statement.}
					token = __in.next();
					if (token.type() != TokenType.SYMBOL_SEMICOLON)
						throw new SyntaxParseException(token, "AQ3s");
					
					// Add asterisk to make it a wildcard
					sb.append("*");
					break;
				}
				
				// Use slash for binary names
				else
					sb.append("/");
			}
			
			// {@squirreljme.error AQ3t Expected dot or semi-colon to follow
			// the identifier in the import statement.}
			else
				throw new SyntaxParseException(token, "AQ3t");
		}
		
		// Attempt to parse binary name
		try
		{
			return new ImportStatementSyntax(isstatic,
				new BinaryName(sb.toString()));
		}
		
		// {@squirreljme.error AQ3u Parsed import declaration is not a valid
		// binary name.}
		catch (InvalidClassFormatException e)
		{
			throw new SyntaxParseException(token, "AQ3u");
		}
	}
}

