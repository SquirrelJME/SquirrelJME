// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This is a thread which.
 *
 * @since 2019/05/09
 */
public final class DeadlockTimeout
	extends Thread
{
	/** Default deadlock time in milliseconds. */
	public static final int DEFAULT_TIMEOUT =
		30_000;
	
	/** The timeout time. */
	protected final long timeoutnano;
	
	/** Has this execution expired? */
	private volatile boolean _expired;
	
	/**
	 * Initializes a timeout thread.
	 *
	 * @since 2019/05/09
	 */
	public DeadlockTimeout()
	{
		this(DEFAULT_TIMEOUT);
	}
	
	/**
	 * Initializes a timeout thread for the given duration.
	 *
	 * @param __ms The timeout in milliseconds.
	 * @since 2019/05/09
	 */
	public DeadlockTimeout(int __ms)
	{
		super("TestDeadlockTimeout");
		
		__ms = (__ms <= 0 ? DEFAULT_TIMEOUT : __ms);
		
		this.timeoutnano = System.nanoTime() + (__ms * 1000000L);
	}
	
	/**
	 * Expires this timeout watch.
	 *
	 * @since 2019/05/09
	 */
	public final void expire()
	{
		// Set as expired
		this._expired = true;
		
		// Also interrupt the thread so it resumes
		this.interrupt();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public final void run()
	{
		// When does this time out?
		long timeoutnano = this.timeoutnano;
		
		// Expiration check loop
		for (;;)
		{
			// Watchdog has expired
			if (this._expired)
				break;
			
			// Has timed out?
			long now = System.nanoTime();
			if (now >= timeoutnano)
			{
				// Print a nasty message
				System.err.println("*** TEST TIMED OUT! ***");
				
				// Exit
				System.exit(-4);
				break;
			}
			
			// Sleep to wait for it
			try
			{
				// Calculate duration
				int durms = (int)((timeoutnano - now) / 1000000L);
				if (durms < 1)
					durms = 1;
				
				// Sleep
				Thread.sleep(durms);
			}
			
			// Ignore any interruptions, just re-run the loop
			catch (InterruptedException e)
			{
			}
		}
	}
}

