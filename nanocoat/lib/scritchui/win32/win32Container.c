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

sjme_errorCode sjme_scritchui_win32_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	HWND containerWindow, componentWindow;
	
	if (inState == NULL || inContainer == NULL || inContainerData == NULL ||
		addComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover windows. */
	containerWindow = inContainer->common.handle[SJME_SUI_WIN32_H_HWND];
	componentWindow = addComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Reparent window, there should always be a parent returned. */
	SetLastError(0);
	if (NULL == SetParent(componentWindow,
		containerWindow))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NOT_SUB_COMPONENT);
	
	/* Make the child window visible. */
	SetLastError(0);
	ShowWindow(componentWindow, SW_SHOW);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent)
{
	HWND componentWindow;
	
	if (inState == NULL || inContainer == NULL || inContainerData == NULL ||
		removeComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover window. */
	componentWindow = removeComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* Make the child window invisible. */
	SetLastError(0);
	ShowWindow(componentWindow, SW_HIDE);
	
	/* Reparent window, there should always be a parent returned. */
	SetLastError(0);
	if (NULL == SetParent(componentWindow,
		inState->common.handle[SJME_SUI_WIN32_H_VOID]))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NOT_SUB_COMPONENT);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	HWND componentWindow;
	
	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get the component to modify. */
	componentWindow = inComponent->common.handle[SJME_SUI_WIN32_H_HWND];
	
	/* We can just do a normal window move. */
	SetLastError(0);
	if (0 == MoveWindow(componentWindow, x, y,
		width, height, TRUE))
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
			
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
