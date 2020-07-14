/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#ifndef __SQUIRRELJME_H__
#define __SQUIRRELJME_H__

#include "jni.h"

jint JNICALL mleDebugInit(JNIEnv* env, jclass classy);
jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy);
jint JNICALL mleObjectInit(JNIEnv* env, jclass classy);
jint JNICALL mleTerminalInit(JNIEnv* env, jclass classy);
jint JNICALL mleThreadInit(JNIEnv* env, jclass classy);

#endif /* __SQUIRRELJME_H__ */
