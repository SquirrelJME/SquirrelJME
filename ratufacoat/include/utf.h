/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Modified UTF String format.
 * 
 * @since 2022/02/26
 */

#ifndef SQUIRRELJME_UTF_H
#define SQUIRRELJME_UTF_H

#include "sjmerc.h"
#include "error.h"
#include "jcharsequence.hxx"

/*--------------------------------------------------------------------------*/

/**
 * This represents a Java modified UTF-8 String, it points somewhere into
 * memory.
 * 
 * @since 2022/02/26
 */
class sjme_utfString : public sjme_jCharSequence
{
private:
	/** Raw UTF pointer data. */
	void* rawData;
	
	/** Was this string allocated in memory, or does it point to one? */
	bool wasAllocated;
	
public:
	sjme_utfString(const sjme_utfString& other);
	
	sjme_utfString(void* memAddr, intptr_t memOffset);
	
	explicit sjme_utfString(const char* text);
	
	~sjme_utfString();
	
	sjme_jchar charAt(sjme_jint index) override;
	
	sjme_jint length() override;
	
	static sjme_utfString format(const char* format, ...);
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_UTF_H */
