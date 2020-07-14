// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VirtualMachine;
import java.util.Map;
import cc.squirreljme.emulator.profiler.ProfilerSnapshot;

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
	protected VirtualMachine createVM(ProfilerSnapshot __ps,
		VMSuiteManager __sm, VMClassLibrary[] __cp, String __maincl,
		Map<String, String> __sprops, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Create a new instance of the VM
		SpringTaskManager tm = new SpringTaskManager(__sm, __ps);
		return tm.startTask(__cp, __maincl, __args, __sprops,
			TaskPipeRedirectType.TERMINAL, TaskPipeRedirectType.TERMINAL,
			false);
	}
}

