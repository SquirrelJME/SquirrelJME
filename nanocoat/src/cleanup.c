/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/cleanup.h"
#include "sjme/nvm/rom.h"

static sjme_errorCode sjme_rom_libraryClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_rom_library inLibrary;
	
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover library. */
	inLibrary = (sjme_rom_library)closeable;
	
	/* Delete the prefix if there is one. */
	if (inLibrary->prefix != NULL)
	{
		/* Free it. */
		if (sjme_error_is(error = sjme_alloc_free(
			(sjme_pointer)inLibrary->prefix)))
			return sjme_error_default(error);
		
		/* Clear. */
		inLibrary->prefix = NULL;
	}
	
	/* Delete name if there is one. */
	if (inLibrary->name != NULL)
	{
		/* Free it. */
		if (sjme_error_is(error = sjme_alloc_free(
			(sjme_pointer)inLibrary->name)))
			return sjme_error_default(error);
		
		/* Clear. */
		inLibrary->name = NULL;
	}
	
	/* Forward close handler. */
	if (inLibrary->functions->close != NULL)
		return inLibrary->functions->close(inLibrary);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_rom_suiteClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/* ------------------------------------------------------------------------ */

sjme_errorCode sjme_nvm_enqueueHandler(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	sjme_nvm_common common;
	
	/* Recover common. */
	common = (sjme_nvm_common)data;
	
	/* Do nothing if this was already cleared. */
	if (common == NULL)
		return SJME_ERROR_NONE;
	
	/* Specific handling depends on the type. */
	switch (common->type)
	{
			/* Forward to enqueue handler for closeable, since we do */
			/* not have anything special to do. */
		default:
			return sjme_closeable_autoEnqueue(weak, data, isBlockFree);
	}
}

sjme_errorCode sjme_nvm_initCommon(
	sjme_attrInNotNull sjme_nvm_common inCommon,
	sjme_attrInValue sjme_nvm_structType inType)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_closeable_closeHandlerFunc handler;
	
	if (inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inType <= SJME_NVM_STRUCT_UNKNOWN ||
		inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Must be a weak pointer. */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(inCommon,
		&weak)) || weak == NULL)
		return sjme_error_default(error);
	
	/* Setup correct enqueue handler. */
	weak->enqueue = sjme_nvm_enqueueHandler;
	weak->enqueueData = inCommon;
	
	/* Set type information. */
	inCommon->type = inType;
	
	/* Which handle is used? */
	handler = NULL;
	switch (inType)
	{
		case SJME_NVM_STRUCT_ROM_LIBRARY:
			handler = sjme_rom_libraryClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_SUITE:
			handler = sjme_rom_suiteClose;
			break;
	}
	
	/* Set handler. */
	inCommon->closeable.closeHandler = handler;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

