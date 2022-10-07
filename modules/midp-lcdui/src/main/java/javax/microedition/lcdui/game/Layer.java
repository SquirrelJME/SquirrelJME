// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Graphics;

public abstract class Layer
{
	Layer()
	{
		throw Debugging.todo();
	}
	
	public abstract void paint(Graphics __a);
	
	public final int getHeight()
	{
		throw Debugging.todo();
	}
	
	public final int getWidth()
	{
		throw Debugging.todo();
	}
	
	public final int getX()
	{
		throw Debugging.todo();
	}
	
	public final int getY()
	{
		throw Debugging.todo();
	}
	
	public final boolean isVisible()
	{
		throw Debugging.todo();
	}
	
	public void move(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public void setPosition(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public void setVisible(boolean __a)
	{
		throw Debugging.todo();
	}
}


