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

sjme_jThrowable::sjme_jThrowable(sjme_utfString& inMessage) :
	message(inMessage), cause(nullptr)
{
}

sjme_jThrowable::sjme_jThrowable(const char* const inMessage) :
	message(inMessage), cause(nullptr)
{
}

sjme_jThrowable::sjme_jThrowable(std::shared_ptr<sjme_jThrowable>& inCause,
	sjme_utfString& inMessage) : message(inMessage), cause(inCause)
{
}

sjme_jThrowable::~sjme_jThrowable() = default;
