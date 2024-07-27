/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Scritch Input Library.
 * 
 * @since 2024/05/01
 */

#ifndef SQUIRRELJME_SCRITCHINPUT_H
#define SQUIRRELJME_SCRITCHINPUT_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHINPUT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The types of events that are possible.
 * 
 * @since 2024/06/28
 */
typedef enum sjme_scritchinput_type
{
	/** Unknown event. */
	SJME_SCRITCHINPUT_TYPE_UNKNOWN = 0,
	
	/** Key event: Pressed. */
	SJME_SCRITCHINPUT_TYPE_KEY_PRESSED = 1,
	
	/** Key event: Released. */
	SJME_SCRITCHINPUT_TYPE_KEY_RELEASED = 2,
	
	/** Key event: Repeated. */
	SJME_SCRITCHINPUT_TYPE_KEY_REPEATED = 4,
	
	/** Mouse event: Button pressed. */
	SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED = 8,
	
	/** Mouse event: Button released. */
	SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED = 16,
	
	/** Mouse event: Motion. */
	SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION = 32,
	
	/** Gamepad event: Button pressed. */
	SJME_SCRITCHINPUT_TYPE_GAMEPAD_BUTTON_PRESSED = 64,
	
	/** Gamepad event: Button released. */
	SJME_SCRITCHINPUT_TYPE_GAMEPAD_BUTTON_RELEASED = 128,
	
	/** Gamepad event: Motion on axis. */
	SJME_SCRITCHINPUT_TYPE_GAMEPAD_AXIS_MOTION = 256,
	
	/** Touch event: Finger pressed. */
	SJME_SCRITCHINPUT_TYPE_TOUCH_FINGER_PRESSED = 512,
	
	/** Touch event: Finger released. */
	SJME_SCRITCHINPUT_TYPE_TOUCH_FINGER_RELEASED = 1024,
	
	/** Touch event: Drag motion. */
	SJME_SCRITCHINPUT_TYPE_TOUCH_DRAG_MOTION = 2048,
	
	/** Stylus event: Pressed. */
	SJME_SCRITCHINPUT_TYPE_STYLUS_PEN_PRESSED = 4096,
	
	/** Stylus event: Released. */
	SJME_SCRITCHINPUT_TYPE_STYLUS_PEN_RELEASED = 8192,
	
	/** Stylus event: Dragging motion. */
	SJME_SCRITCHINPUT_TYPE_STYLUS_DRAG_MOTION = 16384,
	
	/** Stylus event: Hovering over display. */
	SJME_SCRITCHINPUT_TYPE_STYLUS_HOVER_MOTION = 32768,
	
	/** Gyroscope event: Axis motion. */
	SJME_SCRITCHINPUT_TYPE_GYRO_AXIS_MOTION = 65536,
	
	/** Device action (flip open/close, shaken, not stirred). */
	SJME_SCRITCHINPUT_TYPE_DEVICE_ACTION = 131072,
} sjme_scritchinput_type;

typedef enum sjme_scritchinput_key
{	
	/** Backspace. */
	SJME_SCRITCHINPUT_KEY_BACKSPACE = 8,
		
	/** Tab. */
	SJME_SCRITCHINPUT_KEY_TAB = 9,
	
	/** Enter. */
	SJME_SCRITCHINPUT_KEY_ENTER = 10,
		
	/** Escape. */
	SJME_SCRITCHINPUT_KEY_ESCAPE = 27,
	
	/** Star key. */
	SJME_SCRITCHINPUT_KEY_STAR = 42,
	
	/** Space key. */
	SJME_SCRITCHINPUT_KEY_SPACE = 32,
	
	/** Pound key. */
	SJME_SCRITCHINPUT_KEY_POUND = 35,
	
	/** Delete key. */
	SJME_SCRITCHINPUT_KEY_DELETE = 127,
	
	/** Unknown, zero is the invalid index so always make it known. */
	SJME_SCRITCHINPUT_KEY_UNKNOWN = 0,
	
	/** The up arrow key. */
	SJME_SCRITCHINPUT_KEY_UP = -1,
	
	/** Down arrow key. */
	SJME_SCRITCHINPUT_KEY_DOWN = -2,
	
	/** Left arrow key. */
	SJME_SCRITCHINPUT_KEY_LEFT = -3,
	
	/** Right arrow key. */
	SJME_SCRITCHINPUT_KEY_RIGHT = -4,
	
	/** Game Up. */
	SJME_SCRITCHINPUT_KEY_VGAME_UP = -9,
	
	/** Game Down. */
	SJME_SCRITCHINPUT_KEY_VGAME_DOWN = -10,
	
	/** Game Left. */
	SJME_SCRITCHINPUT_KEY_VGAME_LEFT = -11,
	
	/** Game Right. */
	SJME_SCRITCHINPUT_KEY_VGAME_RIGHT = -12,
	
	/** Game fire. */
	SJME_SCRITCHINPUT_KEY_VGAME_FIRE = -13,
	
	/** Game A. */
	SJME_SCRITCHINPUT_KEY_VGAME_A = -14,
	
	/** Game B. */
	SJME_SCRITCHINPUT_KEY_VGAME_B = -15,
	
	/** Game C. */
	SJME_SCRITCHINPUT_KEY_VGAME_C = -16,
	
	/** Game D. */
	SJME_SCRITCHINPUT_KEY_VGAME_D = -17,
	
	/** Shift. */
	SJME_SCRITCHINPUT_KEY_SHIFT = -18,
	
	/** Control. */
	SJME_SCRITCHINPUT_KEY_CONTROL = -19,
	
	/** Alt. */
	SJME_SCRITCHINPUT_KEY_ALT = -20,
	
	/** Logo. */
	SJME_SCRITCHINPUT_KEY_LOGO = -21,
	
	/** Caps lock. */
	SJME_SCRITCHINPUT_KEY_CAPSLOCK = -22,
	
	/** Context menu. */
	SJME_SCRITCHINPUT_KEY_CONTEXT_MENU = -23,
	
	/** Home. */
	SJME_SCRITCHINPUT_KEY_HOME = -24,
	
	/** End. */
	SJME_SCRITCHINPUT_KEY_END = -25,
	
	/** Page Up. */
	SJME_SCRITCHINPUT_KEY_PAGE_UP = -26,
	
	/** Page Down. */
	SJME_SCRITCHINPUT_KEY_PAGE_DOWN = -27,
	
	/** Meta. */
	SJME_SCRITCHINPUT_KEY_META = -28,
	
	/** Numlock. */
	SJME_SCRITCHINPUT_KEY_NUMLOCK = -29,
	
	/** Pause. */
	SJME_SCRITCHINPUT_KEY_PAUSE = -30,
	
	/** Print Screen. */
	SJME_SCRITCHINPUT_KEY_PRINTSCREEN = -31,
	
	/** Scroll lock. */
	SJME_SCRITCHINPUT_KEY_SCROLLLOCK = -32,
	
	/** Insert. */
	SJME_SCRITCHINPUT_KEY_INSERT = -33,
	
	/** Game Virtual Left Command. */
	SJME_SCRITCHINPUT_KEY_VGAME_COMMAND_LEFT = -34,
	
	/** Game Virtual Right Command. */
	SJME_SCRITCHINPUT_KEY_VGAME_COMMAND_RIGHT = -35,
	
	/** Game virtual Center Command. */
	SJME_SCRITCHINPUT_KEY_VGAME_COMMAND_CENTER = -36,
	
	/** Game virtual open LCDUI inspector. */
	SJME_SCRITCHINPUT_KEY_VGAME_LCDUI_INSPECTOR = -37,
	
	/** Number pad divide. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_DIVIDE = -38,
	
	/** Number pad multiply. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_MULTIPLY = -39,
	
	/** Number pad minus. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_MINUS = -40,
	
	/** Number pad plus. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_PLUS = -41,
	
	/** Number pad decimal. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_DECIMAL = -42,
	
	/** Number pad enter. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_ENTER = -43,
	
	/** Number pad 0. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_0 = -50,
	
	/** Number pad 1. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_1 = -51,
	
	/** Number pad 2. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_2 = -52,
	
	/** Number pad 3. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_3 = -53,
	
	/** Number pad 4. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_4 = -54,
	
	/** Number pad 5. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_5 = -55,
	
	/** Number pad 6. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_6 = -56,
	
	/** Number pad 7. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_7 = -57,
	
	/** Number pad 8. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_8 = -58,
	
	/** Number pad 9. */
	SJME_SCRITCHINPUT_KEY_NUMPAD_9 = -59,
	
	/** F24. */
	SJME_SCRITCHINPUT_KEY_F24 = -64,
	
	/** F23. */
	SJME_SCRITCHINPUT_KEY_F23 = -65,
	
	/** F22. */
	SJME_SCRITCHINPUT_KEY_F22 = -66,
	
	/** F21. */
	SJME_SCRITCHINPUT_KEY_F21 = -67,
	
	/** F20. */
	SJME_SCRITCHINPUT_KEY_F20 = -68,
	
	/** F19. */
	SJME_SCRITCHINPUT_KEY_F19 = -69,
	
	/** F18. */
	SJME_SCRITCHINPUT_KEY_F18 = -70,
	
	/** F17. */
	SJME_SCRITCHINPUT_KEY_F17 = -71,
	
	/** F16. */
	SJME_SCRITCHINPUT_KEY_F16 = -72,
	
	/** F15. */
	SJME_SCRITCHINPUT_KEY_F15 = -73,
	
	/** F14. */
	SJME_SCRITCHINPUT_KEY_F14 = -74,
	
	/** F13. */
	SJME_SCRITCHINPUT_KEY_F13 = -75,
	
	/** F12. */
	SJME_SCRITCHINPUT_KEY_F12 = -76,
	
	/** F11. */
	SJME_SCRITCHINPUT_KEY_F11 = -77,
	
	/** F10. */
	SJME_SCRITCHINPUT_KEY_F10 = -78,
	
	/** F9. */
	SJME_SCRITCHINPUT_KEY_F9 = -79,
	
	/** F8. */
	SJME_SCRITCHINPUT_KEY_F8 = -80,
	
	/** F7. */
	SJME_SCRITCHINPUT_KEY_F7 = -81,
	
	/** F6. */
	SJME_SCRITCHINPUT_KEY_F6 = -82,
	
	/** F5. */
	SJME_SCRITCHINPUT_KEY_F5 = -83,
	
	/** F4. */
	SJME_SCRITCHINPUT_KEY_F4 = -84,
	
	/** F3. */
	SJME_SCRITCHINPUT_KEY_F3 = -85,
	
	/** F2. */
	SJME_SCRITCHINPUT_KEY_F2 = -86,
	
	/** F1. */
	SJME_SCRITCHINPUT_KEY_F1 = -87,
} sjme_scritchinput_key;

typedef enum sjme_scritchinput_modifier
{
	/** Alt key modifier. */
	SJME_SCRITCHINPUT_MODIFIER_ALT = 65536,
		
	/** Function (Fn/Chr) key modifier. */
	SJME_SCRITCHINPUT_MODIFIER_CHR = 8388608,
	
	/** Command key modifier. */
	SJME_SCRITCHINPUT_MODIFIER_COMMAND = 4194304,
	
	/** Ctrl key modifier. */
	SJME_SCRITCHINPUT_MODIFIER_CTRL = 262144,
	
	/** Shift key modifier. */
	SJME_SCRITCHINPUT_MODIFIER_SHIFT = 131072,
	
	/** Mask for all the modifier keys. */
	SJME_SCRITCHINPUT_MODIFIER_MASK = 13041664,
} sjme_scritchinput_modifier;

typedef struct sjme_scritchinput_eventDataUnknown
{
	/** Value 1. */
	sjme_jint a;
	
	/** Value 2. */
	sjme_jint b;
	
	/** Value 3. */
	sjme_jint c;
	
	/** Value 4. */
	sjme_jint d;
	
	/** Value 5. */
	sjme_jint e;
	
	/** Value 6. */
	sjme_jint f;
	
	/** Value 7. */
	sjme_jint g;
	
	/** Value 8. */
	sjme_jint h;
	
	/** Value 9. */
	sjme_jint i;
	
	/** Value 10. */
	sjme_jint j;
	
	/** Value 11. */
	sjme_jint k;
	
	/** Value 12. */
	sjme_jint l;
} sjme_scritchinput_eventDataUnknown;

/**
 * Event data for key events.
 * 
 * @since 2024/06/30
 */
typedef struct sjme_scritchinput_eventDataKey
{
	/** Unicode key core or @c sjme_scritchinput_key . */
	sjme_jint code;
	
	/** The modifiers held down, from @c sjme_scritchinput_modifier . */
	sjme_jint modifiers;
} sjme_scritchinput_eventDataKey;

typedef struct sjme_scritchinput_eventDataMouseButton
{
	/** The button being pressed. */
	sjme_jint button;
	
	/** The modifiers held down, from @c sjme_scritchinput_modifier . */
	sjme_jint modifiers;
	
	/** The X coordinate. */
	sjme_jint x;
	
	/** The Y coordinate. */
	sjme_jint y;
} sjme_scritchinput_eventDataMouseButton;

typedef struct sjme_scritchinput_eventDataMouseMotion
{
	/** The buttons being pressed. */
	sjme_jint buttonMask;
	
	/** The modifiers held down, from @c sjme_scritchinput_modifier . */
	sjme_jint modifiers;
	
	/** The X coordinate. */
	sjme_jint x;
	
	/** The Y coordinate. */
	sjme_jint y;
} sjme_scritchinput_eventDataMouseMotion;

typedef struct sjme_scritchinput_eventDataGamePadButton
{
} sjme_scritchinput_eventDataGamePadButton;

typedef struct sjme_scritchinput_eventDataGamePadAxisMotion
{
} sjme_scritchinput_eventDataGamePadAxisMotion;

typedef struct sjme_scritchinput_eventDataTouchFinger
{
} sjme_scritchinput_eventDataTouchFinger;

typedef struct sjme_scritchinput_eventDataTouchDrag
{
} sjme_scritchinput_eventDataTouchDrag;

typedef struct sjme_scritchinput_eventDataStylusPen
{
} sjme_scritchinput_eventDataStylusPen;

typedef struct sjme_scritchinput_eventDataStylusMotion
{
} sjme_scritchinput_eventDataStylusMotion;

typedef struct sjme_scritchinput_eventDataDeviceAction
{
} sjme_scritchinput_eventDataDeviceAction;

/**
 * Structure which contains event data and otherwise.
 * 
 * @since 2024/06/28
 */
typedef struct sjme_scritchinput_event
{
	/** The type of event that has occurred. */
	sjme_scritchinput_type type;
	
	/** The time the event occurred at. */
	sjme_jlong time;
	
	/** The event data. */
	union
	{
		/** Unknown event. */
		sjme_scritchinput_eventDataUnknown unknown;
		
		/** Key events. */
		sjme_scritchinput_eventDataKey key;
		
		/** Mouse button events. */
		sjme_scritchinput_eventDataMouseButton mouseButton;
		
		/** Mouse motion events. */
		sjme_scritchinput_eventDataMouseMotion mouseMotion;
		
		/** Game pad button. */
		sjme_scritchinput_eventDataGamePadButton gamePadButton;
		
		/** Game pad axis motion. */
		sjme_scritchinput_eventDataGamePadAxisMotion gamePadAxisMotion;
		
		/** Touchscreen/Touchpad finger. */
		sjme_scritchinput_eventDataTouchFinger touchFinger;
		
		/** Touch drag. */
		sjme_scritchinput_eventDataTouchDrag touchDrag;
		
		/** Stylus pen. */
		sjme_scritchinput_eventDataStylusPen stylusPen;
		
		/** Stylus motion. */
		sjme_scritchinput_eventDataStylusMotion stylusMotion;
		
		/** Device action. */
		sjme_scritchinput_eventDataDeviceAction deviceAction;
	} data;
} sjme_scritchinput_event;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHINPUT_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHINPUT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHINPUT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHINPUT_H */
