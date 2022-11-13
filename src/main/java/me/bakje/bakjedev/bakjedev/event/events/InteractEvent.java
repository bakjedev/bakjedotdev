package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class InteractEvent extends Event {
    public static class InteractItem extends InteractEvent {

        protected Hand hand;

        public Hand getHand() {
            return hand;
        }

        public InteractItem(Hand hand) {
            this.hand = hand;
        }
    }

    public static class InteractBlock extends InteractEvent {

        protected Hand hand;
        protected BlockHitResult hitResult;

        public Hand getHand() {
            return hand;
        }

        public BlockHitResult getHitResult() {
            return hitResult;
        }

        public InteractBlock(Hand hand, BlockHitResult hitResult) {
            this.hand = hand;
            this.hitResult = hitResult;
        }
    }

    public static class AttackBlock extends InteractEvent {

        protected BlockPos pos;
        protected Direction direction;

        public BlockPos getPos() {
            return pos;
        }

        public Direction getDirection() {
            return direction;
        }

        public AttackBlock(BlockPos pos, Direction direction) {
            this.pos = pos;
            this.direction = direction;
        }
    }

    public static class BreakBlock extends InteractEvent {

        protected BlockPos pos;

        public BlockPos getPos() {
            return pos;
        }

        public BreakBlock(BlockPos pos) {
            this.pos = pos;
        }
    }
}
