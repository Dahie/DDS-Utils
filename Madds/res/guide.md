# Welcome to  Memory Access of DDS

This utility helps you to get a quick overview on the
texture memory usage of your Mod/Game.
  
Select the root-folder of your project to analyze all containing DDS-files.

## Rules of thumb for materials and textures

### Materials:

* Use as few materials as possible to optimize your performance.
* For technical textures try to put objects with similar real life materials on one material/texture.

### Dimensions:

* Specular textures can be quarter the size of the diffuse textures, sometimes even smaller.
* Depending on the level of detail your normal map can be even bigger than your diffuse texture. Adjust the size till your happy with the result.

### Compression:

* Pay attention to the right compression. If your texture uses the alpha channel you need DXT5, if the alpha is just plain white use DXT1.
* Try different DDS-Encoders for DXT-compression. They won't make the texture smaller, but the image quality from compression may be nicer for some textures.
* Aim for a 2048x2048 diffuse texture for your carbody material. If you feel like this is too small to give your texture enough detail go for 2048x4096, but you should stick with one material for your carbody.

Note, these are recommendations: don't feel bound to it and break them if you think it is necessary.


