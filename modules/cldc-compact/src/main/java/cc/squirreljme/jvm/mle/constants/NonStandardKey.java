// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * This interface contains identifiers for non-standard keys.
 *
 * @since 2017/02/12
 */
public interface NonStandardKey
{
	/** Star key. */
	int KEY_STAR =
		42;
	
	/** Pound key. */
	int KEY_POUND =
		35;
	
	/** Unknown, zero is the invalid index so always make it known. */
	byte UNKNOWN =
		0;
	
	/** The up arrow key. */
	int KEY_UP =
		-1;
	
	/** Down arrow key. */
	int KEY_DOWN =
		-2;
	
	/** Left arrow key. */
	int KEY_LEFT =
		-3;
	
	/** Right arrow key. */
	int KEY_RIGHT =
		-4;
	
	/** Game Up. */
	byte VGAME_UP =
		-9;
	
	/** Game Down. */
	byte VGAME_DOWN =
		-10;
	
	/** Game Left. */
	byte VGAME_LEFT =
		-11;
	
	/** Game Right. */
	byte VGAME_RIGHT =
		-12;
	
	/** Game fire. */
	byte VGAME_FIRE =
		-13;
	
	/** Game A. */
	byte VGAME_A =
		-14;
	
	/** Game B. */
	byte VGAME_B =
		-15;
	
	/** Game C. */
	byte VGAME_C =
		-16;
	
	/** Game D. */
	byte VGAME_D =
		-17;
	
	/** Shift. */
	byte SHIFT =
		-18;
	
	/** Control. */
	byte CONTROL =
		-19;
	
	/** Alt. */
	byte ALT =
		-20;
	
	/** Logo. */
	byte LOGO =
		-21;
	
	/** Caps lock. */
	byte CAPSLOCK =
		-22;
	
	/** Context menu. */
	byte CONTEXT_MENU =
		-23;
	
	/** Home. */
	byte HOME =
		-24;
	
	/** End. */
	byte END =
		-25;
	
	/** Page Up. */
	byte PAGE_UP =
		-26;
	
	/** Page Down. */
	byte PAGE_DOWN =
		-27;
	
	/** Meta. */
	byte META =
		-28;
	
	/** Numlock. */
	byte NUMLOCK =
		-29;
	
	/** Pause. */
	byte PAUSE =
		-30;
	
	/** Print Screen. */
	byte PRINTSCREEN =
		-31;
	
	/** Scroll lock. */
	byte SCROLLLOCK =
		-32;
	
	/** Insert. */
	byte INSERT =
		-33;
	
	/** Game Virtual Left Command. */
	byte VGAME_COMMAND_LEFT =
		-34;
	
	/** Game Virtual Right Command. */
	byte VGAME_COMMAND_RIGHT =
		-35;
	
	/** Game virtual Center Command. */
	byte VGAME_COMMAND_CENTER =
		-36;
	
	/** Game virtual open LCDUI inspector. */
	byte VGAME_LCDUI_INSPECTOR =
		-37;
	
	/** Number pad divide. */
	byte NUMPAD_DIVIDE =
		-38;
	
	/** Number pad multiply. */
	byte NUMPAD_MULTIPLY =
		-39;
	
	/** Number pad minus. */
	byte NUMPAD_MINUS =
		-40;
	
	/** Number pad plus. */
	byte NUMPAD_PLUS =
		-41;
	
	/** Number pad decimal. */
	byte NUMPAD_DECIMAL =
		-42;
	
	/** Number pad enter. */
	byte NUMPAD_ENTER =
		-43;
	
	/** Number pad 0. */
	byte NUMPAD_0 =
		-50;
	
	/** Number pad 1. */
	byte NUMPAD_1 =
		-51;
	
	/** Number pad 2. */
	byte NUMPAD_2 =
		-52;
	
	/** Number pad 3. */
	byte NUMPAD_3 =
		-53;
	
	/** Number pad 4. */
	byte NUMPAD_4 =
		-54;
	
	/** Number pad 5. */
	byte NUMPAD_5 =
		-55;
	
	/** Number pad 6. */
	byte NUMPAD_6 =
		-56;
	
	/** Number pad 7. */
	byte NUMPAD_7 =
		-57;
	
	/** Number pad 8. */
	byte NUMPAD_8 =
		-58;
	
	/** Number pad 9. */
	byte NUMPAD_9 =
		-59;
	
	/** F24. */
	byte F24 =
		-64;
	
	/** F1. */
	byte F1 =
		NonStandardKey.F24 - 24;
	
	/** F2. */
	byte F2 =
		NonStandardKey.F1 + 1;
	
	/** F3. */
	byte F3 =
		NonStandardKey.F1 + 2;
	
	/** F3. */
	byte F12 =
		NonStandardKey.F1 + 11;
	
	/** F13. */
	byte F13 =
		NonStandardKey.F1 + 12;
}

