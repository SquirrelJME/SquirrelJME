/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Circular buffer, also known as a round robin.
 * 
 * @since 2024/08/25
 */

#ifndef SQUIRRELJME_CIRCLEBUFFER_H
#define SQUIRRELJME_CIRCLEBUFFER_H

#include "sjme/error.h"
#include "sjme/stdTypes.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CIRCLEBUFFER_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents the mode of how the buffer acts.
 * 
 * @since 2024/08/25
 */
typedef enum sjme_circleBuffer_mode
{
	/** The buffer acts as a queue. */
	SJME_CIRCLE_BUFFER_QUEUE,
	
	/** The buffer acts as a circular window. */
	SJME_CIRCLE_BUFFER_WINDOW,
	
	/** The number of overflow modes. */
	SJME_CIRCLE_BUFFER_NUM_MODES,
} sjme_circleBuffer_mode;

/**
 * Used to refer to the front or the back of a circular buffer.
 * 
 * @since 2024/08/25
 */
typedef enum sjme_circleBuffer_seekEnd
{
	/** The front/head of the buffer. */
	SJME_CIRCLE_BUFFER_HEAD,
	
	/** The back/tail of the buffer. */
	SJME_CIRCLE_BUFFER_TAIL,
	
	/** The number of seek ends. */
	SJME_CIRCLE_BUFFER_NUM_SEEK_END,
	
	/** The front/head of the buffer. */
	SJME_CIRCLE_BUFFER_FIRST = SJME_CIRCLE_BUFFER_HEAD,
	
	/** The back/tail of the buffer. */
	SJME_CIRCLE_BUFFER_LAST = SJME_CIRCLE_BUFFER_TAIL,
	
	/** The front/head of the buffer. */
	SJME_CIRCLE_BUFFER_FRONT = SJME_CIRCLE_BUFFER_HEAD,
	
	/** The back/tail of the buffer. */
	SJME_CIRCLE_BUFFER_BACK = SJME_CIRCLE_BUFFER_TAIL,
} sjme_circleBuffer_seekEnd;

/**
 * Represents a circle buffer and its data.
 * 
 * @since 2024/08/25
 */
typedef struct sjme_circleBuffer
{
	/** The buffer mode. */
	sjme_circleBuffer_mode mode;
	
	/** The size of the buffer. */
	sjme_jint size;
	
	/** The amount of data that is in the buffer. */
	sjme_jint ready;
	
	/** The current read head. */
	sjme_jint readHead;
	
	/** The current write head. */
	sjme_jint writeHead;
	
	/** The buffer storage. */
	sjme_jubyte* buffer;
} sjme_circleBuffer;

/**
 * Returns the number of bytes that are free within the buffer.
 * 
 * @param buffer The buffer to get the empty space of. 
 * @param outAvailable The number of bytes not used in the buffer that
 * are available.
 * @return Any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_available(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outAvailable);

/**
 * Destroys the buffer and frees any associated memory.
 * 
 * @param buffer The buffer to free. 
 * @return Any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_destroy(
	sjme_attrInNotNull sjme_circleBuffer* buffer);

/**
 * Gets data from the buffer, without removing it, from the specified end
 * and position.
 * 
 * @param buffer The buffer to read from. 
 * @param outData The destination buffer.
 * @param length The number of bytes to read.
 * @param seekType Seek from the head or tail?
 * @param seekPos The seek position, relative to @c seekType .
 * @return Any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_get(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNullBuf(length) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType,
	sjme_attrInPositiveNonZero sjme_jint seekPos);

/**
 * Allocates a new circular buffer.
 * 
 * @param inPool The pool to allocate within.
 * @param outBuffer The resultant newly created buffer. 
 * @param inMode The mode that the buffer should be using.
 * @param length The maximum length of the buffer to allocate.
 * @return On any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_circleBuffer** outBuffer,
	sjme_attrInValue sjme_circleBuffer_mode inMode,
	sjme_attrInPositiveNonZero sjme_jint length);

/**
 * Removes data from the buffer at the given end.
 * 
 * @param buffer The buffer to remove from.
 * @param outData The resultant data that was in the buffer.
 * @param length The number of bytes to remove.
 * @param seekType Remove from the head or the tail?
 * @return On any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_pop(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNullBuf(length) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType);

/**
 * Pushes data to the buffer at the given end.
 * 
 * @param buffer The buffer to add to.
 * @param inData The data to be added. 
 * @param length The number of bytes to add.
 * @param seekType Add at the head or the tail?
 * @return On any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_push(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNullBuf(length) sjme_cpointer inData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType);

/**
 * Returns the current amount of data that is stored in the buffer.
 * 
 * @param buffer The buffer to get the current amount of data in.
 * @param outStored The number of bytes stored in the buffer.
 * @return Any resultant error, if any.
 * @since 2024/08/25
 */
sjme_errorCode sjme_circleBuffer_stored(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outStored);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CIRCLEBUFFER_H
}
		#undef SJME_CXX_SQUIRRELJME_CIRCLEBUFFER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CIRCLEBUFFER_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CIRCLEBUFFER_H */
