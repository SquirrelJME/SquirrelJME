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
 * This is a reference to a resource which is used to access resources
 * potentially at a later time.
 *
 * @since 2016/09/21
 */
public interface ResourceReference
{
	/**
	 * Opens the specified resource.
	 *
	 * @return An input stream of the resource data.
	 * @since 2016/09/21
	 */
	public abstract InputStream open();
}

