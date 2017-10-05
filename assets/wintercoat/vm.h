/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Virtual machine state.
 *
 * @since 2017/10/05
 */

/** Header guard. */
#ifndef SJME_hGVMH
#define SJME_hGVMH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXVMH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include <inttypes.h>
#include <stdlib.h>
#include <string.h>

/**
 * Errors which may occur.
 */
typedef int sjme_error;

#define SJME_ERROR_OK		0		/** No error. */

/**
 * System property initialization.
 */
typedef struct sjme_init_property
{
	/** The property key. */
	char* key;
	
	/** The property value. */
	char* value;
} sjme_init_property;

/**
 * Initialzation state.
 */
typedef struct sjme_init
{
	/** The number of files in the system classpath. */
	int numclasspath;
	
	/** The files in the system classpath. */
	char** classpath;
	
	/** The number of system properties. */
	int numproperties;
	
	/** The properties to initialize with. */
	sjme_init_property* properties;
} sjme_init;

/**
 * SquirrelJME VM State.
 */
typedef struct sjme_vm
{
} sjme_vm;

sjme_error wc_initvm(sjme_init* initstruct, sjme_vm** outvm);

/** Debugging. */

#define SJME_DEBUG_INIT		1

#define wc_assert(code, i) \
	wc_assert_real(__FILE__, __LINE__, __func__, (code), (i))
#define wc_todo() wc_todo_real(__FILE__, __LINE__, __func__)
#define wc_verbose(mode, msg, ...) wc_verbose_real(__FILE__, __LINE__, \
	__func__, (mode), (msg), __VA_ARGS__)
void wc_assert_real(const char* const pin, int pline, const char* const pfunc,
	const char* const pcode, int pcond);
void wc_todo_real(const char* const pin, int pline, const char* const pfunc);
void wc_verbose_real(const char* const pin, int pline,
	const char* const pfunc, int pmode,
	const char* const pmesg, ...);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXVMH
}
#undef SJME_cXVMH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXVMH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGVMH */

