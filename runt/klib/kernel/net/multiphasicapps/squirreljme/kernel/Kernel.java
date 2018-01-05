// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServer;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServerFactory;

/**
 * This class represents the micro-kernel which manages the entire SquirrelJME
 * system and all of the needed IPC and running tasks/threads.
 *
 * There must be no way for an instance of this class to be obtained by any
 * client library, this means that once the kernel is initialized and passed to
 * an API bridge the pointer should be tossed out. It can however be
 * initialized as normal.
 *
 * @since 2017/12/08
 */
public abstract class Kernel
{
	/** The task which represents the kernel itself. */
	protected final KernelTask systemtask;
	
	/** The kernel's loopback stream. */
	protected final LoopbackStreams loopback;
	
	/** Tasks which exist within the kernel. */
	private final Map<Integer, KernelTask> _tasks =
		new SortedTreeMap<>();
	
	/**
	 * Servers which are available for usage by the kernel.
	 * Services are indexed by an integer value for speed to prevent massive
	 * lookups based on class types or strings every time they are used.
	 */
	private final ServiceServer[] _servers;
	
	/**
	 * Initializes the base kernel.
	 *
	 * @param __config The configuration to use when initializing the kernel.
	 * @since 2018/01/02
	 */
	protected Kernel(KernelConfiguration __config)
		throws NullPointerException
	{
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Fill in the services which are potentially going to exist, use the
		// ones provided by the implementation configuration
		Set<String> serviceclasses = new LinkedHashSet<>();
		for (String s : __config.services())
			if (s != null)
				serviceclasses.add(s);
		
		// Add ones which are provided by default
		for (String s : DefaultKernelServices.services())
			if (s != null)
				serviceclasses.add(s);
		
		// Always make the zero index server invalid
		List<ServiceServer> servers = new ArrayList<>();
		servers.add(null);
		
		// Go through and attempt to locate implementations of services that
		// clients can use, the mapping is the client used class to the server
		// factory class. This means that for each client class, there may
		// only be one server which exists for it
		for (String sv : serviceclasses)
		{
			// The created server service
			ServiceServer rv = null;
			
			// Map to the config and the default
			for (int i = 0; i < 2; i++)
			{
				String mapped = (i == 0 ? __config.mapService(sv) :
					DefaultKernelServices.mapService(sv));
				if (mapped != null)
					try
					{
						Class<?> svclass = Class.forName(mapped);
						ServiceServer s = ((ServiceServerFactory)svclass.
							newInstance()).createServer();
						
						// Make sure that the client class for the server
						// matches the service string, otherwise odd things
						// will happen
						// {@squirreljme.error AP05 The server provides a
						// service for a different client than than the one
						// which was expected. (The server class; The expected
						// client class; The actual client class)}
						Class<?> clclass = s.clientClass();
						if (!sv.equals(clclass.getName()))
							throw new RuntimeException(String.format(
								"AP05 %s %s %s", svclass, sv, clclass));
						
						// Is okay!
						rv = s;
					}
				
					// {@squirreljme.error AP01 Could not initialize the
					// service. (The service class)}
					catch (IllegalAccessException|InstantiationException e)
					{
						throw new RuntimeException(
							String.format("AP01 %s", sv), e);
					}
				
					// No such class exists
					catch (ClassNotFoundException e)
					{
						mapped = null;
					}
				
				// Service mapped
				if (rv != null)
					break;
			}
			
			// Store service and make its instance available
			if (rv != null)
				servers.add(rv);
		}
		
		// Store services for later usage by tasks
		this._servers = servers.<ServiceServer>toArray(
			new ServiceServer[servers.size()]);
		
		// Initialize the system task, which acts as a special task
		this.loopback = new LoopbackStreams();
		KernelTask systemtask = __config.systemTask(new WeakReference<>(this));
		
		// {@squirreljme.error AP02 System task initialized with the wrong
		// properties.}
		if (systemtask == null || systemtask.index() != 0)
			throw new RuntimeException("AP02");
		
		// Need to remember this task
		_tasks.put(0, systemtask);
		this.systemtask = systemtask;
	}
	
	/**
	 * Returns the kernel's loopback stream.
	 *
	 * @return The kernel loopback stream.
	 * @since 2018/01/04
	 */
	public final LoopbackStreams loopback()
	{
		return this.loopback;
	}
	
	/**
	 * Returns the number of services which are available.
	 *
	 * @return The number of available services.
	 * @since 2018/01/04
	 */
	public final int serviceCount()
	{
		return this._servers.length;
	}
	
	/**
	 * Returns the service associated with the given index.
	 *
	 * @param __dx The index to get.
	 * @return The service at the given index.
	 * @throws IndexOutOfBoundsException If the service index is not valid.
	 * @since 2018/01/05
	 */
	public final ServiceServer serviceGet(int __dx)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AP06 Invalid service index. (The index)}
		ServiceServer[] servers = this._servers;
		if (__dx <= 0 || __dx > servers.length)
			throw new IndexOutOfBoundsException(
				String.format("AP06 %d", __dx));
		return this._servers[__dx];
	}
	
	/**
	 * Returns the service index for the given class.
	 *
	 * @param __cl The class to get the service index for.
	 * @return The index for the class or {@code 0} if there is no service.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final int serviceIndex(Class<?> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Go through services and ask them each if they implement for the
		// given class.
		ServiceServer[] servers = this._servers;
		for (int i = 1, n = servers.length; i < n; i++)
			if (__cl == servers[i].clientClass())
				return i;
		
		// No service found
		return 0;
	}
	
	/**
	 * Returns the task associated with the given index.
	 *
	 * @param __id The index of the task to get.
	 * @return The task associated with the given index.
	 * @throws NoSuchKernelTaskException If no such task exists by that index.
	 * @since 2018/01/02
	 */
	public final KernelTask taskByIndex(int __id)
		throws NoSuchKernelTaskException
	{
		Map<Integer, KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			// {@squirreljme.error AP0g The specified task does not exist.
			// (The task index)}
			KernelTask rv = tasks.get(__id);
			if (rv == null)
				throw new NoSuchKernelTaskException(
					String.format("AP0g %d", __id));
			return rv;
		}
	}
	
	/**
	 * Launches the specified task.
	 *
	 * @param __l The properties of the task to launch.
	 * @return The launched task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public final KernelTask taskLaunch(KernelTaskLaunch __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

