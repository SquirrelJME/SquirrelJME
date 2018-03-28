// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.SerializedEvent;

public abstract class IdleItem
	extends CustomItem
{
	protected IdleItem(String __label)
	{
		super(__label);
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void addedToDisplay(Display __d)
	{
		throw new todo.TODO();
	}
	
	@SerializedEvent
	protected void removedFromDisplay(Display __d)
	{
		throw new todo.TODO();
	}
}

