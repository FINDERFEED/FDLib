package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class CutsceneData implements AutoSerializable {

    private List<CameraPos> cameraPositions = new ArrayList<>();

    @SerializableField
    private int cutsceneTime = 20;

    @SerializableField
    private CurveType moveType = CurveType.CATMULLROM;

    @SerializableField
    private EasingType timeEasing = EasingType.LINEAR;

    @SerializableField
    private EasingType lookEasing = EasingType.LINEAR;

    @SerializableField
    private StopMode stopMode = StopMode.AUTOMATIC;

    @SerializableField
    private CutsceneData nextCutscene;

    private CutsceneScreenEffectData screenEffectData = new CutsceneScreenEffectData();

    // Cutscenes do not load chunks! Use them near player!
    public CutsceneData(){}

    public CutsceneData(CutsceneData cutsceneData){
        this.cutsceneTime = cutsceneData.cutsceneTime;
        this.moveType = cutsceneData.moveType;
        this.timeEasing = cutsceneData.timeEasing;
        this.stopMode = cutsceneData.stopMode;
        this.cameraPositions = new ArrayList<>(cutsceneData.cameraPositions);
        this.nextCutscene = cutsceneData.getNextCutscene();
        this.screenEffectData = cutsceneData.getScreenEffectData();
    }

    public CutsceneData stopMode(StopMode stopMode){
        this.stopMode = stopMode;
        return this;
    }

    public CutsceneData timeEasing(EasingType type){
        this.timeEasing = type;
        return this;
    }
    public CutsceneData lookEasing(EasingType type){
        this.lookEasing = type;
        return this;
    }

    public CutsceneData addCameraPos(CameraPos pos){
        this.cameraPositions.add(pos);
        return this;
    }

    public CutsceneData addCameraPos(int index, CameraPos pos){
        this.cameraPositions.add(index, pos);
        return this;
    }

    public CutsceneData time(int timeInTicks){
        this.cutsceneTime = timeInTicks;
        return this;
    }

    public CutsceneData moveCurveType(CurveType type){
        this.moveType = type;
        return this;
    }

    public CutsceneData nextCutscene(CutsceneData next){
        this.nextCutscene = next;
        return this;
    }

    public <A extends ScreenEffectData,B extends ScreenEffect<A>> CutsceneData addScreenEffect(int tick, ScreenEffectType<A,B> type, A screenEffectData, int inTime, int stayTime, int outTime){
        this.screenEffectData.putScreenEffectOnTick(tick, type, screenEffectData, inTime, stayTime, outTime);
        return this;
    }

    public <A extends ScreenEffectData,B extends ScreenEffect<A>> CutsceneData addScreenEffect(int tick, Supplier<ScreenEffectType<A, B>> type, A screenEffectData, int inTime, int stayTime, int outTime){
        return this.addScreenEffect(tick, type.get(), screenEffectData, inTime, stayTime, outTime);
    }

    public CurveType getMoveType() {
        return moveType;
    }

    public int getCutsceneTime() {
        return cutsceneTime;
    }

    public List<CameraPos> getCameraPositions() {
        return cameraPositions;
    }

    public EasingType getTimeEasing() {
        return timeEasing;
    }

    public EasingType getLookEasing() {
        return lookEasing;
    }

    public StopMode getStopMode() {
        return stopMode;
    }

    public CutsceneData getNextCutscene() {
        return nextCutscene;
    }

    public CutsceneScreenEffectData getScreenEffectData() {
        return screenEffectData;
    }

    public static CutsceneData create(){
        return new CutsceneData();
    }

    public void encode(FriendlyByteBuf buf){
        CompoundTag c = new CompoundTag();
        this.autoSave(c);
        buf.writeNbt(c);

        buf.writeBoolean(this.nextCutscene != null);

        if (this.nextCutscene != null){
            this.nextCutscene.encode(buf);
        }

        this.screenEffectData.encode(buf);

    }


    public static CutsceneData decode(FriendlyByteBuf buf){
        CutsceneData cutsceneData1 = new CutsceneData();
        cutsceneData1.autoLoad(buf.readNbt());

        if (buf.readBoolean()){
            cutsceneData1.nextCutscene = CutsceneData.decode(buf);
        }

        cutsceneData1.screenEffectData = CutsceneScreenEffectData.decode(buf);

        return cutsceneData1;
    }

    @Override
    public void autoLoad(CompoundTag tag) {
        AutoSerializable.super.autoLoad(tag);

        int size = tag.getInt("cameraPosListLength");
        for (int i = 0; i < size;i++){
            CameraPos pos = new CameraPos();
            pos.autoLoad("pos_" + i,tag);
            this.cameraPositions.add(pos);
        }
    }

    @Override
    public void autoSave(CompoundTag tag) {
        AutoSerializable.super.autoSave(tag);

        tag.putInt("cameraPosListLength",this.cameraPositions.size());
        for (int i = 0; i < this.cameraPositions.size();i++){
            CameraPos pos = this.cameraPositions.get(i);
            pos.autoSave("pos_" + i,tag);
        }

    }

    public enum StopMode{
        AUTOMATIC, // Ends on completion
        PLAYER, // Player can end the cutscene with a hotkey
        UNSTOPPABLE // Can only be stopped through cutscene fix command or code
    }

}
