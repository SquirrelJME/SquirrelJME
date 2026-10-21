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
 * @file
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

typedef enum sjme_nvm_rom_suiteRunTargetFlags
{
	/** This is an int based enum. */
	sjme_enumInt(sjme_nvm_rom_suiteRunTargetFlags),
	
	/** Free the main class. */
	SJME_NVM_ROM_SUITE_RT_FREE_MAIN_CLASS = 1,
	
	/** Free the main arguments. */
	SJME_NVM_ROM_SUITE_RT_FREE_MAIN_ARGS = 2,
	
	/** Free the system properties. */
	SJME_NVM_ROM_SUITE_RT_FREE_SYS_PROPS = 4,
	
	/** Free the by ID list. */
	SJME_NVM_ROM_SUITE_RT_FREE_BY_ID = 4,
	
	/** Free the by name list. */
	SJME_NVM_ROM_SUITE_RT_FREE_BY_NAME = 8,
} sjme_nvm_rom_suiteRunTargetFlags;
	
struct sjme_nvm_rom_suiteRunTarget
{
	/** Individual run target flags. */
	sjme_nvm_rom_suiteRunTargetFlags flags;
	
	/** The main class to launch. */
	sjme_lpcstr mainClass;

	/** The arguments to pass to the main class. */
	sjme_list(sjme_lpcstr)* mainArgs;

	/** System properties. */
	sjme_list(sjme_lpcstr)* sysProps;

	/** The main class path by ID. */
	sjme_list(sjme_jint)* byId;

	/** The main class path by name. */
	sjme_list(sjme_lpcstr)* byName;
};
	
struct sjme_nvm_bootParam
{
	/** The boot suite to use. */
	sjme_nvm_rom_suite bootSuite;

	/** Should the boot suite be freed? */
	sjme_jboolean freeBootSuite;

	/** The suite to use for the library set. */
	sjme_nvm_rom_suite librarySuite;

	/** Free the library set? */
	sjme_jboolean freeLibrarySuite;

	/** The run target to select. */
	sjme_nvm_rom_suiteRunTarget runTarget;

	/** If @code runTarget @endcode should be freed. */
	sjme_jboolean freeRunTarget;
	
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

	/** Is JDWP listening for a connection? */
	sjme_jboolean jdwpListening;

	/** The address for JDWP. */
	sjme_lpstr jdwpAddress;

	/** The port for JDWP. */
	sjme_jint jdwpPort;
	
	/** Hooks for the state. */
	const sjme_nvm_stateHooks* hooks;

	/** Optional hook data. */
	sjme_pointer hookData;

	/** An extra handle to close on destruction. */
	sjme_closeable extraCloseHandle;

	/** The initial main class to run. */
	sjme_lpcstr runMain;

	/** The initial Jar to run, if specified via @code -jar @endcode. */
	sjme_lpcstr runJar;

	/** The arguments to run for the main class or Jar. */
	sjme_list(sjme_lpcstr)* runArgs;

	/** The entry point to enter. */
	sjme_jint runEntryId;

	/** Disable ScritchUI. */
	sjme_jboolean noScritchUi;

	/** Prefer this ScritchUI interface. */
	sjme_lpcstr preferScritchUi;

	/**
	 * Start argument zero, for platforms that do not use it to store the
	 * name of the executable that is being launched.
	 *
	 * Note that if this is true, then @link sjme_nal_execPathFunc @endlink
	 * will need to be used to determine the executable path if possible.
	 */
	sjme_jboolean startAtArgZero;
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
 * Copies one run target to another, replacing it, optionally making a
 * defensive copy of all values.
 *
 * @param allocPool The allocation pool to use, required if a defensive copy
 * is performed.
 * @param dest The destination.
 * @param src The source.
 * @param checkValid If the source is valid before copying, otherwise if
 * not then @link SJME_ERROR_NOT_MATCHED @endlink is returned.
 * @param defensive If this should be a defensive copy.
 * @return Any resultant error, if any.
 * @since 2026/06/17
 */
sjme_errorCode sjme_nvm_copyRunTarget(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_nvm_rom_suiteRunTarget* dest,
	sjme_attrInNotNull const sjme_nvm_rom_suiteRunTarget* src,
	sjme_attrInValue sjme_jboolean checkValid,
	sjme_attrInValue sjme_jboolean defensive);

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
	sjme_attrInNotNull const sjme_path* inDirectory,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite);

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
 * Parses a standard command line sequence, to fill
 * in @link sjme_nvm_bootParam @endlink with standard Java parameters such
 * as @code -classpath @endcode or @code -Xsomething @endcode.
 * 
 * @param allocPool The pool to allocate values within.
 * @param nal The native abstraction layer to use.
 * @param outParam The output parameters.
 * @param argc The argument count.
 * @param argv The arguments.
 * @return Any resultant error, if any.
 * Returns @link SJME_ERROR_EXIT @endlink if the parsing should just exit.
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
