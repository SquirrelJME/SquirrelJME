// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.execfinder;

/**
 * This interface describes a classic executable which uses {@code Main-Class}
 * and {@link Class-Path} and is the same as standard desktop applications
 * providing a console based output.
 *
 * @since 2016/05/18
 */
public interface ExecutableClassic
	extends Executable
{
	/**
	 * Returns the class which acts as the main entry point.
	 *
	 * @return The main class.
	 * @since 2016/05/18
	 */
	public abstract String mainClass();
	
	/**
	 * Returns the JARs which this class depends on.
	 *
	 * @param The iteratable set of JARs this program depends on.
	 * @since 2016/05/18
	 */
	public abstract Iterable<String> classPath();
}

