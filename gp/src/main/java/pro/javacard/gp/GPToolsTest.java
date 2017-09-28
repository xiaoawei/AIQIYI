package pro.javacard.gp;

/**
 * Created by Administrator on 2017/4/5.
 */

public class GPToolsTest {
        public static void main(String args[]) {
            /*try {

                CardTerminals terminals = tf.terminals();

                // Select terminal(s) to work on
                List<CardTerminal> do_readers;
                if (System.getenv("GP_READER") != null) {
                    String reader = System.getenv("GP_READER");
                    CardTerminal t = terminals.getTerminal(reader);
                    if (t == null) {
                        fail("Reader \"" + reader + "\" not found.");
                    }
                    do_readers = Arrays.asList(t);
                } else {
                    do_readers = terminals.list(CardTerminals.State.CARD_PRESENT);
                }

                if (do_readers.size() == 0) {
                    fail("No smart card readers with a card found");
                }
                // Work all readers
                for (CardTerminal reader: do_readers) {
                    if (do_readers.size() > 1) {
                        System.out.println("# " + reader.getName());
                    }

                    Card card = reader.connect("*");
                    // GlobalPlatform specific
                    GlobalPlatform gp = new GlobalPlatform(card.getBasicChannel());
                    // --load <applet.cap>
                    File capfile = new File("C:\\pboc3_A000000333FE00000000000666600000.cap");

                    CapFile loadcap = new CapFile(new FileInputStream(capfile));
                    gp.loadCapFile(loadcap);
                }
            } catch (GPException e) {
                if (e.sw == 0x6985) {
                    System.err.println("Applet loading failed. Are you sure the CAP file target is compatible with your card?");
                } else {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
}
