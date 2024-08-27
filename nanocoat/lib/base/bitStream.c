/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/bitStream.h"

sjme_errorCode sjme_bitStream_inputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_bitStream_inputReadByteFunc readFunc,
	sjme_attrInNullable sjme_pointer readFuncData,
	sjme_attrInNullable sjme_closeable forwardClose)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NULL_ARGUMENTS;
}

sjme_errorCode sjme_bitStream_inputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NULL_ARGUMENTS;
}

sjme_errorCode sjme_bitStream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_bitStream_outputWriteByteFunc writeFunc,
	sjme_attrInNullable sjme_pointer writeFuncData,
	sjme_attrInNullable sjme_closeable forwardClose)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NULL_ARGUMENTS;
}
