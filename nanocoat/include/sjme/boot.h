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
 * Boots the virtual machine.
 * 
 * @param config The configuration to use.
 * @param outState The output state of the virtual machine.
 * @param argc The number of arguments passed to the executable.
 * @param argv The command line arguments passed to the executable.
 * @return The booted virtual machine.
 * @since 2023/07/27
 */
jboolean sjme_nvm_boot(const sjme_nvm_bootConfig* config,
	sjme_nvm_state** outState, int argc, char** argv);
	
/**
* Destroys the virtual machine.
* 
* @param state The state to destroy.
* @return If destruction was successful.
* @since 2023/07/27
*/
jboolean sjme_nvm_destroy(sjme_nvm_state* state, jint* exitCode);

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
