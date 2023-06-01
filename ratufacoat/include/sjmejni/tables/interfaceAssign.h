/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

out->DestroyJavaVM = sjme_implDestroyJavaVM;
out->AttachCurrentThread = sjme_implAttachCurrentThread;
out->DetachCurrentThread = sjme_implDetachCurrentThread;
out->GetEnv = sjme_implGetEnv;
out->AttachCurrentThreadAsDaemon = sjme_implAttachCurrentThreadAsDaemon;
