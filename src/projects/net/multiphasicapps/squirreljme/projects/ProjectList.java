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
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This contains a mapping of every project which is available to SquirrelJME.
 *
 * @since 2016/06/15
 */
public class ProjectList
	extends AbstractMap<ProjectName, ProjectGroup>
{
	/** The mapping of projects. */
	protected final Map<ProjectName, ProjectGroup> projects;
	
	/**
	 * This initializes the project list.
	 *
	 * @param __j The directory containing pre-built JAR files, if {@code null}
	 * then binary projects are not available.
	 * @param __s The directories containing source projects, if {@code null}
	 * then source projects are not available.
	 * @throws IOException If there is an error reading the project list.
	 * @throws NullPointerException If both arguments are null
	 * @since 2016/06/15
	 */
	public ProjectList(Path __j, Path... __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__j == null && __s == null)
			throw new NullPointerException("NARG");
		
		// The target map
		Map<ProjectName, ProjectGroup> target = new SortedTreeMap<>();
		
		// Go through binary JAR files
		if (__j != null)
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(__j))
			{
				// Go through all files
				for (Path p : ds)
				{
					// Ignore directories
					if (Files.isDirectory(p))
						continue;
				
					// Open file
					try (FileChannel fc = FileChannel.open(p,
						StandardOpenOption.READ))
					{
						// Open as ZIP
						ZipFile zip = ZipFile.open(fc);
					
						// Load project information
						ProjectInfo pi = new ProjectInfo(this, p, zip, true);
					
						// Add to mapping
						ProjectName name = pi.name();
						ProjectGroup pg = target.get(name);
						if (pg == null)
							target.put(name,
								(pg = new ProjectGroup(this, name)));
						
						// Associate binary
						pg.__setBinary(pi);
					}
				
					// Not a valid ZIP or project, ignore
					catch (IOException|InvalidProjectException e)
					{
						continue;
					}
				}
			}
		
		// Handle source projects
		for (Path sp : __s)
		{
			// Go through
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(sp))
			{
				for (Path p : ds)
				{
					// Try opening the manifest
					Path manpath = p.resolve("META-INF").
						resolve("MANIFEST.MF");
					try (FileChannel fc = FileChannel.open(manpath,
						StandardOpenOption.READ))
					{
						// Load manifest
						JavaManifest man = new JavaManifest(
							Channels.newInputStream(fc));
				
						// Load project
						ProjectInfo pi = new ProjectInfo(this, p, false, man,
							false);
				
						// Add to mapping
						ProjectName name = pi.name();
						ProjectGroup pg = target.get(name);
						if (pg == null)
							target.put(name,
								(pg = new ProjectGroup(this, name)));
				
						// Associate source
						pg.__setSource(pi);
					}
			
					// Not a valid manifest or it is missing
					catch (IOException|InvalidProjectException e)
					{
						continue;
					}
				}
			}
		}
		
		// Lock
		this.projects = UnmodifiableMap.<ProjectName, ProjectGroup>of(target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/19
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		if (__o instanceof String)
			return this.projects.containsKey(new ProjectName((String)__o));
		return this.projects.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/15
	 */
	@Override
	public Set<Map.Entry<ProjectName, ProjectGroup>> entrySet()
	{
		return this.projects.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/19
	 */
	@Override
	public ProjectGroup get(Object __o)
	{
		if (__o instanceof String)
			return this.projects.get(new ProjectName((String)__o));
		return this.projects.get(__o);
	}
	
	/**
	 * Recursively obtains all of the dependencies of the given project name
	 * and any of their dependencies.
	 *
	 * @param __t The type of dependencies to obtain.
	 * @param __n The name of the dependency to get for.
	 * @param __opt If {@code true} then any optional packages are also
	 * included.
	 * @return A set containing the dependencies of the given project.
	 * @throws MissingDependencyException If a depdendency is missing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public Set<ProjectInfo> recursiveDependencies(ProjectType __t,
		ProjectName __n, boolean __opt)
		throws MissingDependencyException, NullPointerException
	{
		return recursiveDependencies(new SortedTreeSet<ProjectInfo>(), __t,
			__n, __opt);
	}
	
	/**
	 * Recursively obtains all of the dependencies of the given project name
	 * and any of their dependencies.
	 *
	 * @param __rv The target set to be given dependencies.
	 * @param __t The type of dependencies to obtain.
	 * @param __n The name of the dependency to get for.
	 * @param __opt If {@code true} then any optional packages are also
	 * included.
	 * @return The target set.
	 * @throws MissingDependencyException If a depdendency is missing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public Set<ProjectInfo> recursiveDependencies(Set<ProjectInfo> __rv,
		ProjectType __t, ProjectName __n, boolean __opt)
		throws MissingDependencyException, NullPointerException
	{
		// Check
		if (__rv == null || __t == null || __n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/19
	 */
	@Override
	public int size()
	{
		return this.projects.size();
	}
}

