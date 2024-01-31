// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.hosted;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VMThreadModel;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.vm.VMClassLibrary;
import java.util.Map;

/**
 * Factory for creating hosted virtual machines.
 *
 * @since 2023/12/03
 */
public class HostedVMFactory
	extends VMFactory
{
	/**
	 * Initializes the base factory.
	 * 
	 * @since 2023/12/03
	 */
	public HostedVMFactory()
	{
		super("hosted");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/03
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __ps,
		JDWPHostFactory __jdwp, VMThreadModel __threadModel,
		VMSuiteManager __suiteManager, VMClassLibrary[] __classPath,
		String __mainClass, Map<String, String> __sysProps, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// This is just an opaque wrapper interface
		return new HostedVirtualMachine(__jdwp, __suiteManager, __classPath,
			__mainClass, __sysProps, __args);
	}
}
