/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/tread.h"

#define SJME_NVM_FRAME_TREAD_ACCESSOR_ADDRESS_GENERIC(cType, javaType, jType) \
	sjme_errorCode SJME_TOKEN_PASTE_PP( \
			sjme_nvm_frameTreadAccessorAddress, javaType)( \
		sjme_attrInNotNull sjme_nvm_frame* frame, \
		sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor, \
		sjme_attrInNotNull sjme_nvm_frameTread* tread, \
		sjme_attrInPositive sjme_jint treadIndex, \
		sjme_attrOutNotNull sjme_pointer* outAddress) \
	{ \
		if (frame == NULL || accessor == NULL || tread == NULL || \
			outAddress == NULL) \
			return SJME_ERROR_NULL_ARGUMENTS; \
		\
		if (treadIndex < 0 || treadIndex >= tread->count) \
			return SJME_ERROR_INVALID_ARGUMENT; \
		\
		/* Calculate address. */ \
		*outAddress = &tread->values. \
			SJME_TOKEN_PASTE_PP(jType, s)[treadIndex]; \
		return SJME_ERROR_NONE; \
	}
	
SJME_NVM_FRAME_TREAD_ACCESSOR_ADDRESS_GENERIC(sjme_jint, Integer, jint)
SJME_NVM_FRAME_TREAD_ACCESSOR_ADDRESS_GENERIC(sjme_jlong, Long, jlong)
SJME_NVM_FRAME_TREAD_ACCESSOR_ADDRESS_GENERIC(sjme_jfloat, Float, jfloat)
SJME_NVM_FRAME_TREAD_ACCESSOR_ADDRESS_GENERIC(sjme_jdouble, Double, jdouble)
SJME_NVM_FRAME_TREAD_ACCESSOR_ADDRESS_GENERIC(sjme_jobject, Object, jobject)

static sjme_errorCode sjme_nvm_frameTreadAccessorGetTreadGeneric(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInOutNotNull sjme_nvm_frameTread** outTread)
{
	if (frame == NULL || accessor == NULL || outTread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get the base tread by its type ID. */
	*outTread = frame->treads[accessor->typeId];
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_frameTreadAccessorReadGeneric(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInNotNull const sjme_nvm_frameTread* tread,
	sjme_attrInPositive sjme_jint treadIndex,
	sjme_attrOutNotNull sjme_pointer outVal)
{
	sjme_pointer fromAddr;
	
	if (frame == NULL || tread == NULL || outVal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (treadIndex < 0 || treadIndex >= tread->count)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Get the source address. */
	fromAddr = NULL;
	if (!accessor->address(frame, accessor, (sjme_nvm_frameTread*)tread,
		treadIndex, &fromAddr) || fromAddr == NULL)
		return SJME_ERROR_TREAD_INVALID_READ;
	
	/* A memory operation can just be used here. */
	memmove(outVal, fromAddr, accessor->size);
	return SJME_JNI_TRUE;
}

static sjme_errorCode sjme_nvm_frameTreadAccessorWriteGeneric(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrInNotNull sjme_nvm_frameTread* tread,
	sjme_attrInPositive sjme_jint treadIndex,
	sjme_attrInNotNull sjme_cpointer inVal)
{
	sjme_pointer toAddr;
	
	if (frame == NULL || tread == NULL || inVal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (treadIndex < 0 || treadIndex >= tread->count)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Get the target address. */
	toAddr = NULL;
	if (!accessor->address(frame, accessor, tread, treadIndex, &toAddr) ||
		toAddr == NULL)
		return SJME_ERROR_TREAD_INVALID_WRITE;
	
	/* A memory operation can just be used here. */
	memmove(toAddr, inVal, accessor->size);
	return SJME_ERROR_NONE;
}

static const sjme_nvm_frameTreadAccessor sjme_nvm_constFrameTreadAccessor[
	SJME_NUM_JAVA_TYPE_IDS] =
{
	/* Integer. */
	{
		SJME_JAVA_TYPE_ID_INTEGER,
		sizeof(sjme_jint),
		"sjme_jint",
		SJME_ERROR_TOP_NOT_INTEGER,
		sjme_nvm_frameTreadAccessorGetTreadGeneric,
		sjme_nvm_frameTreadAccessorAddressInteger,
		sjme_nvm_frameTreadAccessorReadGeneric,
		sjme_nvm_frameTreadAccessorWriteGeneric
	},
	
	/* Long. */
	{
		SJME_JAVA_TYPE_ID_LONG,
		sizeof(sjme_jlong),
		"sjme_jlong",
		SJME_ERROR_TOP_NOT_LONG,
		sjme_nvm_frameTreadAccessorGetTreadGeneric,
		sjme_nvm_frameTreadAccessorAddressLong,
		sjme_nvm_frameTreadAccessorReadGeneric,
		sjme_nvm_frameTreadAccessorWriteGeneric
	},
	
	/* Float. */
	{
		SJME_JAVA_TYPE_ID_FLOAT,
		sizeof(sjme_jfloat),
		"sjme_jfloat",
		SJME_ERROR_TOP_NOT_FLOAT,
		sjme_nvm_frameTreadAccessorGetTreadGeneric,
		sjme_nvm_frameTreadAccessorAddressFloat,
		sjme_nvm_frameTreadAccessorReadGeneric,
		sjme_nvm_frameTreadAccessorWriteGeneric
	},
	
	/* Double. */
	{
		SJME_JAVA_TYPE_ID_DOUBLE,
		sizeof(sjme_jdouble),
		"sjme_jdouble",
		SJME_ERROR_TOP_NOT_DOUBLE,
		sjme_nvm_frameTreadAccessorGetTreadGeneric,
		sjme_nvm_frameTreadAccessorAddressDouble,
		sjme_nvm_frameTreadAccessorReadGeneric,
		sjme_nvm_frameTreadAccessorWriteGeneric
	},
	
	/* Object. */
	{
		SJME_JAVA_TYPE_ID_OBJECT,
		sizeof(sjme_jobject),
		"sjme_jobject",
		SJME_ERROR_TOP_NOT_OBJECT,
		sjme_nvm_frameTreadAccessorGetTreadGeneric,
		sjme_nvm_frameTreadAccessorAddressObject,
		sjme_nvm_frameTreadAccessorReadGeneric,
		sjme_nvm_frameTreadAccessorWriteGeneric
	}
};

const sjme_nvm_frameTreadAccessor* sjme_nvm_frameTreadAccessorByType(
	sjme_attrInRange(SJME_JAVA_TYPE_ID_INTEGER, SJME_NUM_JAVA_TYPE_IDS - 1)
		sjme_javaTypeId typeId)
{
	/* Not valid at all? */
	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return NULL;
	
	return &sjme_nvm_constFrameTreadAccessor[typeId];
}
