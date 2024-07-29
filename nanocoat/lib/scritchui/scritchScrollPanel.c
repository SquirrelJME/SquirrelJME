/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "lib/scritchui/core/coreGeneric.h"

sjme_errorCode sjme_scritchui_core_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScrollPanel* outScrollPanel)
{
	if (inState == NULL || outScrollPanel == NULL)
		return SJME_ERROR_NONE;

	/* Use generic function. */
	return sjme_scritchui_coreGeneric_componentNew(inState,
		(sjme_scritchui_uiComponent*)outScrollPanel,
		sizeof(**outScrollPanel),
		SJME_SCRITCHUI_TYPE_SCROLL_PANEL,
		(sjme_scritchui_coreGeneric_componentNewImplFunc)
			inState->impl->scrollPanelNew,
		NULL);
}
