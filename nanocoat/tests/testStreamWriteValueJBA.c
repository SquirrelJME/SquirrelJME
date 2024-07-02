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

/** Expected test data. */
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

#define WRITE_SEQ(type) \
	if (sjme_error_is(sjme_stream_outputWriteValueJP(stream, \
		SJME_TOKEN_PASTE_PP(SJME_BASIC_TYPE_ID_, type), \
		&value))) \
		sjme_unit_fail(test, "Could not write %s?", #type) \

#define STREAM_SEQ(type, structMember, what) \
	memset(&value, 0, sizeof(value)); \
	value.structMember = what; \
	WRITE_SEQ(type)

#define STREAM_SEQ2(type, hiMember, loMember, hiWhat, loWhat) \
	memset(&value, 0, sizeof(value)); \
	value.hiMember = hiWhat; \
	value.loMember = loWhat; \
	WRITE_SEQ(type)

static sjme_errorCode finishStreamWriteValueJBA(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_resultByteArray* result)
{
	sjme_jint i;
	sjme_test* test;

	/* Recover test. */
	test = (sjme_test*)result->whatever;

	/* The write count should be the buffer size. */
	sjme_unit_equalI(test,
		DATA_LEN, result->length,
		"Number of written bytes incorrect?");

	/* All the buffer bytes should match. */
	sjme_unit_equalI(test,
		0, memcmp(result->array, testData, result->length),
		"Written buffer does not match?");

	/* Success! */
	return SJME_ERROR_NONE;
}

/**
 * Tests writing Java values to the output.
 *  
 * @since 2024/01/09 
 */
SJME_TEST_DECLARE(testStreamWriteValueJBA)
{
	sjme_jvalue value;
	sjme_stream_output stream;
	void* buf;

	/* Setup buffer to write to. */
	buf = sjme_alloca(DATA_LEN);
	if (buf == NULL)
		return sjme_unit_fail(test, "Could not output buffer.");

	/* Clear buffer. */
	memset(buf, 0, DATA_LEN);

	/* Open stream to write all the data in. */
	if (sjme_error_is(sjme_stream_outputOpenByteArray(test->pool,
		&stream, 2, finishStreamWriteValueJBA,
		test)) || stream == NULL)
		return sjme_unit_fail(test, "Could not open output stream.");

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

	/* Close stream. */
	if (sjme_error_is(sjme_stream_outputClose(stream, NULL)))
		return sjme_unit_fail(test, "Could not close output stream.");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
