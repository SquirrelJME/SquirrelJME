/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Utilities.
 * 
 * @file
 * @since 2023/07/26
 */

#ifndef SJME_C_UTIL_H
#define SJME_C_UTIL_H

#include "sjme/error.h"
#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_UTIL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Clips the value so it is in the given range.
 * 
 * @param l The low value.
 * @param v The value to clip.
 * @param h The high value.
 * @return The value clipped in range.
 * @since 2025/12/22
 */
#define sjme_clip(l, v, h) \
	((v) < (l) ? (l) : ((v) >= (h) ? (h) : (v)))
	
/**
 * Returns the minimum of two values.
 * 
 * @param a The first value.
 * @param b The first value.
 * @return The minimum of two values.
 * @since 2025/11/30
 */
#define sjme_min(a, b) \
	(((a) < (b)) ? (a) : (b))

/**
 * Returns the maximum of two values.
 * 
 * @param a The first value.
 * @param b The first value.
 * @return The maximum of two values.
 * @since 2025/11/30
 */
#define sjme_max(a, b) \
	(((a) > (b)) ? (a) : (b))

/**
 * Function for returning the number of entries within a tree.
 * 
 * @param in The tree to get the size of.
 * @return The number of items in the tree.
 * @since 2023/07/26
 */
typedef sjme_jint (*sjme_tree_findCount)(sjme_pointer tree);

/**
 * Function for returning the hash of the search item.
 * 
 * @param what What to get the hash of.
 * @return The hash of the given search item.
 * @since 2023/07/26
 */
typedef sjme_jint (*sjme_tree_findHash)(void* what);

/**
 * Compares an entry in the tree at the given index with the given hash and
 * item.
 * 
 * @param tree The tree to search in.
 * @param what What to being searched for in the tree.
 * @param hash The hash generated from @link sjme_tree_findHash @endlink .
 * @param withIndex Compare @a hash and @a what against the given tree.
 * @return A negative value if lower, zero if equal, or a positive value if
 * greater.
 * @since 2023/07/26
 */
typedef sjme_jint (*sjme_tree_findCompare)(void* tree, void* what,
	sjme_jint hash, sjme_jint withIndex);

/**
 * Random number state.
 * 
 * @since 2023/12/02
 */
typedef struct sjme_random
{
	/** The current seed value. */
	sjme_jlong seed;
} sjme_random;

/**
 * Tree finding functions, used with @link sjme_tree_find @endlink to
 * determine how to search through a given tree.
 * 
 * @since 2023/07/26
 */
typedef struct sjme_tree_findFunc
{
	/** Count function. */
	sjme_tree_findCount count;
	
	/** Hash function. */
	sjme_tree_findHash hash;
	
	/** Compare function. */
	sjme_tree_findCompare compare;
} sjme_tree_findFunc;

/**
 * Compares two null values, nulls are placed before non-nulls.
 * 
 * @param a The first value.
 * @param b The second value.
 * @return The resultant comparison.
 * @since 2024/02/14
 */
sjme_jint sjme_compare_null(
	sjme_attrInNullable sjme_cpointer a,
	sjme_attrInNullable sjme_cpointer b);

/**
 * Initializes the random number generator.
 * 
 * @param outRandom The random state to initialize. 
 * @param seedHi The high seed value.
 * @param seedLo The low seed value.
 * @return Returns @link SJME_JNI_TRUE @endlink on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_random_init(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jint seedHi,
	sjme_attrInValue sjme_jint seedLo);

/**
 * Initializes the random number generator.
 * 
 * @param outRandom The random state to initialize. 
 * @param seed The seed value.
 * @return Returns @link SJME_JNI_TRUE @endlink on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_random_initL(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jlong seed);
	
/**
 * Returns the next random value.
 * 
 * @param random The random state.
 * @param outValue The output value.
 * @return Returns @link SJME_JNI_TRUE @endlink on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_random_nextInt(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue);
	
/**
 * Returns the next random value.
 * 
 * @param random The random state.
 * @return The next random value.
 * @since 2025/07/19
 */
sjme_jint sjme_random_nextIntR(
	sjme_attrInOutNotNull sjme_random* random);
	
/**
 * Returns the next random value within the given range.
 * 
 * @param random The random state.
 * @param outValue The output value.
 * @param maxValue The maximum exclusive value.
 * @return Returns @link SJME_JNI_TRUE @endlink on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_random_nextIntMax(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInPositiveNonZero sjme_jint maxValue);

/**
 * Returns the character at the given index.
 *
 * @param string The string to get from.
 * @param index The index within the string.
 * @return The given character or @c -1 if not valid.
 * @since 2023/12/16
 */
sjme_jint sjme_string_charAt(sjme_lpcstr string, sjme_jint index);

/**
 * Compares two strings up to the given number of characters each, nulls are
 * in the same order as @link sjme_compare_null() @endlink .
 * 
 * @param aString A string. 
 * @param aLen A length.
 * @param bString B string.
 * @param bLen B length.
 * @return The comparison between the two.
 * @since 2024/02/22
 */
sjme_jint sjme_string_compareN(sjme_lpcstr aString, sjme_jint aLen,
	sjme_lpcstr bString, sjme_jint bLen);

/**
 * Decodes the given UTF-8 character.
 *
 * @param at The character sequence to decode.
 * @param stringP Adjustable pointer to the string, when the character is
 * decoded then this will increment accordingly.
 * @return The decoded character or @c -1 if
 * it is not valid.
 * @since 2023/07/27
 */
sjme_jint sjme_string_decodeChar(sjme_lpcstr at, sjme_lpcstr* stringP);

/**
 * Hashes the given string in accordance to @c String::hashCode() .
 * 
 * @param string The string to hash.
 * @return The hashcode of the given string.
 * @since 2023/07/26
 */
sjme_jint sjme_string_hash(sjme_lpcstr string);

/**
 * Hashes the given string in accordance to @c String::hashCode() .
 * 
 * @param string The string to hash.
 * @param limit The string limit.
 * @return The hashcode of the given string.
 * @since 2024/02/20
 */
sjme_jint sjme_string_hashN(sjme_lpcstr string, sjme_jint limit);

/**
 * Returns the length of the string in accordance to @c String::length() .
 * 
 * @param string The string to get the length of.
 * @return The string length or @c -1 if it is not valid.
 * @since 2023/07/29
 */
sjme_jint sjme_string_length(sjme_lpcstr string);

/**
 * Returns the length of the string in accordance to @c String::length() .
 * 
 * @param string The string to get the length of.
 * @param limit The length limit of the C string.
 * @return The string length or @c -1 if it is not valid.
 * @since 2024/02/20
 */
sjme_jint sjme_string_lengthN(sjme_lpcstr string, sjme_jint limit);

/**
 * Swaps an unsigned integer value.
 *
 * @param in The input value.
 * @return The swapped value.
 * @since 2024/01/05
 */
sjme_juint sjme_swap_uint(
	sjme_attrInValue sjme_juint in);

/**
 * Swaps an integer value.
 *
 * @param in The input value.
 * @return The swapped value.
 * @since 2024/01/05
 */
#define sjme_swap_int(in) ((sjme_jint)sjme_swap_uint((sjme_juint)(in)))

/**
 * Swaps a long value.
 *
 * @param in The input value.
 * @return The swapped value.
 * @since 2024/01/05
 */
sjme_jlong sjme_swap_long(
	sjme_attrInValue sjme_jlong in);

/**
 * Swaps an unsigned short value.
 *
 * @param in The input value.
 * @return The swapped value.
 * @since 2024/01/05
 */
sjme_jchar sjme_swap_ushort(
	sjme_attrInValue sjme_jchar in);

/**
 * Swaps a short value.
 *
 * @param in The input value.
 * @return The swapped value.
 * @since 2024/01/05
 */
#define sjme_swap_short(in) ((sjme_jchar)sjme_swap_ushort((sjme_jchar)(in)))

#if defined(SJME_CONFIG_BIG_ENDIAN)
	/** A big endian unsigned short value. */
	#define sjme_big_ushort(v) ((sjme_jushort)(v))
#else
	/** A big endian unsigned short value. */
	#define sjme_big_ushort(v) (sjme_swap_ushort((v)))
#endif

/** A big endian signed short value. */
#define sjme_big_short(v) ((sjme_jshort)sjme_big_ushort((v)))

#if defined(SJME_CONFIG_BIG_ENDIAN)
	/** A big endian unsigned integer value. */
	#define sjme_big_uint(v) ((sjme_juint)(v))
#else
	/** A big endian unsigned integer value. */
	#define sjme_big_uint(v) (sjme_swap_uint((v)))
#endif

/** A big endian signed integer value. */
#define sjme_big_int(v) ((sjme_jint)sjme_big_uint((v)))
	
/**
 * Performs @c memmove() followed by shifting up by 8 the destination buffer,
 * then following a byte swap.
 * 
 * @param dest The destination.
 * @param src The source.
 * @param n The number of bytes to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/10
 */
sjme_errorCode sjme_swap_shu8_uint_memmove(
	sjme_attrInNotNull void* dest,
	sjme_attrInNotNull void* src,
	sjme_attrInPositiveNonZero sjme_jint n);
	
/**
 * Performs @c memmove() followed by swapping the destination buffer.
 * 
 * @param dest The destination.
 * @param src The source.
 * @param n The number of bytes to copy.
 * @return Any resultant error, if any.
 * @since 2024/07/10
 */
sjme_errorCode sjme_swap_uint_memmove(
	sjme_attrInNotNull void* dest,
	sjme_attrInNotNull void* src,
	sjme_attrInPositiveNonZero sjme_jint n);

/**
 * Locates an item within a tree.
 * 
 * @param tree The tree to search in.
 * @param what What is being searched for.
 * @param functions Functions used for the tree search logic.
 * @return The index where the item was found.
 * @since 2023/07/26
 */
sjme_jint sjme_tree_find(void* tree, void* what,
	const sjme_tree_findFunc* functions);

/**
 * Aligns the given address to the given alignment.
 * 
 * @param addr The address to align.
 * @param align The alignment to use.
 * @return The resultant aligned address.
 * @since 2025/02/10
 */
sjme_intPointer sjme_util_alignTo(sjme_intPointer addr,
	sjme_intPointer align);

/**
 * Aligns the given address to the given alignment.
 * 
 * @param addr The address to align.
 * @param align The alignment to use.
 * @return The resultant aligned address.
 * @since 2025/06/15
 */
#define sjme_util_alignToP(addr, align) \
	((sjme_pointer)sjme_util_alignTo(((sjme_intPointer)(addr)), (align)))
	
/**
 * Returns the number of bits in the value.
 * 
 * @param v The value to get the number of bits in. 
 * @return The number of bits in the value.
 * @since 2024/08/22
 */
sjme_juint sjme_util_intBitCountU(
	sjme_attrInValue sjme_juint v);

/**
 * Compacts bits to the left.
 * 
 * @code
 * 10101011110011011110111100000111 ->
 * 10101010000010010000100000000100
 * @endcode
 * 
 * @param v The bits to compact.
 * @param m The mask.
 * @return The compacted bits.
 * @since 2026/01/17
 */
sjme_juint sjme_util_intCompactLeft(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m);
	
/**
 * Compacts bits to the right.
 * 
 * @code
 * 10101011110011011110111100000111 ->
 * 10101000010001000010000100000001
 * @endcode
 * 
 * @param v The bits to compact.
 * @param m The mask.
 * @return The compacted bits.
 * @since 2026/01/17
 */
sjme_juint sjme_util_intCompactRight(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m);

/**
 * Extracts bits to the left.
 * 
 * @param v The bits to extract.
 * @param m The mask.
 * @return The extracted bits.
 * @since 2026/01/17
 */
sjme_juint sjme_util_intExtractLeft(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m);
	
/**
 * Extracts bits to the right.
 * 
 * @param v The bits to extract.
 * @param m The mask.
 * @return The extracted bits.
 * @since 2026/01/17
 */
sjme_juint sjme_util_intExtractRight(
	sjme_attrInValue sjme_juint v,
	sjme_attrInValue sjme_juint m);

/**
 * Returns the value with the highest bit set.
 * 
 * @param v The value to return the highest bit of. 
 * @return The highest bit of the value.
 * @since 2024/08/22
 */
sjme_juint sjme_util_intOneBitHighestU(
	sjme_attrInValue sjme_juint v);

/**
 * Returns the value with the lowest bit set.
 * 
 * @param v The value to return the lowest bit of. 
 * @return The lowest bit of the value.
 * @since 2026/01/17
 */
sjme_juint sjme_util_intOneBitLowestU(
	sjme_attrInValue sjme_juint v);

/**
 * Allows for shifting left/right by 32 for certain CPUs.
 * 
 * @param v The value to shift.
 * @param sh The shift amount, positive is left shift and negative is right
 * shift.
 * @return The resultant shifted value.
 * @since 2025/11/28
 */
sjme_jint sjme_util_intOverShift(
	sjme_attrInValue sjme_jint v,
	sjme_attrInRange(-32, 32) sjme_jint sh);
	
/**
 * Allows for shifting left/right by 32 for certain CPUs.
 * 
 * @param v The value to shift.
 * @param sh The shift amount, positive is left shift and negative is right
 * shift.
 * @return The resultant shifted value.
 * @since 2024/08/29
 */
sjme_juint sjme_util_intOverShiftU(
	sjme_attrInValue sjme_juint v,
	sjme_attrInRange(-32, 32) sjme_jint sh);

/**
 * Reverses the bits in the given integer value.
 * 
 * @param v The input value. 
 * @return The value with the reversed bits.
 * @since 2024/08/18 
 */
sjme_jint sjme_util_intReverse(
	sjme_attrInValue sjme_jint v);

/**
 * Reverses the bits in the given integer value.
 * 
 * @param v The input value. 
 * @return The value with the reversed bits.
 * @since 2024/08/18 
 */
sjme_juint sjme_util_intReverseU(
	sjme_attrInValue sjme_juint v);

/**
 * Returns the number of leading zeroes in the value.
 * 
 * @param v The value to check. 
 * @return The resultant number of leading zeroes.
 * @since 2024/08/22
 */
sjme_juint sjme_util_intZeroesLeadingU(
	sjme_attrInValue sjme_juint v);

/**
 * Returns the number of trailing zeroes in the value.
 * 
 * @param v The value to check. 
 * @return The resultant number of trailing zeroes.
 * @since 2026/01/17
 */
sjme_juint sjme_util_intZeroesTrailingU(
	sjme_attrInValue sjme_juint v);

/**
 * Converts integer value to binary.
 * 
 * @param destBuf The destination buffer.
 * @param destLen The destination length.
 * @param inVal The value to convert to binary.
 * @param bitCount The number of bits to output, if zero then this is all bits.
 * @return Any resultant error, if any.
 * @since 2024/08/24
 */
sjme_errorCode sjme_util_intToBinary(
	sjme_attrInNotNullBuf(destLen) sjme_lpstr destBuf,
	sjme_attrInPositiveNonZero sjme_jint destLen,
	sjme_attrInValue sjme_juint inVal,
	sjme_attrInPositiveNonZero sjme_juint bitCount);

/**
 * Trims ending whitespace from the end of the string.
 * 
 * @param buf The buffer to trim from. 
 * @param length The length of the input buffer.
 * @return On any resultant error, if any.
 * @since 2024/09/06
 */
sjme_errorCode sjme_util_lpstrTrimEnd(
	sjme_attrInNotNullBuf(length) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint length);

#if defined(SJME_CONFIG_HAS_NO_UNALIGNED16)
	
/**
 * Accesses an address for reading in an unaligned way.
 *
 * @param addr The address to access.
 * @return The address of the un-aligned address, or a pointer to a wrapper
 * which contains the value to be read.
 * @since 2025/01/11
 */
const sjme_jshort* sjme_util_memUnaligned16(void* addr);
	
#else
	
/**
 * Accesses an address in an unaligned way.
 *
 * @param addr The address to access.
 * @return The address of the un-aligned address, or a pointer to a wrapper
 * which contains the value to be read.
 * @since 2025/01/11
 */
#define sjme_util_memUnaligned16(addr) ((const sjme_jshort*)(addr))
	
#endif

#if defined(SJME_CONFIG_HAS_NO_UNALIGNED32)
	
/**
 * Accesses an address for reading in an unaligned way.
 *
 * @param addr The address to access.
 * @return The address of the un-aligned address, or a pointer to a wrapper
 * which contains the value to be read.
 * @since 2025/03/02
 */
const sjme_jint* sjme_util_memUnaligned32(void* addr);
	
/**
 * Writes to an address in an unaligned way.
 *
 * @param addr The address to access.
 * @param v The value to write.
 * @return The written value.
 * @since 2025/11/28
 */
sjme_jint* sjme_util_memUnaligned32W(void* addr, sjme_jint v);
	
#else
	
/**
 * Accesses an address in an unaligned way.
 *
 * @param addr The address to access.
 * @return The address of the un-aligned address, or a pointer to a wrapper
 * which contains the value to be read.
 * @since 2025/03/02
 */
#define sjme_util_memUnaligned32(addr) ((const sjme_jint*)(addr))

/**
 * Writes to an address in an unaligned way.
 *
 * @param addr The address to access.
 * @param v The value to write.
 * @return The written value.
 * @since 2025/11/28
 */
#define sjme_util_memUnaligned32W(addr, v) (*((sjme_jint*)(addr)) = (v))
	
#endif

#if defined(SJME_CONFIG_SIZEOF_SIZE_T) && SJME_CONFIG_SIZEOF_SIZE_T == 4

/**
 * Clamps a @code size_t @endcode to @code sjme_jint @endcode .
 *
 * @param in The input value.
 * @return The resultant clamped value.
 * @since 2026/04/13
 */
#define sjme_util_sizeToInt(in) ((sjme_jint)(in))

#else

/**
 * Clamps a @code size_t @endcode to @code sjme_jint @endcode .
 *
 * @param in The input value.
 * @return The resultant clamped value.
 * @since 2026/04/13
 */
sjme_jint sjme_util_sizeToInt(size_t in);

#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_UTIL_H
}
		#undef SJME_CXX_SQUIRRELJME_UTIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_UTIL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UTIL_H */
