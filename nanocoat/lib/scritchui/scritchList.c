/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/core.h"
#include "sjme/alloc.h"
#include "lib/scritchui/core/coreGeneric.h"

static sjme_errorCode sjme_scritchui_core_listNewWrap(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiList inList,
	sjme_attrInValue const sjme_scritchui_impl_initParamList* init)
{
	if (inState == NULL || inList == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set list type. */
	inList->choice.type = init->type;
	
	/* Final forward. */
	return inState->impl->listNew(inState, inList, init);
}

sjme_errorCode sjme_scritchui_core_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType)
{
	sjme_scritchui_impl_initParamList init;
	
	if (inState == NULL || outList == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inChoiceType < 0 || inChoiceType >= SJME_SCRITCHUI_NUM_CHOICE_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (inState->impl->listNew == NULL)
		return sjme_error_notImplemented();
	
	/* Setup initialization data. */
	memset(&init, 0, sizeof(init));
	init.type = inChoiceType;
	
	/* Use generic function. */
	return sjme_scritchui_coreGeneric_componentNew(inState,
		(sjme_scritchui_uiComponent*)outList,
		sizeof(**outList),
		SJME_SCRITCHUI_TYPE_LIST,
		(sjme_scritchui_coreGeneric_componentNewImplFunc)
			sjme_scritchui_core_listNewWrap,
		&init);
}
