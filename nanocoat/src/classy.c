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

/** The magic number for classes. */
#define SJME_CLASS_MAGIC INT32_C(0xCAFEBABE)

/** CLDC 1.1 max version (JSR 30). */
#define SJME_CLASS_CLDC_1_0_MAX INT32_C(3080191)

/** CLDC 1.1 max version. (JSR 139). */
#define SJME_CLASS_CLDC_1_1_MAX INT32_C(3342335)

/** CLDC 8 max version. */
#define SJME_CLASS_CLDC_1_8_MAX INT32_C(3407872)

sjme_errorCode sjme_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_info* outClass)
{
	sjme_errorCode error;
	sjme_jint magic, fullVersion;
	sjme_jshort major, minor;
	sjme_class_version actualVersion;
	sjme_class_poolInfo pool;
	
	if (inPool == NULL || inStream == NULL || outClass == NULL)
		return SJME_ERROR_NONE;
	
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
		inPool, inStream, &pool)) || pool == NULL)
		goto fail_parsePool;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;

fail_parsePool:
	if (pool != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(pool));
fail_badVersion:
fail_readMinor:
fail_readMajor:
fail_badMagic:
fail_readMagic:
	return sjme_error_default(error);
}

sjme_errorCode sjme_class_parseConstantPool(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_poolInfo* outPool)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint index;
	sjme_jbyte tag;
	sjme_list_sjme_class_poolEntry* entries;
	
	if (inPool == NULL || inStream == NULL || outPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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
	for (index = 1; index < count; index++)
	{
		/* Read in tag. */
		tag = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJB(
			inStream, &tag)) || tag < 0)
			goto fail_readTag;
		
		sjme_todo("Impl?");
		return SJME_ERROR_NOT_IMPLEMENTED;
	}
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_readTag:
fail_entryList:
	if (entries != NULL)
		sjme_alloc_free(entries);
fail_poolCount:
fail_readCount:
	return sjme_error_default(error);
}
