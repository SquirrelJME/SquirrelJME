/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/util.h"
#include "sjme/inflate.h"

/**
 * Inflation initializer state.
 * 
 * @since 2024/08/30
 */
typedef struct sjme_stream_inflateInit
{
	/** First handle. */
	sjme_pointer handle;
} sjme_stream_inflateInit;

static sjme_errorCode sjme_stream_inputInflateClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_errorCode error;
	sjme_inflate* state;
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do nothing if already closed. */
	state = inImplState->handle;
	if (state == NULL)
		return SJME_ERROR_NONE;
	
	/* Destroy it. */
	if (sjme_error_is(error = sjme_inflate_destroy(state)))
		return sjme_error_default(error);
	
	/* The state is now not valid. */
	inImplState->handle = NULL;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_inflateInit* init;
	
	init = data;
	if (stream == NULL || inImplState == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set data. */
	inImplState->handle = init->handle;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	sjme_inflate* inState;
	sjme_jint count;
	
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Recover state, fail if closed. */
	inState = inImplState->handle;
	if (inState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Request inflated data. */
	count = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_inflate(inState,
		&count, dest, length)) || count == INT32_MAX)
		return sjme_error_default(error);
	
	/* Success! */
	*readCount = count;
	return SJME_ERROR_NONE;
}

/** Input deflate functions. */
static const sjme_stream_inputFunctions sjme_stream_inputInflateFunctions =
{
	.close = sjme_stream_inputInflateClose,
	.init = sjme_stream_inputInflateInit,
	.read = sjme_stream_inputInflateRead,
};

sjme_errorCode sjme_stream_inputOpenInflate(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_stream_input inCompressed)
{
	sjme_errorCode error;
	sjme_stream_input result;
	sjme_stream_inflateInit init;
	sjme_inflate* state;
	
	if (inPool == NULL || outStream == NULL || inCompressed == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup decompression state data. */
	state = NULL;
	if (sjme_error_is(error = sjme_inflate_new(inPool,
		&state, inCompressed)) || state == NULL)
		goto fail_inflateNew;
	
	/* Set initialization data. */
	memset(&init, 0, sizeof(init));
	init.handle = state;
	
	/* Setup sub-stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_stream_inputOpen(inPool,
		&result, &sjme_stream_inputInflateFunctions,
		&init, NULL)) || result == NULL)
		goto fail_open;
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_open:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	
fail_inflateNew:
	if (state != NULL)
		sjme_inflate_destroy(state);

	return sjme_error_default(error);
}
