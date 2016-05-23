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
 * This is the base for classes which act as components within a display.
 *
 * @since 2016/05/22
 */
public abstract class InternalComponent
	extends InternalElement<UIComponent>
{
	/**
	 * Initializes the internal component.
	 *
	 * @param __r The reference to the external component.
	 * @since 2016/05/22
	 */
	public InternalComponent(Reference<UIComponent> __r)
	{
		super(__r);
	}
}

