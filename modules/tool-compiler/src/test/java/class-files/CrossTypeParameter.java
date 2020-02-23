// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This is used to test where a type parameter is used for another type
 * parameter.
 *
 * @param <T> Type A.
 * @param <E> Type B.
 * @since 2018/05/10
 */
public class CrossTypeParameter<T extends List<E>, E extends List<T>>
{
}

