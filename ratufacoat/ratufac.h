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
#include <errno.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include <signal.h>

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

/* 64-bit system? */
#if __WORDSIZE == 64
	#define RATUFACOAT_64BIT 1

/* 32-bit system? */
#else
	#define RATUFACOAT_32BIT 1
#endif

/**
 * Encoded pointer for different address spaces.
 * 
 * @since 2019/05/31
 */
typedef uint32_t ratufacoat_pointer_t;

/**
 * Native functions support for RatufaCoat.
 * 
 * @since 2019/05/28
 */
typedef struct ratufacoat_native_t
{
} ratufacoat_native_t;

/**
 * Argument handling.
 *
 * @since 2019/05/31
 */
typedef struct ratufacoat_args_t
{
	/** Argument count. */
	int argc;
	
	/** Arguments. */
	char** argv;
} ratufacoat_args_t;

/**
 * Boot options for RatufaCoat.
 *
 * @since 2019/05/31
 */
typedef struct ratufacoat_boot_t
{
	/** Arguments to use. */
	ratufacoat_args_t* args;
	
	/** Native functions. */
	ratufacoat_native_t* native;
	
	/** ROM data. */
	ratufacoat_pointer_t romdata;
	
	/** The size of the ROM. */
	size_t romsize;
} ratufacoat_boot_t;

/**
 * RatufaCoat active machine definition.
 * 
 * @since 2019/05/28
 */
typedef struct ratufacoat_machine_t
{
	/** Native function handlers. */
	ratufacoat_native_t* native;
	
	/** Arguments. */
	ratufacoat_args_t* args;
} ratufacoat_machine_t;

/**
 * Creates a RatufaCoat machine.
 * 
 * @param boot Boot options.
 * @return The resulting virtual machine, will be {@code NULL} if it could
 * not be created.
 * @since 2019/05/28
 */
ratufacoat_machine_t* ratufacoat_createmachine(ratufacoat_boot_t* boot);

/**
 * Fails the VM with a fatal ToDo.
 *
 * @since 2019/05/28
 */
void ratufacoat_todo(void);

/**
 * Allocates a pointer in the encoded address space.
 *
 * @param len The length of the data to allocate.
 * @return The allocated pointer.
 * @since 2019/05/31
 */
ratufacoat_pointer_t ratufacoat_memalloc(size_t len);

/**
 * Frees a pointer in the encoded address space.
 * 
 * @param p The pointer to free.
 * @since 2019/05/31
 */
void ratufacoat_memfree(ratufacoat_pointer_t vp);

/**
 * Returns the real memory pointer for the given encoded pointer.
 * 
 * @param p The source pointer.
 * @return The real address of the pointer.
 * @since 2019/05/31
 */
void* ratufacoat_memrealptr(ratufacoat_pointer_t vp);

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

