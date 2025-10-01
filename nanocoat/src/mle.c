/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

/** Standard shelf definition. */
#define SJME_NVM_MLE_SHELF_DEF(what) \
	{"cc/squirreljme/jvm/mle/"#what, \
	SJME_TOKEN_PASTE(sjme_nvm_mle, what)}

static const sjme_nvm_mle sjme_nvm_mleShelves[] =
{
	SJME_NVM_MLE_SHELF_DEF(AtomicShelf),
	SJME_NVM_MLE_SHELF_DEF(DebugShelf),
	SJME_NVM_MLE_SHELF_DEF(JarPackageShelf),
	SJME_NVM_MLE_SHELF_DEF(MathShelf),
	SJME_NVM_MLE_SHELF_DEF(MidiShelf),
	SJME_NVM_MLE_SHELF_DEF(NativeArchiveShelf),
	SJME_NVM_MLE_SHELF_DEF(ObjectShelf),
	SJME_NVM_MLE_SHELF_DEF(PencilFontShelf),
	SJME_NVM_MLE_SHELF_DEF(PencilShelf),
	SJME_NVM_MLE_SHELF_DEF(ReferenceShelf),
	SJME_NVM_MLE_SHELF_DEF(ReflectionShelf),
	SJME_NVM_MLE_SHELF_DEF(RuntimeShelf),
	SJME_NVM_MLE_SHELF_DEF(StringShelf),
	SJME_NVM_MLE_SHELF_DEF(TaskShelf),
	SJME_NVM_MLE_SHELF_DEF(TerminalShelf),
	SJME_NVM_MLE_SHELF_DEF(ThreadShelf),
	SJME_NVM_MLE_SHELF_DEF(TypeShelf),
	
	{NULL, NULL}
};

/** Type ID to C character indicator. */
static const sjme_cchar sjme_nvm_mleTToA[SJME_NUM_JAVA_TYPE_IDS + 2] =
{
	'I', 'J', 'F', 'D', 'L', 'V', '\0'
};

sjme_errorCode sjme_mle_mleCall(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jmethodID methodID,
	sjme_attrInNotNull sjme_nvm_class_methodInfo methodInfo,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	const sjme_nvm_mle* major;
	sjme_nvm inState;
	
	if (inFrame == NULL || methodID == NULL || methodInfo == NULL ||
		argR == NULL || (argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Look for the shelf. */
	for (major = sjme_nvm_mleShelves; major->className != NULL; major++)
		if (sjme_charSeq_equalsUtfR(methodInfo->inClass->name->seq,
			major->className))
			return sjme_mle_mleCallShelf(inFrame, major,
				methodInfo->name->seq,
				methodInfo->type->seq, argR, argC, argV);

	/* If this is reached, then we need to forward to a native handler... */
	inState = SJME_F_S(inFrame);
	if (inState->hooks != NULL && inState->hooks->nativeCall != NULL)
	{
		/* Perform the native call. */
		if (sjme_error_is(error = inState->hooks->nativeCall(inFrame,
			methodID, methodInfo, argR, argC, argV)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_UNKNOWN_MLE_SHELF);

		/* Successful otherwise. */
		return SJME_ERROR_NONE;
	}

	/* Not found. */
	return SJME_ERROR_UNKNOWN_MLE_SHELF;
}

sjme_errorCode sjme_mle_mleCallFunction(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull const sjme_nvm_mleShelf* function,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_jvalueTyped result;
	sjme_cchar charR;
	
	if (inFrame == NULL || function == NULL ||
		argR == NULL || (argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG_MLE)
	/* Debug. */
	sjme_message("MLE.%s %s",
		function->name, function->type);
#endif
	
	/* Check arguments. */
	for (i = 0; i < argC; i++)
		if (function->argX[i + 1] == '\0' ||
			sjme_nvm_mleTToA[argV[i].t] != function->argX[i + 1])
			return SJME_ERROR_INCOMPATIBLE_MLE_CALL;
	
	/* Forward call. */
	memset(&result, 0, sizeof(result));
	result.t = SJME_NUM_BASIC_TYPE_IDS + SJME_NUM_JAVA_TYPE_IDS;
	if (sjme_error_is(error = function->function(inFrame, &result,
		argC, argV)))
		return sjme_error_default(error);

	/* Return type character code. */
	charR = function->argX[0];
	
	/* Did not write the correct return value? */
	if ((charR == 'V' &&
			result.t != SJME_NUM_BASIC_TYPE_IDS + SJME_NUM_JAVA_TYPE_IDS) ||
		(charR != 'V' && charR != sjme_nvm_mleTToA[result.t]))
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_WRITE);

	/* Set to void? */
	if (charR == 'V')
		result.t = SJME_JAVA_TYPE_ID_VOID;

	/* Success! */
	memmove(argR, &result, sizeof(result));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_mle_mleCallShelf(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull const sjme_nvm_mle* shelf,
	sjme_attrInNotNull sjme_charSeq methodName,
	sjme_attrInNotNull sjme_charSeq methodType,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	const sjme_nvm_mleShelf* minor;
	
	/* Look for the function. */
	for (minor = shelf->shelf; minor->name != NULL; minor++)
		if (sjme_charSeq_equalsUtfR(methodName, minor->name) &&
			sjme_charSeq_equalsUtfR(methodType, minor->type))
			return sjme_mle_mleCallFunction(inFrame, minor, argR, argC, argV);
	
	/* Not found. */
	return SJME_ERROR_UNKNOWN_MLE_FUNCTION;
}
