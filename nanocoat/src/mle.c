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
	{"Lcc/squirreljme/jvm/mle/"#what";", \
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
	sjme_attrInNotNull sjme_lpcstr className,
	sjme_attrInNotNull sjme_lpcstr methodName,
	sjme_attrInNotNull sjme_lpcstr methodType,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	const sjme_nvm_mle* major;
	const sjme_nvm_mleShelf* minor;
	sjme_jint i;
	
	if (inFrame == NULL || className == NULL || methodName == NULL ||
		methodType == NULL || argR == NULL || (argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Look for the shelf. */
	for (major = sjme_nvm_mleShelves; major->className != NULL; major++)
		if (strcmp(className, major->className) == 0)
		{
			/* Look for the function. */
			for (minor = major->shelf; minor->name != NULL; minor++)
				if (strcmp(methodName, minor->name) == 0 &&
					strcmp(methodType, minor->type) == 0)
				{
					/* Check arguments. */
					for (i = 0; i < argC; i++)
						if (minor->argX[i] == '\0' ||
							sjme_nvm_mleTToA[argV[i].type] != minor->argX[i])
							return SJME_ERROR_INCOMPATIBLE_MLE_CALL;
					
					/* Forward call. */
					return minor->function(inFrame, argR, argC, argV);
				}
			
			/* Not found. */
			return SJME_ERROR_UNKNOWN_MLE_FUNCTION;
		}

	/* Not found. */
	return SJME_ERROR_UNKNOWN_MLE_SHELF;
}
