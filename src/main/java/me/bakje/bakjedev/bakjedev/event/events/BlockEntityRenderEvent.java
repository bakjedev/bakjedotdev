package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class BlockEntityRenderEvent extends Event {
    public static class Single extends BlockEntityRenderEvent {

        protected BlockEntity blockEntity;
        protected MatrixStack matrices;
        protected VertexConsumerProvider vertex;

        public BlockEntity getBlockEntity() {
            return blockEntity;
        }

        public MatrixStack getMatrices() {
            return matrices;
        }

        public VertexConsumerProvider getVertex() {
            return vertex;
        }

        public static class Pre extends Single {

            public Pre(BlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider vertex) {
                this.blockEntity = blockEntity;
                this.matrices = matrices;
                this.vertex = vertex;
            }

            public void setBlockEntity(BlockEntity blockEntity) {
                this.blockEntity = blockEntity;
            }

            public void setMatrices(MatrixStack matrices) {
                this.matrices = matrices;
            }

            public void setVertex(VertexConsumerProvider vertex) {
                this.vertex = vertex;
            }
        }

        public static class Post extends Single {
            public Post(BlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
                this.blockEntity = blockEntity;
                this.matrices = matrices;
                this.vertex = vertexConsumers;
            }
        }
    }

    public static class PreAll extends EntityRenderEvent {
    }

    public static class PostAll extends EntityRenderEvent {
    }
}
