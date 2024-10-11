/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Reverse engineered unknown types.
 * 
 * @since 2023/12/09
 */

#ifndef SQUIRRELJME_REVTYPES_H
#define SQUIRRELJME_REVTYPES_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_REVTYPES_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef struct libjvm_re_bool
{
	sjme_jint todo;
} libjvm_re_bool;

typedef struct libjvm_re_ushort
{
	sjme_jint todo;
} libjvm_re_ushort;

typedef struct libjvm_re_int
{
	sjme_jint todo;
} libjvm_re_int;

typedef struct libjvm_re_uint
{
	sjme_jint todo;
} libjvm_re_uint;

typedef struct libjvm_re_long
{
	sjme_jint todo;
} libjvm_re_long;

typedef struct libjvm_re_ulong
{
	sjme_jint todo;
} libjvm_re_ulong;

typedef struct libjvm_re_undefined
{
	sjme_jint todo;
} libjvm_re_undefined;

typedef struct libjvm_re_undefined2
{
	sjme_jint todo;
} libjvm_re_undefined2;

typedef struct libjvm_re_undefined4
{
	sjme_jint todo;
} libjvm_re_undefined4;

typedef struct libjvm_re_undefined8
{
	sjme_jint todo;
} libjvm_re_undefined8;

typedef struct libjvm_re_socklen_t
{
	sjme_jint todo;
} libjvm_re_socklen_t;

typedef struct libjvm_re_sockaddr
{
	sjme_jint todo;
} libjvm_re_sockaddr;

typedef struct libjvm_re_off64_t
{
	sjme_jint todo;
} libjvm_re_off64_t;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_REVTYPES_H
}
		#undef SJME_CXX_SQUIRRELJME_REVTYPES_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_REVTYPES_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_REVTYPES_H */
