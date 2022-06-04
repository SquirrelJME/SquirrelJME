/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>

#include "debug.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

sjme_ToDoException::sjme_ToDoException(const char* file, int line,
	const char* func, const char* message)
{
	this->file = file;
	this->line = line;
	this->func = func;
	this->message = (message == NULL ? NULL : strdup(message));
}

sjme_ToDoException::~sjme_ToDoException() noexcept
{
	if (this->message != NULL)
	{
		free((void*)this->message);
		this->message = NULL;
	}
}

void sjme_messageR(const char* file, int line,
	const char* func, const char* message, ...)
{
	char buf[DEBUG_BUF];
	va_list args;

	/* Load message buffer. */
	va_start(args, message);
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, DEBUG_BUF, message, args);
	va_end(args);

	/* Print output message. */
	if (file != NULL || line > 0 || func != NULL)
		fprintf(stderr, "DB: (%s:%d in %s()): %s\n",
			file, line, func, buf);
	else
		fprintf(stderr, "DB: %s\n",
			buf);
}

sjme_returnNever sjme_todoR(const char* file, int line,
	const char* func, const char* message, ...)
{
	char buf[DEBUG_BUF];
	va_list args;

	/* Load message buffer. */
	va_start(args, message);
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, DEBUG_BUF, message, args);
	va_end(args);

	/* Print output message. */
	if (file != NULL || line > 0 || func != NULL)
		fprintf(stderr, "TD: TODO Hit (%s:%d in %s()): %s\n",
			file, line, func, buf);
	else
		fprintf(stderr, "TD: TODO Hit: %s\n",
			buf);

	/* Throw exception. */
	throw sjme_ToDoException(file, line, func, buf);
}
