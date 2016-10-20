// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.IOException;
import net.multiphasicapps.javac.base.Compiler;

/**
 * This contains the directory of source projects which may be compiled into
 * binary projects.
 *
 * @since 2016/10/20
 */
public final class SourceDirectory
{
	/**
	 * This is a fallback compiler which may be specified when it is not known.
	 * This sets an explicit compiler to use.
	 */
	static volatile Compiler _SPECIFIED_FALLBACK_COMPILER;
	
	/**
	 * Initializes the source directory.
	 *
	 * @param __d The owning project directory.
	 * @param __s The directory contianing namespaces which contain project
	 * sources.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	SourceDirectory(ProjectDirectory __d, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __p == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Sets the fallback compiler to use if no default could be used.
	 *
	 * @param __cc The compiler to use as a fallback.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/29
	 */
	public static void setFallbackCompiler(Compiler __cc)
		throws NullPointerException
	{
		// Check
		if (__cc == null)
			throw new NullPointerException("NARG");
		
		// Set
		SourceDirectory._SPECIFIED_FALLBACK_COMPILER = __cc;
	}
}

