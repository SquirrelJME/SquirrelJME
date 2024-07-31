/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Win32 ScritchUI Implementation.
 * 
 * @since 2024/07/30
 */

#ifndef SQUIRRELJME_WIN32_H
#define SQUIRRELJME_WIN32_H

#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_WIN32_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Window handle. */
#define SJME_SUI_WIN32_H_HWND 0

/** Void window handle. */
#define SJME_SUI_WIN32_H_VOID 3
	
/** Are we on Windows 9x? */
#define SJME_SUI_WIN32_V_WIN9X 3

sjme_errorCode sjme_scritchui_win32_apiInit(
	sjme_attrInNotNull sjme_scritchui inState);
	
sjme_errorCode sjme_scritchui_win32_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything);

sjme_errorCode sjme_scritchui_win32_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated);

sjme_errorCode sjme_scritchui_win32_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInNullable sjme_pointer ignored);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_WIN32_H
}
		#undef SJME_CXX_SQUIRRELJME_WIN32_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_WIN32_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_WIN32_H */
