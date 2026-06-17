/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Selects the JNI header to use.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_USEJNI_H
#define SJME_C_SQUIRRELJME_USEJNI_H

#if defined(SJME_CONFIG_USE_OWN_JNI)
	#include "sjme/jvm/jni.h"
#else
	/* Need to map implementation defines? */
	#if defined(SJME_JNI_IMPLEMENTATION)
		#if !defined(_JNI_IMPLEMENTATION_)
			#define _JNI_IMPLEMENTATION_
		#endif
	#endif

	#include <jni.h>
#endif

#endif /* SJME_C_SQUIRRELJME_USEJNI_H */
