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
