/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/stdGone.h"

#if defined(SJME_CONFIG_HAS_NO_ABORT)
void abort()
{
#if defined(SJME_CONFIG_HAS_NO_EXIT)
	for (;;)
		;
#else
	exit(7);
#endif
}
#endif

#if defined(SJME_CONFIG_HAS_NO_EXIT)
void exit(int exitCode)
{
#if defined(SJME_CONFIG_HAS_NO_ABORT)
	for (;;)
		;
#else
	abort();
#endif
}
#endif

#if defined(SJME_CONFIG_HAS_NO_SNPRINTF)
int snprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	...)
{
	va_list args;
	int result;
	
	if (buf == NULL || format == NULL || bufSize <= 0)
		return -1;
	
	va_start(args, format);

	/* Perform the printing. */
#if defined(MSC_VER)
	result = _vsnprintf(buf, bufSize, format, args);
	buf[bufSize - 1] = 0;
#else
	result = vsnprintf(buf, bufSize, format, args);
#endif
	
	va_end(args);
	
	return result;
}
#endif

#if defined(SJME_CONFIG_HAS_NO_VSNPRINTF)
int vsnprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	sjme_attrInValue va_list args)
{
	int result;
	
	if (buf == NULL || format == NULL || bufSize <= 0)
		return -1;
	
	/* Perform the printing. */
#if defined(MSC_VER)
	result = _vsnprintf(buf, bufSize, format, args);
	buf[bufSize - 1] = 0;
#else
	/* TODO: Nothing we can really do here, unfortunately... */
	/* TODO: Unless I decide to write my own some day. */
	result = vsprintf(buf, format, args);
	buf[bufSize - 1] = 0;
#endif
	
	return result;
}
#endif

#if defined(SJME_CONFIG_HAS_NO_TOLOWER)
int tolower(int c)
{
	if (c >= 'A' && c <= 'Z')
		return 'a' + (c - 'A');
	return c;
}
#endif
	
#if defined(SJME_CONFIG_HAS_NO_TOUPPER)
int toupper(int c)
{
	if (c >= 'a' && c <= 'z')
		return 'A' + (c - 'a');
	return c;
}
#endif

#if defined(SJME_CONFIG_HAS_NO_STRCASECMP)
int strcasecmp(const char* a, const char* b)
{
	int diff;
	
	if (a == NULL || b == NULL)
		return -1;
	
	if (a == b)
		return 0;
	
	while ((*a) != '\0' && (*b) != '\0')
	{
		diff = tolower(toupper(*(b++))) - tolower(toupper(*(a++)));
		if (diff != 0)
			return diff;
	}
	
	/* Final comparison. */
	return tolower(toupper(*b)) - tolower(toupper(*a));
}

int strncasecmp(const char* a, const char* b, size_t n)
{
	int diff;
	size_t i;
	
	if (a == NULL || b == NULL)
		return -1;
	
	if (n == 0 || a == b)
		return 0;
	
	i = 0;
	while ((*a) != '\0' && (*b) != '\0' && (i++) < n)
	{
		diff = tolower(toupper(*(b++))) - tolower(toupper(*(a++)));
		if (diff != 0)
			return diff;
	}
	
	/* Final comparison. */
	return tolower(toupper(*b)) - tolower(toupper(*a));
}
#endif
