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
		SJME_F_T(inFrame), SJME_AS_JARRAYP(&clone),
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
			if (sjme_error_is(error = sjme_nvm_instance_countUp(element)))
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

SJME_NVM_MLE_FUNCTION_DECL_ALT(arrayCopy, boolean)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(arrayCopy, generic)
{
	sjme_jarray src, dst;
	sjme_jint srcOff, dstOff, len;
	sjme_jint mul;

	/* Gather inputs. */
	src = (sjme_jarray)argV[0].v.l;
	srcOff = argV[1].v.i;
	dst = (sjme_jarray)argV[2].v.l;
	dstOff = argV[3].v.i;
	len = argV[4].v.i;

	/* Check arguments. */
	if (src == NULL || dst == NULL ||
		!sjme_nvm_isAR(src, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		!sjme_nvm_isAR(dst, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		src->type != dst->type ||
		srcOff < 0 || (srcOff + len) < 0 || (srcOff + len) > src->length ||
		dstOff < 0 || (dstOff + len) < 0 || (dstOff + len) > dst->length ||
		len < 0)
		return SJME_ERROR_MLE_CALL;

	/* This is just a memmove, based on the actual type size. */
	mul = sjme_nvm_typeMul[src->type];
	memmove(SJME_POINTER_OFFSET(&dst->e, dstOff * mul),
		SJME_POINTER_OFFSET(&src->e, srcOff * mul),
		mul * len);

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(arrayFill, boolean)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(arrayFill, generic)
{
#define MAX_BYTES 8
	sjme_jarray into;
	sjme_jint off, len;
	sjme_jint mul, i, dup;
	sjme_pointer at;
	sjme_jubyte raw[MAX_BYTES];
	
	/* Gather inputs. */
	into = (sjme_jarray)argV[0].v.l;
	off = argV[1].v.i;
	len = argV[2].v.i;

	/* Check arguments. */
	if (into == NULL ||
		!sjme_nvm_isAR(into, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		off < 0 || len < 0 || (off + len) < 0 || (off + len) > into->length)
		return SJME_ERROR_MLE_CALL;

	/* Determine the value being written. */
	mul = sjme_nvm_typeMul[into->type];
	memmove(&raw[0], &argV[3].v, mul);

	/* We can optimize this more by filling more bytes at once if the value */
	/* is smaller. */
	dup = MAX_BYTES / mul;
	for (i = 1, at = &raw[mul]; i < dup;
		i++, at = SJME_POINTER_OFFSET(at, mul))
		memmove(at, &raw[0], mul);

	/* Write the buffer into the target. */
	for (at = SJME_POINTER_OFFSET(&into->e, off * mul);
		len > 0; len -= dup, at = SJME_POINTER_OFFSET(at, MAX_BYTES))
		memmove(at, &raw[0],
			(len * mul < MAX_BYTES ? len * mul : MAX_BYTES));

	/* Success! */
	return SJME_ERROR_NONE;
#undef MAX_BYTES
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
		argR->v.i = array->length;
	else
		argR->v.i = -1;

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(identityHashCode)
{
	sjme_jobject object;

	/* Grab arguments. */
	object = argV[0].v.l;

	/* Cannot be null. */
	if (object == NULL)
		return SJME_ERROR_MLE_CALL;
	
	/* Use the hash that is stored in the object. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = argV->v.l->identityHash;
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
		&subFrame, defaultCon, SJME_NVM_CALL_VIRTUAL,
		1, initArgV)) || subFrame == NULL)
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_MLE_CALL));
	
	/* Note that types in NanoCoat are just pure classes, so they are 1:1. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = result;
	return SJME_ERROR_NONE;
}

/** Duplicate defines for arrayCopy. */
#define SJME_NVM_MLE_DEFINE_ARRAY_COPY(name, type) \
	SJME_NVM_MLE_DEFINE_ALT(arrayCopy, name, \
		SJME_MD(SJME_MD_V, SJME_MD_A##type SJME_MD_I SJME_MD_A##type \
			SJME_MD_I SJME_MD_I), \
		"V", "LILII")

/** Duplicate defines for arrayFile. */
#define SJME_NVM_MLE_DEFINE_ARRAY_FILL(name, type, promote) \
	SJME_NVM_MLE_DEFINE_ALT(arrayFill, name, \
		SJME_MD(SJME_MD_V, SJME_MD_A##type SJME_MD_I SJME_MD_I \
			SJME_MD_##type), \
		"V", "LII" #promote)

SJME_NVM_MLE_SHELF_DECLARE(ObjectShelf) =
{
	SJME_NVM_MLE_DEFINE(arrayClone,
		SJME_MD(SJME_MD_OBJECT, SJME_MD_OBJECT),
		"L", "L"),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(boolean, Z),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, B),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, S),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, C),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, I),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, J),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, F),
	SJME_NVM_MLE_DEFINE_ARRAY_COPY(generic, D),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(boolean, Z, I),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, B, I),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, S, I),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, C, I),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, I, I),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, J, J),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, F, F),
	SJME_NVM_MLE_DEFINE_ARRAY_FILL(generic, D, D),
	SJME_NVM_MLE_DEFINE(arrayLength,
		SJME_MD(SJME_MD_I, SJME_MD_OBJECT),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(newInstance,
		SJME_MD(SJME_MD_OBJECT, SJME_MD_CLASS),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(identityHashCode,
		SJME_MD(SJME_MD_I, SJME_MD_OBJECT),
		"I", "L"),
	
	SJME_NVM_MLE_STOP()
};
