// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.gradle.api.Action;
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework;
import org.gradle.api.invocation.Gradle;
import org.gradle.process.internal.worker.WorkerProcessBuilder;

/**
 * Action to create test workers.
 *
 * @since 2022/09/11
 */
public class VMTestFrameworkWorkerConfigurationAction
	implements Action<WorkerProcessBuilder>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void execute(WorkerProcessBuilder __builder)
	{
		// Use the class paths for ourselves and the system class path which
		// should be Gradle itself... this should make it so that the worker
		// process is able to actually load our test framework classes
		Set<URL> classPathUrls = new LinkedHashSet<>();
		for (ClassLoader classLoader : new ClassLoader[]{
				this.getClass().getClassLoader(),
				Gradle.class.getClassLoader(),
				JUnitPlatformTestFramework.class.getClassLoader(),
				ClassLoader.getSystemClassLoader()})
			if (classLoader instanceof URLClassLoader)
				classPathUrls.addAll(Arrays.asList(
					((URLClassLoader)classLoader).getURLs()));
		
		// Now we need to load up all of these URLs into the class path if
		// we can so the classes are actually known to the worker
		Set<File> classPath = new LinkedHashSet<>();
		for (URL url : classPathUrls)
			if ("file".equals(url.getProtocol()))
				classPath.add(Paths.get(url.getPath()).toFile());
				
		// Add our Java given class path as well, just in case
		for (String splice : System.getProperty("java.class.path")
			.split(Pattern.quote(File.pathSeparator)))
			classPath.add(Paths.get(splice).toFile());
				
		// Use these both
		__builder.setImplementationClasspath(
			new ArrayList<>(classPathUrls));
		__builder.applicationClasspath(classPath);
		
		System.err.printf(">> WORKER: %s %s%n", classPath, classPathUrls);
	}
}
