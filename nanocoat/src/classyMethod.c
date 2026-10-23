/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/task.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/classy.h"
#include "sjme/debug.h"
#include "sjme/util.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/classyUtil.h"
#include "sjme/nvm/cleanup.h"

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
		if (sjme_nvm_byteCodeLutTable(iv) !=
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
	sjme_nvm_class_poolEntryClass* handles;
	
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
		sizeof(*result), SJME_NVM_STRUCT_CODE_INFO,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* In this method! */
	sjme_atomic_s(sjme_nvm_class_methodInfo, &result->inMethod, methodInfo);
	
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

	/* Store full local map reference. */
	sjme_atomic_s(sjme_pointer, &result->localMapBase, localMap);
	
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
			handles = NULL;
			if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
				attrStream, inConstPool,
				SJME_NVM_CLASS_POOL_TYPE_CLASS,
				SJME_JNI_TRUE,
				(sjme_nvm_class_poolEntry**)&handles)))
				goto fail_exceptHandles;
			sjme_atomic_sP(sjme_nvm_class_poolEntryClass, 1,
				&except->handles, handles);
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
	methodInfo->code = sjme_weakUp(result);
	
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
	if (SJME_NVM_ACC_IS(rawFlags, PUBLIC))
		(*outFlags) |= SJME_NVM_ACC_PUBLIC;
	if (SJME_NVM_ACC_IS(rawFlags, PRIVATE))
		(*outFlags) |= SJME_NVM_ACC_PRIVATE;
	if (SJME_NVM_ACC_IS(rawFlags, PROTECTED))
		(*outFlags) |= SJME_NVM_ACC_PROTECTED;
	if (SJME_NVM_ACC_IS(rawFlags, STATIC))
		(*outFlags) |= SJME_NVM_ACC_STATIC;
	if (SJME_NVM_ACC_IS(rawFlags, FINAL))
		(*outFlags) |= SJME_NVM_ACC_FINAL;
	if (SJME_NVM_ACC_IS(rawFlags, SYNCHRONIZED))
		(*outFlags) |= SJME_NVM_ACC_SYNCHRONIZED;
	if (SJME_NVM_ACC_IS(rawFlags, BRIDGE))
		(*outFlags) |= SJME_NVM_ACC_BRIDGE;
	if (SJME_NVM_ACC_IS(rawFlags, VARARGS))
		(*outFlags) |= SJME_NVM_ACC_VARARGS;
	if (SJME_NVM_ACC_IS(rawFlags, NATIVE))
		(*outFlags) |= SJME_NVM_ACC_NATIVE;
	if (SJME_NVM_ACC_IS(rawFlags, ABSTRACT))
		(*outFlags) |= SJME_NVM_ACC_ABSTRACT;
	if (SJME_NVM_ACC_IS(rawFlags, STRICTFP))
		(*outFlags) |= SJME_NVM_ACC_STRICTFP;
	if (SJME_NVM_ACC_IS(rawFlags, SYNTHETIC))
		(*outFlags) |= SJME_NVM_ACC_SYNTHETIC;
	
	/* Can only have a single access mode. */
	if (sjme_util_intBitCountU((*outFlags) & SJME_NVM_ACC_ACCESS_MASK) > 1)
		return SJME_ERROR_INVALID_METHOD_FLAGS;
	
	/* Abstract cannot be final, private, static, strict, or synchronized. */
	if (SJME_NVM_ACC_IS(*outFlags, ABSTRACT))
		if (SJME_NVM_ACC_IS(*outFlags, FINAL) ||
			SJME_NVM_ACC_IS(*outFlags, PRIVATE) ||
			SJME_NVM_ACC_IS(*outFlags, STATIC) ||
			SJME_NVM_ACC_IS(*outFlags, STRICTFP) ||
			SJME_NVM_ACC_IS(*outFlags, SYNCHRONIZED))
			return SJME_ERROR_INVALID_METHOD_FLAGS;
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	result->name = sjme_weakUp(name->utf.utf);
		
	/* Read in type. */
	type = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
		inStream, inConstPool,
		SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &type)) || type == NULL)
		goto fail_readType;
	
	/* Reference it. */
	result->type = sjme_weakUp(type->utf.utf);
		
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
