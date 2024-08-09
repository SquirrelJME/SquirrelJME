/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard C types.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_STDTYPES_H
#define SQUIRRELJME_STDTYPES_H

#include <stdlib.h>
#include <stdint.h>

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STDTYPES_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

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
 * Promoted @c sjme_jbyte .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jbyte_promoted;

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
 * Promoted @c sjme_jubyte .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jubyte_promoted;

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
 * Promoted @c sjme_jshort .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jshort_promoted;

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
 * Promoted @c sjme_jchar .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jchar_promoted;

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
 * Fixed point.
 * 
 * @since 2024/06/27 
 */
typedef sjme_jint sjme_fixed;

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

#if defined(SJME_CONFIG_HAS_ARCH_IA16)
	/**
	 * Pointer to C string.
	 *
	 * @since 2023/12/17
	 */
	typedef sjme_cchar huge* sjme_lpstr;
#else
	/**
	 * Pointer to C string.
	 *
	 * @since 2023/12/17
	 */
	typedef sjme_cchar* sjme_lpstr;
#endif

/** Basic @c sjme_lpstr type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_lpstr ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpstr 1

#if defined(SJME_CONFIG_HAS_ARCH_IA16)
	/**
	 * Pointer to constant C string.
	 *
	 * @since 2023/12/17
	 */
	typedef const sjme_cchar huge* sjme_lpcstr;
#else
	/**
	 * Pointer to constant C string.
	 *
	 * @since 2023/12/17
	 */
	typedef const sjme_cchar* sjme_lpcstr;
#endif

/** Basic @c sjme_lpcstr type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpcstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_lpcstr ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpcstr 1

#if defined(SJME_CONFIG_HAS_ARCH_IA16)
	/**
	 * Generic pointer.
	 *
	 * @since 2023/12/27
	 */
	typedef void huge* sjme_pointer;
#else
	/**
	 * Generic pointer.
	 *
	 * @since 2023/12/27
	 */
	typedef void* sjme_pointer;
#endif

/** Basic @c sjme_pointer type identifier. */
#define SJME_TYPEOF_BASIC_sjme_pointer SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_pointer ? */
#define SJME_TYPEOF_IS_POINTER_sjme_pointer 1

#if defined(SJME_CONFIG_HAS_ARCH_IA16)
	/**
	 * Generic pointer to const data.
	 *
	 * @since 2023/12/27
	 */
	typedef void huge* sjme_cpointer;
#else
	/**
	 * Generic pointer to const data.
	 *
	 * @since 2023/12/27
	 */
	typedef void* sjme_cpointer;
#endif

/** Basic @c sjme_cpointer type identifier. */
#define SJME_TYPEOF_BASIC_sjme_cpointer SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_cpointer ? */
#define SJME_TYPEOF_IS_POINTER_sjme_cpointer 1

/**
 * Integer based pointer.
 * 
 * @since 2024/04/06
 */
typedef intptr_t sjme_intPointer;

/** Calculates a pointer offset. */
#define SJME_POINTER_OFFSET(base, off) \
	(sjme_pointer)(((sjme_intPointer)(base)) + ((sjme_intPointer)(off)))

#if defined(SJME_CONFIG_HAS_POINTER64)
	#define SJME_TYPEOF_BASIC_sjme_intPointer SJME_TYPEOF_BASIC_sjme_jpointer
#else
	#define SJME_TYPEOF_BASIC_sjme_intPointer SJME_TYPEOF_BASIC_sjme_juint
#endif

#define SJME_TYPEOF_IS_POINTER_sjme_intPointer 0

/**
 * Long value.
 * 
 * @since 2023/07/25
 */
typedef union sjme_jlong
{
	/** Parts of the long. */
	struct
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
	} part;
	
	/** The full long. */
	int64_t full;
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
 * Base object information.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_jobjectBase sjme_jobjectBase;

/**
 * Object type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobjectBase* sjme_jobject;

/** Basic @c sjme_jobject type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jobject SJME_BASIC_TYPE_ID_OBJECT

/** Java @c sjme_jobject type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jobject SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @c sjme_jobject ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jobject 1

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

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STDTYPES_H
}
		#undef SJME_CXX_SQUIRRELJME_STDTYPES_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDTYPES_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STDTYPES_H */
