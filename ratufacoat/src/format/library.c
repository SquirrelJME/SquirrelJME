/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/library.h"
#include "format/sqc.h"
#include "format/zip.h"

/** The library drivers which are available for usage. */
const sjme_library_driver* sjme_library_drivers[] =
{
	&sjme_library_zip_driver,
	&sjme_library_sqc_driver,
	
	NULL
};
