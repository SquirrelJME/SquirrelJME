/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "classBuilder.h"
#include "sjme/alloc.h"

sjme_errorCode sjme_classBuilder_addAttribute(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero sjme_jint inNameIndex,
	sjme_attrInNotNull void* inData,
	sjme_attrInPositive sjme_jint inLength)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addAttributeConstantValue(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero const sjme_jint* inValueIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addField(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInPositiveNonZero const sjme_jint* inNameIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inTypeIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addMethod(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInPositiveNonZero const sjme_jint* inNameIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inTypeIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPool(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInPositiveNonZero sjme_jint poolSize)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryClass(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inUtfIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryConstVal(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId javaType,
	sjme_attrInValue sjme_jvalue value)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryMemberRef(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInRange(SJME_CLASS_POOL_ENTRY_MEMBER_TYPE_FIELD,
		SJME_NUM_CLASS_POOL_ENTRY_MEMBER_TYPE)
		sjme_class_poolEntryMemberType type,
	sjme_attrInPositiveNonZero const sjme_jint* inClassIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inNameAndTypeIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryNameAndType(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inNameUtfIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inTypeUtfIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryString(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrOutNullable sjme_jint* outIndex,
	sjme_attrInPositiveNonZero const sjme_jint* inUtfIndex)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_addPoolEntryUtf(
	sjme_attrInNegativeOnePositive sjme_jint order,
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
	sjme_attrInValue sjme_jboolean allowInvalid,
	sjme_attrInNullable void* whatever)
{
	sjme_errorCode error;
	sjme_classBuilder* result;
	
	if (inPool == NULL || outBuilder == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*result),
		&result)) || result == NULL)
		return sjme_error_default(error);
	
	/* Whatever storage, for use as needed. */
	result->whatever = whatever;
	
	/* Allocate output array. */
	if (sjme_error_is(error = sjme_stream_outputOpenByteArray(inPool,
		&result->stream, 1024, NULL,
		result)))
		goto fail_outputStream;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_outputStream:
	if (result != NULL)
		sjme_alloc_free(result);
	return sjme_error_default(error);
}

sjme_errorCode sjme_classBuilder_declareClassA(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint classFlags,
	sjme_attrInPositive const sjme_jint* inClassNameIndex,
	sjme_attrInPositive const sjme_jint* inSuperNameIndex,
	sjme_attrInPositive sjme_jint numInterfaceNames,
	sjme_attrInNotNull const sjme_jint** inInterfaceNameIndexes)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
	
sjme_errorCode sjme_classBuilder_declareClassL(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint classFlags,
	sjme_attrInPositive const sjme_jint* inClassNameIndex,
	sjme_attrInPositive const sjme_jint* inSuperNameIndex,
	sjme_attrInNotNull sjme_list_sjme_jintP* inInterfaceNameIndexes)
{
	if (inInterfaceNameIndexes == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return sjme_classBuilder_declareClassA(order,
		builder, classFlags, inClassNameIndex, inSuperNameIndex,
		inInterfaceNameIndexes->length,
		inInterfaceNameIndexes->elements);
}

sjme_errorCode sjme_classBuilder_declareClassV(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder,
	sjme_attrInValue sjme_jint classFlags,
	sjme_attrInPositive const sjme_jint* inClassNameIndex,
	sjme_attrInPositive const sjme_jint* inSuperNameIndex,
	sjme_attrInPositive sjme_jint numInterfaceNames,
	...)
{
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

sjme_errorCode sjme_classBuilder_nextAttribute(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_classBuilder_nextMember(
	sjme_attrInNegativeOnePositive sjme_jint order,
	sjme_attrInNotNull sjme_classBuilder* builder)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
