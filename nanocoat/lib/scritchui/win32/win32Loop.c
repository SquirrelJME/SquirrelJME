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
	if ((result = PostThreadMessage(
		GetThreadId(inState->loopThread),
		WM_USER,
		(WPARAM)callback, (LPARAM)anything)) == 0)
		return inState->implIntern->getLastError(inState);
	
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
	sjme_thread_mainFunc threadMain;
	sjme_thread_parameter threadAnything;
	
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
	
	/* Error? */
	if (messageResult < 0)
		return inState->implIntern->getLastError(inState);
	
	/* Handle message. */
	switch (message.message)
	{
			/* Callback function. */
		case WM_USER:
			threadMain = (sjme_thread_mainFunc)message.wParam;
			threadAnything = (sjme_thread_parameter)message.lParam;
			
			if (threadMain == NULL)
				return SJME_ERROR_NULL_ARGUMENTS;
				
			return SJME_THREAD_RESULT_AS_ERROR(threadMain(threadAnything));
		
		default:
			sjme_todo("Handle message: %d (%p, %p)",
				message.message, message.wParam, message.lParam);
			break;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
