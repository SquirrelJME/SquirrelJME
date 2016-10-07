// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;


public abstract class Canvas
	extends Displayable
{
	public static final int DOWN =
		6;
	
	public static final int FIRE =
		8;
	
	public static final int GAME_A =
		9;
	
	public static final int GAME_B =
		10;
	
	public static final int GAME_C =
		11;
	
	public static final int GAME_D =
		12;
	
	public static final int KEY_NUM0 =
		48;
	
	public static final int KEY_NUM1 =
		49;
	
	public static final int KEY_NUM2 =
		50;
	
	public static final int KEY_NUM3 =
		51;
	
	public static final int KEY_NUM4 =
		52;
	
	public static final int KEY_NUM5 =
		53;
	
	public static final int KEY_NUM6 =
		54;
	
	public static final int KEY_NUM7 =
		55;
	
	public static final int KEY_NUM8 =
		56;
	
	public static final int KEY_NUM9 =
		57;
	
	public static final int KEY_POUND =
		35;
	
	public static final int KEY_STAR =
		42;
	
	public static final int LEFT =
		2;
	
	public static final int RIGHT =
		5;
	
	public static final int UP =
		1;
	
	protected Canvas()
	{
		super();
		throw new Error("TODO");
	}
	
	protected abstract void paint(Graphics __a);
	
	public int getGameAction(int __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public int getHeight()
	{
		throw new Error("TODO");
	}
	
	public int getKeyCode(int __a)
	{
		throw new Error("TODO");
	}
	
	public String getKeyName(int __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public int getWidth()
	{
		throw new Error("TODO");
	}
	
	public boolean hasPointerEvents()
	{
		throw new Error("TODO");
	}
	
	public boolean hasPointerMotionEvents()
	{
		throw new Error("TODO");
	}
	
	public boolean hasRepeatEvents()
	{
		throw new Error("TODO");
	}
	
	protected void hideNotify()
	{
		throw new Error("TODO");
	}
	
	public boolean isDoubleBuffered()
	{
		throw new Error("TODO");
	}
	
	protected void keyPressed(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void keyReleased(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void keyRepeated(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void pointerDragged(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	protected void pointerPressed(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	protected void pointerReleased(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public final void repaint()
	{
		throw new Error("TODO");
	}
	
	public final void repaint(int __a, int __b, int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	public final void serviceRepaints()
	{
		throw new Error("TODO");
	}
	
	public void setFullScreenMode(boolean __a)
	{
		throw new Error("TODO");
	}
	
	protected void showNotify()
	{
		throw new Error("TODO");
	}
	
	@Override
	protected void sizeChanged(int __a, int __b)
	{
		throw new Error("TODO");
	}
}


