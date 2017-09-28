package com.tsg.xutil.util.nfc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;


import com.sinpo.xnfc.tech.Iso7816;
import com.tsg.xutil.base.BaseActivity;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.util.DensityUtil;
import com.tsg.xutil.util.KeyUtil;
import com.tsg.xutil.util.L;
import com.tsg.xutil.util.SPUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.smartcardio.CardChannel;

import pro.javacard.gp.GPNfcDevice;
import pro.javacard.gp.GPTool1;


public final class TsmCardReader {
    private static final String TAG = "LoyaltyCardReader";
    // AID for our loyalty card service.
    private static final String SAMPLE_CARD_AID = "A00000000902";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final byte[] recgnized = {(byte) 0x80, (byte) 0xCA, (byte) 0x9F, (byte) 0x7F, (byte) 0x00};//80CA9F7F00

    private static final String GET_MOUDULUS = "80010000";//获取modulus
    private static final String GET_EXPONENT = "80020000";//获取exponent

    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    public static String[][] TECHLISTS;
    public static IntentFilter[] FILTERS;

    static {
        try {
            TECHLISTS = new String[][]{{IsoDep.class.getName()}};
            FILTERS = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")};
        } catch (Exception e) {
        }
    }

    public static ProgressDialog loadingDialog;

    static public String tagDiscovered(Tag tag, Context context) {
        loadingDialog = showDialog(context, "正在查询，请勿移开卡片...");
        Log.e(TAG, "New tag discovered");
        String strResult = "";
        IsoDep isoDep = IsoDep.get(tag);
        Iso7816.StdTag tag1 = new Iso7816.StdTag(isoDep);
        if (isoDep != null) {
            try {
                // Connect to the remote NFC device
                isoDep.connect();
                byte[][] recgnizedResponse = sendCmd(isoDep, HexStringToByteArray("00A4040000"));//-----------
                if (!Arrays.equals(SELECT_OK_SW, recgnizedResponse[1])) {
                    loadingDialog.dismiss();
                    return "";
                }
                byte[][] recgnizeResponse = sendCmd(isoDep, recgnized);//识别卡
                if (!Arrays.equals(SELECT_OK_SW, recgnizeResponse[1])) {
                    loadingDialog.dismiss();
                    return "";
                } else {
                    strResult = ByteArrayToHexString(recgnizeResponse[0]);
                    strResult = strResult.substring(30, 38);
                    L.e("*********************************" + strResult);
                    try {
                        strResult = KeyUtil.encryptWithAes(KeyUtil.AESKEY, strResult);//存储序列号
                    } catch (Exception e) {
                        loadingDialog.dismiss();
                        return "";
                    }
                }
                if (isoDep.isConnected()) {
                    isoDep.close();
                }
                tag1.connect();
                CardChannel cardChannel = new GPNfcDevice(tag1);
                String capPath = context.getFilesDir().getPath() + "/turingcard.cap";
                String[] argv = new String[]{"-load", capPath, "-d"};
                GPTool1.main(argv, cardChannel);
                argv = new String[]{"-package", "A00000000901", "-applet", "A00000000902", "-create", "A00000000902", "-param", "C900EF03CF0180", "-d"};
                GPTool1.main(argv, cardChannel);
                tag1.close();
                return strResult;
            } catch (IOException e) {
                Log.e(TAG, "Error communicating with card: " + e.toString());
                loadingDialog.dismiss();
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                loadingDialog.dismiss();
                return "";
            }
        }
        loadingDialog.dismiss();
        return "";
    }

    private static byte[][] getResponse(byte[] b) {
        byte[][] result = new byte[2][];
        int length = b.length;
        byte[] status = {b[length - 2], b[length - 1]};
        byte[] body = Arrays.copyOf(b, length - 2);

        result[0] = body;
        result[1] = status;
        return result;
    }

    public static String load(Parcelable parcelable, Context context) {
        final Tag tag = (Tag) parcelable;
        return tagDiscovered(tag, context);
    }

    /**
     * 确定16进制的长度为2的倍数
     *
     * @param hexStr
     * @return
     */
    private static String getHexDouble(String hexStr) {
        if (hexStr.length() % 2 == 0) {
            return hexStr;
        } else {
            return "0" + hexStr;
        }
    }

    /**
     * 转换成两个字节的长度
     * （0-65535）
     *
     * @param num
     * @return
     */
    public static String twoBytes(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "0";
        }
        String x = Integer.toHexString(Integer.parseInt(num));
        switch (x.length()) {
            case 1: {
                return "000" + x;
            }
            case 2: {
                return "00" + x;
            }
            case 3: {
                return "0" + x;
            }
            case 4: {
                return x;
            }
        }
        return "0";
    }

    /**
     * 向卡内发送命令
     *
     * @param isoDep
     * @param cmdSelect
     * @return
     * @throws IOException
     */
    public static byte[][] sendCmd(IsoDep isoDep, byte[] cmdSelect) throws IOException {
        Log.e(TAG, "Sending: " + ByteArrayToHexString(cmdSelect));
        byte[] result = isoDep.transceive(cmdSelect);
        Log.e(TAG, "Receive: " + ByteArrayToHexString(result));
        byte[][] response = getResponse(result);
        byte[] body = response[0];
        byte[] status = response[1];
        return response;
    }

    public static byte[] BuildSelectApdu(String aid) {
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * 将byte[]转成16进制字串
     *
     * @param bytes
     * @return
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 获取公钥
     *
     * @param modulus
     * @param exponent
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus, 16);
            BigInteger b2 = new BigInteger(exponent, 16);
            System.out.println(b1);
            System.out.println(b2);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将16进制字串转成byte[]
     *
     * @param s
     * @return
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * ProgressDialog
     */
    public static ProgressDialog showDialog(Context context, String messageBody) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage(messageBody);
        pd.getWindow().setLayout(DensityUtil.dp2px(context, 30), DensityUtil.dp2px(context, 30));
        pd.show();
        return pd;
    }
}
