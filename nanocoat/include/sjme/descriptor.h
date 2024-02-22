/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Descriptors and their interpreters.
 * 
 * @since 2024/02/04
 */

#ifndef SQUIRRELJME_DESCRIPTOR_H
#define SQUIRRELJME_DESCRIPTOR_H

#include "sjme/nvm.h"
#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_DESCRIPTOR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents a single interpreted identifier.
 * 
 * @since 2024/02/04
 */
typedef struct sjme_desc_identifier
{
	/** The hash of the identifier. */
	sjme_jint hash;
	
	/** The pointer and length to the identifier. */
	sjme_pointerLen pointer;
} sjme_desc_identifier;

/** List of identifiers. */
SJME_LIST_DECLARE(sjme_desc_identifier, 0);

/**
 * Represents an interpretation of a binary name.
 * 
 * @since 2024/02/04
 */
typedef struct sjme_desc_binaryName
{
	/** The hash of the binary name. */
	sjme_jint hash;
	
	/** The pointer area for the whole binary name. */
	sjme_pointerLen whole;
	
	/** The identifiers that make up the binary name. */
	sjme_list_sjme_desc_identifier identifiers;
} sjme_desc_binaryName;

/**
 * Determines the size of a binary name.
 * 
 * @param numIdents The number of used identifiers.
 * @return The resultant size.
 * @since 2024/02/21
 */
#define SJME_SIZEOF_DESC_BINARY_NAME(numIdents) \
	sizeof(sjme_desc_binaryName) + \
		SJME_SIZEOF_LIST(sjme_desc_identifier, 0, (numIdents))

/**
 * Represents an interpretation of a field type.
 * 
 * @since 2024/02/04
 */
typedef struct sjme_desc_fieldType sjme_desc_fieldType;

struct sjme_desc_fieldType
{
	/** The hash of the field. */
	sjme_jint hash;
	
	/** The pointer area for the whole field type. */
	sjme_pointerLen whole;
	
	/** The type of field this is. */
	sjme_javaTypeId javaType;
	
	/** The cell count of this field. */
	sjme_jint cells;
	
	/** Is this an array? */
	sjme_jboolean isArray : 1;
	
	/** Interpretation of the array component type, if an array. */
	const sjme_desc_fieldType* componentType;
	
	/** Interpretation of object type, if an object. */
	const sjme_desc_binaryName* objectType;
};

/** A list of interpreted field descriptors. */
SJME_LIST_DECLARE(sjme_desc_fieldType, 0);

/**
 * An interpreted class descriptor which is either a field if this is an
 * array, or otherwise a binary name.
 * 
 * @since 2024/02/04
 */
typedef struct sjme_desc_className
{
	/** The hash for this class. */
	sjme_jint hash;

	/** The pointer area for the whole class name. */
	sjme_pointerLen whole;
	
	/** Does this represent a field? */
	sjme_jboolean isField : 1;
	
	/** The descriptor used. */
	union
	{
		/** Binary name descriptor. */
		sjme_desc_binaryName binary;
		
		/** Field type descriptor. */
		sjme_desc_fieldType field; 
	} descriptor;
} sjme_desc_className;

/**
 * Interpretation of a method descriptor.
 * 
 * @since 2024/02/04
 */
typedef struct sjme_desc_methodType
{
	/** The hash of the descriptor. */
	sjme_jint hash;
	
	/** The pointer area for the whole method type. */
	sjme_pointerLen whole;
	
	/** Return value cells. */
	sjme_jint returnCells;
	
	/** Argument cells. */
	sjme_jint argCells;
	
	/** The field descriptors used, index zero is the return value. */
	sjme_list_sjme_desc_fieldType fields;
} sjme_desc_methodType;

/**
 * Compares the binary name against the given binary name.
 * 
 * @param aName The first value. 
 * @param bName The second value.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareBinaryName(
	sjme_attrInNullable const sjme_desc_binaryName* aName,
	sjme_attrInNullable const sjme_desc_binaryName* bName);
	
/**
 * Compares the binary name against the given string.
 * 
 * @param aName The binary name to check.
 * @param bString The string to match against.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareBinaryNameS(
	sjme_attrInNullable const sjme_desc_binaryName* aName,
	sjme_attrInNullable sjme_lpcstr bString);

/**
 * Compares the class against the given class.
 * 
 * @param aClass The first value. 
 * @param bClass The second value.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareClass(
	sjme_attrInNullable const sjme_desc_className* aClass,
	sjme_attrInNullable const sjme_desc_className* bClass);
	
/**
 * Compares the class against the given string.
 * 
 * @param aClass The class to check. 
 * @param bIsFieldType Should this be a field type match?
 * @param bString The string to match against.
 * @return The comparison value.
 * @since 2024/02/11
 */
sjme_jint sjme_desc_compareClassS(
	sjme_attrInNullable const sjme_desc_className* aClass,
	sjme_attrInValue sjme_jboolean bIsFieldType, 
	sjme_attrInNullable sjme_lpcstr bString);

/**
 * Compares the field against the given field.
 * 
 * @param aField The first value. 
 * @param bField The second value.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareField(
	sjme_attrInNullable const sjme_desc_fieldType* aField,
	sjme_attrInNullable const sjme_desc_fieldType* bField);
	
/**
 * Compares the field against the given string.
 * 
 * @param aField The field to check.
 * @param bString The string to match against.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareFieldS(
	sjme_attrInNullable const sjme_desc_fieldType* aField,
	sjme_attrInNullable sjme_lpcstr bString);

/**
 * Compares the identifier against the given identifier.
 * 
 * @param aClass The first value. 
 * @param bClass The second value.
 * @return The comparison value.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareIdentifier(
	sjme_attrInNullable const sjme_desc_identifier* aIdent,
	sjme_attrInNullable const sjme_desc_identifier* bIdent);

/**
 * Compares the identifier against the given string.
 * 
 * @param aIdent The identifier to check.
 * @param bString The string to check against.
 * @return The comparison value.
 * @since 2024/02/11
 */
sjme_jint sjme_desc_compareIdentifierS(
	sjme_attrInNullable const sjme_desc_identifier* aIdent,
	sjme_attrInNullable sjme_lpcstr bString);

/**
 * Compares the method against the given method.
 * 
 * @param aMethod The first value. 
 * @param bMethod The second value.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareMethod(
	sjme_attrInNullable const sjme_desc_methodType* aMethod,
	sjme_attrInNullable const sjme_desc_methodType* bMethod);
	
/**
 * Compares the field against the given string.
 * 
 * @param aMethod The method to check.
 * @param bString The string to match against.
 * @return The comparison value.
 * @since 2024/02/14
 */
sjme_jint sjme_desc_compareMethodS(
	sjme_attrInNullable const sjme_desc_methodType* aMethod,
	sjme_attrInNullable sjme_lpcstr bString);

/**
 * Interprets the given binary name.
 * 
 * @param inPool The allocation pool to use. 
 * @param outName The interpreted result.
 * @param inStr The input string to be interpreted.
 * @param inLen The length of the input string.
 * @return Any resultant error code.
 * @since 2024/02/04
 */
sjme_errorCode sjme_desc_interpretBinaryName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_binaryName** outName,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen);

/**
 * Interprets the given class name.
 * 
 * @param inPool The allocation pool to use. 
 * @param outName The interpreted result.
 * @param inStr The input string to be interpreted.
 * @param inLen The length of the input string.
 * @return Any resultant error code.
 * @since 2024/02/04
 */
sjme_errorCode sjme_desc_interpretClassName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_className** outName,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen);

/**
 * Interprets the given field type.
 * 
 * @param inPool The allocation pool to use. 
 * @param outType The interpreted result.
 * @param inStr The input string to be interpreted.
 * @param inLen The length of the input string.
 * @return Any resultant error code.
 * @since 2024/02/04
 */
sjme_errorCode sjme_desc_interpretFieldType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_fieldType** outType,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen);

/**
 * Interprets the given identifier.
 * 
 * @param outIdent The interpreted result.
 * @param inStr The input string to be interpreted.
 * @param inLen The length of the input string.
 * @return Any resultant error code.
 * @since 2024/02/04
 */
sjme_errorCode sjme_desc_interpretIdentifier(
	sjme_attrOutNotNull sjme_desc_identifier* outIdent,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen);

/**
 * Interprets the given method type.
 * 
 * @param inPool The allocation pool to use. 
 * @param outType The interpreted result.
 * @param inStr The input string to be interpreted.
 * @param inLen The length of the input string.
 * @return Any resultant error code.
 * @since 2024/02/04
 */
sjme_errorCode sjme_desc_interpretMethodType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_methodType** outType,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_DESCRIPTOR_H
}
		#undef SJME_CXX_SQUIRRELJME_DESCRIPTOR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_DESCRIPTOR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_DESCRIPTOR_H */
