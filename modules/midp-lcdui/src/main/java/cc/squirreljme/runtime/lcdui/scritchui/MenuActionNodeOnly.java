// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * A menu action which only has a single node.
 *
 * @since 2024/07/20
 */
public abstract class MenuActionNodeOnly
	implements MenuActionApplicable
{
	/** The node of this menu. */
	@SquirrelJMEVendorApi
	final MenuActionNode _menuNode =
		new MenuActionNode(this);
}
