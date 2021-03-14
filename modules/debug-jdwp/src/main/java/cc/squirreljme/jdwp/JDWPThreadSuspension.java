// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Tracker for JDWP Thread suspension.
 *
 * @since 2021/03/13
 */
public final class JDWPThreadSuspension
{
	/** The timeout to wait for suspension checks. */
	private static final int _TIMEOUT = 
		500_000;
	
	/** The suspension count. */
	private volatile int _count;
	
	/**
	 * Handles thread suspension, if this is to occur.
	 * 
	 * @return {@code true} if the thread is still waiting.
	 * @since 2021/03/13
	 */
	public final boolean await()
	{
		for (;;)
			synchronized (this)
			{
				// If this is zero, we are not waiting
				if (this._count == 0)
					return false;
				
				// Otherwise wait
				try
				{
					this.wait(JDWPThreadSuspension._TIMEOUT);
				}
				catch (InterruptedException e)
				{
					// On interrupt, just return the current wait count
					// But since this is debug suspended this will just loop
					// around again
					return (this._count != 0);
				}
			}
	}
	
	/**
	 * Returns the number of thread suspensions.
	 * 
	 * @return The number of suspensions.
	 * @since 2021/03/13
	 */
	public final int query()
	{
		synchronized (this)
		{
			return this._count;
		}
	}
	
	/**
	 * Resumes the thread.
	 * 
	 * @return The resultant count.
	 * @since 2021/03/13
	 */
	public final int resume()
	{
		synchronized (this)
		{
			// Nothing to resume
			if (this._count == 0)
				return 0;
			
			// Count down and notify ourself in the event someone is
			// waiting on our own monitor
			int rv = --this._count;
			this.notifyAll();
			return rv;
		}
	}
	
	/**
	 * Suspends the given thread.
	 * 
	 * @return The resultant suspension count.
	 * @since 2021/03/13
	 */
	public final int suspend()
	{
		synchronized (this)
		{
			return ++this._count;
		}
	}
}
