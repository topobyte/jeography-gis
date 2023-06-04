# About

Jeography is a geographical information system (GIS) with a strong focus on
OpenStreetMap (OSM) maps and data.
The project is broken down into a number of modules, most of which can be
reused as libraries in other maps-related projects.
In particular, the `core`, `tiles`, `viewers` and `gis` modules are provided
as libraries for this purpose.
The `exe` modules contains the code necessary for running the GIS' executables
and is not distributed for reuse.

# License

This project is released under the terms of the GNU Lesser General Public
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

## Creating a place database

First download place data, for example using [osmocrat](https://github.com/topobyte/osmocrat):

    osmocrat overpass --output places.osm --raw "(node[place=city]; node[place=town]; node[place=island];); out;"

Then create the database:

    ./scripts/create-place-database --input-format xml --input places.osm --output places.sqlite

Then configure the database path in the `File` → `Settings` menu.
