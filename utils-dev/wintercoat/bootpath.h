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
 * Handles the bootstrap classpath.
 *
 * @since 2017/02/05
 */

/** Header guard. */
#ifndef SJME_hGBOOTPATHH
#define SJME_hGBOOTPATHH

#include "wintercoat.h"

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXBOOTPATHH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/**
 * Initializes the bootstrap classpath by initializing it with the given
 * paths.
 *
 * @param paths The paths which are part of the bootstrap class path,
 * terminated by {@code NULL}.
 * @since 2017/02/05
 */
void WC_InitializeBootClassPath(const char** paths);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXBOOTPATHH
}
#undef SJME_cXBOOTPATHH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXBOOTPATHH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGBOOTPATHH */

