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

#ifndef SJME_C_INSTANCE_H
#define SJME_C_INSTANCE_H

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
	sjme_jchar c[sjme_flexibleArrayCountUnion];
	
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
	sjme_jint length;
	
	/** Values within the tread. */
	sjme_alignPointer sjme_nvm_rawFieldValues values;
} sjme_nvm_fieldValues;

struct sjme_jobjectBase
{
	/** Common base for all objects. */
	sjme_nvm_commonBase common;
	
	/** The identity hashcode. */
	sjme_jint identityHash;
	
	/** The current class that this is. */
	sjme_jclass isClass;

	/** The monitor of monitor counts. */
	sjme_atomic_sjme_jint monitorCount;
	
	/** Special value, if needed. */
	sjme_atomic_sjme_intPointer special;
};

struct sjme_jthrowableBase
{
	/** Object base. */
	sjme_jobjectBase object;
};

struct sjme_jbracketJarPackageBase
{
	/** Object base. */
	sjme_jobjectBase object;

	/** The library this refers to. */
	sjme_nvm_rom_library library;
};

struct sjme_jbracketTraceBase
{
	/** Object base. */
	sjme_jobjectBase object;

	/** The pointer to the frame. */
	sjme_nvm_frame frame;

	/** The ID of the frame, used to identify if it has changed. */
	sjme_jint id;

	/**
	 * The index from the base of the stack, the bottom of the stack will
	 * have index zero.
	 */
	sjme_jint baseIndex;

	/** The captured frame data. */
	struct
	{
		/** The class this came from. */
		sjme_jclass inClass;

		/** The code this is in. */
		sjme_nvm_class_codeInfo inCode;

		/** The last executed PC address. */
		sjme_jint lastPc;

		/** The last instruction vector. */
		sjme_byteCode lastIv;
	} capture;
};

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

/**
 * Class fields.
 *
 * @since 2025/06/21
 */
typedef struct sjme_nvm_jclass_fields
{
	/** Base index for fields. */
	sjme_jshort base[SJME_NUM_EXTENDED_JAVA_TYPE_IDS];
	
	/** Count for a given field. */
	sjme_jshort count[SJME_NUM_EXTENDED_JAVA_TYPE_IDS];

	/** Field offsets into the object, for each field type. */
	sjme_intPointer offset[SJME_NUM_EXTENDED_JAVA_TYPE_IDS];
	
	/** Field bindings for this class. */
	sjme_list_sjme_jfieldID* binds;

	/** The allocation size of this class. */
	sjme_jint allocSize;

	/** The allocation base of this class, generally where fields start. */
	sjme_jint allocSelfBase;
} sjme_nvm_jclass_fields;

/**
 * Class methods.
 *
 * @since 2025/06/21
 */
typedef struct sjme_nvm_jclass_methods
{
	/** Base index for methods. */
	sjme_jshort base;
	
	/** The number of methods. */
	sjme_jshort count;
	
	/** Method bindings for this class. */
	sjme_list_sjme_jmethodID* binds;
} sjme_nvm_jclass_methods;

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

	/** Fields. */
	sjme_nvm_jclass_fields fields[SJME_NVM_CLASS_NUM_INSTANCE_TYPE];

	/** Methods. */
	sjme_nvm_jclass_methods methods[SJME_NVM_CLASS_NUM_INSTANCE_TYPE];
	
	/** Interface method binds. */
	sjme_list_sjme_jinterfaceID* interfaceBinds;
	
	/** The classes this implements or extends. */
	sjme_nvm_isClasses isClasses;

	/** The Java type ID of this class. */
	sjme_javaTypeId typeId;

	/** The array type ID of this class. */
	sjme_basicTypeId arrayTypeId;

	/** The component type of this class, if it is an array. */
	sjme_atomic_sjme_jclass componentType;

	/** The phantom array type of this class. */
	sjme_atomic_sjme_jclass phantomArrayType;

	/** Static field data chunk. */
	sjme_pointer staticChunk;
};

struct sjme_jstringBase
{
	/** All strings are objects. */
	sjme_jobjectBase object;

	/** The sequence of characters which make up the string. */
	sjme_atomic_sjme_charSeq seq;

	/** Intern based information. */
	struct
	{
		/** The hash of this string. */
		sjme_jint hashCode;

		/** The length of this string. */
		sjme_jint length;
	} intern;
};

struct sjme_jarrayBase
{
	/** Base object. */
	sjme_jobjectBase object;

	/** The array type. */
	sjme_basicTypeId type;

	/** The length of the array. */
	sjme_jint length;

	/** The elements in the array. */
	sjme_alignPointer sjme_nvm_rawArrayValues e;
};

struct sjme_jweakBase
{
	/** Base object. */
	sjme_jobjectBase object;

	/** Has this been initialized? */
	sjme_atomic_sjme_jint beenInit;

	/** The object this points to. */
	sjme_atomic_sjme_jobject pointer;

	/** The reference queue. */
	sjme_atomic_sjme_jobject queue;
};

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
 * The default accessor for fields.
 * 
 * @param instance The instance to access.
 * @param field The field to access.
 * @return The pointer to the field data directly.
 * @since 2025/06/21
 */
sjme_jvalue* sjme_nvm_instance_fieldAccessor(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field);

/**
 * Initializes the field chunk.
 * 
 * @param contextThread The context thread.
 * @param instance The instance being initialized.
 * @param chunk The chunk being written into.
 * @param fields The field binds.
 * @param placements The placements being used.
 * @return Any resultant error, if any.
 * @since 2025/06/21
 */
sjme_errorCode sjme_nvm_instance_initFields(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_pointer chunk,
	sjme_attrInNotNull sjme_list_sjme_jfieldID* fields,
	sjme_attrInNotNull sjme_nvm_jclass_fields* placements);

/**
 * Initializes the field chunk.
 * 
 * @param chunk The chunk to initialize.
 * @param placements The placements to use.
 * @return Any resultant error, if any.
 * @since 2025/06/21
 */
sjme_errorCode sjme_nvm_instance_initFieldsChunk(
	sjme_attrInNotNull sjme_pointer chunk,
	sjme_attrInNotNull sjme_nvm_jclass_fields* placements);

/**
 * Reads or writes a field based on a stack based value.
 * 
 * @param contextThread The context thread.
 * @param fieldId The field ID.
 * @param instance The instance to access.
 * @param stackType The stack type.
 * @param isPut If a put operation, @c fieldId is set to @c stackType ,
 * otherwise @c stackType is written with the field value.
 * @return Any resultant error, if any.
 * @since 2025/06/26
 */
sjme_errorCode sjme_nvm_instance_fieldAccessStack(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jfieldID fieldId,
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jvalueTyped* stackType,
	sjme_attrInValue sjme_jboolean isPut);
	
/**
 * Enters the object's monitor.
 * 
 * @param contextThread The context thread.
 * @param instance The object instance.
 * @return Any resultant error, if any.
 * @since 2025/06/22
 */
sjme_errorCode sjme_nvm_instance_monitorEnter(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance);

/**
 * Exits the object's monitor.
 * 
 * @param contextThread The context thread.
 * @param instance The object instance.
 * @return Any resultant error, if any.
 * @since 2025/06/22
 */
sjme_errorCode sjme_nvm_instance_monitorExit(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject instance);
	
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
	sjme_attrOutNotNull sjme_jarray* outObject,
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
	sjme_attrOutNotNull sjme_jarray* outObject,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS)
	sjme_basicTypeId componentType,
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
	sjme_attrInNegativeOnePositive sjme_jint allocSize,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject,
	sjme_attrInNotNull sjme_jclass inClass);

/**
 * Allocates a bracket based type.
 * 
 * @param contextThread The thread this is allocating within.
 * @param inType The structure type being allocated.
 * @param outObject The resultant bracket object.
 * @return Any resultant error, if any.
 * @since 2025/06/28
 */
sjme_errorCode sjme_nvm_instance_objectNewBracket(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jobject* outObject);
	
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

/** The object class. */
#define SJME_O_C(obj) \
	((obj)->isClass)

/** The array object class. */
#define SJME_AO_C(arr) \
	SJME_O_C(&arr->object)
	
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
