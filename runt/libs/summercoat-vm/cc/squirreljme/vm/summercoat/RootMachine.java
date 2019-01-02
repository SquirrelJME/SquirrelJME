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

import cc.squirreljme.vm.VMSuiteManager;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This class contains the root machine which is used to contain and manage
 * all of the various virtual machines which are running in SummerCoat. This
 * is essentially a hub which owns all of the sub virtual machines, handling
 * creation of them and such.
 *
 * @since 2019/01/01
 */
public final class RootMachine
{
	/** The manager for suites. */
	protected final VMSuiteManager suites;
	
	/** The profiler information output. */
	protected final ProfilerSnapshot profiler;
	
	/** The base depth of this virtual machine. */
	protected final int baseguestdepth;
	
	/**
	 * Initializes the root machine.
	 *
	 * @param __s The suite manager.
	 * @param __p The profiler snapshot output, this is optional.
	 * @param __gd The guest depth of this VM.
	 * @throws NullPointerException On null arguments, except for {@code __p}.
	 * @since 2019/01/01
	 */
	public RootMachine(VMSuiteManager __s, ProfilerSnapshot __p, int __gd)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.suites = __s;
		this.profiler = __p;
		this.baseguestdepth = __gd;
	}
}

