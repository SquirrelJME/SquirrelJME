/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/jdwp.h"

sjme_errorCode sjme_jdwp_sessionNew(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInNotNull sjme_stream_input in,
	sjme_attrInNotNull sjme_stream_output out)
{
	if (allocPool == NULL || inState == NULL || outSession == NULL ||
		in == NULL || out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
