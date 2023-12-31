/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/stream.h"
#include "sjme/debug.h"

sjme_errorCode sjme_stream_inputClose(
	sjme_attrInNotNull sjme_stream_input stream)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull void* buffer,
	sjme_attrInPositive sjme_jint length)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_inputRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_inputReadIter(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) void* dest,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInPositive sjme_jint length)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stream_outputClose(
	sjme_attrInNotNull sjme_stream_output stream)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
