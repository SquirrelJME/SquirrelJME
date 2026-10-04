// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

/**
 * Helper interface which implements all serializers.
 *
 * @since 2023/09/12
 */
public interface CsvDeserializerSerializer<T>
	extends CsvDeserializer<T>, CsvSerializer<T>
{
}
