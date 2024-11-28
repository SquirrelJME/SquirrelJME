/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instance.h"

sjme_errorCode sjme_nvm_fieldValueSet(
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInNotNull sjme_nvm_fieldValues* into,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jvalue* value)
{
	if (into == NULL || value == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (into->type != javaType)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (atIndex < 0 || atIndex >= into->count)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (javaType == SJME_JAVA_TYPE_ID_INTEGER)
		into->values.jints[atIndex] = value->i;
	else if (javaType == SJME_JAVA_TYPE_ID_LONG)
		into->values.jlongs[atIndex] = value->j;
	else if (javaType == SJME_JAVA_TYPE_ID_FLOAT)
		into->values.jfloats[atIndex] = value->f;
	else if (javaType == SJME_JAVA_TYPE_ID_DOUBLE)
		into->values.jdoubles[atIndex] = value->d;
	else
		into->values.jobjects[atIndex] = value->l;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_jint sjme_nvm_fieldValueSize(
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInPositiveNonZero sjme_jint n)
{
	sjme_jint baseSize;

	if (javaType < 0 || javaType >= SJME_NUM_JAVA_TYPE_IDS ||
		n <= 0)
		return -1;

	if (javaType == SJME_JAVA_TYPE_ID_OBJECT)
		baseSize = (SJME_CONFIG_HAS_POINTER >> 3);
	else if (javaType == SJME_JAVA_TYPE_ID_INTEGER ||
		javaType == SJME_JAVA_TYPE_ID_FLOAT)
		baseSize = 4;
	else
		baseSize = 8;
	
	/* Base size is the offset of where values start */
	return (baseSize * n) +
		offsetof(sjme_nvm_fieldValues, values) +
		offsetof(sjme_nvm_rawFieldValues, jobjects);
}

