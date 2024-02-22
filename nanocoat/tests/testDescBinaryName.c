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

/** Pair of strings. */
#define pair(s) s, strlen(s)

/**
 * Test parsing of class binary names.
 *  
 * @since 2024/01/02 
 */
SJME_TEST_DECLARE(testDescBinaryName)
{
	sjme_desc_binaryName* result;
	sjme_lpcstr string;
	sjme_jint strLen, strHash;
	
	/* Setup base. */
	string = "Squeak";
	strLen = strlen(string);
	strHash = sjme_string_hash(string);
	
	/* Basic name. */
	result = NULL;
	if (sjme_error_is(sjme_desc_interpretBinaryName(test->pool,
		&result, string, strLen) || result == NULL))
		sjme_unit_fail(test, "Could not parse binary name?");
	
	/* Common checks. */
	sjme_unit_equalI(test, result->hash, strHash,
		"Hash incorrect?");
	sjme_unit_equalI(test, result->whole.length, strLen,
		"Length incorrect?");
	sjme_unit_equalP(test, result->whole.pointer, string,
		"Pointer incorrect?");
	
	/* Check that everything is valid. */
	sjme_unit_equalI(test, result->identifiers.length, 1,
		"Incorrect number of elements?");
	sjme_unit_equalI(test,
		0, sjme_desc_compareIdentifierS(
			&result->identifiers.elements[0], "Squeak"),
		"Incorrect identifier 1?");
	
	/* Setup package. */
	string = "Squeak/In/Box";
	strLen = strlen(string);
	strHash = sjme_string_hash(string);
	
	/* With package. */
	result = NULL;
	if (sjme_error_is(sjme_desc_interpretBinaryName(test->pool,
		&result, string, strLen) || result == NULL))
		sjme_unit_fail(test, "Could not parse packaged binary name?");
		
	/* Common checks. */
	sjme_unit_equalI(test, result->hash, strHash,
		"Hash incorrect?");
	sjme_unit_equalI(test, result->whole.length, strLen,
		"Length incorrect?");
	sjme_unit_equalP(test, result->whole.pointer, string,
		"Pointer incorrect?");
	
	/* Test everything. */
	sjme_unit_equalI(test, result->identifiers.length, 3,
		"Incorrect number of elements?");
		
	sjme_unit_equalI(test,
		0, sjme_desc_compareIdentifierS(
			&result->identifiers.elements[0], "Squeak"),
		"Incorrect identifier 1?");
		
	sjme_unit_equalI(test,
		0, sjme_desc_compareIdentifierS(
			&result->identifiers.elements[1], "In"),
		"Incorrect identifier 2?");
		
	sjme_unit_equalI(test,
		0, sjme_desc_compareIdentifierS(
			&result->identifiers.elements[2], "Box"),
		"Incorrect identifier 3?");
	
	/* Check invalid binary names. */
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("")),
		"Blank is a valid binary name?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("/")),
		"Single slash is valid binary name?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("Squeak/")),
		"Ending slash is valid binary name?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("/Squeak")),
		"Starting slash is valid binary name?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("Cute//Squeak")),
		"Double slash is valid binary name?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("[Z")),
		"Array is valid binary name?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_IDENTIFIER,
		sjme_desc_interpretBinaryName(test->pool, &result,
			pair("Squeak/[Z")),
		"Array in package is valid binary name?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
