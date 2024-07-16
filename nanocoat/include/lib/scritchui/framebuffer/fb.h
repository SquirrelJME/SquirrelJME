/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI framebuffer interface header.
 * 
 * @since 2024/07/16
 */

#ifndef SQUIRRELJME_FB_H
#define SQUIRRELJME_FB_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiImpl.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "sjme/config.h"
#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_FB_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Framebuffer implementation functions. */
extern const sjme_scritchui_implFunctions sjme_scritchui_fbFunctions;

sjme_errorCode sjme_scritchui_fb_apiInit(
	sjme_attrInNotNull sjme_scritchui inState);

/**
 * Returns the Framebuffer ScritchUI interface.
 * 
 * @param inPool The allocation pool used.
 * @param outState The newly created state.
 * @param wrappedState The wrapped state to use.
 * @param loopExecute The loop execution to run after init.
 * @param initFrontEnd Optional initial frontend data.
 * @return The library interface.
 * @since 2024/07/16 
 */
sjme_errorCode sjme_scritchui_fb_apiInitBase(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull sjme_scritchui wrappedState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd);

/**
 * Maps the front end for the given base component info.
 * 
 * @param inState The input state.
 * @param topLevel The top level component.
 * @param wrapped The bottom level wrapped component.
 * @return Any resultant error, if any.
 * @since 2024/07/16
 */
sjme_errorCode sjme_scritchui_fb_biMap(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_commonBase* topLevel,
	sjme_attrInNotNull sjme_scritchui_commonBase* wrapped);

sjme_errorCode sjme_scritchui_fb_loopExecute(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);
	
sjme_errorCode sjme_scritchui_fb_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);
	
sjme_errorCode sjme_scritchui_fb_loopExecuteWait(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_fb_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_FB_H
}
		#undef SJME_CXX_SQUIRRELJME_FB_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_FB_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FB_H */
