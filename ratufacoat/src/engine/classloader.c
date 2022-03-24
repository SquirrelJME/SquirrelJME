/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "engine/classloader.h"
#include "debug.h"

sjme_jboolean sjme_classLoaderNew(sjme_classLoader** outLoader,
	sjme_classPath* classPath, sjme_error* error)
{
	if (outLoader == NULL || classPath == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
}
