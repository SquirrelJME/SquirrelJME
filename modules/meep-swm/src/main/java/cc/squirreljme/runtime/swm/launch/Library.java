// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm.launch;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.swm.SuiteInfo;

/**
 * This represents a library that can be used as a dependency, this includes
 * APIs and normal libraries.
 *
 * @since 2020/12/28
 */
public final class Library
{
	/** The library information. */
	protected final SuiteInfo info;
	
	/** The JAR which contains the library. */
	protected final JarPackageBracket jar;
	
	/**
	 * Initializes the library.
	 * 
	 * @param __info The JAR information.
	 * @param __jar The JAR itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	Library(SuiteInfo __info, JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__info == null || __jar == null)
			throw new NullPointerException("NARG");
		
		this.info = __info;
		this.jar = __jar;
	}
}
