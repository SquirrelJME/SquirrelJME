/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"
#include "sjme/stream.h"

#define DATA_LEN 58

/** Input test data. */
static const sjme_jubyte testData[DATA_LEN] =
{
	0x00, 0x01, 0x04, 0xD2, 0x11, 0xD7, 0x00,
	0x73, 0x00, 0x71, 0x00, 0xBC, 0x61, 0x4E,
	0x05, 0x39, 0x7F, 0xB1, 0x12, 0x34, 0x56,
	0x78, 0x12, 0x34, 0x56, 0x78, 0x87, 0x65,
	0x43, 0x21, 0x87, 0x65, 0x43, 0x21, 0x12,
	0x34, 0x56, 0x78, 0x87, 0x65, 0x43, 0x21,
	0x12, 0x34, 0x56, 0x78, 0x12, 0x34, 0x56,
	0x78, 0x87, 0x65, 0x43, 0x21, 0x87, 0x65,
	0x43, 0x21
};

#define READ_SEQ(type) \
	memset(&value, 0, sizeof(value)); \
	if (sjme_error_is(sjme_stream_inputReadValueJ(stream, \
		SJME_TOKEN_PASTE_PP(SJME_BASIC_TYPE_ID_, type), \
		&value))) \
		sjme_unit_fail(test, "Could not read %s?", #type) \

#define STREAM_SEQ(type, structMember, expected) \
	READ_SEQ(type); \
	sjme_unit_equalI(test, value.structMember, (expected), \
		"Incorrect value for %s?", #type)

#define STREAM_SEQ2(type, hiMember, loMember, hiExpected, loExpected) \
	READ_SEQ(type); \
	sjme_unit_equalI(test, value.hiMember, (hiExpected), \
		"Incorrect hi value for %s?", #type); \
	sjme_unit_equalI(test, value.loMember, (loExpected), \
		"Incorrect lo value for %s?", #type)

/**
 * Tests reading of Java values from a stream.
 *  
 * @since 2024/01/05 
 */
SJME_TEST_DECLARE(testStreamReadValueJ)
{
	sjme_jvalue value;
	sjme_stream_input stream;
	sjme_jint single;

	/* Open stream to the raw binary data. */
	stream = NULL;
	if (sjme_error_is(sjme_stream_inputOpenMemory(test->pool,
		&stream, testData, DATA_LEN)) ||
		stream == NULL)
		return sjme_unit_fail(test, "Could not open initial stream.");

	/* dos.writeBoolean(false); */
	STREAM_SEQ(BOOLEAN, z, SJME_JNI_FALSE);

	/* dos.writeBoolean(true); */
	STREAM_SEQ(BOOLEAN, z, SJME_JNI_TRUE);

	/* dos.writeShort(1234); */
	STREAM_SEQ(SHORT, s, 1234);

	/* dos.writeShort(4567); */
	STREAM_SEQ(SHORT, s, 4567);

	/* dos.writeChar('s'); */
	STREAM_SEQ(CHARACTER, c, 's');

	/* dos.writeChar('q'); */
	STREAM_SEQ(CHARACTER, c, 'q');

	/* dos.writeInt(12345678); */
	STREAM_SEQ(INTEGER, i, 12345678);

	/* dos.writeInt(87654321); */
	STREAM_SEQ(INTEGER, i, 87654321);

	/* dos.writeLong(0x1234567812345678L); */
	STREAM_SEQ2(LONG, j.part.hi, j.part.lo, 0x12345678, 0x12345678);

	/* dos.writeLong(0x8765432187654321L); */
	STREAM_SEQ2(LONG, j.part.hi, j.part.lo, 0x87654321, 0x87654321);

	/* dos.writeFloat(Float.intBitsToFloat(0x12345678)); */
	STREAM_SEQ(FLOAT, f.value, 0x12345678);

	/* dos.writeFloat(Float.intBitsToFloat(0x87654321)); */
	STREAM_SEQ(FLOAT, f.value, 0x87654321);

	/* dos.writeDouble(Double.longBitsToDouble(0x1234567812345678L)); */
	STREAM_SEQ2(DOUBLE, d.hi, d.lo, 0x12345678, 0x12345678);

	/* dos.writeDouble(Double.longBitsToDouble(0x8765432187654321L)); */
	STREAM_SEQ2(DOUBLE, d.hi, d.lo, 0x87654321, 0x87654321);

	/* EOF. */
	single = -2;
	if (sjme_error_is(sjme_stream_inputReadSingle(stream,
		&single)) || single < -1)
		return sjme_unit_fail(test, "Could not read final byte?");

	/* Must be end of stream. */
	sjme_unit_equalI(test, single, -1,
		"End of stream not reached?");

	/* Close the stream. */
	if (sjme_error_is(sjme_stream_inputClose(stream)))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
