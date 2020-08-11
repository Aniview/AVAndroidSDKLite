package com.aniview.demo;

import android.net.Uri;

/**
 * @author Maksym Popovych
 */
class PlaybackSample {

//	Live television with short chunks
//	private final static String URL = "https://hls-works.appspot.com/a?url=http://109.68.40.72/life/k1_2/index.m3u8"; //shorter chunks

//	VOD from apple
//	private final static String URL = "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/1840_vod.m3u8";

//	Live television with 6 hour duration
	private final static String URL = "https://hls-works.appspot.com/a?url=http://nana10-hdl-il-sw.ctedgecdn.net/10tv_Desktop/c13_1200.m3u8";

	final static Uri CONTENT_URI = Uri.parse(URL);

}
