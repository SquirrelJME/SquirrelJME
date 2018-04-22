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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the compilation unit which contains the package, the
 * import statements, and any declared classes.
 *
 * @since 2018/04/21
 */
public final class CompilationUnit
{
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
	 * Parses the compilation unit of the class file.
	 *
	 * @param __in The input token source.
	 * @return The parsed compilation unit.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the structure is not valid.
	 * @since 2018/04/21
	 */
	public static CompilationUnit parse(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// This may be set early for class parse
		Modifiers modifiers = null;
		
		// The package the class is in, if it is in one
		QualifiedIdentifier inpackage = null;
		
		// This may be a package-info file which contains annotations
		// associated with a package
		Token token = __in.next();
		if (token.type() == TokenType.SYMBOL_AT)
			modifiers = Modifiers.parse(__in);
		
		// Read in the package statement, if it is there
		token = __in.peek();
		if (token.type() == TokenType.KEYWORD_PACKAGE)
		{
			// Read package declaration
			inpackage = QualifiedIdentifier.parse(__in);
			
			// {@squirreljme.error AQ3j Expected semi-colon to follow the
			// package statement.}
			token = __in.next();
			if (token.type() != TokenType.SYMBOL_SEMICOLON)
				throw new StructureParseException(token, "AQ3j");
			
			// Only semi-colons and EOF may follow
			if (modifiers != null)
			{
				// {@squirreljme.error AQ3f Expected end of file or semicolons
				// after an annotated package statement, annotated packages
				// are only valid in package-info.java.}
				while ((token = __in.next()).type() != TokenType.END_OF_FILE)
					if (token.type() != TokenType.SYMBOL_SEMICOLON)
						throw new StructureParseException(token, "AQ3f");
				
				throw new todo.TODO();
			}
		}
		
		// Read import statements but if there are modifiers then a class
		// must directly follow
		Set<ImportStatement> imports = new LinkedHashSet<>();
		if (modifiers == null)
			for (;;)
			{
				// Stop when there are no more imports
				token = __in.peek();
				if (token.type() != TokenType.KEYWORD_IMPORT)
					break;
				
				// Parse import statement
				imports.add(ImportStatement.parse(__in));
			}
		
		// Read in classes
		List<ClassStructure> classes = new ArrayList<>();
		for (;;)
		{
			// Need to read in the class modifiers
			if (modifiers == null)
			{
				// Ignore semi-colons
				token = __in.peek();
				if (token.type() == TokenType.SYMBOL_SEMICOLON)
					continue;
				
				// But stop parsing on EOF
				else if (token.type() == TokenType.END_OF_FILE)
					break;
				
				// Parse modifiers
				modifiers = Modifiers.parse(__in);
			}
			
			// Read in single class body
			if (true)
				throw new todo.TODO();
			
			// Clear modifiers so that they are not used again
			modifiers = null;
		}
		
		throw new todo.TODO();
	}
}

