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
#include "lib/scritchui/core/coreGeneric.h"

sjme_errorCode sjme_scritchui_core_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	sjme_errorCode error;
	
	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (inState->impl->panelEnableFocus == NULL)
		return sjme_error_notImplemented(0);
	
	/* Forward to native handler. */
	if (sjme_error_is(error = inState->impl->panelEnableFocus(inState,
		inPanel, enableFocus, enableFocus && defaultFocus)))
		return sjme_error_default(error);
	
	/* Set new state. */
	inPanel->enableFocus = enableFocus;
	inPanel->defaultFocus = defaultFocus;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	if (inState == NULL || outPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_componentNew(inState,
		(sjme_scritchui_uiComponent*)outPanel,
		sizeof(**outPanel),
		SJME_SCRITCHUI_TYPE_PANEL,
		(sjme_scritchui_coreGeneric_componentNewImplFunc)
			inState->impl->panelNew,
		NULL);
}
