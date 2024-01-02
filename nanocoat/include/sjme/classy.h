/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Handling of classes.
 * 
 * @since 2024/01/01
 */

#ifndef SQUIRRELJME_CLASSY_H
#define SQUIRRELJME_CLASSY_H

#include "nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CLASSY_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Core class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_class_infoCore sjme_class_infoCore;

/**
 * Opaque class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_class_infoCore* sjme_class_info;

struct sjme_class_infoCore
{
	/** Todo. */
	sjme_jint todo;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CLASSY_H
}
		#undef SJME_CXX_SQUIRRELJME_CLASSY_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSY_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CLASSY_H */
