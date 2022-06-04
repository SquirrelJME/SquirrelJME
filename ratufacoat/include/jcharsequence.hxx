/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Java @c CharSequence.
 *
 * @since 2022/06/04
 */

#ifndef SQUIRRELJME_JCHARSEQUENCE_HXX
#define SQUIRRELJME_JCHARSEQUENCE_HXX

#include "sjmerc.h"

/*--------------------------------------------------------------------------*/

/**
 * Represents a character sequence that can be used to read and handle
 * strings accordingly.
 *
 * @since 2022/06/04
 */
class sjme_jCharSequence
{
public:
	/**
	 * Returns the character at the given index.
	 *
	 * @param index The index to get from.
	 * @return The character at the given index.
	 * @since 2022/06/04
	 */
	virtual sjme_jchar charAt(sjme_jint index) = 0;

	/**
	 * Returns the string length.
	 *
	 * @return The string length.
	 * @since 2022/06/04
	 */
	virtual sjme_jint length() = 0;

	/**
	 * Checks if this sequence equals another sequence.
	 *
	 * @param other The other sequence.
	 * @return If the sequences are equal.
	 * @since 2022/06/04
	 */
	bool operator==(sjme_jCharSequence& other);

	/**
	 * Checks if this sequence is not equal to another sequence.
	 *
	 * @param other The other sequence.
	 * @return If the sequences are not equal.
	 * @since 2022/06/04
	 */
	bool operator!=(sjme_jCharSequence& other);
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_JCHARSEQUENCE_HXX */
