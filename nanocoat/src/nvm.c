/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/tread.h"

static sjme_jboolean sjme_debug_vmTraceErrorIs(sjme_errorCode error)
{
	switch (error)
	{
		case SJME_ERROR_ARGUMENT_COUNT_MISMATCH:
		case SJME_ERROR_ARGUMENT_TYPE_MISMATCH:
		case SJME_ERROR_CLASS_CHANGED:
		case SJME_ERROR_CLASS_TOO_MANY_MEMBERS:
		case SJME_ERROR_FRAME_MISSING_STACK_TREADS:
		case SJME_ERROR_INVALID_BINARY_NAME:
		case SJME_ERROR_INVALID_CLASS_FLAGS:
		case SJME_ERROR_INVALID_CLASS_LOADER:
		case SJME_ERROR_INVALID_CLASS_MAGIC:
		case SJME_ERROR_INVALID_CLASS_NAME:
		case SJME_ERROR_INVALID_CLASS_POOL_COUNT:
		case SJME_ERROR_INVALID_CLASS_POOL_INDEX:
		case SJME_ERROR_INVALID_CLASS_VERSION:
		case SJME_ERROR_INVALID_FIELD_FLAGS:
		case SJME_ERROR_INVALID_FIELD_TYPE:
		case SJME_ERROR_INVALID_IDENTIFIER:
		case SJME_ERROR_INVALID_INSTRUCTION:
		case SJME_ERROR_INVALID_METHOD_FLAGS:
		case SJME_ERROR_INVALID_METHOD_TYPE:
		case SJME_ERROR_INVALID_REFERENCE_POP:
		case SJME_ERROR_INVALID_REFERENCE_PUSH:
		case SJME_ERROR_LINKAGE_ERROR:
		case SJME_ERROR_LOCAL_INDEX_INVALID:
		case SJME_ERROR_LOCAL_INVALID_READ:
		case SJME_ERROR_LOCAL_INVALID_WRITE:
		case SJME_ERROR_METHOD_MULTIPLE_CODE:
		case SJME_ERROR_MLE_CALL:
		case SJME_ERROR_NEGATIVE_ARRAY_SIZE:
		case SJME_ERROR_NO_CLASS:
		case SJME_ERROR_NO_FIELD:
		case SJME_ERROR_NO_METHOD:
		case SJME_ERROR_PURE_VIRTUAL_CALL:
		case SJME_ERROR_STACK_INDEX_INVALID:
		case SJME_ERROR_STACK_INVALID_READ:
		case SJME_ERROR_STACK_INVALID_WRITE:
		case SJME_ERROR_STACK_OVERFLOW:
		case SJME_ERROR_STACK_UNDERFLOW:
		case SJME_ERROR_SUPER_CLASS_INVALID:
		case SJME_ERROR_TOP_NOT_DOUBLE:
		case SJME_ERROR_TOP_NOT_FLOAT:
		case SJME_ERROR_TOP_NOT_INTEGER:
		case SJME_ERROR_TOP_NOT_LONG:
		case SJME_ERROR_TOP_NOT_OBJECT:
		case SJME_ERROR_TREAD_INDEX_INVALID:
		case SJME_ERROR_TREAD_INVALID_READ:
		case SJME_ERROR_TREAD_INVALID_WRITE:
		case SJME_ERROR_UNBOUND_METHOD:
		case SJME_ERROR_UNKNOWN_MLE_FUNCTION:
		case SJME_ERROR_INCOMPATIBLE_MLE_CALL:
		case SJME_ERROR_UNKNOWN_MLE_SHELF:
		case SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE:
			return SJME_JNI_TRUE;

		default:
			return SJME_JNI_FALSE;
	}
}

sjme_errorCode sjme_error_vmErrorR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull void* vmContext,
	sjme_attrInValue sjme_errorCode error)
{
	/* Emit trace? */
	if (sjme_debug_vmTraceErrorIs(error) ||
		error == SJME_ERROR_NOT_IMPLEMENTED)
	{
		/* Emit stack trace, if acceptable. */
		if (sjme_nvm_isAR(vmContext, SJME_NVM_STRUCT_FRAME))
			sjme_nvm_task_stackTrace(((sjme_nvm_frame)vmContext)->inThread);
		else if (sjme_nvm_isAR(vmContext, SJME_NVM_STRUCT_THREAD))
			sjme_nvm_task_stackTrace(vmContext);

#if defined(SJME_CONFIG_DEBUG)
		/* Fail with a TO-DO. */
		sjme_todoR(file, line, func, "NVM ERROR: %d!",
			(int)error);
#endif
	}

	/* Fall through. */
	return sjme_error_default(error);
}


