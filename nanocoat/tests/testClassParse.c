/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/classy.h"
#include <string.h>

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

#include "classes.zip.h"

typedef sjme_errorCode (*testRunFunc)(sjme_test* test,
	sjme_class_info info);

typedef struct testClassInfo
{
	sjme_lpcstr fileName;
	
	sjme_lpcstr binaryName;
	
	testRunFunc testRun;
} testClassInfo;

#define TRY_CLASS(what, useFunc) \
	{ \
		SJME_TOKEN_STRING(what) ".class", \
		"java/lang/" SJME_TOKEN_STRING(what), \
		useFunc, \
	}

static const testClassInfo testClassInfos[] =
{
	TRY_CLASS(Appendable, NULL),
	TRY_CLASS(ArithmeticException, NULL),
	TRY_CLASS(ArrayIndexOutOfBoundsException, NULL),
	TRY_CLASS(ArrayStoreException, NULL),
	TRY_CLASS(AssertionError, NULL),
	TRY_CLASS(AutoCloseable, NULL),
	TRY_CLASS(Boolean, NULL),
	TRY_CLASS(Byte, NULL),
	TRY_CLASS(__CanSetPrintStream__, NULL),
	TRY_CLASS(Character, NULL),
	TRY_CLASS(CharSequence, NULL),
	TRY_CLASS(ClassCastException, NULL),
	TRY_CLASS(Class, NULL),
	TRY_CLASS(ClassFormatError, NULL),
	TRY_CLASS(ClassNotFoundException, NULL),
	TRY_CLASS(Cloneable, NULL),
	TRY_CLASS(CloneNotSupportedException, NULL),
	TRY_CLASS(Comparable, NULL),
	TRY_CLASS(Deprecated, NULL),
	TRY_CLASS(Double, NULL),
	TRY_CLASS(Enum, NULL),
	TRY_CLASS(Error, NULL),
	TRY_CLASS(Exception, NULL),
	TRY_CLASS(Float, NULL),
	TRY_CLASS(FunctionalInterface, NULL),
	TRY_CLASS(IllegalAccessException, NULL),
	TRY_CLASS(IllegalArgumentException, NULL),
	TRY_CLASS(IllegalMonitorStateException, NULL),
	TRY_CLASS(IllegalStateException, NULL),
	TRY_CLASS(IllegalThreadStateException, NULL),
	TRY_CLASS(IncompatibleClassChangeError, NULL),
	TRY_CLASS(IndexOutOfBoundsException, NULL),
	TRY_CLASS(InstantiationException, NULL),
	TRY_CLASS(Integer, NULL),
	TRY_CLASS(__InternMini__$__InternMicro__, NULL),
	TRY_CLASS(__InternMini__, NULL),
	TRY_CLASS(InterruptedException, NULL),
	TRY_CLASS(Iterable, NULL),
	TRY_CLASS(LinkageError, NULL),
	TRY_CLASS(Long, NULL),
	TRY_CLASS(Math, NULL),
	TRY_CLASS(NegativeArraySizeException, NULL),
	TRY_CLASS(NoClassDefFoundError, NULL),
	TRY_CLASS(NoSuchFieldError, NULL),
	TRY_CLASS(NullPointerException, NULL),
	TRY_CLASS(Number, NULL),
	TRY_CLASS(NumberFormatException, NULL),
	TRY_CLASS(Object, NULL),
	TRY_CLASS(OutOfMemoryError, NULL),
	TRY_CLASS(Override, NULL),
	TRY_CLASS(Runnable, NULL),
	TRY_CLASS(Runtime, NULL),
	TRY_CLASS(RuntimeException, NULL),
	TRY_CLASS(RuntimePermission, NULL),
	TRY_CLASS(SecurityException, NULL),
	TRY_CLASS(SecurityManager, NULL),
	TRY_CLASS(Short, NULL),
	TRY_CLASS(StackOverflowError, NULL),
	TRY_CLASS(__Start__, NULL),
	TRY_CLASS(StringBuffer, NULL),
	TRY_CLASS(StringBuilder, NULL),
	TRY_CLASS(String, NULL),
	TRY_CLASS(StringIndexOutOfBoundsException, NULL),
	TRY_CLASS(SuppressWarnings, NULL),
	TRY_CLASS(System, NULL),
	TRY_CLASS(Thread, NULL),
	TRY_CLASS(Throwable, NULL),
	TRY_CLASS(UnsupportedClassVersionError, NULL),
	TRY_CLASS(UnsupportedOperationException, NULL),
	TRY_CLASS(VirtualMachineError, NULL),
	{NULL},
}; 

/**
 * Tests parsing of classes from a set of sample classes.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testClassParse)
{
	const testClassInfo* testInfo;
	sjme_zip zip;
	sjme_zip_entry zipEntry;
	sjme_stream_input in;
	sjme_class_info info;
	sjme_stringPool stringPool;
	
	/* Load the Zip that is full of classes. */
	zip = NULL;
	if (sjme_error_is(test->error = sjme_zip_openMemory(
		test->pool, &zip,
		(sjme_pointer)&classes_zip__bin[0],
		classes_zip__len)) || zip == NULL)
		return sjme_unit_fail(test, "Could not open Zip.");
	
	/* Go through and load every single class. */
	for (testInfo = &testClassInfos[0]; testInfo->binaryName != NULL;
		testInfo++)
	{
		/* Debug. */
		sjme_message(">>> %s", testInfo->fileName);
		
		/* Setup string pool. */
		stringPool = NULL;
		if (sjme_error_is(test->error = sjme_stringPool_new(
			test->pool, &stringPool)) ||
			stringPool == NULL)
			return sjme_unit_fail(test, "Could not create string pool.");
		
		/* Locate entry. */
		memset(&zipEntry, 0, sizeof(zipEntry));
		if (sjme_error_is(test->error = sjme_zip_locateEntry(
			zip, &zipEntry, testInfo->fileName)))
			return sjme_unit_fail(test, "Could not locate %s",
				testInfo->fileName);
			
		/* Open entry. */
		in = NULL;
		if (sjme_error_is(test->error = sjme_zip_entryRead(
			&zipEntry, &in)) || in == NULL)
			return sjme_unit_fail(test, "Could not open %s",
				testInfo->fileName);
		
		/* Load the class. */
		info = NULL;
		if (sjme_error_is(test->error = sjme_class_parse(test->pool,
			in, stringPool, &info)) ||
			info == NULL)
			return sjme_unit_fail(test, "Could not parse %s: %d",
				testInfo->fileName, test->error);
		
		/* Run individual test on it? */
		if (testInfo->testRun != NULL)
			if (sjme_error_is(test->error = testInfo->testRun(
				test, info)))
				return sjme_unit_fail(test, "Class test unit failed %s: %d",
					testInfo->fileName, test->error);
		
		/* Close the class. */
		if (sjme_error_is(test->error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(info))))
			return sjme_unit_fail(test, "Could not close class.");
		
		/* Close the entry. */
		if (sjme_error_is(test->error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(in))))
			return sjme_unit_fail(test, "Could not close entry.");
	
		/* Close the string pool. */
		if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stringPool))))
			return sjme_unit_fail(test, "Could not close string pool.");
			
#if defined(SJME_CONFIG_DEBUG)
		/* Check for never freed memory. */
		sjme_test_leakCheck(test->pool);
#endif
	}
	
	/* Close the Zip. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(zip))))
		return sjme_unit_fail(test, "Could not close Zip.");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
