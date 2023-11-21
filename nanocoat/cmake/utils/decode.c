/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>

#if defined(_MSC_VER)
	typedef unsigned __int8 uint8_t;
#else
	#include <stdint.h>
#endif

#define BUF_SIZE 1024

/**
 * Main entry point.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return Returns @c 0 on success.
 * @since 2023/11/21
 */
int main(int argc, char** argv)
{
	uint8_t buf[BUF_SIZE];
	uint8_t out;
	int len, i, c, of, rawBit;
	
	/* Constant buffer reading loop. */
	for (of = 0;;)
	{
		/* Read in. */
		len = fread(buf, sizeof(buf[0]), BUF_SIZE, stdin);
		
		/* EOF? */
		if (len <= 0 && feof(stdin))
			break;
		
		/* Go through and handle characters accordingly. */
		for (i = 0; i < len; i++)
		{
			/* Read in next character. */
			c = buf[i];
			
			/* How is it mapped? */
			if (c >= 'a' && c <= 'f')
				rawBit = (c - 'a') + 10;
			else if (c >= 'A' && c <= 'F')
				rawBit = (c - 'A') + 10;
			else if (c >= '0' && c <= '9')
				rawBit = c - '0';
				
			/* Invalid character? Ignore. */
			else
				continue;
			
			/* How is this output? */
			if ((of++) == 0)
				out = (rawBit << 4);
			else
			{
				out |= rawBit;
				
				/* Output. */
				fwrite(&out, sizeof(char), 1, stdout);
				
				/* Reset. */
				of = 0;
			}
		}
	}
	
	/* Flush output before stopping. */
	fflush(stdout);
	
	/* Okay! */
	return EXIT_SUCCESS;
}
