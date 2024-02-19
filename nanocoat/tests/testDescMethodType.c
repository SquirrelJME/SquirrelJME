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

/**
 * Tests parsing of method descriptors.
 *  
 * @since 2024/01/01
 */
SJME_TEST_DECLARE(testDescMethodType)
{
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
