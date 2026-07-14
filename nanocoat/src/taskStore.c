/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/taskStore.h"

sjme_errorCode sjme_nvm_store_initFile(
	sjme_attrOutNotNull sjme_nvm_store_file** outFile,
	sjme_attrInNotNull sjme_pointer buf,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	sjme_nvm_store_file* result;
	sjme_pointer baseBuf;

	if (outFile == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (len <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Determine base result, ensure it is aligned. */
	baseBuf = buf;
	result = sjme_util_alignToP(buf, SJME_POINTER_BYTES);

	/* Determine actual length used. */
	len = len - ((sjme_intPointer)result - (sjme_intPointer)buf);

	/* Too small of a buffer to use? */
	if (len <= 0 ||
		len <= (sjme_jint)sizeof(sjme_nvm_store_file) ||
		len <= (sjme_jint)sizeof(sjme_nvm_store_window))
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_store_windowAlloca(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_pointer* rawData,
	sjme_attrInPositiveNonZero sjme_jint numBytes,
	sjme_attrInPositiveNonZero sjme_jint alignment)
{
	if (inWindow == NULL || rawData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numBytes <= 0 || alignment <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_store_windowPop(
	sjme_attrInNotNull sjme_nvm_store_file* inFile)
{
	if (inFile == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_store_windowPush(
	sjme_attrInNotNull sjme_nvm_store_file* inFile,
	sjme_attrOutNotNull sjme_nvm_store_window** outWindow)
{
	if (inFile == NULL || outWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_store_windowSlot(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_jvalue** outValue,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId inType,
	sjme_attrInPositive sjme_jint inSlot)
{
	if (inWindow == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType >= SJME_NUM_JAVA_TYPE_IDS || inSlot < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}