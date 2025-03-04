/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

#if !defined(SJME_CONFIG_HAS_NO_ERRNO)
	#include <errno.h>
#endif

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

#if !defined(SJME_CONFIG_HAS_NO_STDIO)
static sjme_errorCode sjme_nal_default_cFileClose(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState)
{
	FILE* file;
	
	if (inSeekable == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover file handle. */
	file = inSeekable->implState.handle;
	
	/* Close the file. */
	if (0 != fclose(file))
		return sjme_nal_errno(errno);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nal_default_cFileInit(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	if (inSeekable == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We can just set the file handle here. */
	inImplState->handle = data;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nal_default_cFileRead(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	FILE* file;
	sjme_jint left, destAt, rc;
	
	if (inSeekable == NULL || inImplState == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover file handle. */
	file = inSeekable->implState.handle;
	
	/* Seek to read position. */
	if (fseek(file, base, SEEK_SET))
		return sjme_nal_errno(errno);
	
	/* Make sure it is a valid position. */
	if (ftell(file) < 0)
		return sjme_nal_errno(errno);
	
	/* fread() can result in short reads, so read everything fully. */
	destAt = 0;
	left = length;
	while (left > 0)
	{
		/* Read chunk. */
		rc = fread(SJME_POINTER_OFFSET(outBuf, destAt),
			1, left, file);
		
		/* These should never happen. */
		if (feof(file) || ferror(file))
			return sjme_nal_errno(errno);
		
		/* Move shift up. */
		destAt += rc;
		left -= rc;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nal_default_cFileSize(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	FILE* file;
	sjme_jint result;
	
	if (inSeekable == NULL || inImplState == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover file handle. */
	file = inSeekable->implState.handle;
	
	/* Seek to end. */
	if (fseek(file, 0, SEEK_END) < 0)
		return sjme_nal_errno(errno);
	
	/* File size is the given position. */
	result = ftell(file);
	if (result < 0)
		return sjme_nal_errno(errno);
	
	/* Success! */
	*outSize = result;
	return SJME_ERROR_NONE;
}

/** Functions for C File access. */
static const sjme_seekable_functions sjme_nal_default_cFileFunctions =
{
	.close = sjme_nal_default_cFileClose,
	.init = sjme_nal_default_cFileInit,
	.read = sjme_nal_default_cFileRead,
	.size = sjme_nal_default_cFileSize,
};
#endif

static sjme_errorCode sjme_nal_default_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable)
{
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
	sjme_errorCode error;
	FILE* cFile;
	sjme_seekable result;
#endif
	
	if (allocPool == NULL || inPath == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
	/* Open file. */
	cFile = fopen(inPath, "rb");
	if (cFile == NULL)
		return sjme_nal_errno(errno);
	
	/* Setup stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_seekable_open(allocPool,
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
#else
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
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

static sjme_errorCode sjme_nal_default_stdErr(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	sjme_errorCode error;

	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || (buf + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
	error = SJME_ERROR_NONE;
	if (fwrite(SJME_POINTER_OFFSET(buf, off), len, 1, stderr) <= 0)
		error = SJME_ERROR_IO_EXCEPTION;
	if (EOF == fflush(stderr))
		error = SJME_ERROR_IO_EXCEPTION;
	
#else
	error = SJME_ERROR_NONE;
#endif
	
	/* Success? */
	return error;
}

static sjme_errorCode sjme_nal_default_stdOut(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	sjme_errorCode error;

	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || (buf + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
	error = SJME_ERROR_NONE;
	if (fwrite(SJME_POINTER_OFFSET(buf, off), len, 1, stdout) <= 0)
		error = SJME_ERROR_IO_EXCEPTION;
	
#else
	error = SJME_ERROR_NONE;
#endif
	
	/* Success? */
	return error;
}

const sjme_nal sjme_nal_default =
{
	.currentTimeMillis = NULL,
	.fileOpen = sjme_nal_default_fileOpen,
	.getEnv = sjme_nal_default_getEnv,
	.nanoTime = sjme_nal_default_nanoTime,
	{
		{
		},
		{
			.out = sjme_nal_default_stdOut,
		},
		{
			.out = sjme_nal_default_stdErr
		},
	},
};

#if !defined(SJME_CONFIG_HAS_NO_ERRNO)
sjme_errorCode sjme_nal_errno(sjme_jint errNum)
{
	switch (errNum)
	{
		case EIO:
			return SJME_ERROR_IO_EXCEPTION;
		
		case ENOENT:
			return SJME_ERROR_FILE_NOT_FOUND;

		default:
			return SJME_ERROR_UNKNOWN;
	}
}
#endif

sjme_errorCode sjme_nal_stdF(
	sjme_attrInNotNull sjme_nal_stdOFunc outFunc,
	sjme_attrInNotNull sjme_lpcstr format,
	...)
{
	sjme_errorCode error;
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
#define BUF_SIZE 512
	va_list list;
	sjme_cchar buf[BUF_SIZE];
#endif
	
	if (outFunc == NULL || format == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
	/* Start argument parsing. */
	va_start(list, format);
	
	/* Print to buffer */
	error = SJME_ERROR_NONE;
	memset(buf, 0, sizeof(buf));
	if (vsnprintf(buf, BUF_SIZE - 1, format, list) < 0)
		error = SJME_ERROR_IO_EXCEPTION;
	buf[BUF_SIZE - 1] = '\0';
		
	/* End argument parsing. */
	va_end(list);
#else
	error = SJME_ERROR_NONE;
#endif

	return error;
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
#undef BUF_SIZE
#endif
}
