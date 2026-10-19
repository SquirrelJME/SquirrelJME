/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/mleBrackets.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/classy.h"

#define sjme_atomic_type sjme_jbracketTrace
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_common
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_class_info
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_class_methodInfo
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_frame
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_rom_library
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_task
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_thread
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_vmClass_loader
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_class_poolEntryClass
#define sjme_atomic_refs 1
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_class_poolEntryNameAndType
#define sjme_atomic_refs 1
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_threadScheduleMode
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_thread_startType
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_thread_statusType
#include "sjme/atomicTemplateC.h" /* NOLINT */

#define sjme_atomic_type sjme_nvm_stringPool_string
#include "sjme/atomicTemplateC.h" /* NOLINT */
