package cn.timer.ultra.module.modules.cheat

import cn.timer.ultra.event.Event
import cn.timer.ultra.event.EventTarget
import cn.timer.ultra.module.Category
import cn.timer.ultra.module.CombatModule
import cn.timer.ultra.utils.fdp.MSTimer
import cn.timer.ultra.utils.fdp.PacketUtils
import cn.timer.ultra.values.Booleans
import cn.timer.ultra.values.Numbers
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import org.lwjgl.input.Keyboard
import java.util.*


//good work, but skidded by yuxiangll
class FakeLag : CombatModule("FakeLag", Keyboard.KEY_NONE, Category.Cheat) {
    private val fakeLagPosValue = Booleans("FakeLagPosition", true)
    private val fakeLagBlockValue = Booleans("FakeLagBlock", true)
    private val fakeLagAttackValue = Booleans("FakeLagAttack", true)
    private val fakeLagSpoofValue = Booleans("FakeLagC03Spoof", false)
    private val c0FPacketValue = Booleans("C0FPacket", false)
    private val c00PacketValue = Booleans("C00Packet", false)
    private val lagDelayValue = Numbers("LagDelay", 0, 5000, 10)
    private val lagDurationValue = Numbers("LagDuration", 0, 1000, 10)


    private var verus2Stat = false
    private val packetBuffer = LinkedList<Packet<INetHandlerPlayServer>>()
    private var currentTrans = 0
    private var memeTick = 0
    private var vulTickCounterUID = -25767
    private val fakeLagDelay = MSTimer()
    private val fakeLagDuration = MSTimer()
    private var isSent = false
    private val C0FS = LinkedList<C0FPacketConfirmTransaction>()
    private var counter = 0
    private var x = 0.0
    private var y = 0.0
    private var z = 0.0
    private var modified = false

    //var nameMakerFakelag = "FakeLag" + " "+ "["+ fakeLagDelay.toString()+"]"+"[" + fakeLagDuration.toString()+"]"
    init {
        addValues(
            fakeLagPosValue,
            fakeLagBlockValue,
            fakeLagAttackValue,
            fakeLagSpoofValue,
            c0FPacketValue,
            c00PacketValue,
            lagDelayValue,
            lagDurationValue
        )
    }

    private fun reset() {
        memeTick = 0
        currentTrans = 0
        verus2Stat = false
        packetBuffer.clear()
        fakeLagDelay.reset()
        fakeLagDuration.reset()
        counter = 0
        vulTickCounterUID = -25767
    }

    override fun onEnable() {
        C0FS.clear()
        reset()
        //BlinkUtils.clearPacket()
        //BlinkUtils.setBlinkState(all = true)
        //fakeLagDuration.reset()
        //fakeLagDelay.reset()
    }

    override fun onDisable() {
        C0FS.clear()
        modified = false
        for (packet in packetBuffer) {
            PacketUtils.sendPacketNoEvent(packet)
        }
        packetBuffer.clear()
        //BlinkUtils.setBlinkState(
        //    packetTransaction = c0FPacketValue.getValue() || BlinkUtils.transactionStat,
        //    packetKeepAlive = c00PacketValue.getValue() || BlinkUtils.keepAliveStat
        //)
        //BlinkUtils.releasePacket(onlySelected = true)
        //BlinkUtils.setBlinkState(off = true)
    }

    /*
        @EventTarget
        fun onWorld(event: WorldEvent) {
            BlinkUtils.clearPacket()
            BlinkUtils.setBlinkState(off = true)
            fakeLagDuration.reset()
            fakeLagDelay.reset()
        }


     */
    @EventTarget
    fun onWorld(event: WorldEvent) {
        reset()
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (fakeLagDelay.hasTimePassed(lagDelayValue.getValue().toLong())) {
            if (isSent && fakeLagSpoofValue.getValue()) {
                PacketUtils.sendPacketNoEvent(C03PacketPlayer(true))
                if (lagDurationValue.getValue() >= 300) PacketUtils.sendPacketNoEvent(C03PacketPlayer(true))
                isSent = false
            }
            if (packet is C00PacketKeepAlive || packet is C0FPacketConfirmTransaction) {
                event.cancelEvent()
                packetBuffer.add(packet as Packet<INetHandlerPlayServer>)
            }
            if (fakeLagAttackValue.getValue() && (packet is C02PacketUseEntity || packet is C0APacketAnimation)) {
                event.cancelEvent()
                packetBuffer.add(packet as Packet<INetHandlerPlayServer>)
                if (packet is C0APacketAnimation) return
            }
            if (fakeLagBlockValue.getValue() && (packet is C07PacketPlayerDigging || packet is C08PacketPlayerBlockPlacement || packet is C0APacketAnimation)) {
                event.cancelEvent()
                packetBuffer.add(packet as Packet<INetHandlerPlayServer>)
            }
            if (fakeLagPosValue.getValue() && (packet is C03PacketPlayer || packet is C03PacketPlayer.C04PacketPlayerPosition || packet is C03PacketPlayer.C05PacketPlayerLook || packet is C03PacketPlayer.C06PacketPlayerPosLook || packet is C0BPacketEntityAction)) {
                event.cancelEvent()
                packetBuffer.add(packet as Packet<INetHandlerPlayServer>)
            }
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!fakeLagDelay.hasTimePassed(lagDelayValue.getValue().toLong())) fakeLagDuration.reset()
        // Send
        if (fakeLagDuration.hasTimePassed(lagDurationValue.getValue().toLong())) {
            fakeLagDelay.reset()
            fakeLagDuration.reset()
            for (packet in packetBuffer) {
                PacketUtils.sendPacketNoEvent(packet)
            }
            //debugMessage("Release buf(size=${packetBuffer.size})")
            isSent = true
            packetBuffer.clear()
        }
        //nameMakerFakelag = "FakeLag" + " "+ "["+ fakeLagDelay.toString()+"]"+"[" + fakeLagDuration.toString()+"]"
        /*
    if(LiquidBounce.moduleManager[Blink::class.java]!!.state) {
        fakeLagDelay.reset()
        fakeLagDuration.reset()
        return
    }
    */
        /*
    if (mc.thePlayer.isDead) {
        BlinkUtils.setBlinkState(packetTransaction = c0FPacketValue.getValue() || BlinkUtils.transactionStat, packetKeepAlive = c00PacketValue.getValue() || BlinkUtils.keepAliveStat)
        BlinkUtils.releasePacket(onlySelected = true)
        BlinkUtils.setBlinkState(off = true)
        return
    }
    if (fakeLagDuration.hasTimePassed(lagDurationValue.getValue().toLong())) {
        fakeLagDelay.reset()
        fakeLagDuration.reset()
        //disabler.debugMessage("Release buf(size=${BlinkUtils.bufferSize()})")
        BlinkUtils.setBlinkState(packetTransaction = c0FPacketValue.getValue() || BlinkUtils.transactionStat, packetKeepAlive = c00PacketValue.getValue() || BlinkUtils.keepAliveStat)
        BlinkUtils.releasePacket(onlySelected = true)
        BlinkUtils.setBlinkState(off = true)
    } else if (fakeLagDelay.hasTimePassed(lagDelayValue.getValue().toLong())) {
        BlinkUtils.setBlinkState(all = true)
    } else {
        fakeLagDuration.reset()
        BlinkUtils.setBlinkState(off = true)
    }
}


*/


    }

}

class WorldEvent(val worldClient: WorldClient?) : Event()
class UpdateEvent : Event()
class PacketEvent(val packet: Packet<*>, val type: Type) : CancellableEvent() {
    enum class Type {
        RECEIVE,
        SEND
    }

    fun isServerSide() = type == Type.RECEIVE
}

open class CancellableEvent {

    /**
     * Let you know if the event is cancelled
     *
     * @return state of cancel
     */
    var isCancelled: Boolean = false
        private set

    /**
     * Allows you to cancel a event
     */
    fun cancelEvent() {
        isCancelled = true
    }
}
