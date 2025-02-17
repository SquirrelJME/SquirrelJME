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
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/util.h"

static sjme_errorCode sjme_nvm_byteCode_slowLdcAny(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_class_poolEntry* entry)
{
	sjme_jvalueTyped value;
	
	if (inFrame == NULL || entry == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* What happens, depends on the type. */
	memset(&value, 0, sizeof(value));
	switch (entry->type)
	{
		case SJME_NVM_CLASS_POOL_TYPE_INTEGER:
			value.type = SJME_JAVA_TYPE_ID_INTEGER;
			value.value.i = entry->constInteger.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, &value);
		
		case SJME_NVM_CLASS_POOL_TYPE_LONG:
			value.type = SJME_JAVA_TYPE_ID_LONG;
			value.value.j = entry->constLong.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, &value);
		
		case SJME_NVM_CLASS_POOL_TYPE_FLOAT:
			value.type = SJME_JAVA_TYPE_ID_FLOAT;
			value.value.f = entry->constFloat.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, &value);
		
		case SJME_NVM_CLASS_POOL_TYPE_DOUBLE:
			value.type = SJME_JAVA_TYPE_ID_DOUBLE;
			value.value.d = entry->constDouble.value;
			return sjme_nvm_task_frameStackPush(
				inFrame, &value);

		case SJME_NVM_CLASS_POOL_TYPE_CLASS:
			return sjme_nvm_task_frameStackPushClassPD(
				inFrame, entry->classRef.descriptor);
		
		case SJME_NVM_CLASS_POOL_TYPE_STRING:
			return sjme_nvm_task_frameStackPushStringP(
				inFrame, entry->utf.utf);
		
		/* Invalid type. */
		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE);
	}
}

SJME_NVM_BYTECODE_SLOW(AConstNull)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_OBJECT;
	value.value.l = NULL;

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(BIPush)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 2;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_INTEGER;
	value.value.i = (sjme_jbyte)relRawCode[1];

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(DConstZ)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_DOUBLE;
	if (id == 14)
		value.value.d.hi = 0;
	else
		value.value.d.hi = UINT32_C(0x3FF00000);

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(FConstZ)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_FLOAT;
	if (id == 11)
		value.value.f.value = 0;
	else if (id == 12)
		value.value.f.value = INT32_C(1065353216);
	else
		value.value.f.value = INT32_C(1073741824);

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IConstM)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_INTEGER;
	value.value.i = (-1) + (id - 2);

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(LConstZ)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_LONG;
	value.value.j.part.lo = id - 9;

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Ldc)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 2;

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
	if (sjme_error_is(error =  sjme_nvm_byteCode_slowLdcAny(inFrame,
		id, relRawCode, entry)))
		return sjme_error_vmError(inFrame, error);

	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(LdcW)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

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
	if (sjme_error_is(error =  sjme_nvm_byteCode_slowLdcAny(inFrame,
		id, relRawCode, entry)))
		return sjme_error_vmError(inFrame, error);

	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(LdcWTwo)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool value. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_LONG,
		SJME_NVM_CLASS_POOL_TYPE_DOUBLE,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Forward to common handler. */
	if (sjme_error_is(error =  sjme_nvm_byteCode_slowLdcAny(inFrame,
		id, relRawCode, entry)))
		return sjme_error_vmError(inFrame, error);

	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(SIPush)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Setup value to push. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_INTEGER;
	value.value.i = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));

	/* Push to stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
