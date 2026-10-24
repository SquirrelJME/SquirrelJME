/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * CBOR Data Streams.
 *
 * @url https://www.rfc-editor.org/rfc/rfc8949
 * 
 * @file
 * @since 2025/07/13
 */

#ifndef SJME_C_CBOR_H
#define SJME_C_CBOR_H

#include "sjme/config.h"
#include "sjme/error.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_CBOR_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * CBOR State structure.
 *
 * @since 2025/07/13
 */
typedef struct sjme_cborBase sjme_cborBase;

/**
 * CBOR State structure.
 *
 * @since 2025/07/13
 */
typedef sjme_cborBase* sjme_cbor;

/**
 * The JSON token type.
 *
 * @since 2025/07/13
 */
typedef enum sjme_cbor_tokenType
{
	/** This is an int based enum. */
	sjme_enumInt(sjme_cbor_tokenType),
	
	/** Start of file. */
	SJME_CBOR_FILE_START,

	/** Array closed. */
	SJME_CBOR_ARRAY_CLOSE,

	/** Array comma. */
	SJME_CBOR_ARRAY_COMMA,

	/** Array is open. */
	SJME_CBOR_ARRAY_OPEN,

	/** Array value. */
	SJME_CBOR_ARRAY_VALUE,

	/** Map closed. */
	SJME_CBOR_MAP_CLOSE,

	/** Map comma. */
	SJME_CBOR_MAP_COMMA,

	/** Map colon. */
	SJME_CBOR_MAP_COLON,

	/** Map key. */
	SJME_CBOR_MAP_KEY,

	/** Map is open. */
	SJME_CBOR_MAP_OPEN,

	/** Map value. */
	SJME_CBOR_MAP_VALUE,

	/** Unknown comma. */
	SJME_CBOR_UNKNOWN_COMMA,
	
	/** The number of token types. */
	SJME_CBOR_NUM_TOKEN_TYPES,
} sjme_cbor_tokenType;

struct sjme_cborBase
{
	/** The stream to write data to. */
	sjme_stream_output out;

	/** Is this writing in JSON mode? */
	sjme_jboolean isJson;

	/** The last written token. */
	sjme_cbor_tokenType lastToken;
};

/**
 * Puts in an array close.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow an array
 * open, array value, or map close.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putArrayClose(
	sjme_attrInNotNull sjme_cbor cbor);

/**
 * Puts in an array open.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow file start,
 * array open, array comma, map colon, or an unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putArrayOpen(
	sjme_attrInNotNull sjme_cbor cbor);

/**
 * Puts in a colon.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow a map key.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putColon(
	sjme_attrInNotNull sjme_cbor cbor);

/**
 * Puts in a comma.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does follow a map value,
 * array value, array close, or map close.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putComma(
	sjme_attrInNotNull sjme_cbor cbor);

/**
 * Puts in a key. This will place a comma accordingly if this values an array
 * or map value.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow a map
 * open, map comma, or unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putKey(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey);

/**
 * Closes a map.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow a map open,
 * map value, or array close.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapClose(
	sjme_attrInNotNull sjme_cbor cbor);

/**
 * Puts in a map value that has an array as its value.
 * 
 * This sequence is @code [key, colon, arrayOpen] @endcode.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapEntryA(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey);

/**
 * Puts in a map value.
 * 
 * This sequence is @code [key, colon, value] @endcode.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapEntryI(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInValue sjme_intMax inValue);

/**
 * Puts in a map value.
 * 
 * This sequence is @code [key, colon, value] @endcode.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapEntryJ(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInNotNull sjme_jvalueTyped* inValue);

/**
 * Puts in a map value that has a map as its value.
 * 
 * This sequence is @code [key, colon, mapOpen] @endcode.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapEntryM(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey);

/**
 * Puts in a map value.
 * 
 * This sequence is @code [key, colon, value] @endcode.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapEntryS(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInNotNull sjme_lpcstr inValue);

/**
 * Puts in a map value.
 * 
 * This sequence is @code [key, colon, value] @endcode.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inKey The map key.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapEntryZ(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInValue sjme_jboolean inValue);

/**
 * Puts in a map open. This will auto-place a comma accordingly if this
 * follows an array close or map close.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow file start,
 * array open, array comma, map colon, or unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putMapOpen(
	sjme_attrInNotNull sjme_cbor cbor);

/**
 * Puts in a value only.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow an array
 * open, array comma, map comma, or unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putValueI(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInValue sjme_intMax inValue);

/**
 * Puts in a value only.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow an array
 * open, array comma, map comma, or unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putValueJ(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_jvalueTyped* inValue);

/**
 * Puts in a value only.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow an array
 * open, array comma, map comma, or unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE ...?
 * @throws SJME_ERROR_NULL_ARGUMENTS ...?
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putValueS(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inValue);

/**
 * Puts in a value only.
 * 
 * Include errors from @link sjme_stream_outputWrite() @endlink.
 * 
 * @param cbor The CBOR state.
 * @param inValue The value to write.
 * @return Any resultant error, if any.
 * @throws SJME_ERROR_CBOR_INVALID_PRECEDE If this does not follow an array
 * open, array comma, map comma, or unknown comma.
 * @throws SJME_ERROR_NULL_ARGUMENTS On null arguments.
 * @since 2025/07/13
 */
sjme_errorCode sjme_cbor_putValueZ(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInValue sjme_jboolean inValue);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_CBOR_H
}
#undef SJME_CXX_CBOR_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_CBOR_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_CBOR_H */
