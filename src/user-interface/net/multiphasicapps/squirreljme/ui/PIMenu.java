// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

import java.lang.ref.Reference;

/**
 * This represents an internal menu which is used to contain items and such.
 *
 * @since 2016/05/23
 */
public interface PIMenu
	extends PIMenuItem
{
	/**
	 * This is called when the items within a menu have changed and the menu
	 * has to be updated.
	 *
	 * @throws UIException If the update could not be performed.
	 * @since 2016/05/23
	 */
	public abstract void updatedItems()
		throws UIException;
}

