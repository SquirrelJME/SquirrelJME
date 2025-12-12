/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"

#include "sjme/dylibExtra.h"

/**
 * Main entry point for the ScritchUI demo.
 * 
 * @param argc Argument count.
 * @param argv Arguments passed.
 * @return Zero on success, otherwise any other value is failure.
 * @since 2025/12/07
 */
int main(int argc, char** argv)
{
#define SYM_MAX 48
	sjme_errorCode error;
	sjme_dylib lib;
	sjme_scritchui_dylibApiFunc apiInit;
	sjme_cchar apiInitSym[SYM_MAX];

	/* Load the ScritchUI library. */
	lib = NULL;
	if (sjme_error_is(error = sjme_dylib_openExtra(NULL,
		SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI, NULL, &lib)) || lib == NULL)
		goto fail_any;

#if 0
	/* Resolve name to load in. */
	memset(apiInitSym, 0, sizeof(apiInitSym));
	snprintf(apiInitSym, SYM_MAX - 1, "%s%s",
		SJME_TOKEN_STRING_PP(SJME_SCRITCHAUDIO_DYLIB_SYMBOL_PREFIX),
		nameChars);
	apiInitSym[SYM_MAX - 1] = 0;
#endif
	
	return 0;
	
fail_any:
	sjme_emitB("Error: %d", error);
	return EXIT_FAILURE;
#undef SYM_MAX
}
