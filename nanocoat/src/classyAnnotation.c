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
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/classyUtil.h"

static sjme_errorCode sjme_nvm_class_parseAnnotationValue(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrInOutNotNull sjme_nvm_class_annotationValue* value)
{
	sjme_errorCode error;
	sjme_jbyte tag;
	sjme_jshort numValues;
	sjme_jint i;
	sjme_nvm_class_poolEntry* poolValue;
	sjme_nvm_class_annotationValue* subValue;
	
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		value == NULL || attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in the tag. */
	tag = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJB(attrStream,
		&tag)) || tag < 0)
		return sjme_error_default(error);

	/* Set its info. */
	value->tag = tag;
	
	/* Reading arrays is a bit more involved. */
	if (tag == SJME_NVM_CLASS_ANNOTATION_TAG_ARRAY)
	{
		/* Read in array size. */
		numValues = -1;
		if (sjme_error_is(error = sjme_stream_inputReadValueJS(attrStream,
			&numValues)) || numValues < 0)
			return sjme_error_default(error);
		
		/* Allocate storage. */
		value->value.array = NULL;
		if (sjme_error_is(error = sjme_list_alloc(allocPool,
			numValues, &value->value.array,
			sjme_nvm_class_annotationValue, 0)))
			goto fail_allocList;
		
		/* Read in values. */
		for (i = 0; i < numValues; i++)
		{
			/* Which value to read into? */
			subValue = &value->value.array->elements[i];
			
			/* Parse single value, which is an inplace operation */
			/* unless of course there are annotations of annotations. */
			if (sjme_error_is(error = sjme_nvm_class_parseAnnotationValue(
				allocPool, inConstPool, inStringPool, context,
				attrStream, subValue)))
				goto fail_parseValue;
		}
		
		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* Which type of value is being read? */
	switch ((sjme_nvm_class_annotationTag)tag)
	{
			/* Integer constant. */
		case SJME_NVM_CLASS_ANNOTATION_TAG_BYTE:
		case SJME_NVM_CLASS_ANNOTATION_TAG_CHARACTER:
		case SJME_NVM_CLASS_ANNOTATION_TAG_SHORT:
		case SJME_NVM_CLASS_ANNOTATION_TAG_BOOLEAN:
		case SJME_NVM_CLASS_ANNOTATION_TAG_INTEGER:
			poolValue = NULL;
			if (sjme_error_is(error =
				sjme_nvm_class_readPoolRefIndex(
				attrStream, inConstPool,
				SJME_NVM_CLASS_POOL_TYPE_INTEGER,
				SJME_JNI_FALSE, &poolValue)) || poolValue == NULL)
				return sjme_error_default(error);
			
			/* Copy value. */
			value->value.java.v.i = 
				poolValue->constInteger.value;
			break;
			
		case SJME_NVM_CLASS_ANNOTATION_TAG_DOUBLE:
		case SJME_NVM_CLASS_ANNOTATION_TAG_FLOAT:
		case SJME_NVM_CLASS_ANNOTATION_TAG_LONG:
			sjme_todo("Impl?");
			return SJME_ERROR_NOT_IMPLEMENTED;
			
			/* String Constant. */
		case SJME_NVM_CLASS_ANNOTATION_TAG_STRING:
			poolValue = NULL;
			if (sjme_error_is(error =
				sjme_nvm_class_readPoolRefIndex(
				attrStream, inConstPool,
				SJME_NVM_CLASS_POOL_TYPE_UTF,
				SJME_JNI_FALSE, &poolValue)) || poolValue == NULL)
				return sjme_error_default(error);
			
			/* Set string value. */
			value->value.string = sjme_weakUpR(
				sjme_nvm_stringPool_string, poolValue->utf.utf);
			break;
			
		case SJME_NVM_CLASS_ANNOTATION_TAG_ENUM:
			sjme_todo("Impl?");
			return SJME_ERROR_NOT_IMPLEMENTED;
			
		case SJME_NVM_CLASS_ANNOTATION_TAG_CLASS:
			sjme_todo("Impl?");
			return SJME_ERROR_NOT_IMPLEMENTED;
			
		case SJME_NVM_CLASS_ANNOTATION_TAG_ANNOTATION:
			sjme_todo("Impl?");
			return SJME_ERROR_NOT_IMPLEMENTED;
			
		default:
			return SJME_ERROR_CLASS_UNKNOWN_ANNOTATION_TAG;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_parseValue:
fail_allocList:
	if (tag == SJME_NVM_CLASS_ANNOTATION_TAG_ARRAY)
		if (value->value.array != NULL)
		{
			sjme_alloc_free(value->value.array);
			value->value.array = NULL;
		}
		
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_class_classAnnotation(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_stream_input attrStream,
	sjme_attrOutNotNull sjme_nvm_class_annotation* outAnnotation)
{
	sjme_errorCode error;
	sjme_nvm_class_info classInfo;
	sjme_jint i;
	sjme_jshort numValues;
	sjme_nvm_class_annotation result;
	sjme_list(sjme_nvm_class_annotationValue)* values;
	sjme_nvm_class_annotationValue* value;
	sjme_nvm_class_poolEntry* poolType;
	sjme_nvm_class_poolEntry* poolName;
	
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		outAnnotation == NULL || attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover class. */
	classInfo = context;
	if (classInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start with no annotations, these are dynamically allocated. */
	values = NULL;
	result = NULL;
	
	/* Read in the annotation type (the class). */
	poolType = NULL;
	if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(attrStream,
		inConstPool, SJME_NVM_CLASS_POOL_TYPE_UTF,
		SJME_JNI_FALSE, &poolType)) || poolType == NULL)
		goto fail_readType;
	
	/* Read in the number of values. */
	numValues = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(attrStream,
		&numValues)) || numValues < 0)
		goto fail_readNumValues;
	
	/* Allocate initial marker annotation, for any without values. */
	if (sjme_error_is(error = sjme_alloc(allocPool,
		sizeof(*result), (sjme_pointer*)&result)) ||
		result == NULL)
		goto fail_allocSingle;
	
	/* Set its info. */
	result->className = sjme_weakUpR(sjme_nvm_stringPool_string,
		poolType->utf.utf);
	result->numValues = numValues;
	
	/* If there are no sub-values, then we do not care. */
	if (numValues == 0)
		goto skip_noValues;
	
	/* Allocate values. */
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		numValues, &values, sjme_nvm_class_annotationValue, 0)) ||
		values == NULL)
		goto fail_allocList;
	
	/* Read in each value. */
	for (i = 0; i < numValues; i++)
	{
		/* Which value is being read? */
		value = &values->elements[i];
		
		/* Read in the annotation name (the field). */
		poolName = NULL;
		if (sjme_error_is(error = sjme_nvm_class_readPoolRefIndex(
			attrStream, inConstPool, SJME_NVM_CLASS_POOL_TYPE_UTF,
			SJME_JNI_FALSE, &poolName)) || poolName == NULL)
			goto fail_readName;
		
		/* Read in individual value. */
		if (sjme_error_is(error = sjme_nvm_class_parseAnnotationValue(
			allocPool, inConstPool, inStringPool, context,
			attrStream, value)))
			goto fail_readValue;
		
		/* Set field name as it is applicable here. */
		value->fieldName = sjme_weakUpR(sjme_nvm_stringPool_string,
			poolName->utf.utf); 
	}
	
	/* Success! Or the annotation has no values */
skip_noValues:
	*outAnnotation = result;
	return SJME_ERROR_NONE;
	
fail_readArrayCount:
fail_readString:
fail_readName:
fail_readValue:
fail_badTag:
fail_readTag:
fail_readNumValues:
fail_readType:
fail_allocList:
	if (values != NULL)
		sjme_alloc_free(values);
fail_allocSingle:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_class_classAnnotations(
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
	sjme_nvm_class_info classInfo;
	sjme_jint i;
	sjme_jshort numAnnotations;
	sjme_nvm_class_annotation annotation;
	sjme_list(sjme_nvm_class_annotation)* annotations;
	
	if (allocPool == NULL || inConstPool == NULL || inStringPool == NULL ||
		attrName == NULL || attrData == NULL ||
		attrStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover class. */
	classInfo = context;
	if (classInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Read annotation count. */
	numAnnotations = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(attrStream,
		&numAnnotations)) || numAnnotations < 0)
		return sjme_error_default(error);
	
	/* Why? */
	if (numAnnotations == 0)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate annotation storage. */
	annotations = NULL;
	if (sjme_error_is(error = sjme_list_replace(allocPool,
		numAnnotations, &annotations,
		sjme_nvm_class_annotation, 0)) || annotations == NULL)
		goto fail_allocList;
	
	/* Read in each annotation. */
	for (i = 0; i < numAnnotations; i++)
	{
		/* Read single annotation. */
		annotation = NULL;
		if (sjme_error_is(error = sjme_nvm_class_classAnnotation(
			allocPool, inConstPool, inStringPool, context,
			attrStream, &annotation)) ||
			annotation == NULL)
			goto fail_readSingle;
		
		/* Store annotation here. */
		annotations->elements[i] = annotation;
	}
	
	/* Store final list, if anything was even allocated. */
	classInfo->annotations = annotations;
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_readSingle:
fail_allocList:
	if (annotations != NULL)
	{
		for (i = 0; i < numAnnotations; i++)
			if (annotations->elements[i] != NULL)
				sjme_alloc_free(annotations->elements[i]);
		sjme_alloc_free(annotations);
	}
	
	return sjme_error_default(error);
}
