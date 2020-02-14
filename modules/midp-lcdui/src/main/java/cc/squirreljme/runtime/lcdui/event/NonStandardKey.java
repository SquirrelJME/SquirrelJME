// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

/**
 * This byteerface contains identifiers for non-standard keys.
 *
 * @since 2017/02/12
 */
public interface NonStandardKey
{
	/** Unknown, zero is the invalid index so always make it known. */
	public static final byte UNKNOWN =
		0;
	
	/** Game Up. */
	public static final byte VGAME_UP =
		-9;
	
	/** Game Down. */
	public static final byte VGAME_DOWN =
		-10;
	
	/** Game Left. */
	public static final byte VGAME_LEFT =
		-11;
	
	/** Game Right. */
	public static final byte VGAME_RIGHT =
		-12;
	
	/** Game fire. */
	public static final byte VGAME_FIRE =
		-13;
	
	/** Game A. */
	public static final byte VGAME_A =
		-14;
	
	/** Game B. */
	public static final byte VGAME_B =
		-15;
	
	/** Game C. */
	public static final byte VGAME_C =
		-16;
	
	/** Game D. */
	public static final byte VGAME_D =
		-17;
	
	/** Shift. */
	public static final byte SHIFT =
		-18;
	
	/** Control. */
	public static final byte CONTROL =
		-19;
	
	/** Alt. */
	public static final byte ALT =
		-20;
	
	/** Logo. */
	public static final byte LOGO =
		-21;
	
	/** Caps lock. */
	public static final byte CAPSLOCK =
		-22;
	
	/** Context menu. */
	public static final byte CONTEXT_MENU =
		-23;
	
	/** Home. */
	public static final byte HOME =
		-24;
	
	/** End. */
	public static final byte END =
		-25;
	
	/** Page Up. */
	public static final byte PAGE_UP =
		-26;
	
	/** Page Down. */
	public static final byte PAGE_DOWN =
		-27;
	
	/** Meta. */
	public static final byte META =
		-28;
	
	/** Numlock. */
	public static final byte NUMLOCK =
		-29;
	
	/** Pause. */
	public static final byte PAUSE =
		-30;
	
	/** Print Screen. */
	public static final byte PRINTSCREEN =
		-31;
	
	/** Scroll lock. */
	public static final byte SCROLLLOCK =
		-32;
	
	/** Insert. */
	public static final byte INSERT =
		-33;
	
	/** F24. */
	public static final byte F24 =
		-34;
	
	/** F1. */
	public static final byte F1 =
		F24 - 24;
}

