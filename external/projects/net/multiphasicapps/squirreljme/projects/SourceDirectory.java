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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains the directory of source projects which may be compiled into
 * binary projects.
 *
 * The source directory is namespaced which permits multiple namespaces to
 * exist and for the categorization of source projects.
 *
 * @since 2016/10/20
 */
public final class SourceDirectory
	extends AbstractMap<ProjectName, SourceProject>
{
	/** The source project directory. */
	protected final Map<ProjectName, SourceProject> projects =
		new SortedTreeMap<>();
	
	/** The owning directory. */
	protected final ProjectDirectory directory;
	
	/** Read only set. */
	private final __ReadOnlySet__<SourceProject> _readonly =
		new __ReadOnlySet__<>(this.projects);
	
	/**
	 * Initializes the source directory.
	 *
	 * @param __d The owning project directory.
	 * @param __s The directory contianing namespaces which contain project
	 * sources.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	SourceDirectory(ProjectDirectory __d, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.directory = __d;
		
		// Look for resources
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
		{
			for (Path dp : ds)
			{
				// Must be a directory
				if (!Files.isDirectory(dp))
					continue;
			
				// There must be a namespace manifest
				Path nsmanp = dp.resolve("NAMESPACE.MF");
				if (!Files.exists(nsmanp))
					continue;
				
				// The namespace is based on the directory name
				String nsn = dp.getFileName().toString();
				
				// Go through all projects in the namespace and add them
				try (DirectoryStream<Path> ns = Files.newDirectoryStream(dp))
				{
					for (Path np : ns)
					{
						// Must be a directory
						if (!Files.isDirectory(np))
							continue;
						
						// Try adding a project
						try
						{
							__addSource(nsn, np);
						}
				
						// Ignore, but print the warning
						catch (NotAProjectException e)
						{
							e.printStackTrace();
						}
					}
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
		Map<ProjectName, SourceProject> projects = this.projects;
		synchronized (projects)
		{
			return projects.containsKey(__p);
		}
	}
	
	/**
	 * Returns the master project directory.
	 *
	 * @return The master project directory.
	 * @since 2016/10/25
	 */
	public ProjectDirectory directory()
	{
		return this.directory;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/20
	 */
	@Override
	public Set<Map.Entry<ProjectName, SourceProject>> entrySet()
	{
		return this._readonly;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/20
	 */
	@Override
	public SourceProject get(Object __p)
	{
		// Will never be a valid project
		if (!(__p instanceof ProjectName))
			return null;
		
		// Lock
		Map<ProjectName, SourceProject> projects = this.projects;
		synchronized (projects)
		{
			return projects.get(__p);
		}
	}
	
	/**
	 * Adds a source project to the source project list.
	 *
	 * @param __ns The namespace the project is in.
	 * @param __p The base directory of the project.
	 * @return The source project.
	 * @throws InvalidProjectException If the project is not valid.
	 * @throws IOException On read errors.
	 * @throws NotAProjectException If the directory does not refer to a
	 * project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	SourceProject __addSource(String __ns, Path __p)
		throws InvalidProjectException, IOException, NotAProjectException,
			NullPointerException
	{
		// Check
		if (__ns == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Parse the file as a manifest
		Path manp = __p.resolve("META-INF").resolve("MANIFEST.MF");
		try (InputStream is = Channels.newInputStream(FileChannel.open(
			manp, StandardOpenOption.READ)))
		{
			// Create project
			SourceProject rv;
			try
			{
				rv = new SourceProject(this, new SourceProjectManifest(is),
					__p);
			}
			
			// {@squirreljme.error CI08 The source project at the specified
			// path is not valid. (The source directory)}
			catch (InvalidProjectException e)
			{
				// Ignore these instances
				if (e instanceof NotAProjectException)
					throw e;
				
				// Fail
				throw new InvalidProjectException(String.format("CI08 %s",
					__p), e);
			}
			
			// Add to the map
			Map<ProjectName, SourceProject> projects = this.projects;
			synchronized (projects)
			{
				SourceProject old = projects.put(rv.projectName(), rv);
				
				// Potential old project cleanup?
				if (old != null)
					throw new Error("TODO");
			}
			
			// Return it
			return rv;
		}
		
		// {@squirreljme.error CI0a Cannot be a project because there is no
		// manifest located at {@code META-INF/MANIFEST.MF}. (The project
		// path)}
		catch (NoSuchFileException e)
		{
			throw new NotAProjectException(String.format("CI0a %s", __p), e);
		}
	}
}

