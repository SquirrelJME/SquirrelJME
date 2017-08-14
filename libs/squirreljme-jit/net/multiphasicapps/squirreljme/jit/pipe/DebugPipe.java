// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.pipe;

import java.io.PrintStream;

/**
 * This is a pipe which outputs any commands which were sent to it to standard
 * error or some other stream and then passes that information to the pipe
 * that is wraps. This is used for debugging purposes.
 *
 * @since 2017/08/13
 */
public final class DebugPipe
	implements ExpandedPipe
{
	/** The stream to print to. */
	protected final PrintStream print;
	
	/** The pipe to wrap. */
	protected final DebugPipe wrap;
	
	/**
	 * Initializes a debug pipe which prints debug output to standard output.
	 *
	 * @param __w The pipe to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public DebugPipe(ExpandedPipe __w)
		throws NullPointerException
	{
		this(System.err, __w);
	}
	
	/**
	 * Initializes a debug pipe which prints debug output to the specified
	 * stream.
	 *
	 * @param __w The pipe to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public DebugPipe(PrintStream __p, ExpandedPipe __w)
		throws NullPointerException
	{
		// Check
		if (__p == null || __w == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.print = __p;
		this.wrap = __o;
		
		// Mark that this was opened
		__printf("Opened (writing to %s@%08x)", __w.getClass().getName(),
			System.identityHashCode(__w));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void close()
	{
		__printf("Closing");
		this.pipe.close();
	}
	
	/**
	 * Prints formatted text on a line.
	 *
	 * @param __fmt The format to print.
	 * @param __args The arguments to the format.
	 * @since 2017/08/11
	 */
	private void __printf(String __fmt, Object... __args)
	{
		// Print a nice header first
		PrintStream print = this.print;
		print.print("PIPE@");
		print.printf("%08x", System.identityHashCode(this));
		print.print(" -- ");
		
		// Print formatted string
		print.printf(__fmt, __args);	
		
		// End space
		print.println();
	}
}

