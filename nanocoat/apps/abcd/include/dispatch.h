/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Dispatch handlers.
 * 
 * @since 2026/02/22
 */

#ifndef SJME_C_SQUIRRELJME_DISPATCH_H
#define SJME_C_SQUIRRELJME_DISPATCH_H

#include "sjme/config.h"
#include "sjme/native.h"
#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_DISPATCH_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Main function for dispatch.
 * 
 * @param nal The native abstraction layer to use.
 * @param argc Main argument count.
 * @param argv Main arguments passed.
 * @since 2026/02/22
 */
typedef int (*sjme_dispatch_mainFunc)(
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInPositive sjme_jint argc,
	sjme_attrInNotNull const sjme_lpcstr* argv);

/**
 * Prints help.
 * 
 * @since 2026/02/22
 */
typedef void (*sjme_dispatch_helpFunc)(void);

/**
 * Dispatch information.
 * 
 * @since 2026/02/22
 */
typedef struct sjme_dispatch_info
{
	/** The command name. */
	sjme_lpcstr name;
	
	/** The main function. */
	sjme_dispatch_mainFunc main;
	
	/** The help printing function. */
	sjme_dispatch_helpFunc help;
} sjme_dispatch_info;
	
/**
 * The name of the main method for a command.
 * 
 * @param command The command name.
 * @since 2025/08/01
 */
#define sjme_abcd_command_main(command) \
	SJME_TOKEN_PASTE3_PP(sjme_abcd_, command, _main)
	
/**
 * The function declaration for the command's entry point.
 * 
 * @param command The command name.
 * @since 2025/08/01
 */
#define sjme_abcd_command_main_declare(command) \
	int sjme_abcd_command_main(command)(const sjme_nal* nal, \
		sjme_jint argc, const sjme_lpcstr* argv)
	
/**
 * The name of the help method for a command.
 * 
 * @param command The command name.
 * @since 2025/08/01
 */
#define sjme_abcd_command_help(command) \
	SJME_TOKEN_PASTE3_PP(sjme_abcd_, command, _help)
	
/**
 * The function declaration for the command's help.
 * 
 * @param command The command name.
 * @since 2025/08/01
 */
#define sjme_abcd_command_help_declare(command) \
	void sjme_abcd_command_help(command)(void)

/**
 * Declares the prototypes for a command.
 * 
 * @param command The command name.
 * @since 2025/08/01
 */
#define sjme_abcd_command_declare(command) \
	sjme_abcd_command_main_declare(command); \
	sjme_abcd_command_help_declare(command)
	
#pragma region(commands)
	
sjme_abcd_command_declare(ant);
sjme_abcd_command_declare(cat);
sjme_abcd_command_declare(device_address);
sjme_abcd_command_declare(device_manager);
sjme_abcd_command_declare(doja);
sjme_abcd_command_declare(doja_g);
sjme_abcd_command_declare(eclipse);
sjme_abcd_command_declare(emulator);
sjme_abcd_command_declare(iconv);
sjme_abcd_command_declare(jadtool);
sjme_abcd_command_declare(jar);
sjme_abcd_command_declare(jar2prc);
sjme_abcd_command_declare(jasmin);
sjme_abcd_command_declare(javac);
sjme_abcd_command_declare(javadoc);
sjme_abcd_command_declare(javap);
sjme_abcd_command_declare(jdb);
sjme_abcd_command_declare(mekeytool);
sjme_abcd_command_declare(midp);
sjme_abcd_command_declare(netbeans);
sjme_abcd_command_declare(preverify);
sjme_abcd_command_declare(star);
sjme_abcd_command_declare(touch);
sjme_abcd_command_declare(wscompile);
sjme_abcd_command_declare(zip);
	
#pragma endregion(commands)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_DISPATCH_H
}
#undef SJME_CXX_SQUIRRELJME_DISPATCH_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_DISPATCH_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_DISPATCH_H */
