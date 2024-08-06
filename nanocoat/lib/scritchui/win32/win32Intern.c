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

static sjme_errorCode sjme_scritchui_win32_windowProc_GETMINMAXINFO(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	LPMINMAXINFO minMax;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiWindow inWindow;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover component. */
	inComponent = NULL;
	if (sjme_error_is(error = inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return sjme_error_default(error);
	
	/* We can only do this on windows. */
	if (inComponent->common.type != SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_USE_FALLBACK;
	inWindow = (sjme_scritchui_uiWindow)inComponent;
	
	/* Copy size details. */
	minMax = (LPMINMAXINFO)lParam;
	if (minMax != NULL && inWindow->min.width != 0 &&
		inWindow->min.height != 0)
	{
		minMax->ptMinTrackSize.x =
			inWindow->min.width + inWindow->minOverhead.width;
		minMax->ptMinTrackSize.y =
			inWindow->min.height + inWindow->minOverhead.height;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_win32_windowProc_PAINT(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiPaintable paintable;
	sjme_scritchui_pencil pencil;
	sjme_scritchui_pencilFont defaultFont;
	sjme_scritchui_listener_paint* infoPaintCore;
	sjme_frontEnd frontEnd;
	HDC hDc;
	PAINTSTRUCT paintInfo;
	sjme_jint x, y, w, h;
	
	/* Initially set that we did not paint this. */
	if (lResult != NULL)
		*lResult = 1;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover component, ignore if this is something else. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* Get component size. */
	w = 0;
	h = 0;
	if (sjme_error_is(error = inState->apiInThread->componentSize(inState,
		inComponent, &w, &h)))
		return sjme_error_default(error);
	
	/* Get component position. */
	x = 0;
	y = 0;
	if (sjme_error_is(error = inState->apiInThread->componentPosition(inState,
		inComponent, &x, &y)))
		return sjme_error_default(error);
	
	/* Can this actually be painted on? */
	paintable = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paintable)) || paintable == NULL)
	{
		if (error == SJME_ERROR_INVALID_ARGUMENT)
			return SJME_ERROR_USE_FALLBACK;
		return sjme_error_default(error);
	}
	
	/* Get callback info, if there is none then do nothing */
	infoPaintCore = &SJME_SCRITCHUI_LISTENER_CORE(paintable, paint);
	if (infoPaintCore->callback == NULL)
		return SJME_ERROR_USE_FALLBACK;
	
	/* A default font is required. */
	defaultFont = NULL;
	if (sjme_error_is(inState->intern->fontBuiltin(
		inState, &defaultFont)) ||
		defaultFont == NULL)
		return sjme_error_default(error);
	
	/* Begin painting. */
	memset(&paintInfo, 0, sizeof(paintInfo));
	SetLastError(0);
	hDc = BeginPaint(hWnd, &paintInfo);
	if (hDc == NULL)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
		
	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.wrapper = hWnd;
	frontEnd.data = hDc;
	
	/* Setup pencil. */
	pencil = &paintable->pencil;
	memset(pencil, 0, sizeof(*pencil));
	if (sjme_error_is(error = sjme_scritchpen_initStatic(
		pencil, inState,
		&sjme_scritchui_win32_pencilFunctions,
		NULL, NULL,
		SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888,
		x, y,
		w, h, w,
		defaultFont, &frontEnd)))
		goto fail_badPaint;

	/* The clipping area is set to the region that needs redrawing. */
	pencil->api->setClip(pencil,
		paintInfo.rcPaint.left, paintInfo.rcPaint.top,
		paintInfo.rcPaint.right - paintInfo.rcPaint.left,
		paintInfo.rcPaint.bottom - paintInfo.rcPaint.top);
	
	/* Perform any painting. */
	if (sjme_error_is(error = infoPaintCore->callback(
		inState, inComponent, pencil, w, h,
		0)))
		goto fail_badPaint;
	
	/* Stop painting. */
	SetLastError(0);
	EndPaint(hWnd, &paintInfo);
	
	/* We did paint this successfully. */
	if (lResult != NULL)
		*lResult = 0;
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_badPaint:
	if (hDc != NULL)
	{
		SetLastError(0);
		EndPaint(hWnd, &paintInfo);
	}
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_win32_windowProc_USER(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_thread_mainFunc threadMain;
	sjme_thread_parameter threadAnything;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	threadMain = (sjme_thread_mainFunc)wParam;
	threadAnything = (sjme_thread_parameter)lParam;
	
	if (threadMain == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward call. */
	if (sjme_error_is(error = SJME_THREAD_RESULT_AS_ERROR(
		threadMain(threadAnything))))
		return sjme_error_defaultOr(error, SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

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
	
#if 0 && defined(SJME_CONFIG_DEBUG)
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
	sjme_scritchui_uiComponent result;
	
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
	result = (sjme_scritchui_uiComponent)GetWindowLongPtr(hWnd,
		GWLP_USERDATA);
	
	/* Basic validation in case we got some other user data. */
	if (result != NULL && result->common.state != inState)
		result = NULL;
		
	/* Success? */
	*outComponent = result;
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
	LRESULT useResult;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
#if 0 && defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Win32 message: %p %d %p %p)",
		hWnd, message, wParam, lParam);
#endif
	
	/* Handle. */
	useResult = 0;
	error = SJME_ERROR_NONE;
	switch (message)
	{
			/* Get minimum and maximum window bounds. */
		case WM_GETMINMAXINFO:
			error = sjme_scritchui_win32_windowProc_GETMINMAXINFO(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;

			/* Paint window. */
		case WM_PAINT:
			error = sjme_scritchui_win32_windowProc_PAINT(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
			
			/* Callback function. */
		case WM_USER:
			error = sjme_scritchui_win32_windowProc_USER(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
		
			/* Unknown, let Windows handle it. */
		default:
			error = SJME_ERROR_USE_FALLBACK;
			break;
	}
	
#if 0 && defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	if (sjme_error_is(error))
		sjme_message("Win32 message FAIL: %p %d %p %p -> %d)",
			hWnd, message, wParam, lParam, error);
#endif
	
	/* Fallback to default? */
	if (sjme_error_is(error) || error == SJME_ERROR_USE_FALLBACK)
		useResult = DefWindowProc(hWnd, message,
			wParam, lParam);
	
	/* Success? */
	if (lResult != NULL)
		*lResult = useResult;
	if (error == SJME_ERROR_USE_FALLBACK)
		return SJME_ERROR_NONE;
	return error;
}
