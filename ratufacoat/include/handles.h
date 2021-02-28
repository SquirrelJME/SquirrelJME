/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory handle support, used to have potentially loosely bound regions
 * of memory.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_HANDLES_H
#define SQUIRRELJME_HANDLES_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_HANDLES_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Storage for all memory handles. */
typedef struct sjme_memHandles sjme_memHandles;

/** A single memory handle. */
typedef struct sjme_memHandle sjme_memHandle;

typedef enum sjme_memHandleKind
{
	/** Undefined. */
	SJME_MEMHANDLE_KIND_UNDEFINED,
	
	/** Static field storage. */
	SJME_MEMHANDLE_KIND_STATIC_FIELD_DATA,
	
	/** Class information. */
	SJME_MEMHANDLE_KIND_CLASS_INFO,
	
	/** A list of classes. */
	SJME_MEMHANDLE_KIND_CLASS_INFO_LIST,
	
	/** The constant pool. */
	SJME_MEMHANDLE_KIND_POOL,
	
	/** Instance of an object. */
	SJME_MEMHANDLE_KIND_OBJECT_INSTANCE,
	
	/** Virtual method VTable. */
	SJME_MEMHANDLE_KIND_VIRTUAL_VTABLE,
	
	/** Interface method VTable. */
	SJME_MEMHANDLE_KIND_INTERFACE_VTABLE,
	
	/** Boolean array (really byte). */
	SJME_MEMHANDLE_KIND_BOOLEAN_ARRAY,
	
	/** Boolean/byte array. */
	SJME_MEMHANDLE_KIND_BYTE_ARRAY,
	
	/** Short array. */
	SJME_MEMHANDLE_KIND_SHORT_ARRAY,
	
	/** Character array. */
	SJME_MEMHANDLE_KIND_CHARACTER_ARRAY,
	
	/** Integer array. */
	SJME_MEMHANDLE_KIND_INTEGER_ARRAY,
	
	/** Long array. */
	SJME_MEMHANDLE_KIND_LONG_ARRAY,
	
	/** Float array. */
	SJME_MEMHANDLE_KIND_FLOAT_ARRAY,
	
	/** Double array. */
	SJME_MEMHANDLE_KIND_DOUBLE_ARRAY,
	
	/** Object (memory handle) array. */
	SJME_MEMHANDLE_KIND_OBJECT_ARRAY,
	
	/** Static virtual Machine Attributes. */
	SJME_MEMHANDLE_KIND_STATIC_VM_ATTRIBUTES,
	
	/** Quick cast information. */
	SJME_MEMHANDLE_KIND_QUICK_CAST_CHECK,
	
	/** Interface I2XTable. */
	SJME_MEMHANDLE_KIND_I2X_TABLE,
	
	/** Interface XTable. */
	SJME_MEMHANDLE_KIND_INTERFACE_XTABLE,
	
	/** The number of kinds used. */
	SJME_MEMHANDLE_KIND_NUM_KINDS
} sjme_memHandleKind;

/**
 * Initializes the state of memory handles.
 * 
 * @param out The output where handles will go.
 * @param error The error state, if any.
 * @return The failure state.
 * @since 2021/02/27
 */
sjme_returnFail sjme_memHandlesInit(sjme_memHandles** out, sjme_error* error);

/**
 * Destroys and deallocates the state of memory handles.
 * 
 * @param in The handles to destroy.
 * @param error The error state.
 * @return If this failed or not.
 * @since 2021/02/28
 */
sjme_returnFail sjme_memHandlesDestroy(sjme_memHandles* in, sjme_error* error);

/**
 * Initializes a new memory 
 * 
 * @param handles The handle state.
 * @param out The output handle.
 * @param kind The kind of handle to allocate.
 * @param size The size of the handle.
 * @param error The error state.
 * @return If this failed or not.
 * @since 2021/02/28
 */
sjme_returnFail sjme_memHandleNew(sjme_memHandles* handles,
	sjme_memHandle** out, sjme_memHandleKind kind, sjme_jint size,
	sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_HANDLES_H
}
#undef SJME_CXX_SQUIRRELJME_HANDLES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_HANDLES_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_HANDLES_H */
