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
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface NonStandardKey
{
	/** Star key. */
	byte KEY_STAR =
		42;
	
	/** Pound key. */
	byte KEY_POUND =
		35;
	
	/** Unknown, zero is the invalid index so always make it known. */
	byte UNKNOWN =
		0;
	
	/** The up arrow key. */
	byte KEY_UP =
		-1;
	
	/** Down arrow key. */
	byte KEY_DOWN =
		-2;
	
	/** Left arrow key. */
	byte KEY_LEFT =
		-3;
	
	/** Right arrow key. */
	byte KEY_RIGHT =
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
	
	/** Reserved 37. */
	byte RESERVED_37 =
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
	
	/** F23. */
	byte F23 =
		-65;
	
	/** F22. */
	byte F22 =
		-66;
	
	/** F21. */
	byte F21 =
		-67;
	
	/** F20. */
	byte F20 =
		-68;
	
	/** F19. */
	byte F19 =
		-69;
	
	/** F18. */
	byte F18 =
		-70;
	
	/** F17. */
	byte F17 =
		-71;
	
	/** F16. */
	byte F16 =
		-72;
	
	/** F15. */
	byte F15 =
		-73;
	
	/** F14. */
	byte F14 =
		-74;
	
	/** F13. */
	byte F13 =
		-75;
	
	/** F12. */
	byte F12 =
		-76;
	
	/** F11. */
	byte F11 =
		-77;
	
	/** F10. */
	byte F10 =
		-78;
	
	/** F9. */
	byte F9 =
		-79;
	
	/** F8. */
	byte F8 =
		-80;
	
	/** F7. */
	byte F7 =
		-81;
	
	/** F6. */
	byte F6 =
		-82;
	
	/** F5. */
	byte F5 =
		-83;
	
	/** F4. */
	byte F4 =
		-84;
	
	/** F3. */
	byte F3 =
		-85;
	
	/** F2. */
	byte F2 =
		-86;
	
	/** F1. */
	byte F1 =
		-87;
	
	/** Camera Shutter. */
	byte CAMERA_SHUTTER =
		-88;
	
	/** Increase volume. */
	byte VOLUME_INCREASE =
		-89;
	
	/** Decrease volume. */
	byte VOLUME_DECREASE =
		-90;
	
	/** Toggle Power. */
	byte POWER_TOGGLE =
		-91;
	
	/** Toggle On. */
	byte POWER_ON =
		-92;
	
	/** Toggle Off. */
	byte POWER_OFF =
		-93;
	
	/** Accept call. */
	byte CALL_ACCEPT =
		-94;
	
	/** Deny call. */
	byte CALL_DENY =
		-95;
	
	/** Disconnect call. */
	byte CALL_DISCONNECT =
		-96;
}
