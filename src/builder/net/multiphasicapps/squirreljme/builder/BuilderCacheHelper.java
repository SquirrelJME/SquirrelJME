// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.nio.file.Path;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This interface allows the {@link BuilderCache} to operate without
 * requiring that it be duplicated or rewritten.
 *
 * @since 2016/07/22
 */
public interface BuilderCacheHelper
{
	/**
	 * Returns the package list.
	 *
	 * @return The package list.
	 * @since 2016/07/22
	 */
	public abstract PackageList packageList();
	
	/**
	 * Returns the path to the temporary output directory.
	 *
	 * @return The temporary directory path.
	 * @since 2016/07/22
	 */
	public abstract Path temporaryDirectory();
}

