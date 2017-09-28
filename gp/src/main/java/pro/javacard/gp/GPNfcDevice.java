package pro.javacard.gp;

import com.sinpo.xnfc.tech.Iso7816;

import java.nio.ByteBuffer;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

/**
 *
 * 通过nfc将指令发送指android设备上
 *
 * 覆盖javax.smartcardio.CardCardChanel里的transmit方法，
 * 在不改变大量代码的情况下实现com.sinpo.xnfc向pro.javacard.gp的对接
 */

public class GPNfcDevice extends CardChannel {

    public Iso7816.StdTag tag = null;

    @Override
    public Card getCard() {
        return null;
    }

    public GPNfcDevice() {
    }

    public GPNfcDevice(Iso7816.StdTag tag) {
        this.tag = tag ;
    }

    @Override
    public int getChannelNumber() {
        return 0;
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU commandAPDU){
        try {
            return new ResponseAPDU(tag.transceive(commandAPDU.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int transmit(ByteBuffer byteBuffer, ByteBuffer byteBuffer1) throws CardException {
        return 0;
    }

    @Override
    public void close() throws CardException {

    }

}
