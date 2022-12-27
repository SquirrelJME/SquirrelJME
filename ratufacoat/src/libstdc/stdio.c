/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <string.h>

FILE* stdout;

FILE* stderr;

int fputs(const char* string, FILE* file)
{
	size_t len;

	len = strlen(string);
	return file->write(file, string, 0, len);
}

int puts(const char* string)
{
	int result;

	/* Forward to fputs(). */
	result = 0;
	result |= fputs(string, stdout);
	result |= fputs("\n", stdout);

	return result;
}
