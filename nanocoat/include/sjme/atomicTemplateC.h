/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Atomic template, do not include this directly unless you are wishing to
 * define an atomic type.
 * 
 * @since 2026/10/15
 */

/* ReSharper disable once CppMissingIncludeGuard */
/*--------------------------------------------------------------------------*/

/* Include the atomic template header to grab all the defines for */
/* implementations. */
#define sjme_atomic_sourceImpl 1
#include "sjme/atomicTemplate.h" /* NOLINT */
#undef sjme_atomic_sourceImpl

/* ReSharper disable once CppMissingIncludeGuard */
/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_ATOMIC_DARWIN)
	#include "sjme/templates/atomicDarwinC.h"
#elif defined(SJME_CONFIG_HAS_ATOMIC_WIN32)
	#include "sjme/templates/atomicWin32C.h"
#elif defined(SJME_CONFIG_HAS_ATOMIC_GCC)
	#include "sjme/templates/atomicGccC.h"
#elif defined(SJME_CONFIG_HAS_ATOMIC_GCC_LEGACY)
	#include "sjme/templates/atomicGccLegacyC.h"
#else
	#include "sjme/templates/atomicVolatileC.h"
#endif

/* ReSharper disable once CppMissingIncludeGuard */
/*--------------------------------------------------------------------------*/

/* Undefine everything the header would have done normally. */
#undef sjme_atomic_type
#undef sjme_atomic_refs
#undef sjme_atomic_starsP
#undef sjme_atomic_starsS
#undef sjme_atomic_name
#undef sjme_atomic_derefStars
#undef sjme_atomic_subName
#undef sjme_atomic_typeName
#undef sjme_atomic_valueName
#undef sjme_atomic_typeWidth
#undef sjme_atomic_storageType

#if defined(sjme_atomic_isPointer)
	#undef sjme_atomic_isPointer
#endif
