/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JVM Machine Definitions Compatibility header.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_JVM_MD_H
#define SJME_C_SQUIRRELJME_JVM_MD_H

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JVM_MD_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JVM_MD_H
}
#undef SJME_CXX_SQUIRRELJME_JVM_MD_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JVM_MD_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JVM_MD_H */
