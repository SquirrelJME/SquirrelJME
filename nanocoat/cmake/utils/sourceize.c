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

static void makeC(const char* properName)
{
	char efficientSym[256][EFFICIENT_SYM_SIZE];
	char quickBuf[EFFICIENT_SYM_SIZE];
	uint8_t buf[BUF_SIZE];
	uint8_t d;
	int readCount, i, totalSize, charCol;
	const char* uChar;
	
	/* Calculate the most efficient symbol that can be used. */
	for (i = 0; i < 256; i++)
	{
		/* Reset. */
		memset(efficientSym[i], 0, sizeof(efficientSym[i]));
		
		/* Large numbers are treated as unsigned for certain compilers. */
		uChar = (i >= 128 ? "U" : "");
		
		/* Start with normal number. */
		snprintf(efficientSym[i], EFFICIENT_SYM_SIZE - 1,
			"%d%s", i, uChar);
		
		/* Is octal shorter? */
		memset(quickBuf, 0, sizeof(quickBuf));
		snprintf(quickBuf, EFFICIENT_SYM_SIZE - 1,
			"0%o%s", i, uChar);
		if (strlen(quickBuf) < strlen(efficientSym[i]))
			memmove(efficientSym[i], quickBuf, EFFICIENT_SYM_SIZE);
		
		/* Is hex shorter? */
		memset(quickBuf, 0, sizeof(quickBuf));
		snprintf(quickBuf, EFFICIENT_SYM_SIZE - 1,
			"0x%x%s", i, uChar);
		if (strlen(quickBuf) < strlen(efficientSym[i]))
			memmove(efficientSym[i], quickBuf, EFFICIENT_SYM_SIZE);
	}
	
	/* Start header. */
	fprintf(stdout, "#include <sjme/stdTypes.h>\n");
	fprintf(stdout, "const sjme_jubyte %s__bin[] = {\n",
		properName);
	
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
	fprintf(stdout, "const sjme_jint %s__len = %d;\n",
		properName, totalSize);
}

static void makeH(const char* properName)
{
	fprintf(stdout, "#include <sjme/stdTypes.h>\n");
	fprintf(stdout, "extern const sjme_jubyte %s__bin[];\n",
		properName);
	fprintf(stdout, "extern const sjme_jint %s__len;\n",
		properName);
}

int main(int argc, char** argv)
{
	char c;
	char properName[BUF_SIZE];
	int i;
	
	/* Not enough arguments? */
	if (argc <= 2)
	{
		fprintf(stderr, "Usage: %s fileName C|H\n", argv[0]);
		return EXIT_FAILURE;
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
	
	/* Which kind of output is used? */
	if (0 == strcmp(argv[2], "C"))
		makeC(properName);
	else if (0 == strcmp(argv[2], "H"))
		makeH(properName);
	else
	{
		fprintf(stderr, "Invalid file type: %s.\n", argv[2]);
		return EXIT_FAILURE;
	}
	
	/* Make sure output is written. */
	fflush(stdout);
	
	/* Success! */
	return EXIT_SUCCESS;
}
