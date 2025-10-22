/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/comparator.h"
#include "sjme/debug.h"

/**
 * Defines a generic comparator, using a simple subtraction.
 *
 * @param type The type to compare.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/03
 */
#define SJME_COMPARATOR_GENERIC(type, numPointerStars) \
	sjme_jint SJME_COMPARATOR(type, numPointerStars)( \
		sjme_cpointer a, sjme_cpointer b, int elementSize) \
	{ \
		return (sjme_jint)(*((const type*)b) - *((const type*)a)); \
	}

/** Generic @link sjme_jbyte @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_jbyte, 0)

/** Generic @link sjme_jubyte @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_jubyte, 0)

/** Generic @link sjme_jshort @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_jshort, 0)

/** Generic @link sjme_jchar @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_jchar, 0)

/** Generic @link sjme_jint @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_jint, 0)

/** Generic @link sjme_juint @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_juint, 0)

/** Generic @link sjme_cchar @endlink comparator. */
SJME_COMPARATOR_GENERIC(sjme_cchar, 0)

sjme_jint SJME_COMPARATOR(sjme_lpcstr, 0)(sjme_cpointer a, sjme_cpointer b,
	int elementSize)
{
	if (a == NULL || b == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_jint SJME_COMPARATOR_INSENSITIVE(sjme_lpcstr, 0)(
	sjme_cpointer a, sjme_cpointer b, int elementSize)
{
	if (a == NULL || b == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}
