// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;
import net.multiphasicapps.squirreljme.projects.ProjectGroup;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;
import net.multiphasicapps.squirreljme.projects.ProjectList;
import net.multiphasicapps.squirreljme.projects.ProjectType;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * Main entry class for the singular package export system.
 *
 * @since 2016/09/29
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/29
	 */
	public static void main(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Queue all strings
		Deque<String> args = new ArrayDeque<>();
		for (String s : __args)
			args.offerLast(s);
		
		// Include optional projects? Virtualize it?
		boolean optionals = false,
			virtualize = false;
		
		// Output file
		Path outfile = null;
		
		// Handle all arguments
		while (!args.isEmpty())
		{
			String s = args.peekFirst();
			
			// If not a switch then remove it
			if (!s.startsWith("-"))
				break;
			
			// Otherwise remove it
			args.removeFirst();
			
			// Handle
			switch (s)
			{
					// Include optional dependencies
				case "-a":
					optionals = true;
					break;
					
					// The output file
				case "-o":
					outfile = Paths.get(args.removeFirst());
					break;
					
					// Include a virtual environment
				case "-v":
					virtualize = true;
					break;
				
					// {@squirreljme.error DV01 Unknown command line
					// argument. (The switch)}
				default:
					throw new IllegalArgumentException(String.format("DV01 %s",
						s));
			}
		}
		
		// {@squirreljme.error DV02 No global project list has been
		// initialized, this project may only be launched from the build
		// system.}
		ProjectList pl = ProjectList.getGlobalProjectList();
		if (pl == null)
			throw new IllegalStateException("DV02");
			
		// {@squirreljme.error DV03 No projects were specified on the command
		// line. Usage: [-a] [-o file] [-v] (projects).
		// -a: Include optional dependencies.}
		if (args.isEmpty())
			throw new IllegalArgumentException("DV03");
		
		// Build and obtain binary projects to be included in the build
		Set<ProjectInfo> projects = new SortedTreeSet<>();
		ProjectInfo mainproj = __loadProjects(pl, projects, args, optionals);
		
		// Use default output file?
		if (outfile == null)
			outfile = Paths.get("x-squirreljme-" + mainproj.name() + ".jar");
		
		// Merge them together into one
		__merge(mainproj, projects, outfile, virtualize);
	}
	
	/**
	 * Loads all project binaries.
	 *
	 * @param __pl The project list to source projects from.
	 * @param __into The destination set.
	 * @param __from The source project name queue.
	 * @param __opt Include optional dependencies?
	 * @return The main project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	private static ProjectInfo __loadProjects(ProjectList __pl,
		Set<ProjectInfo> __into, Deque<String> __from, boolean __opt)
		throws NullPointerException
	{
		// Check
		if (__into == null || __from == null)
			throw new NullPointerException("NARG");	
		
		// The main project (the first one used)
		ProjectInfo rv = null;
		
		// Read all arguments
		while (!__from.isEmpty())
		{
			// Get group for this project
			String rpn = __from.removeFirst();
			ProjectGroup pg = __pl.get(rpn);
			
			// {@squirreljme.error DV04 No project with the specified name
			// exists. (The project name)}
			if (pg == null)
				throw new IllegalArgumentException(String.format("DV04 %d",
					rpn));
			
			// Compile it along with optional dependencies
			ProjectInfo bin;
			try
			{
				bin = pg.compileSource(null, __opt);
			}
			
			// {@squirreljme.error DV05 Read/write error reading the project
			// information.}
			catch (IOException e)
			{
				throw new RuntimeException("DV05", e);
			}
			
			// Set main project if it has not been set, since there will need
			// to be a known Main-Class or main MIDlet.
			if (rv == null)
				rv = bin;
			
			// Add dependencies
			__into.add(bin);
			__pl.recursiveDependencies(__into, ProjectType.BINARY, pg.name(),
				__opt);
		}
		
		// Return the main project
		return rv;
	}
	
	/**
	 * Merges all of the input binaries 
	 *
	 * @param __main The main class to get the manifest from.
	 * @param __p The projects to merge together.
	 * @param __of The output file.
	 * @param __virt If {@code true} then the project is virtualized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	private static void __merge(ProjectInfo __main,
		Collection<ProjectInfo> __p, Path __of, boolean __virt)
		throws NullPointerException
	{
		// Check
		if (__main == null || __p == null || __of == null)
			throw new NullPointerException("NARG");
		
		// Perform the merge
		Path tempjar = null;
		try
		{
			// Create file
			tempjar = Files.createTempFile("squirreljme-merge", ".jar");
			
			// Write the output ZIP
			try (ZipStreamWriter zsw = new ZipStreamWriter(Channels.
				newOutputStream(FileChannel.open(tempjar,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING))))
			{
				if (true)
					throw new Error("TODO");
			}
			
			// Ok, move the result
			Files.move(tempjar, __of);
		}
		
		// {@squirreljme.error DV06 Failed to read/write for project merge.}
		catch (IOException e)
		{
			throw new RuntimeException("DV06", e);
		}
		
		// Delete temporaries
		finally
		{
			// Delete it
			try
			{
				Files.delete(tempjar);
			}
			
			// Ignore
			catch (IOException e)
			{
			}
		}
	}
}

