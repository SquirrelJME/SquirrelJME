/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/debug.h"

#include "lib/jnisjme/jsInputStream.h"

/**
 * Data for input streams.
 *
 * @since 2026/06/02
 */
typedef struct sjme_js_inputStreamData
{
	/** The JNI Environment. */
	JNIEnv* jEnv;

	/** The InputStream object. */
	jobject jInputStream;
} sjme_js_inputStreamData;

static sjme_errorCode sjme_js_inputStreamAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	if (stream == NULL || inImplState == NULL || outAvail == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_js_inputStreamClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_js_inputStreamInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_js_inputStreamRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_inputFunctions sjme_js_inputStreamFuncs =
{
	sjme_sm(.available, sjme_js_inputStreamAvailable),
	sjme_sm(.close, sjme_js_inputStreamClose),
	sjme_sm(.init, sjme_js_inputStreamInit),
	sjme_sm(.read, sjme_js_inputStreamRead),
};

sjme_errorCode sjme_js_wrapInputStream(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull JNIEnv* jEnv,
	sjme_attrInNotNull jobject jInputStream)
{
	sjme_errorCode error;
	sjme_js_inputStreamData data;

	if (allocPool == NULL || outStream == NULL ||
		jEnv == NULL || jInputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup data. */
	memset(&data, 0, sizeof(data));
	data.jEnv = jEnv;
	data.jInputStream = jInputStream;

	/* Open input stream. */
	if (sjme_error_is(error = sjme_stream_inputOpen(allocPool, outStream,
		&sjme_js_inputStreamFuncs, &data, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}
