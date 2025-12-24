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

#include "sjme/fixed.h"

/**
 * Passed to @link sjme_scritchui_win32_displayQuery @endlink to fill in screen
 * information.
 * 
 * @sincer 2024/08/05
 */
typedef struct sjme_scritchui_win32_displayQueryInfo
{
	/** The current state. */
	sjme_scritchui inState;
	
	/** The screens to output. */
	sjme_scritchui_uiScreen* outScreens;
	
	/** The screen limit. */
	sjme_jint limit;
	
	/** The current screen total. */
	sjme_jint total;
	
	/** The current failure. */
	sjme_errorCode error;
} sjme_scritchui_win32_displayQueryInfo;

static BOOL CALLBACK sjme_scritchui_win32_displayQuery(
	HMONITOR hMonitor, HDC hdcMonitor, LPRECT monitorRect, LPARAM data)
{
	sjme_errorCode error;
	sjme_scritchui_win32_displayQueryInfo* info;
	sjme_scritchui_uiScreen screen;
	MONITORINFOEX monitor;
	
	/* Recover info. */
	info = (sjme_pointer)data;
	if (info == NULL || hMonitor == NULL)
		return TRUE;
	
	/* Stop if we cannot query anymore. */
	if (info->total == info->limit)
		return FALSE;
	
	/* Get information on this monitor. */
	memset(&monitor, 0, sizeof(monitor));
	monitor.cbSize = sizeof(monitor);
	if (0 == GetMonitorInfo(hMonitor, (LPMONITORINFO)&monitor))
	{
		info->error = info->inState->implIntern->getLastError(
			info->inState, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return TRUE;
	}
	
	/* Map screen. */
	screen = NULL;
	if (sjme_error_is(error = info->inState->intern->mapScreen(
		info->inState, info->total, &screen,
		hMonitor)))
	{
		info->error = error;
		return TRUE;
	}
	
	/* Fill in screen. */
	info->outScreens[info->total++] = screen;
	
	/* Always continue the query. */
	return TRUE;
}

static sjme_jboolean sjme_scritchui_win32_dpiWin10(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull HWND hWnd)
{
	sjme_scritchui_win32_intern_GDFW proc;
	
	if (inState == NULL || inState->implIntern->dllProc == NULL ||
		hWnd == NULL)
		return 0;
	
	/* Find procedure. */
	if (sjme_error_is(inState->implIntern->dllProc(inState,
		SJME_SCRITCHUI_WIN32_USER32_DLL,
		"GetDpiForWindow",
		(PROC*)&proc)))
		return 0;
	
	/* Forward call! */
	return proc(hWnd);
}

static sjme_jboolean sjme_scritchui_win32_dpiWin8(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull HMONITOR hMonitor,
	sjme_attrOutNotNull UINT* dpiX,
	sjme_attrOutNotNull UINT* dpiY)
{
	sjme_scritchui_win32_intern_GDFM proc;
	
	if (inState == NULL || hMonitor == NULL || dpiX == NULL || dpiY == NULL ||
		inState->implIntern->dllProc == NULL)
		return SJME_JNI_FALSE;
	
	/* Find procedure. */
	if (sjme_error_is(inState->implIntern->dllProc(inState,
		SJME_SCRITCHUI_WIN32_SHCORE32_DLL,
		"GetDpiForMonitor",
		(PROC*)&proc)))
		return SJME_JNI_FALSE;
	
	/* Forward call! */
	if (S_OK == proc(hMonitor, 2, dpiX, dpiY))
		return SJME_JNI_TRUE;
	return SJME_JNI_FALSE;
}

sjme_errorCode sjme_scritchui_win32_screenGetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen,
	sjme_attrInNullable sjme_scritchui_uiComponent forComponent,
	sjme_attrOutNullable sjme_scritchui_rect* pixelBound,
	sjme_attrOutNullable sjme_scritchui_rect* mmBound)
{
	HWND hWnd;
	HMONITOR hMonitor;
	MONITORINFOEXA info;
	UINT dpiX, dpiY, check;
	sjme_fixed inMm, w, h;
	
	if (inState == NULL || inScreen == NULL ||
		(pixelBound == NULL && mmBound == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Grab the window if there is one. */
	hMonitor = inScreen->screenHandle;
	hWnd = (forComponent == NULL ? NULL :
		forComponent->common.handle[SJME_SUI_WIN32_H_HWND]);
	
	/* Currently set DPI as being unknown. */
	dpiX = 0;
	dpiY = 0;
	
	/* Is there window context? Use the monitor it belongs to most. */
	if (hWnd != NULL)
	{
		/* Which monitor is the window on? */
		hMonitor = MonitorFromWindow(hWnd, MONITOR_DEFAULTTOPRIMARY);
		
		/* Get the DPI for this window. */
		check = sjme_scritchui_win32_dpiWin10(inState, hWnd);
		if (check > 0)
		{
			dpiX = check;
			dpiY = check;
		}
	}

	/* Read in all dimensional details. */
	memset(&info, 0 , sizeof(info));
	info.cbSize = sizeof(info);
	if (!GetMonitorInfoA(hMonitor, (LPMONITORINFO)&info))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* If the DPI is still unknown, get it from the monitor. */
	if (dpiX <= 0 || dpiY <= 0)
		if (!sjme_scritchui_win32_dpiWin8(inState, hMonitor, &dpiX, &dpiY))
		{
			/* Invalidate incase any value was set. */
			dpiX = 0;
			dpiY = 0;
		}
	
	/* DPI still not valid? Fallback to Windows 96. */
	if (dpiX <= 0 || dpiY <= 0)
	{
		dpiX = 96;
		dpiY = 96;
	}
	
	/* Calculate pixels. */
	w = abs(info.rcMonitor.right - info.rcMonitor.left);
	h = abs(info.rcMonitor.bottom - info.rcMonitor.top);
	if (pixelBound != NULL)
	{
		pixelBound->s.x = info.rcMonitor.left;
		pixelBound->s.y = info.rcMonitor.top;
		pixelBound->d.width = w;
		pixelBound->d.height = h;
	}
	
	/* Calculate DPI. */
	if (mmBound != NULL)
	{
		/* Inches to millimeters, 1 / 25.4, try to get more precision! */
		inMm = sjme_fixed_fraction(1,
			sjme_fixed_fraction(127, 5));
		
		mmBound->s.x = 0;
		mmBound->s.y = 0;
		mmBound->d.width = sjme_fixed_int(
			sjme_fixed_mul(sjme_fixed_hi(w), inMm));
		mmBound->d.height = sjme_fixed_int(
			sjme_fixed_mul(sjme_fixed_hi(h), inMm));
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	sjme_scritchui_win32_displayQueryInfo info;
	
	if (inState == NULL || outScreens == NULL || inOutNumScreens == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Initialize base info. */
	memset(&info, 0, sizeof(info));
	info.inState = inState;
	info.outScreens = outScreens;
	info.limit = *inOutNumScreens;
	info.error = SJME_ERROR_NONE;
	
	/* Query display screens. */
	SetLastError(0);
	if (0 == EnumDisplayMonitors(NULL, NULL,
		sjme_scritchui_win32_displayQuery, (LPARAM)&info) ||
		sjme_error_is(info.error))
		return inState->implIntern->getLastError(inState,
			sjme_error_defaultOr(info.error,
				SJME_ERROR_NATIVE_WIDGET_FAILURE));
	
	/* Return total screen count. */
	*inOutNumScreens = info.total;
	return SJME_ERROR_NONE;
}
