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

/** Buffer size. */
#define BUF_SIZE 4096
	
/** Base64 alphabet size. */
#define BASE64_ALPHABET_SIZE 64
	
/** Padding sequence character. */
static const uint8_t base64Padding = '=';
	
/** The Basic and MIME alphabet */
static const uint8_t base64ToAlpha[BASE64_ALPHABET_SIZE] = {
	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
	'J', 'K', 'L', 'M',
	'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
	'V', 'W', 'X', 'Y', 'Z',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'i', 'j', 'k', 'l', 'm',
	'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
	'v', 'w', 'x', 'y', 'z',
	'0', '1', '2', '3', '4', '5', '6', '7',
	'8', '9', '+', '/'
};

/** Invalid bit field. */
#define INVALID_BITS -1

/** Alpha to bit mapping. */
static int32_t alphaToBits[256];

int decodeAsBase64(void)
{
	uint8_t buf[BUF_SIZE];
	uint8_t out[3];
	int i, c, at, d, col, hitPadding;
	int32_t bits, numBits, symbol;
	
	/* Set all alpha bits to the max value for invalidness. */
	for (i = 0; i < 256; i++)
		alphaToBits[i] = INVALID_BITS;
	
	/* Initialize alphabet to bit mapping. */
	for (i = 0; i < BASE64_ALPHABET_SIZE; i++)
		alphaToBits[base64ToAlpha[i]] = i;
	
	/* Read in first line until the newline. */
	memset(buf, 0, sizeof(buf));
	for (at = 0;;)
	{
		/* Read in character. */
		c = fgetc(stdin);
		
		/* This should not happen. */
		if (c == EOF)
		{
			fprintf(stderr, "Too early EOF!\n");
			return EXIT_FAILURE;
		}
		
		/* If end of line, stop. */
		if (c == '\r' || c == '\n')
			break;
		
		/* Set into the buffer. */
		buf[at++] = c;
	}
	
	/* Only check the front end. */
	buf[13] = 0;
	if (0 != strcmp("begin-base64 ", buf))
	{
		fprintf(stderr, "Does not appear to be a MIME file!\n");
		return EXIT_FAILURE;
	}
	
	/* Begin decoding sequence, character by character. */
	col = 0;
	hitPadding = 0;
	for (bits = 0, numBits = 0;;)
	{
		/* Read in character. */
		c = fgetc(stdin);
		
		/* This should not happen. */
		if (c == EOF)
		{
			fprintf(stderr, "Too early EOF!\n");
			return EXIT_FAILURE;
		}
		
		/* Reset column? */
		if (c == '\r' || c == '\n')
		{
			col = 0;
			continue;
		}
		
		/* Otherwise increment column. */
		else
			col++;
		
		/* Determine the symbol bit meaning, ignore unknowns. */
		symbol = alphaToBits[c & 0xFF];
		if (symbol == INVALID_BITS)
		{
			/* Special handling with padding character. */
			if (c == base64Padding)
			{
				/* Already hit padding and we are on the first column? */
				/* Stop decoding. */
				if (hitPadding && col <= 1)
					break;
				
				/* If we are on the first column, and we are the correct */
				/* number of output bytes, then stop decoding. */
				fprintf(stderr, "left over: %d %d\n",
					col, numBits);
				if (col <= 1 && numBits == 0)
					break;
				
				/* We hit padding. */
				hitPadding = 1;
				
				/* Get next padding character. */
				d = fgetc(stdin);
				
				/* Single character? */
				if (c == base64Padding && d != base64Padding)
				{
					out[0] = (bits >> 10) & 0xFF;
					out[1] = (bits >> 2) & 0xFF;
					fwrite(out, 2, 1, stdout);
					
					break;
				}
				
				/* Double character? */
				else if (c == base64Padding && d == base64Padding)
				{
					out[0] = (bits >> 4) & 0xFF;
					fwrite(out, 1, 1, stdout);
					
					break;
				}
			}
			
			/* Skip invalid characters otherwise. */
			continue;
		}
		
		/* Shift in bits to the buffer. */
		bits <<= 6;
		bits |= symbol;
		numBits += 6;
		
		/* Enough bits to decode everything? */
		if (numBits == 24)
		{
			/* Get bits to place. */
			out[0] = (bits >> 16) & 0xFF;
			out[1] = (bits >> 8) & 0xFF;
			out[2] = (bits) & 0xFF;
			
			/* Output them all. */
			fwrite(&out, 1, 3, stdout);
			
			/* Remove bits. */
			bits = 0;
			numBits = 0;
		}
	}
	
	/* Flush output before stopping. */
	fflush(stdout);
	
	/* All done! */
	return EXIT_SUCCESS;
}

int decodeAsHex(void)
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
	/* Needs HEX or BASE64. */
	if (argc <= 1)
	{
		fprintf(stderr, "Usage: %s BASE64|HEX\n", argv[0]);
		return EXIT_FAILURE;
	}
	
	/* Which decoder to use? */
	if (0 == strcmp(argv[1], "BASE64"))
		return decodeAsBase64();
	else if (0 == strcmp(argv[1], "HEX"))
		return decodeAsHex();
	
	/* Wrong argument! */
	fprintf(stderr, "Unknown decode type: %s.\n", argv[1]);
	return EXIT_FAILURE;
}
