// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;
import net.multiphasicapps.squirreljme.lcdui.widget.DisplayableWidget;

/**
 * This represents widgtes that swing uses to display embedded objects.
 *
 * @since 2017/10/25
 */
public class SwingDisplayableWidget
	extends DisplayableWidget
{
	/**
	 * Initializes the widget.
	 *
	 * @param __rd The reference to the displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public SwingDisplayableWidget(Reference<Displayable> __rd)
		throws NullPointerException
	{
		super(__rd);
	}
}

