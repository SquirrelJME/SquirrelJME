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
import java.util.Objects;
import javax.inject.Inject;
import org.gradle.jvm.tasks.Jar;

/**
 * Launches the program but runs it in the Virtual Machine.
 *
 * @since 2020/02/29
 */
public class RunEmulatedTask
	extends AbstractEmulatedTask
{
	/** Main configurations. */
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
	public RunEmulatedTask(Jar __jar, String __emulator)
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
		// We either run the MIDlet or we do not
		return (__midlet != null ?
			"javax.microedition.midlet.__MainHandler__" :
			Objects.requireNonNull(__cfg.mainClass,
			"No main class in project."));
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
