// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This is the record management system which is used to store and obtain
 * records.
 *
 * Record stores must be able to be read and modified by multiple applications
 * concurrently while maintaining atomic access. Applications are permitted
 * to access records managed by other applications.
 *
 * This package is optional.
 *
 * @since 2017/02/26
 */

package javax.microedition.rms;

