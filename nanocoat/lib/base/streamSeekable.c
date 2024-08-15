/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"

sjme_errorCode sjme_stream_inputOpenSeekable(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (seekable == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}
