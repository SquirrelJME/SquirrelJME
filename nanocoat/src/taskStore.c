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

	/* Remember the base of the buffer for freeing. */
	result->bufferBase = baseBuf;

	/* Calculate the actual usable length, this goes from the end of the */
	/* buffer to the actual data start. */
	result->totalLength = ((sjme_intPointer)SJME_POINTER_OFFSET(buf, len) -
		(sjme_intPointer)&result->data[0]);
	result->freeData = result->totalLength;

	/* No other windows exist currently, this is just a blank file until a */
	/* window eventually gets pushed. */
	*outFile = result;
	return SJME_ERROR_NONE;
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

sjme_errorCode sjme_nvm_store_windowLangJava(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_nvm_store_windowJava** outJava,
	sjme_attrInNotNull sjme_nvm_frame inFrame)
{
	sjme_errorCode error;
	sjme_nvm_store_windowJava* result;

	if (inWindow == NULL || outJava == NULL || inFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If this already exists then just use that. */
	result = inWindow->lang.java;
	if (result != NULL)
	{
		/* Wrong frame? */
		if (result->inFrame != inFrame)
			return SJME_ERROR_ILLEGAL_STATE;

		/* Perfectly fine! */
		*outJava = result;
		return SJME_ERROR_NONE;
	}

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
	sjme_errorCode error;
	sjme_intPointer windowBase;
	sjme_nvm_store_window* window;
	sjme_nvm_store_window* oldTail;

	if (inFile == NULL || outWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Sanity check for any corruption. */
	if (inFile->totalLength <= 0 || inFile->usedData < 0 ||
		inFile->freeData < 0 ||
		inFile->totalLength - inFile->usedData != inFile->freeData)
		return SJME_ERROR_MEMORY_CORRUPTION;

	/* Determine the base address for the window. */
	windowBase = sjme_util_alignTo(inFile->usedData,
		SJME_POINTER_BYTES);

	/* Not enough space for a window? */
	if (windowBase >= inFile->totalLength ||
		inFile->freeData <= (sjme_intPointer)sizeof(sjme_nvm_store_window))
		return SJME_ERROR_STACK_OVERFLOW;

	/* Set the new window at the calculated base. */
	window = (sjme_nvm_store_window*)&inFile->data[windowBase];

	/* Add to the tail chain? */
	oldTail = inFile->tail;
	if (oldTail != NULL)
	{
		/* Trim to the new length. */
		oldTail->totalLength =
			(sjme_intPointer)window - (sjme_intPointer)&oldTail->data[0];

		/* This should not happen technically, however in the worst of */
		/* possibilities it may be possible. */
		if (oldTail->totalLength < 0)
			return SJME_ERROR_MEMORY_CORRUPTION;

		/* Link in. */
		oldTail->next = window;
		window->prev = oldTail;
	}

	/* Link in the main head/tail, note that the tail is always replaced. */
	if (inFile->head == NULL)
		inFile->head = window;
	inFile->tail = window;

	/* Setup main window details. */
	window->file = inFile;
	window->totalLength = inFile->totalLength - windowBase;
	window->usedData = 0;

	/* Success! */
	*outWindow = window;
	return SJME_ERROR_NONE;
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