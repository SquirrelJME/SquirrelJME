/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/sqc.h"
#include "debug.h"

/** The magic number for pack libraries. */
#define PACK_MAGIC_NUMBER UINT32_C(0x58455223)

/** The magic number for individual JAR libraries. */
#define JAR_MAGIC_NUMBER UINT32_C(0x00456570)

/* --------------------------------- COMMON -------------------------------- */

/* ---------------------------------- PACK --------------------------------- */

/**
 * Detects pack files.
 * 
 * @param data ROM data. 
 * @param size ROM size.
 * @param error Error output.
 * @return If detected or not.
 * @since 2021/09/12
 */
static sjme_jboolean sjme_detectSqcPack(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_detectMagicNumber(data, size, PACK_MAGIC_NUMBER, error);
}

static sjme_jboolean sjme_initSqcPackInstance(sjme_packInstance* instance)
{
	sjme_todo("Init pack");
}

const sjme_packDriver sjme_packSqcDriver =
{
	.detect = sjme_detectSqcPack,
	.initInstance = sjme_initSqcPackInstance,
};

/* -------------------------------- LIBRARY -------------------------------- */

/**
 * Detects library files.
 * 
 * @param data ROM data. 
 * @param size ROM size.
 * @param error Error output.
 * @return If detected or not.
 * @since 2021/09/12
 */
static sjme_jboolean sjme_detectSqcLib(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_detectMagicNumber(data, size, JAR_MAGIC_NUMBER, error);
}

const sjme_libraryDriver sjme_librarySqcDriver =
{
	.detect = sjme_detectSqcLib,
};
