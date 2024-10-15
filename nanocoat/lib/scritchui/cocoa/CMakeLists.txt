# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUICocoa SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
	cocoaComponent.m
	cocoaContainer.m
	cocoaCore.m
	cocoaIntern.m
	cocoaLabel.m
	cocoaLaf.m
	cocoaLoop.m
	cocoaMenu.m
	cocoaPanel.m
	cocoaPencil.m
	cocoaScreen.m
	cocoaScrollPanel.m
	cocoaView.m
	cocoaWindow.m)

# Always make it PIC
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUICocoa
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUICocoa PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Flags needed for GNUStep
if(SQUIRRELJME_IS_GNUSTEP_DESKTOP)
	# Flags needed for build
	execute_process(
		COMMAND "${GNUSTEP_EXE_PATH}" --objc-flags
		OUTPUT_VARIABLE SQUIRRELJME_GNUSTEP_CFLAGS
		OUTPUT_STRIP_TRAILING_WHITESPACE)
	separate_arguments(SQUIRRELJME_GNUSTEP_CFLAGS UNIX_COMMAND
		${SQUIRRELJME_GNUSTEP_CFLAGS})
	target_compile_options(ScritchUICocoa PRIVATE
		${SQUIRRELJME_GNUSTEP_CFLAGS})

	# Additional includes, mostly needed for CLion to properly show them
	execute_process(
		COMMAND "${GNUSTEP_EXE_PATH}" --variable=GNUSTEP_SYSTEM_HEADERS
		OUTPUT_VARIABLE SQUIRRELJME_GNUSTEP_HEADERS
		OUTPUT_STRIP_TRAILING_WHITESPACE)
	separate_arguments(SQUIRRELJME_GNUSTEP_HEADERS UNIX_COMMAND
		${SQUIRRELJME_GNUSTEP_HEADERS})
	target_include_directories(ScritchUICocoa PRIVATE
		${SQUIRRELJME_GNUSTEP_HEADERS})
endif()

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUICocoa
	"squirreljme-scritchui-cocoa")

# Apple and GNUStep specific link options
if(SQUIRRELJME_IS_APPLE_DESKTOP)
	target_link_libraries(ScritchUICocoa
		"-framework Foundation"
		"-framework Cocoa")
elseif(SQUIRRELJME_IS_GNUSTEP_DESKTOP)
	# gnustep-config can accept multiple switches however it ignores other
	# specified ones, so this has to be done multiple times!
	# Objective C
	execute_process(
		COMMAND "${GNUSTEP_EXE_PATH}" --objc-libs
		OUTPUT_VARIABLE SQUIRRELJME_GNUSTEP_LIBS_OBJC
		OUTPUT_STRIP_TRAILING_WHITESPACE)
	separate_arguments(SQUIRRELJME_GNUSTEP_LIBS_OBJC UNIX_COMMAND
		${SQUIRRELJME_GNUSTEP_LIBS_OBJC})

	# Base Libraries
	execute_process(
		COMMAND "${GNUSTEP_EXE_PATH}" --base-libs
		OUTPUT_VARIABLE SQUIRRELJME_GNUSTEP_LIBS_BASE
		OUTPUT_STRIP_TRAILING_WHITESPACE)
	separate_arguments(SQUIRRELJME_GNUSTEP_LIBS_BASE UNIX_COMMAND
		${SQUIRRELJME_GNUSTEP_LIBS_BASE})

	# GUI Libraries
	execute_process(
		COMMAND "${GNUSTEP_EXE_PATH}" --gui-libs
		OUTPUT_VARIABLE SQUIRRELJME_GNUSTEP_LIBS_GUI
		OUTPUT_STRIP_TRAILING_WHITESPACE)
	separate_arguments(SQUIRRELJME_GNUSTEP_LIBS_GUI UNIX_COMMAND
		${SQUIRRELJME_GNUSTEP_LIBS_GUI})

	# Add everything in
	target_link_options(ScritchUICocoa PRIVATE
		${SQUIRRELJME_GNUSTEP_LIBS_OBJC}
		${SQUIRRELJME_GNUSTEP_LIBS_BASE}
		${SQUIRRELJME_GNUSTEP_LIBS_GUI})

	# These specifically are needed, otherwise GOBJC will link against
	# static libraries and as such the .so will not be referenced by
	# the ScritchUI .so, thus it will have no idea it needs to bring in
	# GNUStep at all
	target_link_libraries(ScritchUICocoa PUBLIC
		-l:libgnustep-base.so -l:libgnustep-gui.so)
endif()
