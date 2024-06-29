/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_gtk2_intern_reconnectSignal(
	sjme_attrInNotNull GtkWidget* inWidget,
	sjme_attrInNotNull void* inOnWhat,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	sjme_attrInNotNull void* inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd,
	sjme_attrInNotNull sjme_lpcstr inSignal,
	sjme_attrInNotNull GCallback inGtkCallback)
{
	/* Disconnect old signal? */
	if (infoCore->extra != 0)
	{
		/* Disconnect. */
		gtk_signal_disconnect(inWidget, (gulong)infoCore->extra);
		
		/* Clear data. */
		infoCore->extra = 0;
		infoCore->callback = 0;
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
	}
	
	/* Connect signal. */
	if (inListener != NULL)
	{
		/* Fill in. */
		infoCore->callback = inListener;
		if (copyFrontEnd != NULL)
			memmove(&infoCore->frontEnd, copyFrontEnd,
				sizeof(*copyFrontEnd));
		
		/* Connect signal now. */
		infoCore->extra = g_signal_connect(inWidget, inSignal,
			inGtkCallback, inOnWhat);
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
