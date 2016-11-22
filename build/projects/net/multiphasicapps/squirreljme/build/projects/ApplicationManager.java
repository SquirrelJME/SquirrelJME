// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to manage applications which are either MIDlets or
 * LIBlets.
 *
 * @since 2016/11/20
 */
public class ApplicationManager
{
	/** The owning project manager. */
	protected final ProjectManager projectman;
	
	/**
	 * Initializes the application manager.
	 *
	 * @param __pm The owning project manager.
	 * @param __libs Source Liblets available to the application manager. 
	 * @param __mids Source MIDlets available to the application manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/20
	 */
	ApplicationManager(ProjectManager __pm, Set<Path> __libs, Set<Path> __mids)
		throws NullPointerException
	{
		// Check
		if (__pm == null || __libs == null || __mids == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projectman = __pm;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of suite hashes currently available.
	 *
	 * @return An array of suite hashes.
	 * @since 2016/11/20
	 */
	public final int[] suiteHashes()
	{
		throw new Error("TODO");
	}
}

