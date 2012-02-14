import java.awt.Container;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Itemizer extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    JTextArea tA;
    JTextArea tA2;
    public boolean shouldClearInput = false;

    public static void main(String[] args) {
        Itemizer ft = new Itemizer();
        ft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ft.setSize(400, 300);
        ft.setVisible(true);
    }

    public Itemizer() {
        super();

        Animation clearer = new Animation(this);
        clearer.start();

        tA = new JTextArea("", 15, 10);
        JScrollPane scrollingArea = new JScrollPane(tA);

        tA2 = new JTextArea("", 15, 10);
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        // JButton changeBtn = new JButton("Change");
        // pane.add(changeBtn);
        pane.add(scrollingArea);
        pane.add(tA2);

        tA.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // warn();
            }

            public void removeUpdate(DocumentEvent e) {
                // warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                Pattern pattern = Pattern.compile("\\\\item");
                String content = tA.getText();
                Matcher matcher = pattern.matcher(content);
                if (!matcher.find()) {
                    String outContent = "";
                    String[] splitcontent = content.split("\\r?\\n");
                    for (String s : splitcontent) {
                        if (!s.trim().equals("")){
                            String thisLine = s.trim();
                            thisLine = thisLine.replaceAll( "—", "--" );
                            thisLine = thisLine.replaceAll( "–", "--" );
                            thisLine = thisLine.replaceAll( "’", "'" );
                            thisLine = thisLine.replaceAll( "“", "``" );
                            thisLine = thisLine.replaceAll( "”", "''" );
                            outContent += "\\item " + thisLine + "\n";
                        }
                    }
                    outContent.trim();
                    // System.out.println(outContent);
                    tA2.setText(outContent);

                    tA2.selectAll();
                    tA2.copy();

                    tA.requestFocus();
                    // tA.selectAll();
                    shouldClearInput = true;
                    tA.setText("");
                }
            }
        });
    }
}

class Animation extends Thread {
    Itemizer itemizer;

    public Animation(Itemizer itemizer) {
        this.itemizer = itemizer;
    }

    public void run() {
        while (true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (this.itemizer.shouldClearInput) {
                this.itemizer.tA.setText("");
                this.itemizer.shouldClearInput = false;
            }
        }
    }
}