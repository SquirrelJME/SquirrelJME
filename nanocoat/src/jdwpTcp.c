/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/jdwp.h"

#if !defined(SJME_CONFIG_NETWORK_NONE)

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_jdwp_sessionNewTcpNetwork(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port)
{
	sjme_errorCode error;
	sjme_stream_input netIn;
	sjme_stream_output netOut;
	
	if (allocPool == NULL || outSession == NULL || inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* TODO: If there is no TCP/IP, perhaps fallback to serial? */
	if (inState->nal->tcpUdp == NULL)
		return sjme_error_notImplemented(0);

	/* Open TCP port. */
	netIn = NULL;
	netOut = NULL;
	if (sjme_error_is(error = inState->nal->tcpUdp(allocPool,
		&netIn, &netOut, SJME_JNI_FALSE, listening, address, port)) ||
		netIn == NULL || netOut == NULL)
		return sjme_error_default(error);

	/* Forward. */
	if (sjme_error_is(error = sjme_jdwp_sessionNew(allocPool, inState,
		outSession, netIn, netOut)))
	{
		/* Close the connections before failing. */
		sjme_closeable_close(SJME_AS_CLOSEABLE(netIn));
		sjme_closeable_close(SJME_AS_CLOSEABLE(netOut));
		
		return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

/*--------------------------------------------------------------------------*/

#endif
