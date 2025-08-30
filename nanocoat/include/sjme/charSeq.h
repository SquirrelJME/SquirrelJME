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

#ifndef SJME_C_CHARSEQ_H
#define SJME_C_CHARSEQ_H

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
 * Returns the character at the given index.
 * 
 * @param inSeq The input character sequence.
 * @param inIndex The index to get from.
 * @return The resultant character.
 * @since 2024/06/27
 */
typedef sjme_jchar (*sjme_charSeq_charAtFunc)(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex);

/**
 * Returns the length of the character sequence.
 * 
 * @param inSeq The input character sequence.
 * @param outLen The sequence length.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_charSeq_lengthFunc)(
	sjme_attrInNotNull sjme_charSeq inSeq,
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
	
	/** The length of the character sequence. */
	sjme_charSeq_lengthFunc length;
} sjme_charSeq_functions;

/**
 * The type of character encoding used in @c sjme_charSeq .
 *
 * @since 2025/03/07
 */
typedef enum sjme_charSeq_type
{
	/** Null character sequence. */
	SJME_CHAR_SEQ_TYPE_NULL,
	
	/** Functional character sequence. */
	SJME_CHAR_SEQ_TYPE_FUNCTION,
	
	/** Functional character sequence, static. */
	SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC,
	
	/** Narrow bytes only. */
	SJME_CHAR_SEQ_TYPE_NARROW,

	/** Wide chars only. */
	SJME_CHAR_SEQ_TYPE_WIDE,

	/** Java Modified-UTF. */
	SJME_CHAR_SEQ_TYPE_UTF,

	/** Static modified-UTF string. */
	SJME_CHAR_SEQ_TYPE_UTF_STATIC,

	/** Static wide chars only. */
	SJME_CHAR_SEQ_TYPE_WIDE_STATIC,
	
	/** The number of character sequence types. */
	SJME_CHAR_SEQ_NUM_TYPES,
} sjme_charSeq_type;
	
struct sjme_charSeqStatic
{
	/** The type of sequence this is. */
	sjme_charSeq_type type;

	/** The length of this sequence. */
	sjme_jint length;

	/** The hashcode for this string. */
	sjme_atomic_sjme_jint hash;

	/** The sequence data. */
	sjme_alignPointer union
	{
		/** The bytes stored in the sequence. */
		sjme_jbyte bytes[sjme_flexibleArrayCountUnion];

		/** The characters stored in the sequence. */
		sjme_jchar chars[sjme_flexibleArrayCountUnion];

		/** Information for function based character sequences. */
		struct
		{
			/** Functions that make up a functional character sequence. */
			const sjme_charSeq_functions* impl;
			
			/** Front-end data, as needed. */
			sjme_frontEndBindable frontEnd;
		} function;
		
		/** Reference to another sequence. */
		sjme_charSeq otherSeq;

		/** Static UTF pointer. */
		sjme_lpcstr staticUtf;

		/** Static wide pointer. */
		sjme_lpcwstr staticWide;
	} data;
};

/**
 * Iterator over character sequences.
 *
 * @since 2024/03/09
 */
typedef struct sjme_charSeq_it sjme_charSeq_it;

/**
 * Iterates the next character.
 *
 * @param it The iterator to iterate.
 * @return The next iterated character.
 * @since 2025/03/09
 */
typedef sjme_jchar (*sjme_charSeq_itFunc)(
	sjme_attrInNotNull sjme_charSeq_it* it);

struct sjme_charSeq_it
{
	/** The sequence this iterates over. */
	sjme_charSeq seq;

	/** The current character index. */
	sjme_jint index;
	
	/** Performs @code *(c) @endcode . */
	sjme_charSeq_itFunc d;
	
	/** Performs @code *(c++) @endcode . */
	sjme_charSeq_itFunc pp;
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
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar);

/**
 * Is the index in the sequence the given character?
 * 
 * @param inSeq The input character sequence.
 * @param inIndex The index to get from.
 * @param wantChar The requested character.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_charSeq_charAtIs(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar wantChar);

/**
 * Returns the character at the given index, note that any errors will be
 * hidden.
 * 
 * @param inSeq The input character sequence.
 * @param inIndex The index to get from.
 * @return The resultant character, any errors will result in @c 0 being
 * returned which may be a valid character.
 * @since 2025/03/09
 */
sjme_jchar sjme_charSeq_charAtR(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex);

/**
 * Makes a copy of the given character sequence.
 * 
 * @param allocPool The allocation pool to allocate within.
 * @param destCopy The destination copy.
 * @param sourceFrom The source to copy from.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_charSeq_dup(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* destCopy,
	sjme_attrInNotNull sjme_charSeq sourceFrom);

/**
 * Duplicates a character sequence to the given buffer.
 * 
 * @param src The source character sequence.
 * @param srcOff The source buffer.
 * @param dst The destination buffer.
 * @param dstOff The destination offset.
 * @param dstLimit The limit of the destination buffer.
 * @param maxChars The number of characters to copy. 
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_charSeq_dupToU(
	sjme_attrInNotNull sjme_charSeq src,
	sjme_attrInPositive sjme_jint srcOff,
	sjme_attrOutNotNullBuf(dstLimit) sjme_lpstr dst,
	sjme_attrInPositive sjme_jint dstOff,
	sjme_attrInPositive sjme_jint dstLimit,
	sjme_attrInNegativeOnePositive sjme_jint maxChars);
	
/**
 * Returns the length of the character sequence.
 * 
 * @param inSeq The input character sequence.
 * @param outLen The sequence length.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
sjme_errorCode sjme_charSeq_length(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outLen);

/**
 * Checks if the given character sequence equals the given character sequence.
 * 
 * @param aSeq The sequence to check.
 * @param bSeq The char sequence to check for equality against.
 * @param outResult The result of the check.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_equals(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_charSeq bSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult);

/**
 * Checks if the given character sequence equals the given character sequence,
 * this returns the result rather than storing it in an output.
 * 
 * @param aSeq The sequence to check.
 * @param bSeq The char sequence to check for equality against.
 * @return Returns whether it matches, note that if there is an error
 * then @c SJME_JNI_FALSE will be returned and the error will be hidden.
 * @since 2024/11/09
 */
sjme_jboolean sjme_charSeq_equalsR(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_charSeq bSeq);

/**
 * Checks if the given character sequence equals the given UTF string.
 * 
 * @param aSeq The sequence to check.
 * @param bUtf The UTF sequence to check for equality against.
 * @param outResult The result of the check.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_equalsUtf(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_lpcstr bUtf,
	sjme_attrOutNotNull sjme_jboolean* outResult);
	
/**
 * Checks if the given character sequence equals the given UTF string.
 * 
 * @param aSeq The sequence to check.
 * @param bUtf The UTF sequence to check for equality against.
 * @return Returns whether it matches, note that if there is an error
 * then @c SJME_JNI_FALSE will be returned and the error will be hidden.
 * @since 2024/08/08 
 */
sjme_jboolean sjme_charSeq_equalsUtfR(
	sjme_attrInNotNull sjme_charSeq aSeq,
	sjme_attrInNotNull sjme_lpcstr bUtf);

/**
 * Frees and cleans up a character sequence.
 * 
 * @param seq The sequence to clean up.
 * @return Any resultant error, if any.
 * @since 2025/06/13
 */
sjme_errorCode sjme_charSeq_free(
	sjme_attrInNotNull sjme_charSeq seq);

/**
 * Hashes the given string in accordance to Java's @c String.hashCode() .
 * 
 * @param inSeq The sequence to hash.
 * @param outHash The resultant hash code.
 * @return Any resultant error, if any.
 * @since 2025/01/25
 */
sjme_errorCode sjme_charSeq_hash(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jint* outHash);

/**
 * Hashes the given string in accordance to Java's @c String.hashCode() , note
 * that this will any errors that occur.
 * 
 * @param inSeq The sequence to hash.
 * @return The resultant hash code, this will be zero if any errors occur
 * which may still be considered a valid hash.
 * @since 2025/03/26
 */
sjme_errorCode sjme_charSeq_hashR(
	sjme_attrInNotNull sjme_charSeq inSeq);

/**
 * Initializes a new iterator.
 * 
 * @param inSeq The input sequence.
 * @param offset The offset into the sequence.
 * @param it The target iterator.
 * @return Any resultant error, if any.
 * @since 2025/03/09
 */
sjme_errorCode sjme_charSeq_itNew(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrOutNotNull sjme_charSeq_it* it);

/**
 * Initializes a new static function based character sequence.
 * 
 * @param outSeq The resultant sequence.
 * @param functions The functions for referring to the characters.
 * @param frontEnd Optional non-bindable front-end data to use.
 * @return Any resultant error, if any.
 * @since 2025/03/18
 */
sjme_errorCode sjme_charSeq_newFunctionStatic(
	sjme_attrOutNotNull sjme_charSeqStatic* outSeq,
	sjme_attrInNotNull const sjme_charSeq_functions* functions,
	sjme_attrInNullable sjme_frontEndBindable* frontEnd);
	
/**
 * Allocates a new narrow character sequence.
 * 
 * @param allocPool The pool to allocate within.
 * @param outSeq The resultant sequence.
 * @param narrow The input narrow bytes for the string.
 * @param offset The offset into the string sequence.
 * @param limitLen The length limit for the sequence.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_charSeq_newNarrow(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull const sjme_jbyte* narrow,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen);

/**
 * Allocates a new narrow character sequence from wide characters.
 * 
 * @param allocPool The pool to allocate within.
 * @param outSeq The resultant sequence.
 * @param wide The input wide characters for the string, the upper byte is
 * ignored.
 * @param offset The offset into the string sequence.
 * @param limitLen The length limit for the sequence.
 * @return Any resultant error, if any.
 * @since 2025/03/08
 */
sjme_errorCode sjme_charSeq_newNarrowFromWide(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull const sjme_jchar* wide,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen);

/**
 * Allocates a new modified-UTF character sequence.
 * 
 * @param allocPool The pool to allocate within.
 * @param outSeq The resultant sequence.
 * @param utfString The input modified-UTF string.
 * @param offset The offset into the string sequence.
 * @param limitLen The length limit for the sequence.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_charSeq_newUtf(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull sjme_lpcstr utfString,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen);

/**
 * Initializes a static character sequence from a UTF string.
 * 
 * @param outSeq The resultant static sequence.
 * @param utfString The string to base from.
 * @param offset The offset into the string sequence.
 * @param limitLen The length limit for the sequence.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_charSeq_newUtfStatic(
	sjme_attrOutNotNull sjme_charSeqStatic* outSeq,
	sjme_attrInNotNull sjme_lpcstr utfString,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen);

/**
 * Allocates a new wide character sequence.
 * 
 * @param allocPool The pool to allocate within.
 * @param outSeq The resultant sequence.
 * @param wide The input wide bytes for the string.
 * @param offset The offset into the string sequence.
 * @param limitLen The length limit for the sequence.
 * @return Any resultant error, if any.
 * @since 2025/03/07
 */
sjme_errorCode sjme_charSeq_newWide(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_charSeq* outSeq,
	sjme_attrInNotNull const sjme_jchar* wide,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen);

/**
 * Allocates a new static wide character sequence.
 * 
 * @param inOutSeq The input/output sequence.
 * @param wide The input wide bytes for the string.
 * @param offset The offset into the string sequence.
 * @param limitLen The length limit for the sequence.
 * @return Any resultant error, if any.
 * @since 2025/04/08
 */
sjme_errorCode sjme_charSeq_newWideStatic(
	sjme_attrInOutNotNull sjme_charSeqStatic* inOutSeq,
	sjme_attrInNotNull const sjme_jchar* wide,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint limitLen);

/**
 * Checks if the given character sequence starts with the given character
 * sequence.
 * 
 * @param inSeq The sequence to check.
 * @param outResult The result of the check.
 * @param otherSeq The char sequence to check the start for.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_startsWithCharSeq(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrOutNotNull sjme_jboolean* outResult,
	sjme_attrInNotNull sjme_charSeq otherSeq);

/**
 * Checks if the given character sequence starts with the given UTF string.
 * 
 * @param inSeq The sequence to check.
 * @param outResult The result of the check.
 * @param startsWithUtf The UTF sequence to check the start for.
 * @return Any resultant error, if any.
 * @since 2024/08/08 
 */
sjme_errorCode sjme_charSeq_startsWithUtf(
	sjme_attrInNotNull sjme_charSeq inSeq,
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
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInNotNull sjme_lpcstr startsWithUtf);
	
/**
 * Returns a temporary @c sjme_lpcstr over the character sequence.
 * 
 * @param inSeq The input sequence.
 * @return The temporary @c sjme_lpcstr .
 * @since 2025/03/07
 */
sjme_lpcstr sjme_charSeq_tempUtf(
	sjme_attrInNotNull sjme_charSeq inSeq);

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
