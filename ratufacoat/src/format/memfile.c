/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "format/memfile.h"

/**
 * "Detects" a memory file by always returning @c true.
 * 
 * @param data The data block.
 * @param size The size of the block.
 * @param error The error used.
 * @return Always @c true.
 * @since 2021/11/09
 */
sjme_jboolean sjme_memFileLibraryDetect(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_true;
}

/**
 * Initializes the memory file library.
 * 
 * @param instance The instance to initialize in.
 * @param error The error state.
 * @return If initialization was a success.
 * @since 2021/11/09 
 */
sjme_jboolean sjme_memFileLibraryInit(void* instance, sjme_error* error)
{
	sjme_todo("Implement this?");
}

const sjme_libraryDriver sjme_libraryMemFileDriver =
{
	.detect = sjme_memFileLibraryDetect,
	.init = sjme_memFileLibraryInit,
};
