/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Software Long Math.
 * 
 * @since 2026/10/30
 */

#ifndef SJME_C_SQUIRRELJME_SOFTLONG_H
#define SJME_C_SQUIRRELJME_SOFTLONG_H

#include "sjme/config.h"
#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SOFTLONG_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_jlong sjme_softLong_add(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jlong sjme_softLong_and(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jboolean sjme_softLong_cmp(
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jlong sjme_softLong_div(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jlong sjme_softLong_mul(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jlong sjme_softLong_neg(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a);
	
sjme_jlong sjme_softLong_or(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jlong sjme_softLong_rem(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
sjme_jlong sjme_softLong_shl(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull sjme_jint s);
	
sjme_jlong sjme_softLong_shr(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull sjme_jint s);
	
sjme_jlong sjme_softLong_sub(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
void sjme_softLong_toDouble(
	sjme_attrOutNotNull sjme_jdouble* r,
	sjme_attrInNotNull const sjme_jlong* a);
	
void sjme_softLong_toFloat(
	sjme_attrOutNotNull sjme_jfloat* r,
	sjme_attrInNotNull const sjme_jlong* a);
	
sjme_jint sjme_softLong_toInteger(
	sjme_attrOutNotNull sjme_jint* r,
	sjme_attrInNotNull const sjme_jlong* a);
	
sjme_jlong sjme_softLong_ushr(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull sjme_jint s);
	
sjme_jlong sjme_softLong_xor(
	sjme_attrOutNotNull sjme_jlong* r,
	sjme_attrInNotNull const sjme_jlong* a,
	sjme_attrInNotNull const sjme_jlong* b);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SOFTLONG_H
}
#undef SJME_CXX_SQUIRRELJME_SOFTLONG_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SOFTLONG_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SOFTLONG_H */
