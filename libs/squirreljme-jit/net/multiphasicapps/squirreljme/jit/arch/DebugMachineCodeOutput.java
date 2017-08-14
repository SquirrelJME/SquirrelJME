// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is a debug machine code output which dumps any instructions for the
 * assembler to an output stream. This is used for debugging the assembler.
 *
 * @since 2017/08/11
 */
public class DebugMachineCodeOutput
	implements MachineCodeOutput
{
	/** The stream to print to. */
	protected final PrintStream print;
	
	/** The translator to wrap. */
	protected final MachineCodeOutput wrap;
	
	/**
	 * Initializes the dumping assembler.
	 *
	 * @param __o The target for machine code generation.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public DebugMachineCodeOutput(MachineCodeOutput __o)
		throws NullPointerException
	{
		this(System.err, __o);
	}
	
	/**
	 * Initializes the dumping assembler.
	 *
	 * @param __p The stream to print to.
	 * @param __o The target for machine code generation.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public DebugMachineCodeOutput(PrintStream __p, MachineCodeOutput __o)
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
	 * @since 2017/08/13
	 */
	@Override
	public void close()
		throws JITException
	{
		__printf("Closing");
		this.wrap.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public JITConfig config()
	{
		return this.wrap.config();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public PositionMarker positionMarker()
	{
		return this.wrap.positionMarker();
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
		print.print("MACH@");
		print.printf("%08x", System.identityHashCode(this));
		print.print(" -- ");
		
		// Print formatted string
		print.printf(__fmt, __args);	
		
		// End space
		print.println();
	}
}

