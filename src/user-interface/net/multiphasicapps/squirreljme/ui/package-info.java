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
 * This package provides the base user interface code which must be implemented
 * by rendering systems or other native means to provide an interactive
 * environment for the user.
 *
 * It is used by the launcher (to make launching programs easier), the console
 * output interface (to show the stdout/stderr of programs), and the MIDP LCD
 * UI using standard interfaces.
 *
 * The entire user interface shares a lock.
 *
 * @since 2016/05/20
 */

package net.multiphasicapps.squirreljme.ui;

