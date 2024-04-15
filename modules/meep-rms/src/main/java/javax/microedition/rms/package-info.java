// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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

