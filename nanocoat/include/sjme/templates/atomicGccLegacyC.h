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

sjme_jboolean SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _compareSet)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName expected,
	sjme_atomic_valueName set)
{
	sjme_atomic_valueName was;
	
	was = (sjme_atomic_valueName)__sync_val_compare_and_swap(
		(volatile sjme_atomic_storageType*)&atomic->value,
		(sjme_atomic_storageType)expected,
		(sjme_atomic_storageType)set);
	if (was == expected)
		return SJME_JNI_TRUE;
	return SJME_JNI_FALSE;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add)
{
	return (sjme_atomic_valueName)__sync_fetch_and_add(
		(volatile sjme_atomic_storageType*)&atomic->value, 
		(sjme_atomic_storageType)add);
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic)
{
	return (sjme_atomic_valueName)__sync_fetch_and_or(
		(volatile sjme_atomic_storageType*)&atomic->value,
		(sjme_atomic_storageType)0);
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value)
{
	return (sjme_atomic_valueName)__sync_lock_test_and_set(
		(volatile sjme_atomic_storageType*)&atomic->value,
		(sjme_atomic_storageType)value);
}
