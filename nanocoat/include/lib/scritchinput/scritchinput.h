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

typedef struct sjme_scritchinput_eventDataKey
{
} sjme_scritchinput_eventDataKey;

typedef struct sjme_scritchinput_eventDataMouseButton
{
} sjme_scritchinput_eventDataMouseButton;

typedef struct sjme_scritchinput_eventDataMouseMotion
{
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
