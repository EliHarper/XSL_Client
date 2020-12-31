package msg;

import java.lang.String;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuditMessage {
    String contents;

    public AuditMessage (String contents) {
        this.contents = contents;
    }

    public AuditMessage () {
        this ("");
    }

    public String getContents() {
        return contents;
    }


    public void setContents(String contents) {
        this.contents = contents;
    }
}
