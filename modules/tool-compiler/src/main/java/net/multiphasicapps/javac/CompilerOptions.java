// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

/**
 * This class is used to specify the options which are used for compilation
 * such as those which would be passed via the command line.
 *
 * This class is immutable.
 *
 * @since 2017/11/28
 */
public final class CompilerOptions
{
	/** Enable debugging output? */
	protected final boolean debug;
	
	/** The version of the Java language to target. */
	protected final CompilerJavaVersion version;
	
	/**
	 * Initializes the compiler with the default set of options.
	 *
	 * @since 2017/11/29
	 */
	public CompilerOptions()
	{
		this.debug = true;
		this.version = CompilerJavaVersion.JAVA_7;
	}
	
	/**
	 * Generate extra debugging information?
	 *
	 * @return If extra debugging information is to be provided.
	 * @since 2017/11/29
	 */
	public final boolean debug()
	{
		return this.debug;
	}
	
	/**
	 * Returns the Java language version to target.
	 *
	 * @return The target language.
	 * @since 2017/11/29
	 */
	public final CompilerJavaVersion version()
	{
		return this.version;
	}
}

