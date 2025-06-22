/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/cleanup.h"
#include "sjme/config.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(arrayClone)
{
	sjme_errorCode error;
	sjme_jarray array;
	sjme_jarray clone;
	sjme_jobject element;
	sjme_jint length, i;

	/* Must be an actual array. */
	array = (sjme_jarray)argV[0].v.l;
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Lock the array. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&array->object.common.lock)))
		return sjme_error_vmError(inFrame, error);

	/* Remember the length, since we will be locking. */
	length = array->length;

	/* Allocate a new array which uses the same type. */
	clone = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), SJME_AS_JOBJECTP(&clone),
		sjme_atomic_sjme_jclass_get(
			&array->object.isClass->componentType), length)) || clone == NULL)
		goto fail_alloc;

	/* Copy all values over. */
	memmove(&clone->e, &array->e, sjme_nvm_typeMul[array->type] * length);

	/* If this is an object array, everything needs to be counted. */
	if (array->type == SJME_JAVA_TYPE_ID_OBJECT)
		for (i = 0; i < length; i++)
		{
			/* Skip null elements. */
			element = array->e.l[i];
			if (element == NULL)
				continue;

			/* Count up. */
			if (sjme_error_is(error = sjme_alloc_weakRef(element, NULL)))
				goto fail_count;
		}

	/* Release lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&array->object.common.lock, NULL)))
		return sjme_error_vmError(inFrame, error);

	/* Return the resultant clone. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(clone);
	return SJME_ERROR_NONE;

fail_count:
fail_alloc:
	sjme_thread_spinLockRelease(&array->object.common.lock, NULL);
	return sjme_error_vmError(inFrame, error);
}

SJME_NVM_MLE_FUNCTION_DECL(arrayLength)
{
	sjme_jarray array;

	/* Cannot be null. */
	array = (sjme_jarray)argV[0].v.l;
	if (array == NULL)
		return SJME_ERROR_MLE_CALL;
	
	/* If an array, set the length. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		argV->v.i = array->length;
	else
		argV->v.i = -1;

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(newInstance)
{
	sjme_errorCode error;
	sjme_jclass inType;
	sjme_jmethodID defaultCon;
	sjme_nvm_frame subFrame;
	sjme_jvalueTyped initArgV[1];
	sjme_jobject result;

	/* Must be an actual class type. */
	inType = (sjme_jclass)argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Locate the default constructor. */
	defaultCon = NULL;
	if (sjme_error_is(sjme_nvm_vmClass_methodIDByNameTypeU(
		inType, SJME_F_T(inFrame), SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE, "<init>", "()V",
		&defaultCon)) || defaultCon == NULL)
		return SJME_ERROR_MLE_CALL;
	
	/* Create new object instance. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(SJME_F_T(inFrame),
		-1, SJME_NVM_STRUCT_OBJECT_INSTANCE, &result,
		inType)) || result == NULL)
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_MLE_CALL));
	
	/* Setup call arguments. */
	memset(&initArgV, 0, sizeof(initArgV));
	initArgV[0].t = SJME_JAVA_TYPE_ID_OBJECT;
	initArgV[0].v.l = result;

	/* Call the default constructor. */
	subFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnter(SJME_F_T(inFrame),
		&subFrame, defaultCon, SJME_NVM_CLASS_MEMBER_INSTANCE,
		1, initArgV)) || subFrame == NULL)
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_MLE_CALL));
	
	/* Note that types in NanoCoat are just pure classes, so they are 1:1. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = result;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(ObjectShelf) =
{
	SJME_NVM_MLE_DEFINE(arrayClone,
		SJME_MD(SJME_MD_OBJECT, SJME_MD_OBJECT),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(arrayLength,
		SJME_MD(SJME_MD_I, SJME_MD_OBJECT),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(newInstance,
		SJME_MD(SJME_MD_OBJECT, SJME_MD_TYPE),
		"L", "L"),
	
	SJME_NVM_MLE_STOP()
};
