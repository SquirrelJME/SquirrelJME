// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.host.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.host.trips.JDWPTripThread;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class is used by threads to determine if a thread needs to halt
 * execution due to a suspension via the debugger.
 *
 * @since 2021/03/13
 */
public final class JDWPHostThreadSuspension
{
	/** The suspension count. */
	protected final JDWPHostCounter count =
		new JDWPHostCounter();
	
	/** The timeout to wait for suspension checks. */
	private static final int _TIMEOUT = 
		1_000;
	
	/** Was suspension notified? */
	private volatile boolean _suspendNotified;
	
	/**
	 * Handles thread suspension, if this is to occur.
	 * 
	 * @param __controller The controller, used to report suspension and
	 * resumption to the remote debugger when it actually happens.
	 * @param __thread The thread which is calling this, used for signals.
	 * @return {@code true} if the thread is still waiting and was
	 * interrupted.
	 * @throws IllegalStateException If {@code __thread} is not the owner
	 * of {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public final boolean await(JDWPHostController __controller,
		Object __thread)
		throws NullPointerException, IllegalStateException
	{
		if (__controller == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error AG0g Another thread is being used with await
		call.} */
		if (__controller.viewThread().suspension(__thread) != this)
			throw new IllegalStateException("AG0g");
		
		// Did we emit the suspension message?
		boolean emitSuspend = false;
		
		// Loop around constantly, handle interrupts which will just do nothing
		// on suspension
		boolean amInterrupting = false;
		for (int lastCount = -1;;)
			synchronized (this)
			{
				// Get the last count at this await cycle, but only when
				// it is done first 
				int queryCount = this.count.query();
				if (lastCount < 0)
					lastCount = queryCount;
				
				// If this is zero, we are not waiting
				if (queryCount == 0)
				{
					// Going from suspended to clear?
					if (lastCount > 0)
						__controller.trip(JDWPTripThread.class,
							JDWPGlobalTrip.THREAD)
							.suspension(__thread, false);
					
					return false;
				}
				
				// Signal if we are suspended
				if (!this._suspendNotified)
				{
					this._suspendNotified = true;
					__controller.trip(JDWPTripThread.class,
						JDWPGlobalTrip.THREAD)
						.suspension(__thread, true);
					
					// Debug
					if (!emitSuspend)
					{
						emitSuspend = true;
						Debugging.debugNote(
							"Thread %s is suspended via the debugger...",
							__controller.viewThread().name(__thread));
					}
				}
				
				// If this is being interrupted then just stop here, although
				// this may result in this loop going back in again anyway
				if (amInterrupting)
					return true;
				
				// Otherwise wait for the next signal
				try
				{
					this.wait(JDWPHostThreadSuspension._TIMEOUT);
				}
				catch (InterruptedException e)
				{
					// Signal we are interrupting, this will return before
					// we sleep.
					amInterrupting = true;
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
		return this.count.query();
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
			int queryCount = this.count.query();
			if (queryCount == 0)
			{
				// Notify any threads in the event they are waiting
				this.notifyAll();
				
				// No suspensions
				return 0;
			}
			
			// Count down, if we reach zero then we will make notifications,
			// so we do not need to needlessly wake threads up
			int rv = this.count.decrement();
			if (rv == 0)
			{
				// Since we are now at zero suspends, the next time we suspend
				// we want to send the signal
				this._suspendNotified = false;
				
				// Notify anyone that is suspended, so it can resume from
				// execution
				this.notifyAll();
			}
			
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
		return this.suspend(false);
	}
	
	/**
	 * Suspends the given thread.
	 * 
	 * @param __ifNot Only perform suspension if the thread is not already
	 * suspended.
	 * @return The resultant suspension count.
	 * @since 2021/04/17
	 */
	public final int suspend(boolean __ifNot)
	{
		synchronized (this)
		{
			// Is this already suspended?
			int count = this.count.query();
			if (__ifNot && count > 0)
				return count;
			
			// Increment the counter and return it
			return this.count.increment();
		}
	}
}
