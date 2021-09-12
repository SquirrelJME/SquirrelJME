/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Support for the standard SquirrelJME Container format, these are both
 * used as library and libraries container.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_SQC_H
#define SQUIRRELJME_SQC_H

#include "format/libraries.h"
#include "format/library.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SQC_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The SQC driver for multiple libraries. */
extern const sjme_libraries_driver sjme_libraries_sqc_driver;

/** The SQC driver for a single library. */
extern const sjme_library_driver sjme_library_sqc_driver;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SQC_H
}
#undef SJME_CXX_SQUIRRELJME_SQC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SQC_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SQC_H */
