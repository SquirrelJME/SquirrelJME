// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * This class represents the base for projects. A project may be an API, a
 * MIDlet, or a LIBlet. However, most projects from the these distinctions are
 * very much the same. A project points to optional source code and binary
 * projects which may or may not exist.
 *
 * @since 2016/12/14
 */
public final class Project
{
	/** The owning project manager. */
	protected final ProjectManager projectman;
	
	/** The type of project this is. */
	protected final NamespaceType type;
	
	/** The name of this project. */
	protected final ProjectName name;
	
	/** Lock for source code and binaries. */
	protected final Object lock;
	
	/** The source representation of this project. */
	private volatile ProjectSource _source;
	
	/** The binary the project is associated with. */
	private volatile Reference<ProjectBinary> _binary;
	
	/** Is this in a build or dependency checking? */
	private volatile boolean _inbuild;
	
	/**
	 * Initializes the project.
	 *
	 * @param __pm The owning project manager.
	 * @param __t The type of namespace the project is in.
	 * @param __n The name of the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	Project(ProjectManager __pm, NamespaceType __t, ProjectName __n)
		throws NullPointerException
	{
		// Check
		if (__pm == null || __t == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projectman = __pm;
		this.type = __t;
		this.name = __n;
		
		// Use shared lock to prevent major deadlocks when accessing sources
		// and binaries, especially across multiple threads
		this.lock = __pm._lock;
	}
	
	/**
	 * Returns the binary representation of the given project, if it does not
	 * exist (since it has not been compiled) or is out of date then it will be
	 * compiled.
	 *
	 * @return The binary project file.
	 * @throws InvalidProjectException If the project could not be compiled or
	 * a binary does not exist and there is no source code.
	 * @since 2016/12/17
	 */
	public final ProjectBinary binary()
		throws InvalidProjectException
	{
		// Lock
		synchronized (this.lock)
		{
			Reference<ProjectBinary> ref = this._binary;
			ProjectBinary rv = (ref != null ? ref.get() : null);
			
			// The base date and path of the binary
			ProjectName name = this.name;
			Path binpath = binaryPath();
			
			// Try opening it as a binary
			if (rv == null)
				try
				{
					// Wrap binary representation
					rv = __createBinary(binpath);
					this._binary = new WeakReference<>(rv);
				}
				catch (InvalidProjectException|IOException e)
				{
					// Ignore
				}
			
			// If there is no source code then just use the binary assuming
			// one exists
			ProjectSource src = source();
			if (src == null)
			{
				// {@squirreljme.error AT05 Could not obtain the binary for
				// the given project because it has no associated source
				// code. (The project name)}
				if (rv == null)
					throw new InvalidProjectException(String.format(
						"AT05 %s", name));
				
				return rv;
			}
			
			// If in a build then it is possible a recursive loop could be
			// entered cuasing multiple threads accessing projects to deadlock
			if (this._inbuild)
				return rv;
			
			// Mark as being in a build so this does not cause potential
			// infinite recursion
			try
			{
				this._inbuild = true;
				
				// Get project dependencies which is used to check if they are
				// out of date and for building if so
				Set<ProjectBinary> deps = src.binaryDependencies(true);
				
				// Trivial rebuild check if the source is newer
				long srcdate = src.time();
				long bindate = (rv == null ? Long.MIN_VALUE : rv.time());
				boolean rebuild = (rv == null || srcdate > bindate);
				
				// Check to see if any dependencies are newer
				if (!rebuild)
					for (ProjectBinary dep : deps)
						if (rebuild |= (dep.time() > bindate))
							break;
				
				// Compile the project?
				if (rebuild)
					try
					{
						src.__compile(binpath, deps);
						this._binary = new WeakReference<>(
							(rv = __createBinary(binpath)));
					}
					catch (IOException e)
					{
						// {@squirreljme.error AT07 There was a read/write
						// error while compiling the project. (The project
						// name)}
						throw new InvalidProjectException(
							String.format("AT07 %s", name), e);
					}
			}
			
			// No longer in a build so can recurse safely
			finally
			{
				this._inbuild = false;
			}
			
			// Return the binary
			return rv;
		}
	}
	
	/**
	 * Returns the path to the output binary.
	 *
	 * @return The path to the output binary.
	 * @since 2016/12/24
	 */
	public final Path binaryPath()
	{
		// Lock
		synchronized (this.lock)
		{
			// If the binary is already set then use that path
			Reference<ProjectBinary> ref = this._binary;
			ProjectBinary bin = (ref != null ? ref.get() : null);
			if (bin != null)
				return bin.path();
		}
		
		// Use default
		return this.projectman.binaryPath().resolve(name + ".jar");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/14
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Returns the name of this project.
	 *
	 * @return The project name.
	 * @since 2016/12/14
	 */
	public final ProjectName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the source project for this project.
	 *
	 * @return The source for this project or {@code null} if it does not
	 * exist.
	 * @since 2016/12/14
	 */
	public final ProjectSource source()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._source;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/14
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
	 * @since 2016/12/14
	 */
	public final NamespaceType type()
	{
		return this.type;
	}
	
	/**
	 * Attempts to load and initialize a binary using the given path.
	 *
	 * @param __p The path to the file.
	 * @return The representation of the given binary.
	 * @throws InvalidProjectException If the project is no valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	final ProjectBinary __createBinary(Path __p)
		throws InvalidProjectException, IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		switch (this.type)
		{
				// {@squirreljme.error AT06 The specified project type
				// cannot have a binary. (The project type)}
			case ASSET:
			case BUILD:
				throw new InvalidProjectException(String.format("AT06 %s",
					this.type));
			
				// An API
			case API:
				return new APIBinary(this, __p);
		
				// MIDlet
			case MIDLET:
				return new MidletBinary(this, __p);
		
				// LIBlet
			case LIBLET:
				return new LibletBinary(this, __p);
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Initializes the project source from the given path.
	 *
	 * @param __p The path to the source code root.
	 * @throws InvalidProjectException If the source code is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	final void __initializeSource(Path __p)
		throws InvalidProjectException, IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent source from changing between calls
		synchronized (this.lock)
		{
			// Depends on the type
			ProjectSource src;
			switch (this.type)
			{
					// {@squirreljme.error AT04 The specified project type
					// cannot have source code. (The project type)}
				case ASSET:
				case BUILD:
					throw new InvalidProjectException(String.format("AT04 %s",
						this.type));
				
					// An API
				case API:
					src = new APISource(this, __p);
					break;
			
					// MIDlet
				case MIDLET:
					src = new MidletSource(this, __p);
					break;
			
					// LIBlet
				case LIBLET:
					src = new LibletSource(this, __p);
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Set source
			this._source = src;
		}
	}
}

