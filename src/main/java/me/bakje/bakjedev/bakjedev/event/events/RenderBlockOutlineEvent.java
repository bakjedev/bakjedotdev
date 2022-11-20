package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public class RenderBlockOutlineEvent extends Event {
    private MatrixStack matrices;
    private VertexConsumer vertexConsumer;
    private BlockPos pos;
    private BlockState state;

    public RenderBlockOutlineEvent(MatrixStack matrices, VertexConsumer vertexConsumer, BlockPos pos, BlockState state) {
        this.matrices = matrices;
        this.vertexConsumer = vertexConsumer;
        this.pos = pos;
        this.state = state;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public void setMatrices(MatrixStack matrices) {
        this.matrices = matrices;
    }

    public VertexConsumer getVertexConsumer() {
        return vertexConsumer;
    }

    public void setVertexConsumer(VertexConsumer vertexConsumer) {
        this.vertexConsumer = vertexConsumer;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public BlockState getState() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

}
