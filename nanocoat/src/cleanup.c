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
#include "sjme/nvm/classy.h"

/** Simple close. */
#define SJME_CLEANUP_CLOSE(x) \
	do { if ((x) != NULL) \
	{ \
		if (sjme_error_is(error = sjme_closeable_close( \
			SJME_AS_CLOSEABLE((x))))) \
			return sjme_error_default(error); \
		\
		(x) = NULL; \
	} } while(0)

#define SJME_CLEANUP_LIST(y) \
	do { if ((y) != NULL) \
	{ \
		for (i = 0, n = (y)->length; i < n; i++) \
			SJME_CLEANUP_CLOSE((y)->elements[i]); \
		\
		if (sjme_error_is(error = sjme_alloc_free((y)))) \
			return sjme_error_default(error); \
		\
		(y) = NULL; \
	} } while(0)

static sjme_errorCode sjme_class_classInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_class_info info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_class_info)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->pool);
	SJME_CLEANUP_CLOSE(info->name);
	SJME_CLEANUP_CLOSE(info->superName);
	SJME_CLEANUP_LIST(info->interfaceNames);
	SJME_CLEANUP_LIST(info->fields);
	SJME_CLEANUP_LIST(info->methods);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_class_fieldInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_class_fieldInfo info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_class_fieldInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->inClass);
	SJME_CLEANUP_CLOSE(info->name);
	SJME_CLEANUP_CLOSE(info->type);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_class_methodInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_class_methodInfo info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_class_methodInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->inClass);
	SJME_CLEANUP_CLOSE(info->name);
	SJME_CLEANUP_CLOSE(info->type);
	SJME_CLEANUP_CLOSE(info->code);
	
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

/* ------------------------------------------------------------------------ */

sjme_errorCode sjme_nvm_alloc(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon)
{
	sjme_errorCode error;
	sjme_closeable_closeHandlerFunc handler;
	sjme_nvm_common result;
	
	if (inPool == NULL || outCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inType <= SJME_NVM_STRUCT_UNKNOWN ||
		inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Which handler is used? */
	handler = NULL;
	switch (inType)
	{
		case SJME_NVM_STRUCT_CLASS_INFO:
			handler = sjme_class_classInfoClose;
			break;
		
		case SJME_NVM_STRUCT_FIELD_INFO:
			handler = sjme_class_fieldInfoClose;
			break;
		
		case SJME_NVM_STRUCT_METHOD_INFO:
			handler = sjme_class_methodInfoClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_LIBRARY:
			handler = sjme_rom_libraryClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_SUITE:
			handler = sjme_rom_suiteClose;
			break;
		
		case SJME_NVM_STRUCT_STRING_POOL:
			handler = sjme_stringPool_close;
			break;
		
		default:
			sjme_todo("Impl? %d", inType);
			return sjme_error_notImplemented(0);
	}
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_closeable_alloc(inPool,
		allocSize, handler, SJME_JNI_TRUE,
		SJME_AS_CLOSEABLEP(&result))) || result == NULL)
		return sjme_error_default(error);
	
	/* Set fields. */
	result->type = inType;
	
	/* Success! */
	*outCommon = result;
	return SJME_ERROR_NONE;
}

