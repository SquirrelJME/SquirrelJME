/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/stdTypes.h"
#include "sjme/util.h"
#include "sjme/debug.h"

sjme_jint sjme_compare_null(
	sjme_attrInNullable sjme_cpointer a,
	sjme_attrInNullable sjme_cpointer b)
{
	/* Nulls before non-null. */
	if (a == NULL)
	{
		if (b == NULL)
			return 0;
		else
			return -1;
	}
	
	return 1;
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
sjme_errorCode sjme_randomInit(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jint seedHi,
	sjme_attrInValue sjme_jint seedLo)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_randomInitL(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jlong seed)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_randomNextInt(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
	
sjme_errorCode sjme_randomNextIntMax(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInPositiveNonZero sjme_jint maxValue)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_jint sjme_string_charAt(sjme_lpcstr string, sjme_jint index)
{
	sjme_jint at;
	sjme_jchar c;
	sjme_lpcstr p;

	/* Not valid? */
	if (string == NULL)
		return -1;

	/* Read until end of string. */
	at = 0;
	for (p = string; *p != 0;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(p, &p);

		/* Not valid? */
		if (c < 0)
			return -1;

		/* If this is the desired character then return it. */
		if ((at++) == index)
			return c;
	}

	/* Could not find character. */
	return -1;
}

sjme_jint sjme_string_compareN(sjme_lpcstr aString, sjme_jint aLen,
	sjme_lpcstr bString, sjme_jint bLen)
{
	sjme_jint result, limit;
	
	/* Compare null. */
	if (aString == NULL || bString == NULL)
		return sjme_compare_null(aString, bString);
		
	/* Determine the max number of characters to compare. */
	if (aLen < bLen)
		limit = aLen;
	else
		limit = bLen;
	
	/* Compare strings up to the limit. */
	result = strncmp(aString, bString, limit);
	if (result != 0)
		return result;
	
	/* If the lengths differ, smaller is first. */
	if (aLen != bLen)
		return aLen - bLen;
	
	/* Equal otherwise. */
	return 0;
}

sjme_jint sjme_string_decodeChar(sjme_lpcstr at, sjme_lpcstr* stringP)
{
	sjme_jubyte c;
	sjme_jint result;

	if (at == NULL)
		return -1;

	/* Read first character. */
	c = (*(at++)) & 0xFF;

	/* Invalid, cannot be this. */
	if (c == 0)
		return -1;

	/* Single byte character? */
	if ((c & 0x80) == 0)
		result = c;

	/* Double byte character? */
	else if ((c & 0xE0) == 0xC0)
	{
		/* Upper bits. */
		result = (c & 0x1F) << 6;

		/* Read next. */
		c = (*(at++)) & 0xFF;

		/* Invalid continuation? */
		if ((c & 0xC0) != 0x80)
			return -1;

		/* Lower bits. */
		result |= (c & 0x3F);
		
		/* Too low of a character? */
		if (result < 0x80 && result != 0)
			return -1;
	}

	/* Triple byte character. */
	else if ((c & 0xF0) == 0xE0)
	{
		/* Upper bits. */
		result = (c & 0x0F) << 12;

		/* Read next. */
		c = (*(at++)) & 0xFF;

		/* Invalid continuation? */
		if ((c & 0xC0) != 0x80)
			return -1;

		/* Middle bits. */
		result |= (c & 0x3F) << 6;

		/* Read next. */
		c = (*(at++)) & 0xFF;

		/* Invalid continuation? */
		if ((c & 0xC0) != 0x80)
			return -1;

		/* Lower bits. */
		result |= (c & 0x3F);
		
		/* Too low of a character? */
		if (result < 0x800)
			return -1;
	}

	/* Invalid sequence. */
	else
		return -1;

	/* Return the result. */
	if (stringP != NULL)
		*stringP = at;
	return result;
}

sjme_jint sjme_string_hash(sjme_lpcstr string)
{
	sjme_jint result;
	sjme_jint c;
	sjme_lpcstr p;
	
	if (string == NULL)
		return 0;
	
	/* Initial result. */
	result = 0;
	
	/* Read until end of string. */
	for (p = string; *p != 0;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(p, &p);

		/* Not valid. */
		if (c < 0)
			return -1;
		
		/* Calculate the hashCode(), the JavaDoc gives the following formula:
		 * == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes! */
		result = ((result << 5) - result) + (sjme_jint)c;
	}
	
	/* Return calculated result. */
	return result;
}

sjme_jint sjme_string_hashN(sjme_lpcstr string, sjme_jint limit)
{
	sjme_jint result;
	sjme_jint c;
	sjme_lpcstr p, end;
	
	if (string == NULL || limit <= 0)
		return 0;
	
	/* Initial result. */
	result = 0;
	
	/* Read until end of string. */
	for (p = string, end = p + limit; *p != 0 && p < end;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(p, &p);

		/* Not valid. */
		if (c < 0)
			return -1;
		
		/* Calculate the hashCode(), the JavaDoc gives the following formula:
		 * == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes! */
		result = ((result << 5) - result) + (sjme_jint)c;
	}
	
	/* Return calculated result. */
	return result;
}

sjme_jint sjme_string_length(sjme_lpcstr string)
{
	sjme_jint result;
	sjme_jint c;
	sjme_lpcstr p;

	if (string == NULL)
		return -1;

	/* Read until end of string. */
	result = 0;
	for (p = string; *p != 0;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(p, &p);

		/* Not valid? */
		if (c < 0)
			return -1;

		/* Counts as a single character. */
		result++;
	}

	/* Use whatever length we found. */
	return result;
}

sjme_jint sjme_string_lengthN(sjme_lpcstr string, sjme_jint limit)
{
	sjme_jint result;
	sjme_jint c;
	sjme_lpcstr p;
	
	if (string == NULL || limit < 0)
		return -1;

	/* Read until end of string. */
	result = 0;
	for (p = string; *p != 0 && result < limit;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(p, &p);

		/* Not valid? */
		if (c < 0)
			return -1;

		/* Counts as a single character. */
		result++;
	}

	/* Use whatever length we found. */
	return result;
}

sjme_errorCode sjme_swap_shu8_uint_memmove(
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInNotNull sjme_pointer src,
	sjme_attrInPositiveNonZero sjme_jint n)
{
	sjme_juint* iDest;
	sjme_jint i, count;
	
	if (dest == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (n < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Pointless? */
	if (n == 0)
		return SJME_ERROR_NONE;
	
	/* Perform initial move. */
	memmove(dest, src, n);
	
	/* Then perform swapping. */
	iDest = dest;
	count = n / sizeof(sjme_juint);
	for (i = 0; i < count; i++)
		iDest[i] = sjme_swap_uint(iDest[i] << 8);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_swap_uint_memmove(
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInNotNull sjme_pointer src,
	sjme_attrInPositiveNonZero sjme_jint n)
{
	sjme_juint* iDest;
	sjme_jint i, count;
	
	if (dest == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (n < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Pointless? */
	if (n == 0)
		return SJME_ERROR_NONE;
	
	/* Perform initial move. */
	memmove(dest, src, n);
	
	/* Then perform swapping. */
	iDest = dest;
	count = n / sizeof(sjme_juint);
	for (i = 0; i < count; i++)
		iDest[i] = sjme_swap_uint(iDest[i]);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_juint sjme_util_bitCountU(
	sjme_attrInValue sjme_juint v)
{
	v = v - ((v >> 1) & UINT32_C(0x55555555));
	v = (v & UINT32_C(0x33333333)) + ((v >> 2) & UINT32_C(0x33333333));
	return ((v + (v >> 4) & UINT32_C(0xF0F0F0F)) * UINT32_C(0x1010101)) >> 24;
}

sjme_juint sjme_util_highestOneBit(
	sjme_attrInValue sjme_juint v)
{
	v = v | (v >> 1);
	v = v | (v >> 2);
	v = v | (v >> 4);
	v = v | (v >> 8);
	v = v | (v >> 16);
	
	return v - (v >> 1);
}

sjme_errorCode sjme_util_intToBinary(
	sjme_attrInNotNullBuf(destLen) sjme_lpstr destBuf,
	sjme_attrInPositiveNonZero sjme_jint destLen,
	sjme_attrInValue sjme_juint inVal,
	sjme_attrInPositiveNonZero sjme_juint bitCount)
{
	sjme_juint sh;
	sjme_cchar* wp;
	
	if (destBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Correct bit count. */
	if (bitCount <= 0 || bitCount > 32)
		bitCount = 32;
	
	/* 0b([01]*32). */
	if (destLen <= (3 + bitCount))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Start with the prefix. */	
	wp = destBuf;
	*(wp++) = '0';
	*(wp++) = 'b';
	
	/* Start from the top and go down. */
	for (sh = (1 << (bitCount - 1)); sh > 0; sh >>= 1)
		*(wp++) = ((inVal & sh) != 0 ? '1' : '0');
	
	/* End with NUL. */
	*(wp++) = '\0';
	return SJME_ERROR_NONE;
}

sjme_juint sjme_util_numLeadingZeroesU(
	sjme_attrInValue sjme_juint v)
{
	v = v | (v >> 1);
	v = v | (v >> 2);
	v = v | (v >> 4);
	v = v | (v >> 8);
	v = v | (v >> 16);
	
	return sjme_util_bitCountU(~v);
}

sjme_jint sjme_util_reverseBits(sjme_jint v)
{
	return (sjme_jint)sjme_util_reverseBitsU((sjme_juint)v);
}

sjme_juint sjme_util_reverseBitsU(sjme_juint v)
{
	v = (((v & UINT32_C(0xAAAAAAAA)) >> 1) |
		((v & UINT32_C(0x55555555)) << 1));
	v = (((v & UINT32_C(0xCCCCCCCC)) >> 2) |
		((v & UINT32_C(0x33333333)) << 2));
	v = (((v & UINT32_C(0xF0F0F0F0)) >> 4) |
		((v & UINT32_C(0x0F0F0F0F)) << 4));
	v = (((v & UINT32_C(0xFF00FF00)) >> 8) |
		((v & UINT32_C(0x00FF00FF)) << 8));
	
	return ((v >> 16) | (v << 16));
}
