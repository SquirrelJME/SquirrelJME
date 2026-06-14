// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.rate;

import cc.squirreljme.rts.map.WorldMap;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class manages the frame rate of the entire game.
 *
 * @since 2026/06/10
 */
public class RateController
	implements Runnable
{
	/** The current rate. */
	private volatile RateSpeed _rate;
	
	/** The rate controller thread, if there is one currently. */
	private volatile Reference<Thread> _loopThread;
	
	/** Reference to self. */
	protected final Reference<RateController> reference =
		new WeakReference<>(this);
	
	/** The screen to render. */
	private volatile ScreenRunnable _screen;
	
	/** The world map simulation to run. */
	private volatile WorldMap _worldMap;
	
	/**
	 * Initializes the rate controller.
	 *
	 * @since 2026/06/10
	 */
	public RateController()
	{
		// Default to the normal rate
		this.rate(RateSpeed.NORMAL);
	}
	
	/**
	 * Sets the frame rate.
	 *
	 * @param __rate The rate to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public final void rate(RateSpeed __rate)
		throws NullPointerException
	{
		if (__rate == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Set the new rate
			this._rate = __rate;
			
			// Poke the thread
			this.threadPoke();
		}
	}
	
	/**
	 * Returns the reference to this instance.
	 *
	 * @return The instance to self.
	 * @since 2026/06/10
	 */
	public Reference<RateController> reference()
	{
		return this.reference;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void run()
	{
		// Lock loop
		for (;;)
			synchronized (this)
			{
				// Get the time of loop entry
				long enterTime = System.nanoTime();
				
				// Get the current rate to run at
				RateSpeed rate = this._rate;
				
				// Run a single frame within the game, if we have a world map
				WorldMap worldMap = this._worldMap;
				if (worldMap != null)
					worldMap.run();
				
				// Only update the screen if we have time to do so, or if
				// there is no actual world map yet
				long cycleTime = System.nanoTime() - enterTime;
				if (cycleTime < rate.nanosPerTic || worldMap == null)
				{
					ScreenRunnable screen = this._screen;
					if (screen != null)
						screen.run();
				}
				
				// How much time was spent in this loop cycle? Should we sleep?
				cycleTime = System.nanoTime() - enterTime;
				if (cycleTime < rate.nanosPerTic)
					try
					{
						// Calculate an accurate wait time
						long ms = rate.nanosPerTic - cycleTime;
						int ns = (int)(ms % 1_000_000L);
						ms /= 1_000_000L;
						
						// Wait for the next wakeup event, or until the
						// next frame
						this.wait(ms, ns);
					}
					catch (InterruptedException __ignored)
					{
						// Ignore this, as the loop was likely woken up
					}
			}
	}
	
	/**
	 * Sets the screen to render.
	 *
	 * @param __screen The screen to render.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public void screen(ScreenRunnable __screen)
		throws NullPointerException
	{
		if (__screen == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Set the new screen
			this._screen = __screen;
			
			// Poke the thread
			this.threadPoke();
		}
	}
	
	/**
	 * Starts the run thread.
	 *
	 * @throws IllegalStateException If a thread has already been started.
	 * @since 2026/06/10
	 */
	public void startThread()
		throws IllegalStateException
	{
		synchronized (this)
		{
			// Cannot start a thread twice!!
			if (this._loopThread != null)
				throw new IllegalStateException("EXST");
			
			// Make a new thread for the game
			Thread thread = new Thread(this, "StrategyGame");
			
			// Try setting the priority to the max, if permitted
			// This is a game, it should run first in the VM and system
			try
			{
				thread.setPriority(Thread.MAX_PRIORITY);
			}
			catch (SecurityException __ignored)
			{
				// Ignored
			}
			
			// Store reference to the thread, this is used for interrupts and
			// to get it back when needed
			this._loopThread = new WeakReference<>(thread);
			
			// Start the thread!
			thread.start();
		}
	}
	
	/**
	 * Returns the running thread this is associated with.
	 *
	 * @return The thread this is associated with.
	 * @since 2026/06/10
	 */
	public final Thread thread()
	{
		Reference<Thread> threadRef = this._loopThread;
		if (threadRef != null)
			return threadRef.get();
		return null;
	}
	
	/**
	 * Pokes the thread, if applicable.
	 *
	 * @since 2026/06/10
	 */
	public final void threadPoke()
	{
		synchronized (this)
		{
			// Interrupt the loop thread so it wakes up in the event it is
			// asleep
			Thread thread = this.thread();
			if (thread != null)
				thread.interrupt();
			
			// Notify the run loop that the rate has changed, or some other
			// event has happened
			this.notifyAll();
		}
	}
	
	/**
	 * Returns the current world map.
	 *
	 * @return The current world map.
	 * @since 2026/06/11
	 */
	public WorldMap worldMap()
	{
		// Do not synchronize, as this will deadlock in the UI loop
		return this._worldMap;
	}
	
	/**
	 * Sets the world map to use.
	 *
	 * @param __map The map to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/11
	 */
	public void worldMap(WorldMap __map)
		throws NullPointerException
	{
		if (__map == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Set the new world map
			this._worldMap = __map;
			
			// Poke the thread
			this.threadPoke();
		}
	}
}
