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

/** The max permitted array size. */
#define MAX_ARRAY 8

/**
 * Represents a component entry.
 * 
 * @since 2024/02/22
 */
typedef struct testDescFieldTypeComponentEntry
{
	/** The Java type. */
	sjme_javaTypeId javaType;
	
	/** The cell count of this field. */
	sjme_jint cells;
	
	/** Is this an array? */
	sjme_jboolean isArray;
} testDescFieldTypeComponentEntry;

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
	
	/** Interpretation of object type, if an object. */
	sjme_lpcstr objectString;
	
	/** The number of dimensions. */
	sjme_jint numDims;
	
	/** Component data. */
	testDescFieldTypeComponentEntry components[MAX_ARRAY];
} testDescFieldTypeEntry;

/** Entries for tests. */
static const testDescFieldTypeEntry testEntries[] =
{
	/* Non-array. */
	{
		"B",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
				1,
				SJME_JNI_FALSE
			}
		}
	},
	{
		"C",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_SHORT_OR_CHAR,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"D",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_DOUBLE,
				2,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"F",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_FLOAT,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"I",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_INTEGER,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"J",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_LONG,
				2,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"S",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_SHORT_OR_CHAR,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"Z",
		NULL,
		0,
		{
			{
				SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"LSqueak/In/Box;",
		"Squeak/In/Box",
		0,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	
	/* Array. */
	{
		"[B",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[C",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_SHORT_OR_CHAR,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[D",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_DOUBLE,
				2,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[F",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_FLOAT,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[I",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_INTEGER,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[J",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_LONG,
				2,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[S",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_SHORT_OR_CHAR,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[Z",
		NULL,
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[LSqueak/In/Box;",
		"Squeak/In/Box",
		1,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[[I",
		NULL,
		2,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_INTEGER,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	{
		"[[LSqueak/In/Box;",
		"Squeak/In/Box",
		2,
		{
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_TRUE,
			},
			{
				SJME_JAVA_TYPE_ID_OBJECT,
				1,
				SJME_JNI_FALSE,
			}
		}
	},
	
	/* End. */
	{NULL}
};

/** String pair. */
#define pair(s) s, strlen(s)

/**
 * Tests parsing of class field descriptors.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testDescFieldType)
{
	const testDescFieldTypeEntry* entry;
	const sjme_desc_fieldType* result;
	const sjme_desc_binaryName* objectResult; 
	sjme_lpcstr string;
	sjme_jint strLen, strHash, atEntry, i;
	sjme_errorCode error;
	
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
		if (sjme_error_is(error = sjme_desc_interpretFieldType(
			test->pool, &result, string, strLen)) ||
			result == NULL)
			return sjme_unit_fail(test, "Could not interpret %s (%d)?",
				string, error);
		
		/* Basic whole value and hash check. */
		sjme_unit_equalI(test, result->hash, strHash,
			"Hash of whole %s incorrect?", string);
		sjme_unit_equalI(test, result->whole.length, strLen,
			"Length of whole %s incorrect?", string);
		sjme_unit_equalP(test, result->whole.pointer, string,
			"Pointer of whole %s incorrect?", string);
		
		/* Should have same number of dimensions. */
		sjme_unit_equalI(test, result->numDims, entry->numDims,
			"Field %s has incorrect number of dimensions?", string);
		
		/* Match all components. */
		for (i = 0; i <= entry->numDims; i++)
		{
			/* Debug. */
			sjme_message("Field %s, fragment %d: %.*s",
				string, i, result->components[i].fragment.length,
				(char*)result->components[i].fragment.pointer);
			
			/* Basic comparison. */
			sjme_unit_equalI(test, result->components[i].javaType,
				entry->components[i].javaType,
				"Field %s has incorrect Java type?", string);
			sjme_unit_equalI(test, result->components[i].cells,
				entry->components[i].cells,
				"Field %s has incorrect number of cells?", string);
			sjme_unit_equalZ(test, result->components[i].isArray,
				entry->components[i].isArray,
				"Field %s has incorrect array state?", string);
			
			/* The last entry will have the object string, if an object. */
			if (i == entry->numDims && entry->objectString != NULL)
			{
				/* Should be an object type. */
				sjme_unit_equalI(test, SJME_JAVA_TYPE_ID_OBJECT,
					result->components[i].javaType,
					"Not an object?");
				
				/* String comparison should be valid. */
				sjme_unit_equalI(test, 0, sjme_desc_compareBinaryNamePS(
					&result->components[i].binaryName,
					entry->objectString),
					"Binary name of object is incorrect?");
				
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
				sjme_unit_equalI(test, 0, sjme_desc_compareBinaryNameP(
					&result->components[i].binaryName,
					objectResult),
					"Field %s has non-equal object?", string);
			}
		}
	}
	
	/* Invalid fields. */
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("")),
		"Blank is valid?");
	
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("X")),
		"Unknown type specifier is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("ZI")),
		"Two primitive types is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("[")),
		"Blank array is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("[[")),
		"Blank double array is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("L")),
		"Only starting L is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_FIELD_TYPE,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("LOops")),
		"Object missing ending semicolon is valid?");
		
	result = NULL;
	sjme_unit_equalI(test, SJME_ERROR_INVALID_BINARY_NAME,
		sjme_desc_interpretFieldType(test->pool,
			&result, pair("L;")),
		"Empty but specified object is valid?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
