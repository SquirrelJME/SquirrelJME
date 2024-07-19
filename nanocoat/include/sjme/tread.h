/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Generic tread reading/writing functions.
 * 
 * @since 2023/11/16
 */

#ifndef SQUIRRELJME_TREAD_H
#define SQUIRRELJME_TREAD_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TREAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Accessor functions for treads.
 * 
 * @since 2023/11/16
 */
typedef struct sjme_nvm_frameTreadAccessor sjme_nvm_frameTreadAccessor;

/**
 * Returns the address of a given index on the tread.
 * 
 * @param frame The frame the access is in.
 * @param accessor The accessor used for the address.
 * @param tread The tread to read from.
 * @param treadIndex The index to access.
 * @param outAddress The output address.
 * @return Returns an error code, if any.
 * @since 2023/11/16
 */
typedef sjme_errorCode (*sjme_nvm_frameTreadAccessorAddress)(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInNotNull sjme_nvm_frameTread* tread,
	sjme_attrInPositive sjme_jint treadIndex,
	sjme_attrOutNotNull sjme_pointer* outAddress);

/**
 * Returns the tread from the given frame.
 * 
 * @param frame The frame to access the tread for.
 * @param outTread The output tread.
 * @return Returns an error code, if any.
 * @since 2023/11/16
 */
typedef sjme_errorCode (*sjme_nvm_frameTreadAccessorGetTread)(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInOutNotNull sjme_nvm_frameTread** outTread);

/**
 * Reads from a tread.
 * 
 * @param frame The frame the access is in.
 * @param accessor The accessor used.
 * @param tread The tread to read from.
 * @param treadIndex The index to access.
 * @param outVal The output value.
 * @return Returns an error code, if any.
 * @since 2023/11/16
 */
typedef sjme_errorCode (*sjme_nvm_frameTreadAccessorRead)(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInNotNull const sjme_nvm_frameTread* tread,
	sjme_attrInPositive sjme_jint treadIndex,
	sjme_attrOutNotNull sjme_pointer outVal);

/**
 * Reads from a tread.
 * 
 * @param frame The frame the access is in.
 * @param accessor The accessor used.
 * @param tread The tread to read from.
 * @param treadIndex The index to access.
 * @param outVal The output value.
 * @return Returns an error code, if any.
 * @since 2023/11/16
 */
typedef sjme_errorCode (*sjme_nvm_frameTreadAccessorWrite)(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInNotNull sjme_nvm_frameTread* tread,
	sjme_attrInPositive sjme_jint treadIndex,
	sjme_attrInNotNull sjme_cpointer inVal);

struct sjme_nvm_frameTreadAccessor
{
	/** The type id. */
	sjme_javaTypeId typeId;
	
	/** The size of the type. */
	size_t size;
	
	/** The name of the type. */
	sjme_lpcstr name;
	
	/** The error code when the top type does not match. */
	sjme_errorCode errorInvalidTop;
	
	/** Get tread function. */
	sjme_nvm_frameTreadAccessorGetTread getTread;
	
	/** Address function. */
	sjme_nvm_frameTreadAccessorAddress address;
	
	/** Read function. */
	sjme_nvm_frameTreadAccessorRead read;
	
	/** Write function. */
	sjme_nvm_frameTreadAccessorWrite write;
};

/**
 * Returns the accessor for the frame tread.
 * 
 * @param typeId The type ID to get for.
 * @return The accessor for the given tread.
 * @since 2023/11/16
 */
const sjme_nvm_frameTreadAccessor* sjme_nvm_frameTreadAccessorByType(
	sjme_attrInRange(SJME_JAVA_TYPE_ID_INTEGER, SJME_NUM_JAVA_TYPE_IDS = 1)
		sjme_javaTypeId typeId);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TREAD_H
}
		#undef SJME_CXX_SQUIRRELJME_TREAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TREAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TREAD_H */
