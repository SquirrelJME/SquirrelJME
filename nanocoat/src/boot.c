/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"

jboolean sjme_nvm_boot(const sjme_nvm_bootConfig* config,
	sjme_nvm_state** outState, int argc, char** argv)
{
	if (config == NULL || outState == NULL)
		return JNI_FALSE;
	
	sjme_todo("sjme_nvm_boot()");
	return JNI_FALSE;
}

jboolean sjme_nvm_destroy(sjme_nvm_state* state, jint* exitCode)
{
	if (state == NULL)
		return JNI_FALSE;
	
	sjme_todo("sjme_nvm_destroy()");
	return JNI_FALSE;
}
