// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

/**
 * This class is used to initialize and setup source and binary project
 * representations in a common means which does not require
 * {@link BuilderFactory} to be initialized to have a shared setup of the
 * project state.
 *
 * @since 2018/07/29
 */
public final class ProjectManager
{
	/** The directory for the project root (source code). */
	protected final Path sourceroot;
	
	/** The default timespace to use for projects. */
	protected final TimeSpaceType deftimespace;
	
	/** The directory for each timespace binaries. */
	private final Path[] _bin;
	
	/** The source managers for each timespace. */
	private final SourceManager[] _sourcemanagers =
		new SourceManager[TimeSpaceType.values().length];
	
	/** Binary managers for each timespace. */
	private final BinaryManager[] _binarymanagers =
		new BinaryManager[TimeSpaceType.values().length];
	
	/**
	 * Initializes the project manager.
	 *
	 * @param __sr The source root.
	 * @param __brs The binary output roots.
	 * @param __dts Default time space.
	 * @throws IllegalArgumentException If the binary outputs does not match
	 * the number of available timespaces.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/29
	 */
	public ProjectManager(Path __sr, Path[] __brs, TimeSpaceType __dts)
		throws IllegalArgumentException, NullPointerException
	{
		if (__sr == null || __brs == null || __dts == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AU0j Binary paths array count does not match
		// the number of time spaces available.}
		if (__brs.length != TimeSpaceType.values().length)
			throw new IllegalArgumentException("AU0j");
		
		// These cannot be null, that would be bad
		for (Path p : (__brs = __brs.clone()))
			if (p == null)
				throw new NullPointerException("NARG");
		
		this.sourceroot = __sr;
		this._bin = __brs;
		this.deftimespace = __dts;
	}
	
	/**
	 * Initializes a binary manager using the default timespace.
	 *
	 * @return The binary manager with the default timespace.
	 * @throws IOException On read errors.
	 * @since 2018/12/22
	 */
	public BinaryManager binaryManager()
		throws IOException
	{
		return this.binaryManager(this.deftimespace);
	}
	
	/**
	 * Returns the binary manager to use for binary project retrieval.
	 *
	 * @param __t The timespace to build for.
	 * @return The manager for the given timespace.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	public BinaryManager binaryManager(TimeSpaceType __t)
		throws IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		int i = __t.ordinal();
		BinaryManager[] binarymanagers = this._binarymanagers;
		BinaryManager rv = binarymanagers[i];
		if (rv == null)
			binarymanagers[i] =
				(rv = new BinaryManager(this._bin[i], sourceManager(__t)));
		return rv;
	}
	
	/**
	 * Builds the specified projects using the default time-space.
	 *
	 * @param __p The projects to be built.
	 * @return The binaries which are associated with the given project.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/22
	 */
	public Binary[] build(String... __p)
		throws NullPointerException
	{
		return this.build(this.deftimespace, __p);
	}
	
	/**
	 * Builds the specified projects.
	 *
	 * @param __t The timespace to use for projects.
	 * @param __p The projects to be built.
	 * @return The binaries which are associated with the given project.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	public Binary[] build(TimeSpaceType __t, String... __p)
		throws NullPointerException
	{
		if (__t == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Need the binary manager to build these projects
		BinaryManager bm;
		try
		{
			bm = this.binaryManager(__t);
		}
		
		// {@squirreljme.error AU0k Could not obtain the binary manager.}
		catch (IOException e)
		{
			throw new RuntimeException("AU0k", e);
		}
		
		// Get binaries
		int n = __p.length;
		Binary[] bins = new Binary[n];
		for (int i = 0; i < n; i++)
			bins[i] = bm.get(__p[i]);
		
		// Do not return duplicate binaries
		Set<Binary> rv = new LinkedHashSet<>();
		
		// Compile all of the project and return required class path for
		// it to operate
		for (Binary i : bins)
			for (Binary b : bm.compile(i))
				rv.add(b);
		
		// Return the completed set
		return rv.<Binary>toArray(new Binary[rv.size()]);
	}
	
	/**
	 * Initializes a source manager using the default timespace.
	 *
	 * @return The source manager with the default timespace.
	 * @throws IOException On read errors.
	 * @since 2018/12/22
	 */
	public SourceManager sourceManager()
		throws IOException
	{
		return this.sourceManager(this.deftimespace);
	}
	
	/**
	 * Returns the source manager to use for source code retrieval.
	 *
	 * @param __t The timespace to build for.
	 * @return The manager for the given timespace.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	public SourceManager sourceManager(TimeSpaceType __t)
		throws IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		int i = __t.ordinal();
		SourceManager[] sourcemanagers = this._sourcemanagers;
		SourceManager rv = sourcemanagers[i];
		if (rv == null)
			sourcemanagers[i] =
				(rv = new SourceManagerFactory(this.sourceroot).get(__t));
		return rv;
	}
	
	/**
	 * Initializes a project manager from the given arguments.
	 *
	 * @param __args The arguments to use.
	 * @return The project manager which was initialized.
	 * @since 2018/07/29
	 */
	public static ProjectManager fromArguments(String... __args)
	{
		// Copy arguments for processing
		Queue<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// Forward it
		return ProjectManager.fromArguments(args);
	}
	
	/**
	 * Initializes the project manager from the given queue of arguments.
	 *
	 * @param __args The arguments to parse.
	 * @return The project manager parsed from the given arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/29
	 */
	public static ProjectManager fromArguments(Queue<String> __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Use default paths based on system properties
		Path sourceroot = Paths.get(
			System.getProperty("cc.squirreljme.builder.root",
				System.getProperty("user.dir", "squirreljme"))),
			binroot = Paths.get(
				System.getProperty(
					"cc.squirreljme.builder.output", "bins")),
			binruntime = null,
			binjit = null,
			bintest = null,
			binbuild = null;
		
		// Default timespace? Default to the build for compatibility purposes
		// since everything does that
		TimeSpaceType deftimespace = TimeSpaceType.BUILD;
		
		// Allow paths to be modified
		String[] parse;
		while (null != (parse = BuilderFactory.__getopts(
			":?RJTBs:o:j:t:b:", __args)))
			switch (parse[0])
			{
					// Change source code root
				case "s":
					sourceroot = Paths.get(parse[1]);
					break;
					
					// Change binary output base root
				case "o":
					binroot = Paths.get(parse[1]);
					break;
					
					// Run-time build path
				case "r":
					binruntime = Paths.get(parse[1]);
					break;
					
					// JIT-time build path
				case "j":
					binjit = Paths.get(parse[1]);
					break;
					
					// Test-time build path
				case "t":
					bintest = Paths.get(parse[1]);
					break;
					
					// Build-time build path
				case "b":
					binbuild = Paths.get(parse[1]);
					break;
					
					// Default RUNTIME
				case "R":
					deftimespace = TimeSpaceType.RUNTIME;
					break;
					
					// Default JIT
				case "J":
					deftimespace = TimeSpaceType.JIT;
					break;
					
					// Default Tests
				case "T":
					deftimespace = TimeSpaceType.TEST;
					break;
					
					// Default build time
				case "B":
					deftimespace = TimeSpaceType.BUILD;
					break;
				
					// {@squirreljme.error AU0l Unknown project argument.
					// Usage: [-s path] [-o path] [-r path] [-j path]
					// [-b path];
					// -s: The project source path;
					// -o: The base directory for binary output;
					// -r: The binary path for the run-time;
					// -j: The binary path for the jit-time;
					// -t: The binary path for the tests;
					// -b: The binary path for the build-time;
					// -R: Default timespace is run-time;
					// -J: Default timespace is JIT;
					// -T: Default timespace is tests;
					// -B: Default timespace is build-time}
				case "?":
				default:
					throw new IllegalArgumentException(
						String.format("AU0l %s", parse[0]));
			}
		
		// Fill with defaults if missing
		if (binruntime == null)
			binruntime = binroot.resolve("brun");
		if (binjit == null)
			binjit = binroot.resolve("bjit");
		if (bintest == null)
			bintest = binroot.resolve("btst");
		if (binbuild == null)
			binbuild = binroot.resolve("bbld");
		
		// Set paths
		return new ProjectManager(sourceroot,
			new Path[]{binruntime, binjit, bintest, binbuild}, deftimespace);
	}
}

