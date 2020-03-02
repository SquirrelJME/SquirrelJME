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
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import java.util.Objects;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.jvm.tasks.Jar;
import org.gradle.process.JavaExecSpec;

/**
 * Launches the program using the host system which is backed by the
 * base emulator.
 *
 * @since 2020/02/29
 */
public class RunNativeTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR task.
	 * @since 2020/02/29
	 */
	@Inject
	public RunNativeTask(Jar __jar)
	{
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Runs the program using the emulator.");
		
		// This only runs if this is an application
		this.onlyIf(this::__onlyIf);
		
		// Run the task accordingly
		this.doLast(new __ActionTask__());
		
		// This needs the JAR task and the emulation task
		this.dependsOn(__jar,
			(Callable<Object>)this::__findEmulatorJarTask);
	}
	
	/**
	 * Finds the emulator JAR task.
	 *
	 * @return The emulator JAR task.
	 * @since 2020/02/29
	 */
	private Object __findEmulatorJarTask()
	{
		return Objects.requireNonNull(this.getProject().getRootProject().
			findProject(":emulators:emulator-base"),
			"No emulator base?").getTasks().getByName("jar");
	}
	
	/**
	 * Only runs if these conditions are met.
	 *
	 * @param __task Checks this task.
	 * @return If this can run or not.
	 * @since 2020/02/29
	 */
	private boolean __onlyIf(Task __task)
	{
		// Only allow if an application!
		return SquirrelJMEPluginConfiguration.configuration(this.getProject())
			.swmType == JavaMEMidletType.APPLICATION;
	}
	
	/**
	 * Returns the project classpath.
	 *
	 * @param __project The project.
	 * @return The class path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/29
	 */
	static Object __projectClassPath(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
		
		return __project.getConfigurations().
			getByName("runtimeClasspath").getFiles();
	}
	
	/**
	 * Performs the task action.
	 *
	 * @since 2020/02/29
	 */
	private class __ActionTask__
		implements Action<Task>
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/02/29
		 */
		@Override
		public void execute(Task __task)
		{
			// Get current and emulator projects
			Project project = RunNativeTask.this.getProject();
			Project emuBase = project.getRootProject().
				project(":emulators:emulator-base");
			
			// Need this to get the program details
			SquirrelJMEPluginConfiguration config =
				SquirrelJMEPluginConfiguration.configuration(project);
			
			// Execute program
			project.javaexec((JavaExecSpec __spec) ->
				{
					// Are we launching a MIDlet?
					JavaMEMidlet midlet = null;
					if (!config.midlets.isEmpty())
						midlet = config.midlets.get(0);
					
					// Which class do we launch?
					__spec.setMain((midlet != null ?
						"javax.microedition.midlet.__MainHandler__" :
						Objects.requireNonNull(config.mainClass,
						"No main class in project.")));
					
					// We need to tell the MIDlet launcher what our main entry
					// point is going to be
					if (midlet != null)
						__spec.args(midlet.mainClass);
					
					// Set the classpath needed
					__spec.classpath(
						((Jar)project.getTasks().getByName("jar"))
							.getOutputs().getFiles(),
						((Jar)emuBase.getTasks().getByName("jar"))
							.getOutputs().getFiles(),
						RunNativeTask.__projectClassPath(project),
						RunNativeTask.__projectClassPath(emuBase));
				});
		}
	}
}
