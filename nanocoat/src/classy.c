/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/classy.h"
#include "sjme/debug.h"

sjme_errorCode sjme_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_info* outClass)
{
	if (inPool == NULL || inStream == NULL || outClass == NULL)
		return SJME_ERROR_NONE;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
