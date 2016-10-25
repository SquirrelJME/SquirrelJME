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

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the directory of binary projects which may be executed or natively
 * compiled.
 *
 * @since 2016/10/20
 */
public final class BinaryDirectory
	extends AbstractMap<ProjectName, BinaryProject>
{
	/** Binary projects that are available. */
	protected final Map<ProjectName, BinaryProject> projects =
		new SortedTreeMap<>();
	
	/** Read only set. */
	private final __ReadOnlySet__<BinaryProject> _readonly =
		new __ReadOnlySet__<>(this.projects);
	
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
				
				// Must end in JAR
				String fn = f.getFileName().toString();
				if (!fn.endsWith(".jar") && !fn.endsWith(".JAR"))
					continue;
				
				// Add the project
				try
				{
					__addProject(f);
				}
				
				// Ignore, but print the warning
				catch (NotAProjectException e)
				{
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
	public boolean containsKey(Object __p)
	{
		// Will never be a valid project
		if (!(__p instanceof ProjectName))
			return false;
		
		// Lock
		Map<ProjectName, BinaryProject> projects = this.projects;
		synchronized (projects)
		{
			return projects.containsKey(__p);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/20
	 */
	@Override
	public Set<Map.Entry<ProjectName, BinaryProject>> entrySet()
	{
		return this._readonly;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/20
	 */
	@Override
	public BinaryProject get(Object __p)
	{
		// Will never be a valid project
		if (!(__p instanceof ProjectName))
			return null;
		
		// Lock
		Map<ProjectName, BinaryProject> projects = this.projects;
		synchronized (projects)
		{
			return projects.get(__p);
		}
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
		
		// Open as a ZIP to read the manifest
		try (FileChannel fc = FileChannel.open(__p, StandardOpenOption.READ);
			ZipFile zf = ZipFile.open(fc))
		{
			// {@squirreljme.error CI01 The specified binary project cannot be
			// a project becauase it has no manifest. (The pathname)}
			ZipEntry ze = zf.get("META-INF/MANIFEST.MF");
			if (ze == null)
				throw new NotAProjectException(String.format("CI01 %s", __p));
			
			// Read in the manifest data
			BinaryProject rv;
			try (InputStream is = ze.open())
			{
				// And create the project
				rv = new BinaryProject(this, new BinaryProjectManifest(is),
					__p);
			}
			
			// {@squirreljme.error CI09 The binary project at the specified
			// path is not valid. (The binary project)}
			catch (InvalidProjectException e)
			{
				// Ignore these instances
				if (e instanceof NotAProjectException)
					throw e;
				
				// Fail
				throw new InvalidProjectException(String.format("CI09 %s",
					__p), e);
			}
			
			// Place into the project map
			Map<ProjectName, BinaryProject> projects = this.projects;
			synchronized (projects)
			{
				BinaryProject old = projects.put(rv.projectName(), rv);
				
				// Perform API cleanup for old project
				if (old != null)
					throw new Error("TODO");
			}
			
			// Return it
			return rv;
		}
	} 
}

