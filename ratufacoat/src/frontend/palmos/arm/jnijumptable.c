/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <PceNativeCall.h>

#include "sjmejni/sjmejni.h"

/**
 * Returns the landing point for the native JNI call that is implemented in
 * ARM.
 *
 * @param emulStateP Emulation state.
 * @param userData68KP User data for the call.
 * @param call68KFuncP 68k function pointer.
 * @return If the call was successful or not.
 * @since 2023/01/06
 */
sjme_juint sjme_palmOsGetPnoLanding(const void* emulStateP,
	void* userData68KP, Call68KFuncType* call68KFuncP)
{
	return 0;
}
