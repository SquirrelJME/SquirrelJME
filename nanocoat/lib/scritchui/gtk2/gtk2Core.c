/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gtk-2.0/gtk/gtk.h>

#include "lib/scritchui/scritchui.h"

/** GTK Function set for Scritch UI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_gtkFunctions =
{
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
