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

#if sjme_atomic_typeWidth == 8
	/** The underlying atomic type. */
	#define SJME_ATOMIC_WIN32_TYPE LONG64
#elif sjme_atomic_typeWidth == 4
	/** The underlying atomic type. */
	#define SJME_ATOMIC_WIN32_TYPE LONG
#else
	#error Unsupported type width
#endif

sjme_jboolean SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _compareSet)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName expected,
	sjme_atomic_valueName set)
{
	sjme_atomic_valueName was;
	
#if sjme_atomic_typeWidth == 8
	was = (sjme_atomic_valueName)InterlockedCompareExchange64(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value,
		(SJME_ATOMIC_WIN32_TYPE)set,
		(SJME_ATOMIC_WIN32_TYPE)expected);
#elif sjme_atomic_typeWidth == 4
	was = (sjme_atomic_valueName)InterlockedCompareExchange(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value,
		(SJME_ATOMIC_WIN32_TYPE)set,
		(SJME_ATOMIC_WIN32_TYPE)expected);
#else
	#error Unsupported type width
#endif
	
	if (was == expected)
		return SJME_JNI_TRUE;
	return SJME_JNI_FALSE;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add)
{
#if sjme_atomic_typeWidth == 8
	return (sjme_atomic_valueName)InterlockedExchangeAdd64(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value, 
		(SJME_ATOMIC_WIN32_TYPE)add);
#elif sjme_atomic_typeWidth == 4
	return (sjme_atomic_valueName)InterlockedExchangeAdd(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value, 
		(SJME_ATOMIC_WIN32_TYPE)add);
#else
	#error Unsupported type width
#endif
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic)
{
#if sjme_atomic_typeWidth == 8
	return (sjme_atomic_valueName)InterlockedExchangeAdd64(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value, 
		(SJME_ATOMIC_WIN32_TYPE)0);
#elif sjme_atomic_typeWidth == 4
	return (sjme_atomic_valueName)InterlockedExchangeAdd(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value, 
		(SJME_ATOMIC_WIN32_TYPE)0);
#else
	#error Unsupported type width
#endif
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value)
{
#if sjme_atomic_typeWidth == 8
	return (sjme_atomic_valueName)InterlockedExchange64(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value,
		(SJME_ATOMIC_WIN32_TYPE)value);
#elif sjme_atomic_typeWidth == 4
	return (sjme_atomic_valueName)InterlockedExchange(
		(volatile SJME_ATOMIC_WIN32_TYPE*)&atomic->value,
		(SJME_ATOMIC_WIN32_TYPE)value);
#else
	#error Unsupported type width
#endif
}

#undef SJME_ATOMIC_WIN32_TYPE
