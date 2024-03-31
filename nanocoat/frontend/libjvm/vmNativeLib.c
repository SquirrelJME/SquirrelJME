/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "sjme/debug.h"
#include "sjme/nvm.h"
#include "frontend/libjvm/jniMissing.h"

sjme_attrUnused void* JNICALL JVM_FindLibraryEntry(
	sjme_attrInNotNull void* libHandle,
	sjme_attrInNotNull sjme_lpcstr inName)
{
	/* Idea: Emulated libraries? Would require a NOEXEC trap. */
	sjme_todo("Impl?");
}

sjme_attrUnused void* JNICALL JVM_LoadLibrary(
	sjme_attrInNotNull sjme_lpcstr inName)
{
	/* Idea: Emulated libraries? Could be used for bootstrapping? */
	sjme_todo("Impl?");
}

sjme_attrUnused void JNICALL JVM_UnloadLibrary(
	sjme_attrInNotNull void* libHandle)
{
	sjme_todo("Impl?");
}
