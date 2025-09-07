/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/jdwp.h"

/** The actual JDWP handshake. */
#define SJME_JDWP_HANDSHAKE "JDWP-Handshake"

/** The length of the handshake. */
#define SJME_JDWP_HANDSHAKE_LEN 14

sjme_errorCode sjme_jdwp_sessionNew(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInNotNull sjme_stream_input in,
	sjme_attrInNotNull sjme_stream_output out)
{
	sjme_errorCode error;
	sjme_jbyte handshake[SJME_JDWP_HANDSHAKE_LEN];
	sjme_jint readLen;
	
	if (allocPool == NULL || inState == NULL || outSession == NULL ||
		in == NULL || out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Send the handshake to the remote end. */
	if (sjme_error_is(error = sjme_stream_outputWrite(out,
		SJME_JDWP_HANDSHAKE, SJME_JDWP_HANDSHAKE_LEN)))
		return sjme_error_defaultOr(error, SJME_ERROR_JDWP_BAD_HANDSHAKE);

	/* Flush after write to force it to send. */
	if (sjme_error_is(error = sjme_stream_outputFlush(out)))
		return sjme_error_defaultOr(error, SJME_ERROR_JDWP_BAD_HANDSHAKE);

	/* Read in the remote handshake. */
	memset(handshake, 0, sizeof(handshake));
	readLen = -1;
	if (sjme_error_is(error = sjme_stream_inputReadFully(in,
		&readLen, &handshake[0], SJME_JDWP_HANDSHAKE_LEN)) ||
		readLen != SJME_JDWP_HANDSHAKE_LEN)
		return sjme_error_defaultOr(error, SJME_ERROR_JDWP_BAD_HANDSHAKE);

	/* Must be the exact handshake. */
	if (0 != memcmp(&handshake[0], SJME_JDWP_HANDSHAKE,
		SJME_JDWP_HANDSHAKE_LEN))
		return SJME_ERROR_JDWP_BAD_HANDSHAKE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
