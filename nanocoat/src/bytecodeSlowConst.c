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
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/util.h"

static sjme_errorCode sjme_nvm_byteCode_slowLdcAny(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_class_poolEntry* entry)
{
	sjme_jvalueTyped value;
	
	if (inFrame == NULL || entry == NULL || commit == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* What happens, depends on the type. */
	memset(&value, 0, sizeof(value));
	switch (entry->type)
	{
		case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
			value.t = SJME_JAVA_TYPE_ID_INTEGER;
			value.v.i = entry->constInteger.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, commit, &value);
		
		case SJME_NVM_CLASS_POOL_TYPE_LONG:
			value.t = SJME_JAVA_TYPE_ID_LONG;
			value.v.j = entry->constLong.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, commit, &value);
		
		case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
			value.t = SJME_JAVA_TYPE_ID_FLOAT;
			value.v.f = entry->constFloat.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, commit, &value);
		
		case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
			value.t = SJME_JAVA_TYPE_ID_DOUBLE;
			value.v.d = entry->constDouble.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, commit, &value);

		case SJME_NVM_CLASS_POOL_TYPE_CLASS:
			return sjme_nvm_task_frameStackPushClassPD(
				inFrame, commit, SJME_P_C_N(entry));
		
		case SJME_NVM_CLASS_POOL_TYPE_STRING:
			return sjme_nvm_task_frameStackPushStringP(
				inFrame, commit, entry->constString.value);
		
		/* Invalid type. */
		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE);
	}
}

#pragma region(AConstNull)
SJME_NVM_BYTECODE_SLOW(AConstNull)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_OBJECT;
	value.v.l = NULL;

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(BIPush)
SJME_NVM_BYTECODE_SLOW(BIPush)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_INTEGER;
	value.v.i = relRawCode[1];
	if ((value.v.i & INT32_C(0x80)) != 0)
		value.v.i |= INT32_C(0xFFFFFF00);

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(DConstZ)
SJME_NVM_BYTECODE_SLOW(DConstZ)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_DOUBLE;
	if (id == 14)
		value.v.d.bits.hi = 0;
	else
		value.v.d.bits.hi = UINT32_C(0x3FF00000);

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(FConstZ)
SJME_NVM_BYTECODE_SLOW(FConstZ)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_FLOAT;
	if (id == 11)
		value.v.f.bits = 0;
	else if (id == 12)
		value.v.f.bits = INT32_C(1065353216);
	else
		value.v.f.bits = INT32_C(1073741824);

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(IConstM)
SJME_NVM_BYTECODE_SLOW(IConstM)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_INTEGER;
	value.v.i = (-1) + (id - 2);

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(LConstZ)
SJME_NVM_BYTECODE_SLOW(LConstZ)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_LONG;
	value.v.j.part.lo = id - 9;

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(Ldc)
SJME_NVM_BYTECODE_SLOW(Ldc)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in pool value. */
	poolIndex = relRawCode[1];
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_INTEGER,
		SJME_NVM_CLASS_POOL_TYPE_FLOAT,
		SJME_NVM_CLASS_POOL_TYPE_STRING,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Forward to common handler. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error =  sjme_nvm_byteCode_slowLdcAny(inFrame,
		&commit, id, relRawCode, entry)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(LdcW)
SJME_NVM_BYTECODE_SLOW(LdcW)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in pool value. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_INTEGER,
		SJME_NVM_CLASS_POOL_TYPE_FLOAT,
		SJME_NVM_CLASS_POOL_TYPE_STRING,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Forward to common handler. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error =  sjme_nvm_byteCode_slowLdcAny(inFrame,
		&commit, id, relRawCode, entry)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(LdcWTwo)
SJME_NVM_BYTECODE_SLOW(LdcWTwo)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in pool value. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_LONG,
		SJME_NVM_CLASS_POOL_TYPE_DOUBLE,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Forward to common handler. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error =  sjme_nvm_byteCode_slowLdcAny(inFrame,
		&commit, id, relRawCode, entry)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);

	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(SIPush)
SJME_NVM_BYTECODE_SLOW(SIPush)
{
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_INTEGER;
	value.v.i = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if ((value.v.i & INT32_C(0x8000)) != 0)
		value.v.i |= INT32_C(0xFFFF0000);

	/* Push to stack. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()
