/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* ReSharper disable CppMissingIncludeGuard */
/* ReSharper disable CppMissingIncludeGuard */
/* ReSharper disable CppNonInlineFunctionDefinitionInHeaderFile */

#include "atomicLintC.h"

sjme_jboolean SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _compareSet)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName expected,
	sjme_atomic_valueName set)
{
	sjme_jboolean result;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsDisable();
#endif
	
	result = (atomic->value == expected);
	if (result)
		atomic->value = set;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsEnable();
#endif
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add)
{
	sjme_atomic_valueName result;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsDisable();
#endif
	
	result = atomic->value;
	atomic->value = (sjme_atomic_valueName)
		(((intptr_t)result) + (intptr_t)add);
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsEnable();
#endif
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic)
{
	sjme_atomic_valueName result;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsDisable();
#endif
	
	result = atomic->value;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsEnable();
#endif
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getLazy)(
	sjme_atomic_typeName* atomic)
{
	return SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(atomic);
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value)
{
	sjme_atomic_valueName result;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsDisable();
#endif
	
	result = atomic->value;
	atomic->value = value;
	
#if defined(SJME_CONFIG_HAS_OS_BAREMETAL)
	sjme_atomic_interruptsEnable();
#endif
	
	return result;
}
