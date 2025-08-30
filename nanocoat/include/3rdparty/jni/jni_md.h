/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#ifndef __SQUIRRELJME_JNI_MD_H__
#define __SQUIRRELJME_JNI_MD_H__

#if defined(_WIN32) || defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#include "jni_win.h"
#elif defined(__APPLE__) || defined(SJME_CONFIG_HAS_OS_MACOS)
	#include "jni_mac.h"
#else
	#include "jni_unix.h"
#endif

#endif /* __SQUIRRELJME_JNI_MD_H__ */
