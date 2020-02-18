// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This represents a virtual machine within SummerCoat.
 *
 * @since 2019/06/30
 */
public class SummerCoatVirtualMachine
	implements VirtualMachine
{
	/** Threads currently running. */
	private final List<Thread> _threads =
		new ArrayList<>();
	
	/**
	 * Initializes the virtual machine.
	 *
	 * @param __bootcpu The boot CPU.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/30
	 */
	public SummerCoatVirtualMachine(NativeCPU __bootcpu)
		throws NullPointerException
	{
		if (__bootcpu == null)
			throw new NullPointerException("NARG");
		
		// Add initial thread
		this._threads.add(new Thread(__bootcpu, "BootCPU"));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final int runVm()
		throws VMException
	{
		// Start all threads
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Run threads
			for (int i = 0, n = threads.size(); i < n; i++)
				try
				{
					// Start the thread
					threads.get(i).start();
				}
				
				// Ignore, in the event this was called multiple times!
				catch (IllegalThreadStateException e)
				{
				}
		} 
		
		// Wait for all threads to stop
		for (;;)
		{
			// Available threads
			int count = 0;
			
			// Lock!
			synchronized (threads)
			{
				// Get iterator
				Iterator<Thread> it = threads.iterator();
				
				// Ask about all the threads
				while (it.hasNext())
				{
					// Get the thread
					Thread t = it.next();
					
					// If thread is alive, count it
					if (t.isAlive())
						count++;
					
					// Otherwise remove it
					else
						it.remove();
				}
				
				// If there are many threads alive, just wait around to not
				// waste CPU and poll periodically for the thread states
				if (count > 0)
					try
					{
						threads.wait(1000L);
					}
					catch (InterruptedException e)
					{
					}
			}
			
			// Return error for now
			if (count == 0)
			{
				todo.TODO.note("Actual return value!");
				return 1;
			}
		}
	}
}

