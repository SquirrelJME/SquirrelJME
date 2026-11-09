/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * These are functions which are defined in the JVM exports table, however
 * their functionality, signature, return type, arguments, and otherwise
 * are unknown. These stubs are to allow for linking.
 * 
 * @since 2026/11/09
 */

#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"

void SJME_JNI_LIB_FUNCTION JNICALL JVM_BeforeHalt(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_CopySwapMemory(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE,
	jint unknownF,
	jint unknownG,
	jint unknownH,
	jint unknownI,
	jint unknownJ,
	jint unknownK)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_CX8Field(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE,
	jint unknownF,
	jint unknownG)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_DefineClassWithSourceCond(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE,
	jint unknownF,
	jint unknownG,
	jint unknownH)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_EnqueueOperation(
	jint unknownA,
	jint unknownB,
	jint unknownC,
	jint unknownD,
	jint unknownE)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetClassLoader(
	jint unknownA,
	jint unknownB)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetFieldAnnotations(
	jint unknownA,
	jint unknownB)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodAnnotations(
	jint unknownA,
	jint unknownB)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodDefaultAnnotationValue(
	jint unknownA,
	jint unknownB)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetMethodParameterAnnotations(
	jint unknownA,
	jint unknownB)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GetThreadInterruptEvent(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_IsUseContainerSupport(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}
