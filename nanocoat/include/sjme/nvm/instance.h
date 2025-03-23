/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Instances of objects.
 * 
 * @since 2024/09/08
 */

#ifndef SQUIRRELJME_INSTANCE_H
#define SQUIRRELJME_INSTANCE_H

#include "sjme/nvm/nvm.h"
#include "sjme/nvm/classyVm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_INSTANCE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

struct sjme_jobjectBase
{
	/** Common base for all objects. */
	sjme_nvm_commonBase common;
	
	/** The identity hashcode. */
	sjme_jint identityHash;
	
	/** The current class that this is. */
	sjme_jclass isClass;
};

/**
 * Raw array values.
 *
 * @since 2025/03/23
 */
typedef union sjme_nvm_rawArrayValues
{
	/** Byte/boolean values. */
	sjme_jbyte b[sjme_flexibleArrayCountUnion];
	
	/** Short values. */
	sjme_jshort s[sjme_flexibleArrayCountUnion];
	
	/** Char values. */
	sjme_jshort c[sjme_flexibleArrayCountUnion];
	
	/** Integer values. */
	sjme_jint i[sjme_flexibleArrayCountUnion];
		
	/** Long values. */
	sjme_jlong j[sjme_flexibleArrayCountUnion];
		
	/** Float values. */
	sjme_jfloat f[sjme_flexibleArrayCountUnion];
		
	/** Double values. */
	sjme_jdouble d[sjme_flexibleArrayCountUnion];
		
	/** Object reference values. */
	sjme_jobject l[sjme_flexibleArrayCountUnion];
} sjme_nvm_rawArrayValues;

/**
 * Raw field values.
 *
 * @since 2024/11/27
 */
typedef union sjme_nvm_rawFieldValues
{
	/** Integer values. */
	sjme_jint i[sjme_flexibleArrayCountUnion];
		
	/** Long values. */
	sjme_jlong j[sjme_flexibleArrayCountUnion];
		
	/** Float values. */
	sjme_jfloat f[sjme_flexibleArrayCountUnion];
		
	/** Double values. */
	sjme_jdouble d[sjme_flexibleArrayCountUnion];
		
	/** Object reference values. */
	sjme_jobject l[sjme_flexibleArrayCountUnion];
} sjme_nvm_rawFieldValues;

/**
 * Stores multiple field values for a given type.
 * 
 * @since 2024/10/27
 */
typedef struct sjme_nvm_fieldValues
{
	/** The type of value this stores. */
	sjme_javaTypeId type;
	
	/** The number of items in this tread. */
	sjme_jint count;
	
	/** Values within the tread. */
	sjme_nvm_rawFieldValues values;
} sjme_nvm_fieldValues;

/**
 * Stores the classes that this class @c implements or @c extends .
 * 
 * @since 2024/11/09
 */
typedef struct sjme_nvm_isClassesBase sjme_nvm_isClassesBase;

/**
 * Stores the classes that this class @c implements or @c extends .
 * 
 * @since 2024/11/09
 */
typedef sjme_nvm_isClassesBase* sjme_nvm_isClasses;

struct sjme_nvm_isClassesBase
{
	/** Common base for all objects. */
	sjme_nvm_commonBase common;
	
	/** Read/write lock for this. */
	sjme_thread_rwLock rwLock;
	
	/** The classes that this class @c implements / @c extends . */
	sjme_list_sjme_jclass* classes;
};

/**
 * The class initialization stage.
 *
 * @since 2025/03/22
 */
typedef enum sjme_nvm_instance_classInit
{
	/** Initialize/load not happening. */
	SJME_VM_CLASS_INIT_LOAD_NEVER = 0,

	/** Initialize/load is currently happening. */
	SJME_VM_CLASS_INIT_LOAD_CURRENT = 1,

	/** Initialize/load is now done. */
	SJME_VM_CLASS_INIT_LOAD_DONE = 2,
} sjme_nvm_instance_classInit;

struct sjme_jclassBase
{
	/** All classes are objects. */
	sjme_jobjectBase object;
	
	/** The binary name of this class. */
	sjme_charSeq binaryName;
	
	/** The has of the binary name. */
	sjme_jint binaryHash;
	
	/** Error emitted when loading/initializing. */
	sjme_atomic_sjme_jint error; 
	
	/** Has the backing class data been loaded? */
	sjme_atomic_sjme_jint isLoaded;
	
	/** Is this class initialized? */
	sjme_atomic_sjme_jint isInitialized;
	
	/** The parsed class file information. */
	sjme_nvm_class_info info;
	
	/** The super class of this class. */
	sjme_atomic_sjme_jclass superClass;
	
	/** Interface classes for this class. */
	sjme_list_sjme_jclass* interfaceClasses;
	
	/** Field value storage. */
	sjme_nvm_fieldValues* staticFields[SJME_NUM_JAVA_TYPE_IDS];
	
	/** Base index for fields. */
	sjme_jshort fieldBase[SJME_NVM_CLASS_NUM_INSTANCE_TYPE]
		[SJME_NUM_JAVA_TYPE_IDS];
	
	/** Count for a given field. */
	sjme_jshort fieldCount[SJME_NVM_CLASS_NUM_INSTANCE_TYPE]
		[SJME_NUM_JAVA_TYPE_IDS];
	
	/** Base index for methods. */
	sjme_jshort methodBase[SJME_NVM_CLASS_NUM_INSTANCE_TYPE];
	
	/** The number of methods. */
	sjme_jshort methodCount[SJME_NVM_CLASS_NUM_INSTANCE_TYPE];
	
	/** Method bindings for this class. */
	sjme_list_sjme_jmethodID* methodBinds[
		SJME_NVM_CLASS_NUM_INSTANCE_TYPE];
	
	/** The classes this implements or extends. */
	sjme_nvm_isClasses* isClasses;

	/** The Java type ID of this class. */
	sjme_javaTypeId typeId;

	/** The array type ID of this class. */
	sjme_basicTypeId arrayTypeId;

	/** The component type of this class, if it is an array. */
	sjme_atomic_sjme_jclass componentType;

	/** The phantom array type of this class. */
	sjme_atomic_sjme_jclass phantomArrayType;
};

struct sjme_jstringBase
{
	/** All strings are objects. */
	sjme_jobjectBase object;

	/** The hash of this string. */
	sjme_jint hashCode;

	/** The length of this string. */
	sjme_jint length;

	/** The sequence of characters which make up the string. */
	sjme_charSeq seq;
};

struct sjme_jarrayBase
{
	/** Base object. */
	sjme_jobjectBase object;

	/** The array type. */
	sjme_javaTypeId type;

	/** The length of the array. */
	sjme_jint length;

	/** The elements in the array. */
	sjme_alignPointer sjme_nvm_rawArrayValues elements;
};

/**
 * Sets the field value.
 * 
 * @param javaType The Java type to use. 
 * @param into The field values to write into.
 * @param atIndex The index to set.
 * @param value The value to store.
 * @return Any resultant error, if any.
 * @since 2024/10/29
 */
sjme_errorCode sjme_nvm_fieldValueSet(
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInNotNull sjme_nvm_fieldValues* into,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jvalue* value);

/**
 * Returns the size for @c sjme_nvm_fieldValues for the given number of
 * values.
 * 
 * @param javaType The Java type to use.
 * @param n The number of values.
 * @return The size of the structure.
 */
sjme_jint sjme_nvm_fieldValueSize(
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInPositiveNonZero sjme_jint n);

/**
 * Checks if the given member can be accessed from the given class.
 * 
 * @param fromClass The source class.
 * @param toMember The member being accessed.
 * @param accessOkay If the access is acceptable.
 * @return On any resultant error, if any.
 * @since 2025/02/26
 */
sjme_errorCode sjme_nvm_instance_checkPermission(
	sjme_attrInNotNull sjme_jclass fromClass,
	sjme_attrInNotNull sjme_jmemberID toMember,
	sjme_attrOutNotNull sjme_jboolean* accessOkay);
	
/**
 * Checks if the given object can be counted down if the old value changes.
 * 
 * @param oldP The old pointer value.
 * @param newV The new value.
 * @return Any resultant error, if any.
 * @since 2025/02/24
 */
sjme_errorCode sjme_nvm_instance_countDown(
	sjme_attrInNotNull sjme_jobject* oldP,
	sjme_attrInNotNull sjme_jobject newV);

/**
 * Allocates a new array object.
 * 
 * @param contextThread The thread the allocation is being performed in.
 * @param outObject The resultant type.
 * @param componentType The component type of the array.
 * @param arrayLength The length of the array to allocate.
 * @return Any resultant error, if any.
 * @since 2025/03/17
 */
sjme_errorCode sjme_nvm_instance_objectArrayNew(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositive sjme_jint arrayLength);

/**
 * Allocates a new array object.
 * 
 * @param contextThread The thread the allocation is being performed in.
 * @param outObject The resultant type.
 * @param componentType The component type of the array.
 * @param arrayLength The length of the array to allocate.
 * @return Any resultant error, if any.
 * @since 2025/03/17
 */
sjme_errorCode sjme_nvm_instance_objectArrayNewT(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId componentType,
	sjme_attrInPositive sjme_jint arrayLength);
	
/**
 * Allocates a new object.
 * 
 * @param contextThread The context thread for the allocation, if a class
 * initialization is required.
 * @param allocSize The allocation size.
 * @param inType The NVM structure type.
 * @param outObject The resultant object.
 * @param inClass The class type to use for the object.
 * @return Any resultant error, if any.
 * @since 2025/02/23
 */
sjme_errorCode sjme_nvm_instance_objectNew(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass inClass);

/**
 * Allocates a new object.
 * 
 * @param contextThread The context thread for the allocation, if a class
 * initialization is required.
 * @param allocSize The allocation size.
 * @param inType The NVM structure type.
 * @param outObject The resultant object.
 * @param inClass The class type to use for the object.
 * @return Any resultant error, if any.
 * @since 2025/02/23
 */
sjme_errorCode sjme_nvm_instance_objectNewN(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_charSeq inClass);
	
/**
 * Allocates a new object.
 * 
 * @param contextThread The context thread for the allocation, if a class
 * initialization is required.
 * @param allocSize The allocation size.
 * @param inType The NVM structure type.
 * @param outObject The resultant object.
 * @param inClass The class type to use for the object.
 * @return Any resultant error, if any.
 * @since 2025/03/09
 */
sjme_errorCode sjme_nvm_instance_objectNewNU(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_lpcstr inClass);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_INSTANCE_H
}
		#undef SJME_CXX_SQUIRRELJME_INSTANCE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_INSTANCE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_INSTANCE_H */
