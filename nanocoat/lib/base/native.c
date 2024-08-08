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
	.getEnv = NULL,
	.nanoTime = sjme_nal_default_nanoTime,
	.stdOutF = sjme_nal_default_stdOutF,
};
