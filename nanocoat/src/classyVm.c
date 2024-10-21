/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/classyVm.h"
#include "sjme/nvm/cleanup.h"

sjme_errorCode sjme_nvm_vmClass_loaderLoad(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr className)
{
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		className == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadArray(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInPositiveNonZero sjme_jint dims)
{
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (dims <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
	
sjme_errorCode sjme_nvm_vmClass_loaderLoadArrayA(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_lpcstr componentType,
	sjme_attrInPositiveNonZero sjme_jint dims)
{
	if (inLoader == NULL || outClass == NULL || contextThread == NULL ||
		componentType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (dims <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderLoadPrimitive(
	sjme_attrInNotNull sjme_nvm_vmClass_loader inLoader,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NUM_BASIC_TYPE_IDS) sjme_basicTypeId basicType)
{
	if (inLoader == NULL || outClass == NULL || contextThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (basicType < 0 || basicType >= SJME_NUM_BASIC_TYPE_IDS ||
		basicType == SJME_JAVA_TYPE_ID_SHORT_OR_CHAR ||
		basicType == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE ||
		basicType == SJME_JAVA_TYPE_ID_OBJECT)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_vmClass_loaderNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_vmClass_loader* outLoader,
	sjme_attrInNotNull sjme_list_sjme_nvm_rom_library* classPath)
{
	sjme_errorCode error;
	sjme_nvm_vmClass_loader result;
	sjme_list_sjme_nvm_rom_library* dup;
	sjme_nvm_rom_library lib;
	sjme_jint i, n, cldcCompact;
	
	if (inState == NULL || outLoader == NULL || classPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Duplicate list. */
	dup = NULL;
	if (sjme_error_is(error = sjme_list_copy(inState->reservedPool,
		classPath->length, classPath, &dup, sjme_nvm_rom_library, 0)))
		goto fail_dupList;
	
	/* There cannot be any NULLs, but also find cldc-compact.jar. */
	cldcCompact = -1;
	for (i = 0, n = dup->length; i < n; i++)
	{
		lib = dup->elements[i];
		if (lib == NULL)
			goto fail_nullJar;
		
		/* Is this cldc-compact? */
		if (cldcCompact < 0 && strcmp("cldc-compact.jar", lib->name) == 0)
			cldcCompact = i;
	}
	
	/* There needs to be this. */
	if (cldcCompact < 0)
		goto fail_noCldcCompact;
	
	/* cldc-compact.jar must always be first, so swap it! */
	if (cldcCompact != 0)
	{
		lib = dup->elements[0];
		dup->elements[0] = dup->elements[cldcCompact];
		dup->elements[cldcCompact] = lib;
	}
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState->reservedPool,
		sizeof(*result), SJME_NVM_STRUCT_VM_CLASS_LOADER,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_alloc; 
	
	/* Setup fields. */
	result->classPath = dup;
	
	/* Success! */
	*outLoader = result;
	return SJME_ERROR_NONE;
	
fail_alloc:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
fail_noCldcCompact:
fail_nullJar:
fail_dupList:
	if (dup != NULL)
		sjme_alloc_free(dup);
	
	return sjme_error_default(error);
}
