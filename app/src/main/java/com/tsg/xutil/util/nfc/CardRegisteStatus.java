package com.tsg.xutil.util.nfc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Parcelable;
import android.util.Log;

import com.tsg.xutil.util.DensityUtil;
import com.tsg.xutil.util.L;

import java.io.IOException;
import java.util.Arrays;

public final class CardRegisteStatus {
    private static final String TAG = "LoyaltyCardReader";
    private static final String SAMPLE_CARD_AID = "A00000000902";
    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final byte[] recgnized = {(byte) 0x80, (byte) 0xCA, (byte) 0x9F, (byte) 0x7F, (byte) 0x00};//80CA9F7F00
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
        if (isoDep != null) {
            try {
                if (isoDep.isConnected()) {
                    isoDep.close();
                }
                isoDep.connect();
                byte[][] recgnizedResponse = sendCmd(isoDep, HexStringToByteArray("00A4040000"));
                if (!Arrays.equals(SELECT_OK_SW, recgnizedResponse[1])) {
                    loadingDialog.dismiss();
                    return "";
                }
                byte[][] recgnizeResponse = sendCmd(isoDep, recgnized);//识别卡
                if (!Arrays.equals(SELECT_OK_SW, recgnizeResponse[1])) {
                    loadingDialog.dismiss();
                    return "No";
                } else {
                    strResult = ByteArrayToHexString(recgnizeResponse[0]);
                    strResult = strResult.substring(30, 38);
                    L.e("*********************************" + strResult);
                }
                return strResult;
            } catch (IOException e) {
                Log.e(TAG, "Error communicating with card: " + e.toString());
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
     * 向卡内发送命令
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
     * 将16进制字串转成byte[]
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
