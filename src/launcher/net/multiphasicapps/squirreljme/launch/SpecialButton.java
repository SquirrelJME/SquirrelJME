// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch;

/**
 * This describes a button which is special in that it has no real assigned
 * glyph or is natural to a language, but to computers instead.
 *
 * The indexes must be within Unicode's private use range, when a given button
 * is pressed it generates the given keycode. This also means that the button
 * codes can be printed to the screen potentially. Thus characters between
 * 0xE000 and 0xF8FF are valid for usage here.
 *
 * @since 2016/05/15
 */
public interface SpecialButton
{
	/** Start of buttons. */
	public static final int START_OF_LIST =
		0xE000;
	
	/** The ESC key. */
	public static final int ESCAPE =
		0xE001;
	
	/** The ALT/Option Key. */
	public static final int ALT_OPTION =
		0xE002;
	
	/** The CTRL key. */
	public static final int CTRL =
		0xE003;
	
	/** The logo key. */
	public static final int LOGO =
		0xE004;
	
	/** The shift key. */
	public static final int SHIFT =
		0xE005;
	
	/** The caps lock key. */
	public static final int CAPS_LOCK =
		0xE006;
	
	/** The shift lock key. */
	public static final int SHIFT_LOCK =
		0xE007;
	
	/** The number lock key. */
	public static final int NUMBER_LOCK =
		0xE008;
	
	/** The scroll lock key. */
	public static final int SCROLL_LOCK =
		0xE009;
	
	/** The delete key. */
	public static final int DELETE =
		0xE00A;
	
	/** The backspace key. */
	public static final int BACKSPACE =
		0xE00B;
	
	/** The enter key. */
	public static final int ENTER =
		0xE009;
	
	/** The insert key. */
	public static final int INSERT =
		0xE00A;
	
	/** The home key. */
	public static final int HOME =
		0xE00B;
	
	/** The end key. */
	public static final int END =
		0xE00C;
	
	/** Page up. */
	public static final int PAGE_UP =
		0xE00D;
	
	/** Page down. */
	public static final int PAGE_DOWN =
		0xE00E;
	
	/** Page left. */
	public static final int PAGE_LEFT =
		0xE00F;
	
	/** Page right. */
	public static final int PAGE_RIGHT =
		0xE010;
	
	/** Up. */
	public static final int UP =
		0xE011;
	
	/** Down. */
	public static final int DOWN =
		0xE012;
	
	/** Left. */
	public static final int LEFT =
		0xE013;
	
	/** Right. */
	public static final int RIGHT =
		0xE014;
	
	/** Numeric pad up. */
	public static final int NUMBER_PAD_UP =
		0xE015;
	
	/** Numeric pad down. */
	public static final int NUMBER_PAD_DOWN =
		0xE016;
	
	/** Numeric pad left. */
	public static final int NUMBER_PAD_LEFT =
		0xE017;
	
	/** Numeric pad right. */
	public static final int NUMBER_PAD_RIGHT =
		0xE018;
	
	/** Secondary up. */
	public static final int SECONDARY_UP =
		0xE019;
	
	/** Secondary down. */
	public static final int SECONDARY_DOWN =
		0xE01A;
	
	/** Secondary left. */
	public static final int SECONDARY_LEFT =
		0xE01B;
	
	/** Secondary right. */
	public static final int SECONDARY_RIGHT =
		0xE01C;
	
	/** Screen contrast increase. */
	public static final int SCREEN_CONTRAST_INCREASE =
		0xE01D;
	
	/** Screen contrast decrease. */
	public static final int SCREEN_CONTRAST_DECREASE =
		0xE01E;
	
	/** Screen backlight toggle. */
	public static final int SCREEN_BACKLIGHT_TOGGLE =
		0xE01F;
	
	/** Screen backlight brighter. */
	public static final int SCREEN_BACKLIGHT_BRIGHTER =
		0xE020;
	
	/** Screen backlight dimmer. */
	public static final int SCREEN_BACKLIGHT_DIMMER =
		0xE021;
	
	/** Keyboard backlight toggle. */
	public static final int KEYBOARD_BACKLIGHT_TOGGLE =
		0xE022;
	
	/** Keyboard backlight brighter. */
	public static final int KEYBOARD_BACKLIGHT_BRIGHTER =
		0xE023;
	
	/** Keyboard backlight dimmer. */
	public static final int KEYBOARD_BACKLIGHT_DIMMER =
		0xE024;
	
	/** Screen brightness increase. */
	public static final int SCREEN_BRIGHTNESS_INCREASE =
		0xE025;
	
	/** Screen brightness decrease. */
	public static final int SCREEN_BRIGHTNESS_DECREASE =
		0xE026;
	
	/** Screen tint increase. */
	public static final int SCREEN_TINT_INCREASE =
		0xE027;
	
	/** Screen tint decrease. */
	public static final int SCREEN_TINT_DECREASE =
		0xE028;
	
	/** Degauss the screen. */
	public static final int SCREEN_DEGAUSS =
		0xE029;
	
	/** Volume mute toggle. */
	public static final int VOLUME_MUTE_TOGGLE =
		0xE02A;
	
	/** Volume increase. */
	public static final int VOLUME_INCREASE =
		0xE02B;
	
	/** Volume decrease. */
	public static final int VOLUME_DECREASE =
		0xE02C;
	
	/** Previous screen display. */
	public static final int SCREEN_DISPLAY_PREVIOUS =
		0xE02D;
	
	/** Next screen display. */
	public static final int SCREEN_DISPLAY_NEXT =
		0xE02E;
	
	/** Eject floppy disk A. */
	public static final int EJECT_FLOPPY_A =
		0xE02F;
	
	/** Eject floppy disk B. */
	public static final int EJECT_FLOPPY_B =
		0xE030;
	
	/** Eject disc A. */
	public static final int EJECT_DISC_A =
		0xE031;
	
	/** Eject disc B. */
	public static final int EJECT_DISC_B =
		0xE032;
	
	/** Media play. */
	public static final int MEDIA_PLAY =
		0xE033;
	
	/** Media pause. */
	public static final int MEDIA_PAUSE =
		0xE034;
	
	/** Media play/pause toggle. */
	public static final int MEDIA_PLAY_PAUSE_TOGGLE =
		0xE035;
	
	/** Media stop. */
	public static final int MEDIA_STOP =
		0xE036;
	
	/** Media rewind. */
	public static final int MEDIA_REWIND =
		0xE037;
	
	/** Media fast forward. */
	public static final int MEDIA_FAST_FORWARD =
		0xE038;
	
	/** Media step single frame back. */
	public static final int MEDIA_FRAME_STEP_PREVIOUS =
		0xE039;
	
	/** Media step single frame forwards. */
	public static final int MEDIA_FRAME_STEP_NEXT =
		0xE03A;
	
	/** Media record. */
	public static final int MEDIA_RECORD =
		0xE03B;
	
	/** The F1 key. */
	public static final int F1 =
		0xE0F1;
	
	/** The F2 key. */
	public static final int F2 =
		0xE0F2;
	
	/** The F3 key. */
	public static final int F3 =
		0xE0F3;
	
	/** The F4 key. */
	public static final int F4 =
		0xE0F4;
	
	/** The F5 key. */
	public static final int F5 =
		0xE0F5;
	
	/** The F6 key. */
	public static final int F6 =
		0xE0F6;
	
	/** The F7 key. */
	public static final int F7 =
		0xE0F7;
	
	/** The F8 key. */
	public static final int F8 =
		0xE0F8;
	
	/** The F9 key. */
	public static final int F9 =
		0xE0F9;
	
	/** The F10 key. */
	public static final int F10 =
		0xE0FA;
	
	/** The F11 key. */
	public static final int F11 =
		0xE0FB;
	
	/** The F12 key. */
	public static final int F12 =
		0xE0FC;
	
	/** The F13 key. */
	public static final int F13 =
		0xE0FD;
	
	/** The F14 key. */
	public static final int F14 =
		0xE0FE;
	
	/** The F15 key. */
	public static final int F15 =
		0xE0FF;
	
	/** The F16 key. */
	public static final int F16 =
		0xE100;
	
	/** The F17 key. */
	public static final int F17 =
		0xE101;
	
	/** The F18 key. */
	public static final int F18 =
		0xE102;
	
	/** The F19 key. */
	public static final int F19 =
		0xE103;
	
	/** The F20 key. */
	public static final int F20 =
		0xE104;
	
	/** The F21 key. */
	public static final int F21 =
		0xE105;
	
	/** The F22 key. */
	public static final int F22 =
		0xE106;
	
	/** The F23 key. */
	public static final int F23 =
		0xE107;
	
	/** The F24 key. */
	public static final int F24 =
		0xE108;
	
	/** The F25 key. */
	public static final int F25 =
		0xE109;
	
	/** The F26 key. */
	public static final int F26 =
		0xE10A;
	
	/** The F27 key. */
	public static final int F27 =
		0xE10B;
	
	/** The F28 key. */
	public static final int F28 =
		0xE10C;
	
	/** The F29 key. */
	public static final int F29 =
		0xE10D;
	
	/** Alpha lock. */
	public static final int ALPHA_LOCK =
		0xE10E;
	
	/** Cut. */
	public static final int CUT =
		0xE10F;
	
	/** Copy. */
	public static final int COPY =
		0xE110;
	
	/** Paste. */
	public static final int PASTE =
		0xE111;
	
	/** Custom. */
	public static final int CUSTOM =
		0xE112;
	
	/** Infinity. */
	public static final int INFINITY =
		0xE113;
	
	/** Natural logarithm. */
	public static final int NATURAL_LOG =
		0xE114;
	
	/** Binary logarithm .*/
	public static final int BINARY_LOG =
		0xE115;
	
	/** Octal logarithm. */
	public static final int OCTAL_LOG =
		0xE116;
	
	/** Decimal logarithm. */
	public static final int DECIMAL_LOG =
		0xE117;
	
	/** Hexadecimal logarithm. */
	public static final int HEX_LOG =
		0xE118;
	
	/** Natural Exponent. */
	public static final int NATURAL_EXPONENT =
		0xE119;
	
	/** Exponent. */
	public static final int EXPONENT =
		0xE11A;
	
	/** Sine. */
	public static final int SINE =
		0xE11B;
	
	/** Inverse sine. */
	public static final int INVERSE_SINE =
		0xE11C;
	
	/** Cosine. */
	public static final int COSINE =
		0xE11D;
	
	/** Inverse cosine. */
	public static final int INVERSE_COSINE =
		0xE11E;
	
	/** Tangent. */
	public static final int TANGENT =
		0xE11F;
	
	/** Inverse Tangent. */
	public static final int INVERSE_TANGENT =
		0xE120;
	
	/** Math Key. */
	public static final int MATH =
		0xE121;
	
	/** Memory key. */
	public static final int MEMORY =
		0xE122;
	
	/** Var-Link key. */
	public static final int VAR_LINK =
		0xE123;
	
	/** Recall memory. */
	public static final int MEMORY_RECALL =
		0xE124;
	
	/** Store memory. */
	public static final int MEMORY_STORE =
		0xE125;
	
	/** Clear memory. */
	public static final int MEMORY_CLEAR =
		0xE126;
	
	/** Units. */
	public static final int UNITS =
		0xE127;
	
	/** Char. */
	public static final int CHAR =
		0xE128;
	
	/** Answer. */
	public static final int ANSWER =
		0xE129;
	
	/** End of buttons. */
	public static final int END_OF_LIST =
		0xF8FF;
}

