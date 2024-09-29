/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

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

/** Public. */
#define SJME_CLASS_ACC_PUBLIC INT16_C(0x0001)

/** Private. */
#define SJME_CLASS_ACC_PRIVATE INT16_C(0x0002)

/** Protected. */
#define SJME_CLASS_ACC_PROTECTED INT16_C(0x0004)

/** Static member. */
#define SJME_CLASS_ACC_STATIC INT16_C(0x0008)

/** Final class or member. */
#define SJME_CLASS_ACC_FINAL INT16_C(0x0010)

/** Alternative @c invokesuper logic. */
#define SJME_CLASS_ACC_SUPER INT16_C(0x0020)

/** Synchronized method. */
#define SJME_CLASS_ACC_SYNCHRONIZED INT16_C(0x0020)

/** Bridge method. */
#define SJME_CLASS_ACC_BRIDGE INT16_C(0x0040)

/** Variable arguments. */
#define SJME_CLASS_ACC_VARARGS INT16_C(0x0080)

/** Native method. */
#define SJME_CLASS_ACC_NATIVE INT16_C(0x0100)

/** Class is an interface. */
#define SJME_CLASS_ACC_INTERFACE INT16_C(0x0200)

/** Abstract class or method. */
#define SJME_CLASS_ACC_ABSTRACT INT16_C(0x0400)

/** Strict floating point method. */
#define SJME_CLASS_ACC_STRICTFP INT16_C(0x0800)

/** Synthetic class or member. */
#define SJME_CLASS_ACC_SYNTHETIC INT16_C(0x1000)

/** Field is volatile. */
#define SJME_CLASS_ACC_VOLATILE INT16_C(0x0040)

/** Field is transient. */
#define SJME_CLASS_ACC_TRANSIENT INT16_C(0x0080)

/** Class is an annotation. */
#define SJME_CLASS_ACC_ANNOTATION INT16_C(0x2000)

/** Class is an enum. */
#define SJME_CLASS_ACC_ENUM INT16_C(0x4000)

static sjme_errorCode sjme_class_readPoolRefIndex(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_poolInfo inClassPool,
	sjme_attrInPositiveNonZero sjme_class_poolType desireType,
	sjme_attrInValue sjme_jboolean canNull,
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
	{
		/* Can be zero index for nothing, however. */
		if (index == 0 && canNull)
		{
			*outEntry = NULL;
			return SJME_ERROR_NONE;
		}
		
		return SJME_ERROR_INVALID_CLASS_POOL_INDEX;
	}
	
	/* Must be the desired type. */
	result = &inClassPool->pool->elements[index];
	if (result->type != desireType)
		return SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
	
	/* Success! */
	*outEntry = result;
	return SJME_ERROR_NONE;
}

static const sjme_class_parseAttributeHandlerInfo sjme_class_classAttr[] =
{
	{NULL, NULL},
};

static sjme_errorCode sjme_class_classFlagsParse(
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

static sjme_errorCode sjme_class_codeAttrLineNumberTable(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	if (inPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_class_codeAttrStackMap(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	if (inPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_class_codeAttrStackMapTable(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	if (inPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static const sjme_class_parseAttributeHandlerInfo sjme_class_codeAttr[] =
{
#if 0
	{"LineNumberTable",
		sjme_class_codeAttrLineNumberTable},
	{"StackMap",
		sjme_class_codeAttrStackMap},
	{"StackMapTable",
		sjme_class_codeAttrStackMapTable},
#endif
	{NULL, NULL},
};

static sjme_errorCode sjme_class_fieldAttrConstantValue(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error;
	sjme_class_fieldInfo fieldInfo;
	sjme_jshort index;
	sjme_class_poolEntry* item;
	
	fieldInfo = context;
	if (inPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL || fieldInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read the constant value index. */
	index = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		attrStream, &index)) || index < 0)
		return sjme_error_default(error);
	
	/* Make sure it is valid. */
	if (index <= 0 || index >= inConstPool->pool->length)
		return SJME_ERROR_INVALID_CLASS_POOL_INDEX;
	
	/* Process based on the pool type used. */
	item = &inConstPool->pool->elements[index];
	if (item->type == SJME_CLASS_POOL_TYPE_INTEGER)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_INTEGER;
		fieldInfo->constVal.value.java.i = item->constInteger.value;
	}
	else if (item->type == SJME_CLASS_POOL_TYPE_FLOAT)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_FLOAT;
		fieldInfo->constVal.value.java.f = item->constFloat.value;
	}
	else if (item->type == SJME_CLASS_POOL_TYPE_LONG)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_LONG;
		fieldInfo->constVal.value.java.j = item->constLong.value;
	}
	else if (item->type == SJME_CLASS_POOL_TYPE_DOUBLE)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_DOUBLE;
		fieldInfo->constVal.value.java.d = item->constDouble.value;
	}
	else if (item->type == SJME_CLASS_POOL_TYPE_STRING)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_OBJECT;
		fieldInfo->constVal.value.string = item->constString.value;
		
		/* Count up as we are using it. */
		if (sjme_error_is(error = sjme_alloc_weakRef(
			fieldInfo->constVal.value.string, NULL)))
			return sjme_error_default(error);
	}
	
	/* Invalid! */
	else
		return SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_class_parseAttributeHandlerInfo sjme_class_fieldAttr[] =
{
	{"ConstantValue", sjme_class_fieldAttrConstantValue},
	{NULL, NULL},
};

static sjme_errorCode sjme_class_fieldFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_fieldFlags* outFlags)
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
	memset(outFlags, 0, sizeof(*outFlags));
	if ((rawFlags & SJME_CLASS_ACC_PUBLIC) != 0)
		outFlags->member.access.public = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_PRIVATE) != 0)
		outFlags->member.access.private = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_PROTECTED) != 0)
		outFlags->member.access.protected = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_STATIC) != 0)
		outFlags->member.isStatic = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_FINAL) != 0)
		outFlags->member.final = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_VOLATILE) != 0)
		outFlags->isVolatile = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_TRANSIENT) != 0)
		outFlags->transient = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_ENUM) != 0)
		outFlags->enumeration = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_SYNTHETIC) != 0)
		outFlags->member.synthetic = SJME_JNI_TRUE;
	
	/* Can only have a single access mode. */
	/* Cannot be both final and volatile. */
	if (((outFlags->member.access.public +
		outFlags->member.access.protected +
		outFlags->member.access.private) > 1) ||
		(outFlags->member.final && outFlags->isVolatile))
		return SJME_ERROR_INVALID_FIELD_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_class_methodAttrCode(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error;
	sjme_jshort maxStack, maxLocals, numExcept;
	sjme_jint codeLen, i, actualCodeLen;
	sjme_class_methodInfo methodInfo;
	sjme_class_codeInfo result;
	sjme_jubyte* rawCode;
	sjme_list_sjme_class_exceptionHandler* excepts;
	sjme_class_exceptionHandler* except;
	
	methodInfo = context;
	if (inPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL || methodInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can only have one. */
	if (methodInfo->code != NULL)
		return SJME_ERROR_METHOD_MULTIPLE_CODE;
	
	/* Make sure we can allocate this. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inPool,
		sizeof(*result), SJME_NVM_STRUCT_CODE,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* In this method! */
	result->inMethod = methodInfo;
	
	/* Read in max stack and locals. */
	maxStack = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		attrStream, &maxStack)) || maxStack < 0)
		goto fail_readMaxStack;
	maxLocals = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		attrStream, &maxLocals)) || maxLocals < 0)
		goto fail_readMaxLocals;
	
	/* Set. */
	result->maxStack = maxStack;
	result->maxLocals = maxLocals;
	
	/* Read in code length. */
	codeLen = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		attrStream, &codeLen)) || codeLen <= 0)
		goto fail_readCodeLen;
	
	/* Allocate. */
	rawCode = sjme_alloca(codeLen);
	if (rawCode == NULL)
	{
		error = SJME_ERROR_OUT_OF_MEMORY;
		goto fail_allocRawCode;
	}
	memset(rawCode, 0, codeLen);
	
	/* Read in code. */
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		attrStream, &actualCodeLen,
		rawCode, codeLen)) ||
		actualCodeLen != codeLen)
		goto fail_readRawCode;
	
	/* Read in exception table count. */
	numExcept = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		attrStream, &numExcept)) || numExcept < 0)
		goto fail_readNumExcept;
	
	/* Only if there are actual exceptions. */
	excepts = NULL;
	if (numExcept > 0)
	{
		/* Allocate base table. */
		if (sjme_error_is(error = sjme_list_alloc(inPool,
			numExcept, &excepts, sjme_class_exceptionHandler, 0)) ||
			excepts == NULL)
			goto fail_allocExcepts;
		result->exceptions = excepts;
		
		/* Read in each exception. */
		for (i = 0; i < numExcept; i++)
		{
			/* Which is being read into? */
			except = &excepts->elements[i];
			
			/* Read in values. */
			except->range.start = -1;
			if (sjme_error_is(error = sjme_stream_inputReadValueJS(
				attrStream, &except->range.start)) ||
				except->range.start < 0)
				goto fail_exceptShorts;
			except->range.end = -1;
			if (sjme_error_is(error = sjme_stream_inputReadValueJS(
				attrStream, &except->range.end)) ||
				except->range.end < 0)
				goto fail_exceptShorts;
			except->handlerPc = -1;
			if (sjme_error_is(error = sjme_stream_inputReadValueJS(
				attrStream, &except->handlerPc)) ||
				except->handlerPc < 0)
				goto fail_exceptShorts;
			
			/* Read in handler class. */
			except->handles = NULL;
			if (sjme_error_is(error = sjme_class_readPoolRefIndex(
				attrStream, inConstPool,
				SJME_CLASS_POOL_TYPE_CLASS,
				SJME_JNI_TRUE,
				(sjme_class_poolEntry**)&except->handles)))
				goto fail_exceptHandles;
		}
	}
	
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_class_parseAttributes(
		inPool, attrStream, inConstPool, inStringPool,
		sjme_class_codeAttr, result)))
		goto fail_parseAttributes;
	
	/* Make sure the code is referenced. */	
	methodInfo->code = result;
	if (sjme_error_is(error = sjme_alloc_weakRef(result, NULL)))
		goto fail_refCode;
	
	/* Success! */
	return SJME_ERROR_NONE;
fail_refCode:
fail_parseAttributes:
fail_exceptHandles:
fail_exceptShorts:
fail_allocExcepts:
fail_readNumExcept:
fail_readRawCode:
fail_allocRawCode:
fail_readCodeLen:
fail_readMaxLocals:
fail_readMaxStack:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

static const sjme_class_parseAttributeHandlerInfo sjme_class_methodAttr[] =
{
	{"Code", sjme_class_methodAttrCode},
	{NULL, NULL},
};

static sjme_errorCode sjme_class_methodFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_class_methodFlags* outFlags)
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
	memset(outFlags, 0, sizeof(*outFlags));
	if ((rawFlags & SJME_CLASS_ACC_PUBLIC) != 0)
		outFlags->member.access.public = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_PRIVATE) != 0)
		outFlags->member.access.private = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_PROTECTED) != 0)
		outFlags->member.access.protected = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_STATIC) != 0)
		outFlags->member.isStatic = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_FINAL) != 0)
		outFlags->member.final = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_SYNCHRONIZED) != 0)
		outFlags->synchronized = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_BRIDGE) != 0)
		outFlags->bridge = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_VARARGS) != 0)
		outFlags->varargs = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_NATIVE) != 0)
		outFlags->native = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_ABSTRACT) != 0)
		outFlags->abstract = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_STRICTFP) != 0)
		outFlags->strictfp = SJME_JNI_TRUE;
	if ((rawFlags & SJME_CLASS_ACC_SYNTHETIC) != 0)
		outFlags->member.synthetic = SJME_JNI_TRUE;
	
	/* Can only have a single access mode. */
	/* Abstract cannot be final, private, static, strict, or synchronized. */
	if (((outFlags->member.access.public +
		outFlags->member.access.protected +
		outFlags->member.access.private) > 1) ||
		(outFlags->abstract && (outFlags->member.final ||
			outFlags->native || outFlags->member.access.private ||
			outFlags->member.isStatic || outFlags->strictfp ||
			outFlags->synchronized)))
		return SJME_ERROR_INVALID_METHOD_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_class_parseAttribute(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull const sjme_class_parseAttributeHandlerInfo* handlers,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error, errorC;
	sjme_jubyte* attrData;
	sjme_jint readCount;
	const sjme_class_parseAttributeHandlerInfo* at;
	sjme_stream_input attrStream;
	
	if (inPool == NULL || inStream == NULL || inConstPool == NULL ||
		inStringPool == NULL || handlers == NULL || context == NULL ||
		attrName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (attrLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Allocate buffer to read in the data. */
	attrData = sjme_alloca(attrLen);
	if (attrData == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(attrData, 0, attrLen);
	
	/* Read in everything. */
	readCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		inStream, &readCount, attrData, attrLen)) ||
		readCount < 0)
		return sjme_error_default(error);
	
	/* Find and call handler for this. */
	for (at = handlers; at->name != NULL && at->handler != NULL; at++)
		if (0 == strcmp(at->name, attrName))
		{
			/* Load stream over the data. */
			attrStream = NULL;
			if (sjme_error_is(error = sjme_stream_inputOpenMemory(
				inPool, &attrStream,
				attrData, attrLen)) || attrStream == NULL)
				return sjme_error_default(error);
			
			/* Process it. */
			error = at->handler(inPool, inConstPool, inStringPool, context,
				attrName, attrStream, attrData, attrLen);
			
			/* Close stream. */
			if (sjme_error_is(errorC = sjme_closeable_close(
				SJME_AS_CLOSEABLE(attrStream))))
				return sjme_error_defaultOr(error, errorC);
			
			/* Failed? */
			if (sjme_error_is(error))
				return sjme_error_default(error);
			return error;
		}
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("No handler for attribute %s.", attrName);
#endif
	
	/* None found, so ignore it. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_class_parse(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrOutNotNull sjme_class_info* outClass)
{
	sjme_errorCode error;
	sjme_jint magic, fullVersion, i;
	sjme_jshort major, minor, interfaceCount, fieldCount, methodCount;
	sjme_class_version actualVersion;
	sjme_class_poolInfo pool;
	sjme_class_info result;
	sjme_class_poolEntry* thisName;
	sjme_class_poolEntry* superName;
	sjme_class_poolEntry* interfaceName;
	sjme_list_sjme_stringPool_string* interfaceNames;
	sjme_list_sjme_class_fieldInfo* fields;
	sjme_list_sjme_class_methodInfo* methods;
	
	if (inPool == NULL || inStream == NULL || inStringPool == NULL ||
		outClass == NULL)
		return SJME_ERROR_NONE;
	
	/* Make sure we can actually allocate the resultant class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inPool,
		sizeof(*result), SJME_NVM_STRUCT_CLASS_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
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
	
	/* Set version. */
	result->version = actualVersion;
	
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
	if (sjme_error_is(error = sjme_class_classFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
	
	/* Read in this name. */
	thisName = NULL;
	if (sjme_error_is(error = sjme_class_readPoolRefIndex(
		inStream, result->pool,
		SJME_CLASS_POOL_TYPE_CLASS,
		SJME_JNI_FALSE, &thisName)) || thisName == NULL)
		goto fail_readThisName;
	
	/* Reference it. */
	result->name = thisName->classRef.descriptor;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->name, NULL)))
		goto fail_refThisName;
	
	/* Read in super name. */
	superName = NULL;
	if (sjme_error_is(error = sjme_class_readPoolRefIndex(
		inStream, result->pool,
		SJME_CLASS_POOL_TYPE_CLASS,
		SJME_JNI_TRUE, &superName)))
		goto fail_readSuperName;
	
	/* Reference it, if valid. */
	if (superName != NULL)
	{
		result->superName = superName->classRef.descriptor;
		if (sjme_error_is(error = sjme_alloc_weakRef(
			result->superName, NULL)))
			goto fail_refSuperName;
	}
	
	/* How many interfaces are there? */
	interfaceCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &interfaceCount)) || interfaceCount < 0)
		goto fail_readInterfaceCount;
	
	/* Allocate interfaces count. */
	interfaceNames = NULL;
	if (sjme_error_is(error = sjme_list_alloc(inPool,
		interfaceCount, &interfaceNames, sjme_stringPool_string, 0)) ||
		interfaceNames == NULL)
		goto fail_allocInterfaceNames;
	result->interfaceNames = interfaceNames;
	
	/* Read in all interfaces. */
	for (i = 0; i < interfaceCount; i++)
	{
		/* Read in name. */
		interfaceName = NULL;
		if (sjme_error_is(error = sjme_class_readPoolRefIndex(
			inStream, result->pool,
			SJME_CLASS_POOL_TYPE_CLASS,
			SJME_JNI_FALSE, &interfaceName)) ||
			interfaceName == NULL)
			goto fail_readThisName;
		
		/* Reference it. */
		interfaceNames->elements[i] = interfaceName->classRef.descriptor;
		if (sjme_error_is(error = sjme_alloc_weakRef(
			interfaceNames->elements[i], NULL)))
			goto fail_refThisName;
	}
	
	/* Read in field count. */
	fieldCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &fieldCount)) || fieldCount < 0)
		goto fail_readFieldCount;
	
	/* Setup list to store fields in. */
	fields = NULL;
	if (sjme_error_is(error = sjme_list_alloc(inPool,
		fieldCount, &fields, sjme_class_fieldInfo, 0)) || fields == NULL)
		goto fail_allocFields;
	result->fields = fields;
	
	/* Load in and process each field. */
	for (i = 0; i < fieldCount; i++)
	{
		/* Parse each field. */
		if (sjme_error_is(error = sjme_class_parseField(
			inPool, inStream, result->pool, inStringPool,
			&fields->elements[i])) ||
			fields->elements[i] == NULL)
			goto fail_parseField;
		
		/* We are referencing this. */
		if (sjme_error_is(error = sjme_alloc_weakRef(
			fields->elements[i], NULL)))
			goto fail_refField;
	}
	
	/* Read in method count. */
	methodCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &methodCount)) || methodCount < 0)
		goto fail_readMethodCount;
	
	/* Setup list to store methods in. */
	methods = NULL;
	if (sjme_error_is(error = sjme_list_alloc(inPool,
		methodCount, &methods, sjme_class_methodInfo, 0)) || methods == NULL)
		goto fail_allocMethods;
	result->methods = methods;
	
	/* Load in and process each method. */
	for (i = 0; i < methodCount; i++)
	{
		/* Parse each method. */
		if (sjme_error_is(error = sjme_class_parseMethod(
			inPool, inStream, result->pool, inStringPool,
			&methods->elements[i])) ||
			methods->elements[i] == NULL)
			goto fail_parseMethod;
		
		/* Reference as we are using this. */
		if (sjme_error_is(error = sjme_alloc_weakRef(
			methods->elements[i], NULL)))
			goto fail_refMethod;
	}
	
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_class_parseAttributes(
		inPool, inStream, result->pool, inStringPool,
		sjme_class_classAttr, result)))
		goto fail_parseAttributes;
	
	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;

fail_parseAttributes:
fail_refMethod:
fail_parseMethod:
fail_allocMethods:
fail_readMethodCount:
fail_refField:
fail_parseField:
fail_allocFields:
fail_readFieldCount:
fail_allocInterfaceNames:
fail_readInterfaceCount:
fail_refSuperName:
fail_readSuperName:
fail_refThisName:
fail_readThisName:
fail_readFlags:
fail_countPool:
fail_parsePool:
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

sjme_errorCode sjme_class_parseAttributes(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull const sjme_class_parseAttributeHandlerInfo* handlers,
	sjme_attrInNotNull sjme_pointer context)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint i, len;
	sjme_class_poolEntry* name;
	
	if (inPool == NULL || inStream == NULL || inConstPool == NULL ||
		inStringPool == NULL || handlers == NULL || context == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in count. */
	count = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &count)) || count < 0)
		goto fail_readCount;
	
	/* Read individual attributes. */
	for (i = 0; i < count; i++)
	{
		/* Read in name. */
		name = NULL;
		if (sjme_error_is(error = sjme_class_readPoolRefIndex(
			inStream, inConstPool,
			SJME_CLASS_POOL_TYPE_UTF,
			SJME_JNI_FALSE, &name)) || name == NULL)
			goto fail_readName;
		
		/* Read in length. */
		len = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJI(
			inStream, &len)) || len < 0)
			goto fail_readLen;
		
		/* Stage it for stack allocations. */
		if (sjme_error_is(error = sjme_class_parseAttribute(
			inPool, inStream, inConstPool, inStringPool, handlers,
			context, (sjme_lpcstr)&name->utf.utf->chars[0],
			len)))
			goto fail_parseSingle;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_parseSingle:
fail_readLen:
fail_readName:
fail_readCount:
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
	sjme_class_poolEntry* target;
	sjme_stringPool_string utf;
	sjme_class_poolInfo result;
	
	if (inPool == NULL || inStream == NULL || outPool == NULL ||
		inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we can actually allocate this. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inPool,
		sizeof(*result), SJME_NVM_STRUCT_POOL,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
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
	result->pool = entries;
	
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
				/* Class reference. */
			case SJME_CLASS_POOL_TYPE_CLASS:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->classRef.descriptorIndex)))
					goto fail_readItem;
				break;
				
				/* Double value. */
			case SJME_CLASS_POOL_TYPE_DOUBLE:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constDouble.value.hi)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constDouble.value.lo)))
					goto fail_readItem;
				
				/* Skip wide index. */
				index++;
				break;
			
				/* Reference to a member. */
			case SJME_CLASS_POOL_TYPE_FIELD:
			case SJME_CLASS_POOL_TYPE_INTERFACE_METHOD:
			case SJME_CLASS_POOL_TYPE_METHOD:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->member.inClassIndex)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->member.nameAndTypeIndex)))
					goto fail_readItem;
				break;
				
				/* Float value. */
			case SJME_CLASS_POOL_TYPE_FLOAT:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constFloat.value.value)))
					goto fail_readItem;
				break;
				
				/* Integer value. */
			case SJME_CLASS_POOL_TYPE_INTEGER:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constInteger.value)))
					goto fail_readItem;
				break;
				
				/* Long value. */
			case SJME_CLASS_POOL_TYPE_LONG:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constLong.value.part.hi)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constLong.value.part.lo)))
					goto fail_readItem;
				
				/* Skip wide index. */
				index++;
				break;
				
				/* Name and type information. */
			case SJME_CLASS_POOL_TYPE_NAME_AND_TYPE:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->nameAndType.nameIndex)))
					goto fail_readItem;
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->nameAndType.descriptorIndex)))
					goto fail_readItem;
				break;
				
				/* Constant string. */
			case SJME_CLASS_POOL_TYPE_STRING:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->constString.valueIndex)))
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
	for (index = 1; index < count - 1; index++)
	{
		/* Which entry is being initialized? */
		entry = &entries->elements[index];
		
		/* Initialize accordingly. */
		switch (entry->type)
		{
				/* These are base elements that need no initialization. */
			case SJME_CLASS_POOL_TYPE_UTF:
			case SJME_CLASS_POOL_TYPE_INTEGER:
			case SJME_CLASS_POOL_TYPE_FLOAT:
				break;
				
				/* Skip wide element. */
			case SJME_CLASS_POOL_TYPE_LONG:
			case SJME_CLASS_POOL_TYPE_DOUBLE:
				index++;
				break;
			
				/* Class type. */
			case SJME_CLASS_POOL_TYPE_CLASS:
				if (entry->classRef.descriptorIndex <= 0 ||
					entry->classRef.descriptorIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a UTF string. */
				target = &entries->elements[entry->classRef.descriptorIndex];
				if (target->type != SJME_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Refer to it and count up, since we are using it. */
				entry->classRef.descriptor = target->utf.utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(
					entry->classRef.descriptor, NULL)))
					goto fail_initItem;
				break;
				
				/* Member reference. */
			case SJME_CLASS_POOL_TYPE_FIELD:
			case SJME_CLASS_POOL_TYPE_INTERFACE_METHOD:
			case SJME_CLASS_POOL_TYPE_METHOD:
				if (entry->member.inClassIndex <= 0 ||
					entry->member.inClassIndex >= entries->length ||
					entry->member.nameAndTypeIndex <= 0 ||
					entry->member.nameAndTypeIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a class. */
				target = &entries->elements[entry->member.inClassIndex];
				if (target->type != SJME_CLASS_POOL_TYPE_CLASS)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set class. */
				entry->member.inClass =
					(const sjme_class_poolEntryClass*)target;
				
				/* Needs to be a name and type. */
				target = &entries->elements[entry->member.nameAndTypeIndex];
				if (target->type != SJME_CLASS_POOL_TYPE_NAME_AND_TYPE)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set name and type. */
				entry->member.nameAndType =
					(const sjme_class_poolEntryNameAndType*)target;
				break;
			
			case SJME_CLASS_POOL_TYPE_NAME_AND_TYPE:
				if (entry->nameAndType.nameIndex <= 0 ||
					entry->nameAndType.nameIndex >= entries->length ||
					entry->nameAndType.descriptorIndex <= 0 ||
					entry->nameAndType.descriptorIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be UTF. */
				target = &entries->elements[entry->nameAndType.nameIndex];
				if (target->type != SJME_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set name. */
				entry->nameAndType.name = target->utf.utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(
					entry->nameAndType.name, NULL)))
					goto fail_initItem;
				
				/* Needs to be UTF. */
				target = &entries->elements[
					entry->nameAndType.descriptorIndex];
				if (target->type != SJME_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set descriptor. */
				entry->nameAndType.descriptor = target->utf.utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(
					entry->nameAndType.descriptor, NULL)))
					goto fail_initItem;
				break;
				
				/* Constant string. */
			case SJME_CLASS_POOL_TYPE_STRING:
				if (entry->constString.valueIndex <= 0 ||
					entry->constString.valueIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a UTF string. */
				target = &entries->elements[entry->constString.valueIndex];
				if (target->type != SJME_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Refer to it and count up, since we are using it. */
				entry->constString.value = target->utf.utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(
					entry->constString.value, NULL)))
					goto fail_initItem;
				break;
			
			default:
				sjme_todo("Impl? %d", tag);
				return SJME_ERROR_NOT_IMPLEMENTED;
		}
	}
	
	/* Success! */
	*outPool = result;
	return SJME_ERROR_NONE;

fail_initItem:
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

sjme_errorCode sjme_class_parseField(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrOutNotNull sjme_class_fieldInfo* outField)
{
	sjme_errorCode error;
	sjme_class_fieldInfo result;
	sjme_class_poolEntry* name;
	sjme_class_poolEntry* type;
	
	if (inPool == NULL || inStream == NULL || inConstPool == NULL ||
		outField == NULL || inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ensure we can allocate the result first. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inPool,
		sizeof(*result), SJME_NVM_STRUCT_FIELD_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_class_fieldFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
		
	/* Read in name. */
	name = NULL;
	if (sjme_error_is(error = sjme_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &name)) || name == NULL)
		goto fail_readName;
	
	/* Reference it. */
	result->name = name->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->name, NULL)))
		goto fail_refName;
		
	/* Read in type. */
	type = NULL;
	if (sjme_error_is(error = sjme_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &type)) || name == NULL)
		goto fail_readType;
	
	/* Reference it. */
	result->type = name->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->type, NULL)))
		goto fail_refType;
		
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_class_parseAttributes(
		inPool, inStream, inConstPool, inStringPool,
		sjme_class_fieldAttr, result)))
		goto fail_parseAttributes;
	
	/* Success! */
	*outField = result;
	return SJME_ERROR_NONE;
	
fail_parseAttributes:
fail_refType:
fail_readType:
fail_refName:
fail_readName:
fail_readFlags:
fail_initResult:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

sjme_errorCode sjme_class_parseMethod(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInOutNotNull sjme_class_methodInfo* outMethod)
{
	sjme_errorCode error;
	sjme_class_methodInfo result;
	sjme_class_poolEntry* name;
	sjme_class_poolEntry* type;
	
	if (inPool == NULL || inStream == NULL || inConstPool == NULL ||
		outMethod == NULL || inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ensure we can allocate the result first. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inPool,
		sizeof(*result), SJME_NVM_STRUCT_METHOD_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_class_methodFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
		
	/* Read in name. */
	name = NULL;
	if (sjme_error_is(error = sjme_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &name)) || name == NULL)
		goto fail_readName;
	
	/* Reference it. */
	result->name = name->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->name, NULL)))
		goto fail_refName;
		
	/* Read in type. */
	type = NULL;
	if (sjme_error_is(error = sjme_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &type)) || name == NULL)
		goto fail_readType;
	
	/* Reference it. */
	result->type = name->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->type, NULL)))
		goto fail_refType;
		
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_class_parseAttributes(
		inPool, inStream, inConstPool, inStringPool,
		sjme_class_methodAttr, result)))
		goto fail_parseAttributes;
	
	/* Success! */
	*outMethod = result;
	return SJME_ERROR_NONE;
	
fail_parseAttributes:
fail_refType:
fail_readType:
fail_refName:
fail_readName:
fail_readFlags:
fail_initResult:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}
