# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# A kernel is effectively a HAL on top of a barebones device/hardware that
# has the purpose of invoking a `main(int argc, char** argv)` of one of the
# valid front-ends such as ABCD or libjvm.

# Base paths for the kernel, consists of the base architecture independent
# sources and architecture specific setup.
set(kernelBase "${CMAKE_CURRENT_SOURCE_DIR}/${SQUIRRELJME_SYSTEM}")
set(kernelRoot "${kernelBase}/${SQUIRRELJME_ARCH}")

# Only if a Kernel CMakeLists for the system and arch exists, can an actual
# kernel be included for a build
if(EXISTS "${kernelBase}/CMakeLists.txt" AND
	EXISTS "${kernelRoot}/CMakeLists.txt")
	# Indicate that this is being used
	message(STATUS "Building system/arch specific kernel...")

	# What are the final images called?
	set(kernelIso "squirreljme-${SQUIRRELJME_SYSTEM}-${SQUIRRELJME_ARCH}.iso")
	set(kernelImg "squirreljme-${SQUIRRELJME_SYSTEM}-${SQUIRRELJME_ARCH}.img")

	# Base kernel support, which generally has architecture and machine
	# independent information
	add_subdirectory("${kernelBase}")

	# Architecture specific information is last, as booting and otherwise is
	# likely to be architecture and machine dependent
	add_subdirectory("${kernelRoot}")

	# There needs to be an image or ISO of the kernel that gets booted
	if(NOT TARGET KernelIso AND
		NOT TARGET KernelImg AND
		NOT TARGET KernelRoot)
		message(FATAL_ERROR "No bootable image for the kernel!")
	endif()

# Indicate why a kernel might be missing
else()
	# Lack of system support?
	if(NOT EXISTS "${kernelBase}/CMakeLists.txt")
		message(STATUS "No base kernel exists for ${SQUIRRELJME_SYSTEM}...")
	endif()

	# Lack of architecture support?
	if(NOT EXISTS "${kernelRoot}/CMakeLists.txt")
		message(STATUS "No root kernel exists for ${SQUIRRELJME_ARCH}...")
	endif()
endif()
