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
#include "sjme/nvm/task.h"
#include "test.h"
#include "unit.h"

const sjme_mock_configSet configTaskSpawn =
{
	NULL,
	0,

	{
		sjme_mock_doNvmState,
		sjme_mock_doRomSuite,
		sjme_mock_doRomMockLibrary,
		NULL
	}
};

/**
 * Tests spawning of tasks with @c sjme_nvm_task_taskNew .
 * 
 * @since 2023/11/29
 */
SJME_TEST_DECLARE(testTaskSpawn)
{
	sjme_mock mock;
	sjme_nvm_task task;
	sjme_nvm_task_taskNewConfig startConfig;

	/* Setup mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock, &configTaskSpawn,
		0))
		return sjme_unit_fail(test, "Could not setup mocks.");

	/* Setup start configuration. */
	memset(&startConfig, 0, sizeof(startConfig));
	startConfig.mainClass = "MockMain";

	/* Start task. */
	task = NULL;
	if (sjme_error_is(sjme_nvm_task_taskNew(mock.nvmState,
		&startConfig, &task)) || task == NULL)
		return sjme_unit_fail(test, "Could not start task.");

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
