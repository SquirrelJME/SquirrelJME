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

#ifndef SJME_C_CLASSY_H
#define SJME_C_CLASSY_H

#include "sjme/nvm/nvm.h"
#include "sjme/nvm/stringPool.h"
#include "sjme/list.h"
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
 * The number of member index sets possible.
 * 
 * @since 2024/10/28
 */
typedef enum sjme_nvm_class_instanceType
{
	/** Static members. */
	SJME_NVM_CLASS_MEMBER_STATIC = 0,
	
	/** Instance members. */
	SJME_NVM_CLASS_MEMBER_INSTANCE = 1,
	
	/** The number of member indexes. */
	SJME_NVM_CLASS_NUM_INSTANCE_TYPE = 2,
} sjme_nvm_class_instanceType;

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
typedef struct sjme_nvm_class_infoBase* sjme_nvm_class_info;

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
typedef struct sjme_nvm_class_poolInfoBase sjme_nvm_class_poolInfoBase;

/**
 * Opaque constant pool information.
 * 
 * @since 2024/09/13
 */
typedef sjme_nvm_class_poolInfoBase* sjme_nvm_class_poolInfo;

/**
 * Core method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_methodInfoBase sjme_nvm_class_methodInfoBase;

/**
 * Opaque method information structure.
 *
 * @since 2024/01/03
 */
typedef sjme_nvm_class_methodInfoBase* sjme_nvm_class_methodInfo;

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
 * Base field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_fieldInfoBase sjme_nvm_class_fieldInfoBase;

/**
 * Opaque field information structure.
 *
 * @since 2024/01/03
 */
typedef sjme_nvm_class_fieldInfoBase* sjme_nvm_class_fieldInfo;

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
typedef struct sjme_nvm_class_codeInfoBase sjme_nvm_class_codeInfoBase;

/**
 * Opaque method code structure.
 *
 * @since 2024/01/03
 */
typedef sjme_nvm_class_codeInfoBase* sjme_nvm_class_codeInfo;

/**
 * Access flags.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_accessFlags
{
	/** Is this public? */
	sjme_jboolean public;

	/** Is this protected? */
	sjme_jboolean protected;
	
	/** Is this private? */
	sjme_jboolean private;
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
	sjme_jboolean final;

	/** Is the class super? */
	sjme_jboolean super;

	/** Is the class an interface? */
	sjme_jboolean interface;

	/** Is the class abstract? */
	sjme_jboolean abstract;

	/** Is the class synthetic? */
	sjme_jboolean synthetic;

	/** Is the class an annotation? */
	sjme_jboolean annotation;

	/** Is the class an enum? */
	sjme_jboolean enumeration;
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
	sjme_jboolean isStatic;

	/** Final member? */
	sjme_jboolean final;

	/** Synthetic member? */
	sjme_jboolean synthetic;
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
	sjme_jboolean isVolatile;

	/** Is this transient? */
	sjme_jboolean transient;

	/** Is this an enumeration. */
	sjme_jboolean enumeration;
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
	sjme_jboolean synchronized;

	/** Bridge method, generated by the compiler? */
	sjme_jboolean bridge;

	/** Variadic arguments? */
	sjme_jboolean varargs;

	/** Is this a native method? */
	sjme_jboolean native;

	/** Abstract? */
	sjme_jboolean abstract;

	/** Strict floating point? */
	sjme_jboolean strictfp;
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

	/** The hash of the descriptor. */
	sjme_jint descriptorHash;
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
	sjme_align64 sjme_jdouble value;
} sjme_nvm_class_poolEntryDouble;

/**
 * A @c SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE which represents a name and type
 * of member without the class.
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

	/** The number of static arguments slots. */
	sjme_jint staticArgSlots;

	/** The return value slots. */
	sjme_jint rvSlots;
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
	sjme_align64 sjme_jlong value;
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
	
	/** The identifier hash. */
	sjme_jint idHash;
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

struct sjme_nvm_class_poolInfoBase
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;
	
	/** Constant pool entries. */
	sjme_list_sjme_nvm_class_poolEntry* pool;
};

struct sjme_nvm_class_infoBase
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;
	
	/** The file name of this class. */
	sjme_lpcstr fileName;
	
	/** The hash of the file name. */
	sjme_jint fileNameHash;
	
	/** The constant pool of this class. */
	sjme_nvm_class_poolInfo pool;
	
	/** The class version. */
	sjme_nvm_class_version version;
	
	/** Class flags. */
	sjme_nvm_class_classFlags flags;
	
	/** The package this class is in. */
	sjme_nvm_stringPool_string inPackage;

	/** The name of this class. */
	sjme_nvm_stringPool_string name;

	/** The superclass of this class. */
	sjme_nvm_stringPool_string superName;

	/** The runtime name as returned by @c Class.getName() . */
	sjme_nvm_stringPool_string runtimeName;

	/** The interfaces this class implements. */
	sjme_list_sjme_nvm_stringPool_string* interfaceNames;

	/** Fields within the method. */
	sjme_list_sjme_nvm_class_fieldInfo* fields;
	
	/** The field count per type. */
	sjme_jshort fieldCount[SJME_NVM_CLASS_NUM_INSTANCE_TYPE]
		[SJME_NUM_EXTENDED_JAVA_TYPE_IDS];
	
	/** The method count per type. */
	sjme_jshort methodCount[SJME_NVM_CLASS_NUM_INSTANCE_TYPE];

	/** Methods within the class. */
	sjme_list_sjme_nvm_class_methodInfo* methods;

	/** Is this an array? */
	sjme_jboolean isArray;

	/** The library this came from. */
	sjme_nvm_rom_library library;
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

struct sjme_nvm_class_fieldInfoBase
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;

	/** Field flags. */
	sjme_nvm_class_fieldFlags flags;
	
	/** The name of this field. */
	sjme_nvm_stringPool_string name;
	
	/** The type of this field. */
	sjme_nvm_stringPool_string type;

	/** The identifier hash. */
	sjme_jint idHash;

	/** The constant value, if any. */
	sjme_nvm_class_fieldConstVal constVal;
	
	/** The Java type of this field. */
	sjme_javaTypeId javaType;
	
	/** The basic type of this field. */
	sjme_basicTypeId basicType;
	
	/** The extended type of this field. */
	sjme_extendedTypeId extendedType;
	
	/** The index of this field within its basic type. */
	sjme_jint typedIndex;
};
	
/** Bits to assist in quicker method determinations. */
typedef struct sjme_nvm_class_methodInfoBits
{
	/** Is this a static initializer? */
	sjme_jboolean isStaticInit;

	/** Is this an instance initializer? */
	sjme_jboolean isInstanceInit;
} sjme_nvm_class_methodInfoBits;

struct sjme_nvm_class_methodInfoBase
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;

	/** Method flags. */
	sjme_nvm_class_methodFlags flags;

	/** The identifier hash. */
	sjme_jint idHash;
	
	/** The name of this method. */
	sjme_nvm_stringPool_string name;
	
	/** The type of this method. */
	sjme_nvm_stringPool_string type;

	/** The argument count for the type. */
	sjme_jint argC;

	/** Argument types. */
	sjme_javaTypeId* argT;

	/** Return type. */
	sjme_javaTypeId argR;

	/** The method code, if it is not native. */
	sjme_nvm_class_codeInfo code;
	
	/** The index of this method in the class. */
	sjme_jshort typedIndex;
	
	/** The class this is in. */
	sjme_nvm_class_info inClass;

	/** Bits to assist in quicker method determinations. */
	sjme_nvm_class_methodInfoBits bits;
};

/**
 * Stores the maximum count for a variable of a given type.
 *
 * @since 2025/01/30
 */
typedef struct sjme_nvm_class_codePerType
{
	/** The maximum number of local variables. */
	sjme_jshort locals;

	/** The maximum number of stack variables. */
	sjme_jshort stack;

	/** The map of local variables to actual local slots. */
	sjme_jshort* localMap;
} sjme_nvm_class_codePerType;

/** The ID for all types. */
#define SJME_NVM_CODE_INFO_ALL_TYPES SJME_NUM_JAVA_TYPE_IDS

/** The number of per types. */
#define SJME_NVM_CODE_INFO_NUM_TYPE_IDS \
	(SJME_NVM_CODE_INFO_ALL_TYPES + 1)

struct sjme_nvm_class_codeInfoBase
{
	/** The common NanoCoat base. */
	sjme_nvm_commonBase common;
	
	/** The method which contains this code. */
	sjme_nvm_class_methodInfo inMethod;
	
	/** Maximum per specific type. */
	sjme_nvm_class_codePerType perType[SJME_NVM_CODE_INFO_NUM_TYPE_IDS];

	/** Exception table. */
	sjme_list_sjme_nvm_class_exceptionHandler* exceptions;

	/** The raw code length. */
	sjme_jint rawCodeLen;

	/** Method byte code. */
	sjme_byteCode* rawCode;
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
 * @param allocPool The allocation pool.
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
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
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
 * Calculates the @c sjme_javaTypeId for the given method descriptor.
 * 
 * @param allocPool The allocation pool to use for @c outArgT .
 * @param typeDesc The input method descriptor.
 * @param outArgC Output argument count.
 * @param outArgT Output argument types.
 * @param outArgR Output return type of method.
 * @return Any resultant error, if any.
 * @since 2025/02/16
 */
sjme_errorCode sjme_nvm_class_calcMethodArgs(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_charSeq typeDesc,
	sjme_attrInNotNull sjme_jint* outArgC,
	sjme_attrInNotNull sjme_javaTypeId** outArgT,
	sjme_attrInNotNull sjme_javaTypeId* outArgR);

/**
 * Counts the number of slots a field descriptor takes up.
 * 
 * @param inDesc The descriptor to count.
 * @param outSlots The number of used slots.
 * @param atP The pointer to the current character index, will be incremented
 * after a read occurs.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_class_descriptorFieldSlots(
	sjme_attrInNotNull sjme_charSeq inDesc,
	sjme_attrOutNotNull sjme_jint* outSlots,
	sjme_attrInOutNullable sjme_jint* atP);

/**
 * Counts the number of slots a method descriptor takes up.
 * 
 * @param inDesc The descriptor to count.
 * @param outArgSlots The number of argument slots.
 * @param outRvSlots The number of return value slots.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_class_descriptorMethodSlots(
	sjme_attrInNotNull sjme_charSeq inDesc,
	sjme_attrOutNotNull sjme_jint* outArgSlots,
	sjme_attrOutNotNull sjme_jint* outRvSlots);
	
/**
 * Determines the @c sjme_javaTypeId or @c sjme_basicTypeId type for the
 * given descriptor.
 * 
 * @param desc The input descriptor. 
 * @param outJavaType The resultant Java type.
 * @param outBasicType The resultant basic type.
 * @param outExtendedType The resultant extended type.
 * @return Any resultant error, if any.
 * @since 2024/10/28
 */
sjme_errorCode sjme_nvm_class_descriptorToType(
	sjme_attrInNotNull sjme_charSeq desc,
	sjme_attrOutNotNull sjme_javaTypeId* outJavaType,
	sjme_attrOutNullable sjme_basicTypeId* outBasicType,
	sjme_attrOutNullable sjme_extendedTypeId* outExtendedType);

/**
 * Parses a single class and loads its class information.
 *
 * @param allocPool The pool to allocate within.
 * @param inStream The stream to parse from when reading the class.
 * @param inStringPool The pool for strings existing in memory already.
 * @param outClass The resultant class information
 * @return Any resultant error code, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_nvm_class_parse(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_info* outClass);

/**
 * Parses attributes.
 * 
 * @param allocPool The allocation pool.
 * @param inStream The stream to read from.
 * @param inConstPool The constant pool.
 * @param inStringPool The string pool.
 * @param handlers The handler used for attribute parsing.
 * @param context The context to pass to the handler.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
sjme_errorCode sjme_nvm_class_parseAttributes(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context);

/**
 * Parses the constant pool of an input class.
 * 
 * @param allocPool The input pool. 
 * @param inStream The stream to read from.
 * @param inStringPool The string pool for reused strings.
 * @param outPool The resultant read constant pool.
 * @return Any resultant error, if any.
 * @since 2024/09/13
 */
sjme_errorCode sjme_nvm_class_parseConstantPool(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_poolInfo* outPool);

/**
 * Parses a single field.
 * 
 * @param allocPool The allocation pool to use.
 * @param inStream The stream to read from.
 * @param inConstPool The class constant pool.
 * @param inStringPool The string pool used.
 * @param outField The resultant field.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
sjme_errorCode sjme_nvm_class_parseField(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outField);

/**
 * Parses a single method.
 * 
 * @param allocPool The allocation pool to use.
 * @param inStream The stream to read from.
 * @param inConstPool The class constant pool.
 * @param inStringPool The string pool used.
 * @param outMethod The resultant method.
 * @return Any resultant error, if any.
 * @since 2024/09/21
 */
sjme_errorCode sjme_nvm_class_parseMethod(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInOutNotNull sjme_nvm_class_methodInfo* outMethod);

/**
 * Checks if the binary name is valid.
 * 
 * @param binaryName The binary name to check.
 * @return Any resultant error, if any.
 * @since 2025/03/16
 */
sjme_errorCode sjme_nvm_class_validBinaryName(
	sjme_attrInNotNull sjme_charSeq binaryName);

/** Pool class reference class. */
#define SJME_P_C_N(entry) \
	((entry)->classRef.descriptor)

/** Pool member entry. */
#define SJME_P_M(entry) \
	((entry)->member)

/** Pool member entry class. */
#define SJME_P_M_C(entry) \
	(SJME_P_M(entry).inClass->descriptor)

/** Pool member entry name. */
#define SJME_P_M_N(entry) \
	(SJME_P_M(entry).nameAndType->name)

/** Pool member entry type. */
#define SJME_P_M_T(entry) \
	(SJME_P_M(entry).nameAndType->descriptor)

/** Calculates the identifier hash for a member. */
#define sjme_nvm_class_idHashMember(name, type) \
	(sjme_charSeq_hashR((name)) ^ \
		sjme_util_intReverse(sjme_charSeq_hashR((type))))

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
