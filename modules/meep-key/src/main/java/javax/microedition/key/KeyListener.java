// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.key;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface KeyListener
{
	@Api
	int DOWN =
		6;
		
	@Api
	int KEY_BACKSPACE =
		8;
		
	@Api
	int KEY_DELETE =
		127;
		
	@Api
	int KEY_DOWN =
		-2;
		
	@Api
	int KEY_ENTER =
		10;
		
	@Api
	int KEY_ESCAPE =
		27;
		
	@Api
	int KEY_LEFT =
		-3;
		
	@Api
	int KEY_NUM0 =
		48;
		
	@Api
	int KEY_NUM1 =
		49;
		
	@Api
	int KEY_NUM2 =
		50;
		
	@Api
	int KEY_NUM3 =
		51;
		
	@Api
	int KEY_NUM4 =
		52;
		
	@Api
	int KEY_NUM5 =
		53;
		
	@Api
	int KEY_NUM6 =
		54;
		
	@Api
	int KEY_NUM7 =
		55;
		
	@Api
	int KEY_NUM8 =
		56;
		
	@Api
	int KEY_NUM9 =
		57;
		
	@Api
	int KEY_POUND =
		35;
		
	@Api
	int KEY_RIGHT =
		-4;
		
	@Api
	int KEY_SELECT =
		-5;
		
	@Api
	int KEY_SPACE =
		32;
		
	@Api
	int KEY_STAR =
		42;
		
	@Api
	int KEY_TAB =
		9;
		
	@Api
	int KEY_UP =
		-1;
		
	@Api
	int LEFT =
		2;
	
	/** Alt key modifier. */
	@Api
	int MODIFIER_ALT =
		65536;
		
	/** Function (Fn/Chr) key modifier. */
	@Api
	int MODIFIER_CHR =
		8388608;
	
	/** Command key modifier. */
	@Api
	int MODIFIER_COMMAND =
		4194304;
	
	/** Ctrl key modifier. */
	@Api
	int MODIFIER_CTRL =
		262144;
	
	/** Mask for all the modifier keys. */
	@Api
	int MODIFIER_MASK =
		13041664;
	
	/** Shift key modifier. */
	@Api
	int MODIFIER_SHIFT =
		131072;
		
	@Api
	int RIGHT =
		5;
		
	@Api
	int UP =
		1;

	@Api
	void keyPressed(InputDevice __dev, int __code, int __mod);

	@Api
	void keyReleased(InputDevice __dev, int __code, int __mod);

	@Api
	void keyRepeated(InputDevice __dev, int __code, int __mod);
}

