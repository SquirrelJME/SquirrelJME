// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit.powerpc;

import net.multiphasicapps.squirreljme.ssjit.SSJITProducerFactory;

/**
 * This is a variant of a PowerPC CPU.
 *
 * This is an interface because third party libraries could add support for
 * new variants.
 *
 * @since 2016/06/25
 */
public interface PowerPCVariant
	extends SSJITProducerFactory.Variant
{
}

