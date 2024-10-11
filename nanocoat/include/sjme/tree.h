/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Support for binary trees.
 *
 * The algorithm is derived from Robert Sedgewick's (of Princeton University)
 * 2008 variant of Red-Black Trees called Left Leaning Red-Black Trees.
 * 
 * @since 2024/01/03
 */

#ifndef SQUIRRELJME_TREE_H
#define SQUIRRELJME_TREE_H

#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"
#include "sjme/comparator.h"
#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TREE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Determines the basic type name for a tree.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2023/12/17
 */
#define SJME_TREE_TYPE_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars))

/**
 * Determines the name of a tree for the given type.
 *
 * @param type The type to store within the tree.
 * @param numPointerStars The number of pointer stars.
 * @return The resultant name.
 * @since 2024/01/03
 */
#define SJME_TREE_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_tree_, \
		SJME_TREE_TYPE_NAME(type, numPointerStars))

/**
 * Determines the name of a tree node for the given type.
 *
 * @param type The type to store within the tree.
 * @param numPointerStars The number of pointer stars.
 * @return The resultant name.
 * @since 2024/01/03
 */
#define SJME_TREE_NODE_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_tree_node_, \
		SJME_TREE_TYPE_NAME(type, numPointerStars))

/**
 * The combine value of a map key and value set.
 *
 * @param keyType The key type.
 * @param keyNumPointerStars The pointer stars for the key.
 * @param valueType The value type.
 * @param valueNumPointerStars The pointer stars for the value.
 * @return The resultant name.
 * @since 2024/01/03
 */
#define SJME_TREE_MAP_TYPE_NAME(keyType, keyNumPointerStars, \
	valueType, valueNumPointerStars) \
	SJME_TOKEN_PASTE3_PP(SJME_TREE_TYPE_NAME(keyType, keyNumPointerStars), \
	__, SJME_TREE_TYPE_NAME(valueType, valueNumPointerStars))

/**
 * Determines the name of a map for the given type.
 *
 * @param keyType The key type.
 * @param keyNumPointerStars The pointer stars for the key.
 * @param valueType The value type.
 * @param valueNumPointerStars The pointer stars for the value.
 * @return The resultant name.
 * @since 2024/01/03
 */
#define SJME_TREE_MAP_NAME(keyType, keyNumPointerStars, \
	valueType, valueNumPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_tree_map_, \
		SJME_TREE_MAP_TYPE_NAME(keyType, keyNumPointerStars, \
		valueType, valueNumPointerStars))

/**
 * Determines the name of a map node for the given type.
 *
 * @param keyType The key type.
 * @param keyNumPointerStars The pointer stars for the key.
 * @param valueType The value type.
 * @param valueNumPointerStars The pointer stars for the value.
 * @return The resultant name.
 * @since 2024/01/03
 */
#define SJME_TREE_MAP_NODE_NAME(keyType, keyNumPointerStars, \
	valueType, valueNumPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_tree_map_node_, \
		SJME_TREE_MAP_TYPE_NAME(keyType, keyNumPointerStars, \
		valueType, valueNumPointerStars))

/**
 * Determines the name of a map entry for the given type.
 *
 * @param keyType The key type.
 * @param keyNumPointerStars The pointer stars for the key.
 * @param valueType The value type.
 * @param valueNumPointerStars The pointer stars for the value.
 * @return The resultant name.
 * @since 2024/01/03
 */
#define SJME_TREE_MAP_ENTRY_NAME(keyType, keyNumPointerStars, \
	valueType, valueNumPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_tree_map_entry_, \
		SJME_TREE_MAP_TYPE_NAME(keyType, keyNumPointerStars, \
		valueType, valueNumPointerStars))

/**
 * Declares a tree with its base type and nodes.
 *
 * @param rawTreeName The raw tree name to use.
 * @param rawNodeName The raw node name to use.
 * @param rawElementName The raw element name to use.
 * @param type The type to store within the tree.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/03
 */
#define SJME_TREE_DECLARE_RAW(rawTreeName, rawNodeName, rawElementName, \
	type, numPointerStars) \
	/** Binary tree node for @c type and @c numPointerStars . */ \
	typedef struct rawNodeName rawNodeName; \
	\
	struct rawNodeName \
	{ \
		/** Is this red? Or black? */ \
		sjme_jboolean red : 1; \
	\
		/** The node to the left. */ \
		rawNodeName* left; \
	\
		/** The node to the right. */ \
		rawNodeName* right; \
	\
		/** The entry within the tree. */ \
		rawElementName entry; \
	}; \
	\
	/** Binary tree for @c type and @c numPointerStars . */ \
	typedef struct rawTreeName \
	{ \
		/** The root node. */ \
		rawNodeName* root; \
	\
		/** The size of individual elements within the tree. */\
		sjme_jint elementSize;\
	} rawTreeName

/**
 * Declares a tree with its base type and nodes.
 *
 * @param type The type to store within the tree.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/03
 */
#define SJME_TREE_DECLARE(type, numPointerStars) \
	SJME_TREE_DECLARE_RAW(SJME_TREE_NAME(type, numPointerStars), \
		SJME_TREE_NODE_NAME(type, numPointerStars), \
        SJME_TOKEN_TYPE(type, numPointerStars), \
		type, numPointerStars)

/**
 * Declares a tree map using the given keys and values.
 *
 * @param keyType The key type.
 * @param keyNumPointerStars The pointer stars for the key.
 * @param valueType The value type.
 * @param valueNumPointerStars The pointer stars for the value.
 * @since 2024/01/03
 */
#define SJME_TREE_MAP_DECLARE(keyType, keyNumPointerStars, \
	valueType, valueNumPointerStars) \
	typedef struct SJME_TREE_MAP_ENTRY_NAME(keyType, keyNumPointerStars, \
		valueType, valueNumPointerStars) \
	{ \
		/** The key type. */ \
		SJME_TOKEN_TYPE(keyType, keyNumPointerStars) key; \
	\
		/** The value type. */ \
		SJME_TOKEN_TYPE(valueType, valueNumPointerStars) value; \
	} SJME_TREE_MAP_ENTRY_NAME(keyType, keyNumPointerStars, \
		valueType, valueNumPointerStars); \
	\
	SJME_TREE_DECLARE_RAW(SJME_TREE_MAP_NAME(keyType, keyNumPointerStars, \
			valueType, valueNumPointerStars), \
		SJME_TREE_MAP_NODE_NAME(keyType, keyNumPointerStars, \
			valueType, valueNumPointerStars), \
		SJME_TREE_MAP_ENTRY_NAME(keyType, keyNumPointerStars, \
			valueType, valueNumPointerStars), \
		type, numPointerStars)

/** A tree of @c sjme_jint . */
SJME_TREE_DECLARE(sjme_jint, 0);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TREE_H
}
		#undef SJME_CXX_SQUIRRELJME_TREE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TREE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TREE_H */
