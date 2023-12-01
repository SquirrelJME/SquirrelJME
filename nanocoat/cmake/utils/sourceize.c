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
#include <string.h>

#include "sjmeint.h"

#define BUF_SIZE 4096
#define EFFICIENT_SYM_SIZE 8

int main(int argc, char** argv)
{
	char efficientSym[256][EFFICIENT_SYM_SIZE];
	char properName[BUF_SIZE];
	char c;
	uint8_t buf[BUF_SIZE];
	uint8_t d;
	int readCount, i, totalSize, charCol;
	
	/* Not enough arguments? */
	if (argc <= 1)
	{
		fprintf(stderr, "Usage: %s fileName\n", argv[0]);
		return EXIT_FAILURE;
	}
	
	/* Calculate the most efficient symbol that can be used. */
	for (i = 0; i < 256; i++)
	{
		/* Reset. */
		memset(efficientSym[i], 0, sizeof(efficientSym[i]));
		
		/* Start with normal number. */
		snprintf(efficientSym[i], EFFICIENT_SYM_SIZE - 1, "%d", i);
		
		/* Is octal shorter? */
		memset(properName, 0, sizeof(properName));
		snprintf(properName, BUF_SIZE - 1, "0%o", i);
		if (strlen(properName) < strlen(efficientSym[i]))
			memmove(efficientSym[i], properName, EFFICIENT_SYM_SIZE);
		
		/* Is hex shorter? */
		memset(properName, 0, sizeof(properName));
		snprintf(properName, BUF_SIZE - 1, "0x%x", i);
		if (strlen(properName) < strlen(efficientSym[i]))
			memmove(efficientSym[i], properName, EFFICIENT_SYM_SIZE);
	}
	
	/* Copy the file name. */
	memset(properName, 0, sizeof(properName));
	snprintf(properName, BUF_SIZE - 1, "%s", argv[1]);
	
	/* Normalize all characters accordingly. */
	for (i = 0; i < BUF_SIZE; i++)
	{
		/* Which character? */
		c = properName[i];
		if (c == 0)
			break;
		
		/* Lowercase it. */
		if (c >= 'A' && c <= 'Z')
			properName[i] = 'a' + (c - 'A');
		
		/* Invalid C identifier characters. */
		else if (!((c >= 'a' && c <= 'z') || (i > 0 && c >= '0' && c <= '9')))
			properName[i] = '_';
	}
	
	/* Start header. */
	fprintf(stdout, "#include <sjme/nvm.h>\n");
	fprintf(stdout, "static const uint8_t %s__bin[] = {\n", properName);
	
	/* Process all the bytes. */
	totalSize = 0;
	charCol = 0;
	for (;;)
	{
		/* Read in. */
		readCount = fread(buf, 1, BUF_SIZE, stdin);
		
		/* EOF? */
		if (readCount <= 0 && feof(stdin))
			break;
		
		/* Process everything. */
		for (i = 0; i < readCount; i++)
		{
			/* Comma? */
			if (totalSize > 0)
			{
				fprintf(stdout, ",");
				charCol++;
			}
			
			/* Encode to stream, efficiently. */
			d = buf[i];
			fprintf(stdout, "%s", efficientSym[d & 0xFF]);
			
			/* Count size up. */
			totalSize++;
			
			/* Newline for more space. */
			charCol += strlen(efficientSym[d & 0xFF]);
			if (charCol > 60)
			{
				fprintf(stdout, "\n");
				charCol = 0;
			}
		}
	}
	
	/* Finish it off and write the size. */
	fprintf(stdout, "};\n");
	fprintf(stdout, "const uint32_t %s__len = %d;\n", properName, totalSize);
	
	/* Make sure output is written. */
	fflush(stdout);
	
	/* Success! */
	return EXIT_SUCCESS;
}
