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
 * This is the internal implementation of the display manager.
 *
 * Internal classes are not meant to be used by the end user, but only by
 * the implementation of a display manager.
 *
 * @see UIDisplayManager
 * @since 2016/05/21
 */
public abstract class InternalDisplayManager
	extends InternalElement<UIDisplayManager>
{
	/**
	 * Initializes the internal display manager.
	 *
	 * @param __ref The external element.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public InternalDisplayManager(Reference<UIDisplayManager> __ref)
		throws NullPointerException
	{
		super(__ref);
	}
}

