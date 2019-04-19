// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMFactory;
import cc.squirreljme.vm.VMSuiteManager;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This is the factory which is capable of creating instances of the
 * SummerCoat virtual machine.
 *
 * @since 2018/12/29
 */
public class SummerCoatFactory
	extends VMFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @since 2018/12/29
	 */
	public SummerCoatFactory()
	{
		super("summercoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/29
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __ps,
		VMSuiteManager __sm, VMClassLibrary[] __cp, String __maincl,
		boolean __ismid, int __gd, Map<String, String> __sprops,
		String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Setup root machine which has our base suite manager
		RootMachine rm = new RootMachine(__sm, __ps, __gd);
		
		// Need to map to cached VMs
		CachingSuiteManager suites = rm.suites;
		int n = __cp.length;
		CachingClassLibrary[] libs = new CachingClassLibrary[n];
		for (int i = 0; i < n; i++)
			libs[i] = suites.loadLibrary(__cp[i]);
		
		// Now create the starting main task
		return new ExitAwaiter(rm.statuses, rm.createTask(libs, __maincl,
			__ismid, __sprops, __args).status, __ps);
	}
}

