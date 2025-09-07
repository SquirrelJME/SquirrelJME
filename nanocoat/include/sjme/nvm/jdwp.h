/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JDWP Packet Support.
 * 
 * @since 2024/02/04
 */

#ifndef SJME_C_JDWP_H
#define SJME_C_JDWP_H

#include "sjme/nvm/nvm.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JDWP_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

struct sjme_jdwpBase
{
	/** The allocation pool to use. */
	sjme_alloc_pool allocPool;

	/** The virtual machine state to access. */
	sjme_nvm inState;
	
	/** The stream to read data from the remote debugger. */
	sjme_stream_input in;

	/** The stream to write data to the remote debugger. */
	sjme_stream_output out;
};

/**
 * Initializes a new JDWP session.
 * 
 * @param allocPool The allocation pool to use.
 * @param inState The state to debug.
 * @param outSession The resultant session.
 * @param in The input from the remote debugger.
 * @param out The output to the remote debugger.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_sessionNew(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInNotNull sjme_stream_input in,
	sjme_attrInNotNull sjme_stream_output out);

#if !defined(SJME_CONFIG_NETWORK_NONE)

/**
 * Initializes a new JDWP session that is connected over a TCP network.
 * 
 * @param allocPool The allocation pool to use.
 * @param inState The state to debug.
 * @param outSession The resultant session.
 * @param listening Is the debugging listening?
 * @param address The address to connect or to bind to.
 * @param port The port to connect to or to bind to.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_sessionNewTcpNetwork(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port);

#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JDWP_H
}
		#undef SJME_CXX_SQUIRRELJME_JDWP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JDWP_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JDWP_H */
