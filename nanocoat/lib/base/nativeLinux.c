/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/intern/nal.h"

#if (SJME_CONFIG_NAL_THREAD_YIELD == SJME_CONFIG_NAL_IMPLEMENT_LINUX)
	#include <sched.h>
#endif

#pragma region(threadYield)
#if (SJME_CONFIG_NAL_THREAD_YIELD == SJME_CONFIG_NAL_IMPLEMENT_LINUX)
	
sjme_errorCode sjme_nal_default_threadYield(void)
{
	/* Just tell the schedular to yield. */
	sched_yield();
	
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadYield)
