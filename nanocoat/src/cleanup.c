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

static sjme_errorCode sjme_nvm_class_classInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_class_info info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_nvm_class_info)closeable;
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


static sjme_errorCode sjme_nvm_class_codeInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_class_codeInfo info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_nvm_class_codeInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->inMethod);
	SJME_CLEANUP_FREE(info->exceptions);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_constantPoolClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_class_poolInfo info;
	sjme_nvm_class_poolEntry* entry;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_nvm_class_poolInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cleanup any pool entries. */
	if (info->pool != NULL)
		for (i = 0, n = info->pool->length; i < n; i++)
		{
			entry = &info->pool->elements[i];
			switch (entry->type)
			{
				case SJME_NVM_CLASS_POOL_TYPE_CLASS:
					SJME_CLEANUP_CLOSE(SJME_P_C_N(entry));
					break;
					
				case SJME_NVM_CLASS_POOL_TYPE_STRING:
					SJME_CLEANUP_CLOSE(entry->constString.value);
					break;
					
				case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
					SJME_CLEANUP_CLOSE(entry->nameAndType.descriptor);
					SJME_CLEANUP_CLOSE(entry->nameAndType.name);
					break;
					
				case SJME_NVM_CLASS_POOL_TYPE_UTF:
					SJME_CLEANUP_CLOSE(entry->utf.utf);
					break;

				default:
					return sjme_error_notImplemented(0);
			}
		}
	
	/* Free list. */
	SJME_CLEANUP_FREE(info->pool);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_fieldIdClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_class_fieldInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_class_fieldInfo info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_nvm_class_fieldInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->name);
	SJME_CLEANUP_CLOSE(info->type);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_methodInfoClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_class_methodInfo info;
	sjme_jint i, n;
	
	/* Recover. */
	info = (sjme_nvm_class_methodInfo)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	SJME_CLEANUP_CLOSE(info->name);
	SJME_CLEANUP_CLOSE(info->type);
	SJME_CLEANUP_CLOSE(info->code);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_rom_libraryClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_rom_library inLibrary;
	
	/* Recover library. */
	inLibrary = (sjme_nvm_rom_library)closeable;
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

static sjme_errorCode sjme_nvm_rom_suiteClose(
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

static sjme_errorCode sjme_nvm_stringPool_close(
	sjme_attrInNullable sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_stringPool stringPool;
	sjme_jint i, n;
	sjme_list_sjme_nvm_stringPool_string* strings;
	sjme_nvm_stringPool_string target;
	
	/* Recover pool. */
	stringPool = (sjme_nvm_stringPool)closeable;
	if (stringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Loaded string cleanup. */
	SJME_CLEANUP_LIST(stringPool->strings);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_stringPool_stringClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_nvm_stringPool_string info;
	
	/* Recover. */
	info = (sjme_nvm_stringPool_string)closeable;
	if (info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_taskClose(
	sjme_attrInNullable sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_taskStringsClose(
	sjme_attrInNullable sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_threadClose(
	sjme_attrInNullable sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_threadFrameClose(
	sjme_attrInNullable sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_instanceClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_message("TODO: sjme_nvm_instanceClose()");
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_vmClass_isClassesClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_vmClass_interfaceIDClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_vmClass_loaderClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_vmClass_methodBindClose(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/* ------------------------------------------------------------------------ */

/** The magic number for NVM objects. */
#define SJME_NVM_OBJECT_MAGIC UINT32_C(0x4E764D3F)

static sjme_errorCode sjme_nvm_closeHandler(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_nvm_common common;
	
	common = SJME_AS_NVM_COMMON(closeable);
	if (common == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Debug. */
	sjme_message("GC FREE: %d:%p", common->type, common);

	/* Forward to specific handler. */
	return common->specificClose(SJME_AS_CLOSEABLE(common));
}

sjme_errorCode sjme_nvm_allocR(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInValue sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_nvm_common* outCommon
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_closeable_closeHandlerFunc handler;
	sjme_nvm_common result;
	sjme_alloc_pool allocPool;
	
	if (inState == NULL || outCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inType <= SJME_NVM_STRUCT_UNKNOWN ||
		inType >= SJME_NVM_NUM_STRUCT)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* This is an error, likely sizeof(result) instead of sizeof(*result). */ 
	if (allocSize < sizeof(sjme_nvm_commonBase))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover pool, some types can use an aliased pool. */
	if (((sjme_alloc_pool)inState)->magic == SJME_ALLOC_POOL_MAGIC)
		allocPool = (sjme_alloc_pool)inState;
	else if (sjme_nvm_isAR(inState, SJME_NVM_STRUCT_STATE))
		allocPool = inState->allocPool;
	else
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Which handler is used? */
	handler = NULL;
	switch (inType)
	{
		case SJME_NVM_STRUCT_CLASS_INFO:
			handler = sjme_nvm_class_classInfoClose;
			break;

		case SJME_NVM_STRUCT_ARRAY_INSTANCE:
		case SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE:
		case SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE:
		case SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE:
		case SJME_NVM_STRUCT_CLASS_INSTANCE:
		case SJME_NVM_STRUCT_OBJECT_INSTANCE:
		case SJME_NVM_STRUCT_STRING_INSTANCE:
		case SJME_NVM_STRUCT_WEAK_INSTANCE:
			handler = sjme_nvm_instanceClose;
			break;
			
		case SJME_NVM_STRUCT_CODE:
			handler = sjme_nvm_class_codeInfoClose;
			break;

		case SJME_NVM_STRUCT_FIELD_ID:
			handler = sjme_nvm_class_fieldIdClose;
			break;
		
		case SJME_NVM_STRUCT_FIELD_INFO:
			handler = sjme_nvm_class_fieldInfoClose;
			break;

		case SJME_NVM_STRUCT_FRAME:
			handler = sjme_nvm_threadFrameClose;
			break;

		case SJME_NVM_STRUCT_INTERFACE_ID:
			handler = sjme_nvm_vmClass_interfaceIDClose;
		break;
		
		case SJME_NVM_STRUCT_IS_CLASSES:
			handler = sjme_nvm_vmClass_isClassesClose;
			break;
		
		case SJME_NVM_STRUCT_METHOD_ID:
			handler = sjme_nvm_vmClass_methodBindClose;
			break;
		
		case SJME_NVM_STRUCT_METHOD_INFO:
			handler = sjme_nvm_class_methodInfoClose;
			break;
		
		case SJME_NVM_STRUCT_POOL:
			handler = sjme_nvm_class_constantPoolClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_LIBRARY:
			handler = sjme_nvm_rom_libraryClose;
			break;
		
		case SJME_NVM_STRUCT_ROM_SUITE:
			handler = sjme_nvm_rom_suiteClose;
			break;
		
		case SJME_NVM_STRUCT_STATE:
			handler = sjme_nvm_stateClose;
			break;
		
		case SJME_NVM_STRUCT_STRING_POOL:
			handler = sjme_nvm_stringPool_close;
			break;
		
		case SJME_NVM_STRUCT_STRING_POOL_STRING:
			handler = sjme_nvm_stringPool_stringClose;
			break;
		
		case SJME_NVM_STRUCT_TASK:
			handler = sjme_nvm_taskClose;
			break;

		case SJME_NVM_STRUCT_TASK_STRINGS:
			handler = sjme_nvm_taskStringsClose;
			break;
		
		case SJME_NVM_STRUCT_THREAD_INSTANCE:
			handler = sjme_nvm_threadClose;
			break;
		
		case SJME_NVM_STRUCT_VM_CLASS_LOADER:
			handler = sjme_nvm_vmClass_loaderClose;
			break;
		
		default:
			sjme_todo("Impl? %d", inType);
			return sjme_error_notImplemented(0);
	}
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_closeable_allocR(allocPool,
		allocSize, sjme_nvm_closeHandler, SJME_AS_CLOSEABLEP(&result)
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
		result == NULL)
		return sjme_error_default(error);
	
	/* Set fields. */
	result->type = inType;
	result->magic = SJME_NVM_OBJECT_MAGIC;
	result->specificClose = handler;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		"GC ALLC: %d:%p (%d)", inType, result, allocSize);
#endif
	
	/* Success! */
	*outCommon = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_isA(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType,
	sjme_attrOutNotNull sjme_jboolean* outResult)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_nvm_common common;
	
	if (outResult == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inType != SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE &&
		(inType <= SJME_NVM_STRUCT_UNKNOWN || inType >= SJME_NVM_NUM_STRUCT))
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Null input is always nothing. */
	if (inWhat == NULL)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* All NVM objects are weakly referenced. */
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakRefGet(inWhat, &weak)) || weak == NULL)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* Must be the type and the magic must be valid! */
	/* Aliases of object types match objects as well. */
	common = inWhat;
	if (common->magic != SJME_NVM_OBJECT_MAGIC)
		*outResult = SJME_JNI_FALSE;
	else if (common->type == inType ||
		(inType == SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE &&
			(common->type == SJME_NVM_STRUCT_ARRAY_INSTANCE ||
			common->type == SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE ||
			common->type == SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE ||
			common->type == SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE ||
			common->type == SJME_NVM_STRUCT_CLASS_INSTANCE ||
			common->type == SJME_NVM_STRUCT_OBJECT_INSTANCE ||
			common->type == SJME_NVM_STRUCT_STRING_INSTANCE ||
			common->type == SJME_NVM_STRUCT_THREAD_INSTANCE ||
			common->type == SJME_NVM_STRUCT_WEAK_INSTANCE)))
		*outResult = SJME_JNI_TRUE;
	else
		*outResult = SJME_JNI_FALSE;
		
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_nvm_isAR(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_NVM_NUM_STRUCT) sjme_nvm_structType inType)
{
	sjme_jboolean result;
	
	/* Forward call. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_nvm_isA(inWhat, inType, &result)))
		return SJME_JNI_FALSE;

	/* Was this the type? */
	return result;
}

