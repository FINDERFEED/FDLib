package com.finderfeed.fdlib.systems.screen.default_components.text;

import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.ValueComponent;
import com.finderfeed.fdlib.systems.screen.annotations.FDScale;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class FDTextBox<T> extends ValueComponent<T> {


    private int firstLineId = 0;
    private int lastLineId = 0;
    private float scrollerWidth = 6;
    private float scrollAmount = 0;
    private boolean mouseSelecting = false;
    private boolean isSelecting;
    protected int selectionCursor = 0;
    protected int cursor = 0;
    protected StringBuilder value = new StringBuilder();
    protected List<Line> lines = new ArrayList<>(List.of(new Line(0,0)));


    public float textOffsetLeft = 3;
    public float textOffsetRight = 3;
    public float textOffsetY = 3;
    public float textScale = 1;
    public Component emptyComponentText = Component.empty();
    public String filter = "(.|\\n)*";


    public FDTextBox(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        this.renderBackground(graphics,x,y,mx,my,partialTicks);
        this.renderText(graphics,x,y,mx,my,partialTicks);
        this.renderScrollBar(graphics,x,y,mx,my,partialTicks);
    }

    public void renderScrollBar(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks){
        float scrollAmount = this.getMaxScrollAmount();
        if (scrollAmount != 0){
            float xp = x + this.getWidth() - this.scrollerWidth - this.textOffsetY;
            FDRenderUtil.renderScrollBar(graphics.pose(), xp, y + this.getTextOffsetY(),
                    this.scrollerWidth, this.getHeight() - this.getTextOffsetY() * 2,
                    this.scrollAmount, scrollAmount,
                    0.3f, 0.3f, 0.3f, 1f, 0.8f, 0.8f, 0.8f, 1f
            );
        }
    }

    public void renderText(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks){
        String str = this.value.toString();
        x += this.textOffsetLeft;
        y += this.textOffsetY;
        Font font = Minecraft.getInstance().font;
        float lineHeight = font.lineHeight * textScale;
        int cursorLine = this.getLineAtCursor(this.cursor);
        firstLineId = -1;
        lastLineId = -1;
        for (int i = 0; i < lines.size();i++){
            Line line = lines.get(i);
            String toRender = str.substring(line.begin,line.end);
            float offs = i * (font.lineHeight * textScale);
            float yPos = y + offs - this.scrollAmount;
            float ys = yPos - y;
            if (ys < 0){
                continue;
            }else if (firstLineId == -1){
                firstLineId = i;
            }
            if (ys > this.getHeight() - this.getTextOffsetY() - font.lineHeight * textScale){
                if (lastLineId == -1){
                    lastLineId = i - 1;
                }
                continue;
            }
            FDRenderUtil.renderScaledText(
                    graphics,
                    toRender.replace("\n",""),
                    x,
                    yPos,
                    textScale,
                    false,
                    0xffffff
            );
            if (this.isFocused() && i == cursorLine){
                float alpha = Util.getMillis() % 800 < 400 ? 0 : 1;
                int index = cursor - line.begin;
                String w = toRender.substring(0, index);
                float sw = font.width(w) * textScale;
                FDRenderUtil.fill(graphics.pose(), x + sw, y + offs - scrollAmount, textScale, lineHeight, 1f, 1f, 1f, alpha);
            }

        }
        if (lastLineId == -1){
            lastLineId = this.lines.size() - 1;
        }

        if (isSelecting && selectionCursor != cursor){
            int selectionLine = this.getLineAtCursor(selectionCursor);
            int line1 = Math.min(cursorLine,selectionLine);
            int line2 = Math.max(cursorLine,selectionLine);
            if (line1 != line2){
                for (int i = line1; i <= line2;i++){
                    Line line = this.lines.get(i);
                    int begin = line.begin;
                    int end = line.end;
                    float startOffset = 0;
                    if (i == line1){
                        begin = Math.min(cursor,selectionCursor);
                        String ws = this.value.substring(line.begin,begin);
                        startOffset = font.width(ws) * textScale;
                    }else if (i == line2){
                        end = Math.max(cursor,selectionCursor);
                    }
                    String ws = this.value.substring(begin,end);
                    float w = font.width(ws) * textScale;
                    float yRenderPos = y + i * lineHeight - scrollAmount;
                    if (yRenderPos - y < 0 || yRenderPos - y > this.getHeight() - this.getTextOffsetY() - font.lineHeight * textScale){
                        continue;
                    }
                    FDRenderUtil.fill(RenderType.guiTextHighlight(),graphics.pose(),
                            x + startOffset,
                            yRenderPos,
                            w + textScale,
                            lineHeight,
                            0f,0f,1f,1f
                    );
                }
            }else{
                Line line = this.lines.get(line1);
                int begin = Math.min(cursor,selectionCursor);
                int end = Math.max(cursor,selectionCursor);
                String ws = this.value.substring(begin,end);
                String ws2 = this.value.substring(line.begin,begin);
                float w = font.width(ws) * textScale;
                float w2 = font.width(ws2) * textScale;
                FDRenderUtil.fill(RenderType.guiTextHighlight(),graphics.pose(),
                        x + w2,
                        y + line1 * lineHeight - scrollAmount,
                        w + textScale,
                        lineHeight,
                        0f,0f,1f,1f
                );
            }
        }
    }

    public abstract void renderBackground(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks);


    public void updateLines(){
        this.lines.clear();
        if (value.isEmpty()){
            this.lines.add(new Line(0,0));
            return;
        }

        int splitLength;
        if (textScale != 0) {
            splitLength = Math.round((this.getWidth() - this.getTextOffsetLeft() - this.getTextOffsetRight()) / textScale);
        }else{
            splitLength = 0;
        }
        Minecraft.getInstance().font.getSplitter().splitLines(
                this.value.toString(),
                splitLength,
                Style.EMPTY,
                true,
                (style,begin,end)->{
                    this.lines.add(new Line(begin,end));
                }
        );
        if (this.value.charAt(this.value.length() - 1) == '\n'){
            this.lines.add(new Line(this.value.length(),this.value.length()));
        }
    }

    public void moveCursorLeft(){
        this.cursor = Mth.clamp(this.cursor - 1,0,this.value.length());
        if (!isSelecting){
            this.selectionCursor = cursor;
        }else{
            if (!Screen.hasShiftDown()){
                this.isSelecting = false;
            }
        }

        this.scrollToCursor(cursor);
    }
    public void moveCursorRight(){
        this.cursor = Mth.clamp(this.cursor + 1,0,this.value.length());
        if (!isSelecting){
            this.selectionCursor = cursor;
        }else{
            if (!Screen.hasShiftDown()){
                this.isSelecting = false;
            }
        }

        this.scrollToCursor(cursor);
    }


    public void moveCursorUp(){
        int line = this.getLineAtCursor(this.cursor);
        if (line > 0){
            Line prev = lines.get(line - 1);
            Line current = lines.get(line);
            int cdelta = prev.end - prev.begin;
            int currentLocalPos = cursor - current.begin;
            if (currentLocalPos > cdelta){
                char endChar = this.value.charAt(prev.end - 1);
                cursor = prev.end;
                if (endChar == '\n'){
                    cursor -= 1;
                }
            }else{
                cursor = prev.begin + currentLocalPos;
            }
        }
        if (!isSelecting){
            this.selectionCursor = cursor;
        }else{
            if (!Screen.hasShiftDown()){
                this.isSelecting = false;
            }
        }

        this.scrollToCursor(cursor);
    }

    public void moveCursorDown(){
        int line = this.getLineAtCursor(this.cursor);
        if (line < lines.size() - 1){
            Line next = lines.get(line + 1);
            Line current = lines.get(line);
            int cdelta = next.end - next.begin;
            int currentLocalPos = cursor - current.begin;
            if (currentLocalPos > cdelta){
                char endChar = this.value.charAt(next.end - 1);
                boolean flag = next.end != this.value.length() || line + 1 != this.lines.size() - 1;
                cursor = next.end;
                if (endChar == '\n' && flag){
                    cursor -= 1;
                }
            }else{
                cursor = next.begin + currentLocalPos;
            }
        }
        if (!isSelecting){
            this.selectionCursor = cursor;
        }else{
            if (!Screen.hasShiftDown()){
                this.isSelecting = false;
            }
        }

        this.scrollToCursor(cursor);
    }

    public int getLineAtCursor(int cursor){
        for (int i = 0; i < lines.size();i++){
            Line line = lines.get(i);
            if (cursor >= line.begin && cursor < line.end){
                return i;
            }
        }
        return lines.size() - 1;
    }

    public boolean removeCharacterAtCursor(boolean update){
        if (!isSelecting) {
            int deletePos = cursor - 1;
            if (deletePos >= 0 && deletePos < this.value.length()) {
                this.value.deleteCharAt(deletePos);
                cursor--;
                this.updateLines();
                this.scrollToCursor(cursor);
                return true;
            }
        }else{
            int begin = Mth.clamp(Math.min(cursor,selectionCursor),0,this.value.length());
            int end = Mth.clamp(Math.max(cursor,selectionCursor),0,this.value.length());
            this.value.delete(begin,end);
            this.cursor = begin;
            if (update) {
                this.updateLines();
            }
            this.isSelecting = false;
            this.selectionCursor = cursor;
            this.scrollToCursor(cursor);
            return true;
        }
        return false;
    }

    public boolean insertText(String text){
        text = text.replace("\r\n","\n");
        text = text.replace("\r","\n");
        if (!isSelecting) {
            String potentialValue = new StringBuilder(this.value).insert(cursor, text).toString();
            if (this.checkString(potentialValue)) {
                this.value = new StringBuilder(potentialValue);
                this.cursor += text.length();
                this.updateLines();
                this.scrollToCursor(cursor);
                return true;
            }
        }else{
            String cache = this.value.toString();
            this.removeCharacterAtCursor(false);
            this.value.insert(cursor,text);
            if (this.checkString(this.value.toString())){
                this.updateLines();
                this.isSelecting = false;
                this.cursor += text.length();
                this.scrollToCursor(cursor);
                return true;
            }else{
                this.value = new StringBuilder(cache);
            }
        }
        return false;
    }

    public boolean checkString(String check){
        int i = 0;
        int substrAmount = 100;
        do {
            int end = Mth.clamp(i + substrAmount,0,check.length());
            String s = check.substring(i,end);
            if (!s.matches(this.filter)){
                return false;
            }
            i += substrAmount;
        } while (i < check.length());
        return true;
    }

    public void setCursorAtMouse(float mx,float my){
        if (!this.value.isEmpty()) {
            Font font = Minecraft.getInstance().font;
            float x = mx - this.getTextOffsetLeft();
            float y = my - textOffsetY;
            y = Mth.clamp(y,0,this.getHeight() / this.textScale - this.textOffsetY - font.lineHeight * textScale) + this.scrollAmount;
            int lineId = (int) Math.floor(y / (font.lineHeight * textScale));
            lineId = Mth.clamp(lineId, 0, this.lines.size() - 1);
            Line line = this.lines.get(lineId);
            if (line.begin != line.end) {
                float prevWidth = font.width(this.value.substring(line.begin, line.begin + 1)) * textScale / 2;
                for (int i = 0; i < line.end - line.begin; i++) {
                    String ws = this.value.substring(line.begin, line.begin + i);
                    float width = font.width(ws) * textScale;
                    if (x < width + (Math.abs(width - prevWidth) / 2)) {
                        this.cursor = line.begin + i;
                        return;
                    }
                    prevWidth = width;
                }
            }
            this.cursor = line.end;
            if (lineId != this.lines.size() - 1){
                this.cursor--;
            }
        }else{
            this.cursor = 0;
        }
    }

    public boolean scroll(int times){
        Font font = Minecraft.getInstance().font;
        float amount = this.textScale * font.lineHeight * times;
        float prev = this.scrollAmount;
        this.scrollAmount = Mth.clamp(this.scrollAmount + amount,0,this.getMaxScrollAmount());
        return prev != this.scrollAmount;
    }

    public void scrollToCursor(int cursor){
        int lineId = this.getLineAtCursor(cursor);
        lastLineId = Mth.clamp(lastLineId,0,this.lines.size() - 1);
        if (lineId > lastLineId){
            this.scroll(lineId - lastLineId);
        }else if (lineId < firstLineId){
            this.scroll(lineId - firstLineId);
        }
    }

    @Override
    public boolean charTyped(char character, int key) {
        return insertText(Character.toString(character));
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT){
            if (!isSelecting){
                this.selectionCursor = this.cursor;
                isSelecting = true;
            }
            return true;
        }
        switch (keyCode){
            case GLFW.GLFW_KEY_V ->{
                if (Screen.hasControlDown()){
                    String s = Minecraft.getInstance().keyboardHandler.getClipboard();
                    this.insertText(s);
                    return true;
                }
                return false;
            }
            case GLFW.GLFW_KEY_C ->{
                if (Screen.hasControlDown() && !this.value.isEmpty() && this.cursor != this.selectionCursor){
                    int begin = Math.min(this.cursor,this.selectionCursor);
                    int end = Math.max(this.cursor,this.selectionCursor);
                    String s = this.value.substring(begin,end);
                    Minecraft.getInstance().keyboardHandler.setClipboard(s);
                    return true;
                }
                return false;
            }
            case GLFW.GLFW_KEY_X ->{
                if (Screen.hasControlDown() && !this.value.isEmpty() && this.cursor != this.selectionCursor){
                    int begin = Math.min(this.cursor,this.selectionCursor);
                    int end = Math.max(this.cursor,this.selectionCursor);
                    String s = this.value.substring(begin,end);
                    Minecraft.getInstance().keyboardHandler.setClipboard(s);
                    this.removeCharacterAtCursor(true);
                    return true;
                }
                return false;
            }
            case GLFW.GLFW_KEY_A -> {
                if (Screen.hasControlDown()){
                    isSelecting = true;
                    this.cursor = this.lines.getLast().end;
                    this.selectionCursor = 0;
                    return true;
                }
                return false;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                this.moveCursorLeft();
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                this.moveCursorRight();
                return true;
            }
            case GLFW.GLFW_KEY_UP -> {
                this.moveCursorUp();
                return true;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                this.moveCursorDown();
                return true;
            }
            case GLFW.GLFW_KEY_ENTER -> {
                this.insertText("\n");
                return true;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                this.removeCharacterAtCursor(true);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        int scroll = (int)scrollY;
        return this.scroll(-scroll);
    }

    @Override
    public boolean keyReleased(int key, int scanCode, int modifiers) {

        return super.keyReleased(key, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int key) {
        if (key == GLFW.GLFW_MOUSE_BUTTON_1) {
            this.setCursorAtMouse((float)mx,(float)my);
            isSelecting = false;
            mouseSelecting = true;
            selectionCursor = this.cursor;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseMoved(double mx, double my) {
        if (mouseSelecting){
            this.setCursorAtMouse((float)mx,(float)my);
            isSelecting = true;
        }
        super.mouseMoved(mx, my);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int key) {
        if (key == GLFW.GLFW_MOUSE_BUTTON_1) {
            mouseSelecting = false;
            return true;
        } else {
            return false;
        }
    }

    public String getStringValue() {
        return value.toString();
    }

    public boolean isSelectionActive(){
        return isSelecting && selectionCursor != cursor;
    }

    public void setValue(String value) {
        if (value.matches(filter)) {
            this.value = new StringBuilder(value);
        }
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }


    @Override
    public void setWidth(float width) {
        super.setWidth(Mth.clamp(width,scrollerWidth,Integer.MAX_VALUE));
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);

    }

    @Override
    public void onFocused(boolean state) {
        if (!state){
            this.isSelecting = false;
            this.cursor = 0;
            this.selectionCursor = 0;
        }
    }

    public float getTextOffsetLeft() {
        return textOffsetLeft;
    }

    public float getTextOffsetRight() {
        return textOffsetRight + this.scrollerWidth + 2;
    }

    public float getTextOffsetY() {
        return textOffsetY;
    }

    public float getMaxScrollAmount(){
        Font font = Minecraft.getInstance().font;
        float lineHeight = font.lineHeight * textScale;
        int lineCount = this.lines.size() + 1;
        return Mth.clamp(lineHeight * lineCount - this.getHeight() - this.textOffsetY * 2,0,Integer.MAX_VALUE);
    }


    @Override
    public void applyOptions(FDEditorComponent owner, Annotation annotation) {
        if (annotation instanceof FDScale scale){
            this.textScale = scale.value();
        }
    }

    public record Line(int begin, int end){};

}
