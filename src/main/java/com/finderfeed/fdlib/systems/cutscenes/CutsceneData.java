package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class CutsceneData implements AutoSerializable {

    private List<CameraPos> cameraPositions = new ArrayList<>();

    @SerializableField
    private int cutsceneTime = 20;

    @SerializableField
    private CurveType moveType = CurveType.CATMULLROM;

    @SerializableField
    private EasingType timeEasing = EasingType.LINEAR;

    private CutsceneData(){}

    public CutsceneData timeEasing(EasingType type){
        this.timeEasing = type;
        return this;
    }

    public CutsceneData addCameraPos(CameraPos pos){
        this.cameraPositions.add(pos);
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

    public static CutsceneData create(){
        return new CutsceneData();
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
}
