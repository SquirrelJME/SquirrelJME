/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

sjme_errorCode sjme_scritchui_win32_intern_getLastError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay)
{
	DWORD winErr;
	
	if (inState == NULL)
		return SJME_ERROR_NONE;
	
	/* Get Windows error, but then also clear it. */
	winErr = GetLastError();
	SetLastError(0);
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("GetLastError() == %d 0x%08x",
		winErr, winErr);
#endif
	
	/* Use given error, if successful. */
	if (winErr == ERROR_SUCCESS)
		return ifOkay;
	
	/* Did not actually map to anything? */
	return sjme_error_default(ifOkay);
}

sjme_errorCode sjme_scritchui_win32_intern_recoverComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrOutNullable sjme_scritchui_uiComponent* outComponent)
{
	if (inState == NULL || outComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* NULL maps to a NULL component. */
	if (hWnd == NULL)
	{
		*outComponent = NULL;
		return SJME_ERROR_NONE;
	}
	
	/* Get the component this refers to. */
	SetLastError(0);
	*outComponent = (sjme_scritchui_uiComponent)GetWindowLongPtr(hWnd,
		GWLP_USERDATA);
		
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_intern_windowProc(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_jboolean fallbackDefault;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiWindow inWindow;
	sjme_thread_mainFunc threadMain;
	sjme_thread_parameter threadAnything;
	LRESULT useResult;
	LPMINMAXINFO minMax;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Win32 message: %p %d %p %p)",
		hWnd, message, wParam, lParam);
#endif
	
	/* Try getting the component this belongs to. */
	inComponent = NULL;
	inState->implIntern->recoverComponent(inState, hWnd,
		&inComponent);
	
	/* Map to distinct types. */
	inWindow = NULL;
	if (inComponent != NULL)
	{
		if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
			inWindow = (sjme_scritchui_uiWindow)inComponent;
	}
	
	/* Default to zero result. */
	useResult = 0;
	
	/* Handle. */
	fallbackDefault = SJME_JNI_TRUE;
	switch (message)
	{
			/* Get minimum and maximum window bounds. */
		case WM_GETMINMAXINFO:
			if (inWindow != NULL)
			{
				/* We are handling this. */
				fallbackDefault = SJME_JNI_FALSE;
				
				/* Copy window details. */
				minMax = (LPMINMAXINFO)lParam;
				if (minMax != NULL && inWindow->min.width != 0 &&
					inWindow->min.height != 0)
				{
					minMax->ptMinTrackSize.x = inWindow->min.width;
					minMax->ptMinTrackSize.y = inWindow->min.height;
				}
			}
			break;
		
			/* Callback function. */
		case WM_USER:
			threadMain = (sjme_thread_mainFunc)wParam;
			threadAnything = (sjme_thread_parameter)lParam;
			
			if (threadMain == NULL)
				return SJME_ERROR_NULL_ARGUMENTS;
			
			/* We are handling this. */
			fallbackDefault = SJME_JNI_FALSE;
			
			/* Forward call. */
			if (sjme_error_is(error = SJME_THREAD_RESULT_AS_ERROR(
				threadMain(threadAnything))))
				return error;
			break;
		
			/* Unknown, let Windows handle it. */
		default:
			break;
	}
	
	/* Fallback to default? */
	if (fallbackDefault)
		useResult = DefWindowProc(hWnd, message,
			wParam, lParam);
	
	/* Success! */
	if (lResult != NULL)
		*lResult = useResult;
	return SJME_ERROR_NONE;
}
