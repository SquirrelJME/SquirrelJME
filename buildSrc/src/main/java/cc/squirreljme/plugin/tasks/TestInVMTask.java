// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import java.io.File;
import java.nio.file.Path;
import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.api.internal.provider.AbstractMinimalProvider;
import org.gradle.api.internal.tasks.testing.TestExecuter;
import org.gradle.api.internal.tasks.testing.TestExecutionSpec;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
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
		
		// Set description
		this.setGroup("squirreljme");
		this.setDescription("Tests inside of " + __vm + ".");
		
		// Where binary results are going to be stored
		this.setProperty(TestInVMTask._BINARY_RESULTS_DIRECTORY,
			this.getProject().getObjects().directoryProperty()
			.dir(new __BinaryResultsDirectoryProvider__()));
		
		// This is deprecated but still has to be set in order for this to
		// work!
		this.setBinResultsDir(
			new File(new __BinaryResultsDirectoryProvider__().get()));
		
		// Do not generate XML
		this.getReports().getEnabledReports().get("html").setEnabled(false);
		
		// Do not generate JUnit XML (at least initially)
		this.getReports().getEnabledReports().get("junitXml")
			.setEnabled(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected TestExecuter<? extends TestExecutionSpec> createTestExecuter()
	{
		return new __Executer__();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	protected TestExecutionSpec createTestExecutionSpec()
	{
		return new __Spec__();
	}
	
	/**
	 * Returns the temporary file root base.
	 *
	 * @return The temporary root.
	 * @since 2020/03/06
	 */
	final Path __tempRoot()
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
		@SuppressWarnings("unchecked")
		public Class<String> getType()
		{
			return String.class;
		}
	}
	
	/**
	 * This is the executer for tests.
	 *
	 * @since 2020/03/06
	 */
	final class __Executer__
		implements TestExecuter<TestExecutionSpec>
	{
		@Override
		public void execute(TestExecutionSpec __spec,
			TestResultProcessor __results)
		{
			throw new Error("execute()");
		}
		
		@Override
		public void stopNow()
		{
			throw new Error("stopNow()");
		}
	}
	
	/**
	 * The specification for tests.
	 *
	 * @since 2020/03/06
	 */
	final class __Spec__
		implements TestExecutionSpec
	{
	}
}
