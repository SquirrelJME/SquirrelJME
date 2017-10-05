/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * The main entry point for Wintercoat.
 *
 * @since 2017/10/05
 */

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include "vm.h"

int main(int argc, char** argv)
{
	sjme_init initstruct;
	sjme_vm* vm;
	
	// Setup initialization structure
	memset(&initstruct, 0, sizeof(initstruct));
	initstruct.numclasspath = argc - 1;
	initstruct.classpath = argv + 1;
	
	// Initialize the virtual machine
	wc_verbose(SJME_DEBUG_INIT, "Initializing the virtual machine...", 0);
	if (wc_initvm(&initstruct, &vm) != SJME_ERROR_OK)
	{
		wc_verbose(SJME_DEBUG_INIT, "Failed to initialize the VM.", 0);
		return EXIT_FAILURE;
	}
	
	return EXIT_SUCCESS;
}

void wc_assert_real(const char* const pin, int pline, const char* const pfunc,
	const char* const pcode, int pcond)
{
	// Check condition
	if (pcond != 0)
	{
		// Print failue state
		fprintf(stderr, "WinterCoat: ASSERT %s:%d: %s, %s\n",
			(pin == NULL ? "NULL" : pin), pline,
			(pfunc == NULL ? "NULL" : pfunc),
			(pcode == NULL ? "NULL" : pcode));
		
		// Failed
		abort();
	}
}

void wc_todo_real(const char* const pin, int pline, const char* const pfunc)
{
	// Print it.
	fprintf(stderr, "WinterCoat: TODO %s:%d: %s\n", (pin == NULL ? "NULL" :
		pin), pline, (pfunc == NULL ? "NULL" : pfunc));
	
	// Never go back.
	abort();
}

void wc_verbose_real(const char* const pin, int pline,
	const char* const pfunc, int pmode,
	const char* const pmesg, ...)
{
#define BUFFER_SIZE 256
	va_list ap;
	char* buf;
	
	// Printing for this mode?
	if (1)
	{
		// Need arguments
		va_start(ap, pmesg);
		
		// Allocate buffer for string
		buf = malloc(sizeof(*buf) * BUFFER_SIZE);
		if (buf != NULL)
		{
			// Print to the buffer
			vsnprintf(buf, BUFFER_SIZE, (pmesg == NULL ? "NULL" : pmesg), ap); 
			buf[BUFFER_SIZE - 1] = '\0';
		
			// Print to output stream
			fprintf(stderr, "WinterCoat: VERBOSE %s:%d: %s [%d]: ",
				(pin == NULL ? "NULL" : pin), pline,
				(pfunc == NULL ? "NULL" : pfunc), pmode);
			fputs(buf, stderr);
			fputs("\n", stderr);
			
			// Free the buffer
			free(buf);
		}
		
		// Stop
		va_end(ap);
	}
#undef BUFFER_SIZE
}

