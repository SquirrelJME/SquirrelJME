// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.squirreljme.runtime.cldc.high.ChoreManager;

/**
 * This is used to manage and launch chores within the Java run-time.
 *
 * This class is thread safe and utilizes a number of threads since multiple
 * things may be happening at once.
 *
 * @since 2017/12/07
 */
public class JavaChoreManager
	extends ChoreManager
{
	/** Chores which are currently available. */
	private final List<Chore> _chores =
		new LinkedList<>();
	
	/** The identifier for the next chore. */
	private volatile int _nextid =
		1;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public int[] listChores(boolean __sys)
	{
		int i = 0;
		int[] rv;
		
		// Lock
		List<Chore> chores = this._chores;
		synchronized (chores)
		{
			// The chores will not be changed so the size is stable
			int n = chores.size();
			rv = new int[n + 1];
			
			// The first chore is always the system chore (the server process)
			if (__sys)
				rv[i++] = 0;
			
			// Add any remaining chores
			for (Chore c : chores)
				if (__sys || (!__sys && !c.isSystem()))
					rv[i++] = c.id();
		}
			
		// Only return the number of actual used chores
		return Arrays.copyOf(rv, i);
	}
	
	/**
	 * This represents a single chore which is managed by the run-time.
	 *
	 * @since 2017/12/07
	 */
	public final class Chore
	{
		/** The internet ID for this chore. */
		protected final int id;
		
		/** Is this a system chore? */
		protected final boolean system;
		
		/**
		 * Initializes the chore represenation.
		 *
		 * @param __id The ID of the chore.
		 * @param __sys Is this a system chore?
		 * @since 2017/12/07
		 */
		private Chore(int __id, boolean __sys)
		{
			this.id = __id;
			this.system = __sys;
		}
		
		/**
		 * Returns the chore ID.
		 *
		 * @return The chore ID.
		 * @since 2017/12/07
		 */
		public int id()
		{
			return this.id;
		}
		
		/**
		 * Returns {@code true} if this is a system chore.
		 *
		 * @return If this is a system chore.
		 * @since 2017/12/07
		 */
		public boolean isSystem()
		{
			return this.system;
		}
	}
}

