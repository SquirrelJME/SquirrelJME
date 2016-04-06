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

import java.util.Deque;
import java.util.LinkedList;
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
	protected final int id;
	
	/** This thread's stack trace and its state. */
	protected final Deque<StackElement> stacktrace =
		new LinkedList<>();
	
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
	 * Initializes the thread.
	 *
	 * @param __owner The owning thread manager.
	 * @param __id The next thread ID.
	 * @param __em The method to execute on entry.
	 * @param __args The arguments to use on entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	JVMThread(JVMThreads __owner, int __id, JVMMethod __em, Object... __args)
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
		
		// By default name the thread based on the method used
		_name = __em.name().toString();
	}
	
	/**
	 * Returns {@code true} if this thread is alive.
	 *
	 * @return If this thread is alive or not.
	 * @since 2016/04/06
	 */
	public boolean isAlive()
	{
		// Lock
		synchronized (lock)
		{
			return _started && !_ended;
		}
	}
	
	/**
	 * Returns {@code true} if this has been terminated.
	 *
	 * @return If this thread was terminated.
	 * @since 2016/04/06
	 */
	public boolean isTerminated()
	{
		// Lock
		synchronized (lock)
		{
			return _ended;
		}
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
			// Setup the initial stack trace
			Deque<StackElement> stack = stacktrace;
			synchronized (lock)
			{
				stack.offerLast(new StackElement(entrymethod, entryargs));
			}
			
			// Execution loop
			for (;;)
				synchronized (lock)
				{
					// Execute the topmost entry
					try
					{
						// Get the top-most element
						StackElement exectop = stack.peekLast();
						
						// Nothing is on the top, the thread terminates
						if (exectop == null)
							break;
						
						throw new Error("TODO");
					}
					
					// Attempt to handle the exception, if it can even be
					// handled. Some exceptions which are thrown by the engine
					// may end up being wrapped and handled.
					catch (Throwable t)
					{
						// Errors are not to be handled because they pertain
						// to the host virtual machine.
						// However if the host VM is out of error then attempt
						// to translate that to the guest virtual machine.
						if ((t instanceof Error) &&
							!(t instanceof OutOfMemoryError))
							throw (Error)t;
						
						throw new Error("TODO");
					}
				}
			
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
	 * This represents the current state of the stack.
	 *
	 * @since 2016/04/06
	 */
	protected final class StackElement
	{
		/** The current method. */
		protected final JVMMethod method;
		
		/** The method to execute. */
		protected final CPProgram program;
		
		/**
		 * Initializes the stack element.
		 *
		 * @param __meth The current method.
		 * @param __args The method arguments which set the initial local
		 * variables.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/04/06
		 */
		private StackElement(JVMMethod __meth, Object... __args)
			throws NullPointerException
		{
			// Check
			if (__meth == null)
				throw new NullPointerException("NARG");
			
			// Set
			method = __meth;
			program = method.program();
			
			throw new Error("TODO");
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

