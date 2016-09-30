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
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.javac.base.Compiler;
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
	/**
	 * This is a fallback compiler which may be specified when it is not known.
	 * This sets an explicit compiler to use.
	 */
	static volatile Compiler _SPECIFIED_FALLBACK_COMPILER;
	
	/** The mapping of projects. */
	protected final Map<ProjectName, ProjectGroup> projects;
	
	/** The output binary directory. */
	protected final Path binarydir;
	
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
		
		// Set
		this.binarydir = (__j != null ? __j :
			Paths.get(System.getProperty("user.dir", ".")));
		
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
	 * Returns the path where binary files are to be placed.
	 *
	 * @return The binary project path.
	 * @since 2016/09/18
	 */
	public final Path binaryPath()
	{
		return this.binarydir;
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
	public Collection<ProjectInfo> recursiveDependencies(ProjectType __t,
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
	public Collection<ProjectInfo> recursiveDependencies(
		Collection<ProjectInfo> __rv, ProjectType __t, ProjectName __n,
		boolean __opt)
		throws MissingDependencyException, NullPointerException
	{
		// Check
		if (__rv == null || __t == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Package name queue
		Deque<ProjectName> q = new ArrayDeque<>();
		Set<ProjectName> did = new HashSet<>();
		q.push(__n);
		
		// Go through the queue
		ProjectName lastproc = null;
		while (!q.isEmpty())
		{
			// Locate project detail
			ProjectName name = q.pop();
			ProjectGroup grp = get(name);
			
			// Did this already? Ignore
			if (did.contains(name))
				continue;
			did.add(name);
			
			// {@squirreljme.error CI01 A project eventually depends on another
			// project which does not exist. (The project being looked up;
			// The missing dependency; The last processed dependency)}
			if (grp == null)
				throw new MissingDependencyException(String.format(
					"CI01 %s %s %s", __n, name, lastproc));
			
			// If our own project lacks a dependency of this type then use
			// any
			ProjectInfo info = grp.ofType(__t);
			if (info == null && name.equals(__n))
				info = grp.any();
			
			// {@squirreljme.error CI07 A dependency for a project exists
			// however it is not available for the given project type. (The
			// project being looked up; The unavailable dependency; The type
			// of project requested)}
			if (info == null)
				throw new MissingDependencyException(String.format(
					"CI07 %s %s %s", __n, name, __t));
			
			// Add information
			__rv.add(info);
			
			// Go through required depends and add them
			for (ProjectName rn : info.dependencies())
				q.push(rn);
			
			// Go through optional ones if requested, these are not
			// required to exist under a given type
			if (__opt)
				for (ProjectName on : info.dependencies(true))
				{
					// Group must exist
					ProjectGroup og = get(on);
					if (og == null)
						continue;
					
					// And info must exist of the given type
					ProjectInfo oi = og.ofType(__t);
					if (oi == null)
						continue;
					
					// Add name for later processing
					q.push(on);
				}
			
			// Set last processed
			lastproc = name;
		}
		
		// Return the target set
		return __rv;
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
	
	/**
	 * Sets the fallback compiler to use if no default could be used.
	 *
	 * @param __cc The compiler to use as a fallback.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	public static void setFallbackCompiler(Compiler __cc)
		throws NullPointerException
	{
		// Check
		if (__cc == null)
			throw new NullPointerException("NARG");
		
		// Set
		ProjectList._SPECIFIED_FALLBACK_COMPILER = __cc;
	}
}

