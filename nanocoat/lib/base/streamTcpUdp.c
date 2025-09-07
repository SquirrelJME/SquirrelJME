/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if !defined(SJME_CONFIG_NETWORK_NONE)

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	#include <winsock2.h>
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	#include <sys/socket.h>
	#include <sys/types.h>
	#include <netdb.h>
	#include <unistd.h>
#else
	#error Unsupported networking?
#endif

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/util.h"

/**
 * Network socket data.
 *
 * @since 2025/09/07
 */
typedef struct sjme_stream_biNetSocket
{
	int todo;
} sjme_stream_biNetSocket;

static sjme_errorCode sjme_stream_inputNetClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputNetInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputNetRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_inputFunctions sjme_stream_inputNetFunctions =
{
	sjme_sm(.available, NULL),
	sjme_sm(.close, sjme_stream_inputNetClose),
	sjme_sm(.init, sjme_stream_inputNetInit),
	sjme_sm(.read, sjme_stream_inputNetRead),
};

static sjme_errorCode sjme_stream_outputNetClose(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetFlush(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_outputNetWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_buffer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (stream == NULL || inImplState == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_stream_outputFunctions sjme_stream_outputNetFunctions =
{
	sjme_sm(.close, sjme_stream_outputNetClose),
	sjme_sm(.flush, sjme_stream_outputNetFlush),
	sjme_sm(.init, sjme_stream_outputNetInit),
	sjme_sm(.write, sjme_stream_outputNetWrite),
};

sjme_errorCode sjme_stream_biOpenTcpUdp(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port)
{
	sjme_errorCode error;
	sjme_stream_input rawIn;
	sjme_stream_output rawOut;
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX)
#define PORT_BUF_SIZE 16
	sjme_cchar portBuf[PORT_BUF_SIZE];
	struct addrinfo posixHints;
	struct addrinfo* posixAddress;
	int fd, lfd, rfd, oldErrno;
#else
	#error Unsupported networking?
#endif
	
	if (allocPool == NULL || (netIn == NULL && netOut == NULL) ||
		(!listening && address == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (port < 1 || port > 65535)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialize blank stream state. */
	rawIn = NULL;
	rawOut = NULL;

#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	/* Wipe everything. */
	memset(&posixHints, 0, sizeof(posixHints));
	memset(&posixAddress, 0, sizeof(posixAddress));
	lfd = -1;
	rfd = -1;

	/* Determine bind/connect address hints, if any */
	posixHints.ai_family = AF_UNSPEC;
	posixHints.ai_socktype = (isUdp ? SOCK_DGRAM : SOCK_STREAM);
	posixHints.ai_flags = (listening && address == NULL ? AI_PASSIVE : AI_ALL);

	/* Convert port to string. */
	memset(portBuf, 0, sizeof(portBuf));
	snprintf(portBuf, PORT_BUF_SIZE - 1, "%d", port);

	/* Lookup address. */
	if (0 != getaddrinfo(address, portBuf,
		&posixHints, &posixAddress))
		return sjme_nal_errno(errno);

	/* Open the appropriate socket. */
	fd = socket(posixAddress->ai_family,
		posixAddress->ai_socktype,
		posixAddress->ai_protocol);
	if (fd < 0)
		goto fail_socket;

	/* Listening for a connection? */
	if (listening)
	{
		/* Bind to address? */
		if (address != NULL)
			if (0 != bind(fd, posixAddress->ai_addr, posixAddress->ai_addrlen))
				goto fail_bind;
			
		/* Listen for a connection. */
		lfd = listen(fd, port);
		if (lfd < 0)
			goto fail_listen;

		/* Accept the next incoming connection. */
		rfd = accept(lfd, NULL, 0);
		if (rfd < 0)
			goto fail_accept;
	}

	/* Connecting instead. */
	else
	{
		/* Connect to the remote system. */
		rfd = connect(fd, posixAddress->ai_addr, posixAddress->ai_addrlen);
		if (rfd < 0)
			goto fail_connect;
	}
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#else
	#error Unsupported networking?
#endif

	/* Do we not care about the input stream? Close it! */
	if (rawIn != NULL && netIn == NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(rawIn))))
			goto fail_closeIn;
		rawIn = NULL;
	}

	/* Ditto for the output stream if we do not care for it. */
	if (rawOut != NULL && netOut == NULL)
	{
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(rawOut))))
			goto fail_closeIn;
		rawOut = NULL;
	}

	/* Return resultant raw streams */
	if (netIn != NULL)
		*netIn = rawIn;
	if (netOut != NULL)
		*netOut = rawOut;

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_closeIn:
	if (rawIn != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawIn));
	if (rawOut != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawOut));
	return sjme_error_default(error);
	
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX)
fail_socket:
fail_bind:
fail_listen:
fail_accept:
fail_connect:
	/* Will be trashed. */
	oldErrno = errno;

	/* Delete address info. */
	freeaddrinfo(posixAddress);

	/* Close remote socket. */
	if (rfd > 0)
		close(rfd);

	/* Close listening socket. */
	if (lfd > 0)
		close(lfd);

	/* Close socket, if it is open. */
	if (fd > 0)
		close(fd);

	/* Return the creation failure. */
	return sjme_nal_errno(oldErrno);
	#undef PORT_BUF_SIZE
#endif
}

/*--------------------------------------------------------------------------*/

#endif
