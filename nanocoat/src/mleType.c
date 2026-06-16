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

SJME_NVM_MLE_FUNCTION_DECL(binaryName)
{
	sjme_errorCode error;
	sjme_jclass inType;

	/* Must be an actual object type. */
	inType = (sjme_jclass)argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Return the name string for the class. */
	argR->v.l = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
		SJME_F_T(inFrame), SJME_AS_JSTRINGP(&argR->v.l),
		inType->info->name)) || argR->v.l == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Return the given string. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(binaryPackageName)
{
	sjme_errorCode error;
	sjme_jclass inType;

	/* Must be an actual object type. */
	inType = (sjme_jclass)argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Return the name string for the class. */
	argR->v.l = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
		SJME_F_T(inFrame), SJME_AS_JSTRINGP(&argR->v.l),
		inType->info->inPackage)) || argR->v.l == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Return the given string. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(classToType)
{
	sjme_jobject inType;

	/* Must be an actual object type. */
	inType = argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* Note that types in NanoCoat are just pure classes, so they are 1:1. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = inType;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(findType)
{
	sjme_errorCode error;
	sjme_jstring string;
	sjme_jclass foundClass;
	sjme_charSeq seq;

	/* Must be an actual string. */
	string = (sjme_jstring)argV[0].v.l;
	if (!sjme_nvm_isAR(string, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* The sequence must be valid. */
	seq = sjme_atomic_sjme_charSeq_get(&string->seq);
	if (seq == NULL)
		return SJME_ERROR_MLE_CALL;

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Looking for class %s...",
		sjme_charSeq_tempUtf(seq));
#endif

	/* Note that on class lookup this way, we always want to initialize */
	/* the target class as it will immediately be fully available. */
	/* Specifically an array type? */
	foundClass = NULL;
	if (sjme_charSeq_charAtIs(seq, 0, '[') ==
		SJME_ERROR_NONE)
	{
		/* Can just use the field type loader here. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadF(
			SJME_F_CL(inFrame), &foundClass,
			SJME_F_T(inFrame), seq, SJME_JNI_TRUE)) ||
			foundClass == NULL)
			return sjme_error_vmError(inFrame, error);
	}

	/* Normal class otherwise. */
	else
	{
		/* Otherwise this will be a binary name. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
			SJME_F_CL(inFrame), &foundClass,
			SJME_F_T(inFrame), seq, SJME_JNI_TRUE)) ||
			foundClass == NULL)
		{
			/* Another error other than not found? */
			if (error != SJME_ERROR_NO_CLASS)
				return sjme_error_vmError(inFrame, error);
		}
	}
	
	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(foundClass);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(inJar)
{
	sjme_errorCode error;
	sjme_jclass inType;
	sjme_nvm_rom_library library;

	/* Must be an actual class type. */
	inType = (sjme_jclass)argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Is there no actual library here? */
	library = inType->info->library;
	if (library == NULL)
	{
		argR->t = SJME_JAVA_TYPE_ID_OBJECT;
		argR->v.l = NULL;
		return SJME_ERROR_NONE;
	}

	/* Lookup the pre-cached bracket for this library. */
	if (sjme_error_is(error = sjme_nvm_task_bracketJarPackage(
		SJME_F_T(inFrame), library, (sjme_jbracketJarPackage*)&argR->v.l)) ||
		argR->v.l == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(objectType)
{
	sjme_jobject object;
	
	/* Must be a non-null object. */
	object = argV[0].v.l;
	if (object == NULL)
		return SJME_ERROR_MLE_CALL;
	
	/* This is rather simple, just getting the class of the object. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(object->isClass);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(runtimeName)
{
	sjme_errorCode error;
	sjme_jclass inType;

	/* Must be an actual class type. */
	inType = (sjme_jclass)argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Return the runtime string for the class. */
	argR->v.l = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
		SJME_F_T(inFrame), SJME_AS_JSTRINGP(&argR->v.l),
		(inType->info->runtimeName != NULL ?
			inType->info->runtimeName : inType->info->name))) ||
		argR->v.l == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Return the given string. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfBoolean)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BOOLEAN);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfByte)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BYTE);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfCharacter)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_CHARACTER);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfFloat)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_FLOAT);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfDouble)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_DOUBLE);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfInteger)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_INTEGER);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfLong)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_LONG);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeOfShort)
{
	/* Direct get of class type. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
		SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_SHORT);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(typeToClass)
{
	sjme_jobject inType;

	/* Must be an actual class type. */
	inType = argV[0].v.l;
	if (!sjme_nvm_isAR(inType, SJME_NVM_STRUCT_CLASS_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* Note that types in NanoCoat are just pure classes, so they are 1:1. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = inType;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(TypeShelf) =
{
	SJME_NVM_MLE_DEFINE(binaryName,
		SJME_MD(SJME_MD_STRING, SJME_MD_CLASS),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(binaryPackageName,
		SJME_MD(SJME_MD_STRING, SJME_MD_CLASS),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(classToType,
		SJME_MD(SJME_MD_CLASS, SJME_MD_CLASS),
		"L", "L"),
#if 0
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
		SJME_MD(SJME_MD_CLASS, SJME_MD_STRING),
		"LL", ),
#if 0
	SJME_NVM_MLE_DEFINE(initClass,
		SJME_MD(,),
		""),
#endif
	SJME_NVM_MLE_DEFINE(inJar,
		SJME_MD(SJME_MD_JAR_PACKAGE, SJME_MD_CLASS),
		"L", "L"),
#if 0
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
#endif
	SJME_NVM_MLE_DEFINE(objectType,
		SJME_MD(SJME_MD_CLASS, SJME_MD_OBJECT),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(runtimeName,
		SJME_MD(SJME_MD_STRING, SJME_MD_CLASS),
		"L", "L"),
#if 0
	SJME_NVM_MLE_DEFINE(superClass,
		SJME_MD(,),
		""),
#endif
	SJME_NVM_MLE_DEFINE(typeOfBoolean,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfByte,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfCharacter,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfFloat,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfDouble,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfInteger,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfLong,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeOfShort,
		SJME_MD(SJME_MD_CLASS,),
		"L", ),
	SJME_NVM_MLE_DEFINE(typeToClass,
		SJME_MD(SJME_MD_CLASS, SJME_MD_CLASS),
		"L", "L"),
	
	SJME_NVM_MLE_STOP()
};
