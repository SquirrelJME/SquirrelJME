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
 * A single entry that is compared against for the test, since there are
 * many different possible scenarios to be tested, this reduces duplicate
 * test code.
 * 
 * @since 2024/02/14
 */
typedef struct testDescFieldTypeEntry
{
	/** The string for the entry. */
	sjme_lpcstr string;
	
	/** The Java type. */
	sjme_javaTypeId javaType;
	
	/** The cell count of this field. */
	sjme_jint cells;
	
	/** Is this an array? */
	sjme_jboolean isArray;
	
	/** Interpretation of the array component type, if an array. */
	sjme_lpcstr componentString;
	
	/** Interpretation of object type, if an object. */
	sjme_lpcstr objectString;
} testDescFieldTypeEntry;

/** Entries for tests. */
static const testDescFieldTypeEntry testEntries[] =
{
	/* Non-array. */
	{
		"B",
		SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
		1,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"C",
		SJME_JAVA_TYPE_ID_SHORT_OR_CHAR,
		1,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"D",
		SJME_JAVA_TYPE_ID_DOUBLE,
		2,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"F",
		SJME_JAVA_TYPE_ID_FLOAT,
		1,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"I",
		SJME_JAVA_TYPE_ID_INTEGER,
		1,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"J",
		SJME_JAVA_TYPE_ID_LONG,
		2,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"S",
		SJME_JAVA_TYPE_ID_SHORT_OR_CHAR,
		1,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"Z",
		SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
		1,
		SJME_JNI_FALSE,
		NULL,
		NULL
	},
	{
		"LSqueak/In/Box;",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_FALSE,
		NULL,
		"Squeak/In/Box"
	},
	
	/* Array. */
	{
		"[B",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"B",
		NULL
	},
	{
		"[C",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"C",
		NULL
	},
	{
		"[D",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"D",
		NULL
	},
	{
		"[F",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"F",
		NULL
	},
	{
		"[I",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"I",
		NULL
	},
	{
		"[J",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"J",
		NULL
	},
	{
		"[S",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"S",
		NULL
	},
	{
		"[Z",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"Z",
		NULL
	},
	{
		"[LSqueak/In/Box;",
		SJME_JAVA_TYPE_ID_OBJECT,
		1,
		SJME_JNI_TRUE,
		"LSqueak/In/Box;",
		NULL
	},
	
	/* End. */
	{NULL}
};

/**
 * Tests parsing of class field descriptors.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testDescFieldType)
{
	const sjme_desc_fieldType* result;
	const testDescFieldTypeEntry* entry;
	const sjme_desc_fieldType* componentResult;
	const sjme_desc_binaryName* objectResult; 
	sjme_lpcstr string;
	sjme_jint strLen, strHash, atEntry;
	
	/* Go through every entry. */
	for (atEntry = 0; testEntries[atEntry].string != NULL;
		 atEntry++)
	{
		/* Get the entry to test. */
		entry = &testEntries[atEntry];
		
		/* Load in string details. */
		string = entry->string;
		strLen = strlen(string);
		strHash = sjme_string_hash(string);
		
		/* Parse the field type. */
		result = NULL;
		if (sjme_error_is(sjme_desc_interpretFieldType(test->pool,
			&result, string, strLen)) ||
			result == NULL)
			return sjme_unit_fail(test, "Could not interpret %s?", string);
		
		/* Basic whole value and hash check. */
		sjme_unit_equalI(test, result->hash, strHash,
			"Hash of whole %s incorrect?", string);
		sjme_unit_equalI(test, result->whole.length, strLen,
			"Length of whole %s incorrect?", string);
		sjme_unit_equalP(test, result->whole.pointer, string,
			"Pointer of whole %s incorrect?", string);
		
		/* Basic matching. */
		sjme_unit_equalI(test, result->javaType, entry->javaType,
			"Field %s has incorrect Java type?", string);
		sjme_unit_equalI(test, result->cells, entry->cells,
			"Field %s has incorrect number of cells?", string);
		sjme_unit_equalZ(test, result->isArray, entry->isArray,
			"Field %s has incorrect array state?", string);
		
		/* Component type. */
		sjme_unit_equalI(test, 0, sjme_desc_compareFieldS(
			result->componentType, entry->componentString),
			"Field %s has incorrect component type?", string);
		if (entry->componentString != NULL)
		{
			/* Parse it. */
			componentResult = NULL;
			if (sjme_error_is(sjme_desc_interpretFieldType(
				test->pool,
				&componentResult, entry->componentString,
				strlen(entry->componentString))) ||
				componentResult == NULL)
				return sjme_unit_fail(test,
				"Field %s has an invalid component string?", string);
			
			/* Should be the same. */
			sjme_unit_equalI(test, 0, sjme_desc_compareField(
				result->componentType, componentResult),
				"Field %s has non-equal component?", string);
		}
		
		/* Class binary name, if an object. */
		sjme_unit_equalI(test, 0, sjme_desc_compareBinaryNameS(
			result->objectType, entry->objectString),
			"Field %s has incorrect object type?", string);
		if (entry->objectString != NULL)
		{
			/* Parse it. */
			objectResult = NULL;
			if (sjme_error_is(sjme_desc_interpretBinaryName(
				test->pool,
				&objectResult, entry->objectString,
				strlen(entry->objectString))) ||
				objectResult == NULL)
				return sjme_unit_fail(test,
				"Field %s has an invalid object string?", string);
			
			/* Should be the same. */
			sjme_unit_equalI(test, 0, sjme_desc_compareBinaryName(
				result->objectType, objectResult),
				"Field %s has non-equal object?", string);
		}
	}
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
