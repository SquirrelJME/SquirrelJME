/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/charSeq.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"

/** Calculates the sequence size. */
#define sjme_charSeq_calcSize(type, count) \
	(sizeof(sjme_charSeqStatic) + (sizeof(type) * ((count) + 1)))

/** Calculates the base and length. */
#define sjme_charSeq_calcBaseLen(raw) \
	do { /* Calculate actual base. */ \
	base = SJME_POINTER_OFFSET((raw), offset * sizeof(*(raw))); \
	 \
	/* Determine actual length. */ \
	if (limitLen >= 0) \
		n = limitLen; \
	else \
	{ \
		n = 0; \
		while (base[n] != '\0') \
			n++; \
	} } while(0)

sjme_errorCode sjme_charSeq_charAt(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar)
{
	sjme_jint result;
	
	if (inSeq == NULL || outChar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inIndex < 0 || inIndex >= inSeq->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Read in character. */
	*outChar = sjme_charSeq_charAtR(inSeq, inIndex);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_charAtIs(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar wantChar)
{
	sjme_errorCode error;
	sjme_jchar actual;
	
	if (inSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inIndex < 0 || inIndex >= inSeq->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Does the character actually match? */
	if (sjme_charSeq_charAtR(inSeq, inIndex) != wantChar)
		return SJME_ERROR_NOT_MATCHED;
	return SJME_ERROR_NONE;
}

sjme_jchar sjme_charSeq_charAtR(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex)
{
	if (inSeq == NULL)
		return 0;

	if (inIndex < 0 || inIndex >= inSeq->length)
		return 0;

	/* Depends on the sequence type. */
	switch (inSeq->type)
	{
		case SJME_CHAR_SEQ_TYPE_NARROW:
			return inSeq->data.bytes[inIndex] & 0xFF;

		case SJME_CHAR_SEQ_TYPE_WIDE:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);

		case SJME_CHAR_SEQ_TYPE_UTF:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);

		case SJME_CHAR_SEQ_TYPE_UTF_STATIC:
			return sjme_string_charAt(inSeq->data.staticUtf, inIndex);

		default:
			return 0;
	}
}

sjme_errorCode sjme_charSeq_dup(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* destCopy,
	sjme_attrInNotNull sjme_charSeq sourceFrom)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_jchar* chars;
	
	if (allocPool == NULL || destCopy == NULL || sourceFrom == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Need to allocate the character data first. */
	n = sourceFrom->length;
	chars = sjme_alloca(sizeof(*chars) * (n + 1));
	if (chars == NULL)
		return sjme_error_outOfMemory(NULL, n * 2);
	memset(chars, 0, sizeof(*chars) * (n + 1));

	/* Load in characters. */
	for (i = 0; i < n; i++)
		if (sjme_error_is(error = sjme_charSeq_charAt(sourceFrom, i,
			&chars[i])))
			return sjme_error_default(error);

	/* Wrap it. */
	return sjme_charSeq_newWide(allocPool, destCopy, chars, 0, n);
}

sjme_errorCode sjme_charSeq_length(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outLen)
{
	if (inSeq == NULL || outLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* The length is always precalculated. */
	*outLen = inSeq->length;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_equals(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_charSeq bSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult)
{
	sjme_errorCode error;
	sjme_jint aHash, bHash, i, n;
	
	if (outResult == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Unequal nulls? */
	if (aSeq == NULL || bSeq == NULL)
		return aSeq == bSeq;

	/* Same pointer? */
	if (aSeq == bSeq)
	{
		*outResult = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}
	
	/* Lengths differ? Will never be equal. */
	n = aSeq->length;
	if (n != bSeq->length)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* Calculate hash of both strings. */
	aHash = 0;
	bHash = 0;
	if (sjme_error_is(error = sjme_charSeq_hash(aSeq, &aHash)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_charSeq_hash(bSeq, &bHash)))
		return sjme_error_default(error);

	/* The hashes are different, they will not be equal. */
	if (aHash != bHash)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* Compare each character. */
	for (i = 0; i < n; i++)
		if (sjme_error_is(error = sjme_charSeq_charAtIs(aSeq, i,
			sjme_charSeq_charAtR(bSeq, i))))
		{
			/* Not matched? */
			if (error == SJME_ERROR_NOT_MATCHED)
			{
				*outResult = SJME_JNI_FALSE;
				return SJME_ERROR_NONE;
			}

			/* Something else is wrong. */
			return sjme_error_default(error);
		}

	/* Did not fail, so is a match! */
	*outResult = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_charSeq_equalsR(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_charSeq bSeq)
{
	sjme_jboolean result;

	/* Forward to safer implementation. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_charSeq_equals(aSeq, bSeq, &result)))
		return SJME_JNI_FALSE;
	
	return result;
}

sjme_errorCode sjme_charSeq_equalsUtf(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_lpcstr bUtf,
	sjme_attrOutNotNull sjme_jboolean* outResult)
{
	sjme_errorCode error;
	sjme_charSeqStatic bSeq;
	
	if (outResult == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nulls are involved? */
	if (aSeq == NULL || bUtf == NULL)
	{
		*outResult = ((aSeq == NULL) == (bUtf == NULL));
		return SJME_ERROR_NONE;
	}

	/* Setup static sequence. */
	memset(&bSeq, 0, sizeof(bSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&bSeq,
		bUtf, 0, -1)))
		return sjme_error_default(error);

	/* Forward comparison. */
	return sjme_charSeq_equals(aSeq, &bSeq, outResult);
}

sjme_jboolean sjme_charSeq_equalsUtfR(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_lpcstr bUtf)
{
	sjme_jboolean result;

	/* Forward to safer implementation. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_charSeq_equalsUtf(aSeq, bUtf, &result)))
		return SJME_JNI_FALSE;
	
	return result;
}

sjme_errorCode sjme_charSeq_hash(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outHash)
{
	sjme_errorCode error;
	sjme_jint result, i, n;
	sjme_jchar c;
	
	if (inSeq == NULL || outHash == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Does the hash need to be calculated? */
	result = sjme_atomic_sjme_jint_get(&inSeq->hash);
	if (result == 0)
	{
		/* Calculate the hashCode(), the JavaDoc gives the following formula:
		 * == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes! */
		for (i = 0, n = inSeq->length; i < n; i++)
		{
			/* Read in character. */
			c = 0;
			if (sjme_error_is(error = sjme_charSeq_charAt(inSeq, i, &c)))
				return sjme_error_default(error);

			/* Calculate. */
			result = ((result << 5) - result) + (sjme_jint)c;
		}

		/* Store hash. */
		sjme_atomic_sjme_jint_compareSet(&inSeq->hash, 0, result);
	}

	/* The hash is always precalculated. */
	*outHash = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_newNarrow(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull const sjme_jbyte* narrow,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_charSeq_newNarrowFromWide(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull const sjme_jchar* wide,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen)
{
	sjme_jint i, n;
	sjme_charSeq result;
	const sjme_jchar* base;
	sjme_jbyte* out;
	
	if (allocPool == NULL || outSeq == NULL || wide == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || limitLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (limitLen >= 0 && (offset + limitLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Calculate actual base and length. */
	sjme_charSeq_calcBaseLen(wide);

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(sjme_alloc(allocPool,
		sjme_charSeq_calcSize(sjme_jbyte, n),
		(sjme_pointer*)&result)) || result == NULL)
		return sjme_error_outOfMemory(allocPool, n);

	/* Fill in. */
	result->type = SJME_CHAR_SEQ_TYPE_NARROW;
	result->length = n;
	out = &result->data.bytes[0];
	for (i = 0; i < n; i++)
		out[i] = (sjme_jbyte)(base[i] & 0xFF);

	/* Success! */
	*outSeq = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_newUtf(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull sjme_lpcstr utfString,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_charSeq_newUtfStatic(
	sjme_attrOutNotNull sjme_charSeqStatic* outSeq,
	sjme_attrInNotNull sjme_lpcstr utfString,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen)
{
	sjme_lpcstr base;
	sjme_jint length;
	
	if (outSeq == NULL || utfString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || limitLen < -1)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (limitLen >= 0 && (offset + limitLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Calculate the actual base. */
	base = SJME_POINTER_OFFSET(utfString, offset);

	/* Calculate details. */
	if (limitLen < 0)
		length = sjme_string_length(base);
	else
		length = sjme_string_lengthN(base, limitLen);

	/* Length is not valid? */
	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Setup target sequence. */
	memset(outSeq, 0, sizeof(*outSeq));
	outSeq->type = SJME_CHAR_SEQ_TYPE_UTF_STATIC;
	outSeq->length = length;
	outSeq->data.staticUtf = base;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_newWide(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull const sjme_jchar* wide,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen)
{
	sjme_jint i, n;
	sjme_jboolean anyWide;
	const sjme_jchar* base;
	
	if (allocPool == NULL || outSeq == NULL || wide == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || limitLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (limitLen >= 0 && (offset + limitLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Calculate actual base and length. */
	sjme_charSeq_calcBaseLen(wide);
	
	/* Check if this really has to be wide. */
	anyWide = SJME_JNI_FALSE;
	for (i = 0; i < n; i++)
		if ((base[i] & 0xFF00) != 0)
		{
			anyWide = SJME_JNI_TRUE;
			break;
		}
			
	/* Can this be treated as narrow instead? */
	if (!anyWide)
		return sjme_charSeq_newNarrowFromWide(allocPool, outSeq,
			base, 0, n);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_charSeq_startsWithCharSeq(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_charSeq startsWithSeq)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_charSeq_startsWithUtf(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_jboolean sjme_charSeq_startsWithUtfR(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_lpcstr sjme_charSeq_tempUtf(
	sjme_attrInNotNull sjme_charSeq inSeq)
{
	sjme_todo("Impl?");
	return (sjme_lpcstr)sjme_error_notImplemented(0);
}




#if 0
static sjme_errorCode sjme_charSeq_basicUtf_charAt(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar)
{
	sjme_jint c;
	
	if (inSeq == NULL || outChar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Obtain UTF character at position. */
	c = sjme_string_charAt(inSeq->context, inIndex);
	if (c < 0)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Success! */
	*outChar = c;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_charSeq_basicUtf_length(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outLen)
{
	sjme_jint len;
	
	if (inSeq == NULL || outLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine string length. */
	len = sjme_string_length(inSeq->context);
	if (len < 0)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Success! */
	*outLen = len;
	return SJME_ERROR_NONE;
}

static const sjme_charSeq_functions sjme_charSeq_basicUtfFunctions =
{
	.charAt = sjme_charSeq_basicUtf_charAt,
	.length = sjme_charSeq_basicUtf_length,
};

sjme_lpcstr sjme_charSeq_asLpcTemp(
	sjme_attrInNotNull const sjme_charSeq inSeq)
{
#define BUF_SIZE 1024
	sjme_attrThreadLocal(sjme_cchar, buf[BUF_SIZE]);
	sjme_jchar c;
	sjme_jint i, n;

	/* Return NULL if NULL. */
	if (inSeq == NULL)
		return NULL;

	/* Read in character length. */
	n = -1;
	if (sjme_error_is(sjme_charSeq_length(inSeq, &n)) || n < 0)
		return NULL;

	/* Clip to max. */
	if (n >= BUF_SIZE)
		n = BUF_SIZE - 1;

	/* Lazily map characters down to ASCII. */
	for (i = 0; i < n; i++)
	{
		if (sjme_error_is(sjme_charSeq_charAt(inSeq, i, &c)))
			return NULL;
		buf[i] = c & 0xFF;
	}

	/* Always end in NUL. */
	buf[n] = 0;

	/* Success! */
	return &buf[0];

#undef BUF_SIZE
}

sjme_errorCode sjme_charSeq_charAt(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar)
{
	if (inSeq == NULL || outChar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inIndex < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (inSeq->impl->charAt == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	return inSeq->impl->charAt(inSeq, inIndex, outChar);
}

sjme_errorCode sjme_charSeq_deleteStatic(
	sjme_attrInNotNull sjme_charSeq inOutSeq)
{
	sjme_errorCode error;
	
	if (inOutSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Already deleted? */
	if (inOutSeq->impl == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If there is a deletion function, then call it. */
	if (inOutSeq->impl->delete != NULL)
		if (sjme_error_is(error = inOutSeq->impl->delete(
			inOutSeq)))
			return sjme_error_default(error);
	
	/* Clear out. */
	memset(inOutSeq, 0, sizeof(*inOutSeq));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_dup(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* destCopy,
	sjme_attrInNotNull const sjme_charSeq sourceFrom)
{
	sjme_errorCode error;
	sjme_charSeq result;
	sjme_jint n, i, allocLen;
	sjme_jchar* chars;
	sjme_jboolean wide;
	
	if (allocPool == NULL || destCopy == NULL || sourceFrom == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Determine length to copy from. */
	n = -1;
	if (sjme_error_is(error = sjme_charSeq_length(sourceFrom, &n)) ||
		n < 0)
		return sjme_error_default(error);

	/* Allocate temporary buffer. */
	allocLen = sizeof(*chars) * (n + 1);
	chars = sjme_alloca(allocLen);
	if (chars == NULL)
		return sjme_error_outOfMemory(NULL, allocLen);
	memset(chars, 0, allocLen);
	
	/* Read in all source characters. */
	for (i = 0; i < n; i++)
		if (sjme_error_is(error = sjme_charSeq_charAt(sourceFrom, i,
			&chars[i])))
			return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_charSeq_equals(
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrInNotNull const sjme_charSeq equalsSeq)
{
	sjme_errorCode error;
	sjme_jint aLen, bLen;
	sjme_jint at;
	sjme_jchar a, b;
	
	if (inSeq == NULL || outResult == NULL || equalsSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Same sequence? */
	if (inSeq == equalsSeq)
	{
		*outResult = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}
	
	/* Get length of both first. */
	aLen = -1;
	bLen = -1;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq,
		&aLen)) || aLen < 0)
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_charSeq_length(equalsSeq,
		&bLen)) || bLen < 0)
		return sjme_error_default(error);
	
	/* Cannot possibly be equal? */
	if (aLen != bLen)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}
	
	/* Compare each character. */
	for (at = 0; at < aLen; at++)
	{
		/* Get both characters. */
		a = 0;
		b = 0;
		if (sjme_error_is(error = sjme_charSeq_charAt(inSeq,
			at, &a)))
			return sjme_error_default(error);
		if (sjme_error_is(error = sjme_charSeq_charAt(equalsSeq,
			at, &b)))
			return sjme_error_default(error);
		
		/* Are they not the same? */
		if (a != b)
		{
			*outResult = SJME_JNI_FALSE;
			return SJME_ERROR_NONE;
		}
	}
	
	/* There were no failed matches, so they are the same! */
	*outResult = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_charSeq_equalsR(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrInNotNull const sjme_charSeq equalsSeq)
{
	sjme_jboolean result;
	
	if (inSeq == NULL || equalsSeq == NULL)
		return SJME_JNI_FALSE;
	
	/* Perform the check. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_charSeq_equals(inSeq, &result,
		equalsSeq)))
		return SJME_JNI_FALSE;
	
	/* Return whatever result was given. */
	return result;
}

sjme_errorCode sjme_charSeq_equalsUtf(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_lpcstr equalsUtf)
{
	sjme_errorCode error;
	sjme_charSeq equalsSeq;
	
	if (inSeq == NULL || outResult == NULL || equalsUtf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup sequence. */
	memset(&equalsSeq, 0, sizeof(equalsSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(
		&equalsSeq, equalsUtf, NULL)))
		return sjme_error_default(error);
	
	/* Forward. */
	return sjme_charSeq_equals(inSeq, outResult, &equalsSeq);
}

sjme_jboolean sjme_charSeq_equalsUtfR(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrInNotNull sjme_lpcstr equalsUtf)
{
	sjme_jboolean result;
	
	if (inSeq == NULL || equalsUtf == NULL)
		return SJME_JNI_FALSE;
	
	/* Perform the check. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_charSeq_equalsUtf(inSeq, &result,
		equalsUtf)))
		return SJME_JNI_FALSE;
	
	/* Return whatever result was given. */
	return result;
}

sjme_errorCode sjme_charSeq_hash(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outHash)
{
	sjme_errorCode error;
	sjme_jint result, i, n;
	sjme_jchar c;
	
	if (inSeq == NULL || outHash == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* How long is the string? */
	n = -1;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq, &n)) || n < 0)
		return sjme_error_default(error);

	/* Calculate for the string. */
	result = 0;
	for (i = 0; i < n; i++)
	{
		/* Get next character. */
		c = 0;
		if (sjme_error_is(error = sjme_charSeq_charAt(inSeq, i, &c)))
			return sjme_error_default(error);
			
		/* Calculate the hashCode(), the JavaDoc gives the following formula: */
		/* == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes! */
		result = ((result << 5) - result) + (sjme_jint)c;
	}

	/* Success! */
	*outHash = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_length(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outLen)
{
	if (inSeq == NULL || outLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inSeq->impl == NULL || inSeq->impl->length == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward. */
	return inSeq->impl->length(inSeq, outLen);
}
	
sjme_errorCode sjme_charSeq_newStatic(
	sjme_attrInNotNull sjme_charSeq inOutSeq,
	sjme_attrInNotNull const sjme_charSeq_functions* inFunctions,
	sjme_attrInNullable sjme_pointer inOptContext,
	sjme_attrInNullable sjme_frontEnd* inOptFrontEnd)
{
	if (inOutSeq == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Clear state. */
	memset(inOutSeq, 0, sizeof(*inOutSeq));
	
	/* Fill in. */
	inOutSeq->impl = inFunctions;
	inOutSeq->context = inOptContext;
	
	/* Copy front end data? */
	if (inOptFrontEnd != NULL)
		memmove(&inOutSeq->frontEnd, inOptFrontEnd,
			sizeof(*inOptFrontEnd));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_newUtfStatic(
	sjme_attrInNotNull sjme_charSeq inOutSeq,
	sjme_attrInNotNull sjme_lpcstr inString,
	sjme_attrInNullable sjme_frontEnd* inOptFrontEnd)
{
	if (inOutSeq == NULL || inString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_charSeq_newStatic(inOutSeq,
		&sjme_charSeq_basicUtfFunctions,
		(sjme_pointer)inString, inOptFrontEnd);
}

sjme_errorCode sjme_charSeq_startsWithCharSeq(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull const sjme_charSeq startsWithSeq)
{
	sjme_errorCode error;
	sjme_jint aLen, bLen;
	
	if (inSeq == NULL || outResult == NULL || startsWithSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get length of both first. */
	aLen = -1;
	bLen = -1;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq, &aLen)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_charSeq_length(startsWithSeq,
		&bLen)))
		return sjme_error_default(error);
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_charSeq_startsWithUtf(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf)
{
	sjme_errorCode error;
	sjme_charSeq startsWithSeq;
	
	if (inSeq == NULL || outResult == NULL || startsWithUtf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup sequence. */
	memset(&startsWithSeq, 0, sizeof(startsWithSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(
		&startsWithSeq, startsWithUtf, NULL)))
		return sjme_error_default(error);
	
	/* Forward. */
	return sjme_charSeq_startsWithCharSeq(inSeq, outResult, &startsWithSeq);
}

sjme_jboolean sjme_charSeq_startsWithUtfR(
	sjme_attrInNotNull const sjme_charSeq inSeq,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf)
{
	sjme_jboolean result;
	
	if (inSeq == NULL || startsWithUtf == NULL)
		return SJME_JNI_FALSE;
	
	/* Perform the check. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_charSeq_startsWithUtf(inSeq, &result,
		startsWithUtf)))
		return SJME_JNI_FALSE;
	
	/* Return whatever result was given. */
	return result;
}
#endif
