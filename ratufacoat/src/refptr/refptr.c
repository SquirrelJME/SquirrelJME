/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "refptr/refptr.h"
#include "memoryintern.h"

sjme_jboolean sjme_refPtrCleanup__(void* refStruct, sjme_error* error)
{
	if (refStruct == NULL)
	{
		if (!sjme_hasError(error))
			sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	return sjme_true;
}
