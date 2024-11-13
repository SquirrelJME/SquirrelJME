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
		
		/** Object reference values. */
		sjme_jobject jobjects[sjme_flexibleArrayCountUnion];
	} values;
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

struct sjme_jclassBase
{
	/** All classes are objects. */
	sjme_jobjectBase object;
	
	/** The binary name of this class. */
	sjme_lpcstr binaryName;
	
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
	sjme_list_sjme_methodID* methodBinds[
		SJME_NVM_CLASS_NUM_INSTANCE_TYPE];
	
	/** The classes this implements or extends. */
	sjme_nvm_isClasses* isClasses;
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
