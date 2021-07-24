/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Entry point for the virtual machine.
 * 
 * @since 2021/04/02
 */

#ifndef BUILDVM_ENTRY_H
#define BUILDVM_ENTRY_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_BUILDVM_ENTRY_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Main BVM entry point.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return The exit status of the process.
 * @since 2021/07/24
 */
sjme_jboolean bvm_main(int argc, char** argv);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_BUILDVM_ENTRY_H
}
#undef SJME_CXX_BUILDVM_ENTRY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_BUILDVM_ENTRY_H */
#endif /* #ifdef __cplusplus */

#endif /* BUILDVM_ENTRY_H */
