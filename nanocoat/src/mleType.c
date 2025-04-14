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

SJME_NVM_MLE_FUNCTION_DECL(findType)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	SJME_NVM_MLE_DEFINE(typeToClass,
			SJME_MD(,),
			""),
#endif
	SJME_NVM_MLE_STOP()
};
