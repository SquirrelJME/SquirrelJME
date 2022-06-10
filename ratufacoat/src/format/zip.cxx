/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/zip.h"
#include "debug.h"

/**
 * Detects library files.
 *
 * @param data ROM data.
 * @param size ROM size.
 * @param error Error output.
 * @return If detected or not.
 * @since 2021/09/12
 */
static sjme_jboolean sjme_detectZip(const void* data, sjme_jint size,
	sjme_error* error)
{
	sjme_todo("sjme_detectZip");
}

const sjme_libraryDriver sjme_libraryZipDriver =
	SJME_DESIGNATED(sjme_libraryDriver,
		s_.detect = sjme_detectZip
	);
