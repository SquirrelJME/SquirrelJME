// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VMThreadModel;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.vm.VMClassLibrary;
import java.util.Map;

/**
 * Factory which creates instances of the SpringCoat virtual machine.
 *
 * @since 2018/11/17
 */
public class SpringCoatFactory
	extends VMFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @since 2018/11/17
	 */
	public SpringCoatFactory()
	{
		super("springcoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __profiler,
		JDWPHostFactory __jdwp, VMThreadModel __threadModel,
		VMSuiteManager __suiteManager, VMClassLibrary[] __classpath,
		String __mainClass, Map<String, String> __sysProps, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Set up the main task manager which runs everything
		SpringTaskManager tm = new SpringTaskManager(__suiteManager,
			__profiler);
		
		// Bind this to the task manager which is the pure global state
		if (__jdwp != null)
			tm.jdwpController = __jdwp.open(tm); 
		
		// Spawn initial virtual machine task
		return tm.startTask(__classpath, __mainClass, __args, __sysProps,
			TaskPipeRedirectType.TERMINAL, TaskPipeRedirectType.TERMINAL,
			false, true);
	}
}

