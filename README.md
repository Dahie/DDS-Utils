# DDS-Utils

DDS-Utils are a bunch of tools to work cross-platform with DirectDrawSurfaces textures in Java.
These work well decent on MacOS and provide an alternative to the nVidia Texture Tools.

## DDS-Utils Provides

- Badds - Batch a DDS, Rescale and Batch-process DDS-files.
- Radds - Review a DDS, Lightweight DDS-viewer
- Dropps - Simple DDS-image-converter
- DDSUtil - Wannabe-library for DDS-conversion

Each subproject has its own project folder in the repository. 
I use eclipse to check each one out. The Tools require the DDSUtils-project in their buildpath.

## History

The project came from some game development projects in summer 2008. I needed tools for 
for reviewing and processing DDS texture files. This was my first own bigger java project and therefore
I'm sure there are things that can be nicer and I'm open for feedback and criticism. 

### State of the project

Core development finished around 2011 and the projects did not get any updates in regards to 
Java language changes or modern ways of code organization.
The tools still run, if you add them in Eclipse or IntelliJ and setup the right build paths.

I moved on from Java a while ago and do not have plans for this project. 
I therefore welcome if anyone wants to fork this or modernize it.


## More Information

Please see the [wiki][wiki]

## Author

DDS-Utils are written by [Daniel Senff][dahie]

## License

Released under a [GNU General Public License v3][license].

[dahie]: http://github.com/Dahie
[imagej]: http://rsb.info.nih.gov/ij/
[wiki]: https://github.com/Dahie/DDS-Utils/wiki
[license]: http://github.com/Dahie/DDS-Utils/blob/master/LICENSE.md
