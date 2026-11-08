/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <glib.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
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
 * @return Always @c TRUE so the loop terminates.
 * @since 2024/04/17
 */
static gboolean sjme_scritchui_gtk2_onceExecute(gpointer inData)
{
	sjme_scritchui_gtk2_onceExecuteData* data;
	sjme_thread_mainFunc callback;
	sjme_thread_parameter anything;
	sjme_errorCode error;
	
	data = (sjme_scritchui_gtk2_onceExecuteData*)inData;
	if (data == NULL)
		return G_SOURCE_REMOVE;
	
	/* Recover data. */
	callback = data->callback;
	anything = data->anything;
	
	/* Clear data before we call in the event of signals or otherwise. */
#if defined(GLIB_AVAILABLE_IN_2_76)
	g_free_sized(data, sizeof(*data));
#else
	g_free(data);
#endif
	data = NULL;
	
	/* Perform the call. */
	error = SJME_THREAD_RESULT_AS_ERROR(callback(anything));
	
	/* Did it fail? */
	if (sjme_error_is(error))
		sjme_message("Native callback error: %d.", error);
	
	/* Always remove. */
	return G_SOURCE_REMOVE;
}

sjme_errorCode sjme_scritchui_gtk2_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui_gtk2_onceExecuteData* data;
	
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup data. */
	data = g_malloc(sizeof(*data));
	memset(data, 0, sizeof(*data));
	data->callback = callback;
	data->anything = anything;
	
	/* Add to the event loop. */
	/* We want to make sure it gets executed as soon as possible! */
	/* GTK docs say for g_main_context_invoke_full(). */
	/* > The notify function should not assume that it is called from any */
	/* > particular thread or with any particular context acquired. */
	/* Thus that cannot be used, we must always add to the idle loop. */
	if (g_idle_add_full(G_PRIORITY_DEFAULT_IDLE,
		sjme_scritchui_gtk2_onceExecute, data, NULL) <= 0)
		sjme_message("Failed idle push.");
	
	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
