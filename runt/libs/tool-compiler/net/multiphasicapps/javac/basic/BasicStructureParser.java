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
import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.javac.FileNameLineAndColumn;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.Tokenizer;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This class parses input tokens from the tokenizer and creates the basic
 * structure of the class file.
 *
 * @since 2018/03/12
 */
public final class BasicStructureParser
	implements Closeable, FileNameLineAndColumn
{
	/** The builder which is used to store partial state. */
	protected final BasicStructureBuilder builder =
		new BasicStructureBuilder();
	
	/** The layer to source tokens from. */
	protected final TokenizerLayer layer;
	
	/** Parser states which store the needed temporary data. */
	private final Deque<__State__> _states =
		new ArrayDeque<>();
	
	/**
	 * Initializes the base state.
	 *
	 * @since 2018/03/13
	 */
	{
		this._states.addLast(new __StatePackage__());
	}
	
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
		this.layer.close();
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
	 * @throws BasicStructureException If the structure could not be parsed.
	 * @throws IOException On read errors.
	 * @since 2018/03/12
	 */
	public final BasicStructure parse()
		throws BasicStructureException, IOException
	{
		Deque<__State__> states = this._states;
		
		// Parsing is done in a loop
		for (boolean goteof = false; !goteof;)
		{
			__State__ state = states.getLast();
			
			// Depends on the area
			__State__.Area area = state.area;
			switch (area)
			{
					// End of file
				case END_OF_FILE:
					goteof = true;
					break;
				
					// Parse the package statement
				case PACKAGE:
					this.__parsePackage((__StatePackage__)state);
					break;
				
					// {@squirreljme.error AQ13 Could not parse the structure
					// because the specified state is not known. (The area)}
				default:
					throw new BasicStructureException(this,
						String.format("AQ13 %s", area));
			}
		}
		
		// Build it
		return this.builder.build();
	}
	
	/**
	 * Parses the package statement.
	 *
	 * @param __state The parsing state.
	 * @throws BasicStructureException If the structure could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	private final void __parsePackage(__StatePackage__ __state)
		throws BasicStructureException, IOException, NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

