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
import cc.squirreljme.plugin.multivm.MultiVMHelpers;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import javax.inject.Inject;
import org.gradle.api.Task;

/**
 * Launches the program but runs it in the Virtual Machine.
 *
 * @since 2020/02/29
 */
@Deprecated
public class RunEmulatedTask
	extends AbstractEmulatedTask
{
	/** Main configurations. */
	@Deprecated
	private static final String[] _MAIN_CONFIGS =
		new String[]{"api", "implementation"};
	
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR task.
	 * @param __emulator The emulator to use.
	 * @since 2020/02/29
	 */
	@Inject
	public RunEmulatedTask(Task __jar, String __emulator)
	{
		super(__jar, __emulator, RunEmulatedTask._MAIN_CONFIGS);
		
		// Setup task
		this.setDescription("Launches the program with " + __emulator + ".");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected String mainClass(SquirrelJMEPluginConfiguration __cfg,
		JavaMEMidlet __midlet)
	{
		return MultiVMHelpers.mainClass(__cfg, __midlet);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected boolean useMidlet(JavaMEMidlet __midlet)
	{
		return __midlet != null;
	}
}
