/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * RetroArch loop.
 * 
 * @since 2022/01/03
 */

#ifndef SQUIRRELJME_LRLOOP_H
#define SQUIRRELJME_LRLOOP_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LRLOOP_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Destroys the given instance instance.
 * 
 * @param state The state to destroy.
 * @since 2022/01/02
 */
void sjme_libRetro_deinit(sjme_libRetroState* state);

/**
 * Initializes the engine loop settings.
 * 
 * @param config The configuration to set.
 * @return If initialization was a success.
 * @since 2022/01/02 
 */
sjme_jboolean sjme_libRetro_loopConfig(sjme_engineConfig* config);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LRLOOP_H
}
#undef SJME_CXX_SQUIRRELJME_LRLOOP_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LRLOOP_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LRLOOP_H */
