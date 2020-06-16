// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.tasks.test.EmulatedTestExecutionSpec;
import cc.squirreljme.plugin.tasks.test.EmulatedTestExecutor;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import org.gradle.api.internal.provider.AbstractMinimalProvider;
import org.gradle.api.tasks.testing.AbstractTestTask;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.tasks.Jar;

/**
 * Tests in virtual machine, this uses an abstract test tasks which implement
 * all of the needed classes for test.
 *
 * Required properties:
 *  - binaryResultsDirectory
 *  - reports.enabledReports.html.outputLocation
 *  - reports.enabledReports.junitXml.outputLocation
 *
 * Gradle Test task {@link Test}.
 *
 * @since 2020/03/06
 */
public class TestInVMTask
	extends AbstractTestTask
{
	/** The binary results directory. */
	private static final String _BINARY_RESULTS_DIRECTORY =
		"binaryResultsDirectory";
	
	/** The emulator to use. */
	protected final String emulator;
	
	/** The JAR task for execution. */
	protected final Jar jar;
	
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR sources.
	 * @param __vm The virtual machine to run.
	 * @since 2020/03/06
	 */
	@Inject
	public TestInVMTask(Jar __jar, String __vm)
	{
		// We need these for tasks and such
		this.emulator = __vm;
		this.jar = __jar;
		
		// Set description
		this.setGroup("squirreljme");
		this.setDescription("Tests inside of " + __vm + ".");
		
		// Depend on the JAR task
		this.dependsOn(__jar);
		
		// The input file for our tests is the JAR we want to look at!
		this.getInputs().file(__jar.getArchiveFile());
		
		// Where binary results are going to be stored, these both have to
		// be set otherwise the test build will fail
		this.setProperty(TestInVMTask._BINARY_RESULTS_DIRECTORY,
			this.getProject().getObjects().directoryProperty()
			.dir(new __BinaryResultsDirectoryProvider__()));
		this.setBinResultsDir(
			new File(new __BinaryResultsDirectoryProvider__().get()));
		
		// Generate HTML reports because they are useful
		this.getReports().getHtml().setDestination(
			this.__tempRoot().resolve("html-reports").toFile());
		
		// Generate JUnit XML, these will be uploaded to CICD
		this.getReports().getJunitXml().setDestination(
			this.__tempRoot().resolve("junit-reports").toFile());
		this.getReports().getJunitXml().setOutputPerTestCase(true);
		
		// Include every test that is of a specific pattern, matching
		// what SquirrelJME uses
		//this.setTestNameIncludePatterns(Arrays.asList("**/*Test", "**/Test*",
		//	"**/Do*", "*Test", "Test*", "Do*"));
		
		// This needs the JAR task and the emulation task
		this.dependsOn(__jar,
			(Callable<Jar>)this::__findEmulatorJarTask,
			(Callable<Jar>)this::__findEmulatorBaseJarTask);
		
		// Enable more logging as console output is rather important to
		// have when running the VMs especially when not fully stable
		this.testLogging(__settings ->
			{
				__settings.setShowCauses(true);
				__settings.setShowStandardStreams(true);
				__settings.setShowStackTraces(true);
			});
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected EmulatedTestExecutor createTestExecuter()
	{
		return new EmulatedTestExecutor(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected EmulatedTestExecutionSpec createTestExecutionSpec()
	{
		return new EmulatedTestExecutionSpec(this.emulator, this.jar);
	}
	
	/**
	 * Finds the emulator JAR task.
	 *
	 * @return The emulator JAR task.
	 * @since 2020/02/29
	 */
	Jar __findEmulatorBaseJarTask()
	{
		return (Jar)Objects.requireNonNull(this.getProject().getRootProject().
			findProject(":emulators:emulator-base"),
			"No emulator base?").getTasks().getByName("jar");
	}
	
	/**
	 * Locates the emulator package to base on.
	 *
	 * @return The emulator base.
	 * @since 2020/02/29
	 */
	Jar __findEmulatorJarTask()
	{
		return (Jar)Objects.requireNonNull(this.getProject().getRootProject().
			findProject(":emulators:" + this.emulator + "-vm"),
			"No emulator?").getTasks().getByName("jar");
	}
	
	/**
	 * Returns the temporary file root base.
	 *
	 * @return The temporary root.
	 * @since 2020/03/06
	 */
	public final Path __tempRoot()
	{
		return this.getProject().getBuildDir().toPath().resolve("vm-test").
			resolve(this.emulator);
	}
	
	/**
	 * Provider for directory.
	 *
	 * @since 2020/03/06
	 */
	class __BinaryResultsDirectoryProvider__
		extends AbstractMinimalProvider<String>
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/03/06
		 */
		@Override
		public String get()
			throws IllegalStateException
		{
			String rv = this.getOrNull();
			if (rv == null)
				throw new IllegalStateException("No directory!");
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/03/06
		 */
		@Override
		public String getOrNull()
		{
			return TestInVMTask.this.__tempRoot().resolve("bin-results")
				.toAbsolutePath().toString();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/03/06
		 */
		@Override
		public Class<String> getType()
		{
			return String.class;
		}
	}
}
