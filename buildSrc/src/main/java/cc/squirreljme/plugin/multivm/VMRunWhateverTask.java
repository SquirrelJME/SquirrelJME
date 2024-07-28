// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import java.net.URI;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;

/**
 * Run whatever Jar we want through the build system.
 *
 * @since 2024/07/28
 */
public class VMRunWhateverTask
	extends DefaultTask
{
	/** The classifier to use. */
	protected final SourceTargetClassifier classifier;
	
	/** The debug server. */
	protected final URI debugServer;
	
	/** Run main class? */
	protected final boolean mainClass;
	
	/** Run which MIDlet? */
	protected final int midlet;
	
	/**
	 * Initializes the task.
	 *
	 * @param __classifier The classifier used.
	 * @param __mainClass The main class to run.
	 * @param __midlet The MIDlet to run.
	 * @param __debugServer The debug server to use.
	 * @since 2024/07/28
	 */
	@Inject
	public VMRunWhateverTask(SourceTargetClassifier __classifier,
		String __mainClass, JavaMEMidlet __midlet, URI __debugServer)
	{
		// Store for later
		this.classifier = __classifier;
		this.mainClass = "main".equals(__mainClass);
		this.midlet = Integer.parseInt((__midlet != JavaMEMidlet.NONE ?
			__midlet.mainClass : "-1"));
		this.debugServer = __debugServer;
		
		// Describe this one
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Run a Jar using the build system.");
		
		// Running this
		this.doLast(new VMRunWhateverTaskAction());
	}
}
