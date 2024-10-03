/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Virtual machine booting.
 * 
 * @since 2023/07/29
 */

#ifndef SQUIRRELJME_BOOT_H
#define SQUIRRELJME_BOOT_H

#include "sjme/nvm/nvm.h"
#include "sjme/alloc.h"
#include "sjme/nvm/rom.h"
#include "sjme/list.h"
#include "sjme/native.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BOOT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The type of default directory used.
 * 
 * This is the same as @c cc.squirreljme.runtime.cldc.full.SystemPathProvider .
 * 
 * @since 2024/08/09
 */
typedef enum sjme_nvm_defaultDirectoryType
{
	/** Unknown. */
	SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN,
	
	/** The cache directory. */
	SJME_NVM_DEFAULT_DIRECTORY_CACHE,
	
	/** The config directory. */
	SJME_NVM_DEFAULT_DIRECTORY_CONFIG,
	
	/** The data directory. */
	SJME_NVM_DEFAULT_DIRECTORY_DATA,
	
	/** The state directory. */
	SJME_NVM_DEFAULT_DIRECTORY_STATE,
	
	/** The number of default directory types. */
	SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPE
} sjme_nvm_defaultDirectoryType;

struct sjme_nvm_bootParam
{
	/** The payload to use for booting the virtual machine. */
	const sjme_payload_config* payload;
	
	/** The boot suite to use. */
	sjme_rom_suite bootSuite;

	/** The suite to use for the library set. */
	sjme_rom_suite librarySuite;

	/** The class path for main by library IDs. */
	const sjme_list_sjme_jint* mainClassPathById;

	/** The class path for main by names. */
	const sjme_list_sjme_lpcstr* mainClassPathByName;

	/** Main class to start in. */
	sjme_lpcstr mainClass;

	/** Main arguments. */
	const sjme_list_sjme_lpcstr* mainArgs;

	/** System properties. */
	const sjme_list_sjme_lpcstr* sysProps;
	
	/** The native abstraction layer to use. */
	const sjme_nal* nal;
};

/**
 * Allocates the reserved pool.
 *
 * @param mainPool The main pool to allocate within.
 * @param outReservedPool The output reserved pool.
 * @return If there is an error or not.
 * @since 2023/12/14
 */
sjme_errorCode sjme_nvm_allocReservedPool(
	sjme_attrInNotNull sjme_alloc_pool* mainPool,
	sjme_attrOutNotNull sjme_alloc_pool** outReservedPool);

/**
 * Boots the virtual machine.
 *
 * @param mainPool The main pool to be allocated within.
 * @param reservedPool An optional reserved pool that can be used, if not
 * specified then one is initialized.
 * @param param The configuration to use.
 * @param outState The output state of the virtual machine.
 * @param argc The number of arguments passed to the executable.
 * @param argv The command line arguments passed to the executable.
 * @return The error code, if any.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_boot(
	sjme_attrInNotNull sjme_alloc_pool* mainPool,
	sjme_attrInNotNull sjme_alloc_pool* reservedPool,
	sjme_attrInNotNull const sjme_nvm_bootParam* param,
	sjme_attrOutNotNull sjme_nvm* outState)
	sjme_attrCheckReturn;

/**
 * Locates the default boot suite.
 * 
 * @param inPool The pool for allocations.
 * @param nal The native abstraction layer to use.
 * @param outSuite The resultant suite.
 * @return Any resultant error code, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_nvm_defaultBootSuite(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrOutNotNull sjme_rom_suite* outSuite);

/**
 * Obtains the default directory for the given type.
 * 
 * This is the same as @c cc.squirreljme.runtime.cldc.full.SystemPathProvider .
 * 
 * @param type The type of directory to get. 
 * @param nal The native abstraction layer to use.
 * @param outPath The path where the directory is written to.
 * @param outPathLen The length of the path.
 * @return Any resultant error, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_nvm_defaultDir(
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrOutNotNull sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen);

/**
 * Destroys the virtual machine.
 * 
 * @param state The state to destroy.
 * @return If destruction was successful.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_destroy(
	sjme_attrInNotNull sjme_nvm state,
	sjme_attrOutNullable sjme_jint* exitCode)
	sjme_attrCheckReturn;

/**
 * Parses a standard command line sequence.
 * 
 * @param inPool The pool to allocate values within.
 * @param nal The native abstraction layer to use.
 * @param param The output parameters. 
 * @param argc The argument count.
 * @param argv The arguments.
 * @return Any resultant error, if any. Returns @c SJME_ERROR_EXIT if the
 * parsing should just exit.
 * @since 2024/08/08
 */
sjme_errorCode sjme_nvm_parseCommandLine(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInOutNotNull sjme_nvm_bootParam* outParam,
	sjme_attrInPositiveNonZero sjme_jint argc,
	sjme_attrInNotNull sjme_lpcstr* argv);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_BOOT_H
}
		#undef SJME_CXX_SQUIRRELJME_BOOT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_BOOT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BOOT_H */
