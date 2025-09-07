/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/jdwp.h"

/** The header size for a packet. */
#define SJME_JDWP_PACKET_HEADER_SIZE 11

sjme_errorCode sjme_jdwp_commReceive(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrOutNotNull sjme_jdwp_packet** outPacket)
{
	sjme_errorCode error;
	sjme_jint ready;
	
	if (session == NULL || outPacket == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Determine the number of available bytes. */
	ready = 0;
	if (sjme_error_is(error = sjme_stream_inputAvailable(session->in,
		&ready)))
		return sjme_error_default(error);

	/* Too small to read a packet? */
	if (ready < SJME_JDWP_PACKET_HEADER_SIZE)
		return SJME_ERROR_NONE;
	
	/* Lock before reading. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&session->inLock)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

	/* Release read lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&session->inLock,
		NULL)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
