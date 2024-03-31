/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"
#include "lib/scritchui/scritchui.h"
#include "sjme/dylib.h"
#include "sjme/debug.h"

/** The class being implemented. */
#define IMPL_CLASS "cc/squirreljme/emulator/scritchui/dylib/" \
	"NativeScritchDylib/NativeScritchDylib"
#define FORWARD_CLASS IMPL_CLASS

#define FORWARD_DESC___link "(" \
	DESC_STRING DESC_STRING ")" DESC_LONG

JNIEXPORT long JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __link)
	(JNIEnv* env, jclass classy, jstring libPath, jstring name)
{
	void* lib;
	
	if (SJME_ERROR_IS(sjme_dylib_open(libPath, &lib)))
		return 0;
	
	sjme_todo("Fail");
}

static const JNINativeMethod mleNativeScritchDylibMethods[] =
{
	FORWARD_list(NativeScritchDylib, __link),
};

FORWARD_init(mleNativeScritchDylibInit, mleNativeScritchDylibMethods)
