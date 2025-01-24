/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Character sequences.
 * 
 * @since 2024/06/26
 */

#ifndef SQUIRRELJME_CHARSEQ_H
#define SQUIRRELJME_CHARSEQ_H

#include "sjme/frontEnd.h"
#include "sjme/stdTypes.h"
#include "sjme/error.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CHARSEQ_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A character sequence which contains a set of characters within a string,
 * may be modifiable or not.
 * 
 * @since 2024/06/26
 */
typedef struct sjme_charSeq sjme_charSeq;

/**
 * Returns the character at the given index.
 * 
 * @param inSeq The input character sequence.
 * @param inIndex The index to get from.
 * @param outChar The resultant character.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_charSeq_charAtFunc)(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar);

/**
 * Deletes the given static character sequence.
 * 
 * @param inSeq The input/output sequence. 
 * @return Any resultant error, if any.
 * @since 2024/06/26
 */
typedef sjme_errorCode (*sjme_charSeq_deleteFunc)(
	sjme_attrInNotNull sjme_charSeq* inSeq);

/**
 * Returns the length of the character sequence.
 * 
 * @param inSeq The input character sequence.
 * @param outLen The sequence length.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_charSeq_lengthFunc)(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jint* outLen);

/**
 * Functions which are used to process character sequences.
 * 
 * @since 2024/06/26
 */
typedef struct sjme_charSeq_functions
{
	/** The character at the given index. */
	sjme_charSeq_charAtFunc charAt;
	
	/** Deleting the sequence and freeing any resources. */
	sjme_charSeq_deleteFunc delete;
	
	/** The length of the character sequence. */
	sjme_charSeq_lengthFunc length;
} sjme_charSeq_functions;

struct sjme_charSeq
{
	/** Front end data, if any. */
	sjme_frontEnd frontEnd;
	
	/** Context pointer, if any. */
	sjme_pointer context;
	
	/** The API for accessing the character sequence. */
	const sjme_charSeq_functions* impl;
};

/**
 * Returns the character at the given index.
 * 
 * @param inSeq The input character sequence.
 * @param inIndex The index to get from.
 * @param outChar The resultant character.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_charSeq_charAt(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar);

/**
 * Deletes the given static character sequence.
 * 
 * @param inOutSeq The input/output sequence. 
 * @return Any resultant error, if any.
 * @since 2024/06/26
 */
sjme_errorCode sjme_charSeq_deleteStatic(
	sjme_attrInNotNull sjme_charSeq* inOutSeq);

/**
 * Returns the length of the character sequence.
 * 
 * @param inSeq The input character sequence.
 * @param outLen The sequence length.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_charSeq_length(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jint* outLen);

/**
 * Checks if the given character sequence equals the given character sequence.
 * 
 * @param inSeq The sequence to check.
 * @param outResult The result of the check.
 * @param equalsSeq The char sequence to check for equality against.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_equalsCharSeq(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull const sjme_charSeq* equalsSeq);

/**
 * Checks if the given character sequence equals the given UTF string.
 * 
 * @param inSeq The sequence to check.
 * @param outResult The result of the check.
 * @param equalsUtf The UTF sequence to check for equality against.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_equalsUtf(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_lpcstr equalsUtf);
	
/**
 * Checks if the given character sequence equals the given UTF string.
 * 
 * @param inSeq The sequence to check.
 * @param equalsUtf The UTF sequence to check for equality against.
 * @return Returns whether it matches, note that if there is an error
 * then @c SJME_JNI_FALSE will be returned and the error will be hidden.
 * @since 2024/08/08 
 */
sjme_jboolean sjme_charSeq_equalsUtfR(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrInNotNull sjme_lpcstr equalsUtf);

/**
 * Initializes the given static character sequence.
 * 
 * @param inOutSeq The input/output sequence.
 * @param inFunctions The input functions for the character sequence.
 * @param inOptContext The context to set.
 * @param inOptFrontEnd The front end data to copy.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_charSeq_newStatic(
	sjme_attrInNotNull sjme_charSeq* inOutSeq,
	sjme_attrInNotNull const sjme_charSeq_functions* inFunctions,
	sjme_attrInNullable sjme_pointer inOptContext,
	sjme_attrInNullable sjme_frontEnd* inOptFrontEnd);

/**
 * Creates a character sequence that accesses the given standard C string
 * as Utf characters. 
 * 
 * @param inOutSeq The resultant sequence.
 * @param inString The string to wrap.
 * @return Any resultant error, if any.
 * @since 2024/07/26
 */
sjme_errorCode sjme_charSeq_newUtfStatic(
	sjme_attrInNotNull sjme_charSeq* inOutSeq,
	sjme_attrInNotNull sjme_lpcstr inString);

/**
 * Checks if the given character sequence starts with the given character
 * sequence.
 * 
 * @param inSeq The sequence to check.
 * @param outResult The result of the check.
 * @param startsWithSeq The char sequence to check the start for.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_startsWithCharSeq(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull const sjme_charSeq* startsWithSeq);

/**
 * Checks if the given character sequence starts with the given UTF string.
 * 
 * @param inSeq The sequence to check.
 * @param outResult The result of the check.
 * @param startsWithSeq The UTF sequence to check the start for.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_startsWithUtf(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf);
	
/**
 * Checks if the given character sequence starts with the given UTF string.
 * 
 * @param inSeq The sequence to check.
 * @param startsWithUtf The UTF sequence to check the start for.
 * @return Returns whether it matches, note that if there is an error
 * then @c SJME_JNI_FALSE will be returned and the error will be hidden.
 * @since 2024/08/08 
 */
sjme_jboolean sjme_charSeq_startsWithUtfR(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CHARSEQ_H
}
		#undef SJME_CXX_SQUIRRELJME_CHARSEQ_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CHARSEQ_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CHARSEQ_H */
