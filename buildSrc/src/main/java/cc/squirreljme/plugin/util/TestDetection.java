// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryTree;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

/**
 * Utilities regarding test detection.
 *
 * @since 2020/08/28
 */
public final class TestDetection
{
	/**
	 * Not used.
	 * 
	 * @since 2020/08/28
	 */
	private TestDetection()
	{
	}
	
	/**
	 * Is this considered a test?
	 *
	 * @param __className The class name to check.
	 * @return If this is considered a test.
	 * @since 2020/08/28
	 */
	public static boolean isTest(String __className)
		throws NullPointerException
	{	
		if (__className == null)
			throw new NullPointerException("NARG");
		
		// Get base name of the class
		int ld = __className.lastIndexOf('.');
		String base = (ld < 0 ? __className : __className.substring(ld + 1));
		
		return (base.startsWith("Do") || base.startsWith("Test") ||
			base.endsWith("Test"));
	}
	
	/**
	 * Returns a collection of source set files.
	 * 
	 * @param __project The __project to scan.
	 * @param __sourceSet The source set to read files from.
	 * @return A collection of files within the source set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/29
	 */
	public static Collection<FileLocation> sourceSetFiles(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Get base source sets
		SourceSet sourceSet = __project.getConvention().getPlugin(
			JavaPluginConvention.class).getSourceSets().getByName(__sourceSet);
		
		// Add normal Java sources along with resources (for Jasmin)
		Deque<DirectoryTree> queue = new LinkedList<>();
		queue.addAll(sourceSet.getJava().getSrcDirTrees());
		queue.addAll(sourceSet.getResources().getSrcDirTrees());
		
		// Discover all the input files (in sources)
		Collection<FileLocation> result = new ArrayList<>();
		while (!queue.isEmpty())
		{
			DirectoryTree dir = queue.removeFirst();
			Path baseDir = dir.getDir().toPath();
			
			// Process all files in each directory
			for (File file : __project.files(dir))
			{
				Path path = file.toPath();
				
				result.add(new FileLocation(path, baseDir.relativize(path)));
			}
		}
		
		return result;
	}
}
