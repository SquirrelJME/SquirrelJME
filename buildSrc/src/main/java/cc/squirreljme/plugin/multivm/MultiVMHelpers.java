// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

/**
 * Helpers for the multi-VM handlers.
 *
 * @since 2020/08/07
 */
public final class MultiVMHelpers
{
	/* Copy buffer size. */
	public static final int COPY_BUFFER =
		4096;
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private MultiVMHelpers()
	{
	}
	
	/**
	 * Returns a collection of the tests that are available.
	 * 
	 * @param __project The project to check.
	 * @param __sourceSet The source set for the project.
	 * @return The list of available tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Collection<String> availableTests(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the cache directory of the project.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __vmType The virtual machine being used.
	 * @param __sourceSet The source set for the library, as there might be
	 * duplicates between them potentially.
	 * @return The path provider to the project cache directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Provider<Path> cacheDir(Project __project,
		VirtualMachineSpecifier __vmType, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> __project.getBuildDir().toPath()
			.resolve("squirreljme").resolve("vm-" + __sourceSet + "-" +
				__vmType.vmName(VMNameFormat.LOWERCASE)));
	}
	
	/**
	 * Copies from the input into the output.
	 * 
	 * @param __in The input.
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static void copy(InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		byte[] buf = new byte[MultiVMHelpers.COPY_BUFFER];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				return;
			
			__out.write(buf, 0, rc);
		}
	}
	
	/**
	 * Returns the task that creates the JAR.
	 * 
	 * @param __project The project to get from.
	 * @param __sourceSet The source set.
	 * @return The jar task used, or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Jar jarTask(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		switch (__sourceSet)
		{
			case SourceSet.MAIN_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("jar");
			
			case SourceSet.TEST_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("testJar");
			
			default:
				throw new IllegalStateException("Unknown sourceSet: " +
					__sourceSet);
		}
	}
}
