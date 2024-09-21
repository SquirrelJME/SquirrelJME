/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/classy.h"
#include "sjme/debug.h"
#include "sjme/cleanup.h"

/** The magic number for classes. */
#define SJME_CLASS_MAGIC INT32_C(0xCAFEBABE)

/** CLDC 1.1 max version (JSR 30). */
#define SJME_CLASS_CLDC_1_0_MAX INT32_C(3080191)

/** CLDC 1.1 max version. (JSR 139). */
#define SJME_CLASS_CLDC_1_1_MAX INT32_C(3342335)

/** CLDC 8 max version. */
#define SJME_CLASS_CLDC_1_8_MAX INT32_C(3407872)

/** Public class. */
#define SJME_CLASS_ACC_PUBLIC INT16_C(0x0001)

/** Final class. */
#define SJME_CLASS_ACC_FINAL INT16_C(0x0010)

/** Alternative @c invokesuper logic. */
#define SJME_CLASS_ACC_SUPER INT16_C(0x0020)

/** Class is an interface. */
#define SJME_CLASS_ACC_INTERFACE INT16_C(0x0200)

/** Class is abstract. */
#define SJME_CLASS_ACC_ABSTRACT INT16_C(0x0400)

/** Class is synthetic. */
#define SJME_CLASS_ACC_SYNTHETIC INT16_C(0x1000)

/** Class is an annotation. */
#define SJME_CLASS_ACC_ANNOTATION INT16_C(0x2000)

/** Class is an enum. */
#define SJME_CLASS_ACC_ENUM INT16_C(0x4000)

static sjme_errorCode sjme_class_readClassFlags(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_classFlags* outFlags)
{
	sjme_errorCode error;
	sjme_jshort rawFlags;
	
	if (inStream == NULL || outFlags == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in flags. */
	rawFlags = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &rawFlags)) || rawFlags < 0)
		return sjme_error_default(error);
	
	/* Translate to bitfield. */
	if ((rawFlags & SJME_CLASS_ACC_PUBLIC) != 0)
		outFlags->access.public = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_FINAL) != 0)
		outFlags->final = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_SUPER) != 0)
		outFlags->super = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_INTERFACE) != 0)
		outFlags->interface = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_ABSTRACT) != 0)
		outFlags->abstract = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_SYNTHETIC) != 0)
		outFlags->synthetic = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_ANNOTATION) != 0)
		outFlags->annotation = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_ENUM) != 0)
		outFlags->enumeration = SJME_JNI_TRUE;
	
	/* Cannot be abstract and final. */
	/* Annotation must be an interface. */
	/* Interface must be abstract and not final, super, or enum */
	if ((outFlags->abstract && outFlags->final) ||
		(outFlags->annotation && !outFlags->interface) ||
		(outFlags->interface && (!outFlags->abstract ||
			outFlags->final || outFlags->super || outFlags->enumeration)))
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_class_readPoolRefIndex(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_poolInfo inClassPool,
	sjme_attrInPositiveNonZero sjme_class_poolType desireType,
	sjme_attrOutNotNull sjme_class_poolEntry** outEntry)
{
	sjme_errorCode error;
	sjme_jshort index;
	sjme_class_poolEntry* result;
	
	if (inStream == NULL || inClassPool == NULL || outEntry == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in index. */
	index = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &index)))
		return sjme_error_default(error);
	
	/* Not a valid index? */
	if (index <= 0 || index >= inClassPool->pool->length)
		return SJME_ERROR_INVALID_CLASS_POOL_INDEX;
	
	/* Must be the desired type. */
	result = &inClassPool->pool->elements[index];
	if (result->type != desireType)
		return SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
	
	/* Success! */
	*outEntry = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrOutNotNull sjme_class_info* outClass)
{
	sjme_errorCode error;
	sjme_jint magic, fullVersion;
	sjme_jshort major, minor;
	sjme_class_version actualVersion;
	sjme_class_poolInfo pool;
	sjme_class_info result;
	
	if (inPool == NULL || inStream == NULL || inStringPool == NULL ||
		outClass == NULL)
		return SJME_ERROR_NONE;
	
	/* Make sure we can actually allocate the resultant class. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), NULL, NULL,
		(sjme_pointer*)&result, NULL)) || result == NULL)
		goto fail_allocResult;
	
	/* Initialize. */
	if (sjme_error_is(error = sjme_nvm_initCommon(
		SJME_AS_NVM_COMMON(result),
		SJME_NVM_STRUCT_CLASS_INFO)))
		goto fail_initResult;
	
	/* Read in magic number. */
	magic = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		inStream, &magic)))
		goto fail_readMagic;
	
	/* It must be valid! */
	if (magic != SJME_CLASS_MAGIC)
	{
		error = SJME_ERROR_INVALID_CLASS_MAGIC;
		goto fail_badMagic;
	}
		
	/* Read in version info. */	
	minor = INT16_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &minor)))
		goto fail_readMinor;
	
	major = INT16_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &major)))
		goto fail_readMajor;
	
	/* Compose and find matching version. */
	fullVersion = (major << 16) | (minor & 0xFFFF);
	if (fullVersion >= SJME_CLASS_CLDC_1_0 &&
		fullVersion <= SJME_CLASS_CLDC_1_0_MAX)
		actualVersion = SJME_CLASS_CLDC_1_0;
	else if (fullVersion >= SJME_CLASS_CLDC_1_1 &&
		fullVersion <= SJME_CLASS_CLDC_1_1_MAX)
		actualVersion = SJME_CLASS_CLDC_1_1;
	else if (fullVersion >= SJME_CLASS_CLDC_1_8 &&
		fullVersion <= SJME_CLASS_CLDC_1_8_MAX)
		actualVersion = SJME_CLASS_CLDC_1_8;
	
	/* Not valid. */
	else
	{
		error = SJME_ERROR_INVALID_CLASS_VERSION;
		goto fail_badVersion;
	}
	
	/* Parse the constant pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_class_parseConstantPool(
		inPool, inStream, inStringPool, &pool)) || pool == NULL)
		goto fail_parsePool;
	
	/* We are using this, so count it up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(pool, NULL)))
		goto fail_countPool;
	result->pool = pool;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_class_readClassFlags(
		inStream, &result->flags)))
		goto fail_readFlags;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;

fail_readFlags:
fail_countPool:
fail_parsePool:
	if (pool != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(pool));
		pool = NULL;
		result->pool = NULL;
	}
fail_badVersion:
fail_readMinor:
fail_readMajor:
fail_badMagic:
fail_readMagic:
fail_initResult:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

sjme_errorCode sjme_class_parseConstantPool(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrOutNotNull sjme_class_poolInfo* outPool)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint index;
	sjme_jbyte tag;
	sjme_list_sjme_class_poolEntry* entries;
	sjme_class_poolEntry* entry;
	sjme_stringPool_string utf;
	sjme_class_poolInfo result;
	
	if (inPool == NULL || inStream == NULL || outPool == NULL ||
		inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we can actually allocate this. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), NULL, NULL,
		(sjme_pointer*)&result, NULL)) || result == NULL)
		goto fail_allocResult;
	
	/* Initialize it. */
	if (sjme_error_is(error = sjme_nvm_initCommon(
		SJME_AS_NVM_COMMON(result), SJME_NVM_STRUCT_POOL)))
		goto fail_initCommon;
	
	/* Read in pool count. */
	count = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &count)) || count < 0)
		goto fail_readCount;
	
	/* Invalid pool size? */
	if (count < 0 || count >= INT16_MAX)
	{
		error = SJME_ERROR_INVALID_CLASS_POOL_COUNT;
		goto fail_poolCount;
	}
	
	/* Count up by one, since zero is included! */
	count += 1;
	
	/* Allocate resultant entries, where they will all go. */
	entries = NULL;
	if (sjme_error_is(error = sjme_list_alloc(inPool,
		count, &entries, sjme_class_poolEntry, 0)) || entries == NULL)
		goto fail_entryList;
	
	/* Read in all entries. */
	/* This is a first pass since index items can refer to later entries. */
	for (index = 1; index < count - 1; index++)
	{
		/* Which entry is being written? */
		entry = &entries->elements[index];
		
		/* Read in tag. */
		tag = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJB(
			inStream, &tag)) || tag < 0)
			goto fail_readTag;
		
		/* Debug. */
		sjme_message("TAG: %d", tag);
		
		/* Set tag. */
		entry->type = tag;
		
		/* Which tag is this? */
		switch (tag)
		{
			case SJME_CLASS_POOL_TYPE_DOUBLE:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constDouble.value.hi)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constDouble.value.lo)))
					goto fail_readItem;
				break;
			
			case SJME_CLASS_POOL_TYPE_CLASS:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->classRef.descriptorIndex)))
					goto fail_readItem;
				break;
			
				/* UTF String. */
			case SJME_CLASS_POOL_TYPE_UTF:
				utf = NULL;
				if (sjme_error_is(error = sjme_stringPool_locateStream(
					inStringPool, inStream, &utf)) || utf == NULL)
					goto fail_readItem;
				
				/* Debug. */
				sjme_message("Read UTF: %s",
					utf->chars);
				
				/* Store and count up entry as we are using it now. */
				entry->utf.utf = utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(
					utf, NULL)))
					goto fail_readItem;
				break;
			
			default:
				sjme_todo("Impl? %d", tag);
				return SJME_ERROR_NOT_IMPLEMENTED;
		}
	}
	
	/* Second stage item linking. */
	for (index = 0; index < count; index++)
	{
		/* Which entry is being initialized? */
		entry = &entries->elements[index];
		
		switch (tag)
		{
			/* Nothing to reference. */
			case SJME_CLASS_POOL_TYPE_UTF:
			case SJME_CLASS_POOL_TYPE_INTEGER:
			case SJME_CLASS_POOL_TYPE_FLOAT:
			case SJME_CLASS_POOL_TYPE_LONG:
			case SJME_CLASS_POOL_TYPE_DOUBLE:
				break;
			
			default:
				sjme_todo("Impl? %d", tag);
				return SJME_ERROR_NOT_IMPLEMENTED;
		}
	}
	
	/* Setup details. */
	result->pool = entries;
	
	/* Success! */
	*outPool = result;
	return SJME_ERROR_NONE;
	
fail_readItem:
fail_readTag:
fail_entryList:
	if (entries != NULL)
		sjme_alloc_free(entries);
fail_poolCount:
fail_readCount:
fail_initCommon:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}
