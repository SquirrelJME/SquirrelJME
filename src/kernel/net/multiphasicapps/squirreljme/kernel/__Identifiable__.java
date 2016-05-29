// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This is associated with processes, threads, and anything else which requires
 * an ID be associated with so that it is identified by a unique value.
 *
 * @since 2016/05/29
 */
interface __Identifiable__
{
	/**
	 * Returns the ID of the current object.
	 *
	 * @return The ID of this object.
	 * @since 2016/05/29
	 */
	public abstract int id();
}

