// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui.extra;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Manages extra game key states, mostly intended for use
 * with {@link GameCanvas#getKeyStates()}.
 *
 * @since 2026/09/25
 */
@SquirrelJMEVendorApi
public final class ExtraGameKeys
	implements ExtraState
{
	/** The bits which are latched high. */
	private volatile int _latch;
	
	/** The bits set from the last call of {@link #trigger()}. */
	private volatile int _last;
	
	/**
	 * Clears all set bits and returns everything to the null state.
	 *
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	public void clear()
	{
		synchronized (this)
		{
			this._latch = 0;
			this._last = 0;
		}
	}
	
	/**
	 * Raises the latch on the specified bits.
	 *
	 * @param __bits The bits to raise.
	 * @return The actual bits which were affected.
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	public int raise(int __bits)
	{
		synchronized (this)
		{
			// The new latch is just the old latch with the new bits.
			int oldLatch = this._latch;
			int newLatch = oldLatch | __bits;
			
			// Set the new latch state.
			this._latch = newLatch;
			
			// Which bits actually changed?
			return (oldLatch ^ newLatch);
		}
	}
	
	/**
	 * Triggers the latch and changes the internal state.
	 *
	 * @return The bits which have been latched and changed state.
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	public int trigger()
	{
		synchronized (this)
		{
			// We need the current and last state
			int latch = this._latch;
			int last = this._last;
			
			// Move over the latch, and clear the new set
			this._last = latch;
			this._latch = 0;
			
			throw Debugging.todo();
		}
	}
}
