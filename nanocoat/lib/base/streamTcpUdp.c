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
	#include <sys/ioctl.h>
	#include <netdb.h>
	#include <unistd.h>
	#include <fcntl.h>
	#include <poll.h>

	#if defined(SJME_CONFIG_HAS_OS_LINUX) || \
		defined(SJME_CONFIG_HAS_OS_BSD)
		#include <netinet/tcp.h>
	#endif
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
typedef struct sjme_stream_biNetSocketData
{
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	/** The socket descriptor. */
	SOCKET sfd;

	/** The listening file descriptor. */
	SOCKET lfd;

	/** The remote file descriptor. */
	SOCKET rfd;
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	/** The socket file descriptor. */
	int sfd;

	/** The listening file descriptor. */
	int lfd;

	/** The remote file descriptor. */
	int rfd;
#endif
} sjme_stream_biNetSocketData;

static sjme_errorCode sjme_stream_inputNetAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	int rfd, avail;
	struct pollfd fds;
#endif
	
	if (stream == NULL || inImplState == NULL || outAvail == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	/* Recover descriptor, if it is closed then nothing is available. */
	rfd = inImplState->handleTwo.i;
	if (rfd < 0)
	{
		*outAvail = 0;
		return SJME_ERROR_NONE;
	}

	/* The descriptor needs to be polled first. */
	memset(&fds, 0, sizeof(fds));
	fds.fd = rfd;
	fds.events = POLLIN;
	if (poll(&fds, 1, 0) < 1)
	{
		*outAvail = 0;
		return SJME_ERROR_NONE;
	}

	/* Try to read the bytes available. */
	avail = -1;
	if (ioctl(rfd, FIONREAD, &avail) < 0 || avail < 0)
	{
		*outAvail = 0;
		return SJME_ERROR_NONE;
	}

	/* Success! */
	*outAvail = avail;
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_stream_inputNetClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_stream_inputNetInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_biNetSocketData* socketData;

	socketData = (sjme_stream_biNetSocketData*)data;
	if (stream == NULL || inImplState == NULL || socketData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just copy the FDs over. */
	inImplState->handle.i = socketData->sfd;
	inImplState->handleTwo.i = socketData->rfd;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputNetRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	int rfd, rc;
#endif
	
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	/* Recover descriptor, if it is closed always read EOF. */
	rfd = inImplState->handleTwo.i;
	if (rfd < 0)
		return SJME_ERROR_END_OF_FILE;

	/* Read in any data. */
	rc = read(rfd, dest, length);
	if (rc < 0)
	{
		/* This may occur if the socket is non-blocking. */
		if (errno == EAGAIN)
			rc = 0;
		else
			return sjme_nal_errno(errno);
	}

	/* Set read count. */
	*readCount = rc;

	/* Success! */
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static const sjme_stream_inputFunctions sjme_stream_inputNetFunctions =
{
	sjme_sm(.available, sjme_stream_inputNetAvailable),
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
	
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_stream_outputNetFlush(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	int sfd, rfd, opt;
	socklen_t optLen;
#endif
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	/* Recover descriptor, if it is closed then fail. */
	rfd = inImplState->handleTwo.i;
	if (rfd < 0)
		return SJME_ERROR_IO_EXCEPTION;

	/* Sync the data. */
	if (fdatasync(rfd) < 0)
	{
		/* Flushing might not be supported for this. */
		if (errno != EINVAL)
			return sjme_nal_errno(errno);

		/* On Linux and BSD we can force TCP No Delay as a flush, which */
		/* is very hackish. */
#if defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_BSD)
		/* This only works on the core socket. */
		sfd = inImplState->handle.i;
		
		/* Perform the rather hackish flush. */
		opt = 0;
		optLen = sizeof(opt);
		if (getsockopt(sfd, IPPROTO_TCP,
			TCP_NODELAY, (char*)&opt, &optLen) >= 0)
		{
			/* Turn it on. */
			opt = 1;
			setsockopt(sfd, IPPROTO_TCP, TCP_NODELAY,
				(char*)&opt, sizeof(opt));

			/* Then turn it off. */
			opt = 0;
			setsockopt(sfd, IPPROTO_TCP, TCP_NODELAY,
				(char*)&opt, sizeof(opt));
		}
#endif
	}

	/* Success! */
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
}

static sjme_errorCode sjme_stream_outputNetInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_biNetSocketData* socketData;

	socketData = (sjme_stream_biNetSocketData*)data;
	if (stream == NULL || inImplState == NULL || socketData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Just copy the FDs over. */
	inImplState->handle.i = socketData->sfd;
	inImplState->handleTwo.i = socketData->rfd;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputNetWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_buffer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	int rfd;
#endif
	
	if (stream == NULL || inImplState == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	/* Recover descriptor, if it is closed then fail. */
	rfd = inImplState->handleTwo.i;
	if (rfd < 0)
		return SJME_ERROR_IO_EXCEPTION;

	/* Attempt writing. */
	if (write(rfd, buf, length) != length)
		return sjme_nal_errno(errno);

	/* Success! */
	return SJME_ERROR_NONE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
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
	sjme_stream_biNetSocketData data;
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX_2001)
#define PORT_BUF_SIZE 16
	sjme_cchar portBuf[PORT_BUF_SIZE];
	int sfd, lfd, rfd, oldErrno, flags;
	struct addrinfo posixHints;
	struct addrinfo* posixAddress;
	struct addrinfo* tryAddress;
#elif defined(SJME_CONFIG_NETWORK_POSIX_OLD)
	int sfd, lfd, rfd, oldErrno, flags;
	struct hostent* posixHost;
	struct sockaddr_in posixAddress;
#endif
	
	if (allocPool == NULL || (netIn == NULL && netOut == NULL) ||
		(!listening && address == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (port < 1 || port > 65535)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialize blank stream state. */
	memset(&data, 0, sizeof(data));
	error = SJME_ERROR_NONE;
	rawIn = NULL;
	rawOut = NULL;

#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX_2001)
	/* Wipe everything. */
	memset(&posixHints, 0, sizeof(posixHints));
	tryAddress = NULL;
	posixAddress = NULL;
	oldErrno = 0;
	sfd = -1;
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

	/* There can actually be multiple addresses to lookup. */
	for (tryAddress = posixAddress; tryAddress != NULL;
		tryAddress = tryAddress->ai_next)
	{
		/* Open the appropriate socket. */
		sfd = socket(tryAddress->ai_family,
			tryAddress->ai_socktype,
			tryAddress->ai_protocol);
		if (sfd < 0)
			goto fail_socket;
	
		/* Listening for a connection? */
		if (listening)
		{
			/* Bind to address? */
			if (address != NULL)
				if (0 != bind(sfd, tryAddress->ai_addr,
					tryAddress->ai_addrlen))
					goto fail_bind;
			
			/* Listen for a connection. */
			lfd = listen(sfd, 1);
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
			if (connect(sfd, tryAddress->ai_addr,
				tryAddress->ai_addrlen) < 0)
				goto fail_connect;

			/* The remote descriptor is just the socket itself. */
			rfd = sfd;
		}

		/* Success, break out. */
		break;

		/* Failure states. */
fail_socket:
fail_bind:
fail_listen:
fail_accept:
fail_connect:
		/* Will be trashed. */
		oldErrno = errno;

		/* Close remote socket. */
		if (rfd > 0)
		{
			close(rfd);
			rfd = -1;
		}

		/* Close listening socket. */
		if (lfd > 0)
		{
			close(lfd);
			lfd = -1;
		}

		/* Close socket, if it is open. */
		if (sfd > 0)
		{
			close(sfd);
			sfd = -1;
		}
	}

	/* All attempted addresses failed? */
	if (sfd < 0 || rfd < 0)
		goto fail_allAddress;

	/* Set socket info. */
	data.sfd = sfd;
	data.lfd = lfd;
	data.rfd = rfd;
#elif defined(SJME_CONFIG_NETWORK_POSIX_OLD)
	/* Wipe everything. */
	posixHost = NULL;
	memset(&posixAddress, 0, sizeof(posixAddress));
	oldErrno = 0;
	lfd = -1;
	rfd = -1;

	/* Lookup host. */
	if (address != NULL)
	{
		posixHost = gethostbyname(address);
		if (posixHost == NULL)
			goto fail_lookupHost;
	}

	/* Convert address. */
	if (address != NULL)
		memmove(&posixAddress.sin_addr,
			posixHost->h_addr, posixHost->h_length);
	posixAddress.sin_family = AF_INET;
	posixAddress.sin_port = htons(port);
	
	/* Open the appropriate socket. */
	sfd = socket(posixAddress.sin_family,
		(isUdp ? SOCK_DGRAM : SOCK_STREAM), 0);
	if (sfd < 0)
		goto fail_socket;

	/* Listening for a connection? */
	if (listening)
	{
		/* Bind to address? */
		if (address != NULL)
			if (0 != bind(sfd, (struct sockaddr*)&posixAddress,
				sizeof(posixAddress)))
				goto fail_bind;
		
		/* Listen for a connection. */
		lfd = listen(sfd, 1);
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
		if (connect(sfd, (struct sockaddr*)&posixAddress,
			sizeof(posixAddress)) < 0)
			goto fail_connect;

		/* The remote descriptor is just the socket itself. */
		rfd = sfd;
	}
	
	/* Set socket info. */
	data.sfd = sfd;
	data.lfd = lfd;
	data.rfd = rfd;
#else
	#error Unsupported networking?
#endif

#if defined(SJME_CONFIG_NETWORK_POSIX)
	/* Attempt to make the socket non-blocking. */
	flags = fcntl(rfd, F_GETFL, 0);
	if (flags >= 0)
		fcntl(rfd, F_SETFL, flags | O_NONBLOCK);
#endif

	/* Open output stream. */
	if (sjme_error_is(error = sjme_stream_outputOpen(allocPool,
		&rawOut, &sjme_stream_outputNetFunctions,
		&data, NULL)) || rawOut == NULL)
		goto fail_openOut;

	/* Open input stream. */
	if (sjme_error_is(error = sjme_stream_inputOpen(allocPool,
		&rawIn, &sjme_stream_inputNetFunctions,
		&data, NULL)) || rawIn == NULL)
		goto fail_openIn;

	/* Any implementation specific cleanup. */
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX_2001)
	/* We do not need the address info anymore. */
	if (posixAddress != NULL)
	{
		freeaddrinfo(posixAddress);
		posixAddress = NULL;
	}
#elif defined(SJME_CONFIG_NETWORK_POSIX_OLD)
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
			goto fail_closeOut;
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
fail_closeOut:
fail_openIn:
	if (rawIn != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawIn));
fail_openOut:
	if (rawOut != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(rawOut));

	/* Earlier implementation specific failures. */
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
#elif defined(SJME_CONFIG_NETWORK_POSIX_2001)
fail_allAddress:
	/* Delete address info. */
	if (posixAddress != NULL)
	{
		freeaddrinfo(posixAddress);
		posixAddress = NULL;
	}
	
	#undef PORT_BUF_SIZE
#elif defined(SJME_CONFIG_NETWORK_POSIX_OLD)
	/* Failure states. */
fail_socket:
fail_bind:
fail_listen:
fail_accept:
fail_connect:
	/* Will be trashed. */
	if (oldErrno == 0)
		oldErrno = errno;

	/* Close remote socket. */
	if (rfd > 0)
		close(rfd);

	/* Close listening socket. */
	if (lfd > 0)
		close(lfd);

	/* Close socket, if it is open. */
	if (sfd > 0)
		close(sfd);
	
fail_lookupHost:
	if (oldErrno == 0)
		oldErrno = errno;
#endif

	/* Is a standard error set? */
	if (sjme_error_is(error))
		return sjme_error_default(error);

	/* Use errno, if possible. */
#if defined(SJME_CONFIG_NETWORK_WINDOWS)
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#elif defined(SJME_CONFIG_NETWORK_POSIX)
	return sjme_nal_errno(oldErrno);
#endif
}

/*--------------------------------------------------------------------------*/

#endif
