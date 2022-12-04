/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

// The class to forward to
#define BUSTRANSPORT_CLASSNAME "cc/squirreljme/emulator/EmulatedBusTransportShelf"

#define BUSTRANSPORT_PRIMARY_DESC "()Lcc/squirreljme/jvm/mle/brackets/BusTransportBracket;"

JNIEXPORT jobject JNICALL Impl_mle_BusTransportShelf_primary(
	JNIEnv* env, jclass classy)
{
	return forwardCallStaticObject(env, BUSTRANSPORT_CLASSNAME,
		"primary", BUSTRANSPORT_PRIMARY_DESC);
}

static const JNINativeMethod mleBusTransportMethods[] =
{
	{"primary", BUSTRANSPORT_PRIMARY_DESC, (void*)Impl_mle_BusTransportShelf_primary},
};

jint JNICALL mleBusTransportInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/BusTransportShelf"),
		mleBusTransportMethods, sizeof(mleBusTransportMethods) /
			sizeof(JNINativeMethod));
}
