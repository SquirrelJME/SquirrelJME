/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Handling of classes.
 * 
 * @since 2024/01/01
 */

#ifndef SQUIRRELJME_CLASSY_H
#define SQUIRRELJME_CLASSY_H

#include "sjme/nvm/nvm.h"
#include "sjme/seekable.h"
#include "sjme/list.h"
#include "sjme/nvm/descriptor.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSY_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The version of the class.
 * 
 * @since 2024/09/13
 */
typedef enum sjme_class_version
{
	/** CLDC 1.1 (JSR 30). */
	SJME_CLASS_CLDC_1_0 = 2949123,
	
	/** CLDC 1.1 (JSR 139). */
	SJME_CLASS_CLDC_1_1 = 3080192,
	
	/** CLDC 8. */
	SJME_CLASS_CLDC_1_8 = 3342336,
} sjme_class_version;

/**
 * Core class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_class_infoCore sjme_class_infoCore;

/**
 * Opaque class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_class_infoCore* sjme_class_info;

/**
 * Core method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodInfoCore sjme_class_methodInfoCore;

/**
 * Opaque method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodInfoCore* sjme_class_methodInfo;

/**
 * Method list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_class_methodInfo, 0);

/** The basic type of @c sjme_class_methodInfo . */
#define SJME_TYPEOF_BASIC_sjme_class_methodInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Core field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldInfoCore sjme_class_fieldInfoCore;

/**
 * Opaque field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldInfoCore* sjme_class_fieldInfo;

/**
 * Field list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_class_fieldInfo, 0);

/** The basic type of @c sjme_class_fieldInfo . */
#define SJME_TYPEOF_BASIC_sjme_class_fieldInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Exception handling information.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_exceptionHandler
{
	/** The range of the exception where it applies. */
	sjme_range range;

	/** The handler PC address. */
	sjme_jint handlerPc;

	/** The type that this catches. */
	sjme_desc_binaryName handles;
} sjme_class_exceptionHandler;

/** A list of exceptions. */
SJME_LIST_DECLARE(sjme_class_exceptionHandler, 0);

/** The basic type of @c sjme_class_exceptionHandler . */
#define SJME_TYPEOF_BASIC_sjme_class_exceptionHandler \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Method code information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_codeInfoCore sjme_class_codeInfoCore;

/**
 * Opaque method code structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_codeInfoCore* sjme_class_codeInfo;

/**
 * Access flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_accessFlags
{
	/** Is this public? */
	sjme_jboolean public : 1;

	/** Is this private? */
	sjme_jboolean private : 1;

	/** Is this protected? */
	sjme_jboolean protected : 1;
} sjme_class_accessFlags;

/**
 * Class flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_classFlags
{
	/** Access flags. */
	sjme_class_accessFlags access;

	/** Is the class final? */
	sjme_jboolean final : 1;

	/** Is the class super? */
	sjme_jboolean super : 1;

	/** Is the class an interface? */
	sjme_jboolean interface : 1;

	/** Is the class abstract? */
	sjme_jboolean abstract : 1;

	/** Is the class synthetic? */
	sjme_jboolean synthetic : 1;

	/** Is the class an annotation? */
	sjme_jboolean annotation : 1;

	/** Is the class an enum? */
	sjme_jboolean enumeration : 1;
} sjme_class_classFlags;

/**
 * Common member flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_memberFlags
{
	/** Access flags. */
	sjme_class_accessFlags access;

	/** Static member? */
	sjme_jboolean isStatic : 1;

	/** Final member? */
	sjme_jboolean final : 1;

	/** Synthetic member? */
	sjme_jboolean synthetic : 1;
} sjme_class_memberFlags;

/**
 * Field flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_fieldFlags
{
	/** Common member flags. */
	sjme_class_memberFlags member;

	/** Is this volatile? */
	sjme_jboolean isVolatile : 1;

	/** Is this transient? */
	sjme_jboolean transient : 1;

	/** Is this an enumeration. */
	sjme_jboolean enumeration : 1;
} sjme_class_fieldFlags;

/**
 * Method flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_class_methodFlags
{
	/** Common member flags. */
	sjme_class_memberFlags member;

	/** Synchronized, monitor entry/exit on call? */
	sjme_jboolean synchronized : 1;

	/** Bridge method, generated by the compiler? */
	sjme_jboolean bridge : 1;

	/** Variadic arguments? */
	sjme_jboolean varargs : 1;

	/** Is this a native method? */
	sjme_jboolean native : 1;

	/** Abstract? */
	sjme_jboolean abstract : 1;

	/** Strict floating point? */
	sjme_jboolean strictfp : 1;
} sjme_class_methodFlags;

/**
 * The type that a constant pool entry may be.
 *
 * @since 2024/01/04
 */
typedef enum sjme_class_poolType
{
	/** Null entry. */
	SJME_CLASS_POOL_TYPE_NULL = 0,

	/** Modified UTF. */
	SJME_CLASS_POOL_TYPE_UTF = 1,

	/** Unused 2. */
	SJME_CLASS_POOL_TYPE_UNUSED_2 = 2,

	/** Integer constant. */
	SJME_CLASS_POOL_TYPE_INTEGER = 3,

	/** Float constant. */
	SJME_CLASS_POOL_TYPE_FLOAT = 4,

	/** Long constant. */
	SJME_CLASS_POOL_TYPE_LONG = 5,

	/** Double constant. */
	SJME_CLASS_POOL_TYPE_DOUBLE = 6,

	/** Class constant. */
	SJME_CLASS_POOL_TYPE_CLASS = 7,

	/** String constant. */
	SJME_CLASS_POOL_TYPE_STRING = 8,

	/** Field reference. */
	SJME_CLASS_POOL_TYPE_FIELD = 9,

	/** Method reference. */
	SJME_CLASS_POOL_TYPE_METHOD = 10,

	/** Interface method reference. */
	SJME_CLASS_POOL_TYPE_INTERFACE_METHOD = 11,

	/** Name and type. */
	SJME_CLASS_POOL_TYPE_NAME_AND_TYPE = 12,

	/** Unused 13. */
	SJME_CLASS_POOL_TYPE_UNUSED_13 = 13,

	/** Unused 14. */
	SJME_CLASS_POOL_TYPE_UNUSED_14 = 14,

	/** Method handle. */
	SJME_CLASS_POOL_TYPE_METHOD_HANDLE = 15,

	/** Method type. */
	SJME_CLASS_POOL_TYPE_METHOD_TYPE = 16,

	/** Unused 17. */
	SJME_CLASS_POOL_TYPE_UNUSED_17 = 17,

	/** Invoke dynamic. */
	SJME_CLASS_POOL_TYPE_INVOKE_DYNAMIC = 18,

	/** The number of pool types. */
	SJME_NUM_CLASS_POOL_TYPE
} sjme_class_poolType;

/**
 * A @c SJME_CLASS_POOL_TYPE_CLASS which represents a class or interface.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryClass
{
	/** The descriptor this represents. */
	sjme_lpcstr descriptor;
} sjme_class_poolEntryClass;

/**
 * A @c SJME_CLASS_POOL_TYPE_DOUBLE which represents a double constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryDouble
{
	/** The constant value. */
	sjme_jdouble value;
} sjme_class_poolEntryDouble;

/**
 * A @c SJME_CLASS_POOL_TYPE_NAME_AND_TYPE which represents a name and type
 * of a member without the class.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryNameAndType sjme_class_poolEntryNameAndType;

/**
 * The type of entry 
 * 
 * @since 2024/01/16
 */
typedef enum sjme_class_poolEntryMemberType
{
	/** Reference to field member. */
	SJME_CLASS_POOL_ENTRY_MEMBER_TYPE_FIELD,
	
	/** Reference to method member. */
	SJME_CLASS_POOL_ENTRY_MEMBER_TYPE_METHOD,
	
	/** Reference to interface method member. */
	SJME_CLASS_POOL_ENTRY_MEMBER_TYPE_INTERFACE_METHOD,
	
	/** The number of member types. */
	SJME_NUM_CLASS_POOL_ENTRY_MEMBER_TYPE
} sjme_class_poolEntryMemberType;

/**
 * Either @c SJME_CLASS_POOL_TYPE_FIELD , @c SJME_CLASS_POOL_TYPE_METHOD ,
 * or @c SJME_CLASS_POOL_TYPE_INTERFACE_METHOD which represents a reference
 * to a class member.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryMember
{
	/** The type of entry this. */
	sjme_class_poolEntryMemberType type;

	/** The class this refers to. */
	sjme_lpcstr inClass;

	/** The name and type used. */
	const sjme_class_poolEntryNameAndType* nameAndType;
} sjme_class_poolEntryMember;

/**
 * A @c SJME_CLASS_POOL_TYPE_FLOAT which represents a float constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryFloat
{
	/** The constant value. */
	sjme_jfloat value;
} sjme_class_poolEntryFloat;

/**
 * A @c SJME_CLASS_POOL_TYPE_INTEGER which represents an integer constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryInteger
{
	/** The constant value. */
	sjme_jint value;
} sjme_class_poolEntryInteger;

/**
 * A @c SJME_CLASS_POOL_TYPE_LONG which represents a long constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryLong
{
	/** The constant value. */
	sjme_jlong value;
} sjme_class_poolEntryLong;

struct sjme_class_poolEntryNameAndType
{
	/** The name. */
	sjme_lpcstr name;

	/** The type. */
	sjme_lpcstr type;
};

/**
 * A @c SJME_CLASS_POOL_TYPE_UTF which is a modified UTF-8 entry.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntryUtf
{
	/** The hash code for the entry. */
	sjme_jint hash;

	/** The UTF data for this entry. */
	sjme_lpcstr utf;
} sjme_class_poolEntryUtf;

/**
 * Represents a single constant pool entry.
 *
 * @since 2024/01/04
 */
typedef struct sjme_class_poolEntry
{
	/** The type of entry that this is. */
	sjme_class_poolType type;

	/** The data for the entry. */
	union
	{
		/** Class. */
		sjme_class_poolEntryClass classRef;

		/** Double. */
		sjme_class_poolEntryDouble constDouble;

		/** Float. */
		sjme_class_poolEntryFloat constFloat;

		/** Integer. */
		sjme_class_poolEntryInteger constInteger;

		/** Long. */
		sjme_class_poolEntryLong constLong;

		/** A class member. */
		sjme_class_poolEntryMember member;

		/** Name and type. */
		sjme_class_poolEntryNameAndType nameAndType;

		/** UTF pool entry. */
		sjme_class_poolEntryUtf utf;
	} data;
} sjme_class_poolEntry;

/** A list of constant pool entries. */
SJME_LIST_DECLARE(sjme_class_poolEntry, 0);

struct sjme_class_infoCore
{
	/** The constant pool of this class. */
	const sjme_list_sjme_class_poolEntry* pool;

	/** The name of this class. */
	sjme_lpcstr name;

	/** The superclass of this class. */
	sjme_lpcstr superName;

	/** The interfaces this class implements. */
	const sjme_list_sjme_lpcstr* interfaceNames;

	/** Fields within the method. */
	const sjme_list_sjme_class_fieldInfo* fields;

	/** Methods within the class. */
	const sjme_list_sjme_class_methodInfo* methods;
};

/**
 * Represents a field's constant value.
 *
 * @since 2024/01/08
 */
typedef struct sjme_class_fieldConstVal
{
	/** The type of value. */
	sjme_javaTypeId type;

	/** The value of the field. */
	union
	{
		/** The Java value. */
		sjme_jvalue java;

		/** Constant string. */
		sjme_lpcstr string;
	} value;
} sjme_class_fieldConstVal;

struct sjme_class_fieldInfoCore
{
	/** The class which contains this field. */
	sjme_class_info inClass;

	/** Field flags. */
	sjme_class_fieldFlags flags;

	/** The constant value, if any. */
	sjme_class_fieldConstVal constVal;
};

struct sjme_class_methodInfoCore
{
	/** The class which contains this method. */
	sjme_class_info inClass;

	/** Method flags. */
	sjme_class_methodFlags flags;

	/** The method code, if it is not native. */
	sjme_class_codeInfo code;
};

struct sjme_class_codeInfoCore
{
	/** The method which contains this code. */
	sjme_class_methodInfo inMethod;

	/** Maximum number of locals. */
	sjme_jint maxLocals;

	/** Maximum number of stack entries. */
	sjme_jint maxStack;

	/** Exception table. */
	const sjme_list_sjme_class_exceptionHandler* exceptions;

	/** Method byte code. */
	sjme_list_sjme_jubyte code;
};

/**
 * Stack map table representation.
 *
 * @since 2024/01/09
 */
typedef struct sjme_class_stackMap
{
	/** Todo. */
	int todo;
} sjme_class_stackMap;

/**
 * Parses a single class and loads its class information.
 *
 * @param inPool The pool to allocate within.
 * @param inStream The stream to parse from when reading the class.
 * @param outClass The resultant class information
 * @return Any resultant error code, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_info* outClass);

sjme_errorCode sjme_class_parseAttributeCode(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_methodInfo inMethod,
	sjme_attrOutNotNull sjme_class_codeInfo* outCode);

sjme_errorCode sjme_class_parseAttributeConstVal(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_fieldInfo inField,
	sjme_attrOutNotNull sjme_class_fieldConstVal* outConstVal);

sjme_errorCode sjme_class_parseAttributeStackMapOld(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_codeInfo inCode,
	sjme_attrOutNotNull sjme_class_stackMap* outStackMap);

sjme_errorCode sjme_class_parseAttributeStackMapNew(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_codeInfo inCode,
	sjme_attrOutNotNull sjme_class_stackMap* outStackMap);

sjme_errorCode sjme_class_parseConstantPool(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_info inClass,
	sjme_attrOutNotNull sjme_list_sjme_class_poolEntry* outPool);

sjme_errorCode sjme_class_parseField(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_fieldInfo* outField);

sjme_errorCode sjme_class_parseFields(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_info inClass,
	sjme_attrOutNotNull sjme_list_sjme_class_fieldInfo* outFields);

sjme_errorCode sjme_class_parseMethod(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInOutNotNull sjme_class_methodInfo* outMethod);

sjme_errorCode sjme_class_parseMethods(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_info inClass,
	sjme_attrOutNotNull sjme_list_sjme_class_methodInfo* outMethods);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSY_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSY_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSY_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSY_H */
