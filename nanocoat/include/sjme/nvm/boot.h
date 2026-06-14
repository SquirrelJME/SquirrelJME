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

#ifndef SJME_C_BOOT_H
#define SJME_C_BOOT_H

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
	
struct sjme_nvm_bootParam
{
	/** The boot suite to use. */
	sjme_nvm_rom_suite bootSuite;

	/** The suite to use for the library set. */
	sjme_nvm_rom_suite librarySuite;

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

	/** Do not start the main task when booting, handle it later. */
	sjme_nvm_bootBelayType belay;

	/** Allow launcher fallback? */
	sjme_jboolean launcherFallback;

	/** The clutter level to use. */
	sjme_nvm_bootClutterLevel clutterLevel;

	/** Do not optimize. */
	sjme_jboolean noOptimize;
};

/**
 * Boots the virtual machine.
 *
 * @param allocPool The main pool to be allocated within.
 * @param param The configuration to use.
 * @param outState The output state of the virtual machine.
 * @param outInitTask The initial task that was created.
 * @return The error code, if any.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_boot(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nvm_bootParam* param,
	sjme_attrOutNotNull sjme_nvm* outState,
	sjme_attrOutNullable sjme_nvm_task* outInitTask);

/**
 * Locates the default boot suite.
 * 
 * @param allocPool The pool for allocations.
 * @param nal The native abstraction layer to use.
 * @param outSuite The resultant suite.
 * @return Any resultant error code, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_nvm_defaultBootSuite(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite);

/**
 * Locates the default boot suite in the given directory.
 * 
 * @param allocPool The pool for allocations.
 * @param nal The native abstraction layer to use.
 * @param inDirectory The directory to look within.
 * @param outSuite The resultant suite.
 * @return Any resultant error code, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_nvm_defaultBootSuiteInDirectory(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInNotNull sjme_lpcstr inDirectory,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite);

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
	sjme_attrOutNotNullBuf(outPathLen) sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen);

/**
 * Destroys the virtual machine.
 * 
 * @param state The state to destroy.
 * @param exitCode The exit code of the virtual machine as a whole, this
 * generally is the exit code of the main task.
 * @return If destruction was successful.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_destroy(
	sjme_attrInNotNull sjme_nvm state,
	sjme_attrOutNullable sjme_jint* exitCode);

/**
 * Parses a standard command line sequence.
 * 
 * @param allocPool The pool to allocate values within.
 * @param nal The native abstraction layer to use.
 * @param outParam The output parameters.
 * @param argc The argument count.
 * @param argv The arguments.
 * @return Any resultant error, if any. Returns @c SJME_ERROR_EXIT if the
 * parsing should just exit.
 * @since 2024/08/08
 */
sjme_errorCode sjme_nvm_parseCommandLine(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
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
