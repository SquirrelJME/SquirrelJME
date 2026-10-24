/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Class reading utilities.
 * 
 * @file
 * @since 2026/10/23
 */

#ifndef SJME_C_SQUIRRELJME_CLASSYUTIL_H
#define SJME_C_SQUIRRELJME_CLASSYUTIL_H

#include "classy.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CLASSYUTIL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/**
 * Reads a single annotation from the class, which is a recursive operation as
 * it may contain inner annotations.
 * 
 * @param allocPool The allocation pool used.
 * @param inConstPool The constant pool for the class.
 * @param inStringPool The string pool to use for class allocations.
 * @param context The attribute context.
 * @param attrStream The attribute stream.
 * @param outAnnotation The resultant read annotation.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_class_classAnnotation(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrOutNotNull sjme_nvm_class_annotation* outAnnotation);
	
/**
 * Parses a class annotation attribute, which is a recursive operation as
 * it may contain inner annotations.
 * 
 * @param allocPool The allocation pool used.
 * @param inConstPool The class constant pool.
 * @param inStringPool The string pool for string referencing.
 * @param context Context pointer.
 * @param attrName The attribute name.
 * @param attrStream The stream to the attribute.
 * @param attrData The raw attribute data.
 * @param attrLen The raw attribute length.
 * @return Any resultant error, if any.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_class_classAnnotations(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen);
	
/**
 * Parses a single class attribute.
 * 
 * @param allocPool The allocation pool used.
 * @param inStream The stream to read the attribute from.
 * @param inConstPool The class constant pool.
 * @param inStringPool The string pool to use for string references.
 * @param handlers The handlers to look through when parsing an attribute.
 * @param context The context for this attribute.
 * @param attrName The name of this attribute.
 * @param attrLen The length of this attribute.
 * @return Any resultant error, if any.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_class_parseAttribute(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInPositive sjme_jint attrLen);
	
/**
 * Reads a single pool referenced index from the given stream and looks up
 * the constant pool data.
 * 
 * @param inStream The stream to read from.
 * @param inClassPool The class constant pool.
 * @param desireType The desired pool type.
 * @param canNull Can this be @code NULL @endcode?
 * @param outEntry The resultant read entry.
 * @return Any resultant error, if any.
 * @since 2026/10/23
 */
sjme_errorCode sjme_nvm_class_readPoolRefIndex(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inClassPool,
	sjme_attrInPositiveNonZero sjme_nvm_class_poolType desireType,
	sjme_attrInValue sjme_jboolean canNull,
	sjme_attrOutNotNull sjme_nvm_class_poolEntry** outEntry);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CLASSYUTIL_H
}
#undef SJME_CXX_SQUIRRELJME_CLASSYUTIL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSYUTIL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CLASSYUTIL_H */
