/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "classBuilder.h"

sjme_errorCode sjme_classBuilder_addPool(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero sjme_jint poolSize)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryClass(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero sjme_jint inUtfIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryConstVal(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInValue sjme_jvalue value)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryMemberRef(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInRange(SJME_CLASS_POOL_ENTRY_MEMBER_TYPE_FIELD,
		SJME_NUM_CLASS_POOL_ENTRY_MEMBER_TYPE)
		sjme_class_poolEntryMemberType type,
	sjme_attrInPositiveNonZero sjme_jint inClassIndex,
	sjme_attrInPositiveNonZero sjme_jint inNameAndTypeIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryNameAndType(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero sjme_jint inNameUtfIndex,
	sjme_attrInPositiveNonZero sjme_jint inTypeUtfIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryString(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero sjme_jint inUtfIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryUtf(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInNotNull sjme_lpcstr inUtf)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_build(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_classBuilder* outBuilder,
	sjme_attrInNullable void* whatever)
{
	if (inPool == NULL || outBuilder == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_finish(
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNotNull void** rawClass)
{
	if (builder == NULL || rawClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
