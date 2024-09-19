/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * String pool.
 * 
 * @since 2024/09/14
 */

#ifndef SQUIRRELJME_STRINGPOOL_H
#define SQUIRRELJME_STRINGPOOL_H

#include "sjme/charSeq.h"
#include "sjme/list.h"
#include "sjme/nvm/nvm.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STRINGPOOL_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents a pool of strings.
 * 
 * @since 2024/09/14
 */
typedef struct sjme_stringPool_base sjme_stringPool_base;

/**
 * Represents a pool of strings.
 * 
 * @since 2024/09/14
 */
typedef sjme_stringPool_base* sjme_stringPool;

/**
 * Represents a single pooled string.
 * 
 * @since 2024/09/14
 */
typedef struct sjme_stringPool_stringBase sjme_stringPool_stringBase;

/**
 * Represents a single pooled string.
 * 
 * @since 2024/09/14
 */
typedef sjme_stringPool_stringBase* sjme_stringPool_string;

/** A list of string pool strings. */
SJME_LIST_DECLARE(sjme_stringPool_string, 0);

struct sjme_stringPool_base
{
	/** The virtual machine common base. */
	sjme_nvm_commonBase common;
	
	/** Strings which are in the pool. */
	sjme_list_sjme_stringPool_string* strings;
	
	/** The pool this is in. */
	sjme_alloc_pool* inPool;
};

struct sjme_stringPool_stringBase
{
	/** The virtual machine common base. */
	sjme_nvm_common common;
	
	/** The char sequence for this string, if needed. */
	sjme_charSeq seq;
	
	/** The hash code for this string. */
	sjme_jint hashCode;
	
	/** The length of the string. */
	sjme_jint length;
	
	/** The string characters, in UTF form. */
	sjme_jbyte chars[sjme_flexibleArrayCount];
};

/**
 * Locates the given string in the string pool.
 * 
 * @param inStringPool The string pool.
 * @param inSeq The string to locate in the pool. 
 * @param outString The resultant pooled string.
 * @return On any resultant error, if any.
 * @since 2024/09/14
 */
sjme_errorCode sjme_stringPool_locateSeq(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_stringPool_string* outString);

/**
 * Locates the given string in the string pool.
 * 
 * @param inStringPool The string pool.
 * @param inStream The stream to read a UTF encoded string from to locate. 
 * @param outString The resultant pooled string.
 * @return On any resultant error, if any.
 * @since 2024/09/14
 */
sjme_errorCode sjme_stringPool_locateStream(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_stringPool_string* outString);

/**
 * Locates the given string in the string pool.
 * 
 * @param inStringPool The string pool.
 * @param inUtf The string to locate in the pool.
 * @param inUtfLen The length of the string, if @c -1 then this will be
 * the same as @c strlen(inUtf) . 
 * @param outString The resultant pooled string.
 * @return On any resultant error, if any.
 * @since 2024/09/14
 */
sjme_errorCode sjme_stringPool_locateUtf(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_lpcstr inUtf,
	sjme_attrInNegativeOnePositive sjme_jint inUtfLen,
	sjme_attrOutNotNull sjme_stringPool_string* outString);

/**
 * Creates a new string pool for managing constant strings.
 * 
 * @param inPool The pool to allocate within.
 * @param outStringPool The resultant string pool.
 * @return Any resultant error, if any,
 * @since 2024/09/14
 */
sjme_errorCode sjme_stringPool_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stringPool* outStringPool);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STRINGPOOL_H
}
		#undef SJME_CXX_SQUIRRELJME_STRINGPOOL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STRINGPOOL_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STRINGPOOL_H */
