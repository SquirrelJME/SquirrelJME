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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.BootCompiler;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is a project group which contains a reference to binary and/or
 * source projects.
 *
 * @since 2016/09/18
 */
public final class ProjectGroup
	implements Comparable<ProjectGroup>
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The owning project list. */
	protected final ProjectList list;
	
	/** The name of the project group. */
	protected final ProjectName name;
	
	/** The current binary project. */
	private volatile ProjectInfo _bin;
	
	/** The current source project. */
	private volatile ProjectInfo _src;
	
	/**
	 * Initializes the project group.
	 *
	 * @param __pl The owning package list.
	 * @param __n The name of this package.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	ProjectGroup(ProjectList __pl, ProjectName __n)
		throws NullPointerException
	{
		// Check
		if (__pl == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.list = __pl;
		this.name = __n;
	}
	
	/**
	 * This returns the associated binary project which contains class files
	 * and other resource.
	 *
	 * @return The binary project information, or {@code null} if there is no
	 * binary project.
	 * @since 2016/09/18
	 */
	public final ProjectInfo binary()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._bin;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final int compareTo(ProjectGroup __o)
	{
		return this.name.compareTo(__o.name);
	}
	
	/**
	 * Compiles the source code for this project.
	 *
	 * @param __bc The compiler to use for compilation.
	 * @return The binary project information.
	 * @throws IOException On read/write errors.
	 * @throws MissingSourceException If the project has no source code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public final ProjectInfo compileSource(BootCompiler __bc)
		throws IOException, MissingSourceException, NullPointerException
	{
		// Check
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI0a Cannot build the project because it does
		// not have source code available.}
		ProjectList list = this.list;
		ProjectInfo src = source();
		if (src == null)
			throw new MissingSourceException("CI0a");
		
		// Temporary output JAR name
		Path tempjarname = Files.createTempFile("squirreljme-compile",
			this.name.toString());
		
		// Need to build the output JAR
		try
		{
			// Create output ZIP to compile into
			try (ZipStreamWriter zip = new ZipStreamWriter(
				Channels.newOutputStream(FileChannel.open(tempjarname,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE))))
			{
				// Setup compiler output
				__CompilerOutput__ co = new __CompilerOutput__(zip);
				
				if (true)
					throw new Error("TODO");
				
				// Call the compiler
				__bc.compile(co);
			}
			
			// Determine the name of the binary
			ProjectInfo bin = binary();
			Path jarname = (bin != null ? bin.path() :
				list.binaryPath().resolve(this.name.toString() + ".jar"));
			
			// Replace any existing binary
			Files.move(tempjarname, jarname,
				StandardCopyOption.REPLACE_EXISTING);
			
			// Initialize project information
			try (ZipFile zip = ZipFile.open(FileChannel.open(jarname,
				StandardOpenOption.READ)))
			{
				bin = new ProjectInfo(list, jarname, zip, true);
			}
			
			// Set binary file
			__setBinary(bin);
			
			// Return it
			return bin;
		}
		
		// Delete any temporary files
		finally
		{
			// Delete it
			Files.delete(tempjarname);	
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ProjectGroup))
			return false;
		
		// Compare
		return this.name.equals(((ProjectGroup)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Returns the project information of the specified type.
	 *
	 * @param __t The type to get the project information for.
	 * @return The project information of the given type or {@code null} if
	 * it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public final ProjectInfo ofType(ProjectType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__t)
		{
				// Binary?
			case BINARY:
				return binary();
				
				// Source?
			case SOURCE:
				return source();
				
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * This returns the associated source project which contains source code
	 * and other resource.
	 *
	 * @return The source project information, or {@code null} if there is no
	 * source project.
	 * @since 2016/09/18
	 */
	public final ProjectInfo source()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._src;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Sets the binary project.
	 *
	 * @param __pi The project to set.
	 * @throws IllegalStateException If the name does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	final void __setBinary(ProjectInfo __pi)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI06 Attempt to set binary project to a group
		// of a different name.}
		if (!this.name.equals(__pi.name()))
			throw new IllegalStateException("CI06");
		
		// Lock
		synchronized (this.lock)
		{
			this._bin = __pi;
		}
	}
	
	/**
	 * Sets the source project.
	 *
	 * @param __pi The project to set.
	 * @throws IllegalStateException If the name does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	final void __setSource(ProjectInfo __pi)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI09 Attempt to set source project to a group
		// of a different name.}
		if (!this.name.equals(__pi.name()))
			throw new IllegalStateException("CI09");
		
		// Lock
		synchronized (this.lock)
		{
			this._src = __pi;
		}
	}
}

