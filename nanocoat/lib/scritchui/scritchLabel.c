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

sjme_errorCode sjme_scritchui_core_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString)
{
	sjme_errorCode error;
	sjme_scritchui_uiLabeled labeled;
	
	if (inState == NULL || inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get labeled item. */
	if (sjme_error_is(error = inState->intern->getLabeled(inState,
		inCommon, &labeled)) || labeled == NULL)
		return sjme_error_default(error);
	
	/* Clear label if already allocated. */
	if (labeled->label != NULL)
	{
		/* Free. */
		if (sjme_error_is(error = sjme_alloc_free(
			labeled->label)))
			return sjme_error_default(error);
			
		/* Wipe. */
		labeled->label = NULL;
	}
	
	/* Make copy of label, if there is one to be set. */
	if (inString != NULL)
		if (sjme_error_is(error = sjme_alloc_strdup(inState->pool,
			&labeled->label, inString)))
			return sjme_error_default(error); 
		
	/* Ignore if not implemented as it is not too important. */
	if (inState->impl->labelSetString == NULL)
		return SJME_ERROR_NONE;
	
	/* Forward implementation, use stored pointer. */
	return inState->impl->labelSetString(inState, inCommon,
		labeled->label);
}

sjme_errorCode sjme_scritchui_core_intern_getLabeled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInOutNotNull sjme_scritchui_uiLabeled* outLabeled)
{
	sjme_scritchui_uiLabeled labeled;
	
	if (inState == NULL || inCommon == NULL || outLabeled == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be labeled. */
	switch (inCommon->type)
	{
		case SJME_SCRITCHUI_TYPE_MENU:
			labeled = &((sjme_scritchui_uiMenu)inCommon)->labeled;
			break;
			
		case SJME_SCRITCHUI_TYPE_MENU_ITEM:
			labeled = &((sjme_scritchui_uiMenuItem)inCommon)->labeled;
			break;
		
		case SJME_SCRITCHUI_TYPE_WINDOW:
			labeled = &((sjme_scritchui_uiWindow)inCommon)->labeled;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outLabeled = labeled;
	return SJME_ERROR_NONE;
}

