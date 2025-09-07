/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/util.h"

#if !defined(SJME_CONFIG_HAS_NO_NETWORKING)

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_stream_biOpenTcpUdp(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port)
{
	if (allocPool == NULL || (netIn == NULL && netOut == NULL) ||
		(!listening && address == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (port < 1 || port > 65535)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/*--------------------------------------------------------------------------*/

#endif
