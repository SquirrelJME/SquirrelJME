/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/atomic.h"
#include "sjme/atomicImpl.h"

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

SJME_ATOMIC_FUNCTION(sjme_jint, 0)

SJME_ATOMIC_FUNCTION(sjme_juint, 0)

SJME_ATOMIC_FUNCTION(sjme_lpstr, 0) /* NOLINT(*-non-const-parameter) */

SJME_ATOMIC_FUNCTION(sjme_lpcstr, 0) /* NOLINT(*-non-const-parameter) */

SJME_ATOMIC_FUNCTION(sjme_jobject, 0)

SJME_ATOMIC_FUNCTION(sjme_jclass, 0)

SJME_ATOMIC_FUNCTION(sjme_pointer, 0)

SJME_ATOMIC_FUNCTION(sjme_intPointer, 0)

SJME_ATOMIC_FUNCTION(sjme_thread, 0)

SJME_ATOMIC_FUNCTION(sjme_charSeq, 0)

/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */

#if defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE)
void sjme_atomic_interruptsDisable(void)
{
	sjme_todo("Impl?");
}

void sjme_atomic_interruptsEnable(void)
{
	sjme_todo("Impl?");
}
#endif
