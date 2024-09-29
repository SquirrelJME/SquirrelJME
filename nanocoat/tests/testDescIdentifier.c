/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/descriptor.h"
#include <string.h>

#include "mock.h"
#include "proto.h"
#include "sjme/util.h"
#include "test.h"
#include "unit.h"

#define pair(s) s, strlen(s)

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
	sjme_stringPool stringPool;
	
	/* Determine the hash of the string. */
	string = "squirrel";
	stringHash = sjme_string_hash(string);

	stringPool = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_new(
		test->pool, &stringPool)) || stringPool == NULL)
		return sjme_unit_fail(test, "Could not create string pool?");
	
	/* Valid identifier. */
	if (sjme_error_is(test->error = sjme_desc_interpretIdentifier(
		test->pool, stringPool, &result,
		string, strlen(string))))
		return sjme_unit_fail(test, "Could not interpret identifier?");
	
	/* Make sure it was calculated correctly. */
	sjme_unit_equalI(test, result->hash, stringHash,
		"Hash set incorrectly?");
	sjme_unit_notEqualP(test, result->whole, NULL,
		"Pointer not valid?");
	sjme_unit_equalI(test,
		0, sjme_string_compareN(
			string, strlen(string),
			(sjme_lpcstr)result->whole->chars,
				result->whole->length),
		"Incorrect string stored?");
	sjme_unit_equalI(test,
		0, sjme_desc_compareIdentifierS(result, "squirrel"),
		"Identifier does not match?");
		
	/* All of these are not valid. */
	memset(&result, 0, sizeof(result));
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(test->pool,
		stringPool, &result, 
		pair("squirrel.squirrel")),
		"Name with '.' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(test->pool, 
		stringPool, &result, 
			pair("squirrel;squirrel")),
		"Name with ';' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(test->pool, 
		stringPool, &result, 
			pair("squirrel[squirrel")),
		"Name with '[' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(test->pool,
		stringPool, &result, 
			pair("squirrel/squirrel")),
		"Name with '/' is valid?");
		
	memset(&result, 0, sizeof(result));
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretIdentifier(test->pool, 
		stringPool, &result, 
			pair("")),
		"Blank is valid?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
