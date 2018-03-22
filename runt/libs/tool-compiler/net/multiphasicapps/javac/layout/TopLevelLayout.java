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
import net.multiphasicapps.javac.token.ExpandingTokenizer;

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
	public static final TopLevelLayout parse(ExpandingTokenizer __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

