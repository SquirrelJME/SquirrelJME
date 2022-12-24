// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VMThreadModel;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jdwp.JDWPFactory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.SpringCoatFactory;
import java.util.Map;

/**
 * Factory for creating the SummerCoat test interpreter.
 *
 * @since 2022/12/23
 */
public class SummerCoatFactory
	extends VMFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/17
	 */
	public SummerCoatFactory()
		throws NullPointerException
	{
		super("summercoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/12/23
	 */
	@Override
	public VirtualMachine createVM(ProfilerSnapshot __ps,
		JDWPFactory __jdwp, VMThreadModel __threadModel, VMSuiteManager __sm,
		VMClassLibrary[] __cp, String __maincl, Map<String, String> __sprops,
		String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		return new SpringCoatFactory().createVM(__ps, __jdwp, __threadModel,
			__sm, __cp, __maincl, __sprops, __args);
	}
}
