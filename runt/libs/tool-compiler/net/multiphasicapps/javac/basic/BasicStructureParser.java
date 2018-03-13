// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import net.multiphasicapps.javac.FileNameLineAndColumn;
import net.multiphasicapps.javac.token.Tokenizer;

/**
 * This class parses input tokens from the tokenizer and creates the basic
 * structure of the class file.
 *
 * @since 2018/03/12
 */
public final class BasicStructureParser
	implements Closeable, FileNameLineAndColumn
{
	/** The layer to source tokens from. */
	protected final TokenizerLayer layer;
	
	/**
	 * Parses the given basic structure from the given tokenizer.
	 *
	 * @param __t The tokenizer to parse from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	public BasicStructureParser(Tokenizer __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Initialize the parser layer
		this.layer = new TokenizerLayer(__t);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int column()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the input source file and builds the structure from it.
	 *
	 * @return The resulting structure.
	 * @throws IOException On read errors.
	 * @since 2018/03/12
	 */
	public final BasicStructure parse()
		throws IOException
	{
		throw new todo.TODO();
	}
}

