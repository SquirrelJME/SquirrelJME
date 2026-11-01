/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/boot.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instanceInspect.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"
#include "sjme/nvm/romMeepSwm.h"

#pragma region(mleInfo)
	#define SJME_NVM_MLE_SHELF JarPackageShelf
#pragma endregion(mleInfo)

SJME_NVM_MLE_FUNCTION_DECL(classPath)
{
	sjme_errorCode error;
	sjme_list(sjme_nvm_rom_library)* libraries;
	sjme_jint i, n;
	sjme_jarray result;
	sjme_jbracketJarPackage bracket;

	/* Something is very wrong! */
	if (SJME_T_K(inFrame)->classLoader == NULL ||
		SJME_T_K(inFrame)->classLoader->classPath == NULL)
		return sjme_error_fatal(SJME_ERROR_NO_SUITES);

	/* Load in the classpath. */
	libraries = SJME_T_K(inFrame)->classLoader->classPath;
	n = libraries->length;

	/* Allocate resultant array. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), &result,
		sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
			SJME_NVM_COMMON_JAR_PACKAGE), n)) || result == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Libraries need to be converted to brackets. */
	for (i = 0; i < n; i++)
	{
		/* Setup bracket. */
		bracket = NULL;
		if (sjme_error_is(error = sjme_nvm_task_bracketJarPackage(
			SJME_F_T(inFrame), libraries->elements[i],
			&bracket)) || bracket == NULL)
			return sjme_error_vmError(inFrame, error);
		
		/* Set into the array. */
		if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
			&result->e, i, NULL, SJME_VLS_JOBJECT(bracket))))
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(result);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(equals)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(openResourcePipe)
{
	sjme_errorCode error;
	sjme_jbracketJarPackage jarPackage;
	sjme_jstring rcName;
	sjme_charSeq seq;
	sjme_nvm_rom_library library;
	sjme_jboolean exists;
	sjme_stream_input stream;
	sjme_jbracketPipe result;

	/* Grab arguments. */
	jarPackage = (sjme_jbracketJarPackage)argV[0].v.l;
	rcName = (sjme_jstring)argV[1].v.l;

	/* Check. */
	if (!sjme_nvm_isAR(jarPackage,
			SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE) ||
		!sjme_nvm_isAR(rcName, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* The library must be set. */
	library = jarPackage->library;
	if (library == NULL)
		return SJME_ERROR_MLE_CALL;

	/* The sequence must be valid. */
	seq = sjme_atomic_g(sjme_charSeq, &rcName->seq);
	if (seq == NULL)
		return SJME_ERROR_MLE_CALL;

	/* Check if the resource exists first. */
	exists = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceExists(library,
		&exists, sjme_charSeq_tempUtf(seq))))
		return sjme_error_vmError(inFrame, error);

	/* Completely missing? Cannot open it. */
	if (!exists)
	{
		argR->t = SJME_JAVA_TYPE_ID_OBJECT;
		argR->v.l = NULL;
		return SJME_ERROR_NONE;
	}

	/* Open stream to the resource. */
	stream = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceAsStream(library,
		&stream, sjme_charSeq_tempUtf(seq))))
		return sjme_error_vmError(inFrame, error);

	/* Allocate pipe bracket. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNewBracket(
		SJME_F_T(inFrame),
		SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE, -1,
		SJME_AS_JOBJECTP(&result))) || result == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Fill in! */
	result->isOutput = SJME_JNI_FALSE;
	result->stream.in = stream;

	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(result);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(prefixCode)
{
	sjme_errorCode error;
	sjme_jbracketJarPackage jarPackage;
	sjme_nvm_rom_library library;
	sjme_jint prefixCode, i, n;
	sjme_nvm_rom_swmManager swmManager;
	sjme_nvm_rom_swmLibrary* info;
	sjme_nvm_rom_swmEntry* entry;
	
	/* Check. */
	jarPackage = (sjme_jbracketJarPackage)argV[0].v.l;
	if (!sjme_nvm_isAR(jarPackage,
		SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* The library must be set. */
	library = jarPackage->library;
	if (library == NULL)
		return SJME_ERROR_MLE_CALL;
	
	/* Default to no prefix code. */
	prefixCode = -1;

	/* Load the SWM manager. */
	swmManager = NULL;
	if (sjme_error_is(error = sjme_nvm_swmManager(SJME_F_S(inFrame),
		&swmManager)) || swmManager == NULL)
		return sjme_error_default(error);

	/* Lookup the library. */
	info = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_swmLookup(
		swmManager, library, &info)) || info == NULL)
	{
		/* The library could be virtual or something else. */
		if (error != SJME_ERROR_LIBRARY_NOT_FOUND)
			return sjme_error_default(error);
	}
	
	/* Find the prefix code information. */
	if (info != NULL && info->entries != NULL)
		for (i = 0, n = info->entries->length; i < n; i++)
		{
			entry = &info->entries->elements[i];
			if (entry->tag.use == SJME_NVM_SWM_USE_INFORMATIONAL &&
				entry->tag.type == SJME_NVM_SWM_DEPENDENCY_PREFIX_CODE)
			{
				prefixCode = entry->prefixCode.prefixCode;
				break;
			}
		}
	
	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = prefixCode;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(rawSize)
{
	sjme_errorCode error;
	sjme_jbracketJarPackage jarPackage;
	sjme_nvm_rom_library library;
	sjme_jint rawSize;
	
	/* Check. */
	jarPackage = (sjme_jbracketJarPackage)argV[0].v.l;
	if (!sjme_nvm_isAR(jarPackage,
		SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* The library must be set. */
	library = jarPackage->library;
	if (library == NULL)
		return SJME_ERROR_MLE_CALL;
	
	/* Determine the raw size. */
	rawSize = -1;
	if (sjme_error_is(error = sjme_nvm_rom_libraryRawSize(
		library, &rawSize)) || rawSize <= 0)
	{
		/* Some other error. */
		if (error != SJME_ERROR_UNSUPPORTED_OPERATION)
			return sjme_error_default(error);
		
		/* Make this invalid. */
		rawSize = -1;
	}
	
	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = rawSize;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(JarPackageShelf) =
{
	SJME_NVM_MLE_DEFINE(classPath,
		SJME_MD(SJME_MD_A(SJME_MD_JAR_PACKAGE), SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_L, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(equals,
		SJME_MD(SJME_MD_Z, SJME_MD_JAR_PACKAGE SJME_MD_JAR_PACKAGE),
		SJME_MP(SJME_MP_I, SJME_MP_I SJME_MP_I)),
#if 0
	SJME_NVM_MLE_DEFINE(libraries,
		SJME_MD(),
		SJME_MP()),
	SJME_NVM_MLE_DEFINE(libraryId,
		SJME_MD(),
		SJME_MP()),
	SJME_NVM_MLE_DEFINE(libraryPath,
		SJME_MD(),
		SJME_MP()),
	SJME_NVM_MLE_DEFINE(list,
		SJME_MD(),
		SJME_MP()),
#endif
	SJME_NVM_MLE_DEFINE(openResourcePipe,
		SJME_MD(SJME_MD_PIPE, SJME_MD_JAR_PACKAGE SJME_MD_STRING),
		SJME_MP(SJME_MP_L, SJME_MP_L SJME_MP_L)),
	SJME_NVM_MLE_DEFINE(prefixCode,
		SJME_MD(SJME_MD_I, SJME_MD_JAR_PACKAGE),
		SJME_MP(SJME_MP_I, SJME_MP_L)),
#if 0
	SJME_NVM_MLE_DEFINE(rawData,
		SJME_MD(),
		SJME_MP()),
#endif
	SJME_NVM_MLE_DEFINE(rawSize,
		SJME_MD(SJME_MD_I, SJME_MD_JAR_PACKAGE),
		SJME_MP(SJME_MP_I, SJME_MP_L)),
	
	SJME_NVM_MLE_STOP()
};
