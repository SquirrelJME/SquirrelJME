// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import org.gradle.api.internal.tasks.testing.TestExecutionSpec;

/**
 * This contains the test execution specification which configures how tests
 * are run.
 *
 * @since 2020/03/06
 */
public class EmulatedTestExecutionSpec
	implements TestExecutionSpec
{
	/** The emulator to use. */
	protected final String emulator;
	
	/**
	 * Initializes the emulation specification.
	 *
	 * @param __emu The emulator to run, defaults to SpringCoat if not set.
	 * @since 2020/03/06
	 */
	public EmulatedTestExecutionSpec(String __emu)
	{
		this.emulator = (__emu == null ? "springcoat" : "summercoat");
	}
}
