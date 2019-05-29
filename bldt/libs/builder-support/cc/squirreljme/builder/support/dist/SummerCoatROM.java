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
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.packfile.PackMinimizer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * This is used to build the packed SummerCoat ROM file.
 *
 * @since 2019/05/28
 */
public class SummerCoatROM
	extends DistBuilder
{
	/**
	 * Initializes the service.
	 *
	 * @since 2019/05/28
	 */
	public SummerCoatROM()
	{
		super("summercoatrom");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/28
	 */
	@Override
	protected void specific(ProjectManager __pm, ZipCompilerOutput __zip)
		throws IOException, NullPointerException
	{
		// All the libraries need to be compiled!!
		Binary[] bins = __pm.buildAll(TimeSpaceType.RUNTIME);
		
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
		
		// Write SummerCoat ROM file
		try (OutputStream out = __zip.output("squirreljme.sqc"))
		{
			// Minimize
			PackMinimizer.minimize(out, boot, libs);
		}
	}
}

