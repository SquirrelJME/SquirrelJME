// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.IOException;
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This acts the base for an instance of a build. This is created by
 * {@link TargetBuilder} if it supports the given configuration.
 * Implementations of this class shall contain all of the logic needed to
 * target a given system.
 *
 * @since 2016/09/02
 */
public abstract class BuildInstance
{
	/** The build configuration. */
	protected final BuildConfig config;
	
	/** The temporary directory for build work. */
	private volatile Path _tempdir;
	
	/**
	 * Initializes the build instance.
	 *
	 * @param __conf The configuration to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	protected BuildInstance(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Builds the output distribution.
	 *
	 * @param __zsw The output ZIP file to write to.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final void buildDistribution(ZipStreamWriter __zsw)
		throws IOException, NullPointerException
	{
		// Check
		if (__zsw == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Compiles the target binary.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/09/02
	 */
	public final void compileBinary()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Initializes an emulator using the specified set of arguments.
	 *
	 * @param __tae The arguments to use.
	 * @return The emulator for the target system.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final Emulator emulator(TargetEmulatorArguments __tae)
		throws IOException, NullPointerException
	{
		// Check
		if (__tae == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Sets the temporary output directory.
	 *
	 * @param __d The directory where temporary files go.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	final void __setTempDir(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._tempdir = __p;
	}
}

