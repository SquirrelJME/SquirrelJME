/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"

sjme_errorCode sjme_scritchui_core_intern_getChoice(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiChoice* outChoice)
{
	if (inState == NULL || inComponent == NULL || outChoice == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemGet(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemSetImage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_choiceLength(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
