/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

SJME_NVM_MLE_FUNCTION_DECL(classPath)
{
	sjme_errorCode error;
	sjme_list(sjme_nvm_rom_library)* libraries;
	sjme_jint i, n;
	sjme_jarray result;

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
		if (sjme_error_is(error = sjme_nvm_task_bracketJarPackage(
			SJME_F_T(inFrame), libraries->elements[i],
			SJME_AS_B_JARPACKAGEP(&result->e.l[i]))))
			return sjme_error_vmError(inFrame, error);
	
	/* Success! */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(result);
	return SJME_ERROR_NONE;
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
	if (jarPackage == NULL || rcName == NULL ||
		!sjme_nvm_isAR(jarPackage,
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
		SJME_F_T(inFrame), SJME_NVM_STRUCT_BRACKET_PIPE_INSTANCE,
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

SJME_NVM_MLE_SHELF_DECLARE(JarPackageShelf) =
{
	SJME_NVM_MLE_DEFINE(classPath,
		SJME_MD(SJME_MD_A(SJME_MD_JAR_PACKAGE), ),
		"L", ),
	SJME_NVM_MLE_DEFINE(openResourcePipe,
		SJME_MD(SJME_MD_PIPE, SJME_MD_JAR_PACKAGE SJME_MD_STRING),
		"L", "LL"),
	SJME_NVM_MLE_STOP()
};
