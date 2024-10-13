/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <stdio.h>

int whatever(char* format, ...)
{
	char buf[256];
	va_list args;
	int result;
	
	va_start(args, format);
	
	result = vsnprintf(buf, 256, "Squeak!", args);
	
	va_end(args);
	
	return result;
}

int main(int argc, char** argv)
{
	whatever("Squeak!", argc, argv);
	return 0;
}
