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

sjme_jint SJME_COMPARATOR(sjme_lpcstr, 0)(const void* a, const void* b,
	int elementSize)
{
	if (a == NULL || b == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_jint SJME_COMPARATOR_INSENSITIVE(sjme_lpcstr, 0)(
	const void* a, const void* b, int elementSize)
{
	if (a == NULL || b == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
