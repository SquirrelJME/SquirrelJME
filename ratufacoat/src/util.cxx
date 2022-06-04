/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "util.h"

sjme_jint sjme_strIndexOf(const char* string, char want)
{
	sjme_jint i = 0;

	/* Find the character. */
	do
	{
		if (string[i] == want)
			return i;
	} while (string[i++] != '\0');

	/* Not found. */
	return -1;
}
