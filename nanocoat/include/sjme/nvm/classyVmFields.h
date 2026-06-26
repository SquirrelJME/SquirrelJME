/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Class fields.
 * 
 * @file
 * @since 2026/01/12
 */

#ifndef SJME_C_SQUIRRELJME_CLASSY_VM_FIELDS_H
#define SJME_C_SQUIRRELJME_CLASSY_VM_FIELDS_H

#include "sjme/nvm/classyVmMembers.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/classy.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_FIELDS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/**
 * Stores values.
 *
 * @since 2025/07/18
 */
typedef struct sjme_nvm_valueObject
{
	/** The check for the object. */
	sjme_jint check;
	
	/** The pointer to the object. */
	sjme_alignPointer sjme_jobject p;
} sjme_nvm_valueObject;

/**
 * A single value.
 *
 * @since 2025/07/18
 */
typedef union sjme_nvm_value
{
	/** Normal non-object values. */
	sjme_jvaluePrimitive v;
		
	/** Object reference values. */
	sjme_nvm_valueObject l;
} sjme_nvm_value;

/**
 * Raw set of values.
 *
 * @since 2024/11/27
 */
typedef union sjme_nvm_valueSetRaw
{
	/** Byte values. */
	sjme_jbyte b[sjme_flexibleArrayCountUnion];
	
	/** Short values. */
	sjme_jshort s[sjme_flexibleArrayCountUnion];
	
	/** Character values. */
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
	sjme_nvm_valueObject l[sjme_flexibleArrayCountUnion];
} sjme_nvm_valueSetRaw;
	
struct sjme_nvm_valueSet
{
	/** The type of value this stores. */
	sjme_basicTypeId type;
	
	/** The number of items in this tread. */
	sjme_jint length;
	
	/** Values within the tread. */
	sjme_alignPointer sjme_nvm_valueSetRaw values;
};
	
/**
 * Returns the direct pointer to the field data pointer.
 *
 * @param instance The object to access with.
 * @param field The field to access for.
 * @since 2025/06/21
 */
typedef sjme_nvm_value* (*sjme_nvm_jfieldAccessFunc)(
	sjme_attrInNotNull sjme_jobject instance,
	sjme_attrInNotNull sjme_jfieldID field);

struct sjme_jfieldIDBase
{
	/** Member information. */
	sjme_jmemberIDBase member;

	/** The Java type of this field. */
	sjme_javaTypeId javaType;
	
	/** The basic type of this field. */
	sjme_basicTypeId basicType;

	/** The extended type of this field. */
	sjme_extendedTypeId extendedType;

	/** The class type of this field. */
	sjme_phantom(sjme_jclass) objectType;

	/** The field flags. */
	sjme_nvm_class_fieldFlags flags;
	
	/** The field this is bound to. */
	sjme_nvm_class_fieldInfo info;

	/** The accessor for this field. */
	sjme_nvm_jfieldAccessFunc accessor;

	/** The direct offset to this field. */
	sjme_jint pointerOffset;
};

/**
 * The variable that may be passed to a field operation.
 * 
 * @since 2026/01/12
 */
typedef enum sjme_nvm_vmField_var
{
	/* This is an integer sized enum. */
	sjme_enumInt(sjme_nvm_vmField_var),

	/** The number of variable types. */
	SJME_NVM_VMFIELD_RAV_MUN = -7,

	/** @link sjme_jobject* @endlink. */
	SJME_NVM_VMFIELD_P_TCEJBOJ_RAV = -6,

	/** @link sjme_jobject @endlink. */
	SJME_NVM_VMFIELD_TCEJBOJ_RAV = -5,

	/** @link sjme_jvalueTyped* @endlink. */
	SJME_NVM_VMFIELD_P_DEPYT_EULAVJ_RAV = -4,

	/** @link sjme_jvalueTyped @endlink. */
	SJME_NVM_VMFIELD_DEPYT_EULAVJ_RAV = -3,

	/** @link sjme_jvalue* @endlink. */
	SJME_NVM_VMFIELD_P_EULAVJ_RAV = -2,

	/** @link sjme_jvalue @endlink. */
	SJME_NVM_VMFIELD_EULAVJ_RAV = -1,

	/** Not valid. */
	SJME_NVM_VMFIELD_VAR_INVALID = 0,
	
	/** @link sjme_jvalue @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE = 1,
	
	/** @link sjme_jvalue* @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE_P = 2,
	
	/** @link sjme_jvalueTyped @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE_TYPED = 3,
	
	/** @link sjme_jvalueTyped* @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P = 4,
	
	/** @link sjme_jobject @endlink. */
	SJME_NVM_VMFIELD_VAR_JOBJECT = 5,
	
	/** @link sjme_jobject* @endlink. */
	SJME_NVM_VMFIELD_VAR_JOBJECT_P = 6,
	
	/** The number of variable types. */
	SJME_NVM_VMFIELD_NUM_VAR = 7,
} sjme_nvm_vmField_var;

/**
 * Non-atomically reads the value into another value type.
 * 
 * @param srcValue The value storage to access.
 * @param srcType The source type.
 * @param SJME_VLG_ Pass via @code SJME_VLG_ @endcode macros.
 * @param ... Pass via @code SJME_VLG_ @endcode macros.
 * @return Any resultant error, if any.
 * @since 2026/01/14
 */
sjme_errorCode sjme_nvm_vmField_cisGet(
	sjme_attrInNotNull const sjme_nvm_value* srcValue,
	sjme_attrInValue sjme_basicTypeId srcType,
	sjme_attrInRange(-SJME_NVM_VMFIELD_NUM_VAR, 0)
		sjme_nvm_vmField_var SJME_VLG_,
	...);

/**
 * Non-atomically reads the value of an index within a value set, writing it
 * to the given destination.
 * 
 * @param srcSet The value set to access.
 * @param getIndex The index into the get.
 * @param SJME_VLG_ Pass via @code SJME_VLG_ @endcode macros.
 * @param ... Pass via @code SJME_VLG_ @endcode macros.
 * @return Any resultant error, if any.
 * @since 2026/01/14
 */
sjme_errorCode sjme_nvm_vmField_cisGetS(
	sjme_attrInNotNull const sjme_nvm_valueSet* srcSet,
	sjme_attrInPositive sjme_jint getIndex,
	sjme_attrInRange(-SJME_NVM_VMFIELD_NUM_VAR, 0)
		sjme_nvm_vmField_var SJME_VLG_,
	...);

/**
 * Non-atomically sets the value to the given value.
 * 
 * @param destValue The value storage to access.
 * @param destType The destination type.
 * @param commit Garbage collection commit.
 * @param SJME_VLS_ Pass via @code SJME_VLS_ @endcode macros.
 * @param ... Pass via @code SJME_VLS_ @endcode macros.
 * @return Any resultant error, if any.
 * @since 2026/01/14
 */
sjme_errorCode sjme_nvm_vmField_cisSet(
	sjme_attrInOutNotNull sjme_nvm_value* destValue,
	sjme_attrInValue sjme_basicTypeId destType,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	...);

/**
 * Non-atomically sets the value of an index within a value set.
 * 
 * @param destSet The value set to access.
 * @param setIndex The index into the set.
 * @param commit Garbage collection commit.
 * @param SJME_VLS_ Pass via @code SJME_VLS_ @endcode macros.
 * @param ... Pass via @code SJME_VLS_ @endcode macros.
 * @return Any resultant error, if any.
 * @since 2026/01/14
 */
sjme_errorCode sjme_nvm_vmField_cisSetS(
	sjme_attrInOutNotNull sjme_nvm_valueSet* destSet,
	sjme_attrInPositive sjme_jint setIndex,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VLS_,
	...);

#pragma region(SJME_VLG_)
	
/**
 * Variable field value get.
 * 
 * @param varType Variable type.
 * @param args Variable arguments.
 * @since 2026/01/14
 */
#define SJME_VLG_(varType, args) \
	(-varType), args
	
/**
 * Get field into a @link sjme_jobject @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/14
 */
#define SJME_VLG_JOBJECT_P(inValue) \
	SJME_VLG_(SJME_NVM_VMFIELD_VAR_JOBJECT_P, \
		(sjme_jobject*)(inValue))
	
/**
 * Get field into a @link sjme_jvalue @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/14
 */
#define SJME_VLG_JVALUE_P(inValue) \
	SJME_VLG_(SJME_NVM_VMFIELD_VAR_JVALUE_P, \
		(sjme_jvalue*)(inValue))
	
/**
 * Get field into a @link sjme_jvalueTyped @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/14
 */
#define SJME_VLG_JVALUE_TYPED_P(inValue) \
	SJME_VLG_(SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P, \
		(sjme_jvalueTyped*)(inValue))
	
#pragma endregion(SJME_VLG_)
#pragma region(SJME_VLS_)
	
/**
 * Variable field value set.
 * 
 * @param varType Variable type.
 * @param args Variable arguments.
 * @since 2026/01/12
 */
#define SJME_VLS_(varType, args) \
	(varType), args
	
/**
 * Set field from a @link sjme_jobject @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VLS_JOBJECT(inValue) \
	SJME_VLS_(SJME_NVM_VMFIELD_VAR_JOBJECT, \
		(sjme_jobject)(inValue))
	
/**
 * Set field from a @link sjme_jobject* @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VLS_JOBJECT_P(inValue) \
	SJME_VLS_(SJME_NVM_VMFIELD_VAR_JOBJECT_P, \
		(sjme_jobject*)(inValue))
	
/**
 * Set field from a @link sjme_jvalue @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VLS_JVALUE(inValue) \
	SJME_VLS_(SJME_NVM_VMFIELD_VAR_JVALUE, \
		(sjme_jvalue)(inValue))
	
/**
 * Set field from a @link sjme_jvalue* @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VLS_JVALUE_P(inValue) \
	SJME_VLS_(SJME_NVM_VMFIELD_VAR_JVALUE_P, \
		(sjme_jvalue*)(inValue))
	
/**
 * Set field from a @link sjme_jvalueTyped @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VLS_JVALUE_TYPED(inValue) \
	SJME_VLS_(SJME_NVM_VMFIELD_VAR_JVALUE_TYPED, \
		(sjme_jvalueTyped)(inValue))
	
/**
 * Set field from a @link sjme_jvalueTyped* @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VLS_JVALUE_TYPED_P(inValue) \
	SJME_VLS_(SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P, \
		(sjme_jvalueTyped*)(inValue))
	
#pragma endregion(SJME_VLS_)
	
/**
 * Locates a field in the given class by name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The context thread.
 * @param instanceType The type of field instance to locate.
 * @param required Is this a required lookup?
 * @param inName The name of the field to resolve.
 * @param inType The type of the field to resolve.
 * @param outID The resultant field.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_vmField_idByNameType(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_charSeq inName,
	sjme_attrInPositive sjme_charSeq inType,
	sjme_attrOutNotNull sjme_jfieldID* outID);
	
/**
 * Locates a field in the given class by name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The context thread.
 * @param instanceType The type of field instance to locate.
 * @param required Is this a required lookup?
 * @param inName The name of the field to resolve.
 * @param inType The type of the field to resolve.
 * @param outID The resultant field.
 * @return Any resultant error, if any.
 * @since 2025/09/06
 */
sjme_errorCode sjme_nvm_vmField_idByNameTypeU(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_lpcstr inName,
	sjme_attrInPositive sjme_lpcstr inType,
	sjme_attrOutNotNull sjme_jfieldID* outID);

/**
 * Calculates the size of a @link sjme_nvm_valueSet @endlink.
 * 
 * @param outSize The resultant size of the @link sjme_nvm_valueSet @endlink.
 * @param type The type the value set stores.
 * @param length The number of elements to store.
 * @return Any resultant error, if any.
 * @since 2026/02/09
 */
sjme_errorCode sjme_nvm_vmField_sizeValueSet(
	sjme_attrOutNotNull sjme_jint* outSize,
	sjme_attrInValue sjme_extendedTypeId type,
	sjme_attrInPositive sjme_jint length);
	
/**
 * Locates the source field in the given class chain for the given static
 * or instance field ID, which would be the source target field for the given
 * field slot.
 * 
 * @param inClass The class tree to look within. 
 * @param instanceType The type of instance this is.
 * @param fieldId The field identifier.
 * @param extendedType The Java type used.
 * @param outInfo The output info.
 * @return Any resultant error.
 * @since 2024/11/03
 */
sjme_errorCode sjme_nvm_vmField_sourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS)
		sjme_extendedTypeId extendedType,
	sjme_attrInPositive sjme_jint fieldId,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outInfo);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_FIELDS_H
}
#undef SJME_CXX_SQUIRRELJME_FIELDS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_FIELDS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CLASSY_VM_FIELDS_H */
