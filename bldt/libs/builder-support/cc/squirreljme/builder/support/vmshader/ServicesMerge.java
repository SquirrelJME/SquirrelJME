// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.vmshader;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is used for merging services together as one.
 *
 * @since 2019/05/19
 */
public final class ServicesMerge
{
	/** Services available. */
	final Set<String> _implementations =
		new LinkedHashSet<>();
	
	/**
	 * Returns the service implementations.
	 *
	 * @return The service implementations.
	 * @since 2019/05/19
	 */
	public final String[] implementations()
	{
		Set<String> impls = this._implementations;
		return impls.<String>toArray(new String[impls.size()]);
	}
}

