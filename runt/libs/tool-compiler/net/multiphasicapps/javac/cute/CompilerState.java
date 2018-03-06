// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This contains the current state of the compiler.
 *
 * @since 2018/03/06
 */
public final class CompilerState
{
	/** Logging. */
	protected final PrintStream log;
	
	/** The class and source paths. */
	protected final Map<CompilerInputLocation, List<CompilerPathSet>> paths;
	
	/**
	 * Initializes the compiler state.
	 *
	 * @param __log The logging output.
	 * @param __ps Path sets for the compiler, used to build the class
	 * structure before compilation is performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public CompilerState(PrintStream __log,
		Map<CompilerInputLocation, List<CompilerPathSet>> __ps)
		throws NullPointerException
	{
		if (__log == null || __ps == null)
			throw new NullPointerException("NARG");
		
		this.log = __log;
		this.paths = __ps;
	}
	
	/**
	 * Logs the specified message.
	 *
	 * @param __t The type of message to display.
	 * @parma __i The current input file.
	 * @param __m The formatted message to show.
	 * @param __args The arguments to the formatted message.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public final void message(MessageType __t, CompilerInput __i, String __m,
		Object... __args)
		throws NullPointerException
	{
		if (__t == null || __i == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Forward
		this.message(__t, __i, -1, -1, __m, __args);
	}
	
	/**
	 * Logs the specified message.
	 *
	 * @param __t The type of message to display.
	 * @parma __i The current input file.
	 * @param __row The current row, negative values are not valid.
	 * @param __col The current column, negative values are not valid.
	 * @param __m The formatted message to show.
	 * @param __args The arguments to the formatted message.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public final void message(MessageType __t, CompilerInput __i, int __row,
		int __col, String __m, Object... __args)
		throws NullPointerException
	{
		if (__t == null || __i == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Print specially formatted messages
		PrintStream log = this.log;
		
		// File
		log.print(__i.name());
		
		// Printing row?
		if (__row >= 0)
		{
			log.print(':');
			log.print(__row);
			
			// Add column also?
			if (__col >= 0)
			{
				log.print(',');
				log.print(__col);
			}
		}
		
		// Print spacer and the message type
		log.print(": [");
		log.print(__t);
		log.print("] ");
		
		// Print the message itself
		log.println(String.format(__m, __args));
	}
}

