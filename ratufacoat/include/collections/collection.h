/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * This header contains the definitions which are used for collection
 * interfaces and otherwise.
 * 
 * @since 2022/04/03
 */

#ifndef SQUIRRELJME_COLLECTION_H
#define SQUIRRELJME_COLLECTION_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COLLECTION_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This is a collection of items.
 * 
 * @since 2022/04/03
 */
typedef struct sjme_collection sjme_collection;

/**
 * Functions that are used to access collections.
 * 
 * @since 2022/04/03
 */
typedef struct sjme_collectionFunctions
{
	/**
	 * Gets the value of the given index or key.
	 * 
	 * @param collection The collection to access.
	 * @param key The key or index to get from.
	 * @param outValue The value that was within the given key.
	 * @param error Any resultant error state.
	 * @return Will return @c sjme_true if successful, otherwise @c sjme_false.
	 * @since 2022/04/03
	 */
	sjme_jboolean (*get)(sjme_collection* collection, sjme_jint key,
		void** outValue, sjme_error* error);
	
	/**
	 * Sets the given index or key to the given value.
	 * 
	 * @param collection The collection to access.
	 * @param key The key or index to set the value to.
	 * @param value The value to be set.
	 * @param error Any resultant error state.
	 * @return Will return @c sjme_true if successful, otherwise @c sjme_false.
	 * @since 2022/04/03
	 */
	sjme_jboolean (*set)(sjme_collection* collection, sjme_jint key,
		void* value, sjme_error* error);
	
	/**
	 * Obtains the size of the collection.
	 * 
	 * @param collection The collection to access.
	 * @param outSize The output resultant size of the collection.
	 * @param error Any resultant error state.
	 * @return Will return @c sjme_true if successful, otherwise @c sjme_false.
	 * @since 2022/04/03
	 */
	sjme_jboolean (*size)(sjme_collection* collection, sjme_jint* outSize,
		sjme_error* error);
} sjme_collectionFunctions;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COLLECTION_H
}
		#undef SJME_CXX_SQUIRRELJME_COLLECTION_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COLLECTION_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COLLECTION_H */
