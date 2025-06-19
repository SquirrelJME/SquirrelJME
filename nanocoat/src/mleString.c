/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(stringCharAt)
{
	sjme_errorCode error;
	sjme_jstring string;
	sjme_jint index;
	sjme_jchar result;

	/* Must be an actual string. */
	string = (sjme_jstring)argV[0].v.l;
	if (!sjme_nvm_isAR(string, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Which index is desired? */
	index = argV[1].v.i;
	if (index < 0 || index >= string->length)
		return SJME_ERROR_MLE_CALL;

	/* Read in character. */
	result = 0;
	if (sjme_error_is(error = sjme_charSeq_charAt(string->seq,
		index, &result)))
		return sjme_error_vmError(inFrame, error);

	/* Give the result. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = result & 0xFFFF;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(stringEquals)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(stringHash)
{
	sjme_jstring string;

	/* Must be an actual string. */
	string = (sjme_jstring)argV[0].v.l;
	if (!sjme_nvm_isAR(string, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* This is a simple value copy operation. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = string->hashCode;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(stringInit, chars)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(stringInit, emptyOrThis)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(stringInit, string)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(stringIsIntern)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(stringLength)
{
	sjme_jstring string;

	/* Must be an actual string. */
	string = (sjme_jstring)argV[0].v.l;
	if (!sjme_nvm_isAR(string, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* This is a simple value copy operation. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = string->length;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(stringToChar)
{
	sjme_jstring source;
	sjme_jint sourceOff;
	sjme_jarray dest;
	sjme_jint destOff;
	sjme_jint len;
	sjme_jint i, s, d;

	/* Map arguments. */
	source = (sjme_jstring)argV[0].v.l;
	sourceOff = argV[1].v.i;
	dest = (sjme_jarray)argV[2].v.l;
	destOff = argV[3].v.i;
	len = argV[4].v.i;

	/* Check types. */
	if (!sjme_nvm_isAR(source, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	if (!sjme_nvm_isAR(dest, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	if (dest->type != SJME_BASIC_TYPE_ID_CHARACTER)
		return SJME_ERROR_MLE_CALL;

	/* Check bounds. */
	if (sourceOff < 0 || destOff < 0 || len < 0 ||
		(sourceOff + len) < 0 || (destOff + len) < 0 ||
		(sourceOff + len) > source->seq->length ||
		(destOff + len) > dest->length)
		return SJME_ERROR_MLE_CALL;
	
	/* Read characters into the target. */
	for (i = 0, s = sourceOff, d = destOff; i < len; i++)
		dest->e.c[d++] = sjme_charSeq_charAtR(source->seq, s++);

	/* Void return. */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(stringValueOf, chars)
{
	sjme_errorCode error;
	sjme_jboolean intern;
	sjme_jarray array;
	sjme_jint off, len;
	sjme_charSeqStatic seq;

	/* Intern the string? */
	intern = !!argV[0].v.i;

	/* Must be an actual array. */
	array = (sjme_jarray)argV[1].v.l;
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Must be a char array. */
	if (array->type != SJME_BASIC_TYPE_ID_CHARACTER)
		return SJME_ERROR_MLE_CALL;

	/* Read offset and length. */
	off = argV[2].v.i;
	len = argV[3].v.i;

	/* Check bounds. */
	if (off < 0 || len < 0 || (off + len) < 0)
		return SJME_ERROR_MLE_CALL;

	/* Wrap a wide sequence. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_charSeq_newWideStatic(&seq,
		(sjme_jchar*)&array->e.c[0], off, array->length)))
		return sjme_error_mask(error, SJME_ERROR_MLE_CALL);

	/* Obtain string value from the sequence. */
	argR->v.l = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfCS(
		SJME_F_T(inFrame),
		SJME_AS_NVM_JSTRINGP(&argR->v.l), intern, &seq) ||
		argR->v.l == NULL))
		return sjme_error_mask(error, SJME_ERROR_MLE_CALL);

	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(stringValueOf, string)
{
	sjme_errorCode error;
	sjme_jboolean intern;
	sjme_jstring string;

	/* Intern the string? */
	intern = !!argV[0].v.i;

	/* Must be an actual string. */
	string = (sjme_jstring)argV[1].v.l;
	if (!sjme_nvm_isAR(string, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Obtain string value from the sequence. */
	argR->v.l = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfCS(
		SJME_F_T(inFrame),
		SJME_AS_NVM_JSTRINGP(&argR->v.l), intern, string->seq) ||
		argR->v.l == NULL))
		return sjme_error_default(error);

	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(StringShelf) =
{
	SJME_NVM_MLE_DEFINE(stringCharAt,
		SJME_MD(SJME_MD_C, SJME_MD_STRING SJME_MD_I),
		"ILI"),
	SJME_NVM_MLE_DEFINE(stringEquals,
		SJME_MD(SJME_MD_Z, SJME_MD_STRING SJME_MD_STRING),
		"ILL"),
	SJME_NVM_MLE_DEFINE(stringHash,
		SJME_MD(SJME_MD_I, SJME_MD_STRING),
		"IL"),
	SJME_NVM_MLE_DEFINE_ALT(stringInit, chars,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_AC SJME_MD_I SJME_MD_I),
		"VLLII"),
	SJME_NVM_MLE_DEFINE_ALT(stringInit, emptyOrThis,
		SJME_MD(SJME_MD_V, SJME_MD_STRING),
		"VL"),
	SJME_NVM_MLE_DEFINE_ALT(stringInit, string,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_STRING),
		"VLL"),
	SJME_NVM_MLE_DEFINE(stringIsIntern,
		SJME_MD(SJME_MD_Z, SJME_MD_STRING),
		"IL"),
	SJME_NVM_MLE_DEFINE(stringLength,
		SJME_MD(SJME_MD_I, SJME_MD_STRING),
		"IL"),
	SJME_NVM_MLE_DEFINE(stringToChar,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_I
			SJME_MD_AC SJME_MD_I SJME_MD_I),
		"VLILII"),
	SJME_NVM_MLE_DEFINE_ALT(stringValueOf, chars,
		SJME_MD(SJME_MD_STRING, SJME_MD_Z SJME_MD_AC SJME_MD_I SJME_MD_I),
		"LILII"),
	SJME_NVM_MLE_DEFINE_ALT(stringValueOf, string,
		SJME_MD(SJME_MD_STRING, SJME_MD_Z SJME_MD_STRING),
		"LIL"),
	SJME_NVM_MLE_STOP()
};
