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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This contains information about a single binary or source project.
 *
 * @since 2016/06/15
 */
public class ProjectInfo
	implements Comparable<ProjectInfo>
{
	/** The owning project list. */
	protected final ProjectList plist;
	
	/** The project manifest. */
	protected final JavaManifest manifest;
	
	/** The path to the project. */
	protected final Path path;
	
	/** Is this a ZIP file? */
	protected final boolean iszip;
	
	/** The name of this project. */
	protected final ProjectName name;
	
	/** Is this a binary project? */
	protected final boolean isbinary;
	
	/** Package groups this project is in. */
	private volatile Reference<Set<String>> _groups;
	
	/** The dependencies of this project. */
	private volatile Reference<Set<ProjectName>> _depends;
	
	/** Optional dependencies of this project. */
	private volatile Reference<Set<ProjectName>> _odepends;
	
	/**
	 * Initializes the project information from the given ZIP.
	 *
	 * @param __l The project list which contains this project.
	 * @param __p The path to the project.
	 * @param __zip The ZIP file for the project data.
	 * @param __bin Is this a binary project?
	 * @throws InvalidProjectException If this is not a valid project.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	ProjectInfo(ProjectList __l, Path __p, ZipFile __zip, boolean __bin)
		throws InvalidProjectException, IOException, NullPointerException
	{
		this(__l, __p, true, __loadManifestFromZIP(__zip), __bin);
	}
	
	/**
	 * Initializes the project information using the given manifest.
	 *
	 * @param __l The project list which contains this project.
	 * @param __p The path to the project.
	 * @param __zz Is this a ZIP file?
	 * @param __man The manifest data.
	 * @param __bin Is this a binary project?
	 * @throws InvalidProjectException If this is not a valid project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	ProjectInfo(ProjectList __l, Path __p, boolean __zz,
		JavaManifest __man, boolean __bin)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__l == null || __p == null || __man == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __l;
		this.path = __p;
		this.iszip = __zz;
		this.manifest = __man;
		this.isbinary = __bin;
		
		// Get main attributes
		JavaManifestAttributes main = __man.getMainAttributes();
		
		// {@squirreljme.error CI04 The project manifest does not specify the
		// project name, it is likely not a project. (The path to the project)}
		String rname = main.get("X-SquirrelJME-Name");
		if (rname == null)
			throw new InvalidProjectException(String.format("CI04 %s", __p));
		
		// Set name
		this.name = new ProjectName(rname);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int compareTo(ProjectInfo __pi)
		throws NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// Compare by name
		return this.name.compareTo(__pi.name);
	}
	
	/**
	 * Returns the date of the project's binary file or source tree.
	 *
	 * @return The date of the project.
	 * @throws InvalidProjectException If the date could not be read.
	 */
	public final FileTime date()
		throws InvalidProjectException
	{
		try
		{
			if (false)
				throw new IOException();
			
			throw new Error("TODO");
		}
		
		// {@squirreljme.error CI08 Could not get the latest modification
		// date of the project.}
		catch (IOException e)
		{
			throw new InvalidProjectException("CI08", e);
		}
	}
	
	/**
	 * Returns all of the projects that this project depends on.
	 *
	 * @return The set of projects this project depends on.
	 * @throws MissingDependencyException If a dependency is missing.
	 * @since 2016/06/25
	 */
	public final Set<ProjectName> dependencies()
		throws MissingDependencyException
	{
		return dependencies(false);
	}
	
	/**
	 * Returns all of the projects that this project depends on.
	 *
	 * @param __opt Select optional dependencies instead.
	 * @return The set of projects this project depends on. If optional
	 * dependencies are specified, they are only included if they exist.
	 * @throws MissingDependencyException If a dependency is missing.
	 * @since 2016/07/22
	 */
	public final Set<ProjectName> dependencies(boolean __opt)
		throws MissingDependencyException
	{
		// Get
		Reference<Set<ProjectName>> ref = (__opt ? this._odepends :
			this._depends);
		Set<ProjectName> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target
			Set<ProjectName> deps = new SortedTreeSet<>();
			
			// Read the manifest
			JavaManifest man = manifest();
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Get the dependency field
			String pids = attr.get((__opt ? "X-SquirrelJME-Optional" :
				"X-SquirrelJME-Depends"));
			if (pids != null)
			{
				int n = pids.length();
				for (int i = 0; i < n; i++)
				{
					char c = pids.charAt(i);
					
					// Ignore whitespace
					if (c <= ' ')
						continue;
					
					// Find the next space
					int j;
					for (j = i + 1; j < n; j++)
					{
						char d = pids.charAt(j);
						
						if (d == ' ')
							break;
					}
					
					// Split string
					String spl = pids.substring(i, j).trim();
					
					// Add it
					deps.add(new ProjectName(spl));
					
					// Skip
					i = j;
				}
			}
			
			// Lock
			rv = UnmodifiableSet.<ProjectName>of(deps);
			ref = new WeakReference<>(rv);
			if (__opt)
				this._odepends = ref;
			else
				this._depends = ref;
		}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be another project
		if (!(__o instanceof ProjectInfo))
			return false;
		
		// Compare the name
		return this.name.equals(((ProjectInfo)__o).name);
	}
	
	/**
	 * Returns the project groups that this project is a part of, this
	 * information is used by the target build system to include extra projects
	 * that may be needed on a target system.
	 *
	 * @return The set of project groups.
	 * @since 2016/09/02
	 */
	public final Set<String> groups()
	{
		// Get
		Reference<Set<String>> ref = this._groups;
		Set<String> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target set
			Set<String> target = new SortedTreeSet<>();
			
			// Fill properties
			String prop = this.manifest.getMainAttributes().get(
				"X-SquirrelJME-Group");
			if (prop != null)
			{
				int n = prop.length();
				for (int i = 0; i < n; i++)
				{
					// Ignore whitespace
					char c = prop.charAt(i);
					if (c <= ' ')
						continue;
					
					// Find end
					int j = i + 1;
					for (; j < n; j++)
						if (prop.charAt(j) <= ' ')
							break;
					
					// Add split
					target.add(prop.substring(i, j));
					i = j + 1;
				}
			}
			
			// Store
			rv = UnmodifiableSet.<String>of(target);
			this._groups = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Returns the main entry class for a general Java program.
	 *
	 * @return The main class to enter the program at or {@code null} if not
	 * found.
	 * @since 2016/06/20
	 */
	public final String mainClass()
	{
		return this.manifest.getMainAttributes().get("Main-Class");
	}
	
	/**
	 * Returns the manifest of this project.
	 *
	 * @return The project manifest.
	 * @since 2016/06/15
	 */
	public final JavaManifest manifest()
	{
		return this.manifest;
	}
	
	/**
	 * Returns the name of this project.
	 *
	 * @return The project name.
	 * @since 2016/06/15
	 */
	public final ProjectName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the path to this project.
	 *
	 * @return The project path.
	 * @since 2016/06/19
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Returns the type of project that this is.
	 *
	 * @return The project type.
	 * @since 2016/09/18
	 */
	public final ProjectType type()
	{
		if (this.isbinary)
			return ProjectType.BINARY;
		return ProjectType.SOURCE;
	}
	
	/**
	 * Loads a manifest from the given ZIP file.
	 *
	 * @parma __zip The ZIP to load the manifest from.
	 * @return The parsed manifest data.
	 * @throws InvalidProjectException If the project is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private static JavaManifest __loadManifestFromZIP(ZipFile __zip)
		throws InvalidProjectException, IOException, NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Find manifest file
		ZipEntry ent = __zip.get("META-INF/MANIFEST.MF");
		
		// {@squirreljme.error CI02 No manifest exists in the JAR.}
		if (ent == null)
			throw new InvalidProjectException("CI02");
		
		// Open input stream
		try (InputStream is = ent.open())
		{
			return new JavaManifest(is);
		}
		
		// {@squirreljme.error CI03 The manifest is not correctly formed.}
		catch (JavaManifestException e)
		{
			throw new InvalidProjectException("CI03", e);
		}
	}
}

