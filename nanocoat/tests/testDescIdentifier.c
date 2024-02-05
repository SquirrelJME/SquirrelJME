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
#include "sjme/util.h"
#include "test.h"
#include "unit.h"

/**
 * Tests parsing of identifiers.
 *  
 * @since 2024/02/04 
 */
SJME_TEST_DECLARE(testDescIdentifier)
{
	sjme_lpcstr string;
	sjme_desc_identifier result;
	sjme_jint stringHash;
	
	/* Determine the hash of the string. */
	string = "squirrel";
	stringHash = sjme_string_hash(string);
	
	/* Valid identifier. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(sjme_desc_interpretIdentifier(
		&result, string, 0)))
		sjme_unitFail(test, "Could not interpret identifier?");
	
	/* Make sure it was calculated correctly. */
	sjme_unitNotEqualI(test, result.hash, stringHash,
		"Hash set incorrectly?");
	sjme_unitEqualP(test, result.pointer.pointer, (void*)string,
		"Pointer not valid?");
	sjme_unitEqualI(test, result.pointer.length, strlen(string),
		"Pointer length not valid?");
		
	/* All of these are not valid. */
	memset(&result, 0, sizeof(result));
	sjme_unitEqualI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(&result,
		"squirrel.squirrel", 0),
		"Name with '.' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unitEqualI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(&result,
			"squirrel;squirrel", 0),
		"Name with ';' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unitEqualI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(&result,
			"squirrel[squirrel", 0),
		"Name with '[' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unitEqualI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(&result,
			"squirrel/squirrel", 0),
		"Name with '/' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unitEqualI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(&result,
			"", 0),
		"Blank is valid?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
