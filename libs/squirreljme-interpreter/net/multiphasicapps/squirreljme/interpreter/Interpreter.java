// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.VerifiedJITInput;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains the interpreter which executes Java byte code in a contained
 * environment which is built for SquirrelJME.
 *
 * @since 2017/10/05
 */
public class Interpreter
	implements Runnable
{
	/** The lock for the global state in the virtual machine. */
	final Object _lock =
		new Object();
	
	/** The input for the JIT. */
	protected final VerifiedJITInput input;
	
	/** The boot class. */
	protected final ClassName bootclass;
	
	/** Extra system properties. */
	private Map<String, String> _properties;
	
	/** Processes within the virtual machine. */
	private final List<VMProcess> _processes =
		new ArrayList<>();
	
	/** Classes which have been loaded by the virtual machine. */
	private final Map<ClassName, ClassInstance> _classes =
		new SortedTreeMap<>();
	
	/** The next ID for new threads. */
	private volatile int _nextthreadid;
	
	/**
	 * Initializes the interpreter to run the given program.
	 *
	 * @param __vji The verified input to use.
	 * @param __props The system properties to use.
	 * @param __boot The MIDlet to enter for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/05
	 */
	public Interpreter(VerifiedJITInput __vji, Map<String, String> __props,
		String __boot)
		throws NullPointerException
	{
		if (__vji == null || __props == null || __boot == null)
			throw new NullPointerException("NARG");
		
		// Set input
		this.input = __vji;
		
		// Copy properties
		Map<String, String> properties = new SortedTreeMap<>();
		properties.putAll(__props);
		this._properties = properties;
		
		// Set starting point
		this.bootclass = new ClassName(__boot.replace('.', '/'));
	}
	
	/**
	 * Creates a new process and returns it.
	 *
	 * @return The newly created process.
	 * @since 2017/10/08
	 */
	public VMProcess createProcess()
	{
		List<VMProcess> processes = this._processes;
		synchronized (this._lock)
		{
			VMProcess rv = new VMProcess(new WeakReference<>(this));
			
			processes.add(rv);
			
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/06
	 */
	@Override
	public void run()
	{
		// Setup main thread, construct/init an object in it and then set
		// the entry point to startApp().
		VMProcess mainprocess = createProcess();
		VMThread mainthread = mainprocess.createThread();
		
		if (true)
			throw new todo.TODO();
		
		// Run the main thread in this thread, like magic!
		mainthread.run();
	}
	
	/**
	 * Returns the next thread ID.
	 *
	 * @return The next thread ID.
	 * @since 2017/10/08
	 */
	final int __nextThreadId()
	{
		synchronized (this._lock)
		{
			return ++this._nextthreadid;
		}
	}
}

