// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.host.views.JDWPViewThreadGroup;
import cc.squirreljme.vm.springcoat.exceptions.SpringMachineExitException;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueString;

/**
 * A view over a group of threads, in SpringCoat this is an individual machine.
 *
 * @since 2021/04/10
 */
public class DebugViewThreadGroup
	implements JDWPViewThreadGroup
{
	/**
	 * Initializes the thread group viewer.
	 * 
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewThreadGroup()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/25
	 */
	@Override
	public Object[] allTypes(Object __which)
	{
		return ((SpringMachine)__which).classLoader().loadedClasses();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/30
	 */
	@Override
	public void exit(Object __which, int __code)
	{
		try
		{
			((SpringMachine)__which).exit(__code);
		}
		catch (SpringMachineExitException ignored)
		{
			// We do not to throw the exception out, since we are exiting
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof SpringMachine);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/18
	 */
	@Override
	public Object findType(Object __which, String __name)
	{
		return ((SpringMachine)__which).classLoader()
			.loadClass(new ClassName(__name));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/24
	 */
	@Override
	public Object instance(Object __threadGroup)
	{
		// The context is always our own task object
		return ((SpringMachine)__threadGroup)
			.taskObject((SpringMachine)__threadGroup);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2026/09/30
	 */
	@Override
	public Object internString(Object __threadGroup, String __string)
	{
		// No string or the thread group is invalid?
		if (__threadGroup == null || __string == null)
			return null;
		
		// This needs to happen in a worker thread that cannot be suspended
		// as the String is just created out of nowhere without any context
		// thread
		try (CallbackThread callback = ((SpringMachine)__threadGroup)
			.obtainCallbackThread(true))
		{
			// We can treat this just as a some intern string
			return callback.thread()._worker
				.asVMObject(new ConstantValueString(__string));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public String name(Object __which)
	{
		return ((SpringMachine)__which).toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public Object[] threads(Object __which)
	{
		// Return all of the threads for this group
		return ((SpringMachine)__which).getThreads();
	}
}
