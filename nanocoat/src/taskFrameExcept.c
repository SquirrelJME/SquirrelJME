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
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/taskStore.h"

sjme_errorCode sjme_nvm_task_frameEmit(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_nvm_task_commonClassId commonClass,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	...)
{
	sjme_errorCode error;
	va_list args;

	if (inFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Start arguments. */
	va_start(args, message);

	/* Forward as this is just a convenience method. */
	error = sjme_nvm_task_threadEmitV(SJME_F_T(inFrame), commonClass, cause,
		message, args);

	/* End. */
	va_end(args);

	/* Return the result of the forward. */
	return error;
}

sjme_errorCode sjme_nvm_task_frameEmitE(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_errorCode errorCode,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	...)
{
	sjme_errorCode error;
	va_list args;

	if (inFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Start arguments. */
	va_start(args, message);

	/* Forward as this is just a convenience method. */
	error = sjme_nvm_task_threadEmitEV(SJME_F_T(inFrame), errorCode, cause,
		message, args);

	/* End. */
	va_end(args);

	/* Return the result of the forward. */
	return error;
}
