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
import cc.squirreljme.plugin.swm.SuiteDependency;
import cc.squirreljme.plugin.swm.SuiteDependencyLevel;
import cc.squirreljme.plugin.swm.SuiteDependencyType;
import cc.squirreljme.plugin.swm.SuiteName;
import cc.squirreljme.plugin.swm.SuiteVersion;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.jar.Attributes;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * The action for {@link TestsJarManifestTask}.
 *
 * @deprecated Move this to {@link AdditionalManifestPropertiesTask}.
 * @since 2022/07/10
 */
@Deprecated
public class TestJarManifestTaskAction
	implements Action<Task>
{
	/** The task output. */
	final Path _taskOutput;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __taskOutput The task output.
	 * @since 2022/07/10
	 */
	public TestJarManifestTaskAction(Path __taskOutput)
	{
		this._taskOutput = __taskOutput;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/07/10
	 */
	@Override
	public void execute(Task __task)
	{
		// Get the project and the config details
		Project project = __task.getProject();
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(project);
		
		// Setup manifest to write into
		java.util.jar.Manifest javaManifest = new java.util.jar.Manifest();
		Attributes attributes = javaManifest.getMainAttributes();
		
		// Set manifest to 1.0
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		
		// MIDlet properties
		attributes.putValue("MIDlet-Name",
			"Tests for " + project.getName());
		attributes.putValue("MIDlet-Version",
			new SuiteVersion(project.getVersion().toString()).toString());
		attributes.putValue("MIDlet-Vendor", config.swmVendor);
		
		// First depend on TAC
		attributes.putValue("MIDlet-Dependency-1",
			new SuiteDependency(SuiteDependencyType.PROPRIETARY,
				SuiteDependencyLevel.REQUIRED,
				new SuiteName("squirreljme.project@tac"), null,
				null).toString());
		
		// Then depend on what we are testing
		attributes.putValue("MIDlet-Dependency-2",
			new SuiteDependency(SuiteDependencyType.PROPRIETARY,
				SuiteDependencyLevel.REQUIRED,
				new SuiteName("squirreljme.project@" + project.getName()),
				null, null).toString());
		
		// SquirrelJME specific indicator that this is for testing
		attributes.putValue("X-SquirrelJME-Tests", "true");
		
		// Main entry point is always the TAC test runner
		attributes.putValue("Main-Class",
			"net.multiphasicapps.tac.MainSuiteRunner");
		
		// Write the manifest output
		try (OutputStream out = Files.newOutputStream(this._taskOutput,
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.WRITE))
		{
			javaManifest.write(out);
			
			// Make sure it is really written!
			out.flush();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
