// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.util.ForwardInputToOutput;
import cc.squirreljme.plugin.util.GradleLoggerOutputStream;
import cc.squirreljme.plugin.util.JavaExecSpecFiller;
import cc.squirreljme.plugin.util.SimpleJavaExecSpecFiller;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;

/**
 * Runs the program within the virtual machine.
 *
 * @since 2020/08/07
 */
public class VMRunTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/16
	 */
	public VMRunTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		// The task owning this
		VMRunTask runTask = (VMRunTask)__task;
		
		// Need this to get the program details
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(__task.getProject());
		
		// Setup detached runner then execute it
		new VMRunTaskDetached(this.classifier,
			__task.getLogger(),
			VMHelpers.runClassPath(__task,
				this.classifier, true),
			runTask.midlet,
			VMHelpers.mainClass(config, runTask.midlet),
			runTask.getProject().getBuildDir().toPath(),
			__task.getProject(),
			runTask.debugServer).run();
	}
}
