// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.lle;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.InputStream;

/**
 * This allows access to the library class path and resources.
 *
 * @since 2020/06/07
 */
public final class LLEJarPackageShelf
{
	/**
	 * Returns the classpath of the current program.
	 *
	 * @return The classpath of the current program.
	 * @since 2020/06/07
	 */
	public static JarPackageBracket[] classPath()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Checks if the given brackets refer to the same JAR.
	 * 
	 * @param __a The first JAR.
	 * @param __b The second JAR.
	 * @return If these are equal or not.
	 * @throws MLECallError If either argument is {@code null}.
	 * @since 2020/07/02
	 */
	public static boolean equals(
		JarPackageBracket __a, JarPackageBracket __b)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the libraries which are available to the virtual machine.
	 * 
	 * @return The libraries that are currently available.
	 * @since 2020/10/31
	 */
	public static JarPackageBracket[] libraries()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the path to the given JAR.
	 * 
	 * Note that this may or may not be a physical path, it could be a
	 * representation of the JAR file in string form.
	 * 
	 * @param __jar The JAR to get the path of.
	 * @return The path of the given JAR.
	 * @throws MLECallError If the JAR is not valid.
	 * @since 2020/10/31
	 */
	public static String libraryPath(JarPackageBracket __jar)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Opens the resource from the input stream.
	 *
	 * @param __jar The JAR to open.
	 * @param __rc The resource to load from the given JAR.
	 * @return Input stream to the resource, may be {@code null} if it does
	 * not exist.
	 * @throws MLECallError If the JAR is not valid or the resource was not
	 * specified.
	 * @since 2020/06/07
	 */
	public static InputStream openResource(JarPackageBracket __jar,
		String __rc)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
