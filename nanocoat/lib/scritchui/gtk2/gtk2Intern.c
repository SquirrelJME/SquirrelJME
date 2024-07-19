/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_scritchui_gtk2_intern_disconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore)
{
	sjme_jint i, n;
	sjme_list_sjme_intPointer* idList;
	
	if (inState == NULL || inWidget == NULL || infoCore == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Disconnect old signal? */
	if (infoCore->extra != 0)
	{
		/* Recover ID list. */
		idList = (sjme_list_sjme_intPointer*)infoCore->extra;
		
		/* Disconnect each signal. */
		for (i = 0, n = idList->length; i < n; i++)
			if (idList->elements[i] != 0)
			{
				gtk_signal_disconnect(inWidget, (gulong)idList->elements[i]);
				idList->elements[i] = 0;
			}
		
		/* Clear out and cleanup. */
		infoCore->extra = 0;
		infoCore->callback = NULL;
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
		
		/* Free ID list. */
		sjme_alloc_free(idList);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_intern_reconnectSignal(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull sjme_pointer inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull sjme_pointer inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull GCallback inGtkCallback,
	sjme_attrInPositiveNonZero sjme_jint numSignals,
	...)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_list_sjme_intPointer* idList;
	va_list argList;
	sjme_lpcstr inSignal;
	
	if (inState == NULL || inWidget == NULL || inOnWhat == NULL ||
		infoCore == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Disconnect old signal? */
	if (infoCore->extra != 0)
		inState->implIntern->disconnectSignal(inState, inWidget, infoCore);
	
	/* Connect signal. */
	if (inListener != NULL)
	{
		/* Setup new list. */
		idList = NULL;
		if (sjme_error_is(error = sjme_list_alloc(inState->pool,
			numSignals, &idList, sjme_intPointer, 0)) || idList == NULL)
			return sjme_error_default(error);
		
		/* Fill in. */
		infoCore->extra = (sjme_intPointer)idList;
		infoCore->callback = inListener;
		if (copyFrontEnd != NULL)
			memmove(&infoCore->frontEnd, copyFrontEnd,
				sizeof(*copyFrontEnd));
		
		/* Start argument handling. */
		va_start(argList, numSignals);
		
		/* Connect all signals accordingly. */
		for (i = 0; i < numSignals; i++)
		{
			/* Get next signal item. */
			inSignal = va_arg(argList, sjme_lpcstr);
			
			/* Connect accordingly. */
			if (inSignal != NULL)
				infoCore->extra = g_signal_connect(inWidget, inSignal,
					inGtkCallback, inOnWhat);
		}
			
		/* End argument handling. */
		va_end(argList);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_intern_widgetInit(
	sjme_attrInNotNull GtkWidget* inWidget)
{
	if (inWidget == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Setup visibility events. */
	gtk_widget_add_events(inWidget,
		GDK_VISIBILITY_NOTIFY_MASK |
		GDK_STRUCTURE_MASK);
	
	return SJME_ERROR_NONE;
}
