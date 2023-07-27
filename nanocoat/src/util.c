/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/util.h"
#include "sjme/debug.h"

jchar sjme_decodeUtfChar(const char* at, const char** stringP)
{
	sjme_todo("sjme_decodeUtfChar()");
	return -1;
}

jint sjme_hashString(const char* string)
{
	jint result;
	jchar c;
	const char* p;
	
	/* Initial result. */
	result = 0;
	
	/* Read until end of string. */
	for (p = string; *p != 0;)
	{
		/* Decode character. */
		c = sjme_decodeUtfChar(p, &p);
		
		/* Calculate the hashCode(), the JavaDoc gives the following formula:
		// == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes! */
		result = ((result << 5) - result) + (jint)c;
	}
	
	/* Return calculated result. */
	return result;
}

jint sjme_treeFind(void* in, void* what,
	const sjme_treeFindFunc* functions)
{
	sjme_todo("sjme_decodeUtfChar()");
	return -1;
}
