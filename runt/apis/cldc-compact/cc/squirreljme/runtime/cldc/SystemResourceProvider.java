// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import java.io.InputStream;

/**
 * This interface is used to provide access to resources for libraries and
 * tasks.
 *
 * @since 2018/02/11
 */
public interface SystemResourceProvider
{
	/**
	 * Loads the specified resource in the given scope.
	 *
	 * @param __scope The scope to load the resource from.
	 * @param __name The name of the resource to load.
	 * @return The resource with the given name under the given scope or
	 * {@code null} if it does not exist.
	 * @since 2018/02/11
	 */
	public abstract InputStream loadResource(SystemResourceScope __scope,
		String __name)
		throws NullPointerException;
}

