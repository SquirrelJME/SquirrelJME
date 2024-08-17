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
	sjme_scritchui inState;
	
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
	sjme_scritchui_uiPanel inPanel;
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
