/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Support for the standard SquirrelJME Container format, these are both
 * used as library and libraries container.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_SQC_H
#define SQUIRRELJME_SQC_H

#include "format/pack.h"
#include "format/library.h"
#include "memchunk.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SQC_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The class version defined on 2020/11/29. */
#define SQC_CLASS_VERSION_20201129 INT16_C(1)

/**
 * State for the SQC.
 * 
 * @since 2021/10/17
 */
typedef struct sjme_sqcState
{
	/** The memory chunk used. */
	const sjme_memChunk* chunk;
	
	/** The class version. */
	sjme_jshort classVersion;
	
	/** The number of properties in the SQC. */
	sjme_jint numProperties;
} sjme_sqcState;

/**
 * This contains the information on the table of contents within an SQC, note
 * that there may be multiple table of contents depending on the items used.
 * 
 * @since 2021/11/07
 */
typedef struct sjme_sqcToc
{
	/** The table of contents chunk, where this chunk is in memory. */
	sjme_memChunk chunk;
	
	/** The number of entries within this TOC. */
	sjme_jint numEntries;
	
	/** The span of the entries within this TOC. */
	sjme_jint span;
} sjme_sqcToc;

/**
 * Holds the state of the pack file accordingly.
 * 
 * @since 2021/11/07
 */
typedef struct sjme_sqcPackState
{
	/** The state of the SQC file. */
	sjme_sqcState sqcState;
	
	/** Table of contents for the various libraries. */
	sjme_sqcToc libToc;
} sjme_sqcPackState;

/**
 * The state of the SQC library. 
 * 
 * @since 2021/11/07
 */
typedef struct sjme_sqcLibraryState
{
	/** The base SQC file state. */
	sjme_sqcState sqcState;
	
	/** The table of contents for the individual entries. */
	sjme_sqcToc entryToc;
} sjme_sqcLibraryState;

/** The SQC driver for multiple libraries. */
extern const sjme_packDriver sjme_packSqcDriver;

/** The SQC driver for a single library. */
extern const sjme_libraryDriver sjme_librarySqcDriver;

/**
 * Destroys the SQC state and anything related to it.
 * 
 * @param sqcInstancePtr The instance to destroy. 
 * @param error Any resultant error.
 * @return If destruction was successful.
 * @since 2021/11/07
 */
sjme_jboolean sjme_sqcDestroy(sjme_sqcState* sqcInstancePtr,
	sjme_error* error);

/**
 * Initializes the SQC instance.
 * 
 * @param formatInstance The format instance.
 * @param sqcState The state pointer for the output.
 * @param error The error state.
 * @return If initializes was successful.
 * @since 2021/11/07 
 */
sjme_jboolean sjme_sqcInit(sjme_formatInstance* formatInstance,
	sjme_sqcState* sqcState, sjme_error* error);

/**
 * Initializes and loads basic information from the table of contents within
 * a SQC.
 * 
 * @param sqcState The state of the SQC.
 * @param outToc The output for the TOC.
 * @param pdxCount The property index for the table of contents count.
 * @param pdxOffset The property index for the table of contents offset.
 * @param pdxSize The property index for the table of contents size.
 * @param error The error state.
 * @return If initialization of the TOC was successful.
 * @since 2021/11/09
 */
sjme_jboolean sjme_sqcInitToc(const sjme_sqcState* sqcState,
	sjme_sqcToc* outToc, sjme_jint pdxCount, sjme_jint pdxOffset,
	sjme_jint pdxSize, sjme_error* error);

/**
 * Gets a property from the SQC.
 * 
 * @param sqcState The SQC to read from.
 * @param index The property index to read from.
 * @param out The read value.
 * @param error The error state.
 * @return If the read was successful.
 * @since 2021/10/29
 */
sjme_jboolean sjme_sqcGetProperty(const sjme_sqcState* sqcState,
	sjme_jint index, sjme_jint* out, sjme_error* error);

/**
 * Reads a value from the given SQC table of contents.
 * 
 * @param sqcToc The SQC to read from.
 * @param outValue The read value.
 * @param rowIndex The major row index to read from.
 * @param itemInSpan The item within the row to read.
 * @param error The error state.
 * @return If reading the value was successful.
 * @since 2021/11/09
 */
sjme_jboolean sjme_sqcTocGet(const sjme_sqcToc* sqcToc, sjme_jint* outValue,
	sjme_jint rowIndex, sjme_jint itemInSpan, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SQC_H
}
#undef SJME_CXX_SQUIRRELJME_SQC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SQC_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SQC_H */
