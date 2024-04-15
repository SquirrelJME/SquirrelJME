// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Graphics;

@Api
public abstract class Layer
{
	Layer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract void paint(Graphics __a);
	
	@Api
	public final int getHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getWidth()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getX()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getY()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean isVisible()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void move(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPosition(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setVisible(boolean __a)
	{
		throw Debugging.todo();
	}
}


