/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/task.h"

typedef sjme_jboolean (*sjme_nvm_byteCode_compareAFunc)(
	sjme_attrInValue sjme_jobject a,
	sjme_attrInValue sjme_jobject b);

typedef sjme_jboolean (*sjme_nvm_byteCode_compareIFunc)(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b);

static sjme_jboolean sjme_nvm_byteCode_compareAEq(
	sjme_attrInValue sjme_jobject a,
	sjme_attrInValue sjme_jobject b)
{
	return a == b;
}

static sjme_jboolean sjme_nvm_byteCode_compareANe(
	sjme_attrInValue sjme_jobject a,
	sjme_attrInValue sjme_jobject b)
{
	return a != b;
}

static sjme_jboolean sjme_nvm_byteCode_compareIEq(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a == b;
}

static sjme_jboolean sjme_nvm_byteCode_compareIGe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a >= b;
}

static sjme_jboolean sjme_nvm_byteCode_compareIGt(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a > b;
}

static sjme_jboolean sjme_nvm_byteCode_compareILe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a <= b;
}

static sjme_jboolean sjme_nvm_byteCode_compareILt(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a < b;
}

static sjme_jboolean sjme_nvm_byteCode_compareINe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a != b;
}

static const sjme_nvm_byteCode_compareAFunc sjme_nvm_byteCode_compareAs[2] =
{
	sjme_nvm_byteCode_compareAEq,
	sjme_nvm_byteCode_compareANe,
};

static const sjme_nvm_byteCode_compareIFunc sjme_nvm_byteCode_compareIs[6] =
{
	sjme_nvm_byteCode_compareIEq,
	sjme_nvm_byteCode_compareINe,
	sjme_nvm_byteCode_compareILt,
	sjme_nvm_byteCode_compareIGe,
	sjme_nvm_byteCode_compareIGt,
	sjme_nvm_byteCode_compareILe,
};

static const sjme_javaTypeId sjme_nvm_byteCode_returnTypes[6] =
{
	SJME_JAVA_TYPE_ID_INTEGER,
	SJME_JAVA_TYPE_ID_LONG,
	SJME_JAVA_TYPE_ID_FLOAT,
	SJME_JAVA_TYPE_ID_DOUBLE,
	SJME_JAVA_TYPE_ID_OBJECT,
	SJME_JAVA_TYPE_ID_VOID,
};

SJME_NVM_BYTECODE_SLOW(IfAX)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Pop single object value. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareAs[id - 198](value.v.l, NULL))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
		pcNew->adjust = sjme_big_short(
			*sjme_util_memUnaligned16(&relRawCode[1]));
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IfX)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Pop single integer value. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareIs[id - 153](value.v.i, 0))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
		pcNew->adjust = sjme_big_short(
			*sjme_util_memUnaligned16(&relRawCode[1]));
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IfICmpX)
{
	sjme_jvalueTyped a, b;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Pop both integer values. */
	memset(&commit, 0, sizeof(commit));
	memset(&b, 0, sizeof(b));
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &b)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareIs[id - 159](a.v.i, b.v.i))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
		pcNew->adjust = sjme_big_short(
			*sjme_util_memUnaligned16(&relRawCode[1]));
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IfACmpX)
{
	sjme_jvalueTyped a, b;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Pop both integer values. */
	memset(&commit, 0, sizeof(commit));
	memset(&b, 0, sizeof(b));
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &b)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareAs[id - 165](a.v.l, b.v.l))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
		pcNew->adjust = sjme_big_short(
			*sjme_util_memUnaligned16(&relRawCode[1]));
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Goto)
{
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Jumps according to the offset. */
	pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
	pcNew->adjust = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(GotoWide)
{
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Jumps according to the offset. */
	pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
	pcNew->adjust = sjme_big_int(*sjme_util_memUnaligned32(&relRawCode[1]));
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(LookupSwitch)
{
	sjme_jint paramBase, divHi;
	sjme_jint pivot, divLo, base;
	sjme_jint matchKey, desire;
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Determine the relative base for parameters. */
	paramBase = ((inFrame->pc + 4) & (~3)) - inFrame->pc;

	/* The initial high division is the pair count. */
	divHi = sjme_big_int(
		*sjme_util_memUnaligned32(&relRawCode[paramBase + 4]));

	/* Cannot have a negative number of pairs. */
	if (divHi < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);

	/* Read in switch value. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Table is completely empty, skip everything. */
	if (divHi == 0)
		goto skip_default;
	
	/* Binary search within the table. */
	desire = value.v.i;
	for (divLo = 0; divLo <= divHi;)
	{
		/* Calculate the pivot to use. */
		pivot = divLo + (((divHi - divLo) + 1) >> 1);
		
		/* Read the pivot value. */
		base = (paramBase + 8) + (pivot * 8);
		matchKey = sjme_big_int(
			*sjme_util_memUnaligned32(&relRawCode[base]));

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("LS @%d->%d (%d): %3d|%3d|%3d: %08x ?= %08x",
			inFrame->pc, paramBase, paramBase - inFrame->pc,
			divLo, pivot, divHi, desire, matchKey);
#endif

		/* Is this the key? */
		if (desire == matchKey)
		{
			/* Jump to this address. */
			pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
			pcNew->adjust = sjme_big_int(
				*sjme_util_memUnaligned32(&relRawCode[base + 4]));
			
			/* No more searching needed! */ 
			goto skip_matched;
		}

		/* Left of pivot? */
		else if (desire < matchKey)
			divHi = pivot - 1;

		/* Right of pivot? */
		else
			divLo = pivot + 1;
	}

	/* Unmatched, so jump is default. */
skip_default:
	pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
	pcNew->adjust = sjme_big_int(
		*sjme_util_memUnaligned32(&relRawCode[paramBase]));
	
skip_matched:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(NoOp)
{
	SJME_NVM_BYTECODE_ENTRY;
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(ReturnX)
{
	sjme_javaTypeId desire;
	sjme_jvalueTyped result;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Must be returning the same type. */
	desire = sjme_nvm_byteCode_returnTypes[id - 172];
	if (inFrame->inCode->inMethod->argR != desire)
		return sjme_error_vmError(inFrame, SJME_ERROR_WRONG_RETURN_TYPE);

	/* If not returning void, pop value to return onto the parent stack. */
	memset(&commit, 0, sizeof(commit));
	if (desire != SJME_JAVA_TYPE_ID_VOID)
	{
		/* Must push onto something. */
		if (inFrame->parent == NULL)
			return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);
		
		/* Pop value. */
		memset(&result, 0, sizeof(result));
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			desire, &commit, &result)))
			return sjme_error_vmError(inFrame, error);

		/* Push onto the parent stack. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame->parent, &result)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Pop the current frame. */
	pcNew->popFrame = SJME_JNI_TRUE;

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(TableSwitch)
{
	sjme_jint paramBase, lo, hi, tableCount;
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Determine the relative base for parameters. */
	paramBase = ((inFrame->pc + 4) & (~3)) - inFrame->pc;

	/* Read low and high values. */
	lo = sjme_big_int(*sjme_util_memUnaligned32(&relRawCode[paramBase + 4]));
	hi = sjme_big_int(*sjme_util_memUnaligned32(&relRawCode[paramBase + 8]));

	/* The table must be valid. */
	tableCount = (hi - lo) + 1;
	if (lo > hi || tableCount <= 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);

	/* Read in switch value. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Would be a default jump? */
	pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
	if (value.v.i < lo || value.v.i > hi)
		pcNew->adjust = sjme_big_int(
			*sjme_util_memUnaligned32(&relRawCode[paramBase]));

	/* In the table. */
	else
		pcNew->adjust = sjme_big_int(*sjme_util_memUnaligned32(
			&relRawCode[paramBase + 12 + (4 * (value.v.i - lo))]));

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}
