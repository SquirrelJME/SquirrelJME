/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"

/** GTK Function set for Scritch UI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_gtkFunctions =
{
	.apiFlags = NULL,
	.apiInit = sjme_scritchui_gtk2_apiInit,
	.loopIterate = NULL,
};

/**
 * Returns the GTK ScritchUI interface.
 * 
 * @return The library interface.
 * @since 2024/03/29 
 */
const sjme_scritchui_apiFunctions* SJME_SCRITCHUI_DYLIB_SYMBOL(gtk2)(void)
{
	return &sjme_scritchUI_gtkFunctions;
}
