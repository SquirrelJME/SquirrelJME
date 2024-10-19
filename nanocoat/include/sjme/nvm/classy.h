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
#include "sjme/nvm/stringPool.h"
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
typedef enum sjme_nvm_class_version
{
	/** CLDC 1.1 (JSR 30). */
	SJME_NVM_CLASS_CLDC_1_0 = 2949123,
	
	/** CLDC 1.1 (JSR 139). */
	SJME_NVM_CLASS_CLDC_1_1 = 3080192,
	
	/** CLDC 8. */
	SJME_NVM_CLASS_CLDC_1_8 = 3342336,
} sjme_nvm_class_version;

/**
 * Core class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_nvm_class_infoCore sjme_nvm_class_infoCore;

/**
 * Opaque class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_nvm_class_infoCore* sjme_nvm_class_info;

/**
 * List of class information.
 * 
 * @since 2024/10/19
 */
SJME_LIST_DECLARE(sjme_nvm_class_info, 0);

/**
 * Opaque constant pool information.
 * 
 * @since 2024/09/13
 */
typedef struct sjme_nvm_class_poolInfoCore sjme_nvm_class_poolInfoCore;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_CLASS which represents a class or interface.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryClass sjme_nvm_class_poolEntryClass;

/**
 * Opaque constant pool information.
 * 
 * @since 2024/09/13
 */
typedef sjme_nvm_class_poolInfoCore* sjme_nvm_class_poolInfo;

/**
 * Core method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_methodInfoCore sjme_nvm_class_methodInfoCore;

/**
 * Opaque method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_methodInfoCore* sjme_nvm_class_methodInfo;

/**
 * Method list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_nvm_class_methodInfo, 0);

/** The basic type of @c sjme_nvm_class_methodInfo . */
#define SJME_TYPEOF_BASIC_sjme_nvm_class_methodInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Core field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_fieldInfoCore sjme_nvm_class_fieldInfoCore;

/**
 * Opaque field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_fieldInfoCore* sjme_nvm_class_fieldInfo;

/**
 * Field list.
 *
 * @since 2024/01/03
 */
SJME_LIST_DECLARE(sjme_nvm_class_fieldInfo, 0);

/** The basic type of @c sjme_nvm_class_fieldInfo . */
#define SJME_TYPEOF_BASIC_sjme_nvm_class_fieldInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Exception handling information.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_exceptionHandler
{
	/** The range of the exception where it applies. */
	sjme_rangeShort range;

	/** The handler PC address. */
	sjme_jshort handlerPc;

	/** The type that this catches. */
	sjme_nvm_class_poolEntryClass* handles;
} sjme_nvm_class_exceptionHandler;

/** A list of exceptions. */
SJME_LIST_DECLARE(sjme_nvm_class_exceptionHandler, 0);

/** The basic type of @c sjme_nvm_class_exceptionHandler . */
#define SJME_TYPEOF_BASIC_sjme_nvm_class_exceptionHandler \
	SJME_BASIC_TYPE_ID_OBJECT

/**
 * Method code information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_codeInfoCore sjme_nvm_class_codeInfoCore;

/**
 * Opaque method code structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_codeInfoCore* sjme_nvm_class_codeInfo;

/**
 * Access flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_accessFlags
{
	/** Is this public? */
	sjme_jboolean public : 1;

	/** Is this protected? */
	sjme_jboolean protected : 1;
	
	/** Is this private? */
	sjme_jboolean private : 1;
} sjme_nvm_class_accessFlags;

/**
 * Class flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_classFlags
{
	/** Access flags. */
	sjme_nvm_class_accessFlags access;

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
} sjme_nvm_class_classFlags;

/**
 * Common member flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_memberFlags
{
	/** Access flags. */
	sjme_nvm_class_accessFlags access;

	/** Static member? */
	sjme_jboolean isStatic : 1;

	/** Final member? */
	sjme_jboolean final : 1;

	/** Synthetic member? */
	sjme_jboolean synthetic : 1;
} sjme_nvm_class_memberFlags;

/**
 * Field flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_fieldFlags
{
	/** Common member flags. */
	sjme_nvm_class_memberFlags member;

	/** Is this volatile? */
	sjme_jboolean isVolatile : 1;

	/** Is this transient? */
	sjme_jboolean transient : 1;

	/** Is this an enumeration. */
	sjme_jboolean enumeration : 1;
} sjme_nvm_class_fieldFlags;

/**
 * Method flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_methodFlags
{
	/** Common member flags. */
	sjme_nvm_class_memberFlags member;

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
} sjme_nvm_class_methodFlags;

/**
 * The type that a constant pool entry may be.
 *
 * @since 2024/01/04
 */
typedef enum sjme_nvm_class_poolType
{
	/** Null entry. */
	SJME_NVM_CLASS_POOL_TYPE_NULL = 0,

	/** Modified UTF. */
	SJME_NVM_CLASS_POOL_TYPE_UTF = 1,

	/** Unused 2. */
	SJME_NVM_CLASS_POOL_TYPE_UNUSED_2 = 2,

	/** Integer constant. */
	SJME_NVM_CLASS_POOL_TYPE_INTEGER = 3,

	/** Float constant. */
	SJME_NVM_CLASS_POOL_TYPE_FLOAT = 4,

	/** Long constant. */
	SJME_NVM_CLASS_POOL_TYPE_LONG = 5,

	/** Double constant. */
	SJME_NVM_CLASS_POOL_TYPE_DOUBLE = 6,

	/** Class constant. */
	SJME_NVM_CLASS_POOL_TYPE_CLASS = 7,

	/** String constant. */
	SJME_NVM_CLASS_POOL_TYPE_STRING = 8,

	/** Field reference. */
	SJME_NVM_CLASS_POOL_TYPE_FIELD = 9,

	/** Method reference. */
	SJME_NVM_CLASS_POOL_TYPE_METHOD = 10,

	/** Interface method reference. */
	SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD = 11,

	/** Name and type. */
	SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE = 12,

	/** Unused 13. */
	SJME_NVM_CLASS_POOL_TYPE_UNUSED_13 = 13,

	/** Unused 14. */
	SJME_NVM_CLASS_POOL_TYPE_UNUSED_14 = 14,

	/** Method handle. */
	SJME_NVM_CLASS_POOL_TYPE_METHOD_HANDLE = 15,

	/** Method type. */
	SJME_NVM_CLASS_POOL_TYPE_METHOD_TYPE = 16,

	/** Unused 17. */
	SJME_NVM_CLASS_POOL_TYPE_UNUSED_17 = 17,

	/** Invoke dynamic. */
	SJME_NVM_CLASS_POOL_TYPE_INVOKE_DYNAMIC = 18,

	/** The number of pool types. */
	SJME_NUM_CLASS_POOL_TYPE
} sjme_nvm_class_poolType;

struct sjme_nvm_class_poolEntryClass
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The index where the descriptor is located. */
	sjme_jshort descriptorIndex;
	
	/** The descriptor this represents. */
	sjme_nvm_stringPool_string descriptor;
};

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_DOUBLE which represents a double constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryDouble
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The constant value. */
	sjme_jdouble value;
} sjme_nvm_class_poolEntryDouble;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE which represents a name and type
 * of a member without the class.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryNameAndType
	sjme_nvm_class_poolEntryNameAndType;

/**
 * Either @c SJME_NVM_CLASS_POOL_TYPE_FIELD ,
 * @c SJME_NVM_CLASS_POOL_TYPE_METHOD ,
 * or @c SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD which represents a reference
 * to a class member.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryMember
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The index where the class is located. */
	sjme_jshort inClassIndex;

	/** The class this refers to. */
	const sjme_nvm_class_poolEntryClass* inClass;
	
	/** The index where the name and type is located. */
	sjme_jshort nameAndTypeIndex;

	/** The name and type used. */
	const sjme_nvm_class_poolEntryNameAndType* nameAndType;
} sjme_nvm_class_poolEntryMember;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_FLOAT which represents a float constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryFloat
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The constant value. */
	sjme_jfloat value;
} sjme_nvm_class_poolEntryFloat;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_INTEGER which represents an integer constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryInteger
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The constant value. */
	sjme_jint value;
} sjme_nvm_class_poolEntryInteger;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_LONG which represents a long constant.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryLong
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The constant value. */
	sjme_jlong value;
} sjme_nvm_class_poolEntryLong;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_STRING which represents a string constant.
 * 
 * @since 2024/09/20
 */
typedef struct sjme_nvm_class_poolEntryString
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The index where the value is located. */
	sjme_jshort valueIndex;
	
	/** The type. */
	sjme_nvm_stringPool_string value;
} sjme_nvm_class_poolEntryString;

struct sjme_nvm_class_poolEntryNameAndType
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The index where the name is located. */
	sjme_jshort nameIndex;
	
	/** The name. */
	sjme_nvm_stringPool_string name;
	
	/** The index where the descriptor is located. */
	sjme_jshort descriptorIndex;

	/** The type. */
	sjme_nvm_stringPool_string descriptor;
};

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_UTF which is a modified UTF-8 entry.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryUtf
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** The UTF data for this entry. */
	sjme_nvm_stringPool_string utf;
} sjme_nvm_class_poolEntryUtf;

/**
 * Represents a single constant pool entry.
 *
 * @since 2024/01/04
 */
typedef union sjme_nvm_class_poolEntry
{
	/** The type of entry that this is. */
	sjme_nvm_class_poolType type;
	
	/** Class. */
	sjme_nvm_class_poolEntryClass classRef;

	/** Double. */
	sjme_nvm_class_poolEntryDouble constDouble;

	/** Float. */
	sjme_nvm_class_poolEntryFloat constFloat;

	/** Integer. */
	sjme_nvm_class_poolEntryInteger constInteger;

	/** Long. */
	sjme_nvm_class_poolEntryLong constLong;
	
	/** String constant. */
	sjme_nvm_class_poolEntryString constString;

	/** A class member. */
	sjme_nvm_class_poolEntryMember member;

	/** Name and type. */
	sjme_nvm_class_poolEntryNameAndType nameAndType;

	/** UTF pool entry. */
	sjme_nvm_class_poolEntryUtf utf;
} sjme_nvm_class_poolEntry;

/** A list of constant pool entries. */
SJME_LIST_DECLARE(sjme_nvm_class_poolEntry, 0);

struct sjme_nvm_class_poolInfoCore
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;
	
	/** Constant pool entries. */
	sjme_list_sjme_nvm_class_poolEntry* pool;
};

struct sjme_nvm_class_infoCore
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;
	
	/** The constant pool of this class. */
	sjme_nvm_class_poolInfo pool;
	
	/** The class version. */
	sjme_nvm_class_version version;
	
	/** Class flags. */
	sjme_nvm_class_classFlags flags;

	/** The name of this class. */
	sjme_nvm_stringPool_string name;

	/** The superclass of this class. */
	sjme_nvm_stringPool_string superName;

	/** The interfaces this class implements. */
	sjme_list_sjme_nvm_stringPool_string* interfaceNames;

	/** Fields within the method. */
	sjme_list_sjme_nvm_class_fieldInfo* fields;

	/** Methods within the class. */
	sjme_list_sjme_nvm_class_methodInfo* methods;
};

/**
 * Represents a field's constant value.
 *
 * @since 2024/01/08
 */
typedef struct sjme_nvm_class_fieldConstVal
{
	/** The type of value. */
	sjme_javaTypeId type;

	/** The value of the field. */
	union
	{
		/** The Java value. */
		sjme_jvalue java;

		/** Constant string. */
		sjme_nvm_stringPool_string string;
	} value;
} sjme_nvm_class_fieldConstVal;

struct sjme_nvm_class_fieldInfoCore
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;

	/** Field flags. */
	sjme_nvm_class_fieldFlags flags;
	
	/** The name of this field. */
	sjme_nvm_stringPool_string name;
	
	/** The type of this field. */
	sjme_nvm_stringPool_string type;

	/** The constant value, if any. */
	sjme_nvm_class_fieldConstVal constVal;
};

struct sjme_nvm_class_methodInfoCore
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;

	/** Method flags. */
	sjme_nvm_class_methodFlags flags;
	
	/** The name of this method. */
	sjme_nvm_stringPool_string name;
	
	/** The type of this method. */
	sjme_nvm_stringPool_string type;

	/** The method code, if it is not native. */
	sjme_nvm_class_codeInfo code;
};

struct sjme_nvm_class_codeInfoCore
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;
	
	/** The method which contains this code. */
	sjme_nvm_class_methodInfo inMethod;

	/** Maximum number of locals. */
	sjme_jint maxLocals;

	/** Maximum number of stack entries. */
	sjme_jint maxStack;

	/** Exception table. */
	sjme_list_sjme_nvm_class_exceptionHandler* exceptions;

	/** Method byte code. */
	sjme_list_sjme_jubyte code;
};

/**
 * Stack map table representation.
 *
 * @since 2024/01/09
 */
typedef struct sjme_nvm_class_stackMap
{
	/** Todo. */
	int todo;
} sjme_nvm_class_stackMap;

/**
 * Handler function for attribute parsing.
 * 
 * @param inPool The allocation pool.
 * @param inConstPool The constant pool.
 * @param inStringPool The string pool.
 * @param context The passed context.
 * @param attrName The name of the attribute.
 * @param attrStream The stream over the attribute data.
 * @param attrData The attribute data.
 * @param attrLen The data length.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
typedef sjme_errorCode (*sjme_nvm_class_parseAttributeFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen);

/**
 * Structure for attribute handlers according to their name and handler.
 * 
 * @since 2024/09/25
 */
typedef struct sjme_nvm_class_parseAttributeHandler
{
	/** The name to handle. */
	sjme_lpcstr name;
	
	/** The handler for the attribute. */
	sjme_nvm_class_parseAttributeFunc handler;
} sjme_nvm_class_parseAttributeHandler;

/**
 * Parses a single class and loads its class information.
 *
 * @param inPool The pool to allocate within.
 * @param inStream The stream to parse from when reading the class.
 * @param inStringPool The pool for strings existing in memory already.
 * @param outClass The resultant class information
 * @return Any resultant error code, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_nvm_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_info* outClass);

/**
 * Parses attributes.
 * 
 * @param inPool The allocation pool.
 * @param inStream The stream to read from.
 * @param inConstPool The constant pool.
 * @param inStringPool The string pool.
 * @param handlers The handler used for attribute parsing.
 * @param context The context to pass to the handler.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
sjme_errorCode sjme_nvm_class_parseAttributes(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context);

/**
 * Parses the constant pool of an input class.
 * 
 * @param inPool The input pool. 
 * @param inStream The stream to read from.
 * @param inStringPool The string pool for reused strings.
 * @param outPool The resultant read constant pool.
 * @return Any resultant error, if any.
 * @since 2024/09/13
 */
sjme_errorCode sjme_nvm_class_parseConstantPool(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_poolInfo* outPool);

/**
 * Parses a single field.
 * 
 * @param inPool The allocation pool to use.
 * @param inStream The stream to read from.
 * @param inConstPool The class constant pool.
 * @param inStringPool The string pool used.
 * @param outField The resultant field.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
sjme_errorCode sjme_nvm_class_parseField(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outField);

/**
 * Parses a single method.
 * 
 * @param inPool The allocation pool to use.
 * @param inStream The stream to read from.
 * @param inConstPool The class constant pool.
 * @param inStringPool The string pool used.
 * @param outMethod The resultant method.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
sjme_errorCode sjme_nvm_class_parseMethod(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInOutNotNull sjme_nvm_class_methodInfo* outMethod);

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
