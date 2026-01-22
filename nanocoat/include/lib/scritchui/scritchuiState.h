/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI state structures.
 * 
 * @file
 * @since 2026/01/21
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHUISTATE_H
#define SJME_C_SQUIRRELJME_SCRITCHUISTATE_H

#include "sjme/multithread.h"
#include "sjme/native.h"
#include "lib/scritchui/scritchuiBasic.h"
#include "lib/scritchui/scritchuiApiStruct.h"
#include "lib/scritchui/scritchuiApiStructImpl.h"
#include "lib/scritchui/scritchuiConst.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHUISTATE_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(scritchui)
	
/**
 * The state of fonts within ScritchUI.
 * 
 * @since 2026/01/18
 */
typedef struct sjme_scritchui_fontState
{
	/** The internal built-in font. */
	sjme_scritchui_pencilFont builtinFont;
	
	/** The total number of scanned fonts. */
	sjme_jint scanTotal;

	/** The fonts which have been registered. */
	sjme_list(sjme_scritchui_pencilFont)* fontRegister;

	/** Pseudo fonts being kept track of. */
	sjme_list(sjme_scritchui_pencilFont)* pseudoRegister;
} sjme_scritchui_fontState;

struct sjme_scritchui_stateBase
{
	/** Common data. */
	sjme_scritchui_uiCommonBase common;
	
	/** Window manager information. */
	const sjme_scritchui_wmInfo* wmInfo;
	
	/** API functions to use. */
	const sjme_scritchui_apiFunctions* api;
	
	/** In thread API functions. */
	const sjme_scritchui_apiFunctions* apiInThread;
	
	/** Internal implementation functions to use. */
	const sjme_scritchui_internFunctions* intern;
	
	/** Implementation functions to use. */
	const sjme_scritchui_implFunctions* impl;
	
	/** Internal implementation functions, which are opaque. */
	const sjme_scritchui_implInternFunctions* implIntern;

	/** Optional externals for helper front-end interface functions. */
	const sjme_scritchui_externalFunctions* externals;
	
	/** The allocation pool to use for allocations. */
	sjme_alloc_pool pool;
	
	/** The event loop thread, if applicable. */
	sjme_thread loopThread;
	
	/** The current loop thread ID, if applicable. */
	sjme_thread_id loopThreadId;
	
	/** Loop thread initializer if one was passed. */
	sjme_thread_mainFunc loopThreadInit;
	
	/** Indicator that the main loop is ready for execution. */
	sjme_alignPointer sjme_atomic(sjme_jint) loopThreadReady;
	
	/** The available screens. */
	sjme_list(sjme_scritchui_uiScreen)* screens;
	
	/** The window manager type used. */
	sjme_scritchui_windowManagerType wmType;
	
	/** Function to obtain the current nanotime, for input events. */
	sjme_nal_nanoTimeFunc nanoTime;
	
	/** Wrapped ScritchUI state, if this is a wrapper. */
	sjme_scritchui wrappedState;
	
	/** Reference to owning state. */
	sjme_alignPointer sjme_atomic(sjme_pointer) topState;
	
	/** The next ID for opaque menu items. */
	sjme_jint nextMenuItemId;
	
	/** Windowing system specific bugs. */
	sjme_scritchui_bugs bugs;

	/** The loop queue for manual event loops. */
	sjme_alignPointer sjme_scritchui_loopQueue loopQueue;

	/** Platform flags (@link sjme_scritchui_lafPlatformFlag @endlink ). */
	sjme_jint platformFlags;
	
	/** Font state. */
	sjme_scritchui_fontState font;
};
	
#pragma endregion(scritchui)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUISTATE_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHUISTATE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUISTATE_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHUISTATE_H */
