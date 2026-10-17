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
#if sjme_atomic_typeWidth == 8
	if (OSAtomicCompareAndSwap64Barrier(
		(sjme_jlongNative)expected,
		(sjme_jlongNative)set,
		(sjme_jlongNative*)&atomic->value))
		return SJME_JNI_TRUE;
#elif sjme_atomic_typeWidth == 4
	if (OSAtomicCompareAndSwap32Barrier(
		(sjme_jint)expected,
		(sjme_jint)set,
		(sjme_jint*)&atomic->value))
		return SJME_JNI_TRUE;
#else
	#error Unsupported type width
#endif
	return SJME_JNI_FALSE;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _getAdd)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName add)
{
#if sjme_atomic_typeWidth == 8
	return (sjme_atomic_valueName)\
		(OSAtomicAdd64Barrier((sjme_jlongNative)add,
		(sjme_jlongNative*)&atomic->value) - (sjme_jlongNative)add);
#elif sjme_atomic_typeWidth == 4
	/* Returns the new value, so it must be reversed! */\
	return (sjme_atomic_valueName)\
		(OSAtomicAdd32Barrier((sjme_jint)add,
		(sjme_jint*)&atomic->value) - (sjme_jint)add);
#else
	#error Unsupported type width 
#endif
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _get)(
	sjme_atomic_typeName* atomic)
{
	sjme_atomic_valueName result;
	
#if sjme_atomic_typeWidth == 8
	result = (sjme_atomic_valueName)OSAtomicXor64((sjme_jlongNative)0,
		(sjme_jlongNative*)&atomic->value);
#elif sjme_atomic_typeWidth == 4
	result = (sjme_atomic_valueName)OSAtomicXor32((sjme_jint)0,
		(sjme_jint*)&atomic->value);
#else
	#error Unsupported type width 
#endif
	
	return result;
}

sjme_atomic_valueName SJME_TOKEN_PASTE3_PP(
		sjme_atomic_, sjme_atomic_subName, _set)(
	sjme_atomic_typeName* atomic,
	sjme_atomic_valueName value)
{
	/* There is no simple set, so sadly this is two */
	/* atomic operations. */
	sjme_atomic_storageType temp;
	OSMemoryBarrier();
	
#if sjme_atomic_typeWidth == 8
	temp = (sjme_atomic_storageType)OSAtomicXor64((sjme_jlongNative)value,
		(sjme_jlongNative*)&atomic->value);
	OSAtomicXor64Barrier(
		(((sjme_jlongNative)temp) ^ ((sjme_jlongNative)value)),
		(sjme_jlongNative*)&atomic->value);
#elif sjme_atomic_typeWidth == 4
	temp = (sjme_atomic_storageType)OSAtomicXor32((sjme_jint)value,
		(sjme_jint*)&atomic->value);
	OSAtomicXor32Barrier((((sjme_jint)temp) ^ ((sjme_jint)value)),
		(sjme_jint*)&atomic->value);
#else
	#error Unsupported type width 
#endif
	
	OSMemoryBarrier();
	
	return (sjme_atomic_valueName)(((sjme_atomic_storageType)temp) ^ 
		((sjme_atomic_storageType)value));
}

