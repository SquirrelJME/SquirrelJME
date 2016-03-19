// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

/**
 * This package contains the common cache manager which is used to initialize
 * values which are stored in a cache.
 *
 * The main purpose of this is to reduce a commonly copied and pasted set of
 * code into one that is much smaller. The initialization is done in a separate
 * class (with package private visibility) to enable access to hidden details.
 *
 * This is done without reflection or lambdas and can run on Java ME.
 *
 * It also permits in specific situations the initializor to be garbage
 * collected when it is no longer needed.
 *
 * @since 2016/03/19
 */

package net.multiphasicapps.cache;

