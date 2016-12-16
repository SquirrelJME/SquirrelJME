// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;

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
	 * @throws ClassFormatError If the class is badly formatted.
	 * @throws ClassNotFoundException If the class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/16
	 */
	public abstract KernelLoadedClass loadClass(String __name)
		throws ClassFormatError, ClassNotFoundException, NullPointerException;
	
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
		throws InvalidSuiteException, NullPointerException;
}

