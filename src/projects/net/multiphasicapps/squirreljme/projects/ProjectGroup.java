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
	 * @param __bc The compiler to use for compilation.
	 * @return The binary project information.
	 * @throws CompilationFailedException If project compilation failed.
	 * @throws IOException On read/write errors.
	 * @throws MissingSourceException If the project has no source code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public final ProjectInfo compileSource(Compiler __bc)
		throws CompilationFailedException, IOException, MissingSourceException,
			NullPointerException
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
		
		// Recursively build any dependencies which are out of date, except
		// for this project
		if (!__recursiveBuildDepends(__bc, src, src))
			return binary();
		
		// Temporary output JAR name
		ProjectName name = this.name;
		Path tempjarname = Files.createTempFile("squirreljme-compile",
			name.toString());
		
		// Need to build the output JAR
		try
		{
			// Create output ZIP to compile into
			try (ZipStreamWriter zip = new ZipStreamWriter(
				Channels.newOutputStream(FileChannel.open(tempjarname,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE))))
			{
				// Setup compiler output and input
				__CompilerOutput__ co = new __CompilerOutput__(zip);
				__CompilerInput__ ci = new __CompilerInput__(list, src);
				
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
				if (!__bc.compile(System.err, co, ci, Arrays.<String>asList(
					"-target", "1.7", "-source", "1.7", "-g",
					"-Xlint:deprecation"), ccthese))
					throw new CompilationFailedException(String.format(
						"CI0c %s", name));
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
			try
			{
				Files.delete(tempjarname);
			}
			
			// Ignore
			catch (IOException e)
			{
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
	 * Recursively attempts to build the dependencies for the current
	 * {@code __at} project if the dependency is out of date or has a missing
	 * binary.
	 *
	 * @param __bc The compiler to use.
	 * @param __root The root project.
	 * @param __at The current at project.
	 * @return {@code false} if no builds were ever performed.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	final boolean __recursiveBuildDepends(Compiler __bc, ProjectInfo __root,
		ProjectInfo __at)
		throws IOException, NullPointerException
	{
		// Check
		if (__bc == null || __root == null || __at == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
	
	/**
	 * Initializes the manifest that is to be used on the binary output file.
	 *
	 * @param __man The manifest to write to.
	 * @param __src The source project to source data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	final void __setupManifest(MutableJavaManifest __man,
		ProjectInfo __src)
		throws NullPointerException
	{
		// Check
		if (__man == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Get main attributes
		MutableJavaManifestAttributes attr = __man.getMainAttributes();
		
		// Try to get the version number of the project manager
		ProjectList list = this.list;
		ProjectGroup pmgrp = list.get("projects");
		ProjectInfo pminf = (pmgrp == null ? null : pmgrp.any());
		attr.put(new JavaManifestKey("Created-By"),
			(pminf != null ? pminf.version() : "0.0.0") + " (SquirrelJME)");
		
		// Is this a MIDlet?
		boolean ismidlet = __src.isMIDlet();
		
		// Add required and optional dependencies
		boolean req = false;
		int depid = 0;
		do
		{
			// Flip
			req = !req;
			
			// Go through dependencies
			for (ProjectName dn : __src.dependencies(req))
			{
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
		} while (!req);
		
		// If it is a midlet then it must have these attributes also
		if (ismidlet)
		{
			attr.put(new JavaManifestKey("MIDlet-Name"), __src.title());
			attr.put(new JavaManifestKey("MIDlet-Vendor"), __src.vendor());
			attr.put(new JavaManifestKey("MIDlet-Version"), __src.version());
		}
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

