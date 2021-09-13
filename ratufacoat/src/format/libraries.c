/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/libraries.h"
#include "format/sqc.h"

/** The libraries drivers which are available for usage. */
const sjme_librariesDriver* const sjme_librariesDrivers[] =
{
	&sjme_librariesSqcDriver,
	
	NULL
};
