/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

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
			return inSeq->data.chars[inIndex] & 0xFFFF;
			
		case SJME_CHAR_SEQ_TYPE_WIDE_STATIC:
			return inSeq->data.staticWide[inIndex] & 0xFFFF;

		default:
			return 0;
	}
}

sjme_errorCode sjme_charSeq_delete(
	sjme_attrInNotNull sjme_charSeq seq)
{
	sjme_errorCode error;
	
	if (seq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Depends on the type. */
	switch (seq->type)
	{
			/* Nothing special needs to be done for these. */
		case SJME_CHAR_SEQ_TYPE_NARROW:
		case SJME_CHAR_SEQ_TYPE_WIDE:
		case SJME_CHAR_SEQ_TYPE_UTF:
			break;

			/* Handle deletion, if it is needed. */
		case SJME_CHAR_SEQ_TYPE_FUNCTION:
			if (seq->data.function.impl->delete != NULL)
				if (sjme_error_is(error = seq->data.function.impl->delete(
					seq)))
					return sjme_error_default(error);

			/* These types cannot be freed! */
		default:
		case SJME_CHAR_SEQ_TYPE_NULL:
		case SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC:
		case SJME_CHAR_SEQ_TYPE_UTF_STATIC:
		case SJME_CHAR_SEQ_TYPE_WIDE_STATIC:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Self free! */
	return sjme_alloc_free(seq);
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

	/* Can we copy based on a pre-known type? */
	switch (sourceFrom->type)
	{
		case SJME_CHAR_SEQ_TYPE_NARROW:
			return sjme_charSeq_newNarrow(allocPool, destCopy,
				sourceFrom->data.bytes, 0, sourceFrom->length);
		
		case SJME_CHAR_SEQ_TYPE_WIDE:
			return sjme_charSeq_newWide(allocPool, destCopy,
				sourceFrom->data.chars, 0, sourceFrom->length);
		
		case SJME_CHAR_SEQ_TYPE_WIDE_STATIC:
			return sjme_charSeq_newWide(allocPool, destCopy,
				sourceFrom->data.staticWide, 0,
				sourceFrom->length);

		default:
			break;
	}

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
			goto fail_charAt;

	/* Wrap it. */
	if (sjme_error_is(error = sjme_charSeq_newWide(allocPool, destCopy,
		chars, 0, n)))
		goto fail_wrap;
	
	/* Cleanup. */
	sjme_alloca_free(chars);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_wrap:
fail_charAt:
	if (chars != NULL)
		sjme_alloca_free(chars);
	return sjme_error_default(error);
}

sjme_errorCode sjme_charSeq_dupToU(
	sjme_attrInNotNull sjme_charSeq src,
	sjme_attrInPositive sjme_jint srcOff,
	sjme_attrOutNotNullBuf(dstLimit) sjme_lpstr dst,
	sjme_attrInPositive sjme_jint dstOff,
	sjme_attrInPositive sjme_jint dstLimit,
	sjme_attrInNegativeOnePositive sjme_jint maxChars)
{
	sjme_jint s, i;
	sjme_cchar* d;
	sjme_jchar u;
	
	if (src == NULL || dst == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (maxChars < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (srcOff < 0 || dstOff < 0 || dstLimit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Use the source sequence length instead? */
	if (maxChars == -1)
		maxChars = src->length;
	
	if ((srcOff + maxChars) < 0 || (dstOff + maxChars) < 0 ||
		(srcOff + maxChars) > src->length ||
		(dstOff + maxChars) > dstLimit)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Can we copy based on a pre-known type? */
	switch (src->type)
	{
			/* Allocated UTF Data. */
		case SJME_CHAR_SEQ_TYPE_UTF:
			memmove(dst, &src->data.bytes[0],
				sizeof(sjme_cchar) * maxChars);
			break;
		
			/* Static UTF Data. */
		case SJME_CHAR_SEQ_TYPE_UTF_STATIC:
			memmove(dst, src->data.staticUtf,
				sizeof(sjme_cchar) * maxChars);
			break;
		
			/* Generic copy. */
		default:
			d = &dst[dstOff];
			for (i = 0, s = srcOff; i < maxChars; i++)
			{
				u = sjme_charSeq_charAtR(src, s++);

				/* Narrow byte sequence. */
				if (u != 0 && u <= 127)
					*(d++) = u;

				/* UTF character sequence? */
				else
				{
					sjme_todo("Impl?");
					return sjme_error_notImplemented(0);
				}
			}
			break;
	}

	/* Success! */
	return SJME_ERROR_NONE;
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

sjme_errorCode sjme_charSeq_free(
	sjme_attrInNotNull sjme_charSeq seq)
{
	sjme_errorCode error;
	sjme_charSeq_type type;
	
	if (seq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* First stage cleanup. */
	type = seq->type;
	switch (type)
	{
			/* Cleanup function based bind. */
		case SJME_CHAR_SEQ_TYPE_FUNCTION:
		case SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC:
			sjme_frontEnd_release(seq, &seq->data.function.frontEnd);
			break;

		default:
			break;
	}

	/* Erase all the data. */
	memset(seq, 0, sizeof(*seq));

	/* Free outer sequence. */
	switch (type)
	{
		case SJME_CHAR_SEQ_TYPE_FUNCTION:
		case SJME_CHAR_SEQ_TYPE_NARROW:
		case SJME_CHAR_SEQ_TYPE_WIDE:
		case SJME_CHAR_SEQ_TYPE_UTF:
			if (sjme_error_is(error = sjme_alloc_free(seq)))
				return sjme_error_default(error);
			break;

		default:
			break;
	}

	/* Success! */
	return SJME_ERROR_NONE;
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
	result = sjme_atomic_g(sjme_jint, &inSeq->hash);
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
		sjme_atomic_cs(sjme_jint, &inSeq->hash, 0, result);
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
	sjme_attrInNullable sjme_frontEndBindable* frontEnd)
{
	sjme_errorCode error;
	
	if (outSeq == NULL || functions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* The length function is required. */
	if (functions->length == NULL)
		return sjme_error_notImplemented(0);
	
	/* Setup target sequence. */
	memset(outSeq, 0, sizeof(*outSeq));
	outSeq->type = SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC;
	outSeq->data.function.impl = functions;
	if (frontEnd != NULL)
		sjme_frontEnd_copy(&outSeq->data.function.frontEnd, frontEnd);
	
	/* Read in length. */
	outSeq->length = -1;
	if (sjme_error_is(error = functions->length(outSeq, &outSeq->length)) ||
		outSeq->length < 0)
	{
		/* Undo validity. */
		memset(outSeq, 0, sizeof(*outSeq));

		/* Fail. */
		return sjme_error_default(error);
	}

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
	sjme_jint i, n;
	sjme_charSeq result;
	const sjme_jbyte* base;
	sjme_jbyte* out;
	
	if (allocPool == NULL || outSeq == NULL || narrow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || limitLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (limitLen >= 0 && (offset + limitLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Calculate actual base and length. */
	sjme_charSeq_calcBaseLen(narrow);

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
		out[i] = base[i];

	/* Success! */
	*outSeq = result;
	return SJME_ERROR_NONE;
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
	sjme_jint n;
	const sjme_jchar* base;
	
	if (inOutSeq == NULL || wide == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || limitLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (limitLen >= 0 && (offset + limitLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Calculate actual base and length. */
	sjme_charSeq_calcBaseLen(wide);
	
	/* Length is not valid? */
	if (n < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Setup target sequence. */
	memset(inOutSeq, 0, sizeof(*inOutSeq));
	inOutSeq->type = SJME_CHAR_SEQ_TYPE_WIDE_STATIC;
	inOutSeq->length = n;
	inOutSeq->data.staticWide = base;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_startsWithCharSeq(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_charSeq otherSeq)
{
	sjme_errorCode error;
	sjme_jint i, inLen, otherLen;
	
	if (inSeq == NULL || outResult == NULL || otherSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get length of both sequences. */
	inLen = -1;
	otherLen = -1;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq, &inLen)) ||
		inLen < 0)
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_charSeq_length(otherSeq, &otherLen)) ||
		otherLen < 0)
		return sjme_error_default(error);

	/* If the other string is empty... then this will always succeed. */
	if (otherLen == 0)
	{
		*outResult = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}

	/* If the other sequence is longer, than this will never be true. */
	if (otherLen > inLen)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* Go through and check each character for mismatches. */
	for (i = 0; i < otherLen; i++)
		if (sjme_charSeq_charAtR(inSeq, i) !=
			sjme_charSeq_charAtR(otherSeq, i))
		{
			*outResult = SJME_JNI_FALSE;
			return SJME_ERROR_NONE;
		}

	/* No mismatches, so success! */
	*outResult = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_charSeq_startsWithUtf(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf)
{
	sjme_charSeqStatic otherSeq;
	sjme_errorCode error;
	
	if (inSeq == NULL || outResult == NULL || startsWithUtf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup static sequence. */
	memset(&otherSeq, 0, sizeof(otherSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&otherSeq,
		startsWithUtf, 0, -1)))
		return sjme_error_default(error);

	/* Forward. */
	return sjme_charSeq_startsWithCharSeq(inSeq, outResult,
		&otherSeq);
}

sjme_jboolean sjme_charSeq_startsWithUtfR(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf)
{
	sjme_jboolean result;
	
	if (inSeq == NULL || startsWithUtf == NULL)
		return SJME_JNI_FALSE;

	/* Forward. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_charSeq_startsWithUtf(inSeq, &result,
		startsWithUtf)))
		return SJME_JNI_FALSE;

	return result;
}

sjme_lpcstr sjme_charSeq_tempUtf(
	sjme_attrInNotNull sjme_charSeq inSeq)
{
#define TEMP_SIZE 768
	sjme_threadLocal(sjme_cchar, buf[TEMP_SIZE]);
	sjme_threadLocal(sjme_jint, tempAt);
	sjme_jint baseAt, n;
	
	if (inSeq == NULL)
		return "null";

	/* Is this already in the format of a standard string? */
	if (inSeq->type == SJME_CHAR_SEQ_TYPE_UTF)
		return (sjme_lpcstr)&inSeq->data.bytes[0];
	else if (inSeq->type == SJME_CHAR_SEQ_TYPE_UTF_STATIC)
		return inSeq->data.staticUtf;

	/* Can we fit this sequence into the target buffer at the tempBase? */
	n = inSeq->length;
	baseAt = tempAt;
	if (sjme_error_is(sjme_charSeq_dupToU(
		inSeq, 0,
		buf, baseAt, TEMP_SIZE - 1,
		n)))
	{
		/* We could not, so set to the start and try again. */
		baseAt = 0;
		if (sjme_error_is(sjme_charSeq_dupToU(
			inSeq, 0,
			buf, 0, TEMP_SIZE - 1,
			n)))
			return "<oversized>";
	}

	/* Store new base. */
	tempAt = baseAt + n;
	buf[tempAt] = '\0';
	tempAt += 1;
	return &buf[baseAt];
#undef TEMP_SIZE
}
