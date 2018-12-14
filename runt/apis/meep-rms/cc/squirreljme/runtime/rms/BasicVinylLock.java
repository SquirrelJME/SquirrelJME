// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

/**
 * This is a basic lock which is entirely managed within the current
 * application.
 *
 * @since 2018/12/14
 */
public final class BasicVinylLock
	implements VinylLock
{
	/** The state of the lock. */
	private volatile boolean _locked;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/14
	 */
	@Override
	public final void close()
	{
		synchronized (this)
		{
			this._locked = false;
		}
	}
	
	/**
	 * Locks this record so only a single set of actions can be performed on
	 * them, even for the same thread.
	 *
	 * @return {@code this}.
	 * @since 2018/12/14
	 */
	public final VinylLock lock()
	{
		for (;;)
			synchronized (this)
			{
				// Wait for the lock if it is locked
				if (this._locked)
				{
					try
					{
						this.wait();
					}
					catch (InterruptedException e)
					{
						// Ignore
					}
					
					// Try again
					continue;
				}
				
				// Otherwise lock and return self
				this._locked = true;
				return this;
			}
	}
}

