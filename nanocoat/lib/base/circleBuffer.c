/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/circleBuffer.h"
#include "sjme/debug.h"

sjme_errorCode sjme_circleBuffer_available(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outAvailable)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_destroy(
	sjme_attrInNotNull sjme_circleBuffer* buffer)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_get(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNullBuf(outDataLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint outDataLen,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType,
	sjme_attrInPositiveNonZero sjme_jint seekPos)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_new(
	sjme_attrOutNotNull sjme_circleBuffer** outBuffer,
	sjme_attrInValue sjme_circleBuffer_mode inMode,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_pop(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNullBuf(outDataLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_push(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNullBuf(outDataLen) sjme_cpointer inData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_stored(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outStored)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
