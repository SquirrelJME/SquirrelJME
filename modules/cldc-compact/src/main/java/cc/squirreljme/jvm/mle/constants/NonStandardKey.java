// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This interface contains identifiers for non-standard keys.
 *
 * @since 2017/02/12
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@SquirrelJMEVendorApi
public interface NonStandardKey
{
	/** Star key. */
	@SquirrelJMEVendorApi
	byte KEY_STAR =
		42;
	
	/** Pound key. */
	@SquirrelJMEVendorApi
	byte KEY_POUND =
		35;
	
	/** Unknown, zero is the invalid index so always make it known. */
	@SquirrelJMEVendorApi
	byte UNKNOWN =
		0;
	
	/** The up arrow key. */
	@SquirrelJMEVendorApi
	byte KEY_UP =
		-1;
	
	/** Down arrow key. */
	@SquirrelJMEVendorApi
	byte KEY_DOWN =
		-2;
	
	/** Left arrow key. */
	@SquirrelJMEVendorApi
	byte KEY_LEFT =
		-3;
	
	/** Right arrow key. */
	@SquirrelJMEVendorApi
	byte KEY_RIGHT =
		-4;
	
	/** Game Up. */
	@SquirrelJMEVendorApi
	byte VGAME_UP =
		-9;
	
	/** Game Down. */
	@SquirrelJMEVendorApi
	byte VGAME_DOWN =
		-10;
	
	/** Game Left. */
	@SquirrelJMEVendorApi
	byte VGAME_LEFT =
		-11;
	
	/** Game Right. */
	@SquirrelJMEVendorApi
	byte VGAME_RIGHT =
		-12;
	
	/** Game fire. */
	@SquirrelJMEVendorApi
	byte VGAME_FIRE =
		-13;
	
	/** Game A. */
	@SquirrelJMEVendorApi
	byte VGAME_A =
		-14;
	
	/** Game B. */
	@SquirrelJMEVendorApi
	byte VGAME_B =
		-15;
	
	/** Game C. */
	@SquirrelJMEVendorApi
	byte VGAME_C =
		-16;
	
	/** Game D. */
	@SquirrelJMEVendorApi
	byte VGAME_D =
		-17;
	
	/** Shift. */
	@SquirrelJMEVendorApi
	byte SHIFT =
		-18;
	
	/** Control. */
	@SquirrelJMEVendorApi
	byte CONTROL =
		-19;
	
	/** Alt. */
	@SquirrelJMEVendorApi
	byte ALT =
		-20;
	
	/** Logo. */
	@SquirrelJMEVendorApi
	byte LOGO =
		-21;
	
	/** Caps lock. */
	@SquirrelJMEVendorApi
	byte CAPSLOCK =
		-22;
	
	/** Context menu. */
	@SquirrelJMEVendorApi
	byte CONTEXT_MENU =
		-23;
	
	/** Home. */
	@SquirrelJMEVendorApi
	byte HOME =
		-24;
	
	/** End. */
	@SquirrelJMEVendorApi
	byte END =
		-25;
	
	/** Page Up. */
	@SquirrelJMEVendorApi
	byte PAGE_UP =
		-26;
	
	/** Page Down. */
	@SquirrelJMEVendorApi
	byte PAGE_DOWN =
		-27;
	
	/** Meta. */
	@SquirrelJMEVendorApi
	byte META =
		-28;
	
	/** Numlock. */
	@SquirrelJMEVendorApi
	byte NUMLOCK =
		-29;
	
	/** Pause. */
	@SquirrelJMEVendorApi
	byte PAUSE =
		-30;
	
	/** Print Screen. */
	@SquirrelJMEVendorApi
	byte PRINTSCREEN =
		-31;
	
	/** Scroll lock. */
	@SquirrelJMEVendorApi
	byte SCROLLLOCK =
		-32;
	
	/** Insert. */
	@SquirrelJMEVendorApi
	byte INSERT =
		-33;
	
	/** Game Virtual Left Command. */
	@SquirrelJMEVendorApi
	byte VGAME_COMMAND_LEFT =
		-34;
	
	/** Game Virtual Right Command. */
	@SquirrelJMEVendorApi
	byte VGAME_COMMAND_RIGHT =
		-35;
	
	/** Game virtual Center Command. */
	@SquirrelJMEVendorApi
	byte VGAME_COMMAND_CENTER =
		-36;
	
	/** Game virtual open LCDUI inspector. */
	@SquirrelJMEVendorApi
	byte VGAME_LCDUI_INSPECTOR =
		-37;
	
	/** Number pad divide. */
	@SquirrelJMEVendorApi
	byte NUMPAD_DIVIDE =
		-38;
	
	/** Number pad multiply. */
	@SquirrelJMEVendorApi
	byte NUMPAD_MULTIPLY =
		-39;
	
	/** Number pad minus. */
	@SquirrelJMEVendorApi
	byte NUMPAD_MINUS =
		-40;
	
	/** Number pad plus. */
	@SquirrelJMEVendorApi
	byte NUMPAD_PLUS =
		-41;
	
	/** Number pad decimal. */
	@SquirrelJMEVendorApi
	byte NUMPAD_DECIMAL =
		-42;
	
	/** Number pad enter. */
	@SquirrelJMEVendorApi
	byte NUMPAD_ENTER =
		-43;
	
	/** Number pad 0. */
	@SquirrelJMEVendorApi
	byte NUMPAD_0 =
		-50;
	
	/** Number pad 1. */
	@SquirrelJMEVendorApi
	byte NUMPAD_1 =
		-51;
	
	/** Number pad 2. */
	@SquirrelJMEVendorApi
	byte NUMPAD_2 =
		-52;
	
	/** Number pad 3. */
	@SquirrelJMEVendorApi
	byte NUMPAD_3 =
		-53;
	
	/** Number pad 4. */
	@SquirrelJMEVendorApi
	byte NUMPAD_4 =
		-54;
	
	/** Number pad 5. */
	@SquirrelJMEVendorApi
	byte NUMPAD_5 =
		-55;
	
	/** Number pad 6. */
	@SquirrelJMEVendorApi
	byte NUMPAD_6 =
		-56;
	
	/** Number pad 7. */
	@SquirrelJMEVendorApi
	byte NUMPAD_7 =
		-57;
	
	/** Number pad 8. */
	@SquirrelJMEVendorApi
	byte NUMPAD_8 =
		-58;
	
	/** Number pad 9. */
	@SquirrelJMEVendorApi
	byte NUMPAD_9 =
		-59;
	
	/** F24. */
	@SquirrelJMEVendorApi
	byte F24 =
		-64;
	
	/** F23. */
	@SquirrelJMEVendorApi
	byte F23 =
		-65;
	
	/** F22. */
	@SquirrelJMEVendorApi
	byte F22 =
		-66;
	
	/** F21. */
	@SquirrelJMEVendorApi
	byte F21 =
		-67;
	
	/** F20. */
	@SquirrelJMEVendorApi
	byte F20 =
		-68;
	
	/** F19. */
	@SquirrelJMEVendorApi
	byte F19 =
		-69;
	
	/** F18. */
	@SquirrelJMEVendorApi
	byte F18 =
		-70;
	
	/** F17. */
	@SquirrelJMEVendorApi
	byte F17 =
		-71;
	
	/** F16. */
	@SquirrelJMEVendorApi
	byte F16 =
		-72;
	
	/** F15. */
	@SquirrelJMEVendorApi
	byte F15 =
		-73;
	
	/** F14. */
	@SquirrelJMEVendorApi
	byte F14 =
		-74;
	
	/** F13. */
	@SquirrelJMEVendorApi
	byte F13 =
		-75;
	
	/** F12. */
	@SquirrelJMEVendorApi
	byte F12 =
		-76;
	
	/** F11. */
	@SquirrelJMEVendorApi
	byte F11 =
		-77;
	
	/** F10. */
	@SquirrelJMEVendorApi
	byte F10 =
		-78;
	
	/** F9. */
	@SquirrelJMEVendorApi
	byte F9 =
		-79;
	
	/** F8. */
	@SquirrelJMEVendorApi
	byte F8 =
		-80;
	
	/** F7. */
	@SquirrelJMEVendorApi
	byte F7 =
		-81;
	
	/** F6. */
	@SquirrelJMEVendorApi
	byte F6 =
		-82;
	
	/** F5. */
	@SquirrelJMEVendorApi
	byte F5 =
		-83;
	
	/** F4. */
	@SquirrelJMEVendorApi
	byte F4 =
		-84;
	
	/** F3. */
	@SquirrelJMEVendorApi
	byte F3 =
		-85;
	
	/** F2. */
	@SquirrelJMEVendorApi
	byte F2 =
		-86;
	
	/** F1. */
	@SquirrelJMEVendorApi
	byte F1 =
		-87;
}
