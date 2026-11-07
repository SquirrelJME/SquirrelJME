/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* Assume limits.h exists. */
#if !defined(SJME_CONFIG_HAS_LIMITS_H)
	#define SJME_CONFIG_HAS_LIMITS_H
#endif

#include "sjme/config.h"

/* Prevent jlong atomic from being used. */
#if !defined(SJME_CONFIG_HAS_NO_JLONG_ATOMIC)
	#define SJME_CONFIG_HAS_NO_JLONG_ATOMIC
#endif

#if defined(SJME_CONFIG_HAS_JLONG_ATOMIC)
	#undef SJME_CONFIG_HAS_JLONG_ATOMIC
#endif

#include "sjme/atomic.h"

#define sjme_atomic_type sjme_jlongNative
#include "sjme/atomicTemplate.h" /* NOLINT */

#define sjme_atomic_type sjme_jlongNative
#include "sjme/atomicTemplateC.h" /* NOLINT */

int main(int argc, char** argv)
{
	sjme_atomic(sjme_jlongNative) test;
	
	/* Call all the functions so they get linked. */
	sjme_atomic_s(sjme_jlongNative, &test, 1);
	sjme_atomic_cs(sjme_jlongNative, &test, 1, 2);
	sjme_atomic_g(sjme_jlongNative, &test);
	sjme_atomic_gl(sjme_jlongNative, &test);
	sjme_atomic_ga(sjme_jlongNative, &test, 1);
	
	return 0;
}
