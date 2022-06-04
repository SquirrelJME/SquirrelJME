/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "jexception.hxx"
#include "memory.h"
#include "utf.h"

sjme_utfString::sjme_utfString(void* memAddr, intptr_t memOffset)
{
	sjme_todo("Implement this?");
}

sjme_utfString::sjme_utfString(const char* text)
{
	if (format == nullptr)
		throw sjme_jNullPointerException(nullptr);

	sjme_todo("Implement this?");
}

sjme_utfString::sjme_utfString(const sjme_utfString& other)
{
	sjme_todo("Implement this?");
}

sjme_utfString::~sjme_utfString()
{
	if (this->wasAllocated)
		sjme_free(this->rawData, nullptr);
	this->rawData = nullptr;
}

sjme_jchar sjme_utfString::charAt(sjme_jint index)
{
	sjme_todo("Implement this?");
	return 0;
}

sjme_jint sjme_utfString::length()
{
	sjme_todo("Implement this?");
	return 0;
}

sjme_utfString sjme_utfString::format(const char* format, ...)
{
	sjme_todo("Implement this?");
	return sjme_utfString(nullptr);
}
