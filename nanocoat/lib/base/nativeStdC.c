/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/intern/nal.h"

#pragma region(getenv)
#if (SJME_CONFIG_NAL_GETENV == SJME_CONFIG_NAL_IMPLEMENT_STDC)

sjme_errorCode sjme_nal_default_getEnv(
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
	len = sjme_util_sizeToInt(strlen(value));
	if (len < 0 || len + 1 > bufLen)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Copy over. */
	memmove(buf, value, sizeof(*buf) * (len + 1));
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(getenv)

#pragma region(pipe)
#if (SJME_CONFIG_NAL_PIPE == SJME_CONFIG_NAL_IMPLEMENT_STDC)

sjme_errorCode sjme_nal_default_stdErr(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	sjme_errorCode error;

	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || (off + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	error = SJME_ERROR_NONE;
	if (fwrite(SJME_POINTER_OFFSET(buf, off), len, 1,
		stderr) <= 0)
		error = SJME_ERROR_IO_EXCEPTION;
	if (EOF == fflush(stderr))
		error = SJME_ERROR_IO_EXCEPTION;
	
	/* Success? */
	return error;
}

sjme_errorCode sjme_nal_default_stdErrFlush(void)
{
	if (EOF == fflush(stderr))
		return SJME_ERROR_IO_EXCEPTION;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nal_default_stdOutFlush(void)
{
	if (EOF == fflush(stdout))
		return SJME_ERROR_IO_EXCEPTION;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nal_default_stdOut(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	sjme_errorCode error;

	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || (off + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	error = SJME_ERROR_NONE;
	if (fwrite(SJME_POINTER_OFFSET(buf, off), len,
		1, stdout) <= 0)
		error = SJME_ERROR_IO_EXCEPTION;
	
	/* Success? */
	return error;
}

#endif
#pragma endregion(pipe)

#pragma region(seekable)
#if (SJME_CONFIG_NAL_SEEKABLE == SJME_CONFIG_NAL_IMPLEMENT_STDC)

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
		rc = sjme_util_sizeToInt(fread(SJME_POINTER_OFFSET(outBuf, destAt),
			1, left, file));
		
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

static sjme_errorCode sjme_nal_default_cFileWrite(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNullBuf(length) sjme_buffer inBuf,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	FILE* file;
	sjme_jint left, destAt, rc;
	
	if (inSeekable == NULL || inImplState == NULL || inBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover file handle. */
	file = inSeekable->implState.handle;
	
	/* Seek to write position. */
	if (fseek(file, base, SEEK_SET))
		return sjme_nal_errno(errno);
	
	/* Make sure it is a valid position. */
	if (ftell(file) < 0)
		return sjme_nal_errno(errno);

	/* Write data. */
	rc = sjme_util_sizeToInt(fwrite(inBuf, 1, length, file));
	
	/* These should never happen. */
	if (feof(file) || ferror(file) || rc != length)
		return sjme_nal_errno(errno);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

/** Functions for C File access. */
static const sjme_seekable_functions sjme_nal_default_cFileFunctions =
{
	sjme_sm(.close, sjme_nal_default_cFileClose),
	sjme_sm(.init, sjme_nal_default_cFileInit),
	sjme_sm(.read, sjme_nal_default_cFileRead),
	sjme_sm(.size, sjme_nal_default_cFileSize),
	sjme_sm(.write, sjme_nal_default_cFileWrite),
};

sjme_errorCode sjme_nal_default_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInValue sjme_nal_openMode openMode)
{
	sjme_errorCode error;
	FILE* cFile;
	sjme_seekable result;
	sjme_lpcstr mode;
	
	if (allocPool == NULL || inPath == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (openMode < SJME_NAL_OPEN_READ ||
		openMode > SJME_NAL_OPEN_WRITE_TRUNCATE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Which mode? */
	if (openMode == SJME_NAL_OPEN_WRITE_TRUNCATE)
		mode = "wb";
	else if (openMode == SJME_NAL_OPEN_WRITE_EXIST)
		mode = "r+b";
	else
		mode = "rb";
	
	/* Open file. */
	cFile = fopen(inPath, mode);
	if (cFile == NULL)
		return sjme_nal_errno(errno);

	/* Always seek to the start. */
	fseek(cFile, 0, SEEK_SET);
	
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
}

#endif
#pragma endregion(seekable)

