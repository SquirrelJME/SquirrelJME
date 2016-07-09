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
 * This package contains classes which are used to write to ZIP files in a
 * stream based fashion. Entries are written one at a time and are directly
 * placed in the output stream. When the ZIP is closed the final index is
 * written.
 *
 * @since 2016/07/09
 */

package net.multiphasicapps.zip.streamwriter;

