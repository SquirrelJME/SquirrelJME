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
const sjme_libraryDriver* const sjme_libraryDrivers[] =
{
	&sjme_libraryZipDriver,
	&sjme_librarySqcDriver,
	
	NULL
};
