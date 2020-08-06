// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.util.ArchiveTaskUtils;
import java.nio.file.Path;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Provider;
import org.gradle.jvm.tasks.Jar;

/**
 * Builds the SummerCoat ROM from the input JAR.
 *
 * @since 2020/08/06
 */
public class SummerCoatRomTask
	extends DefaultTask
{
	/** The JAR to be converted. */
	protected final Jar jar;
	
	/**
	 * Converts the given JAR to a SummerCoat ROM.
	 * 
	 * @param __jar The JAR to convert.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	@Inject
	public SummerCoatRomTask(Jar __jar)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		this.jar = __jar;
		
		// Set description
		this.setGroup("squirreljme");
		this.setDescription("Compiles a SummerCoat ROM.");
		
		// Depend on the JAR task
		this.dependsOn(__jar);
		
		// The JAR is just extension changed to the ROM
		Provider<Path> jarFile = ArchiveTaskUtils.getArchiveFile(__jar);
		this.getInputs().file(jarFile);
		this.getOutputs().file(ArchiveTaskUtils.changeExtension(
			jarFile, ArchiveTaskUtils.SUMMERCOAT_EXTENSION));
		
		// Does the actual conversion
		this.doLast(new Action<Task>(){
			@Override
			public void execute(Task __task)
			{
				System.err.printf("ROM Input: %s%n",
					__task.getInputs().getFiles().getFiles());
				System.err.printf("ROM Output: %s%n",
					__task.getOutputs().getFiles().getFiles());
				
				throw new Error("TODO");
			}
		});
	}
}
