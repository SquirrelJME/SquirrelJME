/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/stdTypes.h"
#include "sjme/util.h"
#include "sjme/debug.h"
#include "sjme/atomic.h"

static sjme_errorCode sjme_random_generate(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInRange(0, 32) sjme_jint bits)
{
	sjme_jint result;
	sjme_jlong seed;
	
	if (random == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (bits < 0 || bits > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Update the seed */
	seed.full = (random->seed.full * INT64_C(0x5DEECE66D) + INT64_C(0xB)) &
		((INT64_C(1) << INT64_C(48)) - INT64_C(1));
	random->seed.full = seed.full;

	/* Calculate resultant value. */
	result = (sjme_jint)((sjme_julongNative)seed.full >>
		(UINT64_C(48) - bits));
	
	/* Emit barrier. */
	sjme_atomic_barrier();

	/* Success! */
	*outValue = result;
	return SJME_ERROR_NONE;
}

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

sjme_jint sjme_lpwcscmp(sjme_lpcwstr a, sjme_lpcwstr b)
{
	/* Compare null. */
	if (a == NULL || b == NULL)
		return sjme_compare_null(a, b);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_jint sjme_lpwcsncmp(sjme_lpcwstr a, sjme_lpcwstr b, sjme_jint n)
{
	/* Compare null. */
	if (a == NULL || b == NULL)
		return sjme_compare_null(a, b);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_jint sjme_lpwcscasecmp(sjme_lpcwstr a, sjme_lpcwstr b)
{
	/* Compare null. */
	if (a == NULL || b == NULL)
		return sjme_compare_null(a, b);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
	
sjme_jint sjme_lpwcsncasecmp(sjme_lpcwstr a, sjme_lpcwstr b, sjme_jint n)
{
	/* Compare null. */
	if (a == NULL || b == NULL)
		return sjme_compare_null(a, b);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
	
sjme_jint sjme_lpwcslen(sjme_lpcwstr s)
{
	if (s == NULL)
		return -1;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
	
sjme_jint sjme_lpwcsnlen(sjme_lpcwstr s, sjme_jint n)
{
	if (s == NULL || n < 0)
		return -1;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_random_init(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jint seedHi,
	sjme_attrInValue sjme_jint seedLo)
{
	sjme_jlong seed;

	/* Unwrap seed. */
	seed.part.lo = seedLo;
	seed.part.hi = seedHi;
	return sjme_random_initL(outRandom, seed);
}

sjme_errorCode sjme_random_initL(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jlong seed)
{
	if (outRandom == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Emit barrier. */
	sjme_atomic_barrier();

	/* Set seed value. */
	outRandom->seed.full = (seed.full ^ INT64_C(0x5DEECE66D)) &
		((INT64_C(1) << INT64_C(48)) - INT64_C(1));
	
	/* Emit barrier. */
	sjme_atomic_barrier();

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_random_nextInt(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue)
{
	/* Forward to the generator. */
	return sjme_random_generate(random, outValue, 32);
}

sjme_jint sjme_random_nextIntR(
	sjme_attrInOutNotNull sjme_random* random)
{
	sjme_jint result;
	
	if (random == NULL)
		return 0;

	/* Load next random. */
	result = 0;
	if (sjme_error_is(sjme_random_generate(random, &result, 32)))
		return 0;

	/* Return it. */
	return result;
}
	
sjme_errorCode sjme_random_nextIntMax(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInPositiveNonZero sjme_jint maxValue)
{
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_jint sjme_string_charAt(sjme_lpcstr string, sjme_jint index)
{
	sjme_jint at;
	sjme_jint c;
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

/** All the comparison functions are the same, reduce duplication. */
#define sjme_string_compareImpl(funcCall, lenCall) \
	sjme_jint result, limit; \
	 \
	/* Compare null. */ \
	if (aString == NULL || bString == NULL) \
		return sjme_compare_null(aString, bString); \
	 \
	/* Take the string length? */ \
	if (aLen == -1) \
		aLen = (sjme_jint)lenCall(aString); \
	if (bLen == -1) \
		bLen = (sjme_jint)lenCall(bString); \
	 \
	/* Determine the max number of characters to compare. */ \
	if (aLen < bLen) \
		limit = aLen; \
	else \
		limit = bLen; \
	 \
	/* Compare strings up to the limit. */ \
	result = (funcCall); \
	if (result != 0) \
		return result; \
	 \
	/* If the lengths differ, smaller is first. */ \
	if (aLen != bLen) \
		return aLen - bLen; \
	 \
	/* Equal otherwise. */ \
	return 0;

sjme_jint sjme_string_compareN(sjme_lpcstr aString, sjme_jint aLen,
	sjme_lpcstr bString, sjme_jint bLen)
{
	sjme_string_compareImpl(strncmp(aString, bString, limit),
		strlen);
}

sjme_jint sjme_string_compareIN(sjme_lpcstr aString, sjme_jint aLen,
	sjme_lpcstr bString, sjme_jint bLen)
{
	sjme_string_compareImpl(strncasecmp(aString, bString, limit),
		strlen);
}

sjme_jint sjme_string_compareWN(sjme_lpcwstr aString, sjme_jint aLen,
	sjme_lpcwstr bString, sjme_jint bLen)
{
	sjme_string_compareImpl(sjme_lpwcsncmp(aString, bString, limit),
		sjme_lpwcslen);
}

sjme_jint sjme_string_compareIWN(sjme_lpcwstr aString, sjme_jint aLen,
	sjme_lpcwstr bString, sjme_jint bLen)
{
	sjme_string_compareImpl(sjme_lpwcsncasecmp(aString, bString, limit),
		sjme_lpwcslen);
}

sjme_jint sjme_string_compareWAN(
	sjme_lpcwstr aString, sjme_jint aLen,
	sjme_lpcstr bString, sjme_jint bLen)
{
	sjme_jint result, limit, i;
	
	/* Compare null. */
	if (aString == NULL || bString == NULL)
		return sjme_compare_null(aString, bString);

	/* Take the string length? */
	if (aLen == -1)
		aLen = (sjme_jint)sjme_lpwcslen(aString);
	if (bLen == -1)
		bLen = (sjme_jint)strlen(bString);
	
	/* Determine the max number of characters to compare. */
	if (aLen < bLen)
		limit = aLen;
	else
		limit = bLen;
	
	/* Compare strings up to the limit. */
	for (i = 0; i < limit; i++)
	{
		/* Correct end of string length if this has been hit. */
		if (aString[i] == '\0' || bString[i] == '\0')
		{
			if (aString[i] == '\0' && aLen > i)
				aLen = i;
			if (bString[i] == '\0' && bLen > i)
				bLen = i;

			/* A comparison no longer needs to happen becuase an end of */
			/* string was reached. */
			break;
		}

		/* If the character values differ, then these are different. */
		result = ((sjme_jint)aString[i]) - ((sjme_jint)bString[i]);
		if (result != 0)
			return result;
	}

	/* If the lengths differ, smaller is first. */
	if (aLen != bLen)
		return aLen - bLen;
	
	/* Equal otherwise. */
	return 0;
}

sjme_jint sjme_string_compareIWAN(
	sjme_lpcwstr aString, sjme_jint aLen,
	sjme_lpcstr bString, sjme_jint bLen)
{
	sjme_jint result, limit, i;

	/* Compare null. */
	if (aString == NULL || bString == NULL)
		return sjme_compare_null(aString, bString);

	/* Take the string length? */
	if (aLen == -1)
		aLen = (sjme_jint)sjme_lpwcslen(aString);
	if (bLen == -1)
		bLen = (sjme_jint)strlen(bString);

	/* Determine the max number of characters to compare. */
	if (aLen < bLen)
		limit = aLen;
	else
		limit = bLen;

	/* Compare strings up to the limit. */
	for (i = 0; i < limit; i++)
	{
		/* Correct end of string length if this has been hit. */
		if (aString[i] == '\0' || bString[i] == '\0')
		{
			if (aString[i] == '\0' && aLen > i)
				aLen = i;
			if (bString[i] == '\0' && bLen > i)
				bLen = i;

			/* A comparison no longer needs to happen becuase an end of */
			/* string was reached. */
			break;
		}

		/* If the character values differ, then these are different. */
		result = ((sjme_jint)towlower(aString[i])) -
			((sjme_jint)tolower(bString[i]));
		if (result != 0)
			return result;
	}

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
	sjme_jint c, rawIndex;
	sjme_lpcstr p;
	
	if (string == NULL || limit < 0)
		return -1;

	/* Read until end of string. */
	result = 0;
	rawIndex = 0;
	for (p = string; *p != 0 && result < limit && rawIndex < limit; rawIndex++)
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

sjme_jlong sjme_swap_long(
	sjme_attrInValue sjme_jlong in)
{
	sjme_juint temp;

	/* Swap high and low first. */
	temp = in.part.hi;
	in.part.hi = (sjme_jint)in.part.lo;
	in.part.lo = temp;

	/* Then finish swap each side. */
	in.part.hi = sjme_swap_int(in.part.hi);
	in.part.lo = sjme_swap_uint(in.part.lo);

	/* Return the result. */
	return in;
}

sjme_juint sjme_swap_uint(
	sjme_attrInValue sjme_juint in)
{
#if SJME_CONFIG_HAS_GCC_BUILTIN(bswap32)
	return __builtin_bswap32(in);
#else
	// 0xAABBCCDD -> 0xBBAADDCC
	in = (((in & 0xFF00FF00) >> 8) | ((in & 0x00FF00FF) << 8));

	// 0xBBAADDCC -> 0xDDCCBBAA
	return (in >> 16) | (in << 16);
#endif
}

sjme_jchar sjme_swap_ushort(
	sjme_attrInValue sjme_jchar in)
{
#if SJME_CONFIG_HAS_GCC_BUILTIN(bswap16)
	return __builtin_bswap16(in);
#else
	return ((in >> 8) | (in << 8));
#endif
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

sjme_intPointer sjme_util_alignTo(sjme_intPointer addr,
	sjme_intPointer align)
{
	sjme_intPointer mod;
	
	/* Force alignment to be valid. */
	if (align <= 0)
		align = 1;

	/* Perform alignment. */
	mod = (addr % align);
	if (mod == 0)
		return addr;
	return addr + (align - mod);
}

sjme_juint sjme_util_intBitCountU(
	sjme_attrInValue sjme_juint v)
{
#if SJME_CONFIG_HAS_GCC_BUILTIN(popcount)
	return __builtin_popcount(v);
#else
	/* Henry S. Warren, Jr. (2013). Hacker's Delight (2nd Edition). */
	/* Addison Wesley. ISBN-13 978-0-321-842268-8. Page 156. */
	v = v - ((v >> 1) & UINT32_C(0x55555555));
	v = (v & UINT32_C(0x33333333)) + ((v >> 2) & UINT32_C(0x33333333));
	return ((v + (v >> 4) & UINT32_C(0xF0F0F0F)) * UINT32_C(0x1010101)) >> 24;
#endif
}

sjme_juint sjme_util_intCompactLeft(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m)
{
	return sjme_util_intReverseU(sjme_util_intCompactRight(
		sjme_util_intReverseU(v), sjme_util_intReverseU(m)));
}
	
sjme_juint sjme_util_intCompactRight(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m)
{
	sjme_juint fm, pb, lb, i;
	
	/* So Hacker's Delight, which is an amazing book to have in your */
	/* library, I did not find what I needed. Inside there was compress */
	/* which really is extract, which is useful however not exactly what */
	/* I needed. However, from looking at it, I rather have gotten inspired */
	/* to write my own compaction method. */
	
	/* First remove bits we do not care about. */
	v &= m;
	
	/* Set the initial final mask to nothing. */
	fm = 0;
	
	/* Effectively what we are doing here is getting the lowest one bit */
	/* over and over until none remains. We only add into the final mask */
	/* if the lowest bit is not the same as the previous bit. */
	/* Note that 32-bits is worse case scenario. */
	pb = INT32_MAX;
	for (i = 0; i < 32; i++)
	{
		/* Get the lowest bit, if there are none left then we can stop. */
		lb = sjme_util_intZeroesTrailingU(v);
		if (lb == 32)
			break;
		
		/* If the previous bit is not one away, then mask it in. */
		if ((pb + 1) != lb)
			fm |= (1 << lb);
		
		/* Mask away the value from the lowest bit, up one so that */
		/* we do not get stuck on the lowest bit. */
		v &= sjme_util_intOverShiftU(UINT32_MAX, (lb + 1));
		
		/* Remember the lowest bit. */
		pb = lb;
	}
	
	/* The final mask is the one we care about. */
	return fm;
}

sjme_juint sjme_util_intExtractLeft(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m)
{
	/* Henry S. Warren, Jr. (2013). Hacker's Delight (2nd Edition). */
	/* Addison Wesley. ISBN-13 978-0-321-842268-8. Page 156. */
	return sjme_util_intReverseU(sjme_util_intExtractRight(
		sjme_util_intReverseU(v), sjme_util_intReverseU(m)));
}
	
sjme_juint sjme_util_intExtractRight(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m)
{
	/* Henry S. Warren, Jr. (2013). Hacker's Delight (2nd Edition). */
	/* Addison Wesley. ISBN-13 978-0-321-842268-8. Page 153. */
	sjme_juint mk, mp, mv, t, i;
	
	/* Clear irrelevant bits. */
	v = v & m;
	
	/* We will count zeroes to the right. */
	mk = ~m << 1;
	
	for (i = 0; i < 5; i++)
	{
		/* Parallel suffix. */
		mp = mk ^ (mk << 1);
		
		/* Cycle again. */
		mp = mp ^ (mp << 2);
		mp = mp ^ (mp << 4);
		mp = mp ^ (mp << 8);
		mp = mp ^ (mp << 16);
		
		/* Bits to move. */
		mv = mp & m;
		
		/* Compress m. */
		m = m ^ mv | (mv > (1 << i));
		t = v & mv;
		
		/* Compress v. */
		v = v ^ t | (t >> (1 << i));
		mk = mk & ~mp;
	}
	
	return v;
}

sjme_juint sjme_util_intOneBitHighestU(
	sjme_attrInValue sjme_juint v)
{
	return 1 << sjme_util_intZeroesLeadingU(v);
}

sjme_juint sjme_util_intOneBitLowestU(
	sjme_attrInValue sjme_juint v)
{
	return 1 << sjme_util_intZeroesTrailingU(v);
}

sjme_jint sjme_util_intOverShift(
	sjme_attrInValue sjme_jint v,
	sjme_attrInRange(-32, 32) sjme_jint sh)
{
	/* Shifting to the right all the way, smears the highest bit. */
	if (sh <= -32)
		return ((v & INT32_C(0x80000000)) ?
			INT32_C(0xFFFFFFFF) : INT32_C(0));

	/* Shifting all the way to the left results in zero. */
	if (sh >= 32)
		return 0;
	
	/* Otherwise the shifted amount. */
	if (sh < 0)
		return v >> (-sh);
	return v << sh;
}

sjme_juint sjme_util_intOverShiftU(
	sjme_attrInValue sjme_juint v,
	sjme_attrInRange(-32, 32) sjme_jint sh)
{
	/* Shifting more than this always results in zero. */
	if (sh <= -32 || sh >= 32)
		return 0;
	
	/* Shift by zero does nothing. */
	else if (sh == 0)
		return v;
	
	/* Otherwise the shifted amount. */
	if (sh < 0)
		return v >> (sjme_juint)(-sh);
	return v << (sjme_juint)sh;
}

sjme_jint sjme_util_intReverse(sjme_jint v)
{
	return (sjme_jint)sjme_util_intReverseU((sjme_juint)v);
}

sjme_juint sjme_util_intReverseU(sjme_juint v)
{
#if SJME_CONFIG_HAS_GCC_BUILTIN(bitreverse32)
	return __builtin_bitreverse32(v);
#else
	/* Henry S. Warren, Jr. (2013). Hacker's Delight (2nd Edition). */
	/* Addison Wesley. ISBN-13 978-0-321-842268-8. */
	v = (((v & UINT32_C(0xAAAAAAAA)) >> 1) |
		((v & UINT32_C(0x55555555)) << 1));
	v = (((v & UINT32_C(0xCCCCCCCC)) >> 2) |
		((v & UINT32_C(0x33333333)) << 2));
	v = (((v & UINT32_C(0xF0F0F0F0)) >> 4) |
		((v & UINT32_C(0x0F0F0F0F)) << 4));
	v = (((v & UINT32_C(0xFF00FF00)) >> 8) |
		((v & UINT32_C(0x00FF00FF)) << 8));
	
	return ((v >> 16) | (v << 16));
#endif
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
	*(wp) = '\0';
	return SJME_ERROR_NONE;
}

sjme_juint sjme_util_intZeroesLeadingU(
	sjme_attrInValue sjme_juint v)
{
#if SJME_CONFIG_HAS_GCC_BUILTIN(clz)
	if (v == 0)
		return 32;
	return __builtin_clz(v);
#else
	/* Henry S. Warren, Jr. (2013). Hacker's Delight (2nd Edition). */
	/* Addison Wesley. ISBN-13 978-0-321-842268-8. */
	v = v | (v >> 1);
	v = v | (v >> 2);
	v = v | (v >> 4);
	v = v | (v >> 8);
	v = v | (v >> 16);
	
	return sjme_util_intBitCountU(~v);
#endif
}

sjme_juint sjme_util_intZeroesTrailingU(
	sjme_attrInValue sjme_juint v)
{
#if SJME_CONFIG_HAS_GCC_BUILTIN(ctz)
	if (v == 0)
		return 32;
	return __builtin_ctz(v);
#else
	return sjme_util_intZeroesLeadingU(
		sjme_util_intReverseU(v));
#endif
}

sjme_errorCode sjme_util_lpstrTrimEnd(
	sjme_attrInNotNullBuf(length) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_jint at;
	sjme_cchar c;
	
	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Find end of string first. */
	for (at = 0; at < length;)
	{
		if (buf[at] == 0)
		{
			if (at > 0)
				at--;
			break;
		}
		
		at++;
	}
	
	/* Past the end? */
	if (at >= length)
		at = length - 1;
	
	/* Remove any whitespace. */
	while (at >= 0)
	{
		c = buf[at];
		
		/* NUL out whitespace. */
		if (c == ' ' || c == '\r' || c == '\n' || c == '\t')
			buf[at--] = 0;
		
		/* Nothing here anymore. */
		else
			break;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

#if defined(SJME_CONFIG_HAS_NO_UNALIGNED16)

/** Filler for unaligned 16-bit access. */
#define SJME_UTIL_UNALIGNED_16_FILL 8

const sjme_jshort* sjme_util_memUnaligned16(void* addr)
{
	sjme_threadLocal(sjme_jshort, temp[SJME_UTIL_UNALIGNED_16_FILL]);
	sjme_threadLocal(sjme_atomic(sjme_jint), fill);
	sjme_jshort* into;
	sjme_jubyte* bytes;

	/* Map in. */
	into = &temp[sjme_atomic_ga(sjme_jint, &fill, 1) &
		(SJME_UTIL_UNALIGNED_16_FILL - 1)];
	bytes = addr;
#if defined(SJME_CONFIG_HAS_BIG_ENDIAN)
	(*into) = ((bytes[0] & 0xFF) << 8) |
		(bytes[1] & 0xFF);
#else
	(*into) = ((bytes[1] & 0xFF) << 8) |
		(bytes[0] & 0xFF);
#endif

	/* Return address of temporary. */
	return into;
}

#endif

#if defined(SJME_CONFIG_HAS_NO_UNALIGNED32)

/** Filler for unaligned 32-bit access. */
#define SJME_UTIL_UNALIGNED_32_FILL 4

const sjme_jint* sjme_util_memUnaligned32(void* addr)
{
	sjme_threadLocal(sjme_jint, temp[SJME_UTIL_UNALIGNED_32_FILL]);
	sjme_threadLocal(sjme_atomic(sjme_jint), fill);
	sjme_jint* into;
	sjme_jushort* shorts;

	/* Map in. */
	into = &temp[sjme_atomic_ga(sjme_jint, &fill, 1) &
		(SJME_UTIL_UNALIGNED_32_FILL - 1)];
	shorts = addr;
#if defined(SJME_CONFIG_HAS_BIG_ENDIAN)
	(*into) = (((*sjme_util_memUnaligned16(&shorts[0])) & 0xFFFF) << 16) |
		((*sjme_util_memUnaligned16(&shorts[1])) & 0xFF);
#else
	(*into) = (((*sjme_util_memUnaligned16(&shorts[1])) & 0xFFFF) << 16) |
		((*sjme_util_memUnaligned16(&shorts[0])) & 0xFF);
#endif

	/* Return address of temporary. */
	return into;
}

sjme_jint* sjme_util_memUnaligned32W(void* addr, sjme_jint v)
{
	sjme_todo("Impl?");
	return NULL;
}

#endif

#if !defined(SJME_CONFIG_SIZEOF_SIZE_T) || SJME_CONFIG_SIZEOF_SIZE_T != 4

sjme_jint sjme_util_sizeToInt(size_t in)
{
	/* size_t is an unsigned value that generally is larger than int. */
	/* However, this may not always be the case as a compiler could say */
	/* support only 16-bit size_t with a 32-bit sjme_jint which is long. */
	/* There should be no overflow as we are promoting to a larger type. */
	if (sjme_noLint(sizeof(size_t) < sizeof(sjme_jint)) || in < INT32_MAX)
		return (sjme_jint)in;
	return INT32_MAX;
}

#endif

