/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/intern/nal.h"

#if (SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_POSIX)
	#include <time.h>
#endif

#pragma region(nanotime)
#if (SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_POSIX)

sjme_errorCode sjme_nal_default_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
	struct timespec spec;
	
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get system native clock. */
	memset(&spec, 0, sizeof(spec));
	if (clock_gettime(CLOCK_MONOTONIC, &spec) != 0)
		return SJME_ERROR_NATIVE_SYSTEM_CLOCK_FAILURE;
	
	/* Translate time. */
	result->full = spec.tv_nsec + (spec.tv_sec * UINT64_C(1000000000));
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(nanotime)

#pragma region(threadSleep)
#if (SJME_CONFIG_NAL_THREAD_SLEEP == SJME_CONFIG_NAL_IMPLEMENT_POSIX)

sjme_errorCode sjme_nal_default_threadSleep(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
	struct timespec request;
	sjme_jint seconds, mod;
	
	/* Yield instead. */
	if (millis <= 0 && nanos <= 0)
		return sjme_nal_default_threadYield();
	
	/* Calculate seconds. */
	seconds = millis / 1000;
	mod = millis % 1000;

	/* Sleep for the given amount of time. */
	request.tv_sec = seconds;
	request.tv_nsec = nanos + (mod * 1000000);
	nanosleep(&request, NULL);

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadSleep)
