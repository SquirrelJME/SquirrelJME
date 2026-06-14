/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>

#include "squirreljme.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/MathAccelShelf"
#define FORWARD_CLASS_NAME MathAccel
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/EmulatedMathAccelShelf"

#define FORWARD_DESC_accel \
	DESC_METHOD(DESC_INT, )
#define FORWARD_DESC_acos \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_asin \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_atan \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_atan2 \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE DESC_DOUBLE)
#define FORWARD_DESC_ceil \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_cos \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_exp \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_floor \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_log \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_pow \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE DESC_DOUBLE)
#define FORWARD_DESC_round \
	DESC_METHOD(DESC_LONG, DESC_DOUBLE)
#define FORWARD_DESC_signum \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_sin \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_sqrt \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)
#define FORWARD_DESC_tan \
	DESC_METHOD(DESC_DOUBLE, DESC_DOUBLE)

FORWARD_IMPL(MathAccel, accel,
	jint, Integer,
	FORWARD_IMPL_none(),
	FORWARD_IMPL_none())
FORWARD_IMPL(MathAccel, acos,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, asin,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, atan,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, atan2,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble a, jdouble b),
	FORWARD_IMPL_pass(a, b))
FORWARD_IMPL(MathAccel, ceil,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, cos,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, exp,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, floor,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, log,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, pow,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble a, jdouble b),
	FORWARD_IMPL_pass(a, b))
FORWARD_IMPL(MathAccel, round,
	jlong, Long,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, signum,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, sin,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, sqrt,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))
FORWARD_IMPL(MathAccel, tan,
	jdouble, Double,
	FORWARD_IMPL_args(jdouble value),
	FORWARD_IMPL_pass(value))

static const JNINativeMethod mleMathAccelMethods[] =
{
	FORWARD_list(MathAccel, accel),
	FORWARD_list(MathAccel, acos),
	FORWARD_list(MathAccel, asin),
	FORWARD_list(MathAccel, atan),
	FORWARD_list(MathAccel, atan2),
	FORWARD_list(MathAccel, ceil),
	FORWARD_list(MathAccel, cos),
	FORWARD_list(MathAccel, exp),
	FORWARD_list(MathAccel, floor),
	FORWARD_list(MathAccel, log),
	FORWARD_list(MathAccel, pow),
	FORWARD_list(MathAccel, round),
	FORWARD_list(MathAccel, signum),
	FORWARD_list(MathAccel, sin),
	FORWARD_list(MathAccel, sqrt),
	FORWARD_list(MathAccel, tan),
};

jint JNICALL mleMathAccelInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/MathAccelShelf"),
		mleMathAccelMethods, sizeof(mleMathAccelMethods) /
			sizeof(JNINativeMethod));
}
