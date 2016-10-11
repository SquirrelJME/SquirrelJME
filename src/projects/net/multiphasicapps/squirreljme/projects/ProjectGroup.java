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
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Set;
import net.multiphasicapps.javac.base.Compiler;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.java.manifest.MutableJavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.
	MutableJavaManifestAttributes;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

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
	
	/** Was this project just compiled? */
	private volatile boolean _justcompiled;
	
	/** Is the project in the compile state? */
	private volatile boolean _inccstate;
	
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
	 * Attempts to return the binary project and if that does not exist then
	 * the source one will be returned if that exists.
	 *
	 * @return Either the binary project or the source one, or {@code null} if
	 * neither exist.
	 * @since 2016/09/19
	 */
	public final ProjectInfo any()
	{
		// Lock
		synchronized (this.lock)
		{
			ProjectInfo rv = this._bin;
			if (rv != null)
				return rv;
			return this._src;
		}
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
	 * @param __bc The compiler to use for compilation, if {@code null} then
	 * a fallback compiler is attempted to be used.
	 * @param __lu The type of dependencies to lookup.
	 * @return The binary project information.
	 * @throws CompilationFailedException If project compilation failed.
	 * @throws IOException On read/write errors.
	 * @throws MissingSourceException If the project has no source code.
	 * @throws NullPointerException If no compiler was specified and there is
	 * no compiler available for usage; or {@code __lu} is {@code null}.
	 * @since 2016/09/18
	 */
	public final ProjectInfo compileSource(Compiler __bc,
		DependencyLookupType __lu)
		throws CompilationFailedException, IOException, MissingSourceException,
			NullPointerException
	{
		return compileSource(__bc, __lu, false);
	}
	
	/**
	 * Compiles the source code for this project.
	 *
	 * @param __bc The compiler to use for compilation, if {@code null} then
	 * a fallback compiler is attempted to be used.
	 * @param __lu The type of dependencies to lookup.
	 * @param __opt If {@code true} then optional dependencies are also checked
	 * and rebuild as needed (if they exist).
	 * @return The binary project information.
	 * @throws CompilationFailedException If project compilation failed.
	 * @throws IOException On read/write errors.
	 * @throws MissingSourceException If the project has no source code.
	 * @throws NullPointerException If no compiler was specified and there is
	 * no compiler available for usage; or {@code __lu} is {@code null}.
	 * @since 2016/09/19
	 */
	public final ProjectInfo compileSource(Compiler __bc,
		DependencyLookupType __lu, boolean __opt)
		throws CompilationFailedException, IOException, MissingSourceException,
			NullPointerException
	{
		// Check
		if (__lu == null)
			throw new NullPointerException("NARG");
		
		// Try the fallback if one was not specified
		if (__bc == null)
		{
			__bc = ProjectList._SPECIFIED_FALLBACK_COMPILER;
			
			// {@squirreljme.error CI0m No fallback compiler could be used,
			// project compilation is not possible.}
			if (__bc == null)
				throw new NullPointerException("CI0m");
		}
		
		// If there is no source code, alternatively try the binary
		ProjectName name = this.name;
		ProjectList list = this.list;
		ProjectInfo src = source();
		if (src == null)
		{
			ProjectInfo bin = binary();
			
			// {@squirreljme.error CI0a Cannot build the project because it
			// does not have source code available.}
			if (bin == null)
				throw new MissingSourceException("CI0a");
			return bin;
		}
		
		// Otherwise build it
		boolean enteredstate = false;
		try
		{
			// If this is in the compilation state then return it
			ProjectInfo bin = null, maybe = binary();
			synchronized (this.lock)
			{
				// This could be part of an optional build dependency which has
				// been built already
				if (this._inccstate)
					return maybe;
			
				// Enter compilation state
				enteredstate = true;
				this._inccstate = true;
			}
		
			// Go through all dependencies and compile those also
			for (ProjectName dn : src.dependencies(__lu, false))
			{
				// {@squirreljme.error CI0l Cannot compile the specified
				// project because the dependency does not exist. (The project
				// being compiled; The dependency that is missing)}
				ProjectGroup dg = list.get(dn);
				if (dg == null)
					throw new MissingDependencyException(String.format(
						"CI0l %s %s", name, dn));
			
				// Perform compilation on it
				dg.compileSource(__bc, __lu, __opt);
			}
		
			// If compiling a project and it has a binary, it might not really
			// be needed to compile its code if it is up to date
			if (maybe != null)
			{
				// Get base dates
				FileTime pbin = maybe.date(),
					psrc = src.date();
			
				// If the binary was just compiled, do not compile it twice
				// If the source is newer than the binary then recompile it
				// Also recompile if any dependency has source code newer than
				// the binary
				if (this._justcompiled ||
					__dependencySourceDate(__lu, name).compareTo(pbin) <= 0)
					bin = maybe;
			} 
		
			// Perform compilation
			if (bin == null)
				bin = __compile(__bc, __lu);
		
			// Compile any optional dependencies if requested following the
			// compilation of this one
			if (__opt)
				for (ProjectName dn : src.dependencies(__lu, true))
				{
					// Ignore dependencies which do not exist
					ProjectGroup dg = list.get(dn);
					if (dg == null)
						continue;
				
					// Compile it
					try
					{
						dg.compileSource(__bc, __lu, __opt);
					}
				
					// Failed to build it, ignore
					catch (InvalidProjectException e)
					{
						// {@squirreljme.error CI0k Failed to compile an
						// optional dependency, it will be ignored. (This
						// project; The dependency; The reason why it failed)}
						System.err.printf("CI0k %s %s %s%n", name, dn,
							e.toString());
					}
				}
		
			// Return the output binary
			return bin;
		}
		
		// Clear the in compilation state
		finally
		{
			if (enteredstate)
				synchronized (this.lock)
				{
					this._inccstate = false;
				}
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
	 * Returns the name of this group.
	 *
	 * @return The group name.
	 * @since 2016/09/29
	 */
	public final ProjectName name()
	{
		return this.name;
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
				
				// Compiled?
			case COMPILED:
				try
				{
					return compileSource(null, DependencyLookupType.INTERNAL,
						false);
				}
				
				// Failed to compile, fallback to the binary
				catch (IOException e)
				{
					return binary();
				}
				
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
	 * Compiles the specified project and caches the output binary.
	 *
	 * @param __bc The compiler interface to use.
	 * @param __lu The dependency lookup type to use.
	 * @return The resulting binary.
	 * @throws CompilationFailedException If compilation failed.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	final ProjectInfo __compile(Compiler __bc, DependencyLookupType __lu)
		throws CompilationFailedException, IOException, NullPointerException
	{
		// Check
		if (__lu == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI0i This project is currently being
		// compiled. (The project name)}
		ProjectName name = this.name;
		System.err.printf("CI0i %s%n", name);
		
		// {@squirreljme.error CI0j Compilation step called for the specified
		// project, however it has no source code. (The project name)}
		ProjectInfo src = source();
		if (src == null)
			throw new MissingSourceException(String.format("CI0j %s", name));
		
		// Temporary output JAR name
		Path tempjarname = Files.createTempFile("squirreljme-compile",
			name.toString());
		
		// Need to build the output JAR
		ProjectInfo bin = null;
		try
		{
			// Create output ZIP to compile into
			try (ZipStreamWriter zip = new ZipStreamWriter(
				Channels.newOutputStream(FileChannel.open(tempjarname,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE))))
			{
				// Setup compiler output and input
				__CompilerOutput__ co = new __CompilerOutput__(zip);
				__CompilerInput__ ci = new __CompilerInput__(list, src, __lu);
				
				// Setup target manifest
				MutableJavaManifest man = new MutableJavaManifest(
					src.manifest());
				__setupManifest(man, src);
				
				// Write the manifest
				try (OutputStream os = zip.nextEntry("META-INF/MANIFEST.MF",
					ZipCompressionType.DEFAULT_COMPRESSION))
				{
					man.write(os);
				}
				
				// Files to be compiled
				Set<String> ccthese = new SortedTreeSet<>();
				
				// Determine the files to be compiled. If any files are not
				// being compiled then place them in the output JAR during the
				// scanning set (simplifies things)
				byte[] buf = null;
				for (String s : src.contents())
					if (__isValidJavaFileName(s))
						ccthese.add(s);
					else
					{
						// Ignore the manifest
						if (s.equals("META-INF/MANIFEST.MF"))
							continue;
						
						// Missing buffer?
						if (buf == null)
							buf = new byte[4096];
						
						// Copy the data
						try (OutputStream os = zip.nextEntry(s,
							ZipCompressionType.DEFAULT_COMPRESSION);
							InputStream is = src.open(s))
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
				buf = null;
				
				// Call the compiler
				// Any calls that are made to the CompilerOutput would have
				// been placed in the JAR, so adding does not have to be
				// performed following this.
				// {@squirreljme.error CI0c Failed to compile the source code
				// for the specified project. (The project name)}
				if (!ccthese.isEmpty())
					if (!__bc.compile(System.err, co, ci, Arrays.<String>
						asList("-target", "1.7", "-source", "1.7", "-g",
						"-Xlint:deprecation"), ccthese))
						throw new CompilationFailedException(String.format(
							"CI0c %s", name));
			}
			
			// Determine the name of the binary
			bin = binary();
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
			
			// Set the just compiled flag
			this._justcompiled = true;
		}
		
		// Delete any temporary files, if they exist
		finally
		{
			// Delete it
			try
			{
				Files.delete(tempjarname);
			}
			
			// Ignore
			catch (IOException e)
			{
			}
		}
		
		// Return it
		return bin;
	}
	
	/**
	 * Returns the highest file time that is associated with source code in
	 * a dependency.
	 *
	 * @param __lu The lookup type for dependencies.
	 * @param __at The current project name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	private FileTime __dependencySourceDate(DependencyLookupType __lu,
		ProjectName __at)
		throws NullPointerException
	{
		// Check
		if (__lu == null || __at == null)
			throw new NullPointerException("NARG");
		
		// Get the group
		ProjectList list = this.list;
		ProjectGroup grp = list.get(__at);
		
		// If missing, use the earliest date possible
		ProjectInfo src;
		if (grp == null || null == (src = grp.source()))
			return FileTime.fromMillis(Long.MIN_VALUE);
		
		// Fix on the base date
		FileTime rv = src.date();
		
		// Go through all dependencies to see if they have newer sources
		for (ProjectName dn : src.dependencies(__lu))
		{
			// Do not consider any projects that do not exist
			ProjectGroup dg = list.get(dn);
			ProjectInfo ds;
			if (dg == null || null == (ds = dg.source()))
				continue;
			
			// Get their date
			FileTime dd = dg.__dependencySourceDate(__lu, dn);
			
			// If the date is newer, use it
			if (dd.compareTo(rv) > 0)
				rv = dd;
		}
		
		// Return it
		return rv;
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
			
			// Clear the just compiled flag
			this._justcompiled = false;
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
	
	/**
	 * Initializes the manifest that is to be used on the binary output file.
	 *
	 * @param __man The manifest to write to.
	 * @param __src The source project to source data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	final void __setupManifest(MutableJavaManifest __man, ProjectInfo __src)
		throws NullPointerException
	{
		// Check
		if (__man == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Get main attributes
		MutableJavaManifestAttributes attr = __man.getMainAttributes();
		
		throw new Error("TODO");
		/*
		// Try to get the version number of the project manager
		ProjectList list = this.list;
		ProjectGroup pmgrp = list.get("projects");
		ProjectInfo pminf = (pmgrp == null ? null : pmgrp.any());
		attr.put(new JavaManifestKey("Created-By"),
			(pminf != null ? pminf.version() : "0.0.0") + " (SquirrelJME)");
		
		// Is this a MIDlet?
		boolean ismidlet = __src.isMIDlet();
		
		// Class path (includes optionals)
		StringBuilder cp = new StringBuilder();
		
		// Add required and optional dependencies
		int depid = 0;
		for (int z = 0; z < 2; z++)
		{
			// Required?
			boolean req = (z == 0);
			
			// Go through dependencies
			for (ProjectName dn : __src.dependencies(!req))
			{
				// Add it regardless
				cp.append(dn);
				cp.append(".jar ");
				
				// {@squirreljme.error CI0b The project has a required
				// dependency on the specified project, however it does not
				// exist. (This project; The dependency)}
				ProjectGroup dg = list.get(dn);
				if (req && dg == null)
					throw new MissingDependencyException(String.format(
						"CI0b %s %s", __src.name(), dn));
				
				// Get any information about it
				ProjectInfo di = (dg != null ? dg.any() : null);
				
				// Determine string form
				// If there is an optional dependency on a project which is
				// missing then use a special SquirrelJME specfic optional
				String format = (di != null ?
					String.format("liblet;%s;%s;%s;%s+",
						(!req ? "optional" : "required"), di.title(),
						di.vendor(), di.version()) :
					String.format("x-squirreljme-namespace;optional;%s;;",
						dn));
				
				// Add liblet dependency
				attr.put(new JavaManifestKey(String.format(
					"LIBlet-Dependency-%d", depid)), format);
				
				// If this is a MIDlet then add the library as a dependency
				if (ismidlet)
					attr.put(new JavaManifestKey(String.format(
						"MIDlet-Dependency-%d", depid)), format);
				
				// Increase dependency ID
				depid++;
			}
		}
		
		// If it is a midlet then it must have these attributes also
		if (ismidlet)
		{
			attr.put(new JavaManifestKey("MIDlet-Name"), __src.title());
			attr.put(new JavaManifestKey("MIDlet-Vendor"), __src.vendor());
			attr.put(new JavaManifestKey("MIDlet-Version"), __src.version());
		}
		
		// Classpath
		attr.put(new JavaManifestKey("Class-Path"), cp.toString());
		*/
	}
	
	/**
	 * Checks whether the given file is a valid Java file name.
	 *
	 * @param __s The file name to check.
	 * @return If {@code true} then the file is valid.
	 * @since 2016/09/19
	 */
	private static boolean __isValidJavaFileName(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Only consider Java sources
		if (!__s.endsWith(".java"))
			return false;
		
		// Remove the extension
		__s = __s.substring(0, __s.length() - 5);
		
		// Go through all characters
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Ok
			if (Character.isLowerCase(c) || Character.isUpperCase(c) ||
				Character.isDigit(c) || c == '_' || c == '/')
				continue;
			
			// Get the last slash and the last dash
			int ls = __s.lastIndexOf('/');
			int ld = __s.lastIndexOf('-');
			
			// Cannot be package-info if...
			// Illegal character before the last slash
			// There is no last slash
			// Dash is before the slash, or is missing
			if (i < ls || ls < 0 || ld < ls)
				return false;
			
			// Check if it ends with the package info
			return __s.endsWith("/package-info");
		}
		
		// Valid
		return true;
	}
}

