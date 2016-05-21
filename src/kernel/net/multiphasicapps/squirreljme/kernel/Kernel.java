// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * This is the base class for the kernel interfaces which are defined by
 * systems to provide anything that the default kernel does not provide
 * when it comes to interfaces. The kernel manages the processes for the
 * system along with inter-process communication and inter-process objects.
 *
 * @since 2016/05/14
 */
public abstract class Kernel
{
	/** The kernel process. */
	private final KernelProcess _kernelprocess;
	
	/** Kernel processes. */
	private final List<KernelProcess> _processes =
		new LinkedList<>();
	
	/** The kernel tracer. */
	final __KernelTraceHolder__ _trace =
		new __KernelTraceHolder__();
	
	/** Set to {@code true} when booting has been completed. */
	private volatile boolean _bootdone;
	
	/** The next process ID to choose. */
	private volatile int _nextid;
	
	/**
	 * Initializes the base kernel interface.
	 *
	 * @since 2016/05/16
	 */
	public Kernel()
	{
		// Setup debug tracer
		_trace.__setTrace(new KernelTracePrinter(System.err));
		
		// Setup kernel process
		KernelProcess kp = new KernelProcess(this, true, _nextid++);
		this._kernelprocess = kp;
		
		// Kernel process is a global one
		this._processes.add(kp);
		
		// Register the current thread
		kp.__addThread(Thread.currentThread());
		
		// Finished boot
		bootFinished(Kernel.class);
	}
	
	/**
	 * Attempts to quit the kernel, if the kernel cannot be quit then nothing
	 * happens.
	 *
	 * @since 2016/05/18
	 */
	public abstract void quitKernel();
	
	/**
	 * This is a signal to indicate that the kernel construction has completed
	 * in the child class. If the current class is not the top level class
	 * for the boot finished indicator then it is ignored.
	 *
	 * @param __top
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	protected final void bootFinished(Class<?> __top)
		throws NullPointerException
	{
		// Check
		if (__top == null)
			throw new NullPointerException("NARG");
		
		// If already complete, ignore
		if (!_bootdone)
		{
			// Inside of a parent constructor still
			if (__top != getClass())
				return;
			
			// Mark booted
			_bootdone = true;
			
			// Execute the runner
			bootFinishRunner();
		}
	}
	
	/**
	 * This method may be implemented by sub-classes and is executed after the
	 * contructors of the given classes are at the end just before they should
	 * return.
	 *
	 * Overrides of this method should call {@code super.bootFinishRunner()}
	 * at the start of the method.
	 *
	 * @since 2016/05/20
	 */
	protected void bootFinishRunner()
	{
		// Does nothing.
	}
	
	/**
	 * Creates a new process and returns it.
	 *
	 * @return The newly created process.
	 * @throws IllegalStateException If no thread owns the current process.
	 * @throws SecurityException If the current process is not permitted to
	 * create new processes.
	 * @since 2016/05/20
	 */
	public final KernelProcess createProcess()
		throws IllegalStateException, SecurityException
	{
		// {@squirreljme.error AY01
		KernelProcess me = currentProcess();
		if (me == null)
			throw new IllegalStateException("AY01");
		
		// Lock
		List<KernelProcess> kps = this._processes;
		KernelProcess rv;
		synchronized (kps)
		{
			// Check access manager
			me.accessManager().createProcess();
			
			// Create it
			rv = new KernelProcess(this, false, _nextid++);
			
			// Add it
			kps.add(rv);
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the process which is associated with the current thread.
	 *
	 * @return The process for the current thread, if {@code null} then no
	 * process owns the thread.
	 * @since 2016/05/16
	 */
	public final KernelProcess currentProcess()
	{
		return processByThread(Thread.currentThread());
	}
	
	/**
	 * Returns the kernel process.
	 *
	 * @return The kernel process.
	 * @since 2016/05/16
	 */
	public final KernelProcess kernelProcess()
	{
		return _kernelprocess;
	}
	
	/**
	 * Locates the processes which host server sockets for the given service
	 * IDs.
	 *
	 * @param __id The service ID number.
	 * @return An array of processes which are hosting the given service.
	 * @throws IllegalArgumentException If the service ID is zero or negative.
	 * @since 2016/05/21
	 */
	public final KernelProcess[] locateServer(int __id)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AY08 Zero or negative service IDs are not
		// permitted for server lookup. (The service ID)}
		if (__id <= 0)
			throw new IllegalArgumentException(String.format("AY08 %d", __id));
		
		// Return value
		KernelProcess[] rv = null;
		
		// Lock
		List<KernelProcess> kps = this._processes;
		synchronized (kps)
		{
			// Are any processes hosting a server at all?
			for (KernelProcess kp : kps)
				if (kp.hostsService(__id))
				{
					// No array?
					if (rv == null)
						rv = new KernelProcess[]{kp};
					
					// Add one entry to the array
					else
					{
						// Setup new array
						int n = rv.length;
						KernelProcess[] cr = new KernelProcess[n + 1];
						
						// Copy old values
						for (int i = 0; i < n; i++)
							cr[i] = rv[i];
						cr[n] = kp;
						
						// Set new
						rv = cr;
					}
				}
		}
		
		// No value to return?
		if (rv == null)
			return KernelProcess._NO_KERNEL_PROCESSES;
		return rv;
	}
	
	/**
	 * Locates the process that owns the given thread.
	 *
	 * @param __t The thread to get the process of.
	 * @return The process which owns the given thread or {@code null} if no
	 * process is bound to it.
	 * @since 2016/05/16
	 */
	public final KernelProcess processByThread(Thread __t)
	{
		// Lock
		List<KernelProcess> kps = this._processes;
		synchronized (kps)
		{
			// Go through all of them
			for (KernelProcess kp : kps)
				if (kp.containsThread(__t))
					return kp;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Does not return until there are no processes remaining.
	 *
	 * @throws InterruptedException If the process was interrupted during
	 * blocking.
	 * @since 2016/05/16
	 */
	public final void untilProcessless()
		throws InterruptedException
	{
		// Loop for a long time
		List<KernelProcess> processes = this._processes;
		Thread curt = Thread.currentThread();
		for (;;)
		{
			// {@squirreljme.error AY06 Thread interrupted.}
			if (curt.isInterrupted())
				throw new InterruptedException("AY06");
			
			// Lock
			synchronized (processes)
			{
				// Go through all processes
				Iterator<KernelProcess> it = processes.iterator();
				while (it.hasNext())
				{
					KernelProcess p = it.next();
					
					// If all threads in the process are dead, then remove
					// the process.
					if (!p.areAnyThreadsAlive())
						it.remove();
				}
				
				// Empty?
				if (processes.isEmpty())
					return;
			}
			
			// Sleep for a bit
			Thread.sleep(750L);
		}
	}
}

