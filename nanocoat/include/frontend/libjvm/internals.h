/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internals to libjvm.
 * 
 * @since 2025/06/25
 */

#ifndef SJME_C_INTERNALS_H
#define SJME_C_INTERNALS_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_INTERNALS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Accesses the JVM in the reserved space. */
#define SJME_RESERVED_JVM(vmOrEnv) ((vmOrEnv)->reserved0)

/** Accesses the JNI Environment in the reserved space. */
#define SJME_RESERVED_ENV(vmOrEnv) ((vmOrEnv)->reserved1)

/** Accesses the NanoCoat VM Task in the reserved space. */
#define SJME_RESERVED_TASK(vmOrEnv) ((vmOrEnv)->reserved2)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_INTERNALS_H
}
#undef SJME_CXX_INTERNALS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_INTERNALS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_INTERNALS_H */
