/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Utilities.
 * 
 * @since 2023/07/26
 */

#ifndef SQUIRRELJME_UTIL_H
#define SQUIRRELJME_UTIL_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_UTIL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Function for returning the number of entries within a tree.
 * 
 * @param in The tree to get the size of.
 * @return The number of items in the tree.
 * @since 2023/07/26
 */
typedef jint (*sjme_treeFindCount)(void* tree);

/**
 * Function for returning the hash of the search item.
 * 
 * @param what What to get the hash of.
 * @return The hash of the given search item.
 * @since 2023/07/26
 */
typedef jint (*sjme_treeFindHash)(void* what);

/**
 * Compares an entry in the tree at the given index with the given hash and
 * item.
 * 
 * @param tree The tree to search in.
 * @param what What to being searched for in the tree.
 * @param hash The hash generated from @c sjme_treeFindHash .
 * @param withIndex Compare @c hash and @c what against the given tree.
 * @return A negative value if lower, zero if equal, or a positive value if
 * greater.
 * @since 2023/07/26
 */
typedef jint (*sjme_treeFindCompare)(void* tree, void* what, jint hash,
	jint withIndex);

/**
 * Tree finding functions, used with @c sjme_treeFind to determine how to
 * search through a given tree.
 * 
 * @since 2023/07/26
 */
typedef struct sjme_treeFindFunc
{
	/** Count function. */
	sjme_treeFindCount count;
	
	/** Hash function. */
	sjme_treeFindHash hash;
	
	/** Compare function. */
	sjme_treeFindCompare compare;
} sjme_treeFindFunc;

/**
 * Hashes the given string in accordance to @c String::hashCode() .
 * 
 * @param string The string to hash.
 * @return The hashcode of the given string.
 * @since 2023/07/26
 */
jint sjme_hashString(const char* string);

/**
 * Locates an item within a tree.
 * 
 * @param tree The tree to search in.
 * @param what What is being searched for.
 * @param functions Functions used for the tree search logic.
 * @return The index where the item was found.
 * @since 2023/07/26
 */
jint sjme_treeFind(void* tree, void* what,
	const sjme_treeFindFunc* functions);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_UTIL_H
}
		#undef SJME_CXX_SQUIRRELJME_UTIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_UTIL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UTIL_H */
