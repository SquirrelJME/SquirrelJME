/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Java Exceptions.
 *
 * @since 2022/06/04
 */

#ifndef SQUIRRELJME_JEXCEPTION_HXX
#define SQUIRRELJME_JEXCEPTION_HXX

#include <memory>
#include <cstdarg>
#include "utf.h"

/*--------------------------------------------------------------------------*/

/**
 * Represents a throwable type.
 *
 * @since 2022/06/04
 */
class sjme_jThrowable : public std::exception
{
private:
	/** The message of the exception. */
	sjme_utfString message;

	/** The cause of the exception. */
	std::shared_ptr<sjme_jThrowable> cause;
public:
	explicit sjme_jThrowable(sjme_utfString& inMessage);

	explicit sjme_jThrowable(const char* inMessage);

	sjme_jThrowable(std::shared_ptr<sjme_jThrowable>& inCause,
		sjme_utfString& inMessage);

	~sjme_jThrowable() override;
};

class sjme_jException : public sjme_jThrowable
{
public:
	explicit sjme_jException(sjme_utfString& inMessage) :
		sjme_jThrowable(inMessage)
	{
	}

	explicit sjme_jException(const char* inMessage) :
		sjme_jThrowable(inMessage)
	{
	}

	sjme_jException(std::shared_ptr<sjme_jThrowable>& inCause,
		sjme_utfString& inMessage) :
		sjme_jThrowable(inCause, inMessage)
	{
	}
};

class sjme_jRuntimeException : public sjme_jException
{
public:
	explicit sjme_jRuntimeException(sjme_utfString& inMessage) :
		sjme_jException(inMessage)
	{
	}

	explicit sjme_jRuntimeException(const char* inMessage) :
		sjme_jException(inMessage)
	{
	}

	sjme_jRuntimeException(std::shared_ptr<sjme_jThrowable>& inCause,
		sjme_utfString& inMessage) :
		sjme_jException(inCause, inMessage)
	{
	}
};

class sjme_jNullPointerException : public sjme_jRuntimeException
{
public:
	explicit sjme_jNullPointerException(sjme_utfString& inMessage) :
		sjme_jRuntimeException(inMessage)
	{
	}

	explicit sjme_jNullPointerException(const char* inMessage) :
		sjme_jRuntimeException(inMessage)
	{
	}

	sjme_jNullPointerException(std::shared_ptr<sjme_jThrowable>& inCause,
		sjme_utfString& inMessage) :
		sjme_jRuntimeException(inCause, inMessage)
	{
	}
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_JEXCEPTION_HXX */
