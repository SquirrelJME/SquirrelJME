/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Dynamic stack frame storage, which is for the storing of variables within
 * a thread frame in a fashion that is equivalent to register windows.
 * Allocation space within a window is only taken when a variable slot is
 * actually used, this is so that methods which exit early or are overly
 * large with complex branches do not cause a large chunk of stack memory to
 * be consumed. Additionally, determining the actual variables which are used
 * and otherwise is a complex optimization process where it needs to be
 * determined for each variable along with various jump states. This does mean
 * that some methods will be optimal while others will not, and if any methods
 * use many variables they will cause more storage to be used if they
 * ultimately end up calling other methods. A new window is initialized when
 * a new frame is entered, which means any old window becomes read-only and
 * cannot be modified. Each window gets layered on top of each other
 * accordingly. Each slot that is taken up will be of a specific size so that
 * it can be reused for types of the same size. If a slot goes from narrow
 * to wide, then it is invalidated. Any narrow slow can store a value in a
 * wide slot. Any slots which ultimately are invalidated or cleared, will
 * remain in their longest historical size. Object wideness matches the
 * width of pointer, thus on 32-bit or less it will be a narrow slot while
 * on 64-bit systems it will be a wide slot. Narrow slots are odd indexed,
 * wide slots are even indexed.
 *
 * @file
 * @since 2026/07/03
 */

#ifndef SJME_C_SQUIRRELJME_TASKSTORE_H
#define SJME_C_SQUIRRELJME_TASKSTORE_H

#include "sjme/config.h"
#include "sjme/nvm/task.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_TASKSTORE_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Stores the full set of windows for an entire thread, this is called the
 * register file.
 *
 * @since 2026/07/12
 */
typedef struct sjme_nvm_store_file sjme_nvm_store_file;

/**
 * A single window for a single frame within a thread, this is a partial
 * segment of a register file.
 *
 * @since 2026/07/12
 */
typedef struct sjme_nvm_store_window sjme_nvm_store_window;

/**
 * Register window information that is useful enough for use as a Java stack.
 *
 * @since 2026/07/13
 */
typedef struct sjme_nvm_store_windowJava sjme_nvm_store_windowJava;

struct sjme_nvm_store_file
{
	/** The total length of the register file. */
	sjme_jint totalLength;

	/** The number of bytes which have been used in the file. */
	sjme_jint usedData;

	/** The buffer base. */
	sjme_pointer bufferBase;

	/** The head register window. */
	sjme_nvm_store_window* head;

	/** The tail register window. */
	sjme_nvm_store_window* tail;

	/** Raw register file data. */
	sjme_alignPointer sjme_jbyte data[sjme_flexibleArrayCount];
};

struct sjme_nvm_store_windowJava
{
	/** The number of variables in this window. */
	sjme_jint numVars;

	/** The variable splice point between locals/stack. */
	sjme_jint varSplice;

	/**
	 * Variable assignments, this determines which slots a given variable is
	 * assigned too and its length.
	 */
	sjme_jubyte* assignedVars;
};

struct sjme_nvm_store_window
{
	/** The total length of the register window. */
	sjme_jint totalLength;

	/** The number of bytes currently in use for this window. */
	sjme_jint usedData;

	/** Language related data for this window, if any. */
	union
	{
		/** Specifically Java data. */
		sjme_nvm_store_windowJava* java;
	} lang;

	/** The register file which owns this. */
	sjme_nvm_store_file* file;

	/** The next register window. */
	sjme_nvm_store_window* next;

	/** Raw register window data. */
	sjme_alignPointer sjme_jbyte data[sjme_flexibleArrayCount];
};

/**
 * Initializes a register file within the given buffer.
 *
 * @param outFile The output register file.
 * @param buf The pointer to the buffer to use.
 * @param len The length of the buffer.
 * @return Any resultant error, if any.
 * @since 2026/07/13
 */
sjme_errorCode sjme_nvm_store_initFile(
	sjme_attrOutNotNull sjme_nvm_store_file** outFile,
	sjme_attrInNotNull sjme_pointer buf,
	sjme_attrInPositiveNonZero sjme_jint len);

/**
 * Allocates raw space within the window.
 *
 * @param inWindow The window to allocate within.
 * @param rawData The raw data of the allocation.
 * @param numBytes The number of bytes to allocate.
 * @param alignment The alignment of the data.
 * @return Any resultant error, if any.
 * @since 2026/07/13
 */
sjme_errorCode sjme_nvm_store_windowAlloca(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_pointer* rawData,
	sjme_attrInPositiveNonZero sjme_jint numBytes,
	sjme_attrInPositiveNonZero sjme_jint alignment);

/**
 * Pops a window from a register file.
 *
 * @param inFile The register file to pop a window from.
 * @return Any resultant error, if any.
 * @since 2026/07/13
 */
sjme_errorCode sjme_nvm_store_windowPop(
	sjme_attrInNotNull sjme_nvm_store_file* inFile);

/**
 * Pushes a new window to the register file.
 *
 * @param inFile The register file to push a window to.
 * @param outWindow The resultant window that was newly pushed.
 * @return Any resultant error, if any.
 * @since 2026/07/13
 */
sjme_errorCode sjme_nvm_store_windowPush(
	sjme_attrInNotNull sjme_nvm_store_file* inFile,
	sjme_attrOutNotNull sjme_nvm_store_window** outWindow);

/**
 * Obtains the slot used for a window and returns a Java compatible value
 * that may be modified accordingly.
 *
 * @param inWindow The window to get the slot for.
 * @param outValue The resultant pointer to a Java value.
 * @param inType The type of data to store in the slot.
 * @param inSlot The slot index.
 * @return Any resultant error, if any.
 * @since 2026/07/13
 */
sjme_errorCode sjme_nvm_store_windowSlot(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_jvalue** outValue,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId inType,
	sjme_attrInPositive sjme_jint inSlot);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_TASKSTORE_H
}
#undef SJME_CXX_SQUIRRELJME_TASKSTORE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_TASKSTORE_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_TASKSTORE_H */