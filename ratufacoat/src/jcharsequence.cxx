/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "jcharsequence.hxx"

bool sjme_jCharSequence::operator==(sjme_jCharSequence& other)
{
	sjme_jint ourLen = this->length();
	sjme_jint otherLen = other.length();

	/* Different size? */
	if (ourLen != otherLen)
		return false;

	/* Compare every character. */
	for (sjme_jint i = 0; i < ourLen; i++)
		if (this->charAt(i) != other.charAt(i))
			return false;

	/* Is a match! */
	return true;
}

bool sjme_jCharSequence::operator!=(sjme_jCharSequence& other)
{
	return !(*this == other);
}
