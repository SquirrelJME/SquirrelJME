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
		return this.layer.column();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		return this.layer.fileName();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		return this.layer.line();
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
		
		BottomToken peek = this.__bottomPeek(),
		base = peek;
		BottomType type = peek.type();
		
		// Package delaration
		if (type == BottomType.KEYWORD_PACKAGE)
		{
			// Consume package
			this.__bottomNext();
			
			// Target string for the package identifier
			StringBuilder sb = new StringBuilder();
			
			// Reading loop, identifier word followed by dot or semicolon
			for (;;)
			{
				BottomToken next = this.__bottomNext();
				
				// {@squirreljme.error AQ16 Expected identifier while parsing
				// the package. (The token)}
				if (next.type != BottomType.IDENTIFIER)
					throw new TokenizerException(next,
						String.format("AQ16 %s", next));
				
				// Use this identifier
				sb.append(next.characters());
				
				// Adding another identifier?
				next = this.__bottomNext();
				type = next.type();
				if (type == BottomType.SYMBOL_DOT)
				{
					sb.append('.');
					continue;
				}
				
				// No more
				else if (type == BottomType.SYMBOL_SEMICOLON)
					break;
				
				// {@squirreljme.error AQ17 Expected either a dot or
				// semi-colon in the package statement. (The token)}
				else
					throw new TokenizerException(next,
						String.format("AQ17 %s", next));
			}
			
			// Build token
			this.__token(ContextType.PACKAGE_DECLARATION, base, sb);
			
			// Start reading imports
			this.__stackReplace(new __AtIntroImports__());
			
			// Done
			return;
		}
		
		// Import statement
		else if (type == BottomType.KEYWORD_IMPORT)
		{
			// Go straight to import processing
			this.__stackReplace(new __AtIntroImports__());
			return;
		}
		
		// Potential start of class, switch
		else if (type.isPotentialClassStart())
		{
			this.__stackReplace(new __AtClass__(false));
			return;
		}
		
		// {@squirreljme.error AQ15 Unexpected token while searching for the
		// package keyword or other parts.}
		else
			throw new TokenizerException(peek, String.format("AQ15 %s", peek));
		
		
		throw new todo.TODO();
	}


	/**
	 * Pushes a new state to the top.
	 *
	 * @param __new The new state entry.
	 * @return The new state.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/09
	 */
	private final __State__ __statePush(__State__ __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// Add to the top
		Deque<__State__> states = this._states;
		states.addLast(__new);
		
		return __new;
	}
	
	/**
	 * Replaces the top stack entry with the new type.
	 *
	 * @param __new The new state.
	 * @return The new state.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/08
	 */
	private final __State__ __stateReplace(__State__ __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// Replace the top
		Deque<__State__> states = this._states;
		states.removeLast();
		states.addLast(__new);
		
		return __new;
	}
}

