/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * This is the core header which is used by the SquirrelJME C output JIT
 * compiler.
 *
 * @since 2016/07/17
 */

/** Header guard. */
#ifndef SJME_hGNETMULTIPHASIGCSQUIRRELH
#define SJME_hGNETMULTIPHASIGCSQUIRRELH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXNETMULTIPHASIGCSQUIRRELH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include <stdlib.h>
#include <stdint.h>

/**
 * Flags which are associated with classes.
 *
 * @since 2016/07/18
 */
typedef enum SJME_ClassFlag
{
	/** Public access. */
	SJME_CLASSFLAG_PUBLIC =
		1,
	
	/** Final. */
	SJME_CLASSFLAG_FINAL =
		2,
	
	/** Super. */
	SJME_CLASSFLAG_SUPER =
		4,
	
	/** Interface. */
	SJME_CLASSFLAG_INTERFACE =
		8,
	
	/** Abstract. */
	SJME_CLASSFLAG_ABSTRACT =
		16,
	
	/** Synthetic. */
	SJME_CLASSFLAG_SYNTHETIC =
		32,
	
	/** Annotation. */
	SJME_CLASSFLAG_ANNOTATION =
		64,
	
	/** Enumeration. */
	SJME_CLASSFLAG_ENUM =
		128,
	
	/** End. */
	NUM_SJME_CLASSFLAG
} SJME_ClassFlag;

/**
 * This enumeration is used to determine which kind of structure is being
 * declared so that its information can be parsed correctly.
 *
 * @since 2016/07/18
 */
typedef enum SJME_StructureType
{
	/** Not defined. */
	SJME_STRUCTURETYPE_UNDEFINED,
	
	/** A class. */
	SJME_STRUCTURETYPE_CLASS,
	
	/** A resource. */
	SJME_STRUCTURETYPE_RESOURCE,
	
	/** A namespace. */
	SJME_STRUCTURETYPE_NAMESPACE,
	
	/** A string. */
	SJME_STRUCTURETYPE_STRING,
	
	/** End. */
	NUM_SJME_STRUCTURETYPE
} SJME_StructureType;

/**
 * A UTF-16 String.
 *
 * @since 2016/07/18
 */
typedef struct SJME_String
{
	/** The type of structure this is. */
	SJME_StructureType structuretype;
	
	/** The string length. */
	uint32_t length;
	
	/** The UTF-16 characters. */
	const uint16_t chars[];
} SJME_String;

/**
 * This represents a class which is available to SquirrelJME.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Class
{
	/** The type of structure this is. */
	SJME_StructureType structuretype;
	
	/** The name of the class. */
	SJME_String* name;
	
	/** Class flags. */
	uint16_t flags;
	
	/** Should always be zero. */
	int zero;
} SJME_Class;

/**
 * This represents a resource which is available to a namespace.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Resource
{
	/** The type of structure this is. */
	SJME_StructureType structuretype;
	
	/** The name of the resource. */
	SJME_String* name;
	
	/** The resource data. */
	const uint8_t* data;
	
	/** The length of the resource. */
	const uint32_t length;
} SJME_Resource;

/**
 * This represents a namespace which is available to the initialization and
 * linking system.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Namespace
{
	/** The type of structure this is. */
	SJME_StructureType structuretype;
	
	/** The name of the namespace. */
	SJME_String* name;
	
	/** Namespace contents. */
	const void** contents;
	
} SJME_Namespace;

/**
 * Namespaces that make up the JVM classpath, this is {@code NULL} terminated.
 *
 * @since 2016/07/21
 */
extern SJME_Namespace** initialNamespaces;

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXNETMULTIPHASIGCSQUIRRELH
}
#undef SJME_cXNETMULTIPHASIGCSQUIRRELH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXNETMULTIPHASIGCSQUIRRELH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGNETMULTIPHASIGCSQUIRRELH */

