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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import java.util.Iterator;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.java.manifest.MutableJavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.
	MutableJavaManifestAttributes;
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
	 * Sets up the manifest used for output.
	 *
	 * @param __main The main project.
	 * @param __mjm The output manifest.
	 * @param __virt Is this virtualized?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	private static void __manifest(ProjectInfo __main,
		MutableJavaManifest __mjm, boolean __virt)
		throws NullPointerException
	{
		// Check
		if (__main == null || __mjm == null)
			throw new NullPointerException("NARG");
		
		// Remove some keys in the main attribute set
		MutableJavaManifestAttributes attr = __mjm.getMainAttributes();
		Iterator<JavaManifestKey> it = attr.keySet().iterator();
		while (it.hasNext())
		{
			String s = it.next().toString();
			switch (s)
			{
					// Remove
				case "class-path":
					it.remove();
					break;
					
					// Potentially keep
				default:
					if (s.startsWith("midlet-dependency-") ||
						s.startsWith("liblet-dependency-"))
						it.remove();
					break;
			}
		}
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
				// Output a manifest
				MutableJavaManifest mjm = new MutableJavaManifest(
					__main.manifest());
				__manifest(__main, mjm, __virt);
				try (OutputStream os = zsw.nextEntry("META-INF/MANIFEST.MF",
					ZipCompressionType.DEFAULT_COMPRESSION))
				{
					mjm.write(os);
				}
				
				// Go through all projects and their files
				byte[] buf = new byte[512];
				for (ProjectInfo pi : __p)
				{
					// Ignore the CLDC libraries
					String pn = pi.name().toString();
					if (!__virt && (pn.equals("cldc-compact") ||
						pn.equals("cldc-full")))
						continue;
					
					// Copy contents
					for (String c : pi.contents())
					{
						// Ignore manifests
						if (c.equals("META-INF/MANIFEST.MF"))
							continue;
						
						// Copy it
						try (OutputStream os = zsw.nextEntry(c,
							ZipCompressionType.DEFAULT_COMPRESSION);
							InputStream is = pi.open(c))
						{
							for (;;)
							{
								// Read
								int rc = is.read(buf);
								
								// EOF?
								if (rc < 0)
									break;
								
								// Write
								os.write(buf, 0, rc);
							}
						}
					}
				}
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

