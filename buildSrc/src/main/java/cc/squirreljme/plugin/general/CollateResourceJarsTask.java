// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.multivm.BangletVariant;
import cc.squirreljme.plugin.multivm.ClutterLevel;
import cc.squirreljme.plugin.multivm.VMHelpers;
import cc.squirreljme.plugin.multivm.VMRunUpToDateWhen;
import cc.squirreljme.plugin.multivm.VMType;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Collates resource Jars for the standalone build.
 *
 * @since 2024/03/04
 */
public class CollateResourceJarsTask
	extends DefaultTask
{
	/** The clutter level to package. */
	protected final ClutterLevel clutterLevel;
	
	/**
	 * Initializes the task.
	 *
	 * @param __processResources The process resources task.
	 * @param __clutterLevel The clutter level used.
	 * @param __jarRoot The output Jar root.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/04
	 */
	@Inject
	public CollateResourceJarsTask(ProcessResources __processResources,
		ClutterLevel __clutterLevel, String __jarRoot)
		throws NullPointerException
	{
		if (__processResources == null || __clutterLevel == null ||
			__jarRoot == null)
			throw new NullPointerException("NARG");
		
		this.clutterLevel = __clutterLevel;
		
		// Set details
		this.setGroup("squirreljme");
		this.setDescription("Collates all files for standalone JARs.");
		
		// Must run after this
		this.mustRunAfter(__processResources);
		
		// Dependencies
		Task fullTask = this.getProject().getRootProject().getTasks()
			.getByName(this.fullSourceTaskName());
		this.dependsOn(VMHelpers.fullSuiteLibrariesTasks(fullTask));
		
		// Inputs
		this.getInputs().files(VMHelpers.fullSuiteLibraries(fullTask));
		
		// Outputs
		this.getOutputs().files(new CollateResourceJarsTaskOutputs(
			this, __processResources, __jarRoot));
		
		// Potential up-to-date check to make sure nothing is stale
		this.getOutputs().upToDateWhen(new VMRunUpToDateWhen(
			new SourceTargetClassifier(
			SourceSet.MAIN_SOURCE_SET_NAME, VMType.SPRINGCOAT,
			BangletVariant.NONE, __clutterLevel)));
		
		this.doLast(new CollateResourceJarsTaskAction(__processResources,
			__jarRoot));
	}
	
	/**
	 * The name of the full task to source from.
	 *
	 * @return The name of the full task to source from.
	 * @since 2024/03/04
	 */
	public final String fullSourceTaskName()
	{
		return "fullSpringCoat" + this.clutterLevel.properNoun();
	}
}
