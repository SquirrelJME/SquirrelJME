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

#if defined(__linux__) || defined(SJME_CONFIG_HAS_LINUX)
	#include "jni_linux.h"
#elif defined(_WIN32) || defined(SJME_CONFIG_HAS_WINDOWS)
	#include "jni_win.h"
#elif defined(__APPLE__) || defined(SJME_CONFIG_HAS_MACOS)
	#include "jni_mac.h"
#endif

#endif /* __SQUIRRELJME_JNI_MD_H__ */
