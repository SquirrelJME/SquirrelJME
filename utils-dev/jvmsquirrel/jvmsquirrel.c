/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------
// This is a miniature Java VM virtual machine for simplistic sanity purposes.
// -------------------------------------------------------------------------*/

#define _JNI_IMPLEMENTATION_

#include "jni.h"
#include "jvm.h"

_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_GetDefaultJavaVMInitArgs(void* pargs)
{
	abort();
}

_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_CreateJavaVM(JavaVM** pvm, void** penv,
	void* args)
{
	abort();
}

_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_GetCreatedJavaVMs(JavaVM** pvm, jsize psz, jsize* pcount)
{
	abort();
}

