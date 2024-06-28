/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI events.
 * 
 * @since 2024/06/28
 */

#ifndef SQUIRRELJME_SCRITCHUIEVENT_H
#define SQUIRRELJME_SCRITCHUIEVENT_H

#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIEVENT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The types of events that are possible.
 * 
 * @since 2024/06/28
 */
typedef enum sjme_scritchui_eventType
{
	/** Unknown event. */
	SJME_SCRITCHUI_EVENT_TYPE_UNKNOWN = 0,
	
	/** Key event: Pressed. */
	SJME_SCRITCHUI_EVENT_TYPE_KEY_PRESSED = 1,
	
	/** Key event: Released. */
	SJME_SCRITCHUI_EVENT_TYPE_KEY_RELEASED = 2,
	
	/** Key event: Repeated. */
	SJME_SCRITCHUI_EVENT_TYPE_KEY_REPEATED = 4,
	
	/** Mouse event: Button pressed. */
	SJME_SCRITCHUI_EVENT_TYPE_MOUSE_BUTTON_PRESSED = 8,
	
	/** Mouse event: Button released. */
	SJME_SCRITCHUI_EVENT_TYPE_MOUSE_BUTTON_RELEASED = 16,
	
	/** Mouse event: Motion. */
	SJME_SCRITCHUI_EVENT_TYPE_MOUSE_MOTION = 32,
	
	/** Gamepad event: Button pressed. */
	SJME_SCRITCHUI_EVENT_TYPE_GAMEPAD_BUTTON_PRESSED = 64,
	
	/** Gamepad event: Button released. */
	SJME_SCRITCHUI_EVENT_TYPE_GAMEPAD_BUTTON_RELEASED = 128,
	
	/** Gamepad event: Motion on axis. */
	SJME_SCRITCHUI_EVENT_TYPE_GAMEPAD_AXIS_MOTION = 256,
	
	/** Touch event: Finger pressed. */
	SJME_SCRITCHUI_EVENT_TYPE_TOUCH_FINGER_PRESSED = 512,
	
	/** Touch event: Finger released. */
	SJME_SCRITCHUI_EVENT_TYPE_TOUCH_FINGER_RELEASED = 1024,
	
	/** Touch event: Drag motion. */
	SJME_SCRITCHUI_EVENT_TYPE_TOUCH_DRAG_MOTION = 2048,
	
	/** Stylus event: Pressed. */
	SJME_SCRITCHUI_EVENT_TYPE_STYLUS_PEN_PRESSED = 4096,
	
	/** Stylus event: Released. */
	SJME_SCRITCHUI_EVENT_TYPE_STYLUS_PEN_RELEASED = 8192,
	
	/** Stylus event: Dragging motion. */
	SJME_SCRITCHUI_EVENT_TYPE_STYLUS_DRAG_MOTION = 16384,
	
	/** Stylus event: Hovering over display. */
	SJME_SCRITCHUI_EVENT_TYPE_STYLUS_HOVER_MOTION = 32768,
	
	/** Gyroscope event: Axis motion. */
	SJME_SCRITCHUI_EVENT_TYPE_GYRO_AXIS_MOTION = 65536,
	
	/** Device action (flip open/close, shaken, not stirred). */
	SJME_SCRITCHUI_EVENT_TYPE_DEVICE_ACTION = 131072,
} sjme_scritchui_eventType;

typedef struct sjme_scritchui_eventDataKey
{
} sjme_scritchui_eventDataKey;

typedef struct sjme_scritchui_eventDataMouseButton
{
} sjme_scritchui_eventDataMouseButton;

typedef struct sjme_scritchui_eventDataMouseMotion
{
} sjme_scritchui_eventDataMouseMotion;

typedef struct sjme_scritchui_eventDataGamePadButton
{
} sjme_scritchui_eventDataGamePadButton;

typedef struct sjme_scritchui_eventDataGamePadAxisMotion
{
} sjme_scritchui_eventDataGamePadAxisMotion;

typedef struct sjme_scritchui_eventDataTouchFinger
{
} sjme_scritchui_eventDataTouchFinger;

typedef struct sjme_scritchui_eventDataTouchDrag
{
} sjme_scritchui_eventDataTouchDrag;

typedef struct sjme_scritchui_eventDataStylusPen
{
} sjme_scritchui_eventDataStylusPen;

typedef struct sjme_scritchui_eventDataStylusMotion
{
} sjme_scritchui_eventDataStylusMotion;

typedef struct sjme_scritchui_eventDataDeviceAction
{
} sjme_scritchui_eventDataDeviceAction;

/**
 * Structure which contains event data and otherwise.
 * 
 * @since 2024/06/28
 */
typedef struct sjme_scritchui_event
{
	/** The type of event that has occurred. */
	sjme_scritchui_eventType type;
	
	/** The time the event occurred at. */
	sjme_jlong time;
	
	/** The event data. */
	union
	{
		/** Key events. */
		sjme_scritchui_eventDataKey key;
		
		/** Mouse button events. */
		sjme_scritchui_eventDataMouseButton mouseButton;
		
		/** Mouse motion events. */
		sjme_scritchui_eventDataMouseMotion mouseMotion;
		
		/** Game pad button. */
		sjme_scritchui_eventDataGamePadButton gamePadButton;
		
		/** Game pad axis motion. */
		sjme_scritchui_eventDataGamePadAxisMotion gamePadAxisMotion;
		
		/** Touchscreen/Touchpad finger. */
		sjme_scritchui_eventDataTouchFinger touchFinger;
		
		/** Touch drag. */
		sjme_scritchui_eventDataTouchDrag touchDrag;
		
		/** Stylus pen. */
		sjme_scritchui_eventDataStylusPen stylusPen;
		
		/** Stylus motion. */
		sjme_scritchui_eventDataStylusMotion stylusMotion;
		
		/** Device action. */
		sjme_scritchui_eventDataDeviceAction deviceAction;
	} data;
} sjme_scritchui_event;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIEVENT_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIEVENT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIEVENT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIEVENT_H */
