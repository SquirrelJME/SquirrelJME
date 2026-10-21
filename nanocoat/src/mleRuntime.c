/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/intern/nal.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleConst.h"
#include "sjme/nvm/mleShelves.h"

#pragma region(mleInfo)
	#define SJME_NVM_MLE_SHELF RuntimeShelf
#pragma endregion(mleInfo)

SJME_NVM_MLE_FUNCTION_DECL(byteOrder)
{
#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
	/* Little endian. */
	argR->v.i = 1;
#else
	/* Big endian. */
	argR->v.i = 0;
#endif
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(currentTimeMillis)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(encoding)
{
	sjme_nvm inState;
	
	/* Obtain from the NAL */
	inState = SJME_F_S(inFrame);
	if (inState != NULL && inState->nal != NULL &&
		inState->nal->encoding != NULL)
		argR->v.i = SJME_F_S(inFrame)->nal->encoding();
	else
		argR->v.i = SJME_NAL_ENCODING_UNSPECIFIED;
	
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(exit)
{
	sjme_nvm_task inTask;

	/* Set the exit code to use. */
	inTask = SJME_F_K(inFrame);
	sjme_atomic_s(sjme_jint, &inTask->exitCode, argV[0].v.i);

	/* If this is the main task, also set the exit code there. */
	if (inTask->isMain)
		sjme_atomic_s(sjme_jint, &SJME_F_S(inFrame)->mainExitCode,
			argV[0].v.i);
	
	/* Have this task start termination. */
	sjme_atomic_cs(sjme_jint, &inTask->terminate,
		SJME_NVM_TERMINATE_NOT,
		SJME_NVM_TERMINATE_CLEANUP);

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(garbageCollect)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(lineEnding)
{
	const sjme_nal* nal;

	nal = SJME_F_S(inFrame)->nal;

	/* Use the system defined line ending. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	if (nal != NULL && nal->lineEnding != NULL)
		argR->v.i = nal->lineEnding();
	else
		argR->v.i = sjme_nal_default_lineEnding();

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(locale)
{
	sjme_nvm inState;
	
	/* Obtain from the NAL */
	inState = SJME_F_S(inFrame);
	if (inState != NULL && inState->nal != NULL &&
		inState->nal->locale != NULL)
		argR->v.i = SJME_F_S(inFrame)->nal->locale();
	else
		argR->v.i = SJME_NAL_LOCALE_UNSPECIFIED;
	
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(memoryProfile)
{
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
#if defined(SJME_CONFIG_HAS_LOW_MEMORY)
	argR->v.i = SJME_NVM_MLE_MEMORY_PROFILE_MINIMAL;
#else
	argR->v.i = SJME_NVM_MLE_MEMORY_PROFILE_NORMAL;
#endif
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(nanoTime)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(phoneModel)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(systemEnv)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(systemProperty)
{
	sjme_errorCode error;
	sjme_jstring key, loaded;
	sjme_jint ik, iv, n;
	const sjme_list(sjme_lpcstr)* sysProps;
	sjme_charSeq keySeq;
	sjme_lpcstr keyString, k, v;

	/* Read in key value, must be a valid string! */
	key = (sjme_jstring)argV[0].v.l;
	if (!sjme_nvm_isAR(key, SJME_NVM_STRUCT_STRING_INSTANCE))
		return SJME_ERROR_MLE_CALL;
	
	/* Has the sequence ever been initialized? */
	keySeq = sjme_atomic_g(sjme_charSeq, &key->seq);
	if (keySeq == NULL)
		return SJME_ERROR_MLE_CALL;

	/* Determine the key to use. */
	keyString = sjme_charSeq_tempUtf(keySeq);

	/* Look within the init config, if it is valid. */
	sysProps = (SJME_F_K(inFrame)->initConfig != NULL &&
		SJME_F_K(inFrame)->initConfig->sysProps != NULL ?
		SJME_F_K(inFrame)->initConfig->sysProps : NULL);
	if (sysProps != NULL)
		for (ik = 0, iv = ik + 1, n = sysProps->length; iv < n; ik++, iv++)
		{
			/* Load in key and value. */
			k = sysProps->elements[ik];
			v = sysProps->elements[iv];

			/* Skip if missing. */
			if (k == NULL || v == NULL)
				continue;

			/* Is this a match? */
			if (strcasecmp(k, v) == 0)
			{
				/* Load in string value. */
				loaded = NULL;
				if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
					SJME_F_T(inFrame), &loaded, SJME_JNI_TRUE, v)) ||
					loaded == NULL)
					return sjme_error_mask(error, SJME_ERROR_MLE_CALL);

				/* Use the loaded string value. */
				argR->t = SJME_JAVA_TYPE_ID_OBJECT;
				argR->v.l = SJME_AS_JOBJECT(loaded);
				return SJME_ERROR_NONE;
			}
		}

	/* Not found. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = NULL;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(vmDescription)
{
	sjme_errorCode error;
	sjme_cchar buf[SJME_MAX_PATH];
	const sjme_path_style* pathStyle;
	sjme_lpcstr str;
	const sjme_nal* nal;
	sjme_jint key;
	sjme_path path;
	sjme_jstring result;

	/* Recover the NAL. */
	nal = SJME_F_S(inFrame)->nal;
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* There can be multiple possibilities! */
	memset(buf, 0, sizeof(buf));
	memset(&path, 0, sizeof(path));
	str = NULL;
	pathStyle = NULL;

	/* Which key is being requested? */
	key = argV[0].v.i;
	switch (argV[0].v.i)
	{
		case SJME_NVM_VM_DESC_VM_VERSION:
		case SJME_NVM_VM_DESC_SQUIRRELJME_API_VERSION:
			str = SQUIRRELJME_VERSION;
			break;

		case SJME_NVM_VM_DESC_VM_NAME:
			str = "SquirrelJME NanoCoat VM";
			break;

		case SJME_NVM_VM_DESC_VM_VENDOR:
			str = "Stephanie Gawroriski";
			break;

		case SJME_NVM_VM_DESC_VM_EMAIL:
			str = "xer@multiphasicapps.net";
			break;

		case SJME_NVM_VM_DESC_VM_URL:
			str = "https://squirreljme.cc/";
			break;

		case SJME_NVM_VM_DESC_VM_INFO:
			if (SJME_F_K(inFrame)->globals.noOptimize)
				str = "native pure interpreter";
			else
				str = "native interpreter w/ optimizations";
			break;

		case SJME_NVM_VM_DESC_OS_NAME:
			str = SQUIRRELJME_SYSTEM;
			break;

		case SJME_NVM_VM_DESC_OS_ARCH:
			str = SQUIRRELJME_ARCH;
			break;

		case SJME_NVM_VM_DESC_OS_VERSION:
			if (nal->osVersion != NULL)
				nal->osVersion(buf, SJME_MAX_PATH - 1);
			else
				str = "Unknown";
			break;

		case SJME_NVM_VM_DESC_EXECUTABLE_PATH:
			/* Ignore failure. */
			if (nal->execPath != NULL)
				nal->execPath(buf, SJME_MAX_PATH - 1);
			break;

		case SJME_NVM_VM_DESC_PATH_SEPARATOR:
			pathStyle = NULL;
			if (nal->pathStyle != NULL)
			{
				nal->pathStyle(&pathStyle);
				if (pathStyle != NULL)
					str = pathStyle->pathSep.str;
			}
			break;

		case SJME_NVM_VM_DESC_VM_SECURITY_POLICY:
			str = "";
			break;

#if defined(SJME_THIRD_PARTY_LEGAL_LINE)
		case SJME_NVM_VM_DESC_THIRD_PARTY_LEGAL_LINE:
			str = SJME_THIRD_PARTY_LEGAL_LINE;
			break;
#endif

#if defined(SJME_THIRD_PARTY_LEGAL_DOCUMENT)
		case SJME_NVM_VM_DESC_THIRD_PARTY_LEGAL_DOCUMENT:
			str = SJME_THIRD_PARTY_LEGAL_DOCUMENT;
			break;
#endif

		default:
			/* A directory reference? */
			if (key >= SJME_NVM_VM_DESC_DEFAULT_DIR_UNKNOWN &&
				key <= SJME_NVM_VM_DESC_DEFAULT_DIR_NUM_RESERVED)
			{
				if (!sjme_error_is(sjme_path_default(nal, &path,
					key - SJME_NVM_VM_DESC_DEFAULT_DIR_UNKNOWN, -1)))
					strncat(buf, path.chars, SJME_MAX_PATH - 1);
			}
			break;
	}

	/* Has a valid path? */
	if (path.chars[0] != '\0')
		str = &path.chars[0];

	/* Has a valid buffer? */
	else if (buf[0] != '\0')
		str = &buf[0];

	/* Nothing to return? */
	if (str == NULL)
	{
		argR->t = SJME_JAVA_TYPE_ID_OBJECT;
		argR->v.l = NULL;
		return SJME_ERROR_NONE;
	}

	/* Make a string from it. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
		SJME_F_T(inFrame), &result, SJME_JNI_TRUE, str)) ||
		result == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Return the string. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = SJME_AS_JOBJECT(result);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(vmStatistic)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmType)
{
	/* Always returns this constant value of NanoCoat. */
	/* Unless pure interpreter is forced. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	if (SJME_F_K(inFrame)->initConfig != NULL &&
		SJME_F_K(inFrame)->initConfig->noOptimize)
		argR->v.i = SJME_NVM_MLE_VM_TYPE_SPRINGCOAT;
	else
		argR->v.i = SJME_NVM_MLE_VM_TYPE_NANOCOAT;

	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(RuntimeShelf) =
{
	SJME_NVM_MLE_DEFINE(byteOrder,
		SJME_MD(SJME_MD_I, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_I, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(currentTimeMillis,
		SJME_MD(SJME_MD_J, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_J, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(encoding,
		SJME_MD(SJME_MD_I, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_I, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(exit,
		SJME_MD(SJME_MD_V, SJME_MD_I),
		SJME_MP(SJME_MP_V, SJME_MP_I)),
	SJME_NVM_MLE_DEFINE(garbageCollect,
		SJME_MD(SJME_MD_V, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_V, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(lineEnding,
		SJME_MD(SJME_MD_I, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_I, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(locale,
		SJME_MD(SJME_MD_I, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_I, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(memoryProfile,
		SJME_MD(SJME_MD_I, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_I, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(nanoTime,
		SJME_MD(SJME_MD_J, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_J, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(phoneModel,
		SJME_MD(SJME_MD_J, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_J, SJME_MDMP___NO_ARGS__)),
	SJME_NVM_MLE_DEFINE(systemEnv,
		SJME_MD(SJME_MD_STRING, SJME_MD_STRING),
		SJME_MP(SJME_MP_L, SJME_MP_L)),
	SJME_NVM_MLE_DEFINE(systemProperty,
		SJME_MD(SJME_MD_STRING, SJME_MD_STRING),
		SJME_MP(SJME_MP_L, SJME_MP_L)),
	SJME_NVM_MLE_DEFINE(vmDescription,
		SJME_MD(SJME_MD_STRING, SJME_MD_I),
		SJME_MP(SJME_MP_L, SJME_MP_I)),
	SJME_NVM_MLE_DEFINE(vmStatistic,
		SJME_MD(SJME_MD_J, SJME_MD_I),
		SJME_MP(SJME_MP_J, SJME_MP_I)),
	SJME_NVM_MLE_DEFINE(vmType,
		SJME_MD(SJME_MD_I, SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_I, SJME_MDMP___NO_ARGS__)),
	
	SJME_NVM_MLE_STOP(),
};

