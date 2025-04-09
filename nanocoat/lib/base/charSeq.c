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

static sjme_jchar sjme_charSeq_itD(
	sjme_attrInNotNull sjme_charSeq_it* it)
{
	if (it == NULL)
		return 0;

	/* Return the character at the current index. */
	return sjme_charSeq_charAtR(it->seq, it->index);
}

static sjme_jchar sjme_charSeq_itPp(
	sjme_attrInNotNull sjme_charSeq_it* it)
{
	if (it == NULL)
		return 0;

	/* Return the character at the current index, then increment. */
	return sjme_charSeq_charAtR(it->seq, it->index++);
}

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
		case SJME_CHAR_SEQ_TYPE_FUNCTION:
		case SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC:
			if (inSeq->data.function.impl == NULL ||
				inSeq->data.function.impl->charAt == NULL)
				return 0;
			return inSeq->data.function.impl->charAt(inSeq, inIndex);
		
		case SJME_CHAR_SEQ_TYPE_NARROW:
			return inSeq->data.bytes[inIndex] & 0xFF;

		case SJME_CHAR_SEQ_TYPE_NULL:
			return 0;

		case SJME_CHAR_SEQ_TYPE_UTF:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);

		case SJME_CHAR_SEQ_TYPE_UTF_STATIC:
			return sjme_string_charAt(inSeq->data.staticUtf, inIndex);

		case SJME_CHAR_SEQ_TYPE_WIDE:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);

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

	/* Null sequences always have zero length. */
	if (inSeq->type == SJME_CHAR_SEQ_TYPE_NULL)
		if (inSeq->length != 0)
			inSeq->length = 0;

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

sjme_errorCode sjme_charSeq_hashR(
	sjme_attrInNotNull sjme_charSeq inSeq)
{
	sjme_jint result;
	
	if (inSeq == NULL)
		return 0;

	/* Calculate the hash. */
	result = 0;
	if (sjme_error_is(sjme_charSeq_hash(inSeq, &result)))
		return 0;

	/* Return whatever the hash was. */
	return result;
}

sjme_errorCode sjme_charSeq_itNew(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrOutNotNull sjme_charSeq_it* it)
{
	if (inSeq == NULL || it == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || offset >= inSeq->length)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup iterator. */
	memset(it, 0, sizeof(*it));
	it->seq = inSeq;
	it->index = offset;
	it->d = sjme_charSeq_itD;
	it->pp = sjme_charSeq_itPp;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_newFunctionStatic(
	sjme_attrOutNotNull sjme_charSeqStatic* outSeq,
	sjme_attrInNotNull const sjme_charSeq_functions* functions,
	sjme_attrInNullable sjme_frontEnd* frontEnd)
{
	sjme_errorCode error;
	sjme_jint length;
	
	if (outSeq == NULL || functions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Read in length. */
	length = -1;
	error = SJME_ERROR_NONE;
	if (functions->length == NULL ||
		sjme_error_is(error = functions->length(outSeq, &length)))
		return sjme_error_defaultOr(error, SJME_ERROR_NOT_IMPLEMENTED);
	
	/* Setup target sequence. */
	memset(outSeq, 0, sizeof(*outSeq));
	outSeq->type = SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC;
	outSeq->length = length;
	outSeq->data.function.impl = functions;
	if (frontEnd != NULL)
		memmove(&outSeq->data.function.frontEnd, frontEnd,
			sizeof(*frontEnd));
	outSeq->data.function.frontEnd.bindType = SJME_FRONTEND_BINDLESS;

	/* Success! */
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

sjme_errorCode sjme_charSeq_newWideStatic(
	sjme_attrInOutNotNull sjme_charSeqStatic* inOutSeq,
	sjme_attrInNotNull const sjme_jchar* wide,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen)
{
	if (inOutSeq == NULL || wide == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || limitLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (limitLen >= 0 && (offset + limitLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
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
#define TEMP_SIZE 512
	sjme_threadLocal(sjme_cchar, buf[TEMP_SIZE]);
	sjme_threadLocal(sjme_jint, tempAt);
	sjme_jint baseAt, i, o, n, stage;
	sjme_jchar c;
	
	if (inSeq == NULL)
		return "null";

	/* Is this already in the format of a standard string? */
	if (inSeq->type == SJME_CHAR_SEQ_TYPE_UTF)
		return (sjme_lpcstr)&inSeq->data.bytes[0];
	else if (inSeq->type == SJME_CHAR_SEQ_TYPE_UTF_STATIC)
		return inSeq->data.staticUtf;

	/* Try a multi-stage write to fit as many temporary strings as needed. */
	for (stage = 0, baseAt = tempAt; stage >= 0;)
	{
		/* Translate all characters. */
		for (i = 0, o = baseAt, n = inSeq->length; i <= n; i++)
		{
			/* Get character here. */
			c = (i < n ? sjme_charSeq_charAtR(inSeq, i) : 0);
			
			/* Overflowing? */
			if (o >= TEMP_SIZE - 1)
			{
				/* On first stage, reset everything to the start. */
				if (stage == 0)
				{
					baseAt = 0;
					tempAt = 0;
					i = -1;
					n = -1;
					stage++;
					break;
				}

				/* On second stage, just give up. */
				else
				{
					stage = -1;
					break;
				}
			}

			/* Direct map. */
			else if (c <= 127)
				buf[o++] = c;
		
			/* Need to encode. */
			else
				sjme_todo("Impl?");
		}

		/* Reached end okay? Stop processing then... */
		if (n >= inSeq->length)
		{
			tempAt = o + 1;
			if (tempAt >= TEMP_SIZE - 1)
				tempAt = 0;
			break;
		}
	}
	
	/* Use the base pointer. */
	return &buf[baseAt];
#undef TEMP_SIZE
}
