// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.classprogram.CPOp;
import net.multiphasicapps.classprogram.CPProgram;

/**
 * This represents a thread within the interpreter.
 *
 * @since 2016/03/01
 */
public class JVMThread
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The thread manager. */
	protected final JVMThreads threads;
	
	/** The entry method. */
	protected final JVMMethod entrymethod;
	
	/** Entry arguments. */
	protected final Object[] entryargs;
	
	/** The current thread ID. */
	protected final long id;
	
	/** This thread's stack trace. */
	protected final Deque<JVMMethod> stacktrace =
		new LinkedList<>();
	
	/** Is this the host VM thread? */
	protected final boolean ishostthread;
	
	/** The internall created thread. */
	private volatile Thread _thread;
	
	/** Has this thread started? */
	private volatile boolean _started;
	
	/** Has this thread ended? */
	private volatile boolean _ended;
	
	/** The name of this thread. */
	private volatile String _name;
	
	/** The thread priority. */
	private volatile int _priority =
		Thread.NORM_PRIORITY;
	
	/**
	 * Initializes a thread which is logically always started but is not an
	 * actual part of the run-time. It is a purely virtual and somewhat
	 * uncounted hosting virtual machine thread.
	 *
	 * @param __own The owning thread manager.
	 * @param __id The thread ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/07
	 */
	JVMThread(JVMThreads __owner, long __id)
	{
		// Check
		if (__owner == null)
			throw new NullPointerException();
		
		// Set
		threads = __owner;
		id = __id;
		
		// Not used
		entrymethod = null;
		entryargs = null;
		
		// This is a host thread
		ishostthread = true;
		_name = "HostVM";
		_started = true;
	}
	
	/**
	 * Initializes the thread.
	 *
	 * @param __owner The owning thread manager.
	 * @param __id The thread ID.
	 * @param __em The method to execute on entry.
	 * @param __args The arguments to use on entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	JVMThread(JVMThreads __owner, long __id, JVMMethod __em, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__owner == null)
			throw new NullPointerException();
		
		// Set
		threads = __owner;
		id = __id;
		entrymethod = __em;
		entryargs = (__args == null ? new Object[0] : __args.clone());
		ishostthread = false;
		
		// By default name the thread based on the method used
		_name = __em.name().toString();
	}
	
	/**
	 * Returns {@code true} if this thread is alive.
	 *
	 * Host threads are always alive.
	 *
	 * @return If this thread is alive or not.
	 * @since 2016/04/06
	 */
	public boolean isAlive()
	{
		// Lock
		synchronized (lock)
		{
			return ishostthread || (_started && !_ended);
		}
	}
	
	/**
	 * Returns {@code true} if this has been terminated.
	 *
	 * Host threads are never terminated.
	 *
	 * @return If this thread was terminated.
	 * @since 2016/04/06
	 */
	public boolean isTerminated()
	{
		// Lock
		synchronized (lock)
		{
			return !ishostthread && _ended;
		}
	}
	
	/**
	 * Returns the stack trace of the current thread.
	 *
	 * @return The thread's stack trace.
	 * @since 2016/04/07
	 */
	public Deque<JVMMethod> stackTrace()
	{
		return stacktrace;
	}
	
	/**
	 * Runs the current thread.
	 *
	 * @return {@code this}.
	 * @throws JVMIllegalThreadStateException If this thread has already
	 * started.
	 * @since 2016/03/01
	 */
	public JVMThread start()
		throws JVMIllegalThreadStateException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error IN07 Thread has already been started.
			// (The current thread)}
			if (_started)
				throw new JVMIllegalThreadStateException(String.format(
					"IN07 %s", this));
			_started = true;
			
			// Setup new thread and run it
			Thread rt = new Thread(new __Runner__());
			_thread = rt;
			
			// Start it
			rt.start();
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/06
	 */
	@Override
	public String toString()
	{
		return "Thread[" + id + "," + _priority + "," + _name + "]";
	}
	
	/**
	 * This is the internal intrepretation loop.
	 *
	 * @since 2016/04/06
	 */
	private void __interpreterLoop()
	{
		// Loop
		try
		{
			// Start execution at the entry method
			entrymethod.interpret(false, this, entryargs);
			
			// Execution ends
			_ended = true;
		}
		
		// Caught some exception
		catch (Throwable t)
		{
			// {@squirreljme.error IN08 An uncaught exception was thrown by
			// a thread. (The current thread)}
			System.err.printf("IN08 %s%n", toString());
			
			// Print it
			t.printStackTrace(System.err);
		}
		
		// Always mark as ended
		finally
		{
			synchronized (lock)
			{
				_ended = true;
			}
		}
	}
	
	/**
	 * This performs the actual thread running work.
	 *
	 * @since 2016/04/06
	 */
	private final class __Runner__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/04/06
		 */
		@Override
		public void run()
		{
			__interpreterLoop();
		}
	}
}

