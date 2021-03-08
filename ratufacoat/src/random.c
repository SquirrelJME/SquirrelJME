/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "random.h"
#include "debug.h"

sjme_returnFail sjme_randomSeed(sjme_randomState* random, sjme_jlong seed,
	sjme_error* error)
{
	sjme_todo("sjme_randomSeed(%p, %08x:%08x, %p",
		random, seed.hi, seed.lo, error);
}

sjme_jboolean sjme_randomNextBoolean(sjme_randomState* random)
{
	sjme_todo("sjme_randomNextBoolean(%s)",
		random);
}

sjme_jint sjme_randomNextInt(sjme_randomState* random)
{
	sjme_todo("sjme_randomNextInt(%s)",
		random);
}
