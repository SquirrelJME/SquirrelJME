/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

/**
 * Execution data.
 * 
 * @since 2024/04/17
 */
typedef struct sjme_scritchui_gtk2_onceExecuteData
{
	/** The callback to execute. */
	sjme_thread_mainFunc callback;
	
	/** The value to pass. */
	sjme_thread_parameter anything;
} sjme_scritchui_gtk2_onceExecuteData;

/**
 * Thread callbacks have a return value we need to handle properly.
 * 
 * @param inData The input once execution data.
 * @since 2024/04/17
 */
static void sjme_scritchui_gtk2_onceExecute(gpointer inData)
{
	sjme_scritchui_gtk2_onceExecuteData* data;
	sjme_thread_mainFunc callback;
	sjme_thread_parameter anything;
	
	data = (sjme_scritchui_gtk2_onceExecuteData*)inData;
	if (data == NULL)
		return;
	
	/* Recover data. */
	callback = data->callback;
	anything = data->anything;
	
	/* Debug. */
	sjme_message("GTK before-exec free...");
	
	/* Clear data before we call in the event of signals or otherwise. */
	g_free_sized(data, sizeof(*data));
	data = NULL;
	
	/* Debug. */
	sjme_message("GTK exec call...");
	
	/* Perform the call. */
	callback(anything);
}

sjme_errorCode sjme_scritchui_gtk2_loopExecute(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	guint eventId;
	sjme_scritchui_gtk2_onceExecuteData* data;
	
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup data. */
	data = g_malloc(sizeof(*data));
	memset(data, 0, sizeof(*data));
	data->callback = callback;
	data->anything = anything;
	
	/* Debug. */
	sjme_message("GTK enqueue...");
	
	/* Add to the event loop. */
	eventId = g_idle_add_once(sjme_scritchui_gtk2_onceExecute, data);
	if (eventId == 0)
	{
		/* Debug. */
		sjme_message("GTK event id was zero?");
		
		g_free(data);
		return SJME_ERROR_CANNOT_CREATE;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
