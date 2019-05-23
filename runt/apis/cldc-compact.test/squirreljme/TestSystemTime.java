// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme;

import cc.squirreljme.runtime.cldc.vki.Assembly;
import cc.squirreljme.runtime.cldc.vki.SystemCallIndex;
import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests that the system time can be obtained without error.
 *
 * @since 2019/05/23
 */
public class TestSystemTime
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/23
	 */
	@Override
	public void test()
	{
		Assembly.sysCall(SystemCallIndex.TIME_LO_MILLI_WALL);
		this.secondary("mwl", Assembly.sysCallV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.TIME_LO_MILLI_WALL));
		
		Assembly.sysCall(SystemCallIndex.TIME_HI_MILLI_WALL);
		this.secondary("mwh", Assembly.sysCallV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.TIME_HI_MILLI_WALL));
		
		Assembly.sysCall(SystemCallIndex.TIME_LO_NANO_MONO);
		this.secondary("nml", Assembly.sysCallV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.TIME_LO_NANO_MONO));
		
		Assembly.sysCall(SystemCallIndex.TIME_HI_NANO_MONO);
		this.secondary("nmh", Assembly.sysCallV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.TIME_HI_NANO_MONO));
	}
}

