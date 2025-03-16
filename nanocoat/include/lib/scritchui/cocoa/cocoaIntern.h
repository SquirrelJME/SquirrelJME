/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal Cocoa header.
 * 
 * @since 2024/08/15
 */

#ifndef SQUIRRELJME_COCOAINTERN_H
#define SQUIRRELJME_COCOAINTERN_H

#import <objc/Object.h>

#include "lib/scritchui/cocoa/cocoa.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COCOAINTERN_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Notification for loop execution. */
extern NSString* const sjme_scritchui_cocoa_loopExecuteNotif;

/**
 * SquirrelJME loop execute data.
 * 
 * @since 2024/08/16
 */
@interface SJMELoopExecute : NSObject
{
@public
	/** The state this is for. */
	sjme_scritchui scritchState;
	
	/** The callback to execute. */
	sjme_thread_mainFunc callback;
	
	/** The thread parameter. */
	sjme_thread_parameter anything;
}
@end

/**
 * Menu which holds menus and menu items.
 * 
 * @since 2024/08/17
 */
@interface SJMEMenu : NSMenu
{
@public
	/** The menu kind this is attached to. */
	sjme_scritchui_uiMenuKind scritchMenuKind;
}

@end

/**
 * Menu item which contains an action.
 * 
 * @since 2024/10/11
 */
@interface SJMEMenuItem : NSMenuItem
{
@public
	/** The menu kind this is attached to. */
	sjme_scritchui_uiMenuKind scritchMenuKind;
}

@end

/**
 * Panel object. This is based on @c NSView as that is a low level way to
 * create custom widgets and otherwise.
 * 
 * @since 2024/08/17
 */
@interface SJMEPanel : NSView
{
@public
	/** The panel this is attached to. */
	sjme_scritchui_uiPanel scritchPanel;
}

@end

/**
 * SquirrelJME Super Object.
 * 
 * @since 2024/08/16
 */
@interface SJMESuperObject : NSObject
{
}

+ (void)postNotification:(NSNotification *)notif;

@end

/**
 * Represents a window.
 * 
 * @since 2024/10/11
 */
@interface SJMEWindow : NSWindow
{
@public
	/** The window this is attached to. */
	sjme_scritchui_uiWindow scritchWindow;
}

@end

/**
 * Determines if there has been an error within Cocoa.
 * 
 * @param inState The input state.
 * @param ifOkay The value to return if there is no error.
 * @return The last error as a SquirrelJME error.
 * @since 2024/10/14
 */
typedef sjme_errorCode (*sjme_scritchui_cocoa_intern_checkErrorFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

/**
 * Updates the framing for a container so that it uses a top-left origin
 * point rather than bottom-left.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The container to frame.
 * @return Any resultant error.
 * @since 2025/01/01
 */
typedef sjme_errorCode (*sjme_scritchui_cocoa_intern_containerFramingFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

/**
 * Handles Cocoa key events.
 * 
 * @param inState The input state.
 * @param inComponent The input component.
 * @param inEvent The input event.
 * @param typeMask The event type mask.
 * @return Any resultant error, if any.
 * @since 2025/03/15
 */
typedef sjme_errorCode (*sjme_scritchui_cocoa_intern_eventKeyFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull NSEvent* inEvent,
	sjme_attrInValue sjme_jint typeMask);

/**
 * Normalizes and handles mouse events from Cocoa in a unified way.
 * 
 * @param inState The ScritchUI state.
 * @param inComponent The component the event occurred in.
 * @param inEvent The event that was generated.
 * @param typeMask The type mask for the event.
 * @param buttonId The button for the event.
 * @return Any resultant error.
 * @since 2025/01/02
 */
typedef sjme_errorCode (*sjme_scritchui_cocoa_intern_eventMouseFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull NSEvent* inEvent,
	sjme_attrInValue sjme_jint typeMask,
	sjme_attrInValue sjme_jint buttonId);

/**
 * Returns the extents of the window, used for offsetting when framing.
 * 
 * @param inState The ScritchUI state.
 * @param inWindow The window to get the extents of.
 * @param outX The X extents.
 * @param outY The Y extents.
 * @return Any resultant error.
 * @since 2025/01/01
 */
typedef sjme_errorCode (*sjme_scritchui_cocoa_intern_windowExtentsFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint* outX,
	sjme_attrInNotNull sjme_jint* outY);

struct sjme_scritchui_implInternFunctions
{
	/** Checks if there has been a Cocoa error. */
	sjme_scritchui_cocoa_intern_checkErrorFunc checkError;

	/** Performs container framing. */
	sjme_scritchui_cocoa_intern_containerFramingFunc containerFraming;
	
	/** Key event handling. */
	sjme_scritchui_cocoa_intern_eventKeyFunc eventKey;
	
	/** Mouse event handling. */
	sjme_scritchui_cocoa_intern_eventMouseFunc eventMouse;
	
	/** Obtains window extents. */
	sjme_scritchui_cocoa_intern_windowExtentsFunc windowExtents;
};

sjme_errorCode sjme_scritchui_cocoa_intern_checkError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

sjme_errorCode sjme_scritchui_cocoa_intern_containerFraming(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_cocoa_intern_eventKey(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull NSEvent* inEvent,
	sjme_attrInValue sjme_jint typeMask);

sjme_errorCode sjme_scritchui_cocoa_intern_eventMouse(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull NSEvent* inEvent,
	sjme_attrInValue sjme_jint typeMask,
	sjme_attrInValue sjme_jint buttonId);

sjme_errorCode sjme_scritchui_cocoa_intern_windowExtents(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint* outX,
	sjme_attrInNotNull sjme_jint* outY);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COCOAINTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_COCOAINTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COCOAINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COCOAINTERN_H */
