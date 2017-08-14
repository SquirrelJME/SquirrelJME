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
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedBasicBlock;
import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This dumps the basic block.
 *
 * @since 2017/08/11
 */
@Deprecated
public class DumpBasicBlock
	extends ExpandedBasicBlock
{
	/** The stream to print to. */
	protected final PrintStream print;
	
	/** The target to wrap. */
	protected final ExpandedBasicBlock wrap;
	
	/** The code string for the dump. */
	protected final String codestring;
	
	/**
	 * Initializes the basic block dumper.
	 *
	 * @param __p The stream to print to.
	 * @param __w The basic block output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public DumpBasicBlock(PrintStream __p, ExpandedBasicBlock __w,
		BasicBlockKey __k)
		throws NullPointerException
	{
		// Check
		if (__p == null || __w == null || __k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.print = __p;
		this.wrap = __w;
		
		// Determine the code string, limit to 8 characters
		String codestring = __k.toString();
		int n = codestring.length();
		if (n > 8)
			codestring = codestring.substring(0, 8);
		else if (n < 8)
			codestring = String.format("%-8s", codestring);
		this.codestring = codestring;
		
		// Mark that this was opened
		__printf("Opened (writing to %s@%08x)", __w.getClass().getName(),
			System.identityHashCode(__w));
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
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void copy(TypedVariable __src, Variable __dest)
		throws JITException, NullPointerException
	{
		__printf("copy(%s, %s)", __src, __dest);
		this.wrap.copy(__src, __dest);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void countReference(TypedVariable __obj, boolean __up)
		throws JITException, NullPointerException
	{
		__printf("countReference(%s, %b)", __obj, __up);
		this.wrap.countReference(__obj, __up);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void monitorEnter(TypedVariable __obj)
		throws JITException, NullPointerException
	{
		__printf("monitorEnter(%s)", __obj);
		this.wrap.monitorEnter(__obj);
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
		print.print("BLOCK@");
		print.print(this.codestring);
		print.print(" -- ");
		
		// Print formatted string
		print.printf(__fmt, __args);	
		
		// End space
		print.println();
	}
}

