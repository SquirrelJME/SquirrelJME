// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This acts as the common base for binary and source projects.
 *
 * @since 2016/12/17
 */
public abstract class ProjectBase
{
	/** The earliest date. */
	private static final FileTime _EARLIEST_DATE =
		FileTime.fromMillis(Long.MIN_VALUE);
	
	/** The owning project. */
	protected final Project project;
	
	/** The path to the source or binary. */
	protected final Path path;
	
	/** The current project time. */
	private volatile long _time =
		Long.MIN_VALUE;
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the file.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	ProjectBase(Project __p, Path __fp)
		throws NullPointerException
	{
		// Check
		if (__p == null || __fp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.project = __p;
		this.path = __fp;
	}
	
	/**
	 * Returns the dependencies that this project relies on for running and
	 * compilation.
	 *
	 * @param __out The output set where dependencies are placed.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/18
	 */
	public abstract void dependencies(Set<Project> __out)
		throws InvalidProjectException, NullPointerException;
	
	/**
	 * Obtains the binary projects which this binary project depends on and
	 * places them into the specified set.
	 *
	 * @param __out The output set where dependencies are placed.
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/18
	 */
	public final void binaryDependencies(Set<ProjectBinary> __out,
		boolean __rec)
		throws InvalidProjectException, NullPointerException
	{
		__internalBinaryDependencies(null, __out, __rec);
	}
		
	/**
	 * Obtains the binary projects which this binary project depends on.
	 *
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @return The binary dependencies.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @since 2016/12/17
	 */
	public final Set<ProjectBinary> binaryDependencies(boolean __rec)
		throws InvalidProjectException
	{
		Set<ProjectBinary> rv = new LinkedHashSet<>();
		binaryDependencies(rv, __rec);
		return rv;
	}
	
	/**
	 * Returns the dependencies of this project.
	 *
	 * @return The project dependencies.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @since 2016/12/18
	 */
	public final Set<Project> dependencies()
		throws InvalidProjectException
	{
		Set<Project> rv = new LinkedHashSet<>();
		dependencies(rv);
		return rv;
	}
	
	/**
	 * Returns the path to the file.
	 *
	 * @return The file path.
	 * @since 2016/12/17
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Recursively determines the time and date of the project base.
	 *
	 * @return The time that the binary or source code last changed in
	 * milliseconds.
	 * @since 2016/12/17
	 */
	public final long time()
	{
		// Use precached time
		long rv = this._time;
		if (rv != Long.MIN_VALUE)
			return rv;
		
		// Otherwise scan
		try
		{
			this._time = (rv = __recursiveDate(this.path).toMillis());
			return rv;
		}
		
		// Just do not update the time
		catch (IOException e)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * Obtains the binary projects which this binary project depends on and
	 * places them into the specified set.
	 *
	 * @param __did The projects which were processed so that they are not
	 * handled multiple times.
	 * @param __out The output set where dependencies are placed.
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @throws InvalidProjectException If a dependency is not valid.
	 * @throws NullPointerException If no output was specified
	 * @since 2016/12/18
	 */
	private final void __internalBinaryDependencies(Set<Project> __did,
		Set<ProjectBinary> __out, boolean __rec)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Must always exist
		if (__did == null)
			__did = new HashSet<>();
		
		// Go through dependencies
		for (Project p : dependencies())
		{
			// Ignore if already handled
			if (__did.contains(p))
				continue;
			__did.add(p);
			
			// Add binary to output
			ProjectBinary bin = p.binary();
			__out.add(bin);
			
			// Get dependencies of those depends
			if (__rec)
				((ProjectBase)bin).
					__internalBinaryDependencies(__did, __out, true);
		}
	}
	
	/**
	 * Walks the directory tree and returns the highest modification date.
	 *
	 * @param __p The path to search through.
	 * @return The highest modification time.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private static FileTime __recursiveDate(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Start at the earliest date
		FileTime rv = _EARLIEST_DATE;
		
		// Look at all entries
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
		{
			for (Path p : ds)
			{
				FileTime tt;
				if (Files.isDirectory(p))
					tt = __recursiveDate(p);
				else
					tt = Files.getLastModifiedTime(p);
				
				// Use this date instead?
				if (tt.compareTo(rv) > 0)
					rv = tt;
			}
		}
		
		// Return the latest
		return rv;
	}
}

