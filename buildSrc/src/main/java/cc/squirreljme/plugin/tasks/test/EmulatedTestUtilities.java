// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.tasks.testing.DefaultTestOutputEvent;
import org.gradle.api.internal.tasks.testing.TestCompleteEvent;
import org.gradle.api.internal.tasks.testing.TestDescriptorInternal;
import org.gradle.api.internal.tasks.testing.TestStartEvent;
import org.gradle.api.tasks.testing.TestOutputEvent;
import org.gradle.api.tasks.testing.TestResult;
import org.gradle.jvm.tasks.Jar;

/**
 * Utilities for tests.
 *
 * @since 2020/03/06
 */
public final class EmulatedTestUtilities
{
	/** Main configurations. */
	private static final String[] _MAIN_CONFIGS =
		new String[]{"api", "implementation"};
	
	/** Test configurations. */
	private static final String[] _TEST_CONFIGS =
		new String[]{"testApi", "testImplementation"};
	
	/** Normal JAR task. */
	private static final String _MAIN_JAR_TASK =
		"jar";
	
	/** Test JAR task. */
	private static final String _TEST_JAR_TASK =
		"testJar";
	
	/**
	 * Not used.
	 *
	 * @since 2020/03/06
	 */
	private EmulatedTestUtilities()
	{
	}
	
	/**
	 * Returns the test classpath of the given project.
	 *
	 * @param __project The project to get the classpath of.
	 * @param __test Get the test class path?
	 * @return The project classpath.
	 * @since 2020/03/07
	 */
	public static Iterable<Path> classPath(Project __project, boolean __test)
	{
		// Which configuration to extract?
		String[] configs = (__test ? EmulatedTestUtilities._TEST_CONFIGS :
			EmulatedTestUtilities._MAIN_CONFIGS);
		String inJar = (__test ? EmulatedTestUtilities._TEST_JAR_TASK :
			EmulatedTestUtilities._MAIN_JAR_TASK);
		
		// Projects to evaluate and the projects we looked at
		Deque<Project> eval = new LinkedList<>();
		Set<Project> did = new TreeSet<>();
		
		// Start at our current project
		eval.add(__project);
		
		// Process all projects for output
		Collection<Path> rv = new LinkedList<>();
		while (!eval.isEmpty())
		{
			// Only evaluate project once
			Project at = eval.removeFirst();
			if (did.contains(at))
				continue;
			did.add(at);
			
			// Only consider proper SquirrelJME projects
			SquirrelJMEPluginConfiguration conf =
				SquirrelJMEPluginConfiguration.configurationOrNull(at);
			if (conf == null)
				continue;
			
			// Add the JARs of execution, but for this project use the
			// main/test JAR accordingly to what we want but for dependencies
			// always use the main JAR becuase we do not want tests at all
			Jar jarTask = (Jar)at.getTasks().findByName((at == __project ?
				inJar : EmulatedTestUtilities._MAIN_JAR_TASK));
			if (jarTask != null)
				for (File file : jarTask.getOutputs().getFiles())
					rv.add(file.toPath().toAbsolutePath());
			
			// Process each configurations dependencies
			for (String config : configs)
			{
				// If no configuration is set
				Configuration buildConfig = at.getConfigurations()
					.findByName(config);
				if (buildConfig == null)
					continue;
					
				// Process dependencies for project
				for (Dependency dependency : buildConfig.getDependencies())
				{
					// Only consider projects
					if (!(dependency instanceof ProjectDependency))
						continue;
					
					// Put in the project to evaluate
					eval.addLast(((ProjectDependency)dependency)
						.getDependencyProject());
				}
			}
		}
		
		return rv;
	}
	
	/**
	 * Returns the test classpath of the given project.
	 *
	 * @param __project The project to get the classpath of.
	 * @param __test Get the test class path?
	 * @return The project classpath.
	 * @since 2020/03/07
	 */
	public static String classpathString(Project __project, boolean __test)
	{
		return EmulatedTestUtilities.classpathAsString(
			EmulatedTestUtilities.classPath(__project, __test));
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @since 2020/02/29
	 */
	public static String classpathAsString(Iterable<Path> __paths)
	{
		StringBuilder sb = new StringBuilder();
		
		for (Path path : __paths)
		{
			if (sb.length() > 0)
				sb.append(File.pathSeparatorChar);
			sb.append(path);
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns the emulator class path.
	 *
	 * @param __p The project for scanning.
	 * @param __emu The emulator to base off.
	 * @return The emulator class path.
	 * @since 2020/03/07
	 */
	public static Iterable<Object> emulatorClassPath(Project __p, String __emu)
	{
		// Find emulator projects
		Project rootProject = __p.getRootProject();
		Project emuProject = rootProject.findProject(
			":emulators:" + __emu + "-vm");
		
		Collection<Object> rv = new LinkedList<>();
		
		// Add the emulator run-time classpath along with the JAR it uses
		// for execution
		rv.add(emuProject.getConfigurations().getByName("runtimeClasspath")
			.getFiles());
		rv.add(((Jar)emuProject.getTasks().getByName("jar")).getOutputs()
			.getFiles());
		
		return rv;
	}
	
	/**
	 * Returns a failing completion.
	 *
	 * @return An event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent failNow()
	{
		return new TestCompleteEvent(System.currentTimeMillis(),
			TestResult.ResultType.FAILURE);
	}
	
	/**
	 * Outputs a message.
	 *
	 * @param __dest The destination.
	 * @param __msg The message.
	 * @return The message event.
	 * @since 2020/03/06
	 */
	public static TestOutputEvent output(TestOutputEvent.Destination __dest,
		String __msg)
	{
		return new DefaultTestOutputEvent(__dest, __msg);
	}
	
	/**
	 * Outputs a message.
	 *
	 * @param __msg The message.
	 * @return The message event.
	 * @since 2020/03/06
	 */
	public static TestOutputEvent output(String __msg)
	{
		return EmulatedTestUtilities.output(
			TestOutputEvent.Destination.StdOut, __msg);
	}
	
	/**
	 * Outputs a message.
	 *
	 * @param __msg The message.
	 * @return The message event.
	 * @since 2020/03/06
	 */
	public static TestOutputEvent outputErr(String __msg)
	{
		return EmulatedTestUtilities.output(
			TestOutputEvent.Destination.StdErr, __msg);
	}
	
	/**
	 * Returns a passing completion.
	 *
	 * @return An event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent passNow()
	{
		return new TestCompleteEvent(System.currentTimeMillis(),
			TestResult.ResultType.SUCCESS);
	}
	
	/**
	 * Either passes or fails.
	 *
	 * @param __pass Does the test pass?
	 * @return The event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent passOrFailNow(boolean __pass)
	{
		return (__pass ? EmulatedTestUtilities.passNow() :
			EmulatedTestUtilities.failNow());
	}
	
	/**
	 * Either passes or skips.
	 *
	 * @param __pass Does the test pass?
	 * @return The event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent passOrSkipNow(boolean __pass)
	{
		return (__pass ? EmulatedTestUtilities.passNow() :
			EmulatedTestUtilities.skipNow());
	}
	
	/**
	 * Depending on the exit code, this will pass, fail, or skip a test.
	 *
	 * @param __exitCode The exit code.
	 * @return The completion event.
	 * @since 2020/03/07
	 */
	public static TestCompleteEvent passSkipOrFailNow(int __exitCode)
	{
		if (__exitCode == ExitValueConstants.SUCCESS)
			return EmulatedTestUtilities.passNow();
		else if (__exitCode == ExitValueConstants.SKIPPED)
			return EmulatedTestUtilities.skipNow();
		return EmulatedTestUtilities.failNow();
	}
	
	/**
	 * Returns a skipping completion.
	 *
	 * @return An event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent skipNow()
	{
		return new TestCompleteEvent(System.currentTimeMillis(),
			TestResult.ResultType.SKIPPED);
	}
	
	/**
	 * Returns a test starting now.
	 *
	 * @return An event
	 * @since 2020/03/06
	 */
	public static TestStartEvent startNow()
	{
		return EmulatedTestUtilities.startNow(null);
	}
	
	/**
	 * Returns a test starting now.
	 *
	 * @param __test The test information.
	 * @return An event
	 * @since 2020/03/06
	 */
	public static TestStartEvent startNow(TestDescriptorInternal __test)
	{
		return new TestStartEvent(System.currentTimeMillis(),
			(__test == null ? null : __test.getId()));
	}
}
