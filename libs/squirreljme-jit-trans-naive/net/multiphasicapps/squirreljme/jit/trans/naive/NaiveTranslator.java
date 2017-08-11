// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.trans.naive;

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedBasicBlock;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;
import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is the naive translator which performs no optimizations and essentially
 * generates very unoptimized and slightly odd machine code for the purpose
 * of being as simple as possible. This translator for the most part acts as a
 * baseline implementation for the first generation code generators so that
 * anything that is needed can be built using a simple interface where
 * optimization is not a concern.
 *
 * @since 2017/08/07
 */
public class NaiveTranslator
	implements ExpandedByteCode
{
	/** The output. */
	protected final MachineCodeOutput out;
	
	/**
	 * Initializes the naive translator.
	 *
	 * @param __out The output for the translator.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public NaiveTranslator(MachineCodeOutput __out)
		throws NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/11
	 */
	@Override
	public ExpandedBasicBlock basicBlock(BasicBlockKey __key)
		throws JITException, NullPointerException
	{
		// Check
		if (__key == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/09
	 */
	@Override
	public void close()
	{
		// The naive translator does not need the close operation because it
		// always writes directly to the machine code output and does not
		// cache an expanded state for optimization.
	}
}

