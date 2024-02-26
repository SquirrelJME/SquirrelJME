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
 * Tests parsing of a blank class.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testClassParseBlank)
{
	sjme_classBuilder builder;
	sjme_jint thisName, thisNameUtf;
	sjme_jint superName, superNameUtf;
	void* rawClass;
	
	/* Setup builder. */
	memset(&builder, 0, sizeof(builder));
	if (sjme_error_is(sjme_classBuilder_build(test->pool,
		&builder, SJME_JNI_FALSE, test)))
		return sjme_unit_fail(test, "Failed to initialize class builder.");
	
	/* Build. */
	sjme_error_alsoV(SJME_ERROR_NONE,
		/* Pool. */
		sjme_classBuilder_addPool(0, &builder,
			3),
			
		sjme_classBuilder_addPoolEntryUtf(1, &builder,
			&thisNameUtf, "Blank"),
		sjme_classBuilder_addPoolEntryClass(2, &builder,
			&thisName, &thisNameUtf),
		
		sjme_classBuilder_addPoolEntryUtf(3, &builder,
			&superNameUtf, "java/lang/Object"),
		sjme_classBuilder_addPoolEntryClass(4, &builder,
			&superName, &superNameUtf),
			
		/* Class. */
		sjme_classBuilder_declareClassA(5, &builder,
			0, &thisName, &superName,
			0, NULL),
		
		/* End. */
		sjme_error_alsoVEnd());
		
	/* Construct. */	
	if (sjme_error_is(sjme_classBuilder_finish(&builder, &rawClass)))
		return sjme_unit_fail(test, "Could not construct class.");
	
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
