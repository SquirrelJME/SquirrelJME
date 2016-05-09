// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.program;

/**
 * This represents a jump target within a program which may be bound to a
 * future basic block.
 *
 * @since 2016/05/09
 */
public final class NRJumpTarget
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The bound basic block. */
	private volatile NRBasicBlock _bound;
	
	/**
	 * This initializes the jump target.
	 *
	 * @since 2016/05/09
	 */
	public NRJumpTarget()
	{
	}
	
	/**
	 * Binds the jump target to the given basic block.
	 *
	 * @param __bl The block to bind to.
	 * @throws NRException If a basic block is already bound.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/09
	 */
	public NRJumpTarget bind(NRBasicBlock __bl)
		throws NRException, NullPointerException
	{
		// Check
		if (__bl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AW03 The current jump target is already
			// bound to a basic block. (The current jump target)}
			NRBasicBlock ib = _bound;
			if (ib != null)
				throw new NRException(NRException.Issue.BOUND_JUMP_TARGET,
					String.format("AW03 %s", this));
			
			// Set
			_bound = __bl;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the basic block that this is bound to.
	 *
	 * @return The bound basic block.
	 * @throws NRException If there is no bound basic block.
	 * @since 2016/05/09
	 */
	public NRBasicBlock get()
		throws NRException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AW02 The current jump target is not bound to
			// any basic block. (The current jump target)}
			NRBasicBlock rv = _bound;
			if (rv == null)
				throw new NRException(NRException.Issue.UNBOUND_JUMP_TARGET,
					String.format("AW02 %s", this));
		
			// Return it
			return rv;
		}
	}
}

