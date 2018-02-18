# Super Easy Video Player (超简单视频播放器)

A video player made for my grandpa. Easy for both youths to config and for seniors to use.

Android Version Required: >= 4.0.3 (API Level >= 15)

Language Supported: zh-CN, en

## Screenshot

![Screenshot.jpg](https://i.loli.net/2018/02/18/5a89158bd11f7.jpg)

## Feature

1. Auto resume play progress after exit.
2. No strange buttons and complicated configurations.

## Usage

1. Get the newest release apk [here](https://github.com/jerrulususu/supereasyvideoplayer/releases) and install.
2. Press "Config Video Path"(选择视频文件夹), and choose any video file on the directory storing the videos.
3. Press "Play"(播放) and enjoy.

## Troubleshooting
1. "Can't open the video"(无法打开此视频): Use MP4 as file format and check to make sure the storage permission is given (MIUI especially!)
2. May jump back for a few seconds when restart the app or rotate the screen: That's the problem with VideoView.getCurrentPosition() method. May fix later.
