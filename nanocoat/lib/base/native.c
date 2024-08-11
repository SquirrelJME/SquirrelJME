/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <string.h>
#include <stdio.h>

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_LINUX) || \
	defined(SJME_CONFIG_HAS_BSD)
	#define SJME_CONFIG_POSIX_CLOCK_GET_TIME
#endif

#if defined(SJME_CONFIG_HAS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1

	#include <windows.h>

	#undef WIN32_LEAN_AND_MEAN
#elif defined(SJME_CONFIG_POSIX_CLOCK_GET_TIME)
	#include <time.h>
#else
#endif

#include "sjme/native.h"


static sjme_errorCode sjme_nal_default_cFileClose(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState)
{
	if (inSeekable == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nal_default_cFileInit(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	if (inSeekable == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nal_default_cFileRead(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNullBuf(length) sjme_jbyte* outBuf,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (inSeekable == NULL || inImplState == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nal_default_cFileSize(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	if (inSeekable == NULL || inImplState == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/** Functions for C File access. */
static const sjme_seekable_functions sjme_nal_default_cFileFunctions =
{
	.close = sjme_nal_default_cFileClose,
	.init = sjme_nal_default_cFileInit,
	.read = sjme_nal_default_cFileRead,
	.size = sjme_nal_default_cFileSize,
};

static sjme_errorCode sjme_nal_default_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable)
{
	sjme_errorCode error;
	FILE* cFile;
	sjme_seekable result;
	
	if (inPool == NULL || inPath == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Open file. */
	cFile = fopen(inPath, "rb");
	if (cFile == NULL)
		return sjme_nal_errno(errno);
	
	/* Setup stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_seekable_open(inPool,
		&result, &sjme_nal_default_cFileFunctions,
		cFile, NULL)) || result == NULL)
	{
		/* Close before we fail. */
		fclose(cFile);
		
		/* Fail. */
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outSeekable = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nal_default_getEnv(
	sjme_attrInNotNull sjme_attrOutNotNullBuf(len) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint bufLen,
	sjme_attrInNotNull sjme_lpcstr env)
{
	sjme_lpcstr value;
	sjme_jint len;
	
	if (buf == NULL || env == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bufLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Get value. */
	value = getenv(env);
	
	/* If missing, fail. */
	if (value == NULL)
		return SJME_ERROR_NO_SUCH_ELEMENT;
	
	/* Check bounds. */
	len = strlen(value);
	if (len < 0 || len + 1 > bufLen)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Copy over. */
	memmove(buf, value, sizeof(*buf) * (len + 1));
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nal_default_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
#if defined(SJME_CONFIG_HAS_WINDOWS)
	LARGE_INTEGER freq;
	LARGE_INTEGER ticks;
#elif defined(SJME_CONFIG_POSIX_CLOCK_GET_TIME)
	struct timespec spec;
#endif
	
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_HAS_WINDOWS)
	/* Get frequency of the clock. */
	memset(&freq, 0, sizeof(freq));
	if (!QueryPerformanceFrequency(&freq))
		return SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE;
	
	/* Get actual counter. */
	memset(&ticks, 0, sizeof(ticks));
	if (!QueryPerformanceCounter(&ticks))
		return SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE;
	
	/* Calculate time. */
	/* Freq: A pointer to a variable that receives the current */
	/* performance-counter frequency, in counts per second. */
	result->full = (ticks.QuadPart / (freq.QuadPart * UINT64_C(1000000000)) /
		UINT64_C(1000000000));
	return SJME_ERROR_NONE;
#elif defined(SJME_CONFIG_POSIX_CLOCK_GET_TIME)
	/* Get system native clock. */
	memset(&spec, 0, sizeof(spec));
	if (clock_gettime(CLOCK_MONOTONIC, &spec) != 0)
		return SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE;
	
	/* Translate time. */
	result->full = spec.tv_nsec + (spec.tv_sec * UINT64_C(1000000000));
	return SJME_ERROR_NONE;
#else
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
}

static sjme_errorCode sjme_nal_default_stdErrF(
	sjme_attrInNotNull sjme_lpcstr format,
	...)
{
	va_list list;
	sjme_errorCode error;
	
	/* Start argument parsing. */
	va_start(list, format);
	
	/* Print directly to formatted output. */
	error = SJME_ERROR_NONE;
	if (vfprintf(stderr, format, list) < 0)
		error = SJME_ERROR_IO_EXCEPTION;
	if (EOF == fflush(stderr))
		error = SJME_ERROR_IO_EXCEPTION;
		
	/* End argument parsing. */
	va_end(list);
	
	/* Success? */
	return error;
}

static sjme_errorCode sjme_nal_default_stdOutF(
	sjme_attrInNotNull sjme_lpcstr format,
	...)
{
	va_list list;
	sjme_errorCode error;
	
	/* Start argument parsing. */
	va_start(list, format);
	
	/* Print directly to formatted output. */
	error = SJME_ERROR_NONE;
	if (vfprintf(stdout, format, list) < 0)
		error = SJME_ERROR_IO_EXCEPTION;
	if (EOF == fflush(stdout))
		error = SJME_ERROR_IO_EXCEPTION;
		
	/* End argument parsing. */
	va_end(list);
	
	/* Success? */
	return error;
}

const sjme_nal sjme_nal_default =
{
	.currentTimeMillis = NULL,
	.fileOpen = sjme_nal_default_fileOpen,
	.getEnv = sjme_nal_default_getEnv,
	.nanoTime = sjme_nal_default_nanoTime,
	.stdErrF = sjme_nal_default_stdErrF,
	.stdOutF = sjme_nal_default_stdOutF,
};

sjme_errorCode sjme_nal_errno(sjme_jint errNum)
{
	switch (errNum)
	{
		case EIO:
			return SJME_ERROR_IO_EXCEPTION;
		
		case ENOENT:
			return SJME_ERROR_FILE_NOT_FOUND;
	}
	
	return SJME_ERROR_UNKNOWN;
}
