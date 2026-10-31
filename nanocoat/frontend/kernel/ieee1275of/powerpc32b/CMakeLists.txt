# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Root kernel library
add_executable(KernelRoot
	boot.s)

# This is assembly
set_property(SOURCE boot.s
	PROPERTY LANGUAGE ASM)

# Make this C
squirreljme_linker_language(KernelRoot C)

# The core libraries are used, since we do need the VM
# Not squirreljme_link_libraries_required because we do not libm!
squirreljme_link_libraries(KernelRoot PRIVATE
	BaseStatic
	CoreStatic)

# Options needed to link the kernel
target_link_options(KernelRoot PRIVATE
	"-nostartfiles"
	"-nostdlib"
	"-nodefaultlibs"
	"-Wl,--no-dynamic-linker"
	"-T" "${CMAKE_CURRENT_SOURCE_DIR}/boot.ld")

# Where is this going?
squirreljme_target_binary_output(KernelRoot
	"${SQUIRRELJME_BINARY_OUTPUT_DIR}")
squirreljme_target_binary_name(KernelRoot
	"squirreljme.elf")

# Commands used to build the bootable ISO image
add_custom_target(KernelIso
	COMMAND "${CMAKE_COMMAND}" "-E" "echo" "${kernelIso}"
	BYPRODUCTS "${SQUIRRELJME_BINARY_OUTPUT_DIR}/${kernelIso}")

# Add target for testing the kernel
add_custom_target(KernelTest
	COMMAND "qemu-system-ppc"
		"-kernel"
		"${SQUIRRELJME_BINARY_OUTPUT_DIR}/squirreljme.elf")

# Make sure the actual kernel is built first
add_dependencies(KernelTest
	KernelRoot)

# Do not run this by default
set_target_properties(KernelTest PROPERTIES
	EXCLUDE_FROM_ALL TRUE
	EXCLUDE_FROM_DEFAULT_BUILD TRUE)


