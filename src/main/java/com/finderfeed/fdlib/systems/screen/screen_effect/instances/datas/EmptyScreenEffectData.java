package com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;

public class EmptyScreenEffectData extends ScreenEffectData {

    public static final NetworkCodec<EmptyScreenEffectData> STREAM_CODEC = NetworkCodec.instance(new EmptyScreenEffectData());

    public EmptyScreenEffectData(){

    }

}
