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

static sjme_errorCode sjme_nvm_store_windowLangInit(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_nvm_store_langType inLangType,
	sjme_attrInNotNull sjme_pointer langData,
	sjme_attrInPositive sjme_jint numVars)
{
	sjme_errorCode error;
	sjme_jint i, n;

	if (inWindow == NULL || inFrame == NULL || langData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numVars < 0 || inLangType <= SJME_NVM_STORE_LANG_NONE ||
		inLangType >= SJME_NVM_STORE_NUM_LANG_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Must always be none! */
	if (inWindow->lang.type != SJME_NVM_STORE_LANG_NONE)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Allocate assigned variable storage. */
	if (sjme_error_is(error = sjme_nvm_store_windowAlloca(inWindow,
		(sjme_pointer)&inWindow->lang.assignedVars,
		sizeof(*inWindow->lang.assignedVars) * numVars,
		SJME_POINTER_BYTES)) || inWindow->lang.assignedVars == NULL)
		return sjme_error_default(error);

	/* Init base data. */
	inWindow->lang.type = inLangType;
	inWindow->lang.data.any = langData;
	inWindow->lang.inFrame = inFrame;
	inWindow->lang.numVars = numVars;

	/* Fill everything with void. */
	for (i = 0, n = numVars; i < n; i++)
		inWindow->lang.assignedVars[i].type = SJME_NVM_STORE_SLOT_MARKER_VOID;

	/* Success! */
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

	/* There already is Java data? */
	if (inWindow->lang.type == SJME_NVM_STORE_LANG_JAVA)
	{
		/* Wrong frame or data is missing? */
		result = inWindow->lang.data.java;
		if (inWindow->lang.inFrame != inFrame || result == NULL)
			return SJME_ERROR_ILLEGAL_STATE;

		/* Perfectly fine! */
		*outJava = result;
		return SJME_ERROR_NONE;
	}

	/* This can only be initialized for none. */
	if (inWindow->lang.type != SJME_NVM_STORE_LANG_NONE)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Allocate storage needed for the Java language. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_store_windowAlloca(inWindow,
		(sjme_pointer*)&result, sizeof(*result),
		SJME_POINTER_BYTES)) || result == NULL)
		return sjme_error_default(error);

	/* Set Java specific info. */
	result->maxLocals = inFrame->inCode->
		perType[SJME_NVM_CODE_INFO_ALL_TYPES].locals;
	result->maxStack = inFrame->inCode->
		perType[SJME_NVM_CODE_INFO_ALL_TYPES].stack;

	/* Initialize base details. */
	if (sjme_error_is(error = sjme_nvm_store_windowLangInit(inWindow,
		inFrame, SJME_NVM_STORE_LANG_JAVA, result,
		result->maxLocals + result->maxStack)))
		return sjme_error_default(error);

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
	sjme_attrOutNotNull sjme_nvm_store_slotInfo* outInfo,
	sjme_attrInPositive sjme_nvm_store_javaSlot inSlot,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_SLOT_TYPES)
		sjme_nvm_store_slotType inSlotType,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_ACCESS_MODES)
		sjme_nvm_store_accessMode inMode,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS + 1) sjme_javaTypeId inType)
{
	sjme_errorCode error;
	sjme_jboolean allocNew;
	sjme_jint inTypeLen, atStorageLen, sizeAlign;
	sjme_pointer varAt;
	sjme_intPointer division, windowBase;
	sjme_nvm_store_slotInfo workInfo;

	if (inWindow == NULL || outInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType < 0 || inType > SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inMode < 0 || inMode >= SJME_NVM_STORE_NUM_ACCESS_MODES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* The "do not care" type is only valid for read. */
	if (inMode != SJME_NVM_STORE_READ && inType == SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* We can just use the slot information function to just get the */
	/* details of what we need to perform a function on the variable slot. */
	/* This also checks for errors, and allows for potential future changes */
	/* to the slot system without major refactoring. */
	memset(&workInfo, 0, sizeof(workInfo));
	if (sjme_error_is(error = sjme_nvm_store_windowSlotInfo(inWindow,
		&workInfo, inSlot, inSlotType)))
		return sjme_error_default(error);

	/* The window base is the actual window. */
	windowBase = (sjme_intPointer)inWindow;

	/* Is the input/storage type wide? */
	/* Note that pointers always have the native system length. */
	inTypeLen = (inType == SJME_JAVA_TYPE_ID_OBJECT ?
		sjme_max(1, SJME_POINTER_BYTES / 4) :
		SJME_TYPEID_SLOTS_JAVA(inType));
	atStorageLen = (workInfo.storage != NULL ?
		workInfo.chain.at->width : 0);

	/* What happens depends on the access mode, as there are different */
	/* state transitions. */
	allocNew = SJME_JNI_FALSE;
	switch (inMode)
	{
		case SJME_NVM_STORE_READ:
			/* This is only invalid if we care about the type and it is */
			/* not the type we actually want. */
			if (inType != SJME_NUM_JAVA_TYPE_IDS &&
				inType != workInfo.type)
				return SJME_ERROR_TREAD_INVALID_READ;
			break;

		case SJME_NVM_STORE_WRITE:
			/* Different slot length? */
			if (inTypeLen != atStorageLen)
			{
				/* Something is already here? */
				if (workInfo.storage != NULL)
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
			if (workInfo.storage == NULL || inTypeLen <= atStorageLen)
				return SJME_ERROR_TREAD_INVALID_WRITE;

			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		case SJME_NVM_STORE_REPLACE_PROMOTE:
			/* The value must exist. */
			if (workInfo.storage == NULL)
				return SJME_ERROR_TREAD_INVALID_WRITE;

			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		case SJME_NVM_STORE_REPLACE_SAME:
			/* The value must exist and be the same width. */
			if (workInfo.storage == NULL || inTypeLen != atStorageLen)
				return SJME_ERROR_TREAD_INVALID_WRITE;

			sjme_todo("Impl");
			return sjme_error_notImplemented(0);

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Not allocating a new value and there is no actual storage? */
	if (!allocNew && workInfo.storage == NULL)
	{
		/* Cannot write here. */
		if (inMode != SJME_NVM_STORE_READ)
			return SJME_ERROR_TREAD_INVALID_WRITE;

		/* Only the metadata was requested. */
		if (inType == SJME_NUM_JAVA_TYPE_IDS)
			goto skip_readMeta;

		/* Fail otherwise. */
		return SJME_ERROR_TREAD_INVALID_READ;
	}

	/* Cannot write variables which are larger than this size. */
	sizeAlign = 4 * inTypeLen;
	if (sizeAlign > SJME_NVM_STORE_MAX_WIDTH_BYTES)
		return SJME_ERROR_TREAD_INVALID_WRITE;

	/* Allocate the variable storage, note that it must be aligned to its */
	/* own size due to pointers and otherwise. */
	/* Unless of course, the size is larger than our pointer size anyway */
	/* then it does not actually matter. */
	varAt = NULL;
	if (sjme_error_is(error = sjme_nvm_store_windowAlloca(inWindow,
		&varAt, sizeAlign,
		sjme_min(SJME_POINTER_BYTES, sizeAlign))) || varAt == NULL)
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
	workInfo.chain.at->width = (sjme_jubyte)(sizeAlign / 4);
	workInfo.chain.at->offsetMultiple = (sjme_jushort)(division);

	/* Always set the new type, this always needs to be a valid type or */
	/* a marker. If a future mode allows for a type change, then anything */
	/* that is not a Java type should be special. */
	if (inType == SJME_STACK_TYPE_WIDE)
		workInfo.chain.at->type = SJME_NVM_STORE_SLOT_MARKER_WIDE;
	else if (inType >= 0 && inType < SJME_NUM_JAVA_TYPE_IDS)
		workInfo.chain.at->type = (sjme_jubyte)inType;
	else
		workInfo.chain.at->type = SJME_NVM_STORE_SLOT_MARKER_SPECIAL;

	/* Note that the storage is at the exact location. This can be done */
	/* because if we are writing a 64-bit value, we refer to the correct */
	/* portion of it. Otherwise, the same for a 32-bit value. */
	/* Since we already set the offsetMultiple, we just need to set the */
	/* workInfo to the up-to-date storage location. */
	workInfo.storage = varAt;

skip_readMeta:
	/* Just copy everything to the output info. */
	memmove(outInfo, &workInfo, sizeof(*outInfo));

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_store_windowSlotInfo(
	sjme_attrInNotNull sjme_nvm_store_window* inWindow,
	sjme_attrOutNotNull sjme_nvm_store_slotInfo* outInfo,
	sjme_attrInPositive sjme_nvm_store_javaSlot inSlot,
	sjme_attrInRange(0, SJME_NVM_STORE_NUM_SLOT_TYPES)
		sjme_nvm_store_slotType inSlotType)
{
	sjme_jint slotLimit, realSlot;
	sjme_nvm_store_windowVar* at;
	sjme_nvm_store_windowJava* inJava;
	sjme_intPointer windowBase;

	if (inWindow == NULL || outInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inSlotType < 0 || inSlotType >= SJME_NVM_STORE_NUM_SLOT_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Need the Java language info for possibly stack/local. */
	inJava = NULL;
	if (inWindow->lang.type == SJME_NVM_STORE_LANG_JAVA)
		inJava = inWindow->lang.data.java;

	/* Gather base information on the slot type. */
	slotLimit = -1;
	realSlot = -1;
	switch (inSlotType)
	{
		case SJME_NVM_STORE_SLOT_TYPE_STACK:
			/* Only valid for Java. */
			if (inJava == NULL)
				return SJME_ERROR_STACK_INDEX_INVALID;

			/* Obtain the slot otherwise. */
			realSlot = inJava->maxStack - (inSlot + 1);
			slotLimit = inJava->maxStack;
			if (inSlot < 0 || inSlot >= slotLimit)
				return SJME_ERROR_STACK_INDEX_INVALID;
			break;

		case SJME_NVM_STORE_SLOT_TYPE_LOCAL:
			/* Only valid for Java. */
			if (inJava == NULL)
				return SJME_ERROR_LOCAL_INDEX_INVALID;

			realSlot = inJava->maxStack + inSlot;
			slotLimit = inJava->maxLocals;
			if (inSlot < 0 || inSlot >= slotLimit)
				return SJME_ERROR_LOCAL_INDEX_INVALID;
			break;

		case SJME_NVM_STORE_SLOT_TYPE_ABSOLUTE:
			realSlot = inSlot;
			slotLimit = inWindow->lang.numVars;
			if (inSlot < 0 || inSlot >= slotLimit)
				return SJME_ERROR_TREAD_INDEX_INVALID;
			break;

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Slot index is not valid? */
	if (realSlot < 0 || realSlot >= inWindow->lang.numVars)
		return SJME_ERROR_TREAD_INDEX_INVALID;

	/* Get the actual store variables here. */
	at = &inWindow->lang.assignedVars[realSlot];

	/* The window base is the actual window. */
	windowBase = (sjme_intPointer)inWindow;

	/* Unallocated? */
	if (at->offsetMultiple == 0)
		outInfo->storage = NULL;

	/* At an offset position. */
	else
		outInfo->storage = (sjme_pointer)(windowBase +
			(4 * (sjme_jint)at->offsetMultiple));

	/* The base type of the slot. */
	outInfo->type = at->type;

	/* Then the chain that connects each slot, logically depending on */
	/* the slot type. */
	outInfo->chain.at = at;
	outInfo->chain.prev = (inSlot <= 0 ? NULL :
		&inWindow->lang.assignedVars[realSlot +
			(inSlotType == SJME_NVM_STORE_SLOT_TYPE_STACK ? 1 : -1)]);
	outInfo->chain.next = (inSlot >= (slotLimit - 1) ? NULL :
		&inWindow->lang.assignedVars[realSlot +
			(inSlotType == SJME_NVM_STORE_SLOT_TYPE_STACK ? -1 : 1)]);

	/* Success! */
	return SJME_ERROR_NONE;
}
