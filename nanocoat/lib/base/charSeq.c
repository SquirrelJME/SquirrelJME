/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/charSeq.h"
#include "sjme/debug.h"

sjme_errorCode sjme_charSeq_deleteStatic(
	sjme_attrInNotNull sjme_charSeq* inOutSeq)
{
	if (inOutSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
