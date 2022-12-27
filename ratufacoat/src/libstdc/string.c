/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stddef.h>
#include <stdint.h>
#include <string.h>

int memcmp(const void* a, const void* b, size_t size)
{
	size_t i;
	int result;

	/* Always the same. */
	if (a == b)
		return 0;

	for (i = 0; i < size; i++)
	{
		result = ((uint8_t*)a)[i] - ((uint8_t*)b)[i];
		if (result != 0)
			return result;
	}

	/* No different. */
	return 0;
}

void* memcpy(void* dest, const void* src, size_t size)
{
	/* Just call the other variant. */
	return memmove(dest, src, size);
}

void* memmove(void* dest, const void* src, size_t size)
{
	size_t i;

	/* Pointless copy. */
	if (src == dest)
		return dest;

	/* Copy from end. */
	if (src < dest)
	{
		for (i = size; i > 0; i--)
			((uint8_t*)dest)[i - 1] = ((uint8_t*)src)[i - 1];
	}

	/* Copy from start. */
	else
	{
		for (i = 0; i < size; i++)
			((uint8_t*)dest)[i] = ((uint8_t*)src)[i];
	}
}

void* memset(void* dest, int value, size_t size)
{
	size_t i;

	for (i = 0; i < size; i++)
		((uint8_t*)dest)[i] = value;

	return dest;
}

int strcmp(const char* a, const char* b)
{
	size_t lenA, lenB, minLen;
	int cmp;

	/* Determine minimum string length. */
	lenA = strlen(a);
	lenB = strlen(b);
	minLen = (lenA < lenB ? lenA : lenB);

	/* Compare the memory within. */
	cmp = memcmp(a, b, minLen);

	/* If strings are the same size, use the given comparison. */
	/* Or the string are not equal anyway despite so. */
	if (lenA == lenB || cmp != 0)
		return cmp;

	/* Otherwise, use the comparison of their lengths. */
	return lenA - lenB;
}

size_t strlen(const char* str)
{
	size_t at;

	for (at = 0; str[at] != '\0'; at++)
		if (str[at] == '\0')
			return at;

	return at;
}

