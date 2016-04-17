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
	implements JVMFrameable
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
	
	/** The stack data storage. */
	protected final JVMDataStore stackvariables =
		new JVMDataStore();
	
	/** This thread's stack trace. */
	protected final Deque<JVMStackFrame> stacktrace =
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
	 * Checks whether the current method can access the given accessible
	 * object.
	 *
	 * @param __o The other accessible object to check.
	 * @throws JVMIncompatibleClassChangeError If the access is denied.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	public boolean checkAccess(JVMAccessibleObject __o)
		throws JVMIncompatibleClassChangeError, NullPointerException
	{
		// Get top stack entry
		Deque<JVMStackFrame> stack = stacktrace;
		synchronized (stack)
		{
			return stack.peekLast().method().checkAccess(__o);
		}
	}
	
	/**
	 * Returns the engine which owns this thread.
	 *
	 * @return The owning engine.
	 * @since 2016/04/09
	 */
	public JVMEngine engine()
	{
		return threads.engine();
	}
	
	/**
	 * Enters the given method.
	 *
	 * @param __m The method to enter.
	 * @param __init Is this an initializer?
	 * @param __args Arguments to this method call.
	 * @return The created stack frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	public JVMStackFrame enterFrame(JVMMethod __m, boolean __init,
		Object... __args)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Deque<JVMStackFrame> stack = stacktrace;
		synchronized (stack)
		{
			JVMStackFrame rv = new JVMStackFrame(this, __m, __init,
				stackvariables, __args);
			stack.offerLast(rv);
			return rv;
		}
	}
	
	/**
	 * Exits the given stack frame.
	 *
	 * @param __fr The frame to exit.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	public JVMThread exitFrame(JVMStackFrame __fr)
		throws NullPointerException
	{
		// Check
		if (__fr == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Deque<JVMStackFrame> stack = stacktrace;
		synchronized (stack)
		{
			// Remove the last
			JVMStackFrame was = stack.pollLast();
			
			// {@squirreljme.error IN0e The callstack for the thread has
			// potentially been corrupted as the last item on the stack is
			// not the current frame. (The current frame; The frame
			// which was at the top of the call stack)}
			if (was != __fr)
				throw new JVMEngineException(__fr, String.format("IN0e %s %s",
					__fr, was));
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the exception trace which represents the stack trace as it
	 * appears internally to the virtual machine.
	 *
	 * @return The exception trace.
	 * @since 2016/04/15
	 */
	public JVMExceptionTrace[] getExceptionTrace()
	{
		// Lock
		Deque<JVMStackFrame> trace = stacktrace;
		synchronized (lock)
		{
			// Setup return array
			int n = trace.size();
			JVMExceptionTrace[] rv = new JVMExceptionTrace[n];
			
			// Go through it
			int i = 0;
			for (JVMStackFrame f : trace)
				rv[i++] = new JVMExceptionTrace(f);
			
			// Return it
			return rv;
		}
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
				throw new JVMIllegalThreadStateException(this, String.format(
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
	 * @since 2016/04/15
	 */
	@Override
	public JVMThread thread()
	{
		return this;
	}
	
	/**
	 * Obtains the owning thread manager.
	 *
	 * @return The owning thread manager.
	 * @since 2016/04/09
	 */
	public JVMThreads threads()
	{
		return threads;
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
			entrymethod.interpret(this, false, entryargs);
			
			// Execution ends
			_ended = true;
		}
		
		// Caught some exception
		catch (Throwable t)
		{
			// {@squirreljme.error IN08 An uncaught exception was thrown by
			// a thread. (The current thread)}
			System.err.printf("IN08 %s%n", toString());
			
			// Get exception cause
			Throwable c = t.getCause();
			JVMEngineException jee = (t instanceof JVMEngineException ?
				(JVMEngineException)t : (c instanceof JVMEngineException ?
				(JVMEngineException)c : null));
			
			// Print it
			System.err.println("********** INTERPRETER **********");
			t.printStackTrace(System.err);
			
			// Print exception as it appears to the virtual machine
			if (jee != null)
			{
				System.err.println("******** VIRTUAL MACHINE ********");
				jee.printVMStackTrace(System.err);
			}
			
			// End marker
			System.err.println("*********************************");
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

