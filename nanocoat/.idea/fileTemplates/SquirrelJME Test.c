#parse("C File Header.h")

#[[#include]]# "test.h"
#[[#include]]# "proto.h"

SJME_TEST_DECLARE(${NAME})
{
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
