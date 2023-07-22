// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import org.jetbrains.annotations.Async;

@Api
public abstract class IdleItem
	extends CustomItem
{
	@Api
	protected IdleItem(String __label)
	{
		super(__label);
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void addedToDisplay(Display __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SerializedEvent
	@Async.Execute
	protected void removedFromDisplay(Display __d)
	{
		throw Debugging.todo();
	}
}

