// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import javax.inject.Inject;
import org.gradle.jvm.tasks.Jar;

/**
 * This is an emulated task which is precisely for running tests.
 *
 * @since 2020/03/06
 */
public class RunTestEmulatedTask
	extends AbstractEmulatedTask
{
	/** Test configurations. */
	private static final String[] _TEST_CONFIGS =
		new String[]{"testApi", "testImplementation"};
	
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR task.
	 * @param __emulator The emulator to use.
	 * @since 2020/02/29
	 */
	@Inject
	public RunTestEmulatedTask(Jar __jar, String __emulator)
	{
		super(__jar, __emulator, RunTestEmulatedTask._TEST_CONFIGS);
		
		// Setup task
		this.setDescription("Tests the program with " + __emulator + ".");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected String mainClass(SquirrelJMEPluginConfiguration __cfg,
		JavaMEMidlet __midlet)
	{
		// Always the TAC test runner is used as the main entry because even
		// though the tests are MIDlets, we want more power in running them
		return "net.multiphasicapps.tac.MainTestRunner";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected boolean useMidlet(JavaMEMidlet __midlet)
	{
		// Never use the MIDlet because we want our specific main class
		return false;
	}
}
