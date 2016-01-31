# MyApplication
Android application in which audio and image play simultaneously

####Camera.java is a main class here, all other java files are not contributing in this project for now.

##Description:
In this android app I am trying to capture an image and record an audio clip and both have same names but different 
extensions. And map these files paths into a .xml file under a "node"  tag.XML file saved in internal memory of the App.
So whenever we open an image file using this App the audio file which associated with it start playing.

## XML file:
Xml file structure is like this:
```
<nodes>

  <node>
    <image>image-url</image>
    <audio>audio-url</audio>
  </node>
  <node>
  ...
  </node>
  .
  .
</nodes>
```
