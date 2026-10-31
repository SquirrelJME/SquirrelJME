/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Software integer math.
 * 
 * @file
 * @since 2026/10/31
 */

#ifndef SJME_C_SQUIRRELJME_SOFTINTEGER_H
#define SJME_C_SQUIRRELJME_SOFTINTEGER_H

#include "sjme/config.h"
#include "sjme/stdTypes.h"
#include "sjme/util.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SOFTINTEGER_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Java Signed shift left. */
#define sjme_shl(a, b) \
	((sjme_jint)sjme_util_intOverShift((sjme_jint)(a), \
		(sjme_jint)(b) & INT32_C(0x1F)))

/** Java Signed shift right. */
#define sjme_shr(a, b) \
	((sjme_jint)sjme_util_intOverShift((sjme_jint)(a), \
		-((sjme_jint)(b) & INT32_C(0x1F))))

/** Java Unsigned shift right. */
#define sjme_ushr(a, b) \
	((sjme_jint)sjme_util_intOverShiftU((sjme_jint)(a), \
		-((sjme_jint)(b) & INT32_C(0x1F))))

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SOFTINTEGER_H
}
#undef SJME_CXX_SQUIRRELJME_SOFTINTEGER_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SOFTINTEGER_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SOFTINTEGER_H */
