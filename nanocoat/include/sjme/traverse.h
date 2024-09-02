/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Binary bit based traversal tree.
 * 
 * @since 2024/09/01
 */

#ifndef SQUIRRELJME_TRAVERSE_H
#define SQUIRRELJME_TRAVERSE_H

#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"
#include "sjme/error.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BITTRAVERSE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Which type of node is this in the tree?
 * 
 * @since 2024/08/22
 */
typedef enum sjme_traverse_nodeType
{
	/** Unknown. */
	SJME_TRAVERSE_UNKNOWN,
	
	/** Node. */
	SJME_TRAVERSE_NODE,
	
	/** Leaf. */
	SJME_TRAVERSE_LEAF,
} sjme_traverse_nodeType;

/**
 * Traversal tree node.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_align64 sjme_traverse_node
	sjme_traverse_node;

struct sjme_align64 sjme_traverse_node
{
	/** Which type of node is this? */
	sjme_traverse_nodeType type;
	
	/** Node data. */
	sjme_align64 union
	{
		/** Data if a node. */
		sjme_align64 struct
		{
			/** Zero branch. */
			sjme_traverse_node* zero;
			
			/** One branch. */
			sjme_traverse_node* one;
		} node;
		
		/** Data if a leaf. */
		sjme_align64 sjme_jubyte data[sjme_flexibleArrayCountUnion];
	} data;
};

/**
 * Storage for traversal tree nodes.
 * 
 * @since 2024/08/22
 */
typedef struct sjme_traverse_storage
{
	/** The actual size of nodes. */
	sjme_jint nodeSize;
	
	/** Next free node. */
	sjme_traverse_node* next;
	
	/** Final end of tree. */
	sjme_traverse_node* finalEnd;
	
	/** Storage for tree nodes. */
	sjme_align64 sjme_jubyte storage[sjme_flexibleArrayCount];
} sjme_traverse_storage;

/**
 * Traversal tree.
 * 
 * @since 2024/08/20
 */
typedef struct sjme_traverse_base
{
	/** The root node. */
	sjme_traverse_node* root;
	
	/** The storage for the tree. */
	sjme_traverse_storage storage;
} sjme_traverse_base;

/**
 * Base traversal tree binary type.
 * 
 * @since 2024/09/01
 */
typedef sjme_traverse_base* sjme_traverse;

/** The type as a traversal tree. */
#define SJME_AS_TRAVERSE(x) ((sjme_traverse)(x))

/**
 * Tree traversal iterator.
 * 
 * @since 2024/09/01
 */
typedef struct sjme_traverse_iterator
{
	/** Which node are we at. */
	sjme_traverse_node* atNode;
} sjme_traverse_iterator;

/**
 * Determines the basic type name for a traversal tree.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/09/01
 */
#define SJME_TRAVERSE_TYPE_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars))

/**
 * Determines the name of a traversal tree for the given type.
 *
 * @param type The type to store within the tree.
 * @param numPointerStars The number of pointer stars.
 * @return The resultant name.
 * @since 2024/09/01
 */
#define SJME_TRAVERSE_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_traverse_, \
		SJME_TRAVERSE_TYPE_NAME(type, numPointerStars))

/**
 * Declares a traversal tree type.
 * 
 * @param type The type to store within the tree.
 * @param numPointerStars The number of pointer stars.
 * @return The resultant type declaration.
 * @since 2024/09/01
 */
#define SJME_TRAVERSE_DECLARE(type, numPointerStars) \
	typedef sjme_traverse SJME_TRAVERSE_NAME(type, numPointerStars)

SJME_TRAVERSE_DECLARE(sjme_jint, 0);

/**
 * Destroys the traversal tree.
 * 
 * @param traverse The tree to destroy. 
 * @return On any resultant error, if any.
 * @since 2024/09/01
 */
sjme_errorCode sjme_traverse_destroy(
	sjme_attrInNotNull sjme_traverse traverse);

/**
 * Starts iteration of the traversal tree.
 * 
 * @param traverse The tree to iterate.
 * @param iterator The resultant iterator state.
 * @return On any resultant error, if any.
 * @since 2024/09/01
 */
sjme_errorCode sjme_traverse_iterate(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_iterator* iterator);

/**
 * Iterates the next set of values down the tree, reaching a node potentially.
 * 
 * @param traverse The tree to iterate.
 * @param iterator The current iterator state.
 * @param leafValue Pointer to the leaf value, if the node is a leaf then this
 * will be set to the value, otherwise it will be set to null.
 * @param leafLength The length of the leaf value.
 * @param bits The bit values to traverse with.
 * @param numBits The number of bits in the traversal value.
 * @return On any resultant error, if any.
 * @since 2024/09/01
 */
sjme_errorCode sjme_traverse_iterateNextR(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrOutNotNull sjme_traverse_iterator* iterator,
	sjme_attrOutNotNull sjme_pointer* leafValue,
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits);

/**
 * Iterates the next set of values down the tree, reaching a node potentially.
 * 
 * @param traverse The tree to iterate.
 * @param iterator The current iterator state.
 * @param leafValue Pointer to the leaf value, if the node is a leaf then this
 * will be set to the value, otherwise it will be set to null.
 * @param bits The bit values to traverse with.
 * @param numBits The number of bits in the traversal value.
 * @param type The type being stored.
 * @param numPointerStars the pointer star count for the stored type.
 * @return On any resultant error, if any.
 * @since 2024/09/02
 */
#define sjme_traverse_iterateNext(traverse, iterator, leafValue, bits, \
	numBits, type, numPointerStars) \
	(sjme_traverse_iterateNextR(SJME_AS_TRAVERSE((traverse)), (iterator), \
	((sjme_pointer*)(leafValue)), \
	sizeof((*(leafValue))), (bits), (numBits)))

/**
 * Allocates a new traversal tree.
 * 
 * @param inPool The pool to allocate within.
 * @param outTraverse The resultant traversal tree.
 * @param elementSize The size of the tree elements.
 * @param maxElements The maximum number of elements permitted in the tree.
 * @return On any resultant error, if any.
 * @since 2024/09/01
 */
sjme_errorCode sjme_traverse_newR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_traverse* outTraverse,
	sjme_attrInPositiveNonZero sjme_jint elementSize,
	sjme_attrInPositiveNonZero sjme_jint maxElements);

/**
 * Allocates a new traversal tree.
 * 
 * @param inPool The pool to allocate within.
 * @param outTraverse The resultant traversal tree.
 * @param maxElements The maximum number of elements permitted in the tree.
 * @param type The type being stored.
 * @param numPointerStars the pointer star count for the stored type.
 * @return On any resultant error, if any.
 * @since 2024/09/02
 */
#define sjme_traverse_new(inPool, outTraverse, maxElements, \
	type, numPointerStars) \
	(sjme_traverse_newR((inPool), ((sjme_traverse*)(outTraverse)), \
	sizeof(SJME_TOKEN_TYPE(type, numPointerStars)), maxElements))

/**
 * Puts a value into the traversal tree.
 * 
 * @param traverse The traversal tree to write into.
 * @param leafValue The pointer to the leaf's value.
 * @param leafLength The length of the leaf value.
 * @param bits The bit values to get to the leaf.
 * @param numBits The number of bits in the value.
 * @return On any resultant error, if any.
 * @since 2024/09/01
 */
sjme_errorCode sjme_traverse_putR(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrInNotNullBuf(leafLength) sjme_pointer leafValue,
	sjme_attrInPositiveNonZero sjme_jint leafLength,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits);

/**
 * Puts a value into the traversal tree.
 * 
 * @param traverse The traversal tree to write into.
 * @param leafValue The pointer to the leaf's value.
 * @param bits The bit values to get to the leaf.
 * @param numBits The number of bits in the value.
 * @param type The type being stored.
 * @param numPointerStars the pointer star count for the stored type.
 * @return On any resultant error, if any.
 * @since 2024/09/02
 */
#define sjme_traverse_put(traverse, leafValue, bits, numBits, \
	type, numPointerStars) \
	(sjme_traverse_putR((traverse), ((sjme_pointer)(leafValue)), \
	sizeof((*(leafValue))), (bits), (numBits)))

/**
 * Removes the given leaf and/or node from a tree, if this is a node then all
 * children will be removed as well.
 * 
 * @param traverse The tree to remove from.
 * @param bits The bit values to remove.
 * @param numBits The number of bits in the value.
 * @return On any resultant error, if any.
 * @since 2024/09/01
 */
sjme_errorCode sjme_traverse_remove(
	sjme_attrInNotNull sjme_traverse traverse,
	sjme_attrInPositive sjme_juint bits,
	sjme_attrInPositiveNonZero sjme_jint numBits);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_BITTRAVERSE_H
}
		#undef SJME_CXX_SQUIRRELJME_BITTRAVERSE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_BITTRAVERSE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TRAVERSE_H */
