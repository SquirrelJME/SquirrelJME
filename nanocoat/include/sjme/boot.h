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

#include "sjme/nvm.h"
#include "sjme/alloc.h"
#include "sjme/rom.h"
#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BOOT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Special enqueue data that represents this is a state. */
#define SJME_NVM_ENQUEUE_STATE ((sjme_pointer)(0x1234))

struct sjme_nvm_bootParam
{
	/** The payload to use for booting the virtual machine. */
	const sjme_payload_config* payload;

	/** The suite to use for the library set. */
	sjme_rom_suite suite;

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
 * Handler for any weak references which have been enqueued.
 * 
 * @param weak The weak reference.
 * @param data The data for the enqueue.
 * @param isBlockFree Is this a block free or a weak free?
 * @return Any resultant error, if any.
 * @since 2024/08/08
 */
sjme_errorCode sjme_nvm_enqueueHandler(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree);

/**
 * Parses a standard command line sequence.
 * 
 * @param inPool The pool to allocate values wihtin.
 * @param param The output parameters. 
 * @param argc The argument count.
 * @param argv The arguments.
 * @return Any resultant error, if any. Returns @c SJME_ERROR_EXIT if the
 * parsing should just exit.
 * @since 2024/08/08
 */
sjme_errorCode sjme_nvm_parseCommandLine(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
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
