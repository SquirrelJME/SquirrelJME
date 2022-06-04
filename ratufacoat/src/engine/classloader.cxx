/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "engine/classloader.h"
#include "debug.h"
#include "memory.h"

sjme_jboolean sjme_classLoaderNew(sjme_classLoader** outLoader,
	sjme_classPath* classPath, sjme_error* error)
{
	sjme_classLoader* result;
	sjme_classPath* dupPath;
	sjme_jint i;

	if (outLoader == NULL || classPath == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Allocate resultant structure. */
	result = (sjme_classLoader*)sjme_malloc(sizeof(*result), error);
	dupPath = (sjme_classPath*)sjme_malloc(
		SJME_SIZEOF_CLASS_PATH(classPath->count), error);
	if (result == NULL || dupPath == NULL)
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, 0);

		if (result != NULL)
			sjme_free(result, error);
		if (dupPath != NULL)
			sjme_free(dupPath, error);

		return sjme_false;
	}

	/* Duplicate entries, since we might have more entries being added to
	 * our classpath in the future such as injected libraries and otherwise,
	 * and we want to allow room to be left or have possibilities for it. */
	dupPath->count = classPath->count;
	for (i = 0; i < dupPath->count; i++)
		dupPath->libraries[i] = classPath->libraries[i];

	/* All fine! */
	*outLoader = result;
	return sjme_true;
}
