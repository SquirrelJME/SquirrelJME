/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

sjme_errorCode sjme_scritchui_win32_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	BOOL result;
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Send to the event thread. */
	SetLastError(0);
	if ((result = PostThreadMessage(
		inState->loopThreadId,
		WM_USER,
		(WPARAM)callback, (LPARAM)anything)) == 0)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_LOOP_ENQUEUE_FAILED);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated)
{
	MSG message;
	BOOL messageResult;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read next message for the event thread. */
	memset(&message, 0, sizeof(message));
	if (blocking)
	{
		/* Wait for the next message. */
		messageResult = GetMessage(&message, NULL,
			0, 0);
		
		/* Quitting? */
		if (messageResult == 0)
		{
			if (outHasTerminated != NULL)
				*outHasTerminated = SJME_JNI_TRUE;
			return SJME_ERROR_NONE;
		}
	}
	else
	{
		/* Grab the next message. */
		messageResult = PeekMessage(&message, NULL,
			0, 0, PM_REMOVE);
		
		/* No message? */
		if (messageResult == 0)
			return SJME_ERROR_NONE;
		
		/* Quitting? */
		if (message.message == WM_QUIT)
		{
			if (outHasTerminated != NULL)
				*outHasTerminated = SJME_JNI_TRUE;
			return SJME_ERROR_NONE;
		}
	}
	
	/* If there is no window, we handle it ourselves. */
	if (message.hwnd == NULL && !(message.message == WM_TIMER))
		return inState->implIntern->windowProc(inState,
			message.hwnd, message.message, message.wParam, message.lParam,
			NULL);
	
	/* Otherwise dispatch to a window procedure handler. */
	SetLastError(0);
	TranslateMessage(&message);
	DispatchMessage(&message);
	
	/* Success? */
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}
