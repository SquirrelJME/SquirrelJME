/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/sqc.h"

/** The magic number for pack libraries. */
#define PACK_MAGIC_NUMBER UINT32_C(0x58455223)

/** The magic number for individual JAR libraries. */
#define JAR_MAGIC_NUMBER UINT32_C(0x00456570)

/* ------------------------------- LIBRARIES ------------------------------- */

const sjme_librariesDriver sjme_libraries_sqc_driver =
{
};

/* -------------------------------- LIBRARY -------------------------------- */

const sjme_libraryDriver sjme_library_sqc_driver =
{
};
