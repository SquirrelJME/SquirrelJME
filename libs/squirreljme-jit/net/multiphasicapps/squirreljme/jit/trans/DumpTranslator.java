// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.trans;

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedBasicBlock;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is a translator which dumps any methods which are called to standard
 * error and then forwards the arguments to another translator. This is used
 * for debugging the byte code expander.
 *
 * @since 2017/08/11
 */
public class DumpTranslator
	implements ExpandedByteCode
{
	/** The stream to print to. */
	protected final PrintStream print;
	
	/** The translator to wrap. */
	protected final ExpandedByteCode wrap;
	
	/**
	 * Initializes the dumping translator.
	 *
	 * @param __o The target for byte code expansion.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public DumpTranslator(ExpandedByteCode __o)
		throws NullPointerException
	{
		this(System.err, __o);
	}
	
	/**
	 * Initializes the dumping translator.
	 *
	 * @param __p The stream to print to.
	 * @param __o The target for byte code expansion.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public DumpTranslator(PrintStream __p, ExpandedByteCode __o)
		throws NullPointerException
	{
		// Check
		if (__p == null || __o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.print = __p;
		this.wrap = __o;
		
		// Mark that this was opened
		__printf("Opened (writing to %s@%08x)", __o.getClass().getName(),
			System.identityHashCode(__o));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/11
	 */
	@Override
	public void close()
	{
		__printf("Closing");
		this.wrap.close();
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
		print.print("TRANSL@");
		print.printf("%08x", System.identityHashCode(this));
		print.print(" -- ");
		
		// Print formatted string
		print.printf(__fmt, __args);	
		
		// End space
		print.println();
	}
}

