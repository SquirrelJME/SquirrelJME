#parse("C File Header.h")

#[[#include]]# "test.h"
#[[#include]]# "proto.h"

sjme_testResult ${NAME}(sjme_test* test)
{
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
