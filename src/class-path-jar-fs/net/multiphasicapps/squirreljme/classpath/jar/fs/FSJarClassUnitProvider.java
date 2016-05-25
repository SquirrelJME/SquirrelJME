// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar.fs;

import java.nio.file.Path;
import net.multiphasicapps.squirreljme.classpath.jar.JarClassUnitProvider;

/**
 * This provides access to JARs which exist on the file system.
 *
 * @since 2016/05/25
 */
public class FSJarClassUnitProvider
	extends JarClassUnitProvider
{
	/** The path to use when searching for JARs. */
	protected final Path path;
	
	/**
	 * Initializes the file system class unit provider which searches in the
	 * specified path.
	 *
	 * @param __p The path to locate class units for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public FSJarClassUnitProvider(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
	}
}

