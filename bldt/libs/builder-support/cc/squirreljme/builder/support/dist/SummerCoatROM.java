// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.dist;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.builder.support.vm.BuildClassLibrary;
import cc.squirreljme.runtime.swm.EntryPoints;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.packfile.PackMinimizer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * This is used to build the packed SummerCoat ROM file.
 *
 * @since 2019/05/28
 */
public class SummerCoatROM
	extends DistBuilder
{
	/** The timespace to use. */
	protected final TimeSpaceType timespace;
	
	/**
	 * Initializes the service.
	 *
	 * @since 2019/05/28
	 */
	public SummerCoatROM()
	{
		this("summercoatrom", TimeSpaceType.RUNTIME);
	}
	
	/**
	 * Initializes the service with the given name and type.
	 *
	 * @param __name The name to use.
	 * @param __ts The timespace type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public SummerCoatROM(String __name, TimeSpaceType __ts)
		throws NullPointerException
	{
		super(__name);
		
		if (__ts == null)
			throw new NullPointerException("NARG");
		
		this.timespace = __ts;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/28
	 */
	@Override
	protected void specific(ProjectManager __pm, ZipCompilerOutput __zip)
		throws IOException, NullPointerException
	{
		// Compile everything and include the launcher bits as well
		Binary[] bins = __pm.buildAll(this.timespace);
		Binary[] lbins = __pm.build(this.timespace, "launcher");
		
		// Bootstrap library used as the kernel entry point
		String boot = null;
		
		// Translate these to VMClassLibraries and find the bootstrap
		int n = bins.length;
		VMClassLibrary[] libs = new VMClassLibrary[n];
		for (int i = 0; i < n; i++)
		{
			Binary b = bins[i];
			
			// Wrap library
			BuildClassLibrary lib;
			libs[i] = (lib = new BuildClassLibrary(b));
			
			// Use the supervisor as the booting point
			String name = lib.name().toString();
			if (name.equals("supervisor") || name.equals("supervisor.jar"))
				boot = name;
		}
		
		// Try to find the starting libraries for the launcher
		int numlbins = lbins.length;
		String[] lstrs = new String[numlbins];
		for (int i = 0; i < numlbins; i++)
		{
			// Get source name
			String name = lbins[i].name().toString();
			
			// Find the library for it
			for (int j = 0; j < n; j++)
			{
				String ln = libs[j].name();
				
				// Matching name?
				if (name.equals(ln) || name.equals(ln + ".jar") ||
					(name + ".jar").equals(ln) ||
					(name + ".jar").equals(ln + ".jar"))
				{
					lstrs[i] = ln;
					break;
				}
			}
		}
		
		// Get the main class of the launcher
		String mainbc = new EntryPoints(lbins[numlbins - 1].manifest()).get(0).
			entryPoint();
		
		// Write SummerCoat ROM file
		this.generate(__zip, new BuildParameters(boot, lstrs, mainbc, libs));
	}
	
	/**
	 * Generates the output files as needed.
	 *
	 * @param __zip The ZIP to write to.
	 * @param __bp The build parameters.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	protected void generate(ZipCompilerOutput __zip, BuildParameters __bp)
		throws IOException, NullPointerException
	{
		if (__zip == null || __bp == null)
			throw new NullPointerException("NARG");
		
		// Write SummerCoat ROM file
		try (OutputStream out = __zip.output("squirreljme.sqc"))
		{
			__bp.minimize(out);
		}
	}
	
	/**
	 * This contains the build parameters for the minimizer.
	 *
	 * @since 2019/07/13
	 */
	public static final class BuildParameters
	{
		/** The boot library. */
		public final String bootlib;
		
		/** Starting libraries. */
		public final String[] startlibs;
		
		/** Main boot class. */
		public final String mainbc;
		
		/** Libraries to use. */
		public final VMClassLibrary[] libs;
		
		/**
		 * Initializes the build parameters.
		 *
		 * @param __boot The boot library.
		 * @param __lstrs Starting libraries.
		 * @param __mainbc Main boot class.
		 * @param __libs Class Library.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/07/13
		 */
		public BuildParameters(String __boot, String[] __lstrs,
			String __mainbc, VMClassLibrary[] __libs)
			throws NullPointerException
		{
			if (__boot == null || __lstrs == null || __mainbc == null ||
				__libs == null)
				throw new NullPointerException("NARG");
			
			this.bootlib = __boot;
			this.startlibs = __lstrs;
			this.mainbc = __mainbc;
			this.libs = __libs;
		}
		
		/**
		 * Minimizes to the given output stream.
		 *
		 * @param __out The stream to write to.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/07/13
		 */
		public final void minimize(OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__out == null)
				throw new NullPointerException("NARG");
			
			PackMinimizer.minimize(__out, this.bootlib, this.startlibs,
				this.mainbc, this.libs);
		}
		
		/**
		 * Minimizes to a byte array.
		 *
		 * @return The byte array of the minimized output.
		 * @
		 * @since 2019/07/13
		 */
		public final byte[] minimize()
			throws IOException
		{
			return PackMinimizer.minimize(this.bootlib, this.startlibs,
				this.mainbc, this.libs);
		}
	}
}

