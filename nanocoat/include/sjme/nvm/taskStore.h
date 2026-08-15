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

struct sjme_nvm_store_file
{
	/** The total length of the register file. */
	sjme_intPointer totalLength;

	/** The number of bytes which have been used in the file. */
	sjme_intPointer usedData;

	/** The number of bytes which are currently free in the file. */
	sjme_intPointer freeData;

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

	/** The maximum number of stack variables. */
	sjme_jint maxStack;

	/** The maximum number of local variables. */
	sjme_jint maxLocals;

	/**
	 * Variable assignments, this determines which slots a given variable is
	 * assigned too and its length.
	 */
	sjme_jubyte* assignedVars;

	/** The frame this is associated with. */
	sjme_nvm_frame inFrame;
};

struct sjme_nvm_store_window
{
	/** The total length of the register window. */
	sjme_intPointer totalLength;

	/** The number of bytes currently in use for this window. */
	sjme_intPointer usedData;

	/** The number of bytes free for this window. */
	sjme_intPointer freeData;

	/** Language related data for this window, if any. */
	struct
	{
		/** Specifically Java data. */
		sjme_nvm_store_windowJava* java;
	} lang;

	/** The register file which owns this. */
	sjme_nvm_store_file* file;

	/** The previous register window. */
	sjme_nvm_store_window* prev;

	/** The next register window. */
	sjme_nvm_store_window* next;

	/** Raw register window data. */
	sjme_alignPointer sjme_jbyte data[sjme_flexibleArrayCount];
};

/**
 * Access mode flags for variables.
 *
 * @since 2026/08/04
 */
typedef enum sjme_nvm_store_accessMode
{
	/**
	 * Read a variable, failing if it does not exist or is of an incompatible
	 * type that is promoted in size.
	 */
	SJME_NVM_STORE_READ,

	/**
	 * Write a variable without performing promotion, if the type is promoted
	 * then the written value will be adjusted accordingly, otherwise this
	 * will fail if writing the value requires promotion.
	 *
	 * @code write _int_ to _nothing_ -> create _int_ @endcode.
	 * @code write _int_ to _int_ -> replace _int_ @endcode.
	 * @code write _int_ to _long_ -> replace _int_ portion of _long_ @endcode.
	 * @code write _long_ to _int_ -> _fail_ @endcode.
	 */
	SJME_NVM_STORE_WRITE,

	/**
	 * Writes a variable and promotes it in size if it is needed to store
	 * the appropriate type. This does nothing if the type is already large
	 * enough to store the given variable.
	 *
	 * @code write _int_ to _nothing_ -> create _int_ @endcode.
	 * @code write _int_ to _int_ -> replace _int_ @endcode.
	 * @code write _int_ to _long_ -> replace _int_ portion of _long_ @endcode.
	 * @code write _long_ to _int_ -> create _long_ @endcode.
	 */
	SJME_NVM_STORE_WRITE_PROMOTE,

	/**
	 * Replaces an existing value, not creating it nor promoting a value.
	 *
	 * @code write _int_ to _nothing_ -> fail @endcode.
	 * @code write _int_ to _int_ -> replace _int_ @endcode.
	 * @code write _int_ to _long_ -> replace _int_ portion of _long_ @endcode.
	 * @code write _long_ to _int_ -> fail @endcode.
	 */
	SJME_NVM_STORE_REPLACE,

	/**
	 * Replaces an existing value, only promoting a smaller value if needed.
	 *
	 * @code write _int_ to _nothing_ -> fail @endcode.
	 * @code write _int_ to _int_ -> replace _int_ @endcode.
	 * @code write _int_ to _long_ -> replace _int_ portion of _long_ @endcode.
	 * @code write _long_ to _int_ -> create _long_ @endcode.
	 */
	SJME_NVM_STORE_REPLACE_PROMOTE,

	/**
	 * Replaces an existing value, it must be of the same exact size.
	 *
	 * @code write _int_ to _nothing_ -> fail @endcode.
	 * @code write _int_ to _int_ -> replace _int_ @endcode.
	 * @code write _int_ to _long_ -> fail @endcode.
	 * @code write _long_ to _int_ -> fail @endcode.
	 */
	SJME_NVM_STORE_REPLACE_SAME,

	/** The number of access modes. */
	SJME_NVM_STORE_NUM_ACCESS_MODES,
} sjme_nvm_store_accessMode;

/**
 * The type of slot being stored.
 *
 * @since 2026/08/14
 */
typedef enum sjme_nvm_store_slotType
{
	/** Local variable. */
	SJME_NVM_STORE_SLOT_TYPE_LOCAL,

	/** Stack variable. */
	SJME_NVM_STORE_SLOT_TYPE_STACK,

	/** Absolutely referenced variable. */
	SJME_NVM_STORE_SLOT_TYPE_ABSOLUTE,

	/** The number of slot types. */
	SJME_NVM_STORE_NUM_SLOT_TYPES,
} sjme_nvm_store_slotType;

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
 * Obtains the Java language information from the stack window.
 *
 * @param inWindow The window to get the Java information for.
 * @param outJava The resultant Java language information.
 * @param inFrame The frame this is for.
 * @return Any resultant error, if any.
 * @since 2026/07/22
 */
sjme_errorCode sjme_nvm_store_windowLangJava(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_nvm_store_windowJava** outJava,
	sjme_attrInNotNull sjme_nvm_frame inFrame);

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
 * If there is no Java information yet initialized, it will be initialized
 * when this is first called.
 *
 * @param inWindow The window to get the slot for.
 * @param outValue The resultant pointer to a Java value.
 * @param inType The type of data to store in the slot.
 * @param inSlot The slot index.
 * @param inSlotType The type of slot to access.
 * @return Any resultant error, if any.
 * @since 2026/07/13
 */
sjme_errorCode sjme_nvm_store_windowSlot(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_jvalue** outValue,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId inType,
	sjme_attrInPositive sjme_jint inSlot,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_SLOT_TYPES)
		sjme_nvm_store_slotType inSlotType);

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