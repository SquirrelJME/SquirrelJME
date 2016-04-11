// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This calculates variables types and their SSA values.
 *
 * @since 2016/04/11
 */
class __SSACalculator__
{
	/** The owning program. */
	protected final CPProgram program;
	
	/** Operation processing queue. */
	protected final Deque<CPOp> queue =
		new LinkedList<>();
	
	/**
	 * This calculates SSA operations.
	 *
	 * @param __prg The program owning this calculator.
	 * @since 2016/04/11
	 */
	__SSACalculator__(CPProgram __prg)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
		
		// At the first instruction to the queue initially
		queue.offerLast(program.get(0));
	}
	
	/**
	 * Performs calculations which remain in the queue.
	 *
	 * @return {@code true} if the queue still has items in it.
	 * @since 2016/04/11
	 */
	public boolean calculate()
	{
		for (;;)
		{
			// If the queue is empty, find the next instruction to process is
			// an untouched instruction
			if (queue.isEmpty())
				for (CPOp xop : program)
					if (xop._calccount == 0)
					{
						// Add it
						queue.add(xop);
						
						// Stop processing because it is possible that this
						// instruction jumps to another instruction which
						// might setup a bunch of states if needed.
						break;
					}
			
			// Poll the operation in the queue
			CPOp xop = queue.pollFirst();
			
			// End of execution?
			if (xop == null)
				return !queue.isEmpty();
			
			// Increase the operation calculation count to later find
			// operations which never got calculations performed for them.
			xop._calccount++;
				
			System.err.printf("DEBUG -- Calc %d%n", xop.address());
		}
	}
}

