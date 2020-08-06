// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import cc.squirreljme.plugin.tasks.SummerCoatRomTask;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.gradle.api.Task;
import org.gradle.api.file.RegularFile;
import org.gradle.api.internal.provider.DefaultProvider;
import org.gradle.api.provider.Provider;
import org.gradle.jvm.tasks.Jar;

/**
 * Utilities for archives.
 *
 * @since 2020/08/06
 */
public final class ArchiveTaskUtils
{
	/** Extension used for SummerCoat ROMs. */
	public static final String SUMMERCOAT_EXTENSION = 
		"sqc";
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/06
	 */
	private ArchiveTaskUtils()
	{
	}
	
	/**
	 * Changes the file extension.
	 * 
	 * @param __input The input file.
	 * @param __newExt The new extension.
	 * @return The replaced file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	public static Provider<Path> changeExtension(
		Provider<Path> __input, String __newExt)
		throws NullPointerException
	{
		if (__input == null || __newExt == null)
			throw new NullPointerException("NARG");
		
		return new DefaultProvider<Path>(() ->
			{
				String file = __input.get().toString();
				
				// If it has no extension, add one
				int ld = file.lastIndexOf('.');
				if (ld < 0)
					return Paths.get(file + __newExt);
				
				// Otherwise replace it
				return Paths.get(file.substring(0, ld + 1) + __newExt);
			});
	}
	
	/**
	 * Returns the archive file for the given task.
	 * 
	 * @param __task The task to get from.
	 * @return The file for the task.
	 * @throws IllegalArgumentException If the task is not supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	public static Provider<Path> getArchiveFile(Task __task)
		throws IllegalArgumentException, NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		// These types are supported for files
		if (__task instanceof Jar)
			return __task.getProject().provider(() ->
				((Jar)__task).getArchiveFile().get().getAsFile().toPath());
		else if (__task instanceof SummerCoatRomTask)
			return __task.getProject().provider(() ->
				__task.getOutputs().getFiles().getSingleFile().toPath());
		
		throw new IllegalArgumentException(
			"Cannot get an archive from this task type: " + __task.getClass());
	}
}
