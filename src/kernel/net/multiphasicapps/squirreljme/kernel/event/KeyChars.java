// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.event;

/**
 * This contains key character codes for pressed key that are useful to have.
 *
 * Note that mouse and joystick buttons are considered keyboard keys for
 * simplicity in the event handler.
 *
 * Keys that have no officially assigned glyph or possible unicode match are to
 * be placed in the private use area, this way they get an actual assigned
 * glyph. The private area starts at {@code 0xE000}.
 *
 * @since 2016/05/15
 */
public interface KeyChars
{
	/** Enter key. */
	public static final char ENTER	=
		0x000A;
		
	/** Escape key. */
	public static final char ESCAPE	=
		0x001B;
		
	/** Backspace key. */
	public static final char BACK_SPACE	=
		0x0008;
		
	/** Delete key. */
	public static final char DELETE	=
		0x007F;
	
	/** ACCEPT. */
	public static final char ACCEPT =
		0xE000;

	/** AGAIN. */
	public static final char AGAIN =
		0xE001;

	/** ALL_CANDIDATES. */
	public static final char ALL_CANDIDATES =
		0xE002;

	/** ALPHANUMERIC. */
	public static final char ALPHANUMERIC =
		0xE003;

	/** ALT_GRAPH. */
	public static final char ALT_GRAPH =
		0xE004;

	/** BEGIN. */
	public static final char BEGIN =
		0xE005;

	/** BREAK. */
	public static final char BREAK =
		0xE006;

	/** CANCEL. */
	public static final char CANCEL =
		0xE007;

	/** CAPS_LOCK. */
	public static final char CAPS_LOCK =
		0xE008;

	/** CLEAR. */
	public static final char CLEAR =
		0xE009;

	/** CODE_INPUT. */
	public static final char CODE_INPUT =
		0xE00A;

	/** COMPOSE. */
	public static final char COMPOSE =
		0xE00B;

	/** CONTEXT_MENU. */
	public static final char CONTEXT_MENU =
		0xE00C;

	/** CONVERT. */
	public static final char CONVERT =
		0xE00D;

	/** COPY. */
	public static final char COPY =
		0xE00E;

	/** CUT. */
	public static final char CUT =
		0xE00F;

	/** DEAD_ABOVEDOT. */
	public static final char DEAD_ABOVEDOT =
		0xE010;

	/** DEAD_ABOVERING. */
	public static final char DEAD_ABOVERING =
		0xE011;

	/** DEAD_ACUTE. */
	public static final char DEAD_ACUTE =
		0xE012;

	/** DEAD_BREVE. */
	public static final char DEAD_BREVE =
		0xE013;

	/** DEAD_CARON. */
	public static final char DEAD_CARON =
		0xE014;

	/** DEAD_CEDILLA. */
	public static final char DEAD_CEDILLA =
		0xE015;

	/** DEAD_CIRCUMFLEX. */
	public static final char DEAD_CIRCUMFLEX =
		0xE016;

	/** DEAD_DIAERESIS. */
	public static final char DEAD_DIAERESIS =
		0xE017;

	/** DEAD_DOUBLEACUTE. */
	public static final char DEAD_DOUBLEACUTE =
		0xE018;

	/** DEAD_GRAVE. */
	public static final char DEAD_GRAVE =
		0xE019;

	/** DEAD_IOTA. */
	public static final char DEAD_IOTA =
		0xE01A;

	/** DEAD_MACRON. */
	public static final char DEAD_MACRON =
		0xE01B;

	/** DEAD_OGONEK. */
	public static final char DEAD_OGONEK =
		0xE01C;

	/** DEAD_SEMIVOICED_SOUND. */
	public static final char DEAD_SEMIVOICED_SOUND =
		0xE01D;

	/** DEAD_TILDE. */
	public static final char DEAD_TILDE =
		0xE01E;

	/** DEAD_VOICED_SOUND. */
	public static final char DEAD_VOICED_SOUND =
		0xE01F;

	/** DEBUG. */
	public static final char DEBUG =
		0xE020;

	/** DOWN. */
	public static final char DOWN =
		0xE021;

	/** EJECT_0. */
	public static final char EJECT_0 =
		0xE022;

	/** EJECT_MAX. */
	public static final char EJECT_MAX =
		0xE025;

	/** END. */
	public static final char END =
		0xE026;

	/** FUNCTION_1. */
	public static final char FUNCTION_1 =
		0xE027;

	/** FUNCTION_MAX. */
	public static final char FUNCTION_MAX =
		0xE03E;

	/** FINAL. */
	public static final char FINAL =
		0xE03F;

	/** FIND. */
	public static final char FIND =
		0xE040;

	/** FULL_WIDTH. */
	public static final char FULL_WIDTH =
		0xE041;

	/** GPIO_BUTTON_0. */
	public static final char GPIO_BUTTON_0 =
		0xE042;

	/** GPIO_BUTTON_MAX. */
	public static final char GPIO_BUTTON_MAX =
		0xE061;

	/** HALF_WIDTH. */
	public static final char HALF_WIDTH =
		0xE062;

	/** HELP. */
	public static final char HELP =
		0xE063;

	/** HIRAGANA. */
	public static final char HIRAGANA =
		0xE064;

	/** HOME. */
	public static final char HOME =
		0xE065;

	/** INPUT_METHOD_ON_OFF. */
	public static final char INPUT_METHOD_ON_OFF =
		0xE066;

	/** INSERT. */
	public static final char INSERT =
		0xE067;

	/** JAPANESE_HIRAGANA. */
	public static final char JAPANESE_HIRAGANA =
		0xE068;

	/** JAPANESE_KATAKANA. */
	public static final char JAPANESE_KATAKANA =
		0xE069;

	/** JAPANESE_ROMAN. */
	public static final char JAPANESE_ROMAN =
		0xE06A;

	/** JOYSTICK_DOWN. */
	public static final char JOYSTICK_DOWN =
		0xE06B;

	/** JOYSTICK_DOWN_SECONDARY. */
	public static final char JOYSTICK_DOWN_SECONDARY =
		0xE06C;

	/** JOYSTICK_FACE_BUTTON_0. */
	public static final char JOYSTICK_FACE_BUTTON_0 =
		0xE06D;

	/** JOYSTICK_FACE_BUTTON_MAX. */
	public static final char JOYSTICK_FACE_BUTTON_MAX =
		0xE08C;

	/** JOYSTICK_HOME. */
	public static final char JOYSTICK_HOME =
		0xE08D;

	/** JOYSTICK_LEFT. */
	public static final char JOYSTICK_LEFT =
		0xE08E;

	/** JOYSTICK_LEFT_SECONDARY. */
	public static final char JOYSTICK_LEFT_SECONDARY =
		0xE08F;

	/** JOYSTICK_LEFT_SHOULDER_0. */
	public static final char JOYSTICK_LEFT_SHOULDER_0 =
		0xE090;

	/** JOYSTICK_LEFT_SHOULDER_MAX. */
	public static final char JOYSTICK_LEFT_SHOULDER_MAX =
		0xE097;

	/** JOYSTICK_LEFT_TRIGGER_0. */
	public static final char JOYSTICK_LEFT_TRIGGER_0 =
		0xE098;

	/** JOYSTICK_LEFT_TRIGGER_MAX. */
	public static final char JOYSTICK_LEFT_TRIGGER_MAX =
		0xE09F;

	/** JOYSTICK_LIGHTGUN_HIT. */
	public static final char JOYSTICK_LIGHTGUN_HIT =
		0xE0A0;

	/** JOYSTICK_LIGHTGUN_MISSED. */
	public static final char JOYSTICK_LIGHTGUN_MISSED =
		0xE0A1;

	/** JOYSTICK_LIGHTGUN_RELOAD. */
	public static final char JOYSTICK_LIGHTGUN_RELOAD =
		0xE0A2;

	/** JOYSTICK_RIGHT. */
	public static final char JOYSTICK_RIGHT =
		0xE0A3;

	/** JOYSTICK_RIGHT_SECONDARY. */
	public static final char JOYSTICK_RIGHT_SECONDARY =
		0xE0A4;

	/** JOYSTICK_RIGHT_SHOULDER_0. */
	public static final char JOYSTICK_RIGHT_SHOULDER_0 =
		0xE0A5;

	/** JOYSTICK_RIGHT_SHOULDER_MAX. */
	public static final char JOYSTICK_RIGHT_SHOULDER_MAX =
		0xE0AC;

	/** JOYSTICK_RIGHT_TRIGGER_0. */
	public static final char JOYSTICK_RIGHT_TRIGGER_0 =
		0xE0AD;

	/** JOYSTICK_RIGHT_TRIGGER_MAX. */
	public static final char JOYSTICK_RIGHT_TRIGGER_MAX =
		0xE0B4;

	/** JOYSTICK_SELECT. */
	public static final char JOYSTICK_SELECT =
		0xE0B5;

	/** JOYSTICK_START. */
	public static final char JOYSTICK_START =
		0xE0B6;

	/** JOYSTICK_STICK_PUSH_0. */
	public static final char JOYSTICK_STICK_PUSH_0 =
		0xE0B7;

	/** JOYSTICK_STICK_PUSH_MAX. */
	public static final char JOYSTICK_STICK_PUSH_MAX =
		0xE0BE;

	/** JOYSTICK_TOGGLE_ANALOG_STICKS. */
	public static final char JOYSTICK_TOGGLE_ANALOG_STICKS =
		0xE0BF;

	/** JOYSTICK_UP. */
	public static final char JOYSTICK_UP =
		0xE0C0;

	/** JOYSTICK_UP_SECONDARY. */
	public static final char JOYSTICK_UP_SECONDARY =
		0xE0C1;

	/** KANA. */
	public static final char KANA =
		0xE0C2;

	/** KANA_LOCK. */
	public static final char KANA_LOCK =
		0xE0C3;

	/** KANJI. */
	public static final char KANJI =
		0xE0C4;

	/** KATAKANA. */
	public static final char KATAKANA =
		0xE0C5;

	/** KEYBOARD_BACKLIGHT_BRIGHTER. */
	public static final char KEYBOARD_BACKLIGHT_BRIGHTER =
		0xE0C6;

	/** KEYBOARD_BACKLIGHT_DIMMER. */
	public static final char KEYBOARD_BACKLIGHT_DIMMER =
		0xE0C7;

	/** KEYBOARD_BACKLIGHT_OFF. */
	public static final char KEYBOARD_BACKLIGHT_OFF =
		0xE0C8;

	/** KEYBOARD_BACKLIGHT_ON. */
	public static final char KEYBOARD_BACKLIGHT_ON =
		0xE0C9;

	/** KEYBOARD_BACKLIGHT_TOGGLE. */
	public static final char KEYBOARD_BACKLIGHT_TOGGLE =
		0xE0CA;

	/** KP_DOWN (Numeric Pad). */
	public static final char KP_DOWN =
		0xE0CB;

	/** KP_LEFT (Numeric Pad). */
	public static final char KP_LEFT =
		0xE0CC;

	/** KP_RIGHT (Numeric Pad). */
	public static final char KP_RIGHT =
		0xE0CD;

	/** KP_UP (Numeric Pad). */
	public static final char KP_UP =
		0xE0CE;

	/** LEFT. */
	public static final char LEFT =
		0xE0CF;

	/** LEFT_ALT. */
	public static final char LEFT_ALT =
		0xE0D0;

	/** LEFT_CONTROL. */
	public static final char LEFT_CONTROL =
		0xE0D1;

	/** LEFT_SHIFT. */
	public static final char LEFT_SHIFT =
		0xE0D2;

	/** LOGO. */
	public static final char LOGO =
		0xE0D3;

	/** MEDIA_CHANGE_ANGLE. */
	public static final char MEDIA_CHANGE_ANGLE =
		0xE0D4;

	/** MEDIA_CHANGE_AUDIO. */
	public static final char MEDIA_CHANGE_AUDIO =
		0xE0D5;

	/** MEDIA_CHANGE_SUBTITLE. */
	public static final char MEDIA_CHANGE_SUBTITLE =
		0xE0D6;

	/** MEDIA_CHANNEL_NEXT. */
	public static final char MEDIA_CHANNEL_NEXT =
		0xE0D7;

	/** MEDIA_CHANNEL_PREVIOUS. */
	public static final char MEDIA_CHANNEL_PREVIOUS =
		0xE0D8;

	/** MEDIA_FASTFORWARD. */
	public static final char MEDIA_FASTFORWARD =
		0xE0D9;

	/** MEDIA_INPUT. */
	public static final char MEDIA_INPUT =
		0xE0DA;

	/** MEDIA_PAUSE. */
	public static final char MEDIA_PAUSE =
		0xE0DB;

	/** MEDIA_PLAY. */
	public static final char MEDIA_PLAY =
		0xE0DC;

	/** MEDIA_PLAY_PAUSE_TOGGLE. */
	public static final char MEDIA_PLAY_PAUSE_TOGGLE =
		0xE0DD;

	/** MEDIA_RECORD. */
	public static final char MEDIA_RECORD =
		0xE0DE;

	/** MEDIA_REWIND. */
	public static final char MEDIA_REWIND =
		0xE0DF;

	/** MEDIA_STOP. */
	public static final char MEDIA_STOP =
		0xE0E0;

	/** META. */
	public static final char META =
		0xE0E1;

	/** MODECHANGE. */
	public static final char MODECHANGE =
		0xE0E2;

	/** MONTAGE_WINDOWS. */
	public static final char MONTAGE_WINDOWS =
		0xE0E3;

	/** PEN_EXTRA_BUTTON_0. */
	public static final char PEN_EXTRA_BUTTON_0 =
		0xE0E4;

	/** PEN_EXTRA_BUTTON_MAX. */
	public static final char PEN_EXTRA_BUTTON_MAX =
		0xE0EB;

	/** PEN_MIDDLE_CLICK. */
	public static final char PEN_MIDDLE_CLICK =
		0xE0EC;

	/** PEN_PRIMARY_CLICK. */
	public static final char PEN_PRIMARY_CLICK =
		0xE0ED;

	/** PEN_SCROLL_DOWN. */
	public static final char PEN_SCROLL_DOWN =
		0xE0EE;

	/** PEN_SCROLL_LEFT. */
	public static final char PEN_SCROLL_LEFT =
		0xE0EF;

	/** PEN_SCROLL_RIGHT. */
	public static final char PEN_SCROLL_RIGHT =
		0xE0F0;

	/** PEN_SCROLL_UP. */
	public static final char PEN_SCROLL_UP =
		0xE0F1;

	/** PEN_SECONDARY_CLICK. */
	public static final char PEN_SECONDARY_CLICK =
		0xE0F2;

	/** NONCONVERT. */
	public static final char NONCONVERT =
		0xE0F3;

	/** NUM_LOCK. */
	public static final char NUM_LOCK =
		0xE0F4;

	/** NUMPAD_0. */
	public static final char NUMPAD_0 =
		0xE0F5;

	/** NUMPAD_1. */
	public static final char NUMPAD_1 =
		0xE0F6;

	/** NUMPAD_2. */
	public static final char NUMPAD_2 =
		0xE0F7;

	/** NUMPAD_3. */
	public static final char NUMPAD_3 =
		0xE0F8;

	/** NUMPAD_4. */
	public static final char NUMPAD_4 =
		0xE0F9;

	/** NUMPAD_5. */
	public static final char NUMPAD_5 =
		0xE0FA;

	/** NUMPAD_6. */
	public static final char NUMPAD_6 =
		0xE0FB;

	/** NUMPAD_7. */
	public static final char NUMPAD_7 =
		0xE0FC;

	/** NUMPAD_8. */
	public static final char NUMPAD_8 =
		0xE0FD;

	/** NUMPAD_9. */
	public static final char NUMPAD_9 =
		0xE0FE;

	/** NUMPAD_ADD. */
	public static final char NUMPAD_ADD =
		0xE0FF;

	/** NUMPAD_ANSWER. */
	public static final char NUMPAD_ANSWER =
		0xE100;

	/** NUMPAD_DECIMAL. */
	public static final char NUMPAD_DECIMAL =
		0xE101;

	/** NUMPAD_DIVIDE. */
	public static final char NUMPAD_DIVIDE =
		0xE102;

	/** NUMPAD_ENTER. */
	public static final char NUMPAD_ENTER =
		0xE103;

	/** NUMPAD_MEMORY_CLEAR. */
	public static final char NUMPAD_MEMORY_CLEAR =
		0xE104;

	/** NUMPAD_MEMORY_RECALL. */
	public static final char NUMPAD_MEMORY_RECALL =
		0xE105;

	/** NUMPAD_MEMORY_STORE. */
	public static final char NUMPAD_MEMORY_STORE =
		0xE106;

	/** NUMPAD_MULTIPLY. */
	public static final char NUMPAD_MULTIPLY =
		0xE107;

	/** NUMPAD_NUMBER_SIGN. */
	public static final char NUMPAD_NUMBER_SIGN =
		0xE108;

	/** NUMPAD_PLUS_MINUS. */
	public static final char NUMPAD_PLUS_MINUS =
		0xE109;

	/** NUMPAD_REMAINDER. */
	public static final char NUMPAD_REMAINDER =
		0xE10A;

	/** NUMPAD_SEPARATOR. */
	public static final char NUMPAD_SEPARATOR =
		0xE10B;

	/** NUMPAD_SQUARE_ROOT. */
	public static final char NUMPAD_SQUARE_ROOT =
		0xE10C;

	/** NUMPAD_SUBTRACT. */
	public static final char NUMPAD_SUBTRACT =
		0xE10D;

	/** PAGE_DOWN. */
	public static final char PAGE_DOWN =
		0xE10E;

	/** PAGE_UP. */
	public static final char PAGE_UP =
		0xE10F;

	/** PASTE. */
	public static final char PASTE =
		0xE110;

	/** PAUSE. */
	public static final char PAUSE =
		0xE111;

	/** PREVIOUS_CANDIDATE. */
	public static final char PREVIOUS_CANDIDATE =
		0xE112;

	/** PRINTSCREEN. */
	public static final char PRINTSCREEN =
		0xE113;

	/** PROPS. */
	public static final char PROPS =
		0xE114;

	/** RIGHT. */
	public static final char RIGHT =
		0xE115;

	/** RIGHT_ALT. */
	public static final char RIGHT_ALT =
		0xE116;

	/** RIGHT_CONTROL. */
	public static final char RIGHT_CONTROL =
		0xE117;

	/** RIGHT_SHIFT. */
	public static final char RIGHT_SHIFT =
		0xE118;

	/** ROMAN_CHARACTERS. */
	public static final char ROMAN_CHARACTERS =
		0xE119;

	/** SCREEN_BACKLIGHT_BRIGHTER. */
	public static final char SCREEN_BACKLIGHT_BRIGHTER =
		0xE11A;

	/** SCREEN_BACKLIGHT_DIMMER. */
	public static final char SCREEN_BACKLIGHT_DIMMER =
		0xE11B;

	/** SCREEN_BACKLIGHT_OFF. */
	public static final char SCREEN_BACKLIGHT_OFF =
		0xE11C;

	/** SCREEN_BACKLIGHT_ON. */
	public static final char SCREEN_BACKLIGHT_ON =
		0xE11D;

	/** SCREEN_BACKLIGHT_TOGGLE. */
	public static final char SCREEN_BACKLIGHT_TOGGLE =
		0xE11E;

	/** SCREEN_BRIGHTNESS_DECREASE. */
	public static final char SCREEN_BRIGHTNESS_DECREASE =
		0xE11F;

	/** SCREEN_BRIGHTNESS_INCREASE. */
	public static final char SCREEN_BRIGHTNESS_INCREASE =
		0xE120;

	/** SCREEN_CONSTRACT_DECREASE. */
	public static final char SCREEN_CONSTRACT_DECREASE =
		0xE121;

	/** SCREEN_CONSTRAST_INCREASE. */
	public static final char SCREEN_CONSTRAST_INCREASE =
		0xE122;

	/** SCROLL_LOCK. */
	public static final char SCROLL_LOCK =
		0xE123;

	/** STOP. */
	public static final char STOP =
		0xE124;

	/** SWITCH_DISPLAYS. */
	public static final char SWITCH_DISPLAYS =
		0xE125;

	/** SYSRQ. */
	public static final char SYSRQ =
		0xE126;

	/** TAB. */
	public static final char TAB =
		0xE127;

	/** TOGGLE_DOCK. */
	public static final char TOGGLE_DOCK =
		0xE128;

	/** UNDO. */
	public static final char UNDO =
		0xE129;

	/** UP. */
	public static final char UP =
		0xE12A;

	/** VOLUME_BASSBOOST_TOGGLE. */
	public static final char VOLUME_BASSBOOST_TOGGLE =
		0xE12B;

	/** VOLUME_LOUDER. */
	public static final char VOLUME_LOUDER =
		0xE12C;

	/** VOLUME_MUTE_TOGGLE. */
	public static final char VOLUME_MUTE_TOGGLE =
		0xE12D;

	/** VOLUME_SOFTER. */
	public static final char VOLUME_SOFTER =
		0xE12E;

	/** WEB_BROWSER. */
	public static final char WEB_BROWSER =
		0xE12F;
}

