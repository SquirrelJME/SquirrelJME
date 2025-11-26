/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Common header for JNI and JVM.
 * 
 * @since 2025/10/20
 */

#ifndef SJME_C_SQUIRRELJME_COMMONJNIJVM_H
#define SJME_C_SQUIRRELJME_COMMONJNIJVM_H

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>
#endif

#pragma region(orderedIncludes)
	#include "sjme/jvm/use/useJni.h"
	#include "sjme/jvm/use/useJvm.h"
#pragma endregion(orderedIncludes)

#include "sjme/debug.h"
#include "frontend/libjvm/internals.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_COMMONJNIJVM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_COMMONJNIJVM_H
}
#undef SJME_CXX_SQUIRRELJME_COMMONJNIJVM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_COMMONJNIJVM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_COMMONJNIJVM_H */
