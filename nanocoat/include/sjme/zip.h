/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Zip Support.
 * 
 * @since 2023/12/31
 */

#ifndef SQUIRRELJME_ZIP_H
#define SQUIRRELJME_ZIP_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ZIP_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Opaque ZIP structure.
 *
 * @since 2023/12/31
 */
typedef struct sjme_zipCore* sjme_zip;

/**
 * Zip access information.
 *
 * @since 2023/12/31
 */
typedef struct sjme_zipCore
{
	/** Todo. */
	sjme_jint todo;
} sjme_zipCore;

/**
 * Closes the specified Zip.
 *
 * @param inZip The Zip to close.
 * @return On any resultant error, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_zip_close(
	sjme_attrInNotNull sjme_zip inZip);

/**
 * Opens a Zip file at the given location.
 *
 * @param inPool The pool for structure allocation.
 * @param outZip The resultant opened Zip file.
 * @param rawData Raw data to the Zip in memory somewhere.
 * @param rawSize The length of the Zip data.
 * @return Any resultant error code, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_zip_open(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_zip* outZip,
	sjme_attrInNotNull void* rawData,
	sjme_attrInPositive sjme_jint rawSize);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ZIP_H
}
		#undef SJME_CXX_SQUIRRELJME_ZIP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ZIP_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ZIP_H */
