/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/stream.h"
#include "sjme/native.h"

static sjme_errorCode sjme_stream_outputStdIoClose(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_nal_stdIo* nal;
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover NAL. */
	nal = inImplState->handle.p;
	if (nal == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Close output, ignore if implementation is missing. */
	if (nal->close == NULL)
		return SJME_ERROR_NONE;
	return nal->close();
}

static sjme_errorCode sjme_stream_outputStdIoFlush(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_nal_stdIo* nal;
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover NAL. */
	nal = inImplState->handle.p;
	if (nal == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Flush output, ignore if implementation is missing. */
	if (nal->flush == NULL)
		return SJME_ERROR_NONE;
	return nal->flush();
}

static sjme_errorCode sjme_stream_outputStdIoInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_nal_stdIo* nal;

	nal = data;
	if (stream == NULL || inImplState == NULL || nal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Store handle. */
	inImplState->handle.p = nal;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputStdIoWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_cpointer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_nal_stdIo* nal;
	
	if (stream == NULL || inImplState == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Recover NAL. */
	nal = inImplState->handle.p;
	if (nal == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Write to the output. */
	if (nal->out == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	return nal->out(buf, 0, length);
}

static const sjme_stream_outputFunctions sjme_stream_outputStdIoFunctions =
{
	.close = sjme_stream_outputStdIoClose,
	.flush = sjme_stream_outputStdIoFlush,
	.init = sjme_stream_outputStdIoInit,
	.write = sjme_stream_outputStdIoWrite,
};

sjme_errorCode sjme_stream_outputOpenStdIo(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull sjme_nal_stdIo* nal)
{
	if (allocPool == NULL || outStream == NULL || nal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Open target stream. */
	return sjme_stream_outputOpen(
		allocPool, outStream, &sjme_stream_outputStdIoFunctions,
		nal, NULL);
}
