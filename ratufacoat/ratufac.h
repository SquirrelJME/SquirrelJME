/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * RatufaCoat main library header.
 *
 * @since 2019/01/14
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATRATACHRATUFACH
#define SJME_hGRATUFACOATRATACHRATUFACH

/** Common includes. */
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATRATACHRATUFACH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/**
 * Native functions support for RatufaCoat.
 * 
 * @since 2019/05/28
 */
typedef struct ratufacoat_native_t
{
} ratufacoat_native_t;

/**
 * RatufaCoat active machine definition.
 * 
 * @since 2019/05/28
 */
typedef struct ratufacoat_machine_t
{
	/** Native function handlers. */
	ratufacoat_native_t* native;
	
	/** Argument count. */
	int argc;
	
	/** Arguments. */
	char** argv;
} ratufacoat_machine_t;

/**
 * Searches the argument list for the given argument.
 *
 * @param machine The machine to search in.
 * @param find The argument to find.
 * @param at The starting index to search in, this can be used to possibly
 * scan for all values.
 * @param key The output key pointer, if this was detected to be a key type.
 * @param keyn The number of characters that make up the key.
 * @param value The output value pointer, if this has a value (equal sign).
 * @param valuen The number of characters which make up the value.
 * @return The index of the argument or {@code -1} if it was not found.
 * @since 2019/05/30
 */
int ratufacoat_findargument(ratufacoat_machine_t* machine, char* find, int at,
	char** key, int* keyn, char** value, int* valuen);

/**
 * Creates a RatufaCoat machine.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return The resulting virtual machine, will be {@code NULL} if it could
 * not be created.
 * @since 2019/05/28
 */
ratufacoat_machine_t* ratufacoat_createmachine(ratufacoat_native_t* native,
	int argc, char** argv);

/**
 * Fails the VM with a fatal ToDo.
 *
 * @since 2019/05/28
 */
void ratufacoat_todo(void);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATRATACHRATUFACH
}
#undef SJME_cXRATUFACOATRATACHRATUFACH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATRATACHRATUFACH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATRATACHRATUFACH */

