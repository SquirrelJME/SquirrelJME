// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.PrintStream;

/**
 * This is used to a class which can easily output messages for the compiler
 * to the given log.
 *
 * @since 2018/03/12
 */
public final class CompilerLogger
{
	/** The stream to print to. */
	protected final PrintStream out;
	
	/**
	 * Initializes the logger.
	 *
	 * @param __out The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	public CompilerLogger(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * Logs the specified message.
	 *
	 * @param __t The type of message to display.
	 * @parma __fn The current input file, may be {@code null}.
	 * @parma __fn The location in the input file, may be {@code null}.
	 * @param __m The formatted message to show.
	 * @param __args The arguments to the formatted message.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public final void message(MessageType __t, FileName __fn,
		LineAndColumn __lc, String __m, Object... __args)
		throws NullPointerException
	{
		if (__t == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.message(__t, new SimpleFileNameLineAndColumn(__fn, __lc), __m,
			__args);
	}
	
	/**
	 * Logs the specified message.
	 *
	 * @param __t The type of message to display.
	 * @parma __flc The file name, line, and column information, may be
	 * {@code null}
	 * @param __m The formatted message to show.
	 * @param __args The arguments to the formatted message.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public final void message(MessageType __t, FileNameLineAndColumn __flc,
		String __m, Object... __args)
		throws NullPointerException
	{
		if (__t == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Print specially formatted messages
		PrintStream log = this.log;
		
		// File
		if (__flc != null)
			log.print(__flc.fileName());
		
		// Use a fallback name if it is possible
		else
			log.print("<unknown>");
		
		// Use estimate line and column information if it was not specified
		int line = -1,
			column = -1;
		if (__flc != null)
		{
			line = __flc.line();
			column = __flc.column();
		}
		
		// Printing row?
		if (__line >= 0)
		{
			log.print(':');
			log.print(__line);
			
			// Add column also?
			if (__col >= 0)
			{
				log.print(',');
				log.print(__col);
			}
		}
		
		// Just the column?
		else if (__col >= 0)
		{
			log.print(',');
			log.print(__col);
		}
		
		// Print spacer and the message type
		log.print(": [");
		log.print(__t);
		log.print("] ");
		
		// Print the message itself
		log.println(String.format(__m, __args));
	}
}

