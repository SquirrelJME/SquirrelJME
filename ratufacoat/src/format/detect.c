/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/detect.h"
#include "debug.h"

sjme_jboolean sjme_detectMagicNumber(void* data, sjme_jint size,
	sjme_jint magic, sjme_error* error)
{
	/* Check parameters first. */
	if (data == NULL || size < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
		return sjme_false;
	}
	
	sjme_todo("sjme_detectMagicNumber()");
}
