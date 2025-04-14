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

/**
 * Passed to @c sjme_scritchui_win32_displayQuery to fill in screen
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
	if (0 == GetMonitorInfo(hMonitor, &monitor))
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
