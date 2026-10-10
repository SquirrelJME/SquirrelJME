/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sjme/util.h>

#include "sjme/nvm/jdwp.h"

/** The header size for a packet. */
#define SJME_JDWP_PACKET_HEADER_SIZE 11

sjme_errorCode sjme_jdwp_commReceive(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrOutNotNull sjme_jdwp_packet** outPacket)
{
	sjme_errorCode error;
	sjme_jint ready, readCount;
	sjme_jdwp_packet basePacket;
	sjme_jdwp_packet* result;
	sjme_jbyte rawHeader[SJME_JDWP_PACKET_HEADER_SIZE];
	
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

	/* Read in raw header data. */
	memset(&rawHeader, 0, sizeof(rawHeader));
	readCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadFully(session->in,
		&readCount, rawHeader, SJME_JDWP_PACKET_HEADER_SIZE)) ||
		readCount != SJME_JDWP_PACKET_HEADER_SIZE)
		goto fail_readRawHeader;

	/* Read base packet fields. */
	memset(&basePacket, 0, sizeof(basePacket));
	basePacket.length = sjme_big_uint(
		*sjme_util_memUnaligned32(&rawHeader[0]));
	basePacket.id = sjme_big_uint(
		*sjme_util_memUnaligned32(&rawHeader[4]));
	basePacket.flags = rawHeader[8];

	/* Is this a reply? */
	if (SJME_JDWP_IS_REPLY(&basePacket))
	{
		basePacket.header.reply.error = sjme_big_ushort(
			*sjme_util_memUnaligned16(&rawHeader[9]));
	}

	/* Normal Command. */
	else
	{
		basePacket.header.command.commandSet = rawHeader[9];
		basePacket.header.command.command = rawHeader[10];
	}

	/* Obtain a new packet. */
	result = NULL;
	if (sjme_error_is(error = sjme_jdwp_packetAlloc(session,
		basePacket.length, &result)) || result == NULL)
		goto fail_allocPacket;

	/* Copy base details. */
	memmove(result, &basePacket, sizeof(*result));

	/* Read in remaining data. */
	readCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadFully(session->in,
		&readCount, &result->data[0], basePacket.length)) ||
		readCount != basePacket.length)
		goto fail_readPacket;

	/* Release read lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&session->inLock,
		NULL)))
		return sjme_error_default(error);

	/* Packet is fully read, so return it. */
	*outPacket = result;
	return SJME_ERROR_NONE;

fail_readPacket:
fail_allocPacket:
	/* Make sure packet is discarded. */
	if (result != NULL)
		sjme_jdwp_packetDiscard(session, result);
	
fail_readRawHeader:
	/* Release lock before failing. */
	sjme_thread_spinLockRelease(&session->inLock, NULL);

	/* Failed. */
	return sjme_error_default(error);
}

sjme_errorCode sjme_jdwp_packetAlloc(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull sjme_jdwp_packet** outPacket)
{
	if (session == NULL || outPacket == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_jdwp_packetDiscard(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInNotNull sjme_jdwp_packet* packet)
{
	if (session == NULL || packet == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
