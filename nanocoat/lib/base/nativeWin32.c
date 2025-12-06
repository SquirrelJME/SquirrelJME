/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/intern/nal.h"

#if defined(SJME_CONFIG_NAL_HAS_ANY_WIN32)
	#define WIN32_LEAN_AND_MEAN 1

	#include <windows.h>

	#undef WIN32_LEAN_AND_MEAN
#endif

#if (SJME_CONFIG_NAL_TCP_UDP == SJME_CONFIG_NAL_IMPLEMENT_WIN32)
	#include <winsock2.h>
#endif

#pragma region(nanotime)
#if (SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
	LARGE_INTEGER freq;
	LARGE_INTEGER ticks;
	
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

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
}

#endif
#pragma endregion(nanotime)

#pragma region(tcpUdp)
#if (SJME_CONFIG_NAL_TCP_UDP == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

typedef struct sjme_stream_biNetSocketData
{
	/** The socket descriptor. */
	SOCKET sfd;

	/** The listening file descriptor. */
	SOCKET lfd;

	/** The remote file descriptor. */
	SOCKET rfd;
} sjme_stream_biNetSocketData;

static sjme_errorCode sjme_stream_inputNetAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	if (stream == NULL || inImplState == NULL || outAvail == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputNetClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputNetInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_biNetSocketData* socketData;

	socketData = (sjme_stream_biNetSocketData*)data;
	if (stream == NULL || inImplState == NULL || socketData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just copy the FDs over. */
	inImplState->handle.i = socketData->sfd;
	inImplState->handleTwo.i = socketData->rfd;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputNetRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_inputFunctions sjme_stream_inputNetFunctions =
{
	sjme_sm(.available, sjme_stream_inputNetAvailable),
	sjme_sm(.close, sjme_stream_inputNetClose),
	sjme_sm(.init, sjme_stream_inputNetInit),
	sjme_sm(.read, sjme_stream_inputNetRead),
};

static sjme_errorCode sjme_stream_outputNetClose(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetFlush(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_biNetSocketData* socketData;

	socketData = (sjme_stream_biNetSocketData*)data;
	if (stream == NULL || inImplState == NULL || socketData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Just copy the FDs over. */
	inImplState->handle.i = socketData->sfd;
	inImplState->handleTwo.i = socketData->rfd;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputNetWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_buffer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_outputFunctions sjme_stream_outputNetFunctions =
{
	sjme_sm(.close, sjme_stream_outputNetClose),
	sjme_sm(.flush, sjme_stream_outputNetFlush),
	sjme_sm(.init, sjme_stream_outputNetInit),
	sjme_sm(.write, sjme_stream_outputNetWrite),
};

sjme_errorCode sjme_nal_default_tcpUdp(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port)
{
	sjme_errorCode error;
	sjme_stream_input rawIn;
	sjme_stream_output rawOut;
	sjme_stream_biNetSocketData data;
	
	if (allocPool == NULL || (netIn == NULL && netOut == NULL) ||
		(!listening && address == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (port < 1 || port > 65535)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialize blank stream state. */
	memset(&data, 0, sizeof(data));
	error = SJME_ERROR_NONE;
	rawIn = NULL;
	rawOut = NULL;

	if (SJME_JNI_TRUE)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Open output stream. */
	if (sjme_error_is(error = sjme_stream_outputOpen(allocPool,
		&rawOut, &sjme_stream_outputNetFunctions,
		&data, NULL)) || rawOut == NULL)
		goto fail_openOut;

	/* Open input stream. */
	if (sjme_error_is(error = sjme_stream_inputOpen(allocPool,
		&rawIn, &sjme_stream_inputNetFunctions,
		&data, NULL)) || rawIn == NULL)
		goto fail_openIn;

	/* Do we not care about the input stream? Close it! */
	if (rawIn != NULL && netIn == NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(rawIn))))
			goto fail_closeIn;
		rawIn = NULL;
	}

	/* Ditto for the output stream if we do not care for it. */
	if (rawOut != NULL && netOut == NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(rawOut))))
			goto fail_closeOut;
		rawOut = NULL;
	}

	/* Return resultant raw streams */
	if (netIn != NULL)
		*netIn = rawIn;
	if (netOut != NULL)
		*netOut = rawOut;

	/* Success! */
	return SJME_ERROR_NONE;

fail_closeIn:
fail_closeOut:
fail_openIn:
	if (rawIn != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawIn));
fail_openOut:
	if (rawOut != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawOut));

	/* Is a standard error set? */
	if (sjme_error_is(error))
		return sjme_error_default(error);

	/* Use errno, if possible. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

#endif
#pragma endregion(tcpUdp)

#pragma region(threadSleep)
#if (SJME_CONFIG_NAL_THREAD_SLEEP == SJME_CONFIG_NAL_IMPLEMENT_WIN32)

sjme_errorCode sjme_nal_default_threadSleep(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
	LARGE_INTEGER baseTime;
	
	/* Yield instead. */
	if (millis <= 0 && nanos <= 0)
		return sjme_nal_default_threadYield();
		
	/* Sleep for the given number of milliseconds. */
	if (millis > 0)
		Sleep(millis);

	/* Burn the CPU to consume the nanoseconds. */
	QueryPerformanceCounter(&baseTime);
	while (nanos > 0)
		nanos = 0; /* TODO */

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadSleep)

#pragma region(threadYield)
#if (SJME_CONFIG_NAL_THREAD_YIELD == SJME_CONFIG_NAL_IMPLEMENT_WIN32)
	
sjme_errorCode sjme_nal_default_threadYield(void)
{
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_NT_4)
	if (!SwitchToThread())
		SetLastError(0);
#else
	Sleep(0);
#endif

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadYield)
