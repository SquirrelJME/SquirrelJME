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

sjme_jboolean SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _compareSet)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName expected,
	sjme_atomic_valueName set)
{
	sjme_jboolean result;
	
	sjme_atomic_interruptsDisable();
	
	result = (atomic->value == expected);
	if (result)
		atomic->value = set;
	
	sjme_atomic_interruptsEnable();
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add)
{
	sjme_atomic_valueName result;
	
	sjme_atomic_interruptsDisable();
	
	result = atomic->value;
	atomic->value = (sjme_atomic_valueName)
		(((intptr_t)result) + (intptr_t)add);
	
	sjme_atomic_interruptsEnable();
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic)
{
	sjme_atomic_valueName result;
	
	sjme_atomic_interruptsDisable();
	
	result = atomic->value;
	
	sjme_atomic_interruptsEnable();
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_type, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value)
{
	sjme_atomic_valueName result;
	
	sjme_atomic_interruptsDisable();
	
	result = atomic->value;
	atomic->value = value;
	
	sjme_atomic_interruptsEnable();
	
	return result;
}
