/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Dynamic stack frame storage, which is for the storing of variables within
 * a thread frame in a fashion that is equivalent to register windows.
 * Allocation space within a window is only taken when a variable slot is
 * actually used, this is so that methods which exit early or are overly
 * large with complex branches do not cause a large chunk of stack memory to
 * be consumed. Additionally, determining the actual variables which are used
 * and otherwise is a complex optimization process where it needs to be
 * determined for each variable along with various jump states. This does mean
 * that some methods will be optimal while others will not, and if any methods
 * use many variables they will cause more storage to be used if they
 * ultimately end up calling other methods. A new window is initialized when
 * a new frame is entered, which means any old window becomes read-only and
 * cannot be modified. Each window gets layered on top of each other
 * accordingly. Each slot that is taken up will be of a specific size so that
 * it can be reused for types of the same size. If a slot goes from narrow
 * to wide, then it is invalidated. Any narrow slow can store a value in a
 * wide slot. Any slots which ultimately are invalidated or cleared, will
 * remain in their longest historical size. Object wideness matches the
 * width of pointer, thus on 32-bit or less it will be a narrow slot while
 * on 64-bit systems it will be a wide slot. Narrow slots are odd indexed,
 * wide slots are even indexed.
 *
 * @file
 * @since 2026/07/03
 */

#ifndef SJME_C_SQUIRRELJME_TASKSTORE_H
#define SJME_C_SQUIRRELJME_TASKSTORE_H

#include "sjme/config.h"
#include "sjme/nvm/task.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_TASKSTORE_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_TASKSTORE_H
}
#undef SJME_CXX_SQUIRRELJME_TASKSTORE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_TASKSTORE_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_TASKSTORE_H */