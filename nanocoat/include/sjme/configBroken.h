/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Potentially broken system and toolchains.
 * 
 * @since 2026/11/08
 */

#ifndef SJME_C_SQUIRRELJME_CONFIGBROKEN_H
#define SJME_C_SQUIRRELJME_CONFIGBROKEN_H

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CONFIGBROKEN_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_OS_SONY_PS2) || \
	defined(SJME_CONFIG_HAS_OS_NINTENDO_GAMECUBE) || \
	defined(SJME_CONFIG_HAS_OS_NINTENDO_WII) || \
	defined(SJME_CONFIG_HAS_OS_NINTENDO_WIIU)
	/** Has broken or no atomics. */
	#define SJME_CONFIG_HAS_BROKEN_ATOMICS
#endif
	
/* DOS: Threading not supported. */
/* Nintendo 3DS: devkitPro has broken/unimplemented pthreads. */
/* Or if proper multithreaded volatiles are not supported. */
#if defined(SJME_CONFIG_HAS_OS_PC_DOS) || \
	defined(SJME_CONFIG_HAS_OS_NINTENDO_3DS) || \
	defined(SJME_CONFIG_HAS_ATOMIC_VOLATILE) || \
	defined(SJME_CONFIG_HAS_BROKEN_ATOMICS)
	/** Has broken or no threading support. */
	#define SJME_CONFIG_HAS_BROKEN_THREADING
#endif
	
#if defined(SJME_CONFIG_HAS_OS_NINTENDO_3DS) || \
	defined(SJME_CONFIG_HAS_OS_NINTENDO_WIIU) || \
	defined(SJME_CONFIG_HAS_OS_NINTENDO_WII)
	/* Has broken or no errno.h. */
	#define SJME_CONFIG_HAS_BROKEN_ERRNO_H
#endif
	
#if defined(SJME_CONFIG_HAS_OS_PALMOS) || \
	defined(SJME_CONFIG_HAS_SDCC)
	/** Has broken or no dynamic library support. */
	#define SJME_CONFIG_HAS_BROKEN_DYLIB
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CONFIGBROKEN_H
}
#undef SJME_CXX_SQUIRRELJME_CONFIGBROKEN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CONFIGBROKEN_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CONFIGBROKEN_H */
