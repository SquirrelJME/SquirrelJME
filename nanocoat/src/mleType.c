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
#include "sjme/nvm/cleanup.h"

SJME_NVM_MLE_FUNCTION_DECL(findType)
{
	sjme_errorCode error;
	sjme_jstring string;
	sjme_jclass foundClass;

	/* Must be an actual string. */
	string = (sjme_jstring)argV[0].value.l;
	if (!sjme_nvm_isAR(string, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Looking for class %s...",
		sjme_charSeq_tempUtf(string->seq));
#endif

	/* Note that on class lookup this way, we always want to initialize */
	/* the target class as it will immediately be fully available. */
	/* Specifically an array type? */
	foundClass = NULL;
	if (sjme_charSeq_charAtIs(string->seq, 0, '[') ==
		SJME_ERROR_NONE)
	{
		/* Can just use the field type loader here. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadF(
			inFrame->inTask->classLoader, &foundClass,
			inFrame->inThread, string->seq, SJME_JNI_TRUE)) ||
			foundClass == NULL)
			return sjme_error_vmError(inFrame, error);
	}

	/* Normal class otherwise. */
	else
	{
		/* Otherwise this will be a binary name. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
			inFrame->inTask->classLoader, &foundClass,
			inFrame->inThread, string->seq, SJME_JNI_TRUE)) ||
			foundClass == NULL)
		{
			/* Another error other than not found? */
			if (error != SJME_ERROR_NO_CLASS)
				return sjme_error_vmError(inFrame, error);
		}
	}
	
	/* Success! */
	argR->type = SJME_JAVA_TYPE_ID_OBJECT;
	argR->value.l = SJME_AS_JOBJECT(foundClass);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeToClass)
{
	sjme_jobject inType;

	/* Must be an actual object type. */
	inType = argV[0].value.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* Note that types in NanoCoat are just pure classes, so they are 1:1. */
	argR->type = SJME_JAVA_TYPE_ID_OBJECT;
	argR->value.l = inType;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(TypeShelf) =
{
#if 0
	SJME_NVM_MLE_DEFINE(binaryName,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(binaryPackageName,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(classToType,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(component,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(componentRoot,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(dimensions,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(enumValues,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(equals,
			SJME_MD(,),
			""),
#endif
	SJME_NVM_MLE_DEFINE(findType,
			SJME_MD(SJME_MD_TYPE, SJME_MD_STRING),
			"LL"),
#if 0
	SJME_NVM_MLE_DEFINE(initClass,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(inJar,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(interfaces,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(isArray,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(isAssignableFrom,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(isClassInit,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(isEnum,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(isInterface,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(isPrimitive,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(objectType,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(runtimeName,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(superClass,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfBoolean,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfByte,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfShort,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfCharacter,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfInteger,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfLong,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfFloat,
			SJME_MD(,),
			""),
	SJME_NVM_MLE_DEFINE(typeOfDouble,
			SJME_MD(,),
			""),
#endif
	SJME_NVM_MLE_DEFINE(typeToClass,
			SJME_MD(SJME_MD_CLASS, SJME_MD_TYPE),
			"LL"),
	
	SJME_NVM_MLE_STOP()
};
