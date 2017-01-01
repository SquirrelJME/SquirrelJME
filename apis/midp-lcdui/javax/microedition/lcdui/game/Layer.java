// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

public abstract class Layer
{
	Layer()
	{
		super();
		throw new Error("TODO");
	}
	
	public abstract void paint(Graphics __a);
	
	public final int getHeight()
	{
		throw new Error("TODO");
	}
	
	public final int getWidth()
	{
		throw new Error("TODO");
	}
	
	public final int getX()
	{
		throw new Error("TODO");
	}
	
	public final int getY()
	{
		throw new Error("TODO");
	}
	
	public final boolean isVisible()
	{
		throw new Error("TODO");
	}
	
	public void move(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public void setPosition(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public void setVisible(boolean __a)
	{
		throw new Error("TODO");
	}
}


