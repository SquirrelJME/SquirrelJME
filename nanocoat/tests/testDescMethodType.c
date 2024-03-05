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
 * What to test and the expected values for method descriptors.
 * 
 * @since 2024/02/18
 */
typedef struct testDescMethodTypeEntry
{
	/** The string for the entry. */
	sjme_lpcstr string;
	
	/** Return value cells. */
	sjme_jint returnCells;
	
	/** Argument cells. */
	sjme_jint argCells;
	
	/** The field descriptors used, index zero is the return value. */
	sjme_lpcstr fields;
} testDescMethodTypeEntry;

/** Entries for tests. */
static const testDescMethodTypeEntry testEntries[] =
{
	/* Void returning void. */
	{
		"()V",
		0,
		0,
		"V\0\0"
	},
	
	/* Void returning boolean. */
	{
		"()Z",
		1,
		0,
		"Z\0\0"
	},
	
	/* Void returning byte. */
	{
		"()B",
		1,
		0,
		"B\0\0"
	},
	
	/* Void returning short. */
	{
		"()S",
		1,
		0,
		"S\0\0"
	},
	
	/* Void returning char. */
	{
		"()C",
		1,
		0,
		"C\0\0"
	},
	
	/* Void returning int. */
	{
		"()I",
		1,
		0,
		"I\0\0"
	},
	
	/* Void returning long. */
	{
		"()J",
		2,
		0,
		"J\0\0"
	},
	
	/* Void returning float. */
	{
		"()F",
		1,
		0,
		"F\0\0"
	},
	
	/* Void returning double. */
	{
		"()D",
		2,
		0,
		"D\0\0"
	},
	
	/* Void returning object. */
	{
		"()LSqueak/In/Box;",
		1,
		0,
		"LSqueak/In/Box;\0\0"
	},
	
	/* Boolean returning void. */
	{
		"(Z)V",
		0,
		1,
		"V\0Z\0\0"
	},
	
	/* Byte returning void. */
	{
		"(B)V",
		0,
		1,
		"V\0B\0\0"
	},
	
	/* Short returning void. */
	{
		"(S)V",
		0,
		1,
		"V\0S\0\0"
	},
	
	/* Char returning void. */
	{
		"(C)V",
		0,
		1,
		"V\0C\0\0"
	},
	
	/* Int returning void. */
	{
		"(I)V",
		0,
		1,
		"V\0I\0\0"
	},
	
	/* Long returning void. */
	{
		"(J)V",
		0,
		2,
		"V\0J\0\0"
	},
	
	/* Float returning void. */
	{
		"(F)V",
		0,
		1,
		"V\0F\0\0"
	},
	
	/* Double returning void. */
	{
		"(D)V",
		0,
		2,
		"V\0D\0\0"
	},
	
	/* Object returning void. */
	{
		"(LSqueak/In/Box;)V",
		0,
		1,
		"V\0LSqueak/In/Box;\0\0"
	},
	
	/* Long long returning object. */
	{
		"(JJ)LSqueak/In/Box;",
		1,
		4,
		"LSqueak/In/Box;\0J\0J\0\0"
	},
	
	/* Object returning long. */
	{
		"(LSqueak/In/Box;)J",
		2,
		1,
		"J\0LSqueak/In/Box;\0\0"
	},
	
	/* Int long returning double. */
	{
		"(IJ)D",
		2,
		3,
		"D\0I\0J\0\0"
	},
	
	/* Long int returning double. */
	{
		"(JI)D",
		2,
		3,
		"D\0J\0I\0\0"
	},
	
	/* End. */
	{NULL},
};

/** String pair. */
#define pair(s) s, strlen(s)

/**
 * Tests parsing of method descriptors.
 *  
 * @since 2024/01/01
 */
SJME_TEST_DECLARE(testDescMethodType)
{
	const testDescMethodTypeEntry* entry;
	const sjme_desc_methodType* result;
	const sjme_desc_fieldType* field;
	sjme_list_sjme_lpcstr* fieldStrings;
	sjme_lpcstr string, subString;
	sjme_jint strLen, subStrLen, strHash, atEntry, i;
	
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
		
		/* Interpret method entry. */
		result = NULL;
		if (sjme_error_is(sjme_desc_interpretMethodType(test->pool,
			&result, string, strLen)) || result == NULL)
			return sjme_unit_fail(test, "Could not interpret %s?", string);
		
		/* Basic whole value and hash check. */
		sjme_unit_equalI(test, result->hash, strHash,
			"Hash of whole %s incorrect?", string);
		sjme_unit_equalI(test, result->whole.length, strLen,
			"Length of whole %s incorrect?", string);
		sjme_unit_equalP(test, result->whole.pointer, string,
			"Pointer of whole %s incorrect?", string);
		
		/* Cells should match. */
		sjme_unit_equalI(test, result->returnCells, entry->returnCells,
			"Return cells of %s incorrect?", string);
		sjme_unit_equalI(test, result->argCells, entry->argCells,
			"Argument cells of %s incorrect?", string);
		
		/* Parse recorded fields. */
		fieldStrings = NULL;
		if (sjme_error_is(sjme_list_flattenArgNul(test->pool,
			&fieldStrings, entry->fields) ||
			fieldStrings == NULL))
			return sjme_unit_fail(test, "Could not parse fields of %s?",
				string);
		
		/* Count should match. */
		sjme_unit_equalI(test, fieldStrings->length, result->fields.length,
			"Incorrect field count for %s?", string);
		
		/* Match each individual field. */
		for (i = 0; i < fieldStrings->length; i++)
		{
			/* Get string information. */
			subString = fieldStrings->elements[i];
			subStrLen = strlen(subString);
			
			/* Parse */
			field = NULL;
			if (sjme_error_is(sjme_desc_interpretFieldType(
				test->pool, &field, subString,
				subStrLen)) || field == NULL)
				return sjme_unit_fail(test, "Could not parse field %s in %s?",
					subString, string);
			
			/* Should be the same field. */
			sjme_unit_equalI(test, 0, sjme_desc_compareFieldC(
				&result->fields.elements[i], field),
				"Decoded field %s is incorrect in %s (%.*s == %s)?",
					subString, string,
					result->fields.elements[i].fragment.length,
					result->fields.elements[i].fragment.pointer, field);
		}
	}
	
	/* Invalid methods types. */
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_METHOD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("")),
		"Blank is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_METHOD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("V")),
		"Return only is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_METHOD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("()")),
		"Arguments only is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_METHOD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("()II")),
		"Double return type is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("()[")),
		"Unspecified array return is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("()L")),
		"Unspecified object return is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("()LOops")),
		"Unclosed object return is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("([)V")),
		"Unspecified array is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("(L)V")),
		"Unspecified object is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("(LOops)V")),
		"Unclosed object is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_METHOD_TYPE,
		sjme_desc_interpretMethodType(test->pool,
			&result, pair("V()")),
		"Wrong order is valid?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
