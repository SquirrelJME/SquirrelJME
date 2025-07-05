/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#ifndef SJME_C_JVM_MD_H
#define SJME_C_JVM_MD_H

#if defined(_WIN32) || defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#include "jvm_win.h"
#elif defined(__APPLE__) || defined(SJME_CONFIG_HAS_OS_MACOS)
	#include "jvm_mac.h"
#else
	#include "jvm_unix.h"
#endif

#endif /* SJME_C_JVM_MD_H */
