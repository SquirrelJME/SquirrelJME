// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ServiceLoader;
import net.multiphasicapps.javac.base.Compiler;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.projects.ProjectList;

/**
 * This is the main entry class for the bootstrap builder.
 *
 * @since 2016/09/18
 */
public class Main
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.bootstrap.binary=(path)
	 * This is the directory which contains binary projects.}
	 */
	public static final String BINARY_PATH_PROPERTY =
		"net.multiphasicapps.squirreljme.bootstrap.binary";
	
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.bootstrap.source=(path)
	 * This is the directory which contains source projects.}
	 */
	public static final String BINARY_SOURCE_PROPERTY =
		"net.multiphasicapps.squirreljme.bootstrap.source";
	
	/**
	 * This is the main entry point for the bootstrap builder.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(String... __args)
	{
		// Forward call and just use defaults, if applicable
		main(__defaultCompiler(), __defaultBootLauncher(), __args);
	}
	
	/**
	 * Main entry point but one where the boot compiler and launcher can be
	 * specified.
	 *
	 * @param __bc The boot compiler.
	 * @param __bl The boot launcher.
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(Compiler __bc, BootLauncher __bl,
		String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// 
		String pwd = System.getProperty("user.dir");
		Path bin = Paths.get(System.getProperty(BINARY_PATH_PROPERTY, pwd));
		Path src = Paths.get(System.getProperty(BINARY_SOURCE_PROPERTY, pwd));
		
		// Setup project list
		ProjectList pl;
		try
		{
			pl = new ProjectList(bin, src);
		}
		
		// {@squirreljme.error CL03 Could not load the package list.}
		catch (IOException e)
		{
			throw new RuntimeException("CL03", e);
		}
		
		// Setup launcher
		Bootstrapper bs = new Bootstrapper(pl, __bc, __bl);
		
		// Run commands
		bs.run(__args);
	}
	
	/**
	 * Selects a default boot compiler to use.
	 *
	 * @return A default boot compiler or {@code null} if none was found.
	 * @since 2016/09/18
	 */
	private static Compiler __defaultCompiler()
	{
		return null;
	}
	
	/**
	 * Selects a default boot launcher to use.
	 *
	 * @return A default boot launcher or {@code null} if none was found.
	 * @since 2016/09/18
	 */
	private static BootLauncher __defaultBootLauncher()
	{
		return null;
	}
}

