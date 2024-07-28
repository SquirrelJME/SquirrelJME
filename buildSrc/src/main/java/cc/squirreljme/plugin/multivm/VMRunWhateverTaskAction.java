// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.swm.JavaMEMidlet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Action for the running of whatever task.
 *
 * @since 2024/07/28
 */
public class VMRunWhateverTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public void execute(Task __task)
	{
		VMRunWhateverTask runTask = (VMRunWhateverTask)__task;
		
		// Get the Jar to Run
		String jar = System.getProperty("jar");
		if (jar == null || jar.isEmpty())
			jar = System.getenv("SQUIRRELJME_JAR");
		
		// This must be here
		if (jar == null || jar.isEmpty())
			throw new RuntimeException("`jar` system property is not set.");
		
		// Targets
		String availableMain = null;
		Map<Integer, JavaMEMidlet> availableMIDlets = new LinkedHashMap<>();
		
		// Load in Jar
		try (ZipFile zip = new ZipFile(Paths.get(jar).toFile()))
		{
			// We need to get the manifest
			ZipEntry entry = zip.getEntry("META-INF/MANIFEST.MF");
			if (entry == null)
				throw new RuntimeException("Jar has no manifest.");
			
			// Parse manifest
			Attributes attr;
			try (InputStream in = zip.getInputStream(entry))
			{
				attr = new Manifest(in).getMainAttributes();
			}
			
			// Is there a main class?
			availableMain = attr.getValue("Main-Class");
			
			// Find MIDlet properties (MIDlet-1: Name, icon.png, main)
			for (int i = 1; i <= 10; i++)
			{
				// Is there something here?
				String maybe = attr.getValue("MIDlet-" + i);
				if (maybe == null || maybe.isEmpty())
					continue;
				
				// Need all three to parse
				String[] splice = maybe.trim().split(Pattern.quote(","));
				if (splice.length != 3)
					continue;
				
				// Load in MIDlet
				availableMIDlets.put(Integer.valueOf(i),
					new JavaMEMidlet(splice[0].trim(), splice[1].trim(),
						splice[2].trim()));
			}
		}
		catch (IOException __e)
		{
			throw new RuntimeException("Could not process Jar.", __e);
		}
		
		// If not wanting a main class, do not use it
		String targetMain = null;
		if (!runTask.mainClass)
			targetMain = availableMain;
		JavaMEMidlet targetMIDlet = availableMIDlets.get(
			Integer.valueOf(runTask.midlet));
		
		// Nothing?
		if (targetMain == null && targetMIDlet == null)
			throw new RuntimeException(String.format(
				"Jar does not have %s (main=%s, midlets=%s)",
				(runTask.mainClass ? "Main" :
					"MIDlet " + runTask.midlet),
				targetMain, availableMIDlets));
		
		// Load in full class path
		List<Path> classPath = new ArrayList<>();
		for (Path path : VMHelpers.runClassPath(runTask.getProject()
			.getRootProject().findProject(":modules:profile-meep"),
			runTask.classifier, true))
			classPath.add(path);
		classPath.add(Paths.get(jar));
		
		// Setup detached runner then execute it
		new VMRunTaskDetached(runTask.classifier,
			__task.getLogger(),
			classPath.toArray(new Path[classPath.size()]),
			targetMIDlet,
			VMHelpers.mainClass(targetMIDlet, targetMain),
			runTask.getProject().getBuildDir().toPath(),
			runTask.getProject(),
			runTask.debugServer).run();
	}
}
