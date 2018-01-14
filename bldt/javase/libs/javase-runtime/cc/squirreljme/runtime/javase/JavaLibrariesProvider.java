// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase;

import cc.squirreljme.kernel.impl.base.file.FileLibrariesProvider;
import cc.squirreljme.kernel.lib.server.LibrariesProvider;

/**
 * This is used to manage programs which are natively installed on the Java
 * SE virtualized system.
 *
 * @since 2018/01/05
 */
public class JavaLibrariesProvider
	extends FileLibrariesProvider
{
	/**
	 * Initializes the server.
	 *
	 * @since 2018/01/05
	 */
	public JavaLibrariesProvider()
	{
	}
}

