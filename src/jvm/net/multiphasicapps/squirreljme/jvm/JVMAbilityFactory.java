// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jvm;

/**
 * This is a factory class which is used to provide the JVM the ability to
 * perform actions such as interacting with the user or loading classes.
 *
 * This is an abstract class so that if in the event an ability is not
 * implemented there can be default fall-back behavior ability interfaces used
 * instead.
 *
 * It is recommended that the methods in this class not be {@code final} and
 * that consideration be made that classes in the ability factory heirarchy
 * can be replaced and extended upon. For example, suppose that there is an
 * ability factory for operating system "Foo". Perhaps "Foo" is incapable of
 * performing specific actions. Then in the future there is a "Foo" comapatible
 * OS with specific extensions and is called "Foo 2". Instead of writing a
 * completely new interface for "Foo 2", instead "Foo 2" can extend the
 * ability factory of "Foo" to provide slightly altered and new functionality.
 *
 * @since 2016/06/24
 */
public abstract class JVMAbilityFactory
{
}

