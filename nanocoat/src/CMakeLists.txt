# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
squirreljme_multilib_add_library(Core
	access.c
	allocSizeOf.c
	boot.c
	bytecode.c
	bytecodeFast.c
	bytecodeFastStack.c
	bytecodeSlow.c
	bytecodeSlowConst.c
	bytecodeSlowFlow.c
	bytecodeSlowLocal.c
	bytecodeSlowMath.c
	bytecodeSlowRef.c
	bytecodeSlowStack.c
	classy.c
	classyVm.c
	cleanup.c
	descriptor.c
	gfx.c
	instance.c
	jdwp.c
	loop.c
	mle.c
	mleAtomic.c
	mleDebug.c
	mleJarPackage.c
	mleMath.c
	mleMidi.c
	mleNativeArchive.c
	mleObject.c
	mlePencil.c
	mlePencilFont.c
	mleReference.c
	mleReflection.c
	mleRuntime.c
	mleScritchUi.c
	mleString.c
	mleTask.c
	mleTerminal.c
	mleThread.c
	mleType.c
	modelessStars.c
	nvm.c
	rom.c
	romLibrary.c
	romLibraryZip.c
	romSuite.c
	romSuiteZip.c
	stackMap.c
	stringPool.c
	task.c
	taskFrame.c
	taskThread.c
	tread.c
	vmConfig.c
	walk.c
	walkCoreDump.c
	walkDebug.c)

# Include main headers
squirreljme_multilib_include_directories(Core PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Core needs base to function
squirreljme_multilib_link_libraries_required(Core PUBLIC
	BaseStatic)

# The core library is rather important!
squirreljme_multilib_export(Core)
squirreljme_multilib_install(Core)

