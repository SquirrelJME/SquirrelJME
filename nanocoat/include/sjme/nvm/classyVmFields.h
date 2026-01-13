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
