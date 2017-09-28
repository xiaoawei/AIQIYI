/*
package pro.javacard.gp;

import com.dk.bleNfc.card.CpuCard;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

*/
/**
 * 通过蓝牙将指令发送指Nfc设备上
 * <p>
 * 有两个问题要注意的是：
 * 1.cpuCard的transceive发送命令要放在子线程执行
 * 2.每发一条指令都要等上一条指令回复后才能继续发送
 *//*


public class GPBleDevice extends CardChannel {

    private CpuCard card;

    public GPBleDevice(CpuCard cpuCard) {
        this.card = cpuCard;
    }

    @Override
    public Card getCard() {
        return null;
    }

    public GPBleDevice() {
    }

    @Override
    public int getChannelNumber() {
        return 0;
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU commandAPDU) {
        ExecutorService pool = null;
        try {
            pool = Executors.newFixedThreadPool(1);  //创建一个线程池
            SendCal sendCal = new SendCal(commandAPDU.getBytes());   //创建带有返回值的任务
            Future f1 = pool.submit(sendCal);  //执行任务并获取Future对象
            byte[] data = (byte[]) f1.get();   //从Future对象上获取任务的返回值
            return new ResponseAPDU(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程池
            if(pool != null) {
                pool.shutdown();
            }
        }
        return null;
    }


    class SendCal implements Callable<byte[]> {
        private byte[] cmd;

        public SendCal(byte[] cmd) {
            this.cmd = cmd;
        }

        @Override
        public byte[] call() throws Exception {
            Thread.sleep(2000);
            System.out.println("当前线程：ID="+Thread.currentThread().getId() +" name=" + Thread.currentThread().getName());
            byte[] data = card.transceive(cmd);
            return data;
        }
    }


    @Override
    public int transmit(ByteBuffer byteBuffer, ByteBuffer byteBuffer1) throws CardException {
        return 0;
    }

    @Override
    public void close() throws CardException {

    }

}
*/
