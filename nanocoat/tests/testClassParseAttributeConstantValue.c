/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

/**
 * Tests parsing of constant values.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testClassParseAttributeConstantValue)
{
	sjme_classBuilder builder;

	memset(&builder, 0, sizeof(builder));
	if (SJME_IS_ERROR(sjme_classBuilder_init(test->pool,
		&builder, test)))
		return sjme_unitFail(test, "Failed to initialize class builder.");

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
