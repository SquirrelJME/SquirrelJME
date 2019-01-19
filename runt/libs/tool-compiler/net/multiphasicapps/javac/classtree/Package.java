// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.classtree;

import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a package which contains a bunch of classes that exist
 * within the package.
 *
 * @since 2019/01/17
 */
public final class Package
{
	/** Units which are in this package. */
	private final Map<String, Unit> _units;
	
	/**
	 * Initializes the package with all of the contained units.
	 *
	 * @param __m The package and maps.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/18
	 */
	public Package(Map<String, Unit> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

