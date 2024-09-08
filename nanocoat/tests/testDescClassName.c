/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/descriptor.h"
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
	sjme_desc_className* result;
	sjme_lpcstr string;
	sjme_jint strLen, strHash;
	
	/* Setup default package. */
	string = "Squeak";
	strLen = strlen(string);
	strHash = sjme_string_hash(string);
	
	/* Parse. */
	result = NULL;
	if (!sjme_desc_interpretClassName(test->pool,
		&result, string, strLen) ||
		result == NULL)
		return sjme_unit_fail(test, "Could not interpret class name?");
	
	/* Check that it is valid. */
	sjme_unit_equalI(test, strHash, result->hash,
		"Hash incorrect?");
	sjme_unit_isFalse(test, result->isField,
		"Was a field?");
	sjme_unit_equalI(test, 0, sjme_desc_compareClassS(result,
		"Squeak"),
		"Incorrect binary name?");
	
	/* Setup base package. */
	string = "Squeak/In/Box";
	strLen = strlen(string);
	strHash = sjme_string_hash(string);
	
	/* Parse. */
	result = NULL;
	if (!sjme_desc_interpretClassName(test->pool,
			&result, string, strLen) ||
		result == NULL)
		return sjme_unit_fail(test, "Could not interpret class name?");
	
	/* Check validity. */
	sjme_unit_equalI(test, strHash, result->hash,
		"Hash incorrect?");
	sjme_unit_isFalse(test, result->isField,
		"Was a field?");
	sjme_unit_equalI(test, 0, sjme_desc_compareClassS(result,
		"Squeak/In/Box"),
		"Incorrect binary name?");
	
	/* Parse. */
	result = NULL;
	if (!sjme_desc_interpretClassName(test->pool,
			&result, string, strLen) ||
		result == NULL)
		return sjme_unit_fail(test, "Could not interpret class name?");
	
	/* Array type. */
	string = "[LSqueak/In/Box;";
	strLen = strlen(string);
	strHash = sjme_string_hash(string);
	
	/* Parse. */
	result = NULL;
	if (!sjme_desc_interpretClassName(test->pool,
			&result, string, strLen) ||
		result == NULL)
		return sjme_unit_fail(test, "Could not interpret class name?");
	
	/* Check. */
	sjme_unit_equalI(test, strHash, result->hash,
		"Hash incorrect?");
	sjme_unit_isTrue(test, result->isField,
		"Was a binary name?");
	sjme_unit_equalI(test, 0, sjme_desc_compareClassS(result,
		"[LSqueak/In/Box;"),
		"Incorrect field name?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
