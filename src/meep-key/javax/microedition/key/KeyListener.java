// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.key;

public interface KeyListener
{
	public static final int DOWN =
		6;
		
	public static final int KEY_BACKSPACE =
		8;
		
	public static final int KEY_DELETE =
		127;
		
	public static final int KEY_DOWN =
		-2;
		
	public static final int KEY_ENTER =
		10;
		
	public static final int KEY_ESCAPE =
		27;
		
	public static final int KEY_LEFT =
		-3;
		
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
		
	public static final int KEY_RIGHT =
		-4;
		
	public static final int KEY_SELECT =
		-5;
		
	public static final int KEY_SPACE =
		32;
		
	public static final int KEY_STAR =
		42;
		
	public static final int KEY_TAB =
		9;
		
	public static final int KEY_UP =
		-1;
		
	public static final int LEFT =
		2;
		
	public static final int MODIFIER_ALT =
		65536;
		
	public static final int MODIFIER_CHR =
		8388608;
		
	public static final int MODIFIER_COMMAND =
		4194304;
		
	public static final int MODIFIER_CTRL =
		262144;
		
	public static final int MODIFIER_MASK =
		13041664;
		
	public static final int MODIFIER_SHIFT =
		131072;
		
	public static final int RIGHT =
		5;
		
	public static final int UP =
		1;

	public abstract void keyPressed(InputDevice __dev, int __code, int __mod);

	public abstract void keyReleased(InputDevice __dev, int __code, int __mod);

	public abstract void keyRepeated(InputDevice __dev, int __code, int __mod);
}

