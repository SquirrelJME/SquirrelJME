// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

import java.io.InputStream;

/**
 * Virtual machine JAR control, provides access to resources and such.
 *
 * @since 2016/06/16
 */
public abstract class VMJar
{
	/**
	 * Returns the JARs which are in the classpath of the current process.
	 *
	 * @return The JARs (of the installed ones) which are part of the current
	 * class path.
	 * @since 2016/06/16
	 */
	public abstract String[] classPathJars();
	
	/**
	 * Obtains the given resource from the JAR file of a given class or the
	 * global namespace.
	 *
	 * @param __base If not {@code null} then the resource is only located in
	 * the JAR that contains the class, otherwise if {@code null} then the
	 * entire class path is searched for the given resource.
	 * @param __abs This should be the fully resolved absolute path of a
	 * resource which does not start with a forward slash.
	 * @return The name of the resource.
	 * @throws NullPointerException If no absolute path was specified.
	 * @since 2016/06/16
	 */
	public abstract String findResource(Class<?> __base,
		String __abs)
		throws NullPointerException;
	
	/**
	 * This goes through the entire class path and locations any resources
	 * that are in named JAR files that match the given absolute name.
	 *
	 * @param __abs This should be the fully resolved absolute path of a
	 * resource which does not start with a forward slash.
	 * @return The array of resource names which pertain to the found
	 * resources, if no resources are found then the array will be blank
	 * however it may return {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public abstract String[] findResources(String __abs)
		throws NullPointerException;
	
	/**
	 * Returns an array which contains all of the JARs which are currently
	 * installed on the current device.
	 *
	 * @return The array of installed JARs.
	 * @since 2016/06/16
	 */
	public abstract String[] installedJars();
	
	/**
	 * Opens a resource which was previously returned by
	 * {@link #findResource(Class, String)} or {@link #findResources(String)}.
	 *
	 * @param __res The name of the resource.
	 * @return The input stream of the given resource or {@code null} if it
	 * could not be opened for any reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public abstract InputStream openResource(String __res)
		throws NullPointerException;
}

