/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/multithread.h"
#include "sjme/native.h"

#if defined(SJME_CONFIG_ONLY_THREAD_SINGLE)

/** The only available thread. */
static const sjme_thread sjme_singleCurrent;

sjme_errorCode sjme_thread_current(
	sjme_attrInOutNotNull sjme_thread* outThread)
{
	sjme_thread result;
	
	if (outThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Threading is not supported, so always refer to a virtual ID. */
	*outThread = sjme_singleCurrent;
	
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_thread_equal(
	sjme_attrInNullable sjme_thread aThread,
	sjme_attrInNullable sjme_thread bThread)
{
	if ((aThread == SJME_THREAD_NULL) != (bThread == SJME_THREAD_NULL))
		return SJME_JNI_FALSE;
	
	return aThread == bThread;
}

sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNullable sjme_thread_id* outThreadId,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	/* Threading not supported. */
	return SJME_ERROR_CANNOT_CREATE;
}

sjme_errorCode sjme_thread_wake(
	sjme_attrInNotNull sjme_thread inThread)
{
	/* No native support. */
	return SJME_ERROR_NONE;
}

#endif
