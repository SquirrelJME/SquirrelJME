// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.executable.ExecutableLoadException;
import net.multiphasicapps.squirreljme.executable.ExecutableMissingException;

/**
 * This is an accessor for accessing a suite that exists within the kernel or
 * another class location. This is used to cache a pre-existing suite so that
 * the kernel, launcher, and user-space processes have a uniform and simple to
 * use interface for suites that may exist.
 *
 * @since 2016/12/16
 */
public abstract class SuiteDataAccessor
{
	/**
	 * Loads a class from this suite and returns a reference to that class.
	 *
	 * @param __name The binary name of the class to load.
	 * @return The loaded class.
	 * @throws ExecutableLoadException If the class is not valid.
	 * @throws ExecutableMissingException If the class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/16
	 */
	public abstract ExecutableClass loadClass(String __name)
		throws ExecutableLoadException, ExecutableMissingException,
			NullPointerException;
	
	/**
	 * Loads the given resource from this suite.
	 *
	 * @param __name The resource to load, if the resource name is the name of
	 * a class file then {@code null} may be returned depending on the
	 * implementation.
	 * @return The input stream for the given resource or {@code null} if it
	 * is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	public abstract InputStream loadResource(String __name)
		throws NullPointerException;
}

