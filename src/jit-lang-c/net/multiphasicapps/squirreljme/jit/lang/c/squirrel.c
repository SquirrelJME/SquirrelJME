/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * This acts as the main entry point for C generated output.
 *
 * @since 2016/07/20
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "squirrel.h"

#define MAIN_CLASS "net/multiphasicapps/squirreljme/os/lang/c/CMain"

/**
 * Main program entry point.
 *
 * @param __argc Argument count.
 * @param __args Argument set.
 * @since 2016/07/20
 */
int main(int __argc, char** __argv)
{
	SJME_VM jvm;
	const SJME_Class* cmain;
	
	// Setup base VM structure
	memset(&jvm, 0, sizeof(jvm));
	jvm.structuretype = SJME_STRUCTURETYPE_VM;
	
	// Count the number of namespaces to initialize at once
	jvm.namespaces = &initialNamespaces;
	for (jvm.namespacecount = 0;; jvm.namespacecount++)
		if (jvm.namespaces->namespaces[jvm.namespacecount] == NULL)
			break;
	
	// Find the starting class
	cmain = SJME_locateClassDefC(&jvm, MAIN_CLASS);
	if (cmain == NULL)
	{
		fprintf(stderr, "Could not find the main class `%s`.\n", MAIN_CLASS);
		abort();
		return -1;
	}
	
	// Not yet implemented
	abort();
}

/**
 * Compares a C string to a VM string.
 *
 * @param __a The C string.
 * @param __b The VM string.
 * @return The comparison.
 * @since 2016/07/21
 */
jint SJME_compareCStringToSJMEString(const char* const __a,
	const SJME_String* const __b)
{
	abort();
	return -1;
}

/**
 * Compares a VM string to a C string.
 *
 * @param __a The VM string.
 * @param __b The C string.
 * @return The comparison.
 * @since 2016/07/21
 */
jint SJME_compareSJMEStringToCString(const SJME_String* const __a,
	const char* const __b)
{
	// Same as above, but a reversed operation
	return -SJME_compareCStringToSJMEString(__b, __a);
}

/**
 * {@inheritDoc}
 * @since 2016/07/21
 */
const SJME_Class* SJME_locateClassDefC(SJME_VM* __vm, const char* const __s)
{
	jint i, n, j;
	const SJME_Namespace* ns;
	const SJME_Class* cl;
	const void* unkcont;
	
	// Check
	if (__vm == NULL || __s == NULL)
	{
		abort();
		return NULL;
	}
	
	// Go through all namespaces
	n = __vm->namespacecount;
	for (i = 0; i < n; i++)
	{
		// Get
		ns = __vm->namespaces->namespaces[i];
		
		// Go through all contents
		for (j = 0;; j++)
		{
			unkcont = ns->contents->contents[j];
			
			// Stop on NULL
			if (unkcont == NULL)
				break;
			
			// Only consider classes
			if ((*((SJME_StructureType*)unkcont)) == SJME_STRUCTURETYPE_CLASS)
			{
				// Cast
				cl = (const SJME_Class*)unkcont;
				
				// Is this name?
				if (0 == SJME_compareCStringToSJMEString(__s, cl->name))
					return cl;
			}
		}
	}
	
	// Not found
	return NULL;
}

