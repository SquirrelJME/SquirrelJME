// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.base.launcher;

import java.io.InputStream;

/**
 * This is used to access resources such as classes and acts as the class
 * path for the current program.
 *
 * Class files are read as if they were normal resources in class file form.
 *
 * @since 2016/09/20
 */
public interface ResourceAccessor
{
	/**
	 * Opens the specified resource.
	 *
	 * @param __n The name of the resource to open, matches the format used
	 * in ZIP files.
	 * @return An input stream for the given resource or {@code null} if it
	 * does not exist.
	 * @since 2016/09/20
	 */
	public abstract InputStream open(String __n);
}

