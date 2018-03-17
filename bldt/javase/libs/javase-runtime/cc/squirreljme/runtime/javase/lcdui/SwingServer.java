// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.server.LcdServer;

/**
 * This provides the display server which is based on the Swing interface.
 *
 * @since 2018/03/16
 */
public class SwingServer
	extends LcdServer
{
	/**
	 * Initializes the server.
	 *
	 * @param __task The task which owns this server.
	 * @param __def The owning definition.
	 * @since 2018/03/16
	 */
	public SwingServer(SystemTask __task, SwingDefinition __def)
	{
		super(__task, __def);
	}
}

