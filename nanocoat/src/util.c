/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/util.h"
#include "sjme/debug.h"

sjme_jchar sjme_decodeUtfChar(const char* at, const char** stringP)
{
	if (at == NULL)
		return -1;
	
	sjme_todo("sjme_decodeUtfChar()");
	return -1;
}

/**
 * Initializes the random number generator.
 * 
 * @param outRandom The random state to initialize. 
 * @param seedHi The high seed value.
 * @param seedLo The low seed value.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/12/02
 */
sjme_jboolean sjme_randomInit(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jint seedHi,
	sjme_attrInValue sjme_jint seedLo)
{
	sjme_todo("Implement this?");
	return SJME_JNI_FALSE;
}

sjme_jboolean sjme_randomInitL(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jlong seed)
{
	sjme_todo("Implement this?");
	return SJME_JNI_FALSE;
}

sjme_jboolean sjme_randomNextInt(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue)
{
	sjme_todo("Implement this?");
	return SJME_JNI_FALSE;
}
	
sjme_jboolean sjme_randomNextIntMax(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInPositiveNonZero sjme_jint maxValue)
{
	sjme_todo("Implement this?");
	return SJME_JNI_FALSE;
}

sjme_jint sjme_stringHash(const char* string)
{
	sjme_jint result;
	sjme_jchar c;
	const char* p;
	
	if (string == NULL)
		return 0;
	
	/* Initial result. */
	result = 0;
	
	/* Read until end of string. */
	for (p = string; *p != 0;)
	{
		/* Decode character. */
		c = sjme_decodeUtfChar(p, &p);
		
		/* Calculate the hashCode(), the JavaDoc gives the following formula:
		// == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes! */
		result = ((result << 5) - result) + (sjme_jint)c;
	}
	
	/* Return calculated result. */
	return result;
}

sjme_jint sjme_stringLength(const char* string)
{
	if (string == NULL)
		return -1;
		
	sjme_todo("sjme_stringLength()");
	return -1;
}

sjme_jint sjme_treeFind(void* in, void* what,
	const sjme_treeFindFunc* functions)
{
	if (in == NULL || functions == NULL)
		return -1;
	
	sjme_todo("sjme_treeFind()");
	return -1;
}
