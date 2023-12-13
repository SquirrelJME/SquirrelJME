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
	/** The payload to use for booting the virtual machine. */
	const sjme_static_payload* payload;

	/** Provides the ability to have a virtual suite. */
	sjme_rom_suiteFunctions virtualSuite;
};

/**
 * Boots the virtual machine.
 *
 * @param mainPool The main pool to be allocated within.
 * @param param The configuration to use.
 * @param outState The output state of the virtual machine.
 * @param argc The number of arguments passed to the executable.
 * @param argv The command line arguments passed to the executable.
 * @return The error code, if any.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_boot(
	sjme_attrInNotNull sjme_alloc_pool* mainPool,
	sjme_attrInNotNull const sjme_nvm_bootParam* param,
	sjme_attrOutNotNull sjme_nvm_state** outState,
	sjme_attrInValue int argc,
	sjme_attrInNullable char** argv) sjme_attrCheckReturn;
	
/**
 * Destroys the virtual machine.
 * 
 * @param state The state to destroy.
 * @return If destruction was successful.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_destroy(
	sjme_attrInNotNull sjme_nvm_state* state,
	sjme_attrOutNullable sjme_jint* exitCode)
	sjme_attrCheckReturn;

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
