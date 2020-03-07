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
import org.gradle.jvm.tasks.Jar;

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
	
	/** The JAR to test. */
	protected final Jar jar;
	
	/**
	 * Initializes the emulation specification.
	 *
	 * @param __emu The emulator to run, defaults to SpringCoat if not set.
	 * @param __jar The JAR to test.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/06
	 */
	public EmulatedTestExecutionSpec(String __emu, Jar __jar)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("No JAR specified.");
		
		this.emulator = (__emu == null ? "springcoat" : "summercoat");
		this.jar = __jar;
	}
}
