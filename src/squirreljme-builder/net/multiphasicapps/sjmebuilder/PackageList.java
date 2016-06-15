// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * This contains a mapping of every package which is available to SquirrelJME.
 *
 * @since 2016/06/15
 */
public class PackageList
	extends AbstractMap<PackageName, PackageInfo>
{
	/**
	 * This initializes the package list.
	 *
	 * @param __j The directory containing pre-built JAR files.
	 * @param __s The directory containing source packages.
	 * @throws IOException If there is an error reading the package list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	public PackageList(Path __j, Path __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__j == null || __s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/15
	 */
	@Override
	public Set<Map.Entry<PackageName, PackageInfo>> entrySet()
	{
		throw new Error("TODO");
	}
}

