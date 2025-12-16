package com.logandhillon.typeofwar.resource;

/**
 * @author Logan Dhillon
 */
public class Music {
    private record MusicFile(String fileName, String songName, String artistName) {
        public String getResourcePath() {
            return "/sound/bgm/" + fileName;
        }
    }
}
