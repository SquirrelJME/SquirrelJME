/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiText.h"
#include "sjme/debug.h"

sjme_errorCode sjme_scritchui_textDeleteStatic(
	sjme_attrInOutNotNull sjme_scritchui_text inOutText)
{
	if (inOutText == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented();
}

sjme_errorCode sjme_scritchui_textNewStatic(
	sjme_attrInOutNotNull sjme_scritchui_text inOutText)
{
	if (inOutText == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented();
}
