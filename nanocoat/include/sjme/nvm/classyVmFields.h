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
 * Stores field object values.
 *
 * @since 2025/07/18
 */
typedef struct sjme_nvm_fieldObject
{
	/** The check for the object. */
	sjme_jint check;
	
	/** The pointer to the object. */
	sjme_alignPointer sjme_jobject p;
} sjme_nvm_fieldObject;

/**
 * Raw field value.
 *
 * @since 2025/07/18
 */
typedef union sjme_nvm_rawFieldValue
{
	/** Normal non-object values. */
	sjme_jvaluePrimitive v;
		
	/** Object reference values. */
	sjme_nvm_fieldObject l;
} sjme_nvm_rawFieldValue;

/**
 * Returns the direct pointer to the field data pointer.
 *
 * @param instance The object to access with.
 * @param field The field to access for.
 * @since 2025/06/21
 */
typedef sjme_nvm_rawFieldValue* (*sjme_nvm_jfieldAccessFunc)(
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
	/** @link sjme_jvalue @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE,
	
	/** @link sjme_jvalue* @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE_P,
	
	/** @link sjme_jvalueTyped @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE_TYPED,
	
	/** @link sjme_jvalueTyped* @endlink. */
	SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P,
	
	/** @link sjme_jobject @endlink. */
	SJME_NVM_VMFIELD_VAR_JOBJECT,
	
	/** The number of variable types. */
	SJME_NVM_VMFIELD_NUM_VAR,
} sjme_nvm_vmField_var;

/**
 * Atomically sets the raw value of a field with a memory barrier.
 * 
 * @param rawField The raw field to access.
 * @param SJME_VFT_ Pass via @code SJME_VFT_ @endcode macros.
 * @param ... Pass via @code SJME_VFT_ @endcode macros.
 * @return Any resultant error, if any.
 * @since 2026/01/12
 */
sjme_errorCode sjme_nvm_vmField_atomicSet(
	sjme_attrInOutNotNull sjme_nvm_rawFieldValue* rawField,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VFT_,
	...);

/**
 * Non-atomically sets the raw value of a field.
 * 
 * @param rawField The raw field to access.
 * @param SJME_VFT_ Pass via @code SJME_VFT_ @endcode macros.
 * @param ... Pass via @code SJME_VFT_ @endcode macros.
 * @return Any resultant error, if any.
 * @since 2026/01/12
 */
sjme_errorCode sjme_nvm_vmField_cisSet(
	sjme_attrInOutNotNull sjme_nvm_rawFieldValue* rawField,
	sjme_attrInRange(0, SJME_NVM_VMFIELD_NUM_VAR)
		sjme_nvm_vmField_var SJME_VFT_,
	...);

/**
 * Variable field value set.
 * 
 * @param varType Variable type.
 * @param args Variable arguments.
 * @since 2026/01/12
 */
#define SJME_VFT_(varType, args) \
	(varType), args
	
/**
 * Set field to a @link sjme_jobject @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VFT_JOBJECT(inValue) \
	SJME_VFT_(SJME_NVM_VMFIELD_VAR_JOBJECT, \
		(sjme_jobject)inValue)
	
/**
 * Set field to a @link sjme_jvalue @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VFT_JVALUE(inValue) \
	SJME_VFT_(SJME_NVM_VMFIELD_VAR_JVALUE, \
		(sjme_jvalue)inValue)
	
/**
 * Set field to a @link sjme_jvalue* @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VFT_JVALUE_P(inValue) \
	SJME_VFT_(SJME_NVM_VMFIELD_VAR_JVALUE_P, \
		(sjme_jvalue*)inValue)
	
/**
 * Set field to a @link sjme_jvalueTyped @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VFT_JVALUE_TYPED(inValue) \
	SJME_VFT_(SJME_NVM_VMFIELD_VAR_JVALUE_TYPED, \
		(sjme_jvalueTyped)inValue)
	
/**
 * Set field to a @link sjme_jvalueTyped* @endlink.
 * 
 * @param inValue The input value.
 * @since 2026/01/12
 */
#define SJME_VFT_JVALUE_TYPED_P(inValue) \
	SJME_VFT_(SJME_NVM_VMFIELD_VAR_JVALUE_TYPED_P, \
		(sjme_jvalueTyped*)inValue)
	
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
