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
 * This class is used by threads to determine if a thread needs to halt
 * execution due to a suspension via the debugger.
 *
 * @since 2021/03/13
 */
public final class JDWPThreadSuspension
{
	/** The timeout to wait for suspension checks. */
	private static final int _TIMEOUT = 
		5_000;
	
	/** The suspension count. */
	private volatile int _count;
	
	/** Was suspension notified? */
	private volatile boolean _suspendNotified;
	
	/**
	 * Handles thread suspension, if this is to occur.
	 * 
	 * @param __jdwp The controller, used to report suspension and resumption
	 * to the remote debugger when it actually happens.
	 * @param __thread The thread which is calling this, used for signals.
	 * @return {@code true} if the thread is still waiting and was
	 * interrupted.
	 * @throws IllegalStateException If {@code __thread} is not the owner
	 * of {@code this}.
	 * @since 2021/03/13
	 */
	public final boolean await(JDWPController __jdwp, JDWPThread __thread)
		throws IllegalStateException
	{
		// {@squirreljme.error AG0g Another thread is being used with await
		// call.}
		if (__thread.debuggerSuspend() != this)
			throw new IllegalStateException("AG0g");
		
		// Loop around constantly, handle interrupts which will just do nothing
		// on suspension
		boolean amInterrupting = false;
		for (int lastCount = -1;;)
			synchronized (this)
			{
				// Get the last count at this await cycle, but only when
				// it is done first 
				if (lastCount < 0)
					lastCount = this._count;
				
				// If this is zero, we are not waiting
				if (this._count == 0)
				{
					// Going from suspended to clear?
					if (lastCount > 0)
						__jdwp.signalThreadSuspend(__thread, false);
					
					return false;
				}
				
				// Signal if we are suspended
				if (!this._suspendNotified)
				{
					this._suspendNotified = true;
					__jdwp.signalThreadSuspend(__thread, true);
				}
				
				// If this is being interrupted then just stop here, although
				// this may result in this loop going back in again anyway
				if (amInterrupting)
					return true;
				
				// Otherwise wait for the next signal
				try
				{
					this.wait(JDWPThreadSuspension._TIMEOUT);
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
			{
				// Notify any threads in the event they are waiting
				this.notifyAll();
				
				// No suspensions
				return 0;
			}
			
			// Count down, if we reach zero then we will make notifications
			// so we do not need to needlessly wake threads up
			int rv = --this._count;
			if (rv == 0)
			{
				// Since we are now at zero suspends, the next time we suspend
				// we want to send the signal
				this._suspendNotified = false;
				
				// Notify anyone that is suspended so it can resume from
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
	 * @param __ifNot Only perform suspension if the thread is not alreayd
	 * suspended.
	 * @return The resultant suspension count.
	 * @since 2021/04/17
	 */
	public final int suspend(boolean __ifNot)
	{
		synchronized (this)
		{
			// Is this already suspended?
			int count = this._count;
			if (__ifNot && count > 0)
				return count;
			
			// Increment the counter and return it
			this._count = (++count);
			return count;
		}
	}
}
