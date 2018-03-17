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

import cc.squirreljme.runtime.cldc.service.NoSuchServiceException;
import cc.squirreljme.runtime.cldc.system.SystemCallDispatch;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.system.type.ClassType;
import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.cldc.task.KernelTask;

/**
 * This implements dispatch to the kernel for system call related operations.
 *
 * @since 2018/03/14
 */
public final class KernelSystemDispatch
	implements SystemCallDispatch
{
	/** Kernel service manager. */
	protected final KernelServices services;
	
	/**
	 * Initializes the system dispatch for the kernel side.
	 *
	 * @param __sv The service manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public KernelSystemDispatch(KernelServices __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		this.services = __sv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final Object dispatch(SystemFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
				// This is not needed, it just indicates that the kernel is
				// initialized and a main program is ready to execute
			case INITIALIZED:
				return VoidType.INSTANCE;
				
			case SERVICE_CALL:
				return __serviceCall(__args);
				
			case SERVICE_COUNT:
				return __serviceCount();
			
			case SERVICE_QUERY_CLASS:
				return __serviceQueryClass((Integer)__args[0]);
			
			case SERVICE_QUERY_INDEX:
				return __serviceQueryIndex((ClassType)__args[0]);
			
				// {@squirreljme.error AP03 Unimplemented kernel function.
				// (The kernel function)}
			default:
				throw new RuntimeException(String.format("AP03 %s", __func));
		}
	}
	
	/**
	 * Call into a service.
	 *
	 * @param __args The arguments to the service call.
	 * @return The value return from the call.
	 * @since 2018/03/16
	 */
	private final Object __serviceCall(Object[] __args)
	{
		// Extract arguments and function to the call
		int svdx = (Integer)__args[0];
		EnumType func = (EnumType)__args[1];
		
		// Expand arguments of the call
		int n = __args.length;
		Object[] passed = new Object[n - 2];
		for (int i = 2, o = 0; i < n; i++, o++)
			passed[o] = __args[i];
		
		// Forward the call to the service manager
		return this.services.byIndex(svdx).server(KernelTask.INSTANCE).
			serviceCall(func, passed);
	}
	
	/**
	 * Returns the number of services which exist.
	 *
	 * @return The count of available services.
	 * @since 2018/03/15
	 */
	private final int __serviceCount()
	{
		return this.services.count();
	}
	
	/**
	 * Queries the client class which is used to initialize a service.
	 *
	 * @param __dx The index of the class.
	 * @return The class the client uses to initialize the service.
	 * @since 2018/03/15
	 */
	private final ClassType __serviceQueryClass(int __dx)
	{
		return new ClassType(this.services.byIndex(__dx).
			clientProviderClass());
	}
	
	/**
	 * Queries the index of the service that provides for the given class.
	 *
	 * @param __cl The class to query.
	 * @return The index of the given service or a negative value if it is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	private final int __serviceQueryIndex(ClassType __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return this.services.byClientClass(__cl.name()).index();
		}
		catch (NoSuchServiceException e)
		{
			return -1;
		}
	}
}

