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
	sjme_attrInNotNull sjme_charSeq className,
	sjme_attrInNotNull sjme_charSeq methodName,
	sjme_attrInNotNull sjme_charSeq methodType,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	const sjme_nvm_mle* major;
	
	if (inFrame == NULL || className == NULL || methodName == NULL ||
		methodType == NULL || argR == NULL || (argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Look for the shelf. */
	for (major = sjme_nvm_mleShelves; major->className != NULL; major++)
		if (sjme_charSeq_equalsUtfR(className, major->className))
			return sjme_mle_mleCallShelf(inFrame, major, methodName,
				methodType, argR, argC, argV);

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
	sjme_jint i;
	
	if (inFrame == NULL || function == NULL ||
		argR == NULL || (argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Check arguments. */
	for (i = 0; i < argC; i++)
		if (function->argX[i + 1] == '\0' ||
			sjme_nvm_mleTToA[argV[i].t] != function->argX[i + 1])
			return SJME_ERROR_INCOMPATIBLE_MLE_CALL;
					
	/* Forward call. */
	return function->function(inFrame, argR, argC, argV);
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
