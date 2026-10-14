/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Simplified MEEP Suite Management System for initial Jar loading.
 *
 * This is not meant to be a full drop in replacement for the suite management
 * system that is in the runtime library. Although this is a duplicate in C,
 * it is mostly only meant to give the ability to launch Jars and dependencies
 * as if they were normal applications similarly to how the existing
 * SquirrelJME's @code VMFactory @endcode/@code SuiteManager @endcode in
 * SquirrelJME already works.
 * 
 * @since 2026/06/17
 */

#ifndef SJME_C_SQUIRRELJME_ROMMEEPSWM_H
#define SJME_C_SQUIRRELJME_ROMMEEPSWM_H

#include "sjme/nvm/rom.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The type of dependency that this is.
 *
 * @since 2026/06/18
 */
typedef enum sjme_nvm_rom_swmDependencyType
{
	/** This is an integer based enum. */
	sjme_enumInt(sjme_nvm_rom_swmDependencyType),

	/** Unknown. */
	SJME_NVM_SWM_DEPENDENCY_UNKNOWN,

	/** A @link sjme_nvm_swmInfoNameVersion @endlink. */
	SJME_NVM_SWM_DEPENDENCY_CONFIGURATION,

	/** A @link sjme_nvm_swmInfoNameOnly @endlink */
	SJME_NVM_SWM_DEPENDENCY_INTERNAL_NAME,

	/** A @link sjme_nvm_swmInfoNameVersion @endlink. */
	SJME_NVM_SWM_DEPENDENCY_PROFILE,

	/** A @link sjme_nvm_swmInfoAll @endlink. */
	SJME_NVM_SWM_DEPENDENCY_STANDARD,

	/** A @link sjme_nvm_swmInfoAll @endlink. */
	SJME_NVM_SWM_DEPENDENCY_SUITE,

	/** A @link sjme_nvm_swmInfoTypedAll @endlink. */
	SJME_NVM_SWM_DEPENDENCY_TYPED_SUITE,

	/** The number of dependency types. */
	SJME_NVM_SWM_NUM_DEPENDENCY_TYPES,
} sjme_nvm_rom_swmDependencyType;

/**
 * Determines how an entry is to be used, whether it depends on a dependency
 * or if it provides one.
 *
 * @since 2026/06/18
 */
typedef enum sjme_nvm_rom_swmEntryUse
{
	/** This is an integer based enum. */
	sjme_enumInt(sjme_nvm_rom_swmEntryUse),

	/** Unknown usage. */
	SJME_NVM_SWM_USE_UNKNOWN,

	/** A hard dependency. */
	SJME_NVM_SWM_USE_DEPENDS,

	/** An optional dependency. */
	SJME_NVM_SWM_USE_DEPENDS_OPTIONAL,

	/** Provides the given dependency. */
	SJME_NVM_SWM_USE_PROVIDES,

	/** The number of uses for a dependency. */
	SJME_NVM_SWM_NUM_USE,
} sjme_nvm_rom_swmEntryUse;

/**
 * The type of suite this is.
 *
 * @since 2026/10/11
 */
typedef enum sjme_nvm_swmSuiteType
{
	/** This is an integer based enum. */
	sjme_enumInt(sjme_nvm_swmSuiteType),

	/** Unknown. */
	SJME_NVM_SWM_SUITE_UNKNOWN,

	/** MIDlet. */
	SJME_NVM_SWM_SUITE_MIDLET,

	/** LIBlet. */
	SJME_NVM_SWM_SUITE_LIBLET,

	/** SquirrelJME API. */
	SJME_NVM_SWM_SUITE_SQUIRRELJME_API,

	/** The number of suite types. */
	SJME_NVM_NUM_SWM_SUITE_TYPES,
} sjme_nvm_swmSuiteType;

/** The length of individual MEEP SWM fields. */
#define SJME_NVM_SWM_FIELD_LENGTH 96

/**
 * Stores the base dependency information.
 *
 * @since 2026/06/18
 */
typedef struct sjme_nvm_rom_swmEntryTag
{
	/** The type of dependency this is. */
	sjme_nvm_rom_swmDependencyType type;

	/** The usage of this entry. */
	sjme_nvm_rom_swmEntryUse use;
} sjme_nvm_rom_swmEntryTag;

/**
 * A simplified SWM version, from @code 0.0.0 @endcode
 * to @code 99.99.99 @endcode.
 *
 * @since 2026/10/11
 */
typedef struct sjme_nvm_swmVersion
{
	/** The major version. */
	sjme_jubyte major;

	/** The minor version. */
	sjme_jubyte minor;

	/** The minor version. */
	sjme_jubyte release;

	/** Is this compact? */
	sjme_jboolean isCompact;
} sjme_nvm_swmVersion;

/**
 * Stores an internal name dependency or a provider, this name is specific only
 * to SquirrelJME and should not be used elsewhere. Generally, this is used
 * internally to speed up dependency resolution in the internal suites despite
 * it technically not being needed.
 *
 * @since 2026/10/11
 */
typedef struct sjme_nvm_swmInfoNameOnly
{
	/** The base dependency info. */
	sjme_nvm_rom_swmEntryTag tag;

	/** The name of the suite. */
	sjme_jchar name[SJME_NVM_SWM_FIELD_LENGTH];
} sjme_nvm_swmInfoNameOnly;

/**
 * Stores a name and a version, this is generally used for CLDC configurations
 * and profiles.
 *
 * @since 2026/10/11
 */
typedef struct sjme_nvm_swmInfoNameVersion
{
	/** The base dependency info. */
	sjme_nvm_rom_swmEntryTag tag;

	/** The referred to name. */
	sjme_jchar name[SJME_NVM_SWM_FIELD_LENGTH];

	/** The version. */
	sjme_nvm_swmVersion version;
} sjme_nvm_swmInfoNameVersion;

/**
 * Stores a name, a vendor, and a version, this is generally used for actual
 * suite references and standards.
 *
 * @since 2026/10/11
 */
typedef struct sjme_nvm_swmInfoAll
{
	/** The base dependency info. */
	sjme_nvm_rom_swmEntryTag tag;

	/** The referred to vendor. */
	sjme_jchar vendor[SJME_NVM_SWM_FIELD_LENGTH];

	/** The version. */
	sjme_nvm_swmVersion version;

	/** The referred to name. */
	sjme_jchar name[SJME_NVM_SWM_FIELD_LENGTH];
} sjme_nvm_swmInfoAll;

/**
 * Stores a target type, a name, a vendor, and a version, this is generally
 * used to refer to specific suites. Specific suites such as MIDlets, LIBlets,
 * or APIs.
 *
 * @since 2026/10/11
 */
typedef struct sjme_nvm_swmInfoTypedAll
{
	/** The base dependency info. */
	sjme_nvm_rom_swmEntryTag tag;

	/** The referred to vendor. */
	sjme_jchar vendor[SJME_NVM_SWM_FIELD_LENGTH];

	/** The version. */
	sjme_nvm_swmVersion version;

	/** The referred to name. */
	sjme_jchar name[SJME_NVM_SWM_FIELD_LENGTH];

	/** The suite type. */
	sjme_nvm_swmSuiteType suiteType;
} sjme_nvm_swmInfoTypedAll;

/**
 * Combined SWM dependency information.
 *
 * @since 2026/06/18
 */
typedef union sjme_nvm_rom_swmEntryBase sjme_nvm_rom_swmEntry;

typedef union sjme_nvm_rom_swmEntryBase
{
	/** The base dependency info. */
	sjme_nvm_rom_swmEntryTag tag;

	/** A CLDC Configuration. */
	sjme_nvm_swmInfoNameVersion configuration;

	/** The internal suite name. */
	sjme_nvm_swmInfoNameOnly internalName;

	/** A CLDC Profile. */
	sjme_nvm_swmInfoNameVersion profile;

	/** A standard. */
	sjme_nvm_swmInfoAll standard;

	/** A suite. */
	sjme_nvm_swmInfoAll suite;

	/** A typed suite. */
	sjme_nvm_swmInfoTypedAll typedSuite;
} sjme_nvm_rom_swmEntryBase;

/** A list of @link sjme_nvm_rom_swmEntry @endlink. */
SJME_LIST_DECLARE(sjme_nvm_rom_swmEntry, 0);

/**
 * Stores information for a single specific library.
 *
 * @since 2026/06/18
 */
typedef struct sjme_nvm_rom_swmLibrary
{
	/** The library this refers to. */
	sjme_nvm_rom_library library;

	/** Dependency and provided entry list. */
	sjme_list(sjme_nvm_rom_swmEntry)* entries;
} sjme_nvm_rom_swmLibrary;

/** A list of @link sjme_nvm_rom_swmLibrary @endlink. */
SJME_LIST_DECLARE(sjme_nvm_rom_swmLibrary, 0);

struct sjme_nvm_rom_swmManagerBase
{
	/** The SWM manager is a bit complicated, so it needs a cleanup helper. */
	sjme_nvm_commonBase common;

	/** The libraries which are known about. */
	sjme_list(sjme_nvm_rom_swmLibrary)* libraries;
};

/**
 * Loads all the MEEP SWM related information from the library suite so that
 * dependency resolution is possible.
 *
 * Note that for other platforms such as DoJa/Star, these will be mapped to
 * MEEP SWM using SquirrelJME specific means and will not be portable. There
 * additionally will be SquirrelJME specific meta-dependencies that are used
 * to assist in classpath resolution.
 *
 * @param allocPool The allocation pool to use.
 * @param inSuite The suite to set up a manager for.
 * @param outSwmManager The resultant SWM manager.
 * @return Any resultant error, if any.
 * @since 2026/06/16
 */
sjme_errorCode sjme_nvm_rom_swmLoad(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_nvm_rom_swmManager* outSwmManager);

/**
 * Looks up the library and all of its dependencies
 *
 * @param swmManager The suite to get the launch parameters from.
 * @param inLibrary The library being resolved.
 * @param outMainClass The main class to launch.
 * @param outMainArgs The arguments to pass to the main class.
 * @param outById The main class path by ID.
 * @param outByName The main class path by name.
 * @return Any resultant error, if any.
 * @since 2026/06/17
 */
sjme_errorCode sjme_nvm_rom_swmResolve(
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H
}
#undef SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ROMMEEPSWM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_ROMMEEPSWM_H */