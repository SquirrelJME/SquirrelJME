/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/rom.h"
#include "sjme/nvm/stringPool.h"
#include "sjme/nvm/classy.h"

/** Simple free. */
#define SJME_CLEANUP_FREE(w) \
	do { if ((w) != NULL) \
	{ \
		if (sjme_error_is(error = sjme_alloc_free((w)))) \
			return sjme_error_default(error); \
		\
		(w) = NULL; \
	} } while(0)

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

/** Cleanup of list. */
#define SJME_CLEANUP_LIST(y) \
	do { if ((y) != NULL) \
	{ \
		for (i = 0, n = (y)->length; i < n; i++) \
			SJME_CLEANUP_CLOSE((y)->elements[i]); \
		\
		SJME_CLEANUP_FREE(y); \
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


static sjme_errorCode sjme_class_codeInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_class_codeInfo info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_class_codeInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->inMethod);
	SJME_CLEANUP_FREE(info->exceptions);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_class_constantPoolClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_class_poolInfo info;
	sjme_class_poolEntry* entry;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_class_poolInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cleanup any pool entries. */
	if (info->pool != NULL)
		for (i = 0, n = info->pool->length; i < n; i++)
		{
			entry = &info->pool->elements[i];
			switch (entry->type)
			{
				case SJME_CLASS_POOL_TYPE_CLASS:
					SJME_CLEANUP_CLOSE(entry->classRef.descriptor);
					break;
					
				case SJME_CLASS_POOL_TYPE_STRING:
					SJME_CLEANUP_CLOSE(entry->constString.value);
					break;
					
				case SJME_CLASS_POOL_TYPE_NAME_AND_TYPE:
					SJME_CLEANUP_CLOSE(entry->nameAndType.descriptor);
					SJME_CLEANUP_CLOSE(entry->nameAndType.name);
					break;
					
				case SJME_CLASS_POOL_TYPE_UTF:
					SJME_CLEANUP_CLOSE(entry->utf.utf);
					break;
			}
		}
	
	/* Free list. */
	SJME_CLEANUP_FREE(info->pool);
	
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
	
	SJME_CLEANUP_CLOSE(info->name);
	SJME_CLEANUP_CLOSE(info->type);
	SJME_CLEANUP_CLOSE(info->code);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_desc_identifierClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_desc_identifier info;
	
	/* Recover. */
	info = (sjme_desc_identifier)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->whole);
	
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

static sjme_errorCode sjme_nvm_stateClose(
	sjme_attrInNullable sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_taskClose(
	sjme_attrInNullable sjme_closeable closeable)
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
	SJME_CLEANUP_LIST(stringPool->strings);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stringPool_stringClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_stringPool_string info;
	
	/* Recover. */
	info = (sjme_stringPool_string)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

/* ------------------------------------------------------------------------ */

sjme_errorCode sjme_nvm_allocR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
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
			
		case SJME_NVM_STRUCT_CODE:
			handler = sjme_class_codeInfoClose;
			break;
		
		case SJME_NVM_STRUCT_FIELD_INFO:
			handler = sjme_class_fieldInfoClose;
			break;
		
		case SJME_NVM_STRUCT_IDENTIFIER:
			handler = sjme_desc_identifierClose;
			break;
		
		case SJME_NVM_STRUCT_METHOD_INFO:
			handler = sjme_class_methodInfoClose;
			break;
		
		case SJME_NVM_STRUCT_POOL:
			handler = sjme_class_constantPoolClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_LIBRARY:
			handler = sjme_rom_libraryClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_SUITE:
			handler = sjme_rom_suiteClose;
			break;
		
		case SJME_NVM_STRUCT_STATE:
			handler = sjme_nvm_stateClose;
			break;
		
		case SJME_NVM_STRUCT_STRING_POOL:
			handler = sjme_stringPool_close;
			break;
		
		case SJME_NVM_STRUCT_STRING_POOL_STRING:
			handler = sjme_stringPool_stringClose;
			break;
		
		case SJME_NVM_STRUCT_TASK:
			handler = sjme_nvm_taskClose;
			break;
		
		default:
			sjme_todo("Impl? %d", inType);
			return sjme_error_notImplemented(0);
	}
	
	/* Allocate result. */
	result = NULL;
#if defined(SJME_CONFIG_DEBUG)
	if (sjme_error_is(error = sjme_closeable_allocR(inPool,
		allocSize, handler, SJME_JNI_TRUE,
		SJME_AS_CLOSEABLEP(&result), file, line, func)) ||
		result == NULL)
#else
	if (sjme_error_is(error = sjme_closeable_alloc(inPool,
		allocSize, handler, SJME_JNI_TRUE,
		SJME_AS_CLOSEABLEP(&result))) || result == NULL)
#endif
		return sjme_error_default(error);
	
	/* Set fields. */
	result->type = inType;
	
	/* Success! */
	*outCommon = result;
	return SJME_ERROR_NONE;
}

