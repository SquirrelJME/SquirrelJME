/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Cocoa Definitions.
 * 
 * @since 2024/08/15
 */

#ifndef SQUIRRELJME_COCOA_H
#define SQUIRRELJME_COCOA_H

#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "sjme/config.h"
#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COCOA_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if !defined(MAC_OS_X_VERSION_10_15)
	/** Version define for macOS 10.15. */
	#define MAC_OS_X_VERSION_10_15 101500
#endif

#if !defined(MAC_OS_VERSION_11_0)
	/** Version define for macOS 11.0. */
	#define MAC_OS_VERSION_11_0 110000
#endif
	
#if defined(SJME_CONFIG_HAS_COCOA_APPLE)
	#if MAC_OS_X_VERSION_MIN_REQUIRED < MAC_OS_X_VERSION_MAX_ALLOWED
		/** The current Cocoa version. */
		#define SJME_CONFIG_COCOA_VERSION MAC_OS_X_VERSION_MIN_REQUIRED
	#elif MAC_OS_X_VERSION_MAX_ALLOWED < MAC_OS_X_VERSION_MIN_REQUIRED
		/** The current Cocoa version. */
		#define SJME_CONFIG_COCOA_VERSION MAC_OS_X_VERSION_MAX_ALLOWED
	#else
		#if defined(SJME_CONFIG_HAS_ARCH_POWERPC)
			/** The current Cocoa version. */
			#define SJME_CONFIG_COCOA_VERSION MAC_OS_X_VERSION_10_4
		#elif defined(SJME_CONFIG_HAS_ARCH_IA32)
			/** The current Cocoa version. */
			#define SJME_CONFIG_COCOA_VERSION MAC_OS_X_VERSION_10_5
		#else
			/** The current Cocoa version. */
			#define SJME_CONFIG_COCOA_VERSION MAC_OS_VERSION_11_0
		#endif
	#endif
	
	#if !defined(SJME_CONFIG_COCOA_VERSION) || SJME_CONFIG_COCOA_VERSION == 0
		#error No Cocoa version defined
	#endif
	
	/** Cocoa version check. */
	#define SJME_CONFIG_COCOA_VERSION_LEAST(against) \
		(against != 0 && \
		SJME_CONFIG_COCOA_VERSION >= against)
	
	/** GNUStep version check. */
	#define SJME_CONFIG_GNUSTEP_BASE_VERSION_LEAST(major, minor, release) 0
	
	/** GNUStep GUI version check. */
	#define SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(major, minor, release) 0
	
#elif defined(SJME_CONFIG_HAS_COCOA_GNUSTEP)
	/** Cocoa version check. */
	#define SJME_CONFIG_COCOA_VERSION_LEAST(against) 0
	
	/** GNUStep version check. */
	#define SJME_CONFIG_GNUSTEP_BASE_VERSION_LEAST(major, minor, release) \
        (GNUSTEP_BASE_MAJOR_VERSION > major ? 1 : \
		(GNUSTEP_BASE_MAJOR_VERSION > major ? 0 : \
		 \
		(GNUSTEP_BASE_MINOR_VERSION > minor ? 1 : \
		(GNUSTEP_BASE_MINOR_VERSION < minor ? 0 : \
		 \
		(GNUSTEP_BASE_SUBMINOR_VERSION >= release)))))
	
	/** GNUStep GUI version check. */
	#define SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(major, minor, release) \
        (GNUSTEP_GUI_MAJOR_VERSION > major ? 1 : \
		(GNUSTEP_GUI_MAJOR_VERSION > major ? 0 : \
		 \
		(GNUSTEP_GUI_MINOR_VERSION > minor ? 1 : \
		(GNUSTEP_GUI_MINOR_VERSION < minor ? 0 : \
		 \
		(GNUSTEP_GUI_SUBMINOR_VERSION >= release)))))

#else
	#error Unknown Cocoa version.
#endif
	
/** Cocoa version check before a given version. */
#define SJME_CONFIG_COCOA_VERSION_BEFORE(against) \
	(!SJME_CONFIG_COCOA_VERSION_LEAST(against))
	
/** GNUstep Base version check before a given version. */
#define SJME_CONFIG_GNUSTEP_BASE_VERSION_BEFORE(major, minor, release) \
	(!SJME_CONFIG_GNUSTEP_BASE_VERSION_LEAST(major, minor, release))
	
/** GNUstep GUI version check before a given version. */
#define SJME_CONFIG_GNUSTEP_GUI_VERSION_BEFORE(major, minor, release) \
	(!SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(major, minor, release))
	
/** Application handle in the ScritchUI state. */
#define SJME_SUI_COCOA_H_NSAPP 0

/** Main event loop thread. */
#define SJME_SUI_COCOA_H_NSTHREAD 1

/** Main run loop. */
#define SJME_SUI_COCOA_H_NSRUNLOOP 2

/** SquirrelJME super object. */
#define SJME_SUI_COCOA_H_SUPER 3
	
/** Widget. */
#define SJME_SUI_COCOA_H_NSVIEW 0
	
/** Secondary widget. */
#define SJME_SUI_COCOA_H_NSVIEWB 1
	
/** Pencil functions for Cocoa. */
extern const sjme_scritchui_pencilImplFunctions
	sjme_scritchui_cocoa_pencilFunctions;

sjme_errorCode sjme_scritchui_cocoa_apiInit(
	sjme_attrInNotNull sjme_scritchui inState);
	
sjme_errorCode sjme_scritchui_cocoa_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent);

sjme_errorCode sjme_scritchui_cocoa_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
sjme_errorCode sjme_scritchui_cocoa_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight);
	
sjme_errorCode sjme_scritchui_cocoa_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_cocoa_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
sjme_errorCode sjme_scritchui_cocoa_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent);

sjme_errorCode sjme_scritchui_cocoa_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint));

sjme_errorCode sjme_scritchui_cocoa_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH);

sjme_errorCode sjme_scritchui_cocoa_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_cocoa_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);

sjme_errorCode sjme_scritchui_cocoa_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuBar inMenuBar,
	sjme_attrInNullable sjme_pointer ignored);

sjme_errorCode sjme_scritchui_cocoa_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem);

sjme_errorCode sjme_scritchui_cocoa_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuItem inMenuItem,
	sjme_attrInNotNull const sjme_scritchui_impl_initParamMenuItem* init);

sjme_errorCode sjme_scritchui_cocoa_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenu inMenu,
	sjme_attrInNullable sjme_pointer ignored);

sjme_errorCode sjme_scritchui_cocoa_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex);

sjme_errorCode sjme_scritchui_cocoa_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus);

sjme_errorCode sjme_scritchui_cocoa_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInNullable sjme_pointer ignored);
	
sjme_errorCode sjme_scritchui_cocoa_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens);
	
sjme_errorCode sjme_scritchui_cocoa_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height);
	
sjme_errorCode sjme_scritchui_cocoa_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_pointer ignored);
	
sjme_errorCode sjme_scritchui_cocoa_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar);
	
sjme_errorCode sjme_scritchui_cocoa_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COCOA_H
}
		#undef SJME_CXX_SQUIRRELJME_COCOA_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COCOA_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COCOA_H */
