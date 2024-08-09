/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Token utilities.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_TOKENUTILS_H
#define SQUIRRELJME_TOKENUTILS_H

#include "sjme/config.h"
#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TOKENUTILS_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Pastes two tokens together.
 * 
 * @param a The first token.
 * @param b The second token.
 * @since 2023/11/15
 */
#define SJME_TOKEN_PASTE(a, b) a##b

/**
 * Pasting two tokens but with preprocessing.
 * 
 * @param a The first token.
 * @param b The second token.
 * @since 2023/11/16
 */
#define SJME_TOKEN_PASTE_PP(a, b) SJME_TOKEN_PASTE(a, b)

/**
 * Pastes three tokens together.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @since 2024/01/03
 */
#define SJME_TOKEN_PASTE3(a, b, c) a##b##c

/**
 * Pasting three tokens but with preprocessing.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @since 2024/01/03
 */
#define SJME_TOKEN_PASTE3_PP(a, b, c) SJME_TOKEN_PASTE3(a, b, c)

/**
 * Pastes four tokens together.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @param d The fourth token.
 * @since 2024/01/03
 */
#define SJME_TOKEN_PASTE4(a, b, c, d) a##b##c##d

/**
 * Pasting four tokens but with preprocessing.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @param d The fourth token.
 * @since 2024/01/03
 */
#define SJME_TOKEN_PASTE4_PP(a, b, c, d) SJME_TOKEN_PASTE4(a, b, c, d)

/**
 * Pastes five tokens together.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @param d The fourth token.
 * @param e The fifth token.
 * @since 2024/01/03
 */
#define SJME_TOKEN_PASTE5(a, b, c, d, e) a##b##c##d##e

/**
 * Pasting five tokens but with preprocessing.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @param d The fourth token.
 * @param e The fifth token.
 * @since 2024/01/03
 */
#define SJME_TOKEN_PASTE5_PP(a, b, c, d, e) SJME_TOKEN_PASTE5(a, b, c, d, e)

/**
 * Pastes six tokens together.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @param d The fourth token.
 * @param e The fifth token.
 * @param f The sixth token.
 * @since 2024/07/01
 */
#define SJME_TOKEN_PASTE6(a, b, c, d, e, f) a##b##c##d##e##f

/**
 * Pasting six tokens but with preprocessing.
 *
 * @param a The first token.
 * @param b The second token.
 * @param c The third token.
 * @param d The fourth token.
 * @param e The fifth token.
 * @param f The sixth token.
 * @since 2024/07/01
 */
#define SJME_TOKEN_PASTE6_PP(a, b, c, d, e, f) \
	SJME_TOKEN_PASTE6(a, b, c, d, e, f)

/**
 * Stringifies the given token.
 * 
 * @param s The token to stringify.
 * @since 2023/11/24
 */
#define SJME_TOKEN_STRING(s) #s

/**
 * Stringifies the given token.
 * 
 * @param s The token to stringify.
 * @since 2023/11/24
 */
#define SJME_TOKEN_STRING_PP(s) SJME_TOKEN_STRING(s)

/**
 * Represents a single token.
 *
 * @param t The token to resolve.
 * @since 2023/12/17
 */
#define SJME_TOKEN_SINGLE(t) t

#define SJME_TOKEN_STARS_0
#define SJME_TOKEN_STARS_1 *
#define SJME_TOKEN_STARS_2 **
#define SJME_TOKEN_STARS_3 ***
#define SJME_TOKEN_STARS_4 ****
#define SJME_TOKEN_STARS_5 *****
#define SJME_TOKEN_STARS_6 ******
#define SJME_TOKEN_STARS_7 *******
#define SJME_TOKEN_STARS_8 ********
#define SJME_TOKEN_STARS_9 *********

#define SJME_TOKEN_STARS_C0
#define SJME_TOKEN_STARS_C1 P
#define SJME_TOKEN_STARS_C2 PP
#define SJME_TOKEN_STARS_C3 PPP
#define SJME_TOKEN_STARS_C4 PPPP
#define SJME_TOKEN_STARS_C5 PPPPP
#define SJME_TOKEN_STARS_C6 PPPPPP
#define SJME_TOKEN_STARS_C7 PPPPPPP
#define SJME_TOKEN_STARS_C8 PPPPPPPP
#define SJME_TOKEN_STARS_C9 PPPPPPPPP

#define SJME_TOKEN_STARS_H0 0
#define SJME_TOKEN_STARS_H1 1
#define SJME_TOKEN_STARS_H2 1
#define SJME_TOKEN_STARS_H3 1
#define SJME_TOKEN_STARS_H4 1
#define SJME_TOKEN_STARS_H5 1
#define SJME_TOKEN_STARS_H6 1
#define SJME_TOKEN_STARS_H7 1
#define SJME_TOKEN_STARS_H8 1
#define SJME_TOKEN_STARS_H9 1

/**
 * Generic pointer star types.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_TOKEN_TYPE(type, numPointerStars) \
	type SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_##numPointerStars)

/**
 * Semicolon token.
 *
 * @since 2024/01/09
 */
#define SJME_TOKEN_SEMI ;

/**
 * Does this have pointer stars?
 *
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_TOKEN_HAS_STARS(numPointerStars) \
	SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_H##numPointerStars)

/** SquirrelJME version string. */
#define SQUIRRELJME_VERSION SJME_TOKEN_STRING_PP(SQUIRRELJME_VERSION_TRIM)

/**
 * Calculates the size of a struct member.
 * 
 * @param type The type of the struct.
 * @param member The member to check.
 * @return The size of the given member.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_STRUCT_MEMBER(type, member) \
	(sizeof((*((type*)0)).member))

/**
 * Uncommon structure size.
 *
 * @param structType The structure type.
 * @param uncommonMember The uncommon member.
 * @param uncommonSize The uncommon size to use.
 * @since 2024/01/01
 */
#define SJME_SIZEOF_UNCOMMON_N(structType, uncommonMember, uncommonSize) \
	(sizeof(structType) + (offsetof(structType, \
		uncommonMember[0]) - offsetof(structType, uncommonMember)) + \
		(uncommonSize))

/**
 * Returns the uncommon member.
 *
 * @param structType The structure type.
 * @param uncommonMember The uncommon member.
 * @param uncommonType The uncommon type.
 * @param base The base structure pointer to access.
 * @since 2024/01/01
 */
#define SJME_UNCOMMON_MEMBER(structType, uncommonMember, uncommonType, base) \
	((uncommonType*)(sjme_pointer)(&((structType*)((base)))->uncommonMember))

/**
 * Returns the basic type ID of the given type.
 *
 * @param type The type to get the basic type of.
 * @since 2023/12/17
 */
#define SJME_TYPEOF_BASIC(type) SJME_TOKEN_PASTE_PP(SJME_TYPEOF_BASIC_, type)

/**
 * Returns the java type ID of the given type.
 *
 * @param type The type to get the basic type of.
 * @since 2023/12/17
 */
#define SJME_TYPEOF_JAVA(type) SJME_TOKEN_PASTE_PP(SJME_TYPEOF_JAVA_, type)

#define SJME_TYPEOF_IS_POINTER_X00 0
#define SJME_TYPEOF_IS_POINTER_X10 1
#define SJME_TYPEOF_IS_POINTER_X01 1
#define SJME_TYPEOF_IS_POINTER_X11 1

/**
 * Is the given type a pointer?
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @return If this is a pointer or not.
 * @since 2024/01/09
 */
#define SJME_TYPEOF_IS_POINTER(type, numPointerStars) \
	SJME_TOKEN_SINGLE(SJME_TOKEN_PASTE3_PP(SJME_TYPEOF_IS_POINTER_X, \
		SJME_TOKEN_PASTE_PP(SJME_TYPEOF_IS_POINTER_, type), \
		SJME_TOKEN_PASTE_PP(SJME_TOKEN_STARS_H, numPointerStars)))

#define SJME_TYPEOF_IS_NOT_POINTER_X0 1
#define SJME_TYPEOF_IS_NOT_POINTER_X1 0

/**
 * Is the given type not a pointer?
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @return If this is not a pointer or it is one.
 * @since 2024/01/09
 */
#define SJME_TYPEOF_IS_NOT_POINTER(type, numPointerStars) \
	SJME_TOKEN_SINGLE(SJME_TOKEN_PASTE_PP(SJME_TYPEOF_IS_NOT_POINTER_X, \
		SJME_TYPEOF_IS_POINTER(type, numPointerStars)))

#define SJME_TYPEOF_IF_POINTER_X0(snippet)
#define SJME_TYPEOF_IF_POINTER_X1(snippet) snippet

/**
 * If the type is a pointer, place the given snippet.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars
 * @param snippet The snippet to place.
 * @return Either @c snippet or nothing.
 * @since 2024/01/09
 */
#define SJME_TYPEOF_IF_POINTER(type, numPointerStars, snippet) \
    SJME_TOKEN_PASTE_PP(SJME_TYPEOF_IF_POINTER_X, \
		SJME_TYPEOF_IS_POINTER(type, numPointerStars))(snippet)

/**
 * If the type is not a pointer, place the given snippet.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars
 * @param snippet The snippet to place.
 * @return Either @c snippet or nothing.
 * @since 2024/01/09
 */
#define SJME_TYPEOF_IF_NOT_POINTER(type, numPointerStars, snippet) \
    SJME_TOKEN_PASTE_PP(SJME_TYPEOF_IF_POINTER_X, \
		SJME_TYPEOF_IS_NOT_POINTER(type, numPointerStars))(snippet)

#define SJME_TYPEOF_IF_POINTER_ORX0(snippet, orSnippet) orSnippet
#define SJME_TYPEOF_IF_POINTER_ORX1(snippet, orSnippet) snippet

/**
 * If the type is a pointer, place the given snippet.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars
 * @param snippet The snippet to place.
 * @param orSnippet The snippet if it is a pointer.
 * @return Either @c snippet or nothing.
 * @since 2024/01/09
 */
#define SJME_TYPEOF_IF_POINTER_OR(type, numPointerStars, snippet, orSnippet) \
    SJME_TOKEN_PASTE_PP(SJME_TYPEOF_IF_POINTER_ORX, \
		SJME_TYPEOF_IS_POINTER(type, numPointerStars))(snippet, orSnippet)

/**
 * If the type is not a pointer, place the given snippet.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars
 * @param snippet The snippet to place.
 * @param orSnippet The snippet if it is a pointer.
 * @return Either @c snippet or nothing.
 * @since 2024/01/09
 */
#define SJME_TYPEOF_IF_NOT_POINTER_OR(type, numPointerStars, snippet, \
	orSnippet) \
    SJME_TOKEN_PASTE_PP(SJME_TYPEOF_IF_POINTER_ORX, \
		SJME_TYPEOF_IS_NOT_POINTER(type, numPointerStars))(snippet, orSnippet)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TOKENUTILS_H
}
		#undef SJME_CXX_SQUIRRELJME_TOKENUTILS_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TOKENUTILS_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TOKENUTILS_H */
