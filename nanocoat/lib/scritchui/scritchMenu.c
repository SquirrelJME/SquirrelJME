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

sjme_errorCode sjme_scritchui_core_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar)
{
	if (inState == NULL || outMenuBar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenuBar,
		sizeof(**outMenuBar),
		SJME_SCRITCHUI_TYPE_MENU_BAR,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuBarNew);
}

sjme_errorCode sjme_scritchui_core_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem)
{
	if (inState == NULL || outMenuItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenuItem,
		sizeof(**outMenuItem),
		SJME_SCRITCHUI_TYPE_MENU_ITEM,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuItemNew);
}

sjme_errorCode sjme_scritchui_core_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu)
{
	if (inState == NULL || outMenu == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_commonNew(inState,
		(sjme_scritchui_uiCommon*)outMenu,
		sizeof(**outMenu),
		SJME_SCRITCHUI_TYPE_MENU,
		(sjme_scritchui_coreGeneric_commonNewImplFunc)
			inState->impl->menuNew);
}
