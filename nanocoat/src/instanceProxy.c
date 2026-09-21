/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instanceProxy.h"
#include "sjme/nvm/task.h"

sjme_errorCode sjme_nvm_instance_proxyClassA(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInPositiveNonZero sjme_jint numInterfaces,
	sjme_attrInNotNull sjme_jclass* inInterfaces)
{
#define VIRTUAL_NAME_LEN 64
	sjme_jint i, identityHash, basicIndex, xorHash;
	sjme_errorCode error;
	sjme_jclass* set;
	sjme_jclass checkClass;
	sjme_cchar virtualName[VIRTUAL_NAME_LEN];
	sjme_cchar letter;

	if (contextThread == NULL || outClass == NULL || handler == NULL ||
		inInterfaces == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numInterfaces <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate for a defensive copy. */
	set = sjme_alloca(sizeof(*set) * (numInterfaces + 1));
	if (set == NULL)
	{
		error = sjme_error_outOfMemory(NULL, numInterfaces);
		goto fail_alloca;
	}

	/* Clear and copy everything over. */
	memset(set, 0, sizeof(*set) * numInterfaces);
	memmove(set, inInterfaces, sizeof(*set) * numInterfaces);

	/* Every class must be valid and only an interface. */
	xorHash = 0;
	for (i = 0; i < numInterfaces; i++)
	{
		/* Nothing here? */
		checkClass = set[i];
		if (checkClass == NULL)
		{
			error = SJME_ERROR_NULL_ARGUMENTS;
			goto fail_noClass;
		}

		/* The checks here are rather extra pedantic/paranoid here because */
		/* proxies can be very powerful and also very broken if used */
		/* incorrectly. */
		/* Class is not an interface? */
		/* Cannot be an existing proxy, we DO NOT want to next proxies. */
		/* Cannot be final, otherwise we cannot extend it. */
		/* It makes no sense to proxy annotations, although maybe it does */
		/* for annotation accessors but that is another thing especially */
		/* where reflection and defaults are considered. */
		/* Cannot proxy enumerations, obviously. */
		/* Disallow proxying VM synthetic classes as these are very special */
		/* classes which are not intended to be used with proxies. */
		/* We also cannot proxy arrays, also obviously. */
		if (!SJME_NVM_ACC_IS(checkClass->info->flags, INTERFACE) ||
			SJME_NVM_ACC_IS(checkClass->info->flags, SPECIAL_PROXY) ||
			SJME_NVM_ACC_IS(checkClass->info->flags, FINAL) ||
			SJME_NVM_ACC_IS(checkClass->info->flags, ANNOTATION) ||
			SJME_NVM_ACC_IS(checkClass->info->flags, ENUM) ||
			SJME_NVM_ACC_IS(checkClass->info->flags, SPECIAL_VM_SYNTHETIC) ||
			checkClass->info->isArray)
			return SJME_ERROR_INVALID_CLASS_FLAGS;

		/* If the interface is not yet initialized, we need to actually do */
		/* that first. Otherwise, we will have no idea about nested */
		/* interfaces or otherwise... */
		if (!sjme_atomic_g(sjme_jint, &checkClass->isLoaded) ||
			!sjme_atomic_g(sjme_jint, &checkClass->isInitialized))
			if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(checkClass,
				contextThread)))
				goto fail_initClass;

		/* XOR in its identity hash to make some unique-ish ID. */
		xorHash ^= checkClass->object.identityHash;
	}

	/* Calculate an identity hash for the proxy, along with a basic index */
	/* for the proxy. */
	identityHash = sjme_nvm_instance_calcIdentityHash(SJME_T_K(contextThread),
		contextThread);
	basicIndex = xorHash + sjme_atomic_ga(sjme_jint,
		&SJME_T_K(contextThread)->globals.sequencedId, 1);
	letter = 'A' + (numInterfaces % 26);

	/* Everything was successful, so we can actually create a virtual class */
	/* here. Of course, we need to make a name for it. */
	/* An attempt is made to keep proxy classes in their own package so that */
	/* they do not do any package-private magical stuff along with messing */
	/* with things in the default package. */
	memset(virtualName, 0, sizeof(virtualName));
	snprintf(virtualName, VIRTUAL_NAME_LEN - 1,
		"$__sjme_x_$%c$_$%08x%02x/$%08x%02x$__",
		letter,
		basicIndex, identityHash & 0xFF,
		identityHash, basicIndex & 0xFF);

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

fail_initClass:
fail_noClass:
fail_alloca:
	if (set != NULL)
		sjme_alloca_free(set);

	return sjme_error_default(error);
#undef VIRTUAL_NAME_LEN
}

sjme_errorCode sjme_nvm_instance_proxyClassL(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInNotNull sjme_list(sjme_jclass)* inInterfaces)
{
	if (contextThread == NULL || outClass == NULL || handler == NULL ||
		inInterfaces == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_proxyClassV(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInNotNull sjme_jclass inInterfaces,
	sjme_attrInNotNull ...)
{
#define MAX_INTERFACES 16
	va_list args;
	sjme_jclass forward[MAX_INTERFACES];
	sjme_jclass at;
	sjme_jint count;

	if (contextThread == NULL || outClass == NULL || handler == NULL ||
		inInterfaces == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Clear forward. */
	memset(forward, 0, sizeof(forward));
	count = 0;

	/* First is always a given. */
	forward[count++] = inInterfaces;

	/* Start arguments. */
	va_start(args, inInterfaces);

	/* Grab all passed interfaces. */
	for (;;)
	{
		/* Are there more interfaces passed? */
		at = va_arg(args, sjme_jclass);
		if (at == NULL)
			break;

		/* Too many? */
		if (count >= MAX_INTERFACES)
			return SJME_ERROR_TOO_LARGE;

		/* Send forward. */
		forward[count++] = at;
	}

	/* End arguments. */
	va_end(args);

	/* Forward call. */
	return sjme_nvm_instance_proxyClassA(contextThread, outClass,
		handler, count, forward);
#undef MAX_INTERFACES
}