/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdlib.h>

/* //// MLE /// */
#define mleGroupId RuntimeShelf
#define mleShelfClass "cc/squirreljme/jvm/mle/RuntimeShelf"
#define mleProxyTarget "cc/squirreljme/emulator/EmulatedRuntimeShelf"
#include "squirreljmeMle.h"
/* //////////// */

#include "squirreljme.h"
#include "sjme/path.h"

#define MLE_DESC_browseLocal DESC_METHOD(DESC_VOID, \
	DESC_BOOLEAN DESC_STRING)
MLE_FUNC_PROXY_STATIC(void, browseLocal)

#define MLE_DESC_compatibilityId DESC_METHOD(DESC_BOOLEAN, \
	DESC_INT)
MLE_FUNC_PROTO(jboolean, compatibilityId, jint id)
{
	/* For now everything returns false. */
	return JNI_FALSE;
}

#define MLE_DESC_garbageCollect DESC_METHOD(DESC_VOID, )
MLE_FUNC_PROTO(void, garbageCollect)
{
	// Does nothing
}

#define MLE_DESC_lineEnding DESC_METHOD(DESC_INTEGER, )
MLE_FUNC_PROTO(jint, lineEnding)
{
#if defined(_WIN32)
	return 3;
#else
	return 1;
#endif
}

#define MLE_DESC_vmDescription DESC_METHOD(DESC_STRING, \
	DESC_INT)
MLE_FUNC_PROTO(jstring, vmDescription, jint id)
{
	sjme_errorCode error;
	char fileName[SJME_MAX_PATH];
	sjme_path fullPath;

	// Executable path of the VM binary (EXECUTABLE_PATH)
	if (id == SJME_NVM_VM_DESC_EXECUTABLE_PATH)
	{
		// Use NAL API
		memset(fileName, 0, sizeof(fileName));
		if (sjme_nal_default.execPath != NULL)
			if (sjme_error_is(error = sjme_nal_default.execPath(fileName,
				SJME_MAX_PATH)))
			{
				// Invalidate the path, do not return any string
				memset(fileName, 0, sizeof(fileName));

				// Setup exception to be thrown
				sjme_jni_throwMLECallError(env, error);
			}

		// Convert to Java String if Valid
		if (fileName[0] != 0)
		{
			fileName[SJME_MAX_PATH - 1] = 0;
			return (*env)->NewStringUTF(env, fileName);
		}

		// Not a valid executable path
		return NULL;
	}

	/* Default directory. */
	if (id > SJME_NVM_VM_DESC_DEFAULT_DIR_UNKNOWN &&
		id < (SJME_NVM_VM_DESC_DEFAULT_DIR_UNKNOWN +
			SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPES))
	{
		/* Lookup default path through the path system. */
		memset(&fullPath, 0, sizeof(fullPath));
		if (sjme_error_default(error = sjme_path_default(NULL,
			&fullPath, id - SJME_NVM_VM_DESC_DEFAULT_DIR_UNKNOWN,
			-1)))
		{
			sjme_jni_throwMLECallError(env, error);
			return NULL;
		}

		// Convert to Java String if Valid
		if (fullPath.length > 0)
		{
			fullPath.chars[SJME_MAX_PATH - 1] = '\0';
			return (*env)->NewStringUTF(env, fullPath.chars);
		}

		// Not a valid path
		return NULL;
	}

	return (jstring)forwardCallStaticObject(env, mleProxyTarget,
		"vmDescription", MLE_DESC_vmDescription,
		id);
}

#define MLE_DESC_vmStatistic DESC_METHOD(DESC_LONG, \
	DESC_INTEGER)
MLE_FUNC_PROXY_STATIC(jlong, vmStatistic)

#define MLE_DESC_memoryProfile DESC_METHOD(DESC_INTEGER, )
MLE_FUNC_PROTO(jint, memoryProfile)
{
	// The value is normal
	return 0;
}

#define MLE_DESC_phoneModel DESC_METHOD(DESC_INTEGER, )
MLE_FUNC_PROTO(jint, phoneModel)
{
	// Just be a generic device here
	return 0;
}

#define MLE_DESC_systemEnv DESC_METHOD(DESC_STRING, \
	DESC_STRING)
MLE_FUNC_PROXY_STATIC(jobject, systemEnv)

#define MLE_DESC_vmType DESC_METHOD(DESC_INT, )
MLE_FUNC_PROTO(jint, vmType)
{
	// The value 1 is Java SE type
	return 1;
}

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(browseLocal),
	MLE_LIST_ITEM(compatibilityId),
	MLE_LIST_ITEM(garbageCollect),
	MLE_LIST_ITEM(lineEnding),
	MLE_LIST_ITEM(memoryProfile),
	MLE_LIST_ITEM(phoneModel),
	MLE_LIST_ITEM(systemEnv),
	MLE_LIST_ITEM(vmDescription),
	MLE_LIST_ITEM(vmStatistic),
	MLE_LIST_ITEM(vmType),
MLE_LIST_END()
