// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This contains the lock for the garbage collector.
 *
 * @since 2020/03/10
 */
public final class GarbageCollectionLock
{
	/** The current lock code. */
	private int _code;
	
	/**
	 * Locks the garbage collector.
	 *
	 * @param __code The locking code.
	 * @return If this was locked.
	 * @since 2020/03/10
	 */
	public boolean lock(int __code)
	{
		synchronized (this)
		{
			// Not yet claimed?
			if (this._code == 0)
			{
				this._code = __code;
				return true;
			}
			
			return false;
		}
	}
	
	/**
	 * Unlocks the garbage collector if the code matches.
	 *
	 * @param __code The locking code.
	 * @since 2020/03/10
	 */
	public void unlock(int __code)
	{
		synchronized (this)
		{
			if (this._code == __code)
				this._code = 0;
		}
	}
}
