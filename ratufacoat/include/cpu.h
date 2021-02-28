/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * CPU Support.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_CPU_H
#define SQUIRRELJME_CPU_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CPU_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This contains the state of a single CPU frame.
 *
 * @since 2019/06/27
 */
typedef struct sjme_cpuframe sjme_cpuframe;

/** Virtual CPU. */
typedef struct sjme_cpu sjme_cpu;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CPU_H
}
#undef SJME_CXX_SQUIRRELJME_CPU_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CPU_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CPU_H */
