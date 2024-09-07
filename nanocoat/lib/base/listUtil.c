/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/listUtil.h"

sjme_errorCode sjme_listUtil_mapAllLinesR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_void** outList,
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInNotNull sjme_listUtil_newListFunc newList,
	sjme_attrInNotNull sjme_listUtil_mapLineFunc mapper)
{
	if (inPool == NULL || outList == NULL || inputStream == NULL ||
		newList == NULL || mapper == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_listUtil_readAllLines(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	if (inPool == NULL || outList == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
