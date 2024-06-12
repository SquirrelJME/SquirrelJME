/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME NanoCoat Virtual Machine Header Definitions.
 * 
 * @since 2023/07/25
 */

#ifndef SQUIRRELJME_NVM_H
#define SQUIRRELJME_NVM_H

#include <stdlib.h>
#include <stdint.h>
#include <setjmp.h>

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_NVM_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

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
	((uncommonType*)(void*)(&((structType*)((base)))->uncommonMember))

/**
 * Basic data type identifier.
 *
 * @since 2023/07/25
 */
typedef enum sjme_basicTypeId
{
	/** Integer. */
	SJME_BASIC_TYPE_ID_INTEGER = 0,

	/** Integer. */
	SJME_JAVA_TYPE_ID_INTEGER = SJME_BASIC_TYPE_ID_INTEGER,

	/** Long. */
	SJME_BASIC_TYPE_ID_LONG = 1,

	/** Long. */
	SJME_JAVA_TYPE_ID_LONG = SJME_BASIC_TYPE_ID_LONG,

	/** Float. */
	SJME_BASIC_TYPE_ID_FLOAT = 2,

	/** Float. */
	SJME_JAVA_TYPE_ID_FLOAT = SJME_BASIC_TYPE_ID_FLOAT,

	/** Double. */
	SJME_BASIC_TYPE_ID_DOUBLE = 3,

	/** Double. */
	SJME_JAVA_TYPE_ID_DOUBLE = SJME_BASIC_TYPE_ID_DOUBLE,

	/** Object. */
	SJME_BASIC_TYPE_ID_OBJECT = 4,

	/** Object. */
	SJME_JAVA_TYPE_ID_OBJECT = SJME_BASIC_TYPE_ID_OBJECT,
	
	/** The number of Java type IDs. */
	SJME_NUM_JAVA_TYPE_IDS = 5,

	/** Boolean or byte. */
	SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE = SJME_NUM_JAVA_TYPE_IDS,
	
	/** Character or short type. */
	SJME_JAVA_TYPE_ID_SHORT_OR_CHAR = 6,
	
	/** Void type. */
	SJME_BASIC_TYPE_ID_VOID = 7,
	
	/** Void type. */
	SJME_JAVA_TYPE_ID_VOID = SJME_BASIC_TYPE_ID_VOID,
	
	/** End of extended Java types. */
	SJME_NUM_EXTENDED_JAVA_TYPE_IDS = 8,

	/** Short. */
	SJME_BASIC_TYPE_ID_SHORT = SJME_NUM_EXTENDED_JAVA_TYPE_IDS,

	/** Character. */
	SJME_BASIC_TYPE_ID_CHARACTER = 9,

	/** Specifically boolean. */
	SJME_BASIC_TYPE_ID_BOOLEAN = 10,

	/** Specifically jbyte. */
	SJME_BASIC_TYPE_ID_BYTE = 11,

	/** Number of basic type IDs. */
	SJME_NUM_BASIC_TYPE_IDS = 11
} sjme_basicTypeId;

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

/**
 * Boolean type.
 * 
 * @since 2023/07/25
 */
typedef enum sjme_jboolean
{
	SJME_JNI_FALSE = 0,

	SJME_JNI_TRUE = 1
} sjme_jboolean;

/** Basic @c sjme_jboolean type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jboolean SJME_BASIC_TYPE_ID_BOOLEAN

/** Java @c sjme_jboolean type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jboolean SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE

/** Is a pointer for @c sjme_jboolean ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jboolean 0

/**
 * Byte type.
 * 
 * @since 2023/07/25
 */
typedef int8_t sjme_jbyte;

/** Basic @c sjme_jbyte type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jbyte SJME_BASIC_TYPE_ID_BYTE

/** Java @c sjme_jbyte type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jbyte SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE

/** Is a pointer for @c sjme_jbyte ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jbyte 0

/**
 * Unsigned byte type.
 * 
 * @since 2023/08/09
 */
typedef uint8_t sjme_jubyte;

/** Basic @c sjme_jubyte type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jubyte SJME_BASIC_TYPE_ID_BYTE

/** Java @c sjme_jubyte type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jubyte SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE

/** Is a pointer for @c sjme_jubyte ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jubyte 0

/**
 * Short type.
 * 
 * @since 2023/07/25
 */
typedef int16_t sjme_jshort;

/** Basic @c sjme_jshort type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jshort SJME_BASIC_TYPE_ID_SHORT

/** Java @c sjme_jshort type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jshort SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @c sjme_jshort ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jshort 0

/**
 * Character type.
 * 
 * @since 2023/07/25
 */
typedef uint16_t sjme_jchar;

/** Basic @c sjme_jchar type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jchar SJME_BASIC_TYPE_ID_CHARACTER

/** Java @c sjme_jchar type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jchar SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @c sjme_jchar ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jchar 0

/**
 * Integer type.
 * 
 * @since 2023/07/25
 */
typedef int32_t sjme_jint;

/** Basic @c sjme_jint type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jint SJME_BASIC_TYPE_ID_INTEGER

/** Java @c sjme_jint type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jint SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @c sjme_jint ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jint 0

/**
 * Unsigned integer type.
 * 
 * @since 2023/11/20
 */
typedef uint32_t sjme_juint;

/** Basic @c sjme_juint type identifier. */
#define SJME_TYPEOF_BASIC_sjme_juint SJME_BASIC_TYPE_ID_INTEGER

/** Java @c sjme_juint type identifier. */
#define SJME_TYPEOF_JAVA_sjme_juint SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @c sjme_juint ? */
#define SJME_TYPEOF_IS_POINTER_sjme_juint 0

/**
 * C Character.
 *
 * @since 2024/01/03
 */
typedef char sjme_cchar;

#if defined(CHAR_BIT) && (CHAR_BIT == 64)
	/** Basic @c sjme_cchar type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_LONG
#elif defined(CHAR_BIT) && (CHAR_BIT == 32)
	/** Basic @c sjme_cchar type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_INTEGER
#elif defined(CHAR_BIT) && (CHAR_BIT == 16)
	/** Basic @c sjme_cchar type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_SHORT
#else
	/** Basic @c sjme_cchar type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_BYTE
#endif

/** Is a pointer for @c sjme_cchar ? */
#define SJME_TYPEOF_IS_POINTER_sjme_cchar 0

/**
 * Pointer to C string.
 *
 * @since 2023/12/17
 */
typedef sjme_cchar* sjme_lpstr;

/** Basic @c sjme_lpstr type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_lpstr ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpstr 1

/**
 * Pointer to constant C string.
 *
 * @since 2023/12/17
 */
typedef const sjme_cchar* sjme_lpcstr;

/** Basic @c sjme_lpcstr type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpcstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_lpcstr ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpcstr 1

/**
 * Generic pointer.
 *
 * @since 2023/12/27
 */
typedef void* sjme_pointer;

/** Basic @c sjme_pointer type identifier. */
#define SJME_TYPEOF_BASIC_sjme_pointer SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_pointer ? */
#define SJME_TYPEOF_IS_POINTER_sjme_pointer 1

/**
 * Integer based pointer.
 * 
 * @since 2024/04/06
 */
typedef intptr_t sjme_intPointer;

/** Calculates a pointer offset. */
#define SJME_POINTER_OFFSET(base, off) \
	(void*)(((sjme_intPointer)(base)) + ((sjme_intPointer)(off)))

/**
 * Long value.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_jlong
{
#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/** Low value. */
	sjme_juint lo;

	/** High value. */
	sjme_jint hi;
#else
	/** High value. */
	sjme_jint hi;
	
	/** Low value. */
	sjme_juint lo;
#endif
} sjme_jlong;

/** Basic @c sjme_jlong type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jlong SJME_BASIC_TYPE_ID_LONG

/** Java @c sjme_jlong type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jlong SJME_JAVA_TYPE_ID_LONG

/** Is a pointer for @c sjme_jlong ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jlong 0

/**
 * Float value.
 * 
 * @sinc 2023/07/25
 */
typedef struct sjme_jfloat
{
	sjme_jint value;
} sjme_jfloat;

/** Basic @c sjme_jfloat type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jfloat SJME_BASIC_TYPE_ID_FLOAT

/** Java @c sjme_jfloat type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jfloat SJME_JAVA_TYPE_ID_FLOAT

/** Is a pointer for @c sjme_jfloat ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jfloat 0

/**
 * Double value.
 * 
 * @sinc 2023/07/25
 */
typedef struct sjme_jdouble
{
#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/** Low value. */
	sjme_juint lo;

	/** High value. */
	sjme_juint hi;
#else
	/** High value. */
	sjme_juint hi;
	
	/** Low value. */
	sjme_juint lo;
#endif
} sjme_jdouble;

/** Basic @c sjme_jdouble type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jdouble SJME_BASIC_TYPE_ID_DOUBLE

/** Java @c sjme_jdouble type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jdouble SJME_JAVA_TYPE_ID_DOUBLE

/** Is a pointer for @c sjme_jdouble ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jdouble 0

/**
 * Temporary index.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_tempIndex;

/**
 * A range of values.
 *
 * @since 2024/01/03
 */
typedef struct sjme_range
{
	/** Start of the range. */
	sjme_jint start;

	/** End of the range. */
	sjme_jint end;
} sjme_range;

/**
 * Represents a pointer and a length.
 * 
 * @since 2024/02/04
 */
typedef struct sjme_pointerLen
{
	/** The pointer to the data. */
	sjme_pointer pointer;
	
	/** The length of the pointer. */
	sjme_jint length;
} sjme_pointerLen;

/**
 * A wrapper used by front ends, which is reserved for use, which stores a
 * natively bound object accordingly as needed.
 * 
 * @since 2023/12/06
 */
typedef sjme_pointer sjme_frontEndWrapper;

/**
 * Any data that is needed by the front end, which is reserved for use.
 *
 * @since 2023/12/14
 */
typedef sjme_pointer sjme_frontEndData;

/**
 * This structure stores any front end data as needed.
 *
 * @since 2023/12/14
 */
typedef struct sjme_frontEnd
{
	/** Any wrapper as needed. */
	sjme_frontEndWrapper wrapper;

	/** Any data as needed. */
	sjme_frontEndData data;
} sjme_frontEnd;

/**
 * Wraps the given front end pointer.
 *
 * @param p The pointer to wrap.
 * @since 2023/12/08
 */
#define SJME_FRONT_END_WRAP(p) ((sjme_frontEndWrapper)(p))

/** The Java type ID. */
typedef sjme_basicTypeId sjme_javaTypeId;

/**
 * Represents multiple type IDs.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_basicTypeIds
{
	/** The number of IDs. */
	sjme_jint count;
	
	/** The IDs. */
	const sjme_javaTypeId ids[sjme_flexibleArrayCount];
} sjme_basicTypeIds;

/**
 * Program counter address.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_pcAddr;

/**
 * Static linkage type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_staticLinkageType;

/**
 * Base object information.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_jobjectBase
{
	/** The reference count of this object, zero it becomes GCed. */
	sjme_jint refCount;
} sjme_jobjectBase;

/**
 * Object type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobjectBase* sjme_jobject;

/**
 * Generic value union.
 *
 * @since 2024/01/05
 */
typedef union sjme_jvalue
{
	/** Boolean. */
	sjme_jboolean z;

	/** Byte. */
	sjme_jbyte b;

	/** Character. */
	sjme_jchar c;

	/** Short. */
	sjme_jshort s;

	/** Integer. */
	sjme_jint i;

	/** Long. */
	sjme_jlong j;

	/** Float. */
	sjme_jfloat f;

	/** Double. */
	sjme_jdouble d;

	/** Object/Reference. */
	sjme_jobject l;
} sjme_jvalue;

/**
 * Class type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobject sjme_jclass;

/** Basic @c sjme_jobject type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jobject SJME_BASIC_TYPE_ID_OBJECT

/** Java @c sjme_jobject type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jobject SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_jobject ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jobject 1

/**
 * Throwable type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobject sjme_jthrowable;

typedef union sjme_anyData
{
	/** Integer. */
	sjme_jint jint;
	
	/** Object. */
	sjme_jobject jobject;
	
	/** Temporary index. */
	sjme_tempIndex tempIndex;
} sjme_anyData;

typedef struct sjme_any
{
	/** Data type used. */
	sjme_basicTypeId type;

	/** Data stored within. */
	sjme_anyData data;
} sjme_any;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
typedef struct sjme_nvm_state sjme_nvm_state;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_nvm_frame sjme_nvm_frame;

/**
 * Exception stack trace mechanism storage.
 *
 * @since 2023/12/08
 */
typedef volatile struct sjme_exceptTrace sjme_exceptTrace;

typedef struct sjme_nvm_thread
{
	/** The VM state this thread is in. */
	sjme_nvm_state* inState;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The thread ID. */
	sjme_jint threadId;
	
	/** The top of the stack. */
	sjme_nvm_frame* top;
	
	/** The number of frames. */
	sjme_jint numFrames;

	/** Current exception handler go back. */
	sjme_exceptTrace* except;
} sjme_nvm_thread;

typedef struct sjme_static_constValue
{
	/** Integer value. */
	sjme_jint jint;
	
	/** Long value. */
	sjme_jlong jlong;
	
	/** Float value. */
	sjme_jfloat jfloat;
	
	/** Double value. */
	sjme_jdouble jdouble;
	
	/** String value. */
	sjme_lpcstr jstring;
	
	/** Class name. */
	sjme_lpcstr jclass;
} sjme_static_constValue;

/**
 * Represents a field type.
 * 
 * @since 2023/08/10
 */
typedef struct sjme_static_fieldType
{
	/** The hash code for the field type. */
	sjme_jint hashCode;
	
	/** The field descriptor. */
	sjme_lpcstr descriptor;
	
	/** The basic type. */
	sjme_basicTypeId basicType;
} sjme_static_fieldType;

typedef struct sjme_static_classField
{
	/** Field name. */
	sjme_lpcstr name;
	
	/** The field type. */
	const sjme_static_fieldType* type;
	
	/** Flags. */
	sjme_jint flags;
	
	/** The constant value type. */
	sjme_basicTypeId valueType;
	
	/** The value. */
	sjme_static_constValue value;
} sjme_static_classField;

typedef struct sjme_static_classFields
{
	/** The number of fields. */
	sjme_jint count;
	
	/** Fields. */
	sjme_static_classField fields[sjme_flexibleArrayCount];
} sjme_static_classFields;

/**
 * Type used for method code functions.
 * 
 * @param currentState The current virtual machine state.
 * @param currentThread The current virtual machine thread.
 * @return Will return @c SJME_JNI_TRUE if execution completed without throwing
 * a @c Throwable object.
 * @since 2023/07/25
 */
typedef sjme_jboolean (*sjme_methodCodeFunction)(
	struct sjme_nvm_state* currentState,
	struct sjme_nvm_thread* currentThread);

/**
 * The variable mapping and setup for any given method.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_static_classCodeLimits
{
	/** The maximum number of @c sjme_basicTypeId local/stack variables. */
	const sjme_jubyte maxVariables[SJME_NUM_JAVA_TYPE_IDS];
} sjme_static_classCodeLimits;

/**
 * Contains information about method code and how variables should be placed
 * on execution and stack handling.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_static_classCode
{
	/** The variable count and thrown index count used. */
	const sjme_static_classCodeLimits* limits;
	
	/** The index where thrown objects are placed. */
	sjme_jshort thrownVarIndex;
	
	/** The method code. */
	sjme_methodCodeFunction code;
} sjme_static_classCode;

/**
 * Represents a standard Java method type, using field descriptors.
 * 
 * @since 2023/08/10
 */
typedef struct sjme_static_methodType
{
	/** The hash code for the method type. */
	sjme_jint hashCode;
	
	/** The descriptor for the method type. */
	sjme_lpcstr descriptor;
	
	/** The return type. */
	const sjme_static_fieldType* returnType;
	
	/** The number of arguments. */
	sjme_jint argCount;
	
	/** The arguments to the method. */
	const sjme_static_fieldType* argTypes[0];
} sjme_static_methodType;

typedef struct sjme_static_classMethod
{
	/** Method name. */
	sjme_lpcstr name;
	
	/** Flags. */
	sjme_jint flags;
	
	/** Name typed. */
	const sjme_static_methodType* type;
	
	/** Method code and any pertaining information. */
	const sjme_static_classCode* code;
} sjme_static_classMethod;

typedef struct sjme_static_classMethods
{
	/** The number of methods. */
	sjme_jint count;
	
	/** Methods. */
	sjme_static_classMethod methods[sjme_flexibleArrayCount];
} sjme_static_classMethods;

typedef struct sjme_static_classInterface
{
	sjme_lpcstr interfaceName;
} sjme_static_classInterface;

typedef struct sjme_static_classInterfaces
{
	/** The number of interfaces. */
	sjme_jint count;
	
	/** Interfaces. */
	sjme_static_classInterface interfaces[sjme_flexibleArrayCount];
} sjme_static_classInterfaces;

typedef struct sjme_static_resource
{
	/** The resource path. */
	sjme_lpcstr path;
	
	/** The hash for the path. */
	sjme_jint pathHash;
	
	/** The size of the resource. */
	sjme_jint size;
	
	/** The resource data. */
	const sjme_jbyte data[sjme_flexibleArrayCount];
} sjme_static_resource;

typedef struct sjme_static_linkage_data_classObject
{
	/** The class name. */
	sjme_lpcstr className;
} sjme_static_linkage_data_classObject;

typedef struct sjme_static_linkage_data_fieldAccess
{
	/** Is this static? */
	sjme_jboolean isStatic;
	
	/** Is this a store? */
	sjme_jboolean isStore;
	
	/** The source method name. */
	sjme_lpcstr sourceMethodName;
	
	/** The source method type. */
	sjme_lpcstr sourceMethodType;
	
	/** The target class. */
	sjme_lpcstr targetClass;
	
	/** The target field name. */
	sjme_lpcstr targetFieldName;
	
	/** The target field type. */
	sjme_lpcstr targetFieldType;
} sjme_static_linkage_data_fieldAccess;

typedef struct sjme_static_linkage_data_invokeSpecial
{
	/** The source method name. */
	sjme_lpcstr sourceMethodName;
	
	/** The source method type. */
	sjme_lpcstr sourceMethodType;
	
	/** The target class. */
	sjme_lpcstr targetClass;
	
	/** The target method name. */
	sjme_lpcstr targetMethodName;
	
	/** The target method type. */
	sjme_lpcstr targetMethodType;
} sjme_static_linkage_data_invokeSpecial;

typedef struct sjme_static_linkage_data_invokeNormal
{
	/** Is this a static invocation? */
	sjme_jboolean isStatic;

	/** The source method name. */
	sjme_lpcstr sourceMethodName;
	
	/** The source method type. */
	sjme_lpcstr sourceMethodType;
	
	/** The target class. */
	sjme_lpcstr targetClass;
	
	/** The target method name. */
	sjme_lpcstr targetMethodName;
	
	/** The target method type. */
	sjme_lpcstr targetMethodType;
} sjme_static_linkage_data_invokeNormal;

typedef struct sjme_static_linkage_data_stringObject
{
	/** The string value. */
	sjme_lpcstr string;
} sjme_static_linkage_data_stringObject;

typedef union sjme_static_linkage_data
{
	/** Reference to class object. */
	sjme_static_linkage_data_classObject classObject;
	
	/** Field access. */
	sjme_static_linkage_data_fieldAccess fieldAccess;
	
	/** Special invocation. */
	sjme_static_linkage_data_invokeSpecial invokeSpecial;
	
	/** Normal invocation. */
	sjme_static_linkage_data_invokeNormal invokeNormal;
	
	/** String object. */
	sjme_static_linkage_data_stringObject stringObject;
} sjme_static_linkage_data;

/**
 * Static linkage.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_static_linkage
{
	/** The type of linkage this is. */
	sjme_staticLinkageType type;
	
	/** Linkage data. */
	sjme_static_linkage_data data;
} sjme_static_linkage;

typedef struct sjme_static_linkages
{
	/** The number of linkages. */
	sjme_jint count;
	
	/** The define linkages. */
	sjme_static_linkage linkages[sjme_flexibleArrayCount];
} sjme_static_linkages;

typedef struct sjme_dynamic_linkage_data_classObject
{
	int todo;
} sjme_dynamic_linkage_data_classObject;

typedef struct sjme_dynamic_linkage_data_fieldAccess
{
	int todo;
} sjme_dynamic_linkage_data_fieldAccess;

typedef struct sjme_dynamic_linkage_data_invokeSpecial
{
	int todo;
} sjme_dynamic_linkage_data_invokeSpecial;

typedef struct sjme_dynamic_linkage_data_invokeNormal
{
	int todo;
} sjme_dynamic_linkage_data_invokeNormal;

typedef struct sjme_dynamic_linkage_data_stringObject
{
	int todo;
} sjme_dynamic_linkage_data_stringObject;

typedef union sjme_dynamic_linkage_data
{
	/** Reference to class object. */
	sjme_dynamic_linkage_data_classObject classObject;
	
	/** Field access. */
	sjme_dynamic_linkage_data_fieldAccess fieldAccess;
	
	/** Special invocation. */
	sjme_dynamic_linkage_data_invokeSpecial invokeSpecial;
	
	/** Normal invocation. */
	sjme_dynamic_linkage_data_invokeNormal invokeNormal;
	
	/** String object. */
	sjme_dynamic_linkage_data_stringObject stringObject;
} sjme_dynamic_linkage_data;

/**
 * Dynamic linkage.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_dynamic_linkage
{
	/** The type of linkage this is. */
	sjme_staticLinkageType type;
	
	/** Linkage data. */
	sjme_dynamic_linkage_data data;
} sjme_dynamic_linkage;

/**
 * Represents the frame of a stack tread.
 * 
 * @since 2023/11/15
 */
typedef struct sjme_nvm_frameTread
{
	/** The number of items in this tread. */
	sjme_jint count;
	
	/** The base index for the stack index. */
	sjme_jint stackBaseIndex;
	
	/** The maximum size this tread can be. */
	sjme_jint max;
	
	/** Values within the tread. */
	union
	{
		/** Integer values. */
		sjme_jint jints[sjme_flexibleArrayCountUnion];
		
		/** Long values. */
		sjme_jlong jlongs[sjme_flexibleArrayCountUnion];
		
		/** Float values. */
		sjme_jfloat jfloats[sjme_flexibleArrayCountUnion];
		
		/** Double values. */
		sjme_jdouble jdoubles[sjme_flexibleArrayCountUnion];
		
		/** Object references. */
		sjme_jobject jobjects[sjme_flexibleArrayCountUnion];
	} values;
} sjme_nvm_frameTread;

/**
 * Calculates the size of a frame tread for a given type.
 * 
 * @param type The type to get the size for.
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/15
 */
#define SJME_SIZEOF_FRAME_TREAD(type, count, baseType) \
	(sizeof(sjme_nvm_frameTread) + \
	/* Need to handle cases where values could be aligned up... */ \
	(offsetof(sjme_nvm_frameTread, values.SJME_TOKEN_PASTE(baseType,s)[0]) - \
		offsetof(sjme_nvm_frameTread, values)) + \
	(sizeof(type) * (size_t)(count)))

/**
 * Calculates the size of a frame tread for a given type via variable.
 * 
 * @param typeId The type to get the size for.
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/15
 */
static sjme_inline sjme_attrArtificial size_t SJME_SIZEOF_FRAME_TREAD_VAR(
	sjme_javaTypeId typeId, sjme_jint count)
{
	switch (typeId)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jint, count, jint);
		
		case SJME_JAVA_TYPE_ID_LONG:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jlong, count, jlong);
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jfloat, count, jfloat);
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jdouble, count, jdouble);
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jobject, count, jobject);
	}
	
	/* Invalid. */
	return 0;
}

/**
 * Represents information on a frame's stack storage.
 * 
 * @since 2023/11/16
 */
typedef struct sjme_nvm_frameStack
{
	/** The number of items in the stack. */
	sjme_jint count;
	
	/** The current limit of this structure. */
	sjme_jint limit;
	
	/** The stack order. */
	sjme_javaTypeId order[sjme_flexibleArrayCount];
} sjme_nvm_frameStack;

/**
 * Calculates the size of a frame stack.
 * 
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_FRAME_STACK(count) \
	(sizeof(sjme_nvm_frameStack) + \
	(sizeof(sjme_javaTypeId) * (size_t)(count)))

typedef struct sjme_nvm_frameLocalMap
{
	/** The maximum number of locals. */
	sjme_jint max;
	
	/** Mapping of a specific variable to a given type index. */
	union
	{
		sjme_jbyte to[SJME_NUM_JAVA_TYPE_IDS];
	} maps[sjme_flexibleArrayCount];
} sjme_nvm_frameLocalMap;

/**
 * Calculates the size of the frame local variable map.
 * 
 * @param count The number of items in the mapping.
 * @return The size in bytes of the local mapping.
 * @since 2023/11/26
 */
#define SJME_SIZEOF_FRAME_LOCAL_MAP(count) \
	(sizeof(sjme_nvm_frameLocalMap) + \
	(SJME_SIZEOF_STRUCT_MEMBER(sjme_nvm_frameLocalMap, maps[0]) * (count)))

struct sjme_nvm_frame
{
	/** The thread this frame is in. */
	sjme_nvm_thread* inThread;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The parent frame. */
	sjme_nvm_frame* parent;
	
	/** The frame index in the thread. */
	sjme_jint frameIndex;
	
	/** The current program counter. */
	sjme_pcAddr pc;
	
	/** Object which is waiting to be thrown for exception handling. */
	sjme_jobject waitingThrown;
	
	/** Frame linkage. */
	sjme_dynamic_linkage* linkage;
	
	/** Temporary stack. */
	sjme_any* tempStack;
	
	/** Reference to this. */
	sjme_jobject thisRef;
	
	/** Class reference. */
	sjme_jclass classObjectRef;
	
	/** The current stack information. */
	sjme_nvm_frameStack* stack;
	
	/** Treads for the stack and locals. */
	sjme_nvm_frameTread* treads[SJME_NUM_BASIC_TYPE_IDS];
	
	/** Mapping of local variables to the tread indexes per type. */
	const sjme_nvm_frameLocalMap* localMap;
};

/**
 * Contains the payload information.
 * 
 * @since 2023/07/27
 */
typedef struct sjme_payload_config sjme_payload_config;

/**
 * Hook for garbage collection detection and/or cancel capability.
 * 
 * @param frame The frame this is garbage collecting in.
 * @param gcWhat what is being garbage collected?
 * @return Returns @c SJME_JNI_TRUE if garbage collection should continue.
 * @since 2023/11/17
 */
typedef sjme_jboolean (*sjme_nvm_stateHookGcFunc)(sjme_nvm_frame* frame,
	sjme_jobject gcWhat);

/**
 * Hooks for alternative function.
 * 
 * @since 2023/11/17
 */
typedef struct sjme_nvm_stateHooks
{
	/** Garbage collection. */
	sjme_nvm_stateHookGcFunc gc;
} sjme_nvm_stateHooks;

/**
 * Structure which stores the pooled memory allocator.
 *
 * @since 2023/11/18
 */
typedef struct sjme_alloc_pool sjme_alloc_pool;

/**
 * Boot parameters for NanoCoat.
 *
 * @since 2023/07/27
 */
typedef struct sjme_nvm_bootParam sjme_nvm_bootParam;

/**
 * Standard Suite structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suiteCore sjme_rom_suiteCore;

/**
 * Opaque suite structure type.
 *
 * @since 2023/12/22
 */
typedef struct sjme_rom_suiteCore* sjme_rom_suite;

/**
 * Structure for a single task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_taskCore* sjme_nvm_task;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
struct sjme_nvm_state
{
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;

	/** The memory pool to use for allocations. */
	sjme_alloc_pool* allocPool;

	/** The reserved memory pool. */
	sjme_alloc_pool* reservedPool;

	/** The copy of the input boot parameters. */
	const sjme_nvm_bootParam* bootParamCopy;
	
	/** Hooks for the state. */
	const sjme_nvm_stateHooks* hooks;

	/* The suite containing all the libraries. */
	sjme_rom_suite suite;
};

/**
 * Method initialization start.
 *
 * @since 2023/07/25
 */
#define SJME_NANOCOAT_START_CALL ((sjme_pcAddr)-1)

/**
 * Method closing end.
 *
 * @since 2023/07/25
 */
#define SJME_NANOCOAT_END_CALL ((sjme_pcAddr)-2)

/**
 * Error codes.
 * 
 * @since 2023/11/14
 */
typedef enum sjme_errorCode
{
	/** No error. */
	SJME_ERROR_NONE = 1,

	/** Generic unknown error. */
	SJME_ERROR_UNKNOWN = 0,

	/** Generic unknown error. */
	SJME_ERROR_UNKNOWN_NEGATIVE = -1,
	
	/** Null arguments. */
	SJME_ERROR_NULL_ARGUMENTS = -2,
	
	/** Local variable out of bounds. */
	SJME_ERROR_LOCAL_INDEX_INVALID = -3,
	
	/** Stack variable out of bounds. */
	SJME_ERROR_STACK_INDEX_INVALID = -4,
	
	/** Stack underflow. */
	SJME_ERROR_STACK_UNDERFLOW = -5,
	
	/** Stack overflow. */
	SJME_ERROR_STACK_OVERFLOW = -6,
	
	/** Top is not an integer type. */
	SJME_ERROR_TOP_NOT_INTEGER = -7,
	
	/** Top is not a long type. */
	SJME_ERROR_TOP_NOT_LONG = -8,
	
	/** Top is not a float type. */
	SJME_ERROR_TOP_NOT_FLOAT = -9,
	
	/** Top is not a double type. */
	SJME_ERROR_TOP_NOT_DOUBLE = -10,
	
	/** Top is not a object type. */
	SJME_ERROR_TOP_NOT_OBJECT = -11,
	
	/** Frame is missing stack treads. */
	SJME_ERROR_FRAME_MISSING_STACK_TREADS = -12,
	
	/** Invalid read of stack. */
	SJME_ERROR_STACK_INVALID_READ = -13,
	
	/** Invalid write of stack. */
	SJME_ERROR_STACK_INVALID_WRITE = -14,
	
	/** Invalid read of stack. */
	SJME_ERROR_LOCAL_INVALID_READ = -15,
	
	/** Invalid write of stack. */
	SJME_ERROR_LOCAL_INVALID_WRITE = -16,
	
	/** Invalid reference pop. */
	SJME_ERROR_INVALID_REFERENCE_POP = -17,
	
	/** Invalid reference push. */
	SJME_ERROR_INVALID_REFERENCE_PUSH = -18,
	
	/** Failed to garbage collect object. */
	SJME_ERROR_COULD_NOT_GC_OBJECT = -19,
	
	/** Object reference count is not zero. */
	SJME_ERROR_OBJECT_REFCOUNT_NOT_ZERO = -20,
	
	/** Garbage collection of object cancelled. */
	SJME_ERROR_OBJECT_GC_CANCELLED = -21,

	/** Out of memory. */
	SJME_ERROR_OUT_OF_MEMORY = -22,

	/** Pool initialization failed. */
	SJME_ERROR_POOL_INIT_FAILED = -23,

	/** Invalid argument. */
	SJME_ERROR_INVALID_ARGUMENT = -24,

	/** Not implemented. */
	SJME_ERROR_NOT_IMPLEMENTED = -25,

	/** Invalid tread read. */
	SJME_ERROR_TREAD_INVALID_READ = -26,

	/** Invalid tread write. */
	SJME_ERROR_TREAD_INVALID_WRITE = -27,

	/** There are no suites available. */
	SJME_ERROR_NO_SUITES = -28,

	/** Classpath cannot be obtained by both ID and Name. */
	SJME_ERROR_CLASS_PATH_BY_BOTH = -29,

	/** Illegal state. */
	SJME_ERROR_ILLEGAL_STATE = -30,

	/** A library was not found. */
	SJME_ERROR_LIBRARY_NOT_FOUND = -31,

	/** Boot failure. */
	SJME_ERROR_BOOT_FAILURE = -32,

	/** Generic JNI exception. */
	SJME_ERROR_JNI_EXCEPTION = -33,

	/** Memory has been corrupted. */
	SJME_ERROR_MEMORY_CORRUPTION = -34,

	/** Index out of bounds. */
	SJME_ERROR_INDEX_OUT_OF_BOUNDS = -35,

	/** Unsupported operation. */
	SJME_ERROR_UNSUPPORTED_OPERATION = -36,

	/** Resource not found. */
	SJME_ERROR_RESOURCE_NOT_FOUND = -37,

	/** Unexpected end of file. */
	SJME_ERROR_UNEXPECTED_EOF = -38,
	
	/** Invalid identifier. */
	SJME_ERROR_INVALID_IDENTIFIER = -39,
	
	/** Invalid binary name. */
	SJME_ERROR_INVALID_BINARY_NAME = -40,
	
	/** Invalid field type. */
	SJME_ERROR_INVALID_FIELD_TYPE = -41,
	
	/** Invalid method type. */
	SJME_ERROR_INVALID_METHOD_TYPE = -42,
	
	/** Invalid class name. */
	SJME_ERROR_INVALID_CLASS_NAME = -43,
	
	/** Could not load library. */
	SJME_ERROR_COULD_NOT_LOAD_LIBRARY = -44,
	
	/** Invalid library symbol. */
	SJME_ERROR_INVALID_LIBRARY_SYMBOL = -45,
	
	/** There is no graphics display. */
	SJME_ERROR_HEADLESS_DISPLAY = -46,
	
	/** Cannot create something. */
	SJME_ERROR_CANNOT_CREATE = -47,
	
	/** Invalid thread state. */
	SJME_ERROR_INVALID_THREAD_STATE = -48,
	
	/** Component is already in a container. */
	SJME_ERROR_ALREADY_IN_CONTAINER = -49,
	
	/** Not a sub component. */
	SJME_ERROR_NOT_SUB_COMPONENT = -50,
	
	/** No such class exists. */
	SJME_ERROR_NO_CLASS = -51,
	
	/** No such method exists. */
	SJME_ERROR_NO_METHOD = -52,
	
	/** There is no listener. */
	SJME_ERROR_NO_LISTENER = -53,
	
	/** Cancel close of window. */
	SJME_ERROR_CANCEL_WINDOW_CLOSE = -54,
	
	/** The class cannot be casted. */
	SJME_ERROR_CLASS_CAST = -55,
	
	/** The number of error codes. */
	SJME_NUM_ERROR_CODES = -56
} sjme_errorCode;

/**
 * Propagates an error code which allows others to run accordingly.
 * 
 * @param error The current error code.
 * @param expression The result from the expression.
 * @return If @c expression is an error, that will be returned otherwise
 * the value in @c error provided @c error is not an error.
 * @since 2024/01/18
 */
sjme_errorCode sjme_error_also(
	sjme_errorCode error, sjme_errorCode expression);

/**
 * Similar to @c sjme_error_also except this allows multiple error expressions
 * to be passed until the final is done via @c sjme_error_alsoVEnd() .
 * 
 * @param error The current error state.
 * @param ... All of the expressions, ends on @c sjme_error_alsoVEnd() .
 * @return The resultant error code.
 * @since 2024/01/18
 */
sjme_errorCode sjme_error_alsoV(
	sjme_errorCode error, ...);

/**
 * The end expression for @c sjme_error_alsoV() .
 * 
 * @return The ending sequence for error codes.
 * @since 2024/01/18 
 */
sjme_errorCode sjme_error_alsoVEnd(void);

/**
 * Is this expression considered an error?
 *
 * @param error The expression.
 * @since 2023/12/08
 */
sjme_jboolean sjme_error_is(
	sjme_errorCode error);

/**
 * Determines the default error code to use.
 *
 * @param error The error code.
 * @return Either @c error or a default error.
 * @since 2023/12/29
 */
sjme_errorCode sjme_error_default(
	sjme_errorCode error);

/**
 * Determines the default error code to use.
 *
 * @param error The error code.
 * @param otherwise The other error code rather than @c SJME_ERROR_UNKNOWN.
 * @return Either @c error or @c otherwise if the former is not valid.
 * @since 2023/12/29
 */
sjme_errorCode sjme_error_defaultOr(
	sjme_errorCode error, sjme_errorCode otherwise);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_NVM_H
}
		#undef SJME_CXX_SQUIRRELJME_NVM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_NVM_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_NVM_H */
