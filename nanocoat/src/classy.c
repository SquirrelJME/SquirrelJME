/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/task.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/classy.h"
#include "sjme/debug.h"
#include "sjme/util.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/cleanup.h"

/** The magic number for classes. */
#define SJME_NVM_CLASS_MAGIC INT32_C(0xCAFEBABE)

/** CLDC 1.1 max version (JSR 30). */
#define SJME_NVM_CLASS_CLDC_1_0_MAX INT32_C(3080191)

/** CLDC 1.1 max version. (JSR 139). */
#define SJME_NVM_CLASS_CLDC_1_1_MAX INT32_C(3342335)

/** CLDC 8 max version. */
#define SJME_NVM_CLASS_CLDC_1_8_MAX INT32_C(3407872)

static sjme_errorCode sjme_nvm_class_readPoolRefIndex(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inClassPool,
	sjme_attrInPositiveNonZero sjme_nvm_class_poolType desireType,
	sjme_attrInValue sjme_jboolean canNull,
	sjme_attrOutNotNull sjme_nvm_class_poolEntry** outEntry)
{
	sjme_errorCode error;
	sjme_jshort index;
	sjme_nvm_class_poolEntry* result;
	
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

static const sjme_nvm_class_parseAttributeHandler sjme_nvm_class_classAttr[] =
{
	{NULL, NULL},
};

static sjme_errorCode sjme_nvm_class_classFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_nvm_class_classFlags* outFlags)
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
	if ((rawFlags & SJME_NVM_ACC_PUBLIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_PUBLIC;
	if ((rawFlags & SJME_NVM_ACC_FINAL) != 0)
		(*outFlags) |= SJME_NVM_ACC_FINAL;
	if ((rawFlags & SJME_NVM_ACC_SUPER) != 0)
		(*outFlags) |= SJME_NVM_ACC_SUPER;
	if ((rawFlags & SJME_NVM_ACC_INTERFACE) != 0)
		(*outFlags) |= SJME_NVM_ACC_INTERFACE;
	if ((rawFlags & SJME_NVM_ACC_ABSTRACT) != 0)
		(*outFlags) |= SJME_NVM_ACC_ABSTRACT;
	if ((rawFlags & SJME_NVM_ACC_SYNTHETIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_SYNTHETIC;
	if ((rawFlags & SJME_NVM_ACC_ANNOTATION) != 0)
		(*outFlags) |= SJME_NVM_ACC_ANNOTATION;
	if ((rawFlags & SJME_NVM_ACC_ENUM) != 0)
		(*outFlags) |= SJME_NVM_ACC_ENUM;
	
	/* Cannot be abstract and final. */
	if (((*outFlags) & (SJME_NVM_ACC_ABSTRACT | SJME_NVM_ACC_FINAL)) ==
		(SJME_NVM_ACC_ABSTRACT | SJME_NVM_ACC_FINAL))
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Annotation must be an interface. */
	if (((*outFlags) & (SJME_NVM_ACC_ANNOTATION | SJME_NVM_ACC_INTERFACE)) ==
		SJME_NVM_ACC_ANNOTATION)
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Interface must be abstract and not final, super, or enum */
	if (((*outFlags) & (SJME_NVM_ACC_INTERFACE)) != 0)
		if (((*outFlags) & (SJME_NVM_ACC_ABSTRACT | SJME_NVM_ACC_FINAL |
			SJME_NVM_ACC_SUPER | SJME_NVM_ACC_ENUM)) != SJME_NVM_ACC_ABSTRACT)
		return SJME_ERROR_INVALID_CLASS_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_codeAttrLineNumberTable(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_nvm_class_codeAttrStackMap(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_nvm_class_codeAttrStackMapTable(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_lpcstr attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static const sjme_nvm_class_parseAttributeHandler sjme_nvm_class_codeAttr[] =
{
#if 0
	{"LineNumberTable",
		sjme_nvm_class_codeAttrLineNumberTable},
	{"StackMap",
		sjme_nvm_class_codeAttrStackMap},
	{"StackMapTable",
		sjme_nvm_class_codeAttrStackMapTable},
#endif
	{NULL, NULL},
};

static sjme_errorCode sjme_nvm_class_fieldAttrConstantValue(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error;
	sjme_nvm_class_fieldInfo fieldInfo;
	sjme_jshort index;
	sjme_nvm_class_poolEntry* item;
	
	fieldInfo = context;
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
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
	if (item->type == SJME_NVM_CLASS_POOL_TYPE_INTEGER)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_INTEGER;
		fieldInfo->constVal.value.java.i = item->constInteger.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_FLOAT)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_FLOAT;
		fieldInfo->constVal.value.java.f = item->constFloat.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_LONG)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_LONG;
		fieldInfo->constVal.value.java.j = item->constLong.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_DOUBLE)
	{
		fieldInfo->constVal.type = SJME_JAVA_TYPE_ID_DOUBLE;
		fieldInfo->constVal.value.java.d = item->constDouble.value;
	}
	else if (item->type == SJME_NVM_CLASS_POOL_TYPE_STRING)
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

static const sjme_nvm_class_parseAttributeHandler sjme_nvm_class_fieldAttr[] =
{
	{"ConstantValue", sjme_nvm_class_fieldAttrConstantValue},
	{NULL, NULL},
};

static sjme_errorCode sjme_nvm_class_fieldFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_nvm_class_fieldFlags* outFlags)
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
	if ((rawFlags & SJME_NVM_ACC_PUBLIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_PUBLIC;
	if ((rawFlags & SJME_NVM_ACC_PRIVATE) != 0)
		(*outFlags) |= SJME_NVM_ACC_PRIVATE;
	if ((rawFlags & SJME_NVM_ACC_PROTECTED) != 0)
		(*outFlags) |= SJME_NVM_ACC_PROTECTED;
	if ((rawFlags & SJME_NVM_ACC_STATIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_STATIC;
	if ((rawFlags & SJME_NVM_ACC_FINAL) != 0)
		(*outFlags) |= SJME_NVM_ACC_FINAL;
	if ((rawFlags & SJME_NVM_ACC_VOLATILE) != 0)
		(*outFlags) |= SJME_NVM_ACC_VOLATILE;
	if ((rawFlags & SJME_NVM_ACC_TRANSIENT) != 0)
		(*outFlags) |= SJME_NVM_ACC_TRANSIENT;
	if ((rawFlags & SJME_NVM_ACC_ENUM) != 0)
		(*outFlags) |= SJME_NVM_ACC_ENUM;
	if ((rawFlags & SJME_NVM_ACC_SYNTHETIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_SYNTHETIC;
	
	/* Can only have a single access mode. */
	if (sjme_util_intBitCountU((*outFlags) & SJME_NVM_ACC_ACCESS_MASK) > 1)
		return SJME_ERROR_INVALID_FIELD_FLAGS;
	
	/* Cannot be both final and volatile. */
	if (((*outFlags) & (SJME_NVM_ACC_FINAL | SJME_NVM_ACC_VOLATILE)) ==
		(SJME_NVM_ACC_FINAL | SJME_NVM_ACC_VOLATILE))
		return SJME_ERROR_INVALID_FIELD_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_methodAttrCodeOpLenVerify(
	sjme_attrInNotNull sjme_byteCode* rawCode,
	sjme_attrInPositiveNonZero sjme_jint codeLen)
{
	sjme_byteCode* ev;
	sjme_byteCode* oldEv;
	sjme_byteCode iv;
	sjme_byteCode* endCode;
	sjme_nvm_byteCode_pcNew pcNew;
	sjme_nvm_frameBase fakeFrame;
#if defined(SJME_CONFIG_DEBUG)
	sjme_byteCode lastIv;
#endif
	
	if (rawCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (codeLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Calculate the end code address. */
	endCode = SJME_POINTER_OFFSET(rawCode, codeLen);

	/* Setup fake frame for length calculation. */
	memset(&fakeFrame, 0, sizeof(fakeFrame));

	/* Go through and check addresses. */
	memset(&pcNew, 0, sizeof(pcNew));
	for (ev = oldEv = rawCode; ev < endCode; oldEv = ev)
	{
		/* Determine IV. */
		iv = *ev;
		
		/* This must always refer to a slow instruction, as byte codes are */
		/* always pre-JIT. */
		if (sjme_nvm_byteCode_lutTable[iv] !=
			&sjme_nvm_byteCode_slowNarrowFunctions)
			return SJME_ERROR_CLASS_VERIFY_BAD_INSTRUCTION;

		/* Determine instruction length. */
		pcNew.adjust = sjme_nvm_byteCode_lengths[iv];
		if (pcNew.adjust < 0)
		{
			/* Calculate new length. */
			fakeFrame.pc = (sjme_intPointer)ev - (sjme_intPointer)rawCode;
			if (sjme_error_is(sjme_nvm_byteCode_calcLength(
				&fakeFrame, iv, ev, &pcNew)))
				return SJME_ERROR_CLASS_VERIFY_BAD_INSTRUCTION_LENGTH;

			/* Still not valid? */
			if (pcNew.adjust < 0)
				return SJME_ERROR_CLASS_VERIFY_BAD_INSTRUCTION_LENGTH;
		}

		/* Would exceed code bounds? */
		ev = SJME_POINTER_OFFSET(ev, pcNew.adjust);
		if (ev > endCode || ev <= oldEv)
			return SJME_ERROR_CLASS_VERIFY_BAD_INSTRUCTION_LENGTH;

#if defined(SJME_CONFIG_DEBUG)
		/* For debugging. */
		lastIv = iv;
#endif
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_methodAttrCode(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInNotNullBuf(attrLen) sjme_pointer attrData,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error;
	sjme_jshort maxStack, maxLocals, numExcept;
	sjme_jint codeLen, i, j, actualCodeLen;
	sjme_nvm_class_methodInfo methodInfo;
	sjme_nvm_class_codeInfo result;
	sjme_jubyte* rawCode;
	sjme_jubyte* rawCodeUnalign;
	sjme_list(sjme_nvm_class_exceptionHandler)* excepts;
	sjme_nvm_class_exceptionHandler* except;
	sjme_nvm_class_codePerType* perType;
	sjme_jshort* localMap;
	
	methodInfo = context;
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		context == NULL || attrName == NULL || attrData == NULL ||
		attrStream == NULL || methodInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can only have one. */
	if (methodInfo->code != NULL)
		return SJME_ERROR_METHOD_MULTIPLE_CODE;
	
	/* Make sure we can allocate this. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
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

	/* Allocate full local map storage. */
	localMap = NULL;
	if (sjme_error_is(error = sjme_alloc(allocPool,
		sizeof(*localMap) * ((maxLocals * SJME_NUM_JAVA_TYPE_IDS) + 1),
		(sjme_pointer)&localMap)) || localMap == NULL)
		goto fail_allocLocalMap;
	
	/* Set. */
	result->perType[SJME_NVM_CODE_INFO_ALL_TYPES].stack = maxStack;
	result->perType[SJME_NVM_CODE_INFO_ALL_TYPES].locals = maxLocals;

	/* Build local and stack counts. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("TODO: Use proper per-type counts.");
#endif
	for (i = 0; i < SJME_NVM_CODE_INFO_ALL_TYPES; i++)
	{
		perType = &result->perType[i];
		
		/* TODO: For now just set all types to the same. */
		*perType = result->perType[SJME_NVM_CODE_INFO_ALL_TYPES];

		/* Local map is always set to specific local indexes per type. */
		perType->localMap = &localMap[maxLocals * i];
		for (j = 0; j < maxLocals; j++)
			perType->localMap[j] = j;
	}
	
	/* Read in code length. */
	codeLen = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		attrStream, &codeLen)) || codeLen <= 0)
		goto fail_readCodeLen;
	
	/* Allocate, processing requires this to be aligned so align it on */
	/* the stack. */
	rawCodeUnalign = sjme_alloca(codeLen + SJME_POINTER_BYTES);
	if (rawCodeUnalign == NULL)
	{
		error = SJME_ERROR_OUT_OF_MEMORY;
		goto fail_allocRawCode;
	}
	rawCode = sjme_util_alignToP(rawCodeUnalign, SJME_POINTER_BYTES);
	memset(rawCode, 0, codeLen);
	
	/* Read in code. */
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		attrStream, &actualCodeLen,
		rawCode, codeLen)) ||
		actualCodeLen != codeLen)
		goto fail_readRawCode;

	/* Perform basic length and type verification of code. */
	if (sjme_error_is(error = sjme_nvm_class_methodAttrCodeOpLenVerify(
		rawCode, codeLen)))
		goto fail_opLenVerify;
	
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
		if (sjme_error_is(error = sjme_list_alloc(allocPool,
			numExcept, &excepts, sjme_nvm_class_exceptionHandler, 0)) ||
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
			if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
				attrStream, inConstPool,
				SJME_NVM_CLASS_POOL_TYPE_CLASS,
				SJME_JNI_TRUE,
				(sjme_nvm_class_poolEntry**)&except->handles)))
				goto fail_exceptHandles;
		}
	}
	
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_nvm_class_parseAttributes(
		allocPool, attrStream, inConstPool, inStringPool,
		sjme_nvm_class_codeAttr, result)))
		goto fail_parseAttributes;
	
	/* Allocate code. */
	result->rawCodeLen = codeLen;
	if (sjme_error_is(error = sjme_alloc_copy(
		allocPool, codeLen, (sjme_pointer*)&result->rawCode, rawCode)) ||
		result->rawCode == NULL)
		goto fail_allocCode;
	
	/* Make sure the code is referenced. */	
	methodInfo->code = result;
	if (sjme_error_is(error = sjme_alloc_weakRef(result, NULL)))
		goto fail_refCode;
	
	/* Success! */
	return SJME_ERROR_NONE;
fail_refCode:
fail_allocCode:
	if (result->rawCode != NULL)
	{
		sjme_alloc_free(result->rawCode);
		result->rawCode = NULL;
	}
fail_parseAttributes:
fail_exceptHandles:
fail_exceptShorts:
fail_allocExcepts:
fail_readNumExcept:
fail_opLenVerify:
fail_readRawCode:
fail_allocRawCode:
fail_readCodeLen:
fail_allocLocalMap:
	if (localMap != NULL)
		sjme_alloc_free(localMap);
fail_readMaxLocals:
fail_readMaxStack:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

static const sjme_nvm_class_parseAttributeHandler sjme_nvm_class_methodAttr[] =
{
	{"Code", sjme_nvm_class_methodAttrCode},
	{NULL, NULL},
};

static sjme_errorCode sjme_nvm_class_methodFlagsParse(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_nvm_class_methodFlags* outFlags)
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
	if ((rawFlags & SJME_NVM_ACC_PUBLIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_PUBLIC;
	if ((rawFlags & SJME_NVM_ACC_PRIVATE) != 0)
		(*outFlags) |= SJME_NVM_ACC_PRIVATE;
	if ((rawFlags & SJME_NVM_ACC_PROTECTED) != 0)
		(*outFlags) |= SJME_NVM_ACC_PROTECTED;
	if ((rawFlags & SJME_NVM_ACC_STATIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_STATIC;
	if ((rawFlags & SJME_NVM_ACC_FINAL) != 0)
		(*outFlags) |= SJME_NVM_ACC_FINAL;
	if ((rawFlags & SJME_NVM_ACC_SYNCHRONIZED) != 0)
		(*outFlags) |= SJME_NVM_ACC_SYNCHRONIZED;
	if ((rawFlags & SJME_NVM_ACC_BRIDGE) != 0)
		(*outFlags) |= SJME_NVM_ACC_BRIDGE;
	if ((rawFlags & SJME_NVM_ACC_VARARGS) != 0)
		(*outFlags) |= SJME_NVM_ACC_VARARGS;
	if ((rawFlags & SJME_NVM_ACC_NATIVE) != 0)
		(*outFlags) |= SJME_NVM_ACC_NATIVE;
	if ((rawFlags & SJME_NVM_ACC_ABSTRACT) != 0)
		(*outFlags) |= SJME_NVM_ACC_ABSTRACT;
	if ((rawFlags & SJME_NVM_ACC_STRICTFP) != 0)
		(*outFlags) |= SJME_NVM_ACC_STRICTFP;
	if ((rawFlags & SJME_NVM_ACC_SYNTHETIC) != 0)
		(*outFlags) |= SJME_NVM_ACC_SYNTHETIC;
	
	/* Can only have a single access mode. */
	if (sjme_util_intBitCountU((*outFlags) & SJME_NVM_ACC_ACCESS_MASK) > 1)
		return SJME_ERROR_INVALID_METHOD_FLAGS;
	
	/* Abstract cannot be final, private, static, strict, or synchronized. */
	if (SJME_NVM_ACC_IS(*outFlags, ABSTRACT))
		if (((*outFlags) & (SJME_NVM_ACC_ABSTRACT | SJME_NVM_ACC_FINAL |
			SJME_NVM_ACC_PRIVATE | SJME_NVM_ACC_STATIC |
			SJME_NVM_ACC_STRICTFP |
			SJME_NVM_ACC_SYNCHRONIZED)) != SJME_NVM_ACC_ABSTRACT)
			return SJME_ERROR_INVALID_METHOD_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_class_parseAttribute(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error, errorC;
	sjme_jubyte* attrData;
	sjme_jint readCount;
	const sjme_nvm_class_parseAttributeHandler* at;
	sjme_stream_input attrStream;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
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
		if (sjme_charSeq_equalsUtfR(attrName, at->name))
		{
			/* Load stream over the data. */
			attrStream = NULL;
			if (sjme_error_is(error = sjme_stream_inputOpenMemory(
				allocPool, &attrStream,
				attrData, attrLen)) || attrStream == NULL)
				return sjme_error_default(error);
			
			/* Process it. */
			error = at->handler(allocPool, inConstPool, inStringPool, context,
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
	
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("No handler for attribute %s.", attrName);
#endif
	
	/* None found, so ignore it. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_calcMethodArgs(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_charSeq typeDesc,
	sjme_attrInNotNull sjme_jint* outArgC,
	sjme_attrInNotNull sjme_javaTypeId** outArgT,
	sjme_attrInNotNull sjme_javaTypeId* outArgR)
{
#define SJME_MAX_ARGS 65
	sjme_errorCode error;
	sjme_javaTypeId args[SJME_MAX_ARGS];
	sjme_javaTypeId* result;
	sjme_cchar d;
	sjme_jint argAt, i, n;
	sjme_jboolean arrayScope, returnScope, returnDid;
	sjme_charSeq_it it;
	
	if (allocPool == NULL || typeDesc == NULL ||
		outArgC == NULL || outArgT == NULL || outArgR == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Quick void method, such as a default constructor. */
	if (sjme_charSeq_equalsUtfR(typeDesc, "()V"))
	{
		*outArgC = 0;
		*outArgT = NULL;
		*outArgR = SJME_JAVA_TYPE_ID_VOID;
		return SJME_ERROR_NONE;
	}

	/* Setup iterator. */
	memset(&it, 0, sizeof(it));
	if (sjme_error_is(error = sjme_charSeq_itNew(typeDesc, 0, &it)))
		return sjme_error_default(error);

	/* Must start with parenthesis. */
	if (it.pp(&it) != '(')
		return sjme_error_vmError(NULL, SJME_ERROR_INVALID_METHOD_TYPE);

	/* Init state. */
	argAt = 0;
	arrayScope = SJME_JNI_FALSE;
	returnScope = SJME_JNI_FALSE;
	returnDid = SJME_JNI_FALSE;
	memset(args, 0, sizeof(args));

	/* Argument handling loop. */
	for (;;)
	{
		/* Type would overflow? */
		if (argAt >= SJME_MAX_ARGS)
			return sjme_error_vmError(NULL,
				SJME_ERROR_INVALID_METHOD_TYPE);
		
		/* Which type? */
		switch (it.pp(&it))
		{
				/* Integer and promotions. */
			case 'Z':
			case 'B':
			case 'S':
			case 'C':
			case 'I':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare integer (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_INTEGER);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Long. */
			case 'J':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare long (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_LONG);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Float. */
			case 'F':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare float (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_FLOAT);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Double. */
			case 'D':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare double (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_DOUBLE);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Arrays. */
			case '[':
				/* Enter the array scope. */
				arrayScope = SJME_JNI_TRUE;
				break;

				/* Object. */
			case 'L':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
					
				}

				/* Keep going until ending `;`. */
				for (d = it.d(&it);; d = it.pp(&it))
				{
					/* Straight up invalid. */
					if (d == '.' || d == '[' || d == '\0')
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);

					/* End of type. */
					if (d == ';')
						break;
				}

				/* Declare object. */
				args[argAt++] = SJME_JAVA_TYPE_ID_OBJECT;
				arrayScope = SJME_JNI_FALSE;
				break;

				/* End of arguments. */
			case ')':
				/* Never valid for arrays or for return types. */
				if (returnScope || arrayScope)
					return sjme_error_vmError(NULL, 
						SJME_ERROR_INVALID_METHOD_TYPE);

				returnScope = SJME_JNI_TRUE;
				break;

				/* Void type. */
			case 'V':
				/* Only valid for return types and never arrays. */
				if (!returnScope || arrayScope)
					return sjme_error_vmError(NULL, 
						SJME_ERROR_INVALID_METHOD_TYPE);

				/* This is only ever the case for return types. */
				returnDid = SJME_JNI_TRUE;

				/* Set special void type. */
				args[argAt++] = SJME_JAVA_TYPE_ID_VOID;
				break;

				/* NUL is only invalid when not properly at the end. */
			case '\0':
				if (returnDid)
					break;
				return sjme_error_vmError(NULL,
					SJME_ERROR_INVALID_METHOD_TYPE);
				
				/* Invalid. */
			default:
				return sjme_error_vmError(NULL,
					SJME_ERROR_INVALID_METHOD_TYPE);
		}

		/* True end of descriptor, with NUL. */
		if (it.d(&it) == '\0')
			break;
	}

	/* Cannot end on array or miss a return type. */
	if (arrayScope || !returnScope || argAt <= 0)
		return sjme_error_vmError(NULL, SJME_ERROR_INVALID_METHOD_TYPE);

	/* Return type is always the last type. */
	*outArgR = args[argAt - 1];

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(allocPool,
		sizeof(*result) * argAt, (sjme_pointer*)&result)) || result == NULL)
		return sjme_error_default(error);

	/* Fill in other arguments. */
	for (i = 0, n = argAt - 1; i < n; i++)
		result[i] = args[i];

	/* Success! */
	*outArgC = n;
	*outArgT = result;
	return SJME_ERROR_NONE;
#undef SJME_MAX_ARGS
}

sjme_errorCode sjme_nvm_class_descriptorFieldSlots(
	sjme_attrInNotNull sjme_charSeq inDesc,
	sjme_attrOutNotNull sjme_jint* outSlots,
	sjme_attrInOutNullable sjme_jint* atP)
{
	sjme_jint at, result;
	sjme_jchar c;
	sjme_jboolean latched, done;
	
	if (inDesc == NULL || outSlots == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use base at or use new one? */
	at = (atP != NULL ? (*atP) : 0);

	/* Read in type character and process it. */
	result = -1;
	for (latched = done = SJME_JNI_FALSE; !done;)
	{
		c = sjme_charSeq_charAtR(inDesc, at++);
		switch (c)
		{
			/* Array. */
			case '[':
				/* Arrays are always single slot. */
				/* However for arrays, we need to skip the bracket and */
				/* handle more of them. */
				result = 1;
				latched = SJME_JNI_TRUE;
				break;

				/* Object. */
			case 'L':
				/* Find ending ;. */
				while (c != ';')
				{
					c = sjme_charSeq_charAtR(inDesc, at++);
					if (c == 0 || c == ')')
						return SJME_ERROR_INVALID_FIELD_TYPE;
				}
				
				if (!latched)
					result = 1;
				done = SJME_JNI_TRUE;
				break;
			
				/* Double slot. */
			case 'J':
			case 'D':
				if (!latched)
					result = 2;
				done = SJME_JNI_TRUE;
				break;
			
			/* Single slot. */
			case 'Z':
			case 'B':
			case 'S':
			case 'C':
			case 'I':
			case 'F':
				if (!latched)
					result = 1;
				done = SJME_JNI_TRUE;
				break;

				/* Void type. */
			case 'V':
				/* If latched, means this was an array of void. */
				if (latched)
					return SJME_ERROR_INVALID_FIELD_TYPE;
				result = 0;
				done = SJME_JNI_TRUE;
				break;

			default:
				return SJME_ERROR_INVALID_FIELD_TYPE;
		}
	}

	/* Should not occur. */
	if (result < 0)
		return SJME_ERROR_INVALID_FIELD_TYPE;

	/* Store resultant at? */
	if (atP != NULL)
		*atP = at;

	/* Return resultant values. */
	*outSlots = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_descriptorMethodSlots(
	sjme_attrInNotNull sjme_charSeq inDesc,
	sjme_attrOutNotNull sjme_jint* outArgSlots,
	sjme_attrOutNotNull sjme_jint* outRvSlots)
{
	sjme_errorCode error;
	sjme_jint total, at, sub;
	sjme_jchar c;
	
	if (inDesc == NULL || outArgSlots == NULL || outRvSlots == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Must start with parenthesis. */
	at = 0;
	if ('(' != sjme_charSeq_charAtR(inDesc, at++))
		return SJME_ERROR_INVALID_METHOD_TYPE;
	
	/* Read until ending parenthesis. */
	total = 0;
	for (;;)
	{
		/* Read in next character. */
		c = sjme_charSeq_charAtR(inDesc, at);

		/* Invalid? */
		if (c == 0)
			return SJME_ERROR_INVALID_METHOD_TYPE;

		/* End of arguments? */
		if (c == ')')
		{
			/* Bump up as reading the field descriptor increments this. */
			at++;
			break;
		}

		/* Count field slots. */
		sub = -1;
		if (sjme_error_is(error = sjme_nvm_class_descriptorFieldSlots(
			inDesc, &sub, &at)) || sub < 0)
			return sjme_error_defaultOr(error, SJME_ERROR_INVALID_METHOD_TYPE);

		/* Add up. */
		total += sub;
	}

	/* Count return value slots. */
	sub = -1;
	if (sjme_error_is(error = sjme_nvm_class_descriptorFieldSlots(
		inDesc, &sub, &at)) || sub < 0)
		return sjme_error_defaultOr(error, SJME_ERROR_INVALID_METHOD_TYPE);

	/* Success! */
	*outArgSlots = total;
	*outRvSlots = sub;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_descriptorToType(
	sjme_attrInNotNull sjme_charSeq desc,
	sjme_attrOutNullable sjme_javaTypeId* outJavaType,
	sjme_attrOutNullable sjme_basicTypeId* outBasicType,
	sjme_attrOutNullable sjme_extendedTypeId* outExtendedType)
{
	sjme_javaTypeId javaType;
	sjme_basicTypeId basicType;
	sjme_extendedTypeId extendedType;
	
	if (outJavaType == NULL || desc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sjme_charSeq_equalsUtfR(desc, "Z"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_BOOLEAN;
		extendedType = SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "B"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_BYTE;
		extendedType = SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "S"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_SHORT;
		extendedType = SJME_JAVA_TYPE_ID_SHORT_OR_CHAR;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "C"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_CHARACTER;
		extendedType = SJME_JAVA_TYPE_ID_SHORT_OR_CHAR;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "I"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_JAVA_TYPE_ID_INTEGER;
		extendedType = SJME_JAVA_TYPE_ID_INTEGER;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "J"))
	{
		javaType = SJME_JAVA_TYPE_ID_LONG;
		basicType = SJME_JAVA_TYPE_ID_LONG;
		extendedType = SJME_JAVA_TYPE_ID_LONG;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "F"))
	{
		javaType = SJME_JAVA_TYPE_ID_FLOAT;
		basicType = SJME_JAVA_TYPE_ID_FLOAT;
		extendedType = SJME_JAVA_TYPE_ID_FLOAT;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "D"))
	{
		javaType = SJME_JAVA_TYPE_ID_DOUBLE;
		basicType = SJME_JAVA_TYPE_ID_DOUBLE;
		extendedType = SJME_JAVA_TYPE_ID_DOUBLE;
	}
	else if (sjme_charSeq_charAtIs(desc, 0, '[') ||
		(sjme_charSeq_charAtIs(desc, 0, 'L') &&
			sjme_charSeq_charAtIs(desc, desc->length - 1,
				';')))
	{
		javaType = SJME_JAVA_TYPE_ID_OBJECT;
		basicType = SJME_JAVA_TYPE_ID_OBJECT;
		extendedType = SJME_JAVA_TYPE_ID_OBJECT;
	}
	
	/* Not valid. */
	else
		return sjme_error_vmError(NULL, SJME_ERROR_INVALID_METHOD_TYPE);
	
	/* Success! */
	if (outJavaType != NULL)
		*outJavaType = javaType;
	if (outBasicType != NULL)
		*outBasicType = basicType;
	if (outExtendedType != NULL)
		*outExtendedType = extendedType;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_parse(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_info* outClass)
{
#define MAX_RUNTIME_NAME 256
	sjme_errorCode error;
	sjme_jint magic, fullVersion, i, lastSlash;
	sjme_jshort major, minor, interfaceCount, fieldCount, methodCount;
	sjme_nvm_class_version actualVersion;
	sjme_nvm_class_poolInfo pool;
	sjme_nvm_class_info result;
	sjme_nvm_class_poolEntry* thisName;
	sjme_nvm_class_poolEntry* superName;
	sjme_nvm_class_poolEntry* interfaceName;
	sjme_list(sjme_nvm_stringPool_string)* interfaceNames;
	sjme_list(sjme_nvm_class_fieldInfo)* fields;
	sjme_list(sjme_nvm_class_methodInfo)* methods;
	sjme_nvm_class_fieldInfo field;
	sjme_nvm_class_methodInfo method;
	sjme_lpstr packageName;
	sjme_cchar runtimeName[MAX_RUNTIME_NAME];
	
	if (allocPool == NULL || inStream == NULL || inStringPool == NULL ||
		outClass == NULL)
		return SJME_ERROR_NONE;
	
	/* Make sure we can actually allocate the resultant class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_CLASS_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in magic number. */
	magic = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		inStream, &magic)))
		goto fail_readMagic;
	
	/* It must be valid! */
	if (magic != SJME_NVM_CLASS_MAGIC)
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
	if (fullVersion >= SJME_NVM_CLASS_CLDC_1_0 &&
		fullVersion <= SJME_NVM_CLASS_CLDC_1_0_MAX)
		actualVersion = SJME_NVM_CLASS_CLDC_1_0;
	else if (fullVersion >= SJME_NVM_CLASS_CLDC_1_1 &&
		fullVersion <= SJME_NVM_CLASS_CLDC_1_1_MAX)
		actualVersion = SJME_NVM_CLASS_CLDC_1_1;
	else if (fullVersion >= SJME_NVM_CLASS_CLDC_1_8 &&
		fullVersion <= SJME_NVM_CLASS_CLDC_1_8_MAX)
		actualVersion = SJME_NVM_CLASS_CLDC_1_8;
	
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
	if (sjme_error_is(error = sjme_nvm_class_parseConstantPool(
		allocPool, inStream, inStringPool, &pool)) || pool == NULL)
		goto fail_parsePool;
	
	/* We are using this, so count it up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(pool, NULL)))
		goto fail_countPool;
	result->pool = pool;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_nvm_class_classFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
	
	/* Read in this name. */
	thisName = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, result->pool,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		SJME_JNI_FALSE, &thisName)) || thisName == NULL)
		goto fail_readThisName;
	
	/* Reference it. */
	result->name = SJME_P_C_N(thisName);
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->name, NULL)))
		goto fail_refThisName;
		
	/* Locate the last slash character in the binary name. */
	lastSlash = result->name->seq->length - 1;
	while (lastSlash > 0)
	{
		if ('/' == sjme_charSeq_charAtR(result->name->seq, lastSlash))
			break;
		
		lastSlash--;
	}

	/* Setup buffer for the package name. */
	packageName = sjme_alloca(sizeof(*packageName) * (lastSlash + 1));
	if (packageName == NULL)
	{
		error = SJME_ERROR_OUT_OF_MEMORY;
		goto fail_inPackage;
	}
	memset(packageName, 0, lastSlash + 1);

	/* Write out package name. */
	if (sjme_error_is(error = sjme_charSeq_dupToU(
		result->name->seq, 0,
		packageName, 0, lastSlash, lastSlash)))
		goto fail_inPackage;
	
	/* Locate string for package name. */
	result->inPackage = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_locateUtf(
		inStringPool, &result->inPackage, packageName, 0, lastSlash)) ||
		result->inPackage == NULL)
		goto fail_inPackage;
	
	/* Reference it. */
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->inPackage, NULL)))
		goto fail_refPackage;

	/* Translate to the name as it would appear at runtime. */
	memset(runtimeName, 0, sizeof(runtimeName));
	if (sjme_error_is(error = sjme_charSeq_dupToU(result->name->seq,
		0, runtimeName, 0, MAX_RUNTIME_NAME - 1,
		-1)))
		goto fail_dupName;
	runtimeName[MAX_RUNTIME_NAME - 1] = 0;
	
	for (i = 0; i < MAX_RUNTIME_NAME; i++)
		if (runtimeName[i] == '/')
			runtimeName[i] = '.';
		else if (runtimeName[i] == '\0')
			break;

	/* Lookup the runtime string. */
	result->runtimeName = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_locateUtf(
		inStringPool, &result->runtimeName, runtimeName, 0, -1)) ||
		result->runtimeName == NULL)
		goto fail_inRuntimeName;
	
	/* Reference it. */
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->runtimeName, NULL)))
		goto fail_refRuntimeName;
	
	/* Read in super name. */
	superName = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, result->pool,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		SJME_JNI_TRUE, &superName)))
		goto fail_readSuperName;
	
	/* Reference it, if valid. */
	if (superName != NULL)
	{
		result->superName = SJME_P_C_N(superName);
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
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		interfaceCount, &interfaceNames, sjme_nvm_stringPool_string, 0)) ||
		interfaceNames == NULL)
		goto fail_allocInterfaceNames;
	result->interfaceNames = interfaceNames;
	
	/* Read in all interfaces. */
	for (i = 0; i < interfaceCount; i++)
	{
		/* Read in name. */
		interfaceName = NULL;
		if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
			inStream, result->pool,
			SJME_NVM_CLASS_POOL_TYPE_CLASS,
			SJME_JNI_FALSE, &interfaceName)) ||
			interfaceName == NULL)
			goto fail_readThisName;
		
		/* Reference it. */
		interfaceNames->elements[i] = SJME_P_C_N(interfaceName);
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
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		fieldCount, &fields, sjme_nvm_class_fieldInfo, 0)) || fields == NULL)
		goto fail_allocFields;
	result->fields = fields;
	
	/* Load in and process each field. */
	for (i = 0; i < fieldCount; i++)
	{
		/* Parse each field. */
		if (sjme_error_is(error = sjme_nvm_class_parseField(
			allocPool, inStream, result->pool, inStringPool,
			&fields->elements[i])) ||
			fields->elements[i] == NULL)
			goto fail_parseField;
		
		/* We are referencing this. */
		if (sjme_error_is(error = sjme_alloc_weakRef(
			fields->elements[i], NULL)))
			goto fail_refField;
	}
	
	/* Determine the indexes of all fields. */
	for (i = 0; i < fieldCount; i++)
	{
		/* Determine the type index for its slot. */
		field = fields->elements[i];
		field->typedIndex = result->fieldCount[
			((field->flags & SJME_NVM_ACC_STATIC) != 0 ? 
			SJME_NVM_CLASS_MEMBER_STATIC : SJME_NVM_CLASS_MEMBER_INSTANCE)]
			[field->extendedType]++;
		
		/* Overflowed? */
		if (field->typedIndex < 0)
		{
			error = SJME_ERROR_CLASS_TOO_MANY_MEMBERS;
			goto fail_overflowFieldIndex;
		}
	}
	
	/* Read in method count. */
	methodCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &methodCount)) || methodCount < 0)
		goto fail_readMethodCount;
	
	/* Setup list to store methods in. */
	methods = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		methodCount, &methods, sjme_nvm_class_methodInfo, 0)) ||
		methods == NULL)
		goto fail_allocMethods;
	result->methods = methods;
	
	/* Load in and process each method. */
	for (i = 0; i < methodCount; i++)
	{
		/* Parse each method. */
		if (sjme_error_is(error = sjme_nvm_class_parseMethod(
			allocPool, inStream, result->pool, inStringPool,
			&methods->elements[i])) ||
			methods->elements[i] == NULL)
			goto fail_parseMethod;
		
		/* Reference as we are using this. */
		if (sjme_error_is(error = sjme_alloc_weakRef(
			methods->elements[i], NULL)))
			goto fail_refMethod;
		
		/* Link back. */
		sjme_atomic_s(sjme_nvm_class_info, &methods->elements[i]->inClass,
			result);
	}
	
	/* Determine the indexes of all methods. */
	for (i = 0; i < methodCount; i++)
	{
		/* Determine the type index for its slot. */
		method = methods->elements[i];
		method->typedIndex = result->methodCount[
			((method->flags & SJME_NVM_ACC_STATIC) != 0 ? 
			SJME_NVM_CLASS_MEMBER_STATIC : SJME_NVM_CLASS_MEMBER_INSTANCE)]++;
		
		/* Overflowed? */
		if (method->typedIndex < 0)
		{
			error = SJME_ERROR_CLASS_TOO_MANY_MEMBERS;
			goto fail_overflowMethodIndex;
		}
	}
	
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_nvm_class_parseAttributes(
		allocPool, inStream, result->pool, inStringPool,
		sjme_nvm_class_classAttr, result)))
		goto fail_parseAttributes;
	
	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;

fail_parseAttributes:
fail_overflowMethodIndex:
fail_refMethod:
fail_parseMethod:
fail_allocMethods:
fail_readMethodCount:
fail_overflowFieldIndex:
fail_refField:
fail_parseField:
fail_allocFields:
fail_readFieldCount:
fail_allocInterfaceNames:
fail_readInterfaceCount:
fail_refSuperName:
fail_readSuperName:
fail_refRuntimeName:
fail_inRuntimeName:
fail_dupName:
fail_refPackage:
fail_inPackage:
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
	return sjme_error_vmError(NULL, error);
#undef MAX_RUNTIME_NAME
}

sjme_errorCode sjme_nvm_class_parseAttributes(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint i, len;
	sjme_nvm_class_poolEntry* name;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
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
		if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
			inStream, inConstPool,
			SJME_NVM_CLASS_POOL_TYPE_UTF,
			SJME_JNI_FALSE, &name)) || name == NULL)
			goto fail_readName;
		
		/* Read in length. */
		len = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJI(
			inStream, &len)) || len < 0)
			goto fail_readLen;
		
		/* Stage it for stack allocations. */
		if (sjme_error_is(error = sjme_nvm_class_parseAttribute(
			allocPool, inStream, inConstPool, inStringPool, handlers,
			context, name->utf.utf->seq,
			len)))
			goto fail_parseSingle;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_parseSingle:
fail_readLen:
fail_readName:
fail_readCount:
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_parseConstantPool(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_poolInfo* outPool)
{
	sjme_errorCode error;
	sjme_jshort count;
	sjme_jint index;
	sjme_jbyte tag;
	sjme_list(sjme_nvm_class_poolEntry)* entries;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntry* target;
	sjme_nvm_stringPool_string utf;
	sjme_nvm_class_poolInfo result;
	
	if (allocPool == NULL || inStream == NULL || outPool == NULL ||
		inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we can actually allocate this. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
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
		error = sjme_error_vmError(NULL, SJME_ERROR_INVALID_CLASS_POOL_COUNT);
		goto fail_poolCount;
	}
	
	/* Count up by one, since zero is included! */
	count += 1;
	
	/* Allocate resultant entries, where they will all go. */
	entries = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		count, &entries, sjme_nvm_class_poolEntry, 0)) || entries == NULL)
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

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("TAG: %d", tag);
#endif
		
		/* Set tag. */
		entry->type = tag;
		
		/* Which tag is this? */
		switch (tag)
		{
				/* Class reference. */
			case SJME_NVM_CLASS_POOL_TYPE_CLASS:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->classRef.descriptorIndex)))
					goto fail_readItem;
				break;
				
				/* Double value. */
			case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
				if (sjme_error_is(error = sjme_stream_inputReadValueJJ(
					inStream,
					(sjme_jlong*)&entry->constDouble.value)))
					goto fail_readItem;
				
				/* Skip wide index. */
				index++;
				break;
			
				/* Reference to a member. */
			case SJME_NVM_CLASS_POOL_TYPE_FIELD:
			case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
			case SJME_NVM_CLASS_POOL_TYPE_METHOD:
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
			case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constFloat.value.bits)))
					goto fail_readItem;
				break;
				
				/* Integer value. */
			case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
				if (sjme_error_is(error = sjme_stream_inputReadValueJI(
					inStream,
					(sjme_jint*)&entry->constInteger.value)))
					goto fail_readItem;
				break;
				
				/* Long value. */
			case SJME_NVM_CLASS_POOL_TYPE_LONG:
				if (sjme_error_is(error = sjme_stream_inputReadValueJJ(
					inStream,
					(sjme_jlong*)&entry->constLong.value)))
					goto fail_readItem;
				
				/* Skip wide index. */
				index++;
				break;
				
				/* Name and type information. */
			case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
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
			case SJME_NVM_CLASS_POOL_TYPE_STRING:
				if (sjme_error_is(error = sjme_stream_inputReadValueJS(
					inStream,
					&entry->constString.valueIndex)))
					goto fail_readItem;
				break;
			
				/* UTF String. */
			case SJME_NVM_CLASS_POOL_TYPE_UTF:
				utf = NULL;
				if (sjme_error_is(error = sjme_nvm_stringPool_locateStream(
					inStringPool, inStream, &utf)) || utf == NULL)
					goto fail_readItem;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
				/* Debug. */
				sjme_message("Read UTF: %s",
					sjme_charSeq_tempUtf(utf->seq));
#endif
				
				/* Store and count up entry as we are using it now. */
				entry->utf.utf = utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(utf, NULL)))
					goto fail_readItem;
				break;
			
			default:
				goto fail_initItem;
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
			case SJME_NVM_CLASS_POOL_TYPE_UTF:
			case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
			case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
				break;
				
				/* Skip wide element. */
			case SJME_NVM_CLASS_POOL_TYPE_LONG:
			case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
				index++;
				break;
			
				/* Class type. */
			case SJME_NVM_CLASS_POOL_TYPE_CLASS:
				if (entry->classRef.descriptorIndex <= 0 ||
					entry->classRef.descriptorIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a UTF string. */
				target = &entries->elements[entry->classRef.descriptorIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Refer to it and count up, since we are using it. */
				SJME_P_C_N(entry) = target->utf.utf;
				if (sjme_error_is(error = sjme_alloc_weakRef(
					SJME_P_C_N(entry), NULL)))
					goto fail_initItem;
				break;
				
				/* Member reference. */
			case SJME_NVM_CLASS_POOL_TYPE_FIELD:
			case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
			case SJME_NVM_CLASS_POOL_TYPE_METHOD:
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
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_CLASS)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set class. */
				sjme_atomic_sP(sjme_nvm_class_poolEntryClass, 1,
					&entry->member.inClass,
					(sjme_nvm_class_poolEntryClass*)target);
				
				/* Needs to be a name and type. */
				target = &entries->elements[entry->member.nameAndTypeIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE)
				{
					error = SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
					goto fail_initItem;
				}
				
				/* Set name and type. */
				sjme_atomic_sP(sjme_nvm_class_poolEntryNameAndType, 1,
					&entry->member.nameAndType,
					(sjme_nvm_class_poolEntryNameAndType*)target);
				break;
			
			case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
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
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
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
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
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
			case SJME_NVM_CLASS_POOL_TYPE_STRING:
				if (entry->constString.valueIndex <= 0 ||
					entry->constString.valueIndex >= entries->length)
				{
					error = SJME_ERROR_INVALID_CLASS_POOL_INDEX;
					goto fail_initItem;
				}
				
				/* Needs to be a UTF string. */
				target = &entries->elements[entry->constString.valueIndex];
				if (target->type != SJME_NVM_CLASS_POOL_TYPE_UTF)
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
				goto fail_initItem;
		}
	}

	/* Third stage initialization, when everything is known. */
	for (index = 1; index < count - 1; index++)
	{
		/* Which entry is being initialized? */
		entry = &entries->elements[index];
		
		/* Initialize accordingly. */
		switch (entry->type)
		{
				/* Class reference. */
			case SJME_NVM_CLASS_POOL_TYPE_CLASS:
				entry->classRef.descriptorHash =
					sjme_charSeq_hashR(SJME_P_C_N(entry)->seq);
				break;
			
				/* Member reference. */
			case SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE:
				/* Calculate the member ID hash. */
				entry->nameAndType.idHash = sjme_nvm_class_idHashMember(
					entry->nameAndType.name->seq,
					entry->nameAndType.descriptor->seq);
				break;

				/* Method reference. */
			case SJME_NVM_CLASS_POOL_TYPE_METHOD:
			case SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD:
				if (sjme_error_is(error = sjme_nvm_class_descriptorMethodSlots(
					SJME_P_M_T(entry)->seq,
					&entry->member.staticArgSlots,
					&entry->member.rvSlots)))
					goto fail_initItem;
				break;

				/* Not considered an error in the third stage. */
			default:
				break;
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
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_parseField(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_class_fieldInfo* outField)
{
	sjme_errorCode error;
	sjme_nvm_class_fieldInfo result;
	sjme_nvm_class_poolEntry* name;
	sjme_nvm_class_poolEntry* type;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
		outField == NULL || inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ensure we can allocate the result first. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_FIELD_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_nvm_class_fieldFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
		
	/* Read in name. */
	name = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &name)) || name == NULL)
		goto fail_readName;
	
	/* Reference it. */
	result->name = name->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->name, NULL)))
		goto fail_refName;
		
	/* Read in type. */
	type = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &type)) || type == NULL)
		goto fail_readType;
	
	/* Reference it. */
	result->type = type->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->type, NULL)))
		goto fail_refType;
		
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_nvm_class_parseAttributes(
		allocPool, inStream, inConstPool, inStringPool,
		sjme_nvm_class_fieldAttr, result)))
		goto fail_parseAttributes;
	
	/* Determine type. */
	if (sjme_error_is(error = sjme_nvm_class_descriptorToType(
		result->type->seq,
		&result->javaType, &result->basicType, &result->extendedType)))
		goto fail_determineType;
	
	/* Initialize constant value to an invalid type. */
	result->constVal.type = SJME_NUM_JAVA_TYPE_IDS;

	/* Calculate the hash identifier. */
	result->idHash = sjme_nvm_class_idHashMember(result->name->seq,
		result->type->seq);
	
	/* Success! */
	*outField = result;
	return SJME_ERROR_NONE;
	
fail_determineType:
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
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_parseMethod(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInOutNotNull sjme_nvm_class_methodInfo* outMethod)
{
	sjme_errorCode error;
	sjme_nvm_class_methodInfo result;
	sjme_nvm_class_poolEntry* name;
	sjme_nvm_class_poolEntry* type;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
		outMethod == NULL || inStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ensure we can allocate the result first. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_METHOD_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Read in flags. */
	if (sjme_error_is(error = sjme_nvm_class_methodFlagsParse(
		inStream, &result->flags)))
		goto fail_readFlags;
		
	/* Read in name. */
	name = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &name)) || name == NULL)
		goto fail_readName;
	
	/* Reference it. */
	result->name = name->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->name, NULL)))
		goto fail_refName;
		
	/* Read in type. */
	type = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &type)) || type == NULL)
		goto fail_readType;
	
	/* Reference it. */
	result->type = type->utf.utf;
	if (sjme_error_is(error = sjme_alloc_weakRef(
		result->type, NULL)))
		goto fail_refType;
		
	/* Parse attributes. */
	if (sjme_error_is(error = sjme_nvm_class_parseAttributes(
		allocPool, inStream, inConstPool, inStringPool,
		sjme_nvm_class_methodAttr, result)))
		goto fail_parseAttributes;

	/* Determine the number of method arguments. */
	if (sjme_error_is(error = sjme_nvm_class_calcMethodArgs(
		allocPool, type->utf.utf->seq,
		&result->argC, &result->argT, &result->argR)))
		goto fail_calcArgs;
	
	/* The identifier hash is used for lookup. */
	result->idHash = sjme_nvm_class_idHashMember(result->name->seq,
		result->type->seq);

	/* Are these initializers? */
	if (sjme_charSeq_equalsUtfR(result->name->seq, "<clinit>"))
		result->bits |= SJME_NVM_CLASS_INIT_STATIC;
	else if (sjme_charSeq_equalsUtfR(result->name->seq, "<init>"))
		result->bits |= SJME_NVM_CLASS_INIT_INSTANCE;
	
	/* Success! */
	*outMethod = result;
	return SJME_ERROR_NONE;

fail_calcArgs:
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
	return sjme_error_vmError(NULL, error);
}

sjme_errorCode sjme_nvm_class_validBinaryName(
	sjme_attrInNotNull sjme_charSeq binaryName)
{
	sjme_jint i, n;
	sjme_jchar c;
	
	if (binaryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	for (i = 0, n = binaryName->length; i < n; i++)
	{
		c = sjme_charSeq_charAtR(binaryName, i);

		/* These characters are invalid. */
		if (c == '.' || c == ';' || c == '[' || c == '<' ||
			c == '>' || c == ':')
			return SJME_ERROR_INVALID_BINARY_NAME;
	}
	
	/* Did not fail, so success! */
	return SJME_ERROR_NONE;
}
