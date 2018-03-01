// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import cc.squirreljme.kernel.packets.LoopbackDatagramDuplex;
import cc.squirreljme.kernel.service.ServiceProvider;
import cc.squirreljme.kernel.service.ServiceProviderFactory;
import cc.squirreljme.runtime.cldc.OperatingSystemType;
import cc.squirreljme.runtime.cldc.SystemKernel;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;

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
	implements SystemKernel
{
	/** The task which represents the kernel itself. */
	protected final KernelTask systemtask;
	
	/** The kernel's loopback stream. */
	@Deprecated
	protected final LoopbackDatagramDuplex loopback =
		new LoopbackDatagramDuplex();
	
	/** Trust group for the system. */
	private final KernelTrustGroup _systemtrustgroup =
		new KernelTrustGroup();
	
	/** Tasks which exist within the kernel. */
	private final Map<Integer, KernelTask> _tasks =
		new SortedTreeMap<>();
	
	/** Services which are available for usage. */
	private final Map<Class<?>, __Service__> _services =
		new LinkedHashMap<>();
	
	/** Index to service mapping. */
	private final Map<Integer, __Service__> _servicesindex =
		new SortedTreeMap<>();
	
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
		
		// Setup service providers and map them all to the same index
		Map<Class<?>, __Service__> services = this._services;
		Map<Integer, __Service__> servicesindex = this._servicesindex;
		synchronized (services)
		{
			// Always put a blank index in
			services.put(null, null);
			servicesindex.put(0, null);
			
			// Load in providers
			int nextdx = 1;
			for (String sv : serviceclasses)
				try
				{
					// Find the custom service provider or just fall back to
					// the default
					String mapped = Objects.toString(__config.mapService(sv),
						DefaultKernelServices.mapService(sv));
					
					// Make a service for it
					__Service__ made = new __Service__(nextdx,
						Class.forName(sv),
						Class.forName(mapped));
					
					// If it worked, record it
					services.put(made._client, made);
					servicesindex.put(nextdx, made);
					
					// All services have unique IDs
					nextdx++;
				}
				
				// {@squirreljme.error AP0b Could not locate a class for the
				// specified service. (The service class)}
				catch (ClassNotFoundException e)
				{
					throw new RuntimeException(
						String.format("AP0b %s", sv), e);
				}
		}
		
		// Initialize the system task, which acts as a special task
		KernelTask systemtask = __config.systemTask(new WeakReference<>(this));
		
		// {@squirreljme.error AP03 System task initialized with the wrong
		// properties.}
		if (systemtask == null || systemtask.index() != 0)
			throw new RuntimeException("AP03");
		
		// Need to remember this task
		_tasks.put(0, systemtask);
		this.systemtask = systemtask;
	}
	
	/**
	 * Returns the operating system type that SquirrelJME is running on.
	 *
	 * @return The operating system type SquirrelJME is running on.
	 * @since 2018/01/13
	 */
	public abstract OperatingSystemType operatingSystemType();
	
	/**
	 * Returns the kernel's loopback datagram duplex.
	 *
	 * @return The kernel loopback datagram duplex.
	 * @since 2018/01/04
	 */
	public final LoopbackDatagramDuplex loopback()
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
		// This may changed during initialization
		Map<Class<?>, __Service__> services = this._services;
		synchronized (services)
		{
			return services.size();
		}
	}
	
	/**
	 * Returns the service associated with the given index.
	 *
	 * @param __dx The index to get.
	 * @return The service at the given index.
	 * @throws IndexOutOfBoundsException If the service index is not valid.
	 * @since 2018/01/05
	 */
	public final ServiceProvider serviceGet(int __dx)
		throws IndexOutOfBoundsException
	{
		__Service__ sv;
		
		// Services may be lazily initialized, and the service index shares
		// the same map set
		Map<Class<?>, __Service__> services = this._services;
		Map<Integer, __Service__> servicesindex = this._servicesindex;
		synchronized (services)
		{
			sv = servicesindex.get(__dx);
		}
		
		// {@squirreljme.error AP04 Invalid service index. (The index)}
		if (sv == null)
			throw new IndexOutOfBoundsException(
				String.format("AP04 %d", __dx));
		
		// The service can be initialize outside of the lock because there is
		// another lock in the service itself
		return sv.__provider();
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
		
		// Lock on the services map because this could be lazily initialized
		Map<Class<?>, __Service__> services = this._services;
		synchronized (services)
		{
			__Service__ sv = services.get(__cl);
			if (sv == null)
				return 0;
			
			return sv._index;
		}
	}
	
	/**
	 * Returns the task which represents the kernel.
	 *
	 * @return The task representing the kernel.
	 * @since 2018/03/01
	 */
	public final KernelTask systemTask()
	{
		return this.systemtask;
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
			// {@squirreljme.error AP05 The specified task does not exist.
			// (The task index)}
			KernelTask rv = tasks.get(__id);
			if (rv == null)
				throw new NoSuchKernelTaskException(
					String.format("AP05 %d", __id));
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
	
	/**
	 * Lists tasks.
	 *
	 * @param __incsys Should system tasks be included?
	 * @return The list of tasks.
	 * @since 2018/01/06
	 */
	public final KernelTask[] taskList(boolean __incsys)
	{
		Map<Integer, KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			List<KernelTask> rv = new ArrayList<>(tasks.size());
			
			for (KernelTask t : tasks.values())
				rv.add(t);
			
			return rv.<KernelTask>toArray(new KernelTask[tasks.size()]);
		}
	}
	
	/**
	 * Returns the system trust group.
	 *
	 * @return The system trust group.
	 * @since 2018/01/11
	 */
	final KernelTrustGroup __systemTrustGroup()
	{
		return this._systemtrustgroup;
	}
	
	/**
	 * This represents an available service.
	 *
	 * @since 2018/01/15
	 */
	private static class __Service__
	{
		/** The index of the service. */
		final int _index;
		
		/** The client class */
		final Class<?> _client;
		
		/** The provider class. */
		final Class<?> _provider;
		
		/** The provider for the service. */
		private volatile ServiceProvider _instance;
		
		/**
		 * Initializes the service.
		 *
		 * @param __dx The service index.
		 * @param __client The client class.
		 * @param __provider The server provider class.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/15
		 */
		private __Service__(int __dx, Class<?> __client, Class<?> __provider)
			throws NullPointerException
		{
			if (__client == null || __provider == null)
				throw new NullPointerException("NARG");
			
			this._index = __dx;
			this._client = __client;
			this._provider = __provider;
		}
		
		/**
		 * Initializes the service provider.
		 *
		 * @return The service provider.
		 * @since 2018/01/15
		 */
		final ServiceProvider __provider()
		{
			// The service may need to be initialized accordingly
			synchronized (this)
			{
				// Is lazilly initialized
				ServiceProvider rv = this._instance;
				if (rv == null)
					try
					{
						rv = ((ServiceProviderFactory)this._provider.
							newInstance()).create();
						
						// {@squirreljme.error AP01 The server provides a
						// service for a different client than than the one
						// which was expected. (The expected client class;
						// The one the service provides)}
						Class<?> clclass = rv.clientClass(),
							wantcl = this._client;
						if (wantcl != clclass)
							throw new RuntimeException(String.format(
								"AP01 %s %s", wantcl, clclass));
						
						// It now exists, so it may be used again
						this._instance = rv;
					}
					
					// {@squirreljme.error AP02 Could not initialize the
					// service. (The service class; The provider class)}
					catch (ClassCastException|IllegalAccessException|
						InstantiationException e)
					{
						throw new RuntimeException(
							String.format("AP02 %s %s", this._client,
							this._provider), e);
					}
				
				return rv;
			}
		}
	}
}

