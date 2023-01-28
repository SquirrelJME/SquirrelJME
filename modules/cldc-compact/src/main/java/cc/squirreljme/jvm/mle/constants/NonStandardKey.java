// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This interface contains identifiers for non-standard keys.
 *
 * @since 2017/02/12
 */
@Exported
public interface NonStandardKey
{
	/** Star key. */
	@Exported
	int KEY_STAR =
		42;
	
	/** Pound key. */
	@Exported
	int KEY_POUND =
		35;
	
	/** Unknown, zero is the invalid index so always make it known. */
	@Exported
	byte UNKNOWN =
		0;
	
	/** The up arrow key. */
	@Exported
	int KEY_UP =
		-1;
	
	/** Down arrow key. */
	@Exported
	int KEY_DOWN =
		-2;
	
	/** Left arrow key. */
	@Exported
	int KEY_LEFT =
		-3;
	
	/** Right arrow key. */
	@Exported
	int KEY_RIGHT =
		-4;
	
	/** Game Up. */
	@Exported
	byte VGAME_UP =
		-9;
	
	/** Game Down. */
	@Exported
	byte VGAME_DOWN =
		-10;
	
	/** Game Left. */
	@Exported
	byte VGAME_LEFT =
		-11;
	
	/** Game Right. */
	@Exported
	byte VGAME_RIGHT =
		-12;
	
	/** Game fire. */
	@Exported
	byte VGAME_FIRE =
		-13;
	
	/** Game A. */
	@Exported
	byte VGAME_A =
		-14;
	
	/** Game B. */
	@Exported
	byte VGAME_B =
		-15;
	
	/** Game C. */
	@Exported
	byte VGAME_C =
		-16;
	
	/** Game D. */
	@Exported
	byte VGAME_D =
		-17;
	
	/** Shift. */
	@Exported
	byte SHIFT =
		-18;
	
	/** Control. */
	@Exported
	byte CONTROL =
		-19;
	
	/** Alt. */
	@Exported
	byte ALT =
		-20;
	
	/** Logo. */
	@Exported
	byte LOGO =
		-21;
	
	/** Caps lock. */
	@Exported
	byte CAPSLOCK =
		-22;
	
	/** Context menu. */
	@Exported
	byte CONTEXT_MENU =
		-23;
	
	/** Home. */
	@Exported
	byte HOME =
		-24;
	
	/** End. */
	@Exported
	byte END =
		-25;
	
	/** Page Up. */
	@Exported
	byte PAGE_UP =
		-26;
	
	/** Page Down. */
	@Exported
	byte PAGE_DOWN =
		-27;
	
	/** Meta. */
	@Exported
	byte META =
		-28;
	
	/** Numlock. */
	@Exported
	byte NUMLOCK =
		-29;
	
	/** Pause. */
	@Exported
	byte PAUSE =
		-30;
	
	/** Print Screen. */
	@Exported
	byte PRINTSCREEN =
		-31;
	
	/** Scroll lock. */
	@Exported
	byte SCROLLLOCK =
		-32;
	
	/** Insert. */
	@Exported
	byte INSERT =
		-33;
	
	/** Game Virtual Left Command. */
	@Exported
	byte VGAME_COMMAND_LEFT =
		-34;
	
	/** Game Virtual Right Command. */
	@Exported
	byte VGAME_COMMAND_RIGHT =
		-35;
	
	/** Game virtual Center Command. */
	@Exported
	byte VGAME_COMMAND_CENTER =
		-36;
	
	/** Game virtual open LCDUI inspector. */
	@Exported
	byte VGAME_LCDUI_INSPECTOR =
		-37;
	
	/** F24. */
	@Exported
	byte F24 =
		-40;
	
	/** F1. */
	@Exported
	byte F1 =
		NonStandardKey.F24 - 24;
	
	/** F2. */
	@Exported
	byte F2 =
		NonStandardKey.F1 + 1;
	
	/** F3. */
	@Exported
	byte F3 =
		NonStandardKey.F1 + 2;
	
	/** F3. */
	@Exported
	byte F12 =
		NonStandardKey.F1 + 11;
	
	/** F13. */
	@Exported
	byte F13 =
		NonStandardKey.F1 + 12;
}

