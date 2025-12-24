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

#ifndef SJME_C_WIN32INTERN_H
#define SJME_C_WIN32INTERN_H

#include "sjme/config.h"

#include <windef.h>

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

#pragma region(missing)

#if !defined(DPI_AWARENESS_CONTEXT_UNAWARE)
	#define DPI_AWARENESS_CONTEXT_UNAWARE \
		((HANDLE)-1)
#endif
	
#if !defined(DPI_AWARENESS_CONTEXT_SYSTEM_AWARE)
	#define DPI_AWARENESS_CONTEXT_SYSTEM_AWARE \
		((HANDLE)-2)
#endif
	
#if !defined(DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE)
	#define DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE \
		((HANDLE)-3)
#endif
	
#if !defined(DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2)
	#define DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2 \
		((HANDLE)-4)
#endif
	
#if !defined(DPI_AWARENESS_CONTEXT_UNAWARE_GDISCALED)
	#define DPI_AWARENESS_CONTEXT_UNAWARE_GDISCALED \
		((HANDLE)-5)
#endif
	
/** @code SetProcessDpiAwarenessContext @endcode . */
typedef BOOL (*sjme_scritchui_win32_intern_SPDAC)(HANDLE);
	
/** @code SetProcessDpiAwareness @endcode . */
typedef BOOL (*sjme_scritchui_win32_intern_SPDA)(int);
	
/** @code SetProcessDPIAware @endcode . */
typedef BOOL (*sjme_scritchui_win32_intern_SPDPIA)(void);
	
/** @code GetDpiForWindow @endcode . */
typedef UINT (*sjme_scritchui_win32_intern_GDFW)(HWND);

/** @code GetDpiForMonitor @endcode . */
typedef HRESULT (*sjme_scritchui_win32_intern_GDFM)(
	HMONITOR, INT, UINT*, UINT*);
	
#pragma endregion(missing)
	
/**
 * Standard DLLs on Windows.
 * 
 * @since 2025/12/23
 */
typedef enum sjme_scritchui_win32_intern_dll
{
	/** user32.dll. */
	SJME_SCRITCHUI_WIN32_USER32_DLL,
	
	/** shcore.dll. */
	SJME_SCRITCHUI_WIN32_SHCORE32_DLL,
	
	/** The number of internal DLLs. */
	SJME_SCRITCHUI_WIN32_NUM_INTERN_DLL,
} sjme_scritchui_win32_intern_dll;

/**
 * Loads a standard DLL and potentially returns the procedure function for
 * the given name.
 * 
 * @param inState The input state.
 * @param dll The standard DLL to locate.
 * @param procName The procedure name.
 * @param outProc The resultant procedure.
 * @return Any resultant error, if any.
 * @since 2025/12/23
 */
typedef sjme_errorCode (*sjme_scritchui_win32_intern_dllProcFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_scritchui_win32_intern_dll dll,
	sjme_attrInNotNull sjme_lpcstr procName,
	sjme_attrOutNotNull PROC* outProc);

/**
 * Calls @c GetLastError() and translates the error code.
 * 
 * @param inState The input state.
 * @param ifOkay The value to return if there is no error.
 * @return The last error code as a SquirrelJME error.
 * @since 2024/07/31
 */
typedef sjme_errorCode (*sjme_scritchui_win32_intern_getLastErrorFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

/**
 * Recovers the component that belongs to a @a HWND .
 * 
 * @param inState The input state.
 * @param hWnd The window to get the component from, if this is
 * the value @c NULL then @a outComponent will be set to @c NULL .
 * @param outComponent The resultant component.
 * @return Any resultant error, if any.
 * @since 2024/08/06
 */
typedef sjme_errorCode (*sjme_scritchui_win32_intern_recoverComponentFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrOutNullable sjme_scritchui_uiComponent* outComponent);

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
	
	/** Recovers the component that belongs to a @a HWND . */
	sjme_scritchui_win32_intern_recoverComponentFunc recoverComponent;
	
	/** Window process handling. */
	sjme_scritchui_win32_intern_windowProcFunc windowProc;
	
	/** Direct Win32 window processor. */
	PROC windowProcWin32;
	
	/** Return function pointer from a standard DLL. */
	sjme_scritchui_win32_intern_dllProcFunc dllProc;
};
	
sjme_errorCode sjme_scritchui_win32_intern_dllProc(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_scritchui_win32_intern_dll dll,
	sjme_attrInNotNull sjme_lpcstr procName,
	sjme_attrOutNotNull PROC* outProc);

sjme_errorCode sjme_scritchui_win32_intern_getLastError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay);

sjme_errorCode sjme_scritchui_win32_intern_recoverComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrOutNullable sjme_scritchui_uiComponent* outComponent);

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
