/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "classBuilder.h"

sjme_errorCode sjme_classBuilder_init(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_classBuilder* outState,
	sjme_attrInNullable void* whatever)
{
	if (inPool == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}