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
/* ReSharper disable CppRedundantCastExpression */

/** Memory order used for GCC. */
#define SJME_ATOMIC_GCC_MEMORY_ORDER __ATOMIC_SEQ_CST

sjme_jboolean SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _compareSet)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName expected,
	sjme_atomic_valueName set)
{
	if (__atomic_compare_exchange_n(
			(volatile sjme_atomic_storageType*)&atomic->value,
			(sjme_atomic_storageType*)&expected,
			(sjme_atomic_storageType)set, 
			0, 
			SJME_ATOMIC_GCC_MEMORY_ORDER,
				SJME_ATOMIC_GCC_MEMORY_ORDER))
		return SJME_JNI_TRUE;
	return SJME_JNI_FALSE;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add)
{
	return (sjme_atomic_valueName)__atomic_fetch_add(
		(volatile sjme_atomic_storageType*)&atomic->value,
		(sjme_atomic_storageType)add,
		SJME_ATOMIC_GCC_MEMORY_ORDER);
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic)
{
	return (sjme_atomic_valueName)__atomic_load_n(
		(volatile sjme_atomic_storageType*)&atomic->value,
		SJME_ATOMIC_GCC_MEMORY_ORDER);
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_type, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value)
{
	return (sjme_atomic_valueName)__atomic_exchange_n(
		(volatile sjme_atomic_storageType*)&atomic->value,
		(sjme_atomic_storageType)value,
		SJME_ATOMIC_GCC_MEMORY_ORDER);
}

#undef SJME_ATOMIC_GCC_MEMORY_ORDER
