// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.swing;

import java.lang.ref.Reference;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.multiphasicapps.squirreljme.ui.InternalMenu;
import net.multiphasicapps.squirreljme.ui.InternalMenuItem;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIGarbageCollectedException;
import net.multiphasicapps.squirreljme.ui.UIMenu;
import net.multiphasicapps.squirreljme.ui.UIMenuItem;

/**
 * This implements a menu item using Swing.
 *
 * @since 2016/05/23
 */
public class SwingMenuItem
	extends InternalMenuItem
{
	/**
	 * Initializes the swing based menu item.
	 *
	 * @param __ref The external reference.
	 * @since 2016/05/23d
	 */
	public SwingMenuItem(Reference<UIMenuItem> __ref)
	{
		super(__ref);
	}
}

