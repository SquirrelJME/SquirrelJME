// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	/** Unknown, zero is the invalid index so always make it known. */
	byte UNKNOWN =
		0;
	
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
	byte META_COMMAND =
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
	
	/** F24. */
	byte F24 =
		-35;
	
	/** F1. */
	byte F1 = NonStandardKey.F24 - 24;
}

