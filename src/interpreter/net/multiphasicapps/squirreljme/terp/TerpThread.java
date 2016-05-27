// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This represents a thread which runs inside of the interpreter.
 *
 * @since 2016/04/21
 */
public class TerpThread
{
	/** The owning core. */
	protected final TerpCore core;
	
	/** The thread this executes from. */
	protected final Thread thread;
	
	/** Special thread? */
	protected final boolean isspecial;
	
	/**
	 * Initializes the interpreter thread which maps a thread to the specified
	 * thread. This is used for the method initalization since it needs a
	 * thread to exist.
	 *
	 * @param __c The owning core.
	 * @param __xt The pre-existing thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public TerpThread(TerpCore __c, Thread __xt)
		throws NullPointerException
	{
		// Check
		if (__c == null || __xt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.core = __c;
		this.thread = __xt;
		this.isspecial = true;
	}
	
	/**
	 * Initializes a new thread which executes the given method with the
	 * specified argument list.
	 *
	 * @param __c The owning virtual machine.
	 * @param __m The method to execute.
	 * @param __a The arguments to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public TerpThread(TerpCore __c, TerpMethod __m, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__c == null || __m == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the core of the interpreter.
	 *
	 * @return The interpreter core.
	 * @since 2016/05/12
	 */
	public TerpCore core()
	{
		return this.core;
	}
	
	/**
	 * Invokes the given method within the current thread's context.
	 *
	 * @param __m The method to invoke.
	 * @param __args The method arguments.
	 * @return The return value of the invoked method.
	 * @throws TerpException If the thread of execution is not this thread or
	 * the method is abstract.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public Object invoke(TerpMethod __m, Object... __args)
		throws TerpException, NullPointerException
	{
		// Check
		if (__m == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
		/*
		// {@squirreljme.error AN0n Attempted to invoke a method which is
		// across the context of a thread.}
		if (core.thread(thread) != this)
			throw new TerpException(core, TerpException.Issue.CROSS_CONTEXT,
				"AN0n");
		
		// Obtain the method program
		Object rawprg = __m.program();
		TerpInterpreter terp;
		
		// Use pure interpreter
		if (rawprg instanceof NBCByteCode)
			terp = new TerpInterpreterPure(this, (NBCByteCode)rawprg);
		
		// Optimized program
		else if (rawprg instanceof NRProgram)
			terp = new TerpInterpreterCompiler(this, (NRProgram)rawprg);
		
		// Unknown
		else
			throw new RuntimeException("WTFX");
		
		// Setup interpreter on the stack
		Deque<TerpInterpreter> stack = this.callstack;
		synchronized (stack)
		{
			stack.offerLast(terp);
		}
		
		// Call interpreter
		try
		{
			return terp.interpret(__args);
		}
		
		// Always remove the top-most frame
		finally
		{
			// {@squirreljme.error AN0p Incorrect stack frame when
			// interpretation has finished. (The expected stack frame; The
			// frame which was at the top)}
			TerpInterpreter xi;
			if (terp != (xi = stack.removeLast()))
				throw new TerpException(this.core, TerpException.Issue.WRONG_STACK,
					String.format("AN0p %s %s", terp, xi));
		}
		*/
	}
}

