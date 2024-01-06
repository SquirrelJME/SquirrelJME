/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

#define DATA_LEN 58

/** Input test data. */
static const sjme_jubyte testData[DATA_LEN] =
{
	0x00, 0x01, 0x04, 0xD2, 0x11, 0xD7, 0x00, 0x73,
	0x00, 0x71, 0x00, 0xBC, 0x61, 0x4E, 0x05,
	0x39, 0x7F, 0xB1, 0x00, 0x00, 0x00, 0x02,
	0xDF, 0xDC, 0x1C, 0x35, 0x00, 0x00, 0x00,
	0x02, 0x8E, 0xEA, 0x4C, 0xB1, 0x12, 0x34,
	0x56, 0x78, 0x87, 0x65, 0x43, 0x21, 0x12,
	0x34, 0x56, 0x78, 0x12, 0x34, 0x56, 0x78,
	0x87, 0x65, 0x43, 0x21, 0x87, 0x65, 0x43,
	0x21
};

/**
 * Tests reading of Java values from a stream.
 *  
 * @since 2024/01/05 
 */
SJME_TEST_DECLARE(testStreamReadValueJ)
{
	sjme_jvalue value;
	
	/* dos.writeBoolean(false); */
	/* dos.writeBoolean(true); */
	/* dos.writeShort(1234); */
	/* dos.writeShort(4567); */
	/* dos.writeChar('s'); */
	/* dos.writeChar('q'); */
	/* dos.writeInt(12345678); */
	/* dos.writeInt(87654321); */
	/* dos.writeLong(12345678901L); */
	/* dos.writeLong(10987654321L); */
	/* dos.writeFloat(Float.intBitsToFloat(0x12345678)); */
	/* dos.writeFloat(Float.intBitsToFloat(0x87654321)); */
	/* dos.writeDouble(Double.longBitsToDouble(0x1234567812345678L)); */
	/* dos.writeDouble(Double.longBitsToDouble(0x8765432187654321L)); */

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
