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
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.java.archives.Manifest;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Adds additional properties to the manifest for the JAR.
 *
 * @since 2020/03/04
 */
public class TestsJarManifestTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR task.
	 * @param __pr The process resources task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/04
	 */
	@Inject
	public TestsJarManifestTask(Jar __jar, ProcessResources __pr)
		throws NullPointerException
	{
		if (__jar == null || __pr == null)
			throw new NullPointerException("No tasks specified");
			
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Builds the test manifest.");
		
		// Configure inputs and outputs
		Project project = this.getProject();
		this.getInputs().files(
			project.provider(project::getBuildFile));
		this.getOutputs().files(
			project.provider(this::__taskOutputAsFileCollection));
			
		// Jar and resources depend on this task
		__jar.dependsOn(this);
		__pr.dependsOn(this);
		
		// This action creates the actual manifest file
		this.doLast(new Action<Task>()
			{
				@Override
				public void execute(Task __task)
				{
					TestsJarManifestTask.this.__doLast(__task);
				}
			});
		
		// When the JAR is created it gets additional manifests from us
		__jar.manifest(new Action<Manifest>()
			{
				@Override
				public void execute(Manifest __manifest)
				{
					__manifest.from(TestsJarManifestTask.this
						.__taskOutput().toFile());
				}
			});
			
		// Jar and resources depend on this task
		__jar.dependsOn(this);
		__pr.dependsOn(this);
	}
	
	/**
	 * Configures the manifest with additional properties.
	 *
	 * @param __task The task to be configured.
	 * @since 2020/03/04
	 */
	void __doLast(Task __task)
	{
		// Get the project and the config details
		Project project = this.getProject();
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(project);
		
		// Setup manifest to write into
		java.util.jar.Manifest javaManifest = new java.util.jar.Manifest();
		Attributes attributes = javaManifest.getMainAttributes();
		
		// Set manifest to 1.0
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		
		// MIDlet propetties
		attributes.putValue("MIDlet-Name",
			"Tests for " + project.getName());
		attributes.putValue("MIDlet-Version",
			new SuiteVersion(project.getVersion().toString()).toString());
		attributes.putValue("MIDlet-Vendor",
			config.swmVendor);
		
		// First depend on TAC
		attributes.putValue("MIDlet-Dependency-1", new SuiteDependency(
			SuiteDependencyType.PROPRIETARY, SuiteDependencyLevel.REQUIRED,
			new SuiteName("squirreljme.project@tac"), null,
			null).toString());
			
		// Then depend on what we are testing
		attributes.putValue("MIDlet-Dependency-2", new SuiteDependency(
			SuiteDependencyType.PROPRIETARY, SuiteDependencyLevel.REQUIRED,
			new SuiteName("squirreljme.project@" + project.getName()),
			null, null).toString());
		
		// SquirrelJME specific indicator that this is for testing
		attributes.putValue("X-SquirrelJME-Tests", "true");
		
		// Main entry point is always the TAC test runner
		attributes.putValue("Main-Class",
			"net.multiphasicapps.tac.MainTestRunner");
		
		// Write the manifest output
		try (OutputStream out = Files.newOutputStream(this.__taskOutput(),
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
	
	/**
	 * Returns the output manifest file.
	 *
	 * @return The output manifest file.
	 * @since 2020/03/04
	 */
	Path __taskOutput()
	{
		return this.getProject().getBuildDir().toPath().
			resolve("SQUIRRELJME-TEST.MF");
	}
	
	/**
	 * Returns the output manifest file.
	 *
	 * @return The output manifest file.
	 * @since 2020/03/04
	 */
	private FileCollection __taskOutputAsFileCollection()
	{
		return this.getProject().files(this.__taskOutput().toFile());
	}
}
