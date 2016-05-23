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
public abstract class InternalMenu
	extends InternalElement<UIMenu>
{
	/**
	 * Initializes the internal menu representation.
	 *
	 * @param __ref The reference to the external menu.
	 * @since 2016/05/23
	 */
	public InternalMenu(Reference<UIMenu> __ref)
	{
		super(__ref);
	}
}

