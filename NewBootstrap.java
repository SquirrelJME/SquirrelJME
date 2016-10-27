// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * New bootstrap build system.
 *
 * @since 2016/10/26
 */
public abstract class NewBootstrap
	implements Runnable
{
	/** The binary path. */
	protected final Path binarypath;
	
	/** The source path. */
	protected final Path sourcepath;
	
	/** The input launch arguments. */
	protected final String[] launchargs;
	
	/**
	 * Initializes the bootstrap base.
	 *
	 * @param __bin The binary output directory.
	 * @param __src The source input namespace directories.
	 * @param __args Arguments to the bootstrap.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/26
	 */
	public NewBootstrap(Path __bin, Path __src, String[] __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.binarypath = __bin;
		this.sourcepath = __src;
		this.launchargs = __args.clone();
		
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point for the new bootstrap system.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On any read/write errors.
	 * @since 2016/10/26
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Get directories for input and output
		Path bin = Paths.get(System.getProperty(
			"net.multiphasicapps.squirreljme.bootstrap.binary")),
			src = Paths.get(System.getProperty(
			"net.multiphasicapps.squirreljme.bootstrap.source"));
		
		// Only build?
		NewBootstrap nb;
		if (Boolean.getBoolean(
			"net.multiphasicapps.squirreljme.build.onlybuild"))
			nb = new DoBuild(bin, src, __args);
		
		// Launch
		else
			nb = new DoLaunch(bin, src, __args);
		
		// Run it
		nb.run();
	}
	
	/**
	 * This performs the build of the bootstrap system.
	 *
	 * @since 2016/10/26
	 */
	public static class DoBuild
		extends NewBootstrap
	{
		/**
		 * Initializes the build.
		 *
		 * @param __bin The binary output directory.
		 * @param __src The source input namespace directories.
		 * @param __args Arguments to the bootstrap.
		 * @throws IOException On read/write errors.
		 * @since 2016/10/26
		 */
		public DoBuild(Path __bin, Path __src, String[] __args)
			throws IOException
		{
			super(__bin, __src, __args);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/26
		 */
		@Override
		public void run()
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This launches the bootstrap system so that SquirrelJME may be
	 * built accordingly.
	 *
	 * @since 2016/10/26
	 */
	public static class DoLaunch
		extends NewBootstrap
	{
		/**
		 * Initializes the launcher.
		 *
		 * @param __bin The binary output directory.
		 * @param __src The source input namespace directories.
		 * @param __args Arguments to the bootstrap.
		 * @throws IOException On read/write errors.
		 * @since 2016/10/26
		 */
		public DoLaunch(Path __bin, Path __src, String[] __args)
			throws IOException
		{
			super(__bin, __src, __args);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/26
		 */
		@Override
		public void run()
		{
			throw new Error("TODO");
		}
	}
}

