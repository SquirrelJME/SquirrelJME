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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.basicassets.BasicAsset;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.EmulatorConfig;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigBuilder;
import net.multiphasicapps.squirreljme.jit.JITNamespaceProcessor;
import net.multiphasicapps.squirreljme.jit.JITNamespaceProcessorProgress;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;
import net.multiphasicapps.squirreljme.projects.ProjectList;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

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
	
	/** The target triplet. */
	protected final JITTriplet triplet;
	
	/** The package browser to lookup namespace contents. */
	protected final PackageBrowser browser;
	
	/** The temporary directory for build work. */
	private volatile Path _tempdir;
	
	/** Packages which have been compiled. */
	private volatile Map<String, ProjectInfo> _compiled =
		new LinkedHashMap<>();
	
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
		this.triplet = __conf.triplet();
		this.browser = new PackageBrowser(this, __conf.packageList());
	}
	
	/**
	 * Configures the emulator.
	 *
	 * @param __conf The configuration to use.
	 * @throws IOException If there was a read/write error when setting up
	 * the emulator.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	protected abstract void configureEmulator(EmulatorConfig __conf)
		throws IOException, NullPointerException;
	
	/**
	 * Modifies the given output configuration that is used to setup the
	 * target JIT.
	 *
	 * @param __conf The configuration that is for building.
	 * @since 2016/09/02
	 */
	protected abstract void modifyOutputConfig(JITConfigBuilder __conf);
	
	/**
	 * This returns the package group that is used to determine which packages
	 * of all of them to unconditionally include in the target JVM. These
	 * package groups can be used include said packages for example if they
	 * provide extra services that may be optionally used.
	 *
	 * @return An array of package groups
	 */
	protected abstract String[] packageGroup();
	
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
		
		// Include the resultant binary
		if (true)
			throw new Error("TODO");
		
		// Write the triplet to the ZIP
		try (OutputStream os = __zsw.nextEntry("target",
			ZipCompressionType.DEFAULT_COMPRESSION);
			PrintStream ps = new PrintStream(os, true, "utf-8"))
		{
			ps.println(this.config.triplet().toString());
		}
		
		// Include basic assets
		byte[] buf = new byte[64];
		for (BasicAsset b : BasicAsset.getAssets())
		{
			String an = b.name();
		
			// Create entry
			try (InputStream is = b.open();
				OutputStream os = __zsw.nextEntry(an,
					ZipCompressionType.DEFAULT_COMPRESSION))
			{
				for (;;)
				{
					int rc = is.read(buf);
					
					// EOF?
					if (rc < 0)
						break;
					
					// Write
					os.write(buf, 0, rc);
				}
			}
		}
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
		// Select packages
		BuildConfig config = this.config;
		__PackageSelection__ ps = new __PackageSelection__(config, this);
		
		// Setup immutable config
		JITConfig jconf = __makeJITConfig();
		
		// Setup namespace processor
		JITNamespaceProcessor jnp = new JITNamespaceProcessor(jconf,
			this.browser, new __JITProgress__(System.err));
		
		// Process all packages to be built
		Map<String, ProjectInfo> compiled = this._compiled;
		for (ProjectInfo pi : ps._all)
		{
			// Add namespace to compilation set
			String na = pi.name() + ".jar";
			compiled.put(na, pi);
			
			// Process
			jnp.processNamespace(na);
		}
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
	public final Emulator emulator(EmulatorArguments __tae)
		throws IOException, NullPointerException
	{
		// Check
		if (__tae == null)
			throw new NullPointerException("NARG");
		
		// Setup configuration to modify
		EmulatorConfig econf = new EmulatorConfig();
		
		// Send to the build instance modifiers
		configureEmulator(econf);
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the temporary directory that was used.
	 *
	 * @return The temporary directory.
	 * @since 2016/09/02
	 */
	protected final Path temporaryDirectory()
	{
		return this._tempdir;
	}
	
	/**
	 * Makes the JIT output configuration which is used when compiling Java
	 * byte code into native code.
	 *
	 * @return The final immutable configuration.
	 * @since 2016/09/02
	 */
	private final JITConfig __makeJITConfig()
	{
		// Setup builder
		JITConfigBuilder b = new JITConfigBuilder();
		
		// Setup basic triplet
		b.setTriplet(this.triplet);
		
		// Send to self to add any properties as needed
		modifyOutputConfig(b);
		
		// No more changes after this
		return b.build();
	}
	
	/**
	 * Returns the package group to use for package inclusion.
	 *
	 * @return The package group to use.
	 * @since 2016/09/02
	 */
	final String[] __packageGroup()
	{
		String[] rv = packageGroup();
		if (rv == null)
			return new String[0];
		return rv;
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

