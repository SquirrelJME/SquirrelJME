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
	
	/** End. */
	NUM_SJME_STRUCTURETYPE
} SJME_StructureType;

/**
 * This represents a class which is available to SquirrelJME.
 *
 * @since 2016/07/18
 */
typedef struct SJME_Class
{
	/** The type of structure this is. */
	SJME_StructureType structuretype;
	
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
	
} SJME_Namespace;

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

