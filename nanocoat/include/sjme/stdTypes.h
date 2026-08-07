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
 * @file
 * @since 2024/08/09
 */

#ifndef SJME_C_STDTYPES_H
#define SJME_C_STDTYPES_H

#include "sjme/config.h"
#include "sjme/stdGone.h"

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
	/* This is an integer sized enum. */
	sjme_enumInt(sjme_basicTypeId),

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
	
	/** Void type. */
	SJME_BASIC_TYPE_ID_VOID = SJME_NUM_JAVA_TYPE_IDS,
	
	/** Void type. */
	SJME_JAVA_TYPE_ID_VOID = SJME_BASIC_TYPE_ID_VOID,

	/** Boolean or byte. */
	SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE = 6,
	
	/** Character or short type. */
	SJME_JAVA_TYPE_ID_SHORT_OR_CHAR = 7,
	
	/** End of extended Java types. */
	SJME_NUM_EXTENDED_JAVA_TYPE_IDS = 8,

	/** Specifically boolean. */
	SJME_BASIC_TYPE_ID_BOOLEAN = SJME_NUM_EXTENDED_JAVA_TYPE_IDS,

	/** Specifically jbyte. */
	SJME_BASIC_TYPE_ID_BYTE = 9,

	/** Short. */
	SJME_BASIC_TYPE_ID_SHORT = 10,

	/** Character. */
	SJME_BASIC_TYPE_ID_CHARACTER = 11,

	/** Number of basic type IDs. */
	SJME_NUM_BASIC_TYPE_IDS = 12,

	/** Narrow type. */
	SJME_STACK_TYPE_NARROW = 24,

	/** Wide type. */
	SJME_STACK_TYPE_WIDE = 48,
} sjme_basicTypeId;

/**
 * Boolean type.
 * 
 * @since 2023/07/25
 */
typedef enum sjme_jboolean
{
	/** False. */
	SJME_JNI_FALSE = 0,
	
	/** True. */
	SJME_JNI_TRUE = 1
} sjme_jboolean;

/** Basic @link sjme_jboolean @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jboolean SJME_BASIC_TYPE_ID_BOOLEAN

/** Java @link sjme_jboolean @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jboolean SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE

/** Is a pointer for @link sjme_jboolean @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jboolean 0

/**
 * Byte type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jbyteNative sjme_jbyte;

/** Basic @link sjme_jbyte @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jbyte SJME_BASIC_TYPE_ID_BYTE

/** Java @link sjme_jbyte @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jbyte SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE

/** Is a pointer for @link sjme_jbyte @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jbyte 0

/**
 * Promoted @link sjme_jbyte @endlink .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jbyte_promoted;

/**
 * Unsigned byte type.
 * 
 * @since 2023/08/09
 */
typedef sjme_jubyteNative sjme_jubyte;

/** Basic @link sjme_jubyte @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jubyte SJME_BASIC_TYPE_ID_BYTE

/** Java @link sjme_jubyte @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jubyte SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE

/** Is a pointer for @link sjme_jubyte @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jubyte 0

/**
 * Promoted @link sjme_jubyte @endlink .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jubyte_promoted;

/**
 * Short type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jshortNative sjme_jshort;

/** Basic @link sjme_jshort @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jshort SJME_BASIC_TYPE_ID_SHORT

/** Java @link sjme_jshort @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jshort SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @link sjme_jshort @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jshort 0

/**
 * Promoted @link sjme_jshort @endlink .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jshort_promoted;

/**
 * Unsigned Short type.
 * 
 * @since 2025/07/04
 */
typedef sjme_jushortNative sjme_jushort;

/** Basic @link sjme_jushort @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jushort SJME_BASIC_TYPE_ID_CHARACTER

/** Java @link sjme_jushort @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jushort SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @link sjme_jushort @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jushort 0

/**
 * Promoted @link sjme_jushort @endlink .
 * 
 * @since 2025/07/04
 */
typedef int sjme_jushort_promoted;

/**
 * Character type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jushortNative sjme_jchar;

/** Basic @link sjme_jchar @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jchar SJME_BASIC_TYPE_ID_CHARACTER

/** Java @link sjme_jchar @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jchar SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @link sjme_jchar @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jchar 0

/**
 * Promoted @link sjme_jchar @endlink .
 * 
 * @since 2024/06/20
 */
typedef int sjme_jchar_promoted;

/**
 * Integer type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jintNative sjme_jint;

/** Basic @link sjme_jint @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jint SJME_BASIC_TYPE_ID_INTEGER

/** Java @link sjme_jint @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jint SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @link sjme_jint @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jint 0

/**
 * Unsigned integer type.
 * 
 * @since 2023/11/20
 */
typedef sjme_juintNative sjme_juint;

/** Basic @link sjme_juint @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_juint SJME_BASIC_TYPE_ID_INTEGER

/** Java @link sjme_juint @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_juint SJME_JAVA_TYPE_ID_INTEGER

/** Is a pointer for @link sjme_juint @endlink ? */
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
	/** Basic @link sjme_cchar @endlink type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_LONG
#elif defined(CHAR_BIT) && (CHAR_BIT == 32)
	/** Basic @link sjme_cchar @endlink type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_INTEGER
#elif defined(CHAR_BIT) && (CHAR_BIT == 16)
	/** Basic @link sjme_cchar @endlink type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_SHORT
#else
	/** Basic @link sjme_cchar @endlink type identifier. */
	#define SJME_TYPEOF_BASIC_sjme_cchar SJME_BASIC_TYPE_ID_BYTE
#endif

/** Is a pointer for @link sjme_cchar @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_cchar 0

/**
 * Pointer to C string.
 *
 * @since 2023/12/17
 */
typedef sjme_cchar sjme_attrHugeP* sjme_lpstr;

/** Basic @link sjme_lpstr @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_lpstr @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpstr 1

/**
 * Pointer to constant C string.
 *
 * @since 2023/12/17
 */
typedef const sjme_cchar sjme_attrHugeP* sjme_lpcstr;

/** Basic @link sjme_lpcstr @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpcstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_lpcstr @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpcstr 1

/**
 * Pointer to wide C string.
 *
 * @since 2025/01/16
 */
typedef sjme_jchar sjme_attrHugeP* sjme_lpwstr;

/** Basic @link sjme_lpwstr @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpwstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_lpwstr @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpwstr 1

/**
 * Pointer to constant wide C string.
 *
 * @since 2025/01/16
 */
typedef const sjme_jchar sjme_attrHugeP* sjme_lpcwstr;

/** Basic @link sjme_lpcwstr @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_lpcwstr SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_lpcwstr @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_lpcwstr 1

/**
 * Generic pointer.
 *
 * @since 2023/12/27
 */
typedef void sjme_attrHugeP* sjme_pointer;

/** Basic @link sjme_pointer @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_pointer SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_pointer @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_pointer 1

/**
 * Generic volatile pointer.
 *
 * @since 2025/01/31
 */
typedef volatile void sjme_attrHugeP* sjme_vpointer;

/** Basic @link sjme_pointer @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_vpointer SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_pointer @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_vpointer 1

/**
 * Generic pointer to const data.
 *
 * @since 2023/12/27
 */
typedef const void sjme_attrHugeP* sjme_cpointer;

/** Basic @link sjme_cpointer @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_cpointer SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_cpointer @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_cpointer 1

/**
 * Buffer type.
 * 
 * @since 2024/08/13
 */
typedef sjme_pointer sjme_buffer;

/**
 * Const buffer type.
 * 
 * @since 2024/08/13
 */
typedef sjme_cpointer sjme_cbuffer;
	
#pragma region(sjme_jlongNative)

/** Basic @link sjme_jlongNative @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jlongNative SJME_BASIC_TYPE_ID_LONG

/** Java @link sjme_jlongNative @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jlongNative SJME_JAVA_TYPE_ID_LONG

/** Is a pointer for @link sjme_jlongNative @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jlongNative 0

/** Basic @link sjme_julongNative @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_julongNative SJME_BASIC_TYPE_ID_LONG

/** Java @link sjme_julongNative @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_julongNative SJME_JAVA_TYPE_ID_LONG

/** Is a pointer for @link sjme_julongNative @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_julongNative 0
	
#pragma endregion(sjme_jlongNative)
#pragma region(sjme_intPointer)

/**
 * Integer based pointer.
 * 
 * @since 2025/12/23
 */
typedef sjme_intPointerNative sjme_intPointer;

/** Calculates a pointer offset. */
#define SJME_POINTER_OFFSET(base, off) \
	(sjme_pointer)(((sjme_intPointer)(base)) + ((sjme_intPointer)(off)))

/** Determines if a pointer overflows. */
#define SJME_POINTER_OVERFLOW(base, off) \
	((sjme_intPointer)SJME_POINTER_OFFSET((base), (off)) < \
		(sjme_intPointer)(base))

#if SJME_CONFIG_HAS_POINTER == 64
	#define SJME_TYPEOF_BASIC_sjme_intPointer \
		SJME_TYPEOF_BASIC_sjme_jlongNative
#elif SJME_CONFIG_HAS_POINTER == 32
	#define SJME_TYPEOF_BASIC_sjme_intPointer SJME_TYPEOF_BASIC_sjme_juint
#elif SJME_CONFIG_HAS_POINTER == 16
	#define SJME_TYPEOF_BASIC_sjme_intPointer SJME_TYPEOF_BASIC_sjme_jushort
#elif SJME_CONFIG_HAS_POINTER == 8
	#define SJME_TYPEOF_BASIC_sjme_intPointer SJME_TYPEOF_BASIC_sjme_jubyte
#else
	#error Unsupported pointer size
#endif

#define SJME_TYPEOF_IS_POINTER_sjme_intPointer 0
	
#pragma endregion(sjme_intPointer)
	
/**
 * Max size integer.
 *
 * @since 2025/07/13
 */
typedef sjme_jlongNative sjme_intMax;
	
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
	sjme_jlongNative full;
} sjme_jlong;

/** Basic @link sjme_jlong @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jlong SJME_BASIC_TYPE_ID_LONG

/** Java @link sjme_jlong @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jlong SJME_JAVA_TYPE_ID_LONG

/** Is a pointer for @link sjme_jlong @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jlong 0

/**
 * Float value.
 * 
 * @sinc 2023/07/25
 */
typedef union sjme_jfloat
{
	/** The raw integer bit value. */
	sjme_jint bits;

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/** The native float value. */
	sjme_jfloatNative native;
#endif
} sjme_jfloat;

/** Basic @link sjme_jfloat @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jfloat SJME_BASIC_TYPE_ID_FLOAT

/** Java @link sjme_jfloat @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jfloat SJME_JAVA_TYPE_ID_FLOAT

/** Is a pointer for @link sjme_jfloat @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jfloat 0

/**
 * Double value.
 * 
 * @sinc 2023/07/25
 */
typedef union sjme_jdouble
{
	struct sjme_packed
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
	} bits;

	/** Long bits. */
	int64_t longBits;

#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
	/** Native hardware double value. */
	sjme_jdoubleNative native;
#endif
} sjme_jdouble;

/** Basic @link sjme_jdouble @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jdouble SJME_BASIC_TYPE_ID_DOUBLE

/** Java @link sjme_jdouble @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jdouble SJME_JAVA_TYPE_ID_DOUBLE

/** Is a pointer for @link sjme_jdouble @endlink ? */
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
 * A range of short values.
 *
 * @since 2024/01/03
 */
typedef struct sjme_rangeShort
{
	/** Start of the range. */
	sjme_jshort start;

	/** End of the range. */
	sjme_jshort end;
} sjme_rangeShort;

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

/** The Java type ID. */
typedef sjme_basicTypeId sjme_javaTypeId;

/** The Extended type ID. */
typedef sjme_basicTypeId sjme_extendedTypeId;

/** Is the given type ID considered wide? */
#define SJME_TYPEID_IS_WIDE(t) \
	((t) == SJME_JAVA_TYPE_ID_LONG || (t) == SJME_JAVA_TYPE_ID_DOUBLE || \
	(t) == SJME_STACK_TYPE_WIDE)

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

/** Basic @link sjme_jobject @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jobject SJME_BASIC_TYPE_ID_OBJECT

/** Java @link sjme_jobject @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jobject SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_jobject @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jobject 1

/**
 * Class type.
 * 
 * @since 2024/10/19
 */
typedef struct sjme_jclassBase sjme_jclassBase;

/**
 * Class type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jclassBase* sjme_jclass;

/** Basic @link sjme_jclass @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jclass SJME_TYPEOF_BASIC_sjme_jobject

/** Java @link sjme_jclass @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jclass SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @link sjme_jclass @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jclass SJME_TYPEOF_IS_POINTER_sjme_jobject

/**
 * String type.
 * 
 * @since 2025/01/20
 */
typedef struct sjme_jstringBase sjme_jstringBase;

/**
 * String type.
 * 
 * @since 2025/01/20
 */
typedef sjme_jstringBase* sjme_jstring;

/** Basic @link sjme_jstring @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jstring SJME_TYPEOF_BASIC_sjme_jobject

/** Java @link sjme_jstring @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jstring SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @link sjme_jstring @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jstring SJME_TYPEOF_IS_POINTER_sjme_jobject

/**
 * Array type.
 * 
 * @since 2025/03/16
 */
typedef struct sjme_jarrayBase sjme_jarrayBase;

/**
 * Array type, a @link SJME_NVM_STRUCT_ARRAY_INSTANCE @endlink .
 * 
 * @since 2025/03/16
 */
typedef sjme_jarrayBase* sjme_jarray;

/** Basic @link sjme_jarray @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jarray SJME_TYPEOF_BASIC_sjme_jobject

/** Java @link sjme_jarray @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jarray SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @link sjme_jarray @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jarray SJME_TYPEOF_IS_POINTER_sjme_jobject

/**
 * Weak reference.
 *
 * @since 2025/06/21
 */
typedef struct sjme_jweakBase sjme_jweakBase;

/**
 * Weak reference.
 *
 * @since 2025/06/21
 */
typedef sjme_jweakBase* sjme_jweak;
	
/** Basic @link sjme_jweak @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jweak SJME_TYPEOF_BASIC_sjme_jobject

/** Java @link sjme_jweak @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jweak SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @link sjme_jweak @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jweak SJME_TYPEOF_IS_POINTER_sjme_jobject

/**
 * Throwable object.
 * 
 * @since 2025/07/05
 */
typedef struct sjme_jthrowableBase sjme_jthrowableBase;

/**
 * Throwable object.
 * 
 * @since 2025/07/05
 */
typedef sjme_jthrowableBase* sjme_jthrowable;

/** Basic @link sjme_jthrowable @endlink type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jthrowable SJME_BASIC_TYPE_ID_OBJECT

/** Java @link sjme_jthrowable @endlink type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jthrowable SJME_BASIC_TYPE_ID_OBJECT

/** Is a pointer for @link sjme_jthrowable @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jthrowable 1
	
/**
 * A character sequence which contains a set of characters within a string.
 * 
 * @since 2024/06/26
 */
typedef struct sjme_charSeqStatic sjme_charSeqStatic;

/**
 * A character sequence which contains a set of characters within a string.
 * 
 * @since 2024/06/26
 */
typedef sjme_charSeqStatic* sjme_charSeq;

/** Is a pointer for @link sjme_charSeq @endlink ? */
#define SJME_TYPEOF_IS_POINTER_sjme_charSeq SJME_TYPEOF_IS_POINTER_sjme_jobject

/**
 * Generic value union, primitives only without @link sjme_jobject @endlink .
 *
 * @since 2025/07/18
 */
typedef union sjme_jvaluePrimitive
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
} sjme_jvaluePrimitive;

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
 * Typed @link sjme_value @endlink .
 *
 * @since 2025/01/04
 */
typedef struct sjme_jvalueTyped
{
	/** The type of this value. */
	sjme_javaTypeId t;

	/** The value of this. */
	sjme_jvalue v;
} sjme_jvalueTyped;

/**
 * Structure which stores the pooled memory allocator.
 *
 * @since 2023/11/18
 */
typedef struct sjme_alloc_poolBase sjme_alloc_poolBase;

/**
 * Structure which stores the pooled memory allocator.
 *
 * @since 2023/11/18
 */
typedef volatile sjme_alloc_poolBase* sjme_alloc_pool;

/**
 * An unresolved function.
 *
 * @since 2025/07/04
 */
typedef int (*sjme_undefinedFunction)(int ignored, ...);

/**
 * An unresolved function, with export call.
 *
 * @since 2025/07/04
 */
typedef int (sjme_attrExportCall *sjme_undefinedExportFunction)(int ignored,
	...);

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
