/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instanceInspect.h"
#include "sjme/nvm/taskStore.h"

sjme_errorCode sjme_nvm_task_threadEmit(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonClass,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	...)
{
	sjme_errorCode error;
	va_list args;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start arguments. */
	va_start(args, message);
	
	/* Forward. */
	error = sjme_nvm_task_threadEmitV(inThread, commonClass, cause,
		message, args);
	
	/* End. */
	va_end(args);
	
	/* Return the result of the forward. */
	return error;
}

sjme_errorCode sjme_nvm_task_threadEmitE(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_errorCode errorCode,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	...)
{
	sjme_errorCode error;
	va_list args;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start arguments. */
	va_start(args, message);
	
	/* Forward. */
	error = sjme_nvm_task_threadEmitEV(inThread, errorCode, cause,
		message, args);
	
	/* End. */
	va_end(args);
	
	/* Return the result of the forward. */
	return error;
}

sjme_errorCode sjme_nvm_task_threadEmitEV(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_errorCode errorCode,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	va_list args)
{
	sjme_errorCode error;
	sjme_nvm_task_commonClassId mapped;
	va_list argsCopy;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Map this error to a common class. */
	mapped = SJME_NVM_TASK_NUM_COMMON_CLASS;
	switch (errorCode)
	{
		case SJME_ERROR_ARRAY_STORE:
			mapped = SJME_NVM_COMMON_EXCEPTION_ARRAY_STORE;
			break;
		
		case SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS:
			mapped = SJME_NVM_COMMON_EXCEPTION_ARRAY_IOOB;
			break;
			
		case SJME_ERROR_ARITHMETIC:
		case SJME_ERROR_DIVIDE_BY_ZERO:
			mapped = SJME_NVM_COMMON_EXCEPTION_ARITHMETIC;
			break;
		
		case SJME_ERROR_CLASS_CAST:
			mapped = SJME_NVM_COMMON_EXCEPTION_CLASS_CAST;
			break;
			
		case SJME_ERROR_MLE_CALL:
			mapped = SJME_NVM_COMMON_EXCEPTION_MLE_CALL;
			break;
			
		case SJME_ERROR_NO_CLASS:
			mapped = SJME_NVM_COMMON_EXCEPTION_CLASS_NOT_FOUND;
			break;
		
		case SJME_ERROR_NULL_STACK_POINTER:
			mapped = SJME_NVM_COMMON_EXCEPTION_NULL_POINTER;
			break;
			
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Was not mapped still? */
	if (mapped == SJME_NVM_TASK_NUM_COMMON_CLASS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Start arguments. */
	va_copy(argsCopy, args);
	
	/* Forward. */
	error = sjme_nvm_task_threadEmitV(inThread, mapped, cause,
		message, argsCopy);
	
	/* End. */
	va_end(argsCopy);
	
	/* Return the result of the forward. */
	return error;
}

sjme_errorCode sjme_nvm_task_threadEmitV(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonClass,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	sjme_attrInValue va_list args)
{
	sjme_errorCode error;
	sjme_jclass tossClass, throwableClass;
	sjme_jthrowable toss, oldTossed;
	sjme_jfieldID initCauseID, causeID;
	sjme_jvalueTyped argV[1];
	sjme_cchar buf[SJME_DEBUG_BUF_SIZE];
	va_list copy;

	/* TODO FIXME: Optimize/shrink for SDCC. */
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This common class is not a throwable? */
	switch (commonClass)
	{
		case SJME_NVM_COMMON_EXCEPTION_ARITHMETIC:
		case SJME_NVM_COMMON_EXCEPTION_ARRAY_IOOB:
		case SJME_NVM_COMMON_EXCEPTION_ARRAY_STORE:
		case SJME_NVM_COMMON_EXCEPTION_CLASS_CAST:
		case SJME_NVM_COMMON_EXCEPTION_NULL_POINTER:
		case SJME_NVM_COMMON_EXCEPTION_LINKAGE_ERROR:
			break;
			
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Locate the class to toss. */
	tossClass = NULL;
	if (sjme_error_is(error = sjme_nvm_task_commonClass(inThread,
		commonClass, &tossClass, SJME_JNI_TRUE)) ||
		tossClass == NULL)
		return sjme_error_default(error);

	/* Allocate new instance of object. */
	toss = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(inThread,
		-1, SJME_NVM_STRUCT_OBJECT_INSTANCE,
		SJME_AS_JOBJECTP(&toss), tossClass)) || toss == NULL)
		return sjme_error_default(error);

	/* Set as tossed! */
	oldTossed = NULL;
	if (!sjme_atomic_cs(sjme_jobject, &inThread->tossed,
		NULL, SJME_AS_JOBJECT(toss)))
	{
		/* Get the old tossed to print it out. */
		oldTossed = SJME_AS_JTHROWABLE(
			sjme_atomic_g(sjme_jobject, &inThread->tossed));

		/* Print it out, assuming it is not our cause. */
		if (oldTossed != cause)
		{
			sjme_message("DROPPING DOUBLE TOSSED EXCEPTION:");
			if (sjme_error_is(error = sjme_nvm_task_stackTraceThrowable(
				inThread, SJME_AS_JTHROWABLE(oldTossed))))
				return sjme_error_default(error);
		}

		/* Set new value. */
		sjme_atomic_s(sjme_jobject, &inThread->tossed,
			SJME_AS_JOBJECT(toss));

		/* Count down the old toss since it is no longer stored, if it */
		/* is not our cause. */
		if (oldTossed != cause)
		{
			if (sjme_error_is(error = sjme_nvm_instance_countDown(
				SJME_AS_JOBJECT(oldTossed))))
				return sjme_error_default(error);
		}
	}

	/* Set level so we can run the constructor. */
	sjme_atomic_s(sjme_jint, &inThread->tossedLevel, inThread->numFrames);

	/* Build message string. */
	va_copy(copy, args);

	memset(buf, 0, sizeof(buf));
	if (message != NULL)
	{
		vsnprintf(buf, SJME_DEBUG_BUF_SIZE - 1, message, copy);
		buf[SJME_DEBUG_BUF_SIZE - 1] = '\0';
	}
	
	va_end(copy);

	/* Setup string argument. */
	memset(&argV, 0, sizeof(argV));
	argV[0].t = SJME_JAVA_TYPE_ID_OBJECT;
	argV[0].v.l = NULL;
	if (message != NULL)
	{
		if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
			inThread, SJME_AS_JSTRINGP(&argV[0].v.l), 
			SJME_JNI_FALSE, buf)))
			return sjme_error_default(error);
	}

	/* Initialize exception. */
	if (sjme_error_is(error = sjme_nvm_instance_defaultInit(inThread,
		NULL, SJME_AS_JOBJECT(toss),
		"(Ljava/lang/String;)V", 1, argV)))
		return sjme_error_default(error);

	/* Count up once as it is now stored in tossed. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(
		SJME_AS_JOBJECT(toss))))
		return sjme_error_default(error);

	/* Initialize cause, if there is one to set. */
	if (cause != NULL)
	{
		/* Locate the throwable class. */
		throwableClass = NULL;
		if (sjme_error_is(error = sjme_nvm_task_commonClass(inThread,
			SJME_NVM_COMMON_THROWABLE, &throwableClass,
			SJME_JNI_TRUE)))
			return sjme_error_default(error);

		/* Lookup initCause to flag it on. */
		initCauseID = NULL;
		if (sjme_error_is(error = sjme_nvm_vmField_idByNameTypeU(
			throwableClass, inThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_TRUE,
			"_initCause", "Z",
			&initCauseID)) || initCauseID == NULL)
			return sjme_error_default(error);

		/* Lookup cause, to set it. */
		causeID = NULL;
		if (sjme_error_is(error = sjme_nvm_vmField_idByNameTypeU(
			throwableClass, inThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_TRUE,
			"_cause", "Ljava/lang/Throwable;",
			&causeID)) || causeID == NULL)
			return sjme_error_default(error);
		
		/* Flag cause as initialized. */
		if (sjme_error_is(error = sjme_nvm_instance_fieldSetI(
			inThread,
			SJME_AS_JOBJECT(toss), initCauseID, 
			SJME_JNI_TRUE)))
			return sjme_error_default(error);
		
		/* Set actual cause now, and its check value directly. */
		if (sjme_error_is(error = sjme_nvm_instance_fieldSetO(
			inThread,
			SJME_AS_JOBJECT(toss), causeID, NULL,
			SJME_AS_JOBJECT(cause))))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
