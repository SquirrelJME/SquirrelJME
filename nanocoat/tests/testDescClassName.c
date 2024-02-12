/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>
#include <string.h>

#include "mock.h"
#include "proto.h"
#include "sjme/util.h"
#include "test.h"
#include "unit.h"

/**
 * Tests parsing of class names.
 *  
 * @since 2024/02/04 
 */
SJME_TEST_DECLARE(testDescClassName)
{
	sjme_desc_binaryName* result;
	sjme_lpcstr string;
	sjme_jint strLen, strHash;
	
	/* Setup base. */
	string = "Squeak";
	strLen = strlen(string);
	strHash = sjme_string_hash(string);
	
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
