// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.WidgetType;

/**
 * This is the base class for a widget which may have operations performed
 * on it through widget handles.
 *
 * @since 2018/03/23
 */
public abstract class LcdWidget
{
	/** The type of widget this is. */
	protected final WidgetType type;
	
	/**
	 * Initializes the base widget.
	 *
	 * @param __type The widget type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public LcdWidget(WidgetType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
}

