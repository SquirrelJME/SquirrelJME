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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
	 * Initializes the top level layout.
	 *
	 * @param __pkg The package represented here.
	 * @param __imps The import statements.
	 * @param __classes The class containers.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public TopLevelLayout(BinaryName __pkg, ClassImport[] __imps,
		ClassContainerLayout[] __classes)
		throws NullPointerException
	{
		if (__pkg == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
		
		// Parse class containers which contain modifiers (including
		// annotations), classnames, possible generic type declarations,
		// extends, and implements.
		List<ClassContainerLayout> classes = new ArrayList<>();
		for (;;)
		{
			token = __t.peek();
			
			// No more class containers?
			if (token.type() == TokenType.END_OF_FILE)
				break;
			
			// Read class container
			classes.add(ClassContainerLayout.parse(__t));
		}
		
		// Return the top level layout containing the entire class structure
		return new TopLevelLayout(
			(inpackage == null ? new BinaryName("") : inpackage),
			imports.<ClassImport>toArray(new ClassImport[imports.size()]),
			classes.<ClassContainerLayout>toArray(
				new ClassContainerLayout[classes.size()]));
	}
}

