package pro.javacard.gp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.common.collect.Lists;
import com.sinpo.xnfc.reader.ReaderManager;
import com.sinpo.xnfc.tech.Iso7816;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERTags;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;

import apdu4j.HexUtils;
import apdu4j.ISO7816;

import static pro.javacard.gp.GlobalPlatform.CLA_GP;
import static pro.javacard.gp.GlobalPlatform.CLA_MAC;
import static pro.javacard.gp.GlobalPlatform.INS_INSTALL;
import static pro.javacard.gp.GlobalPlatform.INS_LOAD;
import static pro.javacard.gp.GlobalPlatform.SCP_01_05;
import static pro.javacard.gp.GlobalPlatform.SCP_02_15;
import static pro.javacard.gp.GlobalPlatform.SCP_ANY;
import static pro.javacard.gp.GlobalPlatform.SHORT_0;

/**
 * NFC传送GP规范命令类
 * 在手机上通过NFC将符合GlobalPlaform规范的命令传送智能卡上，大体代码从GlobalPlatform类复制而来
 * Created by Administrator on 2017/4/8.
 */

public class NfcGP {
    private Handler handler = null;

    private SessionKeyProvider keys = null;
    private byte[] diversification_data = null;
    private int scpMajorVersion = 0;     // Either 1 or 2 or 3
    private GPKeySet sessionKeys;
    private GlobalPlatform.SCPWrapper wrapper = null;
    private Iso7816.StdTag tag = null;
    private CapFile cap = null;
    private int blockSize = 255;
    private EnumSet<GlobalPlatform.APDUMode> securityLevel;
    private byte[] host_cryptogram ;

    /**
     * 构造方法，用于接收Activty传过来的Handler对象刷新UI
     * @param handler 发送消息的handler对象
     */
    public NfcGP(Handler handler) {
        this.handler = handler;
    }

    /**
     * 一个空参数的构造方法，一般用于测试使用
     */
    public NfcGP() {
    }

    /**
     * 用接收到的handler对象刷新主线程UI，展示进度或者提示信息等。
     * @param msg 消息内容
     */
    public void showProcess(String msg) {
        Message message = new Message();
        message.what = 1;
        Bundle b = new Bundle();//存放数据
        b.putString("msg", msg);
        message.setData(b);
        handler.sendMessage(message);
    }

    /**
     * 解释响应的内容返回安全域AID
     * @param fci
     * @param sdAID 安全域AID
     * @return AID
     * @throws GPException
     */
    private AID parseSelectResponse(byte [] fci, AID sdAID) throws GPException {
        try (ASN1InputStream ais = new ASN1InputStream(fci)) {
            if (ais.available() > 0) {
                // Read FCI
                DERApplicationSpecific fcidata = (DERApplicationSpecific) ais.readObject();
                // FIXME System.out.println(ASN1Dump.dumpAsString(fcidata, true));
                if (fcidata.getApplicationTag() == 15) {
                    ASN1Sequence s = ASN1Sequence.getInstance(fcidata.getObject(BERTags.SEQUENCE));
                    for (ASN1Encodable e: Lists.newArrayList(s.iterator())) {
                        ASN1TaggedObject t = DERTaggedObject.getInstance(e);
                        if (t.getTagNo() == 4) {
                            // ISD AID
                            ASN1OctetString isdaid = DEROctetString.getInstance(t.getObject());
                            AID detectedAID = new AID(isdaid.getOctets());
                            if (sdAID == null) {
                                //logger.debug("Auto-detected ISD AID: " + detectedAID);
                            }
                            if (sdAID != null && !detectedAID.equals(sdAID)) {
                                //giveStrictWarning("SD AID in FCI does not match the requested AID!");
                            }
                            sdAID = sdAID == null ? detectedAID : sdAID;
                        } else if (t.getTagNo() == 5) {
                            // Proprietary, usually a sequence
                            if (t.getObject() instanceof ASN1Sequence) {
                                ASN1Sequence prop = ASN1Sequence.getInstance(t.getObject());
                                for (ASN1Encodable enc: Lists.newArrayList(prop.iterator())) {
                                    ASN1Primitive proptag = enc.toASN1Primitive();
                                    if (proptag instanceof DERApplicationSpecific) {
                                        DERApplicationSpecific isddata = (DERApplicationSpecific) proptag;
                                        if (isddata.getApplicationTag() == 19) {
                                            //spec = GPData.get_version_from_card_data(isddata.getEncoded());
                                            //logger.debug("Auto-detected GP version: " + spec);
                                        }
                                    } else if (proptag instanceof DERTaggedObject) {
                                        DERTaggedObject tag = (DERTaggedObject)proptag;
                                        if (tag.getTagNo() == 101) {
                                            //setBlockSize(DEROctetString.getInstance(tag.getObject()));
                                        } else if (tag.getTagNo() == 110) {
                                            //logger.debug("Lifecycle data (ignored): " + HexUtils.bin2hex(tag.getObject().getEncoded()));
                                        } else {
                                            //logger.info("Unknown/unhandled tag in FCI proprietary data: " + HexUtils.bin2hex(tag.getEncoded()));
                                        }
                                    } else {
                                        throw new GPException("Unknown data from card: " + HexUtils.bin2hex(proptag.getEncoded()));
                                    }
                                }
                            } else {
                                // Except Feitian cards which have a plain nested tag
                                if (t.getObject() instanceof DERTaggedObject) {
                                    DERTaggedObject tag = (DERTaggedObject)t.getObject();
                                    if (tag.getTagNo() == 101) {
                                        //setBlockSize(DEROctetString.getInstance(tag.getObject()));
                                    } else {
                                        //logger.info("Unknown/unhandled tag in FCI proprietary data: " + HexUtils.bin2hex(tag.getEncoded()));
                                    }
                                }
                            }
                        } else {
                        }
                    }
                } else {
                    throw new GPException("Unknown data from card: " + HexUtils.bin2hex(fci));
                }
            }
        } catch (IOException | ClassCastException e) {
            throw new GPException("Invalid data: " + e.getMessage(), e);
        }
        return sdAID;
    }


    /**
     * 在Load cap完成后发送installAndMakeSelectable的指令
     * @param packageAID  包名AID
     * @param appletAID  应用AID
     * @param instanceAID  实例AID
     * @param privileges  权限
     * @param installParams  安装参数
     * @param installToken  安装Token
     * @throws GPException  抛出GP异常
     * @throws CardException  抛出CardException异常
     */
    public void installAndMakeSelectable(AID packageAID, AID appletAID, AID instanceAID, GPRegistryEntry.Privileges privileges, byte[] installParams, byte[] installToken) throws GPException, CardException, IOException {

        if (instanceAID == null) {
            instanceAID = appletAID;
        }
        if (installParams == null) {
            installParams = new byte[] { (byte) 0xC9, 0x00 };
        }
        if (installToken == null) {
            installToken = new byte[0];
        }
        byte [] privs = privileges.toBytes();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            bo.write(packageAID.getLength());
            bo.write(packageAID.getBytes());

            bo.write(appletAID.getLength());
            bo.write(appletAID.getBytes());

            bo.write(instanceAID.getLength());
            bo.write(instanceAID.getBytes());

            bo.write(privs.length);
            bo.write(privs);

            bo.write(installParams.length);
            bo.write(installParams);

            bo.write(installToken.length);
            bo.write(installToken);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        CommandAPDU install = new CommandAPDU(CLA_GP, INS_INSTALL, 0x0C, 0x00, bo.toByteArray());
        Iso7816.Response response = tag.installForFinal(bo.toByteArray(), GlobalPlatform.getWrapperIV(wrapper,install));
        //GPException.check(response, "install and make selectable failed");
    }

    /**
     * 发送和解析initializeUpdate命令
     * @param host_challenge
     * @throws GPException
     * @throws IOException
     */
    public void initializeUpdateAndParse(byte[] host_challenge) throws GPException, IOException {
        Iso7816.Response response = tag.initializeUpdate(host_challenge);
        GPException.check(response,"initialize update and parse failed");
        byte[] update_response = response.getData();
        //3.复制GPTool的代码
        // Parse the response
        int offset = 0;
        diversification_data = Arrays.copyOfRange(update_response, 0, 10);
        offset += diversification_data.length;
        // Get used key version from response
        int keyVersion = update_response[offset] & 0xFF;
        offset++;
        // Get major SCP version from Key Information field in response
        scpMajorVersion = update_response[offset];
        offset++;

        // get the protocol "i" parameter, if SCP03
        int scp_i = -1;
        if (scpMajorVersion == 3) {
            scp_i = update_response[offset];
            offset++;
        }

        // FIXME: SCP02 has 2 byte sequence + 6 bytes card challenge but the challenge is discarded.
        // get card challenge
        byte card_challenge[] = Arrays.copyOfRange(update_response, offset, offset + 8);
        offset += card_challenge.length;
        // get card cryptogram
        byte card_cryptogram[] = Arrays.copyOfRange(update_response, offset, offset + 8);
        offset += card_cryptogram.length;

        // Verify response
        // If using explicit key version, it must match.
        if ((keys.getKeysetVersion() > 0) && (keyVersion != keys.getKeysetVersion())) {
            throw new GPException("Key version mismatch: " + keys.getKeysetVersion() + " != " + keyVersion);
        }

        int scpVersion = 0;
        // Set default SCP version based on major version, if not explicitly known.
        if (scpVersion == SCP_ANY) {
            if (scpMajorVersion == 1) {
                scpVersion = SCP_01_05;
            } else if (scpMajorVersion == 2) {
                scpVersion = SCP_02_15;
            } else if (scpMajorVersion == 3) {
                scpVersion = 3; // FIXME: the symbolic numbering of versions needs to be fixed.
            }
        } else if (scpVersion != scpMajorVersion) {
            scpMajorVersion = scpVersion;
            if (scpVersion == 1) {
                scpVersion = SCP_01_05;
            } else if (scpVersion == 2) { //
                scpVersion = SCP_02_15;
            } else {
            }
        }

        // Remove RMAC if SCP01 TODO: this should be generic sanitizer somewhere
        if (scpMajorVersion == 1 && securityLevel.contains(GlobalPlatform.APDUMode.RMAC)) {
            securityLevel.remove(GlobalPlatform.APDUMode.RMAC);
        }

        // Derive session keys
        byte [] seq = null;
        if (scpMajorVersion == 1) {
            sessionKeys = keys.getSessionKeys(scpMajorVersion, diversification_data, host_challenge, card_challenge);
        } else if (scpMajorVersion == 2) {
            seq = Arrays.copyOfRange(update_response, 12, 14);
            sessionKeys = keys.getSessionKeys(2, diversification_data, seq); //
        } else if (scpMajorVersion == 3) {
            if (update_response.length == 32) {
                seq = Arrays.copyOfRange(update_response, 29, 32);
            }
            sessionKeys = keys.getSessionKeys(3, diversification_data, host_challenge, card_challenge);
        } else {
            throw new GPException("Don't know how to handle SCP version " + scpMajorVersion);
        }

        // Verify card cryptogram
        byte[] my_card_cryptogram = null;
        byte[] cntx = GPUtils.concatenate(host_challenge, card_challenge);
        if (scpMajorVersion == 1 || scpMajorVersion == 2) {
            my_card_cryptogram = GPCrypto.mac_3des_nulliv(sessionKeys.getKey(GPData.KeyType.ENC), cntx); //
        } else {
            my_card_cryptogram = GPCrypto.scp03_kdf(sessionKeys.getKey(GPData.KeyType.MAC), (byte) 0x00, cntx, 64);
        }

        // Calculate host cryptogram and initialize SCP wrapper
        if (scpMajorVersion == 1 || scpMajorVersion == 2) {//
            host_cryptogram = GPCrypto.mac_3des_nulliv(sessionKeys.getKey(GPData.KeyType.ENC), GPUtils.concatenate(card_challenge, host_challenge));
            wrapper = new GlobalPlatform.SCP0102Wrapper(sessionKeys, scpVersion, EnumSet.of(GlobalPlatform.APDUMode.MAC), null, null, blockSize);
        } else {
            host_cryptogram = GPCrypto.scp03_kdf(sessionKeys.getKey(GPData.KeyType.MAC), (byte) 0x01, cntx, 64);
            wrapper = new GlobalPlatform.SCP03Wrapper(sessionKeys, scpVersion, EnumSet.of(GlobalPlatform.APDUMode.MAC), null, null, blockSize);
        }

    }

    /**
     * 安装.cap文件到智能卡上
     * @param capPath  cap文件路径
     * @param installParamStr cap文件的安装参数
     * @param instanceAids
     * @throws Exception 抛出异常
     */
    public void loadAndInstall(String capPath, String installParamStr, String[] instanceAids) throws Exception{

        Date date = new Date();
        long startTime = date.getTime();
        Log.i("NfcGP","开始时间:" + date.toLocaleString());


        //1.准备相关参数
        securityLevel = GlobalPlatform.defaultMode.clone();
        GPKeySet.Diversification div = GPKeySet.Diversification.NONE;
        keys = PlaintextKeys.fromMasterKey(GPData.defaultKey, div);  //主密钥
        byte[] host_challenge  = new byte[8];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(host_challenge);
        //GlobalPlatform gp = new GlobalPlatform(new CardChannel());

        //2.选择SD(安全域)
        tag = new Iso7816.StdTag(ReaderManager.mIsodep);
        if(tag == null) {
            showProcess("请将卡片贴紧手机");
            return;
        }
        tag.connect();
        Iso7816.Response response = tag.selectSD();
        GPException.check(response,"select sd failed");
        byte[] fci = response.getData();
        AID sdAid = parseSelectResponse(fci,null);

        //3.打开安全通道，其中包含发送和解析INITIALIZE UPDATE命令
        initializeUpdateAndParse(host_challenge);

        int P1 = GlobalPlatform.APDUMode.getSetValue(securityLevel);
        CommandAPDU externalAuthenticate = new CommandAPDU(CLA_MAC, ISO7816.INS_EXTERNAL_AUTHENTICATE_82, P1, 0, host_cryptogram);
        response = tag.externalAuthentication(host_cryptogram, GlobalPlatform.getWrapperIV(wrapper,externalAuthenticate));
        GPException.check(response,"external authentication failed");
        wrapper.setSecurityLevel(securityLevel);

        // FIXME: ugly stuff, ugly...
        if (scpMajorVersion != 3) {
            GlobalPlatform.SCP0102Wrapper w = (GlobalPlatform.SCP0102Wrapper) wrapper;
            if (securityLevel.contains(GlobalPlatform.APDUMode.RMAC)) {
                w.setRMACIV(w.getIV());
            }
        }

        //3.发送安装指令
        cap = new CapFile(new FileInputStream(capPath));
        byte[] hash = false ? cap.getLoadFileDataHash("SHA1", false) : new byte[0];
        int len = cap.getCodeLength(false);
        // FIXME: parameters are optional for load
        byte[] loadParams = false ? new byte[] { (byte) 0xEF, 0x04, (byte) 0xC6, 0x02, (byte) ((len & 0xFF00) >> 8),
                (byte) (len & 0xFF) } : new byte[0];

        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        try {
            bo.write(cap.getPackageAID().getLength());
            bo.write(cap.getPackageAID().getBytes());

            bo.write(sdAid.getLength());
            bo.write(sdAid.getBytes());

            bo.write(hash.length);
            bo.write(hash);

            bo.write(loadParams.length);
            bo.write(loadParams);
            bo.write(0);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        //4.分批次load cap文件到智能卡
        CommandAPDU installForLoad = new CommandAPDU(GlobalPlatform.CLA_GP, GlobalPlatform.INS_INSTALL, 0x02, 0x00, bo.toByteArray());
        response = tag.install(bo.toByteArray(), GlobalPlatform.getWrapperIV(wrapper,installForLoad));
        GPException.check(response, "Install for Load failed");

        System.out.println(response.getSw1() + " " + response.getSw2());
        showProcess(response.getSw1() + " " + response.getSw2()+" installForLoad完成");


        Log.i("NfcGP","分批load时间前:" + new Date().toLocaleString());


        List<byte[]> blocks = cap.getLoadBlocks(false, false, wrapper.getBlockSize());
        for (int i = 0; i < blocks.size(); i++) {
            CommandAPDU load = new CommandAPDU(0x84, INS_LOAD, (i == (blocks.size() - 1)) ? 0x80 : 0x00, (byte) i, blocks.get(i));
            //response = tag.loadByPart((i == (blocks.size() - 1)) ? (byte)0x80 : (byte)0x00,(byte) i, blocks.get(i),GlobalPlatform.getWrapperIV(wrapper,load));
            response = new Iso7816.Response(tag.transceive(GlobalPlatform.getSendBytes(wrapper,load)));
            //GPException.check(response, "load by part failed");

            System.out.println( i + "  " + response.getSWStr());
            //showProcess(response.getSw1() + " " + response.getSw2()+" Load"+i+"完成");
            //Thread.sleep(2000);
        }

        byte [] installParam = HexUtils.hex2bin(installParamStr);
        // Simple use: only application parameters without tag, prepend 0xC9
        if (installParam[0] != (byte) 0xC9) {
            byte [] newparams = new byte[installParam.length + 2];
            newparams[0] = (byte) 0xC9;
            newparams[1] = (byte) installParam.length;
            System.arraycopy(installParam, 0, newparams, 2, installParam.length);
            installParam = newparams;
        }
        GPRegistryEntry.Privileges privs = new GPRegistryEntry.Privileges();


        Log.i("NfcGP","最后安装时间前:" + new Date().toLocaleString());


        //5.执行安装最后一步，创建相应实例 installAndMakeSelectable
        for(int i = 0; i < instanceAids.length; i++) {
            //Thread.sleep(2000);
            installAndMakeSelectable(cap.getPackageAID(),cap.getAppletAIDs().get(i), new AID(instanceAids[i]), privs, installParam, null);
            showProcess("安装第"+i+"个应用完成");
            //Thread.sleep(2000);
        }
        tag.close();

        Date dateEnd = new Date();
        long endTime = dateEnd.getTime();
        Log.i("NfcGP","总共时间:"+((endTime-startTime)/1000.0f));

    }

    public void personalizationCard() throws Exception {
        //1.准备相关参数
        securityLevel = GlobalPlatform.defaultMode.clone();
        GPKeySet.Diversification div = GPKeySet.Diversification.NONE;
        keys = PlaintextKeys.fromMasterKey(GPData.defaultKey, div);  //主密钥
        byte[] host_challenge  = new byte[8];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(host_challenge);
        //GlobalPlatform gp = new GlobalPlatform(new CardChannel());

        //2.选择SD(安全域)
        tag = new Iso7816.StdTag(ReaderManager.mIsodep);
        if(tag == null) {
            showProcess("请将卡片贴紧手机");
            return;
        }
        tag.connect();
        Iso7816.Response response = tag.selectAID(HexUtils.hex2bin("A0000000035350410000000000000002"));
        GPException.check(response,"select sd failed");
        byte[] fci = response.getData();
        AID sdAid = parseSelectResponse(fci,null);

        //3.打开安全通道，其中包含发送和解析INITIALIZE UPDATE命令
        initializeUpdateAndParse(host_challenge);

        //4.通过GET DATA拿到 Tag '42' 发行者标识号  Tag '45'卡Image号 Tag'66'卡数据 Tag 'E0'密钥信息模板 Tag'C1'缺省密钥版本号的序列计数值
        getDate();
    }

    //4.通过GET DATA拿到 Tag '42' 发行者标识号  Tag '45'卡Image号 Tag'66'卡数据 Tag 'E0'密钥信息模板 Tag'C1'缺省密钥版本号的序列计数值
    public void getDate() throws Exception{
        Log.i("NfcGP","***** GET DATA:");

        // Issuer Identification Number (IIN)
        CommandAPDU dataCmd = new CommandAPDU(CLA_GP, ISO7816.INS_GET_DATA, 0x00, 0x42, 256);
        Iso7816.Response resp = new Iso7816.Response(tag.transceive(GlobalPlatform.getSendBytes(wrapper,dataCmd)));
        if (resp.getSW() == 0x9000) {
            Log.i("NfcGP","IIN " + HexUtils.bin2hex(resp.getData()));
        } else {
            Log.i("NfcGP","GET DATA(IIN) not supported");
        }

        // Card Image Number (CIN)
        dataCmd = new CommandAPDU(CLA_GP, ISO7816.INS_GET_DATA, 0x00, 0x45, 256);
        resp = new Iso7816.Response(tag.transceive(GlobalPlatform.getSendBytes(wrapper,dataCmd)));
        if (resp.getSW() == 0x9000) {
            Log.i("NfcGP","CIN " + HexUtils.bin2hex(resp.getData()));
        } else {
            Log.i("NfcGP","GET DATA(CIN) not supported");
        }

        // Sequence Counter of the default Key Version Number (tag 0xC1)
        dataCmd = new CommandAPDU(CLA_GP, ISO7816.INS_GET_DATA, 0x00, 0xC1, 256);
        resp = new Iso7816.Response(tag.transceive(GlobalPlatform.getSendBytes(wrapper,dataCmd)));
        if (resp.getSW() == 0x9000) {
            byte [] ssc = resp.getData();
            Log.i("NfcGP","SSC " + HexUtils.bin2hex(TLVUtils.getTLVValueAsBytes(ssc, SHORT_0)));
        } else {
            Log.i("NfcGP","GET DATA(SSC) not supported");
        }
        Log.i("NfcGP","*****");
    }

}
