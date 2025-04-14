/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

#define NAME_INTERFACE "cc/squirreljme/jvm/mle/scritchui/ScritchInterface"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/scritchui/NativeScritchInterface"
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/scritchui/EmulatedNativeScritchInterface"

#define FORWARD_DESC_nativeInterface "(" \
	")" DESC_CLASS(NAME_INTERFACE)
#define FORWARD_DESC_panelOnly "(" \
	")" DESC_BOOLEAN

FORWARD_IMPL(NativeScritchInterface, nativeInterface, jobject, Object, \
	FORWARD_IMPL_none(), FORWARD_IMPL_none())
FORWARD_IMPL(NativeScritchInterface, panelOnly, jboolean, Boolean, \
	FORWARD_IMPL_none(), FORWARD_IMPL_none())

static const JNINativeMethod mleNativeScritchInterfaceMethods[] =
{
	FORWARD_list(NativeScritchInterface, nativeInterface),
	FORWARD_list(NativeScritchInterface, panelOnly),
};

FORWARD_init(mleNativeScritchInterfaceInit, mleNativeScritchInterfaceMethods)
