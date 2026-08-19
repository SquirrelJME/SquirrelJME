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

	/* Make sure the file data is cleared first. */
	memset(result, 0, sizeof(*result));

	/* Remember the base of the buffer for freeing. */
	result->bufferBase = baseBuf;

	/* Calculate the actual usable length, this goes from the end of the */
	/* buffer to the actual data start. */
	result->totalLength = ((sjme_intPointer)SJME_POINTER_OFFSET(buf, len) -
		(sjme_intPointer)&result->data[0]);
	result->freeData = result->totalLength;

#if 1
	/* Make sure the data buffer is cleared. */
	memset(&result->data[0], 0, result->totalLength);
#endif

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
	sjme_intPointer placeAt;
	sjme_pointer result;

	if (inWindow == NULL || rawData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numBytes <= 0 || alignment <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Sanity check for any corruption. */
	if (inWindow->totalLength <= 0 || inWindow->usedData < 0 ||
		inWindow->freeData < 0 ||
		inWindow->totalLength - inWindow->usedData != inWindow->freeData)
		return SJME_ERROR_MEMORY_CORRUPTION;

	/* Determine next alignment point. */
	placeAt = sjme_util_alignTo(inWindow->usedData, alignment);

	/* Is there enough free space for this? */
	if (placeAt < 0 ||
		placeAt + numBytes <= 0 || numBytes >= inWindow->freeData ||
		placeAt + numBytes >= inWindow->totalLength)
		return SJME_ERROR_STACK_OVERFLOW;

	/* Grab the next chunk of data. */
	result = &inWindow->data[placeAt];
	inWindow->freeData -= numBytes;
	inWindow->usedData += numBytes;

	/* Wipe it and ensure it is initialized to nothing. */
	memset(result, 0, numBytes);

	/* Success! */
	*rawData = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_store_windowLangJava(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_nvm_store_windowJava** outJava,
	sjme_attrInNotNull sjme_nvm_frame inFrame)
{
	sjme_errorCode error;
	sjme_nvm_store_windowJava* result;
	sjme_jint i, n;

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

	/* Allocate storage needed for the Java language. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_store_windowAlloca(inWindow,
		(sjme_pointer*)&result, sizeof(*result),
		SJME_POINTER_BYTES)) || result == NULL)
		return sjme_error_default(error);

	/* Link in. */
	inWindow->lang.java = result;

	/* Set base frame and associated cached information. */
	result->inFrame = inFrame;
	result->maxLocals = inFrame->inCode->
		perType[SJME_NVM_CODE_INFO_ALL_TYPES].locals;
	result->maxStack = inFrame->inCode->
		perType[SJME_NVM_CODE_INFO_ALL_TYPES].stack;
	result->numVars = result->maxLocals + result->maxStack;

	/* Allocate assigned variable storage. */
	if (sjme_error_is(error = sjme_nvm_store_windowAlloca(inWindow,
		(sjme_pointer)&result->assignedVars,
		sizeof(*result->assignedVars) * result->numVars,
		SJME_POINTER_BYTES)) || result->assignedVars == NULL)
		return sjme_error_default(error);

	/* Fill everything with void. */
	for (i = 0, n = result->numVars; i < n; i++)
		result->assignedVars[i].type = SJME_NVM_STORE_SLOT_FILL_VOID;

	/* Success! */
	*outJava = result;
	return SJME_ERROR_NONE;
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

	/* Make sure the window is cleared to nothing. */
	memset(window, 0, sizeof(*window));

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
	window->totalLength = (inFile->totalLength - windowBase) -
		sizeof(sjme_nvm_store_window);
	window->usedData = 0;
	window->freeData = window->totalLength;

#if 1
	/* Make sure the window data is cleared. */
	memset(&window->data[0], 0, window->totalLength);
#endif

	/* Success! */
	*outWindow = window;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_store_windowSlot(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrInNotNull sjme_nvm_store_windowJava* inJava,
	sjme_attrOutNullable sjme_nvm_value** outStorage,
	sjme_attrOutNullable sjme_javaTypeId* outType,
	sjme_attrInPositive sjme_nvm_store_javaSlot inSlot,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_SLOT_TYPES)
		sjme_nvm_store_slotType inSlotType,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_ACCESS_MODES)
		sjme_nvm_store_accessMode inMode,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS + 1) sjme_javaTypeId inType)
{
	sjme_jint slotLimit, realSlot;

	if (inWindow == NULL || inJava == NULL ||
		(outStorage == NULL && outType == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType > SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inMode < 0 || inMode >= SJME_NVM_STORE_NUM_ACCESS_MODES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* The "do not care" type is only valid for read. */
	if (inMode != SJME_NVM_STORE_READ && inType == SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inSlotType < 0 || inSlotType >= SJME_NVM_STORE_NUM_SLOT_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Gather base information on the slot type. */
	slotLimit = -1;
	realSlot = -1;
	switch (inSlotType)
	{
		case SJME_NVM_STORE_SLOT_TYPE_STACK:
			realSlot = inJava->maxStack - (inSlot + 1);
			slotLimit = inJava->maxStack;
			if (inSlot < 0 || inSlot >= slotLimit)
				return SJME_ERROR_STACK_INDEX_INVALID;
			break;

		case SJME_NVM_STORE_SLOT_TYPE_LOCAL:
			realSlot = inJava->maxStack + inSlot;
			slotLimit = inJava->maxLocals;
			if (inSlot < 0 || inSlot >= slotLimit)
				return SJME_ERROR_LOCAL_INDEX_INVALID;
			break;

		case SJME_NVM_STORE_SLOT_TYPE_ABSOLUTE:
			realSlot = inSlot;
			slotLimit = inJava->numVars;
			if (inSlot < 0 || inSlot >= slotLimit)
				return SJME_ERROR_TREAD_INDEX_INVALID;
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Slot index is not valid? */
	if (realSlot < 0 || realSlot >= inJava->numVars)
		return SJME_ERROR_TREAD_INDEX_INVALID;



	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}
