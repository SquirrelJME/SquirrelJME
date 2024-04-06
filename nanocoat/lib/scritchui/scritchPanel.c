/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/core.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_scritchui_core_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	sjme_scritchui_uiPanel result;
	sjme_errorCode error;

	if (inState == NULL || outPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool, sizeof(*result),
		&result)) || result == NULL)
		return sjme_error_default(error);
	
	/* Set base properties. */
	result->component.common.type = SJME_SCRITCHUI_TYPE_PANEL;
	
	/* Setup native widget. */
	if (inState->impl->panelNew == NULL ||
		sjme_error_is(error = inState->impl->panelNew(inState,
		result)) || result->component.common.handle == NULL)
		goto fail_newWidget;
	
	/* Success! */
	*outPanel = result;
	return SJME_ERROR_NONE;
	
fail_newWidget:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}
