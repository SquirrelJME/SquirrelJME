// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * The timer class is used to schedule events for the future which may
 * repeatedly happen or may happen once.
 *
 * This class is thread safe and multiple threads may interact with this
 * class.
 *
 * This class is not real-time and offers no gaurantee that tasks will execute
 * on time.
 *
 * All instances of this class create a background thread.
 *
 * @since 2018/12/11
 */
public class Timer
{
	/** The thread which runs the task of running things. */
	final __TimerThread__ _thread;
	
	/**
	 * Initializes a timer.
	 *
	 * @since 2018/12/11
	 */
	public Timer()
	{
		this("TimerThread");
	}
	
	/**
	 * Initializes a timer with a thread using the given name.
	 *
	 * @param __s The name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public Timer(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Setup thread and start it
		__TimerThread__ thread;
		this._thread = (thread = new __TimerThread__(__s));
		thread.start();
	}
	
	/**
	 * Cancels the timer and all of its events.
	 *
	 * @since 2018/12/11
	 */
	public void cancel()
	{
		__TimerThread__ thread = this._thread;
		synchronized (thread)
		{
			// Cancel and interrupt the thread so it checks and wakes up
			if (!thread._cancel)
			{
				thread._cancel = true;
				thread.interrupt();
			}
		}
	}
	
	/**
	 * Purges all of the cancelled tasks so that they become garbage collected.
	 *
	 * @since 2018/12/11
	 */
	public void purge()
	{
		throw new todo.TODO();
	}
	
	public void schedule(TimerTask __task, Date __time)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.__schedule(__task, __time, false, false, 0);
	}
	
	public void schedule(TimerTask __task, Date __time, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.__schedule(__task, __time, true, false, __period);
	}
	
	public void schedule(TimerTask __task, long __delay)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.__schedule(__task, __delay, false, false, 0);
	}
	
	public void schedule(TimerTask __task, long __delay, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.__schedule(__task, __delay, true, false, __period);
	}
	
	public void scheduleAtFixedRate(TimerTask __task, Date __first,
		long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.__schedule(__task, __first, true, true, __period);
	}
	
	public void scheduleAtFixedRate(TimerTask __task, long __delay,
		long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.__schedule(__task, __delay, true, true, __period);
	}
	
	private void __schedule(TimerTask __task, Date __first,
		boolean __rep, boolean __fixed, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null || __first == null)
			throw new NullPointerException("NARG");
		
		long datemilli = __first.getTime();
		
		throw new todo.TODO();
	}
	
	private void __schedule(TimerTask __task, long __delay,
		boolean __rep, boolean __fixed, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

