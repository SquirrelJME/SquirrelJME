/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * CRC Calculation.
 * 
 * @since 2021/11/13
 */

#ifndef SQUIRRELJME_CRC_H
#define SQUIRRELJME_CRC_H

#include "sjmerc.h"
#include "memchunk.h"
#include "stream.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CRC_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The size of the CRC table. */
#define SJME_CRC_TABLE_SIZE SJME_JINT_C(256)

/**
 * This contains the state of the CRC calculation.
 * 
 * @since 2021/11/13
 */
typedef struct sjme_crcState
{
	/** Reflect data? */
	sjme_jboolean reflectData;
	
	/** Reflect the remainder? */
	sjme_jboolean reflectRemainder;
	
	/** The final XOR value. */
	sjme_jint finalXor;
	
	/** The current working remainder. */
	sjme_jint currentRemainder;
	
	/** The CRC data table to be used. */
	const sjme_jint (*crcTable)[SJME_CRC_TABLE_SIZE];
} sjme_crcState;

/** The CRC table for ZIP files and their contents. */
extern const sjme_jint sjme_crcTableZip[SJME_CRC_TABLE_SIZE];

/**
 * Returns the calculated checksum.
 * 
 * @param crcState The state of the CRC calculation.
 * @param outChecksum The calculated checksum.
 * @param error The error state, if any.
 * @return If successfully calculated the checksum or not.
 * @since 2021/11/13
 */
sjme_jboolean sjme_crcChecksum(sjme_crcState* crcState,
	sjme_jint* outChecksum, sjme_error* error);

/**
 * Initializes the CRC calculator with the same properties used for ZIP files.
 * 
 * @param outCrcState The output CRC state.
 * @param error The error state, if any.
 * @return If initialization was a success or not.
 * @since 2021/11/13 
 */
sjme_jboolean sjme_crcInitZip(sjme_crcState* outCrcState, sjme_error* error);

/**
 * Offers data from a memory chunk into the calculator.
 * 
 * @param crcState The CRC state to use. 
 * @param chunk The chunk to read from.
 * @param off The offset into the chunk.
 * @param len The number of bytes to offer.
 * @param error The error state, if any.
 * @return If the data was successfully read for calculation.
 * @since 2021/11/13
 */
sjme_jboolean sjme_crcOfferChunk(sjme_crcState* crcState,
	const sjme_memChunk* chunk, sjme_jint off, sjme_jint len,
	sjme_error* error);
	
/**
 * Offers data from direct memory into the calculator.
 * 
 * @param crcState The CRC state to use. 
 * @param data The data in memory to access.
 * @param len The number of bytes to offer.
 * @param error The error state, if any.
 * @return If the data was successfully read for calculation.
 * @since 2021/11/13
 */
sjme_jboolean sjme_crcOfferDirect(sjme_crcState* crcState,
	void* data, sjme_jint len,
	sjme_error* error);

/**
 * Offers data from a data stream into the calculator.
 * 
 * @param crcState The CRC state to use. 
 * @param stream The stream to read bytes from.
 * @param len The number of bytes to offer.
 * @param error The error state, if any.
 * @return If the data was successfully read for calculation.
 * @since 2021/11/13
 */
sjme_jboolean sjme_crcOfferStream(sjme_crcState* crcState,
	sjme_dataStream* stream, sjme_jint len, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CRC_H
}
#undef SJME_CXX_SQUIRRELJME_CRC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CRC_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CRC_H */
