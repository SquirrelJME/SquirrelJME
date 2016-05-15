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
	
	/** Select. */
	public static final int SELECT =
		0xE12A;
	
	/** Start. */
	public static final int START =
		0xE12B;
	
	/** Power on. */
	public static final int POWER_ON =
		0xE12C;
	
	/** Power off. */
	public static final int POWER_OFF =
		0xE12D;
	
	/** Sleep. */
	public static final int POWER_SLEEP =
		0xE12E;
	
	/** HotSync Button. */
	public static final int HOTSYNC =
		0xE12F;
	
	/** Mouse button 1. */
	public static final int MOUSE_BUTTON_1 =
		0xE130;
	
	/** Mouse button 2. */
	public static final int MOUSE_BUTTON_2 =
		0xE131;
	
	/** Mouse button 3. */
	public static final int MOUSE_BUTTON_3 =
		0xE132;
	
	/** Mouse button 4. */
	public static final int MOUSE_BUTTON_4 =
		0xE133;
	
	/** Mouse button 5. */
	public static final int MOUSE_BUTTON_5 =
		0xE134;
	
	/** Mouse button 6. */
	public static final int MOUSE_BUTTON_6 =
		0xE135;
	
	/** Mouse button 7. */
	public static final int MOUSE_BUTTON_7 =
		0xE136;
	
	/** Mouse button 8. */
	public static final int MOUSE_BUTTON_8 =
		0xE137;
	
	/** Mouse button 9. */
	public static final int MOUSE_BUTTON_9 =
		0xE138;
	
	/** Mouse button 10. */
	public static final int MOUSE_BUTTON_10 =
		0xE139;
	
	/** Mouse button 11. */
	public static final int MOUSE_BUTTON_11 =
		0xE13A;
	
	/** Mouse button 12. */
	public static final int MOUSE_BUTTON_12 =
		0xE13B;
	
	/** Mouse button 13. */
	public static final int MOUSE_BUTTON_13 =
		0xE13C;
	
	/** Mouse button 14. */
	public static final int MOUSE_BUTTON_14 =
		0xE13D;
	
	/** Mouse button 15. */
	public static final int MOUSE_BUTTON_15 =
		0xE13E;
	
	/** Mouse button 16. */
	public static final int MOUSE_BUTTON_16 =
		0xE13F;
	
	/** Joystick button 1. */
	public static final int JOYSTICK_BUTTON_1 =
		0xE140;
	
	/** Joystick button 2. */
	public static final int JOYSTICK_BUTTON_2 =
		0xE141;
	
	/** Joystick button 3. */
	public static final int JOYSTICK_BUTTON_3 =
		0xE142;
	
	/** Joystick button 4. */
	public static final int JOYSTICK_BUTTON_4 =
		0xE143;
	
	/** Joystick button 5. */
	public static final int JOYSTICK_BUTTON_5 =
		0xE144;
	
	/** Joystick button 6. */
	public static final int JOYSTICK_BUTTON_6 =
		0xE145;
	
	/** Joystick button 7. */
	public static final int JOYSTICK_BUTTON_7 =
		0xE146;
	
	/** Joystick button 8. */
	public static final int JOYSTICK_BUTTON_8 =
		0xE147;
	
	/** Joystick button 9. */
	public static final int JOYSTICK_BUTTON_9 =
		0xE148;
	
	/** Joystick button 10. */
	public static final int JOYSTICK_BUTTON_10 =
		0xE149;
	
	/** Joystick button 11. */
	public static final int JOYSTICK_BUTTON_11 =
		0xE14A;
	
	/** Joystick button 12. */
	public static final int JOYSTICK_BUTTON_12 =
		0xE14B;
	
	/** Joystick button 13. */
	public static final int JOYSTICK_BUTTON_13 =
		0xE14C;
	
	/** Joystick button 14. */
	public static final int JOYSTICK_BUTTON_14 =
		0xE14D;
	
	/** Joystick button 15. */
	public static final int JOYSTICK_BUTTON_15 =
		0xE14E;
	
	/** Joystick button 16. */
	public static final int JOYSTICK_BUTTON_16 =
		0xE14F;
	
	/** Left trigger. */
	public static final int LEFT_TRIGGER =
		0xE150;
	
	/** Right trigger. */
	public static final int RIGHT_TRIGGER =
		0xE151;
	
	/** Secondary left trigger. */
	public static final int SECONDARY_LEFT_TRIGGER =
		0xE152;
	
	/** Secondary right trigger. */
	public static final int SECONDARY_RIGHT_TRIGGER =
		0xE153;
	
	/** Eject ammunition. */
	public static final int EJECT_AMMUNITION =
		0xE154;
	
	/** Secondary eject ammunition. */
	public static final int SECONDARY_EJECT_AMMUNITION =
		0xE155;
	
	/** Toggle fire mode. */
	public static final int TOGGLE_FIRE_MODE =
		0xE156;
	
	/** Use burst fire mode. */
	public static final int BURST_FIRE_MODE =
		0xE157;
	
	/** Use single shot fire mode. */
	public static final int SINGLE_SHOT_FIRE_MODE =
		0xE158;
	
	/** Use semi-automatic fire mode. */
	public static final int SEMI_AUTOMATIC_FIRE_MODE =
		0xE159;
	
	/** Use automatic fire mode. */
	public static final int AUTOMATIC_FIRE_MODE =
		0xE15A;
	
	/** Brew liquid. */
	public static final int BREW_LIQUID =
		0xE160;
	
	/** Brew Hotter. */
	public static final int BREW_HOTTER =
		0xE161;
	
	/** Brew Colder. */
	public static final int BREW_COLDER =
		0xE162;
	
	/** Toggle fan. */
	public static final int FAN_TOGGLE =
		0xE163;
	
	/** Fan off. */
	public static final int FAN_OFF =
		0xE164;
	
	/** Fan on. */
	public static final int FAN_ON =
		0xE165;
	
	/** Fan speed increase. */
	public static final int FAN_SPEED_INCREASE =
		0xE166;
	
	/** Fan speed decrease. */
	public static final int FAN_SPEED_DECREASE =
		0xE167;
	
	/** Air conditioner toggle. */
	public static final int AC_TOGGLE =
		0xE168;
	
	/** Air conditioner on. */
	public static final int AC_ON =
		0xE169;
	
	/** Air conditioner off. */
	public static final int AC_OFF =
		0xE16A;
	
	/** Air conditioner colder. */
	public static final int AC_COLDER =
		0xE16B;
	
	/** Air conditioner hotter. */
	public static final int AC_HOTTER =
		0xE16C;
	
	/** Air conditioner, toggle heat mode. */
	public static final int AC_TOGGLE_HEAT =
		0xE16D;
	
	/** Air conditioner, heater on. */
	public static final int AC_HEATER_ON =
		0xE16E;
	
	/** Air conditioner, heater off. */
	public static final int AC_HEATER_OFF =
		0xE16F;
	
	/** Heater toggle. */
	public static final int HEATER_TOGGLE =
		0xE170;
	
	/** Heater on. */
	public static final int HEATER_ON =
		0xE171;
	
	/** Heater off. */
	public static final int HEATER_OFF =
		0xE172;
	
	/** Heater hotter. */
	public static final int HEATER_HOTTER =
		0xE173;
	
	/** Heater colder. */
	public static final int HEATER_COLDER =
		0xE174;
	
	/** Washer fluid. */
	public static final int WASHER_FLUID =
		0xE175;
	
	/** Secondary washer fluid. */
	public static final int SECONDARY_WASHER_FLUID =
		0xE176;
	
	/** Front wiper toggle. */
	public static final int FRONT_WIPER_TOGGLE =
		0xE177;
	
	/** Front wiper on. */
	public static final int FRONT_WIPER_ON =
		0xE178;
	
	/** Front wiper off. */
	public static final int FRONT_WIPER_OFF =
		0xE179;
	
	/** Front wiper faster. */
	public static final int FRONT_WIPER_FASTER =
		0xE17A;
	
	/** Front wiper slower. */
	public static final int FRONT_WIPER_SLOWER =
		0xE17B;
	
	/** Front wiper long pause. */
	public static final int FRONT_WIPER_LONGER_PAUSE =
		0xE17C;
	
	/** Front wiper shorter pause. */
	public static final int FRONT_WIPER_SHORTER_PAUSE =
		0xE17D;
	
	/** Back wiper toggle. */
	public static final int BACK_WIPER_TOGGLE =
		0xE17E;
	
	/** Back wiper on. */
	public static final int BACK_WIPER_ON =
		0xE17F;
	
	/** Back wiper off. */
	public static final int BACK_WIPER_OFF =
		0xE180;
	
	/** Back wiper faster. */
	public static final int BACK_WIPER_FASTER =
		0xE181;
	
	/** Back wiper slower. */
	public static final int BACK_WIPER_SLOWER =
		0xE182;
	
	/** Back wiper long pause. */
	public static final int BACK_WIPER_LONGER_PAUSE =
		0xE183;
	
	/** Back wiper shorter pause. */
	public static final int BACK_WIPER_SHORTER_PAUSE =
		0xE184;
	
	/** Turn signal left toggle. */
	public static final int TURN_SIGNAL_LEFT_TOGGLE =
		0xE185;
	
	/** Turn signal left on. */
	public static final int TURN_SIGNAL_LEFT_ON =
		0xE186;
	
	/** Turn signal left off. */
	public static final int TURN_SIGNAL_LEFT_OFF =
		0xE187;
	
	/** Turn signal right toggle. */
	public static final int TURN_SIGNAL_RIGHT_TOGGLE =
		0xE188;
	
	/** Turn signal right on. */
	public static final int TURN_SIGNAL_RIGHT_ON =
		0xE189;
	
	/** Turn signal right off. */
	public static final int TURN_SIGNAL_RIGHT_OFF =
		0xE18A;
	
	/** Hazard lights toggle. */
	public static final int HAZARD_LIGHTS_TOGGLE =
		0xE18B;
	
	/** Hazard lights on. */
	public static final int HAZARD_LIGHTS_ON =
		0xE18C;
	
	/** Hazard lights off. */
	public static final int HAZARD_LIGHTS_OFF =
		0xE18D;
	
	/** Defroster toggle. */
	public static final int DEFROSTER_TOGGLE =
		0xE18E;
	
	/** Defroster on. */
	public static final int DEFROSTER_ON =
		0xE18F;
	
	/** Defroster off. */
	public static final int DEFROSTER_OFF =
		0xE190;
	
	/** Eject tape A. */
	public static final int EJECT_TAPE_A =
		0xE191;
	
	/** Eject tape B. */
	public static final int EJECT_TAPE_B =
		0xE192;
	
	/** End of buttons. */
	public static final int END_OF_LIST =
		0xF8FF;
}

