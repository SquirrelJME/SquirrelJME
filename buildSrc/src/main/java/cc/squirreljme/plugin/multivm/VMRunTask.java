// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;

/**
 * Used to run the virtual machine.
 *
 * @since 2020/08/07
 */
public class VMRunTask
	extends DefaultTask
	implements VMBaseTask, VMExecutableTask
{
	/** Not valid for debug server. */
	public static final URI NO_DEBUG_SERVER =
		URI.create("none:none");
	
	/** JDWP Host. */
	public static final URI JDWP_HOST =
		URI.create("jdwp::5005");
	
	/** The classifier used. */
	@Internal
	@Getter
	protected final SourceTargetClassifier classifier;
	
	/** The main class to execute. */
	@Internal
	@Getter
	protected final String mainClass;
	
	/** The midlet to execute. */
	@Internal
	@Getter
	protected final JavaMEMidlet midlet;
	
	/** GDB Server location. */
	@Internal
	@Getter
	protected final URI debugServer;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __classifier The classifier used.
	 * @param __libTask The task used to create libraries, this may be directly
	 * depended upon.
	 * @param __mainClass The main class used.
	 * @param __midlet The midlet used.
	 * @param __debugServer Optional location of where GDB/JDWP is. 
	 * @since 2020/08/07
	 */
	@Inject
	public VMRunTask(SourceTargetClassifier __classifier,
		VMLibraryTask __libTask, String __mainClass, JavaMEMidlet __midlet,
		URI __debugServer)
		throws NullPointerException
	{
		// Normalize
		if (__mainClass != null && __mainClass.isEmpty())
			__mainClass = null;
		if (__midlet != null && (JavaMEMidlet.NONE == __midlet ||
			JavaMEMidlet.NONE.equals(__midlet)))
			__midlet = null;
		
		if (__classifier == null || __libTask == null ||
			(__mainClass == null && __midlet == null))
			throw new NullPointerException("NARG");
		
		if ((__mainClass == null) == (__midlet == null))
			throw new IllegalArgumentException("Both main and midlet set.");
		
		// These are used when running
		this.classifier = __classifier;
		this.mainClass = __mainClass;
		this.midlet = __midlet;
		
		// Was this actually specified?
		if (__debugServer == null ||
			__debugServer == VMRunTask.NO_DEBUG_SERVER || 
			VMRunTask.NO_DEBUG_SERVER.equals(__debugServer))
			this.debugServer = null;
		else
			this.debugServer = __debugServer;
		
		// Set details of this task
		this.setGroup("squirreljme");
		if (__mainClass != null)
			this.setDescription(String.format(
				"Runs the standard main class (%s).", __mainClass));
		else
			this.setDescription(String.format(
				"Runs the application %s.", __midlet.title));
		
		// This task depends on the various VM libraries of this class
		// depending on the dependencies along with the emulator being
		// available as well
		this.dependsOn(this.getProject().provider(
			new VMRunDependencies(this, __classifier)),
			new VMEmulatorDependencies(this,
				__classifier.getTargetClassifier()));
		
		// Only run if entry points are valid
		this.onlyIf(new CheckForEntryPoints());
		
		// Performs the action of the task
		this.doLast(new VMRunTaskAction(__classifier));
	}
}
