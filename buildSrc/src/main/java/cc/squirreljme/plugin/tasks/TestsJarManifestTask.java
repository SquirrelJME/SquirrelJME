// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import java.nio.file.Path;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Adds additional properties to the manifest for the JAR.
 *
 * @deprecated Move this to {@link AdditionalManifestPropertiesTask}.
 * @since 2020/03/04
 */
@Deprecated
public class TestsJarManifestTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR task.
	 * @param __pr The process resources task.
	 * @param __sourceSet The source set used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/04
	 */
	@Inject
	public TestsJarManifestTask(Jar __jar, ProcessResources __pr,
		String __sourceSet)
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
		this.doLast(new TestJarManifestTaskAction(this.__taskOutput()));
		
		// When the JAR is created it gets additional manifests from us
		__jar.manifest(
			new ManifestTaskModifier(this.__taskOutput()));
			
		// Jar and resources depend on this task
		__jar.dependsOn(this);
		__pr.dependsOn(this);
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
