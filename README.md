# About

This project provides reusable components of the Jeography GIS as a library. It
also contains the main executables of the GIS itself.

# License

This library is released under the terms of the GNU Lesser General Public
License.

See [LGPL.md](LGPL.md) and [GPL.md](GPL.md) for details.

# Running the main GIS user interface

## Using gradle
You can run the main UI using gradle directly:

    ./gradlew run

## Using scripts
Setup the execution environment:

    ./gradlew createRuntime

Then you can run the main executables from the build directory:

    ./scripts/jeography
    ./scripts/geometry-preview
    ./scripts/create-place-database
