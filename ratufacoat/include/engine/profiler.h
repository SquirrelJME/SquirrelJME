/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Contains an implementation of a profiler to output to VisualVM formats, used
 * to test and measure speed of the virtual machine.
 * 
 * @since 2022/03/19
 */

#ifndef SQUIRRELJME_PROFILER_H
#define SQUIRRELJME_PROFILER_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_PROFILER_H
extern "C" {
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This contains the details of the current profiler session and contains
 * the entire snapshot.
 * 
 * @since 2022/03/19
 */
typedef struct sjme_profilerSnapshot sjme_profilerSnapshot;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_PROFILER_H
}
#undef SJME_CXX_SQUIRRELJME_PROFILER_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_PROFILER_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PROFILER_H */
