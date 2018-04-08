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

import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.ExpandingTokenizer;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This class contains the top level layout of a source file, it contains
 * the package statement (if there is one), any imports, and the outermost
 * class declarations.
 *
 * @since 2018/03/22
 */
public final class TopLevelLayout
	implements Layout
{
	/**
	 * Parses and returns the top level layout.
	 *
	 * @param __n The name of the input file.
	 * @param __in The stream to read from.
	 * @throws LayoutParserException If the layout could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public static final TopLevelLayout parse(String __n, InputStream __in)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__n == null || __in == null)
			throw new NullPointerException("NARG");
		
		return TopLevelLayout.parse(new ExpandingTokenizer(__n, __in));
	}
	
	/**
	 * Parses and returns the top level layout.
	 *
	 * @param __n The name of the input file.
	 * @param __in The stream to read from.
	 * @throws LayoutParserException If the layout could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public static final TopLevelLayout parse(String __n, Reader __in)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__n == null || __in == null)
			throw new NullPointerException("NARG");
		
		return TopLevelLayout.parse(new ExpandingTokenizer(__n, __in));
	}
	
	/**
	 * Parses and returns the top level layout.
	 *
	 * @param __t Where to read top level layouts from.
	 * @throws LayoutParserException If the layout could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public static final TopLevelLayout parse(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Parse the package statement
		ExpandedToken token = __t.peek();
		BinaryName inpackage = null;
		if (token.type() == TokenType.KEYWORD_PACKAGE)
		{
			// Consume package
			token = __t.next();
			
			// Read package
			inpackage = LayoutParserUtils.readBinaryName(__t);
			
			// {@squirreljme.error AQ25 Expected semicolon to follow the
			// package statement. (The read token)}
			token = __t.next();
			if (token.type() != TokenType.SYMBOL_SEMICOLON)
				throw new LayoutParserException(token, String.format(
					"AQ25 %s", token));
		}
		
		// Parse import statements
		Set<ClassImport> imports = new LinkedHashSet<>();
		for (;;)
		{
			token = __t.peek();
			
			// No more import statements?
			if (token.type() != TokenType.KEYWORD_IMPORT)
				break;
			
			// Consume the import
			__t.next();
			
			// Read statement
			ClassImport imp = LayoutParserUtils.readClassImport(__t);
			imports.add(imp);
			
			// {@squirreljme.error AQ28 Expected semi-colon to follow the
			// import statement. (The token)}
			token = __t.next();
			if (token.type() != TokenType.SYMBOL_SEMICOLON)
				throw new LayoutParserException(token, String.format("AQ28 %s",
					token));
		}
		
		// Read class modifiers
		if (true)
			throw new todo.TODO();
		
		// Parse class containers which contain modifiers (including
		// annotations), classnames, possible generic type declarations,
		// extends, and implements.
		for (;;)
		{
			// No more class containers?
			if (true)
			{
				if (true)
					throw new todo.TODO();
				break;
			}
			
			// Parse container
			if (true)
				throw new todo.TODO();
		}
		
		// Return the top level layout containing the entire class structure
		throw new todo.TODO();
	}
}

