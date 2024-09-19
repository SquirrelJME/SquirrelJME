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
#include "sjme/nvm/stringPool.h"

static sjme_errorCode sjme_stringPool_close(
	sjme_attrInNullable sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_stringPool stringPool;
	sjme_jint i, n;
	sjme_list_sjme_stringPool_string* strings;
	sjme_stringPool_string target;
	
	/* Recover pool. */
	stringPool = (sjme_stringPool)closeable;
	if (stringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Loaded string cleanup. */
	strings = stringPool->strings;
	if (strings != NULL)
	{
		/* Close every contained string. */
		for (i = 0, n = strings->length; i < n; i++)
		{
			/* Skip blanks. */
			target = strings->elements[i];
			if (target == NULL)
				continue;
			
			/* Close individual string. */
			if (sjme_error_is(error = sjme_closeable_close(
				SJME_AS_CLOSEABLE(target))))
				return sjme_error_default(error);
			
			/* Clear it. */
			strings->elements[i] = NULL;
		}
		
		/* Destroy list. */
		if (sjme_error_is(error = sjme_alloc_free(strings)))
			return sjme_error_default(error);
		strings = NULL;
	}
	
	/* Wipe. */
	stringPool->inPool = NULL;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_rom_libraryClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_rom_library inLibrary;
	
	/* Recover library. */
	inLibrary = (sjme_rom_library)closeable;
	if (inLibrary == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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
	if (weak->enqueue != NULL &&
		weak->enqueue != sjme_nvm_enqueueHandler)
		return SJME_ERROR_ILLEGAL_STATE;
	 
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
		
		case SJME_NVM_STRUCT_STRING_POOL:
			handler = sjme_stringPool_close;
			break;
	}
	
	/* Set handler. */
	inCommon->closeable.closeHandler = handler;
	inCommon->closeable.refCounting = SJME_JNI_TRUE;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

