/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Win32 ScritchUI Implementation internals.
 * 
 * @since 2024/07/30
 */

#ifndef SQUIRRELJME_WIN32INTERN_H
#define SQUIRRELJME_WIN32INTERN_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_WIN32INTERN_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Calls @c GetLastError() and translates the error code.
 * 
 * @param inState The input state.
 * @param ifOkay The value to return if there is no error.
 * @return The last error code.
 * @since 2024/07/31
 */
typedef sjme_errorCode (*sjme_scritchui_win32_intern_getLastErrorFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

/**
 * Internal window procedure handler.
 * 
 * @param inState The ScritchUI state.
 * @param hWnd The window that generated the message.
 * @param message The message type.
 * @param wParam The upper value.
 * @param lParam The lower value.
 * @param lResult Option value where lresult goes.
 * @return Any resultant error, if any.
 * @since 2024/08/05
 */
typedef sjme_errorCode (*sjme_scritchui_win32_intern_windowProcFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult);

struct sjme_scritchui_implInternFunctions
{
	/** Translates the last error code to SquirrelJME errors. */
	sjme_scritchui_win32_intern_getLastErrorFunc getLastError;
	
	/** Window process handling. */
	sjme_scritchui_win32_intern_windowProcFunc windowProc;
};

sjme_errorCode sjme_scritchui_win32_intern_getLastError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

sjme_errorCode sjme_scritchui_win32_intern_windowProc(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_WIN32INTERN_H
}
		#undef SJME_CXX_SQUIRRELJME_WIN32INTERN_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_WIN32INTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_WIN32INTERN_H */
