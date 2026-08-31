/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/taskStore.h"

static sjme_errorCode sjme_nvm_store_fileAlloca(
	sjme_attrInNotNull sjme_nvm_store_file* inFile,
	sjme_attrOutNotNull sjme_pointer* rawData,
	sjme_attrInPositiveNonZero sjme_jint numBytes,
	sjme_attrInPositiveNonZero sjme_jint alignment)
{
	sjme_intPointer placeAt;
	sjme_pointer result;

	if (inFile == NULL || rawData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numBytes <= 0 || alignment <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Sanity check for any corruption. */
	if (inFile->totalLength <= 0 || inFile->usedData < 0 ||
		inFile->freeData < 0 ||
		inFile->totalLength - inFile->usedData != inFile->freeData)
		return SJME_ERROR_MEMORY_CORRUPTION;

	/* Determine next alignment point. */
	placeAt = sjme_util_alignTo(inFile->usedData, alignment);

	/* Is there enough free space for this? */
	if (placeAt < 0 ||
		placeAt + numBytes <= 0 || numBytes >= inFile->freeData ||
		placeAt + numBytes >= inFile->totalLength)
		return SJME_ERROR_STACK_OVERFLOW;

	/* Grab the next chunk of data. */
	result = &inFile->data[placeAt];
	inFile->usedData = sjme_util_alignTo(inFile->usedData,
		alignment) + numBytes;
	inFile->freeData = inFile->totalLength - inFile->usedData;

	/* Wipe it and ensure it is initialized to nothing. */
	memset(result, 0, numBytes);

	/* Debug. */
	sjme_message("alloca() -> %08x + %d/%d -> %08x (%08x) ... %08x",
		placeAt, numBytes, alignment,
		(sjme_jint)result, (sjme_jint)result - (sjme_jint)&inFile->data[0],
		inFile->usedData);

	/* Success! */
	*rawData = result;
	return SJME_ERROR_NONE;
}

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
	sjme_errorCode error;
	sjme_pointer result;
	sjme_intPointer newExtent;

	if (inWindow == NULL || rawData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numBytes <= 0 || alignment <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Sanity check for any corruption. */
	if (inWindow->file == NULL || inWindow->extent < 0)
		return SJME_ERROR_MEMORY_CORRUPTION;

	/* Can only alloca in the tail window. */
	if (inWindow != inWindow->file->tail)
		return SJME_ERROR_STACK_OVERFLOW;

	/* Allocate within the file. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_store_fileAlloca(inWindow->file,
		&result, numBytes, alignment)) || result == NULL)
		return sjme_error_default(error);

	/* Update the extent. */
	newExtent = ((sjme_intPointer)result -
		(sjme_intPointer)inWindow->file->data[0]) + numBytes;
	if (newExtent > inWindow->extent)
		inWindow->extent = newExtent;

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
	sjme_nvm_store_window* window;
	sjme_nvm_store_window* oldTail;

	if (inFile == NULL || outWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Sanity check for any corruption. */
	if (inFile->totalLength <= 0 || inFile->usedData < 0 ||
		inFile->freeData < 0 ||
		inFile->totalLength - inFile->usedData != inFile->freeData)
		return SJME_ERROR_MEMORY_CORRUPTION;

	/* Try allocating the new window. */
	window = NULL;
	if (sjme_error_is(error = sjme_nvm_store_fileAlloca(inFile,
		(sjme_pointer*)&window, sizeof(*window),
		SJME_POINTER_BYTES)) || window == NULL)
		return sjme_error_default(error);

	/* Add to the tail chain? */
	oldTail = inFile->tail;
	if (oldTail != NULL)
	{
		oldTail->next = window;
		window->prev = oldTail;
	}

	/* Link in the main head/tail, note that the tail is always replaced. */
	if (inFile->head == NULL)
		inFile->head = window;
	inFile->tail = window;

	/* Setup main window details. */
	window->file = inFile;
	window->extent = ((sjme_intPointer)window + sizeof(*window)) -
		(sjme_intPointer)inFile->data[0];

	/* Success! */
	*outWindow = window;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_store_windowSlot(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrInNotNull sjme_nvm_store_windowJava* inJava,
	sjme_attrOutNullable sjme_nvm_value** outStorage,
	sjme_attrOutNullable sjme_javaTypeId* outType,
	sjme_attrOutNullable sjme_nvm_store_windowJavaVarChain* outChain,
	sjme_attrInPositive sjme_nvm_store_javaSlot inSlot,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_SLOT_TYPES)
		sjme_nvm_store_slotType inSlotType,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_ACCESS_MODES)
		sjme_nvm_store_accessMode inMode,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS + 1) sjme_javaTypeId inType)
{
	sjme_errorCode error;
	sjme_jint slotLimit, realSlot;
	sjme_nvm_store_windowJavaVar* at;
	sjme_nvm_value* atStorage;
	sjme_jboolean allocNew;
	sjme_jint inTypeLen, atStorageLen, sizeAlign;
	sjme_pointer varAt;
	sjme_intPointer division, windowBase;

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

	/* Get the actual store variables here. */
	at = &inJava->assignedVars[realSlot];

	/* The window base is the actual window. */
	windowBase = (sjme_intPointer)inWindow;

	/* Unallocated? */
	if (at->offsetMultiple == 0)
		atStorage = NULL;

	/* At an offset position. */
	else
		atStorage = (sjme_pointer)(windowBase +
			(4 * (sjme_jint)at->offsetMultiple));

	/* Is the input/storage type wide? */
	inTypeLen = SJME_TYPEID_SLOTS_JAVA(inType);
	atStorageLen = (atStorage != NULL ? SJME_TYPEID_SLOTS_JAVA(at->type) : 0);

	/* What happens depends on the access mode, as there are different */
	/* state transitions. */
	allocNew = SJME_JNI_FALSE;
	switch (inMode)
	{
		case SJME_NVM_STORE_READ:
			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		case SJME_NVM_STORE_WRITE:
			/* Different slot length? */
			if (inTypeLen != atStorageLen)
			{
				/* Something is already here? */
				if (atStorage != NULL)
					return SJME_ERROR_TREAD_INVALID_WRITE;

				/* Allocate otherwise. */
				allocNew = SJME_JNI_TRUE;
			}
			break;

		case SJME_NVM_STORE_WRITE_PROMOTE:
			/* Always allocate if the slot length is different. */
			if (inTypeLen != atStorageLen)
				allocNew = SJME_JNI_TRUE;
			break;

		case SJME_NVM_STORE_REPLACE:
			/* The value must exist and be able to fit. */
			if (atStorage == NULL || inTypeLen <= atStorageLen)
				return SJME_ERROR_TREAD_INVALID_WRITE;

			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		case SJME_NVM_STORE_REPLACE_PROMOTE:
			/* The value must exist. */
			if (atStorage == NULL)
				return SJME_ERROR_TREAD_INVALID_WRITE;

			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		case SJME_NVM_STORE_REPLACE_SAME:
			/* The value must exist and be the same width. */
			if (atStorage == NULL || inTypeLen != atStorageLen)
				return SJME_ERROR_TREAD_INVALID_WRITE;

			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Not allocating a new value and there is no actual storage? */
	if (!allocNew && atStorage == NULL)
	{
		/* Cannot write here. */
		if (inMode != SJME_NVM_STORE_READ)
			return SJME_ERROR_TREAD_INVALID_WRITE;

		/* Only the metadata was requested. */
		if (outType != NULL && inType == SJME_NUM_JAVA_TYPE_IDS)
			goto skip_readMeta;

		/* Fail otherwise. */
		return SJME_ERROR_TREAD_INVALID_READ;
	}

	/* Allocate the variable storage, note that it must be aligned to its */
	/* own size due to pointers and otherwise. */
	varAt = NULL;
	sizeAlign = 4 * inTypeLen;
	if (sjme_error_is(error = sjme_nvm_store_windowAlloca(inWindow,
		&varAt, sizeAlign, sizeAlign)) || varAt == NULL)
		return sjme_error_default(error);

	/* Calculate the division, if this is not a multiple of four then */
	/* something is very wrong with alloca! */
	division = (sjme_intPointer)varAt - windowBase;
	if ((division % 4) != 0)
		return sjme_error_fatal(SJME_ERROR_TREAD_INVALID_WRITE);

	/* If we cannot actually store the multiple because it is too large, */
	/* or it just overflows, then the stack just overflows. */
	division /= 4;
	if (division < 0 || division > SJME_NVM_STORE_MAX_MULTIPLE)
		return SJME_ERROR_STACK_OVERFLOW;

	/* Set and redetermine the multiple and storage. */
	at->offsetMultiple = (sjme_jushort)(division);

	/* Note that the storage is at the exact location. This can be done */
	/* because if we are writing a 64-bit value, we refer to the correct */
	/* portion of it. Otherwise, the same for a 32-bit value. */
	atStorage = varAt;

skip_readMeta:
	/* Requested chain variables in the tread? */
	if (outChain != NULL)
	{
		/* Current chain is simple. */
		outChain->at = at;

		/* Note that stack is top to bottom, and locals are bottom to top. */
		/* That is, stack previous is to the right while local previous is */
		/* to the left. */
		outChain->prev = (inSlot <= 0 ? NULL :
			&inJava->assignedVars[realSlot +
				(inSlotType == SJME_NVM_STORE_SLOT_TYPE_STACK ? 1 : -1)]);
		outChain->next = (inSlot >= (slotLimit - 1) ? NULL :
			&inJava->assignedVars[realSlot +
				(inSlotType == SJME_NVM_STORE_SLOT_TYPE_STACK ? -1 : 1)]);
	}

	/* Requested type? Note that this is just the type here */
	if (outType != NULL)
		*outType = at->type;

	/* Requested direct access to the variable storage? */
	if (outStorage != NULL)
		*outStorage = atStorage;

	/* Success! */
	return SJME_ERROR_NONE;
}
