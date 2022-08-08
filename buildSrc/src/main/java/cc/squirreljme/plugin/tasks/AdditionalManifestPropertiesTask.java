// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.TaskInitialization;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import cc.squirreljme.plugin.swm.SuiteDependency;
import cc.squirreljme.plugin.swm.SuiteDependencyLevel;
import cc.squirreljme.plugin.swm.SuiteDependencyType;
import cc.squirreljme.plugin.swm.SuiteName;
import cc.squirreljme.plugin.swm.SuiteVendor;
import cc.squirreljme.plugin.swm.SuiteVersion;
import cc.squirreljme.plugin.swm.SuiteVersionRange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.jar.Attributes;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.file.FileCollection;
import org.gradle.api.java.archives.Manifest;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Adds additional properties to the manifest file.
 *
 * @since 2020/02/28
 */
public class AdditionalManifestPropertiesTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR Task.
	 * @param __pr The process resources task.
	 * @param __sourceSet The source set used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	@Inject
	public AdditionalManifestPropertiesTask(Jar __jar, ProcessResources __pr,
		String __sourceSet)
		throws NullPointerException
	{
		if (__jar == null || __pr == null)
			throw new NullPointerException("No tasks specified");
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Builds an additional manifest for this project.");
		
		// Configure inputs and outputs
		Project project = this.getProject();
		this.getInputs().files(
			project.provider(project::getBuildFile));
		this.getOutputs().files(
			project.provider(this::__taskOutputAsFileCollection));
		
		// This action creates the actual manifest file
		this.doLast(new AdditionalManifestPropertiesTaskAction(
			this.__taskOutput(), __sourceSet));
			
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
	 * @since 2020/02/28
	 */
	Path __taskOutput()
	{
		return this.getProject().getBuildDir().toPath().
			resolve("SQUIRRELJME.MF");
	}
	
	/**
	 * Returns the output manifest file.
	 *
	 * @return The output manifest file.
	 * @since 2020/02/28
	 */
	private FileCollection __taskOutputAsFileCollection()
	{
		return this.getProject().files(this.__taskOutput().toFile());
	}
}
