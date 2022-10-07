// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;

public abstract class IdleItem
	extends CustomItem
{
	protected IdleItem(String __label)
	{
		super(__label);
		throw Debugging.todo();
	}
	
	@SerializedEvent
	protected void addedToDisplay(Display __d)
	{
		throw Debugging.todo();
	}
	
	@SerializedEvent
	protected void removedFromDisplay(Display __d)
	{
		throw Debugging.todo();
	}
}

