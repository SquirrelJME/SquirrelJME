/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* ReSharper disable CppUnusedIncludeDirective */

#include "sjme/atomic.h"

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

#define sjme_atomic_type sjme_jint
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_juint
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_lpstr
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_lpcstr
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_jobject
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_jclass
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_pointer
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_intPointer
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_charSeq
#include "sjme/atomicTemplateC.h"

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
