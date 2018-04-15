// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.lexical;

import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents an import statement.
 *
 * @since 2018/04/10
 */
public final class ImportDeclaration
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
	public ImportDeclaration(boolean __static, BinaryName __what)
		throws NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		this.isstatic = __static;
		this.what = __what;
		this.wildcard = __what.toString().endsWith("/*");;
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
		
		if (!(__o instanceof ImportDeclaration))
			return false;
		
		ImportDeclaration o = (ImportDeclaration)__o;
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
	 * Parses an import statement.
	 *
	 * @param __t The input token source.
	 * @return The import statement.
	 * @throws LexicalStructureException If the import is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/13
	 */
	public static final ImportDeclaration parse(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ30 Expected "import" when parsing an import
		// statement.}
		ExpandedToken token = __t.next();
		if (token.type() != TokenType.KEYWORD_IMPORT)
			throw new LexicalStructureException(token, "AQ30");
		
		// Is this static?
		boolean isstatic;
		token = __t.peek();
		if ((isstatic = (token.type() == TokenType.KEYWORD_STATIC)))
			__t.next();
		
		// {@squirreljme.error AQ31 Expected identifier at start of import
		// statement.}
		token = __t.peek();
		if (token.type() != TokenType.IDENTIFIER)
			throw new LexicalStructureException(token, "AQ31");
		
		// Read identifier, due to the wildcard we cannot use
		// QualifiedIdentifier because an identifier must follow the dot
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// {@squirreljme.error AQ32 Expected identifier while parsing the
			// import statement.}
			token = __t.next();
			if (token.type() != TokenType.IDENTIFIER)
				throw new LexicalStructureException(token, "AQ32");
			
			// Build
			sb.append(token.characters());
			
			// Done parsing?
			token = __t.next();
			if (token.type() == TokenType.SYMBOL_SEMICOLON)
				break;
			
			// Parse another statement
			else if (token.type() == TokenType.SYMBOL_DOT)
			{
				// This may be the wildcard statement
				token = __t.peek();
				if (token.type() == TokenType.OPERATOR_MULTIPLY)
				{
					// Consume it
					__t.next();
					
					// {@squirreljme.error AQ34 Expected semicolon to follow
					// the wildcard symbol in the import statement.}
					token = __t.next();
					if (token.type() != TokenType.SYMBOL_SEMICOLON)
						throw new LexicalStructureException(token, "AQ34");
					
					// Add asterisk to make it a wildcard
					sb.append("*");
					break;
				}
				
				// Use slash for binary names
				else
					sb.append("/");
			}
			
			// {@squirreljme.error AQ33 Expected dot or semi-colon to follow
			// the identifier in the import statement.}
			else
				throw new LexicalStructureException(token, "AQ33");
		}
		
		return new ImportDeclaration(isstatic, new BinaryName(sb.toString()));
	}
}

