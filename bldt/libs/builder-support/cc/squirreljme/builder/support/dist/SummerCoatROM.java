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
		try (OutputStream out = __zip.output("squirreljme.sqc"))
		{
			// Minimize
			PackMinimizer.minimize(out, boot, lstrs, mainbc, libs);
		}
	}
}

