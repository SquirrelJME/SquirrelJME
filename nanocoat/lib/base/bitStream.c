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
	if (inPool == NULL || resultStream == NULL || readFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length)
{
	if (inPool == NULL || resultStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_inputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_input* resultStream,
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (inPool == NULL || resultStream == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_inputRead(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrOutNotNull sjme_juint* outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount)
{
	if (inStream == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitOrder != SJME_BITSTREAM_LSB && bitOrder != SJME_BITSTREAM_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_outputOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_bitStream_outputWriteByteFunc writeFunc,
	sjme_attrInNullable sjme_pointer writeFuncData,
	sjme_attrInNullable sjme_closeable forwardClose)
{
	if (inPool == NULL || resultStream == NULL || writeFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_outputOpenStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_bitStream_output* resultStream,
	sjme_attrInNotNull sjme_stream_output outputStream,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (inPool == NULL || resultStream == NULL || outputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_bitStream_outputWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInValue sjme_bitStream_order bitOrder,
	sjme_attrInValue sjme_juint outValue,
	sjme_attrInPositiveNonZero sjme_jint bitCount)
{
	if (outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitOrder != SJME_BITSTREAM_LSB && bitOrder != SJME_BITSTREAM_MSB)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
