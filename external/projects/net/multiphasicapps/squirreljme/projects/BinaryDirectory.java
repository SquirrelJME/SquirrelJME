// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This is the directory of binary projects which may be executed or natively
 * compiled.
 *
 * @since 2016/10/20
 */
public final class BinaryDirectory
	extends AbstractMap<ProjectName, BinaryProject>
{
	/**
	 * Initializes the binary directory.
	 *
	 * @param __d The owning project directory.
	 * @param __p The directory where binary projects exist and are placed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	BinaryDirectory(ProjectDirectory __d, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Go through all files in the directory
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
		{
			for (Path f : ds)
			{
				// Ignore directories
				if (Files.isDirectory(f))
					continue;
				
				// Try adding the project
				try
				{
					__addProject(f);
				}
				
				// Ignore
				catch (__CannotBeProjectException__ e)
				{
					// Only print the trace if it ends in JAR
					if (f.getFileName().toString().endsWith(".jar"))
						e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/20
	 */
	@Override
	public Set<Map.Entry<ProjectName, BinaryProject>> entrySet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Adds the given project at the given path to the binary directory.
	 *
	 * @param __p The path of the project to add.
	 * @retrun The added project.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	BinaryProject __addProject(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	} 
}

