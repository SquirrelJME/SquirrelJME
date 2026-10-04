// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * This interface contains identifiers for non-standard keys.
 *
 * @since 2017/02/12
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@SquirrelJMENativeApi(min = "0.4.0")
public interface NonStandardKey
{
	/** Star key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte KEY_STAR =
		42;
	
	/** Pound key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte KEY_POUND =
		35;
	
	/** Unknown, zero is the invalid index so always make it known. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNKNOWN =
		0;
	
	/** The up arrow key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte KEY_UP =
		-1;
	
	/** Down arrow key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte KEY_DOWN =
		-2;
	
	/** Left arrow key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte KEY_LEFT =
		-3;
	
	/** Right arrow key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte KEY_RIGHT =
		-4;
	
	/** Game Up. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_UP =
		-9;
	
	/** Game Down. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_DOWN =
		-10;
	
	/** Game Left. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_LEFT =
		-11;
	
	/** Game Right. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_RIGHT =
		-12;
	
	/** Game fire. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_FIRE =
		-13;
	
	/** Game A. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_A =
		-14;
	
	/** Game B. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_B =
		-15;
	
	/** Game C. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_C =
		-16;
	
	/** Game D. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_D =
		-17;
	
	/** Shift. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SHIFT =
		-18;
	
	/** Control. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CONTROL =
		-19;
	
	/** Alt. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALT =
		-20;
	
	/** Logo. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LOGO =
		-21;
	
	/** Caps lock. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CAPSLOCK =
		-22;
	
	/** Context menu. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CONTEXT_MENU =
		-23;
	
	/** Home. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HOME =
		-24;
	
	/** End. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte END =
		-25;
	
	/** Page Up. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PAGE_UP =
		-26;
	
	/** Page Down. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PAGE_DOWN =
		-27;
	
	/** Meta. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte META =
		-28;
	
	/** Numlock. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMLOCK =
		-29;
	
	/** Pause. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PAUSE =
		-30;
	
	/** Print Screen. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PRINTSCREEN =
		-31;
	
	/** Scroll lock. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SCROLLLOCK =
		-32;
	
	/** Insert. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INSERT =
		-33;
	
	/** Game Virtual Left Command. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_COMMAND_LEFT =
		-34;
	
	/** Game Virtual Right Command. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_COMMAND_RIGHT =
		-35;
	
	/** Game virtual Center Command. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VGAME_COMMAND_CENTER =
		-36;
	
	/** Reserved 37. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte RESERVED_37 =
		-37;
	
	/** Number pad divide. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_DIVIDE =
		-38;
	
	/** Number pad multiply. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_MULTIPLY =
		-39;
	
	/** Number pad minus. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_MINUS =
		-40;
	
	/** Number pad plus. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_PLUS =
		-41;
	
	/** Number pad decimal. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_DECIMAL =
		-42;
	
	/** Number pad enter. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_ENTER =
		-43;
	
	/** Number pad 0. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_0 =
		-50;
	
	/** Number pad 1. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_1 =
		-51;
	
	/** Number pad 2. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_2 =
		-52;
	
	/** Number pad 3. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_3 =
		-53;
	
	/** Number pad 4. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_4 =
		-54;
	
	/** Number pad 5. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_5 =
		-55;
	
	/** Number pad 6. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_6 =
		-56;
	
	/** Number pad 7. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_7 =
		-57;
	
	/** Number pad 8. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_8 =
		-58;
	
	/** Number pad 9. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_9 =
		-59;
	
	/** F24. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F24 =
		-64;
	
	/** F23. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F23 =
		-65;
	
	/** F22. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F22 =
		-66;
	
	/** F21. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F21 =
		-67;
	
	/** F20. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F20 =
		-68;
	
	/** F19. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F19 =
		-69;
	
	/** F18. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F18 =
		-70;
	
	/** F17. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F17 =
		-71;
	
	/** F16. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F16 =
		-72;
	
	/** F15. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F15 =
		-73;
	
	/** F14. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F14 =
		-74;
	
	/** F13. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F13 =
		-75;
	
	/** F12. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F12 =
		-76;
	
	/** F11. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F11 =
		-77;
	
	/** F10. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F10 =
		-78;
	
	/** F9. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F9 =
		-79;
	
	/** F8. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F8 =
		-80;
	
	/** F7. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F7 =
		-81;
	
	/** F6. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F6 =
		-82;
	
	/** F5. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F5 =
		-83;
	
	/** F4. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F4 =
		-84;
	
	/** F3. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F3 =
		-85;
	
	/** F2. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F2 =
		-86;
	
	/** F1. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte F1 =
		-87;
	
	/** Camera Shutter. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CAMERA_SHUTTER =
		-88;
	
	/** Increase volume. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VOLUME_INCREASE =
		-89;
	
	/** Decrease volume. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VOLUME_DECREASE =
		-90;
	
	/** Toggle Power. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte POWER_TOGGLE =
		-91;
	
	/** Toggle On. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte POWER_ON =
		-92;
	
	/** Toggle Off. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte POWER_OFF =
		-93;
	
	/** Accept call. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CALL_ACCEPT =
		-94;
	
	/** Deny call. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CALL_DENY =
		-95;
	
	/** Disconnect call. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CALL_DISCONNECT =
		-96;
}
